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

@Entity
@Table(name = "uploadinfo")
public class UploadReportEntity {

	@Id
	@GeneratedValue	
	@Column(name = "id")
	private Integer id;
	@Column(name = "username")
	private String userName;
	@Column(name = "date")
	private Date date;
	@Column(name = "numberofrecords")
	private String numberOfRecords;
	private String brand;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(String numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

}
