package com.business.common.util;

import org.apache.log4j.Logger;

import com.business.service.BusinessService;
import com.whitespark.ws.ScrapUtil;
import com.whitespark.ws.WhitesparkUtil;

public class ScrappingThread extends Thread {
	
	public Logger logger = Logger.getLogger(ScrappingThread.class);
	
	BusinessService service = null;
	
	public ScrappingThread(BusinessService service) {
		this.service = service;
	}

	public void run() {
		logger.info("start ::  ScrappingThread  ");
		ScrapUtil util = new ScrapUtil();
		try {
			util.scrapDomains(this.service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end ::  ScrappingThread  ");
	}

}
