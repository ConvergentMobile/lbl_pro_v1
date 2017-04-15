package com.business.model.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "schedule")
public class SchedulerEntity {

	@Id
	@GeneratedValue
	private Integer schdulerId;
	@Column(name = "brandid")
	private Integer brandID;
	private Integer partnerId;
	@Column(name = "scheduleTime")
	private Date scheduleTime;
	@Column(name = "nextScheduleTime")
	private Date nextScheduleTime;

	private String hours;
	private Integer recurring;

	public Date getNextScheduleTime() {
		return nextScheduleTime;
	}

	public void setNextScheduleTime(Date nextScheduleTime) {
		this.nextScheduleTime = nextScheduleTime;
	}

	public Integer getSchdulerId() {
		return schdulerId;
	}

	public void setSchdulerId(Integer schdulerId) {
		this.schdulerId = schdulerId;
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
