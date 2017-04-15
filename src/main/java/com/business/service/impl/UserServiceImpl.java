package com.business.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.UsersDTO;
import com.business.model.dataaccess.UserDao;
import com.business.model.pojo.ChangeTrackingEntity;
import com.business.service.UserService;
import com.business.web.bean.UsersBean;
@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;
	@Transactional
	public void saveUserUserStore(UsersBean bean, List<String> listofStores) {
		userDao.saveUserUserStore(bean, listofStores);
	}

	@Transactional
	public boolean isUserExistis(String userName) {
		return userDao.isUserExistis(userName);
	}

	@Transactional
	public boolean saveUser(UsersBean bean) {
		return userDao.saveUser(bean);
	}


	@Transactional
	public List<LocalBusinessDTO> getListOfUSerBusinessInfo() {
	
		return userDao.getListOfUSerBusinessInfo();
	}

	@Transactional
	public List<LblErrorDTO> getListOfUserErrors() {
		return userDao.getListOfUserErrors();
	}

	@Transactional
	public List<LocalBusinessDTO> getSpecificBusinessInfo(List<Integer> listIds) {
		return userDao.getSpecificBusinessInfo(listIds);
	}

	@Transactional
	public String getBrandChannel(String brandName) {
		return userDao.getBrandChannel(brandName);
	}

	@Transactional
	public boolean exportReportDTO(List<ExportReportDTO> exportReports) {
		return userDao.exportReportDTO(exportReports);
	}

	@Transactional
	public void deleteBusinessInfo(List<Integer> listIds) {
		userDao.deleteBusinessInfo(listIds);
	}


	@Transactional
	public LocalBusinessDTO getBusinessInfo(Integer id) {
	
		return userDao.getBusinessInfo(id);
	}


	@Transactional
	public String getBrandByClientId(Integer clientId) {
	
		return userDao.getBrandByClientId(clientId);
	}


	@Transactional
	public boolean updateBusinessInfo(LocalBusinessDTO businessInfoDto) {
	
		return userDao.updateBusinessInfo(businessInfoDto);
	}


	@Transactional
	public boolean saveChangeTrackingInfo(ChangeTrackingEntity entity) {
	
		return userDao.saveChangeTrackingInfo(entity);
	}


	@Transactional
	public List<LblErrorDTO> getSpecificErrorBusinessInfo(List<Integer> listIds) {
	
		return userDao.getSpecificErrorBusinessInfo(listIds);
	}


	@Transactional
	public void deleteErrorBusinessInfo(List<Integer> listIds) {
	userDao.deleteErrorBusinessInfo(listIds);
		
	}


	@Transactional
	public LblErrorDTO getErrorBusinessInfo(Integer integer) {
	
		return userDao.getErrorBusinessInfo(integer);
	}


	@Transactional
	public Integer getListingId(LblErrorDTO businessInfoDto) {
	
		return userDao.getListingId(businessInfoDto);
	}


	@Transactional
	public void updateErrorBusinessInfo(LblErrorDTO businessInfoDto,
			Integer listingId) {
	userDao.updateErrorBusinessInfo(businessInfoDto, listingId);
		
	}


	@Transactional
	public boolean saveErrorBusinessInfo(LblErrorDTO businessInfoDto) {
	
		return userDao.saveErrorBusinessInfo(businessInfoDto);
	}


	@Transactional
	public Set<LocalBusinessDTO> businesSearchInfo(String brands,
			String companyName, String store, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode) {
	
		return userDao.businesSearchInfo(brands, companyName, store, locationPhone,
				locationAddress, locationCity, locationState, locationZipCode);
	}


	@Transactional
	public Set<LblErrorDTO> errorBusinesSearchInfo(String brands,
			String companyName, String store, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode) {
	
		return userDao.errorBusinesSearchInfo(brands, companyName, store, locationPhone, locationAddress,
				locationCity, locationState, locationZipCode);
	}


	@Transactional
	public List<String> getUserStores() {
	
		return userDao.getUserStores();
	}


	@Transactional
	public List<UsersDTO> userInfo(Integer userId) {
	
		return userDao.userInfo(userId);
	}


	@Transactional
	public List<ExportReportDTO> getListingActivityInf() {
	
		return userDao.getListingActivityInf();
	}

	@Transactional
	public List<ExportReportDTO> getListingActivityInfByBrand(
			LocalBusinessDTO businessDTO) {
		return userDao.getListingActivityInfByBrand(businessDTO);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfo() {
		return userDao.getListOfBusinessInfo();
	}
	@Transactional
	public List<LblErrorDTO> getListOfErrors() {
		return userDao.getListOfErrors();
	}

	@Transactional
	public void deleteUSer(int parseInt) {
		userDao.deleteUSer(parseInt);
	}
	@Transactional
	public List<String> getChannelsBasedOnUser(int parseInt) {
		return userDao.getChannelsBasedOnUser(parseInt);
	}

}
