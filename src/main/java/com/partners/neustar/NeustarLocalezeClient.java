package com.partners.neustar;

/**
 * @author Vasanth
 * 
 * NeustarLocaleze API Client
 * 
 * 
 */

import java.rmi.RemoteException;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.LBLConstants;
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


	public static  void neustarLocalezeAPI(BusinessService service , List<LocalBusinessDTO> localBusinessDTOs) {


		String userName = LBLConstants.NEUSTAR_USERNAME;
		String passWord = LBLConstants.NEUSTAR_PASSWORD;
		String serviceID = LBLConstants.NEUSTAR_SERVICEID;
		String endPoint = LBLConstants.NEUSTAR_ENDPOINT;
		OriginationType originationType = new OriginationType();
		originationType.setUsername(userName);
		originationType.setPassword(passWord);
		ArrayOfInt arrayOfInt = new ArrayOfInt();
		int[] param = { 3722 };
		arrayOfInt.setId(param);
		ClientStub stub = null;
		try {
			logger.debug("Posting the data Neustar data to the URL: "+ endPoint);
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
			NeustarLocalezeBean neustarLocalezeBean = new NeustarLocalezeBean();
			neustarLocalezeBean.setPhone(localBusinessDTO.getLocationPhone());
			neustarLocalezeBean.setBusinessName(localBusinessDTO
					.getCompanyName());
			String address = localBusinessDTO
					.getLocationAddress();
			
			if(localBusinessDTO.getSuite()!=null) {
				address = address +", " + localBusinessDTO.getSuite();
			}
			neustarLocalezeBean.setAddress(address);
			neustarLocalezeBean.setCity(localBusinessDTO.getLocationCity());
			neustarLocalezeBean.setState(localBusinessDTO.getLocationState());
			neustarLocalezeBean.setZip(localBusinessDTO.getLocationZipCode());
			neustarLocalezeBean.setPlus4("");
			neustarLocalezeBean.setFax(localBusinessDTO.getFax());
			neustarLocalezeBean.setTollFreeNumber(localBusinessDTO.getTollFree());
			neustarLocalezeBean.setAltNumber(localBusinessDTO.getAdditionalNumber());
			neustarLocalezeBean.setMobileNumber(localBusinessDTO.getMobileNumber());
			neustarLocalezeBean.setCategoryType(localBusinessDTO.getCategory1());
			String categoryName = "";
			if(localBusinessDTO.getCategory1()!=null){
				categoryName = service.getCategoryNameById(Integer.parseInt(localBusinessDTO.getCategory1()));
			}
			
			neustarLocalezeBean.setCategoryName(categoryName);
			neustarLocalezeBean.setAttributeID("");
			neustarLocalezeBean.setAttributeName(localBusinessDTO.getAlternativeName());
			neustarLocalezeBean.setUnstructuredTerms("");
			
			StringBuffer creditCards =  new StringBuffer();
			if( localBusinessDTO.getaMEX()!=null && "Y".equalsIgnoreCase(localBusinessDTO.getaMEX())){
				creditCards.append("Amex,");
			}if( localBusinessDTO.getMasterCard()!=null && "Y".equalsIgnoreCase(localBusinessDTO.getMasterCard())){
				creditCards.append("MasterCard,");
			}if( localBusinessDTO.getDiscover()!=null && "Y".equalsIgnoreCase(localBusinessDTO.getDiscover())){
				creditCards.append("Discover,");
			}if( localBusinessDTO.getVisa()!=null && "Y".equalsIgnoreCase(localBusinessDTO.getVisa())){
				creditCards.append("Visa,");
			}if( localBusinessDTO.getDinersClub()!=null && "Y".equalsIgnoreCase(localBusinessDTO.getDinersClub())){
				creditCards.append("DinersClub,");
			}if( localBusinessDTO.getCash()!=null && "Y".equalsIgnoreCase(localBusinessDTO.getCash())){
				creditCards.append("Cash,");
			}if( localBusinessDTO.getCheck()!=null && "Y".equalsIgnoreCase(localBusinessDTO.getCheck())){
				creditCards.append("Check,");
			}if( localBusinessDTO.getDebitCard()!=null && "Y".equalsIgnoreCase(localBusinessDTO.getDebitCard())){
				creditCards.append("Debit");
			}

			String cards = creditCards.toString();
			if(cards.endsWith(",")){
				cards =  cards.replace(",", "");
			}
			
			neustarLocalezeBean.setCreditCards(cards);
			neustarLocalezeBean.setUrl(localBusinessDTO.getWebAddress());
			neustarLocalezeBean.seteMail(localBusinessDTO.getLocationEmail());
			neustarLocalezeBean.setYearOpenned(localBusinessDTO.getYearEstablished());
			
			
			StringBuffer hoursOfOperation = new StringBuffer();
			String mondayOperations="";
			String tuesdayOperations="";
			String wedOperations="";
			String thursOperations="";
			String fridayOperations="";
			String satOperations="";
			String sunOperations="";
			if(localBusinessDTO.getMondayOpen()!=null && localBusinessDTO.getMondayClose()!=null){
				mondayOperations = localBusinessDTO.getMondayOpen() + "-" + localBusinessDTO.getMondayClose() ;
			} 	
			if(localBusinessDTO.getTuesdayOpen() !=null && localBusinessDTO.getTuesdayClose()!=null){
				tuesdayOperations = localBusinessDTO.getTuesdayOpen() + "-" + localBusinessDTO.getTuesdayClose();
			} 
			
			if(localBusinessDTO.getWednesdayOpen() !=null && localBusinessDTO.getWednesdayClose()!=null){
				wedOperations = localBusinessDTO.getWednesdayOpen() + "-" + localBusinessDTO.getWednesdayClose();
			} 
			if(localBusinessDTO.getThursdayOpen() !=null && localBusinessDTO.getThursdayClose()!=null){
				thursOperations = localBusinessDTO.getThursdayOpen() + "-" + localBusinessDTO.getThursdayClose();
			} 
			if(localBusinessDTO.getFridayOpen() !=null && localBusinessDTO.getFridayClose()!=null){
				fridayOperations = localBusinessDTO.getFridayOpen() + "-" + localBusinessDTO.getFridayClose();
			} 
			if(localBusinessDTO.getSaturdayOpen() !=null && localBusinessDTO.getSaturdayClose()!=null){
				satOperations = localBusinessDTO.getSaturdayOpen() + "-" + localBusinessDTO.getSaturdayClose();
			} 
			if(localBusinessDTO.getSundayOpen() !=null && localBusinessDTO.getSundayClose()!=null){
				satOperations = localBusinessDTO.getSundayOpen() + "-" + localBusinessDTO.getSundayClose();
			} 
			
			neustarLocalezeBean.setHoursOfOperation("Mon-Fri 8am-11am, Sat 8am-3pm, Sun Closed");
			String languages ="";
			if( localBusinessDTO.getLanguages()!=null){
				languages = localBusinessDTO.getLanguages();
			}
			neustarLocalezeBean.setLanguagesSpoken(languages);
			neustarLocalezeBean.setLogoImage(localBusinessDTO.getLogoLink());
			neustarLocalezeBean.setTagLine(localBusinessDTO.getTagline());
			neustarLocalezeBean.setSharedKey("");
			ServiceKeyType serviceKeyType = new ServiceKeyType();
			serviceKeyType.setId(1510);
			// serviceKeyType.setValue("<BPMSPost Edition=\"1.1\"> <Record> <Phone>8583145300</Phone><BusinessName>Localeze</BusinessName> <Department/> <Address>3750 Carmel Mountain Rd, Suite400</Address> <City>San Diego</City> <State>CA</State> <Zip>92130</Zip> </Record></BPMSPost&gt");
			String neustarData = NeustarLocalezeClient
					.getFormattedValue(neustarLocalezeBean);
			logger.info("*** Posting the Data to Neustar:" + neustarData);
			serviceKeyType.setValue(neustarData);
			ArrayOfServiceKeyType arrayOfServiceKeyType = new ArrayOfServiceKeyType();
			ServiceKeyType[] localServiceKey = { serviceKeyType };
			arrayOfServiceKeyType.setServiceKey(localServiceKey);

			query.setServiceKeys(arrayOfServiceKeyType);

			QueryResponse queryResponse = null;
			try {
				queryResponse = stub.query(query);
			} catch (RemoteException e) {
				logger.error("Error while posting the data to neustar" + e);
				e.printStackTrace();
			}

			long transId = queryResponse.getResponse().getTransId();
			logger.info("Transaction Id from Localeze Response is: " + transId);
			int errorCode = queryResponse.getResponse().getErrorCode();
			logger.info("ErrorCode from Localeze Response is: " + errorCode);
			ArrayOfElementResultType result = queryResponse.getResponse()
					.getResult();
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

	public static String getFormattedValue(NeustarLocalezeBean bean) {
		String phone = bean.getPhone();
		String businessName = bean.getBusinessName();
		String address = bean.getAddress();
		String city = bean.getCity();
		String state = bean.getState().trim();
		String zip = bean.getZip();
		String plus4 = bean.getPlus4();
		String fax = bean.getFax();
		if(fax==null){
			fax="";
		}
		String tollFreeNumber = bean.getTollFreeNumber();
		String tagLine = bean.getTagLine();
		String altNumber = bean.getAltNumber();
		if(altNumber==null){
			altNumber="";
		}
		String attributeID = bean.getAttributeID();
		String attributeName = bean.getAttributeName();
		String unstructuredTerms = bean.getUnstructuredTerms();
		String url = bean.getUrl();
		String categoryType = bean.getCategoryType();
		String mobileNumber = bean.getMobileNumber();
		if(mobileNumber==null){
			mobileNumber="";
		}
		String categoryName = bean.getCategoryName();
		String creditCards = bean.getCreditCards();
		String eMail = bean.geteMail();
		String yearOpenned = bean.getYearOpenned();		
		String languagesSpoken = bean.getLanguagesSpoken();
		String logoImage = bean.getLogoImage();
		String hoursOfOperation = bean.getHoursOfOperation();
		String sharedKey = bean.getSharedKey();
		
		String value = "<BPMSPost Edition=\"1.1\"> "
					+ "<Record>"
						+ "<Phone>" + phone + "</Phone>" 
						+ "<BusinessName>" + businessName + "</BusinessName>" 
						+ "<Department/>"
						+ "<Address>" + address + "</Address>" 
						+ "<City>" + city + "</City>" 
						+ "<State>" + state + "</State>" 
						+ "<Zip>" + zip + "</Zip>" 
						+ "<Categories><Category><Type>Primary</Type><Name>" + categoryName + "</Name></Category></Categories>"
						+ "<LogoImage>" + logoImage + "</LogoImage>" 
						+ "<eMail>" + eMail + "</eMail>" 
						+ "<TollFreeNumber>" + tollFreeNumber + "</TollFreeNumber>" 
						+ "<Fax>" + fax + "</Fax>" 
						+ "<AltNumber>" + altNumber + "</AltNumber>" 
						+ "<MobileNumber>" + mobileNumber + "</MobileNumber>" 
						+ "<HoursOfOperation>" + hoursOfOperation + "</HoursOfOperation>"
						+ "<CreditCards>" + creditCards + "</CreditCards>"
						+ "<LanguagesSpoken>" + languagesSpoken + "</LanguagesSpoken>"
						+ "<YearOpenned>" + yearOpenned + "</YearOpenned>"
						+ "<TagLine>" + tagLine + "</TagLine>"
						+ "<SharedKey>" + sharedKey + "</SharedKey>"
						+ "<URL>" + url + "</URL>"
					+ "</Record>"
				+ "</BPMSPost>";	
		return value;
	}

	public static void main(String[] args) {
		
		String userName = LBLConstants.NEUSTAR_USERNAME;
		String passWord = LBLConstants.NEUSTAR_PASSWORD;
		String serviceID = LBLConstants.NEUSTAR_SERVICEID;
		String endPoint = LBLConstants.NEUSTAR_ENDPOINT;
		

		OriginationType originationType = new OriginationType();
		originationType.setUsername(userName);
		originationType.setPassword(passWord);
		ArrayOfInt arrayOfInt = new ArrayOfInt();
		int[] param = { 3722 };
		arrayOfInt.setId(param);

		ClientStub stub = null;
		try {
			System.out.println("Posting the data Neustar data to the URL: "+ endPoint);
			stub = new ClientStub(endPoint);
		} catch (AxisFault e) {
			logger.error("Error while creating a stub for neustar");
			e.printStackTrace();
		}
		Query query = new Query();
		query.setOrigination(originationType);
		query.setServiceId(serviceID);
		query.setElements(arrayOfInt);

		//*******************************
		ServiceKeyType serviceKeyType = new ServiceKeyType();
		serviceKeyType.setId(1510);
		String businessName = "Maid Brigade of Annapolis";
		String department= "";
		String addrese= "2516 Housley Rd";
		String city = "Annapolis";
		String state = "VA";
		String zip = "21401";
		String phone = "4107740527";
		String categoryName = "House Cleaning Services";
		String logoImage = "www.maidbrigade.com/images/maid_brigade_logo.gif";
		String email = "sales@maid-brigade.com";
		String tollFree="8005156243";
		String fax="";
		String alterNumber="";
		String mobile="";
		String hoursOfOperation = "Mon-Fri 8am-11am, Sat 8am-3pm, Sun Closed";
		String creditcards="Visa, Mastercard, Cash, Check";
		String languages="";
		String yearEstd ="1989";
		String tagLine ="Your home. Cleaner.";
		String url ="https://plus.google.com/104736256805137844654";
		String keywords ="house cleaning, maid service, cleaning services, cleaning service, clean house, cleaning company, housekeeping, house cleaning services, move out cleaning, residential cleaning, maid company, move in cleaning, eco cleaning, house cleaners, green cleaning";
		
		/*String value1 = "<BPMSPost Edition=\"1.1\"> "
					+ "<Record>"
						+ "<Phone>" + phone + "</Phone>" 
						+ "<BusinessName>" + businessName + "</BusinessName>" 
						+ "<Department/>"
						+ "<Address>" + addrese + "</Address>" 
						+ "<City>" + city + "</City>" 
						+ "<State>" + state + "</State>" 
						+ "<Zip>" + zip + "</Zip>" 
						+ "<Categories><Category><Type>Primary</Type><Name>" + categoryName + "</Name></Category></Categories>"
						+ "<LogoImage>" + logoImage + "</LogoImage>" 
						+ "<eMail>" + email + "</eMail>" 
						+ "<TollFreeNumber>" + tollFree + "</TollFreeNumber>" 
						+ "<Fax>" + tollFree + "</Fax>" 
						+ "<AltNumber>" + alterNumber + "</AltNumber>" 
						+ "<MobileNumber>" + mobile + "</MobileNumber>" 
						+ "<HoursOfOperation>" + hoursOfOperation + "</HoursOfOperation>"
						+ "<CreditCards>" + creditcards + "</CreditCards>"
						+ "<LanguagesSpoken>" + languages + "</LanguagesSpoken>"
						+ "<YearOpenned>" + yearEstd + "</YearOpenned>"
						+ "<TagLine>" + tagLine + "</TagLine>"
						+ "<SharedKey>" + keywords + "</SharedKey>"
						+ "<URL>" + url + "</URL>"
					+ "</Record>"
				+ "</BPMSPost>";
		//**********************
		*/
		String value2 = "<BPMSPost Edition=\"1.1\"> <Record>"
				+ "<Phone>" + phone + "</Phone>" 
				+ "<BusinessName>" + businessName + "</BusinessName>" 
				+ "<Department/>"
				+ "<Address>" + addrese + "</Address>" 
				+ "<City>" + city + "</City>" 
				+ "<State>" + state + "</State>" 
				+ "<Zip>" + zip + "</Zip>" 
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
		
		serviceKeyType
				.setValue(value2);
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
