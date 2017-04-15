package com.business.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.common.util.ControllerUtil;
import com.business.service.BusinessService;
import com.business.service.ManageBrandService;
import com.business.web.bean.UsersBean;

@Controller
public class ManageBrandController {
	
	Logger logger = Logger.getLogger(BusinessController.class);

	@Autowired
	private BusinessService businessService;
	@Autowired
	private ManageBrandService service;


	private ControllerUtil controllerUtil = new ControllerUtil();
	
	/**
	 * getDeleteBrandChannel
	 * 
	 * @param model
	 * @param request
	 * @param httpSession
	 * @return
	 */

	@RequestMapping(value = "/brandchannel.htm", method = RequestMethod.GET)
	public String getDeleteBrandChannel(Model model,
			HttpServletRequest request, HttpSession httpSession) {

		logger.info("start :: deleteBrandChannel  method");
		if (!loginSessionValidation(model, httpSession)) {
			return "logout";
		}
		List<ChannelNameDTO> channelNames = service.getChannel();

		model.addAttribute("channels", channelNames);
		controllerUtil.setUserAndBussinessDataToModel(model, request, businessService);

		logger.info("end :: deleteBrandChannel  method");
		return "manageBrands";
	}

	/**
	 * channelBrands
	 * 
	 * @param model
	 * @param bean
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/branddelete.htm", params = "brands", method = RequestMethod.POST)
	public String channelBrands(Model model,
			@ModelAttribute("brandchannel") UsersBean bean,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  saveUserDetails method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}

		Integer channelID = bean.getChannelID();
		logger.info("channel id::" + channelID);

		List<BrandInfoDTO> clientNames = service
				.getBrandsByChannelID(channelID);

		List<ChannelNameDTO> channelNames = service.getChannel();
		model.addAttribute("channels", channelNames);
		model.addAttribute("clientNameInfo", clientNames);
		return "manageBrands";
	}
	
	
	/**
	 * deleteBrandChannel
	 * 
	 * @param model
	 * @param bean
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/branddelete", method = RequestMethod.POST)
	public String deleteBrandChannel(Model model,
			@ModelAttribute("brandchannel") UsersBean bean,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  saveUserDetails method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		String[] brandID = bean.getBrandID();
		if(brandID !=null){
		List<Integer> listofbrand = new ArrayList<Integer>();
		
		for (String string : brandID) {
			listofbrand.add(Integer.valueOf(string));

		}
		
			service.deleteBrands(listofbrand);
		}
		
		
		String[] parameterValues = request.getParameterValues("channelIDvalue");
		if (parameterValues != null) {

			List<Integer> listofchannels = new ArrayList<Integer>();
			for (String channelID : parameterValues) {
				logger.info("channelID in delete:::::::::::::::::::::"+channelID);
				listofchannels.add(Integer.valueOf(channelID));
			}
			
			service.deleteChannels(listofchannels);
		}
		List<ChannelNameDTO> channelNames = service.getChannel();
		model.addAttribute("channels", channelNames);
		controllerUtil.setUserAndBussinessDataToModel(model, request, businessService);

		return "manageBrands";
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
