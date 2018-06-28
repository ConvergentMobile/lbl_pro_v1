package com.business.common.dto;


public class RestResponseDTO {
	Integer totalStores;
	Integer totalStoresToAdd;
	Integer totalStoresToUpdate;
	Integer totalStoresToDelete;
	String TrackingId;
	String requestTime;
	String status;
	String responseMessage;
	
	
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
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
		return TrackingId;
	}
	public void setTrackingId(String trackingId) {
		TrackingId = trackingId;
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
