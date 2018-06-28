package com.business.model.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "sweetiq_ranks")
public class SweetIQRankingEntity {
	
	@Id
	@Column(name = "RANK_ID")
	@GeneratedValue
	private Long id;
	private Date date;
	private String sIQLocationId;
	private String sIQclientId;
	private String sIQBranchId;
	private Integer brandId;
	private Long lblStoreId;
	private String searchType;
	private String engine;
	
	private Long totalKeyWords;
	private Long rankedKeyWords;
	private Double averageRank;
	
	private Double coverage;
	
	
	 @OneToMany(mappedBy = "rankEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)

	 private Set<KeywordEntity> keywords = new HashSet<KeywordEntity>();
	


	public Set<KeywordEntity> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<KeywordEntity> keywords) {
		this.keywords = keywords;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getsIQLocationId() {
		return sIQLocationId;
	}

	public void setsIQLocationId(String sIQLocationId) {
		this.sIQLocationId = sIQLocationId;
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

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public Long getTotalKeyWords() {
		return totalKeyWords;
	}

	public void setTotalKeyWords(Long totalKeyWords) {
		this.totalKeyWords = totalKeyWords;
	}

	public Long getRankedKeyWords() {
		return rankedKeyWords;
	}

	public void setRankedKeyWords(Long rankedKeyWords) {
		this.rankedKeyWords = rankedKeyWords;
	}

	public Double getAverageRank() {
		return averageRank;
	}

	public void setAverageRank(Double averageRank) {
		this.averageRank = averageRank;
	}

	public Double getCoverage() {
		return coverage;
	}

	public void setCoverage(Double coverage) {
		this.coverage = coverage;
	}
	
	
	

}
