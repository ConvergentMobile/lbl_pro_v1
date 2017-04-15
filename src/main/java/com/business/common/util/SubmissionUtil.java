package com.business.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.common.dto.SearchDomainDTO;
import com.business.model.pojo.ExportReportEntity;
import com.business.model.pojo.RenewalReportEntity;
import com.business.service.BusinessService;
import com.business.web.bean.AcxiomBean;
import com.csvreader.CsvWriter;
import com.partners.acxiom.AcxiomClient;
import com.partners.factual.FactualClient;
import com.partners.infogroup.InfogroupClient;
import com.partners.neustar.NeustarLocalezeClient;

/**
 * 
 * @author Vasanth
 * 
 */

public class SubmissionUtil {

	Logger logger = Logger.getLogger(SubmissionUtil.class);

	static SubmissionUtil submissionUtil;
	BusinessService service;

	public void initializeService(BusinessService service) {
		this.service = service;
		submissionUtil = new SubmissionUtil();
	}

	/**
	 * doSubmissions
	 * 
	 * @param service
	 * @param resp
	 * @throws Exception
	 */
	public void doSubmissions(BusinessService service, String client) throws Exception {
		this.service = service;
		submissionUtil = new SubmissionUtil();
		logger.debug("Submissions Triggered for specific Client from UI ::"
				+ client);
		logger.debug("******** Start: submitting the data to Providers ***********");
		Map<String, List<BrandInfoDTO>> allActiveBrandsMap = null;
		if (client == null || client.equals("All Brands")) {
			allActiveBrandsMap = service.getAllActiveBrands();
		} else {
			allActiveBrandsMap = service.getActivePartners(client);
		}
		logger.debug("******** Total active brands found for submitting the data to Providers : "
				+ allActiveBrandsMap.size());
		Set<String> activeBrandNamesList = allActiveBrandsMap.keySet();

		for (String brandName : activeBrandNamesList) {
			List<LocalBusinessDTO> localBusinessDTOs = service
					.getListOfBussinessByBrandName(brandName);
			int numberOfRecords = localBusinessDTOs.size();
			if (numberOfRecords > 0) {
				List<BrandInfoDTO> brandInfoDTOs = allActiveBrandsMap
						.get(brandName);
				
				
				for (BrandInfoDTO brandInfoDTO : brandInfoDTOs) {
					int numberOfFailedRecords = 0;
					boolean isAPI=false;
					// call required APIs
					if (brandInfoDTO.getSubmisions() != null) {
						String partner = brandInfoDTO.getSubmisions();
						

						if (partner.equalsIgnoreCase(LBLConstants.LBL_ACXIOM)) {
							isAPI = true;
							logger.info("Writing the data to excel for: "
									+ partner + " for Brand: "
									+ brandInfoDTO.getBrandName());
							String path = LBLConstants.ACXIOM_FILE_PATH;
							try {
								Random rand = new Random();
								int randomNumber = rand.nextInt(1000) + 1;
								String brand = brandInfoDTO.getBrandName();
								path = path.concat("_" + brand + "_"
										+ randomNumber + ".csv");
								try {

									write(localBusinessDTOs, path);
									AcxiomClient.initializeAcxiomService();
									AcxiomClient.uploadAcxiomFile(path,
											brandInfoDTO.getClientId());
									logger.debug("Uploading the Data to Acxiom for Brand "
											+ brand + " is successful");
									updateRenewal(localBusinessDTOs, partner,
											brandInfoDTO.getBrandName());
									sendMail(null, brandName, partner);
								} catch (Exception e) {
									logger.error("There was a error while Uplaoding the Data to Acxiom for Brand "
											+ brand);
									String toMail = LBLConstants.NOTIFY_MAIL;
									numberOfRecords = 0;
									String subject = brandName
											+ " Submission Status to "
											+ partner + " is FAILED";
									MailUtil.sendMail(toMail, subject, subject);

									e.printStackTrace();
								}

							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								File file = new File(path);
								if (file.exists()) {
									logger.debug("deleting the file after submitting to acxiom: "
											+ path);
									file.delete();
								}
							}
						} else if (partner
								.equalsIgnoreCase(LBLConstants.LBL_FACTUAL)) {
							isAPI = true;
							logger.info("Sending info to " + partner
									+ ", and the total businesses are: "
									+ numberOfRecords);
							try{
								FactualClient factualClient = new FactualClient();
								Map<String, List<String>> errorDetails = factualClient
										.exportToFactual(localBusinessDTOs, service);

								List<String> list = errorDetails
										.get("errorDetails");
								sendMail(list, brandName, partner);
								numberOfFailedRecords = list.size();
								updateRenewal(localBusinessDTOs, partner, brandInfoDTO.getBrandName());	
							}catch(Exception e){
								logger.error("There was a issue while submitting data to Factual for Brand: "
										+ brandName);
							}
							

						} else if (partner
								.equalsIgnoreCase(LBLConstants.LBL_INFOGROUP)) {
							isAPI = true;
							logger.info("Sending info to " + partner
									+ ", and the total businesses are: "
									+ numberOfRecords);
							try {
								Map<String, List<String>> errorDetails = InfogroupClient
										.postToInfogroup(localBusinessDTOs,service);

								List<String> list = errorDetails
										.get("errorDetails");

								sendMail(list, brandName, partner);
								updateRenewal(localBusinessDTOs, partner,
										brandInfoDTO.getBrandName());
								numberOfFailedRecords = list.size();

							} catch(Exception e){
								logger.error("There was a issue while submitting data to Factual for Brand: "
										+ brandName);
							}
						} else if (partner
								.equalsIgnoreCase(LBLConstants.LBL_NEUSTAR)) {
							isAPI = true;
							try{
							logger.info("Sending info to " + partner
										+ ", and the total businesses are: "
										+ numberOfRecords);
								
								Map<String, List<String>> errorDetails = NeustarLocalezeClient
										.neustarLocalezeAPI(service,
												localBusinessDTOs);
								List<String> list = errorDetails
										.get("errorDetails");
								sendMail(list, brandName, partner);
								numberOfFailedRecords = list.size();
								updateRenewal(localBusinessDTOs, partner, brandInfoDTO.getBrandName());
							}catch(Exception e){
								logger.error("There was a issue while submitting data to Factual for Brand: "
										+ brandName);
							}
							
						}
					}

					java.util.Date currentDate = DateUtil
							.getCurrentDate("MM/dd/yyyy HH:mm:ss");
					if (brandInfoDTO.getActiveDate() == null) {
						brandInfoDTO.setActiveDate(currentDate);
						service.updateBrandWithActiveDate(brandInfoDTO);
					}

					ChannelNameDTO channelNameDTO = service
							.getChannelById(brandInfoDTO.getChannelID());
					String channelName = "Convergent Mobile";
					if (channelNameDTO != null) {
						channelName = channelNameDTO.getChannelName();
					}
					if(numberOfRecords!=0 && isAPI){
						numberOfRecords = numberOfRecords - numberOfFailedRecords;
						ExportReportEntity exportReportEntity = new ExportReportEntity();
						exportReportEntity.setBrandName(brandName);
						exportReportEntity.setPartner(brandInfoDTO.getSubmisions());
						exportReportEntity.setChannelName(channelName);
						exportReportEntity.setNumberOfRecords(Long
								.valueOf(numberOfRecords));
						exportReportEntity.setDate(currentDate);
						exportReportEntity.setUserName("CustomSubmission");
						service.saveExpostInfo(exportReportEntity);
					}
					
				}
			}
		}
		
	}

