package com.business.model.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "sweetiq_accuracy")
public class SweetIQAccuracyEntity {

	@Id
	@GeneratedValue
	private Long id; 
	private Integer brandId;
	private String locId;
	private String branchId;
	private String directory;
	private String directoryName;
	@Type(type = "clob")
	private String URL;
	private String Name;
	private String provided_Name;
	private String Name_Match;
	private String civic;
	private String provided_Civic;
	private String civic_Match;
	private String Street_Address;
	private String provided_Street_Address;
	private String street_Address_Match;
	private String Address;
	private String location_Address;
	private String address_Match;
	private String city;
	private String provided_City;
	private String city_Match;
	private String province_State;
	private String provided_Province_State;
	private String province_State_Match;
	private String Postal_ZipCode;
	private String provided_PostalZipCode;
	private String postalZiCode_Match;
	private String country;
	private String provided_Country;
	private String country_Match;
	private String listing_Phone;
	private String provided_Phone;
	private String phone_Match;
	@Type(type = "clob")
	private String website;
	@Type(type = "clob")
	private String provided_Website;
	private String website_Match;
	private String average_Match;
	private String duplicate_Status;
	private Date date;
	
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLocId() {
		return locId;
	}
	public void setLocId(String locId) {
		this.locId = locId;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public String getDirectoryName() {
		return directoryName;
	}
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getProvided_Name() {
		return provided_Name;
	}
	public void setProvided_Name(String provided_Name) {
		this.provided_Name = provided_Name;
	}
	public String getName_Match() {
		return Name_Match;
	}
	public void setName_Match(String name_Match) {
		Name_Match = name_Match;
	}
	public String getCivic() {
		return civic;
	}
	public void setCivic(String civic) {
		this.civic = civic;
	}
	public String getProvided_Civic() {
		return provided_Civic;
	}
	public void setProvided_Civic(String provided_Civic) {
		this.provided_Civic = provided_Civic;
	}
	public String getCivic_Match() {
		return civic_Match;
	}
	public void setCivic_Match(String civic_Match) {
		this.civic_Match = civic_Match;
	}
	public String getStreet_Address() {
		return Street_Address;
	}
	public void setStreet_Address(String street_Address) {
		Street_Address = street_Address;
	}
	public String getProvided_Street_Address() {
		return provided_Street_Address;
	}
	public void setProvided_Street_Address(String provided_Street_Address) {
		this.provided_Street_Address = provided_Street_Address;
	}
	public String getStreet_Address_Match() {
		return street_Address_Match;
	}
	public void setStreet_Address_Match(String street_Address_Match) {
		this.street_Address_Match = street_Address_Match;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getLocation_Address() {
		return location_Address;
	}
	public void setLocation_Address(String location_Address) {
		this.location_Address = location_Address;
	}
	public String getAddress_Match() {
		return address_Match;
	}
	public void setAddress_Match(String address_Match) {
		this.address_Match = address_Match;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvided_City() {
		return provided_City;
	}
	public void setProvided_City(String provided_City) {
		this.provided_City = provided_City;
	}
	public String getCity_Match() {
		return city_Match;
	}
	public void setCity_Match(String city_Match) {
		this.city_Match = city_Match;
	}
	public String getProvince_State() {
		return province_State;
	}
	public void setProvince_State(String province_State) {
		this.province_State = province_State;
	}
	public String getProvided_Province_State() {
		return provided_Province_State;
	}
	public void setProvided_Province_State(String provided_Province_State) {
		this.provided_Province_State = provided_Province_State;
	}
	public String getProvince_State_Match() {
		return province_State_Match;
	}
	public void setProvince_State_Match(String province_State_Match) {
		this.province_State_Match = province_State_Match;
	}
	public String getPostal_ZipCode() {
		return Postal_ZipCode;
	}
	public void setPostal_ZipCode(String postal_ZipCode) {
		Postal_ZipCode = postal_ZipCode;
	}
	public String getProvided_PostalZipCode() {
		return provided_PostalZipCode;
	}
	public void setProvided_PostalZipCode(String provided_PostalZipCode) {
		this.provided_PostalZipCode = provided_PostalZipCode;
	}
	public String getPostalZiCode_Match() {
		return postalZiCode_Match;
	}
	public void setPostalZiCode_Match(String postalZiCode_Match) {
		this.postalZiCode_Match = postalZiCode_Match;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvided_Country() {
		return provided_Country;
	}
	public void setProvided_Country(String provided_Country) {
		this.provided_Country = provided_Country;
	}
	public String getCountry_Match() {
		return country_Match;
	}
	public void setCountry_Match(String country_Match) {
		this.country_Match = country_Match;
	}
	public String getListing_Phone() {
		return listing_Phone;
	}
	public void setListing_Phone(String listing_Phone) {
		this.listing_Phone = listing_Phone;
	}
	public String getProvided_Phone() {
		return provided_Phone;
	}
	public void setProvided_Phone(String provided_Phone) {
		this.provided_Phone = provided_Phone;
	}
	public String getPhone_Match() {
		return phone_Match;
	}
	public void setPhone_Match(String phone_Match) {
		this.phone_Match = phone_Match;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getProvided_Website() {
		return provided_Website;
	}
	public void setProvided_Website(String provided_Website) {
		this.provided_Website = provided_Website;
	}
	public String getWebsite_Match() {
		return website_Match;
	}
	public void setWebsite_Match(String website_Match) {
		this.website_Match = website_Match;
	}
	public String getAverage_Match() {
		return average_Match;
	}
	public void setAverage_Match(String average_Match) {
		this.average_Match = average_Match;
	}
	public String getDuplicate_Status() {
		return duplicate_Status;
	}
	public void setDuplicate_Status(String duplicate_Status) {
		this.duplicate_Status = duplicate_Status;
	}
	 

	

}
