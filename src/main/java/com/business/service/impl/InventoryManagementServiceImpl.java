package com.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.model.dataaccess.InventoryManagementDao;
import com.business.service.InventoryManagementService;
@Service
public class InventoryManagementServiceImpl implements InventoryManagementService {
@Autowired
	private InventoryManagementDao dao;



	@Transactional
	public List<BrandInfoDTO> getAllClientLocations() {
		return dao.getAllClientLocations();
	}



	@Transactional
	public List<BrandInfoDTO> getListOfContrctedAcxiomPartners() {
		
		return dao.getListOfContrctedAcxiomPartners();
	}



	@Transactional
	public List<BrandInfoDTO> getListOfContrctedInfoGroupPartners() {
		
		return dao.getListOfContrctedInfoGroupPartners();
	}



	@Transactional
	public List<BrandInfoDTO> getListOfContrctedFactualPartners() {
		
		return dao.getListOfContrctedFactualPartners();
	}



	@Transactional
	public List<BrandInfoDTO> getListOfContrctedLocalezePartners() {
		
		return dao.getListOfContrctedLocalezePartners();
	}



	@Transactional
	public List<BrandInfoDTO> getListOfCOntrctedBrands() {
		
		return dao.getListOfCOntrctedBrands();
	}



	@Transactional
	public List<LocalBusinessDTO> searchBusinessListinginfo(String store) {
	
		return dao.searchBusinessListinginfo(store);
	}



	@Transactional
	public List<LocalBusinessDTO> searchBusinessListinginfoByBrand(String brand) {
		return dao.searchBusinessListinginfoByBrand(brand);
	}

}
