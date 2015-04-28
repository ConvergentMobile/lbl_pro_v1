package com.business.model.dataaccess;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.business.common.dto.LocalBusinessDTO;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ReportParams;
import com.business.model.pojo.ValueObject;

/**
 * 
 * @author Vasanth
 * 
 * 
 * 
 */

public interface ReportDao {

	Set<ReportEntity> getReports();

	List<ValueObject> renewalReport(List<ReportParams> params, int offset,
			int numRecords, String sortField, String sortOrder, String userName, int roleId,Date fromDate, Date toDate) throws Exception;

	List<ValueObject> uploadReport(String userName, List<ReportParams> params,
			int offset, int numRecords, String sortField, String sortOrder, Date fromDate, Date toDate, int roleId)throws Exception;

	List<ValueObject> exportReport(String userName, List<ReportParams> params,
			int offset, int numRecords, String sortField, String sortOrder,
			Date fromDate, Date toDate, int roleId);

	List<LocalBusinessDTO> getUploadReportDetails(String brand, Date date);

}
