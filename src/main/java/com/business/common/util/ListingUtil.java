package com.business.common.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.business.common.dto.LBLStoreDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.service.BusinessService;
import com.business.service.ListingService;

public class ListingUtil {
	
	@Autowired
	private BusinessService businessService;

	@Autowired
	private ListingService listingService;
	
	public ListingUtil(ListingService service, BusinessService businessService) {
		this.listingService = service;
		this.businessService = businessService;
	}
	
	
	void updateBusinessInLBLSystem(LocalBusinessDTO localBusinessDTO) {
		// 1: update business
		Integer clientId = localBusinessDTO.getClientId();
		String store = localBusinessDTO.getStore();
		Long lblStoreID = listingService.getLBLStoreID(clientId, store);
		
		LBLStoreDTO dto = new LBLStoreDTO();
		dto.setClientId(clientId);
		dto.setLblStoreId(lblStoreID);
		dto.setStore(store);

		
		listingService.updateGMBInsightsWithlblStoreID(dto);
	}
	

}
