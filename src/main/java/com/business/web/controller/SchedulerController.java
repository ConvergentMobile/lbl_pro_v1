package com.business.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.PartnerDTO;
import com.business.common.dto.SchedulerDTO;
import com.business.common.util.ControllerUtil;
import com.business.common.util.UploadBeanValidateUtil;
import com.business.service.BusinessService;
import com.business.service.SchedulerService;
import com.business.web.bean.LocalBusinessBean;
import com.business.web.bean.SchedulerBean;
import com.business.web.bean.UploadBusinessBean;
import com.business.web.bean.UsersBean;

@Controller
public class SchedulerController {
	
	Logger logger = Logger.getLogger(SchedulerController.class);

	private ControllerUtil controllerUtil = new ControllerUtil();

	@Autowired
	private BusinessService businessService;
	@Autowired
	private SchedulerService service;
	
	/**
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	
	@RequestMapping(value="/scheduler.htm",method=RequestMethod.GET)
	public String getScheduler(Model model,HttpServletRequest request, HttpSession session){
		
		logger.info("start :: getScheduler");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		
		List<SchedulerDTO> schduleractivity = service.getScheduleListing();
		
		List<PartnerDTO> partners = service.getPartners();		
		model.addAttribute("partners", partners);
		model.addAttribute("schduleractivity", schduleractivity);
		controllerUtil.setUserAndBussinessDataToModel(model, request, businessService);
		model.addAttribute("scheduler", new SchedulerDTO());
		logger.info("end :: get Scheduler  method");
		
		return "scheduler";		
	}
	
	/**
	 * deleteScheduleInfo
	 * 
	 * @param beanStr
	 * @param model
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteBusinessInfotest", method = RequestMethod.POST)
	public String deleteScheduleInfo(@RequestBody String beanStr, Model model,
			HttpSession session, HttpServletRequest request) {
		logger.info("start ::  deleteScheduleInfotest method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		logger.info("Delete Ids info == " + beanStr);
		String[] split = beanStr.split("&");
		List<Integer> listIds = new ArrayList<Integer>();
		for (String value : split) {
			if (value.contains("schdulerId=")) {
				String id = value.substring("schdulerId=".length());
				listIds.add(Integer.parseInt(id));
			}
		}
		logger.info("Selected IDs == " + listIds);
		service.deleteSchedules(listIds);
					
		List<SchedulerDTO> scheduleListing = service.getScheduleListing();
	
		List<SchedulerDTO> schduleractivity = scheduleListing;
		
		List<PartnerDTO> partners = service.getPartners();
		model.addAttribute("scheduler", new SchedulerDTO());
		model.addAttribute("partners", partners);
		model.addAttribute("schduleractivity", schduleractivity);
		controllerUtil.setUserAndBussinessDataToModel(model, request, businessService);
	
		logger.info("end ::  deleteBusinessInfotest method");
		return "scheduler";
	}
	/**
	 * saveSchdulerDetails
	 * 
	 * @param model
	 * @param bean
	 * @param result
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/schdulersave.htm", method = RequestMethod.POST)
	public String saveSchdulerDetails(Model model,
			@ModelAttribute("scheduler") SchedulerBean bean,BindingResult result,
			HttpServletRequest request, HttpSession session) {
		logger.info("start ::  saveSchdulerDetails method");
		if (!loginSessionValidation(model, session)) {
			return "logout";
		}
		
		SchedulerDTO dto = new SchedulerDTO();	

		BeanUtils.copyProperties(bean, dto);		
		Date scheduledDate = bean.getScheduleTime();
		
		Calendar c = Calendar.getInstance();
		c.setTime(scheduledDate);
		
		c.add(Calendar.DATE,bean.getRecurring());
		
		dto.setNextScheduleTime(c.getTime());

		boolean save = service.saveSchedule(dto);
		
		List<PartnerDTO> partners = service.getPartners();	
		model.addAttribute("partners", partners);
		controllerUtil.setUserAndBussinessDataToModel(model, request, businessService);
		List<SchedulerDTO> schduleractivity = service.getScheduleListing();
		
		model.addAttribute("schduleractivity", schduleractivity);
		logger.info("end :: saveSchdulerDetails method");
		
		return "scheduler";

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
