package com.lbl.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SIQSchedulerJob extends QuartzJobBean {

	private SIQSchedulerTask siqTask;

	public void setRunMeTask(SIQSchedulerTask siqTask) {
		this.siqTask = siqTask;
	}

	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		try {
			System.out.println("swetiq cron is getting trigggred...........");
			siqTask.pullSweetIQReport();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
