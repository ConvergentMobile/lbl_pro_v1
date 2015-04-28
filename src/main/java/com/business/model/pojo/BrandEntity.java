package com.business.model.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Vasanth
 * 
 *         POJO bean which creates table with the available fields in the
 *         database table on server load-up.
 * 
 */

@Entity
@Table(name = "brands")
public class BrandEntity {

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "brandid")
	private Integer brandID;
	@Column(name = "brandname")
	private String brandName;
	@Column(name = "channelid")
	private Integer channelID;
	@Column(name = "startdate")
	private Date startDate;
	@Column(name = "locationsinvoiced")
	private String locationsInvoiced;
	@Column(name = "submisions")
	private String submisions;
	@Column(name = "activedate")
	private Date activeDate;
	@Column(name = "partneractive")
	private String partnerActive;
	@Column(name = "clientid")
	private Integer clientId;

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

}
