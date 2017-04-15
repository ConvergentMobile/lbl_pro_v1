package com.business.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChangeTrackingDTO;
import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.UsersDTO;
import com.business.common.util.ControllerUtil;
import com.business.common.util.DateUtil;
import com.business.common.util.LBLConstants;
import com.business.common.util.UploadBeanValidateUtil;
import com.business.model.pojo.ChangeTrackingEntity;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ReportParams;
import com.business.model.pojo.ValueObject;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.InventoryManagementService;
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
@SessionAttributes(value = { "locationAddress", "locationCity",
		"locationPhone", "locationState", "locationZipCode", "webAddress",
		"companyName" })
public class BusinessClientController {

	Logger logger = Logger.getLogger(BusinessClientController.class);

	private ControllerUtil controllerUtil = new ControllerUtil();

	@Autowired
	private BusinessService service;

	@Autowired
	private CheckReportService checkservice;
	@Autowired
	private ReportService reportService;
	@Autowired
	private InventoryManagementService inventoryservice;

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
	public String clientDashBoard(Model model, UsersBean bean,
			LocalBusinessDTO businessDTO, HttpSession session,
			HttpServletRequest request) {

		logger.info("start :: clientDashBoard  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		dashBoardCommonInfo(model, session, businessDTO);
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
	public String clientbusinesslisting(Model model, UsersBean bean,
			LocalBusinessDTO businessDTO, HttpSession session,
			HttpServletRequest request) {
		logger.info("start :: clientbusinesslisting  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		dashBoardCommonInfo(model, session, businessDTO);
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
	public String getClientUploadErrorInfo(Model model, UsersBean bean,
			LocalBusinessDTO businessDTO, HttpSession session,
			HttpServletRequest request) {
		logger.info("start :: getClientUploadErrorInfo  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String uploadJobId = (String) session.getAttribute("uploadJobId");
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfErrors(uploadJobId);
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				size = size - 1;
				logger.info("error count for listing errors::" + size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request,
				listOfErrorBusinessInfo);
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		logger.info("BusinessInformation size == " + errorListSize);
		dashBoardCommonInfo(model, session, businessDTO);
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
	public String getClientErrorInfo(Model model, UsersBean bean,
			LocalBusinessDTO businessDTO, HttpSession session,
			HttpServletRequest request) {
		logger.info("start :: getClientErrorInfo  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				size = size - 1;
				logger.info("error count for listing errors::" + size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request,
				listOfErrorBusinessInfo);
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		logger.info("BusinessInformation size == " + errorListSize);
		dashBoardCommonInfo(model, session, businessDTO);
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
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		if (listIds.size() == 0) {
			Integer size = 0;
			for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
				String errorMessage = lblErrorDTO.getErrorMessage();
				if (errorMessage != null && errorMessage.length() > 0) {
					List<String> errorMsgs = Arrays.asList(errorMessage
							.toString().split(","));
					size = errorMsgs.size();
					size = size - 1;
					logger.info("error count for listing errors::" + size);
					lblErrorDTO.setErrorMessage(String.valueOf(size));
				}
			}
			session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
			controllerUtil.listingAddAttributes(1, model, request,
					listOfErrorBusinessInfo);
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize", errorListSize);
			logger.info("BusinessInformation size == " + errorListSize);
			LocalBusinessDTO businessDTO = new LocalBusinessDTO();
			dashBoardCommonInfo(model, session, businessDTO);

			return "clietlisting-errors";
		}
		List<LblErrorDTO> specificErrorBusinessInfo = service
				.getSpecificErrorBusinessInfo(listIds);
		model.addAttribute("listOfIncorrectData", specificErrorBusinessInfo);
		model.addAttribute("apiService", services);
		// model.addAttribute("errorRecords", "uploadErrorRecords");

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
	public String clientUploadDashBoard(Model model, UsersBean bean,
			LocalBusinessDTO businessDTO, HttpSession session,
			HttpServletRequest request) {
		logger.info("start :: clientUploadDashBoard  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String uploadJobId = (String) session.getAttribute("uploadJobId");
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo(uploadJobId);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);

		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		dashBoardCommonInfo(model, session, businessDTO);
		logger.info("end :: clientUploadDashBoard  method");
		return "dashboard-client";
	}

	@RequestMapping(value = "/client-dashboard_page.htm", method = RequestMethod.GET)
	public String getBusinessListingByPage(@RequestParam("page") int pageNum,
			@RequestParam("checkedvalue") boolean checked, Model model,
			HttpSession session, LocalBusinessDTO businessDTO,
			HttpServletRequest request) {
		logger.info("start :: getBusinessListingByPage  method");
		logger.info("pageNum :: " + pageNum);
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		List<LocalBusinessDTO> listOfBusinessInfo = (List<LocalBusinessDTO>) session
				.getAttribute("listOfBusinessInfo");
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		logger.info("BusinessInformation size == " + listOfBusinessInfo.size());
		dashBoardCommonInfo(model, session, businessDTO);
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

		dashBoardCommonInfo(model, session, businessDTO);
		ArrayList<LocalBusinessDTO> listOfBusinessInfo = new ArrayList<LocalBusinessDTO>(
				businesSearchinfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("companyName", companyName);
		model.addAttribute("locationstore", store);
		model.addAttribute("brands", brands);
		model.addAttribute("locationPhone", locationPhone);
		model.addAttribute("locationAddress", locationAddress);
		model.addAttribute("locationState", locationState);
		model.addAttribute("locationCity", locationCity);
		model.addAttribute("locationZipCode", locationZipCode);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
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

		List<LblErrorDTO> listOfBusinessInfo = new ArrayList<LblErrorDTO>(
				businesSearchinfo);
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				size = size - 1;
				logger.info("error count for listing errors::" + size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request,
				listOfBusinessInfo);
		int errorListSize = listOfBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		logger.info("BusinessInformation size == " + errorListSize);
		LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();
		dashBoardCommonInfo(model, session, localBusinessDTO);
		logger.info("end :: clientErrorBusinessSearch  method");
		return "clietlisting-errors";
	}

	/**
	 * search brand and date range in listingActivity
	 * 
	 * @param model
	 * @param businessDTO
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/searchClientListingActivity.htm", method = RequestMethod.POST)
	public String searchListingActivity(Model model,
			@ModelAttribute("searchBusiness") LocalBusinessDTO businessDTO,
			UsersBean bean, HttpSession session, HttpServletRequest request) {
		logger.info("start :: searchListingActivity  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		List<ExportReportDTO> listingActivityInfo = service
				.getListingActivityInfByBrand(businessDTO);
		Collections.sort(listingActivityInfo);
		dashBoardCommonInfo(model, session, businessDTO);
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);

		model.addAttribute("listingActivityInfo", listingActivityInfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
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
			Model model, HttpSession session, HttpServletRequest request) {
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
			dashBoardCommonInfo(model, session, new LocalBusinessDTO());
			List<LblErrorDTO> listOfErrorBusinessInfo = service
					.getListOfErrors();
			int activeListSize = listOfBusinessInfo.size();
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize", errorListSize);
			model.addAttribute("activeListSize", activeListSize);
			session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
			controllerUtil.listingsAddAttributes(1, model, request,
					listOfBusinessInfo);
			return "dashboard-client";
		}
		String locationAddress = businessInfo.getLocationAddress();
		String locationCity = businessInfo.getLocationCity();
		String locationPhone = businessInfo.getLocationPhone();
		String locationState = businessInfo.getLocationState();
		String locationZipCode = businessInfo.getLocationZipCode();
		String webAddress = businessInfo.getWebAddress();
		String companyName = businessInfo.getCompanyName();
		session.setAttribute("locationAddress", locationAddress);
		session.setAttribute("locationCity", locationCity);
		session.setAttribute("locationPhone", locationPhone);
		session.setAttribute("locationState", locationState);
		session.setAttribute("locationZipCode", locationZipCode);
		session.setAttribute("webAddress", webAddress);
		session.setAttribute("companyName", companyName);
		model.addAttribute("businessInfo", businessInfo);
		logger.info("end :: editClientBusinessInfo  method");
		return "clientbusiness-listings-profile";
	}

	/**
	 * 
	 * this method gets the error information from errorlisting's and returns to
	 * client business-listing-profile
	 * 
	 * @param model
	 * @param bean
	 * @param req
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/editClientErrorInfo", method = RequestMethod.POST)
	public String editBusinessErrorInfo(Model model,
			@RequestBody String beanStr, HttpServletRequest req,
			HttpSession httpSession) {
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
		LblErrorDTO businessInfo = null;
		if (listIds.size() > 0) {
			businessInfo = service.getErrorBusinessInfo(listIds.get(0));
		} else {
			List<LblErrorDTO> listOfErrorBusinessInfo = service
					.getListOfErrors();
			Integer size = 0;
			for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
				String errorMessage = lblErrorDTO.getErrorMessage();
				if (errorMessage != null && errorMessage.length() > 0) {
					List<String> errorMsgs = Arrays.asList(errorMessage
							.toString().split(","));
					size = errorMsgs.size();
					size = size - 1;
					// logger.info("error count for listing errors::"+size);
					lblErrorDTO.setErrorMessage(String.valueOf(size));
				}
			}
			httpSession.setAttribute("listOfBusinessInfo",
					listOfErrorBusinessInfo);
			controllerUtil.listingAddAttributes(1, model, req,
					listOfErrorBusinessInfo);
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize", errorListSize);
			logger.info("BusinessInformation size == " + errorListSize);
			LocalBusinessDTO businessDTO = new LocalBusinessDTO();
			dashBoardCommonInfo(model, httpSession, businessDTO);

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
			logger.info("Adding error details for Key :" + errorKey
					+ ", errorMessage: " + errorsMap.get(field));
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
			BindingResult result, HttpServletRequest req, HttpSession session,
			LocalBusinessDTO businessDTO) {
		logger.info("Start: updateClientBusinessInfo ");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		UploadBeanValidateUtil beanValidateUtil = new UploadBeanValidateUtil();
		UploadBusinessBean uploadBusinessBean = new UploadBusinessBean();
		BeanUtils.copyProperties(bean, uploadBusinessBean);
		StringBuffer errorMessage = beanValidateUtil.validateBusinessListInfo(
				service, uploadBusinessBean);

		if (errorMessage != null && errorMessage.length() > 0) {
			List<String> errorMsgs = Arrays.asList(errorMessage.toString()
					.split(","));
			model.addAttribute("errorListInfo", errorMsgs);
			UploadBeanValidateUtil util = new UploadBeanValidateUtil();
			Map<String, String> errorsMap = util.getErrorsMap(errorMsgs);
			Set<String> keySet = errorsMap.keySet();
			for (String field : keySet) {
				String errorKey = field.concat("_Error");
				logger.info("Adding error details for Key :" + errorKey
						+ ", errorMessage: " + errorsMap.get(field));
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
			String companyName = businessInfoDto.getCompanyName();
			String store = businessInfoDto.getStore();
			String locationAddress = businessInfoDto.getLocationAddress();
			String locationCity = businessInfoDto.getLocationCity();
			String locationPhone = businessInfoDto.getLocationPhone();
			String locationState = businessInfoDto.getLocationState();
			String locationZipCode = businessInfoDto.getLocationZipCode();
			String webAddress = businessInfoDto.getWebAddress();
			String brand = service.getBrandByClientId(clientId);
			businessInfoDto.setClient(brand);
			boolean updateMultiBusinessInfo = service
					.updateBusinessInfo(businessInfoDto);
			String sessionAddress = (String) session
					.getAttribute("locationAddress");
			String sessionCity = (String) session.getAttribute("locationCity");
			String sessionPhone = (String) session
					.getAttribute("locationPhone");
			String sessionState = (String) session
					.getAttribute("locationState");
			String sessionZipCode = (String) session
					.getAttribute("locationZipCode");
			String userName = (String) session.getAttribute("userName");
			String sessionwebAddress = (String) session
					.getAttribute("webAddress");
			String sessioncompanyName = (String) session
					.getAttribute("companyName");

			Date date = new Date();
			ChangeTrackingEntity entity = new ChangeTrackingEntity();
			if (updateMultiBusinessInfo == true) {
				if (locationAddress.equalsIgnoreCase(sessionAddress)) {

				} else {
					entity.setLocationAddress(locationAddress);
					entity.setLocationAddressCDate(date);
				}
				if (locationCity.equalsIgnoreCase(sessionCity)) {

				} else {
					entity.setLocationCity(locationCity);
					entity.setLocationCityCDate(date);
				}
				if (locationPhone.equalsIgnoreCase(sessionPhone)) {

				} else {
					entity.setLocationPhone(locationPhone);
					entity.setLocationPhoneCDate(date);
				}
				if (locationZipCode.equalsIgnoreCase(sessionZipCode)) {

				} else {
					entity.setLocationZipCode(locationZipCode);
					entity.setLocationZipCodeCDate(date);
				}
				if (locationState.equalsIgnoreCase(sessionState)) {

				} else {
					entity.setLocationState(locationState);
					entity.setLocationStateCDate(date);
				}
				if (webAddress.equalsIgnoreCase(sessionwebAddress)) {

				} else {
					entity.setWebSite(webAddress);
					entity.setWebSiteCDate(date);
				}
				if (locationAddress.equalsIgnoreCase(sessionAddress)) {

				} else {
					entity.setLocationAddress(locationAddress);
					entity.setLocationAddressCDate(date);
				}
				if (companyName.equalsIgnoreCase(sessioncompanyName)) {
					entity.setBusinessName(companyName);
				} else {
					entity.setBusinessName(companyName);
					entity.setBusinessNameCDate(date);
				}

				entity.setClientId(clientId);
				entity.setStore(store);
				entity.setDate(date);
				entity.setUser(userName);
				boolean changeTrackInfo = service
						.saveChangeTrackingInfo(entity);
			}
			logger.info("UpdateBusinessInfo ? =" + updateMultiBusinessInfo);
			if (!updateMultiBusinessInfo) {
				message = message + id + ",";
			}
		}
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, req, listOfBusinessInfo);
		dashBoardCommonInfo(model, session, businessDTO);
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
			HttpSession httpSession, LocalBusinessDTO businessDTO) {
		logger.info("start :: saveErrorBusinessInfo  method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}

		UploadBeanValidateUtil beanValidateUtil = new UploadBeanValidateUtil();
		UploadBusinessBean uploadBusinessBean = new UploadBusinessBean();
		BeanUtils.copyProperties(bean, uploadBusinessBean);
		StringBuffer errorMessage = beanValidateUtil.validateUploadBean(
				service, uploadBusinessBean);

		if (errorMessage != null && errorMessage.length() > 0) {
			List<String> errorMsgs = Arrays.asList(errorMessage.toString()
					.split(","));
			model.addAttribute("clientErrorListInfo", errorMsgs);

			UploadBeanValidateUtil util = new UploadBeanValidateUtil();
			Map<String, String> errorsMap = util.getErrorsMap(errorMsgs);
			Set<String> keySet = errorsMap.keySet();
			for (String field : keySet) {
				String errorKey = field.concat("_Error");
				logger.info("Adding error details for Key :" + errorKey
						+ ", errorMessage: " + errorsMap.get(field));
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
		List<Integer> listIds = (List<Integer>) httpSession
				.getAttribute("listIds");
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
			/*
			 * logger.info("Update UserId == " + businessInfoDto.getId());
			 * Integer clientId = businessInfoDto.getClientId(); String
			 * brand=service.getBrandByClientId(clientId);
			 * businessInfoDto.setClient(brand); boolean updateMultiBusinessInfo
			 * = service .saveErrorBusinessInfo(businessInfoDto);
			 */
			logger.info("Update Client == " + businessInfoDto.getId());
			Integer clientId = businessInfoDto.getClientId();
			String brand = service.getBrandByClientId(clientId);
			businessInfoDto.setClient(brand);
			Integer listingId = service.getListingId(businessInfoDto);
			if (listingId > 0) {
				service.updateErrorBusinessInfo(businessInfoDto, listingId);
			}
			businessInfoDto.setId(listingId);
			boolean updateMultiBusinessInfo = service
					.saveErrorBusinessInfo(businessInfoDto);

			logger.info("UpdateBusinessInfo ? =" + updateMultiBusinessInfo);
			if (!updateMultiBusinessInfo) {
				message = message + id + ",";
			}
		}
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage1 = lblErrorDTO.getErrorMessage();
			if (errorMessage1 != null && errorMessage1.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage1.toString()
						.split(","));
				size = errorMsgs.size();
				size = size - 1;
				logger.info("error count for listing errors::" + size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		httpSession.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, req,
				listOfErrorBusinessInfo);
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		logger.info("BusinessInformation size == " + errorListSize);
		dashBoardCommonInfo(model, httpSession, businessDTO);
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
	@RequestMapping(value = "/exportClientBusinessInfo.htm", method = RequestMethod.POST)
	public String exportBusinessInfo(@RequestBody String beanStr, Model model,
			HttpSession session, HttpServletRequest request,
			@RequestParam("checkedvalue") boolean checkedValue) {
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
		Map<String, Integer> exportActivity = new HashMap<String, Integer>();
		List<LocalBusinessDTO> specificBusinessInfo = new ArrayList<LocalBusinessDTO>();
		Map<String, List<LocalBusinessDTO>> brandListingsMap = new HashMap<String, List<LocalBusinessDTO>>();
		List<BrandInfoDTO> brandnames = new ArrayList<BrandInfoDTO>();
		List<String> allbrands = new ArrayList<String>();
		Integer role = (Integer) session.getAttribute("roleId");
		if (role == LBLConstants.CLIENT_ADMIN) {

			brandnames = service.getClientbrnds();
		}
		for (BrandInfoDTO brandInfoDTO : brandnames) {
			allbrands.add(brandInfoDTO.getBrandName());
		}
		if (checkedValue == true) {
			for (String client : allbrands) {
				List<LocalBusinessDTO> allLsiting = service
						.getCLientListOfBusinessInfo(client, services);
				brandListingsMap.put(client, allLsiting);
			}

		} else {
			specificBusinessInfo = service.getSpecificBusinessInfo(listIds,
					services);
			
		}

		List<ExportReportDTO> exportReports = new ArrayList<ExportReportDTO>();
	
	
		Map<String, String> brandChannelMap = new HashMap<String, String>();
		if(!brandListingsMap.isEmpty()){
			for (String brand :allbrands) {
				String channelName = service.getBrandChannel(brand);
				brandChannelMap.put(brand, channelName);
				if(allbrands.size()>1){
				List<LocalBusinessDTO> listingsForBrand = brandListingsMap
						.get(brand);
				specificBusinessInfo.addAll(listingsForBrand);
				exportActivity.put(brand, listingsForBrand.size());
				}
				
			}
		}else{

			for (LocalBusinessDTO localBusinessDTO : specificBusinessInfo) {
				ExportReportDTO exportReportDTO = new ExportReportDTO();
				String brandName = localBusinessDTO.getClient();
				String channelName = service.getBrandChannel(brandName);
				exportReportDTO.setChannelName(channelName);
				exportReportDTO.setBrandName(brandName);
				Integer count = exportActivity.get(brandName);
				if (count == null) {
					count = 1;
					exportActivity.put(brandName, count);
				} else {
					count++;
					exportActivity.put(brandName, count);
				}
			}
		}
		
		Set<String> keySet = exportActivity.keySet();
		String uploadUserName = (String) session.getAttribute("userName");
		for (String brand : keySet) {

			ExportReportDTO exportReportDTO = new ExportReportDTO();
			String channelName = service.getBrandChannel(brand);
			Date currentDate = new Date();
			exportReportDTO.setUserName(uploadUserName);
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
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = specificBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", specificBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				specificBusinessInfo);
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
			Model model, HttpSession session, LocalBusinessDTO businessDTO,
			HttpServletRequest request) {
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
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		dashBoardCommonInfo(model, session, businessDTO);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
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
			Model model, HttpSession session, LocalBusinessDTO businessDTO,
			HttpServletRequest request) {
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
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				size = size - 1;
				logger.info("error count for listing errors::" + size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request,
				listOfErrorBusinessInfo);
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		logger.info("BusinessInformation size == " + errorListSize);
		dashBoardCommonInfo(model, session, businessDTO);
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
	public String saveUserDetails(Model model, LocalBusinessDTO businessDTO,
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
			model.addAttribute("errorListSize", errorListSize);
			model.addAttribute("activeListSize", activeListSize);
			dashBoardCommonInfo(model, session, businessDTO);
			session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
			controllerUtil.listingsAddAttributes(1, model, request,
					listOfBusinessInfo);
			return "dashboard-client";
		}
		dashBoardCommonInfo(model, session, businessDTO);
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		logger.info("end :: saveUserDetails  method");
		return "dashboard-client";
	}

	public void dashBoardCommonInfo(Model model, HttpSession session,
			LocalBusinessDTO businessDTO) {
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
	 * 
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
	public String getConvergentToolbox(Model model, HttpSession httpSession) {
		logger.info("start :: getConvergentToolbox  method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		logger.info("end :: getConvergentToolbox  method");
		return "client-convergent-toolbox";
	}

	/**
	 * help
	 * 
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/clientHelp", method = RequestMethod.GET)
	public String help(Model model, HttpSession httpSession) {
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
	public String getReports(Model model, HttpServletRequest request,
			HttpSession session) {
		logger.info("start :: getReports  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		Set<ReportEntity> reports = reportService.getReports();
		for (ReportEntity rpt : reports)
			logger.info("id, params size: " + rpt.getId() + " --- "
					+ rpt.getParams().size());

		ReportEntity selReport = null;
		if (request.getParameter("reportId") == null) { // get the first report
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
	public ModelAndView runReport(
			@ModelAttribute("reportForm") ReportForm reportForm,
			BindingResult bindingResult,
			@RequestParam(value = "page", required = false) String whichPage,
			HttpServletRequest request, HttpSession session, Model model)
			throws Exception {
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
		String brand = request.getParameter("client");
		List<String> listofStores = null;
		ReportEntity reportob = reportForm.getReport();
		Integer reportId = reportob.getId();
		String[] store = request.getParameterValues("StoreNameValues");
		if (reportId == 5 && store != null) {

			listofStores = new ArrayList<String>();
			for (String string : store) {

				listofStores.add(string);

			}
			logger.info("stores from jsp::" + listofStores.size());
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

		if (fromDate != null && toDate == null) { // if no toDate specified, use
													// today
			toDate = Calendar.getInstance().getTime();
		}

		if (toDate != null) {
			params.put("toDate", sdf.format(toDate));
		}

		ReportEntity report = reportForm.getReport();
		if (report.getParams() != null && report.getParams().size() > 0) {
			for (ReportParams rp : report.getParams()) {
				params.put(rp.getParamName(), rp.getParamValue());
				logger.info("p/v: " + rp.getParamName() + ", "
						+ rp.getParamValue());
			}
		}

		// otherParams are used to pass report params that are not user
		// specified
		Map<String, String> otherParams = new HashMap<String, String>();
		otherParams.put("userName", session.getAttribute("user").toString());
		otherParams.put("roleId", session.getAttribute("roleId").toString());
		Integer id = report.getId();
		if (brand == null || brand == "") {
			rptForm = reportService.runReport(id, report.getParams(),
					otherParams, (page - 1) * recordsPerPage, recordsPerPage,
					reportForm.getSortColumn(), reportForm.getSortOrder(),
					fromDate, toDate, brand);
		} else if (brand != null && listofStores != null) {
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
		if (!reportRows.isEmpty()) {
			noOfRecords = (Integer) reportRows.get(reportRows.size() - 1)
					.getField1();
			noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		}

		logger.info("noOfRecords, noOfPages: " + noOfRecords + ", " + noOfPages);

		if (!reportRows.isEmpty()) {
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
		controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("reportType", reportType);
		session.setAttribute("reportForm", rptForm);
		session.setAttribute("reports", reports);
		logger.info("end :: runReport  method");
		return mv;

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
	@RequestMapping(value = "/getClientUploadReportDetails.htm", method = RequestMethod.GET)
	// @ResponseBody
	public String getUploadReportDetails(@RequestParam("brand") String brand,
			@RequestParam("date") String date,
			@ModelAttribute("reportForm") ReportForm reportForm,
			BindingResult bindingResult, HttpSession session, Model model)
			throws ParseException {
		logger.info("start::getUploadReportDetails method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date2 = DateUtil.getDate("MM/dd/yyyy HH:mm:ss",
				sdf.parse(date.replace("-", "/")));
		System.out.println("date :: " + date2);
		java.sql.Timestamp timestamp = new java.sql.Timestamp(date2.getTime());
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		if (brand != null && date != null) {
			businessDTOs = reportService.getUploadReportDetails(brand,
					timestamp);
		}
		reportForm = (ReportForm) session.getAttribute("reportForm");
		model.addAttribute("reportForm", reportForm);
		Set<ReportEntity> reports = (Set<ReportEntity>) session
				.getAttribute("reports");
		model.addAttribute("reports", reports);
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
	public String addLocationGet(Model model, HttpSession httpSession) {
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
	public String addLocation(
			@ModelAttribute("businessInfo") LocalBusinessBean bean,
			Errors errors, HttpServletRequest req, HttpSession httpSession,
			Model model) {
		logger.info("start :: addLocation method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}

		UploadBeanValidateUtil beanValidateUtil = new UploadBeanValidateUtil();
		UploadBusinessBean uploadBusinessBean = new UploadBusinessBean();
		bean.setActionCode("A");
		BeanUtils.copyProperties(bean, uploadBusinessBean);
		StringBuffer errorMessage = beanValidateUtil.validateUploadBean(
				service, uploadBusinessBean);

		if (errorMessage != null && errorMessage.length() > 0) {
			List<String> errorMsgs = Arrays.asList(errorMessage.toString()
					.split(","));
			model.addAttribute("errorListInfo", errorMsgs);
			UploadBeanValidateUtil util = new UploadBeanValidateUtil();
			Map<String, String> errorsMap = util.getErrorsMap(errorMsgs);
			Set<String> keySet = errorsMap.keySet();
			for (String field : keySet) {
				String errorKey = field.concat("_Error");
				// System.out.println("Adding error details for Key :" +
				// errorKey + ", errorMessage: " + errorsMap.get(field));
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
		String brand = service.getBrandByClientId(clientId);
		localBusinessDTO.setClient(brand);

		String uploadUserName = (String) httpSession.getAttribute("userName");
		String uploadJobId = uploadUserName + "_" + System.currentTimeMillis();
		httpSession.setAttribute("uploadJobId", uploadJobId);
		localBusinessDTO.setUploadJobId(uploadJobId);
		int id = service.addLocation(localBusinessDTO);
		String message = "Location Not added";
		if (id != 0) {
			message = "Location added successfully";
		}
		dashBoardCommonInfo(model, httpSession, localBusinessDTO);
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		httpSession.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, req, listOfBusinessInfo);
		logger.info("end :: addLocation method");
		return "dashboard-client";
	}

	@RequestMapping(value = "getClientCountSort.htm", method = RequestMethod.GET)
	public String getCountSort(@RequestParam("flag") String flag, Model model,
			HttpServletRequest request, HttpServletResponse resp,
			HttpSession session) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String flag1 = request.getParameter("flag");
		logger.info("flag:::::::::::::::::::" + flag1);
		logger.info("flag:::::::::::::::::::" + flag);
		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();

		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		LocalBusinessDTO businessDTO = new LocalBusinessDTO();

		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		dashBoardCommonInformation(model, session, businessDTO, flag);
		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}

		model.addAttribute("flagvalue", flag);
		logger.info("end :: clientDashBoard  method");
		return "dashboard-client";
	}

	public void dashBoardCommonInformation(Model model, HttpSession session,
			LocalBusinessDTO businessDTO, String flag) {
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
				.getListingActivityInf(flag);

		model.addAttribute("searchBusiness", businessDTO);
		model.addAttribute("adminUser", bean);
		model.addAttribute("listingActivityInfo", listingActivityInfo);
	}

	@RequestMapping(value = "/listingsajax", method = RequestMethod.GET)
	public @ResponseBody
	String listingsForBrand(
			@RequestParam(value = "categoryId", required = true) String categoryId,
			Model model) throws IllegalStateException, SystemException {

		List<String> storeForBrand = reportService.getStoreForBrand(categoryId);
		logger.info("stores after selecting brand::" + storeForBrand);

		model.addAttribute("storelist", storeForBrand);

		return storeForBrand.toString();
	}

	@RequestMapping(value = "/clientreportxls.htm", method = RequestMethod.GET)
	public String reportdownload(Model model, HttpServletRequest request,
			HttpSession session) throws Exception {

		ReportForm reportForm = (ReportForm) session.getAttribute("reportForm");
		String reportType = (String) session.getAttribute("reportType");
		model.addAttribute("listOfAPI", reportForm);
		model.addAttribute("reportType", reportType);
		return "reportView";

	}

	/*
	 * @RequestMapping(value = "/clientreports-accuracy.htm", method =
	 * RequestMethod.GET) public String getCheckReportListing(Model model,
	 * HttpSession session, HttpServletRequest request) { if
	 * (!loginSessionValidation(model, session)) { return "logout"; }
	 * 
	 * String brandname = request.getParameter("brand");
	 * logger.info("brandname::::::::::::::" + brandname);
	 * logger.info("brandname::::::::::::::" + brandname); List<String>
	 * storeForBrand = reportService.getStoreForBrand(brandname);
	 * model.addAttribute("brandname", brandname);
	 * model.addAttribute("liststoreNames", storeForBrand); List<AccuracyDTO>
	 * accuracyreports = new ArrayList<AccuracyDTO>();
	 * 
	 * model.addAttribute("mode", "mode");
	 * 
	 * for (int i = 0; i <= 10; i++) { String store = storeForBrand.get(i);
	 * 
	 * AccuracyDTO dto = checkservice.getAccuracyListInfo(store, brandname);
	 * 
	 * accuracyreports.add(dto); }
	 * 
	 * int percentageCategory1 = checkservice.getPercentagecategory1(brandname);
	 * int percentageCategory2 = checkservice.getPercentagecategory2(brandname);
	 * int percentageCategory3 = checkservice.getPercentagecategory3(brandname);
	 * int percentageCategory4 = checkservice.getPercentagecategory4(brandname);
	 * int totalStores = checkservice.gettotalStores(brandname);
	 * model.addAttribute("brandname", brandname);
	 * model.addAttribute("totalStores", totalStores);
	 * model.addAttribute("percentageCategory1", percentageCategory1);
	 * model.addAttribute("percentageCategory2", percentageCategory2);
	 * model.addAttribute("percentageCategory3", percentageCategory3);
	 * model.addAttribute("percentageCategory4", percentageCategory4);
	 * model.addAttribute("accuracyreports", accuracyreports); Set<AccuracyDTO>
	 * accuracyreportInfo = new LinkedHashSet<AccuracyDTO>( accuracyreports);
	 * session.setAttribute("accuracyreports", accuracyreportInfo); return
	 * "clientreports-accuracy";
	 * 
	 * }
	 */
	/*
	 * @RequestMapping(value = "/load-moreclientstore.htm", method =
	 * RequestMethod.GET) public String getAccuaracyListing(Model model,
	 * HttpSession session, HttpServletRequest request) { if
	 * (!loginSessionValidation(model, session)) { return "logout"; }
	 * 
	 * String brandname = request.getParameter("brand");
	 * logger.info("brandname::::::::::::::" + brandname); String brandname1 =
	 * request.getParameter("brand"); logger.info("brandname::::::::::::::" +
	 * brandname); List<String> storeForBrand =
	 * reportService.getStoreForBrand(brandname);
	 * model.addAttribute("brandname", brandname);
	 * model.addAttribute("liststoreNames", storeForBrand); List<AccuracyDTO>
	 * accuracyreports = new ArrayList<AccuracyDTO>();
	 * 
	 * model.addAttribute("mode", ""); for (String store : storeForBrand) {
	 * 
	 * AccuracyDTO dto = checkservice.getAccuracyListInfo(store,brandname);
	 * 
	 * accuracyreports.add(dto); }
	 * 
	 * int percentageCategory1 = checkservice.getPercentagecategory1(brandname);
	 * int percentageCategory2 = checkservice.getPercentagecategory2(brandname);
	 * int percentageCategory3 = checkservice.getPercentagecategory3(brandname);
	 * int percentageCategory4 = checkservice.getPercentagecategory4(brandname);
	 * int totalStores = checkservice.gettotalStores(brandname);
	 * model.addAttribute("brandname", brandname);
	 * model.addAttribute("totalStores", totalStores);
	 * model.addAttribute("percentageCategory1", percentageCategory1);
	 * model.addAttribute("percentageCategory2", percentageCategory2);
	 * model.addAttribute("percentageCategory3", percentageCategory3);
	 * model.addAttribute("percentageCategory4", percentageCategory4);
	 * model.addAttribute("accuracyreports", accuracyreports); Set<AccuracyDTO>
	 * accuracyreportInfo = new LinkedHashSet<AccuracyDTO>( accuracyreports);
	 * session.setAttribute("accuracyreports", accuracyreportInfo);
	 * 
	 * return "clientreports-accuracy";
	 * 
	 * }
	 */

	@RequestMapping(value = "/dashClientStoreListingsearch.htm", method = RequestMethod.POST)
	public String getSearchParametersFromCmAdmin(Model model,

	HttpServletRequest request, HttpSession session) {
		logger.info("start ::  getSearchParametersFromDashBoard method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String store = request.getParameter("store");

		logger.info("store:::::::::::::" + store);

		List<LocalBusinessDTO> listOfBusinessInfo = inventoryservice
				.searchBusinessListinginfo(store);

		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		LocalBusinessDTO businessDTO = new LocalBusinessDTO();
		dashBoardCommonInfo(model, session, businessDTO);
		logger.info("end :: clientDashBoard  method");
		return "dashboard-client";
	}

	@RequestMapping(value = "/getClientStoreBasedOnBrandsandStore.htm", method = RequestMethod.GET)
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

	@RequestMapping(value = "/runClientCtrakingReport.htm", method = RequestMethod.POST)
	public ModelAndView runChangeTrackingReport(
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

		ModelAndView mv = new ModelAndView("client-changeTrackingreport");
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

		String[] StoreName = request.getParameterValues("StoreNameValues");

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

	@RequestMapping(value = "/clientrunRenewalReport.htm", method = RequestMethod.POST)
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
		model.addAttribute("uploadReports", new ArrayList<LocalBusinessDTO>());
		ReportForm rptForm = null;
		ModelAndView mv = new ModelAndView("client-reports");
		Set<ReportEntity> reports = reportService.getReports();
		mv.addObject("reports", reports);

		int page = 1;
		int recordsPerPage = Integer.valueOf("1000");

		if (whichPage != null)
			page = Integer.valueOf(whichPage);

		Map<String, Object> params = new HashMap<String, Object>();

		String brand = request.getParameter("client");
		List<String> storeForBrand = service.getStoreForBrand(brand);
		List<String> listofStores = new ArrayList<String>();
		ReportEntity reportob = reportForm.getReport();
		Integer reportId = reportob.getId();

		String StoreName = request.getParameter("storeVal");
		logger.info("StoreName name in reports page ::" + StoreName);
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

		if (StoreName != "") {
			rptForm = reportService.runRenewalReport(fromDate, toDate,
					StoreName,brand);
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
		mv.addObject("liststoreNames", storeForBrand);
		session.setAttribute("reportType", reportType);
		session.setAttribute("reportForm", rptForm);
		session.setAttribute("reports", reports);
		return mv;

	}

	@RequestMapping(value = "/clientDistrreport.htm", method = RequestMethod.GET)
	public String getDistributionReportDetails(
			@RequestParam("brand") String brand,
			@RequestParam("date") String date,
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
		return "client-reports";
	}

	@RequestMapping(value = "clientsortByStore.htm", method = RequestMethod.GET)
	public String getAllBusinessListingsSortBystore(
			@RequestParam("page") int pageNum,
			@RequestParam("cv") boolean checked,
			@RequestParam("flag") String flag,
			@RequestParam("bn") String companyname,
			@RequestParam("b") String brands,
			@RequestParam("la") String locationAddress,
			@RequestParam("s") String store,
			@RequestParam("zl") String locationZipcode,
			@RequestParam("cl") String locationCity,
			@RequestParam("pl") String locationPhone,
			@RequestParam("sl") String locationState,

			Model model, HttpServletRequest request, HttpServletResponse resp,
			HttpSession session, LocalBusinessDTO businessDTO) {

		// StringBuffer fparam=new StringBuffer();
		String flag1 = request.getParameter("flag");
		logger.info("companyname:::::::::::::::::::" + companyname);
		logger.info("brands:::::::::::::::::::" + brands);
		logger.info("locationAddress:::::::::::::::::::" + locationAddress);
		logger.info("store:::::::::::::::::::" + store);
		logger.info("locationZipcode:::::::::::::::::::" + locationZipcode);
		logger.info("locationCity:::::::::::::::::::" + locationCity);
		logger.info("locationPhone:::::::::::::::::::" + locationPhone);
		logger.info("locationState:::::::::::::::::::" + locationState);
		logger.info("flag:::::::::::::::::::" + flag);

		Map<String, String> fmap = new HashMap<String, String>();
		if (!companyname.isEmpty()) {
			fmap.put("companyName", companyname);
		}
		if (!brands.isEmpty()) {
			fmap.put("client", brands);
		}
		if (!locationAddress.isEmpty()) {
			fmap.put("locationAddress", locationAddress);
		}
		if (!store.isEmpty()) {
			fmap.put("store", store);
		}
		if (!locationZipcode.isEmpty()) {
			fmap.put("locationZipCode", locationZipcode);
		}
		if (!locationCity.isEmpty()) {
			fmap.put("locationCity", locationCity);
		}
		if (!locationPhone.isEmpty()) {
			fmap.put("locationPhone", locationPhone);
		}
		if (!locationState.isEmpty()) {
			fmap.put("locationState", locationState);
		}

		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfoByStore(flag, fmap);

		model.addAttribute("allBusinessInfo", listOfBusinessInfo);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		model.addAttribute("companyName", companyname);
		model.addAttribute("locationstore", store);
		model.addAttribute("brands", brands);
		model.addAttribute("locationPhone", locationPhone);
		model.addAttribute("locationAddress", locationAddress);
		model.addAttribute("locationState", locationState);
		model.addAttribute("locationCity", locationCity);
		model.addAttribute("locationZipCode", locationZipcode);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("selectType", "store");
		model.addAttribute("flagvalue", flag);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		logger.info("BusinessInformation size == " + listOfBusinessInfo.size());
		dashBoardCommonInfo(model, session, businessDTO);
		logger.info("end :: getBusinessListingByPage  method");
		return "dashboard-client";

	}

	@RequestMapping(value = "clientsortByBName.htm", method = RequestMethod.GET)
	public String getAllBusinessListingsSortBybusinessname(
			@RequestParam("page") int pageNum,
			@RequestParam("cv") boolean checked,
			@RequestParam("flag") String flag,
			@RequestParam("bn") String companyname,
			@RequestParam("b") String brands,
			@RequestParam("la") String locationAddress,
			@RequestParam("s") String store,
			@RequestParam("zl") String locationZipcode,
			@RequestParam("cl") String locationCity,
			@RequestParam("pl") String locationPhone,
			@RequestParam("sl") String locationState, Model model,
			HttpServletRequest request, HttpServletResponse resp,
			HttpSession session, LocalBusinessDTO businessDTO) {

		// StringBuffer fparam=new StringBuffer();
		String flag1 = request.getParameter("flag");
		logger.info("companyname:::::::::::::::::::" + companyname);
		logger.info("brands:::::::::::::::::::" + brands);
		logger.info("locationAddress:::::::::::::::::::" + locationAddress);
		logger.info("store:::::::::::::::::::" + store);
		logger.info("locationZipcode:::::::::::::::::::" + locationZipcode);
		logger.info("locationCity:::::::::::::::::::" + locationCity);
		logger.info("locationPhone:::::::::::::::::::" + locationPhone);
		logger.info("locationState:::::::::::::::::::" + locationState);
		logger.info("flag:::::::::::::::::::" + flag);

		Map<String, String> fmap = new HashMap<String, String>();
		if (!companyname.isEmpty()) {
			fmap.put("companyName", companyname);
		}
		if (!brands.isEmpty()) {
			fmap.put("client", brands);
		}
		if (!locationAddress.isEmpty()) {
			fmap.put("locationAddress", locationAddress);
		}
		if (!store.isEmpty()) {
			fmap.put("store", store);
		}
		if (!locationZipcode.isEmpty()) {
			fmap.put("locationZipCode", locationZipcode);
		}
		if (!locationCity.isEmpty()) {
			fmap.put("locationCity", locationCity);
		}
		if (!locationPhone.isEmpty()) {
			fmap.put("locationPhone", locationPhone);
		}
		if (!locationState.isEmpty()) {
			fmap.put("locationState", locationState);
		}

		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfoOrederByBusinessName(flag, fmap);

		model.addAttribute("allBusinessInfo", listOfBusinessInfo);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		model.addAttribute("companyName", companyname);
		model.addAttribute("locationstore", store);
		model.addAttribute("brands", brands);
		model.addAttribute("locationPhone", locationPhone);
		model.addAttribute("locationAddress", locationAddress);
		model.addAttribute("locationState", locationState);
		model.addAttribute("locationCity", locationCity);
		model.addAttribute("locationZipCode", locationZipcode);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("selectType", "businessname");
		model.addAttribute("flagvalue", flag);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		logger.info("BusinessInformation size == " + listOfBusinessInfo.size());
		dashBoardCommonInfo(model, session, businessDTO);
		logger.info("end :: getBusinessListingByPage  method");
		return "dashboard-client";
	}

	@RequestMapping(value = "clientsortByAddress.htm", method = RequestMethod.GET)
	public String getAllBusinessListingsSortByaddress(
			@RequestParam("page") int pageNum,
			@RequestParam("cv") boolean checked,
			@RequestParam("flag") String flag,
			@RequestParam("bn") String companyname,
			@RequestParam("b") String brands,
			@RequestParam("la") String locationAddress,
			@RequestParam("s") String store,
			@RequestParam("zl") String locationZipcode,
			@RequestParam("cl") String locationCity,
			@RequestParam("pl") String locationPhone,
			@RequestParam("sl") String locationState, Model model,
			HttpServletRequest request, HttpServletResponse resp,
			HttpSession session, LocalBusinessDTO businessDTO) {

		// StringBuffer fparam=new StringBuffer();
		String flag1 = request.getParameter("flag");
		logger.info("companyname:::::::::::::::::::" + companyname);
		logger.info("brands:::::::::::::::::::" + brands);
		logger.info("locationAddress:::::::::::::::::::" + locationAddress);
		logger.info("store:::::::::::::::::::" + store);
		logger.info("locationZipcode:::::::::::::::::::" + locationZipcode);
		logger.info("locationCity:::::::::::::::::::" + locationCity);
		logger.info("locationPhone:::::::::::::::::::" + locationPhone);
		logger.info("locationState:::::::::::::::::::" + locationState);
		logger.info("flag:::::::::::::::::::" + flag);

		Map<String, String> fmap = new HashMap<String, String>();
		if (!companyname.isEmpty()) {
			fmap.put("companyName", companyname);
		}
		if (!brands.isEmpty()) {
			fmap.put("client", brands);
		}
		if (!locationAddress.isEmpty()) {
			fmap.put("locationAddress", locationAddress);
		}
		if (!store.isEmpty()) {
			fmap.put("store", store);
		}
		if (!locationZipcode.isEmpty()) {
			fmap.put("locationZipCode", locationZipcode);
		}
		if (!locationCity.isEmpty()) {
			fmap.put("locationCity", locationCity);
		}
		if (!locationPhone.isEmpty()) {
			fmap.put("locationPhone", locationPhone);
		}
		if (!locationState.isEmpty()) {
			fmap.put("locationState", locationState);
		}

		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfoOrederByAddress(flag, fmap);

		model.addAttribute("allBusinessInfo", listOfBusinessInfo);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		model.addAttribute("companyName", companyname);
		model.addAttribute("locationstore", store);
		model.addAttribute("brands", brands);
		model.addAttribute("locationPhone", locationPhone);
		model.addAttribute("locationAddress", locationAddress);
		model.addAttribute("locationState", locationState);
		model.addAttribute("locationCity", locationCity);
		model.addAttribute("locationZipCode", locationZipcode);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("selectType", "address");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		logger.info("BusinessInformation size == " + listOfBusinessInfo.size());
		dashBoardCommonInfo(model, session, businessDTO);
		logger.info("end :: getBusinessListingByPage  method");
		return "dashboard-client";
	}

	@RequestMapping(value = "clientsortByCity.htm", method = RequestMethod.GET)
	public String getAllBusinessListingsSortBycity(
			@RequestParam("page") int pageNum,
			@RequestParam("cv") boolean checked,
			@RequestParam("flag") String flag,
			@RequestParam("bn") String companyname,
			@RequestParam("b") String brands,
			@RequestParam("la") String locationAddress,
			@RequestParam("s") String store,
			@RequestParam("zl") String locationZipcode,
			@RequestParam("cl") String locationCity,
			@RequestParam("pl") String locationPhone,
			@RequestParam("sl") String locationState, Model model,
			HttpServletRequest request, HttpServletResponse resp,
			HttpSession session, LocalBusinessDTO businessDTO) {

		// StringBuffer fparam=new StringBuffer();
		String flag1 = request.getParameter("flag");
		logger.info("companyname:::::::::::::::::::" + companyname);
		logger.info("brands:::::::::::::::::::" + brands);
		logger.info("locationAddress:::::::::::::::::::" + locationAddress);
		logger.info("store:::::::::::::::::::" + store);
		logger.info("locationZipcode:::::::::::::::::::" + locationZipcode);
		logger.info("locationCity:::::::::::::::::::" + locationCity);
		logger.info("locationPhone:::::::::::::::::::" + locationPhone);
		logger.info("locationState:::::::::::::::::::" + locationState);
		logger.info("flag:::::::::::::::::::" + flag);

		Map<String, String> fmap = new HashMap<String, String>();
		if (!companyname.isEmpty()) {
			fmap.put("companyName", companyname);
		}
		if (!brands.isEmpty()) {
			fmap.put("client", brands);
		}
		if (!locationAddress.isEmpty()) {
			fmap.put("locationAddress", locationAddress);
		}
		if (!store.isEmpty()) {
			fmap.put("store", store);
		}
		if (!locationZipcode.isEmpty()) {
			fmap.put("locationZipCode", locationZipcode);
		}
		if (!locationCity.isEmpty()) {
			fmap.put("locationCity", locationCity);
		}
		if (!locationPhone.isEmpty()) {
			fmap.put("locationPhone", locationPhone);
		}
		if (!locationState.isEmpty()) {
			fmap.put("locationState", locationState);
		}

		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfoOrederByCity(flag, fmap);

		model.addAttribute("allBusinessInfo", listOfBusinessInfo);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		model.addAttribute("companyName", companyname);
		model.addAttribute("locationstore", store);
		model.addAttribute("brands", brands);
		model.addAttribute("locationPhone", locationPhone);
		model.addAttribute("locationAddress", locationAddress);
		model.addAttribute("locationState", locationState);
		model.addAttribute("locationCity", locationCity);
		model.addAttribute("locationZipCode", locationZipcode);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("selectType", "city");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		logger.info("BusinessInformation size == " + listOfBusinessInfo.size());
		dashBoardCommonInfo(model, session, businessDTO);
		logger.info("end :: getBusinessListingByPage  method");
		return "dashboard-client";
	}

	@RequestMapping(value = "clientsortByState.htm", method = RequestMethod.GET)
	public String getAllBusinessListingsSortBystate(
			@RequestParam("page") int pageNum,
			@RequestParam("cv") boolean checked,
			@RequestParam("flag") String flag,
			@RequestParam("bn") String companyname,
			@RequestParam("b") String brands,
			@RequestParam("la") String locationAddress,
			@RequestParam("s") String store,
			@RequestParam("zl") String locationZipcode,
			@RequestParam("cl") String locationCity,
			@RequestParam("pl") String locationPhone,
			@RequestParam("sl") String locationState, Model model,
			HttpServletRequest request, HttpServletResponse resp,
			HttpSession session, LocalBusinessDTO businessDTO) {

		// StringBuffer fparam=new StringBuffer();
		String flag1 = request.getParameter("flag");
		logger.info("companyname:::::::::::::::::::" + companyname);
		logger.info("brands:::::::::::::::::::" + brands);
		logger.info("locationAddress:::::::::::::::::::" + locationAddress);
		logger.info("store:::::::::::::::::::" + store);
		logger.info("locationZipcode:::::::::::::::::::" + locationZipcode);
		logger.info("locationCity:::::::::::::::::::" + locationCity);
		logger.info("locationPhone:::::::::::::::::::" + locationPhone);
		logger.info("locationState:::::::::::::::::::" + locationState);
		logger.info("flag:::::::::::::::::::" + flag);

		Map<String, String> fmap = new HashMap<String, String>();
		if (!companyname.isEmpty()) {
			fmap.put("companyName", companyname);
		}
		if (!brands.isEmpty()) {
			fmap.put("client", brands);
		}
		if (!locationAddress.isEmpty()) {
			fmap.put("locationAddress", locationAddress);
		}
		if (!store.isEmpty()) {
			fmap.put("store", store);
		}
		if (!locationZipcode.isEmpty()) {
			fmap.put("locationZipCode", locationZipcode);
		}
		if (!locationCity.isEmpty()) {
			fmap.put("locationCity", locationCity);
		}
		if (!locationPhone.isEmpty()) {
			fmap.put("locationPhone", locationPhone);
		}
		if (!locationState.isEmpty()) {
			fmap.put("locationState", locationState);
		}

		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfoOrederByState(flag, fmap);

		model.addAttribute("allBusinessInfo", listOfBusinessInfo);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		model.addAttribute("companyName", companyname);
		model.addAttribute("locationstore", store);
		model.addAttribute("brands", brands);
		model.addAttribute("locationPhone", locationPhone);
		model.addAttribute("locationAddress", locationAddress);
		model.addAttribute("locationState", locationState);
		model.addAttribute("locationCity", locationCity);
		model.addAttribute("locationZipCode", locationZipcode);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("selectType", "state");
		model.addAttribute("flagvalue", flag);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		logger.info("BusinessInformation size == " + listOfBusinessInfo.size());
		dashBoardCommonInfo(model, session, businessDTO);
		logger.info("end :: getBusinessListingByPage  method");
		return "dashboard-client";
	}

	@RequestMapping(value = "clientsortByZip.htm", method = RequestMethod.GET)
	public String getAllBusinessListingsSortByzip(
			@RequestParam("page") int pageNum,
			@RequestParam("cv") boolean checked,
			@RequestParam("flag") String flag,
			@RequestParam("bn") String companyname,
			@RequestParam("b") String brands,
			@RequestParam("la") String locationAddress,
			@RequestParam("s") String store,
			@RequestParam("zl") String locationZipcode,
			@RequestParam("cl") String locationCity,
			@RequestParam("pl") String locationPhone,
			@RequestParam("sl") String locationState, Model model,
			HttpServletRequest request, HttpServletResponse resp,
			HttpSession session, LocalBusinessDTO businessDTO) {

		// StringBuffer fparam=new StringBuffer();
		String flag1 = request.getParameter("flag");
		logger.info("companyname:::::::::::::::::::" + companyname);
		logger.info("brands:::::::::::::::::::" + brands);
		logger.info("locationAddress:::::::::::::::::::" + locationAddress);
		logger.info("store:::::::::::::::::::" + store);
		logger.info("locationZipcode:::::::::::::::::::" + locationZipcode);
		logger.info("locationCity:::::::::::::::::::" + locationCity);
		logger.info("locationPhone:::::::::::::::::::" + locationPhone);
		logger.info("locationState:::::::::::::::::::" + locationState);
		logger.info("flag:::::::::::::::::::" + flag);

		Map<String, String> fmap = new HashMap<String, String>();
		if (!companyname.isEmpty()) {
			fmap.put("companyName", companyname);
		}
		if (!brands.isEmpty()) {
			fmap.put("client", brands);
		}
		if (!locationAddress.isEmpty()) {
			fmap.put("locationAddress", locationAddress);
		}
		if (!store.isEmpty()) {
			fmap.put("store", store);
		}
		if (!locationZipcode.isEmpty()) {
			fmap.put("locationZipCode", locationZipcode);
		}
		if (!locationCity.isEmpty()) {
			fmap.put("locationCity", locationCity);
		}
		if (!locationPhone.isEmpty()) {
			fmap.put("locationPhone", locationPhone);
		}
		if (!locationState.isEmpty()) {
			fmap.put("locationState", locationState);
		}

		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfoOrederByZip(flag, fmap);

		model.addAttribute("allBusinessInfo", listOfBusinessInfo);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		model.addAttribute("companyName", companyname);
		model.addAttribute("locationstore", store);
		model.addAttribute("brands", brands);
		model.addAttribute("locationPhone", locationPhone);
		model.addAttribute("locationAddress", locationAddress);
		model.addAttribute("locationState", locationState);
		model.addAttribute("locationCity", locationCity);
		model.addAttribute("locationZipCode", locationZipcode);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("selectType", "zip");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		logger.info("BusinessInformation size == " + listOfBusinessInfo.size());
		dashBoardCommonInfo(model, session, businessDTO);
		logger.info("end :: getBusinessListingByPage  method");
		return "dashboard-client";
	}

	@RequestMapping(value = "clientsortByPhone.htm", method = RequestMethod.GET)
	public String getAllBusinessListingsSortByphone(
			@RequestParam("page") int pageNum,
			@RequestParam("cv") boolean checked,
			@RequestParam("flag") String flag,
			@RequestParam("bn") String companyname,
			@RequestParam("b") String brands,
			@RequestParam("la") String locationAddress,
			@RequestParam("s") String store,
			@RequestParam("zl") String locationZipcode,
			@RequestParam("cl") String locationCity,
			@RequestParam("pl") String locationPhone,
			@RequestParam("sl") String locationState, Model model,
			HttpServletRequest request, HttpServletResponse resp,
			HttpSession session, LocalBusinessDTO businessDTO) {

		// StringBuffer fparam=new StringBuffer();
		String flag1 = request.getParameter("flag");
		logger.info("companyname:::::::::::::::::::" + companyname);
		logger.info("brands:::::::::::::::::::" + brands);
		logger.info("locationAddress:::::::::::::::::::" + locationAddress);
		logger.info("store:::::::::::::::::::" + store);
		logger.info("locationZipcode:::::::::::::::::::" + locationZipcode);
		logger.info("locationCity:::::::::::::::::::" + locationCity);
		logger.info("locationPhone:::::::::::::::::::" + locationPhone);
		logger.info("locationState:::::::::::::::::::" + locationState);
		logger.info("flag:::::::::::::::::::" + flag);

		Map<String, String> fmap = new HashMap<String, String>();
		if (!companyname.isEmpty()) {
			fmap.put("companyName", companyname);
		}
		if (!brands.isEmpty()) {
			fmap.put("client", brands);
		}
		if (!locationAddress.isEmpty()) {
			fmap.put("locationAddress", locationAddress);
		}
		if (!store.isEmpty()) {
			fmap.put("store", store);
		}
		if (!locationZipcode.isEmpty()) {
			fmap.put("locationZipCode", locationZipcode);
		}
		if (!locationCity.isEmpty()) {
			fmap.put("locationCity", locationCity);
		}
		if (!locationPhone.isEmpty()) {
			fmap.put("locationPhone", locationPhone);
		}
		if (!locationState.isEmpty()) {
			fmap.put("locationState", locationState);
		}

		if (flag != null && flag.length() == 0) {
			flag = "ASC";
		}

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfoOrederByPhone(flag, fmap);

		model.addAttribute("allBusinessInfo", listOfBusinessInfo);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		model.addAttribute("companyName", companyname);
		model.addAttribute("locationstore", store);
		model.addAttribute("brands", brands);
		model.addAttribute("locationPhone", locationPhone);
		model.addAttribute("locationAddress", locationAddress);
		model.addAttribute("locationState", locationState);
		model.addAttribute("locationCity", locationCity);
		model.addAttribute("locationZipCode", locationZipcode);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("selectType", "phone");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		logger.info("BusinessInformation size == " + listOfBusinessInfo.size());
		dashBoardCommonInfo(model, session, businessDTO);
		logger.info("end :: getBusinessListingByPage  method");
		return "dashboard-client";
	}

}
