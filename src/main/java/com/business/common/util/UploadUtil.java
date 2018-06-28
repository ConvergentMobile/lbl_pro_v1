package com.business.common.util;

import org.apache.log4j.Logger;

import com.business.service.BusinessService;
import com.business.service.ListingService;
import com.business.service.UploadService;

public class UploadUtil {
	
	// validation for update store

	protected static Logger logger = Logger.getLogger(UploadUtil.class);

	BusinessService service;
	UploadService uploadService;
	ListingService listingService;
	
	public UploadUtil(BusinessService service, UploadService uploadService, ListingService listingService) {
		// TODO Auto-generated constructor stub
	}
	

}
