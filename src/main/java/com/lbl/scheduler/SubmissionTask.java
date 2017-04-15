package com.lbl.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.business.common.dto.CustomSubmissionsDTO;
import com.business.common.dto.SchedulerDTO;
import com.business.common.util.SubmissionDemonThread;
import com.business.service.BusinessService;

@Component
public class SubmissionTask {
	Logger logger = Logger.getLogger(SubmissionTask.class);
	@Autowired
	private BusinessService service;

	public void startSubmissions() {

		logger.debug("Start: scheduled Submissions");

		List<CustomSubmissionsDTO> scheduleListing = null;
		try {
			scheduleListing = service.getDaySchedules();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		logger.debug("Total schedules found for today are: "
				+ scheduleListing.size());

		if (scheduleListing != null && scheduleListing.size() > 0) {
			logger.debug("Total schedules found for today are: "
					+ scheduleListing.size());

			List<SubmissionDemonThread> threadList = new ArrayList<SubmissionDemonThread>();
			for (CustomSubmissionsDTO dto : scheduleListing) {
				SubmissionDemonThread thread = new SubmissionDemonThread(
						service, dto);

				threadList.add(thread);
			}

			for (SubmissionDemonThread thread : threadList) {
				thread.start();
			}
			logger.debug("Started All Threads.");
			try {
				for (SubmissionDemonThread thread : threadList) {
					thread.join();
				}
			} catch (Exception e) {
				logger.debug("Exception thrown while joinning threads.");
			}
		} else {
			logger.debug("No Submissions found for Today to submit the Listings.");
		}

		logger.debug("End: scheduled Submissions");
	}
}