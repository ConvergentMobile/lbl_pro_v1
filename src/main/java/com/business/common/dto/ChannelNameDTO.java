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

	private Integer id;
	private String channelName;
	
	private Date   startDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		return "ChannelNameDTO [id=" + id + ", channelName=" + channelName
				+ "]";
	}

}
