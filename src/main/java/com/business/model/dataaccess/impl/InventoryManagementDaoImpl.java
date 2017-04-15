package com.business.model.dataaccess.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.CategoryDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.model.dataaccess.InventoryManagementDao;
import com.business.model.pojo.CategorySyphcode;
import com.business.model.pojo.LocalBusinessEntity;
@Repository
public class InventoryManagementDaoImpl implements InventoryManagementDao{
	
	Logger logger = Logger.getLogger(BusinessDaoImpl.class);
	@Autowired
  private SessionFactory sessionFactory;
	
	
	public List<BrandInfoDTO> getListOfContrctedAcxiomPartners() {
		Session session = sessionFactory.getCurrentSession();
		List<BrandInfoDTO> partners = new ArrayList<BrandInfoDTO>();
		String nameQuery = "SELECT  SUM(locationsInvoiced) FROM BrandEntity WHERE submisions=? ";
		
		
		Query createQuery = session.createQuery(nameQuery);
		createQuery.setString(0, "Acxiom");
		List<String> nonZeroLocationBrandNames = createQuery.list();

		for (String objects : nonZeroLocationBrandNames) {
			BrandInfoDTO dto = new BrandInfoDTO();
			
			dto.setCount(objects);			
			partners.add(dto);
		}
		
		
		return partners;
	}
	
	public List<BrandInfoDTO> getListOfContrctedFactualPartners() {
		Session session = sessionFactory.getCurrentSession();
		List<BrandInfoDTO> partners = new ArrayList<BrandInfoDTO>();
		String nameQuery = "SELECT  SUM(locationsInvoiced) FROM BrandEntity WHERE submisions=? ";
		
		
		Query createQuery = session.createQuery(nameQuery);
		createQuery.setString(0, "Factual");
		List<String> nonZeroLocationBrandNames = createQuery.list();

		for (String objects : nonZeroLocationBrandNames) {
			BrandInfoDTO dto = new BrandInfoDTO();
			
			dto.setCount(objects);			
			partners.add(dto);
		}
		
		
		return partners;
	}
	
	public List<BrandInfoDTO> getListOfContrctedInfoGroupPartners() {
		Session session = sessionFactory.getCurrentSession();
		List<BrandInfoDTO> partners = new ArrayList<BrandInfoDTO>();
		String nameQuery = "SELECT  SUM(locationsInvoiced) FROM BrandEntity WHERE submisions=? ";
		
		
		Query createQuery = session.createQuery(nameQuery);
		createQuery.setString(0, "InfoGroup");
		List<String> nonZeroLocationBrandNames = createQuery.list();

		for (String objects : nonZeroLocationBrandNames) {
			BrandInfoDTO dto = new BrandInfoDTO();
			
			dto.setCount(objects);			
			partners.add(dto);
		}
		
		
		return partners;
	}
	
	public List<BrandInfoDTO> getListOfContrctedLocalezePartners() {
		Session session = sessionFactory.getCurrentSession();
		List<BrandInfoDTO> partners = new ArrayList<BrandInfoDTO>();
		String nameQuery = "SELECT  SUM(locationsInvoiced) FROM BrandEntity WHERE submisions=? ";
		
		
		Query createQuery = session.createQuery(nameQuery);
		createQuery.setString(0, "Neustar");
		List<String> nonZeroLocationBrandNames = createQuery.list();

		for (String objects : nonZeroLocationBrandNames) {
			BrandInfoDTO dto = new BrandInfoDTO();
			
			dto.setCount(objects);			
			partners.add(dto);
		}
		
		
		return partners;
	}

	public List<BrandInfoDTO> getAllClientLocations() {
		Session session = sessionFactory.getCurrentSession();
		List<BrandInfoDTO> partners = new ArrayList<BrandInfoDTO>();
		String nameQuery = "SELECT clientId, locationsInvoiced FROM BrandEntity WHERE clientId IS NOT NULL GROUP BY clientId";
		
		Query createQuery = session.createQuery(nameQuery);
		List<Object[]> nonZeroLocationBrandNames = createQuery.list();

		for (Object[] objects : nonZeroLocationBrandNames) {
			BrandInfoDTO dto = new BrandInfoDTO();
			dto.setClientId((Integer) objects[0]);
			dto.setCount((String) objects[1]);
			partners.add(dto);
		}
		
		return partners;
	}
	public List<BrandInfoDTO> getListOfCOntrctedBrands() {
		Session session = sessionFactory.getCurrentSession();
		List<BrandInfoDTO> partners = new ArrayList<BrandInfoDTO>();
		String nameQuery = "SELECT brandName, locationsInvoiced FROM BrandEntity WHERE brandname IS NOT NULL GROUP BY brandname ";
		Query createQuery = session.createQuery(nameQuery);
		
		List<Object[]> nonZeroLocationBrandNames = createQuery.list();

		for (Object[] objects : nonZeroLocationBrandNames) {
			BrandInfoDTO dto = new BrandInfoDTO();
			dto.setBrandName((String) objects[0]);
			dto.setCount((String) objects[1]);
			partners.add(dto);
		}
		
		
		return partners;
	}

	public List<LocalBusinessDTO> searchBusinessListinginfo(String store) {
		List<LocalBusinessDTO> brandInfoDTO = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		
		criteria.add(Restrictions.eq("store", store));
		List<LocalBusinessEntity> list = criteria.list();
		int size = list.size();
		//logger.info("size:::::::::::::::::::::"+size);
		for (LocalBusinessEntity entity : list) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			
			BeanUtils.copyProperties(entity, dto);
			brandInfoDTO.add(dto);

		}
		return brandInfoDTO;
	}

	
	public List<LocalBusinessDTO> searchBusinessListinginfoByBrand(String brand) {
		List<LocalBusinessDTO> brandInfoDTO = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		
		criteria.add(Restrictions.like("client", brand,MatchMode.ANYWHERE));
		List<LocalBusinessEntity> list = criteria.list();
		int size = list.size();
		
		for (LocalBusinessEntity entity : list) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			
			BeanUtils.copyProperties(entity, dto);
			brandInfoDTO.add(dto);

		}
		return brandInfoDTO;
	}

}
