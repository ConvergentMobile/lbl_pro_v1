package com.business.web.bean;

import java.util.List;

public class BussinessData {

	private List<UploadBusinessBean> validBusinessList;
	
	private List<LblErrorBean> erroredBusinessList;

	public List<UploadBusinessBean> getValidBusinessList() {
		return validBusinessList;
	}

	public void setValidBusinessList(List<UploadBusinessBean> validBusinessList) {
		this.validBusinessList = validBusinessList;
	}

	public List<LblErrorBean> getErroredBusinessList() {
		return erroredBusinessList;
	}

	public void setErroredBusinessList(List<LblErrorBean> erroredBusinessList) {
		this.erroredBusinessList = erroredBusinessList;
	}
}
