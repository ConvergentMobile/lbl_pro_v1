package com.business.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.RestResponseDTO;
import com.business.common.dto.RestStatus;
import com.business.common.dto.RestStoreObject;
import com.business.common.dto.StoresWithErrors;
import com.business.common.dto.UploadHelper;
import com.business.common.util.RestUtilThread;
import com.business.service.BusinessService;
import com.business.service.RestService;
import com.business.service.UploadService;
import com.business.web.bean.UploadBusinessBean;

@RestController
public class LBLRestController {

	static final UUID uid = UUID
			.fromString("7c48e923-b1e6-49b4-9f17-e0e2215c5b83");

	@Autowired
	private RestService restService;

	@Autowired
	private BusinessService businessService;
	@Autowired
	private UploadService uploadService;

	@Autowired
	ServletContext context;

	String getRandomString() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	@RequestMapping(value = "/storeapi", method = RequestMethod.POST, headers = "Accept=application/json")
	public RestResponseDTO updateAddOrDeleteStores(
			@RequestBody RestStoreObject stores) {

		RestResponseDTO dto = new RestResponseDTO();
		try {
			String authKey = stores.getAuthKey();
			String authId = stores.getAuthId();

			boolean isValidClient = restService.validateClient(authId, authKey);

			/*
			 * List<BrandInfoDTO> brandListing =
			 * businessService.getBrandListing(); Map<Integer, Integer> brandMap
			 * = new HashMap<Integer, Integer>(); for (BrandInfoDTO brandInfoDTO
			 * : brandListing) { brandMap.put(brandInfoDTO.getClientId(),
			 * brandInfoDTO.getChannelID()); } Set<Integer> map =
			 * brandMap.keySet(); for (Integer brandID : map) { Integer
			 * channelId = brandMap.get(brandID); RestAuthDTO authDTO = new
			 * RestAuthDTO(); authDTO.setBrandId(brandID);
			 * authDTO.setChannelId(channelId);
			 * authDTO.setAuthId(RandomStringUtils.randomNumeric(7));
			 * authDTO.setAuthKey(RandomStringUtils.randomAlphabetic(10));
			 * restService.addRestAuthDetails(authDTO); }
			 */

			ArrayList<LocalBusinessDTO> storesList = stores.getStores();

			List<LocalBusinessDTO> validStores = new ArrayList<LocalBusinessDTO>();
			validStores.addAll(storesList);
			Integer deleteCount = 0;
			Integer updateCount = 0;
			Integer addCount = 0;

			for (int i = 0; i < storesList.size(); i++) {
				LocalBusinessDTO localBusinessDTO = storesList.get(i);
				String actionCode = localBusinessDTO.getActionCode();
				if (actionCode.equalsIgnoreCase("A")) {
					addCount++;
				} else if (actionCode.equalsIgnoreCase("U")) {
					updateCount++;
				} else if (actionCode.equalsIgnoreCase("D")) {
					deleteCount++;
				}
			}
			dto.setTotalStores(storesList.size());
			dto.setTotalStoresToAdd(addCount);
			dto.setTotalStoresToUpdate(updateCount);
			dto.setTotalStoresToDelete(deleteCount);
			String trackingId = getRandomString();
			dto.setTrackingId(trackingId);
			dto.setStatus("IN_PROGRESS");
			if (!isValidClient) {
				dto.setResponseMessage("Invalid authId/authkey, please provide valid authentication details");
				dto.setStatus("INVALID");
			}
			Date requestTime = new Date();

			SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String formattedDate = sfd.format(requestTime);
			// System.out.println(formattedDate);

			dto.setRequestTime(formattedDate);

			restService.saveTrackingRequest(dto);

			if (isValidClient) {
				RestUtilThread thread = new RestUtilThread(businessService,
						restService, uploadService, validStores, trackingId,
						authId, authKey, false);
				thread.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
			dto.setResponseMessage("Invalid Request provided");
		}

		return dto;
	}

	@RequestMapping(value = "/status/{trackingId}", method = RequestMethod.GET)
	public RestStatus getStatus(@PathVariable("trackingId") String trackingId) {
		RestStatus status = new RestStatus();
		RestResponseDTO response = restService.getStatus(trackingId);

		status.setRequestTime(response.getRequestTime());
		Integer totalStores = response.getTotalStores();
		if (totalStores == null) {
			totalStores = 0;
		}
		status.setTotalStores(totalStores);

		status.setTrackingId(trackingId);
		status.setStatus(response.getStatus());
		String responseMessage = response.getResponseMessage();
		if (responseMessage == null) {
			responseMessage = "";
		}

		if (response.getTrackingId() == null) {
			responseMessage = "Request with tracking id: " + trackingId
					+ " is not found in the LBL system";
		}

		Map<String, Integer> addUpdateMap = uploadService
				.getAddUpdateMapByTrackingId(trackingId);
		status.setStoresAdded(addUpdateMap.get("add"));
		status.setStoresUpdated(addUpdateMap.get("update"));
		Integer totalStoresToDelete = response.getTotalStoresToDelete();
		if (totalStoresToDelete == null) {
			totalStoresToDelete = 0;
		}
		status.setStoresDeleted(totalStoresToDelete);

		List<StoresWithErrors> errorStores = uploadService
				.getErrorStoresByTrackingId(trackingId);
		if (errorStores.size() > 0) {
			responseMessage = "One or more stores failed to add/update";
			status.setStoresWithErrors(errorStores);
		} else {

		}
		status.setResponseMessage(responseMessage);
		return status;
	}

	@RequestMapping(value = "/store", method = RequestMethod.GET)
	public UploadBusinessBean getStore() {
		UploadBusinessBean response = new UploadBusinessBean();
		return response;
	}

	@RequestMapping(value = "/uploadfile", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
	public RestResponseDTO upload(@RequestPart("file") MultipartFile inputFile) {

		// System.out.println(auth);
		HttpHeaders headers = new HttpHeaders();

		/*
		 * String authKey = auth.getAuthKey(); String authId = auth.getAuthID();
		 */
		String authKey = "";
		String authId = null;
		List<UploadBusinessBean> listDataFromFile = null;
		RestResponseDTO dto = new RestResponseDTO();
		if (!inputFile.isEmpty()) {
			try {
				String originalFilename = inputFile.getOriginalFilename();
				// System.out.println("originalFilename: " + originalFilename);

				UploadHelper helper = new UploadHelper();

				if (originalFilename.contains(".csv")) {
					listDataFromFile = helper.getListFromCSV(inputFile);

				} else {
					listDataFromFile = helper.getListDataFromXLS(inputFile);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		List<LocalBusinessDTO> validStores = new ArrayList<LocalBusinessDTO>();

		for (UploadBusinessBean bean : listDataFromFile) {
			LocalBusinessDTO localBusinessDto = new LocalBusinessDTO();
			BeanUtils.copyProperties(bean, localBusinessDto);
			validStores.add(localBusinessDto);
		}
		Integer deleteCount = 0;
		Integer updateCount = 0;
		Integer addCount = 0;
		for (int i = 0; i < validStores.size(); i++) {
			LocalBusinessDTO localBusinessDTO = validStores.get(i);
			String actionCode = localBusinessDTO.getActionCode();
			if (actionCode.equalsIgnoreCase("A")) {
				addCount++;
			} else if (actionCode.equalsIgnoreCase("U")) {
				updateCount++;
			} else if (actionCode.equalsIgnoreCase("D")) {
				deleteCount++;
			}
		}

		dto.setTotalStores(listDataFromFile.size());
		dto.setTotalStoresToAdd(addCount);
		dto.setTotalStoresToUpdate(updateCount);
		dto.setTotalStoresToDelete(deleteCount);
		String trackingId = getRandomString();
		dto.setTrackingId(trackingId);
		dto.setStatus("IN_PROGRESS");
		Date requestTime = new Date();

		SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String formattedDate = sfd.format(requestTime);
		// System.out.println(formattedDate);

		dto.setRequestTime(formattedDate);

		
		if (listDataFromFile.size() == 0) {
			dto.setResponseMessage("Excel data format seems to be incorrect, please check the format and submit again");
			dto.setStatus("INVALID DATA FORMAT");
		} else {
			RestUtilThread thread = new RestUtilThread(businessService,
					restService, uploadService, validStores, trackingId,
					authId, authKey, true);
			thread.start();
		}
		
		restService.saveTrackingRequest(dto);

		return dto;
	}
}
