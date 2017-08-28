package com.lbl.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.business.common.dto.BingAnalyticsDTO;
import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.BingClient;
import com.business.common.util.BingThread;
import com.business.service.BingService;
import com.business.service.BusinessService;
import com.business.web.bean.UploadBusinessBean;

@Component
public class BingSchedulerTask {
	Logger logger = Logger.getLogger(BingSchedulerTask.class);
	@Autowired
	private BingService service;
	@Autowired
	private BusinessService businessService;
	
	public void getBingAnalytics() throws Exception {
		
		BingClient clinet = new BingClient(service);
		
		logger.info("Start: Analytics Collection");
		Map<Integer, String> brandMap = new HashMap<Integer, String>();

		List<BrandInfoDTO> brandListing = businessService.getBrandListing();
		for (BrandInfoDTO brandInfoDTO : brandListing) {
			brandMap.put(brandInfoDTO.getClientId(), brandInfoDTO.getBrandName());
		}
		
		Set<Integer> brandIds = brandMap.keySet();
		for (Integer brandId : brandIds) {

			List<LocalBusinessDTO> allStores = service
					.getStoresByBrand(brandId);
			logger.info("Total Listings Found for "+ brandId + " are: "+ allStores.size());
			int i=0;

			for (LocalBusinessDTO localBusinessDTO : allStores) {
				i++;
				List<BingAnalyticsDTO> analytics = BingClient.getAnalytics(localBusinessDTO);
				service.addAnalytics(analytics);
				if(i>5)
					break;
			}
		
		}
		
		logger.info("End: Analytics Collection");

	}

}
