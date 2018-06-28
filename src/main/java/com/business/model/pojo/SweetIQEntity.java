package com.business.model.pojo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.business.common.dto.KeywordDTO;

@Entity
@Table(name = "sweetiq_lbl_map")
public class SweetIQEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	private String store;
	private Integer brandId;
	private Long lblStoreId;
	
	private String sIQclientId;
	private String sIQBranchId;
	private String sIQLocationId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public Long getLblStoreId() {
		return lblStoreId;
	}
	public void setLblStoreId(Long lblStoreId) {
		this.lblStoreId = lblStoreId;
	}
	public String getsIQclientId() {
		return sIQclientId;
	}
	public void setsIQclientId(String sIQclientId) {
		this.sIQclientId = sIQclientId;
	}
	public String getsIQBranchId() {
		return sIQBranchId;
	}
	public void setsIQBranchId(String sIQBranchId) {
		this.sIQBranchId = sIQBranchId;
	}
	public String getsIQLocationId() {
		return sIQLocationId;
	}
	public void setsIQLocationId(String sIQLocationId) {
		this.sIQLocationId = sIQLocationId;
	}
	
	

}
