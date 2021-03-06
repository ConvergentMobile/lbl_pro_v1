package com.business.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import com.business.common.dto.LblErrorDTO;

/**
 * 
 * @author Vasanth
 *
 */

public class ExcelErrorBuilder extends AbstractExcelView {

	Logger logger = Logger.getLogger(ExcelErrorBuilder.class);

	/**
	 * buildExcelDocument
	 */
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// get data model which is passed by the Spring container
		List<LblErrorDTO> inCorrectData = (List<LblErrorDTO>) model
				.get("listOfIncorrectData");
		//String errorRecords = (String) model.get("apiService");
		String apiService = (String)model.get("apiService");
		if(apiService != null){
			
			if (apiService.contains("MasterTemplate")) {		    	   
		    	   errorListCopyToExcel(workbook, inCorrectData);
				      }else{
				    	errorListCopyToExcel(workbook, inCorrectData);
				      }
			
			}	
		logger.debug("end :: downloadErrorBusiness  method");
	}

	/**
	 * errorListCopyToExcel
	 * 
	 * @param workbook
	 * @param inCorrectData
	 */
	public void errorListCopyToExcel(HSSFWorkbook workbook,
			List<LblErrorDTO> inCorrectData) {
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
		
		header.createCell(88).setCellValue("errorMessage");
		header.createCell(89).setCellValue("client");
		
		// create data rows
		int rowCount = 1;

		for (LblErrorDTO obj : inCorrectData) {
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
			
			aRow.createCell(88).setCellValue(obj.getErrorMessage());
			aRow.createCell(89).setCellValue(obj.getClient());
			
		}
		logger.info("Total ErrorList Rows in Excel Sheet ::" + rowCount);
		
	}

}
