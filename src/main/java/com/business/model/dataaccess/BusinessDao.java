package com.business.model.dataaccess;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.AccuracyGraphDTO;
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
import com.business.common.dto.InsightsHistory;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.PartnerDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.common.dto.SchedulerDTO;
import com.business.common.dto.SearchDomainDTO;
import com.business.common.dto.UploadReportDTO;
import com.business.common.dto.UsersDTO;
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
import com.business.web.bean.CustomSubmissionsBean;
import com.business.web.bean.UploadBusinessBean;
import com.business.web.bean.UsersBean;
import com.google.api.services.mybusiness.v3.model.Location;

/**
 * 
 * @author Vasanth
 * 
 *         BusinessDao Interface to deal with the database operations
 * 
 * 
 */
public interface BusinessDao {
	

	public UsersDTO getUserByUserNameAndPWD(String userName, String password);

	public List<LocalBusinessDTO> getListOfBusinessInfo();

	public List<LocalBusinessDTO> getListOfBrands();

	public List<ExportReportDTO> getListingActivityInf();

	public LocalBusinessDTO getBusinessInfo(Integer id);

	public void deleteBusinessInfo(List<Integer> listIds);

	public List<LocalBusinessDTO> getBrandsInfo(String brands);

	public boolean updateBusinessInfo(LocalBusinessDTO businessDTO);

	public boolean saveErrorBusinessInfo(LblErrorDTO businessDTO);

	public boolean addBusinessList(List<LocalBusinessDTO> businessDTO,
			Date date, String uploadJobId);

	public List<LocalBusinessDTO> getSpecificBusinessInfo(
			List<Integer> listIds, String services);

	public Set<LocalBusinessDTO> searchBusinessinfo(String companyName,
			String store, String locationPhone, String brands);

	public Set<LocalBusinessDTO> businesSearchInfo(String brands,
			String companyName, String store, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode);

	public boolean uploadReportDTO(UploadReportDTO reportDTO);

	public boolean exportReportDTO(List<ExportReportDTO> exportReports);

	public Integer getChannelIdByName(String channelName);

	public Integer saveChannel(String channelName, Date startDate);

	public boolean updateChannel(String channelName, Date startDate,
			Integer channelID);

	public BrandInfoDTO getBrandByBrandNameAndPartner(String brandName,
			String submission);

	public boolean saveBrand(String brandName, Date startDate,
			String locationsInvoiced, String submisions, Integer channelID,
			String partnerActive, Integer clientId, int brandId);

	public boolean updateBrand(Integer brandID, String brandName,
			Date startDate, String locationsInvoiced, String submisions,
			Integer channelID, String partnerActive, Integer clientId,
			Integer id);

	public boolean saveBrandDetails(String channelName, String brandName,
			Date startDate, String locationsInvoiced, String submisions);

	public List<BrandInfoDTO> getDistributorInfo(String brand);

	public String getBrandChannel(String brandName);

	public List<String> getChannels();

	public List<String> getChannels(String channelName);

	public List<String> getChannelsBasedOnUser(Integer userID);

	public boolean isUserExistis(String userName);

	public boolean saveUser(UsersBean bean);

	public BrandInfoDTO getBrandInfoByBrandName(String brandName);

	public ChannelNameDTO getchannelInfo(Integer chennalID);

	public List<UsersDTO> getAllUsersList(Integer roleId);

	public List<UsersDTO> getUserByUserName(String userName);

	public List<UsersDTO> userInfo(Integer userID);

	public List<BrandInfoDTO> getClientNames();

	public List<BrandInfoDTO> getChannelBasedClients(String channelName);

	public List<String> getAllBrandNames();

	public List<LocalBusinessDTO> getStates();

	public List<LocalBusinessDTO> getStore();

	public Integer locationUploadedCount(String brandName);

	public List<String> getBrandAllSubmissions(String brandName);

	public Map<String, List<BrandInfoDTO>> getAllActiveBrands();

	public List<LocalBusinessDTO> getListOfBussinessByBrandName(String brandName);

	public void updateBrandWithActiveDate(BrandInfoDTO brandInfoDTO);

	public ChannelNameDTO getChannelById(Integer channelID);

