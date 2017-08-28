package com.lbl.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class BingSchedulerJob extends QuartzJobBean {

	private BingSchedulerTask bingTask;

	public void setRunMeTask(BingSchedulerTask gmbTask) {
		this.bingTask = gmbTask;
	}

	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		try {
			System.out.println("cron is getting trigggred...........");
			bingTask.getBingAnalytics();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
