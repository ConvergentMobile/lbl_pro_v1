package com.business.service;

import java.util.List;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.LocalBusinessDTO;



public interface InventoryManagementService {

	

	List<BrandInfoDTO> getAllClientLocations();
	public List<BrandInfoDTO> getListOfContrctedAcxiomPartners();
	public List<BrandInfoDTO> getListOfContrctedInfoGroupPartners();
	public List<BrandInfoDTO> getListOfContrctedFactualPartners();
	public List<BrandInfoDTO> getListOfContrctedLocalezePartners();
	public List<BrandInfoDTO> getListOfCOntrctedBrands();
	List<LocalBusinessDTO> searchBusinessListinginfo(String store);
	List<LocalBusinessDTO> searchBusinessListinginfoByBrand(String brand);

}
