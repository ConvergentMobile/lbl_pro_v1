package com.business.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.CitationThread;
import com.business.model.pojo.CheckReportEntity;
import com.business.model.pojo.CitationReportEntity;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.ReportService;
import com.business.web.validator.LocalBusinessValidator;

@Controller
public class UpdateCheckReportController {
	Logger logger = Logger.getLogger(UpdateCheckReportController.class);

	@Autowired
	private CheckReportService service;
	@Autowired
	private BusinessService businessservice;
	@Autowired
	private ReportService reportService;
	@Autowired
	private LocalBusinessValidator businessValidator;
	@RequestMapping(value = "/updateCheckreport.htm", method = RequestMethod.GET)
	public String UpdateCheckReport(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {
		
		CitationThread thread=new CitationThread(businessservice);
		thread.start();

		return "upload-export";

	}
	
	
	
}
