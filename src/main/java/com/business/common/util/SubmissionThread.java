package com.business.common.util;

import org.apache.log4j.Logger;

import com.business.service.BusinessService;

public class SubmissionThread extends Thread {
	
	public Logger logger = Logger.getLogger(SubmissionThread.class);
	
	BusinessService service = null;
	
	public SubmissionThread(BusinessService service) {
		this.service = service;
	}

	public void run() {
		logger.info("start ::  bulkExport method");
		SubmissionUtil submissionUtil = new SubmissionUtil();
		try {
			submissionUtil.doSubmissions(service);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("end ::  bulkExport method");
	}

}
