package com.business.service;

import com.business.common.dto.RestAuthDTO;
import com.business.common.dto.RestResponseDTO;

public interface RestService {
	
	void saveTrackingRequest(RestResponseDTO dto);

	RestResponseDTO getStatus(String trackingId);

	void updateTrackingStatus(String trackingId);

	void updateTrackingRequestMessage(String statusMessage, String trackingId);

	boolean validateClient(String authId, String authKey);

	void addRestAuthDetails(RestAuthDTO authDTO);

}
