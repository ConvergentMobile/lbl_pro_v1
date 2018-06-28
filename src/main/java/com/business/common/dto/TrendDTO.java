package com.business.common.dto;

import java.util.Date;

public class TrendDTO {
	
	Date date;
	Double coverage;
	Double rank;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Double getCoverage() {
		return coverage;
	}
	public void setCoverage(Double coverage) {
		this.coverage = coverage;
	}
	public Double getRank() {
		return rank;
	}
	public void setRank(Double rank) {
		this.rank = rank;
	}


}
