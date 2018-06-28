package com.business.model.dataaccess.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.model.dataaccess.ManageBrandDao;
import com.business.model.pojo.BrandEntity;
import com.business.model.pojo.ChannelEntity;
import com.business.model.pojo.LblErrorEntity;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.model.pojo.SchedulerEntity;
import com.business.model.pojo.User_brands;
@Repository
public class ManageBrandDaoImpl implements ManageBrandDao{
	Logger logger = Logger.getLogger(BusinessDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private HttpSession httpSession;
	
	public List<ChannelNameDTO> getChannel() {
		List<ChannelNameDTO> list = new ArrayList<ChannelNameDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ChannelEntity.class);
		List<ChannelEntity> channelNameList = criteria.list();
		for (ChannelEntity channelNameEntity : channelNameList) {
			ChannelNameDTO channelNameDTO = new ChannelNameDTO();
			String channelName = channelNameEntity.getChannelName();
			Integer channelID = channelNameEntity.getChannelID();
			channelNameDTO.setChannelName(channelName);
			channelNameDTO.setChannelID(channelID);
			list.add(channelNameDTO);

		}
		logger.info("channelnames list==" + list.size());
		return list;
	}

	
	public List<BrandInfoDTO> getBrandsByChannelID(Integer channelID) {
		Session session = sessionFactory.getCurrentSession();

		List<BrandInfoDTO> brandInfoDTOs = new ArrayList<BrandInfoDTO>();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("channelID", channelID));
		List<BrandEntity> list = criteria.list();

		for (BrandEntity brandEntity : list) {
			BrandInfoDTO brandInfoDTO = new BrandInfoDTO();
			BeanUtils.copyProperties(brandEntity, brandInfoDTO);
			brandInfoDTOs.add(brandInfoDTO);

		}
		return brandInfoDTOs;
	}

	
	public void deleteBrands(List<Integer> listofbrand) {
		Session session = sessionFactory.getCurrentSession();
		System.out.println("listof brand:::::::"+listofbrand);
		for (Integer brandid : listofbrand) {
			Criteria criteria = session.createCriteria(BrandEntity.class);
			Integer clientId = null;
			criteria.add(Restrictions.eq("brandID", brandid));
			List<BrandEntity> list = criteria.list();
			for (BrandEntity brandEntity : list) {
				clientId = brandEntity.getClientId();
			}

			Query createQuery = session
					.createQuery("delete from BrandEntity where brandID=" + brandid
							+ "");
			createQuery.executeUpdate();
			logger.info("brandid:::::::::::::::::"+brandid);

			deleteBrnadInfoOnBusinessListings(clientId);
			deleteBrnadInfoOnErrorListings(clientId);
			deleteBrandFromUserBrands(brandid);
			deleteBrandsFromScheduler( brandid);
			
		}

	}

	
	private void deleteBrandFromUserBrands(Integer brandid) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User_brands.class);
		criteria.add(Restrictions.eq("brandID", brandid));
		Query createQuery = session
				.createQuery("delete from  User_brands where brandID="
						+ brandid + "");
		createQuery.executeUpdate();
		
	}


	private void deleteBrandsFromScheduler(Integer brandid) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SchedulerEntity.class);
		criteria.add(Restrictions.eq("brandID", brandid));
		Query createQuery = session
				.createQuery("delete from  SchedulerEntity where brandID="
						+ brandid + "");
		createQuery.executeUpdate();
		
	}


	private void deleteBrnadInfoOnErrorListings(Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		session.createCriteria(LblErrorEntity.class);
		Query createQuery = session
				.createQuery("delete from LblErrorEntity where clientId="
						+ clientId + "");
		createQuery.executeUpdate();
		
	}


	private void deleteBrnadInfoOnBusinessListings(Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		session.createCriteria(LocalBusinessEntity.class);
		Query createQuery = session
				.createQuery("delete from LocalBusinessEntity where clientId="
						+ clientId + "");
		createQuery.executeUpdate();
		
	}


	public void deleteChannel(Integer channelID) {
	
		Session session = sessionFactory.getCurrentSession();
		session.createCriteria(ChannelEntity.class);
		Query createQuery = session
				.createQuery("delete from ChannelEntity where channelID="
						+ channelID + "");
		createQuery.executeUpdate();
		updateTheDeleteChannelInbrnds(channelID);
		logger.info("channel delete id ::" + channelID);
	}
	


	public void deleteChannels(List<Integer> listofchannels) {
		Session session = sessionFactory.getCurrentSession();
		for (Integer channelid : listofchannels) {
				
			session.createCriteria(ChannelEntity.class);
			Query createQuery = session
					.createQuery("delete from ChannelEntity where channelID="
							+ channelid + "");
			createQuery.executeUpdate();
			updateTheDeleteChannelInbrnds(channelid);
			deleteBrandFromUserChannels(channelid);
			
		
		}
	}
	public void updateTheDeleteChannelInbrnds(Integer channelID) {

		Session session = sessionFactory.getCurrentSession();
		session.createCriteria(BrandEntity.class);
		Query createQuery = session
				.createQuery("UPDATE BrandEntity SET channelID=0 WHERE channelID="
						+ channelID + "");
		createQuery.executeUpdate();

	}
	private void deleteBrandFromUserChannels(Integer channelid) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User_brands.class);
		criteria.add(Restrictions.eq("channelID", channelid));
		Query createQuery = session
				.createQuery("delete from  User_brands where channelID="
						+ channelid + "");
		createQuery.executeUpdate();
		
	}
}
