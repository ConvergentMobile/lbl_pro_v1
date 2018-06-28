package com.business.common.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.business.common.dto.LocalBusinessDTO;
import com.business.service.BusinessService;
import com.business.service.ListingService;

public class GMBThread extends Thread {

	public Logger logger = Logger.getLogger(ScrappingThread.class);

	BusinessService service = null;
	
	ListingService listingService = null;

	GMBClient cleint = null;
	List<LocalBusinessDTO> stores;

	public GMBThread(BusinessService service,ListingService listingService,
			List<LocalBusinessDTO> listOfStores) {
		this.service = service;
		this.listingService = listingService;
		this.stores = listOfStores;

	}

	public void run() {
		logger.info("start ::  GMBThread  ");
		cleint = new GMBClient(service);

		try {
			for (LocalBusinessDTO dto : stores) {
				Long lblStoreID = listingService.getLBLStoreID(dto.getClientId(), dto.getStore());
				LocalBusinessDTO businessByLBlStoreID = listingService.getBusinessByLBlStoreID(lblStoreID);
				dto.setGoogleAccountId(businessByLBlStoreID.getGoogleAccountId());
				dto.setGoogleLocationId(businessByLBlStoreID.getGoogleLocationId());
				GMBClient.updateListingOnGoogle(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end ::  GMBThread  ");
	}

}
