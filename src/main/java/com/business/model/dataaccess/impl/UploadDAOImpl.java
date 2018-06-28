package com.business.model.dataaccess.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.business.common.dto.LBLStoreDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.StoresWithErrors;
import com.business.common.util.LBLUtil;
import com.business.model.dataaccess.UploadDAO;
import com.business.model.pojo.LBLStoreEntity;
import com.business.model.pojo.LblErrorEntity;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.web.bean.LocalBusinessBean;
import com.business.web.bean.UploadBusinessBean;

@Repository
public class UploadDAOImpl implements UploadDAO {
	Logger logger = Logger.getLogger(UploadDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	public void deleteBusiness(UploadBusinessBean uploadBean) {

		Long lblStoreId = getLBLStoreID(uploadBean.getClientId(),
				uploadBean.getStore());
		if (lblStoreId != null) {
			// delete if it present in listings and errors
			deleteBusiness(lblStoreId);
			deleteErrorBusiness(lblStoreId);
		}

	}

	/**
	 * for APi
	 */
	public void addBusiness(List<UploadBusinessBean> storesToaddIn,
			String trackingId) {
		Session session = sessionFactory.getCurrentSession();

		List<LocalBusinessDTO> storesToadd = new ArrayList<LocalBusinessDTO>();

		for (UploadBusinessBean validBusiness : storesToaddIn) {
			LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();
			BeanUtils.copyProperties(validBusiness, localBusinessDTO);
			storesToadd.add(localBusinessDTO);
		}

		for (LocalBusinessDTO localBusinessDTO : storesToadd) {

			Long lblStoreId = getLBLStoreID(localBusinessDTO.getClientId(),
					localBusinessDTO.getStore());
			if (lblStoreId != null) {
				// delete if it present in listings and errors
				deleteBusiness(lblStoreId);
				deleteErrorBusiness(lblStoreId);

			} else {
				LBLStoreDTO dto = new LBLStoreDTO();
				long nextLBLStoreId = LBLUtil.getNextLBLStoreId();
				dto.setLblStoreId(nextLBLStoreId);
				dto.setClientId(localBusinessDTO.getClientId());
				dto.setStore(localBusinessDTO.getStore());
				addStoreInLBLStoreMap(dto);
			}

			lblStoreId = getLBLStoreID(localBusinessDTO.getClientId(),
					localBusinessDTO.getStore());

			LocalBusinessEntity entity = new LocalBusinessEntity();
			localBusinessDTO.setLblStoreId(lblStoreId);
			BeanUtils.copyProperties(localBusinessDTO, entity);
			entity.setTrackingId(trackingId);
			session.save(entity);

		}

	}

	public void updateBusiness(List<UploadBusinessBean> storesToUpdateIn,
			String trackingId) {

		List<LocalBusinessDTO> storesToUpdate = new ArrayList<LocalBusinessDTO>();

		for (UploadBusinessBean validBusiness : storesToUpdateIn) {
			LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();
			BeanUtils.copyProperties(validBusiness, localBusinessDTO);
			storesToUpdate.add(localBusinessDTO);
		}

		// implement update
		Session session = sessionFactory.getCurrentSession();

		for (LocalBusinessDTO localBusinessDTO : storesToUpdate) {

			Long lblStoreId = getLBLStoreID(localBusinessDTO.getClientId(),
					localBusinessDTO.getStore());

			String store = localBusinessDTO.getStore();

			// if there is name is store, update mapping table
			String newStore = localBusinessDTO.getNewStore();
			if (newStore != null && newStore.length() > 0) {
				if (!newStore.equalsIgnoreCase(store)) {
					updateStoreInLBLMap(lblStoreId, newStore);
					localBusinessDTO.setStore(newStore);
				}

			}

			lblStoreId = getLBLStoreID(localBusinessDTO.getClientId(),
					localBusinessDTO.getStore());

			if (lblStoreId != null) {
				// delete if it present in listings and errors
				deleteBusiness(lblStoreId);
				deleteErrorBusiness(lblStoreId);

			} else {
				LBLStoreDTO dto = new LBLStoreDTO();
				long nextLBLStoreId = LBLUtil.getNextLBLStoreId();
				dto.setLblStoreId(nextLBLStoreId);
				dto.setClientId(localBusinessDTO.getClientId());
				dto.setStore(localBusinessDTO.getStore());
				addStoreInLBLStoreMap(dto);
			}

			lblStoreId = getLBLStoreID(localBusinessDTO.getClientId(),
					localBusinessDTO.getStore());

			LocalBusinessEntity entity = new LocalBusinessEntity();
			localBusinessDTO.setLblStoreId(lblStoreId);
			BeanUtils.copyProperties(localBusinessDTO, entity);
			entity.setTrackingId(trackingId);
			session.save(entity);

		}

	}

	// added ErrorBusiness for API
	public void addErrorBusiness(LblErrorEntity validBusiness, String trackingId) {

		Session session = sessionFactory.getCurrentSession();
		// add in errors
		Long lblStoreId = getLBLStoreID(validBusiness.getClientId(),
				validBusiness.getStore());
		if (lblStoreId != null) {

			// delete if it present in listings and errors
			deleteBusiness(lblStoreId);
			deleteErrorBusiness(lblStoreId);
		} else {
			LBLStoreDTO dto = new LBLStoreDTO();
			long nextLBLStoreId = LBLUtil.getNextLBLStoreId();
			dto.setLblStoreId(nextLBLStoreId);
			dto.setClientId(validBusiness.getClientId());
			dto.setStore(validBusiness.getStore());
			addStoreInLBLStoreMap(dto);
		}
		// add in errors
		lblStoreId = getLBLStoreID(validBusiness.getClientId(),
				validBusiness.getStore());

		LblErrorEntity entity = new LblErrorEntity();
		validBusiness.setLblStoreId(lblStoreId);
		BeanUtils.copyProperties(validBusiness, entity);
		entity.setTrackingId(trackingId);
		session.save(entity);

	}

	public void deleteErrorBusiness(LblErrorEntity uploadBean) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteriaBusiness = session
				.createCriteria(LocalBusinessEntity.class);

		criteriaBusiness.add(Restrictions.eq("clientId",
				uploadBean.getClientId()));
		criteriaBusiness.add(Restrictions.eq("store", uploadBean.getStore()));

		List<LocalBusinessEntity> listofBusiness = criteriaBusiness.list();
		for (LocalBusinessEntity bean : listofBusiness) {
			if (bean != null) {
				logger.info("Listing Identified to delete: "
						+ uploadBean.getStore());
				session.delete(bean);
			}
		}
	}

