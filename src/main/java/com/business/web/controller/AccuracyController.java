package com.business.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.business.common.dto.AccuracyGraphDTO;
import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.CitationGraphDTO;
import com.business.common.dto.CitationReportDTO;
import com.business.common.dto.CitationStoreHistoryDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.ControllerUtil;
import com.business.model.pojo.AccuarcyGraphEntity;
import com.business.model.pojo.AccuracyReportEntity;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.ReportService;
import com.business.web.bean.LblErrorBean;
import com.business.web.bean.ReportForm;
import com.business.web.bean.UsersBean;
import com.business.web.validator.LocalBusinessValidator;
import com.google.gson.Gson;

@Controller
public class AccuracyController {

	Logger logger = Logger.getLogger(AccuracyController.class);

	@Autowired
	private CheckReportService service;
	@Autowired
	private BusinessService businessService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private LocalBusinessValidator businessValidator;

	@RequestMapping(value = "/runVisiblityReport.htm", method = RequestMethod.GET)
	public String getVisibilityReport(@RequestParam("store") String store,@RequestParam("brandname") String brandName,Model model,HttpServletRequest req,
			HttpSession httpSession) {
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		List<String> storeForBrand = reportService.getStoreForBrand(brandName);
		model.addAttribute("brandname", brandName);
		model.addAttribute("liststoreNames", storeForBrand);
		Map<String, Integer> errorsCount = businessService.getisDirectoryExist(store,brandName);
		Integer googleErrors = errorsCount.get("google");
		Integer bingErrors = errorsCount.get("foursquare");
		Integer yelpErrors = errorsCount.get("yelp");
		Integer YahooErrors = errorsCount.get("yahoo");
		Integer ypErrors = errorsCount.get("yp");
		Integer citysearchErrors = errorsCount.get("citysearch");
		Integer mapquestErrors = errorsCount.get("mapquest");
		Integer superpagesErrors = errorsCount.get("usplaces");
		Integer yellobookErrors = errorsCount.get("yellowbot");
		Integer whitepagesErrors = errorsCount.get("ezlocal");
		Integer totalCount=googleErrors+bingErrors+yellobookErrors+yelpErrors+citysearchErrors+mapquestErrors+ypErrors+YahooErrors+whitepagesErrors+superpagesErrors;
				Integer totalPercentage=totalCount/10;
		model.addAttribute("googleErrors", googleErrors);
		model.addAttribute("bingErrors", bingErrors);
		model.addAttribute("yelpErrors", yelpErrors);
		model.addAttribute("YahooErrors", YahooErrors);
		model.addAttribute("ypErrors", ypErrors);
		model.addAttribute("citysearchErrors", citysearchErrors);
		model.addAttribute("mapquestErrors", mapquestErrors);
		model.addAttribute("superpagesErrors", superpagesErrors);
		model.addAttribute("yellobookErrors", yellobookErrors);
		model.addAttribute("whitepagesErrors", whitepagesErrors);
		model.addAttribute("store", store);
		model.addAttribute("totalPercentage", totalPercentage);
		return "reports-brands-02";
		
	}
	@RequestMapping(value = "/runAccuracyReport.htm", method = RequestMethod.GET)
	public String getAccuracyReport(@RequestParam("store") String store,@RequestParam("brandname") String brandName,Model model,HttpServletRequest req,
			HttpSession httpSession) {
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		List<String> storeForBrand = reportService.getStoreForBrand(brandName);
		model.addAttribute("brandname", brandName);
		model.addAttribute("liststoreNames", storeForBrand);
		
		AccuracyReportEntity  errorsCount = businessService.getpercentageForStoreCount(store);
		Integer googleErrors = 0;
		Integer bingErrors = 0;
		Integer yelpErrors = 0;
		Integer YahooErrors = 0;
		Integer ypErrors = 0;
		Integer citysearchErrors = 0;
		Integer mapquestErrors = 0;
		Integer superpagesErrors = 0;
		Integer yellobookErrors = 0;
		Integer whitepagesErrors = 0;
		Integer averageAccuracy = 0;
		Integer accuracy=0;
		if(errorsCount!=null){
			 googleErrors = errorsCount.getGoogleAccuracy();
			 bingErrors = errorsCount.getFoursquareAccuracy();
			 yelpErrors = errorsCount.getYelpAccuracy();
			 YahooErrors = errorsCount.getYahooAccuracy();
			 ypErrors = errorsCount.getYpAccuracy();
			 citysearchErrors = errorsCount.getCitySearchAccuracy();
			 mapquestErrors = errorsCount.getMapQuestAccuracy();
			 superpagesErrors = errorsCount.getUslocalAccuracy();
			 yellobookErrors = errorsCount.getYellobotAccuracy();
			 whitepagesErrors = errorsCount.getEzlocalAccuracy();
			 averageAccuracy = errorsCount.getAverageAccuracy();
			 accuracy=errorsCount.getAccuracy();
		}
	


		model.addAttribute("googleErrors", googleErrors);
		model.addAttribute("bingErrors", bingErrors);
		model.addAttribute("yelpErrors", yelpErrors);
		model.addAttribute("YahooErrors", YahooErrors);
		model.addAttribute("ypErrors", ypErrors);
		model.addAttribute("citysearchErrors", citysearchErrors);
		model.addAttribute("mapquestErrors", mapquestErrors);
		model.addAttribute("superpagesErrors", superpagesErrors);
		model.addAttribute("yellobookErrors", yellobookErrors);
		model.addAttribute("whitepagesErrors", whitepagesErrors);
		model.addAttribute("averageAccuracy", averageAccuracy);
		model.addAttribute("accuracy", accuracy);
		model.addAttribute("store", store);
		return "reports-brands-03";
		
	}
	@RequestMapping(value = "/runCitaionStoreReport.htm", method = RequestMethod.GET)
	public String getCitationReport(@RequestParam("store") String store,@RequestParam("brandname") String brandname,Model model,HttpServletRequest req,
			HttpSession httpSession) {

		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		logger.info("brandname::::::::::::::" + brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("mode", "");
		List<CitationReportDTO> citationinfo = new ArrayList<CitationReportDTO>();
		List<LocalBusinessDTO> businessDTOs=new ArrayList<LocalBusinessDTO>();
		LocalBusinessDTO businessListinginfo = businessService.getBusinessListinginfo(store, brandname);
		businessDTOs.add(businessListinginfo);
		CitationReportDTO citationReportDTO = businessService
				.getCitationInfoInfoByStore(store, brandname);
		String paths = citationReportDTO.getPaths();
		Map<String,String> pathval = new HashMap<String,String>();
		if (paths != null) {
			String[] split = paths.split(",");
			for (String path : split) {
				if(path.contains("google")){
					pathval.put(path,"Google");
				}else if(path.contains("yahoo")){
					pathval.put(path,"Yahoo");
				}else if(path.contains("yelp")){
					pathval.put(path,"Yelp");
				}else if(path.contains("foursquare")){
					pathval.put(path,"Foursquare");
				}else if(path.contains("ezlocal")){
					pathval.put(path,"Ezlocal");
				}else if(path.contains("yp")){
					pathval.put(path,"YP");
				}else if(path.contains("usplaces")){
					pathval.put(path,"UsPlaces");
				}else if(path.contains("yellowbot")){
					pathval.put(path,"Yellowbot");
				}else if(path.contains("mapquest")){
					pathval.put(path,"MapQuest");
				}else if(path.contains("citysearch")){
					pathval.put(path,"Citysearch");
				}
			

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
		Integer count = businessService.getTotalCitationCount(brandname);


		List<String> storeForBrand = reportService.getStoreForBrand(brandname);
		model.addAttribute("brandname", brandname);
		model.addAttribute("dAValues", domainAuthories);
		model.addAttribute("StoreUrllistvalue", pathval);
		model.addAttribute("liststoreNames", storeForBrand);
		model.addAttribute("totalLocationCount", count);
		model.addAttribute("citationinfo", citationinfo);
		model.addAttribute("storeName", store);
		model.addAttribute("businessDTOs",businessDTOs);
		return "reports-brands-04";

	}
	@RequestMapping(value = "/getGoogleGraphInfo.htm", method = RequestMethod.GET)
	public @ResponseBody
	String getGoogleGraphInfo(@RequestParam("store") String store,@RequestParam("brandname") String brandname,
			@RequestParam("directory") String directory,
	Model model) throws IllegalStateException, SystemException,
			JsonGenerationException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		boolean googleGraphInfo = service.getGoogleGraphInfo(store,directory,brandname);

															
		json = objectMapper.writeValueAsString(googleGraphInfo);
		return json;

	}
	@RequestMapping(value = "/getCitationStoreGraphInfo.htm", method = RequestMethod.GET)
	public @ResponseBody
	String getCitationStoreGraphInfo(@RequestParam("store") String store,@RequestParam("brandname") String brandname,
			@RequestParam("directory") String directory,
	Model model) throws IllegalStateException, SystemException,
			JsonGenerationException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		List<CitationReportDTO> googleGraphInfo = service.getCitationStoregraphInfo(store,brandname);

															
		json = objectMapper.writeValueAsString(googleGraphInfo);
		return json;

	}
	@RequestMapping(value = "/getCitationStoreHistoryInfo.htm", method = RequestMethod.GET)
	public @ResponseBody
	String getCitationStoreHistoryInfo(@RequestParam("store") String store,@RequestParam("brandname") String brandname,
	Model model) throws IllegalStateException, SystemException,
			JsonGenerationException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		List<CitationStoreHistoryDTO> googleGraphInfo = service.getCitationStoreHistoryInfo(store,brandname);

															
		json = objectMapper.writeValueAsString(googleGraphInfo);
		return json;

	}
	@RequestMapping(value = "/getTotalListingGraphInfo.htm", method = RequestMethod.GET)
	public @ResponseBody
	String getTotalListingGraphInfo(@RequestParam("brandname") String brandname,
			
	Model model) throws IllegalStateException, SystemException,
			JsonGenerationException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		List<AccuracyGraphDTO> googleGraphInfo = service.getTotalListingGraphInfo(brandname);


																		
		json = objectMapper.writeValueAsString(googleGraphInfo);
		return json;

	}
	@RequestMapping(value = "/getCitationBrandGraphInfo.htm", method = RequestMethod.GET)
	public @ResponseBody
	String getCitationGraph(@RequestParam("brandname") String brandname,

	Model model) throws IllegalStateException, SystemException,
			JsonGenerationException, JsonMappingException, IOException {
		String json = null;
		ObjectMapper objectMapper = new ObjectMapper();

		List<CitationGraphDTO> citationgraph = service
				.getCitationBrandGraphInfo(brandname);

		logger.info("Total citaion brands found for Brand are:"
				+ citationgraph.size());

		model.addAttribute("accuracygraph", citationgraph);
		json = objectMapper.writeValueAsString(citationgraph);
		return json;

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
}
