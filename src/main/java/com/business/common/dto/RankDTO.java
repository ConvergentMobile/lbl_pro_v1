package com.business.common.dto;

import java.util.List;
import java.util.Set;

import com.business.model.pojo.KeywordEntity;

public class RankDTO {
	
	// metrics
	private Long totalKeywords;
	private Long rankedKeywords;
	private Double averageRanking;
	private Double coverage;
	
	// keywords
	
	List<KeywordDTO> keywords;
	List<TrendDTO> trends;
	

	public List<TrendDTO> getTrends() {
		return trends;
	}

	public void setTrends(List<TrendDTO> trends) {
		this.trends = trends;
	}

	public Long getTotalKeywords() {
		return totalKeywords;
	}

	public void setTotalKeywords(Long totalKeywords) {
		this.totalKeywords = totalKeywords;
	}

	public Long getRankedKeywords() {
		return rankedKeywords;
	}

	public void setRankedKeywords(Long rankedKeywords) {
		this.rankedKeywords = rankedKeywords;
	}

	public Double getAverageRanking() {
		return averageRanking;
	}

	public void setAverageRanking(Double averageRanking) {
		this.averageRanking = averageRanking;
	}

	public Double getCoverage() {
		return coverage;
	}

	public void setCoverage(Double coverage) {
		this.coverage = coverage;
	}

	public List<KeywordDTO> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<KeywordDTO> keywords) {
		this.keywords = keywords;
	}
	

}
