package com.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.KeywordDTO;
import com.business.common.dto.RankDTO;
import com.business.common.dto.SweetIQAccuracyDTO;
import com.business.common.dto.SweetIQCoverageDTO;
import com.business.common.dto.SweetIQDTO;
import com.business.common.dto.SweetIQRankingDTO;
import com.business.model.dataaccess.SweetIQDAO;
import com.business.service.SweetIQService;

@Service
public class SweetIQServiceImpl implements SweetIQService {

	@Autowired
	private SweetIQDAO dao;

	@Transactional
	public void addAccuracyDetails(List<SweetIQAccuracyDTO> accuracyDetails) {
		dao.addAccuracyDetails(accuracyDetails);
	}
	
	@Transactional
	public void saveCoverageReport(List<SweetIQCoverageDTO> coverageReport) {
		dao.saveCoverageReport(coverageReport);
	}
	
	@Transactional
	public List<String> getBranchListFromAccuracy() {
		// TODO Auto-generated method stub
		return dao.getBranchListFromAccuracy();
	}
	
	@Transactional
	public SweetIQCoverageDTO getLocationDirectoryPerformance(Date startDate,
			Date endDate, String store) {
		// TODO Auto-generated method stub
		return dao.getLocationDirectoryPerformance(startDate, endDate, store);
	}
	
	@Transactional
	public SweetIQCoverageDTO getDirectoryPerformance(Date startDate,
			Date endDate, Integer clientId, String store) {
		// TODO Auto-generated method stub
		return dao.getDirectoryPerformance(startDate, endDate, clientId, store);
	}
	
	@Transactional
	public SweetIQCoverageDTO getBrandCoverage(Date startDate, Date endDate,
			Integer clientId) {
		// TODO Auto-generated method stub
		return dao.getBrandCoverage(startDate, endDate, clientId);
	}
	
	@Transactional
	public SweetIQCoverageDTO getListingCoverage(Integer clientId , Date startDate, Date endDate,
			String store) {
		// TODO Auto-generated method stub
		return dao.getListingCoverage(clientId, startDate, endDate, store);
	}
	
	@Transactional
	public void addOrUpdateStores(List<SweetIQDTO> listOfStores) {
		// TODO Auto-generated method stub
		dao.addOrUpdateStores(listOfStores);
	}
	
	@Transactional
	public void addRankAndKeywords(SweetIQRankingDTO dto,
			List<KeywordDTO> keywordList) {
		// TODO Auto-generated method stub
		dao.addRankAndKeywords(dto, keywordList);
	}
	
	@Transactional
	public List<SweetIQRankingDTO> getRanks() {
		// TODO Auto-generated method stub
		return dao.getRanks();
	}
	
	@Transactional
	public RankDTO getRanking(Integer clientId, String store,
			String engine, String type, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return dao.getRanking(clientId, store, engine, type, startDate, endDate);
	}
	
	@Transactional
	public List<SweetIQAccuracyDTO> getAccuarcy(Integer clientId, Date startDate,
			Date endDate, String store) {
		// TODO Auto-generated method stub
		return dao.getAccuarcy(clientId, startDate, endDate, store);
	}
	
	@Transactional
	public List<SweetIQAccuracyDTO> getAccuarcyForStore(Integer clientId,
			Date startDate, Date endDate, String store) {
		// TODO Auto-generated method stub
		return dao.getAccuarcyForStore(clientId, startDate, endDate, store);
	}
	
	@Transactional
	public List<SweetIQDTO> getSweetIQLBLMap(Integer brandId) {
		// TODO Auto-generated method stub
		return dao.getSweetIQLBLMap(brandId);
	}
}
