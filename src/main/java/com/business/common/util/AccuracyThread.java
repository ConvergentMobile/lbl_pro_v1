package com.business.common.util;

import org.apache.log4j.Logger;

import com.business.service.BusinessService;
import com.whitespark.ws.ScrapUtil;
import com.whitespark.ws.WhitesparkUtil;

public class AccuracyThread extends Thread {
	
	public Logger logger = Logger.getLogger(AccuracyThread.class);
	
	BusinessService service = null;
	
	public AccuracyThread(BusinessService service) {
		this.service = service;
	}

	public void run() {
		logger.info("start ::  ScrappingThread  ");
		AccuracyReportUtil util = new AccuracyReportUtil();
		try {
			util.scrapAccuracyReortInfo(this.service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end ::  ScrappingThread  ");
	}

}
