package com.business.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="cat_syphcodes")
public class CategorySyphcode {
	
	@Id
	@GeneratedValue
     private Integer syphId;
     private Integer clientId;     
     private String categoryId;
     @Column(name="category_desc")
     private String categoryDescription;
     @Column(name="syph_code")
     private String syphCode;
     @Column(name="syph_desc")
     private String syphDescription;
     
	public Integer getSyphId() {
		return syphId;
	}
	public void setSyphId(Integer syphId) {
		this.syphId = syphId;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryDescription() {
		return categoryDescription;
	}
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}
	public String getSyphCode() {
		return syphCode;
	}
	public void setSyphCode(String syphCode) {
		this.syphCode = syphCode;
	}
	public String getSyphDescription() {
		return syphDescription;
	}
	public void setSyphDescription(String syphDescription) {
		this.syphDescription = syphDescription;
	}
     
     

}
