package com.business.common.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * It Holds the data Which is used to Transfer the data from UI to BusinessLayer
 * 
 * @author Vasanth
 * 
 */

public class ExportReportDTO implements Comparable{

	private Integer exportID;
	private String businessID;
	private Date date;
	private String partner;
	private String brandName;
	private String activityDescription;
	private String channelName;
	private Integer count;
	private Map<String, Integer> exportActivity;

	public Integer getExportID() {
		return exportID;
	}

	public void setExportID(Integer exportID) {
		this.exportID = exportID;
	}

	public String getBusinessID() {
		return businessID;
	}

	public void setBusinessID(String businessID) {
		this.businessID = businessID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public int compareTo(Object o) {
		
		ExportReportDTO obj=(ExportReportDTO)o;
		if(this.date.before(obj.getDate())){
			return 1;
		}else if(this.date.after(obj.getDate())){
			return -1;
		}else{
			return 0;
		}
		
		
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Map<String, Integer> getExportActivity() {
		return exportActivity;
	}

	public void setExportActivity(Map<String, Integer> exportActivity) {
		this.exportActivity = exportActivity;
	}
	
}
