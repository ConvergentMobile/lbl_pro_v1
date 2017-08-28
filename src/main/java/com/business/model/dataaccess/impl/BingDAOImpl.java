package com.business.model.dataaccess.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.business.common.dto.BingAnalyticsDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.model.dataaccess.BingDAO;
import com.business.model.pojo.BingAnalyticsEntity;
import com.business.model.pojo.CategorySyphcode;
import com.business.model.pojo.LocalBusinessEntity;

@Repository
public class BingDAOImpl implements BingDAO {
	Logger logger = Logger.getLogger(BingDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	public List<LocalBusinessDTO> getStoresByBrand(Integer brandId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("clientId", brandId));
		List<LocalBusinessEntity> list = criteria.list();
		List<LocalBusinessDTO> allListings = new ArrayList<LocalBusinessDTO>();
		for (LocalBusinessEntity localBusinessEntity : list) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessEntity, dto);
			allListings.add(dto);
		}
		return allListings;
	}

	public void addAnalytics(List<BingAnalyticsDTO> analytics) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();

		for (BingAnalyticsDTO bingAnalyticsDTO : analytics) {
			BingAnalyticsEntity entity = new BingAnalyticsEntity();
			BeanUtils.copyProperties(bingAnalyticsDTO, entity);
			session.save(entity);
		}
		logger.info("Anlytics are added");

	}

	public Map<String, String> getBingCategoryDetails(Integer clientId,
			String store) {

		Map<String, String> map = new HashMap<String, String>();

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		criteria.add(Restrictions.eq("store", store));

		CategorySyphcode syphCode = (CategorySyphcode) criteria.uniqueResult();
		if (syphCode != null) {
			map.put(syphCode.getBingCategory(), syphCode.getBingCategoryDesc());
		} else {
			Criteria criteriaBrand = session
					.createCriteria(CategorySyphcode.class);
			criteria.add(Restrictions.eq("clientId", clientId));

			List<CategorySyphcode> syphCodeList = criteriaBrand.list();
			if (syphCodeList != null) {
				for (CategorySyphcode categorySyphcode : syphCodeList) {
					map.put(categorySyphcode.getBingCategory(),
							categorySyphcode.getBingCategoryDesc());
					break;
				}

			}
		}

		return map;
	}

}
