package com.business.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.AccuracyGraphDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.CitationGraphDTO;
import com.business.common.dto.CitationReportDTO;
import com.business.common.dto.CitationStoreHistoryDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.model.pojo.AccuarcyGraphEntity;
import com.business.model.pojo.CitationGraphEntity;
import com.business.model.pojo.CitationReportEntity;
import com.business.model.pojo.ValueObject;

public interface CheckReportService {
	public Set<LocalBusinessDTO> CheckSearchInfo(String brands,
			String companyName, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode);
	public List<LblErrorDTO> getListOfErrors();
	public List<LocalBusinessDTO> getListOfBusinessInfo();
	public List<CheckReportDTO> getListOfCheckreportInfo();
	public Set<CheckReportDTO> CheckReportSearchInfo(String brands,
			String companyName, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode);
	public List<CheckReportDTO> getcheckreport(
			Set<CheckReportDTO> businesSearchinfo);
	public List<LocalBusinessDTO> getStore();
	public List<CheckReportDTO> checkReportSearch(String store);
	public  Set<CheckReportDTO> CheckReportSearchInfo(String store,String locationAddress,String brandname);
	public List<CheckReportDTO> checkListing(String listingId);
	public List<LocalBusinessDTO> getBusinesslisting(String store,String brandname);
	public Integer getErrorCount(String directory, String store);
	public List<AccuracyGraphDTO> getGraphInfo(String brandname);
	public List<LocalBusinessDTO> getListOfBusinessInfo(String client);
	public List<CheckReportDTO> getcheckreportInfo(String directory, String store,Integer brandId);
	public Set<CheckReportDTO> CheckReportListingSearchInfo(String store, String directory,String brandname);
	public Integer getPathCountFromSearchDomain(String store);
	public List<CitationGraphDTO> getCitationGraphInfo(String brandname);
	public LocalBusinessDTO getBusinessInfoByStore(String store);
	
	public Map<String, Integer> getErrorsCount(String store,String brandname);
	public Map<String, List<String>> getPathFromSearch(String store,Integer brandId);
	public Map<String, List<String>> getDomainAuthorities(String store,Integer brandId);
	public String getdomainAuthorityFromSearchDomains(String store,Integer brandId);
	public AccuracyDTO getAccuracyListInfo(String store, String brandname);
	public void saveAccuracyInfo(AccuracyDTO dto);
	public int getPercentagecategory1(String brandname);
	public int getPercentagecategory2(String brandname);
	public int getPercentagecategory3(String brandname);
	public int getPercentagecategory4(String brandname);
	public int gettotalStores(String brandname);
	public double getTotalPercentage(String brandname);
	public Integer isStoreExistInAccuracy(String store, String brandName);
	public void updateAccuracyInfo(AccuracyDTO dto);
	public List<AccuracyDTO> getSortByGoogleAccuracyListInfo(String brandName,String flag);
	public List<AccuracyDTO> getSortByBingAccuracyListInfo(String brandname,
			String flag);
	public List<AccuracyDTO> getSortByYahooAccuracyListInfo(String brandname,
			String flag);
	public List<AccuracyDTO> getSortByYelpAccuracyListInfo(String brandname,
			String flag);
	public List<AccuracyDTO> getSortByYpAccuracyListInfo(String brandname,
			String flag);
	public List<AccuracyDTO> getSortByCitySearchAccuracyListInfo(
			String brandname, String flag);
	public List<AccuracyDTO> getSortByMapquestAccuracyListInfo(
			String brandname, String flag);
	public List<AccuracyDTO> getSortBySuperPagesAccuracyListInfo(
			String brandname, String flag);
	public List<AccuracyDTO> getSortByYellowBookAccuracyListInfo(
			String brandname, String flag);
	public List<AccuracyDTO> getSortByWhitePagesAccuracyListInfo(
			String brandname, String flag);
	public List<AccuracyDTO> getSortByStoreAccuracyListInfo(String brandname,
			String flag);
	public List<AccuracyDTO> getSortByAccuracyListInfo(String brandname,
			String flag);
	public void saveAccuracyGraphInfo(AccuracyGraphDTO accuracyGraphDTO);
	public AccuracyDTO getLocationAccuracy(String store, String brandname);
	public int getGoogleAccuracy(String store, String brandname,
			String directory);
	public List<CheckReportDTO> getCheckreportInfo();
	public void updateCheckreportInfo(Integer clinetId, String zip, String store);
	public void saveCitationreportInfo(CitationReportEntity citationReportEntity);
	public Integer isStoreAndBrandExistInCitation(String store, Integer brandID);
	public void updateCitationreportInfo(
			CitationReportEntity citationReportEntity);
	public List<ValueObject> getCiationData(String brandName,String store);
	public List<ValueObject> getChartDataDS(String brandName);
	public Integer getClinetIdIdByName(String brandName);
	public List<RenewalReportDTO> runRenewalReport(String storeName,
			String brand);
	public List<RenewalReportDTO> runRenewalReportForBrand(String brand);
	public List<RenewalReportDTO> runSummaryReport(Date fromDate, Date toDate,
			String storeName, String brand);
	public List<RenewalReportDTO> runSummaryReportForBrand(Date fromDate,
			Date toDate, String brand);
	public boolean getGoogleGraphInfo(String store, String string, String brandname);
	public List<AccuracyGraphDTO> getTotalListingGraphInfo(String brandname);
	public List<CitationReportDTO> getCitationStoregraphInfo(String store,
			String brandname);
	public List<CitationGraphDTO> getCitationBrandGraphInfo(String brandname);
	public List<CitationStoreHistoryDTO> getCitationStoreHistoryInfo(
			String store, String brandname);
	
	
	
	
}
