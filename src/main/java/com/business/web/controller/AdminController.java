package com.business.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.CategoryDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.common.dto.CustomSubmissionsDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.ControllerUtil;
import com.business.common.util.DateUtil;
import com.business.common.util.LBLConstants;
import com.business.model.pojo.RenewalReportEntity;
import com.business.service.BusinessService;
import com.business.service.CategoryService;
import com.business.service.InventoryManagementService;
import com.business.service.ManageBrandService;
import com.business.service.ReportService;
import com.business.web.bean.CategoryBean;
import com.business.web.bean.CustomSubmissionsBean;
import com.business.web.bean.UsersBean;
import com.business.web.validator.LocalBusinessValidator;

@Controller
public class AdminController {
	Logger logger = Logger.getLogger(AdminController.class);

	@Autowired
	private BusinessService service;
	@Autowired
	private ManageBrandService managebrandservice;
	@Autowired
	private InventoryManagementService inventoryservice;
	@Autowired
	private LocalBusinessValidator businessValidator;
	@Autowired
	private ReportService reportService;
	@Autowired
	private CategoryService categoryService;

	private ControllerUtil controllerUtil = new ControllerUtil();

	@RequestMapping(value = "/admin-listings.htm", method = RequestMethod.GET)
	public String getBusinessListing(Model model, HttpSession session,
			HttpServletRequest request) {
		logger.info("start ::  getBusinessListing method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		controllerUtil.getChannels(model, session, service);
		int activeListSize = listOfBusinessInfo.size();
		List<CategoryDTO> categeoryListing = service.getCategeoryListing();
		model.addAttribute("categeoryListing", categeoryListing);
		/*
		 * List<BrandInfoDTO> brandListing = service.getBrandListing();
		 * model.addAttribute("brandListing", brandListing);
		 */
		List<BrandInfoDTO> listOfContrctedAcxiomPartners = inventoryservice
				.getListOfContrctedAcxiomPartners();
		String count = listOfContrctedAcxiomPartners.get(0).getCount();
		model.addAttribute("contractedpartners", count);
		List<BrandInfoDTO> listOfContrctedFactualPartners = inventoryservice
				.getListOfContrctedFactualPartners();
		String count1 = listOfContrctedFactualPartners.get(0).getCount();
		model.addAttribute("contractedFactualpartners", count1);
		List<BrandInfoDTO> listOfContrctedInfoGroupPartners = inventoryservice
				.getListOfContrctedInfoGroupPartners();
		String count2 = listOfContrctedInfoGroupPartners.get(0).getCount();
		model.addAttribute("contractedInfoGrouppartners", count2);
		List<BrandInfoDTO> listOfContrctedLocalezePartners = inventoryservice
				.getListOfContrctedLocalezePartners();
		String count3 = listOfContrctedLocalezePartners.get(0).getCount();
		model.addAttribute("contractedLocalezepartners", count3);
		List<BrandInfoDTO> allClientLocations = inventoryservice
				.getAllClientLocations();
		model.addAttribute("allClientLocations", allClientLocations);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		model.addAttribute("BrandInfo", new UsersBean());
		logger.info("BusinessInformation size == " + activeListSize);
		logger.info("end ::  getBusinessListing method");
		List<LocalBusinessDTO> listOfBrandsInfo = new ArrayList<LocalBusinessDTO>();

		Map<String, List<BrandInfoDTO>> allActiveBrands = service
				.getAllActiveBrands();
		Set<String> activeBrandNamesList = allActiveBrands.keySet();
		// boolean isAcxiom = false;
		// boolean isHeadersExist = false;
		List<BrandInfoDTO> brandListing = new ArrayList<BrandInfoDTO>();
		BrandInfoDTO brandInfoByBrandName = null;
		for (String brandName : activeBrandNamesList) {
			// BrandInfoDTO dto = new BrandInfoDTO();
			// dto.setBrandName(brandName);
			/* service.getBrandId(brandName); */
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);
			brandInfoByBrandName = service.getBrandInfoByBrandName(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			brandInfoByBrandName.setLocationCotracted(locationContracted
					.toString());
			// dto.setLocationsInvoiced(locationcount.toString());;
			// dto.setLocationCotracted(locationContracted.toString());
			brandListing.add(brandInfoByBrandName);

			// brandListing.add(dto);

		}
		model.addAttribute("brandListing", brandListing);
		model.addAttribute("brandInfoByBrandName", brandInfoByBrandName);
		for (String brandName : activeBrandNamesList) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dto.setBrands(brandName);
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			dto.setLocationCotracted(locationcount);
			dto.setLocationCount(locationContracted);
			listOfBrandsInfo.add(dto);

		}

		long totalSubmissions = 0;
		long possibleSubmissions = 0;

		for (String brandName : activeBrandNamesList) {

			List<BrandInfoDTO> brandInfoDTOs = allActiveBrands.get(brandName);
			for (BrandInfoDTO brandInfoDTO : brandInfoDTOs) {

				Integer uploadedListings = service
						.getlocationsByBrandName(brandInfoDTO.getBrandName());
				totalSubmissions += uploadedListings;
				possibleSubmissions += uploadedListings;
			}

		}
		model.addAttribute("totalSubmissions", totalSubmissions);
		model.addAttribute("possibleSubmissions", possibleSubmissions);

		/*
		 * List<LocalBusinessDTO> storeNames = service.getStore();
		 * model.addAttribute("storeNames", storeNames);
		 */
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		return "admin";
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

	@RequestMapping(value = "/deleteBrandInfo.htm", method = RequestMethod.POST)
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
		managebrandservice.deleteBrands(listIds);
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();

		int activeListSize = listOfBusinessInfo.size();
		List<CategoryDTO> categeoryListing = service.getCategeoryListing();
		model.addAttribute("categeoryListing", categeoryListing);
		/*
		 * List<BrandInfoDTO> brandListing = service.getBrandListing();
		 * model.addAttribute("brandListing", brandListing);
		 */
		List<BrandInfoDTO> listOfContrctedAcxiomPartners = inventoryservice
				.getListOfContrctedAcxiomPartners();
		String count = listOfContrctedAcxiomPartners.get(0).getCount();
		model.addAttribute("contractedpartners", count);
		List<BrandInfoDTO> listOfContrctedFactualPartners = inventoryservice
				.getListOfContrctedFactualPartners();
		String count1 = listOfContrctedFactualPartners.get(0).getCount();
		model.addAttribute("contractedFactualpartners", count1);
		List<BrandInfoDTO> listOfContrctedInfoGroupPartners = inventoryservice
				.getListOfContrctedInfoGroupPartners();
		String count2 = listOfContrctedInfoGroupPartners.get(0).getCount();
		model.addAttribute("contractedInfoGrouppartners", count2);
		List<BrandInfoDTO> listOfContrctedLocalezePartners = inventoryservice
				.getListOfContrctedLocalezePartners();
		String count3 = listOfContrctedLocalezePartners.get(0).getCount();
		model.addAttribute("contractedLocalezepartners", count3);
		List<BrandInfoDTO> allClientLocations = inventoryservice
				.getAllClientLocations();
		model.addAttribute("allClientLocations", allClientLocations);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		model.addAttribute("BrandInfo", new UsersBean());
		logger.info("BusinessInformation size == " + activeListSize);
		logger.info("end ::  getBusinessListing method");
		controllerUtil.getChannels(model, session, service);
		List<LocalBusinessDTO> listOfBrandsInfo = new ArrayList<LocalBusinessDTO>();

		Map<String, List<BrandInfoDTO>> allActiveBrands = service
				.getAllActiveBrands();
		Set<String> activeBrandNamesList = allActiveBrands.keySet();
		// boolean isAcxiom = false;
		// boolean isHeadersExist = false;

		for (String brandName : activeBrandNamesList) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dto.setBrands(brandName);
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			dto.setLocationCotracted(locationcount);
			dto.setLocationCount(locationContracted);
			listOfBrandsInfo.add(dto);

		}
		List<BrandInfoDTO> brandListing = new ArrayList<BrandInfoDTO>();
		for (String brandName : activeBrandNamesList) {
			// BrandInfoDTO dto = new BrandInfoDTO();
			// dto.setBrandName(brandName);
			/* service.getBrandId(brandName); */
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);
			BrandInfoDTO brandInfoByBrandName = service
					.getBrandInfoByBrandName(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			brandInfoByBrandName.setLocationCotracted(locationContracted
					.toString());
			// dto.setLocationsInvoiced(locationcount.toString());;
			// dto.setLocationCotracted(locationContracted.toString());
			brandListing.add(brandInfoByBrandName);

			// brandListing.add(dto);

		}
		model.addAttribute("brandListing", brandListing);

