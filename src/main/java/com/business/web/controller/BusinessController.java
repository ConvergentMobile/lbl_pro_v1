package com.business.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.json.simple.JSONArray;
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

import com.business.common.dto.AppleDTO;
import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.ForgotPasswordDto;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.UsersDTO;
import com.business.common.util.ActivationMail;
import com.business.common.util.AddressValidationUtill;
import com.business.common.util.ControllerUtil;
import com.business.common.util.DateUtil;
import com.business.common.util.LBLConstants;
import com.business.common.util.MailUtil;
import com.business.common.util.RenewalThread;
import com.business.common.util.ScrappingThread;
import com.business.common.util.SubmissionThread;
import com.business.common.util.UploadBeanValidateUtil;
import com.business.common.util.WhitesparkThread;
import com.business.model.pojo.ChangeTrackingEntity;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.model.pojo.StatesListEntity;
import com.business.service.BusinessService;
import com.business.service.InventoryManagementService;
import com.business.web.bean.LblErrorBean;
import com.business.web.bean.LocalBusinessBean;
import com.business.web.bean.UploadBusinessBean;
import com.business.web.bean.UsersBean;
import com.business.web.validator.LocalBusinessValidator;
import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;
import com.whitespark.ws.ScrapUtil;
import com.whitespark.ws.WhitesparkUtil;
import com.whitespark.ws.wsProcessJSON;

/***
 * @author Vasanth
 * 
 *         BusinessController class which gets the data from the View Layer and
 *         sends to the Business Logic Layer and returns respective results to
 *         View Layer
 */

@Controller
@SessionAttributes(value = { "userName", "user", "userID", "roleId",
		"channelName", "locationAddress", "locationCity", "locationPhone",
		"locationState", "locationZipCode", "webAddress", "companyName",
		"store" })
public class BusinessController {

	Logger logger = Logger.getLogger(BusinessController.class);

	@Autowired
	private BusinessService service;
	@Autowired
	private InventoryManagementService inManagementService;

	@Autowired
	private LocalBusinessValidator businessValidator;

	private ControllerUtil controllerUtil = new ControllerUtil();

	private WhitesparkUtil util = new WhitesparkUtil();

	/***
	 * SecurityAction
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public String getLogin(Model model) {
		logger.info("Home Page");
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
	public String getPassword(Model model, UsersBean userBean,
			HttpServletRequest request) {

		logger.info("start ::getPassword method ");

		StringBuffer requestURL = request.getRequestURL();
		String urlrequest = requestURL.toString();
		int indexOf = urlrequest.indexOf("lbl_pro/");
		urlrequest = urlrequest.substring(0, indexOf);
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
			String url = urlrequest + LBLConstants.MAIL_URL + key;

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
		session.setAttribute("listSearchOfBusinessInfo", null);
		session.setAttribute("listOfErrorSearchBusinessInfo", null);
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
		System.out.println("Start: " + new Date());
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
		String phone = userDTO.getPhone();
		if (phone != null && !phone.isEmpty()) {
			java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat(
					"({0}) {1}-{2}");

			String[] phoneNumArr = { phone.substring(0, 3),
					phone.substring(3, 6), phone.substring(6) };
			bean.setPhone(phoneMsgFmt.format(phoneNumArr));
		}
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
		if (role == LBLConstants.USER) {

			List<LocalBusinessDTO> listOfBusinessInfo = service
					.getListOfUSerBusinessInfo();
			List<LblErrorDTO> listOfErrorBusinessInfo = service
					.getListOfUserErrors();
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
			session.setAttribute("loginRole", "User");
			logger.info("end ::  login post method");
			return "dashboard-user";

		}

		controllerUtil.getUploadDropDown(model, request, service);
		List<LocalBusinessDTO> businessDTOs = service.getListOfBrands();
		List<ExportReportDTO> exportReportDTOs = service
				.getListingActivityInf();
		Collections.sort(exportReportDTOs);
		model.addAttribute("adminUser", bean);
		List<UsersDTO> usersList = service.getAllUsersList(role);
		model.addAttribute("usersListInfo", usersList);
		model.addAttribute("brandSize", businessDTOs.size());
		model.addAttribute("uName", bean.getUserName());
		model.addAttribute("listingActivityInfo", exportReportDTOs);
		model.addAttribute("brandsInfo", businessDTOs);
		session.setAttribute("loginRole", "other");
		System.out.println("end: " + new Date());
		logger.info("end ::  login post method");
		return "dashboard";
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

	@RequestMapping(value = "businesslistings.htm", method = RequestMethod.GET)
	public String getBusinessLsitings(@RequestParam("page") int pageNum,
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
			HttpSession session) {

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("companyName", "");
		model.addAttribute("locationstore", "");
		model.addAttribute("brands", "");
		model.addAttribute("locationPhone", "");
		model.addAttribute("locationAddress", "");
		model.addAttribute("locationState", "");
		model.addAttribute("locationCity", "");
		model.addAttribute("locationZipCode", "");
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

	@RequestMapping(value = "businessErrorlistings.htm", method = RequestMethod.GET)
	public String getBusinessErrorLsitings(@RequestParam("page") int pageNum,
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
			HttpSession session) {

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();

		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("companyName", "");
		model.addAttribute("locationstore", "");
		model.addAttribute("brands", "");
		model.addAttribute("locationPhone", "");
		model.addAttribute("locationAddress", "");
		model.addAttribute("locationState", "");
		model.addAttribute("locationCity", "");
		model.addAttribute("locationZipCode", "");
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);

		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				if (size > 2) {
					size = size - 1;
				}

				// logger.info("error count for listing errors::"+size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);

		session.setAttribute("listOfErrorSearchBusinessInfo",
				listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, request,
				listOfErrorBusinessInfo);
		logger.info("BusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  getListingErrors method");
		return "listing-errors";
	}

	@RequestMapping(value = "/saveUser.htm", method = RequestMethod.POST)
	public String saveUserDetails(Model model,
			@ModelAttribute("adminUser") UsersBean bean,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  saveUserDetails method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String viewOnly = bean.getViewOnly();
		logger.info("viewOnly:::::::::::::::::::::::::::" + viewOnly);
		if (viewOnly != null) {
			bean.setViewOnly("Y");
		} else {
			bean.setViewOnly("N");
		}
		List<String> brandvalues = new ArrayList<String>();
		String[] brandID = bean.getBrandID();
		for (String string : brandID) {
			brandvalues.add(string);

		}
		/* List<Integer> brandidvalues=new ArrayList<Integer>(); */
		/*
		 * List<String> brandvalues=new ArrayList<String>(); List<BrandInfoDTO>
		 * channelBasedClient =
		 * service.getChannelBasedClient(bean.getChannelName()); for
		 * (BrandInfoDTO brandInfoDTO : channelBasedClient) { Integer brandID2 =
		 * brandInfoDTO.getBrandID(); brandvalues.add(String.valueOf(brandID2));
		 * }
		 */

		java.util.Iterator<String> itr = brandvalues.iterator();
		while (itr.hasNext()) {
			String number = itr.next();
			if (number.contains("on")) {
				itr.remove();
			}

		}
		String[] stockArr = new String[brandvalues.size()];
		stockArr = brandvalues.toArray(stockArr);
		bean.setBrandID(stockArr);
		controllerUtil.setUserAndBussinessDataToModel(model, request, service);
		List<String> listofStores = new ArrayList<String>();
		String userName = bean.getUserName();

		// Integer role = (Integer) session.getAttribute("roleId");
		Integer roleId = bean.getRoleId();
		if (roleId == null) {
			roleId = service.getRoleId(userName);
		}
		if (roleId == LBLConstants.USER) {

			String[] StoreName = request.getParameterValues("StoreName");
			logger.info("StoreName:::::::::::::" + StoreName);
			for (String string : StoreName) {
				listofStores.add(string);
			}

			logger.info("stores::" + listofStores.size());
		}