	public void saveExpostInfo(ExportReportEntity exportReportEntity);

	public List<ExportReportDTO> getListingActivityInfByBrand(
			LocalBusinessDTO businessDTO);

	public int addLocation(LocalBusinessDTO localBusinessDTO);

	public List<RoleEntity> getRoles();

	public UsersDTO getForgotPassword(String userName);

	public void addOrUpdateForgotPWD(ForgotPasswordDto forgotPasswordDto);

	public ForgotPasswordDto getForgotPwdDtoByEmail(String email);

	public boolean updateUserPassword(UsersBean usersBean);

	// public boolean isValidTocken(String tocken);

	public ForgotPasswordDto getForgotPassByToken(String tocken);

	public List<LocalBusinessDTO> getListOfBusinessInfo(String uploadJobId);

	public boolean isStoreUnique(String store, Integer clientId);

	public List<LblErrorDTO> getListOfErrors();

	public boolean insertErrorBusiness(List<LblErrorDTO> inCorrectData,
			Date date, String uploadJobId);

	// public BrandInfoDTO getClientNameById(Integer clientUserId);

	public LblErrorDTO getErrorBusinessInfo(Integer id);

	public boolean isValidState(String state, String countryCode);

	public boolean isCatagoryExist(String category);

	public boolean validBrand(String brand);

	public List<LblErrorDTO> getListOfErorBusinessInfo();

	public void reomveCorrectErrorData(List<LblErrorDTO> correctDBErrorRecords);

	public List<LblErrorDTO> getSpecificErrorBusinessInfo(List<Integer> listIds);

	public String getBrandByClientId(Integer clientId);

	public int getMaxBrandId();

	public boolean isClientExistis(String clientName);

	public boolean isClientIdExistis(String clientId);

	public List<LblErrorDTO> getListOfErrors(String uploadJobId);

	public boolean updateListingError(LblErrorDTO businessDTO);

	public Set<LblErrorDTO> errorBusinesSearchInfo(String brands,
			String companyName, String store, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode);

	public void deleteErrorBusinessInfo(List<Integer> listIds);

	public void updateBusinessRecords(
			List<LocalBusinessDTO> updateBusinessRecords, Date date,
			String uploadJobId,String userName);

	public void deleteBusinessByActionCode(
			List<LocalBusinessDTO> listofDeletesbyActionCode);

	public void deleteErrorBusinessByActioncode(
			List<LblErrorDTO> listofErrorDeletesbyActionCode);

	public void updateErrorBusinessByActionCode(
			List<LblErrorDTO> listOfErrorUpdaatesByActionCode, Date date,
			String uploadJobId);

	public Integer getListingId(LblErrorDTO businessInfoDto);

	public void updateErrorBusinessInfo(LblErrorDTO businessInfoDto,
			Integer listingId);

	public String getCategoryNameById(Integer categoryId);

	public String getSyphCodeByClientAndCategoryID(String category,
			Integer clientId);

	public List<ChannelNameDTO> getChannel();

	public List<BrandInfoDTO> getBrandsByChannelID(Integer channelID);

	public void deleteBrand(Integer brandID);

	public void deleteChannel(Integer channelID);

	public List<SchedulerDTO> getScheduleListing();

	public List<PartnerDTO> getPartners();

	public void deleteBusinessInfotest(List<Integer> listIds);

	public boolean saveScheduler(SchedulerDTO dto);

	public List<String> getStoresbasedonState(String state);

	public void saveUserUserStore(UsersBean bean, List<String> listofStores);

	public void deleteBrands(List<Integer> listIds);

	public List<LblErrorDTO> getListOfUserErrors();

	public List<LocalBusinessDTO> getListOfUSerBusinessInfo();

	public boolean saveChangeTrackingInfo(ChangeTrackingEntity entity);

	public List<String> getUserStores();

	public List<BrandInfoDTO> getClientbrnds();

	public Map<String, List<BrandInfoDTO>> getActivePartners(String client);

	public void deleteUSer(int parseInt);

	public boolean saveBrand(String brandName, Date startDate,
			String locationsInvoiced, String submisions, Integer channelID,
			String partnerActive, Integer clientId, int brandId, String email);

