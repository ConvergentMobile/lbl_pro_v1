package com.business.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.business.common.dto.LocalBusinessDTO;
import com.business.model.pojo.LblErrorEntity;
import com.business.service.BusinessService;
import com.business.service.ListingService;
import com.business.service.RestService;
import com.business.service.UploadService;
import com.business.web.bean.LblErrorBean;
import com.business.web.bean.UploadBusinessBean;

public class RestUtil {

	// validation for update store

	protected static Logger logger = Logger.getLogger(RestUtil.class);

	BusinessService service;
	private UploadService uploadService;
	private ListingService listingService;
	RestService restService = null;

	List<LocalBusinessDTO> stores = null;
	String trackingId = null;
	String authId = null;
	String authKey = null;

	HashMap<String, String> propertiesToUpdate = new HashMap<String, String>();

	public void processStores(BusinessService service,
			UploadService uploadService, RestService restService,
			List<LocalBusinessDTO> storesList, String trackingId,
			String authId2, String authKey, boolean isFile) throws Exception {
		this.service = service;
		this.uploadService = uploadService;
		this.restService = restService;
		this.stores = storesList;
		this.trackingId = trackingId;
		this.authId = authId2;
		this.authKey = authKey;

		List<UploadBusinessBean> listOfbeans = new ArrayList<UploadBusinessBean>();
		for (LocalBusinessDTO dto : storesList) {
			UploadBusinessBean bean = new UploadBusinessBean();
			BeanUtils.copyProperties(dto, bean);
			listOfbeans.add(bean);
		}

		List<UploadBusinessBean> listOfStoresToAdd = new ArrayList<UploadBusinessBean>();
		List<UploadBusinessBean> listOfStoresToUpdate = new ArrayList<UploadBusinessBean>();
		List<UploadBusinessBean> listOfStoresToDelete = new ArrayList<UploadBusinessBean>();

		List<UploadBusinessBean> totalStores = new ArrayList<UploadBusinessBean>();

		for (LocalBusinessDTO dto : storesList) {
			UploadBusinessBean bean = new UploadBusinessBean();
			if (dto.getActionCode().equalsIgnoreCase("A")) {
				BeanUtils.copyProperties(dto, bean);
				listOfStoresToAdd.add(bean);
			} else if (dto.getActionCode().equalsIgnoreCase("U")) {
				BeanUtils.copyProperties(dto, bean);
				listOfStoresToUpdate.add(bean);

			} else if (dto.getActionCode().equalsIgnoreCase("D")) {
				BeanUtils.copyProperties(dto, bean);
				listOfStoresToDelete.add(bean);

			}
		}

		try {
			if (isFile) {
				totalStores.addAll(listOfStoresToAdd);
				totalStores.addAll(listOfStoresToUpdate);
				totalStores.addAll(listOfStoresToDelete);
				ValidationExecutorService.executeService(service, totalStores);
			} else {
				ValidationExecutorService.executeService(service,
						listOfStoresToAdd);

			}
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		}

		List<UploadBusinessBean> validBusinessList = ValidationExecutorService.validBusinessList;
		List<LblErrorBean> erroredBusinessList = ValidationExecutorService.erroredBusinessList;

		List<UploadBusinessBean> deleteList = ValidationExecutorService.deleteBusinessList;

		int deleteCount = 0;
		for (UploadBusinessBean uploadBean : deleteList) {
			if (uploadBean != null
					&& uploadBean.getActionCode().equalsIgnoreCase("D")) {
				deleteCount++;
				this.uploadService.deleteBusiness(uploadBean);
			}
		}
		logger.info("Total stores marked for delete from rest call are: "
				+ deleteCount);

		List<UploadBusinessBean> storesToadd = new ArrayList<UploadBusinessBean>();
		List<UploadBusinessBean> storesToUpdate = new ArrayList<UploadBusinessBean>();
		for (UploadBusinessBean validBusiness : validBusinessList) {
			/*
			 * boolean isAddressValid = AddressValidationUtill
			 * .validateAddressWithSS(validBusiness);
			 */
			boolean isAddressValid = true;
			if (isAddressValid) {
				if (validBusiness.getActionCode().equalsIgnoreCase("A")) {
					/*
					 * this.uploadService.addBusiness(validBusiness,
					 * this.trackingId);
					 */
					storesToadd.add(validBusiness);
				}
				if (validBusiness.getActionCode().equalsIgnoreCase("U")) {
					/*
					 * this.uploadService.updateBusiness(validBusiness,
					 * this.trackingId);
					 */
					storesToUpdate.add(validBusiness);
				}
			} else {
				// if the store has errors. delete from valid and errors system
				// and add freshly
				LblErrorEntity entity = new LblErrorEntity();
				BeanUtils.copyProperties(validBusiness, entity);
				this.uploadService.addErrorBusiness(entity, this.trackingId);
			}

		}
		// add stores
		this.uploadService.addBusiness(storesToadd, this.trackingId);
		this.uploadService.updateBusiness(storesToUpdate, this.trackingId);
		// update stores

		for (LblErrorBean errorBusiness : erroredBusinessList) {
			LblErrorEntity entity = new LblErrorEntity();
			BeanUtils.copyProperties(errorBusiness, entity);
			this.uploadService.addErrorBusiness(entity, trackingId);
		}

		StringBuffer buffer = new StringBuffer();

		if (!isFile) {
			for (UploadBusinessBean updateStore : listOfStoresToUpdate) {

				Long lblStoreID = listingService.getLBLStoreID(
						updateStore.getClientId(), updateStore.getStore());
				LocalBusinessDTO locationByStoreAndclient = null;
				if (lblStoreID != null)
					locationByStoreAndclient = listingService
							.getBusinessByLBlStoreID(lblStoreID);

				if (locationByStoreAndclient == null) {
					if (buffer.length() == 0)
						buffer.append(updateStore.getStore());
					else {
						buffer.append("," + updateStore.getStore());
					}
				} else {
					analyze(updateStore);

					Set<String> keySet = propertiesToUpdate.keySet();
					for (String key : keySet) {
						String value = propertiesToUpdate.get(key);
						invokeSetter(locationByStoreAndclient, key, value);
					}

					this.uploadService.updateBusinessWithChanges(
							locationByStoreAndclient, this.trackingId);
				}
			}

		}
		// update in table if there are failures to update
		if (buffer.length() > 0) {
			String statusMessage = "Store " + buffer.toString()
					+ " does not found to update";
			this.restService.updateTrackingRequestMessage(statusMessage,
					trackingId);
		}
		// update tracking status
		this.restService.updateTrackingStatus(trackingId);
	}

	public void analyze(final Object obj) {

		ReflectionUtils.doWithFields(obj.getClass(), new FieldCallback() {

			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				String name = field.getName();
				field.setAccessible(true);
				Object value = field.get(obj);
				if (value != null) {
					propertiesToUpdate.put(name, value.toString());
				}

			}
		});

	}

	private void invokeSetter(Object obj, String variableName,
			Object variableValue) {
		try {
			PropertyDescriptor objPropertyDescriptor = new PropertyDescriptor(
					variableName, obj.getClass());

			Class type = PropertyUtils.getPropertyType(obj, variableName);

			if (type.getName().equalsIgnoreCase("java.lang.Integer")) {
				objPropertyDescriptor.getWriteMethod().invoke(obj,
						Integer.parseInt(variableValue.toString()));
			} else {
				objPropertyDescriptor.getWriteMethod().invoke(obj,
						variableValue);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
