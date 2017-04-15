package com.business.model.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * 
 * @author Vasanth
 * 
 *  POJO bean which creates table with the available fields in the
 * database table on server load-up.
 * 
 */
@Entity
@Table(name = "report_params")
public class ReportParams implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="report_params_id")
	protected Integer reportParamsId;
	@Column(name="param_name")
	protected String paramName;
	@Column(name="param_label")	
	protected String paramLabel;
	@Transient
	protected Object paramValue;
	@Transient
	protected String condition;
	
	@ManyToOne
	@JoinColumn(name = "report_id", nullable = false)
	protected ReportEntity report;
	
	public Integer getReportParamsId() {
		return reportParamsId;
	}
	public void setReportParamsId(Integer reportParamsId) {
		this.reportParamsId = reportParamsId;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamLabel() {
		return paramLabel;
	}
	public void setParamLabel(String paramLabel) {
		this.paramLabel = paramLabel;
	}
	public Object getParamValue() {
		return paramValue;
	}
	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public ReportEntity getReport() {
		return report;
	}
	public void setReport(ReportEntity report) {
		this.report = report;
	}
	
}
