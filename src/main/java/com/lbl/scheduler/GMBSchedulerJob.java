package com.lbl.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class GMBSchedulerJob extends QuartzJobBean {

	private GMBSchedulerTask gmbTask;

	public void setRunMeTask(GMBSchedulerTask gmbTask) {
		this.gmbTask = gmbTask;
	}

	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		try {
			System.out.println("cron is getting trigggred...........");
			gmbTask.getGMBInsights();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
