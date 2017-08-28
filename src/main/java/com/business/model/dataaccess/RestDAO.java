package com.business.model.dataaccess;

import com.business.common.dto.RestAuthDTO;
import com.business.common.dto.RestResponseDTO;

public interface RestDAO {

	public void saveTrackingRequest(RestResponseDTO dto);

	public RestResponseDTO getStatus(String trackingId);

	public void updateTrackingStatus(String trackingId);

	public void updateTrackingRequestMessage(String statusMessage, String trackingId);

	public boolean validateClient(String authId, String authKey);

	public void addRestAuthDetails(RestAuthDTO authDTO);
	
}