	public boolean updateBrand(Integer brandID, String brandName,
			Date startDate, String locationsInvoiced, String submission,
			Integer channelID, String partnerActive, Integer clientId,
			Integer id, String email);

	public List<CustomSubmissionsDTO> getDaySchedules() throws Exception;

	public void updateSchedule(SchedulerDTO schedulerDTO) throws Exception;

	public List<LocalBusinessDTO> getCLientListOfBusinessInfo(String client,
			String templateName);

	public List<CategoryDTO> getCategeoryListing();

	public List<BrandInfoDTO> getBrandListing();

	public BrandInfoDTO getBrandInfo(Integer integer);

	public BrandInfoDTO getBrandInfoByBrandName(String brandName,
			String channelName);

	public BrandInfoDTO getBrandInfoByBrand(String brandName);

	public BrandInfoDTO getBrandInfoByChannel(String channelName);

	public List<CategoryDTO> getCategeoryListingByBrand(String clinetname);

	public Set<LocalBusinessDTO> searchBusinessListinginfo(String store,
			String brands);

	public LocalBusinessDTO getBusinessListinginfoByBrandId(String store,String brandName);

	public Integer getlocationInvoicedByBrandname(String brandName);

	public Integer getlocationsByBrandName(String brandName);

	public void saveSearchDomainInfo(SearchDomainDTO domainDTO);

	public void saveCheckReportInfo(CheckReportDTO checkReportDTO);

	public String getFactualCategoryId(Integer clientId);

	public List<LocalBusinessDTO> getAllListings();

	public List<SearchDomainDTO> getDomainsByStore(String store);

	public void insertAccuracyGraphInfo(AccuracyGraphDTO accuracyGraphDTO);

	public List<LocalBusinessDTO> getStoresByBrandName(String clinetname);

	public void updateBrandInforBasedOnBrand(Integer id);

	public List<BrandInfoDTO> getInActiveBrands(Integer brandid);

	public List<StatesListEntity> getStateList();

	public boolean updateBrand(Integer brandID, String brandName,
			Date startDate, String inactive, String locationsInvoiced,
			String submission, Integer channelID, String partnerActive,
			Integer clientId, Integer id, String email);

	public String getGoogleCategory(String category1);

	public boolean isClientIdExistis(Integer clientId);

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByStore(
			String flag, String companyname, String searchType);

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByPhone(
			String flag, Map<String, String> fmap);

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByZip(String flag,
			Map<String, String> fmap);

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByState(
			String flag, Map<String, String> fmap);

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByCity(
			String flag, Map<String, String> fmap);

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByAddress(
			String flag, Map<String, String> fmap);

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByBusinessName(
			String flag, Map<String, String> fmap);

	public List<CustomSubmissionsBean> getCustomSubmissions();

	public List<String> getStoreBasedOnBrandsandStore(String brandname,
			String store);

	public void saveCustomSubmissions(CustomSubmissionsDTO dto);

	public CustomSubmissionsDTO getCustomSubmissions(int parseInt);

	public List<LocalBusinessDTO> getAllBusinessListingsSortBytotallocations(
			String flag);

	public List<ExportReportDTO> getListingActivityInf(String flag);

	public List<CustomSubmissionsBean> getSearchBrandandChannelDetails(
			String channelName, String brandName1);

	public List<SearchDomainDTO> getDomainsByStore(String store, String domain);

	public List<LocalBusinessDTO> getListOfBusinessInfoByStore(String flag,
			Map<String, String> fmap);

	public void deleteBusinessInfoByStoreAndClient(UploadBusinessBean uploadBean);

	public void updateErrorBusinessInfoByAddressVerfication(LblErrorDTO errorDTO);

	public Map<Integer, List<ChangeTrackingDTO>> getBusinessListing(
			List<String> listofStores, String brand, Date fromDate, Date toDate);

	public Integer isStoreExist(String store, String directory);

	public void updateCheckReportInfo(CheckReportDTO highestMatchingStore);

	public ChangeTrackingEntity isClientIdAndStoreExists(Integer clientId,
			String store);

