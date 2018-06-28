package com.business.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.RestAuthDTO;
import com.business.common.dto.RestResponseDTO;
import com.business.model.dataaccess.RestDAO;
import com.business.service.RestService;

@Service
public class RestServiceImpl implements RestService {

	Logger logger = Logger.getLogger(RestServiceImpl.class);

	@Autowired
	private RestDAO restDao;

	@Transactional
	public void saveTrackingRequest(RestResponseDTO dto) {
		restDao.saveTrackingRequest(dto);
	}
	@Transactional
	public void updateTrackingStatus(String trackingId) {
		restDao.updateTrackingStatus(trackingId);
	}

	@Transactional
	public RestResponseDTO getStatus(String trackingId) {
		return restDao.getStatus(trackingId);
	}
	
	@Transactional
	public void updateTrackingRequestMessage(String statusMessage, String trackingId) {
		// TODO Auto-generated method stub
		restDao.updateTrackingRequestMessage(statusMessage, trackingId);
	}
	
	@Transactional
	public boolean validateClient(String authId, String authKey) {
		// TODO Auto-generated method stub
		return restDao.validateClient(authId, authKey);
	}
	
	@Transactional
	public void addRestAuthDetails(RestAuthDTO authDTO) {
		// TODO Auto-generated method stub
		restDao.addRestAuthDetails(authDTO);
	}
}
