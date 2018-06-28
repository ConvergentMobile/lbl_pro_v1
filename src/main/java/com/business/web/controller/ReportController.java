package com.business.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.business.common.dto.BingAnalyticsDTO;
import com.business.common.dto.BingReportDTO;
import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChangeTrackingDTO;
import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.GoogleInsightsReportDTO;
import com.business.common.dto.InsightsGraphDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.MonthlyReportDTO;
import com.business.common.dto.RankDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.common.dto.SweetIQAccuracyDTO;
import com.business.common.dto.SweetIQCoverageDTO;
import com.business.common.dto.TrendDTO;
import com.business.common.util.ControllerUtil;
import com.business.common.util.DateUtil;
import com.business.common.util.LBLConstants;
import com.business.model.pojo.AuditEntity;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ReportParams;
import com.business.model.pojo.ValueObject;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.ListingService;
import com.business.service.ReportService;
import com.business.service.SweetIQService;
import com.business.web.bean.ReportForm;
import com.business.web.bean.UsersBean;

/**
 * @author lbl_dev
 */
@Controller
public class ReportController {
	Logger logger = Logger.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;	
	@Autowired
	private CheckReportService checkReportService;

	@Autowired
	private BusinessService service;

	@Autowired
	private ListingService listingService;

	@Autowired
	private SweetIQService sweetIQService;

	private ControllerUtil controllerUtil = new ControllerUtil();

	static String pathtoPDF = LBLConstants.PDF_PATH;
	static String libertyAccessKey = LBLConstants.LIBERTY_ACCESS_KEY;

	/**
	 * getReports
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/reports.htm", method = RequestMethod.GET)
	public String getReports(Model model, HttpServletRequest request,
			HttpSession session) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		Set<ReportEntity> reports = reportService.getReports();

		ReportEntity selReport = null;
		if (request.getParameter("reportId") == null
				|| request.getParameter("reportId") == "") { // get
			// the
			// first
			// report
			Iterator<ReportEntity> iterator = reports.iterator();
			while (iterator.hasNext()) {
				selReport = iterator.next();
				break;
			}
		} else {
			Integer reportId = Integer
					.valueOf(request.getParameter("reportId"));
			Iterator<ReportEntity> iterator = reports.iterator();
			while (iterator.hasNext()) {
				selReport = iterator.next();
				if (selReport.getId().equals(reportId)) {
					break;
				}
			}
		}

		model.addAttribute("reports", reports);
		ReportForm rptForm = new ReportForm();
		rptForm.setReport(selReport);
		model.addAttribute("reportForm", rptForm);
		controllerUtil.getUploadDropDown(model, request, service);
		return "reports";
	}

	/**
	 * runReport::shows the upload/export info
	 * 
	 * @param reportForm
	 * @param bindingResult
	 * @param whichPage
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/runReport.htm", method = RequestMethod.POST)
	public ModelAndView runChangeReport(
			@ModelAttribute("reportForm") ReportForm reportForm,
			BindingResult bindingResult,
			@RequestParam(value = "page", required = false) String whichPage,
			HttpServletRequest request, HttpSession session, Model model)
			throws Exception {
		if (!loginSessionValidation(model, session)) {
			ModelAndView mv = new ModelAndView("logout");
			return mv;
		}
		model.addAttribute("uploadReports", new ArrayList<LocalBusinessDTO>());
		ReportForm rptForm = null;
		ModelAndView mv = new ModelAndView("reports");
		Set<ReportEntity> reports = reportService.getReports();
		mv.addObject("reports", reports);

		int page = 1;
		int recordsPerPage = Integer.valueOf("1000");

		if (whichPage != null)
			page = Integer.valueOf(whichPage);

		Map<String, Object> params = new HashMap<String, Object>();

		String brand = request.getParameter("client");
		List<String> listofStores = new ArrayList<String>();
		ReportEntity reportob = reportForm.getReport();
		Integer reportId = reportob.getId();

		String[] StoreName = request.getParameterValues("StoreValues");
		/* logger.info("StoreName:::::::::::::" + StoreName.toString()); */
		/*
		 * if(reportId==5 && store!=null){
		 * 
		 * for (String string : store) { logger.info("StoreName:::::::::::::" +
		 * string); listofStores.add(string); } logger.info("stores from jsp::"
		 * +listofStores.size()); }
		 */
		if (reportId == 5 && StoreName != null) {

			logger.info("StoreValues:::::::::::::" + StoreName);
			for (String string : StoreName) {
				listofStores.add(string);
			}

			logger.info("stores::" + listofStores.size());
		}
		logger.info("client name in reports page ::" + brand);
		Date fromDate = reportForm.getStartDate();
		Date toDate = reportForm.getEndDate();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		if (fromDate != null && toDate != null) {
			// check that to_date >= from_date
			if (toDate.compareTo(fromDate) < 0) {
				mv.addObject("error",
						"Start Date cannot be later than End Date");
				controllerUtil.getUploadDropDown(model, request, service);
				return mv;
			} else {
				params.put("fromDate", sdf.format(fromDate));
				params.put("toDate", sdf.format(toDate));
			}
		}

		if (fromDate == null && toDate != null) {
			mv.addObject("error", "Start Date cannot be Null");
			controllerUtil.getUploadDropDown(model, request, service);
			return mv;
		}

		if (fromDate != null) {
			params.put("fromDate", sdf.format(fromDate));
		}

		if (fromDate != null && toDate == null) {
			toDate = Calendar.getInstance().getTime();
		}

		if (toDate != null) {
			params.put("toDate", sdf.format(toDate));
		}

		ReportEntity report = reportForm.getReport();
		if (report.getParams() != null && report.getParams().size() > 0) {
			for (ReportParams rp : report.getParams()) {
				params.put(rp.getParamName(), rp.getParamValue());
				logger.debug("p/v: " + rp.getParamName() + ", "
						+ rp.getParamValue());
			}
		}

		// otherParams are used to pass report params that are not user
		// specified
		Map<String, String> otherParams = new HashMap<String, String>();
		otherParams
				.put("userName", session.getAttribute("userName").toString());
		otherParams.put("roleId", session.getAttribute("roleId").toString());
		Integer id = report.getId();

		if (brand == null || brand == "") {
			rptForm = reportService.runReport(id, report.getParams(),
					otherParams, (page - 1) * recordsPerPage, recordsPerPage,
					reportForm.getSortColumn(), reportForm.getSortOrder(),
					fromDate, toDate, brand);
		} else if (brand != null && listofStores.size() > 0) {
			rptForm = reportService.runchangeReport(id, report.getParams(),
					otherParams, (page - 1) * recordsPerPage, recordsPerPage,
					reportForm.getSortColumn(), reportForm.getSortOrder(),
					fromDate, toDate, brand, listofStores);

		} else {
			rptForm = reportService.runBrandReport(id, report.getParams(),
					otherParams, (page - 1) * recordsPerPage, recordsPerPage,
					reportForm.getSortColumn(), reportForm.getSortOrder(),
					fromDate, toDate, brand);
		}

		int noOfRecords = 0;
		int noOfPages = 0;

		List<ValueObject> reportRows = rptForm.getReportRows();
		if (reportRows != null && !reportRows.isEmpty()) {
			noOfRecords = (Integer) reportRows.get(reportRows.size() - 1)
					.getField1();
			noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		}

		logger.debug("noOfRecords, noOfPages: " + noOfRecords + ", "
				+ noOfPages);

		if (!reportRows.isEmpty()) {
			reportRows.remove(reportRows.size() - 1);
		}

		logger.debug("colHeaders: " + rptForm.getReportColumnHeaders().size());

		request.setAttribute("noOfPages", noOfPages);
		request.setAttribute("currentPage", page);

		int currentPageStart = page - 5;
		if (currentPageStart < 1)
			currentPageStart = 1;

		int currentPageEnd = page + 4;
		if (currentPageStart == 1)
			currentPageEnd = 10;

		if (currentPageEnd > noOfPages)
			currentPageEnd = noOfPages;

		if (noOfPages <= 10) {
			currentPageStart = 1;
			currentPageEnd = noOfPages;
		}
		rptForm.setBrandName(brand);
		request.setAttribute("currentPageStart", currentPageStart);
		request.setAttribute("currentPageEnd", currentPageEnd);
		controllerUtil.getUploadDropDown(model, request, service);
		mv.addObject("reportForm", rptForm);

		String reportType = "other";
		if (id == 1) {
			reportType = "RenewalReport";
		}
		if (id == 2) {
			reportType = "UploadReport";
		}
		if (id == 3) {
			reportType = "ExportReport";
		}
		if (id == 4) {
			reportType = "DistributionReport";
		}
		if (id == 5) {
			reportType = "ChangeTrackingReport";
		}
		mv.addObject("reportType", reportType);

