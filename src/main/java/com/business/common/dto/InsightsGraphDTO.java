package com.business.common.dto;

import java.util.Date;

public class InsightsGraphDTO {
	
	private Integer id;
	private String brandName;
	private Integer brandId;
	private String store;
	private String state;
	private String city;
	private Date date;

	
	private Long searchCount;
	private Long mapCount;
	
	private Long websiteCount;
	private Long directionsCount;
	private Long callsCount;
	
	private Integer year;
	private Integer month;
	private Integer day;
	
	
	private Long directCount;
	private Long inDirectCount;
	
	private Long totalSearchCount;
	private Long totalViewCount;
	private Long totalActionCount;
	
	
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Long getDirectCount() {
		return directCount;
	}
	public void setDirectCount(Long directCount) {
		this.directCount = directCount;
	}
	public Long getInDirectCount() {
		return inDirectCount;
	}
	public void setInDirectCount(Long inDirectCount) {
		this.inDirectCount = inDirectCount;
	}
	public Long getTotalSearchCount() {
		return totalSearchCount;
	}
	public void setTotalSearchCount(Long totalSearchCount) {
		this.totalSearchCount = totalSearchCount;
	}
	public Long getTotalViewCount() {
		return totalViewCount;
	}
	public void setTotalViewCount(Long totalViewCount) {
		this.totalViewCount = totalViewCount;
	}
	public Long getTotalActionCount() {
		return totalActionCount;
	}
	public void setTotalActionCount(Long totalActionCount) {
		this.totalActionCount = totalActionCount;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getSearchCount() {
		return searchCount;
	}
	public void setSearchCount(Long searchCount) {
		this.searchCount = searchCount;
	}
	public Long getMapCount() {
		return mapCount;
	}
	public void setMapCount(Long mapCount) {
		this.mapCount = mapCount;
	}
	public Long getWebsiteCount() {
		return websiteCount;
	}
	public void setWebsiteCount(Long websiteCount) {
		this.websiteCount = websiteCount;
	}
	public Long getDirectionsCount() {
		return directionsCount;
	}
	public void setDirectionsCount(Long directionsCount) {
		this.directionsCount = directionsCount;
	}
	public Long getCallsCount() {
		return callsCount;
	}
	public void setCallsCount(Long callsCount) {
		this.callsCount = callsCount;
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
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	
	

}
