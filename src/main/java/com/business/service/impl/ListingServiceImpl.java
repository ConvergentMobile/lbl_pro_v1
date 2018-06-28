package com.business.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.LBLStoreDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.model.dataaccess.ListingDAO;
import com.business.model.pojo.InsightsGraphEntity;
import com.business.service.ListingService;
import com.business.web.bean.LocalBusinessBean;

@Service
public class ListingServiceImpl implements ListingService {
	
	Logger logger = Logger.getLogger(ListingServiceImpl.class);
	@Autowired
	private ListingDAO listingDao;
	
	@Transactional
	public void addStore(LBLStoreDTO dto) {
		listingDao.addStore(dto);
		
	}
	
	@Transactional
	public Long getLBLStoreID(Integer clientId, String store) {
		// TODO Auto-generated method stub
		return listingDao.getLBLStoreID(clientId, store);
	}
	
	@Transactional
	public void updateLBLBusinessWithLBLStoreID(LocalBusinessDTO dto) {
		// TODO Auto-generated method stub
		listingDao.updateLBLBusinessWithLBLStoreID(dto);
	}
	
	@Transactional
	public void updateLBLErrorWithLBLStoreID(LblErrorDTO dto) {
		// TODO Auto-generated method stub
		listingDao.updateLBLErrorWithLBLStoreID(dto);
	}
	
	
	@Transactional
	public List<LBLStoreDTO> getLBLStoreMap() {
		// TODO Auto-generated method stub
		return listingDao.getLBLStoreMap();
	}
	
	@Transactional
	public Map<Long, String> getLBLStoreMapByCleintId(Integer clientId) {
		// TODO Auto-generated method stub
		return listingDao.getLBLStoreMapByCleintId(clientId);
	}
	
	@Transactional
	public void updateGMBInsightsWithlblStoreID(LBLStoreDTO lblStoreDTO) {
		// TODO Auto-generated method stub
		listingDao.updateGMBInsightsWithlblStoreID(lblStoreDTO);
	}
	
	@Transactional
	public void updateLBLBusinessWithNewStoreName(LocalBusinessBean bean,
			String newStore) {
		// TODO Auto-generated method stub
		listingDao.updateLBLBusinessWithNewStoreName(bean, newStore);
	}
	@Transactional
	public List<LblErrorDTO> getListOfErrors() {
		// TODO Auto-generated method stub
		return listingDao.getListOfErrors();
	}
	
	@Transactional
	public void deleteStoreFromLBLMap(List<Long> listIds) {
		// TODO Auto-generated method stub
		listingDao.deleteStoreFromLBLMap(listIds);
	}
	
	@Transactional
	public void updatechangeTrackingReport() {
		// TODO Auto-generated method stub
		listingDao.updatechangeTrackingReport();
	}
	
	@Transactional
	public LocalBusinessDTO getBusinessByLBlStoreID(Long lblStoreID) {
		// TODO Auto-generated method stub
		return listingDao.getBusinessByLBlStoreID(lblStoreID);
	}
	
	@Transactional
	public void calculateMonthlyGMBCounts() {
		// TODO Auto-generated method stub
		listingDao.calculateMonthlyGMBCounts();
	}
	@Transactional
	public List<InsightsGraphEntity> getStoreInsightsWithNoLBLStore() {
		// TODO Auto-generated method stub
		return listingDao.getStoreInsightsWithNoLBLStore();
	}
	
	@Transactional
	public void deleteFromInsights(List<String> stores) {
		// TODO Auto-generated method stub
		listingDao.deleteFromInsights(stores);
	}

}
