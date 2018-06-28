package com.business.common.dto;

import java.util.Date;

/**
 * 
 * It Holds the data Which is used to Transfer the data from UI to BusinessLayer
 * 
 * @author lbl_dev
 * 
 */

public class BrandInfoDTO {

	private Integer id;
	private Integer brandID;
	private String brandName;	
	private Integer channelID;	
	private Date startDate;
	private String locationsInvoiced;
	private String submisions;	
	private String channelName;
	private String displayDate;
	private Date activeDate;
	private String partnerActive;	 
	private Integer clientId;
	private String count;
	private String locationCotracted;
	private String email; 
	private String inactive;
	private String imagePath;
	
	
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getInactive() {
		return inactive;
	}

	public void setInactive(String inactive) {
		this.inactive = inactive;
	}

	 public String getLocationCotracted() {
		return locationCotracted;
	}
	public void setLocationCotracted(String locationCotracted) {
		this.locationCotracted = locationCotracted;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getDisplayDate() {
		return displayDate;
	}
	public void setDisplayDate(String displayDate) {
		this.displayDate = displayDate;
	}
	
	
	
}
