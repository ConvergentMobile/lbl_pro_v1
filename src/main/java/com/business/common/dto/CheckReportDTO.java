package com.business.common.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 *
 */
public class CheckReportDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer listingId;
	private String searchingId;
	private String directory;
	private String businessname;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String store;
	private Date checkedDate;
	private Integer noOfErrors;
	private Integer directoryListingErrors;
	private String storeUrl;
	private String website;
	private String colourcode;
	private Integer brandId;
	private String actualWebAdress;
	
	

	public String getActualWebAdress() {
		return actualWebAdress;
	}

	public void setActualWebAdress(String actualWebAdress) {
		this.actualWebAdress = actualWebAdress;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	

	

	public String getColourcode() {
		return colourcode;
	}

	public void setColourcode(String colourcode) {
		this.colourcode = colourcode;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getStoreUrl() {
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	public Integer getDirectoryListingErrors() {
		return directoryListingErrors;
	}

	public void setDirectoryListingErrors(Integer directoryListingErrors) {
		this.directoryListingErrors = directoryListingErrors;
	}

	public Date getCheckedDate() {
		return checkedDate;
	}

	public void setCheckedDate(Date checkedDate) {
		this.checkedDate = checkedDate;
	}

	public Integer getNoOfErrors() {
		return noOfErrors;
	}

	public void setNoOfErrors(Integer noOfErrors) {
		this.noOfErrors = noOfErrors;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getListingId() {
		return listingId;
	}

	public void setListingId(Integer listingId) {
		this.listingId = listingId;
	}

	public String getSearchingId() {
		return searchingId;
	}

	public void setSearchingId(String searchingId) {
		this.searchingId = searchingId;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getBusinessname() {
		return businessname;
	}

	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

}
