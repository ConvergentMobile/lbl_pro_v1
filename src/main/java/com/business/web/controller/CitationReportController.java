package com.business.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import com.business.common.dto.ChangeTrackingDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.CitationDTO;
import com.business.common.dto.CitationGraphDTO;
import com.business.common.dto.CitationReportDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.ControllerUtil;
import com.business.model.pojo.CitationGraphEntity;
import com.business.model.pojo.ValueObject;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.ReportService;
import com.business.web.bean.ReportForm;
import com.business.web.bean.UsersBean;
import com.business.web.validator.LocalBusinessValidator;

@Controller
public class CitationReportController {

	Logger logger = Logger.getLogger(CitationReportController.class);

	@Autowired
	private CheckReportService service;
	@Autowired
	private BusinessService businessservice;
	@Autowired
	private ReportService reportService;
	@Autowired
	private LocalBusinessValidator businessValidator;

	private ControllerUtil controllerUtil = new ControllerUtil();

	@RequestMapping(value = "/reports-citation.htm", method = RequestMethod.GET)
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
		Set<CitationReportDTO> citationreportinfo = new LinkedHashSet<CitationReportDTO>(
				listOfBussinessinfo);

		session.setAttribute("citationreportinfo", citationreportinfo);

		/*for (CitationReportDTO citationReportDTO : listOfBussinessinfo) {
			totalLocationCount += citationReportDTO.getPathCount();
		}*/
		if (!listOfBussinessinfo.isEmpty() && listOfBussinessinfo.size() > 10) {
			listOfBussinessinfo = listOfBussinessinfo.subList(0, 10);
		}

		List<String> storeForBrand = reportService.getStoreForBrand(brandname);

		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		model.addAttribute("totalLocationCount", totalLocationCount);
		model.addAttribute("citationinfo", listOfBussinessinfo);

