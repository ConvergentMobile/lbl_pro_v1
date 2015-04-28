package com.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.ForgotPasswordDto;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.UploadReportDTO;
import com.business.common.dto.UsersDTO;
import com.business.common.util.DateUtil;
import com.business.model.dataaccess.BusinessDao;
import com.business.model.pojo.CategorySyphcode;
import com.business.model.pojo.ExportReportEntity;
import com.business.model.pojo.RoleEntity;
import com.business.service.BusinessService;
import com.business.web.bean.UsersBean;

/**
 * 
 * @author Vasanth
 * 
 *         Service implementation of Business
 * 
 */

@Service
public class BusinessServiceImpl implements BusinessService {
	Logger logger = Logger.getLogger(BusinessServiceImpl.class);
	@Autowired
	private BusinessDao businessDao;

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfo() {
		return businessDao.getListOfBusinessInfo();
	}

	@Transactional
	public void deleteBusinessInfo(List<Integer> listIds) {

		businessDao.deleteBusinessInfo(listIds);
	}

	@Transactional
	public List<LocalBusinessDTO> getBrandsInfo(String brands) {

		return businessDao.getBrandsInfo(brands);
	}

	@Transactional
	public boolean updateBusinessInfo(LocalBusinessDTO businessDTO) {
		List<LocalBusinessDTO> listOfBusinessInfo = businessDao
				.getListOfBusinessInfo();
		// listOfBusinessInfo.remove(businessDTO.getId() - 1);

		boolean isValid = true;
		/*
		 * for (LocalBusinessDTO localBusinessDTO : listOfBusinessInfo) { if
		 * (localBusinessDTO.getId().equals(businessDTO.getId())) {
		 * logger.info("Both ids are matched"); continue; } String name =
		 * localBusinessDTO.getName(); String address1 =
		 * localBusinessDTO.getAddress1(); String phoneNumber =
		 * localBusinessDTO.getPhoneNumber(); if
		 * (name.equals(businessDTO.getName()) &&
		 * address1.equals(businessDTO.getAddress1()) &&
		 * phoneNumber.equals(businessDTO.getPhoneNumber())) { isValid = false;
		 * break; } }
		 */
		if (isValid) {
			return businessDao.updateBusinessInfo(businessDTO);
		} else {
			return false;
		}
	}

	@Transactional
	public boolean addBusinessList(List<LocalBusinessDTO> businessDTO,
			Date date, String uploadJobId) {
		return businessDao.addBusinessList(businessDTO, date, uploadJobId);
	}

	@Transactional
	public List<LocalBusinessDTO> getSpecificBusinessInfo(List<Integer> listIds) {
		return businessDao.getSpecificBusinessInfo(listIds);
	}

	@Transactional
	public Set<LocalBusinessDTO> searchBusinessinfo(String companyName,
			String store, String locationPhone, String brands) {

		return businessDao.searchBusinessinfo(companyName, store,
				locationPhone, brands);
	}

	@Transactional
	public boolean uploadReportDTO(UploadReportDTO reportDTO) {

		return businessDao.uploadReportDTO(reportDTO);
	}

	@Transactional
	public UsersDTO getUserByUserNameAndPWD(String userName, String password) {
		return businessDao.getUserByUserNameAndPWD(userName, password);
	}

	@Transactional
	public Integer getChannelIdByName(String channelName) {

		return businessDao.getChannelIdByName(channelName);
	}

