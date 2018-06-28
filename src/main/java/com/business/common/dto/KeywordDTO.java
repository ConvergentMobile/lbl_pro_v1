package com.business.common.dto;

public class KeywordDTO {
	
	private String keyword;
	private Integer avgRank;
	private Integer coverage;
	
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getAvgRank() {
		return avgRank;
	}
	public void setAvgRank(Integer avgRank) {
		this.avgRank = avgRank;
	}
	public Integer getCoverage() {
		return coverage;
	}
	public void setCoverage(Integer coverage) {
		this.coverage = coverage;
	}
	
	

}
