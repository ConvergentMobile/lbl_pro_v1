package com.business.common.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.service.BusinessService;

@Component
public class StageUtil {

	@Autowired
	private BusinessService service;

	public void scrapAccuracyReortInfo(BusinessService service) {
		this.service = service;
		List<AccuracyDTO> accuracyDTOs = service
				.getAccuracyInfoFromAccuracyStage();
		for (AccuracyDTO accuracyDTO : accuracyDTOs) {
			String store = accuracyDTO.getStore();
			String brandname = accuracyDTO.getBrandname();
			Integer accuracyid = service.isStoreExistInAccuracy(store,
					brandname);
			if (accuracyid != 0) {
				accuracyDTO.setId(accuracyid);
				service.updateAccuracyInfo(accuracyDTO);
			} else {
				service.saveAccuracyInfo(accuracyDTO);
			}

		}
		List<CheckReportDTO> checkReportDTOs = service
				.getcheckreportInfoFromCheckReportStage();
		for (CheckReportDTO checkReportDTO : checkReportDTOs) {
			Integer listingId = service.isStoreExist(checkReportDTO.getStore(),
					checkReportDTO.getDirectory());
			if (listingId != 0) {
				checkReportDTO.setListingId(listingId);
				service.updateCheckReportInfo(checkReportDTO);
			} else {
				service.saveCheckReportInfo(checkReportDTO);
			}

		}

	}

}
