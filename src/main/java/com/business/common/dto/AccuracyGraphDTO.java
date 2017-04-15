package com.business.common.dto;

import java.util.Date;

public class AccuracyGraphDTO {
	
	
	private Date date;
	private Long listingCount;
	private Integer year;
	private Integer month;
	private Integer day;
		private String brandName;
	
	private String monthName;
	
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getListingCount() {
		return listingCount;
	}
	public void setListingCount(Long listingCount) {
		this.listingCount = listingCount;
	}
	
	
	

}
