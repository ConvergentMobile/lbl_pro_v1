package com.business.common.dto;

import java.util.Date;

public class InsightsHistory {

	private String date;
	private Long searchCount;
	private Long mapCount;
	private Long websiteCount;
	private Long directionsCount;
	private Long callsCount;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Long searchCount) {
		this.searchCount = searchCount;
	}

	public Long getMapCount() {
		return mapCount;
	}

	public void setMapCount(Long mapCount) {
		this.mapCount = mapCount;
	}

	public Long getWebsiteCount() {
		return websiteCount;
	}

	public void setWebsiteCount(Long websiteCount) {
		this.websiteCount = websiteCount;
	}

	public Long getDirectionsCount() {
		return directionsCount;
	}

	public void setDirectionsCount(Long directionsCount) {
		this.directionsCount = directionsCount;
	}

	public Long getCallsCount() {
		return callsCount;
	}

	public void setCallsCount(Long callsCount) {
		this.callsCount = callsCount;
	}

}
