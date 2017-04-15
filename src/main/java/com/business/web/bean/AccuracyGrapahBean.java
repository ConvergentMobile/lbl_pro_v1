package com.business.web.bean;

import java.util.Date;

public class AccuracyGrapahBean {

	private Date date;
	private Long listingCount;
	private String brandName;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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
