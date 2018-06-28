package com.business.common.dto;

import java.util.List;

public class RestStatus {

	Integer totalStores;
	Integer storesAdded;
	Integer storesUpdated;
	Integer storesDeleted;
	String trackingId;
	String requestTime;
	String status;
	String responseMessage;

	private List<StoresWithErrors> storesWithErrors;

	public Integer getTotalStores() {
		return totalStores;
	}

	public void setTotalStores(Integer totalStores) {
		this.totalStores = totalStores;
	}

	public Integer getStoresAdded() {
		return storesAdded;
	}

	public void setStoresAdded(Integer storesAdded) {
		this.storesAdded = storesAdded;
	}

	public Integer getStoresUpdated() {
		return storesUpdated;
	}

	public void setStoresUpdated(Integer storesUpdated) {
		this.storesUpdated = storesUpdated;
	}

	public Integer getStoresDeleted() {
		return storesDeleted;
	}

	public void setStoresDeleted(Integer storesDeleted) {
		this.storesDeleted = storesDeleted;
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

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public List<StoresWithErrors> getStoresWithErrors() {
		return storesWithErrors;
	}

	public void setStoresWithErrors(List<StoresWithErrors> storesWithErrors) {
		this.storesWithErrors = storesWithErrors;
	}

}
