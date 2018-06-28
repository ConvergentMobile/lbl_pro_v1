package com.business.model.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "store_map")
public class LBLStoreEntity {
	@Id
	private Long lblStoreId;
	private String store;
	private Integer clientId;

	public Long getLblStoreId() {
		return lblStoreId;
	}

	public void setLblStoreId(Long lblStoreId) {
		this.lblStoreId = lblStoreId;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

}
