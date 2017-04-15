package com.business.common.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
public class CitationStoreHistoryDTO{
	private Integer id;
	private Integer brandId;
	private String store;
	private Date date;
	private Integer citationCount;
	private Integer year;
	private Integer month;
	private Integer day;
		private String brandName;
		private String monthName;
		
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
		public Integer getDay() {
			return day;
		}
		public void setDay(Integer day) {
			this.day = day;
		}
		public String getMonthName() {
			return monthName;
		}
		public void setMonthName(String monthName) {
			this.monthName = monthName;
		}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getCitationCount() {
		return citationCount;
	}
	public void setCitationCount(Integer citationCount) {
		this.citationCount = citationCount;
	}
	
	

}
