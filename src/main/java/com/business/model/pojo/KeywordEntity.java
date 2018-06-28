package com.business.model.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sweetiq_keywords")
public class KeywordEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	private String keyword;
	private Integer avgRank;
	private Integer coverage;
	
	@ManyToOne
    @JoinColumn(name = "RANK_ID")
	private SweetIQRankingEntity rankEntity;


	public SweetIQRankingEntity getRankEntity() {
		return rankEntity;
	}
	public void setRankEntity(SweetIQRankingEntity rankEntity) {
		this.rankEntity = rankEntity;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
