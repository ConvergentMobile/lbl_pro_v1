package com.business.model.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author lbl_dev
 * 
 *         POJO bean which creates table with the available fields in the
 *         database table on server load-up.
 * 
 */

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "exportinfo")
public class ExportReportEntity {

	@Id
	@GeneratedValue
	@Column(name="exportid")
	private Integer exportID;
	@Column(name="date")
	private Date date;
	@Column(name="partner")
	private String partner;
	@Column(name="brandname")
	private String brandName;
	@Column(name="channelname")
	private String channelName;
	@Column(name="numberofrecords")
	private Long numberOfRecords;
	private String userName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(Long numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public Integer getExportID() {
		return exportID;
	}

	public void setExportID(Integer exportID) {
		this.exportID = exportID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}
