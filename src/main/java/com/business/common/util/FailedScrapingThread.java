package com.business.common.util;

import org.apache.log4j.Logger;

import com.business.service.BusinessService;
import com.whitespark.ws.ScrapUtil;
import com.whitespark.ws.WhitesparkUtil;

public class FailedScrapingThread extends Thread {
	
	public Logger logger = Logger.getLogger(FailedScrapingThread.class);
	
	BusinessService service = null;
	
	public FailedScrapingThread(BusinessService service) {
		this.service = service;
	}

	public void run() {
		logger.info("start ::  ScrappingThread  ");
		FailedScrapinglUtil util = new FailedScrapinglUtil();
		try {
			util.StoresFailed(this.service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end ::  ScrappingThread  ");
	}

}
