package com.business.common.util;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.business.model.dataaccess.impl.BusinessDaoImpl;
import com.business.service.BusinessService;
import com.business.web.bean.BussinessData;
import com.business.web.bean.LblErrorBean;
import com.business.web.bean.UploadBusinessBean;

/**
 * 
 * @author Vasanth
 * 
 */

public class UploadBeanValidateUtil {
	Logger logger = Logger.getLogger(UploadBeanValidateUtil.class);

	/**
	 * validate upload Business information
	 * 
	 * @param service
	 * @param listDataFromXLS
	 * @return
	 */
	public <Entity> BussinessData validateBusiness(BusinessService service,
			List<UploadBusinessBean> listDataFromXLS) {
		List<UploadBusinessBean> validBusinessList = new ArrayList<UploadBusinessBean>();
		List<LblErrorBean> erroredBusinessList = new ArrayList<LblErrorBean>();
		
	/*	try {
			ValidationExecutorService.executeService(service, listDataFromXLS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		for (int i = 0; i < listDataFromXLS.size(); i++) {
			UploadBusinessBean uploadBean = listDataFromXLS.get(i);
			StringBuffer errorMsg = validateUploadBean(service, uploadBean);

			if (errorMsg.toString() == "" || errorMsg.length() == 0) {
				validBusinessList.add(uploadBean);
			} else {
				String store = uploadBean.getStore();
				Integer client = uploadBean.getClientId();

				// if (!service.isStoreUnique(store, client)) {
				LblErrorBean errorBean = copyBeanDetailsToErrorBean(
						errorMsg.toString(), uploadBean);
				erroredBusinessList.add(errorBean);
				// }
			}
		}
		BussinessData bussinessData = new BussinessData();
		bussinessData.setValidBusinessList(validBusinessList);
		bussinessData.setErroredBusinessList(erroredBusinessList);
		return bussinessData;
	}

	/**
	 * validateUploadBean
	 * 
	 * @param service
	 * @param uploadBean
	 * @return
	 */
	public StringBuffer validateUploadBean(BusinessService service,
			UploadBusinessBean uploadBean) {
		StringBuffer errorMsg = new StringBuffer();

		Integer clientId = uploadBean.getClientId();
		if (clientId != null) {
			String validate = validate(clientId.toString(), 5, null,
					"clientId", LBLConstants.FIELD_TYPE_NUMERIC);
			if (validate.length() == 0) {
				String brand = service.getBrandByClientId(clientId);
				if (brand != null && brand.length() > 0) {
					uploadBean.setClient(brand);
				} else {
					errorMsg.append("Invalid clientId ,");
				}
			}
			errorMsg.append(validate);

		} else {
			errorMsg.append(fieldRequired("ClientID"));
		}
		// if(!"D".equalsIgnoreCase(uploadBean.getActionCode())){
		if (uploadBean.getStore() != null && uploadBean.getStore().length() > 0) {
			String store = uploadBean.getStore();
			/*
			 * Integer client = uploadBean.getClientId();
			 * 
			 * if (service.isStoreUnique(store, client)) {
			 * errorMsg.append("Store already Exist , "); }
			 */
			errorMsg.append(validate(store, 50, null, "Store",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));

		} else {
			errorMsg.append(fieldRequired("Store"));
		}
		// }

		if (uploadBean.getActionCode() != null
				&& uploadBean.getActionCode().length() > 0) {
			String actionCode = uploadBean.getActionCode().toUpperCase();
			if (!LBLConstants.ACTION_CODES.contains(actionCode)) {
				errorMsg.append("ActionCode must be A or U or R or D , ");
			}
			errorMsg.append(validate(actionCode, 1, "equal", "ActionCode",
					LBLConstants.FIELD_TYPE_ALPHA));

		} else {
			errorMsg.append(fieldRequired("ActionCode"));
		}

		if (uploadBean.getCountryCode() != null
				&& uploadBean.getCountryCode().length() > 0) {
			String countryCode = uploadBean.getCountryCode();
			if (!LBLConstants.COUNTRY_CODES.contains(countryCode)) {
				errorMsg.append("CountryCode must be US or CA , ");
			}
			errorMsg.append(validate(countryCode, 2, "equal", "CountryCode",
					LBLConstants.FIELD_TYPE_ALPHA));
		} else {
			errorMsg.append(fieldRequired("CountryCode"));
		}
		if (uploadBean.getCompanyName() != null
				&& uploadBean.getCompanyName().length() > 0) {
			String companyName = uploadBean.getCompanyName();
			errorMsg.append(validate(companyName, 100, null, "CompanyName", ""));
		} else {
			errorMsg.append(fieldRequired("CompanyName"));
		}
		if (uploadBean.getAlternativeName() != null
				&& uploadBean.getAlternativeName().length() > 0) {
			errorMsg.append(validate(uploadBean.getAlternativeName(), 256,
					null, "AlternativeName",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}
		if (uploadBean.getAnchorOrHostBusiness() != null
				&& uploadBean.getAnchorOrHostBusiness().length() > 0) {
			errorMsg.append(validate(uploadBean.getAnchorOrHostBusiness(), 256,
					null, "AnchorOrHostBusiness",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}

		String locationState = uploadBean.getLocationState();
		if (uploadBean.getLocationAddress() != null
				&& uploadBean.getLocationAddress().length() > 0) {
			String locationAddress = uploadBean.getLocationAddress();
			errorMsg.append(validate(locationAddress, 68, null,
					"LocationAddress", LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			if (!conatinsString(locationAddress,
					LBLConstants.NON_LOCATION_ADDRESS_DATA)) {
				errorMsg.append("LocationAddress Cannot contain PO Box or P.O. Box or P O Box or P. O. Box or P.O.BOX or PO BOX, ");
			}
			if (containsParenthesis(locationAddress)) {
				errorMsg.append("LocationAddress cannot conatins Parenthesis, ");
			}
			if (!isValidLocationAdress(locationAddress)
					&& !locationState.equalsIgnoreCase("MI")) {
				errorMsg.append("LocationAddress Must begin with street number (or) <br/> N or S or E or W followed by number, ");
			}
			if (locationState.equalsIgnoreCase("MI")) {
				if (!isValidMIAddress(locationAddress)) {
					errorMsg.append("LocationAddress Must begin with street number (or) <br/> G, ");
				}
			}

			if (containsNumeric(locationAddress)) {
				errorMsg.append("LocationAddress cannot be only number.<br/> Please resubmit valid address, ");
			}
			if (locationAddress.contains("&")) {
				errorMsg.append("Invalid LocationAddress, ");
			}
			if (locationAddress.contains("()")) {
				errorMsg.append("Invalid LocationAddress, ");
			}
		} else {
			errorMsg.append(fieldRequired("LocationAddress"));
		}

		if (uploadBean.getSuite() != null && uploadBean.getSuite().length() > 0) {
			String suite = uploadBean.getSuite();
			errorMsg.append(validate(suite, 20, null, "Suite",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			if (!containsValidData(suite, LBLConstants.NON_SUITE_DATA)) {
				errorMsg.append("Enter Suite Without Fl Ste Rm # <br/> Floor Suite Room Unit Bldg <br/> PO Box P.O. Box or P O Box , ");
			}
		}

		if (uploadBean.getLocationCity() != null
				&& uploadBean.getLocationCity().length() > 0) {
			errorMsg.append(validate(uploadBean.getLocationCity(), 50, null,
					"LocationCity", LBLConstants.FIELD_TYPE_ALPHA));
		} else {
			errorMsg.append(fieldRequired("LocationCity"));
		}
		if (uploadBean.getLocationState() != null
				&& uploadBean.getLocationState().length() > 0) {
			String state = uploadBean.getLocationState();
			String countyCode = uploadBean.getCountryCode();
			if (!service.isValidState(state, countyCode)) {
				errorMsg.append("Invalid LocationState For the entered Country , ");
			}
			errorMsg.append(validate(state, 2, "equal", "LocationState",
					LBLConstants.FIELD_TYPE_ALPHA));
		} else {
			errorMsg.append(fieldRequired("LocationState"));
		}
		if (uploadBean.getLocationZipCode() != null
				&& uploadBean.getLocationZipCode().length() > 0) {
			String locationZipCode = uploadBean.getLocationZipCode();

			if ("CA".equalsIgnoreCase(uploadBean.getCountryCode())) {
				errorMsg.append(validate(locationZipCode,
						getZipCodeLength(uploadBean.getCountryCode()), "equal",
						"LocationZipCode",
						LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			} else {
				errorMsg.append(validate(locationZipCode,
						getZipCodeLength(uploadBean.getCountryCode()), "equal",
						"LocationZipCode", LBLConstants.FIELD_TYPE_NUMERIC));
			}
		} else {
			errorMsg.append(fieldRequired("LocationZipCode"));
		}

		if (uploadBean.getLocationPhone() != null
				&& uploadBean.getLocationPhone().length() > 0) {
			String locationPhone = uploadBean.getLocationPhone();
			errorMsg.append(validate(locationPhone, 10, "equal",
					"LocationPhone", LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getLocationPhone() != null
				&& (uploadBean.getLocationPhone().length() == 10)) {
			String locationphone = uploadBean.getLocationPhone();

			String substring = locationphone.substring(0, 3);
			String phone = locationphone.substring(3, 6);
			if (substring.equals("555")) {
				errorMsg.append("Invalid LocationPhone, ");
			}
			if (phone.equals("555")) {
				errorMsg.append("Invalid LocationPhone, ");
			}
		} else {
			errorMsg.append(fieldRequired("LocationPhone"));
		}
		if (uploadBean.getFax() != null && uploadBean.getFax().length() > 0) {
			String fax = uploadBean.getFax();
			errorMsg.append(validate(fax, 10, "equal", "Fax",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getTollFree() != null
				&& uploadBean.getTollFree().length() > 0) {
			String tollFree = uploadBean.getTollFree();
			errorMsg.append(validate(tollFree, 10, "equal", "TollFree",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getTty() != null && uploadBean.getTty().length() > 0) {
			String tty = uploadBean.getTty();
			errorMsg.append(validate(tty, 10, "equal", "Tty",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getMobileNumber() != null
				&& uploadBean.getMobileNumber().length() > 0) {
			String mobileNumber = uploadBean.getMobileNumber();
			errorMsg.append(validate(mobileNumber, 10, "equal", "MobileNumber",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getAdditionalNumber() != null
				&& uploadBean.getAdditionalNumber().length() > 0) {
			errorMsg.append(validate(uploadBean.getAdditionalNumber(), 10,
					"equal", "AddtionalNumber", LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getLocationEmail() != null
				&& uploadBean.getLocationEmail().length() > 0) {
			String locationEmail = uploadBean.getLocationEmail();
			if (!validEmail(locationEmail)) {
				errorMsg.append("Invalid LocationEmail , ");
			}
			errorMsg.append(validate(locationEmail, 256, null, "LocationEmail",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}

		if (uploadBean.getShortWebAddress() != null
				&& uploadBean.getShortWebAddress().length() > 0) {
			String sWebAddress = uploadBean.getShortWebAddress();
			if (sWebAddress.startsWith("http://")
					|| sWebAddress.startsWith("https://")) {
				errorMsg.append("ShortWebAddress should not start with http:// or https:// , ");
			}
			/*
			 * if (!endsWith(sWebAddress,LBLConstants.WEB_ADDRESS_ENDS_WITH)) {
			 * errorMsg
			 * .append("ShortWebAddress must ends with .com, .net or .org , ");
			 * }
			 */
			errorMsg.append(validate(sWebAddress, 40, null, "ShortWebAddress",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}
		if (uploadBean.getWebAddress() != null
				&& uploadBean.getWebAddress().length() > 0) {
			String webAddress = uploadBean.getWebAddress();
			if (!webAddress.startsWith("http")) {
				errorMsg.append("longWebAddress should starts with http:// or https://, ");
			}
			/*
			 * if (!endsWith(webAddress,LBLConstants.WEB_ADDRESS_ENDS_WITH)) {
			 * errorMsg
			 * .append("WebAddress must ends with .com, .net or .org , "); }
			 */
			errorMsg.append(validate(webAddress, 256, null, "longWebAddress",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}

		if (uploadBean.getCategory1() != null
				&& uploadBean.getCategory1().length() > 0) {
			String category1 = uploadBean.getCategory1();
			if (!service.isCatagoryExist(category1)) {
				errorMsg.append("Category1 not exist/Invalid category1 , ");
			}
			errorMsg.append(validate(category1, 6, "equal", "Category1",
					LBLConstants.FIELD_TYPE_NUMERIC));
		} else {
			errorMsg.append(fieldRequired("Category1"));
		}
		if (uploadBean.getCategory2() != null
				&& uploadBean.getCategory2().length() > 0) {
			String category2 = uploadBean.getCategory2();
			if (!service.isCatagoryExist(category2)) {
				errorMsg.append("Category2 not exist/Invalid category2 , ");
			}
			errorMsg.append(validate(category2, 6, "equal", "Category2",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}

		if (uploadBean.getCategory3() != null
				&& uploadBean.getCategory3().length() > 0) {
			String category3 = uploadBean.getCategory3();
			if (!service.isCatagoryExist(category3)) {
				errorMsg.append("Category3 not exist/Invalid category3 , ");
			}
			errorMsg.append(validate(category3, 6, "equal", "Category3",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}

		if (uploadBean.getCategory4() != null
				&& uploadBean.getCategory4().length() > 0) {
			String category4 = uploadBean.getCategory4();
			if (!service.isCatagoryExist(category4)) {
				errorMsg.append("Category4 not exist/Invalid Category4 , ");
			}
			errorMsg.append(validate(category4, 6, "equal", "Category4",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}

		if (uploadBean.getCategory5() != null
				&& uploadBean.getCategory5().length() > 0) {
			String category5 = uploadBean.getCategory5();
			if (!service.isCatagoryExist(category5)) {
				errorMsg.append("Category5 not exist/Invalid category5 , ");
			}
			errorMsg.append(validate(category5, 6, "equal", "Category5",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}

		if (uploadBean.getLogoLink() != null
				&& uploadBean.getLogoLink().length() > 0) {
			String logoLink = uploadBean.getLogoLink();
			errorMsg.append(validate(logoLink, 128, null, "LogoLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}
		if (uploadBean.getServiceArea() != null
				&& uploadBean.getServiceArea().length() > 0) {
			String sArea = uploadBean.getServiceArea();
			errorMsg.append(validate(sArea, 256, null, "ServiceArea",
					LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getPrimaryContactFirstName() != null
				&& uploadBean.getPrimaryContactFirstName().length() > 0) {
			String pCFName = uploadBean.getPrimaryContactFirstName();
			errorMsg.append(validate(pCFName, 20, null,
					"PrimaryContactFirstName", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getPrimaryContactLastName() != null
				&& uploadBean.getPrimaryContactLastName().length() > 0) {
			String pCLName = uploadBean.getPrimaryContactLastName();
			errorMsg.append(validate(pCLName, 20, null,
					"PrimaryContactLastName", LBLConstants.FIELD_TYPE_ALPHA));
		}

		if (uploadBean.getContactTitle() != null
				&& uploadBean.getContactTitle().length() > 0) {
			String cTitle = uploadBean.getContactTitle();
			errorMsg.append(validate(cTitle, 20, null, "ContactTitle",
					LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getContactEmail() != null
				&& uploadBean.getContactEmail().length() > 0) {
			String cEmail = uploadBean.getContactEmail();
			errorMsg.append(validate(cEmail, 256, null, "ContactEmail",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}
		if (uploadBean.getLocationEmployeeSize() != null
				&& uploadBean.getLocationEmployeeSize().length() > 0) {
			String locationEmployeeSize = uploadBean.getLocationEmployeeSize();
			errorMsg.append(validate(locationEmployeeSize, 5, null,
					"LocationEmployeeSize", LBLConstants.FIELD_TYPE_NUMERIC));
		}

		if (uploadBean.getTitle_ManagerOrOwner() != null
				&& uploadBean.getTitle_ManagerOrOwner().length() > 0) {
			String tManagerOrOwner = uploadBean.getTitle_ManagerOrOwner();
			if (!validTitles(tManagerOrOwner,
					LBLConstants.TITLE_MANAGER_OR_OWNER)) {
				errorMsg.append("For Title_ManagerOrOwner Only options available:"
						+ " CEO or Owner or President or EVP or SVP or VP or Plant Manager or Office Manager. , ");
			}
			errorMsg.append(validate(tManagerOrOwner.split(","), 50, null,
					"Title_ManagerOrOwner", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getProfessionalTitle() != null
				&& uploadBean.getProfessionalTitle().length() > 0) {
			String pTitle = uploadBean.getProfessionalTitle();
			if (!validTitles(pTitle, LBLConstants.PROFESSIONAL_TITLES)) {
				errorMsg.append("For Professional Title Only Options availableare:"
						+ " CPA or DC or DDS or DO or DPM or DVM or MD or OD or PE or PHD. , ");
			}
			errorMsg.append(validate(pTitle.split(","), 3, null,
					"ProfessionalTitle", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getProfessionalAssociations() != null
				&& uploadBean.getProfessionalAssociations().length() > 0) {
			String pAssociations = uploadBean.getProfessionalAssociations();
			if (pAssociations.length() > 256) {
				errorMsg.append("Professional Associations should be less than 256 characterss or same, ");
			}

		}

		if (uploadBean.getMondayOpen() != null
				&& uploadBean.getMondayOpen().length() > 0) {
			String mOpen = uploadBean.getMondayOpen();
			String lengthValidation = lengthValidation(mOpen, 5, "equal",
					"MondayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(mOpen);
				if (time == "") {
					errorMsg.append("Invalid MondayOpen time , ");
				} else {
					uploadBean.setMondayOpen(time);
				}
			}
		}

		if (uploadBean.getMondayClose() != null
				&& uploadBean.getMondayClose().length() > 0) {
			String mClose = uploadBean.getMondayClose();
			String lengthValidation = lengthValidation(mClose, 5, "equal",
					"MondayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(mClose);
				if (time == "") {
					errorMsg.append("Invalid MondayClose time , ");
				} else {
					uploadBean.setMondayClose(time);
				}
			}
		}
		if (uploadBean.getTuesdayOpen() != null
				&& uploadBean.getTuesdayOpen().length() > 0) {
			String tOpen = uploadBean.getTuesdayOpen();
			String lengthValidation = lengthValidation(tOpen, 5, "equal",
					"TuesdayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(tOpen);
				if (time == "") {
					errorMsg.append("Invalid TuesdayOpen time , ");
				} else {
					uploadBean.setTuesdayOpen(time);
				}
			}
		}
		if (uploadBean.getTuesdayClose() != null
				&& uploadBean.getTuesdayClose().length() > 0) {
			String tClose = uploadBean.getTuesdayClose();
			String lengthValidation = lengthValidation(tClose, 5, "equal",
					"TuesdayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(tClose);
				if (time == "") {
					errorMsg.append("Invalid TuesdayClose time , ");
				} else {
					uploadBean.setTuesdayClose(time);
				}
			}
		}
		if (uploadBean.getWednesdayOpen() != null
				&& uploadBean.getWednesdayOpen().length() > 0) {
			String wdOpen = uploadBean.getWednesdayOpen();
			String lengthValidation = lengthValidation(wdOpen, 5, "equal",
					"WednesdayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(wdOpen);
				if (time == "") {
					errorMsg.append("Invalid WednesdayOpen time , ");
				} else {
					uploadBean.setWednesdayOpen(time);
				}
			}
		}
		if (uploadBean.getWednesdayClose() != null
				&& uploadBean.getWednesdayClose().length() > 0) {
			String wdOpen = uploadBean.getWednesdayClose();
			String lengthValidation = lengthValidation(wdOpen, 5, "equal",
					"WednesdayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(wdOpen);
				if (time == "") {
					errorMsg.append("Invalid WednesdayClose time , ");
				} else {
					uploadBean.setWednesdayClose(time);
				}
			}
		}
		if (uploadBean.getThursdayOpen() != null
				&& uploadBean.getThursdayOpen().length() > 0) {
			String thOpen = uploadBean.getThursdayOpen();
			String lengthValidation = lengthValidation(thOpen, 5, "equal",
					"ThursdayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(thOpen);
				if (time == "") {
					errorMsg.append("Invalid ThursdayOpen time , ");
				} else {
					uploadBean.setThursdayOpen(time);
				}
			}
		}
		if (uploadBean.getThursdayClose() != null
				&& uploadBean.getThursdayClose().length() > 0) {
			String thClose = uploadBean.getThursdayClose();
			String lengthValidation = lengthValidation(thClose, 5, "equal",
					"ThursdayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(thClose);
				if (time == "") {
					errorMsg.append("Invalid ThursdayClose time , ");
				} else {
					uploadBean.setThursdayClose(time);
				}
			}
		}
		if (uploadBean.getFridayOpen() != null
				&& uploadBean.getFridayOpen().length() > 0) {
			String fOpen = uploadBean.getFridayOpen();
			String lengthValidation = lengthValidation(fOpen, 5, "equal",
					"FridayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(fOpen);
				if (time == "") {
					errorMsg.append("Invalid FridayOpen time , ");
				} else {
					uploadBean.setFridayOpen(time);
				}
			}
		}

		if (uploadBean.getFridayClose() != null
				&& uploadBean.getFridayClose().length() > 0) {
			String fClose = uploadBean.getFridayClose();
			String lengthValidation = lengthValidation(fClose, 5, "equal",
					"FridayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(fClose);
				if (time == "") {
					errorMsg.append("Invalid FridayClose time , ");
				} else {
					uploadBean.setFridayClose(time);
				}
			}
		}
		if (uploadBean.getSaturdayOpen() != null
				&& uploadBean.getSaturdayOpen().length() > 0) {
			String satOpen = uploadBean.getSaturdayOpen();
			String lengthValidation = lengthValidation(satOpen, 5, "equal",
					"SaturdayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(satOpen);
				if (time == "") {
					errorMsg.append("Invalid SaturdayOpen time , ");
				} else {
					uploadBean.setSaturdayOpen(time);
				}
			}
		}
		if (uploadBean.getSaturdayClose() != null
				&& uploadBean.getSaturdayClose().length() > 0) {
			String satClose = uploadBean.getSaturdayClose();
			String lengthValidation = lengthValidation(satClose, 5, "equal",
					"SaturdayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(satClose);
				if (time == "") {
					errorMsg.append("Invalid SaturdayClose time , ");
				} else {
					uploadBean.setSaturdayClose(time);
				}
			}
		}
		if (uploadBean.getSundayOpen() != null
				&& uploadBean.getSundayOpen().length() > 0) {
			String sunOpen = uploadBean.getSundayOpen();
			String lengthValidation = lengthValidation(sunOpen, 5, "equal",
					"SundayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(sunOpen);
				if (time == "") {
					errorMsg.append("Invalid SundayOpen time , ");
				} else {
					uploadBean.setSundayOpen(time);
				}
			}
		}
		if (uploadBean.getSundayClose() != null
				&& uploadBean.getSundayClose().length() > 0) {
			String sunClose = uploadBean.getSundayClose();
			String lengthValidation = lengthValidation(sunClose, 5, "equal",
					"SundayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(sunClose);
				if (time == "") {
					errorMsg.append("Invalid SundayClose time , ");
				} else {
					uploadBean.setSundayClose(time);
				}
			}
		}

		if (uploadBean.getaMEX() != null && uploadBean.getaMEX().length() > 0) {
			String aMex = uploadBean.getaMEX();
			errorMsg.append(validate(aMex, 1, "equal", "AMEX",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(aMex)) {
				errorMsg.append("Use only Y or N or Blank for AMEX , ");
			}
		}

		if (uploadBean.getDiscover() != null
				&& uploadBean.getDiscover().length() > 0) {
			String discover = uploadBean.getDiscover();
			errorMsg.append(validate(discover, 1, "equal", "Discover",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(discover)) {
				errorMsg.append("Use only Y or N or Blank for Discover , ");
			}
		}
		if (uploadBean.getVisa() != null && uploadBean.getVisa().length() > 0) {
			String visa = uploadBean.getVisa();
			errorMsg.append(validate(visa, 1, "equal", "Visa",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(visa)) {
				errorMsg.append("Use only Y or N or Blank for Visa , ");
			}
		}
		if (uploadBean.getMasterCard() != null
				&& uploadBean.getMasterCard().length() > 0) {
			String masterCard = uploadBean.getMasterCard();
			errorMsg.append(validate(masterCard, 1, "equal", "MasterCard",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(masterCard)) {
				errorMsg.append("Use only Y or N or Blank for MasterCard , ");
			}
		}
		if (uploadBean.getDinersClub() != null
				&& uploadBean.getDinersClub().length() > 0) {
			String dinersClub = uploadBean.getDinersClub();
			errorMsg.append(validate(dinersClub, 1, "equal", "DinersClub",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(dinersClub)) {
				errorMsg.append("Use only Y or N or Blank for DinersClub , ");
			}
		}
		if (uploadBean.getDebitCard() != null
				&& uploadBean.getDebitCard().length() > 0) {
			String debitCard = uploadBean.getDebitCard();
			errorMsg.append(validate(debitCard, 1, "equal", "DebitCard",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(debitCard)) {
				errorMsg.append("Use only Y or N or Blank for DebitCard , ");
			}
		}
		if (uploadBean.getStoreCard() != null
				&& uploadBean.getStoreCard().length() > 0) {
			String storeCard = uploadBean.getStoreCard();
			errorMsg.append(validate(storeCard, 1, "equal", "StoreCard",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(storeCard)) {
				errorMsg.append("Use only Y or N or Blank for StoreCard , ");
			}
		}
		if (uploadBean.getOtherCard() != null
				&& uploadBean.getOtherCard().length() > 0) {
			String otherCard = uploadBean.getOtherCard();
			errorMsg.append(validate(otherCard, 1, "equal", "OtherCard",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(otherCard)) {
				errorMsg.append("Use only Y or N or Blank for OtherCard , ");
			}
		}
		if (uploadBean.getCash() != null && uploadBean.getCash().length() > 0) {
			String cash = uploadBean.getCash();
			errorMsg.append(validate(cash, 1, "equal", "Cash",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(cash)) {
				errorMsg.append("Use only Y or N or Blank for Cash , ");
			}
		}
		if (uploadBean.getCheck() != null && uploadBean.getCheck().length() > 0) {
			String check = uploadBean.getCheck();
			errorMsg.append(validate(check, 1, "equal", "Check",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(check)) {
				errorMsg.append("Use only Y or N or Blank for Check , ");
			}
		}
		if (uploadBean.getTravelersCheck() != null
				&& uploadBean.getTravelersCheck().length() > 0) {
			String travelersCheck = uploadBean.getTravelersCheck();
			errorMsg.append(validate(travelersCheck, 1, "equal",
					"TravelersCheck", LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(travelersCheck)) {
				errorMsg.append("Use only Y or N or Blank for TravelersCheck , ");
			}
		}
		if (uploadBean.getFinancing() != null
				&& uploadBean.getFinancing().length() > 0) {
			String financing = uploadBean.getFinancing();
			errorMsg.append(validate(financing, 1, "equal", "Financing",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(financing)) {
				errorMsg.append("Use only Y or N or Blank for Financing , ");
			}
		}
		if (uploadBean.getGoogleCheckout() != null
				&& uploadBean.getGoogleCheckout().length() > 0) {
			String googleCheckout = uploadBean.getGoogleCheckout();
			errorMsg.append(validate(googleCheckout, 1, "equal",
					"GoogleCheckout", LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(googleCheckout)) {
				errorMsg.append("Use only Y or N or Blank for GoogleCheckout , ");
			}
		}
		if (uploadBean.getInvoice() != null
				&& uploadBean.getInvoice().length() > 0) {
			String invoice = uploadBean.getInvoice();
			errorMsg.append(validate(invoice, 1, "equal", "Invoice",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(invoice)) {
				errorMsg.append("Use only Y or N or Blank for Invoice , ");
			}
		}
		if (uploadBean.getPayPal() != null
				&& uploadBean.getPayPal().length() > 0) {
			String payPal = uploadBean.getPayPal();
			errorMsg.append(validate(payPal, 1, "equal", "PayPal",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(payPal)) {
				errorMsg.append("Use only Y or N or Blank for PayPal , ");
			}
		}

		if (uploadBean.getCouponLink() != null
				&& uploadBean.getCouponLink().length() > 0) {
			String cLink = uploadBean.getCouponLink();
			errorMsg.append(validate(cLink, 256, null, "CouponLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));

		}
		if (uploadBean.getTwitterLink() != null
				&& uploadBean.getTwitterLink().length() > 0) {
			String tLink = uploadBean.getTwitterLink();
			errorMsg.append(validate(tLink, 256, null, "TwitterLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(tLink, "twitter"));
		}

		if (uploadBean.getLinkedInLink() != null
				&& uploadBean.getLinkedInLink().length() > 0) {
			String linkedInLink = uploadBean.getLinkedInLink();
			errorMsg.append(validate(linkedInLink, 256, null, "LinkedInLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(linkedInLink, "linkedin"));
		}
		if (uploadBean.getFacebookLink() != null
				&& uploadBean.getFacebookLink().length() > 0) {
			String facebookLink = uploadBean.getFacebookLink();
			errorMsg.append(validate(facebookLink, 256, null, "FacebookLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(facebookLink, "facebook"));
		}

		if (uploadBean.getAlternateSocialLink() != null
				&& uploadBean.getAlternateSocialLink().length() > 0) {
			String alternateSocialLink = uploadBean.getAlternateSocialLink();
			errorMsg.append(validate(alternateSocialLink, 256, null,
					"AlternateSocialLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(alternateSocialLink,
					"foursquare"));
		}
		if (uploadBean.getYouTubeOrVideoLink() != null
				&& uploadBean.getYouTubeOrVideoLink().length() > 0) {
			String youTubeOrVideoLink = uploadBean.getYouTubeOrVideoLink();
			errorMsg.append(validate(youTubeOrVideoLink, 256, null,
					"YouTubeOrVideoLink", LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(youTubeOrVideoLink, "youtube"));
		}
		if (uploadBean.getGooglePlusLink() != null
				&& uploadBean.getGooglePlusLink().length() > 0) {
			String gPlusLink = uploadBean.getGooglePlusLink();
			errorMsg.append(validate(gPlusLink, 256, null, "GooglePlusLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(gPlusLink, "plus.google"));
		}
		if (uploadBean.getMyspaceLink() != null
				&& uploadBean.getMyspaceLink().length() > 0) {
			String myspaceLink = uploadBean.getMyspaceLink();
			errorMsg.append(validate(myspaceLink, 256, null, "MyspaceLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(myspaceLink, "myspace"));
		}
		if (uploadBean.getPinteristLink() != null
				&& uploadBean.getPinteristLink().length() > 0) {
			String pinteristLink = uploadBean.getPinteristLink();
			errorMsg.append(validate(pinteristLink, 256, null, "PinteristLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(pinteristLink, "pinterest"));
		}

		if (uploadBean.getHelpLink() != null
				&& uploadBean.getHelpLink().length() > 0) {
			String helpLink = uploadBean.getHelpLink();
			errorMsg.append(validate(helpLink, 256, null, "helpLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(helpLink, "help"));
		}

		if (uploadBean.getProducts() != null
				&& uploadBean.getProducts().length() > 0) {
			String products = uploadBean.getProducts();
			errorMsg.append(lengthValidation(products, 256, null, "Products"));
			errorMsg.append(validate(products.split(","), 256, null,
					"Products", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getServices() != null
				&& uploadBean.getServices().length() > 0) {
			String services = uploadBean.getServices();
			errorMsg.append(lengthValidation(services, 256, null, "Services"));
			errorMsg.append(validate(services.split(","), 256, null,
					"Services", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getProductsOrServices_combined() != null
				&& uploadBean.getProductsOrServices_combined().length() > 0) {
			String productsOrServices = uploadBean
					.getProductsOrServices_combined();
			errorMsg.append(lengthValidation(productsOrServices, 256, null,
					"ProductsOrServices_combined"));
			errorMsg.append(validate(productsOrServices.split(","), 256, null,
					"ProductsOrServices_combined",
					LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getBrands() != null
				&& uploadBean.getBrands().length() > 0) {
			String brands = uploadBean.getBrands();
			errorMsg.append(lengthValidation(brands, 256, null, "Brands"));
			errorMsg.append(validate(brands.split(","), 256, null, "Brands",
					LBLConstants.FIELD_TYPE_ALPHA));
			boolean validBrands = true;
			for (String brand : brands.split(",")) {
				if (!service.validBrand(brand)) {
					validBrands = false;
					break;
				}
			}
			if (!validBrands) {
				errorMsg.append("Brands are not valid , ");
			}
		}

		if (uploadBean.getKeywords() != null
				&& uploadBean.getKeywords().length() > 0) {
			String keywords = uploadBean.getKeywords();
			errorMsg.append(lengthValidation(keywords, 256, null, "Keywords"));
			errorMsg.append(validate(keywords.split(","), 256, null,
					"Keywords", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getLanguages() != null
				&& uploadBean.getLanguages().length() > 0) {
			String languages = uploadBean.getLanguages();
			errorMsg.append(lengthValidation(languages, 256, null, "Languages"));
			errorMsg.append(validate(languages.split(","), 256, null,
					"Languages", LBLConstants.FIELD_TYPE_ALPHA));
			if(languages!=null && languages.length()>255){
				uploadBean.setLanguages(languages.substring(0,255));
			}
		}
		String yearEstablished = uploadBean.getYearEstablished();
		if (yearEstablished != null && yearEstablished.length() > 0) {
			errorMsg.append(validate(yearEstablished, 4, "equal",
					"YearEstablished", LBLConstants.FIELD_TYPE_NUMERIC));
			if(!errorMsg.toString().contains("YearEstablished")){
				if (!validateYear(yearEstablished)) {
					errorMsg.append("year must be after 1492 , ");
				}
			}

		}

		if (uploadBean.getTagline() != null
				&& uploadBean.getTagline().length() > 0) {
			errorMsg.append(validate(uploadBean.getTagline(), 150, null,
					"Tagline", LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}

		String businessDescription = uploadBean.getBusinessDescription();
		if (businessDescription != null && businessDescription.length() > 0) {
			errorMsg.append(validate(businessDescription, 250, "minimum",
					"BusinessDescription",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(lengthValidation(businessDescription, 4000, null,
					"BusinessDescription"));
		} else {
			errorMsg.append(fieldRequired("BusinessDescription"));
		}
		String businessDescriptionShort = uploadBean
				.getBusinessDescriptionShort();
		if (businessDescriptionShort != null
				&& businessDescriptionShort.length() > 0) {
			errorMsg.append(validate(businessDescriptionShort, 1, "minimum",
					"ShortDescription", LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(lengthValidation(businessDescriptionShort, 200,
					null, "ShortDescription"));
		} else {
			errorMsg.append(fieldRequired("ShortDescription"));
		}

		String addressprivacyflag = uploadBean.getADDRESSPRIVACYFLAG();
		if (addressprivacyflag != null && addressprivacyflag != ""
				&& addressprivacyflag.length() > 0) {
			errorMsg.append(validate(addressprivacyflag, 1, "equal",
					"Addressprivacyflag", LBLConstants.FIELD_TYPE_ALPHA));
			if (!addressprivacyflag.equalsIgnoreCase("Y")) {
				errorMsg.append("Addressprivacyflag must either be blank or contain Y only. , ");
			}
		}

		/*
		 * if (LBLConstants.SMARTYSTREETS_CHECK.equals("true")) { boolean
		 * isAddressValid = AddressValidationUtill
		 * .validateAddressWithSS(uploadBean);
		 * 
		 * if (!isAddressValid) { errorMsg.append(
		 * "LocationAddress Verification is failed. Kindly verify the address info, "
		 * ); } }
		 */

		return errorMsg;
	}

	public boolean validateYear(String yearEstablished) {
		try {
			Integer year = Integer.valueOf(yearEstablished);
			if (year <= LBLConstants.YEAR_ESTABLISHED) {
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("There was error while validating Year: ",e);
			return false;
		}
		
	}

	/**
	 * upload data socialLink Validation
	 * 
	 * @param tLink
	 * @param str
	 * @return
	 */
	public String socialLinkValidation(String tLink, String str) {
		String linkEndStr = str + ".com";
		if (tLink.indexOf(linkEndStr) > -1) {
			return "";
		}
		return str + " Link must contain " + linkEndStr + " , ";

	}

	/**
	 * upload data validation PaymentMethod
	 * 
	 * @param pMethod
	 * @return
	 */
	public boolean validPaymentMethod(String pMethod) {
		boolean flag = false;
		if (pMethod.equalsIgnoreCase("Y")) {
			flag = true;
		} else if (pMethod.equalsIgnoreCase("N")) {
			flag = true;
		} else if (pMethod == "") {
			flag = true;
		}
		return flag;
	}

	/**
	 * getTime
	 * 
	 * @param mOpen
	 * @return
	 */
	public String getTime(String mOpen) {
		String time = "";
		if (mOpen.equalsIgnoreCase("CLOSE")) {
			return mOpen;
		}
		if (mOpen.indexOf(":") == 2) {
			String[] timeArr = mOpen.split(":");
			int hours = Integer.parseInt(timeArr[0]);
			int mins = Integer.parseInt(timeArr[1]);
			if (hours > 0 && hours <= 12 && mins >= 0 && mins < 60) {
				if (hours - 12 > 0) {
					time = hours - 12 + ":" + mins + "PM";
				} else {
					time = mOpen + "AM";
				}
			}
		}
		if(time.length()==4){
			time = "0"+time;
		}
		return time;
	}

	/**
	 * validate
	 * 
	 * @param values
	 * @param validLength
	 * @param lengthType
	 * @param fieldName
	 * @param fieldType
	 * @return
	 */
	public String validate(String[] values, int validLength, String lengthType,
			String fieldName, String fieldType) {
		String msg = "";
		for (String title : values) {
			String errMsg = validate(title, validLength, lengthType, fieldName,
					fieldType);
			if (errMsg != "" && msg != "") {
				msg += errMsg;
			}
		}
		return msg;
	}

	/**
	 * validTitles
	 * 
	 * @param titles
	 * @param list
	 * @return
	 */
	public boolean validTitles(String titles, List<String> list) {
		if (!titles.contains(",")) {
			if (!list.contains(titles)) {
				return false;
			}
		} else {
			for (String title : titles.split(",")) {
				if (!list.contains(title)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean endsWith(String webAddress, String[] webAddressEndsWith) {
		for (String str : webAddressEndsWith) {
			if (webAddress.endsWith(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * validate Email
	 * 
	 * @param email
	 * @return
	 */
	private boolean validEmail(String email) {
		int indexOf = email.indexOf("@");
		if (indexOf > 0 || (email.indexOf(".") > (indexOf + 1))) {
			return true;
		}
		return false;
	}

	public int getZipCodeLength(String countryCode) {
		int length = 5;
		if (countryCode != null) {
			if (countryCode.equalsIgnoreCase("CA")) {
				length = 6;
			}
		}
		return length;
	}

	public boolean isStartsWithNumber(String locationAddress) {
		return Character.isDigit(locationAddress.charAt(0));
	}

	/**
	 * copyBeanDetailsToErrorBean
	 * 
	 * @param errorMsg
	 * @param bean
	 * @return
	 */
	public LblErrorBean copyBeanDetailsToErrorBean(String errorMsg,
			UploadBusinessBean bean) {
		LblErrorBean errorBean = new LblErrorBean();
		// add logic to set all properties
		BeanUtils.copyProperties(bean, errorBean);
		errorBean.setErrorMessage(errorMsg);
		return errorBean;
	}

	/**
	 * validate
	 * 
	 * @param value
	 * @param validLength
	 * @param lengthType
	 * @param fieldName
	 * @param fieldType
	 * @return
	 */
	public String validate(String value, int validLength, String lengthType,
			String fieldName, String fieldType) {
		String errorMsg = "";
		errorMsg += lengthValidation(value, validLength, lengthType, fieldName);
		errorMsg += fieldTypeValidation(value, fieldName, fieldType);
		return errorMsg;
	}

	/**
	 * fieldTypeValidation
	 * 
	 * @param value
	 * @param fieldName
	 * @param fieldType
	 * @return
	 */
	private String fieldTypeValidation(String value, String fieldName,
			String fieldType) {
		String errorMsg = "";
		value = value.replaceAll(" ", "");
		if (fieldType.equalsIgnoreCase("AN")) {
			if (isAlphanumeric(value)) {
				errorMsg += fieldName + " must be Alphanumeric , ";
			}
		} else if (fieldType.equalsIgnoreCase("A")) {
			if (!isAlphaValue(value)) {
				errorMsg += fieldName + " must be Alpha Value , ";
			}
		}
		if (fieldType.equalsIgnoreCase("N")) {
			if (!isNumericValue(value)) {
				errorMsg += fieldName + " must be Numeric Value , ";
			}
		}
		return errorMsg;
	}

	/**
	 * lengthValidation
	 * 
	 * @param value
	 * @param validLength
	 * @param lengthType
	 * @param fieldName
	 * @return
	 */
	private String lengthValidation(String value, int validLength,
			String lengthType, String fieldName) {
		String errorMsg = "";
		if (!verifyLength(value, validLength, lengthType)) {
			if (lengthType == null) {
				errorMsg += fieldName
						+ " Length must be <br/> less than or equal to "
						+ validLength + " , ";
			} else if (lengthType.equalsIgnoreCase("equal")) {
				errorMsg += fieldName + " Length must be " + validLength
						+ " , ";
			} else {
				errorMsg += fieldName + " Length must be minimum "
						+ validLength + " , ";
			}

		}
		return errorMsg;
	}

	public boolean verifyLength(String store, int lenght, String str) {
		if (str == null) {
			return store.length() <= lenght;
		}
		if (str.equalsIgnoreCase("equal")) {
			return store.length() == lenght;
		}
		return store.length() >= lenght;
	}

	public boolean isAlphanumeric(String str) {
		return (str.matches("[A-Za-z0-9]+") && isAlphaValue(str) && isNumericValue(str));
	}

	public boolean isAlphaValue(String str) {
		return str.matches("[A-Za-z]+");
	}

	public boolean isNumericValue(String str) {
		return str.matches("[0-9]+");
	}

	public String fieldRequired(String fieldName) {
		return (fieldName + " required , ");
	}

	public boolean containsValidData(String data,
			List<String> nonLocationAddressData) {
		int count = 0;
		for (String str : nonLocationAddressData) {
			str=str.trim();
			if (data.indexOf(str) > -1)
				++count;
		}
		if (count > 0) {
			return false;
		}
		return true;
	}
	public boolean conatinsString(String data,
			List<String> nonLocationAddressData) {
		int count = 0;
		for (String str : nonLocationAddressData) {
			str=str.trim();
			if (data.contains(str))
				++count;
		}
		if (count > 0) {
			return false;
		}
		return true;
	}
	/**
	 * validateUploadBean
	 * 
	 * @param service
	 * @param uploadBean
	 * @return
	 */
	public StringBuffer validateBusinessListInfo(BusinessService service,
			UploadBusinessBean uploadBean) {
		StringBuffer errorMsg = new StringBuffer();

		Integer clientId = uploadBean.getClientId();
		if (clientId != null) {
			String validate = validate(clientId.toString(), 5, null,
					"clientId", LBLConstants.FIELD_TYPE_NUMERIC);
			if (validate.length() == 0) {
				String brand = service.getBrandByClientId(clientId);
				if (brand != null && brand.length() > 0) {
					uploadBean.setClient(brand);
				} else {
					errorMsg.append("Invalid clientId ,");
				}
			}
			errorMsg.append(validate);

		} else {
			errorMsg.append(fieldRequired("ClientID"));
		}

		if (uploadBean.getStore() != null && uploadBean.getStore().length() > 0) {
			String store = uploadBean.getStore();

			String client = uploadBean.getClient();
			/*
			 * if (service.isStoreUnique(store,client)) {
			 * errorMsg.append("Store already Exist , "); }
			 */
			errorMsg.append(validate(store, 50, null, "Store",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));

		} else {
			errorMsg.append(fieldRequired("Store"));
		}
		if (uploadBean.getActionCode() != null
				&& uploadBean.getActionCode().length() > 0) {
			String actionCode = uploadBean.getActionCode();
			if (!LBLConstants.ACTION_CODES.contains(actionCode)) {
				errorMsg.append("ActionCode must be A or U or R or D , ");
			}
			errorMsg.append(validate(actionCode, 1, "equal", "ActionCode",
					LBLConstants.FIELD_TYPE_ALPHA));

		} else {
			errorMsg.append(fieldRequired("ActionCode"));
		}

		if (uploadBean.getCountryCode() != null
				&& uploadBean.getCountryCode().length() > 0) {
			String countryCode = uploadBean.getCountryCode();
			if (!LBLConstants.COUNTRY_CODES.contains(countryCode)) {
				errorMsg.append("CountryCode must be US or CA , ");
			}
			errorMsg.append(validate(countryCode, 2, "equal", "CountryCode",
					LBLConstants.FIELD_TYPE_ALPHA));
		} else {
			errorMsg.append(fieldRequired("CountryCode"));
		}
		if (uploadBean.getCompanyName() != null
				&& uploadBean.getCompanyName().length() > 0) {
			String companyName = uploadBean.getCompanyName();
			errorMsg.append(validate(companyName, 100, null, "CompanyName", ""));
		} else {
			errorMsg.append(fieldRequired("CompanyName"));
		}
		if (uploadBean.getAlternativeName() != null
				&& uploadBean.getAlternativeName().length() > 0) {
			errorMsg.append(validate(uploadBean.getAlternativeName(), 256,
					null, "AlternativeName",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}
		if (uploadBean.getAnchorOrHostBusiness() != null
				&& uploadBean.getAnchorOrHostBusiness().length() > 0) {
			errorMsg.append(validate(uploadBean.getAnchorOrHostBusiness(), 256,
					null, "AnchorOrHostBusiness",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}

		String locationState = uploadBean.getLocationState();
		if (uploadBean.getLocationAddress() != null
				&& uploadBean.getLocationAddress().length() > 0) {
			String locationAddress = uploadBean.getLocationAddress();
			errorMsg.append(validate(locationAddress, 68, null,
					"LocationAddress", LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			if (!conatinsString(locationAddress,
					LBLConstants.NON_LOCATION_ADDRESS_DATA)) {
				errorMsg.append("LocationAddress Cannot contain PO Box or P.O. Box or P O Box or P. O. Box or P.O.BOX or PO BOX, ");
			}
			if (containsParenthesis(locationAddress)) {
				errorMsg.append("LocationAddress cannot conatins Parenthesis, ");
			}
			if (!isValidLocationAdress(locationAddress)
					&& !locationState.equalsIgnoreCase("MI")) {
				errorMsg.append("LocationAddress Must begin with street number (or) <br/> N or S or E or W followed by number, ");
			}
			if (locationState.equalsIgnoreCase("MI")) {
				if (!isValidMIAddress(locationAddress)) {
					errorMsg.append("LocationAddress Must begin with street number (or) <br/> G, ");
				}
			}
			if (containsNumeric(locationAddress)) {
				errorMsg.append("LocationAddress cannot be only number.<br/> Please resubmit valid address, ");
			}
			
			if (locationAddress.contains("&")) {
				errorMsg.append("Invalid LocationAddress, ");
			}
			if (locationAddress.contains("()")) {
				errorMsg.append("Invalid LocationAddress, ");
			}

		} else {
			errorMsg.append(fieldRequired("LocationAddress"));
		}

		if (uploadBean.getSuite() != null && uploadBean.getSuite().length() > 0) {
			String suite = uploadBean.getSuite();
			errorMsg.append(validate(suite, 20, null, "Suite",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			if (!containsValidData(suite, LBLConstants.NON_SUITE_DATA)) {
				errorMsg.append("Enter Suite Without Fl,Ste,Rm,#, <br/> Floor,Suite,Room,Unit,Bldg,<br/> PO Box,P.O. Box or P O Box  , ");
			}
		}

		if (uploadBean.getLocationCity() != null
				&& uploadBean.getLocationCity().length() > 0) {
			errorMsg.append(validate(uploadBean.getLocationCity(), 50, null,
					"LocationCity", LBLConstants.FIELD_TYPE_ALPHA));
		} else {
			errorMsg.append(fieldRequired("LocationCity"));
		}
		if (uploadBean.getLocationState() != null
				&& uploadBean.getLocationState().length() > 0) {
			String state = uploadBean.getLocationState();
			String countryCode = uploadBean.getCountryCode();
			if (!service.isValidState(state, countryCode)) {
				errorMsg.append("Invalid LocationState For the entered Country , ");
			}
			errorMsg.append(validate(state, 2, "equal", "LocationState",
					LBLConstants.FIELD_TYPE_ALPHA));
		} else {
			errorMsg.append(fieldRequired("LocationState"));
		}
		if (uploadBean.getLocationZipCode() != null
				&& uploadBean.getLocationZipCode().length() > 0) {
			String locationZipCode = uploadBean.getLocationZipCode();

			if ("CA".equalsIgnoreCase(uploadBean.getCountryCode())) {
				errorMsg.append(validate(locationZipCode,
						getZipCodeLength(uploadBean.getCountryCode()), "equal",
						"LocationZipCode",
						LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			} else {
				errorMsg.append(validate(locationZipCode,
						getZipCodeLength(uploadBean.getCountryCode()), "equal",
						"LocationZipCode", LBLConstants.FIELD_TYPE_NUMERIC));
			}
		} else {
			errorMsg.append(fieldRequired("LocationZipCode"));
		}

		if (uploadBean.getLocationPhone() != null
				&& uploadBean.getLocationPhone().length() > 0) {
			String locationPhone = uploadBean.getLocationPhone();
			errorMsg.append(validate(locationPhone, 10, "equal",
					"LocationPhone", LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getLocationPhone() != null
				&& (uploadBean.getLocationPhone().length() == 10)) {
			String locationphone = uploadBean.getLocationPhone();

			String substring = locationphone.substring(0, 3);
			String phone = locationphone.substring(3, 6);
			if (substring.equals("555")) {
				errorMsg.append("Invalid LocationPhone, ");
			}
			if (phone.equals("555")) {
				errorMsg.append("Invalid LocationPhone, ");
			}
		} else {
			errorMsg.append(fieldRequired("LocationPhone"));
		}
		if (uploadBean.getFax() != null && uploadBean.getFax().length() > 0) {
			String fax = uploadBean.getFax();
			errorMsg.append(validate(fax, 10, "equal", "Fax",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getTollFree() != null
				&& uploadBean.getTollFree().length() > 0) {
			String tollFree = uploadBean.getTollFree();
			errorMsg.append(validate(tollFree, 10, "equal", "TollFree",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getTty() != null && uploadBean.getTty().length() > 0) {
			String tty = uploadBean.getTty();
			errorMsg.append(validate(tty, 10, "equal", "Tty",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getMobileNumber() != null
				&& uploadBean.getMobileNumber().length() > 0) {
			String mobileNumber = uploadBean.getMobileNumber();
			errorMsg.append(validate(mobileNumber, 10, "equal", "MobileNumber",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getAdditionalNumber() != null
				&& uploadBean.getAdditionalNumber().length() > 0) {
			errorMsg.append(validate(uploadBean.getAdditionalNumber(), 10,
					"equal", "AddtionalNumber", LBLConstants.FIELD_TYPE_NUMERIC));
		}
		if (uploadBean.getLocationEmail() != null
				&& uploadBean.getLocationEmail().length() > 0) {
			String locationEmail = uploadBean.getLocationEmail();
			if (!validEmail(locationEmail)) {
				errorMsg.append("Invalid LocationEmail , ");
			}
			errorMsg.append(validate(locationEmail, 256, null, "LocationEmail",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}

		if (uploadBean.getShortWebAddress() != null
				&& uploadBean.getShortWebAddress().length() > 0) {
			String sWebAddress = uploadBean.getShortWebAddress();
			if (sWebAddress.startsWith("http://")
					|| sWebAddress.startsWith("https://")) {
				errorMsg.append("ShortWebAddress should not starts with http:// or https:// , ");
			}
			/*
			 * if (!endsWith(sWebAddress,LBLConstants.WEB_ADDRESS_ENDS_WITH)) {
			 * errorMsg
			 * .append("ShortWebAddress must ends with .com, .net or .org , ");
			 * }
			 */
			errorMsg.append(validate(sWebAddress, 40, null, "ShortWebAddress",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}
		if (uploadBean.getWebAddress() != null
				&& uploadBean.getWebAddress().length() > 0) {
			String webAddress = uploadBean.getWebAddress();
			if (!webAddress.startsWith("http")){
				errorMsg.append("longWebAddress should starts with http:// or https:// , ");
			}
			/*
			 * if (!endsWith(webAddress,LBLConstants.WEB_ADDRESS_ENDS_WITH)) {
			 * errorMsg
			 * .append("WebAddress must ends with .com, .net or .org , "); }
			 */
			errorMsg.append(validate(webAddress, 256, null, "longWebAddress",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}

		if (uploadBean.getCategory1() != null
				&& uploadBean.getCategory1().length() > 0) {
			String category1 = uploadBean.getCategory1();
			if (!service.isCatagoryExist(category1)) {
				errorMsg.append("Category1 not exist/Invalid category1 , ");
			}
			errorMsg.append(validate(category1, 6, "equal", "Category1",
					LBLConstants.FIELD_TYPE_NUMERIC));
		} else {
			errorMsg.append(fieldRequired("Category1"));
		}
		if (uploadBean.getCategory2() != null
				&& uploadBean.getCategory2().length() > 0) {
			String category2 = uploadBean.getCategory2();
			if (!service.isCatagoryExist(category2)) {
				errorMsg.append("Category2 not exist/Invalid category2 , ");
			}
			errorMsg.append(validate(category2, 6, "equal", "Category2",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}

		if (uploadBean.getCategory3() != null
				&& uploadBean.getCategory3().length() > 0) {
			String category3 = uploadBean.getCategory3();
			if (!service.isCatagoryExist(category3)) {
				errorMsg.append("Category3 not exist/Invalid category3 , ");
			}
			errorMsg.append(validate(category3, 6, "equal", "Category3",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}

		if (uploadBean.getCategory4() != null
				&& uploadBean.getCategory4().length() > 0) {
			String category4 = uploadBean.getCategory4();
			if (!service.isCatagoryExist(category4)) {
				errorMsg.append("Category4 not exist/Invalid Category4 , ");
			}
			errorMsg.append(validate(category4, 6, "equal", "Category4",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}

		if (uploadBean.getCategory5() != null
				&& uploadBean.getCategory5().length() > 0) {
			String category5 = uploadBean.getCategory5();
			if (!service.isCatagoryExist(category5)) {
				errorMsg.append("Category5 not exist/Invalid category5 , ");
			}
			errorMsg.append(validate(category5, 6, "equal", "Category5",
					LBLConstants.FIELD_TYPE_NUMERIC));
		}

		if (uploadBean.getLogoLink() != null
				&& uploadBean.getLogoLink().length() > 0) {
			String logoLink = uploadBean.getLogoLink();
			errorMsg.append(validate(logoLink, 128, null, "LogoLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}
		if (uploadBean.getServiceArea() != null
				&& uploadBean.getServiceArea().length() > 0) {
			String sArea = uploadBean.getServiceArea();
			errorMsg.append(validate(sArea, 256, null, "ServiceArea",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}
		if (uploadBean.getPrimaryContactFirstName() != null
				&& uploadBean.getPrimaryContactFirstName().length() > 0) {
			String pCFName = uploadBean.getPrimaryContactFirstName();
			errorMsg.append(validate(pCFName, 20, null,
					"PrimaryContactFirstName", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getPrimaryContactLastName() != null
				&& uploadBean.getPrimaryContactLastName().length() > 0) {
			String pCLName = uploadBean.getPrimaryContactLastName();
			errorMsg.append(validate(pCLName, 20, null,
					"PrimaryContactLastName", LBLConstants.FIELD_TYPE_ALPHA));
		}

		if (uploadBean.getContactTitle() != null
				&& uploadBean.getContactTitle().length() > 0) {
			String cTitle = uploadBean.getContactTitle();
			errorMsg.append(validate(cTitle, 20, null, "ContactTitle",
					LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getContactEmail() != null
				&& uploadBean.getContactEmail().length() > 0) {
			String cEmail = uploadBean.getContactEmail();
			errorMsg.append(validate(cEmail, 256, null, "ContactEmail",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}
		if (uploadBean.getLocationEmployeeSize() != null
				&& uploadBean.getLocationEmployeeSize().length() > 0) {
			String locationEmployeeSize = uploadBean.getLocationEmployeeSize();
			errorMsg.append(validate(locationEmployeeSize, 5, null,
					"LocationEmployeeSize", LBLConstants.FIELD_TYPE_NUMERIC));
		}

		if (uploadBean.getTitle_ManagerOrOwner() != null
				&& uploadBean.getTitle_ManagerOrOwner().length() > 0) {
			String tManagerOrOwner = uploadBean.getTitle_ManagerOrOwner();
			if (!validTitles(tManagerOrOwner,
					LBLConstants.TITLE_MANAGER_OR_OWNER)) {
				errorMsg.append("For Title_ManagerOrOwner Only options available:"
						+ " CEO or Owner or President or EVP or SVP or VP or Plant Manager or Office Manager. , ");
			}
			errorMsg.append(validate(tManagerOrOwner.split(","), 50, null,
					"Title_ManagerOrOwner", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getProfessionalTitle() != null
				&& uploadBean.getProfessionalTitle().length() > 0) {
			String pTitle = uploadBean.getProfessionalTitle();
			if (!validTitles(pTitle, LBLConstants.PROFESSIONAL_TITLES)) {
				errorMsg.append("For Professional Title Only Options availableare:"
						+ " CPA or DC or DDS or DO or DPM or DVM or MD  or OD or PE or PHD. , ");
			}
			errorMsg.append(validate(pTitle.split(","), 3, null,
					"ProfessionalTitle", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getProfessionalAssociations() != null
				&& uploadBean.getProfessionalAssociations().length() > 0) {
			String pAssociations = uploadBean.getProfessionalAssociations();

			if (pAssociations.length() > 256) {
				errorMsg.append("Professional Associations should be less than 256 characterss or same, ");
			}
		}

		if (uploadBean.getMondayOpen() != null
				&& uploadBean.getMondayOpen().length() > 0) {
			String mOpen = uploadBean.getMondayOpen();
			String lengthValidation = lengthValidation(mOpen, 5, "equal",
					"MondayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(mOpen);
				if (time == "") {
					errorMsg.append("Invalid MondayOpen time , ");
				} else {
					uploadBean.setMondayOpen(time);
				}
			}
		}

		if (uploadBean.getMondayClose() != null
				&& uploadBean.getMondayClose().length() > 0) {
			String mClose = uploadBean.getMondayClose();
			String lengthValidation = lengthValidation(mClose, 5, "equal",
					"MondayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(mClose);
				if (time == "") {
					errorMsg.append("Invalid MondayClose time , ");
				} else {
					uploadBean.setMondayClose(time);
				}
			}
		}
		if (uploadBean.getTuesdayOpen() != null
				&& uploadBean.getTuesdayOpen().length() > 0) {
			String tOpen = uploadBean.getTuesdayOpen();
			String lengthValidation = lengthValidation(tOpen, 5, "equal",
					"TuesdayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(tOpen);
				if (time == "") {
					errorMsg.append("Invalid TuesdayOpen time , ");
				} else {
					uploadBean.setTuesdayOpen(time);
				}
			}
		}
		if (uploadBean.getTuesdayClose() != null
				&& uploadBean.getTuesdayClose().length() > 0) {
			String tClose = uploadBean.getTuesdayClose();
			String lengthValidation = lengthValidation(tClose, 5, "equal",
					"TuesdayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(tClose);
				if (time == "") {
					errorMsg.append("Invalid TuesdayClose time , ");
				} else {
					uploadBean.setTuesdayClose(time);
				}
			}
		}
		if (uploadBean.getWednesdayOpen() != null
				&& uploadBean.getWednesdayOpen().length() > 0) {
			String wdOpen = uploadBean.getWednesdayOpen();
			String lengthValidation = lengthValidation(wdOpen, 5, "equal",
					"WednesdayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(wdOpen);
				if (time == "") {
					errorMsg.append("Invalid WednesdayOpen time , ");
				} else {
					uploadBean.setWednesdayOpen(time);
				}
			}
		}
		if (uploadBean.getWednesdayClose() != null
				&& uploadBean.getWednesdayClose().length() > 0) {
			String wdOpen = uploadBean.getWednesdayClose();
			String lengthValidation = lengthValidation(wdOpen, 5, "equal",
					"WednesdayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(wdOpen);
				if (time == "") {
					errorMsg.append("Invalid WednesdayClose time , ");
				} else {
					uploadBean.setWednesdayClose(time);
				}
			}
		}
		if (uploadBean.getThursdayOpen() != null
				&& uploadBean.getThursdayOpen().length() > 0) {
			String thOpen = uploadBean.getThursdayOpen();
			String lengthValidation = lengthValidation(thOpen, 5, "equal",
					"ThursdayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(thOpen);
				if (time == "") {
					errorMsg.append("Invalid ThursdayOpen time , ");
				} else {
					uploadBean.setThursdayOpen(time);
				}
			}
		}
		if (uploadBean.getThursdayClose() != null
				&& uploadBean.getThursdayClose().length() > 0) {
			String thClose = uploadBean.getThursdayClose();
			String lengthValidation = lengthValidation(thClose, 5, "equal",
					"ThursdayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(thClose);
				if (time == "") {
					errorMsg.append("Invalid ThursdayClose time , ");
				} else {
					uploadBean.setThursdayClose(time);
				}
			}
		}
		if (uploadBean.getFridayOpen() != null
				&& uploadBean.getFridayOpen().length() > 0) {
			String fOpen = uploadBean.getFridayOpen();
			String lengthValidation = lengthValidation(fOpen, 5, "equal",
					"FridayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(fOpen);
				if (time == "") {
					errorMsg.append("Invalid FridayOpen time , ");
				} else {
					uploadBean.setFridayOpen(time);
				}
			}
		}

		if (uploadBean.getFridayClose() != null
				&& uploadBean.getFridayClose().length() > 0) {
			String fClose = uploadBean.getFridayClose();
			String lengthValidation = lengthValidation(fClose, 5, "equal",
					"FridayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(fClose);
				if (time == "") {
					errorMsg.append("Invalid FridayClose time , ");
				} else {
					uploadBean.setFridayClose(time);
				}
			}
		}
		if (uploadBean.getSaturdayOpen() != null
				&& uploadBean.getSaturdayOpen().length() > 0) {
			String satOpen = uploadBean.getSaturdayOpen();
			String lengthValidation = lengthValidation(satOpen, 5, "equal",
					"SaturdayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(satOpen);
				if (time == "") {
					errorMsg.append("Invalid SaturdayOpen time , ");
				} else {
					uploadBean.setSaturdayOpen(time);
				}
			}
		}
		if (uploadBean.getSaturdayClose() != null
				&& uploadBean.getSaturdayClose().length() > 0) {
			String satClose = uploadBean.getSaturdayClose();
			String lengthValidation = lengthValidation(satClose, 5, "equal",
					"SaturdayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(satClose);
				if (time == "") {
					errorMsg.append("Invalid SaturdayClose time , ");
				} else {
					uploadBean.setSaturdayClose(time);
				}
			}
		}
		if (uploadBean.getSundayOpen() != null
				&& uploadBean.getSundayOpen().length() > 0) {
			String sunOpen = uploadBean.getSundayOpen();
			String lengthValidation = lengthValidation(sunOpen, 5, "equal",
					"SundayOpen");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(sunOpen);
				if (time == "") {
					errorMsg.append("Invalid SundayOpen time , ");
				} else {
					uploadBean.setSundayOpen(time);
				}
			}
		}
		if (uploadBean.getSundayClose() != null
				&& uploadBean.getSundayClose().length() > 0) {
			String sunClose = uploadBean.getSundayClose();
			String lengthValidation = lengthValidation(sunClose, 5, "equal",
					"SundayClose");
			errorMsg.append(lengthValidation);
			if (lengthValidation != "") {
				String time = getTime(sunClose);
				if (time == "") {
					errorMsg.append("Invalid SundayClose time , ");
				} else {
					uploadBean.setSundayClose(time);
				}
			}
		}

		if (uploadBean.getaMEX() != null && uploadBean.getaMEX().length() > 0) {
			String aMex = uploadBean.getaMEX();
			errorMsg.append(validate(aMex, 1, "equal", "AMEX",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(aMex)) {
				errorMsg.append("Use only Y or N or Blank for AMEX , ");
			}
		}

		if (uploadBean.getDiscover() != null
				&& uploadBean.getDiscover().length() > 0) {
			String discover = uploadBean.getDiscover();
			errorMsg.append(validate(discover, 1, "equal", "Discover",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(discover)) {
				errorMsg.append("Use only Y or N or Blank for Discover , ");
			}
		}
		if (uploadBean.getVisa() != null && uploadBean.getVisa().length() > 0) {
			String visa = uploadBean.getVisa();
			errorMsg.append(validate(visa, 1, "equal", "Visa",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(visa)) {
				errorMsg.append("Use only Y or N or Blank for Visa , ");
			}
		}
		if (uploadBean.getMasterCard() != null
				&& uploadBean.getMasterCard().length() > 0) {
			String masterCard = uploadBean.getMasterCard();
			errorMsg.append(validate(masterCard, 1, "equal", "MasterCard",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(masterCard)) {
				errorMsg.append("Use only Y or N or Blank for MasterCard , ");
			}
		}
		if (uploadBean.getDinersClub() != null
				&& uploadBean.getDinersClub().length() > 0) {
			String dinersClub = uploadBean.getDinersClub();
			errorMsg.append(validate(dinersClub, 1, "equal", "DinersClub",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(dinersClub)) {
				errorMsg.append("Use only Y or N or Blank for DinersClub , ");
			}
		}
		if (uploadBean.getDebitCard() != null
				&& uploadBean.getDebitCard().length() > 0) {
			String debitCard = uploadBean.getDebitCard();
			errorMsg.append(validate(debitCard, 1, "equal", "DebitCard",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(debitCard)) {
				errorMsg.append("Use only Y or N or Blank for DebitCard , ");
			}
		}
		if (uploadBean.getStoreCard() != null
				&& uploadBean.getStoreCard().length() > 0) {
			String storeCard = uploadBean.getStoreCard();
			errorMsg.append(validate(storeCard, 1, "equal", "StoreCard",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(storeCard)) {
				errorMsg.append("Use only Y or N or Blank for StoreCard , ");
			}
		}
		if (uploadBean.getOtherCard() != null
				&& uploadBean.getOtherCard().length() > 0) {
			String otherCard = uploadBean.getOtherCard();
			errorMsg.append(validate(otherCard, 1, "equal", "OtherCard",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(otherCard)) {
				errorMsg.append("Use only Y or N or Blank for OtherCard , ");
			}
		}
		if (uploadBean.getCash() != null && uploadBean.getCash().length() > 0) {
			String cash = uploadBean.getCash();
			errorMsg.append(validate(cash, 1, "equal", "Cash",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(cash)) {
				errorMsg.append("Use only Y or N or Blank for Cash , ");
			}
		}
		if (uploadBean.getCheck() != null && uploadBean.getCheck().length() > 0) {
			String check = uploadBean.getCheck();
			errorMsg.append(validate(check, 1, "equal", "Check",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(check)) {
				errorMsg.append("Use only Y or N or Blank for Check , ");
			}
		}
		if (uploadBean.getTravelersCheck() != null
				&& uploadBean.getTravelersCheck().length() > 0) {
			String travelersCheck = uploadBean.getTravelersCheck();
			errorMsg.append(validate(travelersCheck, 1, "equal",
					"TravelersCheck", LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(travelersCheck)) {
				errorMsg.append("Use only Y or N or Blank for TravelersCheck , ");
			}
		}
		if (uploadBean.getFinancing() != null
				&& uploadBean.getFinancing().length() > 0) {
			String financing = uploadBean.getFinancing();
			errorMsg.append(validate(financing, 1, "equal", "Financing",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(financing)) {
				errorMsg.append("Use only Y or N or Blank for Financing , ");
			}
		}
		if (uploadBean.getGoogleCheckout() != null
				&& uploadBean.getGoogleCheckout().length() > 0) {
			String googleCheckout = uploadBean.getGoogleCheckout();
			errorMsg.append(validate(googleCheckout, 1, "equal",
					"GoogleCheckout", LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(googleCheckout)) {
				errorMsg.append("Use only Y or N or Blank for GoogleCheckout , ");
			}
		}
		if (uploadBean.getInvoice() != null
				&& uploadBean.getInvoice().length() > 0) {
			String invoice = uploadBean.getInvoice();
			errorMsg.append(validate(invoice, 1, "equal", "Invoice",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(invoice)) {
				errorMsg.append("Use only Y or N or Blank for Invoice , ");
			}
		}
		if (uploadBean.getPayPal() != null
				&& uploadBean.getPayPal().length() > 0) {
			String payPal = uploadBean.getPayPal();
			errorMsg.append(validate(payPal, 1, "equal", "PayPal",
					LBLConstants.FIELD_TYPE_ALPHA));
			if (!validPaymentMethod(payPal)) {
				errorMsg.append("Use only Y or N or Blank for PayPal , ");
			}
		}

		if (uploadBean.getCouponLink() != null
				&& uploadBean.getCouponLink().length() > 0) {
			String cLink = uploadBean.getCouponLink();
			errorMsg.append(validate(cLink, 256, null, "CouponLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}
		if (uploadBean.getTwitterLink() != null
				&& uploadBean.getTwitterLink().length() > 0) {
			String tLink = uploadBean.getTwitterLink();
			errorMsg.append(validate(tLink, 256, null, "TwitterLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(tLink, "twitter"));
		}

		if (uploadBean.getLinkedInLink() != null
				&& uploadBean.getLinkedInLink().length() > 0) {
			String linkedInLink = uploadBean.getLinkedInLink();
			errorMsg.append(validate(linkedInLink, 256, null, "LinkedInLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(linkedInLink, "linkedin"));
		}
		if (uploadBean.getFacebookLink() != null
				&& uploadBean.getFacebookLink().length() > 0) {
			String facebookLink = uploadBean.getFacebookLink();
			errorMsg.append(validate(facebookLink, 256, null, "FacebookLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(facebookLink, "facebook"));
		}
		if (uploadBean.getInstagramLink() != null
				&& uploadBean.getInstagramLink().length() > 0) {
			String instagramLink = uploadBean.getInstagramLink();
			errorMsg.append(validate(instagramLink, 256, null, "InstagramLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(instagramLink, "instagram"));
		}
		if (uploadBean.getMenuLink() != null
				&& uploadBean.getMenuLink().length() > 0) {
			String menuLink = uploadBean.getMenuLink();
			errorMsg.append(validate(menuLink, 256, null, "MenuLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(menuLink, "menu"));
		}
		if (uploadBean.getFoursquareLink() != null
				&& uploadBean.getFoursquareLink().length() > 0) {
			String foursquareLink = uploadBean.getFoursquareLink();
			errorMsg.append(validate(foursquareLink, 256, null,
					"FoursquareLink", LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(foursquareLink, "foursquare"));
		}
		if (uploadBean.getAlternateSocialLink() != null
				&& uploadBean.getAlternateSocialLink().length() > 0) {
			String alternateSocialLink = uploadBean.getAlternateSocialLink();
			errorMsg.append(validate(alternateSocialLink, 256, null,
					"AlternateSocialLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(alternateSocialLink,
					"foursquare"));
		}
		if (uploadBean.getYouTubeOrVideoLink() != null
				&& uploadBean.getYouTubeOrVideoLink().length() > 0) {
			String youTubeOrVideoLink = uploadBean.getYouTubeOrVideoLink();
			errorMsg.append(validate(youTubeOrVideoLink, 256, null,
					"YouTubeOrVideoLink", LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(youTubeOrVideoLink, "youtube"));
		}
		if (uploadBean.getGooglePlusLink() != null
				&& uploadBean.getGooglePlusLink().length() > 0) {
			String gPlusLink = uploadBean.getGooglePlusLink();
			errorMsg.append(validate(gPlusLink, 256, null, "GooglePlusLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(gPlusLink, "plus.google"));
		}
		if (uploadBean.getMyspaceLink() != null
				&& uploadBean.getMyspaceLink().length() > 0) {
			String myspaceLink = uploadBean.getMyspaceLink();
			errorMsg.append(validate(myspaceLink, 256, null, "MyspaceLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(myspaceLink, "myspace"));
		}
		if (uploadBean.getPinteristLink() != null
				&& uploadBean.getPinteristLink().length() > 0) {
			String pinteristLink = uploadBean.getPinteristLink();
			errorMsg.append(validate(pinteristLink, 256, null, "PinteristLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(pinteristLink, "pinterest"));
		}

		if (uploadBean.getHelpLink() != null
				&& uploadBean.getHelpLink().length() > 0) {
			String helpLink = uploadBean.getHelpLink();
			errorMsg.append(validate(helpLink, 256, null, "helpLink",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(socialLinkValidation(helpLink, "help"));
		}

		if (uploadBean.getProducts() != null
				&& uploadBean.getProducts().length() > 0) {
			String products = uploadBean.getProducts();
			errorMsg.append(lengthValidation(products, 256, null, "Products"));
			errorMsg.append(validate(products.split(","), 256, null,
					"Products", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getServices() != null
				&& uploadBean.getServices().length() > 0) {
			String services = uploadBean.getServices();
			errorMsg.append(lengthValidation(services, 256, null, "Services"));
			errorMsg.append(validate(services.split(","), 256, null,
					"Services", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getProductsOrServices_combined() != null
				&& uploadBean.getProductsOrServices_combined().length() > 0) {
			String productsOrServices = uploadBean
					.getProductsOrServices_combined();
			errorMsg.append(lengthValidation(productsOrServices, 256, null,
					"ProductsOrServices_combined"));
			errorMsg.append(validate(productsOrServices.split(","), 256, null,
					"ProductsOrServices_combined",
					LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getBrands() != null
				&& uploadBean.getBrands().length() > 0) {
			String brands = uploadBean.getBrands();
			errorMsg.append(lengthValidation(brands, 256, null, "Brands"));
			errorMsg.append(validate(brands.split(","), 256, null, "Brands",
					LBLConstants.FIELD_TYPE_ALPHA));
			boolean validBrands = true;
			for (String brand : brands.split(",")) {
				if (!service.validBrand(brand)) {
					validBrands = false;
					break;
				}
			}
			if (!validBrands) {
				errorMsg.append("Brands are not valid , ");
			}
		}
		if (uploadBean.getKeywords() != null
				&& uploadBean.getKeywords().length() > 0) {
			String keywords = uploadBean.getKeywords();
			errorMsg.append(lengthValidation(keywords, 256, null, "Keywords"));
			errorMsg.append(validate(keywords.split(","), 256, null,
					"Keywords", LBLConstants.FIELD_TYPE_ALPHA));
		}
		if (uploadBean.getLanguages() != null
				&& uploadBean.getLanguages().length() > 0) {
			String languages = uploadBean.getLanguages();
			errorMsg.append(lengthValidation(languages, 256, null, "Languages"));
			errorMsg.append(validate(languages.split(","), 256, null,
					"Languages", LBLConstants.FIELD_TYPE_ALPHA));
			if(languages!=null && languages.length()>255){
				uploadBean.setLanguages(languages.substring(0,255));
			}
		}
		String yearEstablished = uploadBean.getYearEstablished();
		if (yearEstablished != null && yearEstablished.length() > 0) {
			errorMsg.append(validate(yearEstablished, 4, "equal",
					"YearEstablished", LBLConstants.FIELD_TYPE_NUMERIC));
			if(!errorMsg.toString().contains("YearEstablished")){
				if (!validateYear(yearEstablished)) {
					errorMsg.append("year must be after 1492 , ");
				}
			}
			
		}

		if (uploadBean.getTagline() != null
				&& uploadBean.getTagline().length() > 0) {
			errorMsg.append(validate(uploadBean.getTagline(), 150, null,
					"Tagline", LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
		}

		String businessDescription = uploadBean.getBusinessDescription();
		if (businessDescription != null && businessDescription.length() > 0) {
			errorMsg.append(validate(businessDescription, 250, "minimum",
					"BusinessDescription",
					LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(lengthValidation(businessDescription, 4000, null,
					"BusinessDescription"));
		} else {
			errorMsg.append(fieldRequired("BusinessDescription"));
		}
		String businessDescriptionShort = uploadBean
				.getBusinessDescriptionShort();
		if (businessDescriptionShort != null
				&& businessDescriptionShort.length() > 0) {
			errorMsg.append(validate(businessDescriptionShort, 1, "minimum",
					"ShortDescription", LBLConstants.FIELD_TYPE_ALPHA_NUMERIC));
			errorMsg.append(lengthValidation(businessDescriptionShort, 200,
					null, "ShortDescription"));
		} else {
			errorMsg.append(fieldRequired("ShortDescription"));
		}
		String addressprivacyflag = uploadBean.getADDRESSPRIVACYFLAG();
		if (addressprivacyflag != null && addressprivacyflag != ""
				&& addressprivacyflag.length() > 0) {
			errorMsg.append(validate(addressprivacyflag, 1, "equal",
					"Addressprivacyflag", LBLConstants.FIELD_TYPE_ALPHA));
			if (!addressprivacyflag.equalsIgnoreCase("Y")) {
				errorMsg.append("Addressprivacyflag must either be blank or contain Y only. , ");
			}
		}

		/*
		 * if (LBLConstants.SMARTYSTREETS_CHECK.equals("true")) { boolean
		 * isAddressValid = AddressValidationUtill
		 * .validateAddressWithSS(uploadBean);
		 * 
		 * if (!isAddressValid) { errorMsg.append(
		 * "LocationAddress Verification is failed. Kindly verify the address info, "
		 * ); } }
		 */
		return errorMsg;
	}

	private boolean containsParenthesis(String locationAddress) {
		 if(locationAddress.contains("(") || locationAddress.contains(")") || locationAddress.contains("()")){
			return true; 
		 }
		return false;
	}

	private boolean containsNumeric(String locationAddress) {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(locationAddress, pos);
		return locationAddress.length() == pos.getIndex();
	}

	public Map<String, String> getErrorsMap(List<String> errors) {
		Map<String, String> errorMap = new HashMap<String, String>();

		for (String errorMessage : errors) {
			if (StringUtils.containsIgnoreCase(errorMessage, "clientId")) {
				errorMap.put("clientId", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage,
					"businessDescription")) {
				errorMap.put("businessDescription", errorMessage);
			}
			if (StringUtils
					.containsIgnoreCase(errorMessage, "ShortDescription")) {
				errorMap.put("businessDescriptionShort", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "store")) {
				errorMap.put("store", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "companyName")) {
				errorMap.put("companyName", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "alternativeName")) {
				errorMap.put("alternativeName", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage,
					"anchorOrHostBusiness")) {
				errorMap.put("anchorOrHostBusiness", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "locationAddress")) {
				errorMap.put("locationAddress", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "suite")) {
				errorMap.put("suite", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "locationCity")) {
				errorMap.put("locationCity", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "LocationState")) {
				errorMap.put("locationState", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "locationZipCode")) {
				errorMap.put("locationZipCode", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "locationPhone")) {
				errorMap.put("locationPhone", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "fax")) {
				errorMap.put("fax", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "tollFree")) {
				errorMap.put("tollFree", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "tty")) {
				errorMap.put("tty", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "mobileNumber")) {
				errorMap.put("mobileNumber", errorMessage);
			}
			if (StringUtils
					.containsIgnoreCase(errorMessage, "additionalNumber")) {
				errorMap.put("additionalNumber", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "category1")) {
				errorMap.put("category1", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "category2")) {
				errorMap.put("category2", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "category3")) {
				errorMap.put("category3", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "category4")) {
				errorMap.put("category4", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "category5")) {
				errorMap.put("category5", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage,
					"primaryContactFirstName")) {
				errorMap.put("primaryContactFirstName", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage,
					"primaryContactLastName")) {
				errorMap.put("primaryContactLastName", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "contactTitle")) {
				errorMap.put("contactTitle", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "contactEmail")) {
				errorMap.put("contactEmail", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage,
					"locationEmployeeSize")) {
				errorMap.put("locationEmployeeSize", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage,
					"title_ManagerOrOwner")) {
				errorMap.put("title_ManagerOrOwner", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage,
					"For Professional Title Only Options availableare:")) {
				errorMap.put("professionalTitle", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "Associations")) {
				errorMap.put("professionalAssociations", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "shortWebAddress")) {
				errorMap.put("shortWebAddress", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "longWebAddress")) {
				errorMap.put("webAddress",
						errorMessage.replace("longWebAddress", "webAddress"));
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "aMEX")) {
				errorMap.put("aMEX", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "discover")) {
				errorMap.put("discover", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "visa")) {
				errorMap.put("visa", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "masterCard")) {
				errorMap.put("masterCard", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "dinersClub")) {
				errorMap.put("dinersClub", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "debitCard")) {
				errorMap.put("debitCard", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "storeCard")) {
				errorMap.put("storeCard", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "otherCard")) {
				errorMap.put("otherCard", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "cash")) {
				errorMap.put("cash", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "check")) {
				errorMap.put("check", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "travelersCheck")) {
				errorMap.put("travelersCheck", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "financing")) {
				errorMap.put("financing", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "googleCheckout")) {
				errorMap.put("googleCheckout", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "invoice")) {
				errorMap.put("invoice", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "payPal")) {
				errorMap.put("payPal", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "CouponLink")) {
				errorMap.put("couponLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "twitter")) {
				errorMap.put("twitterLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "linkedin")) {
				errorMap.put("linkedInLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "facebook")) {
				errorMap.put("facebookLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "foursquare")) {
				errorMap.put("alternateSocialLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "youtube")) {
				errorMap.put("youTubeOrVideoLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "menu")) {
				errorMap.put("menuLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "plus.google")) {
				errorMap.put("googlePlusLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "myspace")) {
				errorMap.put("myspaceLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "logoLink")) {
				errorMap.put("logoLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "pinterest")) {
				errorMap.put("pinteristLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "help")) {
				errorMap.put("helpLink", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "products")) {
				errorMap.put("products", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "services")) {
				errorMap.put("services", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage,
					"productsOrServices_combined")) {
				errorMap.put("productsOrServices_combined", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "brands")) {
				errorMap.put("brands", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "keywords")) {
				errorMap.put("keywords", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "languages")) {
				errorMap.put("languages", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "1492")) {
				errorMap.put("yearEstablished", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "countryCode")) {
				errorMap.put("countryCode", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "locationEmail")) {
				errorMap.put("locationEmail", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "serviceArea")) {
				errorMap.put("serviceArea", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "mondayOpen")) {
				errorMap.put("mondayOpen", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "mondayClose")) {
				errorMap.put("mondayClose", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "tuesdayOpen")) {
				errorMap.put("tuesdayOpen", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "tuesdayClose")) {
				errorMap.put("tuesdayClose", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "wednesdayOpen")) {
				errorMap.put("wednesdayOpen", errorMessage);
			}

			if (StringUtils.containsIgnoreCase(errorMessage, "wednesdayClose")) {
				errorMap.put("wednesdayClose", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "thursdayOpen")) {
				errorMap.put("thursdayOpen", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "thursdayClose")) {
				errorMap.put("thursdayClose", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "fridayOpen")) {
				errorMap.put("fridayOpen", errorMessage);
			}

			if (StringUtils.containsIgnoreCase(errorMessage, "fridayClose")) {
				errorMap.put("fridayClose", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "saturdayOpen")) {
				errorMap.put("saturdayOpen", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "saturdayClose")) {
				errorMap.put("saturdayClose", errorMessage);
			}

			if (StringUtils.containsIgnoreCase(errorMessage, "sundayOpen")) {
				errorMap.put("sundayOpen", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "sundayClose")) {
				errorMap.put("sundayClose", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage,
					"ADDRESSPRIVACYFLAG")) {
				errorMap.put("ADDRESSPRIVACYFLAG", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "client")) {
				errorMap.put("client", errorMessage);
			}
			if (StringUtils.containsIgnoreCase(errorMessage, "tagline")) {
				errorMap.put("tagline", errorMessage);
			}
			if (errorMap.get("clientId") != null
					|| errorMap.get("store") != null
					|| errorMap.get("companyName") != null
					|| errorMap.get("actionCode") != null
					|| errorMap.get("countryCode") != null
					|| errorMap.get("alternativeName") != null
					|| errorMap.get("anchorOrHostBusiness") != null
					|| errorMap.get("locationAddress") != null
					|| errorMap.get("suite") != null
					|| errorMap.get("locationCity") != null
					|| errorMap.get("locationState") != null
					|| errorMap.get("locationZipCode") != null
					|| errorMap.get("locationPhone") != null
					|| errorMap.get("fax") != null
					|| errorMap.get("tollFree") != null
					|| errorMap.get("tty") != null
					|| errorMap.get("mobileNumber") != null
					|| errorMap.get("additionalNumber") != null
					|| errorMap.get("category1") != null
					|| errorMap.get("category2") != null
					|| errorMap.get("category3") != null
					|| errorMap.get("category4") != null
					|| errorMap.get("category5") != null
					|| errorMap.get("shortWebAddress") != null
					|| errorMap.get("webAddress") != null
					|| errorMap.get("locationEmail") != null
					|| errorMap.get("logoLink") != null
					|| errorMap.get("serviceArea") != null) {
				errorMap.put("LocationInformation", "true");
			} else {
				errorMap.put("LocationInformation", "false");
			}
			if (errorMap.get("primaryContactFirstName") != null
					|| errorMap.get("primaryContactLastName") != null
					|| errorMap.get("contactTitle") != null
					|| errorMap.get("contactEmail") != null
					|| errorMap.get("locationEmployeeSize") != null
					|| errorMap.get("title_ManagerOrOwner") != null
					|| errorMap.get("professionalTitle") != null
					|| errorMap.get("professionalAssociations") != null) {
				errorMap.put("LocationContact", "true");
			} else {
				errorMap.put("LocationContact", "false");
			}
			if (errorMap.get("mondayOpen") != null
					|| errorMap.get("mondayClose") != null
					|| errorMap.get("tuesdayOpen") != null
					|| errorMap.get("tuesdayClose") != null
					|| errorMap.get("wednesdayOpen") != null
					|| errorMap.get("wednesdayClose") != null
					|| errorMap.get("thursdayOpen") != null
					|| errorMap.get("thursdayClose") != null
					|| errorMap.get("fridayOpen") != null
					|| errorMap.get("fridayClose") != null
					|| errorMap.get("saturdayOpen") != null
					|| errorMap.get("saturdayClose") != null
					|| errorMap.get("sundayOpen") != null
					|| errorMap.get("sundayClose") != null) {
				errorMap.put("HoursofOperation", "true");
			} else {
				errorMap.put("HoursofOperation", "false");
			}
			if (errorMap.get("aMEX") != null
					|| errorMap.get("discover") != null
					|| errorMap.get("visa") != null
					|| errorMap.get("masterCard") != null
					|| errorMap.get("dinersClub") != null
					|| errorMap.get("debitCard") != null
					|| errorMap.get("storeCard") != null
					|| errorMap.get("otherCard") != null
					|| errorMap.get("cash") != null
					|| errorMap.get("check") != null
					|| errorMap.get("travelersCheck") != null
					|| errorMap.get("financing") != null
					|| errorMap.get("googleCheckout") != null
					|| errorMap.get("invoice") != null
					|| errorMap.get("payPal") != null) {
				errorMap.put("PaymentMethods", "true");
			} else {
				errorMap.put("PaymentMethods", "false");
			}
			if (errorMap.get("couponLink") != null
					|| errorMap.get("facebookLink") != null
					|| errorMap.get("alternateSocialLink") != null
					|| errorMap.get("youTubeOrVideoLink") != null
					|| errorMap.get("googlePlusLink") != null
					|| errorMap.get("myspaceLink") != null
					|| errorMap.get("logoLink") != null
					|| errorMap.get("pinteristLink") != null
					|| errorMap.get("twitterLink") != null
					|| errorMap.get("helpLink") != null
					|| errorMap.get("menuLink") != null){
				errorMap.put("SocialLinks", "true");
			} else {
				errorMap.put("SocialLinks", "false");
			}
			if (errorMap.get("products") != null
					|| errorMap.get("services") != null
					|| errorMap.get("productsOrServices_combined") != null
					|| errorMap.get("brands") != null
					|| errorMap.get("languages") != null
					|| errorMap.get("keywords") != null
					|| errorMap.get("ADDRESSPRIVACYFLAG") != null
					|| errorMap.get("yearEstablished") != null
					|| errorMap.get("tagline") != null
					|| errorMap.get("businessDescription") != null
					|| errorMap.get("businessDescriptionShort") != null) {
				errorMap.put("EnhancedContent", "true");
			} else {
				errorMap.put("EnhancedContent", "false");
			}
		}
		return errorMap;
	}

	public boolean isValidLocationAdress(String locationAddress) {

		boolean startsWithNumber = isStartsWithNumber(locationAddress);

		if (!startsWithNumber) {
			boolean startsEWNS = isStartsEWNS(locationAddress);
			return startsEWNS;
		}
		return true;
	}

	public boolean isValidMIAddress(String locationAddress) {

		boolean startsWithNumber = isStartsWithNumber(locationAddress);

		if (!startsWithNumber) {
			boolean startsEWNS = isStartsWithG(locationAddress);
			return startsEWNS;
		}
		return true;
	}

	public boolean isStartsWithG(String locationAddress) {
		locationAddress = locationAddress.toUpperCase();
		if (locationAddress.startsWith("G")) {
			return true;
		}
		return false;
	}

	public boolean isStartsEWNS(String locationAddress) {
		locationAddress = locationAddress.toUpperCase();
		if (locationAddress.startsWith("E") || locationAddress.startsWith("W")
				|| locationAddress.startsWith("N")
				|| locationAddress.startsWith("S")) {
			char digit = locationAddress.charAt(1);
			boolean isDigit = Character.isDigit(digit);
			if (isDigit) {
				return true;
			}
		}
		return false;
	}

}
