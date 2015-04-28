package com.business.model.dataaccess;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder.In;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.ForgotPasswordDto;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.UploadReportDTO;
import com.business.common.dto.UsersDTO;
import com.business.model.pojo.AcceptedBrandsEntity;
import com.business.model.pojo.CategoryEntity;
import com.business.model.pojo.CategorySyphcode;
import com.business.model.pojo.ExportReportEntity;
import com.business.model.pojo.RoleEntity;
import com.business.model.pojo.StatesListEntity;
import com.business.web.bean.UsersBean;

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

	public boolean addBusinessList(List<LocalBusinessDTO> businessDTO,Date date, String uploadJobId);

	public List<LocalBusinessDTO> getSpecificBusinessInfo(List<Integer> listIds);

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

	public BrandInfoDTO getBrandByBrandNameAndPartner(String brandName, String submission);

	public boolean saveBrand(String brandName, Date startDate,
			String locationsInvoiced, String submisions, Integer channelID, String partnerActive,Integer clientId, int brandId);

	public boolean updateBrand(Integer brandID, String brandName,
			Date startDate, String locationsInvoiced, String submisions,
			Integer channelID,String partnerActive,Integer clientId, Integer id);

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
	
	public List<ExportReportDTO> getListingActivityInfByBrand(LocalBusinessDTO businessDTO);

	public int addLocation(LocalBusinessDTO localBusinessDTO);
	
	public List<RoleEntity> getRoles();
	
	public UsersDTO getForgotPassword(String userName);

	public void addOrUpdateForgotPWD(ForgotPasswordDto forgotPasswordDto);

	public ForgotPasswordDto getForgotPwdDtoByEmail(String email);
	
	public boolean updateUserPassword(UsersBean usersBean);

	//public boolean isValidTocken(String tocken);

	public ForgotPasswordDto getForgotPassByToken(String tocken);

	public List<LocalBusinessDTO> getListOfBusinessInfo(String uploadJobId);
	
	public boolean isStoreUnique(String store,Integer clientId);
	
	public List<LblErrorDTO> getListOfErrors();
	
	public boolean insertErrorBusiness(List<LblErrorDTO> inCorrectData,Date date, String uploadJobId);
	
	//public BrandInfoDTO getClientNameById(Integer clientUserId);
	
	public LblErrorDTO getErrorBusinessInfo(Integer id);	
	
	public boolean isValidState(String state);

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
			String uploadJobId);
	

	public void deleteBusinessByActionCode(
			List<LocalBusinessDTO> listofDeletesbyActionCode);
	
	public void deleteErrorBusinessByActioncode(
			List<LblErrorDTO> listofErrorDeletesbyActionCode);

	public void updateErrorBusinessByActionCode(
			List<LblErrorDTO> listOfErrorUpdaatesByActionCode ,Date date, String uploadJobId);

	public Integer getListingId(LblErrorDTO businessInfoDto);
	
	public void updateErrorBusinessInfo(LblErrorDTO businessInfoDto,  Integer listingId);

	public String getCategoryNameById(Integer categoryId);
	
	
	public String getSyphCodeByClientAndCategoryID(String category,
			Integer clientId);
}
