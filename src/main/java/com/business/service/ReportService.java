package com.business.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.business.common.dto.LocalBusinessDTO;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ReportParams;
import com.business.web.bean.ReportForm;

/**
 * 
 * @author Vasanth
 * 
 * 
 * 
 */
public interface ReportService {

	Set<ReportEntity> getReports();

	ReportForm runReport(Integer id, List<ReportParams> params,
			Map<String, String> otherParams, int i, int recordsPerPage,
			String sortColumn, String sortOrder, Date fromDate, Date toDate) throws Exception;

	List<LocalBusinessDTO> getUploadReportDetails(String brand, Date date);
	

}