		model.addAttribute("mode", "mode");
		return "reports-citation";

	}

	@RequestMapping(value = "/loadmore-reports-citation.htm", method = RequestMethod.GET)
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
		/*for (CitationReportDTO citationReportDTO : listOfBussinessinfo) {
			totalLocationCount += citationReportDTO.getPathCount();
		}*/
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);

		model.addAttribute("brandname", brandname);
		model.addAttribute("liststoreNames", storeForBrand);
		model.addAttribute("totalLocationCount", totalLocationCount);
		model.addAttribute("citationinfo", listOfBussinessinfo);
		model.addAttribute("mode", "");
		return "reports-citation";

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

	@RequestMapping(value = "/getCitationGraphInfo.htm", method = RequestMethod.GET)
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

	@RequestMapping(value = "/reportscitation.htm", method = RequestMethod.GET)
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
		List<String> pathval = new ArrayList<String>();
		if (paths != null) {
			String[] split = paths.split(",");
			for (String path : split) {
				pathval.add(path);

			}
		}

		String domainAuthority = citationReportDTO.getDomainAuthority();
		List<String> domainAuthories = new ArrayList<String>();
		if (domainAuthority != null) {
			String[] domains = domainAuthority.split(",");
			for (String domain : domains) {
				domainAuthories.add(domain);

			}
		}

		citationinfo.add(citationReportDTO);
		/*int totalCount=0;
		LinkedHashSet<CitationReportDTO> citationReportDTOs = (LinkedHashSet<CitationReportDTO>) session
				.getAttribute("citationreportinfo");
		for (CitationReportDTO citationReportDTO2 : citationReportDTOs) {
			totalCount+=citationReportDTO2.getPathCount();
		}*/
		Integer count = businessservice.getTotalCitationCount(brandname);

		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("dAValues", domainAuthories);
		model.addAttribute("StoreUrllistvalue", pathval);
		model.addAttribute("liststoreNames", storeForBrand);
		model.addAttribute("totalLocationCount", count);
		model.addAttribute("citationinfo", citationinfo);

		return "reports-citation";

	}

	@RequestMapping(value = "/reportbyStorecitation.htm", method = RequestMethod.GET)
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
		List<String> pathval = new ArrayList<String>();
		if (paths != null) {
			String[] split = paths.split(",");
			for (String path : split) {
				pathval.add(path);

			}
		}

		String domainAuthority = citationReportDTO.getDomainAuthority();
		List<String> domainAuthories = new ArrayList<String>();
		if (domainAuthority != null) {
			String[] domains = domainAuthority.split(",");
			for (String domain : domains) {
				domainAuthories.add(domain);

			}
		}

		citationinfo.add(citationReportDTO);
		Integer count = businessservice.getTotalCitationCount(brandname);
		/*int totalCount=0;
		Integer count = businessservice.getTotalCitationCount(brandname);
		LinkedHashSet<CitationReportDTO> citationReportDTOs = (LinkedHashSet<CitationReportDTO>) session
				.getAttribute("citationreportinfo");
		for (CitationReportDTO citationReportDTO2 : citationReportDTOs) {
			totalCount+=citationReportDTO2.getPathCount();
		}*/
		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("dAValues", domainAuthories);
		model.addAttribute("StoreUrllistvalue", pathval);
		model.addAttribute("liststoreNames", storeForBrand);
		model.addAttribute("totalLocationCount", count);
		model.addAttribute("citationinfo", citationinfo);
		model.addAttribute("storeName", store);
		return "location-reports-citation";

	}

	@RequestMapping(value = "/citationreportxls.htm", method = RequestMethod.GET)
	public String reportdownload(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		LinkedHashSet<CitationReportDTO> reportType = (LinkedHashSet<CitationReportDTO>) session
				.getAttribute("citationreportinfo");
		model.addAttribute("listOfAPIs", reportType);
		model.addAttribute("citationreportType", "CitationListReport");
		return "citationreportView";

	}

	@RequestMapping(value = "/citationPdfReport.htm", method = RequestMethod.GET)
	public void reportpdfdownload(Model model, HttpServletRequest request,
			HttpSession session, HttpServletResponse response) throws Exception {
		String brandName = request.getParameter("brand");
		String store = request.getParameter("store");
		logger.info("brandname:::::::::::::::" + brandName);
		Integer brandId = service.getClinetIdIdByName(brandName);
		ServletContext servletContext = request.getSession()
				.getServletContext();
		String reportName = servletContext.getRealPath("citationReportJava");
		File tempFile = File.createTempFile("citationReport", ".pdf");// pdf
		String logo = servletContext.getRealPath("logo-lbl1.png");
		String logoCiation = servletContext
				.getRealPath("ico_title_citations_report.png");
		String rptOut = tempFile.getAbsolutePath();
		try {

			Map<String, Object> params = new HashMap<String, Object>();

			/* String storeId = "000900128"; */
			params.put("brandName", brandName);
			params.put("logoDir", logo);
			params.put("logoCitation", logoCiation);
			params.put("storeId", store);
			params.put("brandId", brandId);
			List<ValueObject> res = service.getCiationData(brandName, store);

			List<ValueObject> subDS1 = service.getChartDataDS(brandName);
			params.put("subDS1", subDS1);

			JasperCompileManager.compileReportToFile(reportName + ".jrxml",
					reportName + ".jasper");
			JasperPrint print = JasperFillManager.fillReport(reportName
					+ ".jasper", params, new JRBeanCollectionDataSource(res));
			JasperExportManager.exportReportToPdfFile(print, rptOut);

			ServletContext context = session.getServletContext();
			final int BUFFER_SIZE = 4096;
			try {

				File downloadFile = new File(rptOut);
				FileInputStream inputStream = new FileInputStream(downloadFile);

				String mimeType = context.getMimeType(rptOut);
				if (mimeType == null) {
					// set to binary type if MIME mapping not found
					mimeType = "application/pdf";
				}
				System.out.println("MIME type: " + mimeType);

				// set content attributes for the response
				response.setContentType(mimeType);
				response.setContentLength((int) downloadFile.length());

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format(
						"attachment; filename=\"%s\"", downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				// get output stream of the response
				OutputStream outStream = response.getOutputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;

				// write bytes read from the input stream into the output stream
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}

				inputStream.close();
				outStream.close();

			} catch (Exception e) {
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}

	}

}