	public void deleteValidBusiness(UploadBusinessBean uploadBean) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteriaBusiness = session
				.createCriteria(LocalBusinessEntity.class);

		criteriaBusiness.add(Restrictions.eq("clientId",
				uploadBean.getClientId()));
		criteriaBusiness.add(Restrictions.eq("store", uploadBean.getStore()));

		List<LocalBusinessEntity> listofBusiness = criteriaBusiness.list();
		for (LocalBusinessEntity bean : listofBusiness) {
			if (bean != null) {
				logger.info("Listing Identified to delete: "
						+ uploadBean.getStore());
				session.delete(bean);
			}
		}
	}

	public LocalBusinessDTO getLocationByStoreAndclient(String store,
			Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("clientId", clientId));

		LocalBusinessEntity entity = (LocalBusinessEntity) criteria
				.uniqueResult();
		if (entity != null && entity.getStore() != null) {
			LocalBusinessDTO dto = new LocalBusinessDTO();

			BeanUtils.copyProperties(entity, dto);
			return dto;
		} else {
			return null;
		}
	}

	public void updateBusinessWithChanges(
			LocalBusinessDTO locationByStoreAndclient, String trackingId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		// add the the store
		LocalBusinessEntity updateEntity = new LocalBusinessEntity();
		BeanUtils.copyProperties(locationByStoreAndclient, updateEntity);
		updateEntity.setTrackingId(trackingId);
		session.saveOrUpdate(updateEntity);

	}

	public List<StoresWithErrors> getErrorStoresByTrackingId(String trackingId) {
		List<StoresWithErrors> errors = new ArrayList<StoresWithErrors>();
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(LblErrorEntity.class);

		criteria.add(Restrictions.eq("trackingId", trackingId));

		List<LblErrorEntity> list = criteria.list();
		for (LblErrorEntity bean : list) {
			StoresWithErrors error = new StoresWithErrors();

			error.setClientID(bean.getClientId().toString());
			error.setStore(bean.getStore());
			error.setActionCode(bean.getActionCode());
			error.setErrorMesssage(bean.getErrorMessage());
			errors.add(error);
		}

		return errors;
	}

	public Map<String, Integer> getAddUpdateMapByTrackingId(String trackingId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Session session = sessionFactory.getCurrentSession();
		String nameQuery = "SELECT COUNT(actionCode) FROM LocalBusinessEntity WHERE trackingId=? and actionCode='A'";
		Query createQuery = session.createQuery(nameQuery);
		createQuery.setString(0, trackingId);
		int addCount = 0;
		List<Long> list = createQuery.list();
		for (Long long1 : list) {
			addCount = long1.intValue();
		}

		String updateQ = "SELECT COUNT(actionCode) FROM LocalBusinessEntity WHERE trackingId=? and actionCode='U'";
		Query updateQuery = session.createQuery(updateQ);
		updateQuery.setString(0, trackingId);
		int updateCount = 0;
		List<Long> updateLiist = updateQuery.list();
		for (Long long2 : updateLiist) {
			updateCount = long2.intValue();
		}

		map.put("add", addCount);
		map.put("update", updateCount);

		return map;
	}

	// /////////////////////////////////////////////////////////////////////

	// changes after introducing store rename options

	// /////////////////////////////////////////////////////////////////////////

	public void deleteBusinessbyUpload(List<UploadBusinessBean> deleteList) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		for (UploadBusinessBean uploadBusinessBean : deleteList) {
			Long lblStoreID = getLBLStoreID(uploadBusinessBean.getClientId(),
					uploadBusinessBean.getStore());

			// delete store from business, errors and from map

			LocalBusinessEntity bean = (LocalBusinessEntity) session
					.createCriteria(LocalBusinessEntity.class)
					.add(Restrictions.eq("lblStoreId", lblStoreID))
					.uniqueResult();
			if (bean != null)
				session.delete(bean);

			LblErrorEntity errorBean = (LblErrorEntity) session
					.createCriteria(LblErrorEntity.class)
					.add(Restrictions.eq("lblStoreId", lblStoreID))
					.uniqueResult();
			if (errorBean != null)
				session.delete(errorBean);

			// delete from LBL map
			LBLStoreEntity mapBean = (LBLStoreEntity) session
					.createCriteria(LBLStoreEntity.class)
					.add(Restrictions.eq("lblStoreId", lblStoreID))
					.uniqueResult();
			if (mapBean != null)
				session.delete(mapBean);

		}
	}

	public void addBusinessByUpload(List<LocalBusinessDTO> storesToadd,
			String uploadJobId, String uploadUserName, Date date) {
		Session session = sessionFactory.getCurrentSession();

		for (LocalBusinessDTO localBusinessDTO : storesToadd) {

			Long lblStoreId = getLBLStoreID(localBusinessDTO.getClientId(),
					localBusinessDTO.getStore());
			if (lblStoreId != null) {
				// delete if it present in listings and errors
				deleteBusiness(lblStoreId);
				deleteErrorBusiness(lblStoreId);

			} else {
				LBLStoreDTO dto = new LBLStoreDTO();
				long nextLBLStoreId = LBLUtil.getNextLBLStoreId();
				dto.setLblStoreId(nextLBLStoreId);
				dto.setClientId(localBusinessDTO.getClientId());
				dto.setStore(localBusinessDTO.getStore());
				addStoreInLBLStoreMap(dto);
			}

			lblStoreId = getLBLStoreID(localBusinessDTO.getClientId(),
					localBusinessDTO.getStore());

			LocalBusinessEntity entity = new LocalBusinessEntity();
			localBusinessDTO.setLblStoreId(lblStoreId);
			BeanUtils.copyProperties(localBusinessDTO, entity);
			entity.setUploadedTime(date);
			entity.setUploadJobId(uploadJobId);
			session.save(entity);

		}

	}

	public void addErrorsbyUpload(List<LblErrorDTO> errorStoresToadd,
			String uploadJobId, Date date) {
		Session session = sessionFactory.getCurrentSession();

		for (LblErrorDTO lblErrorDTO : errorStoresToadd) {

			// add in errors
			Long lblStoreId = getLBLStoreID(lblErrorDTO.getClientId(),
					lblErrorDTO.getStore());
			if (lblStoreId != null) {

				// delete if it present in listings and errors
				deleteBusiness(lblStoreId);
				deleteErrorBusiness(lblStoreId);
			} else {
				LBLStoreDTO dto = new LBLStoreDTO();
				long nextLBLStoreId = LBLUtil.getNextLBLStoreId();
				dto.setLblStoreId(nextLBLStoreId);
				dto.setClientId(lblErrorDTO.getClientId());
				dto.setStore(lblErrorDTO.getStore());
				addStoreInLBLStoreMap(dto);
			}

			// add in errors
			lblStoreId = getLBLStoreID(lblErrorDTO.getClientId(),
					lblErrorDTO.getStore());

			LblErrorEntity entity = new LblErrorEntity();
			lblErrorDTO.setLblStoreId(lblStoreId);
			BeanUtils.copyProperties(lblErrorDTO, entity);
			entity.setUploadedTime(date);
			entity.setUploadJobId(uploadJobId);
			session.save(entity);

		}

	}

	public void updateBusinessByUpload(List<LocalBusinessDTO> storesToUpdate,
			String uploadJobId, String uploadUserName, Date date) {
		Session session = sessionFactory.getCurrentSession();

		for (LocalBusinessDTO localBusinessDTO : storesToUpdate) {

			Long lblStoreId = getLBLStoreID(localBusinessDTO.getClientId(),
					localBusinessDTO.getStore());

			String store = localBusinessDTO.getStore();

			// if there is name is store, update mapping table
			String newStore = localBusinessDTO.getNewStore();
			if (newStore != null && newStore.length() > 0) {
				if (!newStore.equalsIgnoreCase(store)) {
					updateStoreInLBLMap(lblStoreId, newStore);
					localBusinessDTO.setStore(newStore);
					updateLBLBusinessWithNewStoreName(localBusinessDTO, newStore);
				}

			}

			lblStoreId = getLBLStoreID(localBusinessDTO.getClientId(),
					localBusinessDTO.getStore());

			if (lblStoreId != null) {
				// delete if it present in listings and errors
				deleteBusiness(lblStoreId);
				deleteErrorBusiness(lblStoreId);

			} else {
				LBLStoreDTO dto = new LBLStoreDTO();
				long nextLBLStoreId = LBLUtil.getNextLBLStoreId();
				dto.setLblStoreId(nextLBLStoreId);
				dto.setClientId(localBusinessDTO.getClientId());
				dto.setStore(localBusinessDTO.getStore());
				addStoreInLBLStoreMap(dto);
			}

			lblStoreId = getLBLStoreID(localBusinessDTO.getClientId(),
					localBusinessDTO.getStore());

			LocalBusinessEntity entity = new LocalBusinessEntity();
			localBusinessDTO.setLblStoreId(lblStoreId);
			BeanUtils.copyProperties(localBusinessDTO, entity);
			entity.setUploadedTime(date);
			entity.setUploadJobId(uploadJobId);
			session.save(entity);

		}
	}
	
	public void updateLBLBusinessWithNewStoreName(LocalBusinessDTO bean,
			String newStore) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();

		logger.info("Updating Store " + bean.getStore() + " ,with lblStoreID: "
				+ bean.getLblStoreId());

		String query = "update LBLStoreEntity set store=? where lblStoreId='"
				+ bean.getLblStoreId() + "' and clientId='"
				+ bean.getClientId() + "'";

		Query createQuery = session.createQuery(query);
		createQuery.setString(0, newStore);

		int executeUpdate = createQuery.executeUpdate();

		logger.info("Completed Updating Store " + executeUpdate);
		
		LBLStoreDTO lblStoreDTO = new LBLStoreDTO();
		lblStoreDTO.setLblStoreId(bean.getLblStoreId());
		lblStoreDTO.setClientId(bean.getClientId());
		lblStoreDTO.setStore(bean.getStore());
		
		updateGMBInsightsWithlblStoreID(lblStoreDTO);
		
		updateGMBInsightsWithlblStoreIDAndStore(lblStoreDTO, newStore);
		
		
	}
	
	public void updateGMBInsightsWithlblStoreID(LBLStoreDTO lblStoreDTO) {
		// TODO Auto-generated method stub

		Session session = sessionFactory.getCurrentSession();

		logger.info("Updating Store " + lblStoreDTO.getStore()
				+ " ,with lblStoreID: " + lblStoreDTO.getLblStoreId());

		String query = "update InsightsGraphEntity set lblStoreId=? where store='"
				+ lblStoreDTO.getStore()
				+ "' and brandId='"
				+ lblStoreDTO.getClientId() + "'";

		Query createQuery = session.createQuery(query);
		createQuery.setLong(0, lblStoreDTO.getLblStoreId());

		int executeUpdate = createQuery.executeUpdate();

		logger.info("Completed Updating Store " + executeUpdate);
	}
	
	public void updateGMBInsightsWithlblStoreIDAndStore(LBLStoreDTO lblStoreDTO, String newStore) {
		// TODO Auto-generated method stub

		Session session = sessionFactory.getCurrentSession();

		logger.info("Updating Store " + lblStoreDTO.getStore()
				+ " ,with newStore: " + newStore);

		String query = "update InsightsGraphEntity set store=? where lblStoreId=?";

		Query createQuery = session.createQuery(query);
		createQuery.setString(0, newStore);
		createQuery.setLong(1, lblStoreDTO.getLblStoreId());

		int executeUpdate = createQuery.executeUpdate();
		
		
		String monthlyQuery = "update InsightsMonthlyCountEntity set store=? where lblStoreId=?";

		Query monthly = session.createQuery(monthlyQuery);
		monthly.setString(0, newStore);
		monthly.setLong(1, lblStoreDTO.getLblStoreId());
		int execute = monthly.executeUpdate();
		
	
		logger.info("Completed Updating Store " + executeUpdate);
	}

	public void deleteBusiness(Long lblStoreId) {
		Session session = sessionFactory.getCurrentSession();
		LocalBusinessEntity bean = (LocalBusinessEntity) session
				.createCriteria(LocalBusinessEntity.class)
				.add(Restrictions.eq("lblStoreId", lblStoreId)).uniqueResult();
		if (bean != null) {

			session.delete(bean);
		}
	}

	public void deleteErrorBusiness(Long lblStoreId) {
		Session session = sessionFactory.getCurrentSession();
		LblErrorEntity bean = (LblErrorEntity) session
				.createCriteria(LblErrorEntity.class)
				.add(Restrictions.eq("lblStoreId", lblStoreId)).uniqueResult();

		if (bean != null) {

			session.delete(bean);
		}
	}

	public void addStoreInLBLStoreMap(LBLStoreDTO dto) {

		Long lblStoreID = getLBLStoreID(dto.getClientId(), dto.getStore());

		if (lblStoreID == null) {
			Session session = sessionFactory.getCurrentSession();

			LBLStoreEntity storeEntity = new LBLStoreEntity();
			BeanUtils.copyProperties(dto, storeEntity);
			session.saveOrUpdate(storeEntity);
		}

	}

	public Long getLBLStoreID(Integer clientId, String store) {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(LBLStoreEntity.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		criteria.add(Restrictions.eq("store", store));

		LBLStoreEntity lblStore = (LBLStoreEntity) criteria.uniqueResult();
		if (lblStore != null)
			return lblStore.getLblStoreId();
		else {
			return null;
		}
	}

	public void updateStoreInLBLMap(Long lblStoreId, String newStore) {
		// TODO Auto-generated method stub

		Session session = sessionFactory.getCurrentSession();

		String query = "update LBLStoreEntity set store=? where lblStoreId="
				+ lblStoreId;

		Query createQuery = session.createQuery(query);
		createQuery.setString(0, newStore);

		int executeUpdate = createQuery.executeUpdate();

		logger.info("Completed Updating Store Value " + executeUpdate);
	}

}
