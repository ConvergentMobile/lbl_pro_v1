package com.business.model.dataaccess.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.jfree.util.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.business.common.dto.InsightsMonthlyCountDTO;
import com.business.common.dto.LBLStoreDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.model.dataaccess.ListingDAO;
import com.business.model.pojo.ChangeTrackingEntity;
import com.business.model.pojo.ChannelEntity;
import com.business.model.pojo.InsightsGraphEntity;
import com.business.model.pojo.InsightsMonthlyCountEntity;
import com.business.model.pojo.LBLStoreEntity;
import com.business.model.pojo.LblErrorEntity;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.web.bean.LocalBusinessBean;

@Repository
public class ListingDAOImpl implements ListingDAO {

	Logger logger = Logger.getLogger(ListingDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private HttpSession httpSession;

	public void addStore(LBLStoreDTO dto) {
		Session session = sessionFactory.getCurrentSession();

		LBLStoreEntity storeEntity = new LBLStoreEntity();
		BeanUtils.copyProperties(dto, storeEntity);
		session.save(storeEntity);

	}

	public Long getLBLStoreID(Integer clientId, String store) {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(LBLStoreEntity.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		criteria.add(Restrictions.eq("store", store));

		// logger.info("In getLBLStoreID clientId-store" + clientId + "==="+
		// store);

		List<LBLStoreEntity> list = criteria.list();
		if (list != null && list.size() > 0)
			return list.get(0).getLblStoreId();
		else {
			return null;
		}
	}

	public LBLStoreEntity getLBLStore(Integer clientId, String store) {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(LBLStoreEntity.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		criteria.add(Restrictions.eq("store", store));

		LBLStoreEntity storeEntity = (LBLStoreEntity) criteria.uniqueResult();
		return storeEntity;
	}

	public void updateLBLBusinessWithLBLStoreID(LocalBusinessDTO dto) {
		Long lblStoreId = getLBLStoreID(dto.getClientId(), dto.getStore());
		Session session = sessionFactory.getCurrentSession();

		LocalBusinessEntity localBuiness = getLocalBuiness(dto);
		localBuiness.setLblStoreId(lblStoreId);

		/*
		 * logger.info("Updating store " + dto.getStore() + " with lblStoreId: "
		 * + lblStoreId);
		 */
		session.update(localBuiness);

	}

	public void updateLBLErrorWithLBLStoreID(LblErrorDTO dto) {

		Long lblStoreId = getLBLStoreID(dto.getClientId(), dto.getStore());
		Session session = sessionFactory.getCurrentSession();

		LblErrorEntity error = getLocalBuinessError(dto);
		error.setLblStoreId(lblStoreId);

		logger.info("Updating store " + dto.getStore() + " with lblStoreId: "
				+ lblStoreId);
		session.update(error);

	}

	public LocalBusinessEntity getLocalBuiness(LocalBusinessDTO dto) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("clientId", dto.getClientId()));
		criteria.add(Restrictions.eq("store", dto.getStore()));

		List<LocalBusinessEntity> list = criteria.list();
		return list.get(0);
	}

	public LblErrorEntity getLocalBuinessError(LblErrorDTO dto) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LblErrorEntity.class);
		criteria.add(Restrictions.eq("clientId", dto.getClientId()));
		criteria.add(Restrictions.eq("store", dto.getStore()));

