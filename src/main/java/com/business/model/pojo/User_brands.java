package com.business.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_brands")
public class User_brands {

	@Id
	@GeneratedValue
	private Integer userbrandID;
	@Column(name = "channelid")
	private Integer channelID;
	@Column(name = "brandid")
	private Integer brandID;
	@Column(name = "userid")
	private Integer userID;

	public Integer getUserbrandID() {
		return userbrandID;
	}

	public void setUserbrandID(Integer userbrandID) {
		this.userbrandID = userbrandID;
	}

	public Integer getChannelID() {
		return channelID;
	}

	public void setChannelID(Integer channelID) {
		this.channelID = channelID;
	}

	public Integer getBrandID() {
		return brandID;
	}

	public void setBrandID(Integer brandID) {
		this.brandID = brandID;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

}
