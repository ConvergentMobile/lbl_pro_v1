package com.business.web.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.SprintDTO;
import com.business.common.util.ControllerUtil;
import com.business.common.util.LBLConstants;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.ReportService;
import com.business.web.bean.UsersBean;
import com.business.web.validator.LocalBusinessValidator;

@Controller
public class ClientCheckReportListController {

	Logger logger = Logger.getLogger(ClientCheckReportListController.class);

	@Autowired
	private CheckReportService service;

	@Autowired
	private BusinessService businessservice;
	@Autowired
	private ReportService reportService;
	@Autowired
	private LocalBusinessValidator businessValidator;

	private ControllerUtil controllerUtil = new ControllerUtil();

	@RequestMapping(value = "/clientcheckreportview.htm", method = RequestMethod.GET)
	public String getCheckReportListing(Model model, HttpSession session,
			HttpServletRequest request) {
		logger.info("start ::  getCheckReportListing method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String store = request.getParameter("store");
		logger.info("store:::::::::::::::" + store);
		String brandname = request.getParameter("brandname");
		logger.info("brandname:::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		int errorCount = getErrorCount(store,brandname);
		model.addAttribute("directoryShowingErrors", errorCount);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<LocalBusinessDTO> list = service.getListOfBusinessInfo();
	
	
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		LocalBusinessDTO searchBusinessListinginfo = businessservice
				.getBusinessListinginfoByBrandId(store,brandname);

		String brands = searchBusinessListinginfo.getBrands();
		String companyName = searchBusinessListinginfo.getCompanyName();
		String locationPhone = searchBusinessListinginfo.getLocationPhone();

		String locationAddress = searchBusinessListinginfo.getLocationAddress();
		String locationCity = searchBusinessListinginfo.getLocationCity();
		String locationState = searchBusinessListinginfo.getLocationState();
		String locationZipCode = searchBusinessListinginfo.getLocationZipCode();
		String webAddress = searchBusinessListinginfo.getWebAddress();
		String store2 = searchBusinessListinginfo.getStore();
		List<LocalBusinessDTO> businessDTO = service.getBusinesslisting(store,brandname);
		model.addAttribute("webAddress", businessDTO.get(0).getWebAddress());
		model.addAttribute("store", businessDTO.get(0).getStore());
		model.addAttribute("lblportlisting", businessDTO);
		if (locationAddress == null) {
			locationAddress = "";
		}
		Set<CheckReportDTO> checkrceportinfo = service.CheckReportSearchInfo(
				store, locationAddress,brandname);
		Set<CheckReportDTO> checkrceportinformation = new LinkedHashSet<CheckReportDTO>();

		int directoryErrorcount = 0;

		for (CheckReportDTO checkReportDTO : checkrceportinfo) {
			if (checkReportDTO.getNoOfErrors() != 0
					|| checkReportDTO.getNoOfErrors() != -1) {
				directoryErrorcount++;
			}
		}

		int accuracy = directoryErrorcount % 10 * 10;
		// accuracy = 100 - accuracy;

		CheckReportDTO lblCheckDto = new CheckReportDTO();
		lblCheckDto.setDirectory("LBL");
		lblCheckDto.setBusinessname(companyName);
		lblCheckDto.setPhone(locationPhone);
		lblCheckDto.setAddress(locationAddress);
		lblCheckDto.setCity(locationCity);
		lblCheckDto.setState(locationState);
		lblCheckDto.setZip(locationZipCode);
		lblCheckDto.setStore(store2);
		lblCheckDto.setWebsite(webAddress);
		checkrceportinformation.add(lblCheckDto);
		checkrceportinformation.addAll(checkrceportinfo);

		Integer locationAccuracy=0;
		AccuracyDTO accuracyDTO = service.getLocationAccuracy(store, brandname);
		if(accuracyDTO!=null){
			 locationAccuracy = accuracyDTO.getAverageAccuracy();
		}

		int activeListSize = list.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		double totalPercentage = service.getTotalPercentage(brandname);

		totalPercentage = Math.round(totalPercentage);
		model.addAttribute("locationAccuracy", locationAccuracy);
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("directoryErrorcount", directoryErrorcount);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("accuracy", accuracy);
		// controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("listOfBusinessInfo", list);
		controllerUtil.listingsAddAttributes(1, model, request, list);
		logger.info("BusinessInformation size == " + activeListSize);
		model.addAttribute("checkreport", new CheckReportDTO());
		model.addAttribute("listOfCheckreportInfo", checkrceportinformation);
		model.addAttribute("googleKey", LBLConstants.google_Key);
		session.setAttribute("checklistreportdata", checkrceportinformation);

		logger.info("end ::  getCheckReportListing method");

		return "clientcheckreportlisings";
	}

	@RequestMapping(value = "/clientcomparelistings.htm", method = RequestMethod.POST)
	public String getCompareCheckReportListing(Model model,
			HttpSession session, HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String listingId = request.getParameter("listingId");
		logger.info("listingId :::" + listingId);
		String store = request.getParameter("store");
		logger.info("store :::" + store);
		;
		String brandname = request.getParameter("brandname");
		logger.info("brandname:::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);

		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		int errorCount=getErrorCount(store,brandname);

		model.addAttribute("directoryShowingErrors", errorCount);
		
		List<CheckReportDTO> dto = service.checkListing(listingId);

		model.addAttribute("checkreportlisting", dto);

		List<LocalBusinessDTO> businessDTO = service.getBusinesslisting(store,brandname);
		String webAddress = businessDTO.get(0).getWebAddress();
		String companyName = businessDTO.get(0).getCompanyName();
		String locationAddress = businessDTO.get(0).getLocationAddress();
		String locationCity = businessDTO.get(0).getLocationCity();
		String locationPhone = businessDTO.get(0).getLocationPhone();
		String locationState = businessDTO.get(0).getLocationState();
		String locationZipCode = businessDTO.get(0).getLocationZipCode();
		if (dto.size() != 0) {
			String address = dto.get(0).getAddress();

			String businessname = dto.get(0).getBusinessname();
			String city = dto.get(0).getCity();
			String zip = dto.get(0).getZip();
			String phone = dto.get(0).getPhone();
			String storeUrl = dto.get(0).getWebsite();

			String directory = dto.get(0).getDirectory();
			// logger.info("directory:::::::::::::::" + directory);
			model.addAttribute("storeUrl", storeUrl);
			model.addAttribute("store", store);
			String state = dto.get(0).getState();
			if (storeUrl != null && storeUrl.length() > 1
					&& storeUrl.contains("www")) {
				// logger.info("storeUrl:::::::::::::" + storeUrl);
				storeUrl = storeUrl.substring(storeUrl.indexOf("www.") + 4);

				// logger.info("storeUrl:::::::::::::" + storeUrl);
			}
			if (storeUrl != null && storeUrl.length() > 0
					&& storeUrl.contains(".?")) {
				storeUrl = storeUrl.substring(0, storeUrl.indexOf("?") + 1);
			}
			if (webAddress != null && webAddress.length() > 1
					&& webAddress.contains("www")) {
				webAddress = webAddress
						.substring(webAddress.indexOf("www.") + 4);
			}

			if (webAddress != null && webAddress.length() > 0
					&& webAddress.contains("?")) {
				webAddress = webAddress.substring(0,
						webAddress.indexOf("?") + 1);
			}

			int count = 0;
			if (directory == null) {
				directory = "";
			}
			if (address == null) {
				address = "";
			}
			if (city == null) {
				city = "";
			}

			if (zip == null) {
				zip = "";
			}

			if (state == null) {
				state = "";
			}
			if (phone == null) {
				phone = "";
			}
			if (businessname == null) {
				businessname = "";
			}
			if (storeUrl == null) {
				storeUrl = "";
			}

			/*
			 * if(!address.equalsIgnoreCase(locationAddress)){
			 * logger.info("count:::::::::::::::" + count); count++; }
			 */
			if (!businessname.equalsIgnoreCase(companyName)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}
			if (!city.equalsIgnoreCase(locationCity)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}
			if (!zip.equalsIgnoreCase(locationZipCode)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}
			if (!state.equalsIgnoreCase(locationState)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}
			if (!storeUrl.equalsIgnoreCase(webAddress)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}

			if (phone != null && phone.contains("Add")) {
				phone = phone.substring(0, phone.indexOf("Add"));

				phone = phone.trim();
			}
			if (phone != null && phone.contains("+1")) {
				phone = phone.replaceAll("\\+1", "");
				phone = phone.trim();
			}
			if (phone != null && phone.contains("-")) {
				phone = phone.replaceAll("-", "");
				phone = phone.trim();
			}
			if (phone != null && phone.contains(")")) {
				phone = phone.replaceAll("[)]", "");
				phone = phone.trim();
			}
			if (phone != null && phone.contains("(")) {
				phone = phone.replaceAll("[(]", "");
				phone = phone.trim();
			}
			if (phone != null) {
				phone = phone.replaceAll("\\s+", "");
			}

			if (locationPhone != null) {
				locationPhone = locationPhone.replaceAll("\\s+", "");
			}

			if (locationPhone != null && locationPhone.contains("Add")) {
				locationPhone = locationPhone.substring(0,
						locationPhone.indexOf("Add"));

				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains("+1")) {
				locationPhone = locationPhone.replaceAll("\\+1", "");
				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains("-")) {
				locationPhone = locationPhone.replaceAll("-", "");
				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains(")")) {
				locationPhone = locationPhone.replaceAll("[)]", "");
				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains("(")) {
				locationPhone = locationPhone.replaceAll("[(]", "");
				locationPhone = locationPhone.trim();
			}

			if (!phone.equalsIgnoreCase(locationPhone)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}

			String adressColorCode = "B";

			if (locationAddress == null) {
				locationAddress = "";
			}
			String[] locationAddressArray = locationAddress.split(" ");
			String lastWordinLocationAddress = "";
			if (locationAddressArray.length > 0) {
				lastWordinLocationAddress = locationAddressArray[locationAddressArray.length - 1];
			}

			String[] locationDirectoryAddressArray = address.split(" ");
			String lastWordinDirectoryddress = "";
			if (locationAddressArray.length > 0) {
				lastWordinDirectoryddress = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 1];

			}

			if (!locationAddress.equalsIgnoreCase(address)) {

				if (!lastWordinDirectoryddress
						.equalsIgnoreCase(lastWordinLocationAddress)) {
					// get the the Abbrevation
					boolean isAbbreviationExist = businessservice
							.isAbbreviationExist(lastWordinDirectoryddress,
									lastWordinLocationAddress);
					String secondLastWordInDirectory = "";
					String secondLastWordInLBL = "";

					if (locationAddressArray.length > 1
							&& locationDirectoryAddressArray.length > 1) {
						secondLastWordInLBL = locationAddressArray[locationAddressArray.length - 2];
						secondLastWordInDirectory = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 2];
					}

					boolean isSecondWordsMatch = false;
					if (secondLastWordInLBL != ""
							&& secondLastWordInDirectory != "") {
						isSecondWordsMatch = secondLastWordInLBL
								.equalsIgnoreCase(secondLastWordInDirectory);
					}

					if (!isAbbreviationExist || !isSecondWordsMatch) {
						adressColorCode = "R";
						count++;
					}
				}
			}

			int LocationAccuracy = service.getGoogleAccuracy(store, brandname,
					directory);
			// logger.info("googleAccurcay:::::::::::::::" + LocationAccuracy);
			// int accuracy = count % 10 * 10;
			int inAccuracy = (count * 2) % 10 * 10;
			model.addAttribute("LocationAccuracy", LocationAccuracy);
			model.addAttribute("WebsiteUrl", storeUrl);
			model.addAttribute("adressColorCode", adressColorCode);
			model.addAttribute("count", count);
			model.addAttribute("accuracy", inAccuracy);
			model.addAttribute("LBLwebAddress", webAddress);
		}

		model.addAttribute("webAddress", businessDTO.get(0).getWebAddress());
		model.addAttribute("SourceWebsiteUrl", dto.get(0).getWebsite());
		model.addAttribute("store", businessDTO.get(0).getStore());
		model.addAttribute("lblportlisting", businessDTO);
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		model.addAttribute("googleKey", LBLConstants.google_Key);
		controllerUtil.setUserAndBussinessDataToModel(model, request,
				businessservice);

		return "clientcompare_listings";
	}

	private int getErrorCount(String store,String brandName) {
		Map<String, Integer> errorsCount = service
				.getErrorsCount(store,brandName);

		Integer googleErrors = errorsCount.get("google");
		Integer bingErrors = errorsCount.get("bing");
		Integer yelpErrors = errorsCount.get("yelp");
		Integer YahooErrors = errorsCount.get("yahoo");
		Integer ypErrors = errorsCount.get("yp");
		Integer citysearchErrors = errorsCount.get("citysearch");
		Integer mapquestErrors = errorsCount.get("mapquest");
		Integer superpagesErrors = errorsCount.get("superpages");
		Integer yellobookErrors = errorsCount.get("yellowbook");
		Integer whitepagesErrors = errorsCount.get("whitepages");
		int errorCount = 0;
		if(googleErrors==-1){
			googleErrors=0;
		}
		if(bingErrors==-1){
			bingErrors=0;
		}
		if(yellobookErrors==-1){
			yellobookErrors=0;
		}
		if(ypErrors==-1){
			ypErrors=0;
		}
		if(whitepagesErrors==-1){
			whitepagesErrors=0;
		}
		if(superpagesErrors==-1){
			superpagesErrors=0;
		}
		if(mapquestErrors==-1){
			mapquestErrors=0;
		}
		if(citysearchErrors==-1){
			citysearchErrors=0;
		}
		if(YahooErrors==-1){
			YahooErrors=0;
		}
		if(yelpErrors==-1){
			yelpErrors=0;
		}
		
		
		if(googleErrors!=0){
			errorCount++;
			
		}
		if( bingErrors!=0){
			errorCount++;
			
		}
		if( yelpErrors!=0){
			errorCount++;
			
		}
		if( YahooErrors!=0){
			errorCount++;
			
		}
		if(ypErrors!=0){
			errorCount++;
			
		}
		if(citysearchErrors!=0){
			errorCount++;
			
		}
		if(mapquestErrors!=0){
			errorCount++;
			
		}
		if(superpagesErrors!=0){
			errorCount++;
			
		}
		if(yellobookErrors!=0){
			errorCount++;
			
		}
		if(whitepagesErrors!=0){
			errorCount++;
			
		}
		logger.info("greenCount:::::::::"+errorCount);
		return errorCount;
		
	}

	@RequestMapping(value = "/clientcompare-listings.htm", method = RequestMethod.GET)
	public String getCompareReportListing(Model model,
			@RequestParam("directory") String directory,
			@RequestParam("store") String store, HttpSession session,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String directory1 = request.getParameter("directory");
		logger.info("listingId :::" + directory1);
		String store1 = request.getParameter("store");

		logger.info("store :::" + store);
		String brandname = request.getParameter("brandname");
		logger.info("brandname:::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);

		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		Set<CheckReportDTO> checkReportListinginfo = service
				.CheckReportListingSearchInfo(store, directory,brandname);
		List<CheckReportDTO> dto = new ArrayList<CheckReportDTO>(
				checkReportListinginfo);

		logger.info("dto:::::::::::::" + dto.size());
		int errorCount = getErrorCount(store,brandname);
		model.addAttribute("directoryShowingErrors", errorCount);
		if (dto.size() == 0) {
			model.addAttribute("brandname", brandname);
			model.addAttribute("liststoreNames", storeForBrand);
			model.addAttribute("message", "No Data Found");
			model.addAttribute("directory", directory);
		}
		model.addAttribute("checkreportlisting", dto);

		List<LocalBusinessDTO> businessDTO = service.getBusinesslisting(store,brandname);
		String webAddress = businessDTO.get(0).getWebAddress();
		String companyName = businessDTO.get(0).getCompanyName();
		String locationAddress = businessDTO.get(0).getLocationAddress();
		String locationCity = businessDTO.get(0).getLocationCity();
		String locationPhone = businessDTO.get(0).getLocationPhone();
		String locationState = businessDTO.get(0).getLocationState();
		String locationZipCode = businessDTO.get(0).getLocationZipCode();
		if (dto.size() != 0) {
			String address = dto.get(0).getAddress();
			String businessname = dto.get(0).getBusinessname();
			String city = dto.get(0).getCity();
			String zip = dto.get(0).getZip();
			String phone = dto.get(0).getPhone();
			String storeUrl = dto.get(0).getWebsite();
			// System.out.println("storeUrl::::::::::::::::::::::::::" +
			// storeUrl);
			model.addAttribute("storeUrl", storeUrl);
			model.addAttribute("store", store);
			String state = dto.get(0).getState();
			if (storeUrl != null && storeUrl.length() > 1
					&& storeUrl.contains("www")) {
				// logger.info("storeUrl:::::::::::::" + storeUrl);
				storeUrl = storeUrl.substring(storeUrl.indexOf("www.") + 4);

				logger.info("storeUrl:::::::::::::" + storeUrl);
			}
			if (storeUrl != null && storeUrl.length() > 0
					&& storeUrl.contains("?")) {
				storeUrl = storeUrl.substring(0, storeUrl.indexOf("?") + 1);
			}
			if (webAddress != null && webAddress.length() > 1
					&& webAddress.contains("www")) {
				webAddress = webAddress
						.substring(webAddress.indexOf("www.") + 4);
			}

			if (webAddress != null && webAddress.length() > 0
					&& webAddress.contains("?")) {
				webAddress = webAddress.substring(0,
						webAddress.indexOf("?") + 1);
			}

			int count = 0;
			if (businessname == null) {
				businessname = "";
			}
			if (address == null) {
				address = "";
			}
			if (city == null) {
				city = "";
			}

			if (zip == null) {
				zip = "";
			}

			if (state == null) {
				state = "";
			}
			if (phone == null) {
				phone = "";
			}
			if (storeUrl == null) {
				storeUrl = "";
			}

			/*
			 * if (!address.equalsIgnoreCase(locationAddress)) {
			 * logger.info("count:::::::::::::::" + count); count++; }
			 */
			if (!businessname.equalsIgnoreCase(companyName)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}
			if (!city.equalsIgnoreCase(locationCity)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}
			if (!zip.equalsIgnoreCase(locationZipCode)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}
			if (!state.equalsIgnoreCase(locationState)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}
			if (!storeUrl.equalsIgnoreCase(webAddress)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}
			if (phone != null && phone.contains("Add")) {
				phone = phone.substring(0, phone.indexOf("Add"));

				phone = phone.trim();
			}
			if (phone != null && phone.contains("+1")) {
				phone = phone.replaceAll("\\+1", "");
				phone = phone.trim();
			}
			if (phone != null && phone.contains("-")) {
				phone = phone.replaceAll("-", "");
				phone = phone.trim();
			}
			if (phone != null && phone.contains(")")) {
				phone = phone.replaceAll("[)]", "");
				phone = phone.trim();
			}
			if (phone != null && phone.contains("(")) {
				phone = phone.replaceAll("[(]", "");
				phone = phone.trim();
			}
			if (phone != null) {
				phone = phone.replaceAll("\\s+", "");
			}

			if (locationPhone != null) {
				locationPhone = locationPhone.replaceAll("\\s+", "");
			}

			if (locationPhone != null && locationPhone.contains("Add")) {
				locationPhone = locationPhone.substring(0,
						locationPhone.indexOf("Add"));

				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains("+1")) {
				locationPhone = locationPhone.replaceAll("\\+1", "");
				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains("-")) {
				locationPhone = locationPhone.replaceAll("-", "");
				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains(")")) {
				locationPhone = locationPhone.replaceAll("[)]", "");
				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains("(")) {
				locationPhone = locationPhone.replaceAll("[(]", "");
				locationPhone = locationPhone.trim();
			}

			if (!phone.equalsIgnoreCase(locationPhone)) {
				logger.info("count:::::::::::::::" + count);
				count++;
			}

			String adressColorCode = "B";

			if (locationAddress == null) {
				locationAddress = "";
			}
			String[] locationAddressArray = locationAddress.split(" ");
			String lastWordinLocationAddress = "";
			if (locationAddressArray.length > 0) {
				lastWordinLocationAddress = locationAddressArray[locationAddressArray.length - 1];
			}

			String[] locationDirectoryAddressArray = address.split(" ");
			String lastWordinDirectoryddress = "";
			if (locationAddressArray.length > 0) {
				lastWordinDirectoryddress = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 1];
				System.out.println("lastWordinDirectoryddress"
						+ lastWordinDirectoryddress);
			}

			if (!locationAddress.equalsIgnoreCase(address)) {

				if (!lastWordinDirectoryddress
						.equalsIgnoreCase(lastWordinLocationAddress)) {
					// get the the Abbrevation
					boolean isAbbreviationExist = businessservice
							.isAbbreviationExist(lastWordinDirectoryddress,
									lastWordinLocationAddress);

					String secondLastWordInDirectory = "";
					String secondLastWordInLBL = "";

					if (locationAddressArray.length > 1
							&& locationDirectoryAddressArray.length > 1) {
						secondLastWordInLBL = locationAddressArray[locationAddressArray.length - 2];
						secondLastWordInDirectory = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 2];
					}

					boolean isSecondWordsMatch = false;
					if (secondLastWordInLBL != ""
							&& secondLastWordInDirectory != "") {
						isSecondWordsMatch = secondLastWordInLBL
								.equalsIgnoreCase(secondLastWordInDirectory);
					}

					if (!isAbbreviationExist || !isSecondWordsMatch) {
						adressColorCode = "R";
						count++;
					}
				}
			}
			int LocationAccuracy = service.getGoogleAccuracy(store, brandname,
					directory);
			// logger.info("googleAccurcay:::::::::::::::" + LocationAccuracy);
			// int accuracy = count % 10 * 10;
			int inAccuracy = (count * 2) % 10 * 10;
			model.addAttribute("LocationAccuracy", LocationAccuracy);
			model.addAttribute("adressColorCode", adressColorCode);
			model.addAttribute("count", count);
			model.addAttribute("accuracy", inAccuracy);
			model.addAttribute("WebsiteUrl", storeUrl);
			model.addAttribute("LBLwebAddress", webAddress);
		}
		model.addAttribute("webAddress", businessDTO.get(0).getWebAddress());
		model.addAttribute("store", businessDTO.get(0).getStore());
		model.addAttribute("directory", directory);
		model.addAttribute("lblportlisting", businessDTO);
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		model.addAttribute("googleKey", LBLConstants.google_Key);
		controllerUtil.setUserAndBussinessDataToModel(model, request,
				businessservice);

		return "clientcompare_listings";
	}

	public boolean loginSessionValidation(Model model, HttpSession session) {
		String loginName = (String) session.getAttribute("loginName");
		String loginRole = (String) session.getAttribute("loginRole");
		if (loginName == null
				|| (loginRole != null && !loginRole
						.equalsIgnoreCase("clientAdmin"))) {
			model.addAttribute("loginCommand", new UsersBean());
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/clientclreportxls.htm", method = RequestMethod.GET)
	public String reportdownload(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		LinkedHashSet<CheckReportDTO> reportType = (LinkedHashSet<CheckReportDTO>) session
				.getAttribute("checklistreportdata");
		model.addAttribute("listOfAPIs", reportType);
		model.addAttribute("checkreportType", "CheckListReport");
		return "checkreportView";

	}
/*
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/clreportpdf.htm", method = RequestMethod.GET)
	public String reportpdfdownload(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		LinkedHashSet<CheckReportDTO> reportType = (LinkedHashSet<CheckReportDTO>) session
				.getAttribute("checklistreportdata");
		model.addAttribute("listOfAPIs", reportType);
		model.addAttribute("checkreportType", "CheckListReport");
		return "checkpdfView";

	}
*/

	


}
