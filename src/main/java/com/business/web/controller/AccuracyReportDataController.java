package com.business.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.business.common.util.AccuracyThread;
import com.business.common.util.ControllerUtil;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.ReportService;
import com.business.web.validator.LocalBusinessValidator;

@Controller
public class AccuracyReportDataController {

	Logger logger = Logger.getLogger(AccuracyReportDataController.class);

	@Autowired
	private CheckReportService service;
	@Autowired
	private BusinessService businessservice;
	@Autowired
	private ReportService reportService;
	@Autowired
	private LocalBusinessValidator businessValidator;
	private ControllerUtil controllerUtil = new ControllerUtil();

	@RequestMapping(value = "runAccuracyReportdata.htm", method = RequestMethod.GET)
	public String getCheckReportListing(Model model, HttpSession session,
			HttpServletRequest request) {
		
		AccuracyThread thread = new AccuracyThread(businessservice);
		thread.start();

		controllerUtil.getUploadDropDown(model, request, businessservice);
		
		return "upload-export";
	}
}
