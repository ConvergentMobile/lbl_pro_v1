package com.business.web.bean;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 
 * @author Vasanth
 * 
 * 
 * View Bean to fetch data from java server pages(jsp's) to DTO's
 * 
 */

public class BrandBean {

	private Integer brandID;
	private String brandName;
	private Integer channelID;
	private Date startDate;
	private String locationsInvoiced;
	private String submisions;
	private Date activeDate;
	private String partnerActive;
	private Integer clientId;
	
	 public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public Date getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	public String getPartnerActive() {
		return partnerActive;
	}

	public void setPartnerActive(String partnerActive) {
		this.partnerActive = partnerActive;
	}

	public Integer getBrandID() {
		return brandID;
	}

	public void setBrandID(Integer brandID) {
		this.brandID = brandID;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Integer getChannelID() {
		return channelID;
	}

	public void setChannelID(Integer channelID) {
		this.channelID = channelID;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date dateValue = (Date) sdf.parse(startDate);
		this.startDate = dateValue;
	}

	public String getLocationsInvoiced() {
		return locationsInvoiced;
	}

	public void setLocationsInvoiced(String locationsInvoiced) {
		this.locationsInvoiced = locationsInvoiced;
	}

	public String getSubmisions() {
		return submisions;
	}

	public void setSubmisions(String submisions) {
		this.submisions = submisions;
	}

}
