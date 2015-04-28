package com.business.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
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

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.ForgotPasswordDto;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.UsersDTO;
import com.business.common.util.ControllerUtil;
import com.business.common.util.DateUtil;
import com.business.common.util.LBLConstants;
import com.business.common.util.MailUtil;
import com.business.common.util.SubmissionThread;
import com.business.common.util.SubmissionUtil;
import com.business.common.util.UploadBeanValidateUtil;
import com.business.service.BusinessService;
import com.business.web.bean.LblErrorBean;
import com.business.web.bean.LocalBusinessBean;
import com.business.web.bean.UploadBusinessBean;
import com.business.web.bean.UsersBean;
import com.business.web.validator.LocalBusinessValidator;

/***
 * @author Vasanth
 * 
 *         BusinessController class which gets the data from the View Layer and
 *         sends to the Business Logic Layer and returns respective results to
 *         View Layer
 */

@Controller
@SessionAttributes(value = { "userName", "user", "userID", "roleId",
		"channelName" })
public class BusinessController {

	Logger logger = Logger.getLogger(BusinessController.class);

	@Autowired
	private BusinessService service;

	@Autowired
	private LocalBusinessValidator businessValidator;

	private ControllerUtil controllerUtil = new ControllerUtil();

	/***
	 * SecurityAction
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public String getLogin(Model model) {
		logger.info("getLogin Page");
		model.addAttribute("loginCommand", new UsersBean());
		model.addAttribute("message", "");
		return "login";
	}

	/**
	 * getForGotPage
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/forgot.htm", method = RequestMethod.GET)
	public String getForGotPage(Model model) {
		logger.info("ForGot Password  Page");
		model.addAttribute("loginCommand", new UsersBean());
		return "forgot";
	}

	/**
	 * getPassword
	 * 
	 * @param model
	 * @param userBean
	 * @return
	 */
	@RequestMapping(value = "/forgotPass.htm", method = RequestMethod.POST)
	public String getPassword(Model model, UsersBean userBean) {

		logger.info("start ::getPassword method ");

		String userName = userBean.getUserName();
		UsersDTO usersDTO = service.getForgotPassword(userName);

		String message = "Reset Password Link sent to your email. Please check...";
		String forwardPage = "login";

		if (usersDTO == null || usersDTO.getUserName() == null
				|| usersDTO.getEmail() == null) {
			message = "userName doesn't Exists";
			forwardPage = "forgot";
		} else {
			String email = usersDTO.getEmail();
			byte[] bytes = email.getBytes();
			ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
			forgotPasswordDto.setEmail(email);
			String key = bytes.toString() + "_" + DateUtil.getTimeStamp();
			forgotPasswordDto.setEmailCode(key);
			forgotPasswordDto.setValidOrNot(true);
			service.addOrUpdateForgotPWD(forgotPasswordDto);
			String url = LBLConstants.MAIL_URL + key;
			String text = LBLConstants.MAIL_MESSAGE + url;
			boolean sendMail = MailUtil.sendMail(email, text,
					LBLConstants.MAIL_SUBJECT);
			if (!sendMail) {
				message = "Invalid User or Error Occured try again.";
				logger.info("Invalid User or Error Occured try again.");
				forwardPage = "forgot";
			}
		}
		model.addAttribute("loginCommand", new UsersBean());
		model.addAttribute("message", message);
		logger.info("end ::getPassword method ");
		return forwardPage;
	}

	@RequestMapping(value = "resetPassword", method = RequestMethod.GET)
	public String resetPasswordInit(@RequestParam("key") String tocken,
			Model model) {

		logger.info("start :: reset PasswordInit method");

		UsersBean usersBean = new UsersBean();
		logger.info("tocken :: " + tocken);
		String message = "Token Invalid/Expired";
		String forwardPage = "login";

		if (tocken != null && tocken != "") {
			boolean isValidTocken = service.isValidTocken(tocken);
			if (isValidTocken) {
				message = "Please Set your new Password";
				ForgotPasswordDto forgotPassEntity = service
						.getForgotPassByToken(tocken);
				usersBean.setEmail(forgotPassEntity.getEmail());
				forwardPage = "reset-password";
			}
		}
		model.addAttribute("loginCommand", usersBean);
		model.addAttribute("message", message);
		logger.info("end :: reset PasswordInit method");
		return forwardPage;
	}

	@RequestMapping(value = "resetPassword", method = RequestMethod.POST)
	public String resetPassword(
			@ModelAttribute("loginCommand") UsersBean usersBean,
			BindingResult result, Model model) {
		logger.info("start ::  resetPassword post method");
		String message = "Password NOT changed";
		if (usersBean != null) {
			boolean updateUserPassword = service.updateUserPassword(usersBean);
			if (updateUserPassword) {
				message = "Password changed Successfully";
			}
		}
		model.addAttribute("loginCommand", new UsersBean());
		model.addAttribute("message", message);
		logger.info("end ::  resetPassword post method");
		return "login";
	}

	/**
	 * logout Action
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/logout.htm", method = RequestMethod.GET)
	public String logout(Model model, HttpSession session) {
		logger.info("logout Page");
		model.addAttribute("loginCommand", new UsersBean());
		setAttributesInSession(session);
		return "login";
	}

	/**
	 * setAttributesInSession
	 * 
	 * @param session
	 */
	public void setAttributesInSession(HttpSession session) {
		session.setAttribute("loginName", null);
		session.setAttribute("loginRole", null);
		session.setAttribute("userName", null);
		session.setAttribute("channelName", null);
		session.setAttribute("roleId", null);
		session.setAttribute("userID", null);
		session.invalidate();
	}

