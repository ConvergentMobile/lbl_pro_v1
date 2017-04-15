package com.business.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.ControllerUtil;
import com.business.service.BusinessService;
import com.business.service.InventoryManagementService;
import com.business.web.bean.UsersBean;

@Controller
public class InventoryManagementController {
	
	Logger logger = Logger.getLogger(BusinessController.class);

	@Autowired
	private InventoryManagementService service;
	@Autowired
	private BusinessService businessService;

	private ControllerUtil controllerUtil = new ControllerUtil();
	/**
	 * InventoryManagementPage
	 * 
	 * @param model
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/inventory.htm", method = RequestMethod.GET)
	public String inventoryManagementPage(Model model,
			HttpSession session, HttpServletRequest request) {
		logger.info("start ::  Inventory management method ");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
	   List<BrandInfoDTO> listOfContrctedPartners = service.getListOfContrctedAcxiomPartners();
	   model.addAttribute("contractedpartens", listOfContrctedPartners);
		controllerUtil.getUploadDropDown(model, request, businessService);
	 List<BrandInfoDTO> allClientLocations = service.getAllClientLocations();
	   model.addAttribute("allClientLocations", allClientLocations);
	   model.addAttribute("searchBusiness", new LocalBusinessDTO());
		logger.info("end ::Inventory management method ");
		
		return "inventmanagement";
	}
	/**
	 * addFile
	 * 
	 * @param model
	 * @param session
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "/addfile.htm", method = RequestMethod.GET)
	public String addFile(Model model,
			HttpSession session, HttpServletRequest request) {
		logger.info("start ::  Inventory management method ");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
	   List<BrandInfoDTO> listOfContrctedPartners = service.getListOfContrctedAcxiomPartners();
	   model.addAttribute("contractedpartens", listOfContrctedPartners);
		controllerUtil.getUploadDropDown(model, request, businessService);
	 List<BrandInfoDTO> allClientLocations = service.getAllClientLocations();
	   model.addAttribute("allClientLocations", allClientLocations);
	   model.addAttribute("searchBusiness", new LocalBusinessDTO());
		logger.info("end ::Inventory management method ");
		
		return "inventmanagement";
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
}
