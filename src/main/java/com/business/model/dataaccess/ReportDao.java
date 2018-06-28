package com.business.model.dataaccess;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ReportParams;
import com.business.model.pojo.ValueObject;

/**
 * 
 * @author lbl_dev
 * 
 * 
 * 
 */

public interface ReportDao {

	Set<ReportEntity> getReports();

	List<ValueObject> renewalReport(List<ReportParams> params, int offset,
			int numRecords, String sortField, String sortOrder,
			String userName, int roleId, Date fromDate, Date toDate)
			throws Exception;

	List<ValueObject> uploadReport(String userName, List<ReportParams> params,
			int offset, int numRecords, String sortField, String sortOrder,
			Date fromDate, Date toDate, int roleId) throws Exception;

	List<ValueObject> exportReport(String userName, List<ReportParams> params,
			int offset, int numRecords, String sortField, String sortOrder,
			Date fromDate, Date toDate, int roleId);

	List<ValueObject> distributionReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId);

	List<LocalBusinessDTO> getUploadReportDetails(String brand, Date date);

	List<ExportReportDTO> getDistrbutionReportDetails(String brand, String date);

	List<ValueObject> changeTrackingReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId);

	List<ValueObject> renewalbrandReport(List<ReportParams> params, int offset,
			int numRecords, String sortField, String sortOrder,
			String userName, int roleId, Date fromDate, Date toDate,
			String brand);

	List<ValueObject> distributionbrandReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId, String brand);

	List<ValueObject> changeTrackingBrandReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId, String brand);

	public List<String> getStoreForBrand(String categoryId);

	List<ValueObject> changeTrackingBrandListReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId, String brand, List<String> listofStores);

	List<ValueObject> checkReportlisting(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, String brand, int roleId,
			String listofStores);

	List<ValueObject> runRenewalReport(Date fromDate, Date toDate,
			String storeName, String brand);

	Map<String, Integer> getPathCountMapForStores(
			List<LocalBusinessDTO> listOfBussinessinfo);

	List<ValueObject> runRenewalReportForBrand(Date fromDate, Date toDate,
			String brand);

	List<ValueObject> runOvveridenReportForBrand(Date fromDate, Date toDate,
			String brand);

	List<ValueObject> runOvveridenBrandsReport(Date fromDate, Date toDate);

}
