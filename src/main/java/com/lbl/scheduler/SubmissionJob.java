package com.lbl.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SubmissionJob extends QuartzJobBean {
	private SubmissionTask submissionTask;
	
	

	public void setRunMeTask(SubmissionTask submissionTask) {
		this.submissionTask = submissionTask;
	}

	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		submissionTask.startSubmissions();

	}
	
	
}