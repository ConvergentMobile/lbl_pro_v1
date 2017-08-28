package com.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.StoresWithErrors;
import com.business.model.dataaccess.UploadDAO;
import com.business.model.pojo.LblErrorEntity;
import com.business.service.UploadService;
import com.business.web.bean.UploadBusinessBean;

@Service
public class UploadServiceImpl implements UploadService {

	@Autowired
	private UploadDAO uploadDao;

	@Transactional
	public void deleteBusiness(UploadBusinessBean uploadBean) {
		uploadDao.deleteBusiness(uploadBean);
	}

	@Transactional
	public void addBusiness(List<UploadBusinessBean> storesToadd, String trackingId) {
		// TODO Auto-generated method stub
		uploadDao.addBusiness(storesToadd, trackingId);
	}

	@Transactional
	public void updateBusiness(List<UploadBusinessBean> storesToUpdate,
			String trackingId) {
		uploadDao.updateBusiness(storesToUpdate, trackingId);

	}
	
	@Transactional
	public void addErrorBusiness(LblErrorEntity validBusiness,
			String trackingId) {
		uploadDao.addErrorBusiness(validBusiness, trackingId);
		
	}

	@Transactional
	public LocalBusinessDTO getLocationByStoreAndclient(String store,Integer clientId) {
		return uploadDao.getLocationByStoreAndclient(store, clientId);
	}
	
	@Transactional
	public void updateBusinessWithChanges(
			LocalBusinessDTO locationByStoreAndclient, String trackingId) {
		// TODO Auto-generated method stub
		uploadDao.updateBusinessWithChanges(locationByStoreAndclient, trackingId);
	}
	
	@Transactional
	public List<StoresWithErrors> getErrorStoresByTrackingId(String trackingId) {
		// TODO Auto-generated method stub
		return uploadDao.getErrorStoresByTrackingId(trackingId);
	}
	
	@Transactional
	public Map<String, Integer> getAddUpdateMapByTrackingId(String trackingId) {
		// TODO Auto-generated method stub
		return uploadDao.getAddUpdateMapByTrackingId(trackingId);
	}
	
}