		ReportEntity selReport = null;
		Iterator<ReportEntity> iterator = reports.iterator();
		while (iterator.hasNext()) {
			selReport = iterator.next();
			if (selReport.getId().equals(id)) {
				break;
			}
		}
		rptForm.setReport(selReport);
		session.setAttribute("reportType", reportType);
		session.setAttribute("reportForm", rptForm);
		session.setAttribute("reports", reports);
		return mv;

	}

	@RequestMapping(value = "/products_ajax", method = RequestMethod.GET)
	public @ResponseBody
	String productsForCategory(
			@RequestParam(value = "categoryId", required = true) String categoryId,
			Model model) throws IllegalStateException, SystemException {

		// List<String> storeForBrand =
		// reportService.getStoreForBrand(categoryId);
		// logger.info("stores after selecting brand::" + storeForBrand);

		BrandInfoDTO brandInfoByBrandName = service
				.getBrandInfoByBrandName(categoryId);

		Map<Long, String> lblStoreMapByCleintId = listingService
				.getLBLStoreMapByCleintId(brandInfoByBrandName.getClientId());

		List<String> storeForBrand = new ArrayList<String>();

		Set<Long> keySet = lblStoreMapByCleintId.keySet();
		for (Long lblStoreID : keySet) {
			storeForBrand.add(lblStoreMapByCleintId.get(lblStoreID));
		}

		model.addAttribute("storelist", storeForBrand);

		return storeForBrand.toString();
	}

	@RequestMapping(value = "/statesByBrand", method = RequestMethod.GET)
	public @ResponseBody
	String statesByBrand(
			@RequestParam(value = "brandId", required = true) String brandId,
			Model model) throws IllegalStateException, SystemException {

		List<String> allStates = service.getAllStatesListByBrand(brandId);
		logger.info("States after selecting brand::" + brandId);

		return allStates.toString();
	}

	/**
	 * loginSessionValidation
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
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

	/**
	 * getUploadReportDetails
	 * 
	 * @param bindingResult
	 * @param brand
	 * @param date
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getUploadReportDetails.htm", method = RequestMethod.GET)
	public String getUploadReportDetails(@RequestParam("brand") String brand,
			@RequestParam("date") String date,
			@ModelAttribute("reportForm") ReportForm reportForm,
			BindingResult bindingResult, HttpSession session, Model model)
			throws ParseException {

		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date2 = DateUtil.getDate("MM/dd/yyyy HH:mm:ss",
				sdf.parse(date.replace("-", "/")));
		// System.out.println("date :: " + date2);
		java.sql.Timestamp timestamp = new java.sql.Timestamp(date2.getTime());
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		if (brand != null && date != null) {
			businessDTOs = reportService.getUploadReportDetails(brand,
					timestamp);
		}
		reportForm = (ReportForm) session.getAttribute("reportForm");
		model.addAttribute("reportForm", reportForm);
		model.addAttribute("uploadreportpopup", "uploadreportpopup");
		Set<ReportEntity> reports = (Set<ReportEntity>) session
				.getAttribute("reports");
		model.addAttribute("reports", reports);
		model.addAttribute("uploadReports", businessDTOs);
		model.addAttribute("reportType", "UploadReport");
		return "reports";
	}

	/**
	 * 
	 * @param brand
	 * @param date
	 * @param reportForm
	 * @param bindingResult
	 * @param session
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/distrreport.htm", method = RequestMethod.GET)
	public String getDistributionReportDetails(
			@RequestParam("brand") String brand,
			@RequestParam("date") String date, HttpServletRequest request,
			@ModelAttribute("reportForm") ReportForm reportForm,
			BindingResult bindingResult, HttpSession session, Model model)
			throws ParseException {

		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		List<ExportReportDTO> businessDTOs = new ArrayList<ExportReportDTO>();
		if (brand != null && date != null) {
			businessDTOs = reportService.getDistrbutionReportDetails(brand,
					date);
		}
		reportForm = (ReportForm) session.getAttribute("reportForm");
		model.addAttribute("reportForm", reportForm);
		Set<ReportEntity> reports = (Set<ReportEntity>) session
				.getAttribute("reports");
		model.addAttribute("reports", reports);
		model.addAttribute("DistributionReports", businessDTOs);
		model.addAttribute("reportType", "DistributionReport");
		controllerUtil.getUploadDropDown(model, request, service);
		return "reports";
	}

	@RequestMapping(value = "/reportxls.htm", method = RequestMethod.GET)
	public String reportdownload(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		ReportForm reportForm = (ReportForm) session.getAttribute("reportForm");
		String reportType = (String) session.getAttribute("reportType");
		model.addAttribute("listOfAPI", reportForm);
		model.addAttribute("reportType", reportType);
		return "reportView";

	}

	@RequestMapping(value = "/reportpdf.htm", method = RequestMethod.GET)
	public String reportpdfdownload(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		ReportForm reportForm = (ReportForm) session.getAttribute("reportForm");
		String reportType = (String) session.getAttribute("reportType");
		model.addAttribute("listOfAPI", reportForm);
		model.addAttribute("reportType", reportType);
		return "pdfView";

	}

	@RequestMapping(value = "/getStoreBasedOnBrandsandStore.htm", method = RequestMethod.GET)
	public @ResponseBody
	String getStoreBasedOnBrandsandStore(
			@RequestParam("brandname") String brandname,
			@RequestParam("store") String store, Model model,
			HttpSession session, HttpServletRequest request) throws IOException {
		logger.info("start ::  getBusinessListingByPage method");
		logger.info("brandname :: " + brandname);
		logger.info("store  :: " + store);
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		List<String> stores = service.getStoreBasedOnBrandsandStore(brandname,
				store);
		logger.info("stores:::::::::::::" + stores);

		return stores.toString();

	}

	@RequestMapping(value = "/runCtrakingReport.htm", method = RequestMethod.POST)
	public ModelAndView runReport(
			@ModelAttribute("reportForm") ReportForm reportForm,
			BindingResult bindingResult,
			@RequestParam(value = "page", required = false) String whichPage,
			HttpServletRequest request, HttpSession session, Model model)
			throws Exception {
		if (!loginSessionValidation(model, session)) {
			ModelAndView mv = new ModelAndView("logout");
			return mv;
		}
		model.addAttribute("uploadReports", new ArrayList<LocalBusinessDTO>());
		ReportForm rptForm = null;

		ModelAndView mv = new ModelAndView("changeTrackingreport");
		Set<ReportEntity> reports = reportService.getReports();
		mv.addObject("reports", reports);

		int page = 1;
		int recordsPerPage = Integer.valueOf("1000");

		if (whichPage != null)
			page = Integer.valueOf(whichPage);

		Map<String, Object> params = new HashMap<String, Object>();

		String brand = request.getParameter("client");
		List<String> listofStores = new ArrayList<String>();
		ReportEntity reportob = reportForm.getReport();
		Integer reportId = reportob.getId();

		String[] StoreName = request.getParameterValues("StoreValues");

		if (reportId == 5 && StoreName != null) {

			logger.info("StoreValues:::::::::::::" + StoreName);
			for (String string : StoreName) {
				listofStores.add(string);
			}

			logger.info("stores::" + listofStores.size());
		}
		logger.info("client name in reports page ::" + brand);
		Date fromDate = reportForm.getStartDate();
		Date toDate = reportForm.getEndDate();

		Map<Integer, List<ChangeTrackingDTO>> listing = service
				.getBusinessListing(listofStores, brand, fromDate, toDate);

		for (Map.Entry<Integer, List<ChangeTrackingDTO>> entry : listing
				.entrySet()) {

			model.addAttribute("key", entry.getKey());

			List<ChangeTrackingDTO> value = entry.getValue();

			if (entry.getKey() == 1) {
				model.addAttribute("changeList", value);
			}
			if (entry.getKey() == 2) {
				model.addAttribute("changeList2", value);
			}
			model.addAttribute("lissize", value.size());

		}
		model.addAttribute("change", listing);

		return mv;

	}

	@RequestMapping(value = "/changetrackReportxls.htm", method = RequestMethod.GET)
	public String reportChangeTrackingdownload(Model model,
			HttpServletRequest request, HttpSession session) throws Exception {

		ReportForm reportForm = (ReportForm) session
				.getAttribute("reportForms");
		List<ValueObject> reportRows = reportForm.getReportRows();

		model.addAttribute("listOfAPIs", reportRows);
		model.addAttribute("reportType", "ChangeTrackingReport");
		return "reportExcelView";

	}

	@RequestMapping(value = "/runRenewalReport", method = RequestMethod.POST)
	public ModelAndView runRenewalReport(
			@ModelAttribute("reportForm") ReportForm reportForm,
			BindingResult bindingResult,
			@RequestParam(value = "page", required = false) String whichPage,
			HttpServletRequest request, HttpSession session, Model model)
			throws Exception {

		if (!loginSessionValidation(model, session)) {
			ModelAndView mv = new ModelAndView("logout");
			return mv;
		}
		String brand = request.getParameter("client");
		List<String> storeForBrand = service.getStoreForBrand(brand);
		List<String> listofStores = new ArrayList<String>();
		String StoreName = request.getParameter("storeVal");
		logger.info("StoreName name in reports page ::" + StoreName);
		logger.info("client name in reports page ::" + brand);
		Date fromDate = reportForm.getStartDate();
		Date toDate = reportForm.getEndDate();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		ModelAndView mv = new ModelAndView("reports-renewal");
		Map<String, Object> params = new HashMap<String, Object>();
		if (fromDate != null && toDate != null) {
			// check that to_date >= from_date
			if (toDate.compareTo(fromDate) < 0) {
				mv.addObject("error",
						"Start Date cannot be later than End Date");
				controllerUtil.getUploadDropDown(model, request, service);
				return mv;
			} else {
				params.put("fromDate", sdf.format(fromDate));
				params.put("toDate", sdf.format(toDate));
			}
		}

		if (fromDate == null && toDate != null) {
			mv.addObject("error", "Start Date cannot be Null");
			controllerUtil.getUploadDropDown(model, request, service);
			return mv;
		}

		if (fromDate != null) {
			params.put("fromDate", sdf.format(fromDate));
		}

		if (fromDate != null && toDate == null) { // if no toDate specified, use
													// today
			toDate = Calendar.getInstance().getTime();
		}

		if (toDate != null) {
			params.put("toDate", sdf.format(toDate));
		}
		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		if (!StoreName.isEmpty() && !brand.isEmpty()) {
			renewalReportDTOs = service.runRenewalReport(fromDate, toDate,
					StoreName, brand);
		} else {
			renewalReportDTOs = service.runRenewalReportForBrand(fromDate,
					toDate, brand);
		}
		model.addAttribute("brandName", brand);
		model.addAttribute("StoreName", StoreName);
		model.addAttribute("renewalReports", renewalReportDTOs);
		session.setAttribute("renewalReports", renewalReportDTOs);

		return mv;

	}

	@RequestMapping(value = "/renewalReportDownload.htm", method = RequestMethod.GET)
	public String renewalReportDownload(Model model,
			HttpServletRequest request, HttpSession session) throws Exception {
		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		String brand = request.getParameter("brandName");
		String StoreName = request.getParameter("store");
		if (!StoreName.isEmpty() && !brand.isEmpty()) {
			renewalReportDTOs = checkReportService.runRenewalReport(StoreName,
					brand);
		} else {
			renewalReportDTOs = checkReportService
					.runRenewalReportForBrand(brand);
		}

		String reportType = "renewalReport";
		model.addAttribute("listOfrenewalReportAPI", renewalReportDTOs);
		model.addAttribute("renewalreportType", reportType);
		return "renewalReportView";

	}

	/**
	 * 
	 * @param reportForm
	 * @param bindingResult
	 * @param whichPage
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/runOverridenReport", method = RequestMethod.POST)
	public ModelAndView runOverridenReport(
			@ModelAttribute("reportForm") ReportForm reportForm,
			BindingResult bindingResult,
			@RequestParam(value = "page", required = false) String whichPage,
			HttpServletRequest request, HttpSession session, Model model)
			throws Exception {
		if (!loginSessionValidation(model, session)) {
			ModelAndView mv = new ModelAndView("logout");
			return mv;
		}

		ModelAndView mv = new ModelAndView("overridenreport");

		Map<String, Object> params = new HashMap<String, Object>();

		String brand = request.getParameter("client");
		List<String> storeForBrand = service.getStoreForBrand(brand);

		logger.info("client name in reports page ::" + brand);
		Date fromDate = reportForm.getStartDate();
		Date toDate = reportForm.getEndDate();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		if (fromDate != null && toDate != null) {
			// check that to_date >= from_date
			if (toDate.compareTo(fromDate) < 0) {
				mv.addObject("error",
						"Start Date cannot be later than End Date");
				controllerUtil.getUploadDropDown(model, request, service);
				return mv;
			} else {
				params.put("fromDate", sdf.format(fromDate));
				params.put("toDate", sdf.format(toDate));
			}
		}

		if (fromDate == null && toDate != null) {
			mv.addObject("error", "Start Date cannot be Null");
			controllerUtil.getUploadDropDown(model, request, service);
			return mv;
		}

		if (fromDate != null) {
			params.put("fromDate", sdf.format(fromDate));
		}

		if (fromDate != null && toDate == null) { // if no toDate specified, use
													// today
			toDate = Calendar.getInstance().getTime();
		}

		if (toDate != null) {
			params.put("toDate", sdf.format(toDate));
		}

		List<AuditEntity> listOfOverridenListings = new ArrayList<AuditEntity>();
		if (!brand.isEmpty()) {
			// rptForm = reportService.runOvveridenReport(
			// fromDate, toDate,brand);
			listOfOverridenListings = service
					.getListOfOverridenListingsByBrand(fromDate, toDate, brand);
			logger.info("listOfOverridenListings:::::::::::"
					+ listOfOverridenListings.size());
		} else {
			// rptForm = reportService.runOvveridenBrandsReport(
			// fromDate, toDate);
			listOfOverridenListings = service.getListOfOverridenListings(
					fromDate, toDate);
			logger.info("listOfOverridenListings:::::::::::"
					+ listOfOverridenListings.size());
		}
		logger.info("listOfOverridenListings:::::::::::"
				+ listOfOverridenListings.size());

		mv.addObject("liststoreNames", storeForBrand);
		mv.addObject("overridenList", listOfOverridenListings);
		session.setAttribute("listOfOverridenListings", listOfOverridenListings);
		return mv;

	}

	@RequestMapping(value = "/runSummaryReport", method = RequestMethod.GET)
	public String runSummaryReport(HttpServletRequest request,
			HttpSession session, Model model) throws Exception {

		String brand = request.getParameter("brandName");
		List<String> storeForBrand = service.getStoreForBrand(brand);
		List<String> listofStores = new ArrayList<String>();
		String StoreName = request.getParameter("store");
		logger.info("StoreName name in reports page ::" + StoreName);
		logger.info("client name in reports page ::" + brand);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		ModelAndView mv = new ModelAndView("reports-summary");
		Map<String, Object> params = new HashMap<String, Object>();

		Date fromDate = new Date();
		Date toDate = new Date();

		List<RenewalReportDTO> summaryReports = new ArrayList<RenewalReportDTO>();
		if (!StoreName.isEmpty() && !brand.isEmpty()) {
			summaryReports = checkReportService.runSummaryReport(fromDate,
					toDate, StoreName, brand);
		} else {
			summaryReports = checkReportService.runSummaryReportForBrand(
					fromDate, toDate, brand);
		}
		model.addAttribute("summaryReports", summaryReports);

		String reportType = "summaryreportType";
		model.addAttribute("listOfSummaryReportAPI", summaryReports);
		model.addAttribute("summaryreportType", reportType);
		return "summaryReportView";

	}

	@RequestMapping(value = "/runOverrideReport.htm", method = RequestMethod.GET)
	public String summaryReportDownload(Model model,
			HttpServletRequest request, HttpSession session) throws Exception {

		List<RenewalReportDTO> renewalReportDTOs = (List<RenewalReportDTO>) session
				.getAttribute("listOfOverridenListings");
		String reportType = "overridereportType";
		model.addAttribute("listOfOverrideReportAPI", renewalReportDTOs);
		model.addAttribute("overridereportType", reportType);
		return "overrideReportView";

	}

	public static java.util.Date getFormattedDate(String dateValue) {

		java.util.Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateValue);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR, 0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static java.util.Date getFormattedEndDate(String dateValue) {
		java.util.Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateValue);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR, 23);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	@RequestMapping(value = "/pdfheader.htm", method = RequestMethod.GET)
	public String getLogin(Model model) {
		logger.info("Home Page");
		model.addAttribute("loginCommand", new UsersBean());
		model.addAttribute("message", "");
		return "header";
	}

	@RequestMapping(value = "/locPerf.htm", method = RequestMethod.GET)
	public String locPerf(Model model,
			HttpServletRequest request, HttpSession session) throws Exception {

		String brand = request.getParameter("b");
		String store = request.getParameter("s");
		String duration = request.getParameter("d");


		BrandInfoDTO brandInfoByBrand = service.getBrandInfoByBrand(brand);

		String[] startandEndDates = getPastDates(0);

		Date startDate = getFormattedDate(startandEndDates[0]);
		Date endDate = getFormattedDate(startandEndDates[1]);

		Integer clientId = brandInfoByBrand.getClientId();


		SweetIQCoverageDTO sweetIQPerf = sweetIQService.getListingCoverage(
				clientId, startDate, endDate, store);
		Integer coverage = sweetIQPerf.getCoverage();
		model.addAttribute("coverage", coverage);
		Integer totalListingCount = sweetIQPerf.getTotalListingCount();
		model.addAttribute("totalListings", totalListingCount);
		Integer possibleListings = sweetIQPerf.getPossibleListings();
		model.addAttribute("possibleListings", possibleListings);
		
		
		SweetIQCoverageDTO sweetIQCoverage = sweetIQService
				.getDirectoryPerformance(startDate, endDate, clientId, store);
		

		model.addAttribute("google", sweetIQCoverage.getGoogleCoverage());
		model.addAttribute("facebook", sweetIQCoverage.getFacebookCoverage());
		model.addAttribute("bing", sweetIQCoverage.getBingCoverage());
		model.addAttribute("yellowpages",
				sweetIQCoverage.getYellowPagesCoverage());
		model.addAttribute("yahoo",
				sweetIQCoverage.getFourSquareCoverage());
		
		
		List<SweetIQAccuracyDTO> accuracy = sweetIQService.getAccuarcyForStore(
				clientId, startDate, endDate, store);
		
		Integer googleAccuracy = 0;
		Integer facebookAccuray = 0;
		Integer yellowPagesAccuracy = 0;
		Integer bingAccuracy = 0;
		Integer yahooAccuracy = 0;
		
		int i=0;
		for (SweetIQAccuracyDTO sweetIQAccuracyDTO : accuracy) {
			i++;
			String directory = sweetIQAccuracyDTO.getDirectory();
			String averageAcuuracy = sweetIQAccuracyDTO.getAverage_Match();
			Double percentage = Double.parseDouble(averageAcuuracy);
			Integer accuracyPercentage = percentage.intValue();
			if(directory.contains("google")) {
				googleAccuracy = accuracyPercentage;
			}
			else if(directory.contains("facebook") || directory.equalsIgnoreCase("facebook")) {
				facebookAccuray = accuracyPercentage;
			}
			else if(directory.contains("bing")) {
				 bingAccuracy = accuracyPercentage;
			}
			else if(directory.contains("yahoo") || directory.equalsIgnoreCase("foursquare")) {
				yahooAccuracy = accuracyPercentage;
			}
			else if(directory.contains("yellowpages")) {
				yellowPagesAccuracy = accuracyPercentage;
			}
		}
		
		Integer averageAccuracy = 0;
			if(i>0)
				averageAccuracy = (googleAccuracy + facebookAccuray + yellowPagesAccuracy + bingAccuracy + yahooAccuracy)/i;
			
		model.addAttribute("averageAccuracy",averageAccuracy);
		
		List<String> storesList = new ArrayList<String>();

		Map<Long, String> lblStoreMapByCleintId = listingService
				.getLBLStoreMapByCleintId(brandInfoByBrand.getClientId());
		Set<Long> storeSet = lblStoreMapByCleintId.keySet();
		for (Long lblStoreId : storeSet) {
			storesList.add(lblStoreMapByCleintId.get(lblStoreId));
		}
		java.util.Collections.sort(storesList);

		model.addAttribute("storeList", storesList);
		
		String[] startandEnd = getPastDates(1);
		Date start = getFormattedDate(startandEnd[0]);
		Date end= getFormattedDate(startandEnd[1]);
		

		SweetIQCoverageDTO sweetIQPerfOld = sweetIQService.getListingCoverage(
				clientId, start, end, store);
		Integer coverageOld = sweetIQPerfOld.getCoverage();
		
		int increment = coverage - coverageOld;

		
		
		SweetIQCoverageDTO sweetIQCoverageOld = sweetIQService
				.getDirectoryPerformance(start, end, clientId, "");

		model.addAttribute("googleDiff", sweetIQCoverage.getGoogleCoverage()-sweetIQCoverageOld.getGoogleCoverage());
		model.addAttribute("facebookDiff", sweetIQCoverage.getFacebookCoverage()-sweetIQCoverageOld.getFacebookCoverage());
		model.addAttribute("bingDiff", sweetIQCoverage.getBingCoverage()-sweetIQCoverageOld.getFacebookCoverage());
		model.addAttribute("yellowpagesDiff",
				sweetIQCoverage.getYellowPagesCoverage()-sweetIQCoverageOld.getYellowPagesCoverage());
		model.addAttribute("yahooDiff",
				sweetIQCoverage.getFourSquareCoverage()-sweetIQCoverageOld.getFourSquareCoverage());

		model.addAttribute("accuracy", accuracy);
		model.addAttribute("brand", brand);
		
		int d = Integer.parseInt(duration);
		
		model.addAttribute("increment", increment);
		model.addAttribute("months", d);
		model.addAttribute("duration", duration);
		model.addAttribute("store", store);

		return "location-perf";

	}

	@RequestMapping(value = "/brandPerf.htm", method = RequestMethod.GET)
	public String brandPerf(Model model,
			HttpServletRequest request, HttpSession session) throws Exception {

		String brand = request.getParameter("b");
		String duration = request.getParameter("d");
		String store = "";

		BrandInfoDTO brandInfoByBrand = service.getBrandInfoByBrand(brand);

		String[] startandEndDates = getPastDates(0);
		
		
		Date startDate = getFormattedDate(startandEndDates[0]);
		Date endDate = getFormattedDate(startandEndDates[1]);

		Integer clientId = brandInfoByBrand.getClientId();
		
		

		SweetIQCoverageDTO sweetIQPerf = sweetIQService.getListingCoverage(
				clientId, startDate, endDate, store);
		Integer coverage = sweetIQPerf.getCoverage();
		model.addAttribute("coverage", coverage);
		Integer totalListingCount = sweetIQPerf.getTotalListingCount();
		
		
		model.addAttribute("totalListings", totalListingCount);
		Integer possibleListings = sweetIQPerf.getPossibleListings();
		
	
		
		model.addAttribute("possibleListings", possibleListings);
		
		SweetIQCoverageDTO sweetIQCoverage = sweetIQService
				.getDirectoryPerformance(startDate, endDate, clientId, "");

		model.addAttribute("google", sweetIQCoverage.getGoogleCoverage());
		model.addAttribute("facebook", sweetIQCoverage.getFacebookCoverage());
		model.addAttribute("bing", sweetIQCoverage.getBingCoverage());
		model.addAttribute("yellowpages",
				sweetIQCoverage.getYellowPagesCoverage());
		model.addAttribute("yahoo",
				sweetIQCoverage.getFourSquareCoverage());
		
		
		List<SweetIQAccuracyDTO> accuracy = sweetIQService.getAccuarcy(
				clientId, startDate, endDate, store);
		
		Integer googleAccuracy = 0;
		Integer facebookAccuray = 0;
		Integer yellowPagesAccuracy = 0;
		Integer bingAccuracy = 0;
		Integer yahooAccuracy = 0;
		
		
		for (SweetIQAccuracyDTO sweetIQAccuracyDTO : accuracy) {
			String directory = sweetIQAccuracyDTO.getDirectory();
			String averageAcuuracy = sweetIQAccuracyDTO.getAverage_Match();
			Double percentage = Double.parseDouble(averageAcuuracy);
			Integer accuracyPercentage = percentage.intValue();
			if(directory.contains("google")) {
				googleAccuracy = accuracyPercentage;
			}
			else if(directory.contains("facebook") || directory.equalsIgnoreCase("facebook")) {
				facebookAccuray = accuracyPercentage;
			}
			else if(directory.contains("bing")) {
				 bingAccuracy = accuracyPercentage;
			}
			else if(directory.contains("yahoo") || directory.equalsIgnoreCase("foursquare")) {
				yahooAccuracy = accuracyPercentage;
			}
			else if(directory.contains("yellowpages")) {
				yellowPagesAccuracy = accuracyPercentage;
			}
		}
		
		Integer averageAccuracy =  (googleAccuracy + facebookAccuray + yellowPagesAccuracy + bingAccuracy + yahooAccuracy)/5;
		model.addAttribute("averageAccuracy", averageAccuracy);
		model.addAttribute("googleAccuracy", googleAccuracy);
		model.addAttribute("facebookAccuray", facebookAccuray);
		model.addAttribute("yellowPagesAccuracy", yellowPagesAccuracy);
		model.addAttribute("bingAccuracy", bingAccuracy);
		model.addAttribute("yahooAccuracy", yahooAccuracy);
		
		
		
		String[] startandEnd = getPastDates(1);
		Date start = getFormattedDate(startandEnd[0]);
		Date end= getFormattedDate(startandEnd[1]);
		

		SweetIQCoverageDTO sweetIQPerfOld = sweetIQService.getListingCoverage(
				clientId, start, end, store);
		Integer coverageOld = sweetIQPerfOld.getCoverage();
		
		int increment = coverage - coverageOld;

		
		
		SweetIQCoverageDTO sweetIQCoverageOld = sweetIQService
				.getDirectoryPerformance(start, end, clientId, "");

		model.addAttribute("googleDiff", sweetIQCoverage.getGoogleCoverage()-sweetIQCoverageOld.getGoogleCoverage());
		model.addAttribute("facebookDiff", sweetIQCoverage.getFacebookCoverage()-sweetIQCoverageOld.getFacebookCoverage());
		model.addAttribute("bingDiff", sweetIQCoverage.getBingCoverage()-sweetIQCoverageOld.getFacebookCoverage());
		model.addAttribute("yellowpagesDiff",
				sweetIQCoverage.getYellowPagesCoverage()-sweetIQCoverageOld.getYellowPagesCoverage());
		model.addAttribute("yahooDiff",
				sweetIQCoverage.getFourSquareCoverage()-sweetIQCoverageOld.getFourSquareCoverage());

		
		
		List<SweetIQAccuracyDTO> accuracyOld = sweetIQService.getAccuarcy(
				clientId, start, end, store);
		
		Integer googleAccuracyOld = 0;
		Integer facebookAccurayOld = 0;
		Integer yellowPagesAccuracyOld = 0;
		Integer bingAccuracyOld = 0;
		Integer yahooAccuracyOld = 0;
		
		
		for (SweetIQAccuracyDTO sweetIQAccuracyDTO : accuracyOld) {
			String directory = sweetIQAccuracyDTO.getDirectory();
			String averageAcuuracy = sweetIQAccuracyDTO.getAverage_Match();
			Double percentage = Double.parseDouble(averageAcuuracy);
			Integer accuracyPercentage = percentage.intValue();
			if(directory.contains("google")) {
				googleAccuracyOld = accuracyPercentage;
			}
			else if(directory.contains("facebook") || directory.equalsIgnoreCase("facebook")) {
				facebookAccurayOld = accuracyPercentage;
			}
			else if(directory.contains("bing")) {
				bingAccuracyOld = accuracyPercentage;
			}
			else if(directory.contains("yahoo") || directory.equalsIgnoreCase("foursquare")) {
				yahooAccuracyOld = accuracyPercentage;
			}
			else if(directory.contains("yellowpages")) {
				yellowPagesAccuracyOld = accuracyPercentage;
			}
		}
		
		model.addAttribute("googleAccuracyDiff", googleAccuracy-googleAccuracyOld);
		model.addAttribute("facebookAccurayDiff", facebookAccuray-facebookAccurayOld);
		model.addAttribute("yellowPagesAccuracyDiff", yellowPagesAccuracy-yellowPagesAccuracyOld);
		model.addAttribute("bingAccuracyDiff", bingAccuracy-bingAccuracyOld);
		model.addAttribute("yahooAccuracyDiff", yahooAccuracy-yahooAccuracyOld);
		
		int d = Integer.parseInt(duration);
		
		model.addAttribute("increment", increment);
		model.addAttribute("months", d);
		model.addAttribute("brand", brand);

		model.addAttribute("duration", duration);

		return "brand-perf";

	}

	@RequestMapping(value = "/ranking.htm", method = RequestMethod.GET)
	public String rankingReport(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brand = request.getParameter("b");
		String store = request.getParameter("s");
		String duration = request.getParameter("d");

		String engine = request.getParameter("e");
		String type = request.getParameter("t");
		if (engine == null || engine == "") {
			engine = "google";
		}
		if (type == null || type == "") {
			type = "local";
		}

		BrandInfoDTO brandInfoByBrand = service.getBrandInfoByBrand(brand);
		List<String> storesList = new ArrayList<String>();

		Map<Long, String> lblStoreMapByCleintId = listingService
				.getLBLStoreMapByCleintId(brandInfoByBrand.getClientId());
		Set<Long> storeSet = lblStoreMapByCleintId.keySet();
		for (Long lblStoreId : storeSet) {
			storesList.add(lblStoreMapByCleintId.get(lblStoreId));
		}
		java.util.Collections.sort(storesList);

		model.addAttribute("storeList", storesList);

		String[] startandEndDates = getPastDates(0);
		
		Date startDate = getFormattedDate(startandEndDates[0]);
		Date endDate = getFormattedDate(startandEndDates[1]);

		RankDTO rankDTO = sweetIQService.getRanking(
				brandInfoByBrand.getClientId(), store, engine, type, startDate,
				endDate);



		List<TrendDTO> trends = rankDTO.getTrends();

		List<String> cTrends = new ArrayList<String>();
		List<String> rTrends = new ArrayList<String>();

		for (TrendDTO trendDTO : trends) {
			Date date = trendDTO.getDate();

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			cTrends.add("{x:new Date(" + cal.get(Calendar.YEAR) + ","
					+ cal.get(Calendar.MONTH) + "," + 1 + "),  y: "
					+ trendDTO.getCoverage() + " }");

			rTrends.add("{x:new Date(" + cal.get(Calendar.YEAR) + ","
					+ cal.get(Calendar.MONTH) + "," + 1 + "),  y: "
					+ trendDTO.getRank() + " }");
		}
		if (trends.size() == 0) {
			cTrends.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");
			rTrends.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");
		}

		model.addAttribute("rTrends", rTrends);
		model.addAttribute("cTrends", cTrends);

		model.addAttribute("rank", rankDTO);
		model.addAttribute("keywordList", rankDTO.getKeywords());
		model.addAttribute("brand", brand);
		model.addAttribute("store", store);
		model.addAttribute("duration", duration);
		model.addAttribute("coverage", rankDTO.getCoverage());

		model.addAttribute("engine", engine);
		model.addAttribute("type", type);
		return "ranking";

	}

	@RequestMapping(value = "/coverage.htm", method = RequestMethod.GET)
	public String coverage(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brand = request.getParameter("b");
		String store = request.getParameter("s");
		String duration = request.getParameter("d");

		BrandInfoDTO brandInfoByBrand = service.getBrandInfoByBrand(brand);
		List<String> storesList = new ArrayList<String>();

		Integer clientId = brandInfoByBrand.getClientId();
		Map<Long, String> lblStoreMapByCleintId = listingService
				.getLBLStoreMapByCleintId(clientId);
		Set<Long> storeSet = lblStoreMapByCleintId.keySet();
		for (Long lblStoreId : storeSet) {
			storesList.add(lblStoreMapByCleintId.get(lblStoreId));
		}

		String[] startandEndDates = getPastDates(0);

		Date startDate = getFormattedDate(startandEndDates[0]);
		Date endDate = getFormattedDate(startandEndDates[1]);

		SweetIQCoverageDTO sweetIQCoverage = sweetIQService.getListingCoverage(
				clientId, startDate, endDate, store);

		java.util.Collections.sort(storesList);

		model.addAttribute("storeList", storesList);

		Integer coverage = sweetIQCoverage.getCoverage();
		model.addAttribute("coverage", coverage);
		Integer totalListingCount = sweetIQCoverage.getTotalListingCount();
		model.addAttribute("totalListings", totalListingCount);
		Integer possibleListings = sweetIQCoverage.getPossibleListings();
		model.addAttribute("possibleListings", possibleListings);

		model.addAttribute("brand", brand);

		model.addAttribute("duration", duration);

		return "coverage";

	}

	@RequestMapping(value = "/accuracy.htm", method = RequestMethod.GET)
	public String accuracyReport(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brand = request.getParameter("b");
		String store = request.getParameter("s");
		String duration = request.getParameter("d");

		BrandInfoDTO brandInfoByBrand = service.getBrandInfoByBrand(brand);

		List<String> storesList = new ArrayList<String>();

		Integer clientId = brandInfoByBrand.getClientId();
		Map<Long, String> lblStoreMapByCleintId = listingService
				.getLBLStoreMapByCleintId(clientId);
		Set<Long> storeSet = lblStoreMapByCleintId.keySet();
		for (Long lblStoreId : storeSet) {
			storesList.add(lblStoreMapByCleintId.get(lblStoreId));
		}

		String[] startandEndDates = getPastDates(0);
		
		Date startDate = getFormattedDate(startandEndDates[0]);
		Date endDate = getFormattedDate(startandEndDates[1]);

		List<SweetIQAccuracyDTO> accuracy = sweetIQService.getAccuarcy(
				clientId, startDate, endDate, store);

		java.util.Collections.sort(storesList);

		model.addAttribute("storeList", storesList);
		model.addAttribute("accuracyList", accuracy);
		model.addAttribute("store", store);
		model.addAttribute("brand", brand);
		model.addAttribute("duration", duration);

		return "accuracy";

	}

	@RequestMapping(value = "/runGMBLocation.htm", method = RequestMethod.GET)
	public String runGMBLocation(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brand = request.getParameter("brand");
		String store = request.getParameter("store");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		BrandInfoDTO brandInfo = service.getBrandInfoByBrandName(brand);

		Integer brandId = brandInfo.getClientId();

		logger.info("StoreName name in reports page ::" + store);
		logger.info("clientName in reports page ::" + brand);
		logger.info("startDate page ::" + start);
		logger.info("endDate in  page ::" + end);
		Date startDate = null;
		Date endDate = null;
		if (start.length() > 0 || end.length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);

		}
		Calendar cal = Calendar.getInstance();

		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		endDate = cal.getTime();

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(startDate);

		logger.info("startDate ::" + startDate);
		logger.info("endDate::" + endDate);

		String contextPath = request.getServerName() + request.getContextPath();

		String url = contextPath + "/pdfGMBLocation.htm?data=" + brand + "-"
				+ store + "-" + start + "-" + end;

		model.addAttribute("url", url);

		List<String> views = new ArrayList<String>();
		List<String> maps = new ArrayList<String>();
		List<String> websites = new ArrayList<String>();
		List<String> directions = new ArrayList<String>();
		List<String> calls = new ArrayList<String>();
		List<String> anlytics = new ArrayList<String>();
		Long count = service.getInsightCountsForBrand(brandId, store,
				startDate, endDate);

		// List<String> storesList = service.getStoresNames(brand);

		BrandInfoDTO brandInfoByBrand = service.getBrandInfoByBrand(brand);
		List<String> storesList = new ArrayList<String>();

		Map<Long, String> lblStoreMapByCleintId = listingService
				.getLBLStoreMapByCleintId(brandInfoByBrand.getClientId());
		Set<Long> storeSet = lblStoreMapByCleintId.keySet();
		for (Long lblStoreId : storeSet) {
			storesList.add(lblStoreMapByCleintId.get(lblStoreId));
		}

		java.util.Collections.sort(storesList);

		logger.info("storesList ::" + storesList.size());

		if (count != null && count == 0) {
			model.addAttribute("noDataMessage", true);

			model.addAttribute("days", 0);
			model.addAttribute("minYear", 0);
			model.addAttribute("minMonth", 0);
			model.addAttribute("minDate", 0);
			model.addAttribute("maxYear", 0);
			model.addAttribute("maxMonth", 0);
			model.addAttribute("maxDate", 0);

			model.addAttribute("yActions", 0);
			model.addAttribute("yViews", 0);

			views.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");
			maps.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");

			websites.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			calls.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");

			directions.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			anlytics.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			model.addAttribute("viewsHistory", views);
			model.addAttribute("actionsHistory", maps);

			model.addAttribute("websitesHistory", websites);
			model.addAttribute("directionsHistory", directions);
			model.addAttribute("callsHistory", calls);
			model.addAttribute("anlytics", anlytics);
			model.addAttribute("brand", brand);
			model.addAttribute("store", store);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

			model.addAttribute("direct", 0);
			model.addAttribute("discovery", 0);

			model.addAttribute("search", 0);
			model.addAttribute("maps", 0);
			model.addAttribute("views", 0);

			model.addAttribute("website", 0);
			model.addAttribute("directions", 0);
			model.addAttribute("calls", 0);
			model.addAttribute("actions", 0);
			java.util.Collections.sort(storesList);

			int index = storesList.indexOf(store);
			storesList.remove(index);
			storesList.add(0, store);
			model.addAttribute("storeList", storesList);

		} else {
			model.addAttribute("noDataMessage", false);
			Map<String, Long> insightsDataMap = service
					.getInsightsDataForBrandAndStore(brandId, store, startDate,
							endDate);
			// List<String> allStates = service.getAllStates();

			Long directCount = 0L;
			Long discoveryCount = 0L;

			Long searchCount = 0L;
			Long mapsCount = 0L;
			Long viewsCount = 0L;

			Long websiteCount = 0L;
			Long phoneCount = 0L;
			Long directionsCount = 0L;
			Long actionsCount = 0L;

			if (insightsDataMap != null && insightsDataMap.size() > 0) {
				directCount = insightsDataMap.get("QUERIES_DIRECT");
				discoveryCount = insightsDataMap.get("QUERIES_INDIRECT");

				searchCount = insightsDataMap.get("VIEWS_SEARCH");
				mapsCount = insightsDataMap.get("VIEWS_MAPS");
				viewsCount = searchCount + mapsCount;

				websiteCount = insightsDataMap.get("ACTIONS_WEBSITE");
				phoneCount = insightsDataMap.get("ACTIONS_PHONE");
				directionsCount = insightsDataMap
						.get("ACTIONS_DRIVING_DIRECTIONS");
				actionsCount = websiteCount + phoneCount + directionsCount;
			}

			List<String[]> listofWeeks = getListofDates(startDate, endDate);

			Map<String, InsightsGraphDTO> weeklyInsights = new HashMap<String, InsightsGraphDTO>();
			List<String> keys = new ArrayList<String>();

			Map<String, InsightsGraphDTO> dailyMap = service
					.getHistoryForStore(brandId, store, startDate, endDate);

			Set<String> keySet = dailyMap.keySet();

			for (String key : keySet) {
				InsightsGraphDTO insightsGraphDTO = dailyMap.get(key);
				weeklyInsights.put(key, insightsGraphDTO);
				keys.add(key);
			}

			Long maxSerchCounts = 0L;
			Long maxCallCounts = 0L;

			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				InsightsGraphDTO dto = weeklyInsights.get(key);
				Long mapCounts = dto.getMapCount();
				Long searchCounts = dto.getSearchCount();
				Long websiteCounts = dto.getWebsiteCount();
				Long directionsCounts = dto.getDirectionsCount();
				Long callsCounts = dto.getCallsCount();

				if (searchCounts > maxSerchCounts)
					maxSerchCounts = searchCounts;
				if (callsCounts > maxCallCounts)
					maxCallCounts = callsCounts;

				String[] date = key.split("-");
				Integer year = Integer.parseInt(date[0]);
				Integer month = Integer.parseInt(date[1]);
				Integer day = Integer.parseInt(date[2]);

				views.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + searchCounts + " }");
				maps.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + mapCounts + " }");
				websites.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: " + websiteCounts + " }");
				directions.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: " + directionsCounts + " }");
				calls.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + callsCounts + " }");

			}

			List<BingReportDTO> anlyticsData = service.getAnlyticsForStore(
					brand, store, startDate, endDate);

			for (BingReportDTO bingAnalyticsDTO : anlyticsData) {

				String[] date = bingAnalyticsDTO.getDate().split("-");
				Integer year = Integer.parseInt(date[0]);
				Integer month = Integer.parseInt(date[1]);
				Integer day = Integer.parseInt(date[2]);

				anlytics.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: "
						+ bingAnalyticsDTO.getImpressionCount() + " }");
			}
			if (anlyticsData != null && anlyticsData.size() == 0) {
				anlytics.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
						+ "),  y: " + 0 + " }");
			}

			model.addAttribute("anlytics", anlytics);

			model.addAttribute("days", listofWeeks.size());
			model.addAttribute("minYear", calStart.get(Calendar.YEAR));
			model.addAttribute("minMonth", calStart.get(Calendar.MONTH));
			model.addAttribute("minDate", calStart.get(Calendar.DAY_OF_MONTH));
			model.addAttribute("maxYear", cal.get(Calendar.YEAR));
			model.addAttribute("maxMonth", cal.get(Calendar.MONTH));
			model.addAttribute("maxDate", cal.get(Calendar.DAY_OF_MONTH));

			model.addAttribute("yActions", Math.round(maxCallCounts / 2));
			model.addAttribute("yViews", Math.round(maxSerchCounts / 2));

			model.addAttribute("brand", brand);
			model.addAttribute("store", store);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

			model.addAttribute("viewsHistory", views);
			model.addAttribute("actionsHistory", maps);

			model.addAttribute("websitesHistory", websites);
			model.addAttribute("directionsHistory", directions);
			model.addAttribute("callsHistory", calls);

			model.addAttribute("direct", directCount);
			model.addAttribute("discovery", discoveryCount);

			model.addAttribute("search", searchCount);
			model.addAttribute("maps", mapsCount);
			model.addAttribute("views", viewsCount);

			model.addAttribute("website", websiteCount);
			model.addAttribute("directions", directionsCount);
			model.addAttribute("calls", phoneCount);
			model.addAttribute("actions", actionsCount);
			java.util.Collections.sort(storesList);

			int index = storesList.indexOf(store);
			storesList.remove(index);
			storesList.add(0, store);
			model.addAttribute("storeList", storesList);
		}

		return "reports-gmblocation";

	}

	@RequestMapping(value = "/filterGMBLocation.htm", method = RequestMethod.GET)
	public String filterGMBLocation(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brand = request.getParameter("brand");
		String store = request.getParameter("store");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		logger.info("StoreName name in reports page ::" + store);
		logger.info("clientName in reports page ::" + brand);
		logger.info("startDate page ::" + start);
		logger.info("endDate in  page ::" + end);
		Date startDate = null;
		Date endDate = null;
		if (start.length() > 0 || end.length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);

		}

		BrandInfoDTO brandInfo = service.getBrandInfoByBrandName(brand);

		Integer brandId = brandInfo.getClientId();

		Calendar cal = Calendar.getInstance();

		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		endDate = cal.getTime();
		logger.info("startDate ::" + startDate);
		logger.info("endDate::" + endDate);

		String contextPath = request.getServerName() + request.getContextPath();

		String url = contextPath + "/pdfGMBLocation.htm?data=" + brand + "-"
				+ store + "-" + start + "-" + end;

		List<String> views = new ArrayList<String>();
		List<String> maps = new ArrayList<String>();
		List<String> websites = new ArrayList<String>();
		List<String> directions = new ArrayList<String>();
		List<String> calls = new ArrayList<String>();
		List<String> anlytics = new ArrayList<String>();
		Long count = service.getInsightCountsForBrand(brandId, "", startDate,
				endDate);
		List<String> storesList = service.getStoresNames(brand);
		java.util.Collections.sort(storesList);
		logger.info("storesList ::" + storesList.size());

		if (count != null && count == 0) {
			model.addAttribute("noDataMessage", true);

			model.addAttribute("days", 0);
			model.addAttribute("minYear", 0);
			model.addAttribute("minMonth", 0);
			model.addAttribute("minDate", 0);
			model.addAttribute("maxYear", 0);
			model.addAttribute("maxMonth", 0);
			model.addAttribute("maxDate", 0);

			model.addAttribute("yActions", 0);
			model.addAttribute("yViews", 0);

			views.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");
			maps.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");

			websites.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			calls.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");

			directions.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			anlytics.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			model.addAttribute("anlytics", anlytics);
			model.addAttribute("viewsHistory", views);
			model.addAttribute("actionsHistory", maps);

			model.addAttribute("websitesHistory", websites);
			model.addAttribute("directionsHistory", directions);
			model.addAttribute("callsHistory", calls);

			model.addAttribute("brand", brand);
			model.addAttribute("store", store);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

			model.addAttribute("direct", 0);
			model.addAttribute("discovery", 0);

			model.addAttribute("search", 0);
			model.addAttribute("maps", 0);
			model.addAttribute("views", 0);

			model.addAttribute("website", 0);
			model.addAttribute("directions", 0);
			model.addAttribute("calls", 0);
			model.addAttribute("actions", 0);

		} else {
			model.addAttribute("noDataMessage", false);
			Map<String, Long> insightsDataMap = service
					.getInsightsDataForBrandAndStore(brandId, store, startDate,
							endDate);
			// List<String> allStates = service.getAllStates();

			Long directCount = 0L;
			Long discoveryCount = 0L;

			Long searchCount = 0L;
			Long mapsCount = 0L;
			Long viewsCount = 0L;

			Long websiteCount = 0L;
			Long phoneCount = 0L;
			Long directionsCount = 0L;
			Long actionsCount = 0L;

			if (insightsDataMap != null && insightsDataMap.size() > 0) {
				directCount = insightsDataMap.get("QUERIES_DIRECT");
				discoveryCount = insightsDataMap.get("QUERIES_INDIRECT");

				searchCount = insightsDataMap.get("VIEWS_SEARCH");
				mapsCount = insightsDataMap.get("VIEWS_MAPS");
				viewsCount = searchCount + mapsCount;

				websiteCount = insightsDataMap.get("ACTIONS_WEBSITE");
				phoneCount = insightsDataMap.get("ACTIONS_PHONE");
				directionsCount = insightsDataMap
						.get("ACTIONS_DRIVING_DIRECTIONS");
				actionsCount = websiteCount + phoneCount + directionsCount;
			}

			List<String[]> listofWeeks = getListofDates(startDate, endDate);

			Map<String, InsightsGraphDTO> weeklyInsights = new HashMap<String, InsightsGraphDTO>();
			List<String> keys = new ArrayList<String>();

			Map<String, InsightsGraphDTO> dailyMap = service
					.getHistoryForStore(brandId, store, startDate, endDate);

			Set<String> keySet = dailyMap.keySet();

			for (String key : keySet) {
				InsightsGraphDTO insightsGraphDTO = dailyMap.get(key);
				weeklyInsights.put(key, insightsGraphDTO);
				keys.add(key);
			}

			Long maxSerchCounts = 0L;
			Long maxCallCounts = 0L;

			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				InsightsGraphDTO dto = weeklyInsights.get(key);
				Long mapCounts = dto.getMapCount();
				Long searchCounts = dto.getSearchCount();
				Long websiteCounts = dto.getWebsiteCount();
				Long directionsCounts = dto.getDirectionsCount();
				Long callsCounts = dto.getCallsCount();

				if (searchCounts > maxSerchCounts)
					maxSerchCounts = searchCounts;
				if (callsCounts > maxCallCounts)
					maxCallCounts = callsCounts;
				String[] date = key.split("-");
				// System.out.println("dates for graph is: "+ date.toString());
				Integer year = Integer.parseInt(date[0]);
				Integer month = Integer.parseInt(date[1]);
				Integer day = Integer.parseInt(date[2]);

				views.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + searchCounts + " }");
				maps.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + mapCounts + " }");
				websites.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: " + websiteCounts + " }");
				directions.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: " + directionsCounts + " }");
				calls.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + callsCounts + " }");
			}
			Calendar calStart = Calendar.getInstance();

			List<BingReportDTO> anlyticsData = service.getAnlyticsForStore(
					brand, store, startDate, endDate);

			for (BingReportDTO bingAnalyticsDTO : anlyticsData) {

				String[] date = bingAnalyticsDTO.getDate().split("-");
				Integer year = Integer.parseInt(date[0]);
				Integer month = Integer.parseInt(date[1]);
				Integer day = Integer.parseInt(date[2]);

				anlytics.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: "
						+ bingAnalyticsDTO.getImpressionCount() + " }");
			}
			if (anlyticsData != null && anlyticsData.size() == 0) {
				anlytics.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
						+ "),  y: " + 0 + " }");
			}

			model.addAttribute("anlytics", anlytics);

			calStart.setTime(startDate);
			model.addAttribute("days", listofWeeks.size());
			model.addAttribute("minYear", calStart.get(Calendar.YEAR));
			model.addAttribute("minMonth", calStart.get(Calendar.MONTH));
			model.addAttribute("minDate", calStart.get(Calendar.DAY_OF_MONTH));
			model.addAttribute("maxYear", cal.get(Calendar.YEAR));
			model.addAttribute("maxMonth", cal.get(Calendar.MONTH));
			model.addAttribute("maxDate", cal.get(Calendar.DAY_OF_MONTH));

			model.addAttribute("yActions", Math.round(maxCallCounts / 2));
			model.addAttribute("yViews", Math.round(maxSerchCounts / 2));

			model.addAttribute("brand", brand);
			model.addAttribute("store", store);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

			model.addAttribute("viewsHistory", views);
			model.addAttribute("actionsHistory", maps);

			model.addAttribute("websitesHistory", websites);
			model.addAttribute("directionsHistory", directions);
			model.addAttribute("callsHistory", calls);

			model.addAttribute("direct", directCount);
			model.addAttribute("discovery", discoveryCount);

			model.addAttribute("search", searchCount);
			model.addAttribute("maps", mapsCount);
			model.addAttribute("views", viewsCount);

			model.addAttribute("website", websiteCount);
			model.addAttribute("directions", directionsCount);
			model.addAttribute("calls", phoneCount);
			model.addAttribute("actions", actionsCount);

			java.util.Collections.sort(storesList);
			int index = storesList.indexOf(store);
			storesList.remove(index);
			storesList.add(0, store);

			model.addAttribute("storeList", storesList);

		}
		return "reports-gmblocation";
	}

	@RequestMapping(value = "/pdfGMBLocation.htm", method = RequestMethod.GET)
	public String pdfGMBLocation(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String data = request.getParameter("data");
		String[] dataArray = data.split("-");

		String brand = "";
		String store = "";
		String start = "";
		String end = "";

		if (dataArray.length == 4) {
			brand = dataArray[0];
			store = dataArray[1];
			start = dataArray[2];
			end = dataArray[3];

		}

		logger.info("StoreName name in reports page ::" + store);
		logger.info("clientName in reports page ::" + brand);
		logger.info("startDate page ::" + start);
		logger.info("endDate in  page ::" + end);
		Date startDate = null;
		Date endDate = null;
		if (start.length() > 0 || end.length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);

		}

		Calendar cal = Calendar.getInstance();

		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		endDate = cal.getTime();
		logger.info("startDate ::" + startDate);
		logger.info("endDate::" + endDate);

		BrandInfoDTO brandInfo = service.getBrandInfoByBrandName(brand);

		Integer brandId = brandInfo.getClientId();

		List<String> views = new ArrayList<String>();
		List<String> maps = new ArrayList<String>();
		List<String> websites = new ArrayList<String>();
		List<String> directions = new ArrayList<String>();
		List<String> calls = new ArrayList<String>();
		Long count = service.getInsightCountsForBrand(brandId, store,
				startDate, endDate);

		if (count != null && count == 0) {
			model.addAttribute("noDataMessage", true);

			model.addAttribute("days", 0);
			model.addAttribute("minYear", 0);
			model.addAttribute("minMonth", 0);
			model.addAttribute("minDate", 0);
			model.addAttribute("maxYear", 0);
			model.addAttribute("maxMonth", 0);
			model.addAttribute("maxDate", 0);

			model.addAttribute("yActions", 0);
			model.addAttribute("yViews", 0);
			model.addAttribute("ySearches", 0);

			views.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");
			maps.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");

			websites.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			calls.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");

			directions.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			model.addAttribute("viewsHistory", views);
			model.addAttribute("actionsHistory", maps);

			model.addAttribute("websitesHistory", websites);
			model.addAttribute("directionsHistory", directions);
			model.addAttribute("callsHistory", calls);

			model.addAttribute("brand", brand);
			model.addAttribute("store", store);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

			model.addAttribute("direct", 0);
			model.addAttribute("discovery", 0);

			model.addAttribute("search", 0);
			model.addAttribute("maps", 0);
			model.addAttribute("views", 0);

			model.addAttribute("website", 0);
			model.addAttribute("directions", 0);
			model.addAttribute("calls", 0);
			model.addAttribute("actions", 0);

		} else {
			model.addAttribute("noDataMessage", false);
			Map<String, Long> insightsDataMap = service
					.getInsightsDataForBrandAndStore(brandId, store, startDate,
							endDate);
			// List<String> allStates = service.getAllStates();

			List<String> storesList = service.getStoresNames(brand);
			java.util.Collections.sort(storesList);
			logger.info("storesList ::" + storesList.size());

			Long directCount = 0L;
			Long discoveryCount = 0L;

			Long searchCount = 0L;
			Long mapsCount = 0L;
			Long viewsCount = 0L;

			Long websiteCount = 0L;
			Long phoneCount = 0L;
			Long directionsCount = 0L;
			Long actionsCount = 0L;

			if (insightsDataMap != null && insightsDataMap.size() > 0) {
				directCount = insightsDataMap.get("QUERIES_DIRECT");
				discoveryCount = insightsDataMap.get("QUERIES_INDIRECT");

				searchCount = insightsDataMap.get("VIEWS_SEARCH");
				mapsCount = insightsDataMap.get("VIEWS_MAPS");
				viewsCount = searchCount + mapsCount;

				websiteCount = insightsDataMap.get("ACTIONS_WEBSITE");
				phoneCount = insightsDataMap.get("ACTIONS_PHONE");
				directionsCount = insightsDataMap
						.get("ACTIONS_DRIVING_DIRECTIONS");
				actionsCount = websiteCount + phoneCount + directionsCount;
			}

			List<String[]> listofWeeks = getListofDates(startDate, endDate);

			Map<String, InsightsGraphDTO> weeklyInsights = new HashMap<String, InsightsGraphDTO>();
			List<String> keys = new ArrayList<String>();

			Map<String, InsightsGraphDTO> dailyMap = service
					.getHistoryForStore(brandId, store, startDate, endDate);

			Set<String> keySet = dailyMap.keySet();

			for (String key : keySet) {
				InsightsGraphDTO insightsGraphDTO = dailyMap.get(key);
				weeklyInsights.put(key, insightsGraphDTO);
				keys.add(key);
			}

			Long maxSerchCounts = 0L;
			Long maxCallCounts = 0L;

			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				InsightsGraphDTO dto = weeklyInsights.get(key);
				Long mapCounts = dto.getMapCount();
				Long searchCounts = dto.getSearchCount();
				Long websiteCounts = dto.getWebsiteCount();
				Long directionsCounts = dto.getDirectionsCount();
				Long callsCounts = dto.getCallsCount();

				if (searchCounts > maxSerchCounts)
					maxSerchCounts = searchCounts;
				if (callsCounts > maxCallCounts)
					maxCallCounts = callsCounts;
				String[] date = key.split("-");
				// System.out.println("dates for graph is: "+ date.toString());
				Integer year = Integer.parseInt(date[0]);
				Integer month = Integer.parseInt(date[1]);
				Integer day = Integer.parseInt(date[2]);

				views.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + searchCounts + " }");
				maps.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + mapCounts + " }");
				websites.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: " + websiteCounts + " }");
				directions.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: " + directionsCounts + " }");
				calls.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + callsCounts + " }");
			}
			Calendar calStart = Calendar.getInstance();

			calStart.setTime(startDate);
			model.addAttribute("days", listofWeeks.size());
			model.addAttribute("minYear", calStart.get(Calendar.YEAR));
			model.addAttribute("minMonth", calStart.get(Calendar.MONTH));
			model.addAttribute("minDate", calStart.get(Calendar.DAY_OF_MONTH));
			model.addAttribute("maxYear", cal.get(Calendar.YEAR));
			model.addAttribute("maxMonth", cal.get(Calendar.MONTH));
			model.addAttribute("maxDate", cal.get(Calendar.DAY_OF_MONTH));

			model.addAttribute("yActions", Math.round(maxCallCounts / 2));
			model.addAttribute("yViews", Math.round(maxSerchCounts / 2));
			model.addAttribute("ySearches", Math.round(discoveryCount / 2));

			model.addAttribute("brand", brand);
			model.addAttribute("store", store);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

			model.addAttribute("viewsHistory", views);
			model.addAttribute("actionsHistory", maps);

			model.addAttribute("websitesHistory", websites);
			model.addAttribute("directionsHistory", directions);
			model.addAttribute("callsHistory", calls);

			model.addAttribute("direct", directCount);
			model.addAttribute("discovery", discoveryCount);

			model.addAttribute("search", searchCount);
			model.addAttribute("maps", mapsCount);
			model.addAttribute("views", viewsCount);

			model.addAttribute("website", websiteCount);
			model.addAttribute("directions", directionsCount);
			model.addAttribute("calls", phoneCount);
			model.addAttribute("actions", actionsCount);

			java.util.Collections.sort(storesList);
			int index = storesList.indexOf(store);
			storesList.remove(index);
			storesList.add(0, store);

			model.addAttribute("storeList", storesList);

		}

		return "pdf-gmblocation";
	}

	@RequestMapping(value = "/runExcelReport.htm", method = RequestMethod.GET)
	public String runExcelReport(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brand = request.getParameter("brand");
		String state = request.getParameter("state");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		if (state != null && state.equalsIgnoreCase("all")) {
			state = "";
		}

		logger.info("state ::" + state);
		logger.info("brand in Controller ::" + brand);

		Date startDate = null;
		Date endDate = null;
		if (start.length() > 0 || end.length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
		}
		Calendar cal = Calendar.getInstance();

		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		endDate = cal.getTime();
		logger.info("startDate ::" + startDate);
		logger.info("endDate ::" + endDate);

		List<InsightsGraphDTO> insightsExcelData = service
				.getInsightsBrandExcelData(brand, state, startDate, endDate);
		model.addAttribute("startDate", start);
		model.addAttribute("endDate", end);
		model.addAttribute("brand", brand);
		model.addAttribute("insightsExcelData", insightsExcelData);
		model.addAttribute("reportName", "GmbInsights-" + brand);

		return "gmbbrandexcel";
	}

	@RequestMapping(value = "/runGMBMonthlyReport.htm", method = RequestMethod.GET)
	public String runGMBMonthlyReport(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brand = request.getParameter("brand");
		String type = request.getParameter("type");

		Calendar cal = Calendar.getInstance();

		Date endDate = cal.getTime();
		cal.add(Calendar.HOUR_OF_DAY, 23);

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(endDate);
		startCal.add(Calendar.MONTH, -11);
		Date startDate = startCal.getTime();

		logger.info("startDate ::" + startDate);
		logger.info("endDate ::" + endDate);

		List<InsightsGraphDTO> insightsExcelData = service
				.getMonthlyReportData(brand, type);

		Map<String, List<InsightsGraphDTO>> excelData = new HashMap<String, List<InsightsGraphDTO>>();

		if (type.equalsIgnoreCase("location")) {
			for (InsightsGraphDTO insightsGraphDTO : insightsExcelData) {

				String store = insightsGraphDTO.getStore();

				if (excelData.get(store) != null) {
					List<InsightsGraphDTO> existingStoreData = excelData
							.get(store);
					existingStoreData.add(insightsGraphDTO);
					excelData.put(store, existingStoreData);
				} else {
					List<InsightsGraphDTO> storeData = new ArrayList<InsightsGraphDTO>();
					storeData.add(insightsGraphDTO);
					excelData.put(store, storeData);
				}
			}
		} else {
			for (InsightsGraphDTO insightsGraphDTO : insightsExcelData) {

				String store = insightsGraphDTO.getState();

				if (excelData.get(store) != null) {
					List<InsightsGraphDTO> existingStoreData = excelData
							.get(store);
					existingStoreData.add(insightsGraphDTO);
					excelData.put(store, existingStoreData);
				} else {
					List<InsightsGraphDTO> storeData = new ArrayList<InsightsGraphDTO>();
					storeData.add(insightsGraphDTO);
					excelData.put(store, storeData);
				}
			}
		}

		List<MonthlyReportDTO> monthlyData = new ArrayList<MonthlyReportDTO>();

		List<String> lastTwelveMonths = getLastTwelveMonths();

		SortedSet<String> storeorStates = new TreeSet<String>(
				excelData.keySet());
		// Set<String> storeorStates = excelData.keySet();
		for (String key : storeorStates) {
			MonthlyReportDTO dto = new MonthlyReportDTO();
			if (type.equalsIgnoreCase("location")) {
				dto.setStore(key);
			} else {
				dto.setState(key);
			}
			List<InsightsGraphDTO> list = excelData.get(key);

			for (InsightsGraphDTO insightsGraphDTO : list) {

				String[] monthAndName = getMonthName(
						insightsGraphDTO.getDate(), lastTwelveMonths);
				int index = Integer.valueOf(monthAndName[0]);
				String monthName = monthAndName[1];
				Long totalSearchCount = insightsGraphDTO.getTotalSearchCount();
				Long totalViewCount = insightsGraphDTO.getTotalViewCount();
				Long totalActionCount = insightsGraphDTO.getTotalActionCount();
				if (index == 1) {
					dto.setMonth1(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
				if (index == 2) {
					dto.setMonth2(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
				if (index == 3) {
					dto.setMonth3(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
				if (index == 4) {
					dto.setMonth4(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
				if (index == 5) {
					dto.setMonth5(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
				if (index == 6) {
					dto.setMonth6(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
				if (index == 7) {
					dto.setMonth7(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
				if (index == 8) {
					dto.setMonth8(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
				if (index == 9) {
					dto.setMonth9(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
				if (index == 10) {
					dto.setMonth10(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
				if (index == 11) {
					dto.setMonth11(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
				if (index == 12) {
					dto.setMonth12(monthName + "|" + totalSearchCount + "|"
							+ totalViewCount + "|" + totalActionCount);
				}
			}
			monthlyData.add(dto);
		}
		model.addAttribute("excelData", monthlyData);
		model.addAttribute("months", lastTwelveMonths);
		model.addAttribute("reportType", type);

		model.addAttribute("reportName", "GMB-MonthlyResultsBy" + type);

		return "gmbmonthlyexcel";
	}


	private static String getMonthYear(Date date) {
		SimpleDateFormat monthDate = new SimpleDateFormat("MMM-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String month_year = monthDate.format(cal.getTime());
		return month_year;
	}

	private static String[] getMonthName(Date date,
			List<String> lastTwelveMonths) {

		String[] indexAndName = new String[2];

		SimpleDateFormat monthDate = new SimpleDateFormat("MMM-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// cal.add(Calendar.MONTH, -1);
		String month_name = monthDate.format(cal.getTime());

		int monthIndex = lastTwelveMonths.indexOf(month_name);

		// System.out.println((12- monthIndex) + "==" + month_name);

		indexAndName[0] = String.valueOf(12 - monthIndex);
		indexAndName[1] = month_name;
		// System.out.println(indexAndName[0] + "==" + indexAndName[1] );

		return indexAndName;

	}

	// getLastYearMonths();
	// getCurrentYearMonths();

	static List<String> months = Arrays.asList("Jan", "Feb", "Mar", "Apr",
			"May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

	private static Map<String, Long> getLastYearMonths() {
		Map<String, Long> allDates = new LinkedHashMap<String, Long>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");

		Calendar prevYear = Calendar.getInstance();
		prevYear.add(Calendar.YEAR, -1);

		for (int i = 0; i < months.size(); i++) {
			String year = dateFormat.format(prevYear.getTime());
			allDates.put(months.get(i) + "-" + year, 0L);
		}
		// System.out.println(allDates);
		return allDates;
	}

	private static Map<String, Long> getCurrentYearMonths() {
		Map<String, Long> allDates = new LinkedHashMap<String, Long>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");

		Calendar prevYear = Calendar.getInstance();

		for (int i = 0; i < months.size(); i++) {
			String year = dateFormat.format(prevYear.getTime());
			allDates.put(months.get(i) + "-" + year, 0L);
		}
		// System.out.println(allDates);
		return allDates;
	}

	private static List<String> getLastTwelveMonths() {
		List<String> allDates = new ArrayList<String>();

		SimpleDateFormat monthDate = new SimpleDateFormat("MMM-yyyy");
		Calendar cal = Calendar.getInstance();

		for (int i = 1; i <= 12; i++) {
			String month_name1 = monthDate.format(cal.getTime());
			// System.out.println(month_name1);
			allDates.add(month_name1);
			cal.add(Calendar.MONTH, -1);
		}
		// System.out.println(allDates);
		return allDates;
	}

	@RequestMapping(value = "/insights.htm", method = RequestMethod.GET)
	public String insights(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brand = request.getParameter("brand");
		String state = request.getParameter("state");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		Date startDate = null;
		Date endDate = null;
		if (start.length() > 0 || end.length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
		}
		Calendar cal = Calendar.getInstance();

		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		endDate = cal.getTime();

		//BrandInfoDTO brandInfo = service.getBrandInfoByBrandName(brand);

		Integer brandId = Integer.parseInt(brand);
		
		System.out.println("brandId: "+ brandId);

	/*	Map<String, Long> insightsDataMap = service.getInsightsBrandData(
				brandId, state, startDate, endDate);
		
		System.out.println("insightsDataMap: "+ insightsDataMap.size());
		*/
		

		Map<String, InsightsGraphDTO> dailyMap = service.getHistory(brandId,
				state, startDate, endDate);
		
		InsightsGraphDTO totals = dailyMap.get("TotalCounts");


		Long directCount = 0L;
		Long discoveryCount = 0L;

		Long searchCount = 0L;
		Long mapsCount = 0L;
		Long viewsCount = 0L;

		Long websiteCount = 0L;
		Long phoneCount = 0L;
		Long directionsCount = 0L;
		Long actionsCount = 0L;

		if (totals != null) {
			directCount = totals.getDirectCount();
			discoveryCount = totals.getInDirectCount();

			searchCount = totals.getSearchCount();
			mapsCount = totals.getMapCount();

			websiteCount = totals.getWebsiteCount();
			phoneCount = totals.getCallsCount();
			directionsCount = totals.getDirectionsCount();

		}
		viewsCount = searchCount + mapsCount;
		actionsCount = websiteCount + phoneCount + directionsCount;

		List<String[]> listofWeeks = getListofDates(startDate, endDate);

		Map<String, InsightsGraphDTO> weeklyInsights = new HashMap<String, InsightsGraphDTO>();
		List<String> keys = new ArrayList<String>();

		Set<String> keySet = dailyMap.keySet();

		for (String key : keySet) {
			InsightsGraphDTO insightsGraphDTO = dailyMap.get(key);
			weeklyInsights.put(key, insightsGraphDTO);
			if(!"TotalCounts".equalsIgnoreCase(key))
				keys.add(key);
		}

		Long maxSerchCounts = 0L;
		Long maxCallCounts = 0L;

		List<String> views = new ArrayList<String>();
		List<String> maps = new ArrayList<String>();
		List<String> websites = new ArrayList<String>();
		List<String> directions = new ArrayList<String>();
		List<String> calls = new ArrayList<String>();

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			InsightsGraphDTO dto = weeklyInsights.get(key);
			Long mapCounts = dto.getMapCount();
			Long searchCounts = dto.getSearchCount();
			Long websiteCounts = dto.getWebsiteCount();
			Long directionsCounts = dto.getDirectionsCount();
			Long callsCounts = dto.getCallsCount();

			if (searchCounts > maxSerchCounts)
				maxSerchCounts = searchCounts;
			if (callsCounts > maxCallCounts)
				maxCallCounts = callsCounts;

			String[] date = key.split("-");
			Integer year = Integer.parseInt(date[0]);
			Integer month = Integer.parseInt(date[1]);
			Integer day = Integer.parseInt(date[2]);

			views.add("{x:new Date(" + year + "," + (month - 1) + "," + day
					+ "),  y: " + searchCounts + " }");
			maps.add("{x:new Date(" + year + "," + (month - 1) + "," + day
					+ "),  y: " + mapCounts + " }");
			websites.add("{x:new Date(" + year + "," + (month - 1) + "," + day
					+ "),  y: " + websiteCounts + " }");
			directions.add("{x:new Date(" + year + "," + (month - 1) + ","
					+ day + "),  y: " + directionsCounts + " }");
			calls.add("{x:new Date(" + year + "," + (month - 1) + "," + day
					+ "),  y: " + callsCounts + " }");

		}
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(startDate);
		model.addAttribute("days", listofWeeks.size());
		model.addAttribute("minYear", calStart.get(Calendar.YEAR));
		model.addAttribute("minMonth", calStart.get(Calendar.MONTH));
		model.addAttribute("minDate", calStart.get(Calendar.DAY_OF_MONTH));
		model.addAttribute("maxYear", cal.get(Calendar.YEAR));
		model.addAttribute("maxMonth", cal.get(Calendar.MONTH));
		model.addAttribute("maxDate", cal.get(Calendar.DAY_OF_MONTH));

		model.addAttribute("yActions", Math.round(maxCallCounts / 2));
		model.addAttribute("yViews", Math.round(maxSerchCounts / 2));
		model.addAttribute("ySearches", Math.round(discoveryCount / 2));

		model.addAttribute("searchHistory", views);
		model.addAttribute("mapsHistory", maps);

		model.addAttribute("websitesHistory", websites);
		model.addAttribute("directionsHistory", directions);
		model.addAttribute("callsHistory", calls);

		model.addAttribute("direct", directCount);
		model.addAttribute("discovery", discoveryCount);

		model.addAttribute("search", searchCount);
		model.addAttribute("maps", mapsCount);
		model.addAttribute("views", viewsCount);

		model.addAttribute("website", websiteCount);
		model.addAttribute("directions", directionsCount);
		model.addAttribute("calls", phoneCount);
		model.addAttribute("actions", actionsCount);

		model.addAttribute("viewCount", viewsCount);
		model.addAttribute("actionCount", actionsCount);
		model.addAttribute("webCount", websiteCount);
		model.addAttribute("dirCount", directionsCount);
		model.addAttribute("callCount", phoneCount);

		System.out.println(startDate + "==" + endDate);
		model.addAttribute("mainPage", false);
		model.addAttribute("brand", brand);
		model.addAttribute("state", state);
		model.addAttribute("start", getFormattedDate(startDate));
		model.addAttribute("end", getFormattedDate(endDate));

		return "insights";
	}

	@RequestMapping(value = "/topBottom.htm", method = RequestMethod.GET)
	public String topBottom(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brand = request.getParameter("brand");
		String state = request.getParameter("state");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		Date startDate = null;
		Date endDate = null;
		if (start.length() > 0 || end.length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
		}
		Calendar cal = Calendar.getInstance();

		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		endDate = cal.getTime();

		//BrandInfoDTO brandInfo = service.getBrandInfoByBrandName(brand);

		//Integer brandId = brandInfo.getClientId();
		Integer brandId = Integer.parseInt(brand);
		Map<String, List<InsightsGraphDTO>> topBottomSearches = service
				.getTopandBottomSearches(brandId, state, startDate, endDate);

		List<InsightsGraphDTO> topSearchList = topBottomSearches.get("tSearch");
		List<InsightsGraphDTO> bottomSearchList = topBottomSearches
				.get("bSearch");
		List<InsightsGraphDTO> topViewList = topBottomSearches.get("tView");
		List<InsightsGraphDTO> bottomViewList = topBottomSearches.get("bView");
		List<InsightsGraphDTO> topActionList = topBottomSearches.get("tAction");
		List<InsightsGraphDTO> bottomActionList = topBottomSearches
				.get("bAction");

		List<GoogleInsightsReportDTO> topTenSearches = new ArrayList<GoogleInsightsReportDTO>();
		List<GoogleInsightsReportDTO> bottomTenSearches = new ArrayList<GoogleInsightsReportDTO>();
		List<GoogleInsightsReportDTO> topTenViews = new ArrayList<GoogleInsightsReportDTO>();
		List<GoogleInsightsReportDTO> bottomTenViews = new ArrayList<GoogleInsightsReportDTO>();
		List<GoogleInsightsReportDTO> topTenActions = new ArrayList<GoogleInsightsReportDTO>();
		List<GoogleInsightsReportDTO> bottomTenActions = new ArrayList<GoogleInsightsReportDTO>();

		for (InsightsGraphDTO graphDTO : topSearchList) {
			GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			dto.setStore(graphDTO.getStore());
			dto.setState(graphDTO.getState());
			dto.setCity(graphDTO.getCity());
			dto.setTopDirectCount(graphDTO.getDirectCount());
			dto.setTopDiscoveryCount(graphDTO.getInDirectCount());
			dto.setTotalTopSearchCount(graphDTO.getTotalSearchCount());
			topTenSearches.add(dto);
		}

		for (InsightsGraphDTO graphDTO : bottomSearchList) {
			GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			dto.setStore(graphDTO.getStore());
			dto.setState(graphDTO.getState());
			dto.setCity(graphDTO.getCity());
			dto.setBottomDirectCount(graphDTO.getDirectCount());
			dto.setBottomDiscoveryCount(graphDTO.getInDirectCount());
			dto.setTotalBottomSearchCount(graphDTO.getTotalSearchCount());
			bottomTenSearches.add(dto);
		}

		for (InsightsGraphDTO graphDTO : topViewList) {
			GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			dto.setStore(graphDTO.getStore());
			dto.setState(graphDTO.getState());
			dto.setCity(graphDTO.getCity());
			dto.setTopViewSearchCount(graphDTO.getSearchCount());
			dto.setTopViewMapsCount(graphDTO.getMapCount());
			dto.setTotalTopViewsCount(graphDTO.getTotalViewCount());
			topTenViews.add(dto);
		}

		for (InsightsGraphDTO graphDTO : bottomViewList) {
			GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			dto.setStore(graphDTO.getStore());
			dto.setState(graphDTO.getState());
			dto.setCity(graphDTO.getCity());
			dto.setBottomViewSearchCount(graphDTO.getSearchCount());
			dto.setBottomViewMapsCount(graphDTO.getMapCount());
			dto.setTotalBottomViewsCount(graphDTO.getTotalViewCount());
			bottomTenViews.add(dto);
		}

		for (InsightsGraphDTO graphDTO : topActionList) {
			GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			dto.setStore(graphDTO.getStore());
			dto.setState(graphDTO.getState());
			dto.setCity(graphDTO.getCity());
			dto.setTopWebsiteCount(graphDTO.getWebsiteCount());
			dto.setTopDirectionsCount(graphDTO.getDirectionsCount());
			dto.setTopCallsCount(graphDTO.getCallsCount());
			dto.setTotalTopActionsCount(graphDTO.getTotalActionCount());
			topTenActions.add(dto);
		}

		for (InsightsGraphDTO graphDTO : bottomActionList) {
			GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			dto.setStore(graphDTO.getStore());
			dto.setState(graphDTO.getState());
			dto.setCity(graphDTO.getCity());
			dto.setBottomWebsiteCount(graphDTO.getWebsiteCount());
			dto.setBottomDirectionsCount(graphDTO.getDirectionsCount());
			dto.setBottomCallsCount(graphDTO.getCallsCount());
			dto.setTotalBottomActionsCount(graphDTO.getTotalActionCount());
			bottomTenActions.add(dto);
		}

		model.addAttribute("topTenSearches", topTenSearches);
		model.addAttribute("bottomTenSearches", bottomTenSearches);
		model.addAttribute("topTenViews", topTenViews);
		model.addAttribute("bottomTenViews", bottomTenViews);
		model.addAttribute("topTenActions", topTenActions);
		model.addAttribute("bottomTenActions", bottomTenActions);

		model.addAttribute("brand", brand);
		model.addAttribute("mainPage", false);
		model.addAttribute("state", state);
		model.addAttribute("start", getFormattedDate(startDate));
		model.addAttribute("end", getFormattedDate(endDate));

		return "topBottom";
	}

	@RequestMapping(value = "/bing.htm", method = RequestMethod.GET)
	public String bing(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brand = request.getParameter("brand");
		String state = request.getParameter("state");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		Date startDate = null;
		Date endDate = null;
		if (start.length() > 0 || end.length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
		}
		Calendar cal = Calendar.getInstance();

		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		endDate = cal.getTime();

		BrandInfoDTO brandInfo = service.getBrandInfoByBrandName(brand);

		Integer brandId = brandInfo.getClientId();

		List<String> anlytics = new ArrayList<String>();

		Long count = service.getInsightCountsForBrand(brandId, "", startDate,
				endDate);
		if (count != null && count == 0) {
			model.addAttribute("noDataMessage", true);

			anlytics.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");
		}

		else {
			List<BingReportDTO> anlyticsData = service.getAnlytics(brandId,
					state, startDate, endDate);

			for (BingReportDTO bingAnalyticsDTO : anlyticsData) {

				String[] date = bingAnalyticsDTO.getDate().split("-");
				Integer year = Integer.parseInt(date[0]);
				Integer month = Integer.parseInt(date[1]);
				Integer day = Integer.parseInt(date[2]);

				anlytics.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: "
						+ bingAnalyticsDTO.getImpressionCount() + " }");
			}
			if (anlyticsData != null && anlyticsData.size() == 0) {
				anlytics.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
						+ "),  y: " + 0 + " }");
			}

			Map<String, List<BingAnalyticsDTO>> topAnalytics = service
					.getTopandBottomAnalytics(brandId, state, startDate,
							endDate);

			List<BingAnalyticsDTO> tTenAnalytics = topAnalytics
					.get("topAnalytics");
			List<BingAnalyticsDTO> bTenAnalytics = topAnalytics
					.get("bottomAnalytics");

			model.addAttribute("topAnalytics", tTenAnalytics);
			model.addAttribute("bAnalytics", bTenAnalytics);
			model.addAttribute("anlytics", anlytics);

		}
		List<String[]> listofWeeks = getListofDates(startDate, endDate);

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(startDate);
		model.addAttribute("days", listofWeeks.size());
		model.addAttribute("minYear", calStart.get(Calendar.YEAR));
		model.addAttribute("minMonth", calStart.get(Calendar.MONTH));
		model.addAttribute("minDate", calStart.get(Calendar.DAY_OF_MONTH));
		model.addAttribute("maxYear", cal.get(Calendar.YEAR));
		model.addAttribute("maxMonth", cal.get(Calendar.MONTH));
		model.addAttribute("maxDate", cal.get(Calendar.DAY_OF_MONTH));

		model.addAttribute("mainPage", false);
		model.addAttribute("brand", brand);
		model.addAttribute("state", state);
		model.addAttribute("start", getFormattedDate(startDate));
		model.addAttribute("end", getFormattedDate(endDate));

		return "bing";
	}

	@RequestMapping(value = "/runGMBBrand.htm", method = RequestMethod.GET)
	public String runGMBBrand(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		long startTimeInMillis = Calendar.getInstance().getTimeInMillis();

		System.out.println(startTimeInMillis);

		String brand = request.getParameter("brand");
		String state = request.getParameter("state");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		model.addAttribute("mainPage", true);
		if (state == null) {
			state = "";
		}
		if (state != null && state.equalsIgnoreCase("all")) {
			state = "";
		}

		logger.info("state ::" + state);
		logger.info("brand in Controller ::" + brand);

		Date startDate = null;
		Date endDate = null;
		if (start.length() > 0 || end.length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
		}
		Calendar cal = Calendar.getInstance();

		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		endDate = cal.getTime();

		BrandInfoDTO brandInfo = service.getBrandInfoByBrandName(brand);

		Integer brandId = brandInfo.getClientId();

		String contextPath = request.getServerName() + request.getContextPath();

		String url = "";
		if (state.length() > 0) {
			url = contextPath + "/pdfGMBBrand.htm?data=" + brand + "-" + state
					+ "-" + start + "-" + end;
		} else {
			url = contextPath + "/pdfGMBBrand.htm?data=" + brand + "-" + start
					+ "-" + end;
		}
		model.addAttribute("url", url);

		String insightsURL = "insights.htm?state=" + state + "&&brand=" + brandId
				+ "&&start=" + start + "&&end=" + end;
		String topBottomURL = "topBottom.htm?state=" + state + "&&brand="
				+ brandId + "&&start=" + start + "&&end=" + end;


		model.addAttribute("url", url);
		model.addAttribute("insightsURL", insightsURL);
		model.addAttribute("topBottomURL", topBottomURL);

		String startYY = getYYYYMMDD(startDate);
		String endYY = getYYYYMMDD(endDate);

		model.addAttribute("reportName", brand + "-GMB-" + startYY + "-"
				+ endYY);

		List<String> views = new ArrayList<String>();
		List<String> maps = new ArrayList<String>();
		List<String> websites = new ArrayList<String>();
		List<String> directions = new ArrayList<String>();
		List<String> calls = new ArrayList<String>();

		List<String> anlytics = new ArrayList<String>();

		List<String> dailyViewTrends = new ArrayList<String>();
		List<String> dailyActionTrends = new ArrayList<String>();
		List<String> dailyWebsiteTrends = new ArrayList<String>();
		List<String> dailyCallsTrends = new ArrayList<String>();
		List<String> dailyDrivingTrends = new ArrayList<String>();

		Long count = 1L;/* service.getInsightCountsForBrand(brandId, "", startDate,
				endDate);*/
		if (count != null && count == 0) {
			model.addAttribute("noDataMessage", true);

			model.addAttribute("days", 0);
			model.addAttribute("minYear", 0);
			model.addAttribute("minMonth", 0);
			model.addAttribute("minDate", 0);
			model.addAttribute("maxYear", 0);
			model.addAttribute("maxMonth", 0);
			model.addAttribute("maxDate", 0);

			model.addAttribute("yActions", 0);
			model.addAttribute("yViews", 0);
			model.addAttribute("ySearches", 0);

			views.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");
			maps.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");

			websites.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			calls.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");

			anlytics.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			directions.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			String colorCode = "Blue";
			String color = "'" + colorCode + "'";

			String day = "SU";
			int cnt = 0;

			dailyViewTrends.add("{label: " + day + "," + "y:" + cnt + ","
					+ "color:" + color + "}");

			dailyActionTrends.add("{label: " + day + "," + "y:" + cnt + ","
					+ "color:" + color + "}");

			dailyWebsiteTrends.add("{label: " + day + "," + "y:" + cnt + ","
					+ "color:" + color + "}");

			dailyCallsTrends.add("{label: " + day + "," + "y:" + cnt + ","
					+ "color:" + color + "}");

			dailyDrivingTrends.add("{label: " + day + "," + "y:" + cnt + ","
					+ "color:" + color + "}");

			model.addAttribute("searchHistory", views);
			model.addAttribute("mapsHistory", maps);

			model.addAttribute("websitesHistory", websites);
			model.addAttribute("directionsHistory", directions);
			model.addAttribute("callsHistory", calls);

			List<String> searchPreTrends = new ArrayList<String>();

			searchPreTrends.add("{label: Jan,y:0}");

			model.addAttribute("searchPreTrends", searchPreTrends);
			model.addAttribute("searchCurTrends", searchPreTrends);

			model.addAttribute("actionPreTrends", searchPreTrends);
			model.addAttribute("actionCurTrends", searchPreTrends);

			model.addAttribute("viewPreTrends", searchPreTrends);
			model.addAttribute("viewCurTrends", searchPreTrends);
			model.addAttribute("anlytics", anlytics);

			model.addAttribute("dailyViewTrends", dailyViewTrends);
			model.addAttribute("dailyActionTrends", dailyActionTrends);
			model.addAttribute("dailyWebsiteTrends", dailyWebsiteTrends);
			model.addAttribute("dailyCallsTrends", dailyCallsTrends);
			model.addAttribute("dailyDrivingTrends", dailyDrivingTrends);

			model.addAttribute("viewCount", 0);
			model.addAttribute("actionCount", 0);
			model.addAttribute("webCount", 0);
			model.addAttribute("dirCount", 0);
			model.addAttribute("callCount", 0);

			model.addAttribute("brand", brand);
			model.addAttribute("state", state);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

		} else {
			model.addAttribute("noDataMessage", false);

			List<String> allStates = service.getAllStatesListByBrand(brand);

			/*
			 * Map<String, Long> insightsDataMap = service.getInsightsBrandData(
			 * brandId, state, startDate, endDate);
			 * 
			 * java.util.Collections.sort(allStates);
			 * 
			 * Long directCount = 0L; Long discoveryCount = 0L;
			 * 
			 * Long searchCount = 0L; Long mapsCount = 0L; Long viewsCount = 0L;
			 * 
			 * Long websiteCount = 0L; Long phoneCount = 0L; Long
			 * directionsCount = 0L; Long actionsCount = 0L;
			 * 
			 * logger.info("States List ::" + allStates.size());
			 * 
			 * if (insightsDataMap != null && insightsDataMap.size() > 0) {
			 * directCount = insightsDataMap.get("QUERIES_DIRECT");
			 * discoveryCount = insightsDataMap.get("QUERIES_INDIRECT");
			 * 
			 * searchCount = insightsDataMap.get("VIEWS_SEARCH"); mapsCount =
			 * insightsDataMap.get("VIEWS_MAPS");
			 * 
			 * websiteCount = insightsDataMap.get("ACTIONS_WEBSITE"); phoneCount
			 * = insightsDataMap.get("ACTIONS_PHONE"); directionsCount =
			 * insightsDataMap .get("ACTIONS_DRIVING_DIRECTIONS");
			 * 
			 * } viewsCount = searchCount + mapsCount; actionsCount =
			 * websiteCount + phoneCount + directionsCount;
			 * 
			 * List<String[]> listofWeeks = getListofDates(startDate, endDate);
			 * 
			 * Map<String, InsightsGraphDTO> weeklyInsights = new
			 * HashMap<String, InsightsGraphDTO>(); List<String> keys = new
			 * ArrayList<String>();
			 * 
			 * Map<String, InsightsGraphDTO> dailyMap =
			 * service.getHistory(brandId, state, startDate, endDate);
			 * 
			 * Set<String> keySet = dailyMap.keySet();
			 * 
			 * for (String key : keySet) { InsightsGraphDTO insightsGraphDTO =
			 * dailyMap.get(key); weeklyInsights.put(key, insightsGraphDTO);
			 * keys.add(key); }
			 * 
			 * Long maxSerchCounts = 0L; Long maxCallCounts = 0L;
			 * 
			 * for (int i = 0; i < keys.size(); i++) { String key = keys.get(i);
			 * InsightsGraphDTO dto = weeklyInsights.get(key); Long mapCounts =
			 * dto.getMapCount(); Long searchCounts = dto.getSearchCount(); Long
			 * websiteCounts = dto.getWebsiteCount(); Long directionsCounts =
			 * dto.getDirectionsCount(); Long callsCounts = dto.getCallsCount();
			 * 
			 * if (searchCounts > maxSerchCounts) maxSerchCounts = searchCounts;
			 * if (callsCounts > maxCallCounts) maxCallCounts = callsCounts;
			 * 
			 * String[] date = key.split("-"); Integer year =
			 * Integer.parseInt(date[0]); Integer month =
			 * Integer.parseInt(date[1]); Integer day =
			 * Integer.parseInt(date[2]);
			 * 
			 * views.add("{x:new Date(" + year + "," + (month - 1) + "," + day +
			 * "),  y: " + searchCounts + " }"); maps.add("{x:new Date(" + year
			 * + "," + (month - 1) + "," + day + "),  y: " + mapCounts + " }");
			 * websites.add("{x:new Date(" + year + "," + (month - 1) + "," +
			 * day + "),  y: " + websiteCounts + " }");
			 * directions.add("{x:new Date(" + year + "," + (month - 1) + "," +
			 * day + "),  y: " + directionsCounts + " }");
			 * calls.add("{x:new Date(" + year + "," + (month - 1) + "," + day +
			 * "),  y: " + callsCounts + " }");
			 * 
			 * }
			 */

			System.out.println("start:" + new Date());
			
			List<InsightsGraphDTO> insightstrendData = service
					.getMonthlyTrends(brandId, state);
			
			System.out.println("end:" + new Date());
			

			List<String> searchPreTrends = new ArrayList<String>();
			List<String> searchCurTrends = new ArrayList<String>();

			List<String> viewPreTrends = new ArrayList<String>();
			List<String> viewCurTrends = new ArrayList<String>();

			List<String> actionPreTrends = new ArrayList<String>();
			List<String> actionCurTrends = new ArrayList<String>();

			// Action Trends

			Map<String, Long> lastYearActonMonths = getLastYearMonths();
			Map<String, Long> currentYearActionMonths = getCurrentYearMonths();

			for (InsightsGraphDTO insightsGraphDTO : insightstrendData) {

				String monthYear = getMonthYear(insightsGraphDTO.getDate());
				if (lastYearActonMonths.containsKey(monthYear)) {
					lastYearActonMonths.put(monthYear,
							insightsGraphDTO.getTotalActionCount());
				}
				if (currentYearActionMonths.containsKey(monthYear)) {
					currentYearActionMonths.put(monthYear,
							insightsGraphDTO.getTotalActionCount());
				}
			}
			Set<String> lastYearSet = lastYearActonMonths.keySet();
			for (String key : lastYearSet) {
				Long value = lastYearActonMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				actionPreTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			Set<String> lcurrentYearSet = currentYearActionMonths.keySet();
			for (String key : lcurrentYearSet) {
				Long value = currentYearActionMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				actionCurTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			// View Trends
			Map<String, Long> lastYearViewMonths = getLastYearMonths();
			Map<String, Long> currentYearViewMonths = getCurrentYearMonths();

			for (InsightsGraphDTO insightsGraphDTO : insightstrendData) {

				String monthYear = getMonthYear(insightsGraphDTO.getDate());
				if (lastYearViewMonths.containsKey(monthYear)) {
					lastYearViewMonths.put(monthYear,
							insightsGraphDTO.getTotalViewCount());
				}
				if (currentYearViewMonths.containsKey(monthYear)) {
					currentYearViewMonths.put(monthYear,
							insightsGraphDTO.getTotalViewCount());
				}
			}
			Set<String> lastYearViewSet = lastYearViewMonths.keySet();
			for (String key : lastYearViewSet) {
				Long value = lastYearViewMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				viewPreTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			Set<String> currentYearViewSet = currentYearViewMonths.keySet();
			for (String key : currentYearViewSet) {
				Long value = currentYearViewMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				viewCurTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			// search Trends
			Map<String, Long> lastYearMonths = getLastYearMonths();
			Map<String, Long> currentYearMonths = getCurrentYearMonths();

			for (InsightsGraphDTO insightsGraphDTO : insightstrendData) {

				String monthYear = getMonthYear(insightsGraphDTO.getDate());
				if (lastYearMonths.containsKey(monthYear)) {
					lastYearMonths.put(monthYear,
							insightsGraphDTO.getTotalSearchCount());
				}
				if (currentYearMonths.containsKey(monthYear)) {
					currentYearMonths.put(monthYear,
							insightsGraphDTO.getTotalSearchCount());
				}
			}
			Set<String> lastYearsSet = lastYearMonths.keySet();
			for (String key : lastYearsSet) {
				Long value = lastYearMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				searchPreTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			Set<String> lcurrentYearsSet = currentYearMonths.keySet();
			for (String key : lcurrentYearsSet) {
				Long value = currentYearMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				searchCurTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			/*
			 * // Daily trends Map<Integer, InsightsGraphDTO> dailyTrends =
			 * service .getDailyTrends(brand, state, startDate, endDate);
			 * 
			 * Set<Integer> keyPair = dailyTrends.keySet(); String[] days = {
			 * "SU", "MO", "TU", "WE", "TH", "FR", "SA" };
			 * 
			 * Map<String, Integer> maxKey = getMaxKey(dailyTrends);
			 * 
			 * Long viewCount = 0L; Long actionCount = 0L; Long webCount = 0L;
			 * Long dirCount = 0L; Long callCount = 0L;
			 * 
			 * String colorCode = "#00BFFF"; String color = "'" + colorCode +
			 * "'"; String colorG = "GREEN"; String greenColor = "'" + colorG +
			 * "'";
			 * 
			 * for (Integer integer : keyPair) { InsightsGraphDTO
			 * insightsGraphDTO = dailyTrends.get(integer); String day = "'" +
			 * days[integer - 1] + "'"; Long totalViewCount =
			 * insightsGraphDTO.getTotalViewCount(); Long totalActionCount =
			 * insightsGraphDTO.getTotalActionCount(); Long websiteCount2 =
			 * insightsGraphDTO.getWebsiteCount(); Long directionsCount2 =
			 * insightsGraphDTO.getDirectionsCount(); Long callsCount =
			 * insightsGraphDTO.getCallsCount();
			 * 
			 * viewCount = viewCount + totalViewCount; actionCount = actionCount
			 * + totalActionCount; webCount = webCount + websiteCount2; dirCount
			 * = dirCount + directionsCount2; callCount = callCount +
			 * callsCount;
			 * 
			 * Integer viewMax = maxKey.get("view"); Integer actionMax =
			 * maxKey.get("action"); Integer websiteMax = maxKey.get("website");
			 * Integer callsMax = maxKey.get("calls"); Integer directionsMax =
			 * maxKey.get("directions");
			 * 
			 * if (integer == viewMax) { dailyViewTrends.add("{label: " + day +
			 * "," + "y:" + totalViewCount + "," + "color:" + greenColor + "}");
			 * } else { dailyViewTrends.add("{label: " + day + "," + "y:" +
			 * totalViewCount + "," + "color:" + color + "}"); } if (integer ==
			 * actionMax) { dailyActionTrends.add("{label: " + day + "," + "y:"
			 * + totalActionCount + "," + "color:" + greenColor + "}"); } else {
			 * dailyActionTrends.add("{label: " + day + "," + "y:" +
			 * totalActionCount + "," + "color:" + color + "}"); } if (integer
			 * == websiteMax) {
			 * 
			 * dailyWebsiteTrends .add("{label: " + day + "," + "y:" +
			 * websiteCount2 + "," + "color:" + greenColor + "}"); } else {
			 * dailyWebsiteTrends.add("{label: " + day + "," + "y:" +
			 * websiteCount2 + "," + "color:" + color + "}"); } if (integer ==
			 * callsMax) { dailyCallsTrends.add("{label: " + day + "," + "y:" +
			 * callsCount + "," + "color:" + greenColor + "}"); } else {
			 * dailyCallsTrends.add("{label: " + day + "," + "y:" + callsCount +
			 * "," + "color:" + color + "}"); } if (integer == directionsMax) {
			 * dailyDrivingTrends.add("{label: " + day + "," + "y:" +
			 * directionsCount2 + "," + "color:" + greenColor + "}"); } else {
			 * dailyDrivingTrends.add("{label: " + day + "," + "y:" +
			 * directionsCount2 + "," + "color:" + color + "}"); } }
			 */

			model.addAttribute("dailyViewTrends", dailyViewTrends);
			model.addAttribute("dailyActionTrends", dailyActionTrends);
			model.addAttribute("dailyWebsiteTrends", dailyWebsiteTrends);
			model.addAttribute("dailyCallsTrends", dailyCallsTrends);
			model.addAttribute("dailyDrivingTrends", dailyDrivingTrends);

			/*
			 * model.addAttribute("viewCount", viewsCount);
			 * model.addAttribute("actionCount", actionsCount);
			 * model.addAttribute("webCount", websiteCount);
			 * model.addAttribute("dirCount", directionsCount);
			 * model.addAttribute("callCount", phoneCount);
			 */

			// daily trends

			/*
			 * List<BingReportDTO> anlyticsData = service.getAnlytics(brandId,
			 * state, startDate, endDate);
			 * 
			 * for (BingReportDTO bingAnalyticsDTO : anlyticsData) {
			 * 
			 * String[] date = bingAnalyticsDTO.getDate().split("-"); Integer
			 * year = Integer.parseInt(date[0]); Integer month =
			 * Integer.parseInt(date[1]); Integer day =
			 * Integer.parseInt(date[2]);
			 * 
			 * anlytics.add("{x:new Date(" + year + "," + (month - 1) + "," +
			 * day + "),  y: " + bingAnalyticsDTO.getImpressionCount() + " }");
			 * } if (anlyticsData != null && anlyticsData.size() == 0) {
			 * anlytics.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 +
			 * "),  y: " + 0 + " }"); }
			 * 
			 * Map<String, List<InsightsGraphDTO>> topBottomSearches = service
			 * .getTopandBottomSearches(brandId, state, startDate, endDate);
			 * 
			 * List<InsightsGraphDTO> topSearchList = topBottomSearches
			 * .get("tSearch"); List<InsightsGraphDTO> bottomSearchList =
			 * topBottomSearches .get("bSearch"); List<InsightsGraphDTO>
			 * topViewList = topBottomSearches.get("tView");
			 * List<InsightsGraphDTO> bottomViewList = topBottomSearches
			 * .get("bView"); List<InsightsGraphDTO> topActionList =
			 * topBottomSearches .get("tAction"); List<InsightsGraphDTO>
			 * bottomActionList = topBottomSearches .get("bAction");
			 * 
			 * List<GoogleInsightsReportDTO> topTenSearches = new
			 * ArrayList<GoogleInsightsReportDTO>();
			 * List<GoogleInsightsReportDTO> bottomTenSearches = new
			 * ArrayList<GoogleInsightsReportDTO>();
			 * List<GoogleInsightsReportDTO> topTenViews = new
			 * ArrayList<GoogleInsightsReportDTO>();
			 * List<GoogleInsightsReportDTO> bottomTenViews = new
			 * ArrayList<GoogleInsightsReportDTO>();
			 * List<GoogleInsightsReportDTO> topTenActions = new
			 * ArrayList<GoogleInsightsReportDTO>();
			 * List<GoogleInsightsReportDTO> bottomTenActions = new
			 * ArrayList<GoogleInsightsReportDTO>();
			 * 
			 * for (InsightsGraphDTO graphDTO : topSearchList) {
			 * GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			 * dto.setStore(graphDTO.getStore());
			 * dto.setState(graphDTO.getState());
			 * dto.setCity(graphDTO.getCity());
			 * dto.setTopDirectCount(graphDTO.getDirectCount());
			 * dto.setTopDiscoveryCount(graphDTO.getInDirectCount());
			 * dto.setTotalTopSearchCount(graphDTO.getTotalSearchCount());
			 * topTenSearches.add(dto); }
			 * 
			 * for (InsightsGraphDTO graphDTO : bottomSearchList) {
			 * GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			 * dto.setStore(graphDTO.getStore());
			 * dto.setState(graphDTO.getState());
			 * dto.setCity(graphDTO.getCity());
			 * dto.setBottomDirectCount(graphDTO.getDirectCount());
			 * dto.setBottomDiscoveryCount(graphDTO.getInDirectCount());
			 * dto.setTotalBottomSearchCount(graphDTO.getTotalSearchCount());
			 * bottomTenSearches.add(dto); }
			 * 
			 * for (InsightsGraphDTO graphDTO : topViewList) {
			 * GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			 * dto.setStore(graphDTO.getStore());
			 * dto.setState(graphDTO.getState());
			 * dto.setCity(graphDTO.getCity());
			 * dto.setTopViewSearchCount(graphDTO.getSearchCount());
			 * dto.setTopViewMapsCount(graphDTO.getMapCount());
			 * dto.setTotalTopViewsCount(graphDTO.getTotalViewCount());
			 * topTenViews.add(dto); }
			 * 
			 * for (InsightsGraphDTO graphDTO : bottomViewList) {
			 * GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			 * dto.setStore(graphDTO.getStore());
			 * dto.setState(graphDTO.getState());
			 * dto.setCity(graphDTO.getCity());
			 * dto.setBottomViewSearchCount(graphDTO.getSearchCount());
			 * dto.setBottomViewMapsCount(graphDTO.getMapCount());
			 * dto.setTotalBottomViewsCount(graphDTO.getTotalViewCount());
			 * bottomTenViews.add(dto); }
			 * 
			 * for (InsightsGraphDTO graphDTO : topActionList) {
			 * GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			 * dto.setStore(graphDTO.getStore());
			 * dto.setState(graphDTO.getState());
			 * dto.setCity(graphDTO.getCity());
			 * dto.setTopWebsiteCount(graphDTO.getWebsiteCount());
			 * dto.setTopDirectionsCount(graphDTO.getDirectionsCount());
			 * dto.setTopCallsCount(graphDTO.getCallsCount());
			 * dto.setTotalTopActionsCount(graphDTO.getTotalActionCount());
			 * topTenActions.add(dto); }
			 * 
			 * for (InsightsGraphDTO graphDTO : bottomActionList) {
			 * GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
			 * dto.setStore(graphDTO.getStore());
			 * dto.setState(graphDTO.getState());
			 * dto.setCity(graphDTO.getCity());
			 * dto.setBottomWebsiteCount(graphDTO.getWebsiteCount());
			 * dto.setBottomDirectionsCount(graphDTO.getDirectionsCount());
			 * dto.setBottomCallsCount(graphDTO.getCallsCount());
			 * dto.setTotalBottomActionsCount(graphDTO.getTotalActionCount());
			 * bottomTenActions.add(dto); }
			 * 
			 * Map<String, List<BingAnalyticsDTO>> topAnalytics = service
			 * .getTopandBottomAnalytics(brandId, state, startDate, endDate);
			 * 
			 * List<BingAnalyticsDTO> tTenAnalytics = topAnalytics
			 * .get("topAnalytics"); List<BingAnalyticsDTO> bTenAnalytics =
			 * topAnalytics .get("bottomAnalytics");
			 * 
			 * model.addAttribute("topAnalytics", tTenAnalytics);
			 * model.addAttribute("bAnalytics", bTenAnalytics);
			 * 
			 * Calendar calStart = Calendar.getInstance();
			 * 
			 * calStart.setTime(startDate); model.addAttribute("days",
			 * listofWeeks.size()); model.addAttribute("minYear",
			 * calStart.get(Calendar.YEAR)); model.addAttribute("minMonth",
			 * calStart.get(Calendar.MONTH)); model.addAttribute("minDate",
			 * calStart.get(Calendar.DAY_OF_MONTH));
			 * model.addAttribute("maxYear", cal.get(Calendar.YEAR));
			 * model.addAttribute("maxMonth", cal.get(Calendar.MONTH));
			 * model.addAttribute("maxDate", cal.get(Calendar.DAY_OF_MONTH));
			 * 
			 * model.addAttribute("yActions", Math.round(maxCallCounts / 2));
			 * model.addAttribute("yViews", Math.round(maxSerchCounts / 2));
			 * model.addAttribute("ySearches", Math.round(discoveryCount / 2));
			 * 
			 * model.addAttribute("topTenSearches", topTenSearches);
			 * model.addAttribute("bottomTenSearches", bottomTenSearches);
			 * model.addAttribute("topTenViews", topTenViews);
			 * model.addAttribute("bottomTenViews", bottomTenViews);
			 * model.addAttribute("topTenActions", topTenActions);
			 * model.addAttribute("bottomTenActions", bottomTenActions);
			 */

			// for graph .........
			model.addAttribute("brand", brand);
			model.addAttribute("state", state);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

			model.addAttribute("sd", start);
			model.addAttribute("ed", end);

			/*
			 * model.addAttribute("searchHistory", views);
			 * model.addAttribute("mapsHistory", maps);
			 * 
			 * model.addAttribute("websitesHistory", websites);
			 * model.addAttribute("directionsHistory", directions);
			 * model.addAttribute("callsHistory", calls);
			 */

			model.addAttribute("searchPreTrends", searchPreTrends);
			model.addAttribute("searchCurTrends", searchCurTrends);

			model.addAttribute("actionPreTrends", actionPreTrends);
			model.addAttribute("actionCurTrends", actionCurTrends);

			model.addAttribute("viewPreTrends", viewPreTrends);
			model.addAttribute("viewCurTrends", viewCurTrends);

			//model.addAttribute("anlytics", anlytics);

			java.util.Collections.sort(allStates);

			model.addAttribute("statesList", allStates);

			/*
			 * model.addAttribute("direct", directCount);
			 * model.addAttribute("discovery", discoveryCount);
			 * 
			 * model.addAttribute("search", searchCount);
			 * model.addAttribute("maps", mapsCount);
			 * model.addAttribute("views", viewsCount);
			 * 
			 * model.addAttribute("website", websiteCount);
			 * model.addAttribute("directions", directionsCount);
			 * model.addAttribute("calls", phoneCount);
			 * model.addAttribute("actions", actionsCount);
			 */

			logger.info("allStates ::" + allStates.size());
		}

		long endTimeInMillis = Calendar.getInstance().getTimeInMillis();

		long seconds = (endTimeInMillis - startTimeInMillis) / 1000;

		return "gmb-brand";
	}

	Map<String, Integer> getMaxKey(Map<Integer, InsightsGraphDTO> dailyTrends) {

		HashMap<Integer, Long> viewMap = new HashMap<Integer, Long>();
		HashMap<Integer, Long> actionMap = new HashMap<Integer, Long>();
		HashMap<Integer, Long> websiteMap = new HashMap<Integer, Long>();
		HashMap<Integer, Long> callsMap = new HashMap<Integer, Long>();
		HashMap<Integer, Long> directionsMap = new HashMap<Integer, Long>();

		HashMap<String, Integer> indexMap = new HashMap<String, Integer>();

		Set<Integer> keyPair = dailyTrends.keySet();

		for (Integer key : keyPair) {
			InsightsGraphDTO insightsGraphDTO = dailyTrends.get(key);
			viewMap.put(key, insightsGraphDTO.getTotalViewCount());
			actionMap.put(key, insightsGraphDTO.getTotalActionCount());
			websiteMap.put(key, insightsGraphDTO.getWebsiteCount());
			callsMap.put(key, insightsGraphDTO.getCallsCount());
			directionsMap.put(key, insightsGraphDTO.getDirectionsCount());
		}

		Integer maxKeyView = getKey(viewMap);
		Integer maxKeyAction = getKey(actionMap);
		Integer maxKeyWebsite = getKey(websiteMap);
		Integer maxKeyCalls = getKey(callsMap);
		Integer maxKeyDirections = getKey(directionsMap);

		indexMap.put("view", maxKeyView);
		indexMap.put("action", maxKeyAction);
		indexMap.put("website", maxKeyWebsite);
		indexMap.put("calls", maxKeyCalls);
		indexMap.put("directions", maxKeyDirections);

		return indexMap;

	}

	Integer getKey(HashMap<Integer, Long> map) {
		Iterator<Integer> it = map.keySet().iterator();
		Integer viewMapKey = it.next();
		Long viewMapKeyMaxValue = map.get(viewMapKey);
		while (it.hasNext()) {
			Integer k = it.next();
			Long val = map.get(k);
			if (val > viewMapKeyMaxValue) {
				viewMapKeyMaxValue = val;
				viewMapKey = k;
			}
		}
		return viewMapKey;
	}

	@RequestMapping(value = "/generatePDF.htm", method = RequestMethod.GET)
	public void generatePDF(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("PDF is getting created");
		String apiURL = request.getParameter("url");
		String fileName = request.getParameter("report");
		String BaseURL = "http://api.html2pdfrocket.com/pdf";

		// PDFRocketAPI.createPDF(pathtoPDF, apiURL, fileName);

		String APIKey = "ff60aa04-d396-44bc-a262-e75b163071bc";
		String ExtraParams = "JavascriptDelay=3000&MarginBottom=15&MarginLeft=0.25&MarginRight=0.25&MarginTop=0.25&PageWidth=215.9&PageHeight=279.4&FooterUrl=http://23.23.203.174/lbl_pro/pdfheader.htm";
		String value = apiURL;

		URL url;
		String Params = "";
		String file = "";
		try {
			if (ExtraParams != null && !"".equals(ExtraParams)) {
				Params = ExtraParams;
				if (!Params.substring(0, 1).equals("&")) {
					Params = "&" + Params;
				}
			}

			value = URLEncoder.encode(value,
					java.nio.charset.StandardCharsets.UTF_8.toString());
			value += Params;

			// Validate parameters
			if (APIKey == null || "".equals(APIKey))
				throw (new Exception("API key is empty"));
			if (fileName == null || "".equals(fileName))
				throw (new Exception("Filename is empty"));

			// Append parameters for API call
			url = new URL(BaseURL + "?apikey=" + APIKey + "&value=" + value);

			// Download PDF file
			URLConnection connection = url.openConnection();
			InputStream Instream = connection.getInputStream();

			// Write PDF file
			BufferedInputStream BISin = new BufferedInputStream(Instream);
			file = pathtoPDF + File.separatorChar + fileName + ".pdf";
			File f = new File(file);
			FileOutputStream FOSfile = new FileOutputStream(f);
			BufferedOutputStream out = new BufferedOutputStream(FOSfile);

			int i;
			while ((i = BISin.read()) != -1) {
				out.write(i);
			}

			/*
			 * // Clean up out.flush(); out.close();
			 */
			System.out.println("File " + fileName + " created");
			logger.info("PDF " + fileName + "is getting created");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		File fileToD = new File("C:\\Code\\lbl_pro\\pdf.pdf");
		FileInputStream fileIn = new FileInputStream(fileToD);
		ServletOutputStream out = response.getOutputStream();

		byte[] outputByte = new byte[4096];
		// copy binary contect to output stream
		while (fileIn.read(outputByte, 0, 4096) != -1) {
			out.write(outputByte, 0, 4096);
		}
		fileIn.close();
		out.flush();
		out.close();

	}

	@RequestMapping(value = "/pdfGMBBrand.htm", method = RequestMethod.GET)
	public String pdfGMBBrand(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String data = request.getParameter("data");
		String[] dataArray = data.split("-");

		String brand = "";
		String state = "";
		String start = "";
		String end = "";

		if (dataArray.length == 3) {
			brand = dataArray[0];
			start = dataArray[1];
			end = dataArray[2];
			state = "";
		} else {
			brand = dataArray[0];
			state = dataArray[3];
			start = dataArray[2];
			end = dataArray[3];
		}

		logger.info("state ::" + state);
		logger.info("brand in Controller ::" + brand);

		Map<String, String> channelImageBytes = service
				.getChannelImageBytes(brand);

		BrandInfoDTO brandInfo = service.getBrandInfoByBrandName(brand);

		Integer brandId = brandInfo.getClientId();

		/*
		 * byte[] encoded = org.apache.commons.codec.binary.Base64
		 * .encodeBase64(channelImageBytes.get("cImage")); String encodedString
		 * = new String(encoded);
		 * 
		 * model.addAttribute("channelImageData", encodedString);
		 * 
		 * byte[] bEncoded = org.apache.commons.codec.binary.Base64
		 * .encodeBase64(channelImageBytes.get("bImage")); String encodedBrad =
		 * new String(bEncoded);
		 * 
		 * model.addAttribute("brandImageData", encodedBrad);
		 */
		model.addAttribute("brandImage", channelImageBytes.get("bImage"));
		model.addAttribute("channelImage", channelImageBytes.get("cImage"));

		Date startDate = null;
		Date endDate = null;
		if (start.length() > 0 || end.length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
		}
		Calendar cal = Calendar.getInstance();

		cal.setTime(endDate);
		cal.add(Calendar.HOUR_OF_DAY, 23);
		cal.add(Calendar.MINUTE, 59);
		endDate = cal.getTime();
		logger.info("startDate ::" + startDate);
		logger.info("endDate ::" + endDate);

		List<String> views = new ArrayList<String>();
		List<String> maps = new ArrayList<String>();
		List<String> websites = new ArrayList<String>();
		List<String> directions = new ArrayList<String>();
		List<String> calls = new ArrayList<String>();
		List<String> anlytics = new ArrayList<String>();

		List<String> dailyViewTrends = new ArrayList<String>();
		List<String> dailyActionTrends = new ArrayList<String>();
		List<String> dailyWebsiteTrends = new ArrayList<String>();
		List<String> dailyCallsTrends = new ArrayList<String>();
		List<String> dailyDrivingTrends = new ArrayList<String>();

		Long count = service.getInsightCountsForBrand(brandId, "", startDate,
				endDate);
		if (count != null && count == 0) {
			model.addAttribute("noDataMessage", true);

			model.addAttribute("days", 0);
			model.addAttribute("minYear", 0);
			model.addAttribute("minMonth", 0);
			model.addAttribute("minDate", 0);
			model.addAttribute("maxYear", 0);
			model.addAttribute("maxMonth", 0);
			model.addAttribute("maxDate", 0);

			model.addAttribute("yActions", 0);
			model.addAttribute("yViews", 0);
			model.addAttribute("ySearches", 0);

			views.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");
			maps.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");

			websites.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			calls.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0 + "),  y: "
					+ 0 + " }");

			anlytics.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			directions.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
					+ "),  y: " + 0 + " }");

			String colorCode = "Blue";
			String color = "'" + colorCode + "'";

			String day = "SU";
			int cnt = 0;

			dailyViewTrends.add("{label: " + day + "," + "y:" + cnt + ","
					+ "color:" + color + "}");

			dailyActionTrends.add("{label: " + day + "," + "y:" + cnt + ","
					+ "color:" + color + "}");

			dailyWebsiteTrends.add("{label: " + day + "," + "y:" + cnt + ","
					+ "color:" + color + "}");

			dailyCallsTrends.add("{label: " + day + "," + "y:" + cnt + ","
					+ "color:" + color + "}");

			dailyDrivingTrends.add("{label: " + day + "," + "y:" + cnt + ","
					+ "color:" + color + "}");

			model.addAttribute("searchHistory", views);
			model.addAttribute("mapsHistory", maps);

			model.addAttribute("websitesHistory", websites);
			model.addAttribute("directionsHistory", directions);
			model.addAttribute("callsHistory", calls);

			List<String> searchPreTrends = new ArrayList<String>();

			searchPreTrends.add("{label: Jan,y:0}");

			model.addAttribute("searchPreTrends", searchPreTrends);
			model.addAttribute("searchCurTrends", searchPreTrends);

			model.addAttribute("actionPreTrends", searchPreTrends);
			model.addAttribute("actionCurTrends", searchPreTrends);

			model.addAttribute("viewPreTrends", searchPreTrends);
			model.addAttribute("viewCurTrends", searchPreTrends);
			model.addAttribute("anlytics", anlytics);

			model.addAttribute("brand", brand);
			model.addAttribute("state", state);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

		} else {
			model.addAttribute("noDataMessage", false);
			List<String> allStates = service.getAllStatesListByBrand(brand);

			Map<String, Long> insightsDataMap = service.getInsightsBrandData(
					brandId, state, startDate, endDate);

			java.util.Collections.sort(allStates);

			Long directCount = 0L;
			Long discoveryCount = 0L;

			Long searchCount = 0L;
			Long mapsCount = 0L;
			Long viewsCount = 0L;

			Long websiteCount = 0L;
			Long phoneCount = 0L;
			Long directionsCount = 0L;
			Long actionsCount = 0L;

			logger.info("States List ::" + allStates.size());

			if (insightsDataMap != null && insightsDataMap.size() > 0) {
				directCount = insightsDataMap.get("QUERIES_DIRECT");
				discoveryCount = insightsDataMap.get("QUERIES_INDIRECT");

				searchCount = insightsDataMap.get("VIEWS_SEARCH");
				mapsCount = insightsDataMap.get("VIEWS_MAPS");

				websiteCount = insightsDataMap.get("ACTIONS_WEBSITE");
				phoneCount = insightsDataMap.get("ACTIONS_PHONE");
				directionsCount = insightsDataMap
						.get("ACTIONS_DRIVING_DIRECTIONS");

			}
			viewsCount = searchCount + mapsCount;
			actionsCount = websiteCount + phoneCount + directionsCount;

			List<String[]> listofWeeks = getListofDates(startDate, endDate);

			Map<String, InsightsGraphDTO> weeklyInsights = new HashMap<String, InsightsGraphDTO>();
			List<String> keys = new ArrayList<String>();

			Map<String, InsightsGraphDTO> dailyMap = service.getHistory(
					brandId, state, startDate, endDate);

			Set<String> keySet = dailyMap.keySet();

			for (String key : keySet) {
				InsightsGraphDTO insightsGraphDTO = dailyMap.get(key);
				weeklyInsights.put(key, insightsGraphDTO);
				keys.add(key);
			}

			Long maxSerchCounts = 0L;
			Long maxCallCounts = 0L;

			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				InsightsGraphDTO dto = weeklyInsights.get(key);
				Long mapCounts = dto.getMapCount();
				Long searchCounts = dto.getSearchCount();
				Long websiteCounts = dto.getWebsiteCount();
				Long directionsCounts = dto.getDirectionsCount();
				Long callsCounts = dto.getCallsCount();

				if (searchCounts > maxSerchCounts)
					maxSerchCounts = searchCounts;
				if (callsCounts > maxCallCounts)
					maxCallCounts = callsCounts;

				// System.out.println("maxSerchCounts: "+ maxSerchCounts +
				// ", maxCallCounts: "+ maxCallCounts);

				// System.out.println("date is: "+ key);
				String[] date = key.split("-");
				Integer year = Integer.parseInt(date[0]);
				Integer month = Integer.parseInt(date[1]);
				Integer day = Integer.parseInt(date[2]);

				views.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + searchCounts + " }");
				maps.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + mapCounts + " }");
				websites.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: " + websiteCounts + " }");
				directions.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: " + directionsCounts + " }");
				calls.add("{x:new Date(" + year + "," + (month - 1) + "," + day
						+ "),  y: " + callsCounts + " }");

			}

			Map<String, List<BingAnalyticsDTO>> topAnalytics = service
					.getTopandBottomAnalytics(brandId, state, startDate,
							endDate);

			List<BingAnalyticsDTO> tTenAnalytics = topAnalytics
					.get("topAnalytics");
			List<BingAnalyticsDTO> bTenAnalytics = topAnalytics
					.get("bottomAnalytics");

			model.addAttribute("topAnalytics", tTenAnalytics);
			model.addAttribute("bAnalytics", bTenAnalytics);

			List<InsightsGraphDTO> insightstrendData = service
					.getMonthlyTrends(brandId, state);

			List<String> searchPreTrends = new ArrayList<String>();
			List<String> searchCurTrends = new ArrayList<String>();

			List<String> viewPreTrends = new ArrayList<String>();
			List<String> viewCurTrends = new ArrayList<String>();

			List<String> actionPreTrends = new ArrayList<String>();
			List<String> actionCurTrends = new ArrayList<String>();

			// Action Trends

			Map<String, Long> lastYearActonMonths = getLastYearMonths();
			Map<String, Long> currentYearActionMonths = getCurrentYearMonths();

			for (InsightsGraphDTO insightsGraphDTO : insightstrendData) {

				String monthYear = getMonthYear(insightsGraphDTO.getDate());
				if (lastYearActonMonths.containsKey(monthYear)) {
					lastYearActonMonths.put(monthYear,
							insightsGraphDTO.getTotalActionCount());
				}
				if (currentYearActionMonths.containsKey(monthYear)) {
					currentYearActionMonths.put(monthYear,
							insightsGraphDTO.getTotalActionCount());
				}
			}
			Set<String> lastYearSet = lastYearActonMonths.keySet();
			for (String key : lastYearSet) {
				Long value = lastYearActonMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				actionPreTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			Set<String> lcurrentYearSet = currentYearActionMonths.keySet();
			for (String key : lcurrentYearSet) {
				Long value = currentYearActionMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				actionCurTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			// View Trends
			Map<String, Long> lastYearViewMonths = getLastYearMonths();
			Map<String, Long> currentYearViewMonths = getCurrentYearMonths();

			for (InsightsGraphDTO insightsGraphDTO : insightstrendData) {

				String monthYear = getMonthYear(insightsGraphDTO.getDate());
				if (lastYearViewMonths.containsKey(monthYear)) {
					lastYearViewMonths.put(monthYear,
							insightsGraphDTO.getTotalViewCount());
				}
				if (currentYearViewMonths.containsKey(monthYear)) {
					currentYearViewMonths.put(monthYear,
							insightsGraphDTO.getTotalViewCount());
				}
			}
			Set<String> lastYearViewSet = lastYearViewMonths.keySet();
			for (String key : lastYearViewSet) {
				Long value = lastYearViewMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				viewPreTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			Set<String> currentYearViewSet = currentYearViewMonths.keySet();
			for (String key : currentYearViewSet) {
				Long value = currentYearViewMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				viewCurTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			// search Trends
			Map<String, Long> lastYearMonths = getLastYearMonths();
			Map<String, Long> currentYearMonths = getCurrentYearMonths();

			for (InsightsGraphDTO insightsGraphDTO : insightstrendData) {

				String monthYear = getMonthYear(insightsGraphDTO.getDate());
				if (lastYearMonths.containsKey(monthYear)) {
					lastYearMonths.put(monthYear,
							insightsGraphDTO.getTotalSearchCount());
				}
				if (currentYearMonths.containsKey(monthYear)) {
					currentYearMonths.put(monthYear,
							insightsGraphDTO.getTotalSearchCount());
				}
			}
			Set<String> lastYearsSet = lastYearMonths.keySet();
			for (String key : lastYearsSet) {
				Long value = lastYearMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				searchPreTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			Set<String> lcurrentYearsSet = currentYearMonths.keySet();
			for (String key : lcurrentYearsSet) {
				Long value = currentYearMonths.get(key);
				if (value == null) {
					value = 0L;
				}
				String name[] = key.split("-");
				String nameM = "'" + name[0] + "'";
				String monthV = nameM.replace('\'', '\"');
				searchCurTrends.add("{label: " + monthV + "," + "y:" + value
						+ "}");
			}

			List<BingReportDTO> anlyticsData = service.getAnlytics(brandId,
					state, startDate, endDate);

			for (BingReportDTO bingAnalyticsDTO : anlyticsData) {

				String[] date = bingAnalyticsDTO.getDate().split("-");
				Integer year = Integer.parseInt(date[0]);
				Integer month = Integer.parseInt(date[1]);
				Integer day = Integer.parseInt(date[2]);

				anlytics.add("{x:new Date(" + year + "," + (month - 1) + ","
						+ day + "),  y: "
						+ bingAnalyticsDTO.getImpressionCount() + " }");
			}
			if (anlyticsData != null && anlyticsData.size() == 0) {
				anlytics.add("{x:new Date(" + 0 + "," + (1 - 1) + "," + 0
						+ "),  y: " + 0 + " }");
			}

			Map<String, List<InsightsGraphDTO>> topBottomSearches = service
					.getTopandBottomSearches(brandId, state, startDate, endDate);

			List<InsightsGraphDTO> topSearchList = topBottomSearches
					.get("tSearch");
			List<InsightsGraphDTO> bottomSearchList = topBottomSearches
					.get("bSearch");
			List<InsightsGraphDTO> topViewList = topBottomSearches.get("tView");
			List<InsightsGraphDTO> bottomViewList = topBottomSearches
					.get("bView");
			List<InsightsGraphDTO> topActionList = topBottomSearches
					.get("tAction");
			List<InsightsGraphDTO> bottomActionList = topBottomSearches
					.get("bAction");

			List<GoogleInsightsReportDTO> topTenSearches = new ArrayList<GoogleInsightsReportDTO>();
			List<GoogleInsightsReportDTO> bottomTenSearches = new ArrayList<GoogleInsightsReportDTO>();
			List<GoogleInsightsReportDTO> topTenViews = new ArrayList<GoogleInsightsReportDTO>();
			List<GoogleInsightsReportDTO> bottomTenViews = new ArrayList<GoogleInsightsReportDTO>();
			List<GoogleInsightsReportDTO> topTenActions = new ArrayList<GoogleInsightsReportDTO>();
			List<GoogleInsightsReportDTO> bottomTenActions = new ArrayList<GoogleInsightsReportDTO>();

			for (InsightsGraphDTO graphDTO : topSearchList) {
				GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
				dto.setStore(graphDTO.getStore());
				dto.setState(graphDTO.getState());
				dto.setCity(graphDTO.getCity());
				dto.setTopDirectCount(graphDTO.getDirectCount());
				dto.setTopDiscoveryCount(graphDTO.getInDirectCount());
				dto.setTotalTopSearchCount(graphDTO.getTotalSearchCount());
				topTenSearches.add(dto);
			}

			for (InsightsGraphDTO graphDTO : bottomSearchList) {
				GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
				dto.setStore(graphDTO.getStore());
				dto.setState(graphDTO.getState());
				dto.setCity(graphDTO.getCity());
				dto.setBottomDirectCount(graphDTO.getDirectCount());
				dto.setBottomDiscoveryCount(graphDTO.getInDirectCount());
				dto.setTotalBottomSearchCount(graphDTO.getTotalSearchCount());
				bottomTenSearches.add(dto);
			}

			for (InsightsGraphDTO graphDTO : topViewList) {
				GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
				dto.setStore(graphDTO.getStore());
				dto.setState(graphDTO.getState());
				dto.setCity(graphDTO.getCity());
				dto.setTopViewSearchCount(graphDTO.getSearchCount());
				dto.setTopViewMapsCount(graphDTO.getMapCount());
				dto.setTotalTopViewsCount(graphDTO.getTotalViewCount());
				topTenViews.add(dto);
			}

			for (InsightsGraphDTO graphDTO : bottomViewList) {
				GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
				dto.setStore(graphDTO.getStore());
				dto.setState(graphDTO.getState());
				dto.setCity(graphDTO.getCity());
				dto.setBottomViewSearchCount(graphDTO.getSearchCount());
				dto.setBottomViewMapsCount(graphDTO.getMapCount());
				dto.setTotalBottomViewsCount(graphDTO.getTotalViewCount());
				bottomTenViews.add(dto);
			}

			for (InsightsGraphDTO graphDTO : topActionList) {
				GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
				dto.setStore(graphDTO.getStore());
				dto.setState(graphDTO.getState());
				dto.setCity(graphDTO.getCity());
				dto.setTopWebsiteCount(graphDTO.getWebsiteCount());
				dto.setTopDirectionsCount(graphDTO.getDirectionsCount());
				dto.setTopCallsCount(graphDTO.getCallsCount());
				dto.setTotalTopActionsCount(graphDTO.getTotalActionCount());
				topTenActions.add(dto);
			}

			for (InsightsGraphDTO graphDTO : bottomActionList) {
				GoogleInsightsReportDTO dto = new GoogleInsightsReportDTO();
				dto.setStore(graphDTO.getStore());
				dto.setState(graphDTO.getState());
				dto.setCity(graphDTO.getCity());
				dto.setBottomWebsiteCount(graphDTO.getWebsiteCount());
				dto.setBottomDirectionsCount(graphDTO.getDirectionsCount());
				dto.setBottomCallsCount(graphDTO.getCallsCount());
				dto.setTotalBottomActionsCount(graphDTO.getTotalActionCount());
				bottomTenActions.add(dto);
			}
			Calendar calStart = Calendar.getInstance();

			// Daily trends
			Map<Integer, InsightsGraphDTO> dailyTrends = service
					.getDailyTrends(brand, state, startDate, endDate);

			Set<Integer> keyPair = dailyTrends.keySet();
			String[] days = { "SU", "MO", "TU", "WE", "TH", "FR", "SA" };

			Map<String, Integer> maxKey = getMaxKey(dailyTrends);

			Long viewCount = 0L;
			Long actionCount = 0L;
			Long webCount = 0L;
			Long dirCount = 0L;
			Long callCount = 0L;

			String colorCode = "#00BFFF";
			String color = "'" + colorCode + "'";
			String colorG = "GREEN";
			String greenColor = "'" + colorG + "'";

			for (Integer integer : keyPair) {
				InsightsGraphDTO insightsGraphDTO = dailyTrends.get(integer);
				String day = "'" + days[integer - 1] + "'";
				Long totalViewCount = insightsGraphDTO.getTotalViewCount();
				Long totalActionCount = insightsGraphDTO.getTotalActionCount();
				Long websiteCount2 = insightsGraphDTO.getWebsiteCount();
				Long directionsCount2 = insightsGraphDTO.getDirectionsCount();
				Long callsCount = insightsGraphDTO.getCallsCount();

				viewCount = viewCount + totalViewCount;
				actionCount = actionCount + totalActionCount;
				webCount = webCount + websiteCount2;
				dirCount = dirCount + directionsCount2;
				callCount = callCount + callsCount;

				Integer viewMax = maxKey.get("view");
				Integer actionMax = maxKey.get("action");
				Integer websiteMax = maxKey.get("website");
				Integer callsMax = maxKey.get("calls");
				Integer directionsMax = maxKey.get("directions");

				if (integer == viewMax) {
					dailyViewTrends.add("{label: " + day + "," + "y:"
							+ totalViewCount + "," + "color:" + greenColor
							+ "}");
				} else {
					dailyViewTrends.add("{label: " + day + "," + "y:"
							+ totalViewCount + "," + "color:" + color + "}");
				}
				if (integer == actionMax) {
					dailyActionTrends.add("{label: " + day + "," + "y:"
							+ totalActionCount + "," + "color:" + greenColor
							+ "}");
				} else {
					dailyActionTrends.add("{label: " + day + "," + "y:"
							+ totalActionCount + "," + "color:" + color + "}");
				}
				if (integer == websiteMax) {

					dailyWebsiteTrends
							.add("{label: " + day + "," + "y:" + websiteCount2
									+ "," + "color:" + greenColor + "}");
				} else {
					dailyWebsiteTrends.add("{label: " + day + "," + "y:"
							+ websiteCount2 + "," + "color:" + color + "}");
				}
				if (integer == callsMax) {
					dailyCallsTrends.add("{label: " + day + "," + "y:"
							+ callsCount + "," + "color:" + greenColor + "}");
				} else {
					dailyCallsTrends.add("{label: " + day + "," + "y:"
							+ callsCount + "," + "color:" + color + "}");
				}
				if (integer == directionsMax) {
					dailyDrivingTrends.add("{label: " + day + "," + "y:"
							+ directionsCount2 + "," + "color:" + greenColor
							+ "}");
				} else {
					dailyDrivingTrends.add("{label: " + day + "," + "y:"
							+ directionsCount2 + "," + "color:" + color + "}");
				}
			}

			model.addAttribute("dailyViewTrends", dailyViewTrends);
			model.addAttribute("dailyActionTrends", dailyActionTrends);
			model.addAttribute("dailyWebsiteTrends", dailyWebsiteTrends);
			model.addAttribute("dailyCallsTrends", dailyCallsTrends);
			model.addAttribute("dailyDrivingTrends", dailyDrivingTrends);

			model.addAttribute("viewCount", viewCount);
			model.addAttribute("actionCount", actionCount);
			model.addAttribute("webCount", webCount);
			model.addAttribute("dirCount", dirCount);
			model.addAttribute("callCount", callCount);

			// daily trends

			calStart.setTime(startDate);
			model.addAttribute("days", listofWeeks.size());
			model.addAttribute("minYear", calStart.get(Calendar.YEAR));
			model.addAttribute("minMonth", calStart.get(Calendar.MONTH));
			model.addAttribute("minDate", calStart.get(Calendar.DAY_OF_MONTH));
			model.addAttribute("maxYear", cal.get(Calendar.YEAR));
			model.addAttribute("maxMonth", cal.get(Calendar.MONTH));
			model.addAttribute("maxDate", cal.get(Calendar.DAY_OF_MONTH));

			model.addAttribute("yActions", Math.round(maxCallCounts / 2));
			model.addAttribute("yViews", Math.round(maxSerchCounts / 2));
			model.addAttribute("ySearches", Math.round(discoveryCount / 2));

			model.addAttribute("topTenSearches", topTenSearches);
			model.addAttribute("bottomTenSearches", bottomTenSearches);
			model.addAttribute("topTenViews", topTenViews);
			model.addAttribute("bottomTenViews", bottomTenViews);
			model.addAttribute("topTenActions", topTenActions);
			model.addAttribute("bottomTenActions", bottomTenActions);

			// for graph .........
			model.addAttribute("brand", brand);
			model.addAttribute("state", state);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

			model.addAttribute("sd", start);
			model.addAttribute("ed", end);

			model.addAttribute("searchHistory", views);
			model.addAttribute("mapsHistory", maps);

			model.addAttribute("websitesHistory", websites);
			model.addAttribute("directionsHistory", directions);
			model.addAttribute("callsHistory", calls);

			model.addAttribute("searchPreTrends", searchPreTrends);
			model.addAttribute("searchCurTrends", searchCurTrends);

			model.addAttribute("actionPreTrends", actionPreTrends);
			model.addAttribute("actionCurTrends", actionCurTrends);

			model.addAttribute("viewPreTrends", viewPreTrends);
			model.addAttribute("viewCurTrends", viewCurTrends);
			model.addAttribute("anlytics", anlytics);

			java.util.Collections.sort(allStates);

			model.addAttribute("statesList", allStates);

			model.addAttribute("direct", directCount);
			model.addAttribute("discovery", discoveryCount);

			model.addAttribute("search", searchCount);
			model.addAttribute("maps", mapsCount);
			model.addAttribute("views", viewsCount);

			model.addAttribute("website", websiteCount);
			model.addAttribute("directions", directionsCount);
			model.addAttribute("calls", phoneCount);
			model.addAttribute("actions", actionsCount);

			logger.info("allStates ::" + allStates.size());
		}

		System.out.println();

		return "pdf-gmbbrand";
	}

	public static List<String[]> getListofDates(Date start, Date end) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

		/*
		 * Calendar calendar2 = Calendar.getInstance(); calendar2.clear();
		 * calendar2.setTime(start); start = calendar2.getTime();
		 * 
		 * Calendar calendar1 = Calendar.getInstance(); calendar1.clear();
		 * calendar1.setTime(end); end = calendar1.getTime();
		 */

		List<String[]> listofWeeks = new ArrayList<String[]>();

		long difference = (start.getTime() - end.getTime()) / 86400000;
		long days = Math.abs(difference);

		Calendar today = Calendar.getInstance();

		long diffinEndAndToday = (end.getTime() - today.getTimeInMillis()) / 86400000;
		long endDateTostart = Math.abs(diffinEndAndToday);

		int previousStart = -(int) endDateTostart;
		int previousEnd = -1;

		// System.out.println("Insights data is collecting for "+ days +
		// " days");

		for (int i = 0; i <= days; i++) {

			String[] dates = new String[2];

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(calendar.getTime());
			calendar.add(Calendar.DATE, previousStart);

			String startDate = formatter.format(calendar.getTime());
			dates[0] = startDate;

			calendar.setTime(calendar.getTime());
			calendar.add(Calendar.DATE, previousEnd);
			String endDate = formatter.format(calendar.getTime());

			dates[1] = endDate;

			previousStart = previousStart - 1;
			// System.out.println("Start and end dates are: "+ dates[1] +
			// "==="+dates[0]);
			listofWeeks.add(dates);

		}

		return listofWeeks;
	}

	private List<String[]> getListofWeeks(Date startDate, Date endDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
		List<String[]> listofWeeks = new ArrayList<String[]>();
		DateTime dateTime1 = new DateTime(startDate);
		DateTime dateTime2 = new DateTime(endDate);

		int weeks = Weeks.weeksBetween(dateTime1, dateTime2).getWeeks();

		Calendar today = Calendar.getInstance();

		long diffinEndAndToday = (endDate.getTime() - today.getTimeInMillis()) / 86400000;
		long endDateTostart = Math.abs(diffinEndAndToday);

		long diffinDates = (startDate.getTime() - endDate.getTime()) / 86400000;
		long diff = Math.abs(diffinDates);

		int previousStart = -(int) endDateTostart - 1;
		int previousEnd = -7;
		if (diff < 7) {
			previousEnd = -(int) diff - 1;
		}

		for (int i = 0; i <= weeks; i++) {

			String[] dates = new String[2];

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(calendar.getTime());
			calendar.add(Calendar.DATE, previousStart);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);

			String sDate = formatter.format(calendar.getTime());
			dates[0] = sDate;

			calendar.setTime(calendar.getTime());
			calendar.add(Calendar.DATE, previousEnd);

			Date d = calendar.getTime();

			if (d.before(startDate)) {
				d = startDate;
			}
			String eDate = formatter.format(d);
			dates[1] = eDate;

			previousStart = previousStart - 7;
			// System.out.println("Date is: "+ dates[0] + "=====" + dates[1] );

			listofWeeks.add(dates);
		}
		return listofWeeks;
	}

	public static String getFormattedDate(Date dateValue) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, YYYY");
		String format = formatter.format(dateValue);
		return format;
	}

	public static String getYYYYMMDD(Date dateValue) {
		SimpleDateFormat formatter = new SimpleDateFormat("YYYYMMdd");
		String format = formatter.format(dateValue);
		return format;
	}

	public static String[] getPastDates(int monthsold) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

		String[] dates = new String[2];

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(calendar2.getTime());
		calendar2.set(Calendar.DATE, 0);
		calendar2.set(Calendar.MILLISECOND, 0);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.HOUR, 0);
		calendar2.set(Calendar.MONTH, -monthsold);
		String endDate = formatter.format(calendar2.getTime());

		dates[1] = endDate;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(calendar2.getTime());
		calendar2.set(Calendar.MILLISECOND, 0);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.HOUR, 24);
		calendar.add(Calendar.DATE, -30);
		calendar.add(Calendar.DATE, 0);

		String startDate = formatter.format(calendar.getTime());
		dates[0] = startDate;

		System.out.println(dates[0] + "===="+ dates[1]);

		return dates;
	}
	
	public static void main(String[] args) {
		//getLastMonthDates(30);
		getPastDates(1);
	}

}
