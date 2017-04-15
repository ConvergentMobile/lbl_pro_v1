package com.business.model.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "search_domains")
public class SearchDomains {

	@Id
	@GeneratedValue
	private Integer scDomkey;
	private String searchId;
	private String listingName;
	private String domain;
	private String submitUrl;
	private String path;
	private String domainName;

	

	private Date dateSearched;
	private Integer pathsCount;
	private String domainAuthority ;
	

	public String getDomainAuthority() {
		return domainAuthority;
	}

	public void setDomainAuthority(String domainAuthority) {
		this.domainAuthority = domainAuthority;
	}

	public Integer getPathsCount() {
		return pathsCount;
	}

	public void setPathsCount(Integer pathsCount) {
		this.pathsCount = pathsCount;
	}

	public Date getDateSearched() {
		return dateSearched;
	}

	public void setDateSearched(Date dateSearched) {
		this.dateSearched = dateSearched;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public Integer getScDomkey() {
		return scDomkey;
	}

	public void setScDomkey(Integer scDomkey) {
		this.scDomkey = scDomkey;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public String getListingName() {
		return listingName;
	}

	public void setListingName(String listingName) {
		this.listingName = listingName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSubmitUrl() {
		return submitUrl;
	}

	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
