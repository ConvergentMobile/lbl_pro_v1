package com.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.business.common.dto.LocalBusinessDTO;
import com.business.model.dataaccess.ReportDao;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ReportParams;
import com.business.model.pojo.ValueObject;
import com.business.service.ReportService;
import com.business.web.bean.ReportForm;

/**
 * 
 * @author Vasanth
 * 
 * 
 * 
 */

@Service
public class ReportServiceImpl implements ReportService {
	Logger logger = Logger.getLogger(ReportServiceImpl.class);
	
	@Autowired
	private ReportDao reportDao;
		

	public Set<ReportEntity> getReports() {
		return reportDao.getReports();
	}
	
	
	public ReportForm runReport(Integer reportId, List<ReportParams> params, Map<String, String> otherParams, int offset, int numRecords,
									String sortField, String sortOrder,Date fromDate, Date toDate) throws Exception {
		List<String> reportColHeaders = new ArrayList<String>();
		int col = 0;
		List<ValueObject> reportRows;
		ReportForm reportForm = new ReportForm();
		String userName = otherParams.get("userName");
		int roleId = Integer.valueOf(otherParams.get("roleId"));
		try {
			switch (reportId) {
			case 1:
				
				reportRows = reportDao.renewalReport(params, offset,
						numRecords, sortField, sortOrder,userName,roleId,fromDate,toDate);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "# Locations");
				reportColHeaders.add(col++, "Renewal Date");
				reportColHeaders.add(col++, "Partner");
				
				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);
				return reportForm;
			case 2:
				
				reportRows = reportDao.uploadReport(userName, params, offset,
						numRecords, sortField, sortOrder,fromDate,toDate,roleId);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "# Records");
				reportColHeaders.add(col++, "Date");
				
				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);
				
				return reportForm;	
			case 3:
				
				reportRows = reportDao.exportReport(userName, params, offset,
						numRecords, sortField, sortOrder,fromDate,toDate,roleId);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "# Records");
				reportColHeaders.add(col++, "Date");
				
				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);
				
				return reportForm;
			default:
				logger.error("Unknown report");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return null;
	}

	@Transactional
	public List<LocalBusinessDTO> getUploadReportDetails(String brand, Date date) {
		return reportDao.getUploadReportDetails(brand,date);
	}

}
