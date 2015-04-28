package com.business.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.UsersDTO;
import com.business.common.util.ControllerUtil;
import com.business.common.util.DateUtil;
import com.business.common.util.LBLConstants;
import com.business.common.util.UploadBeanValidateUtil;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ReportParams;
import com.business.model.pojo.ValueObject;
import com.business.service.BusinessService;
import com.business.service.ReportService;
import com.business.web.bean.LblErrorBean;
import com.business.web.bean.LocalBusinessBean;
import com.business.web.bean.ReportForm;
import com.business.web.bean.UploadBusinessBean;
import com.business.web.bean.UsersBean;

/***
 * @author Vasanth
 * 
 *         BusinessClientController class which gets the data from the View
 *         Layer and sends to the Business Logic Layer and returns respective
 *         results to View Layer
 * 
 */

@Controller
public class BusinessClientController {

	Logger logger = Logger.getLogger(BusinessClientController.class);

	private ControllerUtil controllerUtil = new ControllerUtil();

	@Autowired
	private BusinessService service;
	
	@Autowired
	private ReportService reportService;
	
	/***
	 * 
	 * get dashboard
	 * 
	 * @param model
	 * @param bean
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dashboardClient.htm", method = RequestMethod.GET)
	public String clientDashBoard(Model model, UsersBean bean,LocalBusinessDTO businessDTO,
			HttpSession session,HttpServletRequest request) {

		logger.info("start :: clientDashBoard  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize",errorListSize);
		model.addAttribute("activeListSize",activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request, listOfBusinessInfo);
		dashBoardCommonInfo(model, session,businessDTO);
		logger.info("end :: clientDashBoard  method");
		return "dashboard-client";
	}
	
	
	
	
	/***
	 * 
	 * get clientbusinesslisting.htm
	 * 
	 * @param model
	 * @param bean
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/clientbusinesslisting.htm", method = RequestMethod.GET)
	public String clientbusinesslisting(Model model, UsersBean bean,LocalBusinessDTO businessDTO,
			HttpSession session,HttpServletRequest request) {
		logger.info("start :: clientbusinesslisting  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize",errorListSize);
		model.addAttribute("activeListSize",activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request, listOfBusinessInfo);
		dashBoardCommonInfo(model, session,businessDTO);
		logger.info("end :: clientbusinesslisting  method");
		return "clientbusiness-listings";
	}
	
	/***
	 * 
	 * getClientErrorInfo
	 * 
	 * @param model
	 * @param bean
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/clientListing-error.htm", method = RequestMethod.GET)
	public String getClientUploadErrorInfo(Model model, UsersBean bean,LocalBusinessDTO businessDTO,
			HttpSession session,HttpServletRequest request) {
		logger.info("start :: getClientUploadErrorInfo  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String uploadJobId = (String) session.getAttribute("uploadJobId");
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors(uploadJobId);
		Integer size=0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();				
			if(errorMessage!=null && errorMessage.length()>0){
				List<String> errorMsgs = Arrays.asList(errorMessage.toString().split(","));	
				 size = errorMsgs.size();					
				 size=size-1;
				 logger.info("error count for listing errors::"+size);
				 lblErrorDTO.setErrorMessage(String.valueOf(size));
			}				
		}			
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request, listOfErrorBusinessInfo);
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		logger.info("BusinessInformation size == " + errorListSize);	
		dashBoardCommonInfo(model, session,businessDTO);		
		logger.info("end :: getClientUploadErrorInfo  method");
		return "clietlisting-errors";
	}
	
	
	
	/***
	 * 
	 * getClientErrorInfo
	 * 
	 * @param model
	 * @param bean
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/listingClient-error.htm", method = RequestMethod.GET)
	public String getClientErrorInfo(Model model, UsersBean bean,LocalBusinessDTO businessDTO,
			HttpSession session,HttpServletRequest request) {
		logger.info("start :: getClientErrorInfo  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		Integer size=0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();				
			if(errorMessage!=null && errorMessage.length()>0){
				List<String> errorMsgs = Arrays.asList(errorMessage.toString().split(","));	
				 size = errorMsgs.size();					
				 size=size-1;
				 logger.info("error count for listing errors::"+size);
				 lblErrorDTO.setErrorMessage(String.valueOf(size));
			}				
		}			
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request, listOfErrorBusinessInfo);
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		logger.info("BusinessInformation size == " + errorListSize);	
		dashBoardCommonInfo(model, session,businessDTO);		
		logger.info("end :: getClientErrorInfo  method");
		return "clietlisting-errors";
	}
	/***
	 * This method gets the selected business records and download to
	 * mastertemplate
	 * 
	 * 
	 * @param beanStr
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ClientErrorBusiness", method = RequestMethod.POST)
	public String downloadClientErrorBusiness(@RequestBody String beanStr,
			Model model, HttpServletRequest request, HttpSession session)
			throws Exception {
		logger.info("start :: downloadClientErrorBusiness  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		logger.info("Bean value == " + beanStr);
		String[] split = beanStr.split("&");
		String services = null;
		List<Integer> listIds = new ArrayList<Integer>();

		for (String value : split) {
			if (value.contains("id=")) {
				String id = value.substring("id=".length());
				listIds.add(Integer.parseInt(id));
			}
			
			if (value.contains("serviceName=")) {
				String serviceName = value.substring("serviceName=".length());
				services = serviceName;
				// logger.info(serviceName);
			}
		}
		logger.info("Selected IDs == " + listIds);
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();		
		if (listIds.size() == 0) {
			Integer size=0;
			for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
				String errorMessage = lblErrorDTO.getErrorMessage();				
				if(errorMessage!=null && errorMessage.length()>0){
					List<String> errorMsgs = Arrays.asList(errorMessage.toString().split(","));	
					 size = errorMsgs.size();					
					 size=size-1;
					 logger.info("error count for listing errors::"+size);
					 lblErrorDTO.setErrorMessage(String.valueOf(size));
				}				
			}			
			session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
			controllerUtil.listingAddAttributes(1, model, request, listOfErrorBusinessInfo);
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize", errorListSize);
			logger.info("BusinessInformation size == " + errorListSize);	
			LocalBusinessDTO businessDTO = new LocalBusinessDTO();
			dashBoardCommonInfo(model, session,businessDTO);
			
			return "clietlisting-errors";
		}
		List<LblErrorDTO> specificErrorBusinessInfo = service
				.getSpecificErrorBusinessInfo(listIds);
		model.addAttribute("listOfIncorrectData", specificErrorBusinessInfo);
		model.addAttribute("apiService", services);
		//model.addAttribute("errorRecords", "uploadErrorRecords");
		
		return "excelErrorView";
	}
	
	
	/***
	 * 
	 * get clientUploadDashBoard
	 * 
	 * @param model
	 * @param bean
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/clientViewListing.htm", method = RequestMethod.GET)
	public String clientUploadDashBoard(Model model, UsersBean bean,LocalBusinessDTO businessDTO,
			HttpSession session,HttpServletRequest request) {
		logger.info("start :: clientUploadDashBoard  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String uploadJobId = (String) session.getAttribute("uploadJobId");
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo(uploadJobId);
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize",errorListSize);
		model.addAttribute("activeListSize",activeListSize);
		
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request, listOfBusinessInfo);
		dashBoardCommonInfo(model, session,businessDTO);
		logger.info("end :: clientUploadDashBoard  method");
		return "dashboard-client";
	}
	
	@RequestMapping(value = "/client-dashboard_page.htm", method = RequestMethod.GET)
	public String getBusinessListingByPage(@RequestParam("page") int pageNum,
			Model model, HttpSession session,LocalBusinessDTO businessDTO,HttpServletRequest request) {
		logger.info("start :: getBusinessListingByPage  method");
		logger.info("pageNum :: "+pageNum);
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		List<LocalBusinessDTO> listOfBusinessInfo = (List<LocalBusinessDTO>) session.getAttribute("listOfBusinessInfo");
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize",errorListSize);
		model.addAttribute("activeListSize",activeListSize);
		controllerUtil.listingsAddAttributes(pageNum, model, request, listOfBusinessInfo);
		logger.info("BusinessInformation size == " + listOfBusinessInfo.size());
		dashBoardCommonInfo(model, session,businessDTO);
		logger.info("end :: getBusinessListingByPage  method");
		return "dashboard-client";
	}

	/**
	 * 
	 * This method gets parameters from view layer,sends to the Business Logic
	 * Layer and gets the keyword and returns to the businesslisting's
	 * 
	 * @param model
	 * @param businessDTO
	 * @param request
	 * @param session
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = "/clientBusinessSearch.htm", method = RequestMethod.POST)
	public String clientBusinessSearch(Model model,
			@ModelAttribute("businessSearch") LocalBusinessDTO businessDTO,
			HttpServletRequest request, HttpSession session, UsersBean bean) {
		logger.info("start :: clientBusinessSearch  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String brands = businessDTO.getClient();
		String companyName = businessDTO.getCompanyName();
		String store = businessDTO.getStore();
		String locationPhone = businessDTO.getLocationPhone();
		String locationAddress = businessDTO.getLocationAddress();
		String locationCity = businessDTO.getLocationCity();
		String locationState = businessDTO.getLocationState();
		String locationZipCode = businessDTO.getLocationZipCode();
		Set<LocalBusinessDTO> businesSearchinfo = service.businesSearchInfo(
				brands, companyName, store, locationPhone, locationAddress,
				locationCity, locationState, locationZipCode);
		
		dashBoardCommonInfo(model, session,businessDTO);
		ArrayList<LocalBusinessDTO> listOfBusinessInfo = new ArrayList<LocalBusinessDTO>(businesSearchinfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize",errorListSize);
		model.addAttribute("activeListSize",activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request, listOfBusinessInfo);
		logger.info("end :: clientBusinessSearch  method");
		return "dashboard-client";
	}
	
	
	/**
	 * 
	 * This method gets parameters from view layer,sends to the Business Logic
	 * Layer and gets the keyword and returns to the errorlisting's
	 * 
	 * @param model
	 * @param businessDTO
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/clientErrorSearch.htm", method = RequestMethod.POST)
	public String clientErrorBusinessSearch(Model model,
			@ModelAttribute("businessSearch") LblErrorDTO businessDTO,
			HttpServletRequest request, HttpSession session) {
		logger.info("start :: clientErrorBusinessSearch  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String brands = businessDTO.getClient();
		String companyName = businessDTO.getCompanyName();
		String store = businessDTO.getStore();
		String locationPhone = businessDTO.getLocationPhone();
		String locationAddress = businessDTO.getLocationAddress();
		String locationCity = businessDTO.getLocationCity();
		String locationState = businessDTO.getLocationState();
		String locationZipCode = businessDTO.getLocationZipCode();
		Set<LblErrorDTO> businesSearchinfo = service.errorBusinesSearchInfo(
				brands, companyName, store, locationPhone, locationAddress,
				locationCity, locationState, locationZipCode);
	
		List<LblErrorDTO> listOfBusinessInfo = new ArrayList<LblErrorDTO>(businesSearchinfo);
		Integer size=0;
		for (LblErrorDTO lblErrorDTO : listOfBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();				
			if(errorMessage!=null && errorMessage.length()>0){
				List<String> errorMsgs = Arrays.asList(errorMessage.toString().split(","));	
				 size = errorMsgs.size();					
				 size=size-1;
				 logger.info("error count for listing errors::"+size);
				 lblErrorDTO.setErrorMessage(String.valueOf(size));
			}				
		}			
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request, listOfBusinessInfo);
		int errorListSize = listOfBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		logger.info("BusinessInformation size == " + errorListSize);	
		LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();
		dashBoardCommonInfo(model, session,localBusinessDTO);	
		logger.info("end :: clientErrorBusinessSearch  method");
		return "clietlisting-errors";
	}

	
	
	
	/**
	 *	search brand and date range in listingActivity 
	 * 
	 * @param model
	 * @param businessDTO
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/searchClientListingActivity.htm", method = RequestMethod.POST)
	public String searchListingActivity(Model model,@ModelAttribute("searchBusiness") LocalBusinessDTO businessDTO,
			UsersBean bean , HttpSession session,HttpServletRequest request) {
		logger.info("start :: searchListingActivity  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}		
			
		List<ExportReportDTO> listingActivityInfo = service.getListingActivityInfByBrand(businessDTO);
		Collections.sort(listingActivityInfo);
		dashBoardCommonInfo(model, session,businessDTO);
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();		
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request, listOfBusinessInfo);
			
		model.addAttribute("listingActivityInfo", listingActivityInfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize",errorListSize);
		model.addAttribute("activeListSize",activeListSize);
		businessDTO.setBrands("");
		model.addAttribute("searchBusiness", businessDTO);
		logger.info("end :: searchListingActivity  method");
		return "dashboard-client";
	}
	

	/**
	 * this method gets the selected recrod's in businesslisting's and returns
	 * to clientbusiness-listings-profile
	 * 
	 * @param beanStr
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/editClientBusinessInfo", method = RequestMethod.POST)
	public String editClientBusinessInfo(@RequestBody String beanStr,
			Model model, HttpSession session,HttpServletRequest request) {
		logger.info("start :: editClientBusinessInfo  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String[] split = beanStr.split("&");
		List<Integer> listIds = new ArrayList<Integer>();
		for (String value : split) {
			if (value.contains("id=")) {
				String id = value.substring("id=".length());
				listIds.add(Integer.parseInt(id));
			}
		}
		session.setAttribute("listIds", listIds);
		session.setAttribute("currentClientUpdateIdIndex", 0);
		logger.info("Selected IDs == " + listIds);
		LocalBusinessDTO businessInfo = null;
		if (listIds.size() > 0) {
			businessInfo = service.getBusinessInfo(listIds.get(0));
		} else {
			List<LocalBusinessDTO> listOfBusinessInfo = service
					.getListOfBusinessInfo();			
			dashBoardCommonInfo(model, session,new LocalBusinessDTO());
			List<LblErrorDTO> listOfErrorBusinessInfo = service
					.getListOfErrors();
			int activeListSize = listOfBusinessInfo.size();
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize",errorListSize);
			model.addAttribute("activeListSize",activeListSize);
			session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
			controllerUtil.listingsAddAttributes(1, model, request, listOfBusinessInfo);
			return "dashboard-client";
		}
		model.addAttribute("businessInfo", businessInfo);
		logger.info("end :: editClientBusinessInfo  method");
		return "clientbusiness-listings-profile";
	}
	/**
	 * 
	 * this method gets the error information from errorlisting's and returns
	 * to client business-listing-profile
	 * 
	 * @param model
	 * @param bean
	 * @param req
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/editClientErrorInfo", method = RequestMethod.POST)
	public String editBusinessErrorInfo(Model model,@RequestBody String beanStr,			
			HttpServletRequest req, HttpSession httpSession) {
		logger.info("start :: editBusinessErrorInfo  method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		String[] split = beanStr.split("&");
		List<Integer> listIds = new ArrayList<Integer>();
		for (String value : split) {
			if (value.contains("id=")) {
				String id = value.substring("id=".length());
				listIds.add(Integer.parseInt(id));
			}
		}
		httpSession.setAttribute("listIds", listIds);
		httpSession.setAttribute("currentUpdateIdIndex", 0);
		logger.info("Selected IDs == " + listIds);		
		LblErrorDTO businessInfo=null;
		if(listIds.size()>0){
		 businessInfo = service.getErrorBusinessInfo(listIds.get(0));
		}else{
			List<LblErrorDTO> listOfErrorBusinessInfo = service
					.getListOfErrors();
			Integer size=0;
			for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
				String errorMessage = lblErrorDTO.getErrorMessage();				
				if(errorMessage!=null && errorMessage.length()>0){
					List<String> errorMsgs = Arrays.asList(errorMessage.toString().split(","));	
					 size = errorMsgs.size();					
					 size=size-1;
					 //logger.info("error count for listing errors::"+size);
					 lblErrorDTO.setErrorMessage(String.valueOf(size));
				}				
			}	
			httpSession.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
			controllerUtil.listingAddAttributes(1, model, req, listOfErrorBusinessInfo);
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize", errorListSize);
			logger.info("BusinessInformation size == " + errorListSize);	
			LocalBusinessDTO businessDTO = new LocalBusinessDTO();
			dashBoardCommonInfo(model, httpSession,businessDTO);
			
			return "clietlisting-errors";
		}
		String errorMessage = businessInfo.getErrorMessage();
		List<String> error = Arrays.asList(errorMessage.toString().split(","));
		model.addAttribute("clientErrorListInfo", error);
		model.addAttribute("businessInfo", businessInfo);
		
		UploadBeanValidateUtil util = new UploadBeanValidateUtil();
		  Map<String, String> errorsMap = util.getErrorsMap(error);
		  Set<String> keySet = errorsMap.keySet();
		  for (String field : keySet) {
		   String errorKey = field.concat("_Error");
		   logger.info("Adding error details for Key :" + errorKey +  ", errorMessage: " + errorsMap.get(field));
		   model.addAttribute(errorKey, errorsMap.get(field));
		  }
		  logger.info("end :: editBusinessErrorInfo  method");
		return "clienterror-listings-profile";
	}

	/***
	 * 
	 * This method update the information to database and returns to list page
	 * 
	 * @param model
	 * @param bean
	 * @param result
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateClientBusiness.htm", method = RequestMethod.POST)
	public String updateClientBusinessInfo(Model model,
			@ModelAttribute("businessInfo") LocalBusinessBean bean,
			BindingResult result, HttpServletRequest req, HttpSession session,LocalBusinessDTO businessDTO) {
		logger.info("Start: updateClientBusinessInfo ");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		
		UploadBeanValidateUtil beanValidateUtil = new UploadBeanValidateUtil();
		UploadBusinessBean uploadBusinessBean = new UploadBusinessBean();
		BeanUtils.copyProperties(bean, uploadBusinessBean);
		StringBuffer errorMessage = beanValidateUtil.validateBusinessListInfo(service, uploadBusinessBean);
		
	
		if(errorMessage!=null && errorMessage.length()>0 ){	
			List<String> errorMsgs = Arrays.asList(errorMessage.toString().split(","));		
			model.addAttribute("errorListInfo", errorMsgs); 
			UploadBeanValidateUtil util = new UploadBeanValidateUtil();
			  Map<String, String> errorsMap = util.getErrorsMap(errorMsgs);
			  Set<String> keySet = errorsMap.keySet();
			  for (String field : keySet) {
			   String errorKey = field.concat("_Error");
			  logger.info("Adding error details for Key :" + errorKey +  ", errorMessage: " + errorsMap.get(field));
			   model.addAttribute(errorKey, errorsMap.get(field));
			  }
			return "clientbusiness-listings-profile";
		}
		
		
		String multiUpdatedStr = bean.getMultiUpdateString();
		LocalBusinessDTO businessInfoDto = new LocalBusinessDTO();
		if (bean.getLocationClosed() == null) {
			bean.setLocationClosed("N");
		}
		BeanUtils.copyProperties(bean, businessInfoDto);
		businessInfoDto.setId(bean.getId());
		List<Integer> listIds = (List<Integer>) session.getAttribute("listIds");
		String message = "";
		int index = 1;
		for (Integer id : listIds) {
			if (index++ > 1) {
				businessInfoDto = new LocalBusinessDTO();
				businessInfoDto = service.getBusinessInfo(id);
				String[] multiUpdateArr = multiUpdatedStr.split("\\|");
				for (String str : multiUpdateArr) {
					String[] mulitUpdatedArr = str.split("=");
					try {
						org.apache.commons.beanutils.BeanUtils.setProperty(
								businessInfoDto, mulitUpdatedArr[0],
								mulitUpdatedArr[1]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			logger.info("Update UserId == " + businessInfoDto.getId());
			Integer clientId = businessInfoDto.getClientId();
			String brand=service.getBrandByClientId(clientId);
			businessInfoDto.setClient(brand);
			boolean updateMultiBusinessInfo = service
					.updateBusinessInfo(businessInfoDto);
			logger.info("UpdateBusinessInfo ? =" + updateMultiBusinessInfo);
			if (!updateMultiBusinessInfo) {
				message = message + id + ",";
			}
		}
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize",errorListSize);
		model.addAttribute("activeListSize",activeListSize);		
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, req, listOfBusinessInfo);
		dashBoardCommonInfo(model, session,businessDTO);
		model.addAttribute("message", message == "" ? "Update Success Fully"
				: "Update Fail for Ids " + message);
		logger.info("End: updateClientBusinessInfo ");
		return "dashboard-client";
	}
	
	
	/**
	 * This method save the errorBusiness information to database and returns to
	 * error-list page
	 * 
	 * @param model
	 * @param bean
	 * @param result
	 * @param req
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/saveClientErrorinfo", method = RequestMethod.POST)
	public String saveErrorBusinessInfo(Model model,
			@ModelAttribute("businessInfo") LblErrorBean bean,
			BindingResult result, HttpServletRequest req,
			HttpSession httpSession,LocalBusinessDTO businessDTO) {
		logger.info("start :: saveErrorBusinessInfo  method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		
		UploadBeanValidateUtil beanValidateUtil = new UploadBeanValidateUtil();
		UploadBusinessBean uploadBusinessBean = new UploadBusinessBean();
		BeanUtils.copyProperties(bean, uploadBusinessBean);
		StringBuffer errorMessage = beanValidateUtil.validateUploadBean(service, uploadBusinessBean);
		
		if(errorMessage!=null && errorMessage.length()>0 ){
			List<String> errorMsgs = Arrays.asList(errorMessage.toString().split(","));		
			model.addAttribute("clientErrorListInfo", errorMsgs); 
			
			UploadBeanValidateUtil util = new UploadBeanValidateUtil();
			  Map<String, String> errorsMap = util.getErrorsMap(errorMsgs);
			  Set<String> keySet = errorsMap.keySet();
			  for (String field : keySet) {
			   String errorKey = field.concat("_Error");
			  logger.info("Adding error details for Key :" + errorKey +  ", errorMessage: " + errorsMap.get(field));
			   model.addAttribute(errorKey, errorsMap.get(field));
			  }
			
			return "clienterror-listings-profile";
		}
		String multiUpdatedStr = bean.getMultiUpdateString();
		LblErrorDTO businessInfoDto = new LblErrorDTO();
		if (bean.getLocationClosed() == null) {
			bean.setLocationClosed("N");
		}
		BeanUtils.copyProperties(bean, businessInfoDto);
		businessInfoDto.setId(bean.getId());
		List<Integer> listIds = (List<Integer>) httpSession.getAttribute("listIds");
		String message = "";
		int index = 1;
		for (Integer id : listIds) {
			if (index++ > 1) {
				businessInfoDto = new LblErrorDTO();
				businessInfoDto = service.getErrorBusinessInfo(id);
				String[] multiUpdateArr = multiUpdatedStr.split("\\|");
				for (String str : multiUpdateArr) {
					String[] mulitUpdatedArr = str.split("=");
					try {
						org.apache.commons.beanutils.BeanUtils.setProperty(
								businessInfoDto, mulitUpdatedArr[0],
								mulitUpdatedArr[1]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			/*logger.info("Update UserId == " + businessInfoDto.getId());
			Integer clientId = businessInfoDto.getClientId();
			String brand=service.getBrandByClientId(clientId);
			businessInfoDto.setClient(brand);
			boolean updateMultiBusinessInfo = service
					.saveErrorBusinessInfo(businessInfoDto);*/
			logger.info("Update Client == " + businessInfoDto.getId());
			Integer clientId = businessInfoDto.getClientId();
			String brand = service.getBrandByClientId(clientId);
			businessInfoDto.setClient(brand);
			Integer listingId = service.getListingId(businessInfoDto);
			if (listingId > 0) {
				service.updateErrorBusinessInfo(businessInfoDto, listingId);
			} 
			businessInfoDto.setId(listingId);
			boolean	updateMultiBusinessInfo = service
						.saveErrorBusinessInfo(businessInfoDto);
			