		LblErrorEntity businessEntity = (LblErrorEntity) criteria
				.uniqueResult();
		return businessEntity;
	}

	public List<LBLStoreDTO> getLBLStoreMap() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LBLStoreEntity.class);
		List<LBLStoreDTO> stores = new ArrayList<LBLStoreDTO>();

		List<LBLStoreEntity> list = criteria.list();
		for (LBLStoreEntity lblStoreEntity : list) {
			LBLStoreDTO dto = new LBLStoreDTO();
			BeanUtils.copyProperties(lblStoreEntity, dto);

			stores.add(dto);

		}
		return stores;
	}

	public Map<Long, String> getLBLStoreMapByCleintId(Integer clientId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LBLStoreEntity.class);
		criteria.add(Restrictions.eq("clientId", clientId));

		Map<Long, String> map = new HashMap<Long, String>();

		List<LBLStoreEntity> list = criteria.list();
		for (LBLStoreEntity lblStoreEntity : list) {

			map.put(lblStoreEntity.getLblStoreId(), lblStoreEntity.getStore());

		}
		return map;
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

	public void updateLBLBusinessWithNewStoreName(LocalBusinessBean bean,
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

	public List<LblErrorDTO> getListOfErrors() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LblErrorEntity.class);
		List<LblErrorDTO> stores = new ArrayList<LblErrorDTO>();

		List<LblErrorEntity> list = criteria.list();
		for (LblErrorEntity lblStoreEntity : list) {
			LblErrorDTO dto = new LblErrorDTO();
			BeanUtils.copyProperties(lblStoreEntity, dto);

			stores.add(dto);

		}
		return stores;
	}

	public void deleteStoreFromLBLMap(List<Long> listIds) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();

		for (Long id : listIds) {
			LBLStoreEntity bean = (LBLStoreEntity) session
					.createCriteria(LBLStoreEntity.class)
					.add(Restrictions.eq("lblStoreId", id)).uniqueResult();
			;
			session.delete(bean);
		}

	}

	List<ChangeTrackingEntity> getchangeTracking() {

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(ChangeTrackingEntity.class);
		List<ChangeTrackingEntity> list = criteria.list();
		return list;

	}

	public void updatechangeTrackingReport() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();

		List<ChangeTrackingEntity> getchangeTracking = getchangeTracking();
		for (ChangeTrackingEntity changeTrackingEntity : getchangeTracking) {

			String store = changeTrackingEntity.getStore();
			Integer clientId = changeTrackingEntity.getClientId();

			Long lblStoreID = getLBLStoreID(clientId, store);
			if (lblStoreID != null) {

				String query = "update ChangeTrackingEntity set lblStoreId=? where store='"
						+ store + "' and clientId='" + clientId + "'";

				Query createQuery = session.createQuery(query);
				createQuery.setLong(0, lblStoreID);

				int executeUpdate = createQuery.executeUpdate();
			}

		}
	}

	public LocalBusinessDTO getBusinessByLBlStoreID(Long lblStoreID) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("lblStoreId", lblStoreID));

		LocalBusinessDTO dto = new LocalBusinessDTO();

		Map<Long, String> map = new HashMap<Long, String>();

		LocalBusinessEntity entity = (LocalBusinessEntity) criteria
				.uniqueResult();
		if (entity != null) {
			BeanUtils.copyProperties(entity, dto);
			return dto;
		} else {
			return null;
		}

	}

	public void calculateMonthlyGMBCounts() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		
		
		List<Integer> brands = new ArrayList<Integer>();

		brands.add(3471);
		brands.add(3225);
		brands.add(3456);
		brands.add(3427);
		brands.add(4085);
		
		brands.add(3520);
		brands.add(3628);
		brands.add(3456);
		brands.add(3991);
		brands.add(4094);
		brands.add(3378);

		for (Integer brandID : brands) {
			 List<InsightsMonthlyCountDTO> monthlyCountsForStore = getMonthlyCountsForStore(brandID);
			 
				for (InsightsMonthlyCountDTO insightsMonthlyCountDTO : monthlyCountsForStore) {
					InsightsMonthlyCountEntity monthlyEntity = new InsightsMonthlyCountEntity();
					BeanUtils.copyProperties(insightsMonthlyCountDTO, monthlyEntity);
					session.save(monthlyEntity);
				}
		}

	}
	
	@Override
	public List<InsightsGraphEntity> getStoreInsightsWithNoLBLStore() {
		// TODO Auto-generated method stub
		List<InsightsGraphEntity> stores = new ArrayList<InsightsGraphEntity>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT DISTINCT store FROM InsightsGraphEntity where lblStoreId is null";
		Query createQuery = session.createQuery(sql);
		List<String> brandNames = createQuery.list();
		for (String store : brandNames) {
			InsightsGraphEntity obj = new InsightsGraphEntity();
			obj.setStore(store);
			stores.add(obj);
		}
		return stores;
	}
	

	public void deleteFromInsights(List<String> stores) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Query createQuery = session.createQuery("delete from InsightsGraphEntity where store IN (:list) ");
		createQuery.setParameterList("list", stores);
		int executeUpdate = createQuery.executeUpdate();
		logger.info("Total stores deleted are: "+ executeUpdate );
	}

	public List<InsightsMonthlyCountDTO> getMonthlyCountsForStore(
			Integer clientId) {

		Session session = sessionFactory.getCurrentSession();
		List<InsightsMonthlyCountDTO> trendData = new ArrayList<InsightsMonthlyCountDTO>();

		String query = "";
		Query createQuery = null;

		query = "SELECT date, sum(totalSearchCount) as searches, sum(totalViewCount) as views, sum(totalActionCount) as actions, store, state, lblStoreId, brandId "
				+ "from InsightsGraphEntity where brandId=? GROUP BY YEAR(date), MONTH(date), store order by store";
		
				createQuery = session.createQuery(query);
		createQuery.setInteger(0, clientId);

		List<Object[]> dataCount = createQuery.list();

		for (Object[] objects : dataCount) {
			InsightsMonthlyCountDTO history = new InsightsMonthlyCountDTO();
			if (objects[0] != null) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.S");
				Date date = null;
				try {
					date = format.parse(objects[0].toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				history.setDate(date);
				history.setSearchCount(Long.valueOf(objects[1].toString()));
				history.setViewCount(Long.valueOf(objects[2].toString()));
				history.setActionCount(Long.valueOf(objects[3].toString()));
				history.setStore(objects[4].toString());
				history.setState(objects[5].toString());
				if(objects[6]!=null) {
					history.setLblStoreId(Long.valueOf(objects[6].toString()));
				}
				history.setBrandId(Integer.parseInt(objects[7].toString()));
				trendData.add(history);
			}
		}
		return trendData;
	}
	
	public InsightsMonthlyCountDTO getCurrentMonthCountsForStore(Integer clientId) {
		Session session = sessionFactory.getCurrentSession();

		String query = "";
		Query createQuery = null;

		query = "SELECT date, sum(totalSearchCount) as searches, sum(totalViewCount) as views, sum(totalActionCount) as actions, store, state, lblStoreId, brandId "
				+ "from InsightsGraphEntity where brandId=? GROUP BY YEAR(date), MONTH(date), store order by store";
		
				createQuery = session.createQuery(query);
		createQuery.setInteger(0, clientId);

		List<Object[]> dataCount = createQuery.list();
		InsightsMonthlyCountDTO history = new InsightsMonthlyCountDTO();
		for (Object[] objects : dataCount) {
	
			if (objects[0] != null) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.S");
				Date date = null;
				try {
					date = format.parse(objects[0].toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				history.setDate(date);
				history.setSearchCount(Long.valueOf(objects[1].toString()));
				history.setViewCount(Long.valueOf(objects[2].toString()));
				history.setActionCount(Long.valueOf(objects[3].toString()));
				history.setStore(objects[4].toString());
				history.setState(objects[5].toString());
				if(objects[6]!=null) {
					history.setLblStoreId(Long.valueOf(objects[6].toString()));
				}
				history.setBrandId(Integer.parseInt(objects[7].toString()));

			}
		}
		return history;
	}
	
	

}
