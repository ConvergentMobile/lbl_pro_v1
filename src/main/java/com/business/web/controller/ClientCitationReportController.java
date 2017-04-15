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
import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.AccuracyGraphDTO;
import com.business.common.dto.ChangeTrackingDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.CitationDTO;
import com.business.common.dto.CitationGraphDTO;
import com.business.common.dto.CitationReportDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.ControllerUtil;
import com.business.model.pojo.CitationGraphEntity;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.ReportService;
import com.business.web.bean.ReportForm;
import com.business.web.bean.UsersBean;
import com.business.web.validator.LocalBusinessValidator;

@Controller
public class ClientCitationReportController {

	Logger logger = Logger.getLogger(ClientCitationReportController.class);

	@Autowired
	private CheckReportService service;
	@Autowired
	private BusinessService businessservice;
	@Autowired
	private ReportService reportService;
	@Autowired
	private LocalBusinessValidator businessValidator;

	private ControllerUtil controllerUtil = new ControllerUtil();

	@RequestMapping(value = "/clientreports-citation.htm", method = RequestMethod.GET)
	public String getCheckReportListing(
			@RequestParam("brand") String brandname, Model model,
			HttpSession session, HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		logger.info("brandname::::::::::::::" + brandname);
		model.addAttribute("brandname", brandname);

		Integer totalLocationCount = 0;
		totalLocationCount = businessservice.getTotalCitationCount(brandname);
		List<CitationReportDTO> listOfBussinessinfo = businessservice
				.getListOfCitationInfo(brandname);
		if (!listOfBussinessinfo.isEmpty() && listOfBussinessinfo.size() > 10) {
			listOfBussinessinfo = listOfBussinessinfo.subList(0, 10);
		}

		List<String> storeForBrand = reportService.getStoreForBrand(brandname);

		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		model.addAttribute("totalLocationCount", totalLocationCount);
		model.addAttribute("citationinfo", listOfBussinessinfo);
		
		 Set<CitationReportDTO> citationreportinfo = new
		 LinkedHashSet<CitationReportDTO>( listOfBussinessinfo);
		 
		 session.setAttribute("citationreportinfo", citationreportinfo);
		model.addAttribute("mode", "mode");
		return "clientreports-citation";

	}

	@RequestMapping(value = "/clientloadmore-reports-citation.htm", method = RequestMethod.GET)
	public String getLoadMoreCitations(@RequestParam("brand") String brandname,
			Model model, HttpSession session, HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		model.addAttribute("brandname", brandname);

		Integer totalLocationCount = 0;
		totalLocationCount = businessservice.getTotalCitationCount(brandname);
		List<CitationReportDTO> listOfBussinessinfo = businessservice
				.getListOfCitationInfo(brandname);

		List<String> storeForBrand = reportService.getStoreForBrand(brandname);

		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		model.addAttribute("totalLocationCount", totalLocationCount);
		model.addAttribute("citationinfo", listOfBussinessinfo);
		model.addAttribute("mode", "");
		return "clientreports-citation";

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
	@RequestMapping(value = "/getClientCitationGraphInfo.htm", method = RequestMethod.GET)
	public @ResponseBody
	String getCitationGraph(@RequestParam("brandname") String brandname,

	Model model) throws IllegalStateException, SystemException,
			JsonGenerationException, JsonMappingException, IOException {
		String json = null;
		ObjectMapper objectMapper = new ObjectMapper();

		List<CitationGraphDTO> citationgraph = service
				.getCitationGraphInfo(brandname);

		logger.info("Total citaion brands found for Brand are:"
				+ citationgraph.size());

		model.addAttribute("accuracygraph", citationgraph);
		json = objectMapper.writeValueAsString(citationgraph);
		return json;

	}

	@RequestMapping(value = "/clientreportscitation.htm", method = RequestMethod.GET)
	public String getCitationtListing(
			@RequestParam("brandname") String brandname,
			@RequestParam("store") String store, Model model,
			HttpSession session, HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		logger.info("brandname::::::::::::::" + brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("mode", "");
		List<CitationReportDTO> citationinfo = new ArrayList<CitationReportDTO>();
		CitationReportDTO citationReportDTO = businessservice
				.getCitationInfoInfoByStore(store, brandname);
		String paths = citationReportDTO.getPaths();
		List<String> pathval=new ArrayList<String>();
		if(paths!=null){
			String[] split = paths.split(",");
			for (String path : split) {
				pathval.add(path);
				
			}	
		}
		
		String domainAuthority = citationReportDTO.getDomainAuthority();
		List<String> domainAuthories=new ArrayList<String>();
		if(domainAuthority!=null){
			String[] domains = domainAuthority.split(",");
			for (String domain : domains) {
				domainAuthories.add(domain);
				
			}
		}
	
		
		
		citationinfo.add(citationReportDTO);

		Integer count = businessservice.getTotalCitationCount(brandname);

		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("dAValues", domainAuthories);
		model.addAttribute("StoreUrllistvalue", pathval);
		model.addAttribute("liststoreNames", storeForBrand);
		model.addAttribute("totalLocationCount", count);
		model.addAttribute("citationinfo", citationinfo);

		return "clientreports-citation";

	}

	@RequestMapping(value = "/clientreportbyStorecitation.htm", method = RequestMethod.GET)
	public String getCitationtByStoreListing(
			@RequestParam("brandname") String brandname,
			@RequestParam("store") String store, Model model,
			HttpSession session, HttpServletRequest request) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		logger.info("brandname::::::::::::::" + brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("mode", "");
		List<CitationReportDTO> citationinfo = new ArrayList<CitationReportDTO>();
		CitationReportDTO citationReportDTO = businessservice
				.getCitationInfoInfoByStore(store, brandname);
		String paths = citationReportDTO.getPaths();
		List<String> pathval=new ArrayList<String>();
		if(paths!=null){
			String[] split = paths.split(",");
			for (String path : split) {
				pathval.add(path);
				
			}	
		}
		
		String domainAuthority = citationReportDTO.getDomainAuthority();
		List<String> domainAuthories=new ArrayList<String>();
		if(domainAuthority!=null){
			String[] domains = domainAuthority.split(",");
			for (String domain : domains) {
				domainAuthories.add(domain);
				
			}
		}
		citationinfo.add(citationReportDTO);

		Integer count = businessservice.getTotalCitationCount(brandname);

		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("dAValues", domainAuthories);
		model.addAttribute("StoreUrllistvalue", pathval);
		model.addAttribute("liststoreNames", storeForBrand);
		model.addAttribute("totalLocationCount", count);
		model.addAttribute("citationinfo", citationinfo);

		return "clientlocation-reports-citation";

	}

	@RequestMapping(value = "/clientcitationreportxls.htm", method = RequestMethod.GET)
	public String reportdownload(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		LinkedHashSet<CitationReportDTO> reportType = (LinkedHashSet<CitationReportDTO>) session
				.getAttribute("citationreportinfo");
		model.addAttribute("listOfAPIs", reportType);
		model.addAttribute("citationreportType", "CitationListReport");
		return "citationreportView";

	}

/*	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/citationreportpdf.htm", method = RequestMethod.GET)
	public String reportpdfdownload(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		LinkedHashSet<AccuracyDTO> reportType = (LinkedHashSet<AccuracyDTO>) session
				.getAttribute("citationreportinfo");
		model.addAttribute("listOfAPIs", reportType);
		model.addAttribute("citationreportType", "CitationListReport");
		return "citationpdfView";

	}*/

}
