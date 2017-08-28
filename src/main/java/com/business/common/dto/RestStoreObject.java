package com.business.common.dto;

import java.util.ArrayList;

public class RestStoreObject {

	private String authKey;
	private String authId;
	private ArrayList<LocalBusinessDTO> stores;
	
	
	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public ArrayList<LocalBusinessDTO> getStores() {
		return stores;
	}

	public void setStores(ArrayList<LocalBusinessDTO> stores) {
		this.stores = stores;
	}

	

}
