package com.business.web.bean;

import java.sql.Date;


/**
 * 
 * @author Vasanth
 * 
 * 
 *         View Bean to fetch data from java server pages(jsp's) to DTO's
 * 
 */

public class UsersBean {
	
	private String name;
	private String lastName;	
	private String passWord;
	private String confirmPassWord;
	private String email;
	private String phone;		
	private Integer userID;
	private String userName;
	private String channelName;
	private String brandName;	
	private Integer roleId;
	private String startDate;
	private String locationsInvoiced;
	private String locationsUploaded;
	private String submisions;
	private String searchName;
	private String fullName;
	private String checkbox;	
	private Date activeDate;
	private String partnerActive;
	private String[] brandID;
	private Integer clientId;
	private String saveType;
	private String viewOnly;
	
	public String getSaveType() {
		return saveType;
	}
	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public Date getActiveDate() {
	  return activeDate;
	 }
	 public void setActiveDate(Date activeDate) {
	  this.activeDate = activeDate;
	 }
	 public String getPartnerActive() {
	  return partnerActive;
	 }
	 public void setPartnerActive(String partnerActive) {
	  this.partnerActive = partnerActive;
	 }
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getConfirmPassWord() {
		return confirmPassWord;
	}
	public void setConfirmPassWord(String confirmPassWord) {
		this.confirmPassWord = confirmPassWord;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getLocationsInvoiced() {
		return locationsInvoiced;
	}
	public void setLocationsInvoiced(String locationsInvoiced) {
		this.locationsInvoiced = locationsInvoiced;
	}
	public String getLocationsUploaded() {
		return locationsUploaded;
	}
	public void setLocationsUploaded(String locationsUploaded) {
		this.locationsUploaded = locationsUploaded;
	}
	public String getSubmisions() {
		return submisions;
	}
	public void setSubmisions(String submisions) {
		this.submisions = submisions;
	}
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	public String getCheckbox() {
		return checkbox;
	}
	public void setCheckbox(String checkbox) {
		this.checkbox = checkbox;
	}
	public String[] getBrandID() {
		return brandID;
	}
	public void setBrandID(String[] brandID) {
		this.brandID = brandID;
	}
	public String getViewOnly() {
		return viewOnly;
	}
	public void setViewOnly(String viewOnly) {
		this.viewOnly = viewOnly;
	}
	
	
	

}
