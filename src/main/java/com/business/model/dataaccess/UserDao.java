package com.business.model.dataaccess;

import java.util.List;
import java.util.Set;

import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.UsersDTO;
import com.business.model.pojo.ChangeTrackingEntity;
import com.business.web.bean.UsersBean;

public interface UserDao {
	public void saveUserUserStore(UsersBean bean, List<String> listofStores);

	public boolean isUserExistis(String userName);

	public boolean saveUser(UsersBean bean);

	public List<LocalBusinessDTO> getListOfUSerBusinessInfo();

	public List<LblErrorDTO> getListOfUserErrors();

	public List<LocalBusinessDTO> getSpecificBusinessInfo(List<Integer> listIds);

	public String getBrandChannel(String brandName);

	public boolean exportReportDTO(List<ExportReportDTO> exportReports);

	public void deleteBusinessInfo(List<Integer> listIds);

	public LocalBusinessDTO getBusinessInfo(Integer integer);

	public String getBrandByClientId(Integer clientId);

	public boolean updateBusinessInfo(LocalBusinessDTO businessInfoDto);

	public boolean saveChangeTrackingInfo(ChangeTrackingEntity entity);

	public List<LblErrorDTO> getSpecificErrorBusinessInfo(List<Integer> listIds);

	public void deleteErrorBusinessInfo(List<Integer> listIds);

	public LblErrorDTO getErrorBusinessInfo(Integer integer);

	public Integer getListingId(LblErrorDTO businessInfoDto);

	public void updateErrorBusinessInfo(LblErrorDTO businessInfoDto,
			Integer listingId);

	public boolean saveErrorBusinessInfo(LblErrorDTO businessInfoDto);

	public Set<LocalBusinessDTO> businesSearchInfo(String brands,
			String companyName, String store, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode);

	public Set<LblErrorDTO> errorBusinesSearchInfo(String brands,
			String companyName, String store, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode);

	public List<String> getUserStores();

	public List<UsersDTO> userInfo(Integer userId);

	public List<ExportReportDTO> getListingActivityInf();
	public List<ExportReportDTO> getListingActivityInfByBrand(
			LocalBusinessDTO businessDTO);

	public List<LocalBusinessDTO> getListOfBusinessInfo();

	public List<LblErrorDTO> getListOfErrors();
	public void deleteUSer(int parseInt);

	public List<String> getChannelsBasedOnUser(int userID);
}
