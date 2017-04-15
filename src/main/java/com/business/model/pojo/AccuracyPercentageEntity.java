package com.business.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Vasanth POJO bean which creates table with the available fields in
 *         the database table on server load-up.
 * 
 */
@Entity
@Table(name = "accuracy_percentage")
public class AccuracyPercentageEntity {

	@Id
	@GeneratedValue
	private Integer id;
	private Integer percentage1;
	private Integer percentage2;
	private Integer percentage3;
	private Integer percentage4;
	private double totalPercentage;
	private Integer brandId;
	

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPercentage1() {
		return percentage1;
	}

	public void setPercentage1(Integer percentage1) {
		this.percentage1 = percentage1;
	}

	public Integer getPercentage2() {
		return percentage2;
	}

	public void setPercentage2(Integer percentage2) {
		this.percentage2 = percentage2;
	}

	public Integer getPercentage3() {
		return percentage3;
	}

	public void setPercentage3(Integer percentage3) {
		this.percentage3 = percentage3;
	}

	public Integer getPercentage4() {
		return percentage4;
	}

	public void setPercentage4(Integer percentage4) {
		this.percentage4 = percentage4;
	}

	public double getTotalPercentage() {
		return totalPercentage;
	}

	public void setTotalPercentage(double totalPercentage) {
		this.totalPercentage = totalPercentage;
	}

	

}
