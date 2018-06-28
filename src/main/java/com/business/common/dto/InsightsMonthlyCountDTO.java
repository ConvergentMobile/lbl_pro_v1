package com.business.common.dto;

import java.util.Date;

public class InsightsMonthlyCountDTO {
	
	private Date date;
	private String store;
	private String state;
	private Long lblStoreId;
	private Integer brandId;
	private Long searchCount;
	private Long actionCount;
	private Long viewCount;
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
	public Long getLblStoreId() {
		return lblStoreId;
	}
	public void setLblStoreId(Long lblStoreId) {
		this.lblStoreId = lblStoreId;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public Long getSearchCount() {
		return searchCount;
	}
	public void setSearchCount(Long searchCount) {
		this.searchCount = searchCount;
	}
	public Long getActionCount() {
		return actionCount;
	}
	public void setActionCount(Long actionCount) {
		this.actionCount = actionCount;
	}
	public Long getViewCount() {
		return viewCount;
	}
	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}
	
	


}