		String passWord = bean.getPassWord();
		String email = bean.getEmail();
		model.addAttribute("brand", new UsersBean());
		controllerUtil.getChannels(model, session, service);
		if (userName.length() > 0 && passWord.length() > 0) {
			if (bean.getUserID() == null) {
				boolean userExistis = service.isUserExistis(userName);
				if (userExistis) {
					model.addAttribute("message", "User Is Already Exists");
					logger.info("user is already exists");
					logger.info("end ::  saveUserDetails method");
					return "manage-account";
				}
			}
			boolean save = false;
			if (roleId == LBLConstants.USER) {
				service.saveUserUserStore(bean, listofStores);
			} else {
				save = service.saveUser(bean);
			}

			if (save == true) {
				StringBuffer requestURL = request.getRequestURL();

				ActivationMail.sendMail(email, userName, passWord, requestURL);

			}

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

	@RequestMapping(value = "/emaillink.htm", method = RequestMethod.POST)
	public String sendingActivationEmail(Model model,
			@ModelAttribute("adminUser") UsersBean bean,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  sendingActivationEmail method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		controllerUtil.setUserAndBussinessDataToModel(model, request, service);
		model.addAttribute("brand", new UsersBean());
		controllerUtil.getChannels(model, session, service);
		String userName = bean.getUserName();
		String passWord = bean.getPassWord();

		if (userName.length() > 0 && passWord.length() > 0) {

			StringBuffer requestURL = request.getRequestURL();
			logger.info("email url" + requestURL);
			String email = bean.getEmail();
			ActivationMail.sendMail(email, userName, passWord, requestURL);
			controllerUtil.getRoles(model, service);
			controllerUtil.getChannels(model, session, service);
			controllerUtil.setUserAndBussinessDataToModel(model, request,
					service);
			model.addAttribute("adminUser", new UsersBean());
			model.addAttribute("brand", new UsersBean());

		}
		logger.info("end ::  sendingActivationEmail method");
		return "manage-account";
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
				if (size >= 2) {
					size = size - 1;
				}

				// logger.info("error count for listing errors::"+size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		session.setAttribute("listOfErrorSearchBusinessInfo",
				listOfErrorBusinessInfo);
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
		session.setAttribute("listOfErrorSearchBusinessInfo",
				listOfErrorBusinessInfo);
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
			@RequestParam("checkedvalue") boolean checked, Model model,
			HttpSession session, HttpServletRequest request) {
		logger.info("start ::  getBusinessListingByPage method");
		logger.info("pageNum :: " + pageNum);
		logger.info("isChecked  :: " + checked);
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String flag1 = request.getParameter("flag");
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
		// Integer id = Integer.valueOf(req.getParameter("id"));

		String sessionAddress = (String) httpSession
				.getAttribute("locationAddress");
		String sessionCity = (String) httpSession.getAttribute("locationCity");
		String sessionPhone = (String) httpSession
				.getAttribute("locationPhone");
		String sessionState = (String) httpSession
				.getAttribute("locationState");
		String sessionZipCode = (String) httpSession
				.getAttribute("locationZipCode");
		String userName = (String) httpSession.getAttribute("userName");
		String sessionwebAddress = (String) httpSession
				.getAttribute("webAddress");
		String sessioncompanyName = (String) httpSession
				.getAttribute("companyName");

		LocalBusinessDTO businesslistingBystore = service
				.getBusinesslistingBystore(bean.getStore());

		if (businesslistingBystore != null) {
			if (sessionAddress == null || sessionAddress.equals("")) {
				sessionAddress = businesslistingBystore.getLocationAddress();
			}
			if (sessionCity == null || sessionCity.equals("")) {
				sessionCity = businesslistingBystore.getLocationCity();
			}
			if (sessionPhone == null || sessionPhone.equals("")) {
				sessionPhone = businesslistingBystore.getLocationPhone();
			}
			if (sessionState == null || sessionState.equals("")) {
				sessionState = businesslistingBystore.getLocationState();
			}
			if (sessionZipCode == null || sessionZipCode.equals("")) {
				sessionZipCode = businesslistingBystore.getLocationZipCode();
			}
			if (sessionwebAddress == null || sessionwebAddress.equals("")) {
				sessionwebAddress = businesslistingBystore.getWebAddress();
			}
			if (sessioncompanyName == null || sessioncompanyName.equals("")) {
				sessioncompanyName = businesslistingBystore.getCompanyName();
			}
		}

		UploadBeanValidateUtil beanValidateUtil = new UploadBeanValidateUtil();
		UploadBusinessBean uploadBusinessBean = new UploadBusinessBean();
		BeanUtils.copyProperties(bean, uploadBusinessBean);
		StringBuffer errorMessage = beanValidateUtil.validateBusinessListInfo(
				service, uploadBusinessBean);

		String error = errorMessage.toString();
		if (businesslistingBystore != null) {
			boolean isAddressChanged = isAddressChanged(businesslistingBystore,
					bean);
			if (isAddressChanged && !error.contains("LocationAddress")) {
				boolean isAddressValid = AddressValidationUtill
						.validateAddressWithSS(uploadBusinessBean);

				if (!isAddressValid)
					errorMessage
							.append(", LocationAddress Verification is failed.</br> Kindly verify the address info, ");
			}
		}

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
			List<Integer> listIds = (List<Integer>) httpSession
					.getAttribute("listIds");
			logger.info("id in update:::::::::::" + listIds);
			model.addAttribute("listId", listIds);
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

			boolean isBnamechanged = false;
			boolean isCitychanged = false;
			boolean isPhonechanged = false;
			boolean isStatechanged = false;
			boolean isZipCodechanged = false;
			boolean iswebAddresschanged = false;
			boolean isAddresschanged = false;

			Date date = new Date();
			ChangeTrackingEntity entity = new ChangeTrackingEntity();
			if (updateMultiBusinessInfo == true) {
				if (locationAddress.equalsIgnoreCase(sessionAddress)) {

				} else {
					entity.setLocationAddress(sessionAddress);
					isAddresschanged = true;
				}
				if (locationCity.equalsIgnoreCase(sessionCity)) {

				} else {
					entity.setLocationCity(sessionCity);
					isCitychanged = true;
				}
				if (locationPhone.equalsIgnoreCase(sessionPhone)) {

				} else {
					entity.setLocationPhone(sessionPhone);
					isPhonechanged = true;
				}
				if (locationZipCode.equalsIgnoreCase(sessionZipCode)) {

				} else {
					entity.setLocationZipCode(sessionZipCode);
					isZipCodechanged = true;
				}
				if (locationState.equalsIgnoreCase(sessionState)) {

				} else {
					entity.setLocationState(sessionState);
					isStatechanged = true;
				}
				if (webAddress.equalsIgnoreCase(sessionwebAddress)) {

				} else {
					entity.setWebSite(sessionwebAddress);
					iswebAddresschanged = true;
				}

				if (companyName.equalsIgnoreCase(sessioncompanyName)) {
					entity.setBusinessName(companyName);
				} else {
					entity.setBusinessName(sessioncompanyName);
					isBnamechanged = true;
				}

				entity.setClientId(clientId);
				entity.setStore(store);
				entity.setDate(date);
				entity.setUser(userName);
				ChangeTrackingEntity changeTrackingEntity = service
						.isClientIdAndStoreExists(clientId, store);
				if (changeTrackingEntity != null) {

					changeTrackingEntity
							.setChangeTrackingId(changeTrackingEntity
									.getChangeTrackingId());
					if ((isBnamechanged == true)) {
						changeTrackingEntity
								.setBusinessName(sessioncompanyName);
						changeTrackingEntity.setBusinessNameCDate(date);
					}
					if (isAddresschanged == true) {
						changeTrackingEntity.setLocationAddress(sessionAddress);
						changeTrackingEntity.setLocationAddressCDate(date);
					}
					if (iswebAddresschanged == true) {
						changeTrackingEntity.setWebSite(sessionwebAddress);
						changeTrackingEntity.setWebSiteCDate(date);
					}
					if (isStatechanged == true) {
						changeTrackingEntity.setLocationState(sessionState);
						changeTrackingEntity.setLocationStateCDate(date);
					}
					if (isPhonechanged == true) {
						changeTrackingEntity.setLocationPhone(sessionPhone);
						changeTrackingEntity.setLocationPhoneCDate(date);
					}
					if (isZipCodechanged == true) {
						changeTrackingEntity.setLocationZipCode(sessionZipCode);
						changeTrackingEntity.setLocationZipCodeCDate(date);
					}
					if (isCitychanged == true) {
						changeTrackingEntity.setLocationCity(sessionCity);
						changeTrackingEntity.setLocationCityCDate(date);
					}

					boolean changeTrackInfo = service
							.updateChangeTrackingInfo(changeTrackingEntity);
				} else {
					boolean isChanged = false;
					if (isBnamechanged == true) {
						isChanged = true;
						entity.setBusinessNameCDate(date);
					}
					if (isAddresschanged == true) {
						isChanged = true;
						entity.setLocationAddressCDate(date);
					}
					if (iswebAddresschanged == true) {
						isChanged = true;
						entity.setWebSiteCDate(date);
					}
					if (isStatechanged == true) {
						isChanged = true;
						entity.setLocationStateCDate(date);
					}
					if (isPhonechanged == true) {
						isChanged = true;
						entity.setLocationPhoneCDate(date);
					}
					if (isZipCodechanged == true) {
						isChanged = true;
						entity.setLocationZipCodeCDate(date);
					}
					if (isCitychanged == true) {
						isChanged = true;
						entity.setLocationCityCDate(date);
					}
					if (isChanged) {
						boolean changeTrackInfo = service
								.saveChangeTrackingInfo(entity);
					}

				}

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
		httpSession.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, req, listOfBusinessInfo);

		model.addAttribute("message", message == "" ? "Update Success Fully"
				: "Update Fail for Ids " + message);
		logger.info("end ::  updateBusinessInfo method");
		return "business-listings";
	}

