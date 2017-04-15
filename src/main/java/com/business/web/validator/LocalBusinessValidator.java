package com.business.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.business.service.BusinessService;
import com.business.web.bean.LocalBusinessBean;

/***
 * 
 * @author Vasanth
 * 
 * ValidatorClass which validate the parameters from the viewLayer and
 *  returns to viewPage 
 * 
 * 
 */

@Component
public class LocalBusinessValidator implements Validator {

	@Autowired
	private BusinessService businessService;

	public boolean supports(Class<?> arg0) {

		return LocalBusinessBean.class.equals(arg0);
	}	
	public void validate(Object target, Errors errors) {

		//LocalBusinessBean businessBean = (LocalBusinessBean) target;
				

		ValidationUtils.rejectIfEmpty(errors, "store",
				"Store required");

		ValidationUtils.rejectIfEmpty(errors, "actionCode",
				"businessApp.lable.validation.actionCode.required");

		ValidationUtils.rejectIfEmpty(errors, "countryCode",
				"businessApp.lable.validation.countryCode.required");

		ValidationUtils.rejectIfEmpty(errors, "companyName",
				"businessApp.lable.validation.companyName.required");

		ValidationUtils.rejectIfEmpty(errors, "locationAddress",
				"businessApp.lable.validation.locationAddress.required");

		ValidationUtils.rejectIfEmpty(errors, "locationCity",
				"businessApp.lable.validation.locationCity.required");

		ValidationUtils.rejectIfEmpty(errors, "locationState",
				"businessApp.lable.validation.locationState.required");
		
		ValidationUtils.rejectIfEmpty(errors, "locationZipCode",
				"businessApp.lable.validation.locationZipCode.required");

		ValidationUtils.rejectIfEmpty(errors, "locationPhone",
				"businessApp.lable.validation.locationPhone.required");
		
		ValidationUtils.rejectIfEmpty(errors, "category1",
				"businessApp.lable.validation.category1.required");

		ValidationUtils.rejectIfEmpty(errors, "businessDescription",
				"businessApp.lable.validation.businessDescription.required");
		
		
		ValidationUtils.rejectIfEmpty(errors, "businessDescriptionShort",
				"businessApp.lable.validation.businessDescriptionShort.required");
	
	}

}
