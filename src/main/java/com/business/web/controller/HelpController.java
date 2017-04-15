package com.business.web.controller;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.ControllerUtil;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
import com.business.service.ReportService;
import com.business.web.bean.ReportForm;
import com.business.web.bean.UsersBean;
import com.business.web.validator.LocalBusinessValidator;

@Controller
public class HelpController {

	Logger logger = Logger.getLogger(HelpController.class);

	@Autowired
	private CheckReportService service;
	@Autowired
	private BusinessService businessservice;
	@Autowired
	private ReportService reportService;
	@Autowired
	private LocalBusinessValidator businessValidator;

	private ControllerUtil controllerUtil = new ControllerUtil();

	@RequestMapping(value = "/i01.htm", method = RequestMethod.GET)
	public String main(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i01";

	}

	@RequestMapping(value = "/i02.htm", method = RequestMethod.GET)
	public String main2(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i02";

	}

	@RequestMapping(value = "/i03.htm", method = RequestMethod.GET)
	public String main3(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i03";

	}

	@RequestMapping(value = "/i04.htm", method = RequestMethod.GET)
	public String main4(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i04";

	}

	@RequestMapping(value = "/i05.htm", method = RequestMethod.GET)
	public String main5(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i05";

	}

	@RequestMapping(value = "/i06.htm", method = RequestMethod.GET)
	public String main6(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i02";

	}

	@RequestMapping(value = "/i07.htm", method = RequestMethod.GET)
	public String main7(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i07";

	}

	@RequestMapping(value = "/i08.htm", method = RequestMethod.GET)
	public String main8(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i08";

	}

	@RequestMapping(value = "/i09.htm", method = RequestMethod.GET)
	public String main9(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i09";

	}

	@RequestMapping(value = "/i10.htm", method = RequestMethod.GET)
	public String main10(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i10";

	}

	@RequestMapping(value = "/i11.htm", method = RequestMethod.GET)
	public String main11(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i11";

	}

	@RequestMapping(value = "/i12.htm", method = RequestMethod.GET)
	public String main12(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i12";

	}

	@RequestMapping(value = "/i13.htm", method = RequestMethod.GET)
	public String main13(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i13";

	}

	@RequestMapping(value = "/i14.htm", method = RequestMethod.GET)
	public String main14(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i14";

	}
	@RequestMapping(value = "/i14_1.htm", method = RequestMethod.GET)
	public String main14_1(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i14_1";

	}

	@RequestMapping(value = "/i15.htm", method = RequestMethod.GET)
	public String main15(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i15";

	}

	@RequestMapping(value = "/i16.htm", method = RequestMethod.GET)
	public String main16(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i16";

	}

	@RequestMapping(value = "/i17.htm", method = RequestMethod.GET)
	public String main17(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i17";

	}

	@RequestMapping(value = "/i18.htm", method = RequestMethod.GET)
	public String main18(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i18";

	}

	@RequestMapping(value = "/i19.htm", method = RequestMethod.GET)
	public String main19(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i19";

	}

	@RequestMapping(value = "/i20.htm", method = RequestMethod.GET)
	public String main20(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i20";

	}

	@RequestMapping(value = "/i21.htm", method = RequestMethod.GET)
	public String main21(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i21";

	}

	@RequestMapping(value = "/i22.htm", method = RequestMethod.GET)
	public String main22(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i22";

	}

	@RequestMapping(value = "/i23.htm", method = RequestMethod.GET)
	public String main23(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i23";

	}

	@RequestMapping(value = "/i24.htm", method = RequestMethod.GET)
	public String main24(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i24";

	}

	@RequestMapping(value = "/i25.htm", method = RequestMethod.GET)
	public String main25(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i25";

	}

	@RequestMapping(value = "/i26.htm", method = RequestMethod.GET)
	public String main26(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i26";

	}

	@RequestMapping(value = "/i27.htm", method = RequestMethod.GET)
	public String main27(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i27";

	}

	@RequestMapping(value = "/i28.htm", method = RequestMethod.GET)
	public String main28(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i28";

	}

	@RequestMapping(value = "/i29.htm", method = RequestMethod.GET)
	public String main29(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i29";

	}

	@RequestMapping(value = "/i30.htm", method = RequestMethod.GET)
	public String main30(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i30";

	}

	@RequestMapping(value = "/i31.htm", method = RequestMethod.GET)
	public String main31(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i31";

	}

	@RequestMapping(value = "/i32.htm", method = RequestMethod.GET)
	public String main32(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i32";

	}

	@RequestMapping(value = "/i33.htm", method = RequestMethod.GET)
	public String main33(Model model, HttpSession session,
			HttpServletRequest request) {

		return "i33";

	}

	@RequestMapping(value = "/01.htm", method = RequestMethod.GET)
	public String Header(Model model, HttpSession session,
			HttpServletRequest request) {

		return "01";

	}

	@RequestMapping(value = "/02.htm", method = RequestMethod.GET)
	public String Header2(Model model, HttpSession session,
			HttpServletRequest request) {

		return "02";

	}

	@RequestMapping(value = "/03.htm", method = RequestMethod.GET)
	public String Header3(Model model, HttpSession session,
			HttpServletRequest request) {

		return "03";

	}

	@RequestMapping(value = "/04.htm", method = RequestMethod.GET)
	public String Header4(Model model, HttpSession session,
			HttpServletRequest request) {

		return "04";

	}