		long totalSubmissions = 0;
		long possibleSubmissions = 0;

		for (String brandName : activeBrandNamesList) {

			List<BrandInfoDTO> brandInfoDTOs = allActiveBrands.get(brandName);
			for (BrandInfoDTO brandInfoDTO : brandInfoDTOs) {

				Integer uploadedListings = service
						.getlocationsByBrandName(brandInfoDTO.getBrandName());
				totalSubmissions += uploadedListings;
				possibleSubmissions += uploadedListings;
			}

		}
		model.addAttribute("totalSubmissions", totalSubmissions);
		model.addAttribute("possibleSubmissions", possibleSubmissions);

		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		/*
		 * List<LocalBusinessDTO> storeNames = service.getStore();
		 * model.addAttribute("storeNames", storeNames);
		 */
		return "admin";
	}

	@RequestMapping(value = "/addBrand", method = RequestMethod.GET)
	public String getAddLocation(Model model, HttpSession httpSession) {
		logger.info("start :: getAddLocation method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}

		model.addAttribute("BrandInfo", new UsersBean());
		logger.info("end :: getAddLocation method");
		return "add-brand";
	}

	@RequestMapping(value = "/addBrand.htm", method = RequestMethod.POST)
	public String addBrands(Model model,
			@ModelAttribute("BrandInfo") UsersBean bean,
			HttpServletRequest request,
			@RequestPart("brandImage") MultipartFile brandImage,
			@RequestPart("channelImage") MultipartFile channelImage)
			throws ParseException {
		logger.info("start ::  addBrands method");
		HttpSession session = request.getSession();
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		ServletContext context = session.getServletContext();

		String cImage = "";
		String bImage = "";

		String mode = request.getParameter("saveType");
		if ("update".equalsIgnoreCase(mode)) {
			String brandImagePath = request.getParameter("bImagePath");
			String channelImagePath = request.getParameter("cImagePath");
			bean.setcImagePath(channelImagePath);
			bean.setbImagePath(brandImagePath);
		}

		if (!channelImage.isEmpty()) {
			String cPath = "/images/channelimages/";
			String channelImagesPath = context.getRealPath(cPath);
			try {
				String originalFilename = channelImage.getOriginalFilename();
				//System.out.println("channelImage: " + originalFilename);
				byte barr[] = channelImage.getBytes();

				BufferedOutputStream bout = new BufferedOutputStream(
						new FileOutputStream(channelImagesPath + "/"
								+ originalFilename));
				bout.write(barr);
				bout.flush();
				bout.close();

				cImage = cPath + originalFilename;
				cImage = cImage.substring(1);
				bean.setcImagePath(cImage);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!brandImage.isEmpty()) {
			try {
				String bPath = "/images/brandimages/";
				String brandImagesPath = context.getRealPath(bPath);
				String originalFilename = brandImage.getOriginalFilename();
				//System.out.println("brandImage: " + originalFilename);
				byte barr[] = brandImage.getBytes();

				BufferedOutputStream bout = new BufferedOutputStream(
						new FileOutputStream(brandImagesPath + "/"
								+ originalFilename));
				bout.write(barr);
				bout.flush();
				bout.close();
				bImage = bPath + originalFilename;
				bImage = bImage.substring(1);
				bean.setbImagePath(bImage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String message = "";
		Date startDateValue = DateUtil.getCurrentDate("MM/dd/yyyy HH:mm:ss");

		if (bean.getChannelName().trim().length() > 0
				&& bean.getBrandName().trim().length() > 0) {
			message = controllerUtil.saveOrUpdateBrandAndChannel(bean, message,
					startDateValue, service);
		} if (bean.getChannelName().trim().length() > 0) {
			controllerUtil.saveOrUpdateChannel(bean, startDateValue, message,
					service);
		} if (bean.getBrandName().trim().length() > 0) {
			Integer channelID = service.getChannelIdByName("Convergent Mobile");
			if (channelID == 0) {
				channelID = service.saveChannel("Convergent Mobile",
						startDateValue, bean.getcImagePath());
			}
			message = controllerUtil.saveOrUpdateBrand(bean, message,
					startDateValue, channelID, service);
		}

		//logger.info("mesassa" + message);
		model.addAttribute("message", message);
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();

		int activeListSize = listOfBusinessInfo.size();
		List<CategoryDTO> categeoryListing = service.getCategeoryListing();
		model.addAttribute("categeoryListing", categeoryListing);
		/*
		 * List<BrandInfoDTO> brandListing = service.getBrandListing();
		 * model.addAttribute("brandListing", brandListing);
		 */
		List<BrandInfoDTO> listOfContrctedAcxiomPartners = inventoryservice
				.getListOfContrctedAcxiomPartners();
		String count = listOfContrctedAcxiomPartners.get(0).getCount();
		model.addAttribute("contractedpartners", count);
		List<BrandInfoDTO> listOfContrctedFactualPartners = inventoryservice
				.getListOfContrctedFactualPartners();
		String count1 = listOfContrctedFactualPartners.get(0).getCount();
		model.addAttribute("contractedFactualpartners", count1);
		List<BrandInfoDTO> listOfContrctedInfoGroupPartners = inventoryservice
				.getListOfContrctedInfoGroupPartners();
		String count2 = listOfContrctedInfoGroupPartners.get(0).getCount();
		model.addAttribute("contractedInfoGrouppartners", count2);
		List<BrandInfoDTO> listOfContrctedLocalezePartners = inventoryservice
				.getListOfContrctedLocalezePartners();
		String count3 = listOfContrctedLocalezePartners.get(0).getCount();
		model.addAttribute("contractedLocalezepartners", count3);
		List<BrandInfoDTO> allClientLocations = inventoryservice
				.getAllClientLocations();
		model.addAttribute("allClientLocations", allClientLocations);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		model.addAttribute("brand", new UsersBean());
		logger.info("BusinessInformation size == " + activeListSize);
		controllerUtil.getRoles(model, service);
		controllerUtil.getChannels(model, session, service);
		controllerUtil.setUserAndBussinessDataToModel(model, request, service);
		controllerUtil.getChannels(model, session, service);
		logger.info("end ::  getBusinessListing method");
		List<LocalBusinessDTO> listOfBrandsInfo = new ArrayList<LocalBusinessDTO>();

		Map<String, List<BrandInfoDTO>> allActiveBrands = service
				.getAllActiveBrands();
		Set<String> activeBrandNamesList = allActiveBrands.keySet();
		// boolean isAcxiom = false;
		// boolean isHeadersExist = false;

		for (String brandName : activeBrandNamesList) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dto.setBrands(brandName);
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			dto.setLocationCotracted(locationcount);
			dto.setLocationCount(locationContracted);
			listOfBrandsInfo.add(dto);

		}
		List<BrandInfoDTO> brandListing = new ArrayList<BrandInfoDTO>();
		for (String brandName : activeBrandNamesList) {
			// BrandInfoDTO dto = new BrandInfoDTO();
			// dto.setBrandName(brandName);
			/* service.getBrandId(brandName); */
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);
			BrandInfoDTO brandInfoByBrandName = service
					.getBrandInfoByBrandName(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			brandInfoByBrandName.setLocationCotracted(locationContracted
					.toString());
			// dto.setLocationsInvoiced(locationcount.toString());;
			// dto.setLocationCotracted(locationContracted.toString());
			brandListing.add(brandInfoByBrandName);

			// brandListing.add(dto);

		}
		model.addAttribute("brandListing", brandListing);

		long totalSubmissions = 0;
		long possibleSubmissions = 0;

		for (String brandName : activeBrandNamesList) {

			List<BrandInfoDTO> brandInfoDTOs = allActiveBrands.get(brandName);
			for (BrandInfoDTO brandInfoDTO : brandInfoDTOs) {

				Integer uploadedListings = service
						.getlocationsByBrandName(brandInfoDTO.getBrandName());
				totalSubmissions += uploadedListings;
				possibleSubmissions += uploadedListings;
			}

		}
		model.addAttribute("totalSubmissions", totalSubmissions);
		model.addAttribute("possibleSubmissions", possibleSubmissions);
		model.addAttribute("BrandInfo", new UsersBean());
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		/*
		 * List<LocalBusinessDTO> storeNames = service.getStore();
		 * model.addAttribute("storeNames", storeNames);
		 */
		logger.info("end ::  addBrands method");
		return "admin";
	}

	@RequestMapping(value = "/editBrandInformation", method = RequestMethod.POST)
	public String editBusinessInfo(@RequestBody String beanStr, Model model,
			UsersBean bean, HttpSession session, HttpServletRequest request) {
		logger.info("start ::  editBusinessInfo method");
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
		session.setAttribute("currentUpdateIdIndex", 0);
		logger.info("Selected IDs == " + listIds);
		BrandInfoDTO businessInfo = null;
		if (listIds.size() > 0) {
			businessInfo = service.getBrandInfo(listIds.get(0));

		}
		logger.info("end ::  editBusinessInfo method");
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();

		int activeListSize = listOfBusinessInfo.size();
		List<CategoryDTO> categeoryListing = service.getCategeoryListing();
		model.addAttribute("categeoryListing", categeoryListing);
		/*
		 * List<BrandInfoDTO> brandListing = service.getBrandListing();
		 * model.addAttribute("brandListing", brandListing);
		 */
		List<BrandInfoDTO> listOfContrctedAcxiomPartners = inventoryservice
				.getListOfContrctedAcxiomPartners();
		String count = listOfContrctedAcxiomPartners.get(0).getCount();
		model.addAttribute("contractedpartners", count);
		List<BrandInfoDTO> listOfContrctedFactualPartners = inventoryservice
				.getListOfContrctedFactualPartners();
		String count1 = listOfContrctedFactualPartners.get(0).getCount();
		model.addAttribute("contractedFactualpartners", count1);
		List<BrandInfoDTO> listOfContrctedInfoGroupPartners = inventoryservice
				.getListOfContrctedInfoGroupPartners();
		String count2 = listOfContrctedInfoGroupPartners.get(0).getCount();
		model.addAttribute("contractedInfoGrouppartners", count2);
		List<BrandInfoDTO> listOfContrctedLocalezePartners = inventoryservice
				.getListOfContrctedLocalezePartners();
		String count3 = listOfContrctedLocalezePartners.get(0).getCount();
		model.addAttribute("contractedLocalezepartners", count3);
		List<BrandInfoDTO> allClientLocations = inventoryservice
				.getAllClientLocations();
		model.addAttribute("allClientLocations", allClientLocations);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		controllerUtil.getChannels(model, session, service);
		model.addAttribute("brandchannel", new UsersBean());
		logger.info("BusinessInformation size == " + activeListSize);
		List<LocalBusinessDTO> listOfBrandsInfo = new ArrayList<LocalBusinessDTO>();
		model.addAttribute("BrandInfo", new UsersBean());
		Map<String, List<BrandInfoDTO>> allActiveBrands = service
				.getAllActiveBrands();
		Set<String> activeBrandNamesList = allActiveBrands.keySet();
		// boolean isAcxiom = false;
		// boolean isHeadersExist = false;

		for (String brandName1 : activeBrandNamesList) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dto.setBrands(brandName1);
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName1);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName1);
			dto.setLocationCotracted(locationcount);
			dto.setLocationCount(locationContracted);
			listOfBrandsInfo.add(dto);

		}
		List<BrandInfoDTO> brandListing = new ArrayList<BrandInfoDTO>();
		for (String brandName2 : activeBrandNamesList) {
			// BrandInfoDTO dto = new BrandInfoDTO();
			// dto.setBrandName(brandName);
			/* service.getBrandId(brandName); */
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName2);
			BrandInfoDTO brandInfoByBrandName = service
					.getBrandInfoByBrandName(brandName2);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName2);
			brandInfoByBrandName.setLocationCotracted(locationContracted
					.toString());
			// dto.setLocationsInvoiced(locationcount.toString());;
			// dto.setLocationCotracted(locationContracted.toString());
			brandListing.add(brandInfoByBrandName);

			// brandListing.add(dto);

		}
		model.addAttribute("brandListing", brandListing);

		long totalSubmissions = 0;
		long possibleSubmissions = 0;

		for (String brandName3 : activeBrandNamesList) {

			List<BrandInfoDTO> brandInfoDTOs = allActiveBrands.get(brandName3);
			for (BrandInfoDTO brandInfoDTO : brandInfoDTOs) {

				Integer uploadedListings2 = service
						.getlocationsByBrandName(brandInfoDTO.getBrandName());
				totalSubmissions += uploadedListings2;
				possibleSubmissions += uploadedListings2;
			}

		}

		Integer chalID = businessInfo.getChannelID();
		ChannelNameDTO getchannelInfo = service.getchannelInfo(chalID);
		bean.setChannelName(getchannelInfo.getChannelName());
		bean.setcImagePath(getchannelInfo.getImagePath());
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String brandName = businessInfo.getBrandName();
		bean.setBrandName(brandName);
		Integer uploadedListings = service.getlocationsByBrandName(brandName);

		bean.setEmail(businessInfo.getEmail());
		bean.setLocationsInvoiced(businessInfo.getLocationsInvoiced());
		bean.setClientId(businessInfo.getClientId());
		String submisions = businessInfo.getSubmisions();
		logger.info("submisions::::::::::" + submisions);
		bean.setSubmisions(submisions);
		bean.setStartDate(sdf.format(businessInfo.getStartDate()));
		bean.setbImagePath(businessInfo.getImagePath());
		bean.setSaveType("update");
		model.addAttribute("BrandInfo", new UsersBean());
		model.addAttribute("BrandInfo", bean);
		model.addAttribute("cImagePath", bean.getcImagePath());
		model.addAttribute("bImagePath", bean.getbImagePath());
		model.addAttribute("count", uploadedListings);
		model.addAttribute("editbrandinadmin", "yes");
		model.addAttribute("totalSubmissions", totalSubmissions);
		model.addAttribute("possibleSubmissions", possibleSubmissions);
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		model.addAttribute("brandsInfo", listOfBrandsInfo);

		logger.info("end ::  getBusinessListing method");

		return "admin";

	}

	@RequestMapping(value = "/cancelRenewal.htm", method = RequestMethod.POST)
	public String cancelRenewal(@RequestBody String beanStr, Model model,
			UsersBean bean, HttpSession session, HttpServletRequest request) {
		logger.info("start ::  editBusinessInfo method");
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
		session.setAttribute("currentUpdateIdIndex", 0);
		logger.info("Selected IDs == " + listIds);
		BrandInfoDTO businessInfo = null;
		if (listIds.size() > 0) {
			businessInfo = service.getBrandInfo(listIds.get(0));

		}
		String brand = businessInfo.getBrandName();
		Integer clientId = businessInfo.getClientId();
		List<String> storeForBrand = reportService.getStoreForBrand(brand);
		for (String store : storeForBrand) {
			RenewalReportEntity renewalReportEntity = service
					.isStoreExistInRenewal(store, clientId);
			Date activeDate = renewalReportEntity.getActiveDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(activeDate);
			cal.add(Calendar.YEAR, 1);
			renewalReportEntity.setCancelledEffeciveDate(cal.getTime());
			renewalReportEntity.setStatus("cancel");
			service.updateRenewalInfo(renewalReportEntity);

		}

		logger.info("end ::  editBusinessInfo method");
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();

		int activeListSize = listOfBusinessInfo.size();
		List<CategoryDTO> categeoryListing = service.getCategeoryListing();
		model.addAttribute("categeoryListing", categeoryListing);
		/*
		 * List<BrandInfoDTO> brandListing = service.getBrandListing();
		 * model.addAttribute("brandListing", brandListing);
		 */
		List<BrandInfoDTO> listOfContrctedAcxiomPartners = inventoryservice
				.getListOfContrctedAcxiomPartners();
		String count = listOfContrctedAcxiomPartners.get(0).getCount();
		model.addAttribute("contractedpartners", count);
		List<BrandInfoDTO> listOfContrctedFactualPartners = inventoryservice
				.getListOfContrctedFactualPartners();
		String count1 = listOfContrctedFactualPartners.get(0).getCount();
		model.addAttribute("contractedFactualpartners", count1);
		List<BrandInfoDTO> listOfContrctedInfoGroupPartners = inventoryservice
				.getListOfContrctedInfoGroupPartners();
		String count2 = listOfContrctedInfoGroupPartners.get(0).getCount();
		model.addAttribute("contractedInfoGrouppartners", count2);
		List<BrandInfoDTO> listOfContrctedLocalezePartners = inventoryservice
				.getListOfContrctedLocalezePartners();
		String count3 = listOfContrctedLocalezePartners.get(0).getCount();
		model.addAttribute("contractedLocalezepartners", count3);
		List<BrandInfoDTO> allClientLocations = inventoryservice
				.getAllClientLocations();
		model.addAttribute("allClientLocations", allClientLocations);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		controllerUtil.getChannels(model, session, service);
		model.addAttribute("brandchannel", new UsersBean());
		logger.info("BusinessInformation size == " + activeListSize);
		List<LocalBusinessDTO> listOfBrandsInfo = new ArrayList<LocalBusinessDTO>();
		model.addAttribute("BrandInfo", new UsersBean());
		Map<String, List<BrandInfoDTO>> allActiveBrands = service
				.getAllActiveBrands();
		Set<String> activeBrandNamesList = allActiveBrands.keySet();
		// boolean isAcxiom = false;
		// boolean isHeadersExist = false;

		for (String brandName1 : activeBrandNamesList) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dto.setBrands(brandName1);
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName1);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName1);
			dto.setLocationCotracted(locationcount);
			dto.setLocationCount(locationContracted);
			listOfBrandsInfo.add(dto);

		}
		List<BrandInfoDTO> brandListing = new ArrayList<BrandInfoDTO>();
		for (String brandName2 : activeBrandNamesList) {

			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName2);
			BrandInfoDTO brandInfoByBrandName = service
					.getBrandInfoByBrandName(brandName2);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName2);
			brandInfoByBrandName.setLocationCotracted(locationContracted
					.toString());

			brandListing.add(brandInfoByBrandName);

		}
		model.addAttribute("brandListing", brandListing);

		long totalSubmissions = 0;
		long possibleSubmissions = 0;

		for (String brandName3 : activeBrandNamesList) {

			List<BrandInfoDTO> brandInfoDTOs = allActiveBrands.get(brandName3);
			for (BrandInfoDTO brandInfoDTO : brandInfoDTOs) {

				Integer uploadedListings2 = service
						.getlocationsByBrandName(brandInfoDTO.getBrandName());
				totalSubmissions += uploadedListings2;
				possibleSubmissions += uploadedListings2;
			}

		}

		Integer chalID = businessInfo.getChannelID();
		ChannelNameDTO getchannelInfo = service.getchannelInfo(chalID);
		bean.setChannelName(getchannelInfo.getChannelName());
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String brandName = brand;
		bean.setBrandName(brandName);
		Integer uploadedListings = service.getlocationsByBrandName(brandName);

		bean.setEmail(businessInfo.getEmail());
		bean.setLocationsInvoiced(businessInfo.getLocationsInvoiced());
		bean.setClientId(clientId);
		String submisions = businessInfo.getSubmisions();
		logger.info("submisions::::::::::" + submisions);
		bean.setSubmisions(submisions);
		bean.setStartDate(sdf.format(businessInfo.getStartDate()));
		// bean.setSaveType("update");
		model.addAttribute("BrandInfo", new UsersBean());
		model.addAttribute("BrandInfo", bean);
		model.addAttribute("count", uploadedListings);
		// model.addAttribute("editbrandinadmin", "yes");
		model.addAttribute("totalSubmissions", totalSubmissions);
		model.addAttribute("possibleSubmissions", possibleSubmissions);
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		model.addAttribute("brandsInfo", listOfBrandsInfo);

		logger.info("end ::  getBusinessListing method");

		return "admin";

	}

	@RequestMapping(value = "/brandsandChannelSearch.htm", method = RequestMethod.POST)
	public String brandDetailSearchInManageAccount(Model model,
			@ModelAttribute("brand") UsersBean bean,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  brandDetailSearchInManageAccount method");

		String channelName = bean.getChannelName();
		logger.info("channelName:::::::::::::::::::" + channelName);
		String brandName1 = bean.getBrandName();
		logger.info("brandName::::::::::::::" + brandName1);

		model.addAttribute("bandNameSearch", "yes");
		controllerUtil.getRoles(model, service);
		controllerUtil.setUserAndBussinessDataToModel(model, request, service);
		controllerUtil.getChannels(model, session, service);
		List<BrandInfoDTO> brandInfo = new ArrayList<BrandInfoDTO>();
		BrandInfoDTO branddeatils = null;
		if (!channelName.isEmpty() && !brandName1.isEmpty()) {
			branddeatils = service.getBrandInfoByBrandName(brandName1,
					channelName);

		} else if (channelName.isEmpty()) {
			branddeatils = service.getBrandInfoByBrand(brandName1);
		} else if (brandName1.isEmpty()) {
			branddeatils = service.getBrandInfoByChannel(channelName);
		}
		brandInfo.add(branddeatils);

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();

		int activeListSize = listOfBusinessInfo.size();
		List<CategoryDTO> categeoryListing = service.getCategeoryListing();
		model.addAttribute("categeoryListing", categeoryListing);

		List<BrandInfoDTO> listOfContrctedAcxiomPartners = inventoryservice
				.getListOfContrctedAcxiomPartners();
		String count = listOfContrctedAcxiomPartners.get(0).getCount();
		model.addAttribute("contractedpartners", count);
		List<BrandInfoDTO> listOfContrctedFactualPartners = inventoryservice
				.getListOfContrctedFactualPartners();
		String count1 = listOfContrctedFactualPartners.get(0).getCount();
		model.addAttribute("contractedFactualpartners", count1);
		List<BrandInfoDTO> listOfContrctedInfoGroupPartners = inventoryservice
				.getListOfContrctedInfoGroupPartners();
		String count2 = listOfContrctedInfoGroupPartners.get(0).getCount();
		model.addAttribute("contractedInfoGrouppartners", count2);
		List<BrandInfoDTO> listOfContrctedLocalezePartners = inventoryservice
				.getListOfContrctedLocalezePartners();
		String count3 = listOfContrctedLocalezePartners.get(0).getCount();
		model.addAttribute("contractedLocalezepartners", count3);
		List<BrandInfoDTO> allClientLocations = inventoryservice
				.getAllClientLocations();
		model.addAttribute("allClientLocations", allClientLocations);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		controllerUtil.getChannels(model, session, service);

		model.addAttribute("BrandInfo", new UsersBean());
		logger.info("BusinessInformation size == " + activeListSize);
		List<LocalBusinessDTO> listOfBrandsInfo = new ArrayList<LocalBusinessDTO>();

		Map<String, List<BrandInfoDTO>> allActiveBrands = service
				.getAllActiveBrands();
		Set<String> activeBrandNamesList = allActiveBrands.keySet();
		// boolean isAcxiom = false;
		// boolean isHeadersExist = false;

		for (String brandName : activeBrandNamesList) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dto.setBrands(brandName);
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			dto.setLocationCotracted(locationcount);
			dto.setLocationCount(locationContracted);
			listOfBrandsInfo.add(dto);

		}
		/*
		 * List<BrandInfoDTO> brandListing = new ArrayList<BrandInfoDTO>(); for
		 * (String brandName : activeBrandNamesList) { //BrandInfoDTO dto = new
		 * BrandInfoDTO(); //dto.setBrandName(brandName);
		 * service.getBrandId(brandName); Integer locationcount = service
		 * .getlocationInvoicedByBrandname(brandName); BrandInfoDTO
		 * brandInfoByBrandName = service.getBrandInfoByBrandName(brandName);
		 * 
		 * Integer locationContracted = service
		 * .getlocationsByBrandName(brandName);
		 * brandInfoByBrandName.setLocationCotracted
		 * (locationContracted.toString());
		 * //dto.setLocationsInvoiced(locationcount.toString());;
		 * //dto.setLocationCotracted(locationContracted.toString());
		 * brandListing.add(brandInfoByBrandName);
		 * 
		 * //brandListing.add(dto);
		 * 
		 * }
		 */
		model.addAttribute("brandListing", brandInfo);
		long totalSubmissions = 0;
		long possibleSubmissions = 0;

		for (String brandName : activeBrandNamesList) {

			List<BrandInfoDTO> brandInfoDTOs = allActiveBrands.get(brandName);
			for (BrandInfoDTO brandInfoDTO : brandInfoDTOs) {

				Integer uploadedListings = service
						.getlocationsByBrandName(brandInfoDTO.getBrandName());
				totalSubmissions += uploadedListings;
				possibleSubmissions += uploadedListings;
			}

		}
		model.addAttribute("totalSubmissions", totalSubmissions);
		model.addAttribute("possibleSubmissions", possibleSubmissions);
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		/*
		 * List<LocalBusinessDTO> storeNames = service.getStore();
		 * model.addAttribute("storeNames", storeNames);
		 */
		logger.info("end ::  getBusinessListing method");
		return "admin";
	}

	@RequestMapping(value = "/brandcategeroyserach.htm", method = RequestMethod.GET)
	public String getBrandCategoryListing(Model model, HttpSession session,
			HttpServletRequest request) {
		logger.info("start ::  getBusinessListing method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String clinetname = request.getParameter("clinetname");
		List<CategoryDTO> categeoryListing = service
				.getCategeoryListingByBrand(clinetname);
		/*
		 * List<LocalBusinessDTO> storeNames =
		 * service.getStoresByBrandName(clinetname);
		 * model.addAttribute("storeNames", storeNames);
		 */
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		controllerUtil.getChannels(model, session, service);
		int activeListSize = listOfBusinessInfo.size();

		model.addAttribute("categeoryListing", categeoryListing);
		/*
		 * List<BrandInfoDTO> brandListing = service.getBrandListing();
		 * model.addAttribute("brandListing", brandListing);
		 */
		List<BrandInfoDTO> listOfContrctedAcxiomPartners = inventoryservice
				.getListOfContrctedAcxiomPartners();
		String count = listOfContrctedAcxiomPartners.get(0).getCount();
		model.addAttribute("contractedpartners", count);
		List<BrandInfoDTO> listOfContrctedFactualPartners = inventoryservice
				.getListOfContrctedFactualPartners();
		String count1 = listOfContrctedFactualPartners.get(0).getCount();
		model.addAttribute("contractedFactualpartners", count1);
		List<BrandInfoDTO> listOfContrctedInfoGroupPartners = inventoryservice
				.getListOfContrctedInfoGroupPartners();
		String count2 = listOfContrctedInfoGroupPartners.get(0).getCount();
		model.addAttribute("contractedInfoGrouppartners", count2);
		List<BrandInfoDTO> listOfContrctedLocalezePartners = inventoryservice
				.getListOfContrctedLocalezePartners();
		String count3 = listOfContrctedLocalezePartners.get(0).getCount();
		model.addAttribute("contractedLocalezepartners", count3);
		List<BrandInfoDTO> allClientLocations = inventoryservice
				.getAllClientLocations();
		model.addAttribute("allClientLocations", allClientLocations);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		model.addAttribute("BrandInfo", new UsersBean());
		List<LocalBusinessDTO> listOfBrandsInfo = new ArrayList<LocalBusinessDTO>();

		Map<String, List<BrandInfoDTO>> allActiveBrands = service
				.getAllActiveBrands();
		Set<String> activeBrandNamesList = allActiveBrands.keySet();
		// boolean isAcxiom = false;
		// boolean isHeadersExist = false;

		for (String brandName : activeBrandNamesList) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dto.setBrands(brandName);
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			dto.setLocationCotracted(locationcount);
			dto.setLocationCount(locationContracted);
			listOfBrandsInfo.add(dto);

		}
		List<BrandInfoDTO> brandListing = new ArrayList<BrandInfoDTO>();
		for (String brandName : activeBrandNamesList) {
			// BrandInfoDTO dto = new BrandInfoDTO();
			// dto.setBrandName(brandName);
			/* service.getBrandId(brandName); */
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);
			BrandInfoDTO brandInfoByBrandName = service
					.getBrandInfoByBrandName(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			brandInfoByBrandName.setLocationCotracted(locationContracted
					.toString());
			// dto.setLocationsInvoiced(locationcount.toString());;
			// dto.setLocationCotracted(locationContracted.toString());
			brandListing.add(brandInfoByBrandName);

			// brandListing.add(dto);

		}
		model.addAttribute("brandListing", brandListing);

		long totalSubmissions = 0;
		long possibleSubmissions = 0;

		for (String brandName : activeBrandNamesList) {

			List<BrandInfoDTO> brandInfoDTOs = allActiveBrands.get(brandName);
			for (BrandInfoDTO brandInfoDTO : brandInfoDTOs) {

				Integer uploadedListings = service
						.getlocationsByBrandName(brandInfoDTO.getBrandName());
				totalSubmissions += uploadedListings;
				possibleSubmissions += uploadedListings;
			}

		}
		model.addAttribute("totalSubmissions", totalSubmissions);
		model.addAttribute("possibleSubmissions", possibleSubmissions);
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		/*
		 * List<LocalBusinessDTO> storeNames = service.getStore();
		 * model.addAttribute("storeNames", storeNames);
		 */
		logger.info("BusinessInformation size == " + activeListSize);
		logger.info("end ::  getBusinessListing method");

		return "admin";
	}

	@RequestMapping(value = "/brandinventoryserach.htm", method = RequestMethod.GET)
	public @ResponseBody
	String listingsForBrand(
			@RequestParam(value = "clinetname", required = true) String clinetname,
			Model model) throws IllegalStateException, SystemException,
			JsonGenerationException, JsonMappingException, IOException {
		String json = null;
		ObjectMapper objectMapper = new ObjectMapper();
		logger.info("clinetname is:" + clinetname);
		List<LocalBusinessDTO> storeNames = service
				.getStoresByBrandName(clinetname);

		logger.info("Total Stores found for Brand are:" + storeNames.size());

		model.addAttribute("storeNames", storeNames);
		json = objectMapper.writeValueAsString(storeNames);
		return json;
	}

	@RequestMapping(value = "/getInactiveBrands.htm", method = RequestMethod.GET)
	public @ResponseBody
	String inActiveBrands(
			@RequestParam(value = "brandid", required = true) String brandid,
			Model model) throws IllegalStateException, SystemException,
			JsonGenerationException, JsonMappingException, IOException {
		String json = null;
		ObjectMapper objectMapper = new ObjectMapper();
		logger.info("brandid is:" + brandid);
		List<BrandInfoDTO> inactivenrands = service.getInActiveBrands(Integer
				.parseInt(brandid));

		logger.info("Total inactive brands found for Brand are:"
				+ inactivenrands.size());

		model.addAttribute("inactivenrands", inactivenrands);
		json = objectMapper.writeValueAsString(inactivenrands);
		return json;
	}

	@RequestMapping(value = "/businessCategory.htm", method = RequestMethod.POST)
	public String uploadCategories(
			@ModelAttribute("CatgoryBusiness") CategoryBean bean, Model model,
			HttpServletRequest request, HttpSession session,
			HSSFWorkbook workbook, HttpServletResponse response) {

		CommonsMultipartFile file = bean.getTargetFile();
		logger.info("File Name == " + file.getOriginalFilename());
		String fName = file.getOriginalFilename();
		logger.info("File Name : " + fName);

		Map<String, Long> brandsCountsMap = new HashMap<String, Long>();

		String headerpopup = "";
		List<CategoryBean> listDataFromXLS = getListDataFromXLS(file,
				brandsCountsMap, headerpopup);
		for (CategoryBean categoryBean : listDataFromXLS) {
			Integer clientId = categoryBean.getClientId();
			boolean clientIdExistis = service.isClientIdExistis(clientId);
			logger.info("clientIdExistis" + clientIdExistis);
			if (clientIdExistis) {
				categoryService.deleteCategoryInfo(clientId);

			}
		}
		int size = listDataFromXLS.size();
		logger.info("size:::::::::::" + size);
		if (listDataFromXLS.isEmpty() || size < 2) {
			headerpopup = "Invalid Category Conversion  Template";
			model.addAttribute("headerPopup", headerpopup);
		}

		if (listDataFromXLS != null && size > 0) {
			categoryService.saveCategoryInfo(listDataFromXLS);
		}

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		controllerUtil.getChannels(model, session, service);
		int activeListSize = listOfBusinessInfo.size();
		List<CategoryDTO> categeoryListing = service.getCategeoryListing();
		model.addAttribute("categeoryListing", categeoryListing);
		/*
		 * List<BrandInfoDTO> brandListing = service.getBrandListing();
		 * model.addAttribute("brandListing", brandListing);
		 */
		List<BrandInfoDTO> listOfContrctedAcxiomPartners = inventoryservice
				.getListOfContrctedAcxiomPartners();
		String count = listOfContrctedAcxiomPartners.get(0).getCount();
		model.addAttribute("contractedpartners", count);
		List<BrandInfoDTO> listOfContrctedFactualPartners = inventoryservice
				.getListOfContrctedFactualPartners();
		String count1 = listOfContrctedFactualPartners.get(0).getCount();
		model.addAttribute("contractedFactualpartners", count1);
		List<BrandInfoDTO> listOfContrctedInfoGroupPartners = inventoryservice
				.getListOfContrctedInfoGroupPartners();
		String count2 = listOfContrctedInfoGroupPartners.get(0).getCount();
		model.addAttribute("contractedInfoGrouppartners", count2);
		List<BrandInfoDTO> listOfContrctedLocalezePartners = inventoryservice
				.getListOfContrctedLocalezePartners();
		String count3 = listOfContrctedLocalezePartners.get(0).getCount();
		model.addAttribute("contractedLocalezepartners", count3);
		List<BrandInfoDTO> allClientLocations = inventoryservice
				.getAllClientLocations();
		model.addAttribute("allClientLocations", allClientLocations);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		model.addAttribute("BrandInfo", new UsersBean());
		List<LocalBusinessDTO> listOfBrandsInfo = new ArrayList<LocalBusinessDTO>();

		Map<String, List<BrandInfoDTO>> allActiveBrands = service
				.getAllActiveBrands();
		Set<String> activeBrandNamesList = allActiveBrands.keySet();
		// boolean isAcxiom = false;
		// boolean isHeadersExist = false;

		for (String brandName : activeBrandNamesList) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dto.setBrands(brandName);
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			dto.setLocationCotracted(locationcount);
			dto.setLocationCount(locationContracted);
			listOfBrandsInfo.add(dto);

		}
		List<BrandInfoDTO> brandListing = new ArrayList<BrandInfoDTO>();
		for (String brandName : activeBrandNamesList) {
			// BrandInfoDTO dto = new BrandInfoDTO();
			// dto.setBrandName(brandName);
			/* service.getBrandId(brandName); */
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);
			BrandInfoDTO brandInfoByBrandName = service
					.getBrandInfoByBrandName(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			brandInfoByBrandName.setLocationCotracted(locationContracted
					.toString());
			// dto.setLocationsInvoiced(locationcount.toString());;
			// dto.setLocationCotracted(locationContracted.toString());
			brandListing.add(brandInfoByBrandName);

			// brandListing.add(dto);

		}
		model.addAttribute("brandListing", brandListing);
		long totalSubmissions = 0;
		long possibleSubmissions = 0;

		for (String brandName : activeBrandNamesList) {

			List<BrandInfoDTO> brandInfoDTOs = allActiveBrands.get(brandName);
			for (BrandInfoDTO brandInfoDTO : brandInfoDTOs) {

				Integer uploadedListings = service
						.getlocationsByBrandName(brandInfoDTO.getBrandName());
				totalSubmissions += uploadedListings;
				possibleSubmissions += uploadedListings;
			}

		}
		model.addAttribute("BrandInfo", new UsersBean());
		model.addAttribute("totalSubmissions", totalSubmissions);
		model.addAttribute("possibleSubmissions", possibleSubmissions);
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		logger.info("BusinessInformation size == " + activeListSize);
		logger.info("size:::::::::::" + size);
		logger.info("end ::  getBusinessListing method");
		/*
		 * List<LocalBusinessDTO> storeNames = service.getStore();
		 * model.addAttribute("storeNames", storeNames);
		 */
		return "admin";

	}

	private List<CategoryBean> getListDataFromXLS(CommonsMultipartFile file,
			Map<String, Long> brandsCountsMap, String headerpopup) {
		List<CategoryBean> tListData = new ArrayList<CategoryBean>();
		try {
			Sheet sheet = (Sheet) WorkbookFactory.create(file.getInputStream())
					.getSheetAt(0);
			DataFormatter df = new DataFormatter();
			Iterator<Row> rowIterator = sheet.rowIterator();
			boolean header = true;
			while (rowIterator.hasNext()) {
				Row row = (Row) rowIterator.next();
				if (header) {
					header = false;
					headerpopup = "";
					if (!headersValid(row, df)) {
						headerpopup = "Invalid Category Conversion  Template";
						return tListData;
					}
					continue;
				}

				CategoryBean categoryBean = new CategoryBean();

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();
					if (cell == null) // Process only the rows with some data
						continue;
					org.apache.commons.beanutils.BeanUtils.setProperty(
							categoryBean,
							LBLConstants.UPLOAD_CATEGORY_PROPERTIES[cell
									.getColumnIndex()], df
									.formatCellValue(cell));

				}
				tListData.add(categoryBean);
			}
		} catch (Exception e) {
			logger.error("Exception : " + e);
			e.printStackTrace();
		}

		logger.info("Excel sheet Records size == " + tListData.size());
		logger.info(tListData);
		return tListData;
	}

	/**
	 * headersValidation
	 * 
	 * @param row
	 * @param df
	 * @return
	 */
	public boolean headersValid(Row row, DataFormatter df) {
		Iterator<Cell> cellIterator = row.cellIterator();
		boolean isHeadersValid = true;
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if (cell == null
					|| (!df.formatCellValue(cell).equalsIgnoreCase(
							LBLConstants.UPLOAD_CATGORY_HEADERS[cell
									.getColumnIndex()]))) {
				isHeadersValid = false;
				break;
			}
		}
		return isHeadersValid;
	}

	@RequestMapping(value = "/dashListingsearch.htm", method = RequestMethod.POST)
	public String getSearchParametersFromDashBoard(Model model,
			@ModelAttribute("searchBusiness") LocalBusinessDTO businessDTO,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  getSearchParametersFromDashBoard method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		String store = businessDTO.getStore();
		String brands = businessDTO.getClient();
		logger.info("store:::::::::::::" + store);
		logger.info("brands:::::::::::::" + brands);

		Set<LocalBusinessDTO> searchBusinessinfo = service
				.searchBusinessListinginfo(store, brands);
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
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		logger.info("end ::  getSearchParametersFromDashBoard method");
		return "business-listings";
	}

	@RequestMapping(value = "/dashStoreListingsearch.htm", method = RequestMethod.POST)
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
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		logger.info("end ::  getSearchParametersFromDashBoard method");
		return "business-listings";
	}

	@RequestMapping(value = "inactive-brands.htm", method = RequestMethod.POST)
	public String getInactiveBrandsCmAdmin(@RequestBody String beanStr,
			Model model, UsersBean bean, HttpSession session,
			HttpServletRequest request) {
		logger.info("start ::  Inactive brands method");
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

		if (listIds.size() > 0) {
			service.updateBrandInfo(listIds.get(0));

		}
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		controllerUtil.getChannels(model, session, service);
		int activeListSize = listOfBusinessInfo.size();
		List<CategoryDTO> categeoryListing = service.getCategeoryListing();
		model.addAttribute("categeoryListing", categeoryListing);
		/*
		 * List<BrandInfoDTO> brandListing = service.getBrandListing();
		 * model.addAttribute("brandListing", brandListing);
		 */
		List<BrandInfoDTO> listOfContrctedAcxiomPartners = inventoryservice
				.getListOfContrctedAcxiomPartners();
		String count = listOfContrctedAcxiomPartners.get(0).getCount();
		model.addAttribute("contractedpartners", count);
		List<BrandInfoDTO> listOfContrctedFactualPartners = inventoryservice
				.getListOfContrctedFactualPartners();
		String count1 = listOfContrctedFactualPartners.get(0).getCount();
		model.addAttribute("contractedFactualpartners", count1);
		List<BrandInfoDTO> listOfContrctedInfoGroupPartners = inventoryservice
				.getListOfContrctedInfoGroupPartners();
		String count2 = listOfContrctedInfoGroupPartners.get(0).getCount();
		model.addAttribute("contractedInfoGrouppartners", count2);
		List<BrandInfoDTO> listOfContrctedLocalezePartners = inventoryservice
				.getListOfContrctedLocalezePartners();
		String count3 = listOfContrctedLocalezePartners.get(0).getCount();
		model.addAttribute("contractedLocalezepartners", count3);
		List<BrandInfoDTO> allClientLocations = inventoryservice
				.getAllClientLocations();
		model.addAttribute("allClientLocations", allClientLocations);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.getUploadDropDown(model, request, service);
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		model.addAttribute("BrandInfo", new UsersBean());
		logger.info("BusinessInformation size == " + activeListSize);
		logger.info("end ::  getBusinessListing method");
		List<LocalBusinessDTO> listOfBrandsInfo = new ArrayList<LocalBusinessDTO>();

		Map<String, List<BrandInfoDTO>> allActiveBrands = service
				.getAllActiveBrands();
		Set<String> activeBrandNamesList = allActiveBrands.keySet();
		// boolean isAcxiom = false;
		// boolean isHeadersExist = false;
		List<BrandInfoDTO> brandListing = new ArrayList<BrandInfoDTO>();
		for (String brandName : activeBrandNamesList) {
			// BrandInfoDTO dto = new BrandInfoDTO();
			// dto.setBrandName(brandName);
			/* service.getBrandId(brandName); */
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);
			BrandInfoDTO brandInfoByBrandName = service
					.getBrandInfoByBrandName(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			brandInfoByBrandName.setLocationCotracted(locationContracted
					.toString());
			// dto.setLocationsInvoiced(locationcount.toString());;
			// dto.setLocationCotracted(locationContracted.toString());
			brandListing.add(brandInfoByBrandName);

			// brandListing.add(dto);

		}
		model.addAttribute("brandListing", brandListing);
		for (String brandName : activeBrandNamesList) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dto.setBrands(brandName);
			Integer locationcount = service
					.getlocationInvoicedByBrandname(brandName);

			Integer locationContracted = service
					.getlocationsByBrandName(brandName);
			dto.setLocationCotracted(locationcount);
			dto.setLocationCount(locationContracted);
			listOfBrandsInfo.add(dto);

		}

		long totalSubmissions = 0;
		long possibleSubmissions = 0;

		for (String brandName : activeBrandNamesList) {

			List<BrandInfoDTO> brandInfoDTOs = allActiveBrands.get(brandName);
			for (BrandInfoDTO brandInfoDTO : brandInfoDTOs) {

				Integer uploadedListings = service
						.getlocationsByBrandName(brandInfoDTO.getBrandName());
				totalSubmissions += uploadedListings;
				possibleSubmissions += uploadedListings;
			}

		}
		model.addAttribute("totalSubmissions", totalSubmissions);
		model.addAttribute("possibleSubmissions", possibleSubmissions);

		/*
		 * List<LocalBusinessDTO> storeNames = service.getStore();
		 * model.addAttribute("storeNames", storeNames);
		 */
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		model.addAttribute("brandsInfo", listOfBrandsInfo);
		return "admin";

	}

	@RequestMapping(value = "custom-submissions.htm", method = RequestMethod.GET)
	public String getCustomSubmissions(Model model, HttpServletRequest request,
			HttpSession session) {
		logger.info("start ::  getSearchParametersFromDashBoard method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		List<CustomSubmissionsBean> submissionsBean = service
				.getCustomSubmissions();
		controllerUtil.getChannels(model, session, service);
		controllerUtil.getUploadDropDown(model, request, service);
		model.addAttribute("BrandInfo", new UsersBean());
		model.addAttribute("submissionsBean", submissionsBean);
		return "custom-submissions";

	}

	@RequestMapping(value = "editSubmissionInformation", method = RequestMethod.POST)
	public String editCustomSubmissions(Model model,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  editCustomSubmissions method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String submissionId = request.getParameter("submissionId");
		logger.info("submissionId:::::::::::::::::::" + submissionId);

		String brandName = null;
		String channelName = null;

		List<CustomSubmissionsBean> submissionsBean = service
				.getCustomSubmissions();
		for (CustomSubmissionsBean customSubmissionsBean : submissionsBean) {
			if (customSubmissionsBean.getSubmissionId() == Integer
					.parseInt(submissionId)) {
				brandName = customSubmissionsBean.getBrandName();
				channelName = customSubmissionsBean.getChannelName();
				break;
			}
		}
		model.addAttribute("editsubmission", "yes");
		model.addAttribute("brand", brandName);
		model.addAttribute("channel", channelName);
		model.addAttribute("submissionId", submissionId);
		model.addAttribute("submissionsBean", submissionsBean);
		model.addAttribute("BrandInfo", new UsersBean());
		return "custom-submissions";

	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/customsubmission.htm", method = RequestMethod.POST)
	public String saveCustomSubmissions(HttpServletRequest request, Model model) {

		String submissionId = request.getParameter("submissionId");

		System.out.println("submissionId: " + submissionId);

		String acxiomtime = null;
		String acxionfreq = request.getParameter("acxiomfreq");
		logger.info("acxionfreq::::" + acxionfreq);
		String acEtime = request.getParameter("acxiomEtime");
		String acWtime = request.getParameter("acxiomWtime");
		String acMtime = request.getParameter("acxiomMtime");

		String factualTime = null;
		String factualfreq = request.getParameter("factualfreq");
		String factualEtime = request.getParameter("factualEtime");
		String factualWtime = request.getParameter("factualWtime");
		String factualMtime = request.getParameter("factualMtime");

		String infogroupTime = null;
		String infogroupfreq = request.getParameter("infogroupfreq");
		String infogroupEtime = request.getParameter("infogroupEtime");
		String infogroupWtime = request.getParameter("infogroupWtime");
		String infogroupMtime = request.getParameter("infogroupMtime");

		String localezeTime = null;
		String localezefreq = request.getParameter("localezefreq");
		logger.info("localezefreq::::" + localezefreq);
		String localezeEtime = request.getParameter("localezeEtime");
		String localezeWtime = request.getParameter("localezeWtime");
		String localezeMtime = request.getParameter("localezeMtime");

		if (acEtime != null && acEtime != "") {
			acxiomtime = acEtime;
		} else if (acWtime != null && acWtime != "") {
			acxiomtime = acWtime;
		} else if (acMtime != null && acMtime != "") {
			acxiomtime = acMtime;
		}

		if (factualEtime != null && factualEtime != "") {
			factualTime = factualEtime;
		} else if (factualWtime != null && factualWtime != "") {
			factualTime = factualWtime;
		} else if (factualMtime != null && factualMtime != "") {
			factualTime = factualMtime;
		}

		if (infogroupEtime != null && infogroupEtime != "") {
			infogroupTime = infogroupEtime;
		} else if (infogroupWtime != null && infogroupWtime != "") {
			infogroupTime = infogroupWtime;
		} else if (infogroupMtime != null && infogroupMtime != "") {
			infogroupTime = infogroupMtime;
		}

		if (localezeEtime != null && localezeEtime != "") {
			localezeTime = localezeEtime;
		} else if (localezeWtime != null && localezeWtime != "") {
			localezeTime = localezeWtime;
		} else if (localezeMtime != null && localezeMtime != "") {
			localezeTime = localezeMtime;
		}

		if (acxionfreq.equalsIgnoreCase("Daily")) {
			acxiomtime = "Everyday";
		}
		if (localezefreq.equalsIgnoreCase("Daily")) {
			localezeTime = "Everyday";
		}
		if (infogroupfreq.equalsIgnoreCase("Daily")) {
			infogroupTime = "Everyday";
		}
		if (factualfreq.equalsIgnoreCase("Daily")) {
			factualTime = "Everyday";
		}

		/*
		 * System.out.println("localeze frequency:" + localezefreq + " Time:" +
		 * localezeTime); System.out.println("Fatual Frequency: " + factualfreq
		 * + " Time: " + factualTime); System.out.println(" acxiom frequency:" +
		 * acxionfreq + "timeing:" + acxiomtime);
		 * System.out.println("infogroup frequency:" + infogroupfreq + " Time:"
		 * + infogroupTime);
		 */

		CustomSubmissionsDTO customSubmissionsDTO = service
				.getCustomSubmissions(Integer.parseInt(submissionId));

		CustomSubmissionsDTO dto = new CustomSubmissionsDTO();
		dto.setSubmissionId(Integer.parseInt(submissionId));
		dto.setBrandId(customSubmissionsDTO.getBrandId());
		dto.setBrandName(customSubmissionsDTO.getBrandName());
		dto.setChannelId(customSubmissionsDTO.getChannelId());
		dto.setChannelName(customSubmissionsDTO.getChannelName());
		dto.setAcxiomFrequency(acxionfreq);
		dto.setAcxiomTiming(acxiomtime);
		dto.setFactualFrequency(factualfreq);
		dto.setFactualTiming(factualTime);

		Date actiomScheduledDate = DateUtil.getDateByDayOfWeek(
				"MM/dd/yyyy HH:mm:ss", acxionfreq, acxiomtime);

		Date factualScheduledDate = DateUtil.getDateByDayOfWeek(
				"MM/dd/yyyy HH:mm:ss", factualfreq, factualTime);

		Date infoGroupScheduledDate = DateUtil.getDateByDayOfWeek(
				"MM/dd/yyyy HH:mm:ss", infogroupfreq, infogroupTime);

		Date localezeScheduledDate = DateUtil.getDateByDayOfWeek(
				"MM/dd/yyyy HH:mm:ss", localezefreq, localezeTime);

		dto.setFactualScheduledDate(factualScheduledDate);
		dto.setInfogroupScheduledDate(infoGroupScheduledDate);
		dto.setAcxiomScheduledDate(actiomScheduledDate);
		dto.setLocalezeScheduledDate(localezeScheduledDate);
		dto.setInfogroupFrequency(infogroupfreq);
		dto.setInfogroupTiming(infogroupTime);
		dto.setLocalezeFrequency(localezefreq);
		dto.setLocalezeTiming(localezeTime);
		service.saveCustomSubmissions(dto);

		/*
		 * System.out.println("frequency:" + localezefreq + "timeing:" +
		 * localezeTime);
		 */
		List<CustomSubmissionsBean> submissionsBean = service
				.getCustomSubmissions();
		model.addAttribute("BrandInfo", new UsersBean());
		model.addAttribute("submissionsBean", submissionsBean);
		return "custom-submissions";

	}

	@RequestMapping(value = "brandChannelSearch.htm", method = RequestMethod.POST)
	public String brandandchannelsearchincustomsubmission(Model model,
			@ModelAttribute("brand") UsersBean bean,
			HttpServletRequest request, HttpSession session) {

		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String channelName = bean.getChannelName();
		logger.info("channelName:::::::::::::::::::" + channelName);
		String brandName1 = bean.getBrandName();
		logger.info("brandName::::::::::::::" + brandName1);
		/*
		 * List<BrandInfoDTO> brandInfo = new ArrayList<BrandInfoDTO>();
		 * BrandInfoDTO branddeatils = null; if (!channelName.isEmpty() &&
		 * !brandName1.isEmpty()) { branddeatils =
		 * service.getBrandInfoByBrandName(brandName1, channelName);
		 * 
		 * } else if (channelName.isEmpty()) { branddeatils =
		 * service.getBrandInfoByBrand(brandName1); } else if
		 * (brandName1.isEmpty()) { branddeatils =
		 * service.getBrandInfoByChannel(channelName); }
		 * brandInfo.add(branddeatils);
		 */

		List<CustomSubmissionsBean> submissionsBean = service
				.getSearchBrandandChannelDetails(channelName, brandName1);
		controllerUtil.getChannels(model, session, service);
		controllerUtil.getUploadDropDown(model, request, service);
		model.addAttribute("BrandInfo", new UsersBean());
		model.addAttribute("submissionsBean", submissionsBean);
		return "custom-submissions";

	}

}
