package com.business.model.dataaccess.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.business.common.dto.PartnerDTO;
import com.business.common.dto.SchedulerDTO;
import com.business.model.dataaccess.SchedulerDao;
import com.business.model.pojo.PartnersEntity;
import com.business.model.pojo.SchedulerEntity;

@Repository
public class SchedulerDaoImpl implements SchedulerDao{
	Logger logger = Logger.getLogger(BusinessDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private HttpSession httpSession;
	public List<SchedulerDTO> getScheduleListing() {
		Session session = sessionFactory.getCurrentSession();
		List<SchedulerDTO> schedulerDTOs = new ArrayList<SchedulerDTO>();

		Criteria createCriteria = session.createCriteria(SchedulerEntity.class);
		List<SchedulerEntity> list = createCriteria.list();

		for (SchedulerEntity schedulerEntity : list) {
			SchedulerDTO schedulerDTO = new SchedulerDTO();
			Integer brandID = schedulerEntity.getBrandID();
			String brandById = getBrandById(brandID);
			Integer partnerId = schedulerEntity.getPartnerId();
			String partnerByID = getPartnerByID(partnerId);
			Date scheduleTime = schedulerEntity.getScheduleTime();
			String hours = schedulerEntity.getHours();
			Integer recurring = schedulerEntity.getRecurring();
			schedulerDTO.setBrandName(brandById);
			schedulerDTO.setPartnerName(partnerByID);
			schedulerDTO.setRecurring(recurring);
			schedulerDTO.setSchdulerId(schedulerEntity.getSchdulerId());
			schedulerDTO.setHours(hours);
			schedulerDTO.setNextScheduleTime(schedulerEntity
					.getNextScheduleTime());

			schedulerDTO.setScheduleTime(scheduleTime);

			schedulerDTOs.add(schedulerDTO);


		}

		return schedulerDTOs;
	}
	public String getBrandById(Integer brandId) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT DISTINCT brandName FROM BrandEntity where brandID=?";
		Query createQuery = session.createQuery(sql);
		createQuery.setInteger(0, brandId);
		List<String> brandNames = createQuery.list();
		return brandNames != null && !brandNames.isEmpty() ? brandNames.get(0)
				: null;
	}
	public String getPartnerByID(Integer partnerId) {

		Session session = sessionFactory.getCurrentSession();

		String sql = "SELECT partnerName FROM PartnersEntity where partnerId=?";
		Query createQuery = session.createQuery(sql);
		createQuery.setInteger(0, partnerId);
		List<String> brandNames = createQuery.list();

		return brandNames != null && !brandNames.isEmpty() ? brandNames.get(0)
				: null;

	}

	public List<PartnerDTO> getPartners() {
		List<PartnerDTO> list = new ArrayList<PartnerDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PartnersEntity.class);
		List<PartnersEntity> pList = criteria.list();
		for (PartnersEntity partnersEntity : pList) {
			PartnerDTO partnerDTO = new PartnerDTO();

			BeanUtils.copyProperties(partnersEntity, partnerDTO);
			list.add(partnerDTO);

		}
		logger.info("partner names list==" + list.size());
		return list;
	}

	public void deleteSchedules(List<Integer> listIds) {
		Session session = sessionFactory.getCurrentSession();
		for (Integer id : listIds) {
			SchedulerEntity bean = (SchedulerEntity) session
					.createCriteria(SchedulerEntity.class)
					.add(Restrictions.eq("schdulerId", id)).uniqueResult();
			;
			session.delete(bean);
		}
	}

	public boolean saveSchedule(SchedulerDTO dto) {
		Session session = sessionFactory.getCurrentSession();

		SchedulerEntity schdulerEntity = new SchedulerEntity();

		BeanUtils.copyProperties(dto, schdulerEntity);

		session.save(schdulerEntity);

		return true;
	}

}
