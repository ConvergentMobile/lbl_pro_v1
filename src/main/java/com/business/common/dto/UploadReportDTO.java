package com.business.common.dto;

import java.util.Date;

/**
 * @author lbl_dev It Holds the data Which is used to Transfer the data from UI
 *         to BusinessLayer
 * 
 * 
 * 
 */
public class UploadReportDTO {

	private String userName;
	private Date date;
	private String numberOfRecords;
	private String brand;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

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
