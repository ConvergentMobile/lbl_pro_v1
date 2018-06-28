package com.business.web.bean;

import java.util.Date;


/**
 * 
 * @author lbl_dev
 * 
 * 
 * View Bean to fetch data from java server pages(jsp's) to DTO's
 * 
 */



public class UploadReportBean {

	private String userName;
	private Date date;
	private String numberOfRecords;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(String numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

}
