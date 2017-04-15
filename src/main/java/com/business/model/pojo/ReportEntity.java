package com.business.model.pojo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

/**
 * 
 * @author Vasanth
 * 
 *  POJO bean which creates table with the available fields in the
 * database table on server load-up.
 * 
 */
@Entity
@Table(name = "reports")
public class ReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="report_id")
	protected Integer id;
	@Column(name="name")
	protected String name;	
	@Column(name="description")
	protected String description;
	
	@OneToMany(mappedBy = "report")
	@IndexColumn(name="report_params_id", base = 1)
	protected List<ReportParams> params;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ReportParams> getParams() {
		return params;
	}
	public void setParams(List<ReportParams> params) {
		this.params = params;
	}	
	
}
