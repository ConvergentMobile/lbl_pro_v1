package com.business.model.pojo;

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
@Table(name="stateslist")
public class StatesListEntity {
	@Id
	@GeneratedValue
	private Integer id;
	private String state;
	private String code;
	private String country;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	
}
