package com.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.BingAnalyticsDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.model.dataaccess.BingDAO;
import com.business.service.BingService;
@Service
public class BingServiceImpl implements BingService {
	
	@Autowired
	BingDAO dao;
	
	@Transactional
	public List<LocalBusinessDTO> getStoresByBrand(Integer brandId) {
		// TODO Auto-generated method stub
		return dao.getStoresByBrand(brandId);
	}
	
	@Transactional
	public void addAnalytics(List<BingAnalyticsDTO> analytics) {
		// TODO Auto-generated method stub
		dao.addAnalytics(analytics);
	}
	@Transactional
	public Map<String, String> getBingCategoryDetails(Integer clientId,
			String store) {
		// TODO Auto-generated method stub
		return dao.getBingCategoryDetails(clientId, store);
	}

}