	@RequestMapping(value = "/05.htm", method = RequestMethod.GET)
	public String Header5(Model model, HttpSession session,
			HttpServletRequest request) {

		return "05";

	}

	@RequestMapping(value = "/06.htm", method = RequestMethod.GET)
	public String Header6(Model model, HttpSession session,
			HttpServletRequest request) {

		return "06";

	}

	@RequestMapping(value = "/07.htm", method = RequestMethod.GET)
	public String Header7(Model model, HttpSession session,
			HttpServletRequest request) {

		return "07";

	}

	@RequestMapping(value = "/08.htm", method = RequestMethod.GET)
	public String Header8(Model model, HttpSession session,
			HttpServletRequest request) {

		return "08";

	}

	@RequestMapping(value = "/09.htm", method = RequestMethod.GET)
	public String Header9(Model model, HttpSession session,
			HttpServletRequest request) {

		return "09";

	}

	@RequestMapping(value = "/10.htm", method = RequestMethod.GET)
	public String Header10(Model model, HttpSession session,
			HttpServletRequest request) {

		return "10";

	}

	@RequestMapping(value = "/11.htm", method = RequestMethod.GET)
	public String Header11(Model model, HttpSession session,
			HttpServletRequest request) {

		return "11";

	}

	@RequestMapping(value = "/12.htm", method = RequestMethod.GET)
	public String Header12(Model model, HttpSession session,
			HttpServletRequest request) {

		return "12";

	}

	@RequestMapping(value = "/13.htm", method = RequestMethod.GET)
	public String Header13(Model model, HttpSession session,
			HttpServletRequest request) {

		return "13";

	}

	@RequestMapping(value = "/14.htm", method = RequestMethod.GET)
	public String Header14(Model model, HttpSession session,
			HttpServletRequest request) {

		return "14";

	}
	@RequestMapping(value = "/14_1.htm", method = RequestMethod.GET)
	public String Header14_1(Model model, HttpSession session,
			HttpServletRequest request) {

		return "14_1";

	}


	@RequestMapping(value = "/15.htm", method = RequestMethod.GET)
	public String Header15(Model model, HttpSession session,
			HttpServletRequest request) {

		return "15";

	}

	@RequestMapping(value = "/16.htm", method = RequestMethod.GET)
	public String Header16(Model model, HttpSession session,
			HttpServletRequest request) {

		return "16";

	}

	@RequestMapping(value = "/17.htm", method = RequestMethod.GET)
	public String Header17(Model model, HttpSession session,
			HttpServletRequest request) {

		return "17";

	}

	@RequestMapping(value = "/18.htm", method = RequestMethod.GET)
	public String Header18(Model model, HttpSession session,
			HttpServletRequest request) {

		return "18";

	}

	@RequestMapping(value = "/19.htm", method = RequestMethod.GET)
	public String Header19(Model model, HttpSession session,
			HttpServletRequest request) {

		return "19";

	}

	@RequestMapping(value = "/20.htm", method = RequestMethod.GET)
	public String Header20(Model model, HttpSession session,
			HttpServletRequest request) {

		return "20";

	}

	@RequestMapping(value = "/21.htm", method = RequestMethod.GET)
	public String Header21(Model model, HttpSession session,
			HttpServletRequest request) {

		return "21";

	}
	@RequestMapping(value = "/22.htm", method = RequestMethod.GET)
	public String Header22(Model model, HttpSession session,
			HttpServletRequest request) {

		return "22";

	}
	@RequestMapping(value = "/23.htm", method = RequestMethod.GET)
	public String Header23(Model model, HttpSession session,
			HttpServletRequest request) {

		return "21";

	}
	@RequestMapping(value = "/24.htm", method = RequestMethod.GET)
	public String Header24(Model model, HttpSession session,
			HttpServletRequest request) {

		return "24";

	}
	@RequestMapping(value = "/25.htm", method = RequestMethod.GET)
	public String Header25(Model model, HttpSession session,
			HttpServletRequest request) {

		return "25";

	}

	@RequestMapping(value = "/26.htm", method = RequestMethod.GET)
	public String Header26(Model model, HttpSession session,
			HttpServletRequest request) {

		return "26";

	}

	@RequestMapping(value = "/27.htm", method = RequestMethod.GET)
	public String Header27(Model model, HttpSession session,
			HttpServletRequest request) {

		return "27";

	}

	@RequestMapping(value = "/28.htm", method = RequestMethod.GET)
	public String Header28(Model model, HttpSession session,
			HttpServletRequest request) {

		return "28";

	}

	@RequestMapping(value = "/29.htm", method = RequestMethod.GET)
	public String Header29(Model model, HttpSession session,
			HttpServletRequest request) {

		return "29";

	}

	@RequestMapping(value = "/30.htm", method = RequestMethod.GET)
	public String Header30(Model model, HttpSession session,
			HttpServletRequest request) {

		return "30";

	}

	@RequestMapping(value = "/31.htm", method = RequestMethod.GET)
	public String Header31(Model model, HttpSession session,
			HttpServletRequest request) {

		return "31";

	}

	@RequestMapping(value = "/32.htm", method = RequestMethod.GET)
	public String Header32(Model model, HttpSession session,
			HttpServletRequest request) {

		return "32";

	}

	@RequestMapping(value = "/33.htm", method = RequestMethod.GET)
	public String Header33(Model model, HttpSession session,
			HttpServletRequest request) {

		return "33";

	}

}