	public boolean updateChangeTrackingInfo(ChangeTrackingEntity entity);

	public void updateAccuracyInfo(AccuracyDTO dto);

	public void saveAccuracyInfo(AccuracyDTO dto);

	public Integer isStoreExistInAccuracy(String store, String brandName);

	public void saveAccuracyGraphInfo(AccuracyGraphDTO accuracyGraphDTO);

	public Map<String, Integer> getErrorsCount(String store);

	public List<String> getStoreForBrand(String string);

	public void saveIntoFailedScapes(FailedScrapingsEntity scrapingsEntity);

	public List<FailedScrapingsEntity> getAllStoresFromFailedScraping();

	public boolean isStoreAndDirectoryExist(String store, String directory);

	public void deleteFailedScapingEntity(FailedScrapingsEntity scrapingsEntity);

	public void deleteFailedScapingEntity(String store, String directory);

	public String getStateFromStateList(String state);

	public boolean isAbbreviationExist(String lastWordinDirectoryddress,
			String lastWordinLocationAddress);

	public LocalBusinessDTO getBusinesslistingBystore(String store);

	public void updateCustomSubmissions(CustomSubmissionsDTO schedulerDTO,
			String brandName);

	public List<LocalBusinessDTO> getListOfBusinessInfoForExport(
			String templateName);

	public Integer getlocationInvoicedByClient(Integer clientId);

	public List<BrandInfoDTO> getClientNamesByBrand(String brand);

	public List<BrandInfoDTO> getChannelBasedClientsByBrand(String channelName,
			String brand);

	public List<StatesListEntity> getStateListByState(String state);

	public void updateRenewalInfo(RenewalReportEntity entity);

	public void saveRenewalInfo(RenewalReportEntity entity);

	public RenewalReportEntity isStoreExistInRenewal(String store,Integer brandId);

	public String getSyphCodeByStore(String store);

	public Date getActiveDateForClient(String brandName,String partner);

	public List<AccuracyDTO> getAccuracyInfoFromAccuracyStage();

	public List<CheckReportDTO> getcheckreportInfoFromCheckReportStage();

	public Integer getClientIdFromBusiness(String store, String zip);

	public List<LocalBusinessDTO> getListOfBussinessByBrandId(Integer brandID);

	public Map<String, Integer> getPathCountMapForStores(String store,
			Integer brandID);

	public List<CitationReportDTO> getListOfCitationInfo(String brandname);

	public Map<String, Integer> getPathCountMapForStores(
			List<LocalBusinessDTO> listOfBussinessinfo);

	public Map<String, List<String>> getPathFromSearch(String store,
			Integer brandID);

	public Map<String, List<String>> getDomainAuthorities(String store,
			Integer brandID);

	public void saveCitationreportInfo(CitationReportEntity citationReportEntity);

	public void updateCitationreportInfo(
			CitationReportEntity citationReportEntity);

	public Integer isStoreAndBrandExistInCitation(String store, Integer brandID);

	public void saveCitationGraphInfo(CitationGraphEntity graphEntity);

	public Integer getTotalCitationCount(String brandname);

	public CitationReportDTO getCitationInfoInfoByStore(String store,
			String brandname);

	public int getPercentagecategory1(String brandName);

	public int getPercentagecategory2(String brandName);

	public int getPercentagecategory3(String brandName);

	public int getPercentagecategory4(String brandName);

	public double getTotalPercentage(String brandName);

	public void saveAccuracyPercentageInfo(
			AccuracyPercentageEntity percentageEntity);

	public AccuracyPercentageEntity getAcccuracyPercentageInfo(String brandname);

	public List<LocalBusinessDTO> getListOfBusinessInfoByClient(String services);

	public Integer getRoleId(String userName);

	public List<BrandInfoDTO> getChannelBasedClient(String channelName);

	public void addErrorToBusinessList(LocalBusinessDTO localBusinessDTO,
			Date date, String string,List<Integer> listIds);

	public List<ValueObject> getData(String string, String string2);

	public List<ValueObject> getDS(String storeId, String directory,
			String brandname);

	public List<ValueObject> getComapareListData(String storeId,
			String directory, String brandname);

