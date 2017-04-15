package com.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import com.business.model.dataaccess.CheckReportDao;
import com.business.model.pojo.CitationGraphEntity;
import com.business.model.pojo.CitationReportEntity;
import com.business.model.pojo.ValueObject;
import com.business.service.CheckReportService;
@Service
public class CheckreportServiceImpl implements CheckReportService {
	@Autowired
	private CheckReportDao checkReportDao;

	@Transactional
	public Set<LocalBusinessDTO> CheckSearchInfo(String brands,
			String companyName, String locationPhone, String locationAddress,
			String locationCity, String locationState, String locationZipCode) {
		return checkReportDao.CheckSearchInfo(brands, companyName,
				locationPhone, locationAddress, locationCity, locationState,
				locationZipCode);
	}

	@Transactional
	public List<LblErrorDTO> getListOfErrors() {
		return checkReportDao.getListOfErrors();
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfo() {
		return checkReportDao.getListOfBusinessInfo();
	}

	@Transactional
	public List<CheckReportDTO> getListOfCheckreportInfo() {
		return checkReportDao.getListOfCheckreportInfo();
	}


	@Transactional
	public Set<CheckReportDTO> CheckReportSearchInfo(String brands,
			String companyName, String locationPhone, String locationAddress,
			String locationCity, String locationState, String locationZipCode) {
		return checkReportDao.CheckReportSearchInfo(brands, companyName, locationPhone,
				locationAddress, locationCity, locationState,
				locationZipCode);
	}

	@Transactional
	public List<CheckReportDTO> getcheckreport(
			Set<CheckReportDTO> businesSearchinfo) {
		return null;
	}

	public List<LocalBusinessDTO> getStore() {
		return null;
	}

	@Transactional
	public List<CheckReportDTO> checkReportSearch(String store) {
		return checkReportDao.checkReportSearch(store);
	}

	@Transactional
	public  Set<CheckReportDTO> CheckReportSearchInfo(String store,String locationAddress,String brandname) {
		return checkReportDao.CheckReportSearchInfo(store,locationAddress,brandname);
	}

	@Transactional
	public List<CheckReportDTO> checkListing(String listingId) {
		return checkReportDao.checkListing(listingId);
	}

	@Transactional
	public List<LocalBusinessDTO> getBusinesslisting(String store,String brandname) {
		return checkReportDao.getBusinesslisting(store,brandname);
	}

	@Transactional
	public Integer getErrorCount(String directory, String store) {
		
		return checkReportDao.getErrorCount(directory, store);
	}

	@Transactional
	public List<AccuracyGraphDTO> getGraphInfo(String brandname) {
		return checkReportDao.getGraphInfo(brandname);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfo(String client) {
		
		return checkReportDao.getListOfBusinessInfo(client);
	}

	@Transactional
	public List<CheckReportDTO> getcheckreportInfo(String directory,
			String store,Integer brandId) {
	
		return checkReportDao.getcheckreportInfo(directory, store,brandId);
	}

	@Transactional
	public Set<CheckReportDTO> CheckReportListingSearchInfo(String store,
			String directory,String brandname) {
		return checkReportDao.CheckReportListingSearchInfo(store, directory,brandname) ;
	}


	@Transactional
	public Integer getPathCountFromSearchDomain(String store) {
		return checkReportDao.getPathCountFromSearchDomain(store);
	}

	@Transactional
	public List<CitationGraphDTO> getCitationGraphInfo(String brandname) {
		return checkReportDao.getCitationGraphInfo(brandname);
	}

	@Transactional
	public LocalBusinessDTO getBusinessInfoByStore(String store) {
	
		return checkReportDao.getBusinessInfoByStore(store);
	}
	@Transactional
	public Map<String, Integer> getErrorsCount(String store,String brandname) {
		return checkReportDao.getErrorsCount(store,brandname);
	}

	@Transactional
	public Map<String, List<String>> getPathFromSearch(String store,Integer brandId) {
		return checkReportDao.getPathFromSearch(store,brandId);
	}
	
	@Transactional
	public Map<String, List<String>> getDomainAuthorities(String store,Integer brandId) {
		
		return checkReportDao.getDomainAuthorities(store,brandId);
	}

	@Transactional
	public String getdomainAuthorityFromSearchDomains(String store,Integer brandId) {
		return checkReportDao.getdomainAuthorityFromSearchDomains(store,brandId);
	}

	@Transactional
	public AccuracyDTO getAccuracyListInfo(String store, String brandName) {
		return checkReportDao.getAccuracyListInfo(store,brandName);
	}

	@Transactional
	public void saveAccuracyInfo(AccuracyDTO dto) {
		checkReportDao.saveAccuracyInfo(dto);
		
	}

	@Transactional
	public int getPercentagecategory1(String brandname) {
		return checkReportDao.getPercentagecategory1(brandname);
	}

	@Transactional
	public int getPercentagecategory2(String brandname) {
		return checkReportDao.getPercentagecategory2(brandname);
	}

	@Transactional
	public int getPercentagecategory3(String brandname) {
		return checkReportDao.getPercentagecategory3(brandname);
	}

	@Transactional
	public int getPercentagecategory4(String brandname) {
		return checkReportDao.getPercentagecategory4(brandname);
	}

	@Transactional
	public int gettotalStores(String brandname) {
		return checkReportDao.gettotalStores(brandname);
	}

	@Transactional
	public double getTotalPercentage(String brandname) {
		
		return checkReportDao.getTotalPercentage(brandname);
	}

	@Transactional
	public Integer isStoreExistInAccuracy(String store, String brandName) {
		
		return checkReportDao.isStoreExistInAccuracy(store, brandName);
	}

	@Transactional
	public void updateAccuracyInfo(AccuracyDTO dto) {
		checkReportDao.updateAccuracyInfo(dto);
		
	}

	@Transactional
	public  List<AccuracyDTO> getSortByGoogleAccuracyListInfo(String brandName,String flag) {
		
		return checkReportDao.getSortByGoogleAccuracyListInfo(brandName,flag);
	}

	@Transactional
	public List<AccuracyDTO> getSortByBingAccuracyListInfo(String brandname,
			String flag) {
		
		return checkReportDao.getSortByBingAccuracyListInfo(brandname, flag);
	}

	@Transactional
	public List<AccuracyDTO> getSortByYahooAccuracyListInfo(String brandname,
			String flag) {
		
		return checkReportDao.getSortByYahooAccuracyListInfo(brandname, flag);
	}

	@Transactional
	public List<AccuracyDTO> getSortByYelpAccuracyListInfo(String brandname,
			String flag) {
		
		return checkReportDao.getSortByYelpAccuracyListInfo(brandname, flag);
	}

	@Transactional
	public List<AccuracyDTO> getSortByYpAccuracyListInfo(String brandname,
			String flag) {
		
		return checkReportDao.getSortByYpAccuracyListInfo(brandname, flag);
	}

	@Transactional
	public List<AccuracyDTO> getSortByCitySearchAccuracyListInfo(
			String brandname, String flag) {
		
		return checkReportDao.getSortByCitySearchAccuracyListInfo(brandname, flag);
	}

	@Transactional
	public List<AccuracyDTO> getSortByMapquestAccuracyListInfo(
			String brandname, String flag) {
		
		return checkReportDao.getSortByMapquestAccuracyListInfo(brandname, flag);
	}

	@Transactional
	public List<AccuracyDTO> getSortBySuperPagesAccuracyListInfo(
			String brandname, String flag) {
		
		return checkReportDao.getSortBySuperPagesAccuracyListInfo(brandname, flag);
	}

	@Transactional
	public List<AccuracyDTO> getSortByYellowBookAccuracyListInfo(
			String brandname, String flag) {
		
		return checkReportDao.getSortByYellowBookAccuracyListInfo(brandname, flag);
	}

	@Transactional
	public List<AccuracyDTO> getSortByWhitePagesAccuracyListInfo(
			String brandname, String flag) {
		
		return checkReportDao.getSortByWhitePagesAccuracyListInfo(brandname, flag);
	}

	@Transactional
	public List<AccuracyDTO> getSortByStoreAccuracyListInfo(String brandname,
			String flag) {
		return checkReportDao.getSortByStoreAccuracyListInfo(brandname, flag);
	}

	@Transactional
	public List<AccuracyDTO> getSortByAccuracyListInfo(String brandname,
			String flag) {
		return checkReportDao.getSortByAccuracyListInfo(brandname, flag);
	}

	@Transactional
	public void saveAccuracyGraphInfo(AccuracyGraphDTO accuracyGraphDTO) {
		checkReportDao.saveAccuracyGraphInfo(accuracyGraphDTO);
		
	}

	@Transactional
	public AccuracyDTO getLocationAccuracy(String store, String brandname) {
		return checkReportDao.getLocationAccuracy(store,brandname);
	}

	@Transactional
	public int getGoogleAccuracy(String store, String brandname,
			String directory) {
		
		 return checkReportDao.getGoogleAccuracy(store,brandname,directory);
	}

	@Transactional
	public List<CheckReportDTO> getCheckreportInfo() {
		return checkReportDao.getCheckreportInfo();
	}

	@Transactional
	public void updateCheckreportInfo(Integer clinetId, String zip, String store) {
		checkReportDao.updateCheckreportInfo(clinetId,zip,store);
		
	}

	@Transactional
	public void saveCitationreportInfo(CitationReportEntity citationReportEntity) {
		checkReportDao.saveCitationreportInfo(citationReportEntity);
		
	}

	@Transactional
	public Integer isStoreAndBrandExistInCitation(String store, Integer brandID) {
		return checkReportDao.isStoreAndBrandExistInCitation(store,brandID);
	}

	@Transactional
	public void updateCitationreportInfo(
			CitationReportEntity citationReportEntity) {
		checkReportDao.updateCitationreportInfo(citationReportEntity);
		
	}

	@Transactional
	public List<ValueObject> getCiationData(String brandName,String store) {
		return checkReportDao.getCiationData(brandName,store);
	}

	@Transactional
	public List<ValueObject> getChartDataDS(String brandName) {
		return checkReportDao.getChartDataDS(brandName);
	}

	@Transactional
	public Integer getClinetIdIdByName(String brandName) {
		return checkReportDao.getClinetIdIdByName(brandName);
	}

	@Transactional
	public List<RenewalReportDTO> runRenewalReport(String storeName,
			String brand) {
		return checkReportDao.runRenewalReport(storeName,brand);
	}

	@Transactional
	public List<RenewalReportDTO> runRenewalReportForBrand(String brand) {
		return checkReportDao.runRenewalReportForBrand(brand);
	}

	@Transactional
	public List<RenewalReportDTO> runSummaryReport(Date fromDate, Date toDate,
			String storeName, String brand) {
		return checkReportDao.runSummaryReport(fromDate,toDate,storeName,brand);
	}

	@Transactional
	public List<RenewalReportDTO> runSummaryReportForBrand(Date fromDate,
			Date toDate, String brand) {
		return checkReportDao.runSummaryReportForBrand(fromDate,toDate,brand);
	}
	@Transactional
	public boolean getGoogleGraphInfo(String store, String string,String brandName) {
		return checkReportDao.getGoogleGraphInfo(store,string,brandName);
	}

	@Transactional
	public List<AccuracyGraphDTO> getTotalListingGraphInfo(String brandname) {
		return checkReportDao.getTotalListingGraphInfo(brandname);
	}
	@Transactional
	public List<CitationReportDTO> getCitationStoregraphInfo(String store,
			String brandname) {
		return checkReportDao.getCitationStoregraphInfo(store,brandname);
	}

	@Transactional
	public List<CitationGraphDTO> getCitationBrandGraphInfo(String brandname) {
		return checkReportDao.getCitationBrandGraphInfo(brandname);
	}

	@Transactional
	public List<CitationStoreHistoryDTO> getCitationStoreHistoryInfo(
			String store, String brandname) {
		return checkReportDao.getCitationStoreHistoryInfo(store,brandname);
	}

	
	

}
