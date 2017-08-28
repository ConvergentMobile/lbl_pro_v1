package com.business.service;

import java.util.List;
import java.util.Map;

import com.business.common.dto.BingAnalyticsDTO;
import com.business.common.dto.LocalBusinessDTO;

public interface BingService {

	List<LocalBusinessDTO> getStoresByBrand(Integer brandId);

	void addAnalytics(List<BingAnalyticsDTO> analytics);

	Map<String, String> getBingCategoryDetails(Integer clientId, String store);

}
