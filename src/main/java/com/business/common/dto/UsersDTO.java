package com.business.common.dto;

/**
 * 
 * It Holds the data Which is used to Transfer the data from UI to BusinessLayer
 * 
 * @author Vasanth
 * 
 */

public class UsersDTO {
	
	
	private Integer userID;
	private String name;
	private String lastName;
	private String userName;
	private String passWord;
	private String confirmPassWord;
	private String email;
	private String phone;	
	private String channelName;
	private Integer roleId;
	private String searchName;
	private String fullName;
	private String checkbox;
	private String[] brandID;
	private String viewOnly;
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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
