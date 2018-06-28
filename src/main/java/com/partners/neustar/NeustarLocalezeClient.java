package com.partners.neustar;

/**
 * @author lbl_dev
 * 
 * NeustarLocaleze API Client
 * 
 * 
 */

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.common.util.LBLConstants;
import com.business.common.util.SubmissionUtil;
import com.business.service.BusinessService;
import com.business.web.bean.NeustarLocalezeBean;
import com.partners.neustar.client.ClientStub;
import com.partners.neustar.client.ClientStub.ArrayOfElementResultType;
import com.partners.neustar.client.ClientStub.ArrayOfInt;
import com.partners.neustar.client.ClientStub.ArrayOfServiceKeyType;
import com.partners.neustar.client.ClientStub.ElementResultType;
import com.partners.neustar.client.ClientStub.OriginationType;
import com.partners.neustar.client.ClientStub.Query;
import com.partners.neustar.client.ClientStub.QueryResponse;
import com.partners.neustar.client.ClientStub.ServiceKeyType;

public class NeustarLocalezeClient {
	static Logger logger = Logger.getLogger(NeustarLocalezeClient.class);

	public static Map<String, List<String>> neustarLocalezeAPI(
			BusinessService service, List<LocalBusinessDTO> localBusinessDTOs) {
		Map<String, List<String>> errorDetails = new HashMap<String, List<String>>();

		List<String> erroredStores = new ArrayList<String>();
		String userName = LBLConstants.NEUSTAR_USERNAME;
		String passWord = LBLConstants.NEUSTAR_PASSWORD;
		String serviceID = LBLConstants.NEUSTAR_SERVICEID;
		String endPoint = LBLConstants.NEUSTAR_ENDPOINT;
		OriginationType originationType = new OriginationType();
		originationType.setUsername(userName);
		originationType.setPassword(passWord);
		ArrayOfInt arrayOfInt = new ArrayOfInt();
		int[] param = { 3700 };
		arrayOfInt.setId(param);
		ClientStub stub = null;
		try {
			logger.debug("Posting the data Neustar data to the URL: "
					+ endPoint);
			stub = new ClientStub(endPoint);
		} catch (AxisFault e) {
			logger.error("Error while creating a stub for neustar");
			e.printStackTrace();
		}
		Query query = new Query();
		query.setOrigination(originationType);
		query.setServiceId(serviceID);
		query.setElements(arrayOfInt);
		for (LocalBusinessDTO localBusinessDTO : localBusinessDTOs) {
			RenewalReportDTO renewalReportDTO = service
					.isRenewed(localBusinessDTO.getStore(),
							localBusinessDTO.getClientId());
			if (renewalReportDTO != null) {
				Date cancelledEffeciveDate = renewalReportDTO
						.getCancelledEffeciveDate();
				boolean isDate = false;
				if (cancelledEffeciveDate != null) {
					Date currentDateDate = new Date();
					isDate = cancelledEffeciveDate.compareTo(currentDateDate) < 0;
				}

				if ((renewalReportDTO.getStatus().equals("Renewed")
						|| renewalReportDTO.getStatus().equals("Active") || isDate)) {
					NeustarLocalezeBean neustarLocalezeBean = new NeustarLocalezeBean();
					neustarLocalezeBean.setPhone(localBusinessDTO
							.getLocationPhone());
					neustarLocalezeBean.setBusinessName(localBusinessDTO
							.getCompanyName().replaceAll("&", "&amp;"));
					String address = localBusinessDTO.getLocationAddress();

					if (localBusinessDTO.getSuite() != null
							&& localBusinessDTO.getSuite().length() > 0) {
						address = address + ", " + localBusinessDTO.getSuite();
					}
					neustarLocalezeBean.setAddress(address);
					neustarLocalezeBean.setCity(localBusinessDTO
							.getLocationCity());
					neustarLocalezeBean.setState(localBusinessDTO
							.getLocationState());
					neustarLocalezeBean.setZip(localBusinessDTO
							.getLocationZipCode());
					neustarLocalezeBean.setPlus4("");
					neustarLocalezeBean.setFax(localBusinessDTO.getFax());
					neustarLocalezeBean.setTollFreeNumber(localBusinessDTO
							.getTollFree());
					neustarLocalezeBean.setAltNumber(localBusinessDTO
							.getAdditionalNumber());
					neustarLocalezeBean.setMobileNumber(localBusinessDTO
							.getMobileNumber());
					neustarLocalezeBean.setCategoryType(localBusinessDTO
							.getCategory1());
					String categoryName = "";
					if (localBusinessDTO.getCategory1() != null) {
						categoryName = service.getCategoryNameById(Integer
								.parseInt(localBusinessDTO.getCategory1()));
					} else if (localBusinessDTO.getCategory2() != null) {
						categoryName = service.getCategoryNameById(Integer
								.parseInt(localBusinessDTO.getCategory2()));
					} else if (localBusinessDTO.getCategory3() != null) {
						categoryName = service.getCategoryNameById(Integer
								.parseInt(localBusinessDTO.getCategory3()));
					} else if (localBusinessDTO.getCategory4() != null) {
						categoryName = service.getCategoryNameById(Integer
								.parseInt(localBusinessDTO.getCategory4()));
					} else if (localBusinessDTO.getCategory5() != null) {
						categoryName = service.getCategoryNameById(Integer
								.parseInt(localBusinessDTO.getCategory5()));
					}

					neustarLocalezeBean.setCategoryName(categoryName
							.replaceAll("&", "&amp;"));
					neustarLocalezeBean.setAttributeID("");
					neustarLocalezeBean.setAttributeName(localBusinessDTO
							.getAlternativeName());
					neustarLocalezeBean.setUnstructuredTerms("");

					StringBuffer creditCards = new StringBuffer();
					if (localBusinessDTO.getaMEX() != null
							&& "Y".equalsIgnoreCase(localBusinessDTO.getaMEX())) {
						creditCards.append("Amex,");
					}
					if (localBusinessDTO.getMasterCard() != null
							&& "Y".equalsIgnoreCase(localBusinessDTO
									.getMasterCard())) {
						creditCards.append("MasterCard,");
					}
					if (localBusinessDTO.getDiscover() != null
							&& "Y".equalsIgnoreCase(localBusinessDTO
									.getDiscover())) {
						creditCards.append("Discover,");
					}
					if (localBusinessDTO.getVisa() != null
							&& "Y".equalsIgnoreCase(localBusinessDTO.getVisa())) {
						creditCards.append("Visa,");
					}
					if (localBusinessDTO.getDinersClub() != null
							&& "Y".equalsIgnoreCase(localBusinessDTO
									.getDinersClub())) {
						creditCards.append("DinersClub,");
					}
					if (localBusinessDTO.getCash() != null
							&& "Y".equalsIgnoreCase(localBusinessDTO.getCash())) {
						creditCards.append("Cash,");
					}
					if (localBusinessDTO.getCheck() != null
							&& "Y".equalsIgnoreCase(localBusinessDTO.getCheck())) {
						creditCards.append("Check,");
					}
					if (localBusinessDTO.getDebitCard() != null
							&& "Y".equalsIgnoreCase(localBusinessDTO
									.getDebitCard())) {
						creditCards.append("Debit");
					}

					String cards = creditCards.toString();
					if (cards.endsWith(",")) {
						cards = cards.replace(",", "");
					}

					neustarLocalezeBean.setCreditCards(cards);
					neustarLocalezeBean
							.setUrl(localBusinessDTO.getWebAddress());
					neustarLocalezeBean.seteMail(localBusinessDTO
							.getLocationEmail());
					neustarLocalezeBean.setYearOpenned(localBusinessDTO
							.getYearEstablished());

					StringBuffer hoursOfOperation = new StringBuffer();
					String mondayOperations = "";
					String tuesdayOperations = "";
					String wedOperations = "";
					String thursOperations = "";
					String fridayOperations = "";
					String satOperations = "";
					String sunOperations = "";
					if (localBusinessDTO.getMondayOpen() != null
							&& localBusinessDTO.getMondayClose() != null) {
						mondayOperations = localBusinessDTO.getMondayOpen()
								+ "-" + localBusinessDTO.getMondayClose();
					}
					if (localBusinessDTO.getTuesdayOpen() != null
							&& localBusinessDTO.getTuesdayClose() != null) {
						tuesdayOperations = localBusinessDTO.getTuesdayOpen()
								+ "-" + localBusinessDTO.getTuesdayClose();
					}

					if (localBusinessDTO.getWednesdayOpen() != null
							&& localBusinessDTO.getWednesdayClose() != null) {
						wedOperations = localBusinessDTO.getWednesdayOpen()
								+ "-" + localBusinessDTO.getWednesdayClose();
					}
					if (localBusinessDTO.getThursdayOpen() != null
							&& localBusinessDTO.getThursdayClose() != null) {
						thursOperations = localBusinessDTO.getThursdayOpen()
								+ "-" + localBusinessDTO.getThursdayClose();
					}
					if (localBusinessDTO.getFridayOpen() != null
							&& localBusinessDTO.getFridayClose() != null) {
						fridayOperations = localBusinessDTO.getFridayOpen()
								+ "-" + localBusinessDTO.getFridayClose();
					}
					if (localBusinessDTO.getSaturdayOpen() != null
							&& localBusinessDTO.getSaturdayClose() != null) {
						satOperations = localBusinessDTO.getSaturdayOpen()
								+ "-" + localBusinessDTO.getSaturdayClose();
					}
					if (localBusinessDTO.getSundayOpen() != null
							&& localBusinessDTO.getSundayClose() != null) {
						satOperations = localBusinessDTO.getSundayOpen() + "-"
								+ localBusinessDTO.getSundayClose();
					}

					/*
					 * neustarLocalezeBean .setHoursOfOperation(
					 * "Mon-Fri 8am-11am, Sat 8am-3pm, Sun Closed");
					 */
					neustarLocalezeBean
							.setHoursOfOperation(getHours(localBusinessDTO));
					String languages = "";
					if (localBusinessDTO.getLanguages() != null) {
						languages = localBusinessDTO.getLanguages();
					}
					neustarLocalezeBean.setLanguagesSpoken(languages);
					neustarLocalezeBean.setLogoImage(localBusinessDTO
							.getLogoLink());
					neustarLocalezeBean.setTagLine(localBusinessDTO
							.getTagline());
					neustarLocalezeBean.setSharedKey("");
					ServiceKeyType serviceKeyType = new ServiceKeyType();
					serviceKeyType.setId(1510);
					// serviceKeyType.setValue("<BPMSPost Edition=\"1.1\"> <Record> <Phone>8583145300</Phone><BusinessName>Localeze</BusinessName> <Department/> <Address>3750 Carmel Mountain Rd, Suite400</Address> <City>San Diego</City> <State>CA</State> <Zip>92130</Zip> </Record></BPMSPost&gt");
					String neustarData = NeustarLocalezeClient
							.getFormattedValue(neustarLocalezeBean);
					logger.info("*** Posting the Data to Neustar:"
							+ neustarData);
					serviceKeyType.setValue(neustarData);
					ArrayOfServiceKeyType arrayOfServiceKeyType = new ArrayOfServiceKeyType();
					ServiceKeyType[] localServiceKey = { serviceKeyType };
					arrayOfServiceKeyType.setServiceKey(localServiceKey);

					query.setServiceKeys(arrayOfServiceKeyType);

					QueryResponse queryResponse = null;
					try {
						queryResponse = stub.query(query);
					} catch (RemoteException e) {
						erroredStores.add(localBusinessDTO.getStore());
						logger.error("Error while posting the data to neustar"
								+ e);
						// e.printStackTrace();
					}

					long transId = queryResponse.getResponse().getTransId();
					logger.info("Transaction Id from Localeze Response is: "
							+ transId);
					int errorCode = queryResponse.getResponse().getErrorCode();
					logger.info("ErrorCode from Localeze Response is: "
							+ errorCode);
					ArrayOfElementResultType result = queryResponse
							.getResponse().getResult();
					ElementResultType[] element = result.getElement();
					for (ElementResultType elementResultType : element) {
						int id = elementResultType.getId();
						logger.info("Element Reult ID is: " + id);
						int errorCode2 = elementResultType.getErrorCode();
						logger.info("ErrorCode from Localeze " + errorCode2);
						String value = elementResultType.getValue();
						logger.info("Value == " + value);
					}
				}
			}
		}
		errorDetails.put("errorDetails", erroredStores);
		return errorDetails;
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

		if ("CLOSE".equalsIgnoreCase(mondayOpen)
				|| "CLOSE".equalsIgnoreCase(mondayClose)) {
			workingHours.append("MON CLOSE, ");
		}

		else if (mondayOpen != null && mondayOpen.trim().length() > 0
				&& mondayClose != null && mondayClose.trim().length() > 0) {
			workingHours.append("MON " + mondayOpen + "-" + mondayClose + ", ");
		}
		if ("CLOSE".equalsIgnoreCase(mondayOpen)
				|| "CLOSE".equalsIgnoreCase(mondayClose)) {
			workingHours.append("TUE CLOSE, ");
		} else if (tuesdayOpen != null && tuesdayOpen.trim().length() > 0
				&& tuuesdayClose != null && tuuesdayClose.trim().length() > 0) {
			workingHours.append("TUE " + tuesdayOpen + "-" + tuuesdayClose
					+ ", ");
		}

		if ("CLOSE".equalsIgnoreCase(wedOpen)
				|| "CLOSE".equalsIgnoreCase(wedClose)) {
			workingHours.append("WED CLOSE, ");
		} else if (wedOpen != null && wedOpen.trim().length() > 0
				&& wedClose != null && wedClose.trim().length() > 0) {
			workingHours.append("WED " + wedOpen + "-" + wedClose + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(thursdayOpen)
				|| "CLOSE".equalsIgnoreCase(thursdayClose)) {
			workingHours.append("THU CLOSE, ");
		} else if (tuesdayOpen != null && tuesdayOpen.trim().length() > 0
				&& tuuesdayClose != null && tuuesdayClose.trim().length() > 0) {
			workingHours.append("TUE " + tuesdayOpen + "-" + tuuesdayClose
					+ ", ");
		}

		if ("CLOSE".equalsIgnoreCase(fridayOpen)
				|| "CLOSE".equalsIgnoreCase(fridayClose)) {
			workingHours.append("FRI CLOSE, ");
		} else if (fridayOpen != null && fridayOpen.trim().length() > 0
				&& fridayClose != null && fridayClose.trim().length() > 0) {
			workingHours.append("FRI " + fridayOpen + "-" + fridayClose + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(satOpen)
				|| "CLOSE".equalsIgnoreCase(satClose)) {
			workingHours.append("SAT CLOSE, ");
		} else if (satOpen != null && satOpen.trim().length() > 0
				&& satClose != null && satClose.trim().length() > 0) {
			workingHours.append("SAT " + satOpen + "-" + satClose + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(sunOpen)
				|| "CLOSE".equalsIgnoreCase(sunClose)) {
			workingHours.append("SUN CLOSE");
		} else if (sunOpen != null && sunOpen.trim().length() > 0
				&& sunClose != null && sunClose.trim().length() > 0) {
			workingHours.append("SUN" + sunOpen + "-" + sunClose);
		}

		String hours = workingHours.toString();
		return hours;

	}

	public static String getFormattedValue(NeustarLocalezeBean bean) {
		String phone = bean.getPhone();
		String businessName = bean.getBusinessName();
		String address = bean.getAddress();
		String city = bean.getCity();
		String state = bean.getState().trim();
		String zip = bean.getZip();
		String plus4 = bean.getPlus4();
		String fax = bean.getFax();
		if (fax == null) {
			fax = "";
		}
		String tollFreeNumber = bean.getTollFreeNumber();
		String tagLine = bean.getTagLine();
		String altNumber = bean.getAltNumber();
		if (altNumber == null) {
			altNumber = "";
		}
		String attributeID = bean.getAttributeID();
		String attributeName = bean.getAttributeName();
		String unstructuredTerms = bean.getUnstructuredTerms();
		String url = bean.getUrl();
		String categoryType = bean.getCategoryType();
		String mobileNumber = bean.getMobileNumber();
		if (mobileNumber == null) {
			mobileNumber = "";
		}
		String categoryName = bean.getCategoryName();
		String creditCards = bean.getCreditCards();
		String eMail = bean.geteMail();
		String yearOpenned = bean.getYearOpenned();
		String languagesSpoken = bean.getLanguagesSpoken();
		String logoImage = bean.getLogoImage();
		String hoursOfOperation = bean.getHoursOfOperation();
		String sharedKey = bean.getSharedKey();

		StringBuffer sb = new StringBuffer();

		String start = "<BPMSPost Edition=\"1.1\"> " + "<Record>" + "<Phone>"
				+ phone + "</Phone>" + "<BusinessName>" + businessName
				+ "</BusinessName>" + "<Address>" + address + "</Address>"
				+ "<City>" + city + "</City>" + "<State>" + state + "</State>"
				+ "<Zip>" + zip + "</Zip>"
				+ "<Categories><Category><Type>Primary</Type><Name>"
				+ categoryName + "</Name></Category></Categories>";

		sb.append(start);

		if (logoImage != null && logoImage.length() > 0) {
			sb.append("<LogoImage>" + logoImage + "</LogoImage>");
		}
		if (eMail != null && eMail.length() > 0) {
			sb.append("<eMail>" + eMail + "</eMail>");
		}
		if (tollFreeNumber != null && tollFreeNumber.length() > 0) {
			sb.append("<TollFreeNumber>" + tollFreeNumber
					+ "</TollFreeNuomber>");
		}
		if (fax != null && fax.length() > 0) {
			sb.append("<Fax>" + fax + "</Fax>");
		}
		if (altNumber != null && altNumber.length() > 0) {
			sb.append("<AltNumber>" + altNumber + "</AltNumber>");
		}
		if (mobileNumber != null && mobileNumber.length() > 0) {
			sb.append("<MobileNumber>" + mobileNumber + "</MobileNumber>");
		}
		if (hoursOfOperation != null && hoursOfOperation.length() > 0) {
			sb.append("<HoursOfOperation>" + hoursOfOperation
					+ "</HoursOfOperation>");
		}
		if (creditCards != null && creditCards.length() > 0) {
			sb.append("<CreditCards>" + creditCards + "</CreditCards>");
		}
		if (languagesSpoken != null && languagesSpoken.length() > 0) {
			sb.append("<LanguagesSpoken>" + languagesSpoken
					+ "</LanguagesSpoken>");
		}
		if (yearOpenned != null && yearOpenned.length() > 0) {
			sb.append("<YearOpenned>" + yearOpenned + "</YearOpenned>");
		}
		if (tagLine != null && tagLine.length() > 0) {
			sb.append("<TagLine>" + tagLine + "</TagLine>");
		}
		if (sharedKey != null && sharedKey.length() > 0) {
			sb.append("<SharedKey>" + sharedKey + "</SharedKey>");
		}
		if (url != null && url.length() > 0) {
			sb.append("<URL>" + url + "</URL>");
		}

		sb.append("</Record>" + "</BPMSPost>");
		String value = sb.toString();

		if (value != null) {
			value.replaceAll("null", "");
		}
		return value;
	}

	public static void main(String[] args) {

		getDetails();

		String userName = LBLConstants.NEUSTAR_USERNAME;
		String passWord = LBLConstants.NEUSTAR_PASSWORD;
		String serviceID = LBLConstants.NEUSTAR_SERVICEID;
		String endPoint = LBLConstants.NEUSTAR_ENDPOINT;

		OriginationType originationType = new OriginationType();
		originationType.setUsername(userName);
		originationType.setPassword(passWord);
		ArrayOfInt arrayOfInt = new ArrayOfInt();
		int[] param = { 3700 };
		arrayOfInt.setId(param);

		ClientStub stub = null;
		try {
			System.out.println("Posting the data Neustar data to the URL: "
					+ endPoint);
			stub = new ClientStub(endPoint);
		} catch (AxisFault e) {
			logger.error("Error while creating a stub for neustar");
			e.printStackTrace();
		}
		Query query = new Query();
		query.setOrigination(originationType);
		query.setServiceId(serviceID);
		query.setElements(arrayOfInt);

		// *******************************
		ServiceKeyType serviceKeyType = new ServiceKeyType();
		serviceKeyType.setId(1510);
		String businessName = "Maid Brigade of Annapolis";
		String department = "";
		String addrese = "2516 Housley Rd";
		String city = "Annapolis";
		String state = "VA";
		String zip = "21401";
		String phone = "4107740527";
		String categoryName = "House Cleaning Services";
		String logoImage = "www.maidbrigade.com/images/maid_brigade_logo.gif";
		String email = "sales@maid-brigade.com";
		String tollFree = "8005156243";
		String fax = "";
		String alterNumber = "";
		String mobile = "";
		String hoursOfOperation = "Mon-Fri 8am-11am, Sat 8am-3pm, Sun Closed";
		String creditcards = "Visa, Mastercard, Cash, Check";
		String languages = "";
		String yearEstd = "1989";
		String tagLine = "Your home. Cleaner.";
		String url = "https://plus.google.com/104736256805137844654";
		String keywords = "house cleaning, maid service, cleaning services, cleaning service, clean house, cleaning company, housekeeping, house cleaning services, move out cleaning, residential cleaning, maid company, move in cleaning, eco cleaning, house cleaners, green cleaning";

		/*
		 * String value1 = "<BPMSPost Edition=\"1.1\"> " + "<Record>" +
		 * "<Phone>" + phone + "</Phone>" + "<BusinessName>" + businessName +
		 * "</BusinessName>" + "<Department/>" + "<Address>" + addrese +
		 * "</Address>" + "<City>" + city + "</City>" + "<State>" + state +
		 * "</State>" + "<Zip>" + zip + "</Zip>" +
		 * "<Categories><Category><Type>Primary</Type><Name>" + categoryName +
		 * "</Name></Category></Categories>" + "<LogoImage>" + logoImage +
		 * "</LogoImage>" + "<eMail>" + email + "</eMail>" + "<TollFreeNumber>"
		 * + tollFree + "</TollFreeNumber>" + "<Fax>" + tollFree + "</Fax>" +
		 * "<AltNumber>" + alterNumber + "</AltNumber>" + "<MobileNumber>" +
		 * mobile + "</MobileNumber>" + "<HoursOfOperation>" + hoursOfOperation
		 * + "</HoursOfOperation>" + "<CreditCards>" + creditcards +
		 * "</CreditCards>" + "<LanguagesSpoken>" + languages +
		 * "</LanguagesSpoken>" + "<YearOpenned>" + yearEstd + "</YearOpenned>"
		 * + "<TagLine>" + tagLine + "</TagLine>" + "<SharedKey>" + keywords +
		 * "</SharedKey>" + "<URL>" + url + "</URL>" + "</Record>" +
		 * "</BPMSPost>"; //**********************
		 */
		String value2 = "<BPMSPost Edition=\"1.1\"> <Record>" + "<Phone>"
				+ phone
				+ "</Phone>"
				+ "<BusinessName>"
				+ businessName
				+ "</BusinessName>"
				+ "<Department/>"
				+ "<Address>"
				+ addrese
				+ "</Address>"
				+ "<City>"
				+ city
				+ "</City>"
				+ "<State>"
				+ state
				+ "</State>"
				+ "<Zip>"
				+ zip
				+ "</Zip>"
				+ "<Categories><Category><Type>Primary</Type><Name>Tire Dealers Retail</Name></Category></Categories>"
				+ "<LogoImage>client1.com/images/logo.jpg</LogoImage>"
				+ "<eMail>info@client1.com</eMail>"
				+ "<TollFreeNumber>8003846441</TollFreeNumber>"
				+ "<Fax></Fax><AltNumber></AltNumber>"
				+ "<MobileNumber></MobileNumber>"
				+ "<HoursOfOperation></HoursOfOperation><CreditCards>Y, </CreditCards>"
				+ "<LanguagesSpoken></LanguagesSpoken>"
				+ "<YearOpenned>1898</YearOpenned>"
				+ "<TagLine>Lorem ipsum dolor sit amet, consetetur sadipscing elitr.</TagLine>"
				+ "<URL>client1.com/location14479</URL></Record></BPMSPost>";

		value2 = "<BPMSPost Edition=\"1.1\"> <Record><Phone>8883948530</Phone>"
				+ "<BusinessName>PODS Moving &amp; Storage of Northeast Atlanta</BusinessName>"
				+ "<Address>16 Overmeyer Way</Address>"
				+ "<City>Forest Park</City"
				+ "><State>GA</State>"
				+ "<Zip>30297</Zip>"
				+ "<Categories><Category><Type>Primary</Type><Name>STORAGE CONTAINERS, FACILITIES & WAREHOUSES</Name></Category></Categories><URL>www.pods.com</URL></Record></BPMSPost>";

		serviceKeyType.setValue(value2);
		ArrayOfServiceKeyType arrayOfServiceKeyType = new ArrayOfServiceKeyType();
		ServiceKeyType[] localServiceKey = { serviceKeyType };
		arrayOfServiceKeyType.setServiceKey(localServiceKey);

		query.setServiceKeys(arrayOfServiceKeyType);

		QueryResponse queryResponse = null;
		try {
			queryResponse = stub.query(query);
		} catch (RemoteException e) {
			logger.error("Error while posting the data to neustar");
			e.printStackTrace();
		}

		long transId = queryResponse.getResponse().getTransId();
		logger.info("TransId == " + transId);
		int errorCode = queryResponse.getResponse().getErrorCode();
		logger.info("ErrorCode == " + errorCode);
		ArrayOfElementResultType result = queryResponse.getResponse()
				.getResult();
		ElementResultType[] element = result.getElement();
		for (ElementResultType elementResultType : element) {
			int id = elementResultType.getId();
			logger.info("Element ID == " + id);
			int errorCode2 = elementResultType.getErrorCode();
			logger.info("ErrorCode == " + errorCode2);
			String value = elementResultType.getValue();
			logger.info("Value == " + value);
		}

	}

	public static void getDetails() {

		String userName = LBLConstants.NEUSTAR_USERNAME;
		String passWord = LBLConstants.NEUSTAR_PASSWORD;
		String serviceID = LBLConstants.NEUSTAR_SERVICEID;
		String endPoint = LBLConstants.NEUSTAR_ENDPOINT;

		OriginationType originationType = new OriginationType();
		originationType.setUsername(userName);
		originationType.setPassword(passWord);
		ArrayOfInt arrayOfInt = new ArrayOfInt();
		int[] param = { 3720 };
		arrayOfInt.setId(param);

		ClientStub stub = null;
		try {
			System.out.println("Posting the data Neustar data to the URL: "
					+ endPoint);
			stub = new ClientStub(endPoint);
		} catch (AxisFault e) {
			logger.error("Error while creating a stub for neustar");
			e.printStackTrace();
		}
		Query query = new Query();
		query.setOrigination(originationType);
		query.setServiceId(serviceID);
		query.setElements(arrayOfInt);

		// *******************************
		ServiceKeyType serviceKeyType = new ServiceKeyType();
		serviceKeyType.setId(1510);
		String businessName = "Maid Brigade of Annapolis";
		String department = "";
		String addrese = "2516 Housley Rd";
		String city = "Annapolis";
		String state = "VA";
		String zip = "21401";
		String phone = "4107740527";
		String categoryName = "House Cleaning Services";
		String logoImage = "www.maidbrigade.com/images/maid_brigade_logo.gif";
		String email = "sales@maid-brigade.com";
		String tollFree = "8005156243";
		String fax = "";
		String alterNumber = "";
		String mobile = "";
		String hoursOfOperation = "Mon-Fri 8am-11am, Sat 8am-3pm, Sun Closed";
		String creditcards = "Visa, Mastercard, Cash, Check";
		String languages = "";
		String yearEstd = "1989";
		String tagLine = "Your home. Cleaner.";
		String url = "https://plus.google.com/104736256805137844654";
		String keywords = "house cleaning, maid service, cleaning services, cleaning service, clean house, cleaning company, housekeeping, house cleaning services, move out cleaning, residential cleaning, maid company, move in cleaning, eco cleaning, house cleaners, green cleaning";

		/*
		 * String value1 = "<BPMSPost Edition=\"1.1\"> " + "<Record>" +
		 * "<Phone>" + phone + "</Phone>" + "<BusinessName>" + businessName +
		 * "</BusinessName>" + "<Department/>" + "<Address>" + addrese +
		 * "</Address>" + "<City>" + city + "</City>" + "<State>" + state +
		 * "</State>" + "<Zip>" + zip + "</Zip>" +
		 * "<Categories><Category><Type>Primary</Type><Name>" + categoryName +
		 * "</Name></Category></Categories>" + "<LogoImage>" + logoImage +
		 * "</LogoImage>" + "<eMail>" + email + "</eMail>" + "<TollFreeNumber>"
		 * + tollFree + "</TollFreeNumber>" + "<Fax>" + tollFree + "</Fax>" +
		 * "<AltNumber>" + alterNumber + "</AltNumber>" + "<MobileNumber>" +
		 * mobile + "</MobileNumber>" + "<HoursOfOperation>" + hoursOfOperation
		 * + "</HoursOfOperation>" + "<CreditCards>" + creditcards +
		 * "</CreditCards>" + "<LanguagesSpoken>" + languages +
		 * "</LanguagesSpoken>" + "<YearOpenned>" + yearEstd + "</YearOpenned>"
		 * + "<TagLine>" + tagLine + "</TagLine>" + "<SharedKey>" + keywords +
		 * "</SharedKey>" + "<URL>" + url + "</URL>" + "</Record>" +
		 * "</BPMSPost>"; //**********************
		 */
		// String value2 =
		// "<BPMSPost Edition=\"1.1\"><Record><BusinessName/>Maid Brigade of Annapolis</BusinessName/><Zip>21401</Zip></Record></BPMSPost>";
		String value2 = "<BPMSPost Edition=\"1.1\">"
				+ " <Record><Phone>8883948530</Phone><BusinessName>PODS Moving &amp; Storage of Bakersfield</BusinessName>"
				+ "<Address>4615 Grissom Street</Address><City>Bakersfield</City><State>CA</State><Zip>93313</Zip><Categories><Category><Type>Primary</Type><Name>STORAGE CONTAINERS, FACILITIES & WAREHOUSES</Name></Category></Categories></Record></BPMSPost>";

		serviceKeyType.setValue(value2);
		ArrayOfServiceKeyType arrayOfServiceKeyType = new ArrayOfServiceKeyType();
		ServiceKeyType[] localServiceKey = { serviceKeyType };
		arrayOfServiceKeyType.setServiceKey(localServiceKey);

		query.setServiceKeys(arrayOfServiceKeyType);

		QueryResponse queryResponse = null;
		try {
			queryResponse = stub.query(query);
		} catch (RemoteException e) {
			logger.error("Error while posting the data to neustar");
			e.printStackTrace();
		}

		long transId = queryResponse.getResponse().getTransId();
		logger.info("TransId == " + transId);
		int errorCode = queryResponse.getResponse().getErrorCode();
		logger.info("ErrorCode == " + errorCode);
		ArrayOfElementResultType result = queryResponse.getResponse()
				.getResult();
		ElementResultType[] element = result.getElement();
		for (ElementResultType elementResultType : element) {
			int id = elementResultType.getId();
			logger.info("Element ID == " + id);
			int errorCode2 = elementResultType.getErrorCode();
			logger.info("ErrorCode == " + errorCode2);
			String value = elementResultType.getValue();
			logger.info("Value == " + value);
		}

	}

}
