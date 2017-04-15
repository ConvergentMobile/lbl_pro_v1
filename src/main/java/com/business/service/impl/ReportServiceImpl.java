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

import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.model.dataaccess.ReportDao;
import com.business.model.pojo.LocalBusinessEntity;
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

	public ReportForm runReport(Integer reportId, List<ReportParams> params,
			Map<String, String> otherParams, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			String brand) throws Exception {
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
						numRecords, sortField, sortOrder, userName, roleId,
						fromDate, toDate);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "# Locations");
				reportColHeaders.add(col++, "Renewal  \n Date");
				reportColHeaders.add(col++, "Partner");

				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);
				return reportForm;
			case 2:

				reportRows = reportDao.uploadReport(userName, params, offset,
						numRecords, sortField, sortOrder, fromDate, toDate,
						roleId);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "# Records");
				reportColHeaders.add(col++, "Date");
				reportColHeaders.add(col++, "UserName");
				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);

				return reportForm;
			case 3:

				reportRows = reportDao.exportReport(userName, params, offset,
						numRecords, sortField, sortOrder, fromDate, toDate,
						roleId);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "#Records");
				reportColHeaders.add(col++, "Template");
				reportColHeaders.add(col++, "Date");
				reportColHeaders.add(col++, "UserName");

				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);

				return reportForm;
			case 4:
				reportRows = reportDao.distributionReport(userName, params,
						offset, numRecords, sortField, sortOrder, fromDate,
						toDate, roleId);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "# Records");
				reportColHeaders.add(col++, "Provider");
				reportColHeaders.add(col++, "Last \n Submission");

				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);

				return reportForm;

			case 5:
				reportRows = reportDao.changeTrackingReport(userName, params,
						offset, numRecords, sortField, sortOrder, fromDate,
						toDate, roleId);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "Store#");
				reportColHeaders.add(col++, "BusinessName");
				reportColHeaders.add(col++, "Address");
				reportColHeaders.add(col++, "City");
				reportColHeaders.add(col++, "State");
				reportColHeaders.add(col++, "Zip");
				reportColHeaders.add(col++, "Phone");
				reportColHeaders.add(col++, "WebSite");
				reportColHeaders.add(col++, "Hourse Of Operation");
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
		return reportDao.getUploadReportDetails(brand, date);
	}

	public ReportForm runBrandReport(Integer reportId,
			List<ReportParams> params, Map<String, String> otherParams,
			int offset, int numRecords, String sortField, String sortOrder,
			Date fromDate, Date toDate, String brand) throws Exception {
		List<String> reportColHeaders = new ArrayList<String>();
		int col = 0;
		List<ValueObject> reportRows;
		ReportForm reportForm = new ReportForm();
		String userName = otherParams.get("userName");
		int roleId = Integer.valueOf(otherParams.get("roleId"));
		try {
			switch (reportId) {
			case 1:

				reportRows = reportDao.renewalbrandReport(params, offset,
						numRecords, sortField, sortOrder, userName, roleId,
						fromDate, toDate, brand);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "# Locations");
				reportColHeaders.add(col++, "Renewal \n Date");
				reportColHeaders.add(col++, "Partner");

				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);
				return reportForm;
			case 2:

				reportRows = reportDao.uploadReport(userName, params, offset,
						numRecords, sortField, sortOrder, fromDate, toDate,
						roleId);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "# Records");
				reportColHeaders.add(col++, "Date");
				reportColHeaders.add(col++, "UserName");
				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);

				return reportForm;
			case 3:

				reportRows = reportDao.exportReport(userName, params, offset,
						numRecords, sortField, sortOrder, fromDate, toDate,
						roleId);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "# Records");
				reportColHeaders.add(col++, "Template");
				reportColHeaders.add(col++, "Date");
				reportColHeaders.add(col++, "UserName");
				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);

				return reportForm;
			case 4:
				reportRows = reportDao.distributionbrandReport(userName,
						params, offset, numRecords, sortField, sortOrder,
						fromDate, toDate, roleId, brand);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "# Records");
				reportColHeaders.add(col++, "Provider");
				reportColHeaders.add(col++, "Last  \n Submission");

				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);

				return reportForm;

			case 5:
				reportRows = reportDao.changeTrackingBrandReport(userName,
						params, offset, numRecords, sortField, sortOrder,
						fromDate, toDate, roleId, brand);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "Store#");
				reportColHeaders.add(col++, "BusinessName");
				reportColHeaders.add(col++, "Address");
				reportColHeaders.add(col++, "City");
				reportColHeaders.add(col++, "State");
				reportColHeaders.add(col++, "Zip");
				reportColHeaders.add(col++, "Phone");
				reportColHeaders.add(col++, "WebSite");
				reportColHeaders.add(col++, "Hourse Of Operation");
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
	public List<ExportReportDTO> getDistrbutionReportDetails(String brand,
			String date) {
		return reportDao.getDistrbutionReportDetails(brand, date);
	}

	public List<String> getStoreForBrand(String categoryId) {
		return reportDao.getStoreForBrand(categoryId);
	}

	public ReportForm runchangeReport(Integer reportId,
			List<ReportParams> params, Map<String, String> otherParams,
			int offset, int numRecords, String sortField, String sortOrder,
			Date fromDate, Date toDate, String brand, List<String> listofStores)
			throws Exception {
		List<String> reportColHeaders = new ArrayList<String>();
		int col = 0;
		List<ValueObject> reportRows;
		ReportForm reportForm = new ReportForm();
		String userName = otherParams.get("userName");
		int roleId = Integer.valueOf(otherParams.get("roleId"));
		try {
			switch (reportId) {

			case 5:
				reportRows = reportDao.changeTrackingBrandListReport(userName,
						params, offset, numRecords, sortField, sortOrder,
						fromDate, toDate, roleId, brand, listofStores);
				col = 0;
				reportColHeaders.add(col++, "Brand");
				reportColHeaders.add(col++, "Store#");
				reportColHeaders.add(col++, "BusinessName");
				reportColHeaders.add(col++, "Address");
				reportColHeaders.add(col++, "City");
				reportColHeaders.add(col++, "State");
				reportColHeaders.add(col++, "Zip");
				reportColHeaders.add(col++, "Phone");
				reportColHeaders.add(col++, "WebSite");
				reportColHeaders.add(col++, "Hourse Of Operation");
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

	public ReportForm checkreport(Integer reportId, List<ReportParams> params,
			Map<String, String> otherParams, int offset, int numRecords,
			String sortField, String sortOrder, String brand,
			String listofStores) {
		List<String> reportColHeaders = new ArrayList<String>();
		int col = 0;
		List<ValueObject> reportRows;
		ReportForm reportForm = new ReportForm();
		String userName = otherParams.get("userName");
		int roleId = Integer.valueOf(otherParams.get("roleId"));
		try {
			switch (reportId) {

			case 5:
				reportRows = reportDao.checkReportlisting(userName, params,
						offset, numRecords, sortField, sortOrder, brand,
						roleId, listofStores);
				col = 0;
				reportColHeaders.add(col++, "Directory");

				reportColHeaders.add(col++, "BusinessName");
				reportColHeaders.add(col++, "Address");
				reportColHeaders.add(col++, "City");
				reportColHeaders.add(col++, "State");
				reportColHeaders.add(col++, "Zip");
				reportColHeaders.add(col++, "Phone");

				reportForm.setReportColumnHeaders(reportColHeaders);
				reportForm.setReportRows(reportRows);

				return reportForm;

			default:
				logger.error("Unknown report");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw new Exception(e.getMessage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}

	@Transactional
	public ReportForm runRenewalReport(Date fromDate, Date toDate,
			String storeName, String brand) {
		List<String> reportColHeaders = new ArrayList<String>();
		int col = 0;
		List<ValueObject> reportRows;
		ReportForm reportForm = new ReportForm();

		reportRows = reportDao.runRenewalReport(fromDate, toDate, storeName,brand);
		col = 0;
		reportColHeaders.add(col++, "Store");
		reportColHeaders.add(col++, "Acxiom");
		reportColHeaders.add(col++, "Infogroup");
		reportColHeaders.add(col++, "Factual");
		reportColHeaders.add(col++, "Neustar");
		reportForm.setReportColumnHeaders(reportColHeaders);
		reportForm.setReportRows(reportRows);
		return reportForm;
	}
	@Transactional
	public Map<String, Integer> getPathCountMapForStores(
			List<LocalBusinessDTO> listOfBussinessinfo) {
		return reportDao.getPathCountMapForStores(listOfBussinessinfo);
	}

	@Transactional
	public ReportForm runRenewalReportForBrand(Date fromDate, Date toDate,
			String brand) {
		List<String> reportColHeaders = new ArrayList<String>();
		int col = 0;
		List<ValueObject> reportRows;
		ReportForm reportForm = new ReportForm();

		reportRows = reportDao.runRenewalReportForBrand(fromDate, toDate, brand);
		col = 0;
		reportColHeaders.add(col++, "Store");
		reportColHeaders.add(col++, "Acxiom");
		reportColHeaders.add(col++, "Infogroup");
		reportColHeaders.add(col++, "Factual");
		reportColHeaders.add(col++, "Neustar");
		reportForm.setReportColumnHeaders(reportColHeaders);
		reportForm.setReportRows(reportRows);
		return reportForm;
	}
	@Transactional
	public ReportForm runOvveridenReport(Date fromDate, Date toDate,
			String brand) {
		List<String> reportColHeaders = new ArrayList<String>();
		int col = 0;
		List<ValueObject> reportRows;
		ReportForm reportForm = new ReportForm();

		reportRows = reportDao.runOvveridenReportForBrand(fromDate, toDate, brand);
		col = 0;
		reportColHeaders.add(col++, "Brand");
		reportColHeaders.add(col++, "Store");
		reportColHeaders.add(col++, "Date");
		reportColHeaders.add(col++, "UserName");
		
		reportForm.setReportColumnHeaders(reportColHeaders);
		reportForm.setReportRows(reportRows);
		return reportForm;
	}

	@Transactional
	public ReportForm runOvveridenBrandsReport(Date fromDate, Date toDate) {
		List<String> reportColHeaders = new ArrayList<String>();
		int col = 0;
		List<ValueObject> reportRows;
		ReportForm reportForm = new ReportForm();

		reportRows = reportDao.runOvveridenBrandsReport(fromDate, toDate);
		col = 0;
		reportColHeaders.add(col++, "Brand");
		reportColHeaders.add(col++, "Store");
		reportColHeaders.add(col++, "Date");
		reportColHeaders.add(col++, "UserName");
		
		reportForm.setReportColumnHeaders(reportColHeaders);
		reportForm.setReportRows(reportRows);
		return reportForm;
	}

}
