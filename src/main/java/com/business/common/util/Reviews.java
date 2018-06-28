package com.business.common.util;

import java.util.List;

import com.google.api.services.mybusiness.v4.model.Review;

public class Reviews {
	
	List<Review> reviews;
	String storeCode;
	String state;
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	

}
