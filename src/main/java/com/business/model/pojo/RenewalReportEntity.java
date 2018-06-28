package com.business.model.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author lbl_dev
 * 
 *         POJO bean which creates table with the available fields in the
 *         database table on server load-up.
 * 
 */

@Entity
@Table(name = "renewal_report")
public class RenewalReportEntity {

	@Id
	@GeneratedValue
	private Integer renewal_id;
	private String store;
	private Date acxiomActiveDate;
	private Date infogroupActiveDate;
	private Date factualActiveDate;
	private Date localezeActiveDate;
	private Integer clientId;
	private Date acxiomRenewalDate;
	private Date infogroupRenewalDate;
	private Date factualRenewalDate;
	private Date localezeRenewalDate;
	private String status;
	private Date activeDate;
	private Date renewalDate;
	private String businessName;
	private Date cancelledEffeciveDate;
	
	

	public Date getCancelledEffeciveDate() {
		return cancelledEffeciveDate;
	}

	public void setCancelledEffeciveDate(Date cancelledEffeciveDate) {
		this.cancelledEffeciveDate = cancelledEffeciveDate;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Date getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	public Date getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(Date renewalDate) {
		this.renewalDate = renewalDate;
	}

	public Date getAcxiomRenewalDate() {
		return acxiomRenewalDate;
	}

	public void setAcxiomRenewalDate(Date acxiomRenewalDate) {
		this.acxiomRenewalDate = acxiomRenewalDate;
	}

	public Date getInfogroupRenewalDate() {
		return infogroupRenewalDate;
	}

	public void setInfogroupRenewalDate(Date infogroupRenewalDate) {
		this.infogroupRenewalDate = infogroupRenewalDate;
	}

	public Date getFactualRenewalDate() {
		return factualRenewalDate;
	}

	public void setFactualRenewalDate(Date factualRenewalDate) {
		this.factualRenewalDate = factualRenewalDate;
	}

	public Date getLocalezeRenewalDate() {
		return localezeRenewalDate;
	}

	public void setLocalezeRenewalDate(Date localezeRenewalDate) {
		this.localezeRenewalDate = localezeRenewalDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getRenewal_id() {
		return renewal_id;
	}

	public void setRenewal_id(Integer renewal_id) {
		this.renewal_id = renewal_id;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public Date getAcxiomActiveDate() {
		return acxiomActiveDate;
	}

	public void setAcxiomActiveDate(Date acxiomActiveDate) {
		this.acxiomActiveDate = acxiomActiveDate;
	}

	public Date getInfogroupActiveDate() {
		return infogroupActiveDate;
	}

	public void setInfogroupActiveDate(Date infogroupActiveDate) {
		this.infogroupActiveDate = infogroupActiveDate;
	}

	public Date getFactualActiveDate() {
		return factualActiveDate;
	}

	public void setFactualActiveDate(Date factualActiveDate) {
		this.factualActiveDate = factualActiveDate;
	}

	public Date getLocalezeActiveDate() {
		return localezeActiveDate;
	}

	public void setLocalezeActiveDate(Date localezeActiveDate) {
		this.localezeActiveDate = localezeActiveDate;
	}

}
