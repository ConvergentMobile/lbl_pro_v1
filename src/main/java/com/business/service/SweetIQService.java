package com.business.service;

import java.util.Date;
import java.util.List;

import com.business.common.dto.KeywordDTO;
import com.business.common.dto.RankDTO;
import com.business.common.dto.SweetIQAccuracyDTO;
import com.business.common.dto.SweetIQCoverageDTO;
import com.business.common.dto.SweetIQDTO;
import com.business.common.dto.SweetIQRankingDTO;

public interface SweetIQService {
	
	public void addAccuracyDetails(List<SweetIQAccuracyDTO> accuracyDetails);

	public List<String> getBranchListFromAccuracy();

	public void saveCoverageReport(List<SweetIQCoverageDTO> coverageReport);


	public SweetIQCoverageDTO getLocationDirectoryPerformance(Date startDate,
			Date endDate, String store);

	public SweetIQCoverageDTO getDirectoryPerformance(Date startDate,
			Date endDate, Integer clientId, String store);

	public SweetIQCoverageDTO getListingCoverage(Integer clientId, Date startDate, Date endDate,
			String store);

	public SweetIQCoverageDTO getBrandCoverage(Date startDate, Date endDate,
			Integer clientId);

	public void addOrUpdateStores(List<SweetIQDTO> listOfStores);

	public void addRankAndKeywords(SweetIQRankingDTO dto,
			List<KeywordDTO> keywordList);

	public List<SweetIQRankingDTO> getRanks();

	public List<SweetIQDTO> getSweetIQLBLMap(Integer brandId);

	public RankDTO getRanking(Integer clientId, String store,
			String engine, String type, Date startDate, Date endDate);

	public List<SweetIQAccuracyDTO> getAccuarcy(Integer clientId, Date startDate,
			Date endDate, String store);

	public List<SweetIQAccuracyDTO> getAccuarcyForStore(Integer clientId,
			Date startDate, Date endDate, String store);

}
