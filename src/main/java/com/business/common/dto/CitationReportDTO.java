package com.business.common.dto;

import java.util.Date;

public class CitationReportDTO {
	private Integer citationId;
	private Integer pathCount;
	private String directory;
	private String businesName;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String store;
	private Integer brandId;
	private String domainAuthority;
	private String paths;
	private Date dateSearched;
	private Integer year;
	private Integer month;
	private Integer day;
		private String brandName;
		
		private String monthName;
		
		public String getMonthName() {
			return monthName;
		}
		public void setMonthName(String monthName) {
			this.monthName = monthName;
		}
	
	
	
	

	public Integer getYear() {
			return year;
		}

		public void setYear(Integer year) {
			this.year = year;
		}

		public Integer getMonth() {
			return month;
		}

		public void setMonth(Integer month) {
			this.month = month;
		}

		public Integer getDay() {
			return day;
		}

		public void setDay(Integer day) {
			this.day = day;
		}

		public String getBrandName() {
			return brandName;
		}

		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}

	public Date getDateSearched() {
		return dateSearched;
	}

	public void setDateSearched(Date dateSearched) {
		this.dateSearched = dateSearched;
	}
	public String getBusinesName() {
		return businesName;
	}

	public void setBusinesName(String businesName) {
		this.businesName = businesName;
	}

	public String getDomainAuthority() {
		return domainAuthority;
	}

	public void setDomainAuthority(String domainAuthority) {
		this.domainAuthority = domainAuthority;
	}

	public String getPaths() {
		return paths;
	}

	public void setPaths(String paths) {
		this.paths = paths;
	}

	public Integer getCitationId() {
		return citationId;
	}

	public void setCitationId(Integer citationId) {
		this.citationId = citationId;
	}

	public Integer getPathCount() {
		return pathCount;
	}

	public void setPathCount(Integer pathCount) {
		this.pathCount = pathCount;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
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
