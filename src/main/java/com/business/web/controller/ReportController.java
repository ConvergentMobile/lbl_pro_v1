package com.business.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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

import com.business.common.dto.ChangeTrackingDTO;
import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.GoogleInsightsReportDTO;
import com.business.common.dto.InsightsGraphDTO;
import com.business.common.dto.InsightsHistory;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.common.util.ControllerUtil;
import com.business.common.util.DateUtil;
import com.business.model.pojo.AuditEntity;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ReportParams;
import com.business.model.pojo.ValueObject;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.ReportService;
import com.business.web.bean.ReportForm;
import com.business.web.bean.UsersBean;
import com.ctc.wstx.io.EBCDICCodec;

/**
 * @author Vasanth
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

	private ControllerUtil controllerUtil = new ControllerUtil();

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

		List<String> storeForBrand = reportService.getStoreForBrand(categoryId);
		logger.info("stores after selecting brand::" + storeForBrand);

		model.addAttribute("storelist", storeForBrand);

		return storeForBrand.toString();
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

	@RequestMapping(value = "/runGMBLocation.htm", method = RequestMethod.GET)
	public String runGMBLocation(Model model, HttpServletRequest request,
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
		Integer count = service.getInsightCountsForBrand(brand, store);

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
					.getInsightsDataForBrandAndStore(brand, store, startDate,
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
					.getHistoryForStore(brand, store, startDate, endDate);

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
		Integer count = service.getInsightCountsForBrand(brand, "");

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
					.getInsightsDataForBrandAndStore(brand, store, startDate,
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
					.getHistoryForStore(brand, store, startDate, endDate);

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
		List<String> views = new ArrayList<String>();
		List<String> maps = new ArrayList<String>();
		List<String> websites = new ArrayList<String>();
		List<String> directions = new ArrayList<String>();
		List<String> calls = new ArrayList<String>();
		Integer count = service.getInsightCountsForBrand(brand, "");

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
					.getInsightsDataForBrandAndStore(brand, store, startDate,
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
					.getHistoryForStore(brand, store, startDate, endDate);

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
		cal.add(Calendar.HOUR_OF_DAY, 23);
		cal.add(Calendar.MINUTE, 59);
		endDate = cal.getTime();
		logger.info("startDate ::" + startDate);
		logger.info("endDate ::" + endDate);

		List<InsightsGraphDTO> insightsExcelData = service
				.getInsightsBrandExcelData(brand, state, startDate, endDate);
		model.addAttribute("startDate", start);
		model.addAttribute("endDate", end);
		model.addAttribute("insightsExcelData", insightsExcelData);
		model.addAttribute("reportName", "GmbInsights-" + brand);

		return "gmbbrandexcel";
	}

	@RequestMapping(value = "/runGMBBrand.htm", method = RequestMethod.GET)
	public String runGMBBrand(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		String brand = request.getParameter("brand");
		String state = request.getParameter("state");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

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
		cal.add(Calendar.HOUR_OF_DAY, 23);
		cal.add(Calendar.MINUTE, 59);
		endDate = cal.getTime();
		logger.info("startDate ::" + startDate);
		logger.info("endDate ::" + endDate);

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

		List<String> views = new ArrayList<String>();
		List<String> maps = new ArrayList<String>();
		List<String> websites = new ArrayList<String>();
		List<String> directions = new ArrayList<String>();
		List<String> calls = new ArrayList<String>();

		Integer count = service.getInsightCountsForBrand(brand, "");
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

			model.addAttribute("searchHistory", views);
			model.addAttribute("mapsHistory", maps);

			model.addAttribute("websitesHistory", websites);
			model.addAttribute("directionsHistory", directions);
			model.addAttribute("callsHistory", calls);

			model.addAttribute("brand", brand);
			model.addAttribute("state", state);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

		} else {
			model.addAttribute("noDataMessage", false);
			List<String> allStates = service.getAllStatesListByBrand(brand);

			Map<String, Long> insightsDataMap = service.getInsightsBrandData(
					brand, state, startDate, endDate);

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

			Map<String, InsightsGraphDTO> dailyMap = service.getHistory(brand,
					state, startDate, endDate);

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

			Map<String, List<InsightsGraphDTO>> topBottomSearches = service
					.getTopandBottomSearches(brand, state, startDate, endDate);

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

		return "reports-gmbbrand";
	}

	@RequestMapping(value = "/pdfGMBBrand.htm", method = RequestMethod.GET)
	public String pdfGMBBrand(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		/*
		 * String url =
		 * "pdfGMBBrand.htm?data="+brand+"-"+state+"-"+start+"-"+end;
		 * model.addAttribute("url", url);
		 */

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

		Integer count = service.getInsightCountsForBrand(brand, "");
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

			model.addAttribute("searchHistory", views);
			model.addAttribute("mapsHistory", maps);

			model.addAttribute("websitesHistory", websites);
			model.addAttribute("directionsHistory", directions);
			model.addAttribute("callsHistory", calls);

			model.addAttribute("brand", brand);
			model.addAttribute("state", state);
			model.addAttribute("start", getFormattedDate(startDate));
			model.addAttribute("end", getFormattedDate(endDate));

		} else {
			model.addAttribute("noDataMessage", false);
			List<String> allStates = service.getAllStatesListByBrand(brand);

			Map<String, Long> insightsDataMap = service.getInsightsBrandData(
					brand, state, startDate, endDate);

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

			Map<String, InsightsGraphDTO> dailyMap = service.getHistory(brand,
					state, startDate, endDate);

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

			Map<String, List<InsightsGraphDTO>> topBottomSearches = service
					.getTopandBottomSearches(brand, state, startDate, endDate);

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

	public String getFormattedDate(Date dateValue) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, YYYY");
		String format = formatter.format(dateValue);
		return format;
	}

}
