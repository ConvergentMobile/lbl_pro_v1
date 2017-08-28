package com.business.model.dataaccess.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.RestAuthDTO;
import com.business.common.dto.RestResponseDTO;
import com.business.model.dataaccess.RestDAO;
import com.business.model.pojo.RestAuthEntity;
import com.business.model.pojo.RestResponseEntity;

@Repository
public class RestDaoImpl implements RestDAO {

	Logger logger = Logger.getLogger(RestDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public void saveTrackingRequest(RestResponseDTO dto) {
		// TODO Auto-generated method stub
		RestResponseEntity entity = new RestResponseEntity();
		Session session = sessionFactory.getCurrentSession();
		BeanUtils.copyProperties(dto, entity);
		session.save(entity);
	}

	@Transactional
	public void updateTrackingStatus(String trackingId) {
		// TODO Auto-generated method stub

		Session session = sessionFactory.getCurrentSession();

		Query createQuery = session
				.createQuery("UPDATE RestResponseEntity SET status='COMPLETED' WHERE trackingId='"
						+ trackingId + "'");
		createQuery.executeUpdate();

	}

	@Transactional
	public void updateTrackingRequestMessage(String statusMessage,
			String trackingId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();

		Query createQuery = session
				.createQuery("UPDATE RestResponseEntity SET responseMessage='"
						+ statusMessage + "' WHERE trackingId='" + trackingId
						+ "'");
		createQuery.executeUpdate();
	}

	@Transactional
	public boolean validateClient(String authId, String authKey) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RestAuthEntity.class);
		criteria.add(Restrictions.eq("authId", authId));
		criteria.add(Restrictions.eq("authKey", authKey));

		List<RestAuthEntity> list = criteria.list();
		if (list != null && list.size() > 0) {
			return true;
		}

		return false;
	}
	
	@Override
	public void addRestAuthDetails(RestAuthDTO authDTO) {
		// TODO Auto-generated method stub
		RestAuthEntity entity = new RestAuthEntity();
		Session session = sessionFactory.getCurrentSession();
		BeanUtils.copyProperties(authDTO, entity);
		session.save(entity);
		
	}

	@Transactional
	public RestResponseDTO getStatus(String trackingId) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RestResponseEntity.class);
		criteria.add(Restrictions.eq("trackingId", trackingId));
		RestResponseEntity entity = (RestResponseEntity) criteria
				.uniqueResult();

		RestResponseDTO dto = new RestResponseDTO();
		if (entity != null) {
			BeanUtils.copyProperties(entity, dto);
		}

		return dto;
	}

}
