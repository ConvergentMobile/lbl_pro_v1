package com.business.web.bean;

import java.util.Date;

public class SchedulerBean {

	private Integer schdulerId;
	private Integer brandID;
	private Integer partnerId;

	private Integer recurring;
	private String hours;
	private Date nextScheduleTime;
	private Date scheduleTime;

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

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public Integer getRecurring() {
		return recurring;
	}

	public void setRecurring(Integer recurring) {
		this.recurring = recurring;
	}

}
