package com.business.common.util;

import org.apache.log4j.Logger;

import com.business.service.BusinessService;

public class SubmissionThread extends Thread {
	
	public Logger logger = Logger.getLogger(SubmissionThread.class);
	
	BusinessService service = null;

	private String client = null;
	
	public SubmissionThread(BusinessService service,String client) {
		this.service = service;
		this.client =client;
	}

	public void run() {
		logger.info("start ::  bulkExport method");
		SubmissionUtil submissionUtil = new SubmissionUtil();
		try {
			submissionUtil.doSubmissions(service,client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end ::  bulkExport method");
	}

}
