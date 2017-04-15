package com.business.common.util;

import org.apache.log4j.Logger;

import com.business.service.BusinessService;

public class CitationThread extends Thread{
	public Logger logger = Logger.getLogger(CitationThread.class);
	BusinessService service = null;
	
	public CitationThread(BusinessService service) {
		this.service = service;
	}

	public void run() {
		logger.info("start ::  CitationThread  ");
		CitationReportUtil util = new CitationReportUtil();
		try {
			util.scrapCitationReortInfo(this.service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end ::  CitationThread  ");
	}


}
