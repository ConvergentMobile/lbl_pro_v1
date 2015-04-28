package com.business.model.pojo;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Vasanth
 * 
 *         POJO bean which creates table with the available fields in the
 *         database table on server load-up.
 * 
 */


@Entity
@Table(name="channels")
public class ChannelEntity {

	@Id
	@GeneratedValue
	@Column(name="channelid")
	private Integer channelID;
	@Column(name="channelname")
	private String channelName;
	@Column(name="startdate")
	private Date  startDate;
	
	

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
