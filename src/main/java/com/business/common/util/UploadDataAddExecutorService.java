package com.business.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.business.common.dto.LocalBusinessDTO;
import com.business.service.BusinessService;
import com.business.web.bean.LblErrorBean;
import com.business.web.bean.UploadBusinessBean;

public class UploadDataAddExecutorService implements Callable<String> {
	public static Logger logger = Logger
			.getLogger(UploadDataAddExecutorService.class);
	List<LocalBusinessDTO> listings = null;
	BusinessService bservice = null;
	String uploadJobId = null;

	public UploadDataAddExecutorService(List<LocalBusinessDTO> listings,
			String uploadJobId, BusinessService service) {
		this.listings = listings;
		this.bservice = service;
		this.uploadJobId = uploadJobId;
	}

	public UploadDataAddExecutorService() {
		// TODO Auto-generated constructor stub
	}

	public String call() throws Exception {
		try {
			logger.info("Inerting listings");
			this.bservice.addBusinessList(this.listings, new Date(),
					this.uploadJobId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public boolean addBusinessList(List<LocalBusinessDTO> businessDTO,
			Date date, String uploadJobId, BusinessService service)
			throws InterruptedException {

		ExecutorService executor = Executors.newFixedThreadPool(10);
		// create a list to hold the Future object associated with Callable
		List<Future<String>> list = new ArrayList<Future<String>>();

		/*
		 * int size = businessDTO.size(); int listSizeForThread = size / 3;
		 * listSizeForThread = listSizeForThread + 1;
		 */

		int size = businessDTO.size();
		int listSizeForThread = size / 10;

		System.out.println("listSizeForThread: " + listSizeForThread);

		listSizeForThread = listSizeForThread + 1;

		int noOfThreads = 0;
		if (size <= 100) {
			noOfThreads = 1;
		} else {
			noOfThreads = listSizeForThread;
		}
		int toIndex = 0;
		int fromIndex = 0;

		for (int i = 0; i < noOfThreads; i++) {
			toIndex = (i + 1) * listSizeForThread;
			fromIndex = i * listSizeForThread;

			if (toIndex > size) {
				toIndex = size;
			}
			if (fromIndex > size) {
				fromIndex = size;
			}

			if (noOfThreads == 1) {
				fromIndex = 0;
				toIndex = size;
			}
			List<LocalBusinessDTO> listings = businessDTO.subList(fromIndex,
					toIndex);

			Callable<String> callable = new UploadDataAddExecutorService(
					listings, uploadJobId, service);
			Future<String> future = executor.submit(callable);
			list.add(future);
		}

		// shut down the executor service now
		executor.shutdown();
		executor.awaitTermination(100, TimeUnit.SECONDS);
		return true;

	}
}