	/**
	 * This method fetches the list of business from the database and return's
	 * to view layer
	 * 
	 * @param bean
	 * @param model
	 * @param businessDTO
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/login.htm", method = RequestMethod.POST)
	public String login(@ModelAttribute("loginCommand") UsersBean bean,
			Model model, HttpServletRequest request,
			@ModelAttribute("searchBusiness") LocalBusinessDTO businessDTO,
			HttpSession session) {
		logger.info("start ::  login post method");

		UsersDTO userDTO = service.getUserByUserNameAndPWD(bean.getUserName(),
				bean.getPassWord());
		if (userDTO == null) {
			model.addAttribute("loginCommand", new UsersBean());
			model.addAttribute("invalidUser", "invalidUser");
			setAttributesInSession(session);
			return "login";
		}
		logger.info("Role type == " + userDTO);
		int role = userDTO.getRoleId();
		session.setAttribute("roleId", role);
		Integer userID = userDTO.getUserID();
		session.setAttribute("userID", userID);
		String channelName = userDTO.getChannelName();
		session.setAttribute("channelName", channelName);
		String userName = userDTO.getUserName();
		session.setAttribute("userName", userName);
		String name = userDTO.getName();
		String lastName = userDTO.getLastName();
		String fullName = name + " " + lastName;
		userDTO.setFullName(fullName);
		BeanUtils.copyProperties(userDTO, bean);
		session.setAttribute("loginName", userName);

		if (userDTO.getViewOnly() != null
				&& userDTO.getViewOnly().equalsIgnoreCase("Y")) {
			session.setAttribute("isViewOnly", true);
		} else {
			session.setAttribute("isViewOnly", false);
		}

		if (role == LBLConstants.CLIENT_ADMIN) {
			List<LocalBusinessDTO> listOfBusinessInfo = service
					.getListOfBusinessInfo();
			List<LblErrorDTO> listOfErrorBusinessInfo = service
					.getListOfErrors();
			int activeListSize = listOfBusinessInfo.size();
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize", errorListSize);
			model.addAttribute("activeListSize", activeListSize);
			String user = userDTO.getUserName();
			session.setAttribute("user", user);
			List<ExportReportDTO> listingActivityInfo = service
					.getListingActivityInf();
			Collections.sort(listingActivityInfo);
			session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
			controllerUtil.listingsAddAttributes(1, model, request,
					listOfBusinessInfo);
			model.addAttribute("listingActivityInfo", listingActivityInfo);
			model.addAttribute("adminUser", bean);
			session.setAttribute("loginRole", "clientAdmin");
			logger.info("end ::  login post method");
			return "dashboard-client";
		}

		controllerUtil.getUploadDropDown(model, request, service);
		List<LocalBusinessDTO> businessDTOs = service.getListOfBrands();
		List<ExportReportDTO> exportReportDTOs = service
				.getListingActivityInf();
		Collections.sort(exportReportDTOs);
		List<UsersDTO> usersList = service.getAllUsersList(role);
		model.addAttribute("usersListInfo", usersList);
		model.addAttribute("brandSize", businessDTOs.size());
		model.addAttribute("uName", bean.getUserName());
		model.addAttribute("listingActivityInfo", exportReportDTOs);
		model.addAttribute("brandsInfo", businessDTOs);
		session.setAttribute("loginRole", "other");
		logger.info("end ::  login post method");
		return "dashboard";
	}

	/**
	 * 
	 * saveUserDetails
	 * 
	 * @param model
	 * @param bean
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/saveUser.htm", method = RequestMethod.POST)
	public String saveUserDetails(Model model,
			@ModelAttribute("adminUser") UsersBean bean,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  saveUserDetails method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String viewOnly = request.getParameter("viewonly");
		if (viewOnly != null) {
			bean.setViewOnly("Y");
		} else {
			bean.setViewOnly("N");
		}
		controllerUtil.setUserAndBussinessDataToModel(model, request, service);
		model.addAttribute("brand", new UsersBean());
		controllerUtil.getChannels(model, session, service);
		if (bean.getUserName().length() > 0 && bean.getPassWord().length() > 0) {
			if (bean.getUserID() == null) {
				boolean userExistis = service.isUserExistis(bean.getUserName());
				if (userExistis) {
					model.addAttribute("message", "user is already exists");
					logger.info("user is already exists");
					logger.info("end ::  saveUserDetails method");
					return "manage-account";
				}
			}
			boolean save = service.saveUser(bean);
			controllerUtil.getRoles(model, service);
			controllerUtil.getChannels(model, session, service);
			controllerUtil.setUserAndBussinessDataToModel(model, request,
					service);
			model.addAttribute("adminUser", new UsersBean());
			model.addAttribute("brand", new UsersBean());

		}
		logger.info("end ::  saveUserDetails method");
		return "manage-account";
	}

	/**
	 * 
	 * Link to list of business information page
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/business-listings.htm", method = RequestMethod.GET)
	public String getBusinessListing(Model model, HttpSession session,
			HttpServletRequest request) {
		logger.info("start ::  getBusinessListing method");
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
		controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		logger.info("BusinessInformation size == " + activeListSize);
		logger.info("end ::  getBusinessListing method");
		return "business-listings";
	}

	/**
	 * 
	 * Link to list of listing error information page
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/listing-error.htm", method = RequestMethod.GET)
	public String getListingErrors(Model model, HttpSession session,
			HttpServletRequest request) {
		logger.info("start ::  getListingErrors method");
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
				// logger.info("error count for listing errors::"+size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request,
				listOfErrorBusinessInfo);
		logger.info("BusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  getListingErrors method");
		return "listing-errors";
	}

	/**
	 * 
	 * Link to to list of business information page by uploadId
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/view-listings.htm", method = RequestMethod.GET)
	public String getBusinessListingByuploadId(Model model,
			HttpSession session, HttpServletRequest request) {
		logger.info("start ::  getBusinessListingByuploadId method");
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
		controllerUtil.getUploadDropDown(model, request, service);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		logger.info("BusinessInformation size == " + listOfBusinessInfo.size());
		logger.info("end ::  getBusinessListingByuploadId method");
		return "business-listings";
	}

	/**
	 * 
	 * Link to to list of error information page by uploadId
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/view-errors.htm", method = RequestMethod.GET)
	public String getErrorListingByuploadId(Model model, HttpSession session,
			HttpServletRequest request) {
		logger.info("start ::  getErrorListingByuploadId method");
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
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request,
				listOfErrorBusinessInfo);
		logger.info("end ::  getErrorListingByuploadId method");
		return "listing-errors";
	}

	/**
	 * getBusinessListingByPage
	 * 
	 * @param pageNum
	 * @param model
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/business-listings_page.htm", method = RequestMethod.GET)
	public String getBusinessListingByPage(@RequestParam("page") int pageNum,
			Model model, HttpSession session, HttpServletRequest request) {
		logger.info("start ::  getBusinessListingByPage method");
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
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		logger.info("BusinessInformation size == " + listOfBusinessInfo.size());
		logger.info("end ::  getBusinessListingByPage method");
		return "business-listings";
	}

	/**
	 * This method update the business information to database and returns to
	 * list page
	 * 
	 * @param model
	 * @param bean
	 * @param result
	 * @param req
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/updateBusiness", method = RequestMethod.POST)
	public String updateBusinessInfo(Model model,
			@ModelAttribute("businessInfo") LocalBusinessBean bean,
			BindingResult result, HttpServletRequest req,
			HttpSession httpSession) {
		logger.info("start ::  updateBusinessInfo method");
		if (!loginSessionValidation(model, httpSession)) {
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
			return "business-listings-profile";
		}
		String multiUpdatedStr = bean.getMultiUpdateString();
		LocalBusinessDTO businessInfoDto = new LocalBusinessDTO();
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
			String brand = service.getBrandByClientId(clientId);
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
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		httpSession.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, req, listOfBusinessInfo);

		model.addAttribute("message", message == "" ? "Update Success Fully"
				: "Update Fail for Ids " + message);
		logger.info("end ::  updateBusinessInfo method");
		return "business-listings";
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
	@RequestMapping(value = "/updateErrorBusiness.htm", method = RequestMethod.POST)
	public String saveErrorBusinessInfo(Model model,
			@ModelAttribute("businessInfo") LblErrorBean bean,
			BindingResult result, HttpServletRequest req,
			HttpSession httpSession) {
		logger.info("start ::  saveErrorBusinessInfo method");
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
			model.addAttribute("errorListInfo", errorMsgs);
			UploadBeanValidateUtil util = new UploadBeanValidateUtil();
			Map<String, String> errorsMap = util.getErrorsMap(errorMsgs);
			Set<String> keySet = errorsMap.keySet();
			for (String field : keySet) {
				String errorKey = field.concat("_Error");
				System.out.println("Adding error details for Key :" + errorKey
						+ ", errorMessage: " + errorsMap.get(field));
				model.addAttribute(errorKey, errorsMap.get(field));
			}
			return "error-listings-profile";
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
			 * logger.info("Update Client == " + businessInfoDto.getId());
			 * Integer clientId = businessInfoDto.getClientId(); String
			 * brand=service.getBrandByClientId(clientId);
			 * businessInfoDto.setClient(brand);
			 */
			UploadBusinessBean lblBean = new UploadBusinessBean();
			BeanUtils.copyProperties(businessInfoDto, lblBean);

