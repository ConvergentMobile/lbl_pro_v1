package com.business.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.business.common.dto.LocalBusinessDTO;
import com.business.service.BusinessService;

/***
 * 
 * @author vasanth
 * 
 */

@Component
public class ExcelBuilder extends AbstractExcelView {
	@Autowired
	BusinessService service;
	Logger logger = Logger.getLogger(ExcelBuilder.class);

	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String apiService = (String) model.get("apiService");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ "bulkExports-" + apiService + ".xls" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");

		List<LocalBusinessDTO> listOfBusiness = (List<LocalBusinessDTO>) model
				.get("listOfAPI");

		logger.info("@@@@@@@@@@@Service Name : " + apiService);
		// logger.info("Business list size : "+listOfBusiness.size());
		if (apiService != null) {
			if (apiService.contains("BingTemplate")) {
				bingPlacesSampleTemplate(workbook, listOfBusiness);
			}
			if (apiService.contains("GoogleTemplate")) {
				googleTemplate(workbook, listOfBusiness);
			}
			if (apiService.contains("MasterTemplate")) {
				masterTemplateData(workbook, listOfBusiness);
			}
			if (apiService.contains("YPTemplate")) {
				ypTemplateData(workbook, listOfBusiness);
			}
			if (apiService.contains("yelpTemplate")) {
				yelpTemplateData(workbook, listOfBusiness);
			}
			if (apiService.contains("yextTemplate")) {
				yextTemplateData(workbook, listOfBusiness);
			}
			if (apiService.contains("InfogroupTemplate")) {
				infogroupTemplateData(workbook, listOfBusiness);
			}
			if (apiService.contains("LocalezeTemplate")) {
				localezeFeedTemplateData(workbook, listOfBusiness);
			}
			if (apiService.contains("AcxiomUsTemplate")) {
				acxiomUsTemplateTemplateData(workbook, listOfBusiness);
			}
			if (apiService.contains("AcxiomCanTemplate")) {
				acxiomCanTemplateTemplateData(workbook, listOfBusiness);
			}
		} else {
			masterTemplateData(workbook, listOfBusiness);
		}

	}

	private void acxiomCanTemplateTemplateData(HSSFWorkbook workbook,
			List<LocalBusinessDTO> listOfBusiness) {

		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet("errorInfo");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// Master template
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("ClientID");
		header.createCell(1).setCellValue("CreateDate");
		header.createCell(2).setCellValue("BusinessName");
		header.createCell(3).setCellValue("UpdateFlag");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("ContactFN");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("ContactLN");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("ContactGender");
		header.createCell(7).setCellValue("ContactTitle");
		header.createCell(8).setCellValue("Address1");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("Filler");
		header.createCell(10).setCellValue("City");
		header.getCell(10).setCellStyle(style);
		header.createCell(11).setCellValue("Province");
		header.getCell(11).setCellStyle(style);
		header.createCell(12).setCellValue("PostalCode");
		header.getCell(12).setCellStyle(style);
		header.createCell(13).setCellValue("Filler");
		header.createCell(14).setCellValue("PrimaryPhone");
		header.createCell(15).setCellValue("Fax");
		header.createCell(16).setCellValue("Cat1");
		header.createCell(17).setCellValue("Cat2");
		header.createCell(18).setCellValue("Cat3");
		header.createCell(19).setCellValue("Cat4");
		header.createCell(20).setCellValue("Cat5");
		header.createCell(21).setCellValue("Cat6");
		header.createCell(22).setCellValue("CategoryTypeID");
		header.createCell(23).setCellValue("Website");
		header.getCell(23).setCellStyle(style);
		header.createCell(24).setCellValue("Email");
		header.createCell(25).setCellValue("YearEstablished");
		header.createCell(26).setCellValue("Products");
		header.createCell(27).setCellValue("Services");
		header.createCell(28).setCellValue("Brands");
		header.createCell(29).setCellValue("Hours");
		header.getCell(29).setCellStyle(style);
		header.createCell(30).setCellValue("PaymentMethods");
		header.createCell(31).setCellValue("LanguagesSpoken");
		header.createCell(32).setCellValue("FreeCode");
		header.createCell(33).setCellValue("DiscountCode");
		header.createCell(34).setCellValue("DeliveryCode");
		header.createCell(35).setCellValue("Slogans");
		header.createCell(36).setCellValue("Trademark");
		header.createCell(37).setCellValue("Models");
		header.createCell(38).setCellValue("Specialties");
		header.createCell(39).setCellValue("ProgramsOffered");
		header.createCell(40).setCellValue("ProductFeatures");
		header.createCell(41).setCellValue("ServiceFeatures");
		header.createCell(42).setCellValue("LocationFeatures");
		header.createCell(43).setCellValue("GroupsServed");
		header.createCell(44).setCellValue("SpecialConsiderations");
		header.createCell(45).setCellValue("OrderingMethods");
		header.createCell(46).setCellValue("ProfessionalsOnStaff");
		header.createCell(47).setCellValue("Associations");
		header.createCell(48).setCellValue("Certifications");
		header.createCell(49).setCellValue("AdditionalPhoneNumbers");
		header.createCell(50).setCellValue("AdditionalPhoneNumberCodes");
		header.createCell(51).setCellValue("DisplayName");
		header.createCell(52).setCellValue("CorporateName");

		// create data rows
		int rowCount = 1;

		for (LocalBusinessDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			String countryCode = obj.getCountryCode();
			if (countryCode != null && countryCode.length() > 0
					&& countryCode.equalsIgnoreCase("CA")) {
				aRow.createCell(0).setCellValue(obj.getClientId());
				Date uploadedTime = obj.getUploadedTime();
				if (uploadedTime == null) {
					uploadedTime = new Date();
				}
				aRow.createCell(1).setCellValue(uploadedTime);
				aRow.createCell(2).setCellValue(obj.getCompanyName());
				aRow.createCell(3).setCellValue(obj.getActionCode());
				aRow.createCell(4).setCellValue(
						obj.getPrimaryContactFirstName());
				aRow.createCell(5)
						.setCellValue(obj.getPrimaryContactLastName());
				aRow.createCell(6).setCellValue("");
				aRow.createCell(7).setCellValue(obj.getContactTitle());
				aRow.createCell(8).setCellValue(obj.getLocationAddress());
				aRow.createCell(9).setCellValue("");
				aRow.createCell(10).setCellValue(obj.getLocationCity());
				aRow.createCell(11).setCellValue("");
				aRow.createCell(12).setCellValue(obj.getLocationZipCode());
				aRow.createCell(13).setCellValue("");
				aRow.createCell(14).setCellValue(obj.getLocationPhone());
				aRow.createCell(15).setCellValue(obj.getFax());
				aRow.createCell(16).setCellValue(obj.getSyph1());
				aRow.createCell(17).setCellValue(obj.getSyph2());
				aRow.createCell(18).setCellValue(obj.getSyph3());
				aRow.createCell(19).setCellValue(obj.getSyph6());
				aRow.createCell(20).setCellValue(obj.getSyph5());
				aRow.createCell(21).setCellValue("");
				aRow.createCell(22).setCellValue("");
				aRow.createCell(23).setCellValue(obj.getWebAddress());
				aRow.createCell(24).setCellValue(obj.getLocationEmail());
				aRow.createCell(25).setCellValue(obj.getYearEstablished());
				aRow.createCell(26).setCellValue(obj.getProducts());
				aRow.createCell(27).setCellValue(obj.getServices());
				aRow.createCell(28).setCellValue(obj.getBrands());
				aRow.createCell(29).setCellValue(getInfoGroupHours(obj));
				aRow.createCell(30).setCellValue(getAcxiomPaymentTypes(obj));
				aRow.createCell(31).setCellValue(
						getAcxiomLanguages(obj.getLanguages()));
				aRow.createCell(32).setCellValue("");
				aRow.createCell(33).setCellValue("");
				aRow.createCell(34).setCellValue("");
				aRow.createCell(35).setCellValue(obj.getTagline());
				aRow.createCell(36).setCellValue("");
				aRow.createCell(37).setCellValue("");
				aRow.createCell(38).setCellValue(obj.getKeywords());
				aRow.createCell(39).setCellValue("");
				aRow.createCell(40).setCellValue("");
				aRow.createCell(41).setCellValue("");
				aRow.createCell(42).setCellValue("");
				aRow.createCell(43).setCellValue("");
				aRow.createCell(44).setCellValue("");
				aRow.createCell(45).setCellValue("");
				aRow.createCell(46).setCellValue("");
				aRow.createCell(47).setCellValue(
						obj.getProfessionalAssociations());
				aRow.createCell(48).setCellValue("");
				aRow.createCell(49).setCellValue(obj.getAdditionalNumber());
				aRow.createCell(50).setCellValue("");
				aRow.createCell(51).setCellValue("");
				aRow.createCell(52).setCellValue("");

			}

		}
		logger.info("Total Rows : " + rowCount);

	}

	private String getAcxiomPaymentTypes(LocalBusinessDTO localBusinessDTO) {
		StringBuffer creditCards = new StringBuffer();
		if (localBusinessDTO.getaMEX() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getaMEX())) {
			creditCards.append("A");
		}
		if (localBusinessDTO.getMasterCard() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getMasterCard())) {
			creditCards.append("M");
		}
		if (localBusinessDTO.getDiscover() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDiscover())) {
			creditCards.append("D");
		}
		if (localBusinessDTO.getVisa() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getVisa())) {
			creditCards.append("V");
		}
		if (localBusinessDTO.getDinersClub() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDinersClub())) {
			creditCards.append("C");
		}
		if (localBusinessDTO.getCash() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getCash())) {
			creditCards.append("H");
		}
		if (localBusinessDTO.getCheck() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getCheck())) {
			creditCards.append("J");
		}
		if (localBusinessDTO.getDebitCard() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDebitCard())) {
			creditCards.append("E");
		}
		if (localBusinessDTO.getTravelersCheck() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getTravelersCheck())) {
			creditCards.append("T");
		}
		if (localBusinessDTO.getPayPal() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getPayPal())) {
			creditCards.append("P");
		}

		String cards = creditCards.toString();

		return cards;
	}

	private void acxiomUsTemplateTemplateData(HSSFWorkbook workbook,
			List<LocalBusinessDTO> listOfBusiness) {

		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet("errorInfo");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// Master template
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("ListingID");
		header.createCell(1).setCellValue("Vendor_Id");
		header.createCell(2).setCellValue("ClientRecordID");
		header.createCell(3).setCellValue("Record_Status");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("Company_Name");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("Location_Address");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("Location_City");
		header.createCell(7).setCellValue("Location_State");
		header.createCell(8).setCellValue("Location Zip Code");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("Mailing_Address");
		header.createCell(10).setCellValue("Mailing_City");
		header.getCell(10).setCellStyle(style);
		header.createCell(11).setCellValue("Mailing_State");
		header.getCell(11).setCellStyle(style);

		header.createCell(12).setCellValue("Mailing Zip Code");

		header.createCell(13).setCellValue("Landmark_Address");
		header.getCell(13).setCellStyle(style);
		header.createCell(14).setCellValue("Landmark_City");
		header.createCell(15).setCellValue("Landmark_State");
		header.createCell(16).setCellValue("Landmark Zip Code");
		header.createCell(17).setCellValue("Location_Phone");
		header.createCell(18).setCellValue("Fax");
		header.createCell(19).setCellValue("SYPH 1");
		header.createCell(20).setCellValue("SYPH 2");
		header.createCell(21).setCellValue("SYPH 3");
		header.createCell(22).setCellValue("SYPH 4");
		header.createCell(23).setCellValue("SYPH 5");
		header.createCell(24).setCellValue("SYPH 6");
		header.getCell(24).setCellStyle(style);
		header.createCell(25).setCellValue("Franchise/Chain");
		header.createCell(26).setCellValue("Contact First Name");
		header.createCell(27).setCellValue("Contact Last Name");
		header.createCell(28).setCellValue("Contact Gender");
		header.createCell(29).setCellValue("Contact Title");
		header.createCell(30).setCellValue("Contact_Email");
		header.getCell(30).setCellStyle(style);
		header.createCell(31).setCellValue("Company_email");
		header.createCell(32).setCellValue("Employee_Size");
		header.createCell(33).setCellValue("Web Address/URL");
		header.createCell(34).setCellValue("Toll_Free");
		header.createCell(35).setCellValue("Hours Of Operation");
		header.createCell(36).setCellValue("Credit Cards Accepted");
		header.createCell(37).setCellValue("Financing");
		header.createCell(38).setCellValue("Google_Checkout");
		header.createCell(39).setCellValue("Invoice");
		header.createCell(40).setCellValue("Long/Local Webaddress");
		header.createCell(41).setCellValue("Menu Link");
		header.createCell(42).setCellValue("Coupon_Link");
		header.createCell(43).setCellValue("Twitter_Link");
		header.createCell(44).setCellValue("LinkedIn_Link");
		header.createCell(45).setCellValue("Facebook_Link");
		header.createCell(46).setCellValue("Alternate Social Link");
		header.createCell(47).setCellValue("YouTube/Video Link");
		header.createCell(48).setCellValue("Logo_Link");
		header.createCell(49).setCellValue("Pinterest");
		header.createCell(50).setCellValue("Products");
		header.createCell(51).setCellValue("Services");
		header.createCell(52).setCellValue("Brands");
		header.createCell(53).setCellValue("Keywords");
		header.createCell(54).setCellValue("Ordering_Methods");
		header.createCell(55).setCellValue("Business Mobile Number");
		header.createCell(56).setCellValue("Call Tracking Number");
		header.createCell(57).setCellValue("Additional Phone Number");
		header.createCell(58).setCellValue("Parking_Options");
		header.createCell(59).setCellValue("Validation_Options");
		header.createCell(60).setCellValue("Professional_Associations");
		header.createCell(61).setCellValue("Certifications");
		header.createCell(62).setCellValue("ATM");
		header.createCell(63).setCellValue("Languages");
		header.createCell(64).setCellValue("Alternative Business Name");
		header.createCell(65).setCellValue("Anchor/Host Business");
		header.createCell(66).setCellValue("Unofficial_Landmark");
		header.createCell(67).setCellValue("Price_Range");
		header.createCell(68).setCellValue("Reservations");
		header.createCell(69).setCellValue("Banquet/Meeting Rooms");
		header.createCell(70).setCellValue("Restaurant/Bar Types");
		header.createCell(71).setCellValue("Dress_Code");
		header.createCell(72).setCellValue("Shuttle_Service");
		header.createCell(73).setCellValue("Free_Internet");
		header.createCell(74).setCellValue("Internet_Access");
		header.createCell(75).setCellValue("Food_Court");
		header.createCell(76).setCellValue("Park Permit Required");
		header.createCell(77).setCellValue("Equipment_Rentals");
		header.createCell(78).setCellValue("Lodging/Campgrounds");
		header.createCell(79).setCellValue("Live_Entertainment");
		header.createCell(80).setCellValue("Smoking_Preference");
		header.createCell(81).setCellValue("Zagat_Rating");
		header.createCell(82).setCellValue("Gift_Shop");
		header.createCell(83).setCellValue("Presence_of_ECommerce");
		header.createCell(84).setCellValue("Green_Company_Indicator");
		header.createCell(85).setCellValue("Long_Company_Name");
		header.createCell(86).setCellValue("Seasonal_Hours");
		header.createCell(87).setCellValue("DiscountCode");
		header.createCell(88).setCellValue("Delivery Code");
		header.createCell(89).setCellValue("Free_Code");
		header.createCell(90).setCellValue("Year Business Established");
		header.createCell(91).setCellValue("Directions");
		header.createCell(92).setCellValue("Slogans");
		header.createCell(93).setCellValue("ADDRESSPRIVACYFLAG");
		header.createCell(94).setCellValue("SERVICEAREA");
		header.createCell(95).setCellValue("Professionals On Staff");
		header.createCell(96).setCellValue("General_Content");
		// create data rows
		int rowCount = 1;

		for (LocalBusinessDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			String countryCode = obj.getCountryCode();
			if (countryCode != null && countryCode.length() > 0
					&& countryCode.equalsIgnoreCase("US")) {
				aRow.createCell(0).setCellValue("");
				aRow.createCell(1).setCellValue(obj.getClientId());
				aRow.createCell(2).setCellValue(obj.getStore());
				aRow.createCell(3).setCellValue("");
				aRow.createCell(4).setCellValue(obj.getCompanyName());
				StringBuffer locationAddressSuite = new StringBuffer();
				locationAddressSuite.append(obj.getLocationAddress());
				locationAddressSuite.append(", ");
				locationAddressSuite.append(obj.getSuite());
				aRow.createCell(5)
						.setCellValue(locationAddressSuite.toString());
				aRow.createCell(6).setCellValue(obj.getLocationCity());
				aRow.createCell(7).setCellValue(obj.getLocationState());
				aRow.createCell(8).setCellValue(obj.getLocationZipCode());
				aRow.createCell(9).setCellValue("");
				aRow.createCell(10).setCellValue("");
				aRow.createCell(11).setCellValue("");
				aRow.createCell(12).setCellValue("");
				aRow.createCell(13).setCellValue("");
				aRow.createCell(14).setCellValue("");
				aRow.createCell(15).setCellValue("");
				aRow.createCell(16).setCellValue("");
				aRow.createCell(17).setCellValue(obj.getLocationPhone());
				aRow.createCell(18).setCellValue(obj.getFax());
				aRow.createCell(19).setCellValue(obj.getSyph1());
				aRow.createCell(20).setCellValue(obj.getSyph2());
				aRow.createCell(21).setCellValue(obj.getSyph3());
				aRow.createCell(22).setCellValue(obj.getSyph4());
				aRow.createCell(23).setCellValue(obj.getSyph5());
				aRow.createCell(24).setCellValue("");
				aRow.createCell(25).setCellValue("");
				aRow.createCell(26).setCellValue(
						obj.getPrimaryContactFirstName());
				aRow.createCell(27).setCellValue(
						obj.getPrimaryContactLastName());
				aRow.createCell(28).setCellValue("");
				aRow.createCell(29).setCellValue(obj.getContactTitle());
				aRow.createCell(30).setCellValue(obj.getContactEmail());
				aRow.createCell(31).setCellValue(obj.getLocationEmail());
				aRow.createCell(32).setCellValue(obj.getLocationEmployeeSize());
				aRow.createCell(33).setCellValue(obj.getWebAddress());
				aRow.createCell(34).setCellValue(obj.getTollFree());
				aRow.createCell(35).setCellValue(getInfoGroupHours(obj));
				aRow.createCell(36).setCellValue(getAcxiomPaymentTypes(obj));
				aRow.createCell(37).setCellValue(obj.getFinancing());
				aRow.createCell(38).setCellValue(obj.getGoogleCheckout());
				aRow.createCell(39).setCellValue(obj.getInvoice());
				aRow.createCell(40).setCellValue(obj.getWebAddress());
				aRow.createCell(41).setCellValue(obj.getMenuLink());
				aRow.createCell(42).setCellValue(obj.getCouponLink());
				aRow.createCell(43).setCellValue(obj.getTwitterLink());
				aRow.createCell(44).setCellValue(obj.getLinkedInLink());
				aRow.createCell(45).setCellValue(obj.getFacebookLink());
				aRow.createCell(46).setCellValue(obj.getAlternateSocialLink());
				aRow.createCell(47).setCellValue(obj.getYouTubeOrVideoLink());
				aRow.createCell(48).setCellValue(obj.getLogoLink());
				aRow.createCell(49).setCellValue(obj.getPinteristLink());
				aRow.createCell(50).setCellValue(obj.getProducts());
				aRow.createCell(51).setCellValue(obj.getServices());
				aRow.createCell(52).setCellValue(obj.getBrands());
				aRow.createCell(53).setCellValue(obj.getKeywords());
				aRow.createCell(54).setCellValue("");
				aRow.createCell(55).setCellValue(obj.getMobileNumber());
				aRow.createCell(56).setCellValue("");
				aRow.createCell(57).setCellValue(obj.getAdditionalNumber());
				aRow.createCell(58).setCellValue("");
				aRow.createCell(59).setCellValue("");
				aRow.createCell(60).setCellValue(
						obj.getProfessionalAssociations());
				aRow.createCell(61).setCellValue("");
				aRow.createCell(62).setCellValue("");
				aRow.createCell(63).setCellValue(obj.getLanguages());
				aRow.createCell(64).setCellValue(obj.getAlternativeName());
				aRow.createCell(65).setCellValue(obj.getMobileNumber());
				aRow.createCell(66).setCellValue("");
				aRow.createCell(67).setCellValue(obj.getAdditionalNumber());
				aRow.createCell(68).setCellValue("");
				aRow.createCell(69).setCellValue("");
				aRow.createCell(70).setCellValue("");
				aRow.createCell(71).setCellValue(
						obj.getProfessionalAssociations());
				aRow.createCell(72).setCellValue("");
				aRow.createCell(73).setCellValue(
						getAcxiomLanguages(obj.getLanguages()));
				aRow.createCell(74).setCellValue(obj.getAlternativeName());
				aRow.createCell(75).setCellValue(obj.getAnchorOrHostBusiness());
				aRow.createCell(76).setCellValue("");
				aRow.createCell(77).setCellValue("");
				aRow.createCell(78).setCellValue("");
				aRow.createCell(79).setCellValue("");
				aRow.createCell(80).setCellValue("");
				aRow.createCell(81).setCellValue("");
				aRow.createCell(82).setCellValue("");
				aRow.createCell(83).setCellValue("");
				aRow.createCell(84).setCellValue("");
				aRow.createCell(85).setCellValue(obj.getCompanyName());
				aRow.createCell(86).setCellValue("");
				aRow.createCell(87).setCellValue("");
				aRow.createCell(88).setCellValue("");
				aRow.createCell(89).setCellValue("");
				aRow.createCell(90).setCellValue(obj.getYearEstablished());
				aRow.createCell(91).setCellValue("");
				aRow.createCell(92).setCellValue(obj.getTagline());
				aRow.createCell(93).setCellValue(obj.getADDRESSPRIVACYFLAG());
				aRow.createCell(94).setCellValue(obj.getServiceArea());
				aRow.createCell(95).setCellValue("");
				aRow.createCell(96).setCellValue(obj.getBusinessDescription());
			}

		}
		logger.info("Total Rows : " + rowCount);

	}

	private void yextTemplateData(HSSFWorkbook workbook,
			List<LocalBusinessDTO> listOfBusiness) {

		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet("errorInfo");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// Master template
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Store ID");
		header.createCell(1).setCellValue("Name");
		header.createCell(2).setCellValue("Address");
		header.createCell(3).setCellValue("Address2");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("City");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("State");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("Zip");
		header.createCell(7).setCellValue("Latitude");
		header.createCell(8).setCellValue("Longitude");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("Main Phone");
		header.createCell(10).setCellValue("Local Phone");
		header.getCell(10).setCellStyle(style);
		header.createCell(11).setCellValue("Toll-Free Phone");
		header.getCell(11).setCellStyle(style);
		header.createCell(12).setCellValue("Fax Number");
		header.getCell(12).setCellStyle(style);
		header.createCell(13).setCellValue("Alternate Phone");
		header.createCell(14).setCellValue("TTY Phone");
		header.createCell(15).setCellValue("Email");
		header.createCell(16).setCellValue("Website");
		header.createCell(17).setCellValue("Reservation URL");
		header.createCell(18).setCellValue("Order URL");
		header.createCell(19).setCellValue("Menu URL");
		header.createCell(20).setCellValue("Featured Message Text");
		header.createCell(21).setCellValue("Linkable Featured Message URL");
		header.createCell(22).setCellValue("Business Description");
		header.createCell(23).setCellValue("Hours");
		header.getCell(23).setCellStyle(style);
		header.createCell(24).setCellValue("Logo URL");
		header.createCell(25).setCellValue("Photo1 URL");
		header.createCell(26).setCellValue("Photo2 URL");
		header.createCell(27).setCellValue("Photo3 URL");
		header.createCell(28).setCellValue("Photo4 URL");
		header.createCell(29).setCellValue("Photo5 URL");
		header.getCell(29).setCellStyle(style);
		header.createCell(30).setCellValue("YouTube Video URL");
		header.createCell(31).setCellValue(
				"Categories (prioritize by importance)");
		header.createCell(32).setCellValue("Payment Methods");
		header.createCell(33).setCellValue("Brands");
		header.createCell(34).setCellValue("Products");
		header.createCell(35).setCellValue("Services");
		header.createCell(36).setCellValue("Specialties");
		header.createCell(37).setCellValue("Languages");
		header.createCell(38).setCellValue("Associations");
		header.createCell(39).setCellValue("Year Established");
		header.createCell(40).setCellValue("Twitter");
		header.createCell(41).setCellValue("Facebook");
		header.createCell(42).setCellValue("Custom1");
		header.createCell(43).setCellValue("Custom2");

		// create data rows
		int rowCount = 1;

		for (LocalBusinessDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(obj.getStore());
			aRow.createCell(1).setCellValue(obj.getCompanyName());
			aRow.createCell(2).setCellValue(obj.getLocationAddress());
			aRow.createCell(3).setCellValue(obj.getSuite());
			aRow.createCell(4).setCellValue(obj.getLocationCity());
			aRow.createCell(5).setCellValue(obj.getLocationState());
			aRow.createCell(6).setCellValue(obj.getLocationZipCode());
			aRow.createCell(7).setCellValue("");
			aRow.createCell(8).setCellValue("");
			aRow.createCell(9).setCellValue(obj.getLocationPhone());
			aRow.createCell(10).setCellValue(obj.getMobileNumber());
			aRow.createCell(11).setCellValue(obj.getTollFree());
			aRow.createCell(12).setCellValue(obj.getFax());
			aRow.createCell(13).setCellValue(obj.getAdditionalNumber());
			aRow.createCell(14).setCellValue(obj.getTty());
			aRow.createCell(15).setCellValue(obj.getLocationEmail());
			aRow.createCell(16).setCellValue(obj.getWebAddress());
			aRow.createCell(17).setCellValue("");
			aRow.createCell(18).setCellValue("");
			aRow.createCell(19).setCellValue(obj.getMenuLink());
			aRow.createCell(20).setCellValue("");
			aRow.createCell(21).setCellValue("");
			aRow.createCell(22).setCellValue(obj.getBusinessDescription());
			aRow.createCell(23).setCellValue(getHours(obj));
			aRow.createCell(24).setCellValue(obj.getLogoLink());
			aRow.createCell(25).setCellValue("");
			aRow.createCell(26).setCellValue("");
			aRow.createCell(27).setCellValue("");
			aRow.createCell(28).setCellValue("");
			aRow.createCell(29).setCellValue("");
			aRow.createCell(30).setCellValue(obj.getYouTubeOrVideoLink());
			aRow.createCell(31).setCellValue(obj.getCategory1());
			aRow.createCell(32).setCellValue(getPaymentTypes(obj));
			aRow.createCell(33).setCellValue(obj.getBrands());
			aRow.createCell(34).setCellValue(obj.getProducts());
			aRow.createCell(35).setCellValue(obj.getServices());
			aRow.createCell(36).setCellValue(obj.getKeywords());
			aRow.createCell(37).setCellValue(obj.getLanguages());
			aRow.createCell(38).setCellValue(obj.getProfessionalAssociations());
			aRow.createCell(39).setCellValue(obj.getYearEstablished());
			aRow.createCell(40).setCellValue(obj.getTwitterLink());
			aRow.createCell(41).setCellValue(obj.getFacebookLink());
			aRow.createCell(42).setCellValue("");
			aRow.createCell(43).setCellValue("");

		}
		logger.info("Total Rows : " + rowCount);

	}

	private void yelpTemplateData(HSSFWorkbook workbook,
			List<LocalBusinessDTO> listOfBusiness) {

		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet("template");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// Google Template
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("StoreCode");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("GroupName");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("BusinessName");
		header.getCell(2).setCellStyle(style);
		header.createCell(3).setCellValue("BusinessStreetAddress1");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("BusinessStreetAddress2");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("BusinessCity");
		header.createCell(6).setCellValue("BusinessState");
		header.getCell(6).setCellStyle(style);
		header.createCell(7).setCellValue("BusinessZipCode");
		header.getCell(7).setCellStyle(style);
		header.createCell(8).setCellValue("BusinessCountry");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("BusinessPhoneNumber");
		header.getCell(9).setCellStyle(style);
		header.createCell(10).setCellValue("BusinessHours");
		header.getCell(10).setCellStyle(style);
		header.createCell(11).setCellValue("BusinessDisplayUrl");
		header.getCell(11).setCellStyle(style);
		header.createCell(12).setCellValue("BusinessUrl");
		header.getCell(12).setCellStyle(style);
		header.createCell(13).setCellValue("YelpCategory1");
		header.createCell(14).setCellValue("YelpCategory2");
		header.createCell(15).setCellValue("YelpCategory3");
		header.createCell(16).setCellValue("ServiceArea");
		header.createCell(17).setCellValue("BusinessOwnerFirstName");
		header.createCell(18).setCellValue("BusinessOwnerLastName");
		header.createCell(19).setCellValue("BusinessOwnerEmail");
		header.createCell(20).setCellValue("Business Owner Mobile Phone");
		header.createCell(21).setCellValue("AboutThisBizSpecialties");
		header.createCell(22).setCellValue("AboutThisBizYearEstablished");
		header.createCell(23).setCellValue("AboutThisBizHistory");
		header.createCell(24).setCellValue("AboutThisBizRole");
		header.createCell(25).setCellValue("AboutThisBizBio");
		header.createCell(26).setCellValue("AboutThisBizBioFirstName");
		header.createCell(27).setCellValue("AboutThisBizBioLastName");
		header.createCell(28).setCellValue("DesktopCtaButtonText");
		header.createCell(29).setCellValue("DesktopCtaText");
		header.createCell(30).setCellValue("DesktopCtaUrl");
		header.createCell(31).setCellValue("DesktopCtaEndDate");
		header.createCell(32).setCellValue("MobileCtaActionText");
		header.createCell(33).setCellValue("MobileCtaDescText");
		header.createCell(34).setCellValue("MobileCtaUrl");
		header.createCell(35).setCellValue("MobileCtaEndDate");

		// create data rows
		int rowCount = 1;
		for (LocalBusinessDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(obj.getStore());
			aRow.createCell(1).setCellValue(obj.getClient());
			aRow.createCell(2).setCellValue(obj.getCompanyName());
			aRow.createCell(3).setCellValue(obj.getLocationAddress());
			aRow.createCell(4).setCellValue(obj.getSuite());
			aRow.createCell(5).setCellValue(obj.getLocationCity());
			aRow.createCell(6).setCellValue(obj.getLocationState());
			aRow.createCell(7).setCellValue(obj.getLocationZipCode());
			aRow.createCell(8).setCellValue(obj.getCountryCode());
			aRow.createCell(9).setCellValue(obj.getLocationPhone());
			aRow.createCell(10).setCellValue(getYPHours(obj));
			aRow.createCell(11).setCellValue(obj.getWebAddress());
			aRow.createCell(12).setCellValue(obj.getWebAddress());
			aRow.createCell(13).setCellValue(obj.getCategory1());
			aRow.createCell(14).setCellValue(obj.getCategory2());
			aRow.createCell(15).setCellValue(obj.getCategory3());
			aRow.createCell(16).setCellValue(obj.getServiceArea());
			aRow.createCell(17).setCellValue(obj.getPrimaryContactFirstName());
			aRow.createCell(18).setCellValue(obj.getPrimaryContactLastName());
			aRow.createCell(19).setCellValue("");
			aRow.createCell(20).setCellValue("");
			aRow.createCell(21).setCellValue(obj.getBusinessDescription());
			aRow.createCell(22).setCellValue(obj.getYearEstablished());
			aRow.createCell(23).setCellValue(
					obj.getProductsOrServices_combined());
			aRow.createCell(24).setCellValue("");
			aRow.createCell(25).setCellValue("");
			aRow.createCell(26).setCellValue("");
			aRow.createCell(27).setCellValue("");
			aRow.createCell(28).setCellValue("");
			aRow.createCell(29).setCellValue("");
			aRow.createCell(30).setCellValue("");
			aRow.createCell(31).setCellValue("");
			aRow.createCell(32).setCellValue("");
			aRow.createCell(33).setCellValue("");
			aRow.createCell(34).setCellValue("");
			aRow.createCell(35).setCellValue("");

		}
		logger.info("Total Rows : " + rowCount);

	}

	public static boolean is24hours(LocalBusinessDTO localBusinessDTO) {
		boolean is24hours = false;
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
		List<String> hours = new ArrayList<String>();
		hours.add(sunOpen);
		hours.add(sunClose);
		hours.add(satClose);
		hours.add(satOpen);
		hours.add(fridayOpen);
		hours.add(fridayClose);
		hours.add(thursdayClose);
		hours.add(thursdayOpen);
		hours.add(wedClose);
		hours.add(wedOpen);
		hours.add(tuuesdayClose);
		hours.add(tuesdayOpen);
		hours.add(mondayOpen);
		hours.add(mondayClose);
		if (hours.contains("CLOSE")) {
			is24hours = false;
		} else {
			boolean sunis24hours = false;
			boolean monis24hours = false;
			boolean tuesis24hours = false;
			boolean wedis24hours = false;
			boolean thuis24hours = false;
			boolean friis24hours = false;
			boolean satis24hours = false;
			if (sunOpen != null && sunClose != null && mondayOpen != null
					&& mondayClose != null && tuesdayOpen != null
					&& tuuesdayClose != null && wedOpen != null
					&& wedClose != null && thursdayOpen != null
					&& thursdayClose != null && fridayOpen != null
					&& fridayClose != null && satOpen != null
					&& satClose != null) {
				if (sunOpen.equals("00:00") && sunClose.equals("23:59")) {
					sunis24hours = true;
				}
				if (mondayOpen.equals("00:00") && mondayClose.equals("23:59")) {
					monis24hours = true;
				}
				if (tuesdayOpen.equals("00:00")
						&& tuuesdayClose.equals("23:59")) {
					tuesis24hours = true;
				}
				if (wedOpen.equals("00:00") && wedClose.equals("23:59")) {
					wedis24hours = true;
				}
				if (thursdayOpen.equals("00:00")
						&& thursdayClose.equals("23:59")) {
					thuis24hours = true;
				}
				if (fridayOpen.equals("00:00") && fridayClose.equals("23:59")) {
					friis24hours = true;
				}
				if (satOpen.equals("00:00") && satClose.equals("23:59")) {
					satis24hours = true;
				}
				if (sunis24hours && monis24hours && tuesis24hours
						&& wedis24hours && thuis24hours && friis24hours
						&& satis24hours) {
					is24hours = true;
				}
			} else {
				is24hours = false;
			}

		}

		return is24hours;

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

		if ("CLOSE".equalsIgnoreCase(sunOpen)
				|| "CLOSE".equalsIgnoreCase(sunClose)) {
			// workingHours.append("Sun CLOSE,  ");
		} else if (sunOpen != null && sunOpen.trim().length() > 0
				&& sunClose != null && sunClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(sunOpen);
				Date date = formatter.parse(sunClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();

				cal.setTime(dt);
				cal2.setTime(date);

				int hour = cal.get(Calendar.HOUR);

				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);

				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}

					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Sun " + time + "-" + close + ", ");
		}
		if ("CLOSE".equalsIgnoreCase(mondayOpen)
				|| "CLOSE".equalsIgnoreCase(mondayClose)) {
			// workingHours.append("Mon CLOSE, ");
		}

		else if (mondayOpen != null && mondayOpen.trim().length() > 0
				&& mondayClose != null && mondayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(mondayOpen);
				Date date = formatter.parse(mondayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Mon " + time + "-" + close + ", ");
		}
		if ("CLOSE".equalsIgnoreCase(tuesdayOpen)
				|| "CLOSE".equalsIgnoreCase(tuuesdayClose)) {
			// workingHours.append("Tue CLOSE, ");
		} else if (tuesdayOpen != null && tuesdayOpen.trim().length() > 0
				&& tuuesdayClose != null && tuuesdayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(tuesdayOpen);
				Date date = formatter.parse(tuuesdayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Tue " + time + "-" + close + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(wedOpen)
				|| "CLOSE".equalsIgnoreCase(wedClose)) {
			// workingHours.append("Wed CLOSE, ");
		} else if (wedOpen != null && wedOpen.trim().length() > 0
				&& wedClose != null && wedClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(wedOpen);
				Date date = formatter.parse(wedClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Wed " + time + "-" + close + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(thursdayOpen)
				|| "CLOSE".equalsIgnoreCase(thursdayClose)) {
			// workingHours.append("Thu CLOSE, ");
		} else if (thursdayOpen != null && thursdayOpen.trim().length() > 0
				&& thursdayClose != null && thursdayClose.trim().length() > 0) {
			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(thursdayOpen);
				Date date = formatter.parse(thursdayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

			workingHours.append("Thu " + time + "-" + close + ", ");

		}

		if ("CLOSE".equalsIgnoreCase(fridayOpen)
				|| "CLOSE".equalsIgnoreCase(fridayClose)) {
			// workingHours.append("Fri CLOSE, ");
		} else if (fridayOpen != null && fridayOpen.trim().length() > 0
				&& fridayClose != null && fridayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(fridayOpen);
				Date date = formatter.parse(fridayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Fri " + time + "-" + close + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(satOpen)
				|| "CLOSE".equalsIgnoreCase(satClose)) {
			// workingHours.append("Sat CLOSE, ");
		} else if (satOpen != null && satOpen.trim().length() > 0
				&& satClose != null && satClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(satOpen);
				Date date = formatter.parse(satClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Sat " + time + "-" + close);
		}
		String hours = workingHours.toString();
		if (hours != null && hours.endsWith(", ")) {
			hours = hours.substring(0, hours.length() - 2);
		}
		return hours;

	}

	public static String getGoogleHours(LocalBusinessDTO localBusinessDTO) {
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

		if ("CLOSE".equalsIgnoreCase(sunOpen)
				|| "CLOSE".equalsIgnoreCase(sunClose)) {
			// workingHours.append("Sun CLOSE,  ");
		} else if (sunOpen != null && sunOpen.trim().length() > 0
				&& sunClose != null && sunClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(sunOpen);
				Date date = formatter.parse(sunClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();

				cal.setTime(dt);
				cal2.setTime(date);

				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2);
				}
				if (i == 1) {
					time = 12 + hour + ":" + String.format("%02d", minutes2);
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes);
				}
				if (j == 1) {
					close = 12 + hour2 + ":" + String.format("%02d", minutes);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("1" + ":" + time + ":" + close + ",");
		}
		if ("CLOSE".equalsIgnoreCase(mondayOpen)
				|| "CLOSE".equalsIgnoreCase(mondayClose)) {
			// workingHours.append("Mon CLOSE, ");
		}

		else if (mondayOpen != null && mondayOpen.trim().length() > 0
				&& mondayClose != null && mondayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(mondayOpen);
				Date date = formatter.parse(mondayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2);
				}
				if (i == 1) {
					time = 12 + hour + ":" + String.format("%02d", minutes2);
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes);
				}
				if (j == 1) {
					close = 12 + hour2 + ":" + String.format("%02d", minutes);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("2" + ":" + time + ":" + close + ",");
		}
		if ("CLOSE".equalsIgnoreCase(tuesdayOpen)
				|| "CLOSE".equalsIgnoreCase(tuuesdayClose)) {
			// workingHours.append("Tue CLOSE, ");
		} else if (tuesdayOpen != null && tuesdayOpen.trim().length() > 0
				&& tuuesdayClose != null && tuuesdayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(tuesdayOpen);
				Date date = formatter.parse(tuuesdayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2);
				}
				if (i == 1) {
					time = 12 + hour + ":" + String.format("%02d", minutes2);
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes);
				}
				if (j == 1) {
					close = 12 + hour2 + ":" + String.format("%02d", minutes);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("3" + ":" + time + ":" + close + ",");
		}

		if ("CLOSE".equalsIgnoreCase(wedOpen)
				|| "CLOSE".equalsIgnoreCase(wedClose)) {
			// workingHours.append("Wed CLOSE, ");
		} else if (wedOpen != null && wedOpen.trim().length() > 0
				&& wedClose != null && wedClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(wedOpen);
				Date date = formatter.parse(wedClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2);
				}
				if (i == 1) {
					time = 12 + hour + ":" + String.format("%02d", minutes2);
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes);
				}
				if (j == 1) {
					close = 12 + hour2 + ":" + String.format("%02d", minutes);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("4" + ":" + time + ":" + close + ",");
		}

		if ("CLOSE".equalsIgnoreCase(thursdayOpen)
				|| "CLOSE".equalsIgnoreCase(thursdayClose)) {
			// workingHours.append("Thu CLOSE, ");
		} else if (thursdayOpen != null && thursdayOpen.trim().length() > 0
				&& thursdayClose != null && thursdayClose.trim().length() > 0) {
			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(thursdayOpen);
				Date date = formatter.parse(thursdayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2);
				}
				if (i == 1) {
					time = 12 + hour + ":" + String.format("%02d", minutes2);
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes);
				}
				if (j == 1) {
					close = 12 + hour2 + ":" + String.format("%02d", minutes);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

			workingHours.append("5" + ":" + time + ":" + close + ",");

		}

		if ("CLOSE".equalsIgnoreCase(fridayOpen)
				|| "CLOSE".equalsIgnoreCase(fridayClose)) {
			// workingHours.append("Fri CLOSE, ");
		} else if (fridayOpen != null && fridayOpen.trim().length() > 0
				&& fridayClose != null && fridayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(fridayOpen);
				Date date = formatter.parse(fridayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2);
				}
				if (i == 1) {
					time = 12 + hour + ":" + String.format("%02d", minutes2);
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes);
				}
				if (j == 1) {
					close = 12 + hour2 + ":" + String.format("%02d", minutes);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("6" + ":" + time + ":" + close + ",");
		}

		if ("CLOSE".equalsIgnoreCase(satOpen)
				|| "CLOSE".equalsIgnoreCase(satClose)) {
			// workingHours.append("Sat CLOSE, ");
		} else if (satOpen != null && satOpen.trim().length() > 0
				&& satClose != null && satClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(satOpen);
				Date date = formatter.parse(satClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2);
				}
				if (i == 1) {
					time = 12 + hour + ":" + String.format("%02d", minutes2);
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes);
				}
				if (j == 1) {
					close = 12 + hour2 + ":" + String.format("%02d", minutes);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("7" + ":" + time + ":" + close);
		}

		String hours = workingHours.toString();
		if (hours != null && hours.endsWith(",")) {
			hours = hours.substring(0, hours.length() - 1);
		}
		return hours;

	}

	public static String getYPHours(LocalBusinessDTO localBusinessDTO) {
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

		if ("CLOSE".equalsIgnoreCase(sunOpen)
				|| "CLOSE".equalsIgnoreCase(sunClose)) {
			// workingHours.append("Sun CLOSE,  ");
		} else if (sunOpen != null && sunOpen.trim().length() > 0
				&& sunClose != null && sunClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(sunOpen);
				Date date = formatter.parse(sunClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();

				cal.setTime(dt);
				cal2.setTime(date);

				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Sun " + time + " - " + close + ", ");
		}
		if ("CLOSE".equalsIgnoreCase(mondayOpen)
				|| "CLOSE".equalsIgnoreCase(mondayClose)) {
			// workingHours.append("Mon CLOSE, ");
		}

		else if (mondayOpen != null && mondayOpen.trim().length() > 0
				&& mondayClose != null && mondayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(mondayOpen);
				Date date = formatter.parse(mondayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Mon " + time + " - " + close + ", ");
		}
		if ("CLOSE".equalsIgnoreCase(tuesdayOpen)
				|| "CLOSE".equalsIgnoreCase(tuuesdayClose)) {
			// workingHours.append("Tue CLOSE, ");
		} else if (tuesdayOpen != null && tuesdayOpen.trim().length() > 0
				&& tuuesdayClose != null && tuuesdayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(tuesdayOpen);
				Date date = formatter.parse(tuuesdayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Tue " + time + " - " + close + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(wedOpen)
				|| "CLOSE".equalsIgnoreCase(wedClose)) {
			// workingHours.append("Wed CLOSE, ");
		} else if (wedOpen != null && wedOpen.trim().length() > 0
				&& wedClose != null && wedClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(wedOpen);
				Date date = formatter.parse(wedClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Wed " + time + " - " + close + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(thursdayOpen)
				|| "CLOSE".equalsIgnoreCase(thursdayClose)) {
			// workingHours.append("Thu CLOSE, ");
		} else if (thursdayOpen != null && thursdayOpen.trim().length() > 0
				&& thursdayClose != null && thursdayClose.trim().length() > 0) {
			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(thursdayOpen);
				Date date = formatter.parse(thursdayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

			workingHours.append("Thu " + time + " - " + close + ", ");

		}

		if ("CLOSE".equalsIgnoreCase(fridayOpen)
				|| "CLOSE".equalsIgnoreCase(fridayClose)) {
			// workingHours.append("Fri CLOSE, ");
		} else if (fridayOpen != null && fridayOpen.trim().length() > 0
				&& fridayClose != null && fridayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(fridayOpen);
				Date date = formatter.parse(fridayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Fri " + time + " - " + close + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(satOpen)
				|| "CLOSE".equalsIgnoreCase(satClose)) {
			// workingHours.append("Sat CLOSE, ");
		} else if (satOpen != null && satOpen.trim().length() > 0
				&& satClose != null && satClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(satOpen);
				Date date = formatter.parse(satClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Sat " + time + " - " + close);
		}
		String hours = workingHours.toString();
		if (hours != null && hours.endsWith(", ")) {
			hours = hours.substring(0, hours.length() - 2);
		}

		return hours;

	}

	public String getPaymentTypesForYp(LocalBusinessDTO localBusinessDTO) {
		StringBuffer creditCards = new StringBuffer();

		if (localBusinessDTO.getaMEX() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getaMEX())) {
			creditCards.append("AMEX");
		}
		if (localBusinessDTO.getMasterCard() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getMasterCard())) {
			creditCards.append(",Master Card");
		}
		if (localBusinessDTO.getDiscover() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDiscover())) {
			creditCards.append(",Discover");
		}
		if (localBusinessDTO.getVisa() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getVisa())) {
			creditCards.append(",Visa");
		}
		if (localBusinessDTO.getDinersClub() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDinersClub())) {
			creditCards.append(",Diners Club");
		}
		if (localBusinessDTO.getCash() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getCash())) {
			creditCards.append(",Cash");
		}
		if (localBusinessDTO.getCheck() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getCheck())) {
			creditCards.append(",Check");
		}
		if (localBusinessDTO.getDebitCard() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDebitCard())) {
			creditCards.append(",Debit card");
		}
		if (localBusinessDTO.getTravelersCheck() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getTravelersCheck())) {
			creditCards.append(",Traveler’s Check");
		}
		if (localBusinessDTO.getFinancing() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getFinancing())) {
			creditCards.append(",Financing");
		}
		if (localBusinessDTO.getInvoice() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getInvoice())) {
			creditCards.append(",Invoice");
		}
		String cards = creditCards.toString();
		if (cards != null && cards.startsWith(",")) {
			cards = cards.replaceFirst(",", "");
		}

		return cards;
	}

	public String getPaymentTypesForGoogle(LocalBusinessDTO localBusinessDTO) {
		StringBuffer creditCards = new StringBuffer();

		if (localBusinessDTO.getaMEX() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getaMEX())) {
			creditCards.append("");
		}
		if (localBusinessDTO.getMasterCard() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getMasterCard())) {
			creditCards.append("");
		}
		if (localBusinessDTO.getDiscover() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDiscover())) {
			creditCards.append("");
		}
		if (localBusinessDTO.getVisa() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getVisa())) {
			creditCards.append("");
		}
		if (localBusinessDTO.getDinersClub() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDinersClub())) {
			creditCards.append("");
		}
		if (localBusinessDTO.getCash() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getCash())) {
			if (!"Y".equalsIgnoreCase(localBusinessDTO.getaMEX())
					&& !"Y".equalsIgnoreCase(localBusinessDTO.getMasterCard())
					&& !"Y".equalsIgnoreCase(localBusinessDTO.getDiscover())
					&& !"Y".equalsIgnoreCase(localBusinessDTO.getVisa())
					&& !"Y".equalsIgnoreCase(localBusinessDTO.getDinersClub())
					&& !"Y".equalsIgnoreCase(localBusinessDTO.getCheck())
					&& !"Y".equalsIgnoreCase(localBusinessDTO.getDebitCard())
					&& !"Y".equalsIgnoreCase(localBusinessDTO
							.getTravelersCheck())
					&& !"Y".equalsIgnoreCase(localBusinessDTO.getFinancing())
					&& !"Y".equalsIgnoreCase(localBusinessDTO.getInvoice())) {
				creditCards.append("Cash");
			}

		}
		if (localBusinessDTO.getCheck() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getCheck())) {
			creditCards.append("");
		}
		if (localBusinessDTO.getDebitCard() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDebitCard())) {
			creditCards.append("");
		}
		if (localBusinessDTO.getTravelersCheck() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getTravelersCheck())) {
			creditCards.append("");
		}
		if (localBusinessDTO.getFinancing() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getFinancing())) {
			creditCards.append("");
		}
		if (localBusinessDTO.getInvoice() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getInvoice())) {
			creditCards.append("");
		}
		String cards = creditCards.toString();

		return cards;
	}

	public String getPaymentTypes(LocalBusinessDTO localBusinessDTO) {
		StringBuffer creditCards = new StringBuffer();
		if (localBusinessDTO.getaMEX() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getaMEX())) {
			creditCards.append("American Express");
		}
		if (localBusinessDTO.getMasterCard() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getMasterCard())) {
			creditCards.append(",MasterCard");
		}
		if (localBusinessDTO.getDiscover() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDiscover())) {
			creditCards.append(",Discover");
		}
		if (localBusinessDTO.getVisa() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getVisa())) {
			creditCards.append(",Visa");
		}
		if (localBusinessDTO.getDinersClub() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDinersClub())) {
			creditCards.append(",Diner’s Club");
		}
		if (localBusinessDTO.getCash() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getCash())) {
			creditCards.append(",Cash");
		}
		if (localBusinessDTO.getCheck() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getCheck())) {
			creditCards.append(",Check");
		}
		if (localBusinessDTO.getDebitCard() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDebitCard())) {
			creditCards.append(",Debit card");
		}
		if (localBusinessDTO.getTravelersCheck() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getTravelersCheck())) {
			creditCards.append(",Traveler’s Check");
		}
		if (localBusinessDTO.getFinancing() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getFinancing())) {
			creditCards.append(",Financing");
		}
		if (localBusinessDTO.getInvoice() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getInvoice())) {
			creditCards.append(",Invoice");
		}
		String cards = creditCards.toString();
		if (cards != null && cards.startsWith(",")) {
			cards = cards.replaceFirst(",", "");
		}
		return cards;
	}

	private void ypTemplateData(HSSFWorkbook workbook,
			List<LocalBusinessDTO> listOfBusiness) {
		HSSFSheet sheet = workbook.createSheet("template");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");

		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// create header row
		// BingPlacesSample Template
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("national_id");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("listed_name");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("account_ring_to");
		header.getCell(2).setCellStyle(style);
		header.createCell(3).setCellValue("account_ctn");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("omit_address_ind");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("heading");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("street");
		header.getCell(6).setCellStyle(style);
		header.createCell(7).setCellValue("post_code");
		header.getCell(7).setCellStyle(style);
		header.createCell(8).setCellValue("email_address");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("url");
		header.getCell(9).setCellStyle(style);
		header.createCell(10).setCellValue("omit_review_ind");
		header.getCell(10).setCellStyle(style);
		header.createCell(11).setCellValue("omit_tn_ind");
		header.createCell(12).setCellValue("payment_types");
		header.createCell(13).setCellValue("hoo");
		header.createCell(14).setCellValue("additional_phone");
		header.createCell(15).setCellValue("additional_phone_desc");
		header.createCell(16).setCellValue("tollfree_phone");
		header.createCell(17).setCellValue("tollfree_phone_desc");
		header.createCell(18).setCellValue("fax");
		header.createCell(19).setCellValue("extra_email");
		header.createCell(20).setCellValue("extra_email_desc");
		header.createCell(21).setCellValue("extra_url");
		header.createCell(22).setCellValue("extra_url_desc");
		header.createCell(23).setCellValue("location_desc");
		header.createCell(24).setCellValue("slogan");
		header.createCell(25).setCellValue("general_desc");
		header.createCell(26).setCellValue("services_products");
		header.createCell(27).setCellValue("brands");
		header.createCell(28).setCellValue("amenities");
		header.createCell(29).setCellValue("associations");
		header.createCell(30).setCellValue("languages_spoken");
		header.createCell(31).setCellValue("in_business_since");
		header.createCell(32).setCellValue("affiliations");
		header.createCell(33).setCellValue("store_front_photo");
		/* header.createCell(34).setCellValue("YP internet HEADING"); */

		// create data rows
		int rowCount = 1;
		for (LocalBusinessDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue("");
			aRow.createCell(1).setCellValue(obj.getCompanyName());
			aRow.createCell(2).setCellValue(obj.getLocationPhone());
			aRow.createCell(3).setCellValue("");
			aRow.createCell(4).setCellValue("0");
			String ypInternetHeading = obj.getYpInternetHeading();
			aRow.createCell(5).setCellValue(ypInternetHeading);
			aRow.createCell(6).setCellValue(obj.getLocationAddress());
			aRow.createCell(7).setCellValue(obj.getLocationZipCode());
			aRow.createCell(8).setCellValue(obj.getLocationEmail());
			aRow.createCell(9).setCellValue(obj.getWebAddress());
			aRow.createCell(10).setCellValue("N");
			aRow.createCell(11).setCellValue("N");
			aRow.createCell(12).setCellValue(getPaymentTypesForYp(obj));
			String hours = getHours(obj);
			StringBuffer hour = new StringBuffer();
			hour.append("Store");
			hour.append("#");
			hour.append(" ");
			hour.append(obj.getStore());
			hour.append(" ");
			hour.append("-");
			hour.append(" ");
			hour.append(hours);
			String hoo = hour.toString();

			aRow.createCell(13).setCellValue(getYPHours(obj));

			String additionalNumber = obj.getAdditionalNumber();
			aRow.createCell(14).setCellValue(additionalNumber);
			if (additionalNumber != null && additionalNumber != "") {
				aRow.createCell(15).setCellValue("Additional Phone Number");
			} else {
				aRow.createCell(15).setCellValue("");
			}

			String tollFree = obj.getTollFree();
			aRow.createCell(16).setCellValue(tollFree);
			if (tollFree != null && tollFree != "") {

				aRow.createCell(17).setCellValue("Toll Free Number");
			} else {

				aRow.createCell(17).setCellValue("");
			}

			aRow.createCell(18).setCellValue(obj.getFax());
			aRow.createCell(19).setCellValue("");
			aRow.createCell(20).setCellValue("");
			aRow.createCell(21).setCellValue("");
			aRow.createCell(22).setCellValue("");
			aRow.createCell(23).setCellValue("");
			aRow.createCell(24).setCellValue(obj.getTagline());
			aRow.createCell(25).setCellValue(obj.getBusinessDescription());
			aRow.createCell(26).setCellValue(
					obj.getProductsOrServices_combined());
			aRow.createCell(27).setCellValue(obj.getBrands());
			aRow.createCell(28).setCellValue("");
			aRow.createCell(29).setCellValue(obj.getProfessionalAssociations());
			aRow.createCell(30).setCellValue(obj.getLanguages());
			aRow.createCell(31).setCellValue(obj.getYearEstablished());
			aRow.createCell(32).setCellValue("");
			aRow.createCell(33).setCellValue("");
			/* aRow.createCell(34).setCellValue(obj.getYpInternetHeading()); */
		}
		logger.info("Total Rows : " + rowCount);

	}

	public void bingPlacesSampleTemplate(HSSFWorkbook workbook,
			List<LocalBusinessDTO> listOfBusiness) {
		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet("template");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeight((short) 280);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// create header row
		// BingPlacesSample Template
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Store ID");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("Name");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("Alternative Name");
		header.getCell(2).setCellStyle(style);
		header.createCell(3).setCellValue("IsChainBusiness");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("BusinessOwnerEmail");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("Address Line 1");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("Address Line 2");
		header.getCell(6).setCellStyle(style);
		header.createCell(7).setCellValue("City");
		header.getCell(7).setCellStyle(style);
		header.createCell(8).setCellValue("State");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("Zip Code");
		header.getCell(9).setCellStyle(style);
		header.createCell(10).setCellValue("Country Code");
		header.getCell(10).setCellStyle(style);
		header.createCell(11).setCellValue("Main Phone");
		header.getCell(11).setCellStyle(style);
		header.createCell(12).setCellValue("Category");
		header.getCell(12).setCellStyle(style);
		header.createCell(13).setCellValue("Primary Category");
		header.getCell(13).setCellStyle(style);
		header.createCell(14).setCellValue("Latitude");
		header.getCell(14).setCellStyle(style);
		header.createCell(15).setCellValue("Longitude");
		header.getCell(15).setCellStyle(style);
		header.createCell(16).setCellValue("Website");
		header.getCell(16).setCellStyle(style);
		header.createCell(17).setCellValue("Open 24 Hours");
		header.getCell(17).setCellStyle(style);
		header.createCell(18).setCellValue("Operating Hours");
		header.getCell(18).setCellStyle(style);
		header.createCell(19).setCellValue("Special Hours");
		header.getCell(19).setCellStyle(style);
		header.createCell(20).setCellValue("Photos");
		header.getCell(20).setCellStyle(style);
		header.createCell(21).setCellValue("Menu URL");
		header.getCell(21).setCellStyle(style);
		header.createCell(22).setCellValue("Online Order URL");
		header.getCell(22).setCellStyle(style);
		header.createCell(23).setCellValue("Facebook address");
		header.getCell(23).setCellStyle(style);
		header.createCell(24).setCellValue("Twitter address");
		header.getCell(24).setCellStyle(style);
		header.createCell(25).setCellValue("Email");
		header.getCell(25).setCellStyle(style);
		header.createCell(26).setCellValue("Status");
		header.getCell(26).setCellStyle(style);
		header.createCell(27).setCellValue("Hide Address");
		header.getCell(27).setCellStyle(style);
		header.createCell(28).setCellValue("Delete From Account");
		header.getCell(28).setCellStyle(style);
		header.createCell(29).setCellValue("NPI");
		header.getCell(29).setCellStyle(style);
		header.createCell(30).setCellValue("Hotel Amenities");
		header.getCell(30).setCellStyle(style);
		header.createCell(31).setCellValue("Hotel Star Rating");
		header.getCell(31).setCellStyle(style);
		header.createCell(32).setCellValue("Indicative Price of one meal");
		header.getCell(32).setCellStyle(style);
		header.createCell(33).setCellValue("PublishLink");
		header.getCell(33).setCellStyle(style);

		// create data rows
		int rowCount = 1;
		for (LocalBusinessDTO obj : listOfBusiness) {

			String primaryCategory = "";
			String categorySyph = obj.getCategorySyph();
			if (categorySyph != null && categorySyph.contains(",")) {
				primaryCategory.substring(0, primaryCategory.indexOf(","));
			}

			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(obj.getStore());
			aRow.createCell(1).setCellValue(obj.getCompanyName());
			aRow.createCell(2).setCellValue(obj.getAlternativeName());
			aRow.createCell(3).setCellValue("Y");
			aRow.createCell(4).setCellValue("");
			aRow.createCell(5).setCellValue(obj.getLocationAddress());
			aRow.createCell(6).setCellValue(obj.getSuite());
			aRow.createCell(7).setCellValue(obj.getLocationCity());
			aRow.createCell(8).setCellValue(obj.getLocationState());
			aRow.createCell(9).setCellValue(obj.getLocationZipCode());
			aRow.createCell(10).setCellValue(obj.getCountryCode());

			aRow.createCell(11).setCellValue(obj.getLocationPhone());
			aRow.createCell(12).setCellValue(categorySyph);
			aRow.createCell(13).setCellValue(primaryCategory);

			aRow.createCell(14).setCellValue("");
			aRow.createCell(15).setCellValue("");
			aRow.createCell(16).setCellValue(obj.getWebAddress());
			aRow.createCell(17).setCellValue("N");
			
			String hours = getHours(obj);
			aRow.createCell(18).setCellValue(hours);
			aRow.createCell(19).setCellValue("");
			aRow.createCell(20).setCellValue(obj.getLogoLink());
			aRow.createCell(21).setCellValue("");
			aRow.createCell(22).setCellValue("");
			aRow.createCell(23).setCellValue(obj.getFacebookLink());
			aRow.createCell(24).setCellValue(obj.getTwitterLink());
			aRow.createCell(25).setCellValue("");
			aRow.createCell(26).setCellValue("Open");
			aRow.createCell(27).setCellValue("N");
			aRow.createCell(28).setCellValue("");
			aRow.createCell(29).setCellValue("");
			aRow.createCell(30).setCellValue("");
			aRow.createCell(31).setCellValue("");
			aRow.createCell(32).setCellValue("");
			aRow.createCell(33).setCellValue("");

			
		}
		logger.info("Total Rows : " + rowCount);
	}

	public void googleTemplate(HSSFWorkbook workbook,
			List<LocalBusinessDTO> listOfBusiness) {
		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet("template");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// Google Template
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Store Code");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("Business name");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("Address Line 1");
		header.getCell(2).setCellStyle(style);
		header.createCell(3).setCellValue("Address Line 2");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("City");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("District");
		header.createCell(6).setCellValue("State");
		header.getCell(6).setCellStyle(style);
		header.createCell(7).setCellValue("Country");
		header.getCell(7).setCellStyle(style);
		header.createCell(8).setCellValue("Postal Code");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("Primary phone");
		header.getCell(9).setCellStyle(style);
		header.createCell(10).setCellValue("Additional phones");
		header.getCell(10).setCellStyle(style);
		header.createCell(11).setCellValue("Website");
		header.getCell(11).setCellStyle(style);
		header.createCell(12).setCellValue("Primary category");
		header.getCell(12).setCellStyle(style);
		header.createCell(13).setCellValue("Additional categories");
		header.createCell(14).setCellValue("Hours");
		header.createCell(15).setCellValue("Photos");
		header.createCell(16).setCellValue("Description");
		header.createCell(17).setCellValue("Labels");
		header.createCell(18).setCellValue("AdWords location extensions phone");

		// create data rows
		int rowCount = 1;
		for (LocalBusinessDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(obj.getStore());
			aRow.createCell(1).setCellValue(obj.getCompanyName());
			aRow.createCell(2).setCellValue(obj.getLocationAddress());
			StringBuffer suitevalue = new StringBuffer();
			String suite = obj.getSuite();
			if (suite != null && !suite.isEmpty()) {
				suitevalue.append("Suite");
				suitevalue.append(" ");
				suitevalue.append(suite);
				aRow.createCell(3).setCellValue(suitevalue.toString());
			} else {
				aRow.createCell(3).setCellValue(suite);
			}

			aRow.createCell(4).setCellValue(obj.getLocationCity());
			aRow.createCell(5).setCellValue("");
			aRow.createCell(6).setCellValue(obj.getLocationState());
			aRow.createCell(7).setCellValue(obj.getCountryCode());
			aRow.createCell(8).setCellValue(obj.getLocationZipCode());
			String locationPhone = obj.getLocationPhone();

			if (locationPhone != null && !locationPhone.isEmpty()) {

				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat(
						"({0}) {1}-{2}");

				String[] phoneNumArr = { locationPhone.substring(0, 3),
						locationPhone.substring(3, 6),
						locationPhone.substring(6) };
				locationPhone = phoneMsgFmt.format(phoneNumArr);

			}
			aRow.createCell(9).setCellValue(locationPhone);
			aRow.createCell(10).setCellValue(obj.getAdditionalNumber());
			StringBuffer webaddress = new StringBuffer();
			String webAddress = obj.getWebAddress();
			if (webAddress != null && !webAddress.isEmpty()) {
				webaddress.append("http://");
				webaddress.append(webAddress);
				aRow.createCell(11).setCellValue(webaddress.toString());
			} else {
				aRow.createCell(11).setCellValue(webAddress);
			}

			String categorySyph = obj.getPrimaryCategory();
			aRow.createCell(12).setCellValue(categorySyph);
			aRow.createCell(13).setCellValue(obj.getAdditionalCategories());
			aRow.createCell(14).setCellValue(getGoogleHours(obj));
			aRow.createCell(15).setCellValue("");
			aRow.createCell(16).setCellValue(obj.getBusinessDescriptionShort());
			aRow.createCell(17).setCellValue("");
			aRow.createCell(18).setCellValue("");

		}
		logger.info("Total Rows : " + rowCount);
	}

	public void masterTemplateData(HSSFWorkbook workbook,
			List<LocalBusinessDTO> listOfBusiness) {
		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet("errorInfo");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// Master template
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Client ID");
		header.createCell(1).setCellValue("Store #");
		header.createCell(2).setCellValue("Action Code");
		header.createCell(3).setCellValue("Country Code");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("Company Name");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("Alternative Name");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("Anchor/Host Business");
		header.createCell(7).setCellValue("Location Address");
		header.createCell(8).setCellValue("Suite");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("Location City");
		header.createCell(10).setCellValue("Location State");
		header.getCell(10).setCellStyle(style);
		header.createCell(11).setCellValue("Location Zip Code");
		header.getCell(11).setCellStyle(style);
		header.createCell(12).setCellValue("Location Phone");
		header.getCell(12).setCellStyle(style);
		header.createCell(13).setCellValue("Fax");
		header.createCell(14).setCellValue("Toll Free");
		header.createCell(15).setCellValue("TTY");
		header.createCell(16).setCellValue("Mobile Number");
		header.createCell(17).setCellValue("Additional Number");
		header.createCell(18).setCellValue("Location Email");
		header.createCell(19).setCellValue("Short Web Address");
		header.createCell(20).setCellValue("Web Address");
		header.createCell(21).setCellValue("Category 1");
		header.createCell(22).setCellValue("Category 2");
		header.createCell(23).setCellValue("Category 3");
		header.getCell(23).setCellStyle(style);
		header.createCell(24).setCellValue("Category 4");
		header.createCell(25).setCellValue("Category 5");
		header.createCell(26).setCellValue("Logo Link");
		header.createCell(27).setCellValue("Service Area");
		header.createCell(28).setCellValue("Primary Contact First Name");
		header.createCell(29).setCellValue("Primary Contact Last Name");
		header.getCell(29).setCellStyle(style);
		header.createCell(30).setCellValue("Contact Title");
		header.createCell(31).setCellValue("Contact Email");
		header.createCell(32).setCellValue("Location Employee Size");
		header.createCell(33).setCellValue("Title - Manager/Owner");
		header.createCell(34).setCellValue("Professional Title");
		header.createCell(35).setCellValue("Professional Associations");
		header.createCell(36).setCellValue("Monday Open");
		header.createCell(37).setCellValue("Monday Close");
		header.createCell(38).setCellValue("Tuesday Open");
		header.createCell(39).setCellValue("Tuesday Close");
		header.createCell(40).setCellValue("Wednesday Open");
		header.createCell(41).setCellValue("Wednesday Close");
		header.createCell(42).setCellValue("Thursday Open");
		header.createCell(43).setCellValue("Thursday Close");
		header.createCell(44).setCellValue("Friday Open");
		header.createCell(45).setCellValue("Friday Close");
		header.createCell(46).setCellValue("Saturday Open");
		header.createCell(47).setCellValue("Saturday Close");
		header.createCell(48).setCellValue("Sunday Open");
		header.createCell(49).setCellValue("Sunday Close");
		header.createCell(50).setCellValue("AMEX");
		header.createCell(51).setCellValue("Discover");
		header.createCell(52).setCellValue("Visa");
		header.createCell(53).setCellValue("Master Card");
		header.createCell(54).setCellValue("Diners Club");
		header.createCell(55).setCellValue("Debit Card");
		header.createCell(56).setCellValue("Store Card");
		header.createCell(57).setCellValue("Other Card");
		header.createCell(58).setCellValue("Cash");
		header.createCell(59).setCellValue("Check");
		header.createCell(60).setCellValue("Traveler's Check");
		header.createCell(61).setCellValue("Financing");
		header.createCell(62).setCellValue("Google Checkout");
		header.createCell(63).setCellValue("Invoice");
		header.createCell(64).setCellValue("PayPal");
		header.createCell(65).setCellValue("Coupon Link");
		header.createCell(66).setCellValue("Twitter Link");
		header.createCell(67).setCellValue("LinkedIn Link");
		header.createCell(68).setCellValue("Facebook Link");
		header.createCell(69).setCellValue("Foursquare Link");
		header.createCell(70).setCellValue("YouTube/Video Link");
		header.createCell(71).setCellValue("Google Plus Link");
		header.createCell(72).setCellValue("Myspace Link");
		header.createCell(73).setCellValue("Pinterist Link");

		header.createCell(74).setCellValue("Yelp Link");
		header.createCell(75).setCellValue("Instagram Link");
		header.createCell(76).setCellValue("Menu Link");

		header.createCell(77).setCellValue("Products");
		header.createCell(78).setCellValue("Services");
		header.createCell(79).setCellValue("Products/Services - combined");
		header.createCell(80).setCellValue("Brands");
		header.createCell(81).setCellValue("Keywords");
		header.createCell(82).setCellValue("Languages");
		header.createCell(83).setCellValue("Year Established");
		header.createCell(84).setCellValue("Tagline");
		header.createCell(85).setCellValue("Business Description");
		header.getCell(85).setCellStyle(style);
		header.createCell(86).setCellValue("Business Description Short");
		header.getCell(86).setCellStyle(style);
		header.createCell(87).setCellValue("ADDRESSPRIVACYFLAG");

		// create data rows
		int rowCount = 1;

		for (LocalBusinessDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(obj.getClientId());
			aRow.createCell(1).setCellValue(obj.getStore());
			aRow.createCell(2).setCellValue(obj.getActionCode());
			aRow.createCell(3).setCellValue(obj.getCountryCode());
			aRow.createCell(4).setCellValue(obj.getCompanyName());
			aRow.createCell(5).setCellValue(obj.getAlternativeName());
			aRow.createCell(6).setCellValue(obj.getAnchorOrHostBusiness());
			aRow.createCell(7).setCellValue(obj.getLocationAddress());
			aRow.createCell(8).setCellValue(obj.getSuite());
			aRow.createCell(9).setCellValue(obj.getLocationCity());
			aRow.createCell(10).setCellValue(obj.getLocationState());
			aRow.createCell(11).setCellValue(obj.getLocationZipCode());
			aRow.createCell(12).setCellValue(obj.getLocationPhone());
			aRow.createCell(13).setCellValue(obj.getFax());
			aRow.createCell(14).setCellValue(obj.getTollFree());
			aRow.createCell(15).setCellValue(obj.getTty());
			aRow.createCell(16).setCellValue(obj.getMobileNumber());
			aRow.createCell(17).setCellValue(obj.getAdditionalNumber());
			aRow.createCell(18).setCellValue(obj.getLocationEmail());
			aRow.createCell(19).setCellValue(obj.getShortWebAddress());
			aRow.createCell(20).setCellValue(obj.getWebAddress());
			aRow.createCell(21).setCellValue(obj.getCategory1());
			aRow.createCell(22).setCellValue(obj.getCategory2());
			aRow.createCell(23).setCellValue(obj.getCategory3());
			aRow.createCell(24).setCellValue(obj.getCategory4());
			aRow.createCell(25).setCellValue(obj.getCategory5());
			aRow.createCell(26).setCellValue(obj.getLogoLink());
			aRow.createCell(27).setCellValue(obj.getServiceArea());
			aRow.createCell(28).setCellValue(obj.getPrimaryContactFirstName());
			aRow.createCell(29).setCellValue(obj.getPrimaryContactLastName());
			aRow.createCell(30).setCellValue(obj.getContactTitle());
			aRow.createCell(31).setCellValue(obj.getContactEmail());
			aRow.createCell(32).setCellValue(obj.getLocationEmployeeSize());
			aRow.createCell(33).setCellValue(obj.getTitle_ManagerOrOwner());
			aRow.createCell(34).setCellValue(obj.getProfessionalTitle());
			aRow.createCell(35).setCellValue(obj.getProfessionalAssociations());
			aRow.createCell(36).setCellValue(obj.getMondayOpen());
			aRow.createCell(37).setCellValue(obj.getMondayClose());
			aRow.createCell(38).setCellValue(obj.getTuesdayOpen());
			aRow.createCell(39).setCellValue(obj.getTuesdayClose());
			aRow.createCell(40).setCellValue(obj.getWednesdayOpen());
			aRow.createCell(41).setCellValue(obj.getWednesdayClose());
			aRow.createCell(42).setCellValue(obj.getThursdayOpen());
			aRow.createCell(43).setCellValue(obj.getThursdayClose());
			aRow.createCell(44).setCellValue(obj.getFridayOpen());
			aRow.createCell(45).setCellValue(obj.getFridayClose());
			aRow.createCell(46).setCellValue(obj.getSaturdayOpen());
			aRow.createCell(47).setCellValue(obj.getSaturdayClose());
			aRow.createCell(48).setCellValue(obj.getSundayOpen());
			aRow.createCell(49).setCellValue(obj.getSundayClose());
			aRow.createCell(50).setCellValue(obj.getaMEX());
			aRow.createCell(51).setCellValue(obj.getDiscover());
			aRow.createCell(52).setCellValue(obj.getVisa());
			aRow.createCell(53).setCellValue(obj.getMasterCard());
			aRow.createCell(54).setCellValue(obj.getDinersClub());
			aRow.createCell(55).setCellValue(obj.getDebitCard());
			aRow.createCell(56).setCellValue(obj.getStoreCard());
			aRow.createCell(57).setCellValue(obj.getOtherCard());
			aRow.createCell(58).setCellValue(obj.getCash());
			aRow.createCell(59).setCellValue(obj.getCheck());
			aRow.createCell(60).setCellValue(obj.getTravelersCheck());
			aRow.createCell(61).setCellValue(obj.getFinancing());
			aRow.createCell(62).setCellValue(obj.getGoogleCheckout());
			aRow.createCell(63).setCellValue(obj.getInvoice());
			aRow.createCell(64).setCellValue(obj.getPayPal());
			aRow.createCell(65).setCellValue(obj.getCouponLink());
			aRow.createCell(66).setCellValue(obj.getTwitterLink());
			aRow.createCell(67).setCellValue(obj.getLinkedInLink());
			aRow.createCell(68).setCellValue(obj.getFacebookLink());
			aRow.createCell(69).setCellValue(obj.getFoursquareLink());
			aRow.createCell(70).setCellValue(obj.getYouTubeOrVideoLink());
			aRow.createCell(71).setCellValue(obj.getGooglePlusLink());
			aRow.createCell(72).setCellValue(obj.getMyspaceLink());
			aRow.createCell(73).setCellValue(obj.getPinteristLink());
			aRow.createCell(74).setCellValue(obj.getYelpLink());
			aRow.createCell(75).setCellValue(obj.getInstagramLink());
			aRow.createCell(76).setCellValue(obj.getMenuLink());
			aRow.createCell(77).setCellValue(obj.getProducts());
			aRow.createCell(78).setCellValue(obj.getServices());
			aRow.createCell(79).setCellValue(
					obj.getProductsOrServices_combined());
			aRow.createCell(80).setCellValue(obj.getBrands());
			aRow.createCell(81).setCellValue(obj.getKeywords());
			aRow.createCell(82).setCellValue(obj.getLanguages());
			aRow.createCell(83).setCellValue(obj.getYearEstablished());
			aRow.createCell(84).setCellValue(obj.getTagline());
			aRow.createCell(85).setCellValue(obj.getBusinessDescription());
			aRow.createCell(86).setCellValue(obj.getBusinessDescriptionShort());
			aRow.createCell(87).setCellValue(obj.getADDRESSPRIVACYFLAG());

		}
		logger.info("Total Rows : " + rowCount);

	}

	private void infogroupTemplateData(HSSFWorkbook workbook,
			List<LocalBusinessDTO> listOfBusiness) {

		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet("errorInfo");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// Master template
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Submission Type");
		header.createCell(1).setCellValue("Company Name");
		header.createCell(2).setCellValue("Alternative Name");
		header.createCell(3).setCellValue("Anchor/Host Business");
		header.createCell(4).setCellValue("Location Phone");
		header.createCell(5).setCellValue("Location Address");
		header.createCell(6).setCellValue("Suite");
		header.createCell(7).setCellValue("Location City");
		header.createCell(8).setCellValue("Location State/Province");
		header.createCell(9).setCellValue("Location Zip/Postal Code");
		header.createCell(10).setCellValue("Mailing Address");
		header.createCell(11).setCellValue("Mailing City");
		header.createCell(12).setCellValue("Mailing State/Province");
		header.createCell(13).setCellValue("Mailing Zip/Postal Code");
		header.createCell(14).setCellValue("Landmark Address");
		header.createCell(15).setCellValue("Landmark City");
		header.createCell(16).setCellValue("Landmark State/Province");
		header.createCell(17).setCellValue("Landmark Zip/Postal Code");
		header.createCell(18).setCellValue("Business Description");
		header.createCell(19).setCellValue("Toll Free");
		header.createCell(20).setCellValue("Additional Number");
		header.createCell(21).setCellValue("Fax");
		header.createCell(22).setCellValue("Mobile Number");
		header.createCell(23).setCellValue("TTY");
		header.createCell(24).setCellValue("Primary SIC");
		header.createCell(25).setCellValue("SIC2");
		header.createCell(26).setCellValue("SIC3");
		header.createCell(27).setCellValue("SIC4");
		header.createCell(28).setCellValue("SIC5");
		header.createCell(29).setCellValue("Primary Contact First Name");
		header.createCell(30).setCellValue("Primary Contact Last Name");
		header.createCell(31).setCellValue("Primary Contact Actual Title");
		header.createCell(32)
				.setCellValue("Primary Contact Standardized Title");
		header.createCell(33).setCellValue("Primary Contact Credential");
		header.createCell(34).setCellValue("Primary Contact Email");
		header.createCell(35).setCellValue("Is Professional Record?");
		header.createCell(36).setCellValue("Professional First Name");
		header.createCell(37).setCellValue("Professional Last Name");
		header.createCell(38).setCellValue("Professional Email");
		header.createCell(39).setCellValue("Professional Firm Name");
		header.createCell(40).setCellValue("Professional Credential");
		header.createCell(41).setCellValue("Professional Standardized Title");
		header.createCell(42).setCellValue("Location Employee Size");
		header.createCell(43).setCellValue("Short Web Address");
		header.createCell(44).setCellValue("Web Address");
		header.createCell(45).setCellValue("Coupon Link");
		header.createCell(46).setCellValue("Twitter Link");
		header.createCell(47).setCellValue("LinkedIn Link");
		header.createCell(48).setCellValue("Facebook Link");
		header.createCell(49).setCellValue("Google Plus Link");
		header.createCell(50).setCellValue("Pinterest Link");
		header.createCell(51).setCellValue("Myspace Link");
		header.createCell(52).setCellValue("Alternate Social Link");
		header.createCell(53).setCellValue("YouTube/Video Link");
		header.createCell(54).setCellValue("Logo Link");
		header.createCell(55).setCellValue("Primary Image");
		header.createCell(56).setCellValue("Image 2");
		header.createCell(57).setCellValue("Image 3");
		header.createCell(58).setCellValue("Image 4");
		header.createCell(59).setCellValue("Image 5");
		header.createCell(60).setCellValue("Operating Hours");
		header.createCell(61).setCellValue("Seasonal Hours");
		header.createCell(62).setCellValue("AMEX");
		header.createCell(63).setCellValue("Discover");
		header.createCell(63).setCellValue("Visa");
		header.createCell(65).setCellValue("Master Card");
		header.createCell(66).setCellValue("Diners Club");
		header.createCell(67).setCellValue("Debit Card");
		header.createCell(68).setCellValue("Store Card");
		header.createCell(69).setCellValue("Other Card");
		header.createCell(70).setCellValue("Cash");
		header.createCell(71).setCellValue("Check");
		header.createCell(72).setCellValue("Traveler's Check");
		header.createCell(73).setCellValue("Invoice");
		header.createCell(74).setCellValue("Financing");
		header.createCell(75).setCellValue("Google Checkout");
		header.createCell(76).setCellValue("PayPal");
		header.createCell(77).setCellValue("Brands");
		header.createCell(78).setCellValue("Products");
		header.createCell(79).setCellValue("Services");
		header.createCell(80).setCellValue("Keywords");
		header.createCell(81).setCellValue("Professional Associations");
		header.createCell(82).setCellValue("Languages");
		header.createCell(83).setCellValue("ATM");
		header.createCell(84).setCellValue("Parking Validation Options");
		header.createCell(85).setCellValue("Duns Number");
		header.createCell(86).setCellValue("Unofficial Landmark");
		header.createCell(87).setCellValue("Price Range");
		header.createCell(88).setCellValue("Reservations");
		header.createCell(89).setCellValue("Banquet/Meeting Rooms");
		header.createCell(90).setCellValue("Restaurant/Bar Types");
		header.createCell(91).setCellValue("Dress Code");
		header.createCell(92).setCellValue("Shuttle Service");
		header.createCell(93).setCellValue("Free Internet");
		header.createCell(94).setCellValue("Internet Access");
		header.createCell(95).setCellValue("Food Court");
		header.createCell(96).setCellValue("Park Permit Required");
		header.createCell(97).setCellValue("Equipment Rentals");
		header.createCell(98).setCellValue("Lodging/Campgrounds");
		header.createCell(99).setCellValue("Live Entertainment");
		header.createCell(100).setCellValue("Smoking Preference");
		header.createCell(101).setCellValue("Gift Shop");
		header.createCell(102).setCellValue("Presence of E-Commerce");
		header.createCell(103).setCellValue("Green Company Indicator");
		header.createCell(104).setCellValue("LGBT Friendly");
		header.createCell(105).setCellValue("Meta Tag Data");
		header.createCell(106).setCellValue("Golf Courses");
		header.createCell(107).setCellValue(
				"Golf Courses - Semi-Private HOO for public");
		header.createCell(108).setCellValue("Golf Courses - Caddy Service");
		header.createCell(109).setCellValue(
				"Golf Courses - Championship Course");
		header.createCell(110).setCellValue("Golf Courses - Valet Parking");
		header.createCell(111).setCellValue("Hotels");
		header.createCell(112).setCellValue("Hotels - Elevator");
		header.createCell(113).setCellValue("Hotels - Indoor Pool");
		header.createCell(114).setCellValue("Hotels - Outdoor Pool");
		header.createCell(115).setCellValue("Hotels - Hot Tub");
		header.createCell(116).setCellValue("Hotels - Exercise Facility");
		header.createCell(117).setCellValue("Hotels - Pet Friendly");
		header.createCell(118).setCellValue("Hotels - Room Service");
		header.createCell(119).setCellValue("Hotels - Cable TV");
		header.createCell(120).setCellValue("Hotels - Continental Breakfast");
		header.createCell(121).setCellValue("Hotels - Kitchens");
		header.createCell(122).setCellValue("Hotels - Guest Laundry");
		header.createCell(123).setCellValue("Hotels - Concierge Svc");
		header.createCell(124).setCellValue("Hotels - Valet Parking");
		header.createCell(125).setCellValue("Landmark Info");
		header.createCell(126).setCellValue("Landmark Info - Admission Fee");
		header.createCell(127).setCellValue("New Car Dealer");
		header.createCell(128).setCellValue("Recreational");
		header.createCell(129).setCellValue(
				"Recreational - # of tents/RV sites");
		header.createCell(130).setCellValue(
				"Recreational - Sheltered Picnic Areas");
		header.createCell(131).setCellValue("Recreational - Swimming Areas");
		header.createCell(132).setCellValue("Recreational - Fishing Areas");
		header.createCell(133).setCellValue("Recreational - Boating Areas");
		header.createCell(134).setCellValue("Restaurants");
		header.createCell(135).setCellValue("Restaurants - Table Svc");
		header.createCell(136).setCellValue("Restaurants - Happy Hour Menu");
		header.createCell(137).setCellValue("Restaurants - Valet Parking");
		header.createCell(138).setCellValue("Shopping Center");
		header.createCell(139).setCellValue("Airports");
		header.createCell(140).setCellValue("Amusement Places");
		header.createCell(141).setCellValue("Amusement Parks");
		header.createCell(142).setCellValue("Amusement Parks - Kid's Rides");
		header.createCell(143).setCellValue("Amusement Parks - Water Park");
		header.createCell(144).setCellValue("Bars");
		header.createCell(145).setCellValue("Bars - Happy Hour");
		header.createCell(146).setCellValue("Bars - Keno");
		header.createCell(147).setCellValue("Motorcycle Dealer Information");
		header.createCell(148).setCellValue("Casinos");
		header.createCell(149).setCellValue("Casinos - Keno");
		header.createCell(150).setCellValue("Casinos - Valet Parking");
		header.createCell(151).setCellValue("Complex Info");
		header.createCell(152).setCellValue("Complex Info - Valet Parking");
		header.createCell(153).setCellValue("Concert Place Info");
		header.createCell(154).setCellValue("Convenience Stores");
		header.createCell(155).setCellValue("Service Stations");
		header.createCell(156).setCellValue("Dept Store");
		header.createCell(157).setCellValue("Electronic Store");
		header.createCell(158).setCellValue("Used Car Dealer");
		header.createCell(159).setCellValue("Index");
		header.createCell(160).setCellValue("CMR#");
		header.createCell(161).setCellValue("Provided Infogroup ID");

		// create data row
		int rowCount = 1;

		for (LocalBusinessDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(obj.getSubmissionType());
			aRow.createCell(1).setCellValue(obj.getCompanyName());
			aRow.createCell(2).setCellValue(obj.getAlternativeName());
			aRow.createCell(3).setCellValue(obj.getAnchorOrHostBusiness());
			aRow.createCell(4).setCellValue(obj.getLocationPhone());
			aRow.createCell(5).setCellValue(obj.getLocationAddress());
			aRow.createCell(6).setCellValue(obj.getSuite());
			aRow.createCell(7).setCellValue(obj.getLocationCity());
			aRow.createCell(8).setCellValue(obj.getLocationState());
			aRow.createCell(9).setCellValue(obj.getLocationZipCode());
			aRow.createCell(10).setCellValue("");
			aRow.createCell(11).setCellValue("");
			aRow.createCell(12).setCellValue("");
			aRow.createCell(13).setCellValue("");
			aRow.createCell(14).setCellValue("");
			aRow.createCell(15).setCellValue("");
			aRow.createCell(16).setCellValue("");
			aRow.createCell(17).setCellValue("");
			aRow.createCell(18).setCellValue(obj.getBusinessDescription());
			aRow.createCell(19).setCellValue(obj.getTollFree());
			aRow.createCell(20).setCellValue(obj.getAdditionalNumber());
			aRow.createCell(21).setCellValue(obj.getFax());
			aRow.createCell(22).setCellValue(obj.getMobileNumber());
			aRow.createCell(23).setCellValue(obj.getTty());
			aRow.createCell(24).setCellValue(obj.getCategory1());
			aRow.createCell(25).setCellValue(obj.getCategory2());
			aRow.createCell(26).setCellValue(obj.getCategory3());
			aRow.createCell(27).setCellValue(obj.getCategory4());
			aRow.createCell(28).setCellValue(obj.getCategory5());
			aRow.createCell(29).setCellValue(obj.getPrimaryContactFirstName());
			aRow.createCell(30).setCellValue(obj.getPrimaryContactLastName());
			aRow.createCell(31).setCellValue(obj.getContactTitle());
			aRow.createCell(32).setCellValue("");
			aRow.createCell(33).setCellValue("");
			aRow.createCell(34).setCellValue(obj.getContactEmail());
			aRow.createCell(35).setCellValue("");
			aRow.createCell(36).setCellValue("");
			aRow.createCell(37).setCellValue("");
			aRow.createCell(38).setCellValue("");
			aRow.createCell(39).setCellValue("");
			aRow.createCell(40).setCellValue("");
			aRow.createCell(41).setCellValue("");
			aRow.createCell(42).setCellValue(obj.getLocationEmployeeSize());
			aRow.createCell(43).setCellValue(obj.getShortWebAddress());
			aRow.createCell(44).setCellValue(obj.getWebAddress());
			aRow.createCell(45).setCellValue(obj.getCouponLink());
			aRow.createCell(46).setCellValue(obj.getTwitterLink());
			aRow.createCell(47).setCellValue(obj.getLinkedInLink());
			aRow.createCell(48).setCellValue(obj.getFacebookLink());
			aRow.createCell(49).setCellValue(obj.getGooglePlusLink());
			aRow.createCell(50).setCellValue(obj.getPinteristLink());
			aRow.createCell(51).setCellValue(obj.getMyspaceLink());
			aRow.createCell(52).setCellValue(obj.getAlternateSocialLink());
			aRow.createCell(53).setCellValue(obj.getYouTubeOrVideoLink());
			aRow.createCell(54).setCellValue(obj.getLogoLink());
			aRow.createCell(55).setCellValue("");
			aRow.createCell(56).setCellValue("");
			aRow.createCell(57).setCellValue("");
			aRow.createCell(58).setCellValue("");
			aRow.createCell(59).setCellValue("");
			aRow.createCell(60).setCellValue(getInfoGroupHours(obj));
			aRow.createCell(61).setCellValue("");
			aRow.createCell(62).setCellValue(obj.getaMEX());
			aRow.createCell(63).setCellValue(obj.getDiscover());
			aRow.createCell(64).setCellValue(obj.getVisa());
			aRow.createCell(65).setCellValue(obj.getMasterCard());
			aRow.createCell(66).setCellValue(obj.getDinersClub());
			aRow.createCell(67).setCellValue(obj.getDebitCard());
			aRow.createCell(68).setCellValue(obj.getStoreCard());
			aRow.createCell(69).setCellValue(obj.getOtherCard());
			aRow.createCell(70).setCellValue(obj.getCash());
			aRow.createCell(71).setCellValue(obj.getCheck());
			aRow.createCell(72).setCellValue(obj.getTravelersCheck());
			aRow.createCell(73).setCellValue(obj.getInvoice());
			aRow.createCell(74).setCellValue(obj.getFinancing());
			aRow.createCell(75).setCellValue(obj.getGoogleCheckout());
			aRow.createCell(76).setCellValue(obj.getPayPal());
			aRow.createCell(77).setCellValue(obj.getBrands());
			aRow.createCell(78).setCellValue(obj.getProducts());
			aRow.createCell(79).setCellValue(obj.getServices());
			aRow.createCell(80).setCellValue(obj.getKeywords());
			aRow.createCell(81).setCellValue(obj.getProfessionalAssociations());
			aRow.createCell(82).setCellValue(obj.getLanguages());
			aRow.createCell(83).setCellValue("");
			aRow.createCell(84).setCellValue("");
			aRow.createCell(85).setCellValue("");
			aRow.createCell(86).setCellValue("");
			aRow.createCell(87).setCellValue("");
			aRow.createCell(88).setCellValue("");
			aRow.createCell(89).setCellValue("");
			aRow.createCell(90).setCellValue("");
			aRow.createCell(91).setCellValue("");
			aRow.createCell(92).setCellValue("");
			aRow.createCell(93).setCellValue("");
			aRow.createCell(94).setCellValue("");
			aRow.createCell(95).setCellValue("");
			aRow.createCell(96).setCellValue("");
			aRow.createCell(97).setCellValue("");
			aRow.createCell(98).setCellValue("");
			aRow.createCell(99).setCellValue("");
			aRow.createCell(100).setCellValue("");
			aRow.createCell(101).setCellValue("");
			aRow.createCell(102).setCellValue("");
			aRow.createCell(103).setCellValue("");
			aRow.createCell(104).setCellValue("");
			aRow.createCell(105).setCellValue("");
			aRow.createCell(106).setCellValue("");
			aRow.createCell(107).setCellValue("");
			aRow.createCell(108).setCellValue("");
			aRow.createCell(109).setCellValue("");
			aRow.createCell(110).setCellValue("");
			aRow.createCell(111).setCellValue("");
			aRow.createCell(112).setCellValue("");
			aRow.createCell(113).setCellValue("");
			aRow.createCell(114).setCellValue("");
			aRow.createCell(115).setCellValue("");
			aRow.createCell(116).setCellValue("");
			aRow.createCell(117).setCellValue("");
			aRow.createCell(118).setCellValue("");
			aRow.createCell(119).setCellValue("");
			aRow.createCell(120).setCellValue("");
			aRow.createCell(121).setCellValue("");
			aRow.createCell(122).setCellValue("");
			aRow.createCell(123).setCellValue("");
			aRow.createCell(124).setCellValue("");
			aRow.createCell(125).setCellValue("");
			aRow.createCell(126).setCellValue("");
			aRow.createCell(127).setCellValue("");
			aRow.createCell(128).setCellValue("");
			aRow.createCell(129).setCellValue("");
			aRow.createCell(130).setCellValue("");
			aRow.createCell(131).setCellValue("");
			aRow.createCell(132).setCellValue("");
			aRow.createCell(133).setCellValue("");
			aRow.createCell(134).setCellValue("");
			aRow.createCell(135).setCellValue("");
			aRow.createCell(136).setCellValue("");
			aRow.createCell(137).setCellValue("");
			aRow.createCell(138).setCellValue("");
			aRow.createCell(139).setCellValue("");
			aRow.createCell(140).setCellValue("");
			aRow.createCell(141).setCellValue("");
			aRow.createCell(142).setCellValue("");
			aRow.createCell(143).setCellValue("");
			aRow.createCell(144).setCellValue("");
			aRow.createCell(145).setCellValue("");
			aRow.createCell(146).setCellValue("");
			aRow.createCell(147).setCellValue("");
			aRow.createCell(148).setCellValue("");
			aRow.createCell(149).setCellValue("");
			aRow.createCell(150).setCellValue("");
			aRow.createCell(151).setCellValue("");
			aRow.createCell(152).setCellValue("");
			aRow.createCell(153).setCellValue("");
			aRow.createCell(154).setCellValue("");
			aRow.createCell(155).setCellValue("");
			aRow.createCell(156).setCellValue("");
			aRow.createCell(157).setCellValue("");
			aRow.createCell(158).setCellValue("");
			aRow.createCell(159).setCellValue(obj.getStore());
			aRow.createCell(160).setCellValue("");
			aRow.createCell(161).setCellValue("");

		}
		logger.info("Total Rows : " + rowCount);

	}

	private static String getInfoGroupHours(LocalBusinessDTO localBusinessDTO) {
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

		if ("CLOSE".equalsIgnoreCase(sunOpen)
				|| "CLOSE".equalsIgnoreCase(sunClose)) {
			workingHours.append("Sun Closed,  ");
		} else if (sunOpen != null && sunOpen.trim().length() > 0
				&& sunClose != null && sunClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(sunOpen);
				Date date = formatter.parse(sunClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();

				cal.setTime(dt);
				cal2.setTime(date);

				int hour = cal.get(Calendar.HOUR);

				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);

				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}

					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Sun " + time + "-" + close + ", ");
		}
		if ("CLOSE".equalsIgnoreCase(mondayOpen)
				|| "CLOSE".equalsIgnoreCase(mondayClose)) {
			workingHours.append("Mon Closed, ");
		}

		else if (mondayOpen != null && mondayOpen.trim().length() > 0
				&& mondayClose != null && mondayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(mondayOpen);
				Date date = formatter.parse(mondayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Mon " + time + "-" + close + ", ");
		}
		if ("CLOSE".equalsIgnoreCase(tuesdayOpen)
				|| "CLOSE".equalsIgnoreCase(tuuesdayClose)) {
			workingHours.append("Tue Closed, ");
		} else if (tuesdayOpen != null && tuesdayOpen.trim().length() > 0
				&& tuuesdayClose != null && tuuesdayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(tuesdayOpen);
				Date date = formatter.parse(tuuesdayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Tue " + time + "-" + close + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(wedOpen)
				|| "CLOSE".equalsIgnoreCase(wedClose)) {
			workingHours.append("Wed Closed, ");
		} else if (wedOpen != null && wedOpen.trim().length() > 0
				&& wedClose != null && wedClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(wedOpen);
				Date date = formatter.parse(wedClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Wed " + time + "-" + close + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(thursdayOpen)
				|| "CLOSE".equalsIgnoreCase(thursdayClose)) {
			workingHours.append("Thu Closed, ");
		} else if (thursdayOpen != null && thursdayOpen.trim().length() > 0
				&& thursdayClose != null && thursdayClose.trim().length() > 0) {
			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(thursdayOpen);
				Date date = formatter.parse(thursdayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

			workingHours.append("Thu " + time + "-" + close + ", ");

		}

		if ("CLOSE".equalsIgnoreCase(fridayOpen)
				|| "CLOSE".equalsIgnoreCase(fridayClose)) {
			workingHours.append("Fri Closed, ");
		} else if (fridayOpen != null && fridayOpen.trim().length() > 0
				&& fridayClose != null && fridayClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(fridayOpen);
				Date date = formatter.parse(fridayClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Fri " + time + "-" + close + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(satOpen)
				|| "CLOSE".equalsIgnoreCase(satClose)) {
			workingHours.append("Sat Closed, ");
		} else if (satOpen != null && satOpen.trim().length() > 0
				&& satClose != null && satClose.trim().length() > 0) {

			String time = null;
			String close = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			try {
				Date dt = formatter.parse(satOpen);
				Date date = formatter.parse(satClose);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				int minutes = date.getMinutes();
				int minutes2 = dt.getMinutes();
				cal.setTime(dt);
				// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sunOpen));
				cal2.setTime(date);
				int hour = cal.get(Calendar.HOUR);
				int i = cal.get(Calendar.AM_PM);
				if (i == 0) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " AM";
				}
				if (i == 1) {
					if (hour == 0) {
						hour = 12;
					}
					time = hour + ":" + String.format("%02d", minutes2) + " PM";
				}
				int hour2 = cal2.get(Calendar.HOUR);
				int j = cal2.get(Calendar.AM_PM);
				if (j == 0) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " AM";
				}
				if (j == 1) {
					if (hour2 == 0) {
						hour2 = 12;
					}
					close = hour2 + ":" + String.format("%02d", minutes)
							+ " PM";
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			workingHours.append("Sat " + time + "-" + close);
		}
		String hours = workingHours.toString();
		if (hours != null && hours.endsWith(", ")) {
			hours = hours.substring(0, hours.length() - 2);
		}
		return hours;
	}

	private void localezeFeedTemplateData(HSSFWorkbook workbook,
			List<LocalBusinessDTO> listOfBusiness) {

		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet("errorInfo");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// Master template
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("BUSINESS NAME");
		header.createCell(1).setCellValue("DEPARTMENT");
		header.createCell(2).setCellValue("ADDRESS LINE 1");
		header.createCell(3).setCellValue("ADDRESS LINE 2");
		header.createCell(4).setCellValue("CITY");
		header.createCell(5).setCellValue("STATE");
		header.createCell(6).setCellValue("POSTAL CODE");
		header.createCell(7).setCellValue("PRIMARY PHONE");
		header.createCell(8).setCellValue("CATEGORY");
		header.createCell(9).setCellValue("WEBSITE");
		header.createCell(10).setCellValue("LOGO URL");
		header.createCell(11).setCellValue("EMAIL");
		header.createCell(12).setCellValue("TOLL FREE");
		header.createCell(13).setCellValue("FAX");
		header.createCell(14).setCellValue("MOBILE");
		header.createCell(15).setCellValue("ALTERNATE");
		header.createCell(16).setCellValue("HOUR OF OPERATION");
		header.createCell(17).setCellValue("PAYMENT METHODS");
		header.createCell(18).setCellValue("LANGUAGES SPOKEN");
		header.createCell(19).setCellValue("YEAR ESTABLISHED");
		header.createCell(20).setCellValue("TAGLINE");
		header.createCell(21).setCellValue("Client ID");
		header.createCell(22).setCellValue("CATEGORY 2");
		header.createCell(23).setCellValue("CATEGORY 3");
		header.createCell(24).setCellValue("KEYWORDS");
		header.createCell(25).setCellValue("FACEBOOK");
		header.createCell(26).setCellValue("LINKEDIN");
		header.createCell(27).setCellValue("TWITTER");
		header.createCell(28).setCellValue("GOOGLEPLUS");
		header.createCell(29).setCellValue("YELP");
		header.createCell(30).setCellValue("FOURSQUARE");

		// create data rows
		int rowCount = 1;

		for (LocalBusinessDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(obj.getCompanyName());
			aRow.createCell(1).setCellValue("");
			aRow.createCell(2).setCellValue(obj.getLocationAddress());
			aRow.createCell(3).setCellValue(obj.getSuite());
			aRow.createCell(4).setCellValue(obj.getLocationCity());
			aRow.createCell(5).setCellValue(obj.getLocationState());
			aRow.createCell(6).setCellValue(obj.getLocationZipCode());
			aRow.createCell(7).setCellValue(obj.getLocationPhone());
			aRow.createCell(8).setCellValue(obj.getCategory1());
			aRow.createCell(9).setCellValue(obj.getWebAddress());
			aRow.createCell(10).setCellValue(obj.getLogoLink());
			aRow.createCell(11).setCellValue(obj.getLocationEmail());
			aRow.createCell(12).setCellValue(obj.getTollFree());
			aRow.createCell(13).setCellValue(obj.getFax());
			aRow.createCell(14).setCellValue(obj.getMobileNumber());
			aRow.createCell(15).setCellValue("");
			aRow.createCell(16).setCellValue(getLocalezeHours(obj));
			aRow.createCell(17).setCellValue(getPaymentTypes(obj));
			aRow.createCell(18).setCellValue(obj.getLanguages());
			aRow.createCell(19).setCellValue(obj.getYearEstablished());
			aRow.createCell(20).setCellValue(obj.getTagline());
			aRow.createCell(21).setCellValue(obj.getStore());
			aRow.createCell(22).setCellValue(obj.getCategory2());
			aRow.createCell(23).setCellValue(obj.getCategory3());
			aRow.createCell(24).setCellValue(obj.getKeywords());
			aRow.createCell(25).setCellValue(obj.getFacebookLink());
			aRow.createCell(26).setCellValue(obj.getLinkedInLink());
			aRow.createCell(27).setCellValue(obj.getTwitterLink());
			aRow.createCell(28).setCellValue(obj.getGooglePlusLink());
			aRow.createCell(29).setCellValue(obj.getYelpLink());
			aRow.createCell(30).setCellValue(obj.getFoursquareLink());

		}
		logger.info("Total Rows : " + rowCount);

	}

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

	public static String getLocalezeHours(LocalBusinessDTO localBusinessDTO) {
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

	private static String getLanguages(LocalBusinessDTO localBusinessDTO) {
		StringBuffer languageSpoken = new StringBuffer();
		String language = localBusinessDTO.getLanguages();
		language = language.trim();
		String[] split = language.split(",");
		if (split.length == 0) {
			languageSpoken.append("EN");
		} else {
			for (String languages : split) {
				if (languages.equalsIgnoreCase("English")) {
					languageSpoken.append("EN");
				}
				if (languages.equalsIgnoreCase("Spanish")) {
					languageSpoken.append("ES");
				}
				if (languages.equalsIgnoreCase("French")) {
					languageSpoken.append("FR");
				}
				if (languages.equalsIgnoreCase("German")) {
					languageSpoken.append("DE");
				}
				if (languages.equalsIgnoreCase("Italian")) {
					languageSpoken.append("IT");
				}
				if (languages.equalsIgnoreCase("Portuguese")) {
					languageSpoken.append("PT");
				}
				if (languages.equalsIgnoreCase("Russian")) {
					languageSpoken.append("RU");
				}
				if (languages.equalsIgnoreCase("Chinese")) {
					languageSpoken.append("ZH");
				}
				if (languages.equalsIgnoreCase("Japanese")) {
					languageSpoken.append("JA");
				}
				if (languages.equalsIgnoreCase("Korean")) {
					languageSpoken.append("KO");
				}
				if (languages.equalsIgnoreCase("Thai")) {
					languageSpoken.append("TH");
				}
			}

		}
		return languageSpoken.toString();
	}

}
