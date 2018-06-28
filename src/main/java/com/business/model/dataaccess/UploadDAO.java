package com.business.model.dataaccess;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.StoresWithErrors;
import com.business.model.pojo.LblErrorEntity;
import com.business.web.bean.UploadBusinessBean;

public interface UploadDAO {

	void deleteBusiness(UploadBusinessBean uploadBean);

	void addBusiness(List<UploadBusinessBean> storesToadd, String trackingId);

	void updateBusiness(List<UploadBusinessBean> storesToUpdate, String trackingId);

	void addErrorBusiness(LblErrorEntity validBusiness, String trackingId);

	public LocalBusinessDTO getLocationByStoreAndclient(String store,Integer clientId);

	void updateBusinessWithChanges(LocalBusinessDTO locationByStoreAndclient,
			String trackingId);

	List<StoresWithErrors> getErrorStoresByTrackingId(String trackingId);

	Map<String, Integer> getAddUpdateMapByTrackingId(String trackingId);

	void deleteBusinessbyUpload(List<UploadBusinessBean> deleteList);

	void addBusinessByUpload(List<LocalBusinessDTO> storesToadd, String uploadJobId, String uploadUserName, Date date);

	void updateBusinessByUpload(List<LocalBusinessDTO> storesToUpdate, String uploadJobId, String uploadUserName, Date date);

	void addErrorsbyUpload(List<LblErrorDTO> errorStoresToadd,String uploadJobId, Date date);

}
