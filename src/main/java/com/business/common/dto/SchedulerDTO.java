package com.business.common.dto;

import java.util.Date;

/**
 * @author lbl_dev
 * 
 */
public class SchedulerDTO {
	private Integer schdulerId;
	private Integer brandID;
	private String brandName;
	private String partnerName;
	private Integer partnerId;
	private Date scheduleTime;
	private Integer recurring;
	private Date nextScheduleTime;

	public Integer getSchdulerId() {
		return schdulerId;
	}

	public void setSchdulerId(Integer schdulerId) {
		this.schdulerId = schdulerId;
	}

	public Date getNextScheduleTime() {
		return nextScheduleTime;

	}

	public void setNextScheduleTime(Date nextScheduleTime) {
		this.nextScheduleTime = nextScheduleTime;
	}


	private String hours;

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public Integer getBrandID() {
		return brandID;
	}

	public void setBrandID(Integer brandID) {
		this.brandID = brandID;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getRecurring() {
		return recurring;
	}

	public void setRecurring(Integer recurring) {
		this.recurring = recurring;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

}
