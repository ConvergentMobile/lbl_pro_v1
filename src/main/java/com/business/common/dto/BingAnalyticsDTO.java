package com.business.common.dto;

import java.util.Date;

public class BingAnalyticsDTO {

	private Integer id;
	private String brandName;
	private Integer brandId;
	private String store;
	private String state;
	private String city;
	private Date date;
	private Integer impressionCount;
	

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

	public Integer getImpressionCount() {
		return impressionCount;
	}

	public void setImpressionCount(Integer impressionCount) {
		this.impressionCount = impressionCount;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	

}
