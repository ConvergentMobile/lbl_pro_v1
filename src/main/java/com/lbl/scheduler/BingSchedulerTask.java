package com.lbl.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.business.common.dto.BingAnalyticsDTO;
import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.LBLStoreDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.BingClient;
import com.business.model.pojo.InsightsGraphEntity;
import com.business.model.pojo.LBLStoreEntity;
import com.business.service.BingService;
import com.business.service.BusinessService;
import com.business.service.ListingService;
import com.gmb.util.GMBUtil;

@Component
public class BingSchedulerTask {
	Logger logger = Logger.getLogger(BingSchedulerTask.class);
	@Autowired
	private BingService service;
	@Autowired
	private BusinessService businessService;

	@Autowired
	private ListingService listingService;

	public void getBingAnalytics() throws Exception {

		//businessService.getAllListings();

		BingClient client = new BingClient(service);

	
		Set<Integer> brandIds = new HashSet<Integer>();

		
		////////// 1. update lisitng
		
		logger.info("Updating Store with LBL store ID");
		List<LblErrorDTO> listOfErrors = listingService.getListOfErrors();
		List<LocalBusinessDTO> allListings = businessService.getAllListings();
		
		for (LocalBusinessDTO localBusinessDTO : allListings) {
			
				Long lblStoreID = listingService.getLBLStoreID(localBusinessDTO.getClientId(), localBusinessDTO.getStore());
				if(lblStoreID==null) {
					LBLStoreDTO dto = new LBLStoreDTO();
					dto.setClientId(localBusinessDTO.getClientId());
					dto.setStore(localBusinessDTO.getStore());
					long next = GMBUtil.getNext();
					dto.setLblStoreId(next);
					listingService.addStore(dto);
				}
				
				// update listings
				listingService.updateLBLBusinessWithLBLStoreID(localBusinessDTO);
		}
		logger.info("Updating errors with LBL store ID");
			//////////2. update errors
		for (LblErrorDTO localBusinessDTO : listOfErrors) {
			
			Long lblStoreID = listingService.getLBLStoreID(localBusinessDTO.getClientId(), localBusinessDTO.getStore());
			if(lblStoreID==null) {
				LBLStoreDTO dto = new LBLStoreDTO();
				dto.setClientId(localBusinessDTO.getClientId());
				dto.setStore(localBusinessDTO.getStore());
				long next = GMBUtil.getNext();
				dto.setLblStoreId(next);
				listingService.addStore(dto);
			}
			// update error listings
			listingService.updateLBLErrorWithLBLStoreID(localBusinessDTO);
			
			
		}
		logger.info("Updating errors with LBL store ID completed");
		
	/*	//////////3. update reports
		logger.info("update reports with LBL StoreID");
		List<LBLStoreDTO> listOfMap = listingService.getLBLStoreMap();
		
		logger.info("total Count: "+ listOfMap.size());
		
		int i =0;
		
		for (LBLStoreDTO lblStoreDTO : listOfMap) {
			 if(i%100==0)
				 logger.info("Strores Completed ae: "+ i);

				listingService.updateGMBInsightsWithlblStoreID(lblStoreDTO);

		}
		*/
		
		// CAlculate Monthly Counts
		
		//logger.info("Calculating month GMB Counts");

		//listingService.calculateMonthlyGMBCounts();
		
		
		Map<Integer, String> brandMap = new HashMap<Integer, String>();

		List<BrandInfoDTO> brandListing = businessService.getBrandListing();
		for (BrandInfoDTO brandInfoDTO : brandListing) {
			brandMap.put(brandInfoDTO.getClientId(), brandInfoDTO.getBrandName());
			brandIds.add(brandInfoDTO.getClientId());
		}
		

		/*for (Integer brandId : brandIds) {

			List<LocalBusinessDTO> allStores = service
					.getStoresByBrand(brandId);
			logger.info("Total Listings Found for "+ brandId + " are: "+ allStores.size());

			for (LocalBusinessDTO localBusinessDTO : allStores) {
				List<BingAnalyticsDTO> analytics = BingClient.getAnalytics(localBusinessDTO);
				service.addAnalytics(analytics);

			}
		
		}*/
		

		//////////3. update reports
		//listingService.updatechangeTrackingReport();
	
		
		//logger.info("Start: Analytics Collection");
		
		/*List<LblErrorDTO> listOfErrors = listingService.getListOfErrors();
		List<LocalBusinessDTO> allListings = businessService.getAllListings();
	
		
		List<String> allStores = new ArrayList<String>();
		List<String> errorStores = new ArrayList<String>();
		List<String> storesToDelete = new ArrayList<String>();
		
		
		for (LocalBusinessDTO localBusinessDTO : allListings) {
			allStores.add(localBusinessDTO.getStore());
		}
		for (LblErrorDTO error : listOfErrors) {
			errorStores.add(error.getStore());
		}
		
		allStores.addAll(errorStores);
		
		List<InsightsGraphEntity> insightsData =  listingService.getStoreInsightsWithNoLBLStore();
		int totalStores=0;
		for (InsightsGraphEntity insightsGraphEntity : insightsData) {
			String store = insightsGraphEntity.getStore();
			if(!allStores.contains(store) ) 
			{
				totalStores++;
				System.out.println("Store not found in system: "+ store);
				storesToDelete.add(store);
			}
		}
		
		listingService.deleteFromInsights(storesToDelete);
		
		System.out.println("Total Store not found in system: "+ totalStores);
		*/

		/*for (Integer brandId : brandIds) {

			List<LocalBusinessDTO> allStores = service
					.getStoresByBrand(brandId);
			logger.info("Total Listings Found for "+ brandId + " are: "+ allStores.size());
			int i =0;
			for (LocalBusinessDTO localBusinessDTO : allStores) {

				List<BingAnalyticsDTO> analytics = BingClient.getAnalytics(localBusinessDTO);
				service.addAnalytics(analytics);
			}
		
		}
		
		logger.info("End: Analytics Collection");*/
		
		
		// change tracking
		// gmbreports
		// bing analytics


	}

}
