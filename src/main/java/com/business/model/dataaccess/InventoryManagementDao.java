package com.business.model.dataaccess;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.LocalBusinessDTO;

public interface InventoryManagementDao {
	
	public List<BrandInfoDTO> getListOfContrctedAcxiomPartners();
	public List<BrandInfoDTO> getListOfContrctedInfoGroupPartners();
	public List<BrandInfoDTO> getListOfContrctedFactualPartners();
	public List<BrandInfoDTO> getListOfContrctedLocalezePartners();
	List<BrandInfoDTO> getAllClientLocations();
	public List<BrandInfoDTO> getListOfCOntrctedBrands();
	List<LocalBusinessDTO> searchBusinessListinginfo(String store);
	public List<LocalBusinessDTO> searchBusinessListinginfoByBrand(String brand);

}
