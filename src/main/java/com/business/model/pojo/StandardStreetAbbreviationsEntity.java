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
@Table(name="street_abbreviations")
public class StandardStreetAbbreviationsEntity {
	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String abbreviation;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}


	
}
