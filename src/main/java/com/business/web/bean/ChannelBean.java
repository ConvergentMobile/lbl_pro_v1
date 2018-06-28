package com.business.web.bean;

import java.sql.Date;


/**
 * 
 * @author lbl_dev
 * 
 * 
 * View Bean to fetch data from java server pages(jsp's) to DTO's
 * 
 */


public class ChannelBean {

	private Integer channelID;
	private String channelName;
	private Date   startDate;
	

	public Integer getChannelID() {
		return channelID;
	}

	public void setChannelID(Integer channelID) {
		this.channelID = channelID;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	

	
}
