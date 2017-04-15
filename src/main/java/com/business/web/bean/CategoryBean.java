package com.business.web.bean;

import org.springframework.web.multipart.commons.CommonsMultipartFile;


public class CategoryBean {
	
	private Integer clientId;
	private String categoryId;
    private String categoryLabel;   
    private String syphCode;   
    private String syphDescription;
    private String localezeCategory; 
    private String sicCode;
    private String sicDescription;
    private String factualCategoryId;
    private String factualCategoryDescription;
    private String googleCategory;
	private String bingCategory;
	private String bingCategoryDesc;
	private String ypInternetHeading;
	private String ypDesc;
	private String store;
	private String appleCategory;
	
	
    
    
    
    public String getAppleCategory() {
		return appleCategory;
	}
	public void setAppleCategory(String appleCategory) {
		this.appleCategory = appleCategory;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	private CommonsMultipartFile targetFile;	
    
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getCategoryLabel() {
		return categoryLabel;
	}
	public void setCategoryLabel(String categoryLabel) {
		this.categoryLabel = categoryLabel;
	}
	public String getSyphCode() {
		return syphCode;
	}
	public void setSyphCode(String syphCode) {
		this.syphCode = syphCode;
	}
	public String getSyphDescription() {
		return syphDescription;
	}
	public void setSyphDescription(String syphDescription) {
		this.syphDescription = syphDescription;
	}
	public String getLocalezeCategory() {
		return localezeCategory;
	}
	public void setLocalezeCategory(String localezeCategory) {
		this.localezeCategory = localezeCategory;
	}
	public String getSicCode() {
		return sicCode;
	}
	public void setSicCode(String sicCode) {
		this.sicCode = sicCode;
	}
	public String getSicDescription() {
		return sicDescription;
	}
	public void setSicDescription(String sicDescription) {
		this.sicDescription = sicDescription;
	}
	public String getFactualCategoryId() {
		return factualCategoryId;
	}
	public void setFactualCategoryId(String factualCategoryId) {
		this.factualCategoryId = factualCategoryId;
	}
	public String getFactualCategoryDescription() {
		return factualCategoryDescription;
	}
	public void setFactualCategoryDescription(String factualCategoryDescription) {
		this.factualCategoryDescription = factualCategoryDescription;
	}
	public String getGoogleCategory() {
		return googleCategory;
	}
	public void setGoogleCategory(String googleCategory) {
		this.googleCategory = googleCategory;
	}
	public String getBingCategory() {
		return bingCategory;
	}
	public void setBingCategory(String bingCategory) {
		this.bingCategory = bingCategory;
	}

	public CommonsMultipartFile getTargetFile() {
		return targetFile;
	}
	public void setTargetFile(CommonsMultipartFile targetFile) {
		this.targetFile = targetFile;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getBingCategoryDesc() {
		return bingCategoryDesc;
	}
	public void setBingCategoryDesc(String bingCategoryDesc) {
		this.bingCategoryDesc = bingCategoryDesc;
	}
	public String getYpInternetHeading() {
		return ypInternetHeading;
	}
	public void setYpInternetHeading(String ypInternetHeading) {
		this.ypInternetHeading = ypInternetHeading;
	}
	public String getYpDesc() {
		return ypDesc;
	}
	public void setYpDesc(String ypDesc) {
		this.ypDesc = ypDesc;
	}
}