			StringBuffer error = beanValidateUtil.validateUploadBean(service,
					lblBean);
			logger.info("Validation result is: " + error.toString());
			if (error != null && error.length() == 0) {

				logger.info("Update Client == " + businessInfoDto.getId());
				Integer clientId = businessInfoDto.getClientId();
				String brand = service.getBrandByClientId(clientId);
				businessInfoDto.setClient(brand);
				Integer listingId = service.getListingId(businessInfoDto);

				if (listingId > 0) {
					// businessInfoDto.setId(listingId);
					service.updateErrorBusinessInfo(businessInfoDto, listingId);
				}
				// businessInfoDto.setId(listingId);
				boolean updateMultiBusinessInfo = service
						.saveErrorBusinessInfo(businessInfoDto);

				logger.info("UpdateBusinessInfo ? =" + updateMultiBusinessInfo);
				if (!updateMultiBusinessInfo) {
					message = message + id + ",";
				}
			} else {
				service.updateListingError(businessInfoDto);
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
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		httpSession.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, req,
				listOfErrorBusinessInfo);
		logger.info("BusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  saveErrorBusinessInfo method");
		return "listing-errors";
	}

	/**
	 * 
	 * This method delete's the selected business information and returns to
	 * list page
	 * 
	 * @param beanStr
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteBusinessInfo", method = RequestMethod.POST)
	public String deleteBusinessInfo(@RequestBody String beanStr, Model model,
			HttpSession session, HttpServletRequest request) {
		logger.info("start ::  deleteBusinessInfo method");
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
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		logger.info("end ::  deleteBusinessInfo method");
		return "business-listings";
	}

	/**
	 * 
	 * This method delete's the selected business information and returns to
	 * list page
	 * 
	 * @param beanStr
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteErrorBusinessInfo", method = RequestMethod.POST)
	public String deleteErrorBusinessInfo(@RequestBody String beanStr,
			Model model, HttpSession session, HttpServletRequest request) {
		logger.info("start ::  deleteErrorBusinessInfo method");
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
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request,
				listOfErrorBusinessInfo);
		logger.info("errorBusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  deleteErrorBusinessInfo method");
		return "listing-errors";
	}

	/**
	 * 
	 * This method takes the selected information from businesslisting's and
	 * export to selected partner
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/bulkExports", method = RequestMethod.GET)
	public String exportInfoFromUploadExport(Model model,
			HttpServletRequest request, HttpSession session) {

		logger.info("start ::  exportInfoFromUploadExport method");

		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String templateName = request.getParameter("service");
		List<LocalBusinessDTO> specificBusinessInfo = service
				.getListOfBusinessInfo();

		List<ExportReportDTO> exportReports = new ArrayList<ExportReportDTO>();
		Map<String, Integer> exportActivity = new HashMap<String, Integer>();
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
		Set<String> keySet = exportActivity.keySet();

		for (String brand : keySet) {

			ExportReportDTO exportReportDTO = new ExportReportDTO();
			String channelName = service.getBrandChannel(brand);
			Date currentDate = new Date();
			exportReportDTO.setDate(new java.sql.Date(currentDate.getTime()));
			exportReportDTO.setPartner(templateName);
			exportReportDTO.setChannelName(channelName);
			exportReportDTO.setBrandName(brand);
			Integer count = exportActivity.get(brand);
			exportReportDTO.setCount(count);
			exportReports.add(exportReportDTO);
		}

		boolean exportReportInfo = service.exportReportDTO(exportReports);

		if (true) {
			model.addAttribute("listOfAPI", specificBusinessInfo);
			model.addAttribute("apiService", templateName);
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
		logger.info("end ::  exportInfoFromUploadExport method");
		return "business-listings";
	}

	/**
	 * 
	 * this method get the DistributorInformtion In DashBoard and return's in
	 * popup
	 * 
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/distrBrands", method = RequestMethod.GET)
	public String getDistributorInfoInDashBoard(Model model,
			HttpServletRequest req, HttpSession session) {
		logger.info("start ::  getDistributorInfoInDashBoard method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String brandName = req.getParameter("distr");
		BrandInfoDTO brandNameInfo = service.getBrandInfoByBrandName(brandName);
		Integer locationUploadedCount = service
				.locationUploadedCount(brandName);
		List<UsersBean> listOfUserBean = new ArrayList<UsersBean>();
		UsersBean bean = new UsersBean();
		Integer chalID = brandNameInfo.getChannelID();
		ChannelNameDTO getchannelInfo = service.getchannelInfo(chalID);
		bean.setChannelName(getchannelInfo.getChannelName());
		bean.setBrandName(brandNameInfo.getBrandName());
		Date startDate = brandNameInfo.getStartDate();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		bean.setStartDate(sdf.format(startDate));
		bean.setLocationsInvoiced(brandNameInfo.getLocationsInvoiced());
		bean.setLocationsUploaded(brandNameInfo.getLocationsInvoiced());
		bean.setSubmisions(brandNameInfo.getSubmisions());
		bean.setClientId(brandNameInfo.getClientId());
		listOfUserBean.add(bean);
		model.addAttribute("locationsUploaded", locationUploadedCount);
		model.addAttribute("brandNameInfo", listOfUserBean);
		model.addAttribute("showDistrpopup", "yes");
		List<LocalBusinessDTO> listOfBrandsInfo = service.getListOfBrands();
		List<ExportReportDTO> listingActivityInfo = service
				.getListingActivityInf();
		Collections.sort(listingActivityInfo);
		List<UsersDTO> usersList = service.getAllUsersList((Integer) session
				.getAttribute("roleId"));
		model.addAttribute("usersListInfo", usersList);
		model.addAttribute("brandSize", listOfBrandsInfo.size());
		model.addAttribute("listingActivityInfo", listingActivityInfo);
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		logger.info("end ::  getDistributorInfoInDashBoard method");
		return "dashboard";
	}

	/**
	 * this method get the selected brand and returns to business listing's
	 * 
	 * @param model
	 * @param bean
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/editbrandInfo", method = RequestMethod.GET)
	public String brandInfoTOBusinessListing(Model model,
			@ModelAttribute("searchBusiness") LocalBusinessBean bean,
			HttpServletRequest req, HttpSession session) {
		logger.info("start ::  brandInfoTOBusinessListing method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String brands = req.getParameter("brand");
		String brands2 = bean.getBrands();
		List<LocalBusinessDTO> businessInfo = service.getBrandsInfo(brands);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = businessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", businessInfo);
		controllerUtil.listingsAddAttributes(1, model, req, businessInfo);
		model.addAttribute("allBusinessInfo", businessInfo);
		model.addAttribute("businessSearch", new LocalBusinessDTO());
		logger.info("end ::  brandInfoTOBusinessListing method");
		return "business-listings";
	}

	/**
	 * 
	 * this method gets the error information from errorlisting's and returns to
	 * business-listing-profile
	 * 
	 * @param model
	 * @param bean
	 * @param req
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/editErrorBusinessInfo", method = RequestMethod.POST)
	public String editBusinessErrorInfo(Model model,
			@RequestBody String beanStr, HttpServletRequest req,
			HttpSession httpSession) {
		logger.info("start ::  editBusinessErrorInfo method");
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
					logger.info("error count for listing errors::" + size);
					lblErrorDTO.setErrorMessage(String.valueOf(size));
				}
			}
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize", errorListSize);
			httpSession.setAttribute("listOfBusinessInfo",
					listOfErrorBusinessInfo);
			controllerUtil.listingAddAttributes(1, model, req,
					listOfErrorBusinessInfo);
			logger.info("errorBusinessInformation size:  "
					+ listOfErrorBusinessInfo.size());
			return "listing-errors";
		}

		HttpSession session = req.getSession();
		session.setAttribute("userID", businessInfo.getId());
		String errorMessage = businessInfo.getErrorMessage();
		List<String> errors = Arrays.asList(errorMessage.toString().split(","));
		model.addAttribute("errorListInfo", errors);
		model.addAttribute("businessInfo", businessInfo);

		UploadBeanValidateUtil util = new UploadBeanValidateUtil();
		Map<String, String> errorsMap = util.getErrorsMap(errors);
		Set<String> keySet = errorsMap.keySet();
		for (String field : keySet) {
			String errorKey = field.concat("_Error");
			System.out.println("Adding error details for Key :" + errorKey
					+ ", errorMessage: " + errorsMap.get(field));
			model.addAttribute(errorKey, errorsMap.get(field));
		}
		logger.info("end ::  editBusinessErrorInfo method");
		return "error-listings-profile";

	}

	/**
	 * 
	 * this method gets the selected recrod's in businesslisting's and returns
	 * to business-listing-profile
	 * 
	 * @param beanStr
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/editBusinessInfo", method = RequestMethod.POST)
	public String editBusinessInfo(@RequestBody String beanStr, Model model,
			HttpSession httpSession, HttpServletRequest request) {
		logger.info("start ::  editBusinessInfo method");
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
		LocalBusinessDTO businessInfo = null;
		if (listIds.size() > 0) {
			businessInfo = service.getBusinessInfo(listIds.get(0));
		} else {
			List<LocalBusinessDTO> listOfBusinessInfo = service
					.getListOfBusinessInfo();
			List<LblErrorDTO> listOfErrorBusinessInfo = service
					.getListOfErrors();
			int activeListSize = listOfBusinessInfo.size();
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize", errorListSize);
			model.addAttribute("activeListSize", activeListSize);
			httpSession.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
			controllerUtil.listingsAddAttributes(1, model, request,
					listOfBusinessInfo);
			model.addAttribute("allBusinessInfo", listOfBusinessInfo);
			model.addAttribute("businessSearch", new LocalBusinessDTO());
			return "business-listings";
		}
		model.addAttribute("businessInfo", businessInfo);
		logger.info("end ::  editBusinessInfo method");
		return "business-listings-profile";
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
	@RequestMapping(value = "/exportBusinessInfo", method = RequestMethod.POST)
	public String exportBusinessInfo(@RequestBody String beanStr, Model model,
			HttpSession session, HttpServletRequest request) {
		logger.info("start ::  exportBusinessInfo method");
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
		int exportSize = specificBusinessInfo.size();
		List<ExportReportDTO> exportReports = new ArrayList<ExportReportDTO>();
		Map<String, Integer> exportActivity = new HashMap<String, Integer>();
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
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = exportSize;
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", specificBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				specificBusinessInfo);
		logger.info("end ::  exportBusinessInfo method");
		return "business-listings";
	}

	/**
	 * 
	 * This method gets parameters from view layer,sends to the Business Logic
	 * Layer and gets the keyword and returns to the businesslisting's
	 * 
	 * 
	 * @param model
	 * @param businessDTO
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dashsearch.htm", method = RequestMethod.POST)
	public String getSearchParametersFromDashBoard(Model model,
			@ModelAttribute("searchBusiness") LocalBusinessDTO businessDTO,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  getSearchParametersFromDashBoard method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String companyName = businessDTO.getCompanyName();
		String store = businessDTO.getStore();
		String locationPhone = businessDTO.getLocationPhone();
		String brands = businessDTO.getClient();
		Set<LocalBusinessDTO> searchBusinessinfo = service.searchBusinessinfo(
				companyName, store, locationPhone, brands);
		List<LocalBusinessDTO> listOfBusinessInfo = new ArrayList<LocalBusinessDTO>(
				searchBusinessinfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		logger.info("end ::  getSearchParametersFromDashBoard method");
		return "business-listings";
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
	 * @return
	 */
	@RequestMapping(value = "/search.htm", method = RequestMethod.POST)
	public String businessSearch(Model model,
			@ModelAttribute("businessSearch") LocalBusinessDTO businessDTO,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  BusinessSearch method");
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

		List<LocalBusinessDTO> listOfBusinessInfo = new ArrayList<LocalBusinessDTO>(
				businesSearchinfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		logger.info("end ::  BusinessSearch method");
		return "business-listings";
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
	@RequestMapping(value = "/errorSearch.htm", method = RequestMethod.POST)
	public String errorBusinessSearch(Model model,
			@ModelAttribute("businessSearch") LblErrorDTO businessDTO,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  errorBusinessSearch method");
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
		int errorListSize = listOfBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request,
				listOfBusinessInfo);
		logger.info("end ::  errorBusinessSearch method");
		return "listing-errors";
	}

	/**
	 * get dashBoard
	 * 
	 * @param model
	 * @param businessDTO
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dash-board.htm", method = RequestMethod.GET)
	public String dashBoard(Model model,
			@ModelAttribute("searchBusiness") LocalBusinessDTO businessDTO,
			HttpSession session, HttpServletRequest request) {
		logger.info("start ::  dashBoard method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		controllerUtil.getUploadDropDown(model, request, service);
		List<LocalBusinessDTO> listOfBrandsInfo = service.getListOfBrands();
		List<ExportReportDTO> listingActivityInfo = service
				.getListingActivityInf();
		Collections.sort(listingActivityInfo);
		List<UsersDTO> usersList = service.getAllUsersList((Integer) session
				.getAttribute("roleId"));
		model.addAttribute("usersListInfo", usersList);
		model.addAttribute("brandSize", listOfBrandsInfo.size());
		model.addAttribute("listingActivityInfo", listingActivityInfo);
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		logger.info("end ::  dashBoard method");
		return "dashboard";
	}

	/**
	 * 
	 * gets search user from dashboard and returns to view layer
	 * 
	 * @param model
	 * @param businessDTO
	 * @param bean
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/dashUsersSearch.htm", method = RequestMethod.POST)
	public String dashboardUserSearch(Model model,
			@ModelAttribute("searchBusiness") LocalBusinessDTO businessDTO,
			UsersBean bean, HttpSession session) {
		logger.info("start ::  dashboardUserSearch method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String userName = bean.getSearchName();
		List<UsersDTO> searchUser = service.getUserByUserName(userName);
		List<LocalBusinessDTO> listOfBrandsInfo = service.getListOfBrands();
		List<ExportReportDTO> listingActivityInfo = service
				.getListingActivityInf();
		Collections.sort(listingActivityInfo);
		model.addAttribute("brandSize", listOfBrandsInfo.size());
		model.addAttribute("listingActivityInfo", listingActivityInfo);
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		model.addAttribute("usersListInfo", searchUser);
		logger.info("end ::  dashboardUserSearch method");
		return "dashboard";
	}

	/**
	 * this will return the user to manage-account
	 * 
	 * @param model
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/manage-account.htm", method = RequestMethod.GET)
	public String ManageAccount(Model model, HttpSession session,
			HttpServletRequest request) {
		logger.info("start ::  ManageAccount method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		controllerUtil.getRoles(model, service);
		controllerUtil.getChannels(model, session, service);
		controllerUtil.setUserAndBussinessDataToModel(model, request, service);
		model.addAttribute("adminUser", new UsersBean());
		model.addAttribute("brand", new UsersBean());
		logger.info("end ::  ManageAccount method");
		return "manage-account";
	}

	/***
	 * this method gets the user and return's to manage-account
	 * 
	 * @param model
	 * @param bean
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/userInfo.htm", method = RequestMethod.POST)
	public String dashboardToManageAccount(Model model,
			@ModelAttribute("searchBusiness") UsersBean bean,
			HttpSession session, HttpServletRequest request) {
		logger.info("start ::  dashboardToManageAccount method");
		Integer userID = bean.getUserID();
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		controllerUtil.getRoles(model, service);
		int role = (Integer) session.getAttribute("roleId");
		List<UsersDTO> usersList = service.getAllUsersList(role);
		model.addAttribute("usersListInfo", usersList);
		List<UsersDTO> userInfo = service.userInfo(userID);
		if (userInfo.isEmpty()
				|| (userInfo.get(0).getRoleId() == LBLConstants.CONVERGENT_MOBILE_ADMIN && !(role == LBLConstants.CONVERGENT_MOBILE_ADMIN))) {
			return "logout";
		}
		controllerUtil.setUserAndBussinessDataToModel(model, request, service);
		List<String> channelNames = service.getChannelsBasedOnUser(userID);
		model.addAttribute("channels", channelNames);
		UsersDTO usersDTO = userInfo.get(0);
		BeanUtils.copyProperties(usersDTO, bean);
		model.addAttribute("adminUser", bean);
		model.addAttribute("brand", new UsersBean());
		logger.info("end ::  dashboardToManageAccount method");
		return "manage-account";
	}

	/**
	 * this will return user to upload-export
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/upload-export.htm", method = RequestMethod.GET)
	public String uploadExport(Model model, HttpSession session,
			HttpServletRequest request) {
		logger.info("start ::  uploadExport method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		controllerUtil.getUploadDropDown(model, request, service);
		logger.info("end ::  uploadExport method");
		return "upload-export";
	}

	/**
	 * 
	 * this method get the search parameter and return's brand info
	 * 
	 * @param model
	 * @param bean
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/brandsSearch.htm", method = RequestMethod.POST)
	public String brandDetailSearchInManageAccount(Model model,
			@ModelAttribute("brand") UsersBean bean,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  brandDetailSearchInManageAccount method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		model.addAttribute("bandNameSearch", "yes");
		controllerUtil.getRoles(model, service);
		controllerUtil.setUserAndBussinessDataToModel(model, request, service);
		controllerUtil.getChannels(model, session, service);
		BrandInfoDTO brandNameInfo = service.getBrandInfoByBrandName(bean
				.getBrandName());
		Integer locationUploadedCount = service.locationUploadedCount(bean
				.getBrandName());
		if (brandNameInfo != null) {
			Integer chalID = brandNameInfo.getChannelID();
			ChannelNameDTO getchannelInfo = service.getchannelInfo(chalID);
			bean.setChannelName(getchannelInfo.getChannelName());
			bean.setBrandName(brandNameInfo.getBrandName());
			Date startDate = brandNameInfo.getStartDate();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			bean.setStartDate(sdf.format(startDate));
			bean.setLocationsInvoiced(brandNameInfo.getLocationsInvoiced());
			bean.setLocationsUploaded(brandNameInfo.getLocationsInvoiced());
			bean.setClientId(brandNameInfo.getClientId());
			bean.setSubmisions(brandNameInfo.getSubmisions());
			model.addAttribute("locationsUploaded", locationUploadedCount);
			logger.info("end ::  brandDetailSearchInManageAccount method");
			return "manage-account";
		} else {
			model.addAttribute("message", "Brand Name is not available ....");
			bean.setBrandName(null);
			logger.info("end ::  brandDetailSearchInManageAccount method");
			return "manage-account";
		}
	}

	/**
	 * addBrands to the db
	 * 
	 * @param model
	 * @param bean
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/brandsSubmit.htm", method = RequestMethod.POST)
	public String addBrands(Model model,
			@ModelAttribute("brand") UsersBean bean, HttpServletRequest request)
			throws ParseException {
		logger.info("start ::  addBrands method");
		HttpSession session = request.getSession();
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		controllerUtil.getRoles(model, service);
		controllerUtil.getChannels(model, session, service);
		controllerUtil.setUserAndBussinessDataToModel(model, request, service);
		model.addAttribute("adminUser", new UsersBean());
		model.addAttribute("brand", new UsersBean());
		String message = "";
		Date startDateValue = DateUtil.getCurrentDate("MM/dd/yyyy HH:mm:ss");
		if (bean.getChannelName().trim().length() > 0
				&& bean.getBrandName().trim().length() > 0) {
			message = controllerUtil.saveOrUpdateBrandAndChannel(bean, message,
					startDateValue, service);
		} else if (bean.getChannelName().trim().length() > 0) {
			controllerUtil.saveOrUpdateChannel(bean, startDateValue, message,
					service);
		} else if (bean.getBrandName().trim().length() > 0) {
			Integer channelID = service.getChannelIdByName("Convergent Mobile");
			if (channelID == 0) {
				channelID = service.saveChannel("Convergent Mobile",
						startDateValue);
			}
			message = controllerUtil.saveOrUpdateBrand(bean, message,
					startDateValue, channelID, service);
		}
		model.addAttribute("message", message);
		logger.info("end ::  addBrands method");
		return "manage-account";
	}

	/**
	 * send the business information to partners
	 * 
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "sendSubmission.htm", method = RequestMethod.GET)
	public String bulkExport(Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		/*
		 * logger.info("start ::  bulkExport method"); SubmissionUtil
		 * submissionUtil = new SubmissionUtil();
		 * submissionUtil.doSubmissions(service);
		 * logger.info("end ::  bulkExport method");
		 */

		SubmissionThread submission = new SubmissionThread(service);
		submission.start();

		return "upload-export";
	}

	/**
	 * searchUsers
	 * 
	 * @param model
	 * @param bean
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/usersSearch.htm", method = RequestMethod.POST)
	public String searchUsers(Model model,
			@ModelAttribute("adminUser") UsersBean bean, HttpSession session) {
		logger.info("start ::  searchUsers method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String userName = bean.getSearchName();
		List<UsersDTO> searchUser = service.getUserByUserName(userName);
		controllerUtil.getRoles(model, service);
		controllerUtil.getChannels(model, session, service);
		model.addAttribute("usersListInfo", searchUser);
		model.addAttribute("adminUser", new UsersBean());
		model.addAttribute("brand", new UsersBean());
		logger.info("end ::  searchUsers method");
		return "manage-account";
	}

	/**
	 * shows list of UserDetails in view layer
	 * 
	 * @param model
	 * @param bean
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userShowInfo.htm", method = RequestMethod.POST)
	public String showUserDetails(Model model,
			@ModelAttribute("adminUser") UsersBean bean,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  showUserDetails method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String userID = bean.getCheckbox();
		// String userID = request.getParameter("userID");
		int role = (Integer) session.getAttribute("roleId");
		List<UsersDTO> usersList = service.getAllUsersList(role);
		model.addAttribute("usersListInfo", usersList);
		List<UsersDTO> userInfo = service.userInfo(Integer.parseInt(userID));
		if (userInfo.isEmpty()
				|| (userInfo.get(0).getRoleId() == LBLConstants.CONVERGENT_MOBILE_ADMIN && !(role == LBLConstants.CONVERGENT_MOBILE_ADMIN))) {
			return "logout";
		}
		List<String> channelNames = service.getChannelsBasedOnUser(Integer
				.parseInt(userID));
		model.addAttribute("channels", channelNames);
		UsersDTO usersDTO = userInfo.get(0);
		BeanUtils.copyProperties(usersDTO, bean);
		controllerUtil.getRoles(model, service);
		controllerUtil.setUserAndBussinessDataToModel(model, request, service);
		model.addAttribute("adminUser", bean);
		model.addAttribute("brand", new UsersBean());
		logger.info("end ::  showUserDetails method");
		return "manage-account";
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
	 * search brand and date range in listingActivity
	 * 
	 * @param model
	 * @param businessDTO
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/searchListingActivity.htm", method = RequestMethod.POST)
	public String searchListingActivity(Model model,
			@ModelAttribute("searchBusiness") LocalBusinessDTO businessDTO,
			UsersBean bean, HttpSession session) {
		logger.info("start ::  searchListingActivity method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		List<LocalBusinessDTO> listOfBrandsInfo = service.getListOfBrands();
		List<ExportReportDTO> listingActivityInfo = service
				.getListingActivityInfByBrand(businessDTO);
		Collections.sort(listingActivityInfo);
		List<UsersDTO> usersList = service.getAllUsersList((Integer) session
				.getAttribute("roleId"));
		model.addAttribute("usersListInfo", usersList);
		model.addAttribute("brandSize", listOfBrandsInfo.size());
		model.addAttribute("listingActivityInfo", listingActivityInfo);
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		businessDTO.setBrands("");
		model.addAttribute("searchBusiness", businessDTO);
		logger.info("end ::  searchListingActivity method");
		return "dashboard";
	}

	/**
	 * addBusiness
	 * 
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/addLocation", method = RequestMethod.GET)
	public String getAddLocation(Model model, HttpSession httpSession) {
		logger.info("start :: getAddLocation method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}

		model.addAttribute("businessInfo", new LocalBusinessDTO());
		logger.info("end :: getAddLocation method");
		return "business-listings-profile";
	}

	/**
	 * 
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/addLocation", method = RequestMethod.POST)
	public String addLocation(
			@ModelAttribute("businessInfo") LocalBusinessBean bean,
			Errors errors, HttpServletRequest req, HttpSession httpSession,
			Model model) {
		logger.info("start :: addLocation post  method");
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
				// logger.info("Adding error details for Key :" +
				// errorKey + ", errorMessage: " + errorsMap.get(field));
				model.addAttribute(errorKey, errorsMap.get(field));

			}
			return "business-listings-profile";
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
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		httpSession.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, req, listOfBusinessInfo);
		model.addAttribute("message", message);
		logger.info("end :: addLocation post  method");
		return "business-listings";
	}

	/**
	 * getConvergentToolbox
	 * 
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/getConvergentToolbox", method = RequestMethod.GET)
	public String getConvergentToolbox(Model model, HttpSession httpSession) {
		logger.info("start :: getConvergentToolbox  method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		logger.info("end :: getConvergentToolbox  method");
		return "convergent-toolbox";
	}

	/**
	 * help
	 * 
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public String help(Model model, HttpSession httpSession) {

		logger.info("start :: help  method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		logger.info("end :: help  method");
		return "help";
	}

	/**
	 * 
	 * this method gets the selected businessInformation and downloads to
	 * masterTemplate
	 * 
	 * @param beanStr
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadErrorBusiness", method = RequestMethod.POST)
	public String downloadErrorBusiness(@RequestBody String beanStr,
			Model model, HttpServletRequest request, HttpSession session)
			throws Exception {
		logger.info("start :: downloadErrorBusiness  method");
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
		logger.info("Service Name == " + services);
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
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize", errorListSize);
			session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
			controllerUtil.listingAddAttributes(1, model, request,
					listOfErrorBusinessInfo);
			logger.info("BusinessInformation size == "
					+ listOfErrorBusinessInfo.size());
			return "listing-errors";
		}
		List<LblErrorDTO> specificErrorBusinessInfo = service
				.getSpecificErrorBusinessInfo(listIds);
		model.addAttribute("listOfIncorrectData", specificErrorBusinessInfo);
		model.addAttribute("apiService", services);
		// model.addAttribute("errorRecords", "uploadErrorRecords");

		return "excelErrorView";
	}

	@RequestMapping(value = "/getChannelBrands", method = RequestMethod.GET)
	public @ResponseBody
	String listingsForBrand(
			@RequestParam(value = "channelName", required = true) String channelName,
			Model model) throws IllegalStateException, SystemException,
			JsonGenerationException, JsonMappingException, IOException {
		String json = null;
		ObjectMapper objectMapper = new ObjectMapper();
		List<BrandInfoDTO> channelBasedClients = service
				.getChannelBasedClients(channelName);
		logger.info("Total brands found for Channel are:"
				+ channelBasedClients.size());

		model.addAttribute("clientNameInfo", channelBasedClients);
		json = objectMapper.writeValueAsString(channelBasedClients);
		return json;
	}

}
