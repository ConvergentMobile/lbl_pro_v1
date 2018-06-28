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

public class UploadDataUpdateExecutorService implements Callable<String> {
	public static Logger logger = Logger
			.getLogger(UploadDataUpdateExecutorService.class);
	List<LocalBusinessDTO> listings = null;
	BusinessService bservice = null;
	String uploadJobId = null;
	String userName = null;

	public UploadDataUpdateExecutorService(List<LocalBusinessDTO> listings,
			String uploadJobId, BusinessService service, String userName) {
		this.listings = listings;
		this.bservice = service;
		this.uploadJobId = uploadJobId;
		this.userName = userName;

	}

	public UploadDataUpdateExecutorService() {
		// TODO Auto-generated constructor stub
	}

	public String call() throws Exception {
		logger.info("Updating listings");
		this.bservice.updateBusinessRecords(this.listings, new Date(),
				this.uploadJobId, this.userName);
		return null;
	}

	public boolean updateBusinessList(List<LocalBusinessDTO> businessDTO,
			Date date, String uploadJobId, BusinessService service,
			String userName) throws InterruptedException {

		ExecutorService executor = Executors.newFixedThreadPool(10);
		// create a list to hold the Future object associated with Callable
		List<Future<String>> list = new ArrayList<Future<String>>();

		int size = businessDTO.size();
		int listSizeForThread = size / 10;

		//System.out.println("listSizeForThread: " + listSizeForThread);

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

			Callable<String> callable = new UploadDataUpdateExecutorService(
					listings, uploadJobId, service, userName);
			Future<String> future = executor.submit(callable);
			list.add(future);
		}

		// shut down the executor service now
		executor.shutdown();

		executor.awaitTermination(100, TimeUnit.SECONDS);

		return true;

	}
}
