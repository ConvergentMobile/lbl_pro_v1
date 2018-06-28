package com.business.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author lbl_dev
 *  POJO bean which creates table with the available fields in the
 * database table on server load-up.
 *
 */
@Entity
@Table(name="accepted_brands")
public class AcceptedBrandsEntity {
	
	@Id
	@GeneratedValue
	private Integer id;
	@Column(name="brandname")
	private String brandName;
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
	
	

}
