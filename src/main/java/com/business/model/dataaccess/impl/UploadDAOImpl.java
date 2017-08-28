package com.business.model.dataaccess.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.StoresWithErrors;
import com.business.model.dataaccess.UploadDAO;
import com.business.model.pojo.LblErrorEntity;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.web.bean.UploadBusinessBean;

@Repository
public class UploadDAOImpl implements UploadDAO {
	Logger logger = Logger.getLogger(UploadDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	public void deleteBusiness(UploadBusinessBean uploadBean) {
		Session session = sessionFactory.getCurrentSession();
/*
		Criteria criteria = session.createCriteria(LblErrorEntity.class);

		criteria.add(Restrictions.eq("clientId", uploadBean.getClientId()));
		criteria.add(Restrictions.eq("store", uploadBean.getStore()));

		List<LblErrorEntity> list = criteria.list();
		for (LblErrorEntity bean : list) {
			if (bean != null) {
				session.delete(bean);
			}
		}
	*/

		String query = "delete from LocalBusinessEntity where store='"
				+ uploadBean.getStore() + "' and clientId='"
				+ uploadBean.getClientId() + "'";
		Query createQuery = session.createQuery(query);
		createQuery.executeUpdate();
		
		String queryError = "delete from LblErrorEntity where store='"
				+ uploadBean.getStore() + "' and clientId='"
				+ uploadBean.getClientId() + "'";
		Query errorQuery = session.createQuery(queryError);
		errorQuery.executeUpdate();

		/*Criteria criteriaBusiness = session
				.createCriteria(LocalBusinessEntity.class);

		criteriaBusiness.add(Restrictions.eq("clientId",
				uploadBean.getClientId()));
		criteriaBusiness.add(Restrictions.eq("store", uploadBean.getStore()));

		List<LocalBusinessEntity> listofBusiness = criteriaBusiness.list();
		for (LocalBusinessEntity bean : listofBusiness) {
			if (bean != null) {
				session.delete(bean);
			}
		}
		session.close();*/
	}

	/**
	 * 
	 */
	public void addBusiness(List<UploadBusinessBean> storesToadd,
			String trackingId) {
		Session session = sessionFactory.getCurrentSession();

		for (UploadBusinessBean validBusiness : storesToadd) {
			LocalBusinessEntity entity = new LocalBusinessEntity();

			BeanUtils.copyProperties(validBusiness, entity);
			entity.setTrackingId(trackingId);
			LocalBusinessDTO locationByStoreAndclient = getLocationByStoreAndclient(
					validBusiness.getStore(), validBusiness.getClientId());
			if (locationByStoreAndclient != null) {
				logger.info("Store found in db, so deleting it");
				deleteBusiness(validBusiness);

				session.save(entity);

			} else {
				logger.info("Store does not found in db, so adding it");

				session.save(entity);

			}
		}
	}

	public void updateBusiness(List<UploadBusinessBean> storesToUpdate,
			String trackingId) {
		// implement uodate
		Session session = sessionFactory.getCurrentSession();

		for (UploadBusinessBean validBusiness : storesToUpdate) {
			LocalBusinessEntity entity = new LocalBusinessEntity();

			// delete existing store
			deleteBusiness(validBusiness);

			// add the the store
			LocalBusinessEntity updateEntity = new LocalBusinessEntity();
			BeanUtils.copyProperties(validBusiness, updateEntity);
			updateEntity.setTrackingId(trackingId);
			session.save(updateEntity);
		}

	}

	public void addErrorBusiness(LblErrorEntity validBusiness, String trackingId) {
		UploadBusinessBean bean = new UploadBusinessBean();
		BeanUtils.copyProperties(validBusiness, bean);

		// delete existing store
		deleteBusiness(bean);

		// add to errors list
		Session session = sessionFactory.getCurrentSession();
		LblErrorEntity entity = new LblErrorEntity();
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

		LocalBusinessEntity entity = (LocalBusinessEntity)criteria.uniqueResult();
		if (entity != null && entity.getStore()!=null) {
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

}
