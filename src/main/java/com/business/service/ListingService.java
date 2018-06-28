package com.business.service;

import java.util.List;
import java.util.Map;

import com.business.common.dto.LBLStoreDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.model.pojo.InsightsGraphEntity;
import com.business.web.bean.LocalBusinessBean;

public interface ListingService {

	void addStore(LBLStoreDTO dto);

	Long getLBLStoreID(Integer clientId, String store);

	void updateLBLBusinessWithLBLStoreID(LocalBusinessDTO localBusinessDTO);

	void updateLBLErrorWithLBLStoreID(LblErrorDTO localBusinessDTO);

	List<LBLStoreDTO> getLBLStoreMap();
	
	Map<Long, String> getLBLStoreMapByCleintId(Integer clientId);

	void updateGMBInsightsWithlblStoreID(LBLStoreDTO lblStoreDTO);

	void updateLBLBusinessWithNewStoreName(LocalBusinessBean bean,
			String newStore);

	List<LblErrorDTO> getListOfErrors();

	void deleteStoreFromLBLMap(List<Long> listIds);

	void updatechangeTrackingReport();

	LocalBusinessDTO getBusinessByLBlStoreID(Long lblStoreID);

	void calculateMonthlyGMBCounts();

	List<InsightsGraphEntity> getStoreInsightsWithNoLBLStore();

	void deleteFromInsights(List<String> storesToDelete);


}
