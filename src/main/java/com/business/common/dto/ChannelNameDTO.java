package com.business.common.dto;

import java.util.Date;


/**
 * 
 * It Holds the data Which is used to Transfer the data from UI to BusinessLayer
 * 
 * @author Vasanth
 *
 */

public class ChannelNameDTO {

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

	@Override
	public String toString() {
		return "ChannelNameDTO [channelID=" + channelID + ", channelName="
				+ channelName + ", startDate=" + startDate + "]";
	}

	

}
