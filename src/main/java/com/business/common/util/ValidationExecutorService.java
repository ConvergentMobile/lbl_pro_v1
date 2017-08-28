package com.business.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.business.service.BusinessService;
import com.business.web.bean.LblErrorBean;
import com.business.web.bean.UploadBusinessBean;

public class ValidationExecutorService implements Callable<String> {

	public static Logger logger = Logger
			.getLogger(ValidationExecutorService.class);
	public static List<UploadBusinessBean> validBusinessList = null;
	public static List<LblErrorBean> erroredBusinessList = null;
	public static List<UploadBusinessBean> deleteBusinessList = null;
	static int totalSize = 0;

	List<UploadBusinessBean> listings = null;
	BusinessService bservice = null;

	public ValidationExecutorService(List<UploadBusinessBean> listings,
			BusinessService service) {
		this.listings = listings;
		this.bservice = service;
	}

	public String call() throws Exception {
		int size = listings.size();
		totalSize = totalSize + size;
		UploadBeanValidateUtil validateUtil = new UploadBeanValidateUtil();

		for (int i = 0; i < size; i++) {
			UploadBusinessBean uploadBean = listings.get(i);
			String actionCode = uploadBean.getActionCode();
			if (!actionCode.equalsIgnoreCase("D")) {
				StringBuffer errorMsg = validateUtil.validateUploadBean(
						bservice, uploadBean);

				if (errorMsg.equals("") || errorMsg.length() == 0) {
					logger.info("Count: Valid Lisitng Identified:"
							+ validBusinessList.size());
					validBusinessList.add(uploadBean);
				} else {
					logger.info("Count:: Listing with Errors Identified:"
							+ validBusinessList.size());

					LblErrorBean errorBean = validateUtil
							.copyBeanDetailsToErrorBean(errorMsg.toString(),
									uploadBean);
					erroredBusinessList.add(errorBean);
				}

			}else {
				deleteBusinessList.add(uploadBean);
			}
		}
		return null;
	}

	public static void executeService(BusinessService service,
			List<UploadBusinessBean> totalListngs) throws InterruptedException {
		validBusinessList = new ArrayList<UploadBusinessBean>();
		deleteBusinessList = new ArrayList<UploadBusinessBean>();
		erroredBusinessList = new ArrayList<LblErrorBean>();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		// create a list to hold the Future object associated with Callable
		List<Future<String>> list = new ArrayList<Future<String>>();

		int size = totalListngs.size();
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

			//System.out.println("Start and End is: " + fromIndex + "- "
				//	+ toIndex);

			List<UploadBusinessBean> listings = totalListngs.subList(fromIndex,
					toIndex);

			Callable<String> callable = new ValidationExecutorService(listings,
					service);
			Future<String> future = executor.submit(callable);
			list.add(future);
		}

		for (Future<String> future : list) {
			try {
				logger.info("future.get = " + future.get());
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Thread.sleep(3000);
		// shut down the executor service now
		executor.shutdown();

		executor.awaitTermination(10000, TimeUnit.SECONDS);

	}
}
