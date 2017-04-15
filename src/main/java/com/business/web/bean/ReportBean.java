package com.business.web.bean;

/**
 * 
 * @author VASANTH
 * 
 * View Bean to fetch data from java server pages(jsp's) to DTO's
 *
 */
public class ReportBean {
	
	private String name;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String phoneNumber;
	private String countryCode;
	private String email;
	private String hours;
	private String yearEstablished;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getYearEstablished() {
		return yearEstablished;
	}
	public void setYearEstablished(String yearEstablished) {
		this.yearEstablished = yearEstablished;
	}

	
}
