package com.business.model.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="accuracy_graph")
public class AccuarcyGraphEntity {
	
	@Id
	@GeneratedValue
	private Integer acgId;
	private Date date;
	private Long listingCount;
	private String brandName;
	
	
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Integer getAcgId() {
		return acgId;
	}
	public void setAcgId(Integer acgId) {
		this.acgId = acgId;
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
