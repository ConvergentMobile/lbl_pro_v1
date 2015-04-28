package com.business.web.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ValueObject;

public class ReportForm implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Integer reportId;
	protected ReportEntity report;
	protected Date startDate;
	protected Date endDate;
	protected List<ValueObject> reportRows;
	protected List<String> reportColumnHeaders;
	protected String sortColumn;
	protected String sortOrder;
	
	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public ReportEntity getReport() {
		return report;
	}

	public void setReport(ReportEntity report) {
		this.report = report;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<ValueObject> getReportRows() {
		return reportRows;
	}

	public void setReportRows(List<ValueObject> reportRows) {
		this.reportRows = reportRows;
	}

	public List<String> getReportColumnHeaders() {
		return reportColumnHeaders;
	}

	public void setReportColumnHeaders(List<String> reportColumnHeaders) {
		this.reportColumnHeaders = reportColumnHeaders;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	
}
