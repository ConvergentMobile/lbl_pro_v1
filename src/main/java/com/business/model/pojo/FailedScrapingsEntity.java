package com.business.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author Vasanth
 *  POJO bean which creates table with the available fields in the
 * database table on server load-up.
 *
 */
@Entity
@Table(name="failed_scrapings")
public class FailedScrapingsEntity {

	@Id
	@GeneratedValue	

	private Integer fScrapingId;
	
	private String store;

	private String directory;

	public Integer getfScrapingId() {
		return fScrapingId;
	}

	public void setfScrapingId(Integer fScrapingId) {
		this.fScrapingId = fScrapingId;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}


	
	
	
}
