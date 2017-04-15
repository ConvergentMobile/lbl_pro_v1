package com.business.web.bean;

import java.util.Date;

public class CitationGraphBean {
	private Integer id;
	private String brandName;
	private Integer brandId;
	private String store;
	private Date date;
	private Integer citationCount;
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
