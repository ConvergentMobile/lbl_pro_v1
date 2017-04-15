package com.business.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.AccuracyThread;
import com.business.common.util.ControllerUtil;
import com.business.common.util.FailedScrapingThread;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.ReportService;
import com.business.web.bean.UsersBean;
import com.business.web.validator.LocalBusinessValidator;

@Controller
public class FailedScrapingController {

	Logger logger = Logger.getLogger(FailedScrapingController.class);

	@Autowired
	private CheckReportService service;
	@Autowired
	private BusinessService businessservice;
	@Autowired
	private ReportService reportService;

	@Autowired
	private LocalBusinessValidator businessValidator;

	private ControllerUtil controllerUtil = new ControllerUtil();

	@RequestMapping(value = "runFailedScrapingdata.htm", method = RequestMethod.GET)
	public String getCheckReportListing(Model model, HttpSession session,
			HttpServletRequest request) {
		
		FailedScrapingThread thread = new FailedScrapingThread(businessservice);
		thread.start();

		controllerUtil.getUploadDropDown(model, request, businessservice);
		
		return "upload-export";
	}
	
}
