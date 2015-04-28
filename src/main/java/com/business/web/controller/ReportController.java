package com.business.web.controller;

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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.DateUtil;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ReportParams;
import com.business.model.pojo.ValueObject;
import com.business.service.ReportService;
import com.business.web.bean.ReportForm;
import com.business.web.bean.UsersBean;

/**
 * 
 * @author Vasanth
 * 
 * 
 * 
 */
@Controller
public class ReportController {
	Logger logger = Logger.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;

	/**
	 * getReports
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/reports.htm", method = RequestMethod.GET)
	public String getReports(Model model, HttpServletRequest request, HttpSession session) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		Set<ReportEntity> reports = reportService.getReports();
		for (ReportEntity rpt : reports)
			logger.debug("id, params size: " + rpt.getId() + " --- " + rpt.getParams().size());

		ReportEntity selReport = null;
		if (request.getParameter("reportId") == null || request.getParameter("reportId") == "") { //get the first report
			 Iterator<ReportEntity> iterator = reports.iterator();
			 while (iterator.hasNext()) {
				 selReport = iterator.next();
				 break;
			 }
		} else {
			Integer reportId = Integer.valueOf(request.getParameter("reportId"));
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
	public ModelAndView runReport(@ModelAttribute("reportForm") ReportForm reportForm, BindingResult bindingResult,
										@RequestParam(value = "page", required = false) String whichPage,
										HttpServletRequest request, HttpSession session,Model model) throws Exception {
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

		Date fromDate = reportForm.getStartDate();
		Date toDate = reportForm.getEndDate();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		if (fromDate != null && toDate != null) {
	 		//check that to_date >= from_date
	 		if (toDate.compareTo(fromDate) < 0) {
				mv.addObject("error", "Start Date cannot be later than End Date");
				return mv;
	 		} else {
	 			params.put("fromDate", sdf.format(fromDate));
	 			params.put("toDate", sdf.format(toDate));
	 		}
		}

		if (fromDate == null && toDate != null) {
			mv.addObject("error", "Start Date cannot be Null");
			return mv;
		}
		
		if (fromDate != null) {
	 		params.put("fromDate", sdf.format(fromDate));
		}

		if (fromDate != null && toDate == null) { //if no toDate specified, use today
			toDate = Calendar.getInstance().getTime();
		}

		if (toDate != null) {
	 		params.put("toDate", sdf.format(toDate));
		}

		ReportEntity report = reportForm.getReport();
		if (report.getParams() != null && report.getParams().size() > 0) {
			for (ReportParams rp : report.getParams()) {
				params.put(rp.getParamName(), rp.getParamValue());
				logger.debug("p/v: " + rp.getParamName() + ", " + rp.getParamValue());
			}
		}

		//otherParams are used to pass report params that are not user specified
		Map<String, String> otherParams = new HashMap<String, String>();
		otherParams.put("userName", session.getAttribute("userName").toString());
		otherParams.put("roleId", session.getAttribute("roleId").toString());
		Integer id = report.getId();
		rptForm = reportService.runReport(id, report.getParams(), otherParams, (page-1)*recordsPerPage,
 													recordsPerPage, reportForm.getSortColumn(), reportForm.getSortOrder(),fromDate,toDate);
		
		int noOfRecords = 0;
		int noOfPages = 0;

		List<ValueObject> reportRows = rptForm.getReportRows();
		if (! reportRows.isEmpty()) {
			noOfRecords = (Integer) reportRows.get(reportRows.size()-1).getField1();
			noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		}

        logger.debug("noOfRecords, noOfPages: " + noOfRecords + ", " + noOfPages);

        if (! reportRows.isEmpty()) {
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

        request.setAttribute("currentPageStart", currentPageStart);
        request.setAttribute("currentPageEnd", currentPageEnd);        
		mv.addObject("reportForm", rptForm);

		String reportType = "other";
		if (id == 2) {
			reportType = "UploadReport";
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
		session.setAttribute("reportForm", rptForm);
		session.setAttribute("reports", reports);
        return mv;

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
	//@ResponseBody
	public String getUploadReportDetails(@RequestParam("brand") String brand,@RequestParam("date") String date,
			@ModelAttribute("reportForm") ReportForm reportForm, BindingResult bindingResult, HttpSession session,Model model) throws ParseException{
		
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date2 = DateUtil.getDate("MM/dd/yyyy HH:mm:ss", sdf.parse(date.replace("-", "/")));
		System.out.println("date :: "+date2);
		java.sql.Timestamp timestamp = new java.sql.Timestamp(date2.getTime());
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		if (brand != null && date != null) {
			businessDTOs = reportService.getUploadReportDetails(brand,timestamp);
		}
		reportForm = (ReportForm) session.getAttribute("reportForm");
		model.addAttribute("reportForm", reportForm);
		Set<ReportEntity> reports = (Set<ReportEntity>) session.getAttribute("reports");
		model.addAttribute("reports",reports);
		model.addAttribute("uploadReports", businessDTOs);
		model.addAttribute("reportType", "UploadReport");
		return "reports";
	}
}
