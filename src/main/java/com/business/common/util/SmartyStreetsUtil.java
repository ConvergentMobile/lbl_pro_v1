package com.business.common.util;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.service.BusinessService;
import com.business.web.bean.UploadBusinessBean;

public class SmartyStreetsUtil {

	public void validateAndUpdateResult(BusinessService service,
			List<LocalBusinessDTO> correctBusinessList,
			List<LocalBusinessDTO> updateBusinesList,
			List<LblErrorDTO> inCorrectDataList,
			List<LblErrorDTO> listOfErrorUpdatesByActionCode) {

		UploadBusinessBean uploadBean = new UploadBusinessBean();

		for (LocalBusinessDTO businessDTO : correctBusinessList) {
			BeanUtils.copyProperties(businessDTO, uploadBean);
			boolean isAddressValid = AddressValidationUtill
					.validateAddressWithSS(uploadBean);
			if (!isAddressValid) {
				service.deleteBusinessInfoByStoreAndClient(uploadBean);
			}
		}

		for (LblErrorDTO lblErrorDTO : inCorrectDataList) {

			BeanUtils.copyProperties(lblErrorDTO, uploadBean);

			boolean isAddressValid = AddressValidationUtill
					.validateAddressWithSS(uploadBean);

			LblErrorDTO errorDTO = new LblErrorDTO();
			if (!isAddressValid) {
				BeanUtils.copyProperties(uploadBean, errorDTO);
				service.updateErrorBusinessInfoByAddressVerfication(errorDTO);
			}
		}

		for (LocalBusinessDTO businessDTO : updateBusinesList) {
			BeanUtils.copyProperties(businessDTO, uploadBean);
			boolean isAddressValid = AddressValidationUtill
					.validateAddressWithSS(uploadBean);
			if (!isAddressValid) {
				service.deleteBusinessInfoByStoreAndClient(uploadBean);
			}
		}

		/*
		 * for (LblErrorDTO lblErrorDTO : listOfErrorUpdatesByActionCode) {
		 * 
		 * String errorMessage = lblErrorDTO.getErrorMessage();
		 * BeanUtils.copyProperties(lblErrorDTO, uploadBean);
		 * 
		 * boolean isAddressValid = AddressValidationUtill
		 * .validateAddressWithSS(uploadBean);
		 * 
		 * LblErrorDTO errorDTO = new LblErrorDTO(); if(!isAddressValid){
		 * BeanUtils.copyProperties(uploadBean, errorDTO);
		 * errorDTO.setErrorMessage(errorMessage.concat(
		 * "LocationAddress Verification is failed. Kindly verify the address info, "
		 * )); service.updateErrorBusinessInfoByAddressVerfication(errorDTO); }
		 * }
		 */

	}

}
