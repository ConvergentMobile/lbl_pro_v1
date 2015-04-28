package com.business.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author Vasanth
 *  POJO bean which creates table with the available fields in the
 * database table on server load-up.
 *
 */
@Entity
@Table(name="categorys")
public class CategoryEntity {

	@Id
	@GeneratedValue	
	private Integer id;
	@Column(name="category_code")
	private String categoryCode;
	@Column(name="category_name")
	private String categoryName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	
	
}
