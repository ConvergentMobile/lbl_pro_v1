package com.business.common.util;

import org.apache.log4j.Logger;

import com.business.service.BusinessService;
import com.whitespark.ws.WhitesparkUtil;

public class WhitesparkThread extends Thread {
	
	public Logger logger = Logger.getLogger(WhitesparkThread.class);
	
	BusinessService service = null;
	
	public WhitesparkThread(BusinessService service) {
		this.service = service;
	}

	public void run() {
		logger.info("start ::  WhiteSpart Search ");
		WhitesparkUtil util = new WhitesparkUtil();
		try {
			util.triggerWhiteSparkSerach(this.service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end ::  WhiteSpart Search ");
	}

}