			logger.info("UpdateBusinessInfo ? =" + updateMultiBusinessInfo);
			if (!updateMultiBusinessInfo) {
				message = message + id + ",";
			}
		}
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		Integer size=0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage1 = lblErrorDTO.getErrorMessage();				
			if(errorMessage1!=null && errorMessage1.length()>0){
				List<String> errorMsgs = Arrays.asList(errorMessage1.toString().split(","));	
				 size = errorMsgs.size();					
				 size=size-1;
				 logger.info("error count for listing errors::"+size);
				 lblErrorDTO.setErrorMessage(String.valueOf(size));
			}				
		}	
		httpSession.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, req, listOfErrorBusinessInfo);
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		logger.info("BusinessInformation size == " + errorListSize);	
		dashBoardCommonInfo(model, httpSession,businessDTO);
		logger.info("end :: saveErrorBusinessInfo  method");
		return "clietlisting-errors";
	}
	
	/**
	 * 
	 * this method fetches the exportBusinessInfo and store's the information
	 * 
	 * @param beanStr
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/exportClientBusinessInfo", method = RequestMethod.POST)
	public String exportBusinessInfo(@RequestBody String beanStr, Model model,
			HttpSession session,HttpServletRequest request) {
		logger.info("start :: exportBusinessInfo  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		logger.info("Delete Ids info == " + beanStr);
		String[] split = beanStr.split("&");
		List<Integer> listIds = new ArrayList<Integer>();
		String services = null;
		for (String value : split) {
			if (value.contains("id=")) {
				String id = value.substring("id=".length());
				listIds.add(Integer.parseInt(id));
			}
			if (value.contains("serviceName=")) {
				String serviceName = value.substring("serviceName=".length());
				services = serviceName;
				// logger.info(serviceName);
			}
		}
		logger.info("Selected IDs == " + listIds);
		logger.info("Service Name == " + services);
		List<LocalBusinessDTO> specificBusinessInfo = service
				.getSpecificBusinessInfo(listIds);
		List<ExportReportDTO> exportReports = new ArrayList<ExportReportDTO>();
		Map<String, Integer> exportActivity = new HashMap<String, Integer>();
		for (LocalBusinessDTO localBusinessDTO : specificBusinessInfo) {
			ExportReportDTO exportReportDTO = new ExportReportDTO();
			String brandName = localBusinessDTO.getClient();
			String channelName = service.getBrandChannel(brandName);
			exportReportDTO.setChannelName(channelName);
			exportReportDTO.setBrandName(brandName);
			Integer count = exportActivity.get(brandName); 
			if(count == null)
			{
				count = 1;
				exportActivity.put(brandName, count);
			}
			else{
				count++;
				exportActivity.put(brandName, count);
			}
		}
		Set<String> keySet = exportActivity.keySet();
		
		for (String brand : keySet) {
			
			ExportReportDTO exportReportDTO = new ExportReportDTO();
			String channelName = service.getBrandChannel(brand);
			Date currentDate = new Date();
			exportReportDTO.setDate(new java.sql.Date(currentDate.getTime()));
			exportReportDTO.setPartner(services);
			exportReportDTO.setChannelName(channelName);
			exportReportDTO.setBrandName(brand);
			Integer count = exportActivity.get(brand); 
			exportReportDTO.setCount(count);
			exportReports.add(exportReportDTO);
		}
		
		boolean exportReportInfo = service.exportReportDTO(exportReports);
		if (true) {
			model.addAttribute("listOfAPI", specificBusinessInfo);
			model.addAttribute("apiService", services);
			return "excelView";
		}
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		int activeListSize = specificBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize",errorListSize);
		model.addAttribute("activeListSize",activeListSize);
		session.setAttribute("listOfBusinessInfo", specificBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request, specificBusinessInfo);
		logger.info("end :: exportBusinessInfo  method");
		return "dashboard-client";
	}
	
	/***
	 * This method delete's the selected information and returns to list page
	 * 
	 * @param model
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = "/deleteClientBusinessInfo", method = RequestMethod.POST)
	public String deleteClientBusinessInfo(@RequestBody String beanStr,
			Model model, HttpSession session,LocalBusinessDTO businessDTO,HttpServletRequest request) {
		logger.info("start :: deleteClientBusinessInfo  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		logger.info("Delete Ids info == " + beanStr);
		String[] split = beanStr.split("&");
		List<Integer> listIds = new ArrayList<Integer>();

		for (String value : split) {
			if (value.contains("id=")) {
				String id = value.substring("id=".length());
				listIds.add(Integer.parseInt(id));
			}
		}
		logger.info("Selected IDs == " + listIds);
		service.deleteBusinessInfo(listIds);
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize",errorListSize);
		model.addAttribute("activeListSize",activeListSize);
		dashBoardCommonInfo(model, session,businessDTO);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request, listOfBusinessInfo);
		logger.info("end :: deleteClientBusinessInfo  method");
		return "dashboard-client";
	}
	
	
	/***
	 * This method delete's the selected information and returns to list page
	 * 
	 * @param model
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = "/deleteClientErrorBusinessInfo", method = RequestMethod.POST)
	public String deleteClientErrorBusinessInfo(@RequestBody String beanStr,
			Model model, HttpSession session,LocalBusinessDTO businessDTO,HttpServletRequest request) {
		logger.info("start :: deleteClientErrorBusinessInfo  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		logger.info("Delete Ids info == " + beanStr);
		String[] split = beanStr.split("&");
		List<Integer> listIds = new ArrayList<Integer>();

		for (String value : split) {
			if (value.contains("id=")) {
				String id = value.substring("id=".length());
				listIds.add(Integer.parseInt(id));
			}
		}
		logger.info("Selected IDs == " + listIds);
		service.deleteErrorBusinessInfo(listIds);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		Integer size=0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();				
			if(errorMessage!=null && errorMessage.length()>0){
				List<String> errorMsgs = Arrays.asList(errorMessage.toString().split(","));	
				 size = errorMsgs.size();					
				 size=size-1;
				 logger.info("error count for listing errors::"+size);
				 lblErrorDTO.setErrorMessage(String.valueOf(size));
			}				
		}			
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request, listOfErrorBusinessInfo);
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		logger.info("BusinessInformation size == " + errorListSize);	
		dashBoardCommonInfo(model, session,businessDTO);		
		logger.info("end :: deleteClientErrorBusinessInfo  method");
		return "clietlisting-errors";
	}

	/**
	 * saveUserDetails
	 * 
	 * @param model
	 * @param bean
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/saveClientUser.htm", method = RequestMethod.POST)
	public String saveUserDetails(Model model,LocalBusinessDTO businessDTO,
			@ModelAttribute("adminUser") UsersBean bean,
			HttpServletRequest request, HttpSession session) {
		logger.info("start :: saveUserDetails  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		if (bean.getUserName().length() > 0 && bean.getPassWord().length() > 0) {
			String fullName = bean.getFullName();
			String[] split = fullName.split(" ");
			bean.setLastName(split[split.length - 1]);
			String name = "";
			for (int index = 0; index < split.length - 1; index++) {
				name += split[index];
			}
			bean.setName(name);
			bean.setRoleId(LBLConstants.CLIENT_ADMIN);
			String channelName = (String) session.getAttribute("channelName");
		
			bean.setChannelName(channelName);
			boolean saveManageAccount = service.saveUser(bean);
			List<LocalBusinessDTO> listOfBusinessInfo = service
					.getListOfBusinessInfo();
			List<LblErrorDTO> listOfErrorBusinessInfo = service
					.getListOfErrors();
			int activeListSize = listOfBusinessInfo.size();
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize",errorListSize);
			model.addAttribute("activeListSize",activeListSize);
			dashBoardCommonInfo(model, session,businessDTO);
			session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
			controllerUtil.listingsAddAttributes(1, model, request, listOfBusinessInfo);
			return "dashboard-client";
		}
		dashBoardCommonInfo(model, session,businessDTO);
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize",errorListSize);
		model.addAttribute("activeListSize",activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request, listOfBusinessInfo);
		logger.info("end :: saveUserDetails  method");
		return "dashboard-clientt";
	}

	public void dashBoardCommonInfo(Model model, HttpSession session,LocalBusinessDTO businessDTO) {
		Integer userId = (Integer) session.getAttribute("userID");
		List<UsersDTO> userInfo = service.userInfo(userId);
		UsersDTO usersDTO = userInfo.get(0);
		String name = usersDTO.getName();
		String lastName = usersDTO.getLastName();
		String fullName = name + " " + lastName;
		usersDTO.setFullName(fullName);
		UsersBean bean = new UsersBean();
		BeanUtils.copyProperties(usersDTO, bean);
		List<ExportReportDTO> listingActivityInfo = service
				.getListingActivityInf();
		Collections.sort(listingActivityInfo);
		model.addAttribute("searchBusiness", businessDTO);
		model.addAttribute("adminUser", bean);
		model.addAttribute("listingActivityInfo", listingActivityInfo);
	}

	/**
	 * user returns to clientbusinessListingProfile
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/clientlistings-profile.htm", method = RequestMethod.GET)
	public String businessListingProfile(Model model, HttpSession session) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		return "clientbusiness-listings-profile";
	}

	/**
	 * loginSessionValidation
	 * @param model
	 * @param session
	 * @return
	 */
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
	
	/**
	 * getConvergentToolbox
	 * 
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/clntToolbox", method = RequestMethod.GET)
	public String getConvergentToolbox(Model model,HttpSession httpSession) {
		logger.info("start :: getConvergentToolbox  method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		logger.info("end :: getConvergentToolbox  method");
		return "client-convergent-toolbox";
	}
	
	/**
	 * help
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/clientHelp", method = RequestMethod.GET)
	public String help(Model model,HttpSession httpSession) {
		logger.info("start :: help  method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		logger.info("end :: help  method");
		return "client-help";
	}

	/**
	 * getReports
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/client-reports.htm", method = RequestMethod.GET)
	public String getReports(Model model, HttpServletRequest request, HttpSession session) {
		logger.info("start :: getReports  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		Set<ReportEntity> reports = reportService.getReports();
		for (ReportEntity rpt : reports)
			logger.info("id, params size: " + rpt.getId() + " --- " + rpt.getParams().size());

		ReportEntity selReport = null;
		if (request.getParameter("reportId") == null) { //get the first report
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
		logger.info("end :: getReports  method");
		return "client-reports";
	}

	/**
	 * runReport :shows the upload/export info
	 * 
	 * @param reportForm
	 * @param bindingResult
	 * @param whichPage
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/runClientReport.htm", method = RequestMethod.POST)
	public ModelAndView runReport(@ModelAttribute("reportForm") ReportForm reportForm, BindingResult bindingResult,
										@RequestParam(value = "page", required = false) String whichPage,
										HttpServletRequest request, HttpSession session,Model model) throws Exception {
		logger.info("start :: runReport  method");
		if (!loginSessionValidation(model, session)) {
			ModelAndView mv = new ModelAndView("logout");
			return mv;
		}
		ReportForm rptForm = null;
		ModelAndView mv = new ModelAndView("client-reports");

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
				logger.info("p/v: " + rp.getParamName() + ", " + rp.getParamValue());
			}
		}

		//otherParams are used to pass report params that are not user specified
		Map<String, String> otherParams = new HashMap<String, String>();
		otherParams.put("userName", session.getAttribute("user").toString());
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

        logger.info("noOfRecords, noOfPages: " + noOfRecords + ", " + noOfPages);

        if (! reportRows.isEmpty()) {
        	reportRows.remove(reportRows.size() - 1);
        }

 		logger.info("colHeaders: " + rptForm.getReportColumnHeaders().size());

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
        String reportType = "other";
		if (id == 2) {
			reportType = "UploadReport";
		}
		mv.addObject("reportType", reportType);
        request.setAttribute("currentPageStart", currentPageStart);
        request.setAttribute("currentPageEnd", currentPageEnd);
		mv.addObject("reportForm", rptForm);
		Set<ReportEntity> reports = reportService.getReports();
		mv.addObject("reports", reports);
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
		logger.info("end :: runReport  method");
        return mv;

	}
	
	
	/**
	 * getUploadReportDetails
	 * @param bindingResult
	 * @param brand
	 * @param date
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/getClientUploadReportDetails.htm", method = RequestMethod.GET)
	//@ResponseBody
	public String getUploadReportDetails(@RequestParam("brand") String brand,@RequestParam("date") String date,
			@ModelAttribute("reportForm") ReportForm reportForm, BindingResult bindingResult, HttpSession session,Model model) throws ParseException{
		logger.info("start::getUploadReportDetails method");
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
		logger.info("end :: getUploadReportDetails method");
		return "client-reports";
	}
	
	
	
	
	
	/**
	 * addBusiness
	 * 
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/addClientLocation", method = RequestMethod.GET)
	public String addLocationGet(Model model,HttpSession httpSession) {
		logger.info("start :: addLocationGet method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}		
		
		model.addAttribute("businessInfo", new LocalBusinessDTO());
		logger.info("end :: addLocationGet method");
		return "clientbusiness-listings-profile";
	}
	
	

	/**
	 * 
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/addClientLocation", method = RequestMethod.POST)
	public String addLocation(@ModelAttribute("businessInfo") LocalBusinessBean bean,
			Errors errors, HttpServletRequest req,
			HttpSession httpSession,Model model) {
		logger.info("start :: addLocation method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		
		UploadBeanValidateUtil beanValidateUtil = new UploadBeanValidateUtil();
		UploadBusinessBean uploadBusinessBean = new UploadBusinessBean();
		bean.setActionCode("A");
		BeanUtils.copyProperties(bean, uploadBusinessBean);
		StringBuffer errorMessage = beanValidateUtil.validateUploadBean(service, uploadBusinessBean);
		
		if(errorMessage!=null && errorMessage.length()>0 ){
			List<String> errorMsgs = Arrays.asList(errorMessage.toString().split(","));					
			model.addAttribute("errorListInfo", errorMsgs); 
			 UploadBeanValidateUtil util = new UploadBeanValidateUtil();
			  Map<String, String> errorsMap = util.getErrorsMap(errorMsgs);
			  Set<String> keySet = errorsMap.keySet();
			  for (String field : keySet) {
			   String errorKey = field.concat("_Error");
			   //System.out.println("Adding error details for Key :" + errorKey +  ", errorMessage: " + errorsMap.get(field));
			   model.addAttribute(errorKey, errorsMap.get(field));
			  }
			return "clientbusiness-listings-profile";
		}
				
		if (bean.getLocationClosed() == null) {
			bean.setLocationClosed("N");
		}
		
		LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();
		BeanUtils.copyProperties(uploadBusinessBean, localBusinessDTO);
		

		Integer clientId = localBusinessDTO.getClientId();
		String brand=service.getBrandByClientId(clientId);
		localBusinessDTO.setClient(brand);
		
		String uploadUserName = (String) httpSession.getAttribute("userName");
		String uploadJobId = uploadUserName + "_"
				+ System.currentTimeMillis();
		httpSession.setAttribute("uploadJobId", uploadJobId);
		localBusinessDTO.setUploadJobId(uploadJobId);		
		int id = service.addLocation(localBusinessDTO);
		String message = "Location Not added";
		if (id != 0) {
			message = "Location added successfully";
		}
		dashBoardCommonInfo(model, httpSession,localBusinessDTO);
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize",errorListSize);
		model.addAttribute("activeListSize",activeListSize);
		httpSession.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, req, listOfBusinessInfo);
		logger.info("end :: addLocation method");
		return "dashboard-clientt";
	}

}
