package com.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.AccuracyGraphDTO;
import com.business.common.dto.BingAnalyticsDTO;
import com.business.common.dto.BingReportDTO;
import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.CategoryDTO;
import com.business.common.dto.ChangeTrackingDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.CitationReportDTO;
import com.business.common.dto.CustomSubmissionsDTO;
import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.ForgotPasswordDto;
import com.business.common.dto.InsightsGraphDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.PartnerDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.common.dto.SchedulerDTO;
import com.business.common.dto.SearchDomainDTO;
import com.business.common.dto.UploadReportDTO;
import com.business.common.dto.UsersDTO;
import com.business.common.util.DateUtil;
import com.business.model.dataaccess.BusinessDao;
import com.business.model.pojo.AccuracyPercentageEntity;
import com.business.model.pojo.AccuracyReportEntity;
import com.business.model.pojo.AuditEntity;
import com.business.model.pojo.ChangeTrackingEntity;
import com.business.model.pojo.CitationGraphEntity;
import com.business.model.pojo.CitationReportEntity;
import com.business.model.pojo.ExportReportEntity;
import com.business.model.pojo.FailedScrapingsEntity;
import com.business.model.pojo.RenewalReportEntity;
import com.business.model.pojo.RoleEntity;
import com.business.model.pojo.StatesListEntity;
import com.business.model.pojo.ValueObject;
import com.business.service.BusinessService;
import com.business.web.bean.CustomSubmissionsBean;
import com.business.web.bean.UploadBusinessBean;
/*import com.business.web.bean.CustomSubmissionsBean;*/
import com.business.web.bean.UsersBean;
import com.google.api.services.mybusiness.v4.model.Location;

