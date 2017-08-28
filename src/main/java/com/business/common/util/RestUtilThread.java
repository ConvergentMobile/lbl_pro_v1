package com.business.common.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.business.common.dto.LocalBusinessDTO;
import com.business.service.BusinessService;
import com.business.service.RestService;
import com.business.service.UploadService;

public class RestUtilThread extends Thread {

	public Logger logger = Logger.getLogger(RestUtilThread.class);

	BusinessService service = null;
	UploadService uploadService = null;
	RestService restService =null;
	List<LocalBusinessDTO> stores = null;
	String trackingId = null;
	boolean isFile=false;
	String authId = null;
	String authKey = null;
	public RestUtilThread(BusinessService service, RestService restService, UploadService uploadService, List<LocalBusinessDTO> validStores, String trackingId, String authId2, String authKey, boolean isFile) {
		this.service = service;
		this.uploadService = uploadService;
		this.restService = restService;
		this.stores = validStores;
		this.trackingId = trackingId;
		this.authId = authId2;
		this.authKey = authKey;
		this.isFile=isFile;
	}

	public void run() {
		logger.info("start ::  RestUtilThread  ");
		RestUtil util = new RestUtil();
		try {
			util.processStores(this.service , this.uploadService,this.restService, this.stores, this.trackingId, this.authId, this.authKey, isFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end ::  RestUtilThread  ");
	}

}