	public void updateRenewal(List<LocalBusinessDTO> localBusinessDTOs, String partner, String brandName) {
		java.util.Date currentDate = DateUtil.getCurrentDate("MM/dd/yyyy HH:mm:ss");

		RenewalReportEntity entity = new RenewalReportEntity();
		for (LocalBusinessDTO localBusinessDTO : localBusinessDTOs) {
			RenewalReportEntity reportEntity = service.isStoreExistInRenewal(localBusinessDTO.getStore(),
					localBusinessDTO.getClientId());

			if (reportEntity != null && reportEntity.getStore() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);
				cal.add(Calendar.YEAR, 1);
				Date renewalDate = cal.getTime();
				if (partner.equalsIgnoreCase(LBLConstants.LBL_ACXIOM) && reportEntity.getAcxiomActiveDate() == null) {
					reportEntity.setAcxiomActiveDate(currentDate);
				}
				if (partner.equalsIgnoreCase(LBLConstants.LBL_FACTUAL) && reportEntity.getFactualActiveDate() == null) {
					reportEntity.setFactualActiveDate(currentDate);
				}
				if (partner.equalsIgnoreCase(LBLConstants.LBL_INFOGROUP)
						&& reportEntity.getInfogroupActiveDate() == null) {
					reportEntity.setInfogroupActiveDate(currentDate);
				}
				if (partner.equalsIgnoreCase(LBLConstants.LBL_NEUSTAR)
						&& reportEntity.getLocalezeActiveDate() == null) {
					reportEntity.setLocalezeActiveDate(currentDate);
				}
				if (partner.equalsIgnoreCase(LBLConstants.LBL_ACXIOM) && reportEntity.getAcxiomRenewalDate() == null) {
					reportEntity.setAcxiomRenewalDate(renewalDate);
				}
				if (partner.equalsIgnoreCase(LBLConstants.LBL_FACTUAL)
						&& reportEntity.getFactualRenewalDate() == null) {
					reportEntity.setFactualRenewalDate(renewalDate);
				}
				if (partner.equalsIgnoreCase(LBLConstants.LBL_INFOGROUP)
						&& reportEntity.getInfogroupRenewalDate() == null) {
					reportEntity.setInfogroupRenewalDate(renewalDate);
				}
				if (partner.equalsIgnoreCase(LBLConstants.LBL_NEUSTAR)
						&& reportEntity.getLocalezeRenewalDate() == null) {
					reportEntity.setLocalezeRenewalDate(renewalDate);
				}
				if (reportEntity.getClientId() == null) {
					reportEntity.setClientId(localBusinessDTO.getClientId());
				}
				if (reportEntity.getActiveDate() == null) {
					reportEntity.setActiveDate(currentDate);
				}
				Date renewalDate2 = reportEntity.getRenewalDate();
				if (renewalDate2 == null) {
					reportEntity.setRenewalDate(renewalDate);
					;
				} else if (renewalDate2 != null && reportEntity.getStatus().equals("cancel")
						&& new Date().after(renewalDate2)) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(renewalDate2);
					calendar.add(Calendar.YEAR, 1);

					reportEntity.setRenewalDate(calendar.getTime());
					reportEntity.setAcxiomRenewalDate(calendar.getTime());
					reportEntity.setFactualRenewalDate(calendar.getTime());
					reportEntity.setInfogroupRenewalDate(calendar.getTime());
					reportEntity.setLocalezeRenewalDate(calendar.getTime());
				}

				service.updateRenewalInfo(reportEntity);
			} else {
				Date acxiomActiveDate = service.getActiveDateForClient(brandName, LBLConstants.LBL_ACXIOM);
				Date infogroupActiveDate = service.getActiveDateForClient(brandName, LBLConstants.LBL_INFOGROUP);
				Date neustarActiveDate = service.getActiveDateForClient(brandName, LBLConstants.LBL_NEUSTAR);
				Date factulActiveDate = service.getActiveDateForClient(brandName, LBLConstants.LBL_FACTUAL);
				entity.setStore(localBusinessDTO.getStore());

				Calendar cal = Calendar.getInstance();
				if (acxiomActiveDate != null) {
					cal.setTime(acxiomActiveDate);
					cal.add(Calendar.YEAR, 1);
					Date acxiomRenewalDate = cal.getTime();
					entity.setAcxiomRenewalDate(acxiomRenewalDate);
				}
				if (infogroupActiveDate != null) {
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(infogroupActiveDate);
					cal2.add(Calendar.YEAR, 1);
					Date infogroupRenewalDate = cal2.getTime();
					entity.setInfogroupRenewalDate(infogroupRenewalDate);
				}
				if (neustarActiveDate != null) {
					Calendar cal3 = Calendar.getInstance();
					cal3.setTime(neustarActiveDate);
					cal3.add(Calendar.YEAR, 1);
					Date neustarRenewalDate = cal3.getTime();
					entity.setLocalezeRenewalDate(neustarRenewalDate);
				}
				if (factulActiveDate != null) {
					Calendar cal4 = Calendar.getInstance();
					cal4.setTime(factulActiveDate);
					cal4.add(Calendar.YEAR, 1);
					Date factualRenewalDate = cal4.getTime();
					entity.setFactualRenewalDate(factualRenewalDate);
				}
				Calendar cal5 = Calendar.getInstance();
				Date uploadedTime = localBusinessDTO.getUploadedTime();
				if (uploadedTime == null && acxiomActiveDate != null) {
					uploadedTime = acxiomActiveDate;
				}
				cal5.setTime(uploadedTime);
				cal5.add(Calendar.YEAR, 1);
				entity.setActiveDate(uploadedTime);
				entity.setRenewalDate(cal5.getTime());

				entity.setBusinessName(localBusinessDTO.getCompanyName());
				entity.setAcxiomActiveDate(acxiomActiveDate);
				entity.setFactualActiveDate(factulActiveDate);
				entity.setInfogroupActiveDate(infogroupActiveDate);
				entity.setLocalezeActiveDate(neustarActiveDate);
				entity.setClientId(localBusinessDTO.getClientId());
				entity.setStatus("Active");

				service.saveRenewalInfo(entity);
			}

		}

	}

	/**
	 * copyPropertiesFromLocalBusinessDTOToAcxiomBean
	 * 
	 * @param localBusinessDTO
	 * @param acxiomBean
	 * @throws Exception
	 */
	public void copyPropertiesFromLocalBusinessDTOToAcxiomBean(BusinessService service,
			LocalBusinessDTO localBusinessDTO, AcxiomBean acxiomBean) throws Exception {

		try {

			String hours = getHours(localBusinessDTO);

			acxiomBean.setAdditionalPhoneNumber(getStringValue(localBusinessDTO.getAdditionalNumber()));

			acxiomBean.setClientRecordID(getStringValue(localBusinessDTO.getStore()));
			acxiomBean.setAddressPrivacyFlag("");
			acxiomBean.setAlternateSocialLink(getStringValue(localBusinessDTO.getAlternateSocialLink()));
			acxiomBean.setAlternativeBusinessName("");
			acxiomBean.setAnchorHostBusiness(getStringValue(localBusinessDTO.getAnchorOrHostBusiness()));
			acxiomBean.setAtm("");
			acxiomBean.setBanquetMeetingRooms("");
			acxiomBean.setBrands(getStringValue(localBusinessDTO.getBrands()));
			acxiomBean.setBusinessMobileNumber(getStringValue(localBusinessDTO.getMobileNumber()));
			acxiomBean.setCallTrackingNumber("");
			acxiomBean.setCertifications("");
			acxiomBean.setCompany_email(localBusinessDTO.getLocationEmail());
			acxiomBean.setCompany_Name(getStringValue(localBusinessDTO.getCompanyName()));
			acxiomBean.setContact_Email(getStringValue(localBusinessDTO.getContactEmail()));
			acxiomBean.setContactFirstName(getStringValue(localBusinessDTO.getPrimaryContactFirstName()));
			acxiomBean.setContactGender("M");
			acxiomBean.setContactLastName(getStringValue(localBusinessDTO.getPrimaryContactLastName()));
			acxiomBean.setContactTitle(getStringValue(localBusinessDTO.getContactTitle()));
			acxiomBean.setCoupon_Link(getStringValue(localBusinessDTO.getCouponLink()));

			StringBuffer creditCards = new StringBuffer();
			if (localBusinessDTO.getaMEX() != null && "Y".equalsIgnoreCase(localBusinessDTO.getaMEX())) {
				creditCards.append("A");
			}
			if (localBusinessDTO.getMasterCard() != null && "Y".equalsIgnoreCase(localBusinessDTO.getMasterCard())) {
				creditCards.append("M");
			}
			if (localBusinessDTO.getDiscover() != null && "Y".equalsIgnoreCase(localBusinessDTO.getDiscover())) {
				creditCards.append("D");
			}
			if (localBusinessDTO.getVisa() != null && "Y".equalsIgnoreCase(localBusinessDTO.getVisa())) {
				creditCards.append("V");
			}
			if (localBusinessDTO.getDinersClub() != null && "Y".equalsIgnoreCase(localBusinessDTO.getDinersClub())) {
				creditCards.append("C");
			}
			if (localBusinessDTO.getCash() != null && "Y".equalsIgnoreCase(localBusinessDTO.getCash())) {
				creditCards.append("H");
			}
			if (localBusinessDTO.getCheck() != null && "Y".equalsIgnoreCase(localBusinessDTO.getCheck())) {
				creditCards.append("J");
			}
			if (localBusinessDTO.getDebitCard() != null && "Y".equalsIgnoreCase(localBusinessDTO.getDebitCard())) {
				creditCards.append("E");
			}
			if (localBusinessDTO.getPayPal() != null && "Y".equalsIgnoreCase(localBusinessDTO.getPayPal())) {
				creditCards.append("P");
			}
			String cards = creditCards.toString();
			if (cards.endsWith(",")) {
				cards = cards.replace(",", "");
			}

			acxiomBean.setCreditCardsAccepted(cards);
			acxiomBean.setDeliveryCode("");
			acxiomBean.setDirections("");
			acxiomBean.setDiscountCode("");
			acxiomBean.setDress_Code("");
			acxiomBean.setEmployee_Size(getStringValue(localBusinessDTO.getLocationEmployeeSize()));
			acxiomBean.setEquipment_Rentals("");
			acxiomBean.setFacebook_Link(getStringValue(localBusinessDTO.getFacebookLink()));
			acxiomBean.setFax(getStringValue(localBusinessDTO.getFax()));
			acxiomBean.setFinancing(getStringValue(localBusinessDTO.getFinancing()));
			acxiomBean.setFood_Court("");
			acxiomBean.setFranchiseChain("");
			acxiomBean.setFree_Code("");
			acxiomBean.setFree_Internet("");
			acxiomBean.setGeneral_Content("");
			acxiomBean.setGift_Shop("");
			acxiomBean.setGoogle_Checkout(getStringValue(localBusinessDTO.getGoogleCheckout()));
			acxiomBean.setGreen_Company_Indicator("");

			acxiomBean.setHoursOfOperation(hours);
			acxiomBean.setInternet_Access("");
			acxiomBean.setInvoice(getStringValue(localBusinessDTO.getInvoice()));
			acxiomBean.setKeywords(getStringValue(localBusinessDTO.getKeywords()));
			acxiomBean.setLandmark_Address("");
			acxiomBean.setLandmark_City("");
			acxiomBean.setLandmark_State("");
			acxiomBean.setLandmarkZipCode("");

			String languages = getAcxiomLanguages(localBusinessDTO.getLanguages());
			acxiomBean.setLanguages(languages);

			acxiomBean.setLinkedIn_Link(getStringValue(localBusinessDTO.getLinkedInLink()));

			AcxiomClient acxiomClinet = new AcxiomClient();
			// get the listingId for update
			long listingId = 0;
			try {
				listingId = acxiomClinet.getListingId(localBusinessDTO.getStore());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			if (listingId != 0) {
				acxiomBean.setListingID(Long.valueOf(listingId));
				acxiomBean.setRecord_Status("U");
			} else {
				acxiomBean.setRecord_Status("A");
			}
			// need to add ListingId that is
			// acxiomBean.setListingID(Long.valueOf(l));
			acxiomBean.setLive_Entertainment("");
			String locationAddress = localBusinessDTO.getLocationAddress();
			if (localBusinessDTO.getSuite() != null) {
				locationAddress = locationAddress + ", " + localBusinessDTO.getSuite();
			}
			acxiomBean.setLocation_Address(getStringValue(locationAddress));
			acxiomBean.setLocation_City(getStringValue(localBusinessDTO.getLocationCity()));
			acxiomBean.setLocation_Phone(getStringValue(localBusinessDTO.getLocationPhone()));
			acxiomBean.setLocation_State(getStringValue(localBusinessDTO.getLocationState()));
			acxiomBean.setLocationZipCode(getStringValue(localBusinessDTO.getLocationZipCode()));
			acxiomBean.setLodgingCampgrounds("");
			acxiomBean.setLogo_Link(getStringValue(localBusinessDTO.getLogoLink()));
			acxiomBean.setLong_Company_Name(getStringValue(localBusinessDTO.getCompanyName()));
			acxiomBean.setLongLocalWebaddress("");
			acxiomBean.setMailing_Address("");
			acxiomBean.setMailing_City("");
			acxiomBean.setMailing_State("");
			acxiomBean.setMailingZipCode("");
			acxiomBean.setMenuLink(getStringValue(localBusinessDTO.getMyspaceLink()));
			acxiomBean.setOrdering_Methods("");
			acxiomBean.setParking_Options("");
			acxiomBean.setParkPermitRequired("");
			acxiomBean.setPinterest(getStringValue(localBusinessDTO.getPinteristLink()));
			acxiomBean.setPresence_of_ECommerce("");
			acxiomBean.setPrice_Range("");
			acxiomBean.setProducts(getStringValue(localBusinessDTO.getProducts()));
			acxiomBean.setProfessional_Associations(getStringValue(localBusinessDTO.getProfessionalAssociations()));
			acxiomBean.setProfessionalsOnStaff("");

			acxiomBean.setReservations("");
			acxiomBean.setRestaurantBarTypes("");
			acxiomBean.setSeasonal_Hours("");
			acxiomBean.setServicearea(getStringValue(localBusinessDTO.getServiceArea()));
			acxiomBean.setServices(getStringValue(localBusinessDTO.getServices()));
			acxiomBean.setShuttle_Service("");
			acxiomBean.setSlogans("");
			acxiomBean.setSmoking_Preference("");
			acxiomBean.setSYPH_1(getSYPHCode(service, localBusinessDTO.getCategory1(), localBusinessDTO.getClientId()));

			String syph_1 = acxiomBean.getSYPH_1();
			if (syph_1 == null || syph_1.length() == 0) {
				String syphCodeByClientAndCategoryID = service.getSyphCodeByStore(localBusinessDTO.getStore());
				acxiomBean.setSYPH_1(syphCodeByClientAndCategoryID);
			}

			Integer clientId = localBusinessDTO.getClientId();
			String category2 = localBusinessDTO.getCategory2();
			if (category2 != null && category2.length() > 0) {
				acxiomBean.setSYPH_2(getSYPHCode(service, category2, clientId));
			}
			String category3 = localBusinessDTO.getCategory3();
			if (category3 != null && category3.length() > 0) {
				acxiomBean.setSYPH_3(getSYPHCode(service, category3, clientId));
			}
			String category4 = localBusinessDTO.getCategory4();
			if (category4 != null && category4.length() > 0) {
				acxiomBean.setSYPH_2(getSYPHCode(service, category4, clientId));
			}
			String category5 = localBusinessDTO.getCategory5();
			if (category5 != null && category5.length() > 0) {
				acxiomBean.setSYPH_2(getSYPHCode(service, category5, clientId));
			}
			acxiomBean.setSYPH_6("");
			acxiomBean.setToll_Free(getStringValue(localBusinessDTO.getTollFree()));
			acxiomBean.setTwitter_Link(getStringValue(localBusinessDTO.getTwitterLink()));
			acxiomBean.setUnofficial_Landmark("");
			acxiomBean.setValidation_Options("");
			acxiomBean.setVendor_Id(new Long(localBusinessDTO.getClientId()));
			acxiomBean.setWebAddressURL(getStringValue(localBusinessDTO.getWebAddress()));
			acxiomBean.setYearBusinessEstablished(getStringValue(localBusinessDTO.getYearEstablished()));
			acxiomBean.setYouTubeVideoLink(getStringValue(localBusinessDTO.getYouTubeOrVideoLink()));
			acxiomBean.setZagat_Rating("");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * getStringValue
	 * 
	 * @param str
	 * @return
	 */
	public String getStringValue(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

	/**
	 * write
	 * 
	 * @param localBusinessDTOs
	 * @param isHeadersExist
	 * @throws Exception
	 */
	public void write(List<LocalBusinessDTO> localBusinessDTOs, String path) throws Exception {

		if (submissionUtil == null)
			submissionUtil = new SubmissionUtil();

		File file = new File(path);
		if (file.exists()) {
			file.delete();
			file.createNewFile();
		}

		CsvWriter csvOutput = new CsvWriter(new FileWriter(file, true), ',');

		String[] headers = { "ListingID", "Vendor_Id", "ClientRecordID", "Record_Status", "Company_Name",
				"Location_Address", "Location_City", "Location_State", "LocationZipCode", "Mailing_Address",
				"Mailing_City", "Mailing_State", "MailingZipCode", "Landmark_Address", "Landmark_City",
				"Landmark_State", "LandmarkZipCode", "Location_Phone", "Fax", "SYPH_1", "SYPH_2", "SYPH_3", "SYPH_4",
				"SYPH_5", "SYPH_6", "FranchiseChain", "ContactFirstName", "ContactLastName", "ContactGender",
				"ContactTitle", "Contact_Email", "Company_email", "Employee_Size", "WebAddressURL", "Toll_Free",
				"HoursOfOperation", "CreditCardsAccepted", "Financing", "Google_Checkout", "Invoice",
				"LongLocalWebaddress", "MenuLink", "Coupon_Link", "Twitter_Link", "LinkedIn_Link", "Facebook_Link",
				"AlternateSocialLink", "YouTubeVideoLink", "Logo_Link", "Pinterest", "Products", "Services", "Brands",
				"Keywords", "Ordering_Methods", "BusinessMobileNumber", "CallTrackingNumber", "AdditionalPhoneNumber",
				"Parking_Options", "Validation_Options", "Professional_Associations", "Certifications", "Atm",
				"Languages", "AlternativeBusinessName", "AnchorHostBusiness", "Unofficial_Landmark", "Price_Range",
				"Reservations", "BanquetMeetingRooms", "RestaurantBarTypes", "Dress_Code", "Shuttle_Service",
				"Free_Internet", "Internet_Access", "Food_Court", "ParkPermitRequired", "Equipment_Rentals",
				"LodgingCampgrounds", "Live_Entertainment", "Smoking_Preference", "Zagat_Rating", "Gift_Shop",
				"Presence_of_ECommerce", "Green_Company_Indicator", "Long_Company_Name", "Seasonal_Hours",
				"DiscountCode", "DeliveryCode", "Free_Code", "YearBusinessEstablished", "Directions", "Slogans",
				"AddressPrivacyFlag", "Servicearea", "ProfessionalsOnStaff", "General_Content" };
		int size = localBusinessDTOs.size();
		logger.info("size:::::::::::" + size);
		for (int index = -1; index < size - 1; index++) {
			RenewalReportDTO renewalReportDTO = service.isRenewed(localBusinessDTOs.get(index + 1).getStore(),
					localBusinessDTOs.get(index + 1).getClientId());
			if (renewalReportDTO != null) {
				Date cancelledEffeciveDate = renewalReportDTO.getCancelledEffeciveDate();
				boolean isDate = false;
				if (cancelledEffeciveDate != null) {
					Date currentDateDate = new Date();
					isDate = cancelledEffeciveDate.compareTo(currentDateDate) < 0;
				}

				if ((renewalReportDTO.getStatus().equals("Renewed") || renewalReportDTO.getStatus().equals("Active")
						|| isDate)) {
					if (index < 0) {
						// if (!isHeadersExist) {
						writeTocsv(headers, null, csvOutput);
						// }
						continue;
					}
					LocalBusinessDTO localBusinessDTO = localBusinessDTOs.get(index + 1);
					AcxiomBean acxiomBean = new AcxiomBean();
					submissionUtil.copyPropertiesFromLocalBusinessDTOToAcxiomBean(service, localBusinessDTO,
							acxiomBean);
					writeTocsv(headers, acxiomBean, csvOutput);
				}
			}

		}
		csvOutput.flush();
		csvOutput.close();
	}

	public RenewalReportDTO isRenewed(String store, Integer clientId) {
		return service.isRenewed(store, clientId);
	}

	/**
	 * writeTocsv
	 * 
	 * @param headers
	 * @param acxiomBean
	 * @param csvOutput
	 * @throws IOException
	 */
	public static void writeTocsv(String[] headers, AcxiomBean acxiomBean, CsvWriter csvOutput) throws IOException {
		for (String header : headers) {
			if (acxiomBean != null) {

				if (!header.startsWith("SYPH")) {
					header = header.substring(0, 1).toLowerCase() + header.substring(1);
				}
				try {
					csvOutput.write(org.apache.commons.beanutils.BeanUtils.getProperty(acxiomBean, header));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				csvOutput.write(header);
			}

		}
		csvOutput.endRecord();
	}

	public String getSYPHCode(BusinessService service, String category, Integer clientId) {
		String syphcode = service.getSyphCodeByClientAndCategoryID(category, clientId);
		return syphcode;
	}

	/**
	 * 
	 * @param languages
	 * @return
	 */
	public String getAcxiomLanguages(String languages) {
		StringBuffer language = new StringBuffer();
		if (StringUtils.containsIgnoreCase(languages, "Chinese")) {
			language.append("C");
		}
		if (StringUtils.containsIgnoreCase(languages, "German")) {
			language.append("G");
		}
		if (StringUtils.containsIgnoreCase(languages, "French")) {
			language.append("F");
		}
		if (StringUtils.containsIgnoreCase(languages, "Italian")) {
			language.append("I");
		}
		if (StringUtils.containsIgnoreCase(languages, "Japanese")) {
			language.append("J");
		}
		if (StringUtils.containsIgnoreCase(languages, "Korean")) {
			language.append("K");
		}
		if (StringUtils.containsIgnoreCase(languages, "Spanish")) {
			language.append("S");
		}
		if (StringUtils.containsIgnoreCase(languages, "Russian")) {
			language.append("R");
		}
		return language.toString();
	}

	public static String getHours(LocalBusinessDTO localBusinessDTO) {
		StringBuffer workingHours = new StringBuffer();
		String mondayOpen = localBusinessDTO.getMondayOpen();
		String mondayClose = localBusinessDTO.getMondayClose();

		String tuesdayOpen = localBusinessDTO.getTuesdayOpen();
		String tuuesdayClose = localBusinessDTO.getTuesdayClose();

		String wedOpen = localBusinessDTO.getWednesdayOpen();
		String wedClose = localBusinessDTO.getWednesdayClose();

		String thursdayOpen = localBusinessDTO.getThursdayOpen();
		String thursdayClose = localBusinessDTO.getThursdayClose();

		String fridayOpen = localBusinessDTO.getFridayOpen();
		String fridayClose = localBusinessDTO.getFridayClose();

		String satOpen = localBusinessDTO.getSaturdayOpen();
		String satClose = localBusinessDTO.getSaturdayClose();

		String sunOpen = localBusinessDTO.getSundayOpen();
		String sunClose = localBusinessDTO.getSundayClose();

		if ("CLOSE".equalsIgnoreCase(mondayOpen) || "CLOSE".equalsIgnoreCase(mondayClose)) {
			workingHours.append("MON CLOSE, ");
		}

		else if (mondayOpen != null && mondayOpen.trim().length() > 0 && mondayClose != null
				&& mondayClose.trim().length() > 0) {
			workingHours.append("MON " + mondayOpen + "-" + mondayClose + ", ");
		}
		if ("CLOSE".equalsIgnoreCase(mondayOpen) || "CLOSE".equalsIgnoreCase(mondayClose)) {
			workingHours.append("TUE CLOSE, ");
		} else if (tuesdayOpen != null && tuesdayOpen.trim().length() > 0 && tuuesdayClose != null
				&& tuuesdayClose.trim().length() > 0) {
			workingHours.append("TUE " + tuesdayOpen + "-" + tuuesdayClose + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(wedOpen) || "CLOSE".equalsIgnoreCase(wedClose)) {
			workingHours.append("WED CLOSE, ");
		} else if (wedOpen != null && wedOpen.trim().length() > 0 && wedClose != null && wedClose.trim().length() > 0) {
			workingHours.append("WED " + wedOpen + "-" + wedClose + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(thursdayOpen) || "CLOSE".equalsIgnoreCase(thursdayClose)) {
			workingHours.append("THU CLOSE, ");
		} else if (tuesdayOpen != null && tuesdayOpen.trim().length() > 0 && tuuesdayClose != null
				&& tuuesdayClose.trim().length() > 0) {
			workingHours.append("TUE " + tuesdayOpen + "-" + tuuesdayClose + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(fridayOpen) || "CLOSE".equalsIgnoreCase(fridayClose)) {
			workingHours.append("FRI CLOSE, ");
		} else if (fridayOpen != null && fridayOpen.trim().length() > 0 && fridayClose != null
				&& fridayClose.trim().length() > 0) {
			workingHours.append("FRI " + fridayOpen + "-" + fridayClose + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(satOpen) || "CLOSE".equalsIgnoreCase(satClose)) {
			workingHours.append("SAT CLOSE, ");
		} else if (satOpen != null && satOpen.trim().length() > 0 && satClose != null && satClose.trim().length() > 0) {
			workingHours.append("SAT " + satOpen + "-" + satClose + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(sunOpen) || "CLOSE".equalsIgnoreCase(sunClose)) {
			workingHours.append("SUN CLOSE");
		} else if (sunOpen != null && sunOpen.trim().length() > 0 && sunClose != null && sunClose.trim().length() > 0) {
			workingHours.append("SUN" + sunOpen + "-" + sunClose);
		}

		String hours = workingHours.toString();
		return hours;

	}

	public void saveSearchDomainInfo(SearchDomainDTO domainDTO) {

		service.saveSearchDomainInfo(domainDTO);

	}

	public static void sendMail(List<String> list, String brandName, String partner) {
		String body = "";
		String status = "SUCCESS";

		StringBuffer bodyText = new StringBuffer();

		if (list != null && list.size() > 0) {
			status = "FAILED";
			for (int j = 0; j < list.size(); j++) {
				if (bodyText.length() == 0) {
					bodyText.append("Total Stores which are failed to submit are: " + list.size() + ", stores are: ");
				}
				bodyText.append(list.get(j).toString() + ", ");
			}
			body = bodyText.toString();
			body = body.substring(0, body.length() - 1);
		}

		String toMail = LBLConstants.NOTIFY_MAIL;

		String subject = brandName + " Submission Status to " + partner + " is " + status;
		if (body.length() == 0) {
			body = subject;
		}
		MailUtil.sendNotification(toMail, body, subject);
	}
}
