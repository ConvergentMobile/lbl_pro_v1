package com.business.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.business.common.dto.LblErrorDTO;
import com.business.service.BusinessService;

public class UploadDataErrorsAddExecutorService implements Callable<String> {

	List<LblErrorDTO> listings = null;
	BusinessService bservice = null;
	String uploadJobId = null;

	public UploadDataErrorsAddExecutorService(List<LblErrorDTO> listings,
			String uploadJobId, BusinessService service) {
		this.listings = listings;
		this.bservice = service;
		this.uploadJobId = uploadJobId;

	}

	public UploadDataErrorsAddExecutorService() {
		// TODO Auto-generated constructor stub
	}

	public String call() throws Exception {

		System.out.println("inserting error records ----------------");

		this.bservice.insertErrorBusiness(this.listings, new Date(),
				this.uploadJobId);
		return null;
	}

	public boolean insertErrorBusiness(List<LblErrorDTO> businessDTO,
			Date date, String uploadJobId, BusinessService service)
			throws InterruptedException {

		ExecutorService executor = Executors.newFixedThreadPool(10);
		// create a list to hold the Future object associated with Callable
		List<Future<String>> list = new ArrayList<Future<String>>();

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
			System.out.println("from : " + fromIndex + " toIdex : " + toIndex
					+ " , Total is: " + size);

			List<LblErrorDTO> listings = businessDTO
					.subList(fromIndex, toIndex);

			Callable<String> callable = new UploadDataErrorsAddExecutorService(
					listings, uploadJobId, service);
			Future<String> future = executor.submit(callable);
			list.add(future);
		}

		// shut down the executor service now
		executor.shutdown();

		System.out.println("execution completed");

		executor.awaitTermination(100, TimeUnit.SECONDS);

		System.out.println("All tasks are finished!");
		return true;

	}
}
