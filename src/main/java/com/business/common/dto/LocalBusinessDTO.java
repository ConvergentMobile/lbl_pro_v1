package com.business.common.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * It Holds the data Which is used to Transfer the data from UI to BusinessLayer
 * 
 * @author Vasanth
 * 
 */
public class LocalBusinessDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1517265286540211458L;
	private String inCorrectValues;
	private String isCorrectFormat;
	private Long brandsCount;
	private Integer id;
	private String store;
	private String actionCode;
	private String companyName;
	private String alternativeName;
	private String anchorOrHostBusiness;
	private String locationAddress;
	private String suite;
	private String locationCity;
	private String locationState;
	private String locationZipCode;
	private String locationPhone;
	private String fax;
	private String tollFree;
	private String tty;
	private String mobileNumber;
	private String additionalNumber;
	private String category1;
	private String category2;
	private String category3;
	private String category4;
	private String category5;
	private String primaryContactFirstName;
	private String primaryContactLastName;
	private String contactTitle;
	private String contactEmail;
	private String locationEmployeeSize;
	private String title_ManagerOrOwner;
	private String professionalTitle;
	private String professionalAssociations;
	private String shortWebAddress;
	private String webAddress;
	private String aMEX;
	private String discover;
	private String visa;
	private String masterCard;
	private String dinersClub;
	private String debitCard;
	private String storeCard;
	private String otherCard;
	private String cash;
	private String check;
	private String travelersCheck;
	private String financing;
	private String googleCheckout;
	private String invoice;
	private String payPal;
	private String couponLink;
	private String twitterLink;
	private String linkedInLink;
	private String facebookLink;
	private String alternateSocialLink;

	private String youTubeOrVideoLink;
	private String googlePlusLink;
	private String myspaceLink;
	private String logoLink;
	private String pinteristLink;
	private String yelpLink;
	private String products;
	private String services;
	private String productsOrServices_combined;
	private String brands;
	private String client;
	private String keywords;
	private String languages;
	private String yearEstablished;
	private String tagline;
	private String businessDescription;
	private String searchName;
	private String locationClosed;
	private String multiUpdateString;
	private String dateRange;
	private String uploadJobId;
	private Date uploadedTime;
	private String countryCode;
	private String locationEmail;
	private String serviceArea;
	private String mondayOpen;
	private String mondayClose;
	private String tuesdayOpen;
	private String tuesdayClose;
	private String wednesdayOpen;
	private String wednesdayClose;
	private String thursdayOpen;
	private String thursdayClose;
	private String fridayOpen;
	private String fridayClose;
	private String saturdayOpen;
	private String saturdayClose;
	private String sundayOpen;
	private String sundayClose;
	private String ADDRESSPRIVACYFLAG;
	private Integer clientId;
	private Integer locationCount;
	private Integer locationCotracted;
	private String foursquareLink;
	private String instagramLink;
	private String menuLink;
	private String categorySyph;

	private String ypInternetHeading;
	private String businessDescriptionShort;

	private String primaryCategory;
	private String additionalCategories;
	private String localezeCategory;
	private String infoGroupCategory;
	private String syph1;
	private String syph2;
	private String syph3;
	private String syph4;
	private String syph5;
	private String syph6;
	private String submissionType;
	
	private String appleCategory;
	
	private String googleAccountId;
	private String googleLocationId;
	
	private String searchType;
	private String searchValue;
	
	private String trackingId;
	
	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	
	
	

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getGoogleAccountId() {
		return googleAccountId;
	}

	public void setGoogleAccountId(String googleAccountId) {
		this.googleAccountId = googleAccountId;
	}

	public String getGoogleLocationId() {
		return googleLocationId;
	}

	public void setGoogleLocationId(String googleLocationId) {
		this.googleLocationId = googleLocationId;
	}

	public String getAppleCategory() {
		return appleCategory;
	}

	public void setAppleCategory(String appleCategory) {
		this.appleCategory = appleCategory;
	}

	public String getSubmissionType() {
		return submissionType;
	}

	public void setSubmissionType(String submissionType) {
		this.submissionType = submissionType;
	}

	public String getSyph1() {
		return syph1;
	}

	public void setSyph1(String syph1) {
		this.syph1 = syph1;
	}

	public String getSyph2() {
		return syph2;
	}

	public void setSyph2(String syph2) {
		this.syph2 = syph2;
	}

	public String getSyph3() {
		return syph3;
	}

	public void setSyph3(String syph3) {
		this.syph3 = syph3;
	}

	public String getSyph4() {
		return syph4;
	}

	public void setSyph4(String syph4) {
		this.syph4 = syph4;
	}

	public String getSyph5() {
		return syph5;
	}

	public void setSyph5(String syph5) {
		this.syph5 = syph5;
	}

	public String getSyph6() {
		return syph6;
	}

	public void setSyph6(String syph6) {
		this.syph6 = syph6;
	}

	public String getInfoGroupCategory() {
		return infoGroupCategory;
	}

	public void setInfoGroupCategory(String infoGroupCategory) {
		this.infoGroupCategory = infoGroupCategory;
	}

	public String getLocalezeCategory() {
		return localezeCategory;
	}

	public void setLocalezeCategory(String localezeCategory) {
		this.localezeCategory = localezeCategory;
	}

	public Date getUploadedTime() {
		return uploadedTime;
	}

	public void setUploadedTime(Date uploadedTime) {
		this.uploadedTime = uploadedTime;
	}

	public String getPrimaryCategory() {
		return primaryCategory;
	}

	public void setPrimaryCategory(String primaryCategory) {
		this.primaryCategory = primaryCategory;
	}

	public String getAdditionalCategories() {
		return additionalCategories;
	}

	public void setAdditionalCategories(String additionalCategories) {
		this.additionalCategories = additionalCategories;
	}

	public String getBusinessDescriptionShort() {
		return businessDescriptionShort;
	}

	public void setBusinessDescriptionShort(String businessDescriptionShort) {
		this.businessDescriptionShort = businessDescriptionShort;
	}

	public String getYpInternetHeading() {
		return ypInternetHeading;
	}

	public void setYpInternetHeading(String ypInternetHeading) {
		this.ypInternetHeading = ypInternetHeading;
	}

	public String getCategorySyph() {
		return categorySyph;
	}

	public void setCategorySyph(String categorySyph) {
		this.categorySyph = categorySyph;
	}

	public String getFoursquareLink() {
		return foursquareLink;
	}

	public void setFoursquareLink(String foursquareLink) {
		this.foursquareLink = foursquareLink;
	}

	public String getInstagramLink() {
		return instagramLink;
	}

	public void setInstagramLink(String instagramLink) {
		this.instagramLink = instagramLink;
	}

	public String getMenuLink() {
		return menuLink;
	}

	public void setMenuLink(String menuLink) {
		this.menuLink = menuLink;
	}

	public Integer getLocationCount() {
		return locationCount;
	}

	public void setLocationCount(Integer locationCount) {
		this.locationCount = locationCount;
	}

	public Integer getLocationCotracted() {
		return locationCotracted;
	}

	public void setLocationCotracted(Integer locationCotracted) {
		this.locationCotracted = locationCotracted;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	public String getMultiUpdateString() {
		return multiUpdateString;
	}

	public void setMultiUpdateString(String multiUpdateString) {
		this.multiUpdateString = multiUpdateString;

	}

	public String getInCorrectValues() {
		return inCorrectValues;
	}

	public void setInCorrectValues(String inCorrectValues) {
		this.inCorrectValues = inCorrectValues;
	}

	public String getIsCorrectFormat() {
		return isCorrectFormat;
	}

	public void setIsCorrectFormat(String isCorrectFormat) {
		this.isCorrectFormat = isCorrectFormat;
	}

	public Long getBrandsCount() {
		return brandsCount;
	}

	public void setBrandsCount(Long brandsCount) {
		this.brandsCount = brandsCount;
	}

	/*
	 * public int hashCode() {
	 * 
	 * return this.id.hashCode(); }
	 * 
	 * 
	 * public boolean equals(Object obj) { if (this == obj) { return true; }
	 * 
	 * if (obj instanceof LocalBusinessDTO) { LocalBusinessDTO dto =
	 * (LocalBusinessDTO) obj; if (this.id == dto.id) { return true; } } return
	 * false; }
	 */

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAlternativeName() {
		return alternativeName;
	}

	public void setAlternativeName(String alternativeName) {
		this.alternativeName = alternativeName;
	}

	public String getAnchorOrHostBusiness() {
		return anchorOrHostBusiness;
	}

	public void setAnchorOrHostBusiness(String anchorOrHostBusiness) {
		this.anchorOrHostBusiness = anchorOrHostBusiness;
	}

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	public String getSuite() {
		return suite;
	}

	public void setSuite(String suite) {
		this.suite = suite;
	}

	public String getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getLocationState() {
		return locationState;
	}

	public void setLocationState(String locationState) {
		this.locationState = locationState;
	}

	public String getLocationZipCode() {
		return locationZipCode;
	}

	public void setLocationZipCode(String locationZipCode) {
		this.locationZipCode = locationZipCode;
	}

	public String getLocationPhone() {
		return locationPhone;
	}

	public void setLocationPhone(String locationPhone) {
		this.locationPhone = locationPhone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTollFree() {
		return tollFree;
	}

	public void setTollFree(String tollFree) {
		this.tollFree = tollFree;
	}

	public String getTty() {
		return tty;
	}

	public void setTty(String tty) {
		this.tty = tty;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAdditionalNumber() {
		return additionalNumber;
	}

	public void setAdditionalNumber(String additionalNumber) {
		this.additionalNumber = additionalNumber;
	}

	public String getCategory1() {
		return category1;
	}

	public void setCategory1(String category1) {
		this.category1 = category1;
	}

	public String getCategory2() {
		return category2;
	}

	public void setCategory2(String category2) {
		this.category2 = category2;
	}

	public String getCategory3() {
		return category3;
	}

	public void setCategory3(String category3) {
		this.category3 = category3;
	}

	public String getCategory4() {
		return category4;
	}

	public void setCategory4(String category4) {
		this.category4 = category4;
	}

	public String getCategory5() {
		return category5;
	}

	public void setCategory5(String category5) {
		this.category5 = category5;
	}

	public String getPrimaryContactFirstName() {
		return primaryContactFirstName;
	}

	public void setPrimaryContactFirstName(String primaryContactFirstName) {
		this.primaryContactFirstName = primaryContactFirstName;
	}

	public String getPrimaryContactLastName() {
		return primaryContactLastName;
	}

	public void setPrimaryContactLastName(String primaryContactLastName) {
		this.primaryContactLastName = primaryContactLastName;
	}

	public String getContactTitle() {
		return contactTitle;
	}

	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getLocationEmployeeSize() {
		return locationEmployeeSize;
	}

	public void setLocationEmployeeSize(String locationEmployeeSize) {
		this.locationEmployeeSize = locationEmployeeSize;
	}

	public String getTitle_ManagerOrOwner() {
		return title_ManagerOrOwner;
	}

	public void setTitle_ManagerOrOwner(String title_ManagerOrOwner) {
		this.title_ManagerOrOwner = title_ManagerOrOwner;
	}

	public String getProfessionalTitle() {
		return professionalTitle;
	}

	public void setProfessionalTitle(String professionalTitle) {
		this.professionalTitle = professionalTitle;
	}

	public String getProfessionalAssociations() {
		return professionalAssociations;
	}

	public void setProfessionalAssociations(String professionalAssociations) {
		this.professionalAssociations = professionalAssociations;
	}

	public String getShortWebAddress() {
		return shortWebAddress;
	}

	public void setShortWebAddress(String shortWebAddress) {
		this.shortWebAddress = shortWebAddress;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public String getaMEX() {
		return aMEX;
	}

	public void setaMEX(String aMEX) {
		this.aMEX = aMEX;
	}

	public String getDiscover() {
		return discover;
	}

	public void setDiscover(String discover) {
		this.discover = discover;
	}

	public String getVisa() {
		return visa;
	}

	public void setVisa(String visa) {
		this.visa = visa;
	}

	public String getMasterCard() {
		return masterCard;
	}

	public void setMasterCard(String masterCard) {
		this.masterCard = masterCard;
	}

	public String getDinersClub() {
		return dinersClub;
	}

	public void setDinersClub(String dinersClub) {
		this.dinersClub = dinersClub;
	}

	public String getDebitCard() {
		return debitCard;
	}

	public void setDebitCard(String debitCard) {
		this.debitCard = debitCard;
	}

	public String getStoreCard() {
		return storeCard;
	}

	public void setStoreCard(String storeCard) {
		this.storeCard = storeCard;
	}

	public String getOtherCard() {
		return otherCard;
	}

	public void setOtherCard(String otherCard) {
		this.otherCard = otherCard;
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getTravelersCheck() {
		return travelersCheck;
	}

	public void setTravelersCheck(String travelersCheck) {
		this.travelersCheck = travelersCheck;
	}

	public String getFinancing() {
		return financing;
	}

	public void setFinancing(String financing) {
		this.financing = financing;
	}

	public String getGoogleCheckout() {
		return googleCheckout;
	}

	public void setGoogleCheckout(String googleCheckout) {
		this.googleCheckout = googleCheckout;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getPayPal() {
		return payPal;
	}

	public void setPayPal(String payPal) {
		this.payPal = payPal;
	}

	public String getCouponLink() {
		return couponLink;
	}

	public void setCouponLink(String couponLink) {
		this.couponLink = couponLink;
	}

	public String getTwitterLink() {
		return twitterLink;
	}

	public void setTwitterLink(String twitterLink) {
		this.twitterLink = twitterLink;
	}

	public String getLinkedInLink() {
		return linkedInLink;
	}

	public void setLinkedInLink(String linkedInLink) {
		this.linkedInLink = linkedInLink;
	}

	public String getFacebookLink() {
		return facebookLink;
	}

	public void setFacebookLink(String facebookLink) {
		this.facebookLink = facebookLink;
	}

	public String getAlternateSocialLink() {
		return alternateSocialLink;
	}

	public void setAlternateSocialLink(String alternateSocialLink) {
		this.alternateSocialLink = alternateSocialLink;
	}

	public String getYouTubeOrVideoLink() {
		return youTubeOrVideoLink;
	}

	public void setYouTubeOrVideoLink(String youTubeOrVideoLink) {
		this.youTubeOrVideoLink = youTubeOrVideoLink;
	}

	public String getGooglePlusLink() {
		return googlePlusLink;
	}

	public void setGooglePlusLink(String googlePlusLink) {
		this.googlePlusLink = googlePlusLink;
	}

	public String getMyspaceLink() {
		return myspaceLink;
	}

	public void setMyspaceLink(String myspaceLink) {
		this.myspaceLink = myspaceLink;
	}

	public String getLogoLink() {
		return logoLink;
	}

	public void setLogoLink(String logoLink) {
		this.logoLink = logoLink;
	}

	public String getPinteristLink() {
		return pinteristLink;
	}

	public void setPinteristLink(String pinteristLink) {
		this.pinteristLink = pinteristLink;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public String getProductsOrServices_combined() {
		return productsOrServices_combined;
	}

	public void setProductsOrServices_combined(
			String productsOrServices_combined) {
		this.productsOrServices_combined = productsOrServices_combined;
	}

	public String getBrands() {
		return brands;
	}

	public void setBrands(String brands) {
		this.brands = brands;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getYearEstablished() {
		return yearEstablished;
	}

	public void setYearEstablished(String yearEstablished) {
		this.yearEstablished = yearEstablished;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getBusinessDescription() {
		return businessDescription;
	}

	public void setBusinessDescription(String businessDescription) {
		this.businessDescription = businessDescription;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getLocationClosed() {
		return locationClosed;
	}

	public void setLocationClosed(String locationClosed) {
		this.locationClosed = locationClosed;
	}

	public String getUploadJobId() {
		return uploadJobId;
	}

	public void setUploadJobId(String uploadJobId) {
		this.uploadJobId = uploadJobId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getLocationEmail() {
		return locationEmail;
	}

	public void setLocationEmail(String locationEmail) {
		this.locationEmail = locationEmail;
	}

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	public String getMondayOpen() {
		return mondayOpen;
	}

	public void setMondayOpen(String mondayOpen) {
		this.mondayOpen = mondayOpen;
	}

	public String getMondayClose() {
		return mondayClose;
	}

	public void setMondayClose(String mondayClose) {
		this.mondayClose = mondayClose;
	}

	public String getTuesdayOpen() {
		return tuesdayOpen;
	}

	public void setTuesdayOpen(String tuesdayOpen) {
		this.tuesdayOpen = tuesdayOpen;
	}

	public String getTuesdayClose() {
		return tuesdayClose;
	}

	public void setTuesdayClose(String tuesdayClose) {
		this.tuesdayClose = tuesdayClose;
	}

	public String getWednesdayOpen() {
		return wednesdayOpen;
	}

	public void setWednesdayOpen(String wednesdayOpen) {
		this.wednesdayOpen = wednesdayOpen;
	}

	public String getWednesdayClose() {
		return wednesdayClose;
	}

	public void setWednesdayClose(String wednesdayClose) {
		this.wednesdayClose = wednesdayClose;
	}

	public String getThursdayOpen() {
		return thursdayOpen;
	}

	public void setThursdayOpen(String thursdayOpen) {
		this.thursdayOpen = thursdayOpen;
	}

	public String getThursdayClose() {
		return thursdayClose;
	}

	public void setThursdayClose(String thursdayClose) {
		this.thursdayClose = thursdayClose;
	}

	public String getFridayOpen() {
		return fridayOpen;
	}

	public void setFridayOpen(String fridayOpen) {
		this.fridayOpen = fridayOpen;
	}

	public String getFridayClose() {
		return fridayClose;
	}

	public void setFridayClose(String fridayClose) {
		this.fridayClose = fridayClose;
	}

	public String getSaturdayOpen() {
		return saturdayOpen;
	}

	public void setSaturdayOpen(String saturdayOpen) {
		this.saturdayOpen = saturdayOpen;
	}

	public String getSaturdayClose() {
		return saturdayClose;
	}

	public void setSaturdayClose(String saturdayClose) {
		this.saturdayClose = saturdayClose;
	}

	public String getSundayOpen() {
		return sundayOpen;
	}

	public void setSundayOpen(String sundayOpen) {
		this.sundayOpen = sundayOpen;
	}

	public String getSundayClose() {
		return sundayClose;
	}

	public void setSundayClose(String sundayClose) {
		this.sundayClose = sundayClose;
	}

	public String getADDRESSPRIVACYFLAG() {
		return ADDRESSPRIVACYFLAG;
	}

	public void setADDRESSPRIVACYFLAG(String aDDRESSPRIVACYFLAG) {
		ADDRESSPRIVACYFLAG = aDDRESSPRIVACYFLAG;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getYelpLink() {
		return yelpLink;
	}

	public void setYelpLink(String yelpLink) {
		this.yelpLink = yelpLink;
	}

}