/**
 * 
 * @author lbl_dev
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
	public void deleteBusinessInfo(List<Long> listIds) {

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
		 * (localBusinessDTO.getId().equals(businessDTO.getId())) { logger.info(
		 * "Both ids are matched"); continue; } String name =
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
	public List<LocalBusinessDTO> getSpecificBusinessInfo(
			List<Long> listIds, String services) {
		return businessDao.getSpecificBusinessInfo(listIds, services);
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
	public List<ExportReportDTO> getListingActivityInf(
			LocalBusinessDTO businessDTO) {

		return businessDao.getListingActivityInf(businessDTO);
	}

	@Transactional
	public LocalBusinessDTO getBusinessInfo(Long id) {

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
	public Integer saveChannel(String channelName, Date startDate, String imagePath) {

		return businessDao.saveChannel(channelName, startDate, imagePath);
	}

	@Transactional
	public boolean updateChannel(String channelName, Date startDate,
			Integer channelID, String imagePath) {

		return businessDao.updateChannel(channelName, startDate, channelID, imagePath);
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

	/*
	 * @Transactional public BrandInfoDTO getClientNameById(Integer
	 * clientUserId) { return businessDao.getClientNameById(clientUserId); }
	 */
	@Transactional
	public LblErrorDTO getErrorBusinessInfo(Long lblStoreID) {
		return businessDao.getErrorBusinessInfo(lblStoreID);
	}

	@Transactional
	public boolean saveErrorBusinessInfo(LblErrorDTO businessDTO) {
		return businessDao.saveErrorBusinessInfo(businessDTO);
	}

	@Transactional
	public boolean isValidState(String state, String countryCode) {
		return businessDao.isValidState(state, countryCode);
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
	public List<LblErrorDTO> getSpecificErrorBusinessInfo(List<Long> listIds) {
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
	public void deleteErrorBusinessInfo(List<Long> listIds) {
		businessDao.deleteErrorBusinessInfo(listIds);

	}

	@Transactional
	public void updateBusinessRecords(
			List<LocalBusinessDTO> updateBusinessRecords, Date date,
			String uploadJobId, String userName) {
		businessDao.updateBusinessRecords(updateBusinessRecords, date,
				uploadJobId, userName);
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

	/*
	 * @Transactional public void updateErrorBusinessInfo(LblErrorDTO
	 * businessInfoDto,BusinessServiceImpl) {
	 * businessDao.updateErrorBusinessInfo(businessInfoDto); }
	 */

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

	@Transactional
	public List<ChannelNameDTO> getChannel() {
		return businessDao.getChannel();
	}

	@Transactional
	public List<BrandInfoDTO> getBrandsByChannelID(Integer channelID) {
		return businessDao.getBrandsByChannelID(channelID);
	}

	@Transactional
	public void deleteBrand(Integer brandID) {
		businessDao.deleteBrand(brandID);
	}

	@Transactional
	public void deleteChannel(Integer channelID) {
		businessDao.deleteChannel(channelID);
	}

	@Transactional
	public List<SchedulerDTO> getScheduleListing() {
		return businessDao.getScheduleListing();
	}

	@Transactional
	public List<PartnerDTO> getPartners() {
		return businessDao.getPartners();
	}

	@Transactional
	public void deleteBusinessInfotest(List<Integer> listIds) {
		businessDao.deleteBusinessInfotest(listIds);
	}

	@Transactional
	public boolean saveScheduler(SchedulerDTO dto) {
		return businessDao.saveScheduler(dto);
	}

	@Transactional
	public List<String> getStoresbasedonState(String state) {
		return businessDao.getStoresbasedonState(state);
	}

	@Transactional
	public void saveUserUserStore(UsersBean bean, List<String> listofStores) {
		businessDao.saveUserUserStore(bean, listofStores);

	}

	@Transactional
	public void deleteBrands(List<Integer> listIds) {
		businessDao.deleteBrands(listIds);

	}

	@Transactional
	public List<LblErrorDTO> getListOfUserErrors() {
		return businessDao.getListOfUserErrors();
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfUSerBusinessInfo() {
		return businessDao.getListOfUSerBusinessInfo();
	}

	@Transactional
	public boolean saveChangeTrackingInfo(ChangeTrackingEntity entity) {
		return businessDao.saveChangeTrackingInfo(entity);
	}

	@Transactional
	public List<String> getUserStores() {
		return businessDao.getUserStores();
	}

	@Transactional
	public List<BrandInfoDTO> getClientbrnds() {
		return businessDao.getClientbrnds();
	}

	@Transactional
	public Map<String, List<BrandInfoDTO>> getActivePartners(String client) {
		return businessDao.getActivePartners(client);
	}

	@Transactional
	public void deleteUSer(int parseInt) {
		businessDao.deleteUSer(parseInt);
	}

	@Transactional
	public boolean saveBrand(String brandName, Date startDateValue,
			String locationsInvoiced, String submission, Integer channelID,
			String partnerActive, Integer clientId, int brandId, String email, String imagePath) {
		return businessDao.saveBrand(brandName, startDateValue,
				locationsInvoiced, submission, channelID, partnerActive,
				clientId, brandId, email, imagePath);

	}

	@Transactional
	public boolean updateBrand(Integer brandID, String brandName,
			Date startDate, String locationsInvoiced, String submission,
			Integer channelID, String partnerActive, Integer clientId,
			Integer id, String email) {
		return businessDao.updateBrand(brandID, brandName, startDate,
				locationsInvoiced, submission, channelID, partnerActive,
				clientId, id, email);

	}

	@Transactional
	public List<CustomSubmissionsDTO> getDaySchedules() throws Exception {
		return businessDao.getDaySchedules();
	}

	@Transactional
	public void updateSchedule(SchedulerDTO dto) throws Exception {

		businessDao.updateSchedule(dto);
	}

	@Transactional
	public List<LocalBusinessDTO> getCLientListOfBusinessInfo(String client,
			String templateName) {
		return businessDao.getCLientListOfBusinessInfo(client, templateName);
	}

	@Transactional
	public List<CategoryDTO> getCategeoryListing() {
		return businessDao.getCategeoryListing();
	}

	@Transactional
	public List<BrandInfoDTO> getBrandListing() {
		return businessDao.getBrandListing();
	}

	@Transactional
	public BrandInfoDTO getBrandInfo(Integer integer) {
		return businessDao.getBrandInfo(integer);
	}

	@Transactional
	public BrandInfoDTO getBrandInfoByBrandName(String brandName,
			String channelName) {
		return businessDao.getBrandInfoByBrandName(brandName, channelName);
	}

	@Transactional
	public BrandInfoDTO getBrandInfoByBrand(String brandName) {
		return businessDao.getBrandInfoByBrand(brandName);
	}

	@Transactional
	public BrandInfoDTO getBrandInfoByChannel(String channelName) {
		return businessDao.getBrandInfoByChannel(channelName);
	}

	@Transactional
	public List<CategoryDTO> getCategeoryListingByBrand(String clinetname) {
		return businessDao.getCategeoryListingByBrand(clinetname);
	}

	@Transactional
	public Set<LocalBusinessDTO> searchBusinessListinginfo(String store,
			String brands) {
		return businessDao.searchBusinessListinginfo(store, brands);
	}

	@Transactional
	public LocalBusinessDTO getBusinessListinginfoByBrandId(String store,
			String brandName) {
		return businessDao.getBusinessListinginfoByBrandId(store, brandName);
	}

	@Transactional
	public Integer getlocationInvoicedByBrandname(String brandName) {
		return businessDao.getlocationInvoicedByBrandname(brandName);
	}

	@Transactional
	public Integer getlocationsByBrandName(String brandName) {
		return businessDao.getlocationsByBrandName(brandName);
	}

	@Transactional
	public void saveSearchDomainInfo(SearchDomainDTO domainDTO) {

		businessDao.saveSearchDomainInfo(domainDTO);
	}

	@Transactional
	public void saveCheckReportInfo(CheckReportDTO checkReportDTO) {

		businessDao.saveCheckReportInfo(checkReportDTO);

	}

	@Transactional
	public String getFactualCategoryId(Integer clientId) {
		return businessDao.getFactualCategoryId(clientId);
	}

	@Transactional
	public List<LocalBusinessDTO> getAllListings() {
		return businessDao.getAllListings();
	}

	@Transactional
	public List<SearchDomainDTO> getDomainsByStore(String store) {
		return businessDao.getDomainsByStore(store);

	}

	@Transactional
	public void insertAccuracyGraphInfo(AccuracyGraphDTO accuracyGraphDTO) {

		businessDao.insertAccuracyGraphInfo(accuracyGraphDTO);
	}

	@Transactional
	public List<LocalBusinessDTO> getStoresByBrandName(String clinetname) {

		return businessDao.getStoresByBrandName(clinetname);
	}

	@Transactional
	public void updateBrandInfo(Integer id) {

		businessDao.updateBrandInforBasedOnBrand(id);
	}

	@Transactional
	public List<BrandInfoDTO> getInActiveBrands(Integer brandid) {
		return businessDao.getInActiveBrands(brandid);
	}

	@Transactional
	public List<StatesListEntity> getStateList() {

		return businessDao.getStateList();
	}

	@Transactional
	public boolean updateBrand(Integer brandID, String brandName,
			Date startDate, String inactive, String locationsInvoiced,
			String submission, Integer channelID, String partnerActive,
			Integer clientId, Integer id, String email, String imagePath) {
		return businessDao.updateBrand(brandID, brandName, startDate, inactive,
				locationsInvoiced, submission, channelID, partnerActive,
				clientId, id, email, imagePath);

	}

	@Transactional
	public String getGoogleCategory(String category1) {
		return businessDao.getGoogleCategory(category1);
	}

	@Transactional
	public boolean isClientIdExistis(Integer clientId) {
		return businessDao.isClientIdExistis(clientId);

	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByStore(
			String flag, String companyname, String searchType) {
		return businessDao.getListOfBusinessInfoOrederByStore(flag,
				companyname, searchType);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByPhone(
			String flag, Map<String, String> fmap) {

		return businessDao.getListOfBusinessInfoOrederByPhone(flag, fmap);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByZip(String flag,
			Map<String, String> fmap) {

		return businessDao.getListOfBusinessInfoOrederByZip(flag, fmap);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByState(
			String flag, Map<String, String> fmap) {

		return businessDao.getListOfBusinessInfoOrederByState(flag, fmap);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByCity(
			String flag, Map<String, String> fmap) {

		return businessDao.getListOfBusinessInfoOrederByCity(flag, fmap);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByAddress(
			String flag, Map<String, String> fmap) {

		return businessDao.getListOfBusinessInfoOrederByAddress(flag, fmap);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByBusinessName(
			String flag, Map<String, String> fmap) {

		return businessDao
				.getListOfBusinessInfoOrederByBusinessName(flag, fmap);
	}

	@Transactional
	public List<CustomSubmissionsBean> getCustomSubmissions() {

		return businessDao.getCustomSubmissions();
	}

	@Transactional
	public List<String> getStoreBasedOnBrandsandStore(String brandname,
			String store) {

		return businessDao.getStoreBasedOnBrandsandStore(brandname, store);
	}

	@Transactional
	public LocalBusinessDTO getBusinesslistingBystore(String store) {
		return businessDao.getBusinesslistingBystore(store);
	}

	@Transactional
	public void saveCustomSubmissions(CustomSubmissionsDTO dto) {
		businessDao.saveCustomSubmissions(dto);

	}

	@Transactional
	public CustomSubmissionsDTO getCustomSubmissions(int parseInt) {
		return businessDao.getCustomSubmissions(parseInt);
	}

	@Transactional
	public List<LocalBusinessDTO> getAllBusinessListingsSortBytotallocations(
			String flag) {

		return businessDao.getAllBusinessListingsSortBytotallocations(flag);
	}

	@Transactional
	public List<ExportReportDTO> getListingActivityInf(String flag) {
		return businessDao.getListingActivityInf(flag);
	}

	@Transactional
	public List<CustomSubmissionsBean> getSearchBrandandChannelDetails(
			String channelName, String brandName1) {
		return businessDao.getSearchBrandandChannelDetails(channelName,
				brandName1);
	}

	@Transactional
	public List<SearchDomainDTO> getDomainsByStore(String store, String domain) {
		return businessDao.getDomainsByStore(store, domain);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfoByStore(String flag,
			Map<String, String> fmap) {
		return businessDao.getListOfBusinessInfoByStore(flag, fmap);
	}

	@Transactional
	public void deleteBusinessInfoByStoreAndClient(UploadBusinessBean uploadBean) {
		businessDao.deleteBusinessInfoByStoreAndClient(uploadBean);
	}

	@Transactional
	public void updateErrorBusinessInfoByAddressVerfication(LblErrorDTO errorDTO) {

		businessDao.updateErrorBusinessInfoByAddressVerfication(errorDTO);
	}
	
	@Transactional
	public void updateErrorInfoBySmartyStreets(UploadBusinessBean uploadBean) {
		businessDao.updateErrorInfoBySmartyStreets(uploadBean);
		
	}

	@Transactional
	public Map<Integer, List<ChangeTrackingDTO>> getBusinessListing(
			List<String> listofStores, String brand, Date fromDate, Date toDate) {

		return businessDao.getBusinessListing(listofStores, brand, fromDate,
				toDate);
	}

	@Transactional
	public Integer isStoreExist(String store, String directory) {

		return businessDao.isStoreExist(store, directory);
	}

	@Transactional
	public void updateCheckReportInfo(CheckReportDTO highestMatchingStore) {
		businessDao.updateCheckReportInfo(highestMatchingStore);

	}

	@Transactional
	public ChangeTrackingEntity isClientIdAndStoreExists(Integer clientId,
			Long store) {
		return businessDao.isClientIdAndStoreExists(clientId, store);
	}

	@Transactional
	public boolean updateChangeTrackingInfo(ChangeTrackingEntity entity) {
		return businessDao.updateChangeTrackingInfo(entity);
	}

	@Transactional
	public void updateAccuracyInfo(AccuracyDTO dto) {
		businessDao.updateAccuracyInfo(dto);

	}

	@Transactional
	public void saveAccuracyInfo(AccuracyDTO dto) {

		businessDao.saveAccuracyInfo(dto);
	}

	@Transactional
	public Integer isStoreExistInAccuracy(String store, String brandName) {

		return businessDao.isStoreExistInAccuracy(store, brandName);
	}

	@Transactional
	public void saveAccuracyGraphInfo(AccuracyGraphDTO accuracyGraphDTO) {

		businessDao.saveAccuracyGraphInfo(accuracyGraphDTO);
	}

	@Transactional
	public Map<String, Integer> getErrorsCount(String store) {

		return businessDao.getErrorsCount(store);
	}

	@Transactional
	public List<String> getStoreForBrand(String string) {

		return businessDao.getStoreForBrand(string);
	}

	@Transactional
	public void saveIntoFailedScapes(FailedScrapingsEntity scrapingsEntity) {
		businessDao.saveIntoFailedScapes(scrapingsEntity);

	}

	@Transactional
	public List<FailedScrapingsEntity> getAllStoresFromFailedScraping() {

		return businessDao.getAllStoresFromFailedScraping();
	}

	@Transactional
	public boolean isStoreAndDirectoryExist(String store, String directory) {

		return businessDao.isStoreAndDirectoryExist(store, directory);
	}

	@Transactional
	public void deleteFailedScapingEntity(FailedScrapingsEntity scrapingsEntity) {
		businessDao.deleteFailedScapingEntity(scrapingsEntity);

	}

	@Transactional
	public void deleteFailedScapingEntity(String store, String directory) {
		businessDao.deleteFailedScapingEntity(store, directory);

	}

	@Transactional
	public String getStateFromStateList(String state) {

		return businessDao.getStateFromStateList(state);
	}

	@Transactional
	public boolean isAbbreviationExist(String lastWordinDirectoryddress,
			String lastWordinLocationAddress) {
		return businessDao.isAbbreviationExist(lastWordinDirectoryddress,
				lastWordinLocationAddress);
	}

	@Transactional
	public void updateCustomSubmissions(CustomSubmissionsDTO schedulerDTO,
			String brandName) {
		businessDao.updateCustomSubmissions(schedulerDTO, brandName);

	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfoForExport(
			String templateName) {
		return businessDao.getListOfBusinessInfoForExport(templateName);
	}

	@Transactional
	public Integer getlocationInvoicedByClient(Integer clientId) {
		return businessDao.getlocationInvoicedByClient(clientId);
	}

	@Transactional
	public List<BrandInfoDTO> getClientNamesByBrand(String brand) {
		return businessDao.getClientNamesByBrand(brand);
	}

	@Transactional
	public List<BrandInfoDTO> getChannelBasedClientsByBrand(String channelName,
			String brand) {
		return businessDao.getChannelBasedClientsByBrand(channelName, brand);
	}

	@Transactional
	public List<StatesListEntity> getStateListByState(String state) {
		return businessDao.getStateListByState(state);
	}

	@Transactional
	public RenewalReportEntity isStoreExistInRenewal(String store,
			Integer brandId) {
		return businessDao.isStoreExistInRenewal(store, brandId);
	}

	@Transactional
	public void updateRenewalInfo(RenewalReportEntity entity) {
		businessDao.updateRenewalInfo(entity);
	}

	@Transactional
	public void saveRenewalInfo(RenewalReportEntity entity) {
		businessDao.saveRenewalInfo(entity);

	}

	@Transactional
	public String getSyphCodeByStore(String store) {
		return businessDao.getSyphCodeByStore(store);

	}

	@Transactional
	public Date getActiveDateForClient(String brandName, String partner) {

		return businessDao.getActiveDateForClient(brandName, partner);
	}

	@Transactional
	public List<AccuracyDTO> getAccuracyInfoFromAccuracyStage() {
		return businessDao.getAccuracyInfoFromAccuracyStage();
	}

	@Transactional
	public List<CheckReportDTO> getcheckreportInfoFromCheckReportStage() {
		return businessDao.getcheckreportInfoFromCheckReportStage();
	}

	@Transactional
	public Integer getClientIdFromBusiness(String store, String zip) {
		return businessDao.getClientIdFromBusiness(store, zip);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBussinessByBrandId(Integer brandID) {
		return businessDao.getListOfBussinessByBrandId(brandID);
	}

	@Transactional
	public Map<String, Integer> getPathCountMapForStores(String store,
			Integer brandID) {
		return businessDao.getPathCountMapForStores(store, brandID);
	}

	@Transactional
	public List<CitationReportDTO> getListOfCitationInfo(String brandname) {
		return businessDao.getListOfCitationInfo(brandname);
	}

	@Transactional
	public Map<String, Integer> getPathCountMapForStores(
			List<LocalBusinessDTO> listOfBussinessinfo) {
		return businessDao.getPathCountMapForStores(listOfBussinessinfo);
	}

	@Transactional
	public Map<String, List<String>> getPathFromSearch(String store,
			Integer brandID) {
		return businessDao.getPathFromSearch(store, brandID);
	}

	@Transactional
	public Map<String, List<String>> getDomainAuthorities(String store,
			Integer brandID) {

		return businessDao.getDomainAuthorities(store, brandID);
	}

	@Transactional
	public Integer isStoreAndBrandExistInCitation(String store, Integer brandID) {

		return businessDao.isStoreAndBrandExistInCitation(store, brandID);
	}

	@Transactional
	public void updateCitationreportInfo(
			CitationReportEntity citationReportEntity) {

		businessDao.updateCitationreportInfo(citationReportEntity);
	}

	@Transactional
	public void saveCitationreportInfo(CitationReportEntity citationReportEntity) {
		businessDao.saveCitationreportInfo(citationReportEntity);

	}

	@Transactional
	public void saveCitationGraphInfo(CitationGraphEntity graphEntity) {
		businessDao.saveCitationGraphInfo(graphEntity);

	}

	@Transactional
	public Integer getTotalCitationCount(String brandname) {

		return businessDao.getTotalCitationCount(brandname);
	}

	@Transactional
	public CitationReportDTO getCitationInfoInfoByStore(String store,
			String brandname) {
		return businessDao.getCitationInfoInfoByStore(store, brandname);
	}

	@Transactional
	public int getPercentagecategory1(String brandName) {

		return businessDao.getPercentagecategory1(brandName);
	}

	@Transactional
	public int getPercentagecategory2(String brandName) {

		return businessDao.getPercentagecategory2(brandName);
	}

	@Transactional
	public int getPercentagecategory3(String brandName) {

		return businessDao.getPercentagecategory3(brandName);
	}

	@Transactional
	public int getPercentagecategory4(String brandName) {

		return businessDao.getPercentagecategory4(brandName);
	}

	@Transactional
	public double getTotalPercentage(String brandName) {

		return businessDao.getTotalPercentage(brandName);
	}

	@Transactional
	public void saveAccuracyPercentageInfo(
			AccuracyPercentageEntity percentageEntity) {
		businessDao.saveAccuracyPercentageInfo(percentageEntity);

	}

	@Transactional
	public AccuracyPercentageEntity getAcccuracyPercentageInfo(String brandname) {
		return businessDao.getAcccuracyPercentageInfo(brandname);
	}

	@Transactional
	public List<LocalBusinessDTO> getListOfBusinessInfoByClient(String services) {
		return businessDao.getListOfBusinessInfoByClient(services);
	}

	@Transactional
	public Integer getRoleId(String userName) {
		return businessDao.getRoleId(userName);
	}

	@Transactional
	public List<BrandInfoDTO> getChannelBasedClient(String channelName) {
		return businessDao.getChannelBasedClient(channelName);
	}

	@Transactional
	public void addErrorToBusinessList(LocalBusinessDTO localBusinessDTO,
			Date date, String string, List<Long> listIds) {
		businessDao.addErrorToBusinessList(localBusinessDTO, date, string,
				listIds);

	}

	@Transactional
	public List<ValueObject> getData(String string, String string2) {
		return businessDao.getData(string, string2);
	}

	@Transactional
	public List<ValueObject> getComapareListData(String storeId,
			String directory, String brandname) {

		return businessDao.getComapareListData(storeId, directory, brandname);
	}

	@Transactional
	public List<ValueObject> getDS(String storeId, String directory,
			String brandname) {

		return businessDao.getDS(storeId, directory, brandname);
	}

	@Transactional
	public List<AuditEntity> getListOfOverridenListingsByBrand(Date fromDate,
			Date toDate, String brand) {
		return businessDao.getListOfOverridenListingsByBrand(fromDate, toDate,
				brand);
	}

	@Transactional
	public List<AuditEntity> getListOfOverridenListings(Date fromDate,
			Date toDate) {
		return businessDao.getListOfOverridenListings(fromDate, toDate);
	}

	@Transactional
	public List<RenewalReportDTO> runRenewalReport(Date fromDate, Date toDate,
			String storeName, String brand) {
		return businessDao.runRenewalReport(fromDate, toDate, storeName, brand);

	}

	@Transactional
	public List<RenewalReportDTO> runRenewalReportForBrand(Date fromDate,
			Date toDate, String brand) {
		return businessDao.runRenewalReportForBrand(fromDate, toDate, brand);
	}

	@Transactional
	public RenewalReportDTO isRenewed(String store, Integer clientId) {
		return businessDao.isRenewed(store, clientId);
	}

	@Transactional
	public List<RenewalReportDTO> runSummaryReport(Date fromDate, Date toDate,
			String storeName, String brand) {
		return businessDao.runSummaryReport(fromDate, toDate, storeName, brand);
	}

	@Transactional
	public List<RenewalReportDTO> runSummaryReportForBrand(Date fromDate,
			Date toDate, String brand) {
		return businessDao.runSummaryReportForBrand(fromDate, toDate, brand);
	}

	@Transactional
	public LocalBusinessDTO getBusinessListinginfo(String store,
			String brandname) {
		return businessDao.runSummaryReportForBrand(store, brandname);
	}

	@Transactional
	public List<LblErrorDTO> getListOfBusinessErrorInfoByStore(String flag,
			Map<String, String> fmap) {
		return businessDao.getListOfBusinessErrorInfoByStore(flag, fmap);
	}

	@Transactional
	public List<LblErrorDTO> getListOfBusinessErrorInfoByBusinessName(
			String flag, Map<String, String> fmap) {

		return businessDao.getListOfBusinessErrorInfoByBusinessName(flag, fmap);
	}

	@Transactional
	public List<LblErrorDTO> getListOfBusinessErrorInfoByAddress(String flag,
			Map<String, String> fmap) {

		return businessDao.getListOfBusinessErrorInfoByAddress(flag, fmap);
	}

	@Transactional
	public List<LblErrorDTO> getListOfBusinessErrorInfoByCity(String flag,
			Map<String, String> fmap) {

		return businessDao.getListOfBusinessErrorInfoByCity(flag, fmap);
	}

	@Transactional
	public List<LblErrorDTO> getListOfBusinessErrorInfoByState(String flag,
			Map<String, String> fmap) {

		return businessDao.getListOfBusinessErrorInfoByState(flag, fmap);
	}

	@Transactional
	public List<LblErrorDTO> getListOfBusinessErrorInfoByZip(String flag,
			Map<String, String> fmap) {

		return businessDao.getListOfBusinessErrorInfoByZip(flag, fmap);
	}

	@Transactional
	public List<LblErrorDTO> getListOfBusinessErrorInfoByPhone(String flag,
			Map<String, String> fmap) {

		return businessDao.getListOfBusinessErrorInfoByPhone(flag, fmap);
	}

	@Transactional
	public Map<String, Integer> getisDirectoryExist(String store,
			String brandName) {
		return businessDao.getisDirectoryExist(store, brandName);
	}

	@Transactional
	public AccuracyReportEntity getpercentageForStoreCount(String store) {
		return businessDao.getpercentageForStoreCount(store);
	}

	@Transactional
	public Integer getCountForBrand(Integer clients,
			List<UploadBusinessBean> listDataFromXLS) {
		return businessDao.getCountForBrand(clients, listDataFromXLS);
	}

	@Transactional
	public boolean isBusinessExcelRecordUnique(LocalBusinessDTO excelRecord) {

		return businessDao.isBusinessExcelRecordUnique(excelRecord);
	}

	@Transactional
	public boolean updateBusinessWithGoogleMB(String googleAccountId,
			List<Location> locations) {
		return businessDao.updateBusinessWithGoogleMB(googleAccountId,
				locations);
	}

	@Transactional
	public List<LocalBusinessDTO> getLocationsByGoogleAccount(
			String googleAccountId) {

		return businessDao.getLocationsByGoogleAccount(googleAccountId);
	}
	@Transactional
	public LocalBusinessDTO getLocationByGoogleAccount(String googleAccountId,
			String locationId) {

		return businessDao.getLocationByGoogleAccount(googleAccountId,locationId);
	}

	@Transactional
	public Map<String, Long> getInsightsDataForBrandAndStore(Integer brand,
			String store, Date startDate, Date endDate) {

		return businessDao.getInsightsDataForBrandAndStore(brand, store,
				startDate, endDate);
	}

	@Transactional
	public List<String> getAllStates() {
		return businessDao.getAllStates();

	}

	@Transactional
	public List<InsightsGraphDTO> getViewsHistoryByWeek(String state,
			String brand, String store, Date startDate, Date endDate) {

		return businessDao.getViewsHistoryByWeek(state, brand, store,
				startDate, endDate);
	}
	
	@Transactional
	public  Map<String, InsightsGraphDTO> getHistory(Integer brand,
			String state, Date startDate, Date endDate) {
		return businessDao.getHistory(brand,
				state, startDate, endDate);
	}
	@Transactional
	public Map<String, InsightsGraphDTO> getHistoryForStore(Integer brand,
			String store, Date startDate, Date endDate) {
		return businessDao.getHistoryForStore(brand,
				store, startDate, endDate);
	}
	@Transactional
	public List<String> getAllStatesListByBrand(String brand) {
		return businessDao.getAllStatesListByBrand(brand);
	}

	@Transactional
	public void addInsightGraphDetails(LocalBusinessDTO dto, Date formattedEndDate, String googleAccountId,
			String googleLocationId, Long directCount, Long inDirectCount,Long searchCount, Long mapCount,
			Long callsCount, Long directionsCount, Long websiteCount) {
		businessDao.addInsightGraphDetails(dto , formattedEndDate, googleAccountId, googleLocationId,directCount,inDirectCount,
				searchCount, mapCount, callsCount, directionsCount, websiteCount);

	}
	@Transactional
	public List<String> getStoresNames(String brand) {
		return businessDao.getStoresNames(brand);
	}
	@Transactional
	public Map<String, Long> getInsightsData(String brand, String store,
			Date startDate, Date endDate) {
		return businessDao.getInsightsData(brand, store, startDate,
				endDate);
	}
	@Transactional
	public Map<String, Long> getInsightsBrandData(Integer brand, String state,
			Date startDate, Date endDate) {
		return businessDao.getInsightsBrandData(brand, state, startDate,
				endDate);
	}
	@Transactional
	public List<InsightsGraphDTO> getInsightsBrandExcelData(String brand,
			String state, Date startDate, Date endDate) {

		return businessDao.getInsightsBrandExcelData(brand, state, startDate,
				endDate);
	}
	
	@Transactional
	public List<InsightsGraphDTO> getBrandViewsHistoryByWeek(String brand,
			String state, Date startDate, Date endDate) {

		return businessDao.getBrandViewsHistoryByWeek(brand, state, startDate,
				endDate);
	}
	

	@Transactional
	public List<LocalBusinessDTO> getStoresByGMBAccount(String googleAccountId) {

		return businessDao.getStoresByGMBAccount(googleAccountId);
	}
	@Transactional
	public Long getInsightCountsForBrand(Integer brand, String store, Date startDate, Date endDate) {

		return businessDao.getInsightCountsForBrand(brand, store, startDate, endDate);
	}
	@Transactional
	public void updateStoresWithGMBAccountId(String client,
			String googleAccountId) {
		businessDao.updateStoresWithGMBAccountId(client, googleAccountId);
		
	}
	@Transactional
	public void deleteExistingRecords(Integer clientId, Date formattedEndDate) {

		businessDao.deleteExistingRecords(clientId,formattedEndDate);
	}
	
	@Transactional
	public void deleteExistingRecords(Integer clientId, Date formattedDate,
			Date formattedDate2) {
		businessDao.deleteExistingRecords(clientId,formattedDate, formattedDate2);
		
	}
	
	@Transactional
	public Map<String, String> getChannelImageBytes(String brand) {

		return businessDao.getChannelImageBytes(brand);
	}
	@Transactional
	public List<InsightsGraphDTO> getMonthlyReportData(String brand, String type) {

		return businessDao.getMonthlyReportData(brand, type);
	}
	@Transactional
	public List<InsightsGraphDTO> getMonthlyTrends(Integer brand, String state) {

		return businessDao.getMonthlyTrends(brand, state);
	}
	
	@Transactional
	public List<BingReportDTO> getAnlytics(Integer brand, String state, Date startDate,
			Date endDate) {
		return businessDao.getAnlytics(brand,state,startDate,endDate);
		
	}
	
	@Transactional
	public List<BingReportDTO> getAnlyticsForStore(String brand, String store,
			Date startDate, Date endDate) {

		return businessDao.getAnlyticsForStore(brand,store,startDate,endDate);
	}
	
	@Transactional
	public Map<String, List<InsightsGraphDTO>> getTopandBottomSearches(
			Integer brand, String state, Date startDate, Date endDate) {

		return businessDao.getTopandBottomSearches(brand,state,startDate,endDate);
	}
	
	@Transactional
	public Map<String, List<BingAnalyticsDTO>> getTopandBottomAnalytics(
			Integer brand, String state, Date startDate, Date endDate) {

		return businessDao.getTopandBottomAnalytics(brand,state,startDate,endDate);
	}
	
	@Transactional
	public Map<Integer, InsightsGraphDTO> getDailyTrends(String brand,
			String state, Date startDate, Date endDate) {

		return businessDao.getDailyTrends(brand,state,startDate,endDate);
	}
	
	@Transactional
	public void updateInsightMonthlyCountsForStore(LocalBusinessDTO dto,
			String accountId, String googleLocationId) {
		// TODO Auto-generated method stub
		businessDao.updateInsightMonthlyCountsForStore(dto, accountId, googleLocationId);
	}

	
}
