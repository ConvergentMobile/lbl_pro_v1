package com.business.model.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "api_tracking")
public class RestResponseEntity {

	@Id
	@GeneratedValue
	protected Integer id;
	Integer totalStores;
	Integer totalStoresToAdd;
	Integer totalStoresToUpdate;
	Integer totalStoresToDelete;
	String trackingId;
	String requestTime;
	String status;
	@Type(type = "clob")
	String responseMessage;

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTotalStores() {
		return totalStores;
	}

	public void setTotalStores(Integer totalStores) {
		this.totalStores = totalStores;
	}

	public Integer getTotalStoresToAdd() {
		return totalStoresToAdd;
	}

	public void setTotalStoresToAdd(Integer totalStoresToAdd) {
		this.totalStoresToAdd = totalStoresToAdd;
	}

	public Integer getTotalStoresToUpdate() {
		return totalStoresToUpdate;
	}

	public void setTotalStoresToUpdate(Integer totalStoresToUpdate) {
		this.totalStoresToUpdate = totalStoresToUpdate;
	}

	public Integer getTotalStoresToDelete() {
		return totalStoresToDelete;
	}

	public void setTotalStoresToDelete(Integer totalStoresToDelete) {
		this.totalStoresToDelete = totalStoresToDelete;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