	private boolean isAddressChanged(LocalBusinessDTO businesslistingBystore,
			LocalBusinessBean bean) {
		boolean isAddressChanged = false;
		String sesssionAddress = businesslistingBystore.getLocationAddress();
		String locationAddress = bean.getLocationAddress();
		String sessionCity = businesslistingBystore.getLocationCity();
		String locationCity = bean.getLocationCity();
		String sessionState = businesslistingBystore.getLocationState();
		String locationState = bean.getLocationState();
		String sessionZipCode = businesslistingBystore.getLocationZipCode();
		String locationZipCode = bean.getLocationZipCode();
		if (sesssionAddress.equalsIgnoreCase(locationAddress)) {
			return true;
		}
		if (sessionCity.equalsIgnoreCase(locationCity)) {
			return true;
		}
		if (sessionState.equalsIgnoreCase(locationState)) {
			return true;
		}
		if (sessionZipCode.equalsIgnoreCase(locationZipCode)) {
			return true;
		}

		return isAddressChanged;
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
		String errors = errorMessage.toString();
		if (!errors.contains("LocationAddress")) {
			boolean isAddressValid = AddressValidationUtill
					.validateAddressWithSS(uploadBusinessBean);

			if (!isAddressValid)
				errorMessage
						.append(", LocationAddress Verification is failed.</br> Kindly verify the address info, ");
		}

		if (errorMessage != null && errorMessage.length() > 0) {
			List<String> errorMsgs = Arrays.asList(errorMessage.toString()
					.split(","));
			model.addAttribute("errorListInfo", errorMsgs);
			UploadBeanValidateUtil util = new UploadBeanValidateUtil();
			Map<String, String> errorsMap = util.getErrorsMap(errorMsgs);
			Set<String> keySet = errorsMap.keySet();
			for (String field : keySet) {
				String errorKey = field.concat("_Error");
				/*
				 * System.out.println("Adding error details for Key :" +
				 * errorKey + ", errorMessage: " + errorsMap.get(field));
				 */
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
				service.updateErrorBusinessInfo(businessInfoDto, 0);
				// Integer listingId = service.getListingId(businessInfoDto);

				// if (listingId > 0) {
				// businessInfoDto.setId(listingId);
				// service.updateErrorBusinessInfo(businessInfoDto, listingId);
				// }
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
		httpSession.setAttribute("listOfErrorSearchBusinessInfo",
				listOfErrorBusinessInfo);
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
		String client = request.getParameter("client");
		logger.info("client for export info :: " + client);
		List<LocalBusinessDTO> specificBusinessInfo = new ArrayList<LocalBusinessDTO>();
		List<String> allBrandNames = new ArrayList<String>();

		Map<String, List<LocalBusinessDTO>> brandListingsMap = new HashMap<String, List<LocalBusinessDTO>>();
		List<ExportReportDTO> exportReports = new ArrayList<ExportReportDTO>();
		Map<String, Integer> exportActivity = new HashMap<String, Integer>();
		List<BrandInfoDTO> brandnames = new ArrayList<BrandInfoDTO>();
		Integer role = (Integer) session.getAttribute("roleId");

		if (client == null || client.equals("All Brands")) {
			if (role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
				brandnames = service.getClientNames();
			} else if (role == LBLConstants.CHANNEL_ADMIN
					|| role == LBLConstants.PURIST) {
				String channelName = (String) session
						.getAttribute("channelName");
				brandnames = service.getChannelBasedClients(channelName);
			}
			for (BrandInfoDTO brandInfoDTO : brandnames) {
				allBrandNames.add(brandInfoDTO.getBrandName());
			}
			for (String brand : allBrandNames) {
				List<LocalBusinessDTO> clientListings = service
						.getCLientListOfBusinessInfo(brand, templateName);
				brandListingsMap.put(brand, clientListings);
			}

		} else {
			allBrandNames.add(client);
			List<LocalBusinessDTO> clientListings = service
					.getCLientListOfBusinessInfo(client, templateName);

			brandListingsMap.put(client, clientListings);

		}

		Map<String, String> brandChannelMap = new HashMap<String, String>();

		for (String brand : allBrandNames) {
			String channelName = service.getBrandChannel(brand);
			brandChannelMap.put(brand, channelName);

			List<LocalBusinessDTO> listingsForBrand = brandListingsMap
					.get(brand);
			specificBusinessInfo.addAll(listingsForBrand);
			exportActivity.put(brand, listingsForBrand.size());

		}

		Set<String> keySet = exportActivity.keySet();
		String uploadUserName = (String) session.getAttribute("userName");
		for (String brand : keySet) {

			ExportReportDTO exportReportDTO = new ExportReportDTO();
			String channelName = service.getBrandChannel(brand);
			Date currentDate = new Date();
			exportReportDTO.setUserName(uploadUserName);
			exportReportDTO.setDate(new java.sql.Date(currentDate.getTime()));
			exportReportDTO.setPartner(templateName);
			exportReportDTO.setChannelName(channelName);
			exportReportDTO.setBrandName(brand);
			Integer count = exportActivity.get(brand);
			exportReportDTO.setCount(count);
			exportReports.add(exportReportDTO);
		}

		boolean exportReportInfo = service.exportReportDTO(exportReports);

		if (exportReportInfo) {
			model.addAttribute("listOfAPI", specificBusinessInfo);
			model.addAttribute("apiService", templateName);
			return "excelView";
		}
		// List<LblErrorDTO> listOfErrorBusinessInfo =
		// service.getListOfErrors();
		int activeListSize = specificBusinessInfo.size();
		// int errorListSize = listOfErrorBusinessInfo.size();
		// model.addAttribute("errorListSize", errorListSize);
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
		LocalBusinessDTO businessDTO = new LocalBusinessDTO();
		model.addAttribute("searchBusiness", businessDTO);
		dashBoardCommonInfo(model, session, businessDTO);
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
			/*
			 * System.out.println("Adding error details for Key :" + errorKey +
			 * ", errorMessage: " + errorsMap.get(field));
			 */
			model.addAttribute(errorKey, errorsMap.get(field));
		}
		model.addAttribute("listIds", listIds);
		logger.info("end ::  editBusinessErrorInfo method");
		return "error-listings-profile";

	}

	@RequestMapping(value = "/editErrorBusinessInfo", method = RequestMethod.GET)
	public String geteditBusinessErrorInfo(Model model, HttpServletRequest req,
			HttpSession httpSession) {
		logger.info("start ::  editBusinessErrorInfo method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		String listId = req.getParameter("listid");

		List<Integer> listIds = new ArrayList<Integer>();
		LblErrorDTO businessInfo = null;
		if (listId != null && listId.length() > 0) {
			String[] split = listId.split(",");
			for (String string : split) {
				string = string.replaceAll("\\s+", "");
				if (string.contains("[")) {
					string = string.replace("[", "");
				}
				if (string.contains("]")) {
					string = string.replace("]", "");
				}
				listIds.add(Integer.parseInt(string));
			}
			businessInfo = service.getErrorBusinessInfo(listIds.get(0));
			httpSession.setAttribute("listIds", listIds);
			httpSession.setAttribute("currentUpdateIdIndex", 0);
			logger.info("Selected IDs == " + listIds);
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
			/*
			 * System.out.println("Adding error details for Key :" + errorKey +
			 * ", errorMessage: " + errorsMap.get(field));
			 */
			model.addAttribute(errorKey, errorsMap.get(field));
		}
		logger.info("end ::  editBusinessErrorInfo method");
		return "error-listings-profile";

	}

	@RequestMapping(value = "/movetoListings.htm", method = RequestMethod.POST)
	public String moveToBusinessInfo(Model model, @RequestBody String beanStr,
			HttpServletRequest req, HttpSession httpSession) {
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

		LblErrorDTO businessInfo = service.getErrorBusinessInfo(listIds.get(0));
		String errorrMsg = businessInfo.getErrorMessage();
		/*
		 * if (errorrMsg != null && errorrMsg.length() > 0) { List<String>
		 * errorMsgs = Arrays.asList(errorrMsg .toString().split(",")); for
		 * (String string : errorMsgs) { errorrMsg=string; }
		 * 
		 * }
		 */
		logger.info("errorrMsg:::::::::::::" + errorrMsg);
		if (errorrMsg != null && errorrMsg.length() > 0
				&& errorrMsg.contains("LocationAddress")) {

			LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();
			// BeanUtils.copyProperties(businessInfo, localBusinessDTO);*/
			String uploadUserName = (String) httpSession
					.getAttribute("userName");
			// LocalBusinessDTO localBusinessDTO=new LocalBusinessDTO();
			String uploadJobId = uploadUserName + "_"
					+ System.currentTimeMillis();
			service.addErrorToBusinessList(localBusinessDTO, new Date(),
					uploadJobId, listIds);
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
			HttpSession session = req.getSession();
			session.setAttribute("userID", businessInfo.getId());
			String errorMessage = businessInfo.getErrorMessage();
			List<String> errors = Arrays.asList(errorMessage.toString().split(
					","));
			model.addAttribute("errorListInfo", errors);
			model.addAttribute("businessInfo", businessInfo);
			model.addAttribute("errormessage", "errormessage");
			UploadBeanValidateUtil util = new UploadBeanValidateUtil();
			Map<String, String> errorsMap = util.getErrorsMap(errors);
			Set<String> keySet = errorsMap.keySet();
			for (String field : keySet) {
				String errorKey = field.concat("_Error");
				/*
				 * System.out.println("Adding error details for Key :" +
				 * errorKey + ", errorMessage: " + errorsMap.get(field));
				 */
				model.addAttribute(errorKey, errorsMap.get(field));
			}
			logger.info("end ::  editBusinessErrorInfo method");
			return "listing-errors";
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
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		httpSession.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(1, model, req,
				listOfErrorBusinessInfo);
		logger.info("errorBusinessInformation size:  "
				+ listOfErrorBusinessInfo.size());
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
			/*
			 * System.out.println("Adding error details for Key :" + errorKey +
			 * ", errorMessage: " + errorsMap.get(field));
			 */
			model.addAttribute(errorKey, errorsMap.get(field));
		}
		logger.info("end ::  editBusinessErrorInfo method");
		return "listing-errors";

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
		String brandName = request.getParameter("brandval");
		logger.info("brandName in edit" + brandName);
		String storeVal = request.getParameter("storeval");
		logger.info("storeval in edit" + storeVal);

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
		String locationAddress = businessInfo.getLocationAddress();
		String locationCity = businessInfo.getLocationCity();
		String locationPhone = businessInfo.getLocationPhone();
		String locationState = businessInfo.getLocationState();
		String locationZipCode = businessInfo.getLocationZipCode();
		String webAddress = businessInfo.getWebAddress();
		String companyName = businessInfo.getCompanyName();
		String store = businessInfo.getStore();
		httpSession.setAttribute("locationAddress", locationAddress);
		httpSession.setAttribute("locationCity", locationCity);
		httpSession.setAttribute("locationPhone", locationPhone);
		httpSession.setAttribute("locationState", locationState);
		httpSession.setAttribute("locationZipCode", locationZipCode);
		httpSession.setAttribute("webAddress", webAddress);
		httpSession.setAttribute("companyName", companyName);
		httpSession.setAttribute("brandName", brandName);
		httpSession.setAttribute("store", store);
		model.addAttribute("listId", listIds);
		model.addAttribute("brandName", brandName);
		httpSession.setAttribute("storeVal", storeVal);
		model.addAttribute("businessInfo", businessInfo);
		logger.info("end ::  editBusinessInfo method");
		return "business-listings-profile";
	}

	@RequestMapping(value = "/editBusinessInfor.htm", method = RequestMethod.GET)
	public String editBusinessInfor(Model model,
			@ModelAttribute("businessInfo") LocalBusinessBean bean,
			HttpSession httpSession, HttpServletRequest request) {
		logger.info("start ::  editBusinessInfo method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		String brandName = request.getParameter("brandval");
		logger.info("brandName in edit" + brandName);
		String storeVal = request.getParameter("storeval");
		logger.info("storeval in edit" + storeVal);

		Integer id = bean.getId();
		logger.info("id in edit businessmethod::::::::::::::" + id);
		/* String[] split = beanStr.split("&"); */
		List<Integer> listIds = new ArrayList<Integer>();
		String listId = request.getParameter("listid");
		logger.info("listId:::::::::::" + listId);
		LocalBusinessDTO businessInfo = null;
		if (listId != null && listId.length() > 0) {
			String[] split = listId.split(",");
			for (String string : split) {
				string = string.replaceAll("\\s+", "");
				if (string.contains("[")) {
					string = string.replace("[", "");
				}
				if (string.contains("]")) {
					string = string.replace("]", "");
				}
				listIds.add(Integer.parseInt(string));
			}

			httpSession.setAttribute("listIds", listIds);
			httpSession.setAttribute("currentUpdateIdIndex", 0);
			logger.info("Selected IDs == " + listIds);
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
			model.addAttribute("listId", listId);
			model.addAttribute("allBusinessInfo", listOfBusinessInfo);
			model.addAttribute("businessSearch", new LocalBusinessDTO());
			model.addAttribute("businessInfo", new LocalBusinessDTO());
			return "business-listings-profile";
		}
		String locationAddress = businessInfo.getLocationAddress();
		String locationCity = businessInfo.getLocationCity();
		String locationPhone = businessInfo.getLocationPhone();
		String locationState = businessInfo.getLocationState();
		String locationZipCode = businessInfo.getLocationZipCode();
		String webAddress = businessInfo.getWebAddress();
		String companyName = businessInfo.getCompanyName();
		String store = businessInfo.getStore();
		httpSession.setAttribute("locationAddress", locationAddress);
		httpSession.setAttribute("locationCity", locationCity);
		httpSession.setAttribute("locationPhone", locationPhone);
		httpSession.setAttribute("locationState", locationState);
		httpSession.setAttribute("locationZipCode", locationZipCode);
		httpSession.setAttribute("webAddress", webAddress);
		httpSession.setAttribute("companyName", companyName);
		httpSession.setAttribute("brandName", brandName);
		httpSession.setAttribute("store", store);
		model.addAttribute("listId", listIds);
		model.addAttribute("brandName", brandName);
		httpSession.setAttribute("storeVal", storeVal);
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
	@RequestMapping(value = "/exportBusinessInfo.htm", method = RequestMethod.POST)
	public String exportBusinessInfo(@RequestBody String beanStr, Model model,
			HttpSession session, HttpServletRequest request,
			@RequestParam("checkedvalue") boolean checkedValue) {
		logger.info("start ::  exportBusinessInfo method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		HttpSession session2 = request.getSession();
		List<LocalBusinessDTO> businessDTOs = (List<LocalBusinessDTO>) session2
				.getAttribute("listSearchOfBusinessInfo");
		logger.info("businessDTOs:::::::::::::::::::::" + businessDTOs);
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
		List<LocalBusinessDTO> specificBusinessInfo = new ArrayList<LocalBusinessDTO>();
		List<String> allbrands = new ArrayList<String>();
		Map<String, List<LocalBusinessDTO>> brandListingsMap = new HashMap<String, List<LocalBusinessDTO>>();
		List<ExportReportDTO> exportReports = new ArrayList<ExportReportDTO>();
		Map<String, Integer> exportActivity = new HashMap<String, Integer>();
		List<BrandInfoDTO> brandnames = new ArrayList<BrandInfoDTO>();
		Integer role = (Integer) session.getAttribute("roleId");
		if (role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
			brandnames = service.getClientNames();
		} else if (role == LBLConstants.CHANNEL_ADMIN
				|| role == LBLConstants.PURIST) {
			String channelName = (String) session.getAttribute("channelName");
			brandnames = service.getChannelBasedClients(channelName);
		}
		for (BrandInfoDTO brandInfoDTO : brandnames) {

			String brandName = brandInfoDTO.getBrandName();
			allbrands.add(brandName);
		}
		List<Integer> listId = new ArrayList<Integer>();

		if (checkedValue == true && businessDTOs == null) {

			for (String client : allbrands) {
				List<LocalBusinessDTO> allLsiting = service
						.getCLientListOfBusinessInfo(client, services);
				brandListingsMap.put(client, allLsiting);
			}
		} else if (checkedValue == true && businessDTOs != null) {

			for (LocalBusinessDTO localBusinessDTO : businessDTOs) {
				listId.add(localBusinessDTO.getId());
			}
			logger.info("listIds::::::::::::::" + listId.size());
			specificBusinessInfo = service.getSpecificBusinessInfo(listId,
					services);
		} else {
			specificBusinessInfo = service.getSpecificBusinessInfo(listIds,
					services);
		}

		Map<String, String> brandChannelMap = new HashMap<String, String>();
		if (!brandListingsMap.isEmpty()) {
			for (String brand : allbrands) {
				String channelName = service.getBrandChannel(brand);
				brandChannelMap.put(brand, channelName);

				List<LocalBusinessDTO> listingsForBrand = brandListingsMap
						.get(brand);
				specificBusinessInfo.addAll(listingsForBrand);
				exportActivity.put(brand, listingsForBrand.size());

			}
		} else {

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

		int exportSize = specificBusinessInfo.size();

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
			model.addAttribute("exportMessage", "exportMessage");
			return "excelView";
		}
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = exportSize;
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		// session.setAttribute("listOfBusinessInfo", specificBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				specificBusinessInfo);
		model.addAttribute("exportMessage", "exportMessage");
		logger.info("end ::  exportBusinessInfo method");
		return "business-listings";
	}

	@RequestMapping(value = "/getJSOnExport.htm", method = RequestMethod.GET)
	@ResponseBody
	public String getJSOnExport(@RequestParam("client") String client,
			Model model, HttpSession session, HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info("start ::  exportInfoFromUploadExport method");

		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String templateName = request.getParameter("service");
		logger.info("client for export info :: " + client);
		List<LocalBusinessDTO> specificBusinessInfo = new ArrayList<LocalBusinessDTO>();
		List<String> allBrandNames = new ArrayList<String>();

		Map<String, List<LocalBusinessDTO>> brandListingsMap = new HashMap<String, List<LocalBusinessDTO>>();
		List<ExportReportDTO> exportReports = new ArrayList<ExportReportDTO>();
		Map<String, Integer> exportActivity = new HashMap<String, Integer>();
		List<BrandInfoDTO> brandnames = new ArrayList<BrandInfoDTO>();
		Integer role = (Integer) session.getAttribute("roleId");

		if (client == null || client.equals("All Brands")) {
			if (role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
				brandnames = service.getClientNames();
			} else if (role == LBLConstants.CHANNEL_ADMIN
					|| role == LBLConstants.PURIST) {
				String channelName = (String) session
						.getAttribute("channelName");
				brandnames = service.getChannelBasedClients(channelName);
			}
			for (BrandInfoDTO brandInfoDTO : brandnames) {
				allBrandNames.add(brandInfoDTO.getBrandName());
			}
			for (String brand : allBrandNames) {
				List<LocalBusinessDTO> clientListings = service
						.getCLientListOfBusinessInfo(brand, templateName);
				brandListingsMap.put(brand, clientListings);
			}

		} else {
			allBrandNames.add(client);
			List<LocalBusinessDTO> clientListings = service
					.getCLientListOfBusinessInfo(client, templateName);

			brandListingsMap.put(client, clientListings);

		}

		Map<String, String> brandChannelMap = new HashMap<String, String>();

		for (String brand : allBrandNames) {
			String channelName = service.getBrandChannel(brand);
			brandChannelMap.put(brand, channelName);

			List<LocalBusinessDTO> listingsForBrand = brandListingsMap
					.get(brand);
			specificBusinessInfo.addAll(listingsForBrand);
			exportActivity.put(brand, listingsForBrand.size());

		}

		Set<String> keySet = exportActivity.keySet();
		String uploadUserName = (String) session.getAttribute("userName");
		for (String brand : keySet) {

			ExportReportDTO exportReportDTO = new ExportReportDTO();
			String channelName = service.getBrandChannel(brand);
			Date currentDate = new Date();
			exportReportDTO.setUserName(uploadUserName);
			exportReportDTO.setDate(new java.sql.Date(currentDate.getTime()));
			exportReportDTO.setPartner(templateName);
			exportReportDTO.setChannelName(channelName);
			exportReportDTO.setBrandName(brand);
			Integer count = exportActivity.get(brand);
			exportReportDTO.setCount(count);
			exportReports.add(exportReportDTO);
		}

		boolean exportReportInfo = service.exportReportDTO(exportReports);

		List<Map> maplist = new ArrayList<Map>();
		for (LocalBusinessDTO localBusinessDTO : specificBusinessInfo) {
			Map responseDetailsJson = new LinkedHashMap();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayaddress = new JSONArray();
			JSONArray jsonArraystructred = new JSONArray();
			JSONArray jsonArrayphone = new JSONArray();
			JSONArray jsonArrayaltphone = new JSONArray();
			JSONArray jsonArrayaltcontent = new JSONArray();
			JSONArray jsonArrayaltcontentdisplay = new JSONArray();
			JSONArray jsonArrayaltcontententry = new JSONArray();
			StringBuffer sb = new StringBuffer();
			sb.append(localBusinessDTO.getClientId());
			sb.append("-");
			sb.append(localBusinessDTO.getStore());
			responseDetailsJson.put("site_code", sb.toString());
			JSONArray busniessarray = new JSONArray();
			Map businesmap = new LinkedHashMap();
			businesmap.put("status", "open");
			// businesmap.put("reopen_date", "");
			busniessarray.add(businesmap);
			responseDetailsJson.put("business_status", busniessarray);
			Map formDetailsJson = new LinkedHashMap();
			formDetailsJson.put("name", localBusinessDTO.getCompanyName());
			if (localBusinessDTO.getCountryCode().equalsIgnoreCase("US")) {
				formDetailsJson.put("locale", "en-US");
			} else if (localBusinessDTO.getCountryCode().equalsIgnoreCase("CA")) {
				formDetailsJson.put("locale", "en-CA");
			} else {
				formDetailsJson.put("locale", "");
			}

			jsonArray.add(formDetailsJson);
			Map formDetailsJsons = new LinkedHashMap();
			Map structuredadrres = new LinkedHashMap();
			structuredadrres.put("street_address",
					localBusinessDTO.getLocationAddress());
			structuredadrres
					.put("locality", localBusinessDTO.getLocationCity());
			structuredadrres.put("country_code",
					localBusinessDTO.getCountryCode());
			structuredadrres.put("postal_code",
					localBusinessDTO.getLocationZipCode());
			jsonArraystructred.add(structuredadrres);
			formDetailsJsons.put("structured_address", jsonArraystructred);

			jsonArrayaddress.add(formDetailsJsons);
			responseDetailsJson.put("names", jsonArray);
			responseDetailsJson.put("alternate_names",
					localBusinessDTO.getAlternativeName());
			responseDetailsJson.put("main_address", formDetailsJsons);

			Map formDetailsJsonphone = new LinkedHashMap();
			String locationPhone = localBusinessDTO.getLocationPhone();
			String mobilePhone = localBusinessDTO.getMobileNumber();
			String additionalPhone = localBusinessDTO.getAdditionalNumber();
			formDetailsJsonphone.put("number", locationPhone);
			formDetailsJsonphone.put("type", "Landline");
			jsonArrayphone.add(formDetailsJsonphone);
			responseDetailsJson.put("phone_number", jsonArrayphone);
			Map formDetailsJsonaltphone = new LinkedHashMap();
			Map formDetailsJsonaltphone1 = new LinkedHashMap();
			Map formDetailsJsonaltphone2 = new LinkedHashMap();
			Map formDetailsJsonaltphone3 = new LinkedHashMap();
			String fax = localBusinessDTO.getFax();
			String tollFree = localBusinessDTO.getTollFree();

			if (fax != null && fax.length() > 0) {
				formDetailsJsonaltphone.put("number", fax);
				formDetailsJsonaltphone.put("type", "Fax");
			}
			if (tollFree != null && tollFree.length() > 0) {
				formDetailsJsonaltphone1.put("number", tollFree);
				formDetailsJsonaltphone1.put("type", "Landline");
			}
			if (additionalPhone != null && additionalPhone.length() > 0) {
				formDetailsJsonaltphone3.put("number", additionalPhone);
				formDetailsJsonaltphone3.put("type", "Landline");
			}
			if (mobilePhone != null && mobilePhone.length() > 0) {
				formDetailsJsonaltphone2.put("number", mobilePhone);
				formDetailsJsonaltphone2.put("type", "Mobile");
			}

			// formDetailsJsonaltphone.put("intent", "reservation");
			if (!formDetailsJsonaltphone.isEmpty()) {
				jsonArrayaltphone.add(formDetailsJsonaltphone);
			}
			if (!formDetailsJsonaltphone1.isEmpty()) {
				jsonArrayaltphone.add(formDetailsJsonaltphone1);
			}
			if (!formDetailsJsonaltphone2.isEmpty()) {
				jsonArrayaltphone.add(formDetailsJsonaltphone2);
			}
			if (!formDetailsJsonaltphone3.isEmpty()) {
				jsonArrayaltphone.add(formDetailsJsonaltphone3);
			}


			responseDetailsJson.put("alternate_phone_numbers",
					jsonArrayaltphone);
			String webAddress = localBusinessDTO.getWebAddress();
			StringBuilder sbs = new StringBuilder();
			if (webAddress != null && webAddress.length() > 0
					&& webAddress.contains("www")) {

				// sbs.append("https://");
				sbs.append(webAddress);
			}
			responseDetailsJson.put("home_page", sbs.toString());
			StringBuffer links = new StringBuffer();
			String foursquareLink = localBusinessDTO.getFoursquareLink();
			String yelpLink = localBusinessDTO.getYelpLink();
			String youTubeOrVideoLink = localBusinessDTO
					.getYouTubeOrVideoLink();
			String couponLink = localBusinessDTO.getMyspaceLink();
			String twitterLink = localBusinessDTO.getTwitterLink();
			String facebookLink = localBusinessDTO.getFacebookLink();
			String alternateSocialLink = localBusinessDTO
					.getAlternateSocialLink();
			String googlePlusLink = localBusinessDTO.getGooglePlusLink();
			String pinteristLink = localBusinessDTO.getPinteristLink();
			String instagramLink = localBusinessDTO.getInstagramLink();
			String linkedInLink = localBusinessDTO.getLinkedInLink();
			if (facebookLink != null && facebookLink.length() > 0) {
				links.append(facebookLink);
				links.append(",");
			}
			if (yelpLink != null && facebookLink.length() > 0) {
				links.append(yelpLink);
				links.append(",");
			}
			if (youTubeOrVideoLink != null && youTubeOrVideoLink.length() > 0) {
				links.append(youTubeOrVideoLink);
				links.append(",");
			}
			if (couponLink != null && couponLink.length() > 0) {
				links.append(couponLink);
				links.append(",");
			}
			if (twitterLink != null && twitterLink.length() > 0) {
				links.append(twitterLink);
				links.append(",");
			}
			if (alternateSocialLink != null && alternateSocialLink.length() > 0) {
				links.append(alternateSocialLink);
				links.append(",");
			}
			if (googlePlusLink != null && googlePlusLink.length() > 0) {
				links.append(googlePlusLink);
				links.append(",");
			}
			if (pinteristLink != null && pinteristLink.length() > 0) {
				links.append(pinteristLink);
				links.append(",");
			}
			if (instagramLink != null && instagramLink.length() > 0) {
				links.append(instagramLink);
				links.append(",");
			}
			if (foursquareLink != null && foursquareLink.length() > 0) {
				links.append(foursquareLink);
				links.append(",");
			}
			if (linkedInLink != null && linkedInLink.length() > 0) {
				links.append(linkedInLink);
			}

			/*
			 * links.append(youTubeOrVideoLink); links.append(",");
			 * links.append(couponLink); links.append(",");
			 * links.append(twitterLink); links.append(",");
			 * links.append(alternateSocialLink); links.append(",");
			 * links.append(googlePlusLink); links.append(",");
			 * links.append(pinteristLink); links.append(",");
			 * links.append(instagramLink); links.append(",");
			 * links.append(menuLink); links.append(",");
			 * links.append(logoLink);
			 */
			responseDetailsJson.put("social_media_urls", links.toString());
			responseDetailsJson.put("hours", getHours(localBusinessDTO));
			responseDetailsJson.put("categories",
					localBusinessDTO.getAppleCategory());

			JSONArray jsonArrayaltcontentcoordinate = new JSONArray();
			Map responseDetailsJsoncontent = new LinkedHashMap();
			responseDetailsJsoncontent.put("short_abstract",
					localBusinessDTO.getBusinessDescriptionShort());
			responseDetailsJsoncontent.put("locale", "en_US");
			jsonArrayaltcontent.add(responseDetailsJsoncontent);
			responseDetailsJson.put("content", responseDetailsJsoncontent);
			Map responseDetailsJsondisplay = new LinkedHashMap();
			Map responseDetailsJsoncoordinate = new LinkedHashMap();
			responseDetailsJsoncoordinate.put("latitude", "");
			responseDetailsJsoncoordinate.put("longitude", "");
			jsonArrayaltcontentcoordinate.add(responseDetailsJsoncoordinate);
			responseDetailsJsondisplay.put("coordinates",
					responseDetailsJsoncoordinate);
			responseDetailsJsondisplay.put("source", "");
			jsonArrayaltcontentdisplay.add(responseDetailsJsondisplay);
			responseDetailsJson
					.put("display_point", jsonArrayaltcontentdisplay);
			Map responseDetailsJsonentry = new LinkedHashMap();
			Map responseDetailsentry = new LinkedHashMap();
			Map responseDetailsJsoncoordinates = new LinkedHashMap();
			responseDetailsJsoncoordinates.put("latitude", "");
			responseDetailsJsoncoordinates.put("longitude", "");
			jsonArrayaltcontentcoordinate.add(responseDetailsJsoncoordinates);
			responseDetailsentry.put("coordinates",
					responseDetailsJsoncoordinates);
			responseDetailsentry.put("source", "");
			responseDetailsentry.put("navigation", "");
			jsonArrayaltcontententry.add(responseDetailsentry);
			// responseDetailsJson.put("entry_points",
			// jsonArrayaltcontententry);
			// responseDetailsJson.put("geometry", "");
			responseDetailsJson.put("chain", "");
			responseDetailsJson.put("amenities", "");
			getPaymentTypes(localBusinessDTO);
			responseDetailsJson.put("payment_methods",
					getPaymentTypes(localBusinessDTO));
			responseDetailsJson.put("contactless_payments_status", "");
			responseDetailsJson.put("apple_pay_status", "");

			JSONArray pricingarray = new JSONArray();
			Map pricingmap = new LinkedHashMap();
			JSONArray rangeArry = new JSONArray();
			Map rangemap = new LinkedHashMap();
			rangemap.put("currency_code", "");
			rangemap.put("minrate", "");
			rangemap.put("maxrate", "");
			rangeArry.add(rangemap);
			pricingmap.put("indicator", "");
			pricingmap.put("range", rangemap);
			pricingarray.add(pricingmap);
			// responseDetailsJson.put("pricing", pricingarray);

			// responseDetailsJson.put("metadata", "");
			responseDetailsJson.put("content",
					localBusinessDTO.getBusinessDescription());

			JSONArray jsonArrayaddition = new JSONArray();
			Map additionInfoMAp = new LinkedHashMap();
			additionInfoMAp.put("room_facilities", "");
			Map checkinMap = new LinkedHashMap();
			checkinMap.put("time", "");
			additionInfoMAp.put("checkin", checkinMap);

			Map checkoutMap = new LinkedHashMap();
			checkoutMap.put("time", "");
			additionInfoMAp.put("checkout", checkoutMap);
			jsonArrayaddition.add(additionInfoMAp);
			// responseDetailsJson.put("additional_info", jsonArrayaddition);
			maplist.add(responseDetailsJson);

		}

		/*
		 * if (exportReportInfo) { model.addAttribute("listOfAPI",
		 * specificBusinessInfo); model.addAttribute("apiService",
		 * templateName); return "excelView"; }
		 */
		// List<LblErrorDTO> listOfErrorBusinessInfo =
		// service.getListOfErrors();
		int activeListSize = specificBusinessInfo.size();
		// int errorListSize = listOfErrorBusinessInfo.size();
		// model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", specificBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				specificBusinessInfo);
		ObjectMapper objectMapper = new ObjectMapper();
		logger.info("end ::  exportInfoFromUploadExport method");
		return objectMapper.writeValueAsString(maplist);
	}

	public List<String> getPaymentTypes(LocalBusinessDTO localBusinessDTO) {
		StringBuffer creditCards = new StringBuffer();
		List<String> crList = new ArrayList<String>();
		if (localBusinessDTO.getaMEX() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getaMEX())) {
			crList.add("American Express");
		}
		if (localBusinessDTO.getMasterCard() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getMasterCard())) {
			crList.add("MasterCard");
		}
		if (localBusinessDTO.getDiscover() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDiscover())) {
			crList.add("Discover");
		}
		if (localBusinessDTO.getVisa() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getVisa())) {
			crList.add("Visa");
		}
		if (localBusinessDTO.getDinersClub() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDinersClub())) {
			crList.add("Diner's Club");
		}
		if (localBusinessDTO.getCash() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getCash())) {
			crList.add("Cash");
		}
		if (localBusinessDTO.getCheck() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getCheck())) {
			crList.add("Check");
		}
		if (localBusinessDTO.getDebitCard() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getDebitCard())) {
			crList.add("Debit card");
		}
		if (localBusinessDTO.getTravelersCheck() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getTravelersCheck())) {
			crList.add("Traveler's Check");
		}
		if (localBusinessDTO.getFinancing() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getFinancing())) {
			crList.add("Financing");
		}
		if (localBusinessDTO.getInvoice() != null
				&& "Y".equalsIgnoreCase(localBusinessDTO.getInvoice())) {
			crList.add("Invoice");
		}

		return crList;
	}

	public static String getHours(LocalBusinessDTO localBusinessDTO) {
		StringBuffer workingHours = new StringBuffer();
		String mondayOpen = localBusinessDTO.getMondayOpen();
		String mondayClose = localBusinessDTO.getMondayClose();

		String tuesdayOpen = localBusinessDTO.getTuesdayOpen();
		String tuuesdayClose = localBusinessDTO.getTuesdayClose();

		String wedOpen = localBusinessDTO.getWednesdayOpen();
		String wedClose = localBusinessDTO.getWednesdayClose();

		String thursdayOpen = localBusinessDTO.getThursdayOpen();
		String thursdayClose = localBusinessDTO.getThursdayClose();

		String fridayOpen = localBusinessDTO.getFridayOpen();
		String fridayClose = localBusinessDTO.getFridayClose();

		String satOpen = localBusinessDTO.getSaturdayOpen();
		String satClose = localBusinessDTO.getSaturdayClose();

		String sunOpen = localBusinessDTO.getSundayOpen();
		String sunClose = localBusinessDTO.getSundayClose();

		if ("CLOSE".equalsIgnoreCase(mondayOpen)
				|| "CLOSE".equalsIgnoreCase(mondayClose)) {
			// workingHours.append("MON CLOSE, ");
		}

		else if (mondayOpen != null && mondayOpen.trim().length() > 0
				&& mondayClose != null && mondayClose.trim().length() > 0) {
			workingHours.append("MO " + mondayOpen + "-" + mondayClose + ", ");
		}
		if ("CLOSE".equalsIgnoreCase(tuesdayOpen)
				|| "CLOSE".equalsIgnoreCase(tuuesdayClose)) {
			// /workingHours.append("TUE CLOSE, ");
		} else if (tuesdayOpen != null && tuesdayOpen.trim().length() > 0
				&& tuuesdayClose != null && tuuesdayClose.trim().length() > 0) {
			workingHours.append("TU " + tuesdayOpen + "-" + tuuesdayClose
					+ ", ");
		}

		if ("CLOSE".equalsIgnoreCase(wedOpen)
				|| "CLOSE".equalsIgnoreCase(wedClose)) {
			// workingHours.append("WED CLOSE, ");
		} else if (wedOpen != null && wedOpen.trim().length() > 0
				&& wedClose != null && wedClose.trim().length() > 0) {
			workingHours.append("WE " + wedOpen + "-" + wedClose + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(thursdayOpen)
				|| "CLOSE".equalsIgnoreCase(thursdayClose)) {
			// workingHours.append("THU CLOSE, ");
		} else if (thursdayOpen != null && thursdayOpen.trim().length() > 0
				&& thursdayClose != null && thursdayClose.trim().length() > 0) {
			workingHours.append("TH " + tuesdayOpen + "-" + tuuesdayClose
					+ ", ");
		}

		if ("CLOSE".equalsIgnoreCase(fridayOpen)
				|| "CLOSE".equalsIgnoreCase(fridayClose)) {
			// workingHours.append("FRI CLOSE, ");
		} else if (fridayOpen != null && fridayOpen.trim().length() > 0
				&& fridayClose != null && fridayClose.trim().length() > 0) {
			workingHours.append("FR " + fridayOpen + "-" + fridayClose + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(satOpen)
				|| "CLOSE".equalsIgnoreCase(satClose)) {
			// workingHours.append("SAT CLOSE, ");
		} else if (satOpen != null && satOpen.trim().length() > 0
				&& satClose != null && satClose.trim().length() > 0) {
			workingHours.append("SA " + satOpen + "-" + satClose + ", ");
		}

		if ("CLOSE".equalsIgnoreCase(sunOpen)
				|| "CLOSE".equalsIgnoreCase(sunClose)) {
			// workingHours.append("SUN CLOSE");
		} else if (sunOpen != null && sunOpen.trim().length() > 0
				&& sunClose != null && sunClose.trim().length() > 0) {
			workingHours.append("SU " + sunOpen + "-" + sunClose);
		}

		String hours = workingHours.toString();
		return hours;

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
		
		String searchType = businessDTO.getSearchType();
		String searchValue = businessDTO.getSearchValue();
		System.out.println("searchType:"+ searchType + ",----"+ searchValue) ;

		if(searchType!="" && searchValue!="" ) {
			if(searchType.equalsIgnoreCase("sbBR")){
				brands = searchValue;
			}
			if(searchType.equalsIgnoreCase("sbBN")){
				companyName = searchValue;
			}
			if(searchType.equalsIgnoreCase("sbS")){
				store = searchValue;
			}
			if(searchType.equalsIgnoreCase("sbP")){
				locationPhone = searchValue;
			}
		}

		Set<LocalBusinessDTO> businesSearchinfo = service.businesSearchInfo(
				brands, companyName, store, locationPhone, locationAddress,
				locationCity, locationState, locationZipCode);
	
		List<LocalBusinessDTO> listOfBusinessInfo = new ArrayList<LocalBusinessDTO>(
				businesSearchinfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("companyName", companyName);
		model.addAttribute("locationstore", store);
		model.addAttribute("brands", brands);
		model.addAttribute("locationPhone", locationPhone);
		model.addAttribute("locationAddress", locationAddress);
		model.addAttribute("locationState", locationState);
		model.addAttribute("locationCity", locationCity);
		model.addAttribute("locationZipCode", locationZipCode);
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		session.setAttribute("listSearchOfBusinessInfo", listOfBusinessInfo);
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
		
		String searchType = businessDTO.getSearchType();
		String searchValue = businessDTO.getSearchValue();
		System.out.println("searchType:"+ searchType + ",----"+ searchValue) ;

		if(searchType!="" && searchValue!="" ) {
			if(searchType.equalsIgnoreCase("sbBR")){
				brands = searchValue;
			}
			if(searchType.equalsIgnoreCase("sbBN")){
				companyName = searchValue;
			}
			if(searchType.equalsIgnoreCase("sbS")){
				store = searchValue;
			}
			if(searchType.equalsIgnoreCase("sbP")){
				locationPhone = searchValue;
			}
		}
		
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
		model.addAttribute("companyName", companyName);
		model.addAttribute("locationstore", store);
		model.addAttribute("brands", brands);
		model.addAttribute("locationPhone", locationPhone);
		model.addAttribute("locationAddress", locationAddress);
		model.addAttribute("locationState", locationState);
		model.addAttribute("locationCity", locationCity);
		model.addAttribute("locationZipCode", locationZipCode);
		session.setAttribute("listOfErrorSearchBusinessInfo",
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
		dashBoardCommonInfo(model, session, businessDTO);
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
		dashBoardCommonInfo(model, session, businessDTO);
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
		/* HttpSession session = request.getSession(); */
		Integer role = (Integer) session.getAttribute("roleId");
		logger.info("role:::::::::::" + role);
		List<UsersDTO> usersList = service.getAllUsersList(role);
		List<BrandInfoDTO> clientNames = null;
		if (role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
			clientNames = service.getClientNames();
		} else if (role == LBLConstants.CHANNEL_ADMIN
				|| role == LBLConstants.PURIST) {
			String channelName = (String) session.getAttribute("channelName");
			clientNames = service.getChannelBasedClients(channelName);
		}
		ObjectMapper mapper = new ObjectMapper();

		List<StatesListEntity> stateNames = service.getStateList();

		String stateNamesvalue = "";
		try {
			stateNamesvalue = mapper.writeValueAsString(stateNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("stateNamesInfor", stateNamesvalue);
		model.addAttribute("stateNamesInfo", stateNames);

		String clientNamesvalues = "";
		try {
			clientNamesvalues = mapper.writeValueAsString(clientNames);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("clientNameInfor", clientNamesvalues);
		model.addAttribute("clientNameInfo", clientNames);
		model.addAttribute("usersListInfo", usersList);
		UsersBean userbean = new UsersBean();
		model.addAttribute("adminUser", userbean);
		model.addAttribute("userIdValue", userbean.getUserID());
		controllerUtil.getChannels(model, session, service);
		// controllerUtil.setUserAndBussinessDataToModel(model, request,
		// service);
		model.addAttribute("adminUser", userbean);
		model.addAttribute("brand", userbean);
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
		List<BrandInfoDTO> clientNames = null;
		if (role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			for (UsersDTO userInfor : userInfo) {
				if (userInfor.getRoleId() == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
					clientNames = service.getClientNames();
				} else {
					String channelName = userInfor.getChannelName();
					clientNames = service.getChannelBasedClient(channelName);
				}

			}

		} else if (role == LBLConstants.CHANNEL_ADMIN
				|| role == LBLConstants.PURIST) {
			String channelName = (String) session.getAttribute("channelName");
			clientNames = service.getChannelBasedClients(channelName);
		}
		ObjectMapper mapper = new ObjectMapper();
		/* List<LocalBusinessDTO> stateNames = service.getStates(); */
		List<StatesListEntity> stateNames = service.getStateList();

		String stateNamesvalue = "";
		try {
			stateNamesvalue = mapper.writeValueAsString(stateNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("stateNamesInfor", stateNamesvalue);
		model.addAttribute("stateNamesInfo", stateNames);

		String clientNamesvalues = "";
		try {
			clientNamesvalues = mapper.writeValueAsString(clientNames);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("clientNameInfor", clientNamesvalues);
		model.addAttribute("clientNameInfo", clientNames);
		model.addAttribute("usersListInfo", usersList);
		List<String> channelNames = service.getChannelsBasedOnUser(userID);
		model.addAttribute("channels", channelNames);
		UsersDTO usersDTO = userInfo.get(0);
		model.addAttribute("userIdValue", userID);
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
			bean.setEmail(brandNameInfo.getEmail());
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
						startDateValue, bean.getcImagePath());
			}
			message = controllerUtil.saveOrUpdateBrand(bean, message,
					startDateValue, channelID, service);
		}
		logger.info("mesassa" + message);
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
		String client = req.getParameter("client");
		logger.info("clent name for send submissions::" + client);
		SubmissionThread submission = new SubmissionThread(service, client);
		submission.start();
		controllerUtil.getUploadDropDown(model, req, service);
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

	/*
	 * @RequestMapping(value = "/userdelete.htm", method = RequestMethod.POST)
	 * public String deleteUser(Model model,
	 * 
	 * @ModelAttribute("adminUser") UsersBean bean, HttpServletRequest request,
	 * HttpSession session) { logger.info("start :: UserDelete method"); if
	 * (!loginSessionValidation(model, session)) { return "logout"; } String
	 * userID = bean.getCheckbox(); // String userID =
	 * request.getParameter("userID"); int role = (Integer)
	 * session.getAttribute("roleId"); List<UsersDTO> usersList =
	 * service.getAllUsersList(role); model.addAttribute("usersListInfo",
	 * usersList); service.deleteUSer(Integer.parseInt(userID));
	 * 
	 * List<String> channelNames = service.getChannelsBasedOnUser(Integer
	 * .parseInt(userID)); model.addAttribute("channels", channelNames);
	 * 
	 * controllerUtil.getRoles(model, service);
	 * controllerUtil.setUserAndBussinessDataToModel(model, request, service);
	 * model.addAttribute("adminUser", bean); model.addAttribute("brand", new
	 * UsersBean()); logger.info("end :: UserDelete method"); return
	 * "manage-account"; }
	 */

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
		// controllerUtil.setUserAndBussinessDataToModel(model, request,
		// service);
		List<BrandInfoDTO> clientNames = null;
		if (role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			for (UsersDTO userInfor : userInfo) {
				if (userInfor.getRoleId() == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
					clientNames = service.getClientNames();
				} else {
					String channelName = userInfor.getChannelName();
					clientNames = service.getChannelBasedClient(channelName);
				}

			}

		} else if (role == LBLConstants.CHANNEL_ADMIN
				|| role == LBLConstants.PURIST) {
			String channelName = (String) session.getAttribute("channelName");
			clientNames = service.getChannelBasedClients(channelName);
		}
		ObjectMapper mapper = new ObjectMapper();
		/* List<LocalBusinessDTO> stateNames = service.getStates(); */
		List<StatesListEntity> stateNames = service.getStateList();

		String stateNamesvalue = "";
		try {
			stateNamesvalue = mapper.writeValueAsString(stateNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("stateNamesInfor", stateNamesvalue);
		model.addAttribute("stateNamesInfo", stateNames);

		String clientNamesvalues = "";
		try {
			clientNamesvalues = mapper.writeValueAsString(clientNames);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("clientNameInfor", clientNamesvalues);
		model.addAttribute("clientNameInfo", clientNames);
		model.addAttribute("usersListInfo", usersList);
		model.addAttribute("adminUser", new UsersBean());
		model.addAttribute("adminUser", bean);
		model.addAttribute("userIdValue", userID);
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
		dashBoardCommonInfo(model, session, businessDTO);
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
	 * template
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
			@RequestParam("checkedValue") boolean checkedval, Model model,
			HttpServletRequest request, HttpSession session) throws Exception {
		logger.info("start :: downloadErrorBusiness  method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		HttpSession session2 = request.getSession();
		List<LblErrorDTO> specificErrorBusinessInfo = null;
		List<LblErrorDTO> businessDTOs = (List<LblErrorDTO>) session2
				.getAttribute("listOfErrorSearchBusinessInfo");
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
		} else if (checkedval == true && businessDTOs != null) {
			List<Integer> listId = new ArrayList<Integer>();
			for (LblErrorDTO lblErrorDTO : businessDTOs) {
				listId.add(lblErrorDTO.getId());
			}
			specificErrorBusinessInfo = service
					.getSpecificErrorBusinessInfo(listId);

		} else if (checkedval == true && businessDTOs == null) {

			specificErrorBusinessInfo = service.getListOfErrors();

		} else {
			specificErrorBusinessInfo = service
					.getSpecificErrorBusinessInfo(listIds);
		}
		model.addAttribute("listOfIncorrectData", specificErrorBusinessInfo);
		model.addAttribute("apiService", services);
		// model.addAttribute("errorRecords", "uploadErrorRecords");

		return "excelErrorView";
	}

	/*
	 * @RequestMapping(value = "/brandchannel.htm", method = RequestMethod.GET)
	 * public String getDeleteBrandChannel(Model model, HttpServletRequest
	 * request, HttpSession httpSession) {
	 * 
	 * logger.info("start :: deleteBrandChannel  method"); if
	 * (!loginSessionValidation(model, httpSession)) { return "logout"; }
	 * List<ChannelNameDTO> channelNames = service.getChannel();
	 * 
	 * model.addAttribute("channels", channelNames);
	 * controllerUtil.setUserAndBussinessDataToModel(model, request, service);
	 * 
	 * logger.info("end :: deleteBrandChannel  method"); return "manageBrands";
	 * }
	 * 
	 * @RequestMapping(value = "/branddelete.htm", params = "brands", method =
	 * RequestMethod.POST) public String channelBrands(Model model,
	 * 
	 * @ModelAttribute("brandchannel") UsersBean bean, HttpServletRequest
	 * request, HttpSession session) {
	 * logger.info("start ::  saveUserDetails method"); if
	 * (!loginSessionValidation(model, session)) { return "logout"; }
	 * 
	 * Integer channelID = bean.getChannelID(); logger.info("channel id::" +
	 * channelID);
	 * 
	 * List<BrandInfoDTO> clientNames = service
	 * .getBrandsByChannelID(channelID);
	 * 
	 * List<ChannelNameDTO> channelNames = service.getChannel();
	 * model.addAttribute("channels", channelNames);
	 * model.addAttribute("clientNameInfo", clientNames); return "manageBrands";
	 * }
	 * 
	 * @RequestMapping(value = "/branddelete", method = RequestMethod.POST)
	 * public String deleteBrandChannel(Model model,
	 * 
	 * @ModelAttribute("brandchannel") UsersBean bean, HttpServletRequest
	 * request, HttpSession session) {
	 * logger.info("start ::  saveUserDetails method"); if
	 * (!loginSessionValidation(model, session)) { return "logout"; }
	 * 
	 * List<Integer> listofbrand = new ArrayList<>(); String[] brandID =
	 * bean.getBrandID(); for (String string : brandID) {
	 * listofbrand.add(Integer.valueOf(string));
	 * 
	 * } Integer channelID = bean.getChannelID();
	 * logger.info("delete brand id::" + brandID + "delete channel id::" +
	 * channelID); if (brandID != null) { service.deleteBrands(listofbrand); }
	 * if (channelID != null) { service.deleteChannel(channelID); }
	 * List<ChannelNameDTO> channelNames = service.getChannel();
	 * model.addAttribute("channels", channelNames);
	 * controllerUtil.setUserAndBussinessDataToModel(model, request, service);
	 * 
	 * return "manageBrands"; }
	 */

	@RequestMapping(value = "/getstore.htm", method = RequestMethod.GET)
	public @ResponseBody
	String getStore(Model model, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		logger.info("start ::  getStores method");
		String state = request.getParameter("state");
		logger.info("state value ::::" + state);
		List<String> stores = service.getStoresbasedonState(state);
		model.addAttribute("storelist", stores);
		// System.out.println(stores.toString());
		return stores.toString();

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
				.getChannelBasedClient(channelName);
		logger.info("Total brands found for Channel are:"
				+ channelBasedClients.size());

		model.addAttribute("clientNameInfo", channelBasedClients);
		json = objectMapper.writeValueAsString(channelBasedClients);
		return json;
	}

	@RequestMapping(value = "/parsejson.htm", method = RequestMethod.GET)
	public void parseJson(HttpServletRequest request) throws Exception {
		String data = request.getParameter("data");
		logger.info("data in parse jdon:::::::::::::::::" + data);
		logger.info("service::::::::::::" + service);
		wsProcessJSON.processJSON(data);

	}

	/**
	 * send the business information to partners
	 * 
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "runWsReport.htm", method = RequestMethod.GET)
	public String runWsReport(Model model, HttpServletRequest req,
			HttpServletResponse resp, HttpSession session) {
		logger.info("start ::  runWsReport method");

		WhitesparkThread thread = new WhitesparkThread(service);
		thread.start();

		// WhitesparkUtil util = new WhitesparkUtil();
		// util.triggerWhiteSparkSerach(service);

		controllerUtil.getUploadDropDown(model, req, service);

		logger.info("End ::  runWsReport method");
		return "upload-export";
	}

	/**
	 * send the business information to partners
	 * 
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "parseDomains.htm", method = RequestMethod.GET)
	public String parseDomains(Model model, HttpServletRequest req,
			HttpServletResponse resp, HttpSession session) {
		logger.info("start ::  parseDomains method");

		ScrappingThread thread = new ScrappingThread(service);
		thread.start();

		// WhitesparkUtil util = new WhitesparkUtil();
		// util.triggerWhiteSparkSerach(service);

		controllerUtil.getUploadDropDown(model, req, service);

		logger.info("End ::  parseDomains method");
		return "upload-export";
	}

	@RequestMapping(value = "renewListings.htm", method = RequestMethod.GET)
	public String renewalBrands(Model model, HttpServletRequest req,
			HttpServletResponse resp, HttpSession session) {
		logger.info("start ::  renewalBrands method");
		String client = req.getParameter("client");
		logger.info("clent name for send submissions::" + client);
		RenewalThread thread = new RenewalThread(service, client);
		thread.start();

		// WhitesparkUtil util = new WhitesparkUtil();
		// util.triggerWhiteSparkSerach(service);

		controllerUtil.getUploadDropDown(model, req, service);

		logger.info("End ::  renewalBrands method");
		return "upload-export";
	}

	@RequestMapping(value = "/userdelete.htm", method = RequestMethod.POST)
	public String deleteUser(Model model,
			@ModelAttribute("adminUser") UsersBean bean,
			HttpServletRequest request, HttpSession session) {
		logger.info("start :: UserDelete method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		Integer userID = bean.getUserID();
		// String userID = request.getParameter("userID");
		int role = (Integer) session.getAttribute("roleId");
		List<UsersDTO> usersList = service.getAllUsersList(role);
		model.addAttribute("usersListInfo", usersList);
		service.deleteUSer((userID));

		List<String> channelNames = service.getChannelsBasedOnUser((userID));
		model.addAttribute("channels", channelNames);

		controllerUtil.getRoles(model, service);
		controllerUtil.setUserAndBussinessDataToModel(model, request, service);
		model.addAttribute("adminUser", bean);
		model.addAttribute("brand", new UsersBean());
		logger.info("end :: UserDelete method");
		return "manage-account";
	}

	@RequestMapping(value = "businesstorelistings.htm", method = RequestMethod.GET)
	public String getbusinesstorelistings(Model model, HttpServletRequest req,
			HttpServletResponse resp, HttpSession session) {

		String store = (String) session.getAttribute("storeVal");

		store = (String) session.getAttribute("store");
		logger.info("store::::::::::::::::::::::" + store);
		String brand = (String) session.getAttribute("brandName");
		logger.info("brand::::::::::::::::::::::" + brand);

		List<LocalBusinessDTO> listOfBusinessInfo = null;
		if (brand != null && !brand.isEmpty()) {
			listOfBusinessInfo = inManagementService
					.searchBusinessListinginfoByBrand(brand);
		} else if (store != null && !store.isEmpty()) {
			listOfBusinessInfo = inManagementService
					.searchBusinessListinginfo(store);
		} else {
			listOfBusinessInfo = service.getListOfBusinessInfo();
		}

		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, req, listOfBusinessInfo);
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		logger.info("end ::  getSearchParametersFromDashBoard method");

		return "business-listings";
	}

	@RequestMapping(value = "sortByStore.htm", method = RequestMethod.GET)
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
			HttpSession session) {

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

		/*
		 * List<LocalBusinessDTO> listOfBusinessInfo = new
		 * ArrayList<LocalBusinessDTO>(); for (LocalBusinessEntity
		 * localBusinessEntity : businessDTOs) { LocalBusinessDTO dto=new
		 * LocalBusinessDTO(); BeanUtils.copyProperties(localBusinessEntity,
		 * dto); listOfBusinessInfo.add(dto); }
		 */
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

		return "business-listings";
	}

	@RequestMapping(value = "sortByBName.htm", method = RequestMethod.GET)
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
			HttpSession session) {

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
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("selectType", "businessname");
		model.addAttribute("flagvalue", flag);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		return "business-listings";
	}

	@RequestMapping(value = "sortByAddress.htm", method = RequestMethod.GET)
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
			HttpSession session) {

		// String flag1 = request.getParameter("flag");

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
		model.addAttribute("selectType", "address");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		return "business-listings";
	}

	@RequestMapping(value = "sortByCity.htm", method = RequestMethod.GET)
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
			HttpSession session) {
		String flag1 = request.getParameter("flag");
		logger.info("flag:::::::::::::::::::" + flag1);
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
		model.addAttribute("selectType", "city");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		return "business-listings";
	}

	@RequestMapping(value = "sortByState.htm", method = RequestMethod.GET)
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
			HttpSession session) {

		String flag1 = request.getParameter("flag");
		logger.info("flag:::::::::::::::::::" + flag1);
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
		model.addAttribute("selectType", "state");
		model.addAttribute("flagvalue", flag);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		model.addAttribute("checked", "true");
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("checkedvalue", checked);

		return "business-listings";
	}

	@RequestMapping(value = "sortByZip.htm", method = RequestMethod.GET)
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
			HttpSession session) {
		String flag1 = request.getParameter("flag");
		logger.info("flag:::::::::::::::::::" + flag1);
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
		model.addAttribute("selectType", "zip");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		return "business-listings";
	}

	@RequestMapping(value = "sortByPhone.htm", method = RequestMethod.GET)
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
			HttpSession session) {
		String flag1 = request.getParameter("flag");
		logger.info("flag:::::::::::::::::::" + flag1);
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
		model.addAttribute("selectType", "phone");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		return "business-listings";
	}

	@RequestMapping(value = "sortByLCount.htm", method = RequestMethod.GET)
	public String getAllBusinessListingsSortBytotallocations(
			@RequestParam("flag") String flag, Model model,
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
				.getAllBusinessListingsSortBytotallocations(flag);

		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		controllerUtil.getUploadDropDown(model, request, service);
		List<LocalBusinessDTO> listOfBrandsInfo = service.getListOfBrands();
		List<ExportReportDTO> listingActivityInfo = service
				.getListingActivityInf();
		Collections.sort(listingActivityInfo);

		List<UsersDTO> usersList = service.getAllUsersList((Integer) session
				.getAttribute("roleId"));
		model.addAttribute("usersListInfo", usersList);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("brandSize", listOfBrandsInfo.size());
		model.addAttribute("listingActivityInfo", listingActivityInfo);
		model.addAttribute("brandsInfo", listOfBusinessInfo);
		LocalBusinessDTO businessDTO = new LocalBusinessDTO();
		model.addAttribute("searchBusiness", businessDTO);
		dashBoardCommonInfo(model, session, businessDTO);
		logger.info("end ::  dashBoard method");
		return "dashboard";
	}

	@RequestMapping(value = "getCountSort.htm", method = RequestMethod.GET)
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

		List<ExportReportDTO> listingActivityInfo = service
				.getListingActivityInf(flag);
		if (flag.equalsIgnoreCase("ASC")) {
			flag = "DESC";
		} else {
			flag = "ASC";
		}
		controllerUtil.getUploadDropDown(model, request, service);
		List<LocalBusinessDTO> listOfBrandsInfo = service.getListOfBrands();

		List<UsersDTO> usersList = service.getAllUsersList((Integer) session
				.getAttribute("roleId"));
		model.addAttribute("usersListInfo", usersList);
		model.addAttribute("flagvalue", flag);
		model.addAttribute("brandSize", listOfBrandsInfo.size());
		model.addAttribute("listingActivityInfo", listingActivityInfo);
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		LocalBusinessDTO businessDTO = new LocalBusinessDTO();
		model.addAttribute("searchBusiness", businessDTO);
		dashBoardCommonInfo(model, session, businessDTO);
		logger.info("end ::  dashBoard method");
		return "dashboard";
	}

	@RequestMapping(value = "searchBrand.htm", method = RequestMethod.GET)
	public String getSaerchByBrand(@RequestParam("brand") String brand,
			Model model, HttpServletRequest request, HttpServletResponse resp,
			HttpSession session) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		controllerUtil.getRoles(model, service);

		Integer role = (Integer) session.getAttribute("roleId");
		logger.info("role:::::::::::" + role);
		List<UsersDTO> usersList = service.getAllUsersList(role);
		List<BrandInfoDTO> clientNames = null;
		if (role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
			clientNames = service.getClientNamesByBrand(brand);
		} else if (role == LBLConstants.CHANNEL_ADMIN
				|| role == LBLConstants.PURIST) {
			String channelName = (String) session.getAttribute("channelName");
			clientNames = service.getChannelBasedClientsByBrand(channelName,
					brand);
		}
		ObjectMapper mapper = new ObjectMapper();

		List<StatesListEntity> stateNames = service.getStateList();

		String stateNamesvalue = "";
		try {
			stateNamesvalue = mapper.writeValueAsString(stateNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("stateNamesInfor", stateNamesvalue);
		model.addAttribute("stateNamesInfo", stateNames);
		String clientNamesvalues = "";
		try {
			clientNamesvalues = mapper.writeValueAsString(clientNames);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("clientNameInfor", clientNamesvalues);

		model.addAttribute("clientNameInfo", clientNames);
		model.addAttribute("usersListInfo", usersList);
		model.addAttribute("adminUser", new UsersBean());
		controllerUtil.getChannels(model, session, service);

		model.addAttribute("adminUser", new UsersBean());
		model.addAttribute("brand", new UsersBean());
		logger.info("end ::  ManageAccount method");
		return "manage-account";
	}

	@RequestMapping(value = "searchState.htm", method = RequestMethod.GET)
	public String getSaerchByState(@RequestParam("state") String state,
			Model model, HttpServletRequest request, HttpServletResponse resp,
			HttpSession session) {
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		controllerUtil.getRoles(model, service);
		/* HttpSession session = request.getSession(); */
		Integer role = (Integer) session.getAttribute("roleId");
		logger.info("role:::::::::::" + role);
		List<UsersDTO> usersList = service.getAllUsersList(role);
		List<BrandInfoDTO> clientNames = null;
		if (role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
			clientNames = service.getClientNames();
		} else if (role == LBLConstants.CHANNEL_ADMIN
				|| role == LBLConstants.PURIST) {
			String channelName = (String) session.getAttribute("channelName");
			clientNames = service.getChannelBasedClients(channelName);
		}
		ObjectMapper mapper = new ObjectMapper();

		List<StatesListEntity> stateNames = service.getStateListByState(state);

		String stateNamesvalue = "";
		try {
			stateNamesvalue = mapper.writeValueAsString(stateNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("stateNamesInfor", stateNamesvalue);
		model.addAttribute("stateNamesInfo", stateNames);

		String clientNamesvalues = "";
		try {
			clientNamesvalues = mapper.writeValueAsString(clientNames);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("clientNameInfor", clientNamesvalues);
		model.addAttribute("clientNameInfo", clientNames);
		model.addAttribute("usersListInfo", usersList);
		model.addAttribute("adminUser", new UsersBean());
		controllerUtil.getChannels(model, session, service);

		model.addAttribute("adminUser", new UsersBean());
		model.addAttribute("brand", new UsersBean());
		logger.info("end ::  ManageAccount method");
		return "manage-account";
	}

	@RequestMapping(value = "/business-listingserror_page.htm", method = RequestMethod.GET)
	public String getErrorBusinessListingByPage(
			@RequestParam("page") int pageNum,
			@RequestParam("checkedvalue") boolean checked, Model model,
			HttpSession session, HttpServletRequest request) {
		logger.info("start ::  getListingErrors method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		HttpSession session2 = request.getSession();
		List<LblErrorDTO> listOfErrorBusinessInfo = (List<LblErrorDTO>) session
				.getAttribute("listOfErrorSearchBusinessInfo");
		/*
		 * if(listOfErrorBusinessInfo==null){
		 * listOfErrorBusinessInfo=service.getListOfErrors(); }
		 * /*List<LblErrorDTO> listOfErrorBusinessInfo =
		 * service.getListOfErrors();
		 */
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
		controllerUtil.listingAddAttributes(pageNum, model, request,
				listOfErrorBusinessInfo);
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		logger.info("BusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  getListingErrors method");
		return "listing-errors";
	}

	@RequestMapping(value = "/saveChannelUser.htm", method = RequestMethod.POST)
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
			Integer roleId = (Integer) session.getAttribute("roleId");
			if (roleId == LBLConstants.CHANNEL_ADMIN) {
				bean.setRoleId(LBLConstants.CHANNEL_ADMIN);
			} else {
				bean.setRoleId(LBLConstants.PURIST);
			}

			String channelName = (String) session.getAttribute("channelName");

			bean.setChannelName(channelName);
			String formattedphone = bean.getPhone();
			if (formattedphone != null && !formattedphone.isEmpty()) {
				String phone = ScrapUtil.getFormattedPhone(formattedphone);
				bean.setPhone(phone);
			}

			boolean saveManageAccount = service.saveUser(bean);
			dashBoardCommonInfo(model, session, businessDTO);
			controllerUtil.getUploadDropDown(model, request, service);
			List<LocalBusinessDTO> listOfBrandsInfo = service.getListOfBrands();
			List<ExportReportDTO> listingActivityInfo = service
					.getListingActivityInf();
			Collections.sort(listingActivityInfo);
			List<UsersDTO> usersList = service
					.getAllUsersList((Integer) session.getAttribute("roleId"));
			model.addAttribute("usersListInfo", usersList);
			model.addAttribute("brandSize", listOfBrandsInfo.size());
			model.addAttribute("listingActivityInfo", listingActivityInfo);
			model.addAttribute("brandsInfo", listOfBrandsInfo);
			logger.info("end ::  dashBoard method");
			return "dashboard";
		}
		dashBoardCommonInfo(model, session, businessDTO);
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

	private void dashBoardCommonInfo(Model model, HttpSession session,
			LocalBusinessDTO businessDTO) {
		String userName = (String) session.getAttribute("userName");
		List<UsersDTO> userInfo = service.getUserByUserName(userName);
		UsersDTO usersDTO = userInfo.get(0);
		String name = usersDTO.getName();
		String lastName = usersDTO.getLastName();
		String fullName = name + " " + lastName;
		usersDTO.setFullName(fullName);
		UsersBean bean = new UsersBean();
		BeanUtils.copyProperties(usersDTO, bean);
		String phone = bean.getPhone();
		if (phone != null && !phone.isEmpty()) {
			java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat(
					"({0}) {1}-{2}");

			String[] phoneNumArr = { phone.substring(0, 3),
					phone.substring(3, 6), phone.substring(6) };
			bean.setPhone(phoneMsgFmt.format(phoneNumArr));
		}
		model.addAttribute("adminUser", bean);
		model.addAttribute("searchBusiness", businessDTO);

	}

	@RequestMapping(value = "sortByErrorStore.htm", method = RequestMethod.GET)
	public String getAllBusinessErrorListingsSortBystore(
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
			HttpSession session) {

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

		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfBusinessErrorInfoByStore(flag, fmap);

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
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
		/*
		 * List<LblErrorDTO> listOfErrorBusinessInfo =
		 * service.getListOfErrors();
		 */
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("selectType", "store");
		model.addAttribute("flagvalue", flag);
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				if (size > 2) {
					size = size - 1;
				}

				// logger.info("error count for listing errors::"+size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(pageNum, model, request,
				listOfErrorBusinessInfo);
		logger.info("BusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  getListingErrors method");
		return "listing-errors";
	}

	@RequestMapping(value = "sortByErrorBName.htm", method = RequestMethod.GET)
	public String getAllBusinessErrorListingsSortBybusinessname(
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
			HttpSession session) {

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
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfBusinessErrorInfoByBusinessName(flag, fmap);

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
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
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("selectType", "businessname");
		model.addAttribute("flagvalue", flag);
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				if (size > 2) {
					size = size - 1;
				}

				// logger.info("error count for listing errors::"+size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(pageNum, model, request,
				listOfErrorBusinessInfo);
		logger.info("BusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  getListingErrors method");
		return "listing-errors";
	}

	@RequestMapping(value = "sortByErrorAddress.htm", method = RequestMethod.GET)
	public String getAllBusinessErrorListingsSortByaddress(
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
			HttpSession session) {

		// String flag1 = request.getParameter("flag");

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
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfBusinessErrorInfoByAddress(flag, fmap);

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
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
		model.addAttribute("selectType", "address");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				if (size > 2) {
					size = size - 1;
				}

				// logger.info("error count for listing errors::"+size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(pageNum, model, request,
				listOfErrorBusinessInfo);
		logger.info("BusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  getListingErrors method");
		return "listing-errors";
	}

	@RequestMapping(value = "sortByErrorCity.htm", method = RequestMethod.GET)
	public String getAllBusinessErrorListingsSortBycity(
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
			HttpSession session) {
		String flag1 = request.getParameter("flag");
		logger.info("flag:::::::::::::::::::" + flag1);
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
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfBusinessErrorInfoByCity(flag, fmap);

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
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
		model.addAttribute("selectType", "city");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				if (size > 2) {
					size = size - 1;
				}

				// logger.info("error count for listing errors::"+size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(pageNum, model, request,
				listOfErrorBusinessInfo);
		logger.info("BusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  getListingErrors method");
		return "listing-errors";
	}

	@RequestMapping(value = "sortByErrorState.htm", method = RequestMethod.GET)
	public String getAllBusinessErrorListingsSortBystate(
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
			HttpSession session) {

		String flag1 = request.getParameter("flag");
		logger.info("flag:::::::::::::::::::" + flag1);
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
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfBusinessErrorInfoByState(flag, fmap);

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
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
		model.addAttribute("selectType", "state");
		model.addAttribute("flagvalue", flag);
		controllerUtil.listingsAddAttributes(pageNum, model, request,
				listOfBusinessInfo);
		model.addAttribute("checked", "true");
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		model.addAttribute("checkedvalue", checked);
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				if (size > 2) {
					size = size - 1;
				}

				// logger.info("error count for listing errors::"+size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(pageNum, model, request,
				listOfErrorBusinessInfo);
		logger.info("BusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  getListingErrors method");
		return "listing-errors";
	}

	@RequestMapping(value = "sortByErrorZip.htm", method = RequestMethod.GET)
	public String getAllBusinessErrorListingsSortByzip(
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
			HttpSession session) {
		String flag1 = request.getParameter("flag");
		logger.info("flag:::::::::::::::::::" + flag1);
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
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfBusinessErrorInfoByZip(flag, fmap);

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
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
		model.addAttribute("selectType", "zip");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				if (size > 2) {
					size = size - 1;
				}

				// logger.info("error count for listing errors::"+size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(pageNum, model, request,
				listOfErrorBusinessInfo);
		logger.info("BusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  getListingErrors method");
		return "listing-errors";
	}

	@RequestMapping(value = "sortByErrorPhone.htm", method = RequestMethod.GET)
	public String getAllBusinessErrorListingsSortByphone(
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
			HttpSession session) {
		String flag1 = request.getParameter("flag");
		logger.info("flag:::::::::::::::::::" + flag1);
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
		List<LblErrorDTO> listOfErrorBusinessInfo = service
				.getListOfBusinessErrorInfoByPhone(flag, fmap);

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
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
		model.addAttribute("selectType", "phone");
		model.addAttribute("checked", "true");
		model.addAttribute("checkedvalue", checked);
		model.addAttribute("flagvalue", flag);
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		Integer size = 0;
		for (LblErrorDTO lblErrorDTO : listOfErrorBusinessInfo) {
			String errorMessage = lblErrorDTO.getErrorMessage();
			if (errorMessage != null && errorMessage.length() > 0) {
				List<String> errorMsgs = Arrays.asList(errorMessage.toString()
						.split(","));
				size = errorMsgs.size();
				if (size > 2) {
					size = size - 1;
				}
				// logger.info("error count for listing errors::"+size);
				lblErrorDTO.setErrorMessage(String.valueOf(size));
			}
		}
		model.addAttribute("errorListSize", errorListSize);
		session.setAttribute("listOfBusinessInfo", listOfErrorBusinessInfo);
		controllerUtil.listingAddAttributes(pageNum, model, request,
				listOfErrorBusinessInfo);
		logger.info("BusinessInformation size == "
				+ listOfErrorBusinessInfo.size());
		logger.info("end ::  getListingErrors method");
		return "listing-errors";
	}
}