	public List<AuditEntity> getListOfOverridenListingsByBrand(Date fromDate,
			Date toDate, String brand);

	public List<AuditEntity> getListOfOverridenListings(Date fromDate,
			Date toDate);

	public List<RenewalReportDTO> runRenewalReport(Date fromDate, Date toDate,
			String storeName, String brand);

	public List<RenewalReportDTO> runRenewalReportForBrand(Date fromDate,
			Date toDate, String brand);

	public RenewalReportDTO isRenewed(String store, Integer clientId);

	public List<RenewalReportDTO> runSummaryReport(Date fromDate, Date toDate,
			String storeName, String brand);

	public List<RenewalReportDTO> runSummaryReportForBrand(Date fromDate,
			Date toDate, String brand);

	public LocalBusinessDTO runSummaryReportForBrand(String store,
			String brandname);

	public List<LblErrorDTO> getListOfBusinessErrorInfoByStore(String flag,
			Map<String, String> fmap);

	public List<LblErrorDTO> getListOfBusinessErrorInfoByBusinessName(
			String flag, Map<String, String> fmap);

	public List<LblErrorDTO> getListOfBusinessErrorInfoByAddress(String flag,
			Map<String, String> fmap);

	public List<LblErrorDTO> getListOfBusinessErrorInfoByCity(String flag,
			Map<String, String> fmap);

	public List<LblErrorDTO> getListOfBusinessErrorInfoByState(String flag,
			Map<String, String> fmap);

	public List<LblErrorDTO> getListOfBusinessErrorInfoByZip(String flag,
			Map<String, String> fmap);

	public List<LblErrorDTO> getListOfBusinessErrorInfoByPhone(String flag,
			Map<String, String> fmap);

	public Map<String, Integer> getisDirectoryExist(String store, String brandName);

	public AccuracyReportEntity getpercentageForStoreCount(String store);


	public Integer getCountForBrand(Integer clients,
			List<UploadBusinessBean> listDataFromXLS);

	public boolean isBusinessExcelRecordUnique(LocalBusinessDTO excelRecord);

	public boolean updateBusinessWithGoogleMB(String googleAccountId, List<Location> locations);

	public List<LocalBusinessDTO> getLocationsByGoogleAccount(String googleAccountId);
	public LocalBusinessDTO getLocationByGoogleAccount(String googleAccountId, String locationId);
	

	public Map<String, Long> getInsightsDataForBrandAndStore(String brand, String store, Date startDate,
			Date endDate);

	public List<String> getAllStates();

	public List<InsightsGraphDTO> getViewsHistoryByWeek(String state,
			String brand, String store, Date startDate, Date endDate);

	public void addInsightGraphDetails(LocalBusinessDTO dto, Date formattedEndDate, String googleAccountId,
			String googleLocationId,Long directCount, Long inDirectCount, Long searchCount, Long mapCount,
			Long callsCount, Long directionsCount, Long websiteCount);

	public List<String> getStoresNames(String brand);

	public Map<String, Long> getInsightsData(String brand, String store,
			Date startDate, Date endDate);

	public Map<String, Long> getInsightsBrandData(String brand, String state,
			Date startDate, Date endDate);

	public List<InsightsGraphDTO> getBrandViewsHistoryByWeek(String brand,
			String state, Date startDate, Date endDate);

	public Map<String, List<InsightsGraphDTO>> getTopandBottomSearches(
			String brand, String state, Date startDate, Date endDate);

	public List<LocalBusinessDTO> getStoresByGMBAccount(String googleAccountId);

	public Integer getInsightCountsForBrand(String brand, String store);

	public void updateStoresWithGMBAccountId(String client,
			String googleAccountId);

	public  Map<String, InsightsGraphDTO> getHistory(String brand,
			String state, Date startDate, Date endDate);

	public Map<String, InsightsGraphDTO> getHistoryForStore(String brand,
			String store, Date startDate, Date endDate);

	public List<String> getAllStatesListByBrand(String brand);

	public List<InsightsGraphDTO> getInsightsBrandExcelData(String brand,
			String state, Date startDate, Date endDate);

	public void deleteExistingRecords(Integer clientId, Date formattedEndDate);

}
