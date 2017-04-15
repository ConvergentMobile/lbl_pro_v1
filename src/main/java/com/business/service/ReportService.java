package com.business.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.business.common.dto.ExportReportDTO;
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
			String sortColumn, String sortOrder, Date fromDate, Date toDate,String brand) throws Exception;

	List<LocalBusinessDTO> getUploadReportDetails(String brand, Date date);

	ReportForm runBrandReport(Integer id, List<ReportParams> params,
			Map<String, String> otherParams, int i, int recordsPerPage,
			String sortColumn, String sortOrder, Date fromDate, Date toDate,
			String brand) throws Exception;

	List<ExportReportDTO> getDistrbutionReportDetails(String brand,String date);

	public List<String> getStoreForBrand(String categoryId);

	ReportForm runchangeReport(Integer id, List<ReportParams> params,
			Map<String, String> otherParams, int i, int recordsPerPage,
			String sortColumn, String sortOrder, Date fromDate, Date toDate,
			String brand, List<String> listofStores) throws Exception;

	ReportForm checkreport(Integer id, List<ReportParams> params,
			Map<String, String> otherParams, int i, int recordsPerPage,
			String sortColumn, String sortOrder, String store, String brand);

	ReportForm runRenewalReport(Date fromDate, Date toDate, String storeName, String brand);

	Map<String, Integer> getPathCountMapForStores(List<LocalBusinessDTO> listOfBussinessinfo);

	ReportForm runRenewalReportForBrand(Date fromDate, Date toDate, String brand);

	ReportForm runOvveridenReport(Date fromDate, Date toDate, String brand);

	ReportForm runOvveridenBrandsReport(Date fromDate, Date toDate);
	


	

}