	@Transactional
	public BrandInfoDTO getBrandByBrandNameAndPartner(String brandName,
			String submission) {

		return businessDao.getBrandByBrandNameAndPartner(brandName, submission);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBrands() {
		return businessDao.getListOfBrands();
	}

	@Transactional
	public Set<LocalBusinessDTO> businesSearchInfo(String brands,
			String companyName, String store, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode) {

		return businessDao.businesSearchInfo(brands, companyName, store,
				locationPhone, locationAddress, locationCity, locationState,
				locationZipCode);
	}

	@Transactional
	public List<ExportReportDTO> getListingActivityInf() {
		return businessDao.getListingActivityInf();
	}

	@Transactional
	public LocalBusinessDTO getBusinessInfo(Integer id) {

		return businessDao.getBusinessInfo(id);
	}

	@Transactional
	public boolean saveBrandDetails(String channelName, String brandName,
			Date startDate, String locationsInvoiced, String submisions) {

		return businessDao.saveBrandDetails(channelName, brandName, startDate,
				locationsInvoiced, submisions);
	}

	@Transactional
	public List<BrandInfoDTO> getDistributorInfo(String brand) {

		return businessDao.getDistributorInfo(brand);
	}

	@Transactional
	public boolean exportReportDTO(List<ExportReportDTO> exportReports) {

		return businessDao.exportReportDTO(exportReports);
	}

	@Transactional
	public Integer saveChannel(String channelName, Date startDate) {

		return businessDao.saveChannel(channelName, startDate);
	}

	@Transactional
	public boolean updateChannel(String channelName, Date startDate,
			Integer channelID) {

		return businessDao.updateChannel(channelName, startDate, channelID);
	}

	@Transactional
	public boolean saveBrand(String brandName, Date startDate,
			String locationsInvoiced, String submisions, Integer channelID,
			String partnerActive, Integer clientId, int brandId) {

		return businessDao.saveBrand(brandName, startDate, locationsInvoiced,
				submisions, channelID, partnerActive, clientId, brandId);
	}

	@Transactional
	public boolean updateBrand(Integer brandID, String brandName,
			Date startDate, String locationsInvoiced, String submisions,
			Integer channelID, String partnerActive, Integer clientId,
			Integer id) {

		return businessDao.updateBrand(brandID, brandName, startDate,
				locationsInvoiced, submisions, channelID, partnerActive,
				clientId, id);
	}

	@Transactional
	public String getBrandChannel(String brandName) {
		return businessDao.getBrandChannel(brandName);
	}

	@Transactional
	public List<String> getChannels() {

		return businessDao.getChannels();
	}

	@Transactional
	public boolean isUserExistis(String userName) {
		return businessDao.isUserExistis(userName);
	}

	@Transactional
	public boolean saveUser(UsersBean bean) {
		return businessDao.saveUser(bean);
	}

	@Transactional
	public BrandInfoDTO getBrandInfoByBrandName(String brandName) {
		return businessDao.getBrandInfoByBrandName(brandName);
	}

	@Transactional
	public ChannelNameDTO getchannelInfo(Integer chennalID) {

		return businessDao.getchannelInfo(chennalID);
	}

	@Transactional
	public List<UsersDTO> getAllUsersList(Integer roleId) {

		return businessDao.getAllUsersList(roleId);
	}

	@Transactional
	public List<UsersDTO> getUserByUserName(String userName) {
		return businessDao.getUserByUserName(userName);
	}

	@Transactional
	public List<UsersDTO> userInfo(Integer userID) {

		return businessDao.userInfo(userID);
	}

	@Transactional
	public List<BrandInfoDTO> getClientNames() {

		return businessDao.getClientNames();
	}

	@Transactional
	public List<String> getAllBrandNames() {

		return businessDao.getAllBrandNames();
	}

	@Transactional
	public List<LocalBusinessDTO> getStates() {

		return businessDao.getStates();
	}

	@Transactional
	public List<LocalBusinessDTO> getStore() {

		return businessDao.getStore();
	}

	@Transactional
	public List<BrandInfoDTO> getChannelBasedClients(String channelName) {

		return businessDao.getChannelBasedClients(channelName);
	}

	@Transactional
	public Integer locationUploadedCount(String brandName) {
		return businessDao.locationUploadedCount(brandName);
	}

	@Transactional
	public List<String> getChannels(String channelName) {
		return businessDao.getChannels(channelName);
	}

	@Transactional
	public List<String> getChannelsBasedOnUser(Integer userID) {

		return businessDao.getChannelsBasedOnUser(userID);
	}

	@Transactional
	public List<String> getBrandAllSubmissions(String brandName) {
		return businessDao.getBrandAllSubmissions(brandName);
	}

	@Transactional
	public Map<String, List<BrandInfoDTO>> getAllActiveBrands() {
		return businessDao.getAllActiveBrands();
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBussinessByBrandName(String brandName) {
		return businessDao.getListOfBussinessByBrandName(brandName);
	}

	@Transactional
	public void updateBrandWithActiveDate(BrandInfoDTO brandInfoDTO) {
		businessDao.updateBrandWithActiveDate(brandInfoDTO);
	}

	@Transactional
	public ChannelNameDTO getChannelById(Integer channelID) {
		return businessDao.getChannelById(channelID);
	}

	@Transactional
	public void saveExpostInfo(ExportReportEntity exportReportEntity) {
		businessDao.saveExpostInfo(exportReportEntity);
	}

	@Transactional
	public List<ExportReportDTO> getListingActivityInfByBrand(
			LocalBusinessDTO businessDTO) {
		return businessDao.getListingActivityInfByBrand(businessDTO);
	}

	@Transactional
	public int addLocation(LocalBusinessDTO localBusinessDTO) {
		return businessDao.addLocation(localBusinessDTO);
	}

	@Transactional
	public List<RoleEntity> getRoles() {
		return businessDao.getRoles();
	}

	@Transactional
	public UsersDTO getForgotPassword(String userName) {
		return businessDao.getForgotPassword(userName);
	}

	@Transactional
	public void addOrUpdateForgotPWD(ForgotPasswordDto forgotPasswordDto) {
		ForgotPasswordDto existForgotPassDto = getForgotPwdDtoByEmail(forgotPasswordDto
				.getEmail());
		if (existForgotPassDto != null) {
			forgotPasswordDto.setId(existForgotPassDto.getId());
		}
		businessDao.addOrUpdateForgotPWD(forgotPasswordDto);
	}

	@Transactional
	public ForgotPasswordDto getForgotPwdDtoByEmail(String email) {
		return businessDao.getForgotPwdDtoByEmail(email);
	}

	@Transactional
	public boolean updateUserPassword(UsersBean usersBean) {
		boolean updateUserPassword = businessDao.updateUserPassword(usersBean);
		if (updateUserPassword) {
			updateForgotPass(usersBean.getEmail());
		}
		return updateUserPassword;
	}

	@Transactional
	public void updateForgotPass(String email) {
		ForgotPasswordDto forgotPwdDto = businessDao
				.getForgotPwdDtoByEmail(email);
		forgotPwdDto.setValidOrNot(false);
		businessDao.addOrUpdateForgotPWD(forgotPwdDto);
	}

	@Transactional
	public boolean isValidTocken(String tocken) {
		ForgotPasswordDto forgotPassDto = getForgotPassByToken(tocken);
		if (forgotPassDto.isValidOrNot()) {
			long timeStamp = Long.valueOf(tocken.split("_")[1]);
			boolean validateTime = DateUtil.validateTime(timeStamp,
					DateUtil.getTimeStamp());
			return validateTime;
		}
		return false;
	}

	@Transactional
	public ForgotPasswordDto getForgotPassByToken(String tocken) {
		return businessDao.getForgotPassByToken(tocken);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfo(String uploadJobId) {
		return businessDao.getListOfBusinessInfo(uploadJobId);
	}

	@Transactional
	public boolean isStoreUnique(String store, Integer clientId) {
		return businessDao.isStoreUnique(store, clientId);
	}

	@Transactional
	public List<LblErrorDTO> getListOfErrors() {
		return businessDao.getListOfErrors();
	}

	@Transactional
	public boolean insertErrorBusiness(List<LblErrorDTO> inCorrectData,
			Date date, String uploadJobId) {
		return businessDao
				.insertErrorBusiness(inCorrectData, date, uploadJobId);

	}

	/*@Transactional
	public BrandInfoDTO getClientNameById(Integer clientUserId) {
		return businessDao.getClientNameById(clientUserId);
	}
*/
	@Transactional
	public LblErrorDTO getErrorBusinessInfo(Integer id) {
		return businessDao.getErrorBusinessInfo(id);
	}

	@Transactional
	public boolean saveErrorBusinessInfo(LblErrorDTO businessDTO) {
		return businessDao.saveErrorBusinessInfo(businessDTO);
	}

	@Transactional
	public boolean isValidState(String state) {
		return businessDao.isValidState(state);
	}

	@Transactional
	public boolean isCatagoryExist(String category) {
		return businessDao.isCatagoryExist(category);
	}

	@Transactional
	public boolean validBrand(String brand) {
		return businessDao.validBrand(brand);
	}

	@Transactional
	public List<LblErrorDTO> getListOfErorBusinessInfo() {
		return businessDao.getListOfErorBusinessInfo();
	}

	@Transactional
	public void reomveCorrectErrorData(List<LblErrorDTO> correctDBErrorRecords) {
		businessDao.reomveCorrectErrorData(correctDBErrorRecords);
	}

	@Transactional
	public List<LblErrorDTO> getSpecificErrorBusinessInfo(List<Integer> listIds) {
		return businessDao.getSpecificErrorBusinessInfo(listIds);
	}

	@Transactional
	public String getBrandByClientId(Integer clientId) {
		return businessDao.getBrandByClientId(clientId);
	}

	@Transactional
	public int getMaxBrandId() {
		return businessDao.getMaxBrandId();
	}

	@Transactional
	public boolean isClientExistis(String clientName) {
		return businessDao.isClientExistis(clientName);
	}

	@Transactional
	public List<LblErrorDTO> getListOfErrors(String uploadJobId) {
		return businessDao.getListOfErrors(uploadJobId);
	}

	@Transactional
	public boolean updateListingError(LblErrorDTO businessDTO) {
		return businessDao.updateListingError(businessDTO);
	}

	@Transactional
	public Set<LblErrorDTO> errorBusinesSearchInfo(String brands,
			String companyName, String store, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode) {
		return businessDao.errorBusinesSearchInfo(brands, companyName, store,
				locationPhone, locationAddress, locationCity, locationState,
				locationZipCode);
	}

	@Transactional
	public void deleteErrorBusinessInfo(List<Integer> listIds) {
		businessDao.deleteErrorBusinessInfo(listIds);

	}

	@Transactional
	public void updateBusinessRecords(
			List<LocalBusinessDTO> updateBusinessRecords, Date date,
			String uploadJobId) {
		businessDao.updateBusinessRecords(updateBusinessRecords, date,
				uploadJobId);
	}

	@Transactional
	public void deleteBusinessByActionCode(
			List<LocalBusinessDTO> listofDeletesbyActionCode) {

		businessDao.deleteBusinessByActionCode(listofDeletesbyActionCode);
	}

	@Transactional
	public void deleteErrorBusinessByActioncode(
			List<LblErrorDTO> listofErrorDeletesbyActionCode) {
		businessDao
				.deleteErrorBusinessByActioncode(listofErrorDeletesbyActionCode);
	}

	@Transactional
	public void updateErrorBusinessByActionCode(
			List<LblErrorDTO> listOfErrorUpdaatesByActionCode, Date date,
			String uploadJobId) {
		businessDao.updateErrorBusinessByActionCode(
				listOfErrorUpdaatesByActionCode, date, uploadJobId);
	}

	@Transactional
	public Integer getListingId(LblErrorDTO businessInfoDto) {

		return businessDao.getListingId(businessInfoDto);

	}

/*	@Transactional
	public void updateErrorBusinessInfo(LblErrorDTO businessInfoDto,BusinessServiceImpl) {
		businessDao.updateErrorBusinessInfo(businessInfoDto);
	}*/
	
	@Transactional
	public void updateErrorBusinessInfo(LblErrorDTO businessInfoDto,
			Integer listingId) {
		businessDao.updateErrorBusinessInfo(businessInfoDto, listingId);
	}
	@Transactional
	public String getCategoryNameById(Integer categoryId) {
		return businessDao.getCategoryNameById(categoryId);
	}

	@Transactional
	public String getSyphCodeByClientAndCategoryID(String category,
			Integer clientId) {
		return businessDao.getSyphCodeByClientAndCategoryID(category, clientId);
	}
}
