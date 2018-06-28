package com.business.common.dto;

import java.util.Date;

public class SweetIQCoverageDTO {

	private Date date;
	private String locId;
	private Integer brandId;
	private Integer locationCount;
	private Integer totalListingCount;
	private Integer possibleListings;
	private Integer coverage;

	private Integer bingCoverage;
	private Integer facebookCoverage;
	private Integer yelpCoverage;
	private Integer yellowPagesCoverage;
	private Integer googleCoverage;
	private Integer fourSquareCoverage;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getLocId() {
		return locId;
	}
	public void setLocId(String locId) {
		this.locId = locId;
	}
	public Integer getLocationCount() {
		return locationCount;
	}
	public void setLocationCount(Integer locationCount) {
		this.locationCount = locationCount;
	}
	public Integer getTotalListingCount() {
		return totalListingCount;
	}
	public void setTotalListingCount(Integer totalListingCount) {
		this.totalListingCount = totalListingCount;
	}
	public Integer getPossibleListings() {
		return possibleListings;
	}
	public void setPossibleListings(Integer possibleListings) {
		this.possibleListings = possibleListings;
	}
	public Integer getCoverage() {
		return coverage;
	}
	public void setCoverage(Integer coverage) {
		this.coverage = coverage;
	}
	public Integer getBingCoverage() {
		return bingCoverage;
	}
	public void setBingCoverage(Integer bingCoverage) {
		this.bingCoverage = bingCoverage;
	}
	public Integer getFacebookCoverage() {
		return facebookCoverage;
	}
	public void setFacebookCoverage(Integer facebookCoverage) {
		this.facebookCoverage = facebookCoverage;
	}
	public Integer getYelpCoverage() {
		return yelpCoverage;
	}
	public void setYelpCoverage(Integer yelpCoverage) {
		this.yelpCoverage = yelpCoverage;
	}
	public Integer getYellowPagesCoverage() {
		return yellowPagesCoverage;
	}
	public void setYellowPagesCoverage(Integer yellowPagesCoverage) {
		this.yellowPagesCoverage = yellowPagesCoverage;
	}
	public Integer getGoogleCoverage() {
		return googleCoverage;
	}
	public void setGoogleCoverage(Integer googleCoverage) {
		this.googleCoverage = googleCoverage;
	}
	public Integer getFourSquareCoverage() {
		return fourSquareCoverage;
	}
	public void setFourSquareCoverage(Integer fourSquareCoverage) {
		this.fourSquareCoverage = fourSquareCoverage;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	
	

	
}
