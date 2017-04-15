package com.business.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.AccuracyGraphDTO;
import com.business.model.pojo.AccuracyPercentageEntity;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.ReportService;
import com.business.web.bean.UsersBean;
import com.business.web.validator.LocalBusinessValidator;
import com.google.gson.Gson;

@Controller
public class AccuracyReportController {

	Logger logger = Logger.getLogger(AccuracyReportController.class);

	@Autowired
	private CheckReportService service;
	@Autowired
	private BusinessService businessservice;
	@Autowired
	private ReportService reportService;

	@Autowired
	private LocalBusinessValidator businessValidator;

	@RequestMapping(value = "/reports-accuracy.htm", method = RequestMethod.GET)
	public String getCheckReportListing(
			@RequestParam("brand") String brandname, Model model,
			HttpSession session, HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "mode");

		int intialCount = 10;
		int size = storeForBrand.size();
		if (size < 10) {
			intialCount = size;
		}

		for (int i = 0; i < intialCount; i++) {
			String store = storeForBrand.get(i);

			AccuracyDTO dto = service.getAccuracyListInfo(store, brandname);

			accuracyreports.add(dto);
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();

		int totalStores = service.gettotalStores(brandname);
		int totalstoresSize = Math.round(totalStores);
		model.addAttribute("totalstoresSize", totalstoresSize);
		model.addAttribute("brandname", brandname);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreports);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";

	}

	public boolean loginSessionValidation(Model model, HttpSession session) {
		String loginName = (String) session.getAttribute("loginName");
		String loginRole = (String) session.getAttribute("loginRole");
		if (loginName == null
				|| (loginRole != null && loginRole
						.equalsIgnoreCase("clientAdmin"))) {
			model.addAttribute("loginCommand", new UsersBean());
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/load-morestore.htm", method = RequestMethod.GET)
	public String getAccuaracyListing(Model model, HttpSession session,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);

		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		for (String store : storeForBrand) {

			AccuracyDTO dto = service.getAccuracyListInfo(store, brandname);

			accuracyreports.add(dto);
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();

		int totalStores = service.gettotalStores(brandname);
		int totalstoresSize = Math.round(totalStores);
		model.addAttribute("totalstoresSize", totalstoresSize);
		model.addAttribute("brandname", brandname);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreports);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/getGraphInfo.htm", method = RequestMethod.GET)
	public @ResponseBody
	String inActiveBrands(@RequestParam("brandname") String brandname,

	Model model) throws IllegalStateException, SystemException,
			JsonGenerationException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		List<AccuracyGraphDTO> accuracygraph = service.getGraphInfo(brandname);

		logger.info("Total inactive brands found for Brand are:"
				+ accuracygraph.size());

		model.addAttribute("accuracygraph", accuracygraph);
		model.addAttribute("accuracyListSize",
				new Gson().toJson(accuracygraph.size()));
		json = objectMapper.writeValueAsString(accuracygraph);
		return json;

	}

	@RequestMapping(value = "/getListingByStore.htm", method = RequestMethod.GET)
	public String getCheckReportListingBysStore(Model model,
			HttpSession session, @RequestParam("store") String store,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		logger.info("store::::::::::::::" + store);
		String brandname = request.getParameter("brand");
		String percentage = request.getParameter("percentage");
		logger.info("percentage::::::::::::::" + percentage);
		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");

		AccuracyDTO dto = service.getAccuracyListInfo(store, brandname);

		accuracyreports.add(dto);
		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();
		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreports);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";

	}

	@RequestMapping(value = "/accuracyreportxls.htm", method = RequestMethod.GET)
	public String reportdownload(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);

		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		for (String store : storeForBrand) {

			AccuracyDTO dto = service.getAccuracyListInfo(store, brandname);

			accuracyreports.add(dto);
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();
		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreports);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		LinkedHashSet<AccuracyDTO> reportType = (LinkedHashSet<AccuracyDTO>) (accuracyreportInfo);

		model.addAttribute("listOfAPIs", reportType);
		model.addAttribute("accuracyreportType", "AccuracyListReport");
		return "accuracyreportView";

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/accuracyreportpdf.htm", method = RequestMethod.GET)
	public String reportpdfdownload(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		LinkedHashSet<AccuracyDTO> reportType = (LinkedHashSet<AccuracyDTO>) session
				.getAttribute("accuracyreports");
		model.addAttribute("listOfAPIs", reportType);
		model.addAttribute("accuracyreportType", "AccuracyListReport");
		return "accuracypdfView";

	}

	@RequestMapping(value = "/sByGoolge.htm", method = RequestMethod.GET)
	public String getSortByGoogleAccuaracyListing(Model model,
			HttpSession session, @RequestParam("flag") String flag,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);

		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortByGoogleAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();
		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/sByBing.htm", method = RequestMethod.GET)
	public String getSortByBingAccuaracyListing(Model model,
			HttpSession session, @RequestParam("flag") String flag,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);

		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortByBingAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();

		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/sByYahoo.htm", method = RequestMethod.GET)
	public String getSortByYahooAccuaracyListing(Model model,
			HttpSession session, @RequestParam("flag") String flag,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		String brandname1 = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortByYahooAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();

		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/sByYelp.htm", method = RequestMethod.GET)
	public String getSortByYelpAccuaracyListing(Model model,
			HttpSession session, @RequestParam("flag") String flag,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		String brandname1 = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortByYelpAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();
		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/sByYp.htm", method = RequestMethod.GET)
	public String getSortByYpAccuaracyListing(Model model, HttpSession session,
			@RequestParam("flag") String flag, HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		String brandname1 = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortByYpAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();
		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/sByCitySearch.htm", method = RequestMethod.GET)
	public String getSortByCitySearchAccuaracyListing(Model model,
			HttpSession session, @RequestParam("flag") String flag,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		String brandname1 = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortByCitySearchAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();

		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/sByMapQuest.htm", method = RequestMethod.GET)
	public String getSortByMapQuestAccuaracyListing(Model model,
			HttpSession session, @RequestParam("flag") String flag,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		String brandname1 = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortByMapquestAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();

		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/sBySuperPages.htm", method = RequestMethod.GET)
	public String getSortBySuperPagesAccuaracyListing(Model model,
			HttpSession session, @RequestParam("flag") String flag,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		String brandname1 = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortBySuperPagesAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();

		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/sByYellowBook.htm", method = RequestMethod.GET)
	public String getSortByYellowBookAccuaracyListing(Model model,
			HttpSession session, @RequestParam("flag") String flag,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		String brandname1 = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortByYellowBookAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}

		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();

		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/sByWhitePages.htm", method = RequestMethod.GET)
	public String getSortByWhitePagesAccuaracyListing(Model model,
			HttpSession session, @RequestParam("flag") String flag,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		String brandname1 = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortByWhitePagesAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();
		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/sByStore.htm", method = RequestMethod.GET)
	public String getSortByStoreAccuaracyListing(Model model,
			HttpSession session, @RequestParam("flag") String flag,
			HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		String brandname1 = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortByStoreAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();
		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

	@RequestMapping(value = "/sByAccracy.htm", method = RequestMethod.GET)
	public String getSortByAccuaracyListing(Model model, HttpSession session,
			@RequestParam("flag") String flag, HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String brandname = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		String brandname1 = request.getParameter("brand");
		logger.info("brandname::::::::::::::" + brandname);
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		List<AccuracyDTO> accuracyreports = new ArrayList<AccuracyDTO>();

		model.addAttribute("mode", "");
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<AccuracyDTO> accuracyreportsvalue = service
				.getSortByAccuracyListInfo(brandname, flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		AccuracyPercentageEntity accuracyPercentageEntity = businessservice
				.getAcccuracyPercentageInfo(brandname);
		Integer percentage1 = accuracyPercentageEntity.getPercentage1();
		Integer percentage2 = accuracyPercentageEntity.getPercentage2();
		Integer percentage3 = accuracyPercentageEntity.getPercentage3();
		Integer percentage4 = accuracyPercentageEntity.getPercentage4();
		double totalPercentage = accuracyPercentageEntity.getTotalPercentage();
		int totalStores = service.gettotalStores(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("totalPercentage", totalPercentage);
		model.addAttribute("totalStores", totalStores);
		model.addAttribute("percentageCategory1", percentage1);
		model.addAttribute("percentageCategory2", percentage2);
		model.addAttribute("percentageCategory3", percentage3);
		model.addAttribute("percentageCategory4", percentage4);
		model.addAttribute("accuracyreports", accuracyreportsvalue);
		Set<AccuracyDTO> accuracyreportInfo = new LinkedHashSet<AccuracyDTO>(
				accuracyreports);
		session.setAttribute("accuracyreports", accuracyreportInfo);
		return "reports-accuracy";
	}

}
