package com.business.common.util;

import org.apache.log4j.Logger;

import com.business.service.BusinessService;
import com.whitespark.ws.ScrapUtil;
import com.whitespark.ws.WhitesparkUtil;

public class RenewalThread extends Thread {
	
	public Logger logger = Logger.getLogger(RenewalThread.class);
	
	BusinessService service = null;
	
	String clinet=null;
	public RenewalThread(BusinessService service, String client) {
		this.service = service;
		this.clinet=client;
	}

	public void run() {
		logger.info("start ::  RenewalThread  ");
		ScrapUtil util = new ScrapUtil();
		try {
			util.renewBrands(this.service,this.clinet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end ::  RenewalThread  ");
	}

}
