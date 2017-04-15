package com.business.common.dto;

public class CitationDTO {
	private Integer id;
	private String store;
	private String businessName;
	private String address;
	private String phoneNumber;
	private Integer citationCount;
	private String path;
	private String domainAuthority ;
	private String state;
	private String zipCode;
	private String citationUrl;
	
	
	

	public String getCitationUrl() {
		return citationUrl;
	}

	public void setCitationUrl(String citationUrl) {
		this.citationUrl = citationUrl;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getDomainAuthority() {
		return domainAuthority;
	}

	public void setDomainAuthority(String domainAuthority) {
		this.domainAuthority = domainAuthority;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getCitationCount() {
		return citationCount;
	}

	public void setCitationCount(Integer citationCount) {
		this.citationCount = citationCount;
	}

}
