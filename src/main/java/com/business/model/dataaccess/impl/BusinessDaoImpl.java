package com.business.model.dataaccess.impl;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.AccuracyGraphDTO;
import com.business.common.dto.BingAnalyticsDTO;
import com.business.common.dto.BingReportDTO;
import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.CategoryDTO;
import com.business.common.dto.ChangeTrackingDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.CitationReportDTO;
import com.business.common.dto.CustomSubmissionsDTO;
import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.ForgotPasswordDto;
import com.business.common.dto.InsightsGraphDTO;
import com.business.common.dto.InsightsMonthlyCountDTO;
import com.business.common.dto.LBLStoreDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.PartnerDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.common.dto.SchedulerDTO;
import com.business.common.dto.SearchDomainDTO;
import com.business.common.dto.UploadReportDTO;
import com.business.common.dto.UsersDTO;
import com.business.common.util.DAOUtil;
import com.business.common.util.DateUtil;
import com.business.common.util.LBLConstants;
import com.business.model.dataaccess.BusinessDao;
import com.business.model.pojo.AcceptedBrandsEntity;
import com.business.model.pojo.AccuarcyGraphEntity;
import com.business.model.pojo.AccuracyPercentageEntity;
import com.business.model.pojo.AccuracyReportEntity;
import com.business.model.pojo.AccuracyStageReportEntity;
import com.business.model.pojo.AuditEntity;
import com.business.model.pojo.BrandEntity;
import com.business.model.pojo.CategoryEntity;
import com.business.model.pojo.CategorySyphcode;
import com.business.model.pojo.ChangeTrackingEntity;
import com.business.model.pojo.ChannelEntity;
import com.business.model.pojo.CheckReportEntity;
import com.business.model.pojo.CheckReportStageEntity;
import com.business.model.pojo.CitationGraphEntity;
import com.business.model.pojo.CitationReportEntity;
import com.business.model.pojo.CustomSubmissions;
import com.business.model.pojo.ExportReportEntity;
import com.business.model.pojo.FailedScrapingsEntity;
import com.business.model.pojo.ForgotPasswordEntity;
import com.business.model.pojo.InsightsGraphEntity;
import com.business.model.pojo.InsightsMonthlyCountEntity;
import com.business.model.pojo.LBLStoreEntity;
import com.business.model.pojo.LblErrorEntity;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.model.pojo.PartnersEntity;
import com.business.model.pojo.RenewalReportEntity;
import com.business.model.pojo.RoleEntity;
import com.business.model.pojo.SchedulerEntity;
import com.business.model.pojo.SearchDomains;
import com.business.model.pojo.StandardStreetAbbreviationsEntity;
import com.business.model.pojo.StatesListEntity;
import com.business.model.pojo.UploadReportEntity;
import com.business.model.pojo.UserStoresEntity;
import com.business.model.pojo.User_brands;
import com.business.model.pojo.UsersEntity;
import com.business.model.pojo.ValueObject;
import com.business.web.bean.CustomSubmissionsBean;
import com.business.web.bean.UploadBusinessBean;
import com.business.web.bean.UsersBean;
import com.google.api.services.mybusiness.v4.model.Location;

/**
 * 
 * @author lbl_dev
 * 
 *         DataAccess layer of Business Which interacts with database
 * 
 */

@Repository
public class BusinessDaoImpl implements BusinessDao {
	Logger logger = Logger.getLogger(BusinessDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private HttpSession httpSession;

	/**
	 * get listOfBusinessInfo
	 */

	public List<LocalBusinessDTO> getListOfBusinessInfo() {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		logger.info("roleId::::::::::::::" + roleId);
		logger.info("userName::::::::::::::" + userName);
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
					logger.info(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					logger.info(brand);
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;
				}
			}
		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
		}

		if (allBrandNames != null && allBrandNames.size() > 0) {
			sql += " order by store DESC";
		}

		logger.debug("Fetching the list of businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LocalBusinessEntity> businessEntities = query.list();
		logger.info("total businesses are: " + businessEntities.size());
		for (LocalBusinessEntity localBusinessBean : businessEntities) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);

			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByStore(
			String flag, String companyname, String searchType) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			}
			if (allBrandNames != null && allBrandNames.size() > 0) {
				sql += " where ";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					brandsMap.put(index, brand);
					index++;
				}
			}
		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
		}

		if (allBrandNames != null && allBrandNames.size() > 0
				&& companyname.isEmpty()) {
			sql += " order by store " + flag;
		}
		if (allBrandNames != null && allBrandNames.size() > 0
				&& !companyname.isEmpty()) {
			sql += " WHERE " + searchType + " like '%" + companyname
					+ "%' order by store " + flag;
		}

		logger.debug("Fetching the list of businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LocalBusinessEntity> businessEntities = query.list();
		logger.info("total businesses are: " + businessEntities.size());
		for (LocalBusinessEntity localBusinessBean : businessEntities) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	/**
	 * get All Listings
	 * 
	 * @return
	 */
	public List<LocalBusinessDTO> getAllListings() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		List<LocalBusinessEntity> list = criteria.list();
		List<LocalBusinessDTO> allListings = new ArrayList<LocalBusinessDTO>();
		for (LocalBusinessEntity localBusinessEntity : list) {
			LocalBusinessDTO dto = new LocalBusinessDTO();

			BeanUtils.copyProperties(localBusinessEntity, dto);
			allListings.add(dto);
		}
		return allListings;
	}

	/**
	 * getListOfErrors
	 * 
	 * 
	 * 
	 */
	public List<LblErrorDTO> getListOfErrors() {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> businessDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LblErrorEntity";
		List<String> allBrandNames = new ArrayList<String>();
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// String brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}
			if (allBrandNames != null && allBrandNames.size() > 0) {
				sql += " where ";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					brandsMap.put(index, brand);
					index++;
				}
			}

		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
		}
		if (allBrandNames != null && allBrandNames.size() > 0) {
			sql += " order by uploadedTime desc";
		}

		logger.debug("Fetching the list of error businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LblErrorEntity> businessEntities = query.list();
		logger.info("total errored businesses are: " + businessEntities.size());
		for (LblErrorEntity localBusinessBean : businessEntities) {
			LblErrorDTO dto = new LblErrorDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	/**
	 * deleteBusinessInfo
	 */
	public void deleteBusinessInfo(List<Long> listIds) {

		Session session = sessionFactory.getCurrentSession();
		for (Long id : listIds) {
			LocalBusinessEntity bean = (LocalBusinessEntity) session
					.createCriteria(LocalBusinessEntity.class)
					.add(Restrictions.eq("lblStoreId", id)).uniqueResult();
			;
			session.delete(bean);
		}

	}

	/***
	 * getBrandsInfo
	 */
	public List<LocalBusinessDTO> getBrandsInfo(String brands) {
		List<LocalBusinessDTO> list = new ArrayList<LocalBusinessDTO>();

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("client", brands));
		List<LocalBusinessEntity> criteriaList = criteria.list();

		for (LocalBusinessEntity localBusinessBean : criteriaList) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			list.add(dto);
		}
		return list;
	}

	/***
	 * updateBusinessInfo
	 */

	public boolean updateBusinessInfo(LocalBusinessDTO businessDTO) {
		Session session = sessionFactory.getCurrentSession();

		LocalBusinessEntity bean = (LocalBusinessEntity) session.get(
				LocalBusinessEntity.class, businessDTO.getId());
		// delete entry
		if(bean!=null)
			session.delete(bean);
		BeanUtils.copyProperties(businessDTO, bean);

		// save enrey with latest details
		session.save(bean);

		return true;
	}

	/***
	 * uploadBusinessInfo
	 */

	public boolean addBusinessList(List<LocalBusinessDTO> businessDTO,
			Date date, String uploadJobId) {
		Session session = sessionFactory.getCurrentSession();

		for (LocalBusinessDTO localBusinessDTO : businessDTO) {
			LocalBusinessEntity businessBean = new LocalBusinessEntity();
			BeanUtils.copyProperties(localBusinessDTO, businessBean);
			businessBean.setUploadedTime(date);
			businessBean.setUploadJobId(uploadJobId);
			businessBean.setLocationClosed("N");
			Serializable save = session.save(businessBean);
			// logger.info("Business Information Saved or Not ? " + (save !=
			// null));
		}
		return true;
	}

	/**
	 * getSpecificBusinessInfo
	 */
	public List<LocalBusinessDTO> getSpecificBusinessInfo(List<Long> listIds,
			String services) {
		Session session = sessionFactory.getCurrentSession();
		List<LocalBusinessDTO> list = new ArrayList<LocalBusinessDTO>();
		for (Long id : listIds) {
			// LocalBusinessDTO dto = new LocalBusinessDTO();
			/*
			 * LocalBusinessEntity entityBean = (LocalBusinessEntity) session
			 * .load(LocalBusinessEntity.class, id);
			 */
			LocalBusinessDTO dto = getBusinessInfo(id);
			// BeanUtils.copyProperties(entityBean, dto);
			String client = dto.getClient();
			List<String> category = getCategory(client, services);
			StringBuffer sb = new StringBuffer();

			if (services.contains("GoogleTemplate")) {
				for (int i = 0; i < category.size(); i++) {
					String categorys = category.get(i);
					if (categorys != null) {
						sb.append(categorys);

						if (i < category.size() - 1) {

							sb.append(",");

						}
					}

				}
			}
			String additinalCategory = sb.toString();
			if (additinalCategory != null && additinalCategory.endsWith(",")) {
				additinalCategory = additinalCategory.substring(0,
						additinalCategory.length() - 1);
			}
			StringBuffer stringbuffer = new StringBuffer();
			List<String> categorylist = getBingCategory(client, services);
			if (services.contains("BingTemplate")) {
				for (int i = 0; i < categorylist.size(); i++) {
					String categorys = categorylist.get(i);

					if (categorys != null) {
						stringbuffer.append(categorys);
						if (i < categorylist.size() - 1) {

							stringbuffer.append("||");

						}
					}

				}
			}
			String bingCategory = stringbuffer.toString();
			if (bingCategory != null && bingCategory.endsWith("||")) {

				bingCategory = bingCategory.substring(0,
						bingCategory.length() - 1);
			}
			if (bingCategory != null && bingCategory.endsWith("|")) {

				bingCategory = bingCategory.substring(0,
						bingCategory.length() - 1);
			}

			String ypInternetHeading = getYpInternetHeading(client, services);
			String primeryCategory = getPrimeryCategory(client);
			logger.info("primeryCategoryin dao ::" + primeryCategory);

			dto.setPrimaryCategory(primeryCategory);
			dto.setYpInternetHeading(ypInternetHeading);
			dto.setAdditionalCategories(additinalCategory);
			dto.setCategorySyph(bingCategory);
			list.add(dto);
		}
		logger.info("Specific BusinessInfo List size == " + list.size());
		return list;
	}

	/**
	 * 
	 * searchBusinessinfo based on params
	 * 
	 */
	@Transactional
	public Set<LocalBusinessDTO> searchBusinessinfo(String companyName,
			String store, String locationPhone, String brands) {

		Set<LocalBusinessDTO> setOfLocalBusinessDto = new HashSet<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		logger.info("searchBusinessinfo Name == " + companyName);
		logger.info("searchBusinessinfo Address == " + store);
		logger.info("searchBusinessinfo PhoneNumber == " + locationPhone);
		logger.info("searchBusinessinfo BrandName==" + brands);
		logger.info("searchBusinessinfo Before action set size == "
				+ setOfLocalBusinessDto.size());
		companyName = DAOUtil.removeCommaFromString(companyName, "");
		store = DAOUtil.removeCommaFromString(store, "");
		brands = DAOUtil.removeCommaFromString(brands, "");
		if (companyName.equals("") && store.equals("")
				&& locationPhone.equals("") && brands.equals("")) {
			List<LocalBusinessDTO> listOfBusinessInfo = getListOfBusinessInfo();
			for (LocalBusinessDTO localBusinessDTO : listOfBusinessInfo) {
				setOfLocalBusinessDto.add(localBusinessDTO);
			}
		} else {
			StringBuffer whereCondtion = new StringBuffer(
					"select * from business where");
			if (!companyName.equals("")) {
				if (companyName.contains("'")) {
					companyName = companyName.replaceAll("'", "''");
				}
				whereCondtion = whereCondtion.append(" companyName like '%"
						+ companyName + "%'");
			}
			if (!store.equals("")) {
				if (store.contains("'")) {
					store = store.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business where")) {
					whereCondtion = whereCondtion.append(" store like '%"
							+ store + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and store like '%"
							+ store + "%'");
				}
			}
			if (!locationPhone.equals("")) {
				if (locationPhone.contains("'")) {
					locationPhone = locationPhone.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business where")) {
					whereCondtion = whereCondtion
							.append(" locationPhone like '%" + locationPhone
									+ "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and locationPhone like '%"
									+ locationPhone + "%'");
				}
			}
			if (!brands.equals("")) {
				if (brands.contains("'")) {
					brands = brands.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business where")) {
					whereCondtion = whereCondtion.append(" client like '%"
							+ brands + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and client like '%"
							+ brands + "%'");
				}
			}
			logger.info("@@@@whereCondtion: " + whereCondtion);

			List<LocalBusinessDTO> whereCoditionRecords = getSQlWhereCoditionRecords(
					whereCondtion.toString(), session);

			if (whereCoditionRecords.size() > 0) {
				for (LocalBusinessDTO localBusinessDTO : whereCoditionRecords) {
					setOfLocalBusinessDto.add(localBusinessDTO);
				}
			}
		}
		logger.info("searchBusinessinfo Set  of Record size After == "
				+ setOfLocalBusinessDto.size());
		return setOfLocalBusinessDto;
	}

	/**
	 * getWhereCoditionRecords
	 * 
	 * @param query
	 * @param session
	 * @return
	 */
	public List<LocalBusinessDTO> getWhereCoditionRecords(String query,
			Session session) {
		List<LocalBusinessDTO> nameList = new ArrayList<LocalBusinessDTO>();
		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			List<String> allBrandNames = new ArrayList<String>();
			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}
			int index = 0;
			int totalBrands = allBrandNames.size();
			for (String brand : allBrandNames) {

				if (roleId == LBLConstants.CHANNEL_ADMIN
						|| roleId == LBLConstants.PURIST) {
					if (index == 0) {
						query += " AND (";
					}
				} else {
					if (index == 0) {
						query += " AND (";
					}
				}

				if (index != 0) {
					query += " OR";
				}
				query += " client=?";
				if (roleId == LBLConstants.CHANNEL_ADMIN
						|| roleId == LBLConstants.PURIST) {
					if (totalBrands == index + 1) {
						query += " )";
					}
				}
				if (roleId == LBLConstants.CLIENT_ADMIN) {
					if (totalBrands == index + 1) {
						query += " )";
					}
				}

				brandsMap.put(index, brand);
				index++;
			}
		}
		logger.debug("Query executed to fecth recrds are: " + query);
		SQLQuery createQuery = session.createSQLQuery(query);
		createQuery.addEntity(LocalBusinessEntity.class);

		for (Integer position : brandsMap.keySet()) {
			createQuery.setString(position, brandsMap.get(position));
		}
		List<LocalBusinessEntity> listOfName = (List<LocalBusinessEntity>) createQuery
				.list();
		for (LocalBusinessEntity localBusinessBean : listOfName) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			nameList.add(dto);
		}
		return nameList;
	}

	/**
	 * uploadReportDTO
	 */

	public boolean uploadReportDTO(UploadReportDTO reportDTO) {

		Session session = sessionFactory.getCurrentSession();
		UploadReportEntity entity = new UploadReportEntity();
		BeanUtils.copyProperties(reportDTO, entity);

		Serializable save = session.save(entity);

		return save != null;
	}

	/**
	 * exportReportDTO
	 * 
	 * @param reportDTO
	 * @return
	 */
	public boolean exportReportDTO(ExportReportDTO reportDTO) {
		Session session = sessionFactory.getCurrentSession();

		ExportReportEntity entity = new ExportReportEntity();
		BeanUtils.copyProperties(reportDTO, entity);

		Serializable save = session.save(entity);

		return save != null;
	}

	/**
	 * getUserByUserNameAndPWD :: gets user details by username and password
	 */
	public UsersDTO getUserByUserNameAndPWD(String userName, String password) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UsersEntity.class);
		criteria.add(Restrictions.eq("userName", userName));
		criteria.add(Restrictions.eq("passWord", password));
		List<UsersEntity> list = criteria.list();
		UsersDTO usersDTO = null;
		if (list.size() > 0) {
			usersDTO = new UsersDTO();
			UsersEntity usersEntity = list.get(0);
			Integer userID = usersEntity.getUserID();
			Criteria createCriteria = session.createCriteria(User_brands.class);
			createCriteria.add(Restrictions.eq("userID", userID));
			List<User_brands> list3 = createCriteria.list();
			Integer channelID = null;
			for (User_brands user_brands : list3) {

				channelID = user_brands.getChannelID();
			}

			Criteria Criteria = session.createCriteria(ChannelEntity.class);
			Criteria.add(Restrictions.eq("channelID", channelID));
			List<ChannelEntity> list2 = Criteria.list();
			for (ChannelEntity channelEntity : list2) {
				String channelName = channelEntity.getChannelName();
				usersDTO.setChannelName(channelName);
			}
			BeanUtils.copyProperties(usersEntity, usersDTO);
		}
		return usersDTO;
	}

	/**
	 * getChannelIdByName :: get channelid based on chanel name
	 */
	public Integer getChannelIdByName(String channelName) {
		Integer channelID = 0;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ChannelEntity.class);
		criteria.add(Restrictions.eq("channelName", channelName));
		List list = criteria.list();
		if (list.size() > 0) {
			ChannelEntity channelEntity = (ChannelEntity) list.get(0);
			channelID = channelEntity.getChannelID();
			logger.info("channelID:::::::::::" + channelID);
		}
		return channelID;
	}

	public Map<String, String> getChannelImageBytes(String brand) {

		Map<String, String> brandAndChannelImages = new HashMap<String, String>();

		Session cmAdmin = sessionFactory.getCurrentSession();
		Criteria cmAdminCriteria = cmAdmin.createCriteria(ChannelEntity.class);
		cmAdminCriteria.add(Restrictions.eq("channelName", "ConvergentMobile"));
		ChannelEntity cmAdminEntity = (ChannelEntity) cmAdminCriteria.list()
				.get(0);
		String cmImagePath = cmAdminEntity.getImagePath();
		/*
		 * int cmblobLength = 0; byte[] cmAdminBlobAsBytes = null; try {
		 * cmblobLength = (int) cmImage.length(); cmAdminBlobAsBytes =
		 * cmImage.getBytes(1, cmblobLength); } catch (SQLException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		String brandChannel = getBrandChannel(brand);

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brand));
		BrandEntity brandEntity = (BrandEntity) criteria.list().get(0);

		String imagePath = brandEntity.getImagePath();
		if (imagePath == null || imagePath.length() == 0) {
			brandAndChannelImages.put("bImage", cmImagePath);
		} else {
			/*
			 * int blobLength = 0; byte[] brandBlobAsBytes = null; try {
			 * blobLength = (int) image.length(); brandBlobAsBytes =
			 * image.getBytes(1, blobLength); } catch (SQLException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */
			brandAndChannelImages.put("bImage", imagePath);
		}

		Session sessionChannel = sessionFactory.getCurrentSession();
		Criteria channelCriteria = sessionChannel
				.createCriteria(ChannelEntity.class);
		channelCriteria.add(Restrictions.eq("channelName", brandChannel));
		ChannelEntity channel = (ChannelEntity) channelCriteria.list().get(0);
		String cimagePath = channel.getImagePath();
		if (cimagePath == null || cimagePath.length() == 0) {
			brandAndChannelImages.put("cImage", cmImagePath);
		} else {
			/*
			 * int cblobLength = 0; byte[] channleBlobAsBytes = null; try {
			 * cblobLength = (int) cimage.length(); channleBlobAsBytes =
			 * cimage.getBytes(1, cblobLength); } catch (SQLException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 */
			brandAndChannelImages.put("cImage", cimagePath);
		}

		return brandAndChannelImages;

	}

	/**
	 * getBrandByBrandNameAndPartner
	 * 
	 */
	public BrandInfoDTO getBrandByBrandNameAndPartner(String brandName,
			String submission) {
		Integer brandID = 0;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brandName));
		criteria.add(Restrictions.eq("submisions", submission));
		List<BrandEntity> list = criteria.list();
		if (list != null && list.size() > 0) {
			BrandEntity brandEntity = list.get(0);
			BrandInfoDTO brandInfoDTO = new BrandInfoDTO();
			BeanUtils.copyProperties(brandEntity, brandInfoDTO);
			return brandInfoDTO;
		}
		return null;
	}

	/**
	 * get brandNameID ::get brand id based on name and submissions
	 * 
	 * @param brandName
	 * @param session
	 * @return
	 */
	public Integer brandNameID(String brandName, Session session) {

		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brandName));
		List list = criteria.list();
		Integer channelID = -1;
		if (list.size() > 0) {
			BrandEntity entity = (BrandEntity) list.get(0);
			channelID = entity.getChannelID();
		}
		return channelID;
	}

	/**
	 * getListOfBrands :: Gets brands lists.
	 */
	public List<LocalBusinessDTO> getListOfBrands() {
		Session session = sessionFactory.getCurrentSession();
		List<LocalBusinessDTO> brands = new ArrayList<LocalBusinessDTO>();
		String nameQuery = "SELECT client, COUNT(client) from LocalBusinessEntity where client IS NOT NULL GROUP BY client ORDER BY COUNT(client)";
		Integer role = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		boolean isSuperAdmin = true;
		if (!(role == LBLConstants.CONVERGENT_MOBILE_ADMIN) && userName != null) {
			isSuperAdmin = false;
			// nameQuery
			// +=" AND channelID = (SELECT u.channelID from UsersEntity u where
			// u.userName=?)";
		}
		// nameQuery += " GROUP BY brands";
		List<LocalBusinessDTO> zeroLocationBrandNames = getZeroLocationBrandNames(
				isSuperAdmin, userName);
		Query createQuery = session.createQuery(nameQuery);
		/*
		 * if(!role.equalsIgnoreCase(LBLConstants.CONVERGENT_MOBILE_ADMIN) &&
		 * userName!= null) createQuery.setString(0, userName);
		 */
		List<Object[]> nonZeroLocationBrandNames = createQuery.list();
		List<String> allBrandNames = getAllBrandNames(isSuperAdmin, userName);
		for (Object[] objects : nonZeroLocationBrandNames) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dto.setBrands((String) objects[0]);
			Object brandcountvalue = objects[1];

			dto.setBrandsCount((Long) brandcountvalue);
			if (allBrandNames.contains((String) objects[0])) {
				brands.add(dto);
			}
			//logger.info(Arrays.toString(objects));
		}
		brands.addAll(zeroLocationBrandNames);

		Collections.sort(brands, new Comparator<LocalBusinessDTO>() {

			public int compare(LocalBusinessDTO o1, LocalBusinessDTO o2) {
				return o1.getBrands().compareTo(o2.getBrands());
			}

		});

		return brands;
	}

	/**
	 * getZeroLocationBrandNames
	 * 
	 * @return
	 */
	public List<LocalBusinessDTO> getZeroLocationBrandNames(
			boolean isSuperAdmin, String userName) {
		List<LocalBusinessDTO> localBusinessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String query = "SELECT client from LocalBusinessEntity where client IS NOT NULL GROUP BY client";
		Query createQuery = session.createQuery(query);
		List<String> nonZeroLocationBrands = createQuery.list();
		List<String> brandsList;
		if (isSuperAdmin) {
			brandsList = getAllBrandNames();
		} else {
			brandsList = getAllBrandNames(isSuperAdmin, userName);
		}
		for (String brand : brandsList) {
			if (!nonZeroLocationBrands.contains(brand)) {
				LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();
				localBusinessDTO.setBrands(brand);
				localBusinessDTO.setBrandsCount(Long.valueOf(0));
				localBusinessDTOs.add(localBusinessDTO);
			}
		}
		return localBusinessDTOs;
	}

	/**
	 * get businesSearchInfo:: get business search information based on
	 * parameters
	 * 
	 */
	public Set<LocalBusinessDTO> businesSearchInfo(String brands,
			String companyName, String store, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode) {

		Set<LocalBusinessDTO> setOfBusinessRecords = new HashSet<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();

		if (brands.equals("") && companyName.equals("") && store.equals("")
				&& locationPhone.equals("") && locationAddress.equals("")
				&& locationCity.equals("") && locationState.equals("")
				&& locationZipCode.equals("")) {
			List<LocalBusinessDTO> listOfBusinessInfo = getListOfBusinessInfo();

			for (LocalBusinessDTO localBusinessDTO : listOfBusinessInfo) {

				setOfBusinessRecords.add(localBusinessDTO);
			}
		} else {
			StringBuffer whereCondtion = new StringBuffer(
					"select * from business where");
			if (!brands.equals("")) {
				if (brands.contains("'")) {
					brands = brands.replaceAll("'", "''");
					whereCondtion = whereCondtion.append(" client like '%"
							+ brands + "%'");
				} else {
					whereCondtion = whereCondtion.append(" client like '%"
							+ brands + "%'");
				}

			}

			if (!companyName.equals("")) {
				if (companyName.contains("'")) {
					companyName = companyName.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business where")) {
					whereCondtion = whereCondtion.append(" companyName like '%"
							+ companyName + "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and companyName like '%" + companyName
									+ "%'");
				}
			}
			/*
			 * if (!store.equals("")) { if(store.contains("'")){
			 * store=store.replaceAll("'", "''"); } if
			 * (whereCondtion.toString().equalsIgnoreCase(
			 * "select * from business where")) { whereCondtion =
			 * whereCondtion.append(" store like '%" + store + "%'"); } else {
			 * whereCondtion = whereCondtion.append(" and store like '%" + store
			 * + "%'"); } }
			 */
			if (!store.equals("")) {
				if (store.contains("'")) {
					store = store.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business where")) {
					whereCondtion = whereCondtion.append(" store like '%"
							+ store + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and store like '%"
							+ store + "%'");
				}
			}
			if (!locationPhone.equals("")) {
				if (locationPhone.contains("'")) {
					locationPhone = locationPhone.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business where")) {
					whereCondtion = whereCondtion
							.append(" locationPhone like '%" + locationPhone
									+ "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and locationPhone like '%"
									+ locationPhone + "%'");
				}
			}
			if (!locationAddress.equals("")) {
				if (locationAddress.contains("'")) {
					locationAddress = locationAddress.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business where")) {
					whereCondtion = whereCondtion
							.append(" locationAddress like '%"
									+ locationAddress + "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and locationAddress like '%"
									+ locationAddress + "%'");
				}
			}
			if (!locationCity.equals("")) {
				if (locationCity.contains("'")) {
					locationCity = locationCity.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business where")) {
					whereCondtion = whereCondtion
							.append(" locationCity like '%" + locationCity
									+ "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and locationCity like '%" + locationCity
									+ "%'");
				}
			}
			if (!locationState.equals("")) {
				if (locationState.contains("'")) {
					locationState = locationState.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business where")) {
					whereCondtion = whereCondtion
							.append(" locationState like '%" + locationState
									+ "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and locationState like '%"
									+ locationState + "%'");
				}
			}
			if (!locationZipCode.equals("")) {
				if (locationZipCode.contains("'")) {
					locationZipCode = locationZipCode.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business where")) {
					whereCondtion = whereCondtion
							.append(" locationZipCode like '%"
									+ locationZipCode + "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and locationZipCode like '%"
									+ locationZipCode + "%'");
				}
			}

			logger.info("### businesslisting search fields Query : "
					+ whereCondtion);
			List<LocalBusinessDTO> whereCoditionRecords = getSQlWhereCoditionRecords(
					whereCondtion.toString(), session);
			if (whereCoditionRecords.size() > 0) {
				for (LocalBusinessDTO localBusinessDTO : whereCoditionRecords) {
					setOfBusinessRecords.add(localBusinessDTO);
				}
			}

		}
		return setOfBusinessRecords;
	}

	private List<LocalBusinessDTO> getSQlWhereCoditionRecords(String query,
			Session session) {
		List<LocalBusinessDTO> nameList = new ArrayList<LocalBusinessDTO>();
		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			List<String> allBrandNames = new ArrayList<String>();
			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}
			int index = 0;
			int totalBrands = allBrandNames.size();
			for (String brand : allBrandNames) {

				if (roleId == LBLConstants.CHANNEL_ADMIN
						|| roleId == LBLConstants.PURIST) {
					if (index == 0) {
						query += " AND (";
					}
				} else {
					if (index == 0) {
						query += " AND (";
					}
				}

				if (index != 0) {
					query += " OR";
				}
				query += " client=?";
				if (roleId == LBLConstants.CHANNEL_ADMIN
						|| roleId == LBLConstants.PURIST) {
					if (totalBrands == index + 1) {
						query += " )";
					}
				}
				if (roleId == LBLConstants.CLIENT_ADMIN) {
					if (totalBrands == index + 1) {
						query += " )";
					}
				}

				brandsMap.put(index, brand);
				index++;
			}
		}
		logger.debug("Query executed to fecth recrds are: " + query);
		SQLQuery createQuery = session.createSQLQuery(query);

		for (Integer position : brandsMap.keySet()) {
			logger.info("position" + position + "position Value"
					+ brandsMap.get(position));
			createQuery.setString(position, brandsMap.get(position));
		}
		createQuery.addEntity(LocalBusinessEntity.class);

		List<LocalBusinessEntity> listOfName = (List<LocalBusinessEntity>) createQuery
				.list();
		for (LocalBusinessEntity localBusinessBean : listOfName) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			nameList.add(dto);
		}
		return nameList;

	}

	/**
	 * getBusinessInfo
	 * 
	 */
	public LocalBusinessDTO getBusinessInfo(Long id) {
		Session session = sessionFactory.getCurrentSession();

		LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();

		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("lblStoreId", id));

		LocalBusinessEntity bean = (LocalBusinessEntity) criteria
				.uniqueResult();

		BeanUtils.copyProperties(bean, localBusinessDTO);

		return localBusinessDTO;
	}

	/**
	 * saveBrandDetails
	 * 
	 */
	public boolean saveBrandDetails(String channelName, String brandName,
			Date startDate, String locationsInvoiced, String submisions) {

		Session session = sessionFactory.getCurrentSession();
		if (channelName.trim().length() > 0) {
			ChannelEntity channelEntity = new ChannelEntity();
			channelEntity.setChannelName(channelName);
			channelEntity.setStartDate(startDate);
			Serializable save = session.save(channelEntity);
			BrandEntity brandEntity = new BrandEntity();
			brandEntity.setBrandName(brandName);
			brandEntity.setChannelID(Integer.parseInt(save.toString()));
			brandEntity.setLocationsInvoiced(locationsInvoiced);
			brandEntity.setStartDate(startDate);
			brandEntity.setSubmisions(submisions);
			Serializable save2 = session.save(brandEntity);

			if (save2.toString() != null && save.toString() != null) {

				CustomSubmissions submissions = new CustomSubmissions();
				submissions.setBrandId(Integer.valueOf(save2.toString()));
				submissions.setBrandName(brandName);
				submissions.setChannelId(Integer.valueOf(save.toString()));
				submissions.setChannelName(channelName);
				session.save(submissions);

			}

		} else {
			ChannelEntity channelEntity = new ChannelEntity();
			channelEntity.setChannelName("Convergent Mobile");
			channelEntity.setStartDate(startDate);
			Serializable save = session.save(channelEntity);
			BrandEntity brandEntity = new BrandEntity();
			brandEntity.setBrandName(brandName);
			// brandEntity.setChannelID(Integer.parseInt(save.toString()));
			brandEntity.setLocationsInvoiced(locationsInvoiced);
			brandEntity.setStartDate(startDate);
			brandEntity.setSubmisions(submisions);
			Serializable save2 = session.save(brandEntity);

			if (save2.toString() != null && save.toString() != null) {

				CustomSubmissions submissions = new CustomSubmissions();
				submissions.setBrandId(Integer.valueOf(save2.toString()));
				submissions.setBrandName(brandName);
				submissions.setChannelId(Integer.valueOf(save.toString()));
				submissions.setChannelName(channelName);
				session.save(submissions);

			}
		}
		return true;
	}

	/**
	 * getDistributorInfo:: get distributor information based on brand
	 * 
	 */
	public List<BrandInfoDTO> getDistributorInfo(String brand) {
		List<BrandInfoDTO> list = new ArrayList<BrandInfoDTO>();
		String channelName = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brand));
		List<BrandEntity> brandList = criteria.list();
		if (brandList.size() > 0) {
			BrandEntity brandEntity = (BrandEntity) brandList.get(0);
			Integer channelID = brandEntity.getChannelID();
			criteria = session.createCriteria(ChannelEntity.class);
			criteria.add(Restrictions.eq("channelID", channelID));
			List<ChannelEntity> channelList = criteria.list();
			ChannelEntity channelEntity = (ChannelEntity) channelList.get(0);
			channelName = channelEntity.getChannelName();
		}
		for (BrandEntity brandEntity : brandList) {
			BrandInfoDTO brandInfoDTO = new BrandInfoDTO();
			brandInfoDTO.setBrandName(brandEntity.getBrandName());
			brandInfoDTO.setChannelName(channelName);
			brandInfoDTO.setLocationsInvoiced(brandEntity
					.getLocationsInvoiced());
			brandInfoDTO.setSubmisions(brandEntity.getSubmisions());
			brandInfoDTO.setStartDate(brandEntity.getStartDate());
			BeanUtils.copyProperties(brandEntity, brandInfoDTO);
			list.add(brandInfoDTO);
		}
		return list;
	}

	/**
	 * getListingActivityInf :: Gets Upload/export report information
	 */
	public List<ExportReportDTO> getListingActivityInf() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UploadReportEntity.class);
		Integer role = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<ExportReportDTO> exportReportList = new ArrayList<ExportReportDTO>();

		if (role == LBLConstants.CLIENT_ADMIN && userName != null) {

			Criteria userCriteria = session.createCriteria(UsersEntity.class);
			userCriteria.add(Restrictions.eq("userName", userName));
			List<UsersEntity> usersEntities = userCriteria.list();
			UsersEntity usersEntity = new UsersEntity();
			if (usersEntities != null && !usersEntities.isEmpty()) {
				usersEntity = usersEntities.get(0);
			}
			List<String> brand = new ArrayList<String>();
			List<Integer> listbrandID = new ArrayList<Integer>();
			Integer userID = usersEntity.getUserID();
			Criteria brandcriteria = session.createCriteria(User_brands.class);
			brandcriteria.add(Restrictions.eq("userID", userID));
			List<User_brands> list2 = brandcriteria.list();
			for (User_brands user_brands : list2) {
				Integer brandID = user_brands.getBrandID();
				listbrandID.add(brandID);

			}
			brand = getBrandById(listbrandID);

			if (brand.size() > 0)
				criteria.add(Restrictions.in("brand", brand));
		} else if ((role == LBLConstants.CHANNEL_ADMIN || role == LBLConstants.PURIST)
				&& userName != null) {

			/*
			 * String sql =
			 * "SELECT b.brandName from BrandEntity b where b.channelID= (SELECT u.channelID from UsersEntity u where u.userName=?)"
			 * ; Query query = session.createQuery(sql); query.setString(0,
			 * userName); List<String> list = query.list();
			 */
			List<String> allBrandNames = getAllBrandNames(false, userName);
			if (allBrandNames != null && allBrandNames.size() > 0)
				criteria.add(Restrictions.in("brand", allBrandNames));

		}

		List<UploadReportEntity> uploadReportList = criteria.list();
		for (UploadReportEntity uploadReportEntity : uploadReportList) {
			ExportReportDTO dto = new ExportReportDTO();
			dto.setExportID(uploadReportEntity.getId());
			dto.setCount(Integer.valueOf(uploadReportEntity
					.getNumberOfRecords()));
			Date myUploadedDate = uploadReportEntity.getDate();
			java.sql.Date sqlDate = new java.sql.Date(myUploadedDate.getTime());
			dto.setDate(sqlDate);
			dto.setBrandName(uploadReportEntity.getBrand());
			String activityDesc = "Uploaded to System";
			dto.setActivityDescription(activityDesc);
			exportReportList.add(dto);

		}

		Criteria exportCriteria = session
				.createCriteria(ExportReportEntity.class);
		if (role == LBLConstants.CLIENT_ADMIN && userName != null) {

			Criteria userCriteria = session.createCriteria(UsersEntity.class);
			userCriteria.add(Restrictions.eq("userName", userName));
			List<UsersEntity> usersEntities = userCriteria.list();
			UsersEntity usersEntity = new UsersEntity();
			if (usersEntities != null && !usersEntities.isEmpty()) {
				usersEntity = usersEntities.get(0);
			}
			List<String> brand = new ArrayList<String>();
			List<Integer> listbrandID = new ArrayList<Integer>();
			Integer userID = usersEntity.getUserID();
			Criteria brandcriteria = session.createCriteria(User_brands.class);
			brandcriteria.add(Restrictions.eq("userID", userID));
			List<User_brands> list2 = brandcriteria.list();
			for (User_brands user_brands : list2) {
				Integer brandID = user_brands.getBrandID();
				listbrandID.add(brandID);
			}
			brand = getBrandById(listbrandID);

			if (brand.size() > 0)
				exportCriteria.add(Restrictions.in("brandName", brand));

		} else if ((role == LBLConstants.CHANNEL_ADMIN || role == LBLConstants.PURIST)
				&& userName != null) {
			/*
			 * String sql =
			 * "SELECT b.brandName from BrandEntity b where b.channelID= (SELECT u.channelID from UsersEntity u where u.userName=?)"
			 * ; Query query = session.createQuery(sql); query.setString(0,
			 * userName); List<String> list = query.list();
			 */
			List<String> allBrandNames = getAllBrandNames(false, userName);
			if (allBrandNames != null && allBrandNames.size() > 0)
				exportCriteria.add(Restrictions.in("brandName", allBrandNames));

		}

		List<ExportReportEntity> exportReportEntities = exportCriteria.list();
		for (ExportReportEntity exportReportEntity : exportReportEntities) {
			ExportReportDTO exportReportDTO = new ExportReportDTO();
			exportReportDTO.setDate(new java.sql.Date(exportReportEntity
					.getDate().getTime()));
			exportReportDTO.setExportID(exportReportEntity.getExportID());
			String brandName = exportReportEntity.getBrandName();
			exportReportDTO.setBrandName(brandName);
			String partner = exportReportEntity.getPartner();
			String activityDesc = null;
			if (partner.contains("Template")) {
				String[] tempalteName = partner.split("Template");
				String temp = tempalteName[0];
				if (temp.contains("AcxiomCan")) {
					temp = "Acxiom Canada";
				} else if (temp.contains("AcxiomUS")) {
					temp = "Acxiom US";
				}
				partner = temp + " Template";

				activityDesc = "Export to " + partner;
			} else {
				activityDesc = "Distributed to " + partner;
			}

			exportReportDTO.setActivityDescription(activityDesc);
			exportReportDTO
					.setCount(exportReportEntity.getNumberOfRecords() != null ? Long
							.valueOf(exportReportEntity.getNumberOfRecords())
							.intValue() : 0);
			exportReportList.add(exportReportDTO);
		}
		return exportReportList;
	}

	/**
	 * U
	 * 
	 * saveChannel
	 * 
	 */

	public Integer saveChannel(String channelName, Date startDate,
			String imagePath) {

		Session session = sessionFactory.getCurrentSession();

		ChannelEntity channelEntity = new ChannelEntity();
		channelEntity.setChannelName(channelName);
		channelEntity.setStartDate(startDate);
		channelEntity.setImagePath(imagePath);
		Serializable save = session.save(channelEntity);

		return Integer.valueOf(save.toString());
	}

	/**
	 * updateChannel
	 * 
	 */

	public boolean updateChannel(String channelName, Date startDate,
			Integer channelID, String imagePath) {
		Session session = sessionFactory.getCurrentSession();
		ChannelEntity channelEntity = new ChannelEntity();
		channelEntity.setChannelID(channelID);
		channelEntity.setChannelName(channelName);
		channelEntity.setStartDate(startDate);
		channelEntity.setImagePath(imagePath);
		session.saveOrUpdate(channelEntity);
		return true;
	}

	/**
	 * saveBrand
	 */
	public boolean saveBrand(String brandName, Date startDate,
			String locationsInvoiced, String submisions, Integer channelID,
			String partnerActive, Integer clientId, int brandId) {
		Session session = sessionFactory.getCurrentSession();

		BrandEntity brandEntity = getBrandEntity(brandName, startDate,
				locationsInvoiced, submisions, channelID, partnerActive,
				clientId, brandId);
		Serializable save = session.save(brandEntity);

		if (save.toString() != null) {
			String channelName = getChannelNameById(channelID);
			CustomSubmissions customSubmissions = new CustomSubmissions();
			customSubmissions.setBrandId(brandId);
			customSubmissions.setBrandName(brandName);
			customSubmissions.setChannelId(channelID);
			customSubmissions.setChannelName(channelName);
			session.save(customSubmissions);
		}

		return save != null;
	}

	/**
	 * getBrandEntity
	 * 
	 * @param brandName
	 * @param startDate
	 * @param locationsInvoiced
	 * @param submisions
	 * @param channelID
	 * @return
	 */
	private BrandEntity getBrandEntity(String brandName, Date startDate,
			String locationsInvoiced, String submisions, Integer channelID,
			String partnerActive, Integer clientId, int brandId) {
		BrandEntity brandEntity = new BrandEntity();
		brandEntity.setBrandID(brandId);
		brandEntity.setBrandName(brandName);
		brandEntity.setChannelID(channelID);
		brandEntity.setStartDate(startDate);
		brandEntity.setLocationsInvoiced(locationsInvoiced);
		brandEntity.setSubmisions(submisions);
		brandEntity.setPartnerActive(partnerActive);
		brandEntity.setClientId(clientId);
		return brandEntity;
	}

	/**
	 * updateBrand
	 */
	public boolean updateBrand(Integer brandID, String brandName,
			Date startDate, String locationsInvoiced, String submisions,
			Integer channelID, String partnerActive, Integer clientId,
			Integer id) {
		Session session = sessionFactory.getCurrentSession();

		BrandEntity brandEntity = getBrandEntity(brandName, startDate,
				locationsInvoiced, submisions, channelID, partnerActive,
				clientId, brandID);
		brandEntity.setId(id);
		session.saveOrUpdate(brandEntity);

		if (brandEntity != null) {
			String channelName = getChannelNameById(channelID);
			String sql = "UPDATE CustomSubmissions SET brandId=" + brandID
					+ ",brandName=" + brandName + ",channelId=" + channelID
					+ ",channelName=" + channelName + " WHERE brandid=?";
			Query createQuery = session.createQuery(sql);
			createQuery.setInteger(0, brandID);
			createQuery.executeUpdate();

		}

		return true;
	}

	/**
	 * getBrandChannel:get channel based on brand
	 * 
	 */
	public String getBrandChannel(String brandName) {
		String channelName = "Convergent Mobile";
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brandName));
		List<BrandEntity> brandList = criteria.list();
		if (brandList.size() > 0) {
			BrandEntity brandEntity = (BrandEntity) brandList.get(0);
			Integer channelID = brandEntity.getChannelID();
			criteria = session.createCriteria(ChannelEntity.class);
			criteria.add(Restrictions.eq("channelID", channelID));
			List<ChannelEntity> channelList = criteria.list();
			ChannelEntity channelEntity = (ChannelEntity) channelList.get(0);
			channelName = channelEntity.getChannelName();
		}

		return channelName;
	}

	/**
	 * get list of channels
	 */

	public List<String> getChannels() {

		List<String> list = new ArrayList<String>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ChannelEntity.class);
		List<ChannelEntity> channelNameList = criteria.list();
		// criteria.setProjection(Projections.projectionList().add(Projections.property("clientName")));
		for (ChannelEntity channelNameEntity : channelNameList) {
			String channelName = channelNameEntity.getChannelName();
			list.add(channelName);

		}
		logger.info("channelnames list==" + list.size());
		return list;

	}

	/**
	 * checking the user based on username
	 * 
	 */

	public boolean isUserExistis(String userName) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UsersEntity.class);
		criteria.add(Restrictions.eq("userName", userName));
		List list = criteria.list();

		return list.size() > 0;
	}

	/**
	 * saveUser :: saves new user/update existing user
	 */
	public boolean saveUser(UsersBean bean) {
		Session session = sessionFactory.getCurrentSession();
		Integer channelId = 0;
		Integer roleId = bean.getRoleId();
		Integer role = (Integer) httpSession.getAttribute("roleId");
		logger.info("roleId::::::::" + role);
		if ((role == LBLConstants.CHANNEL_ADMIN || role == LBLConstants.PURIST)) {
			if (roleId == LBLConstants.CLIENT_ADMIN) {
				// channelId = getChannelIdByBrandId(bean.getBrandID());
				String userName = (String) httpSession.getAttribute("userName");
				channelId = getChannelIdByUserName(userName);
			} else {
				String channelName = bean.getChannelName();
				if (channelName != null && channelName.length() > 0) {
					Criteria criteria = session
							.createCriteria(ChannelEntity.class);
					criteria.add(Restrictions.eq("channelName", channelName));
					List<ChannelEntity> list = criteria.list();
					for (ChannelEntity channelEntity : list) {
						channelId = channelEntity.getChannelID();
					}
				}
			}

		} else if (role == LBLConstants.CLIENT_ADMIN) {
			String channelName = bean.getChannelName();
			if (channelName != null && channelName.length() > 0) {
				Criteria criteria = session.createCriteria(ChannelEntity.class);
				criteria.add(Restrictions.eq("channelName", channelName));
				List<ChannelEntity> list = criteria.list();
				for (ChannelEntity channelEntity : list) {
					channelId = channelEntity.getChannelID();
				}
			}
		} else {
			String channelName = bean.getChannelName();
			if (channelName != null && channelName.length() > 0) {
				Criteria criteria = session.createCriteria(ChannelEntity.class);
				criteria.add(Restrictions.eq("channelName", channelName));
				List<ChannelEntity> list = criteria.list();
				for (ChannelEntity channelEntity : list) {
					channelId = channelEntity.getChannelID();
				}
			}
		}

		UsersEntity entity = new UsersEntity();
		BeanUtils.copyProperties(bean, entity);
		if (entity.getUserID() == null) {
			Serializable save = session.save(entity);
			String[] brandID = bean.getBrandID();
			for (String string : brandID) {

				User_brands brands = new User_brands();
				brands.setUserID(Integer.valueOf(save.toString()));
				brands.setBrandID(Integer.parseInt(string));
				brands.setChannelID(channelId);
				session.save(brands);
			}
		} else {
			session.update(entity);

			String[] brandIds = bean.getBrandID();
			StringBuffer ids = new StringBuffer();

			for (int i = 0; i < brandIds.length; i++) {
				ids.append(brandIds[i]);

			}
			String id = ids.toString();

			if (!(id.length() == 0) && bean.getBrandID().length > 0) {
				String sql = "delete FROM User_brands WHERE userid = ?";
				Query query = session.createQuery(sql);
				query.setInteger(0, bean.getUserID());
				int executeUpdate = query.executeUpdate();
				if (executeUpdate > 0) {

					String[] brandID = bean.getBrandID();
					for (String string : brandID) {

						if (string.trim().length() != 0) {
							string = string.replaceAll("\\s+", "");
							if (string.contains("{")) {
								string = string.replace("{", "");
							}
							if (string.contains("}")) {
								string = string.replace("}", "");
							}
							String[] split = string.split(",");
							for (String barndId : split) {

								barndId = barndId.replaceAll("\\s+", "");
								User_brands brands = new User_brands();
								brands.setUserID(bean.getUserID());
								brands.setBrandID(Integer.parseInt(barndId));
								brands.setChannelID(channelId);
								session.save(brands);

							}

						}
					}
				} else {

					String[] brandID = bean.getBrandID();
					for (String string : brandID) {

						if (string.trim().length() != 0) {
							string = string.replaceAll("\\s+", "");
							if (string.contains("{")) {
								string = string.replace("{", "");
							}
							if (string.contains("}")) {
								string = string.replace("}", "");
							}
							String[] split = string.split(",");
							for (String barndId : split) {

								barndId = barndId.replaceAll("\\s+", "");
								User_brands brands = new User_brands();
								brands.setUserID(bean.getUserID());
								brands.setBrandID(Integer.parseInt(barndId));
								brands.setChannelID(channelId);
								session.save(brands);

							}

						}

					}

				}
			}
		}
		return true;
	}

	/**
	 * getChannelIdByBrandId
	 * 
	 * @param brandID
	 * @return
	 */
	public Integer getChannelIdByBrandId(Integer brandID) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandID", brandID));
		List<BrandEntity> list = criteria.list();
		int channelId = 1;
		if (list != null && !list.isEmpty()) {
			BrandEntity brandEntity = list.get(0);
			channelId = brandEntity.getChannelID();
		}
		return channelId;
	}

	/**
	 * getBrandInfoByBrandName
	 */
	public BrandInfoDTO getBrandInfoByBrandName(String brandName) {
		BrandInfoDTO brandInfoDTO = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brandName));
		// criteria.add(Restrictions.eq("partnerActive", "Y"));
		List<BrandEntity> list = criteria.list();
		if (list.size() > 0) {
			brandInfoDTO = new BrandInfoDTO();
			BrandEntity entity = (BrandEntity) list.get(0);
			BeanUtils.copyProperties(entity, brandInfoDTO);
			String submissionsStr = "";
			for (BrandEntity brandEntity : list) {
				if (brandEntity.getPartnerActive() == null
						|| brandEntity.getPartnerActive().equalsIgnoreCase("N")) {
					continue;
				}
				Date activeDate = brandInfoDTO.getActiveDate();
				Date date = null;
				if (activeDate != null) {
					date = DateUtil.getDate(activeDate);
				}
				submissionsStr += brandEntity.getSubmisions() + "_" + date
						+ ",";
			}
			Integer channelID = entity.getChannelID();
			String channelName = getChannelNameById(channelID);
			brandInfoDTO.setChannelName(channelName);
			brandInfoDTO.setInactive(entity.getInactive());
			brandInfoDTO.setSubmisions(submissionsStr == "" ? submissionsStr
					: submissionsStr.substring(0, submissionsStr.length() - 1));
		}
		return brandInfoDTO;
	}

	/**
	 * get channel information based channel id
	 */
	public ChannelNameDTO getchannelInfo(Integer chennalID) {
		ChannelNameDTO channelInfo = new ChannelNameDTO();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ChannelEntity.class);
		criteria.add(Restrictions.eq("channelID", chennalID));
		List list = criteria.list();
		ChannelEntity entity = (ChannelEntity) list.get(0);
		BeanUtils.copyProperties(entity, channelInfo);
		return channelInfo;
	}

	/**
	 * getChannelIdByUserName from users table
	 */
	public Integer getChannelIdByUserName(String userName) {
		Integer userID = 0;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UsersEntity.class);
		criteria.add(Restrictions.eq("userName", userName));
		List list = criteria.list();
		if (list.size() > 0) {
			UsersEntity userEntity = (UsersEntity) list.get(0);
			userID = userEntity.getUserID();
		}

		Criteria channelCriteria = session.createCriteria(User_brands.class);
		channelCriteria.add(Restrictions.eq("userID", userID));
		List<User_brands> list2 = channelCriteria.list();
		Integer channelID = null;
		for (User_brands usersEntity : list2) {
			channelID = usersEntity.getChannelID();
		}

		return channelID;
	}

	/**
	 * getAllUsersList based on role
	 */
	public List<UsersDTO> getAllUsersList(Integer roleId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UsersEntity.class);

		List<UsersDTO> userList = new ArrayList<UsersDTO>();

		List<UsersEntity> list = criteria.list();
		// for (UsersEntity usersEntity : list) {
		if (roleId == LBLConstants.CHANNEL_ADMIN
				|| roleId == LBLConstants.PURIST) {

			String userName = (String) httpSession.getAttribute("userName");

			Integer channelID = getChannelIdByUserName(userName);
			Criteria channelUserCriteria = session
					.createCriteria(User_brands.class);
			channelUserCriteria.add(Restrictions.eq("channelID", channelID));
			List<User_brands> channelUsers = channelUserCriteria.list();
			channelUsers = removeDuplicateUsers(channelUsers);
			for (User_brands channelUser : channelUsers) {

				Session userSession = sessionFactory.getCurrentSession();
				Criteria userCriteria = userSession
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userID",
						channelUser.getUserID()));
				List<UsersEntity> users = userCriteria.list();
				if (users.size() > 0) {
					UsersDTO usersDTO = new UsersDTO();
					BeanUtils.copyProperties(users.get(0), usersDTO);
					userList.add(usersDTO);
				}
			}
			// }

		} else {
			for (UsersEntity usersEntity : list) {
				UsersDTO usersDTO = new UsersDTO();
				BeanUtils.copyProperties(usersEntity, usersDTO);

				userList.add(usersDTO);
			}
		}
		return userList;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	static ArrayList<User_brands> removeDuplicateUsers(List<User_brands> list) {

		// Store unique items in result.
		ArrayList<User_brands> result = new ArrayList<User_brands>();
		Set<Integer> userIds = new HashSet<Integer>();

		for (int i = 0; i < list.size(); i++) {
			userIds.add(list.get(i).getUserID());
		}
		ArrayList<Integer> users = new ArrayList<Integer>(userIds);
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < users.size(); j++) {
				User_brands user_brands = list.get(i);

				Integer userId = users.get(j);
				if (user_brands.getUserID() == userId) {
					result.add(user_brands);
				}
			}
		}

		return result;
	}

	/**
	 * getUserByUserName
	 */
	public List<UsersDTO> getUserByUserName(String userName) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UsersEntity.class);

		List<UsersDTO> userList = new ArrayList<UsersDTO>();

		if (roleId == LBLConstants.CHANNEL_ADMIN
				|| roleId == LBLConstants.PURIST) {

			String user = (String) httpSession.getAttribute("userName");
			Integer userid = (Integer) httpSession.getAttribute("userID");
			Integer channelID = getChannelIdByUserName(user);

			Criteria channelUserCriteria = session
					.createCriteria(User_brands.class);
			// channelUserCriteria.add(Restrictions.eq("userID", userid));
			channelUserCriteria.add(Restrictions.eq("channelID", channelID));
			List<User_brands> channelUsers = channelUserCriteria.list();
			channelUsers = removeDuplicateUsers(channelUsers);

			List<Integer> userids = new ArrayList<Integer>();
			for (User_brands channelUser : channelUsers) {
				Integer userID2 = channelUser.getUserID();
				userids.add(userID2);
			}
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.like("userName", userName,
					MatchMode.ANYWHERE));
			disjunction.add(Restrictions.like("name", userName,
					MatchMode.ANYWHERE));
			disjunction.add(Restrictions.like("lastName", userName,
					MatchMode.ANYWHERE));
			criteria.add(disjunction);
			criteria.add(Restrictions.in("userID", userids));
			List<UsersEntity> users = criteria.list();
			if (users.size() > 0) {
				UsersEntity userEntity = (UsersEntity) users.get(0);
				UsersDTO usersDTO = new UsersDTO();
				BeanUtils.copyProperties(userEntity, usersDTO);
				userList.add(usersDTO);
			}

			// }

		} else {
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.like("userName", userName,
					MatchMode.ANYWHERE));
			disjunction.add(Restrictions.like("name", userName,
					MatchMode.ANYWHERE));
			disjunction.add(Restrictions.like("lastName", userName,
					MatchMode.ANYWHERE));
			criteria.add(disjunction);
			List<UsersEntity> list = criteria.list();
			for (UsersEntity usersEntity : list) {
				UsersDTO usersDTO = new UsersDTO();
				BeanUtils.copyProperties(usersEntity, usersDTO);

				userList.add(usersDTO);
			}
		}
		return userList;
	}

	/**
	 * get user details based on user id
	 */
	public List<UsersDTO> userInfo(Integer userID) {

		Session session = sessionFactory.getCurrentSession();
		String channelName = null;
		Integer channelID2 = null;
		Integer brandId = 0;
		List<String> brandIds = new ArrayList<String>();
		Criteria criteria = session.createCriteria(UsersEntity.class);
		criteria.add(Restrictions.eq("userID", userID));
		List<UsersDTO> userList = new ArrayList<UsersDTO>();
		List<UsersEntity> list = criteria.list();
		for (UsersEntity usersEntity : list) {

			Criteria Criteria = session.createCriteria(User_brands.class);
			Criteria.add(Restrictions.eq("userID", userID));
			List<User_brands> channelid = Criteria.list();
			for (User_brands user_brands : channelid) {
				channelID2 = user_brands.getChannelID();
				brandIds.add(String.valueOf(user_brands.getBrandID()));

			}

			Criteria createCriteria = session
					.createCriteria(ChannelEntity.class);
			createCriteria.add(Restrictions.eq("channelID", channelID2));
			List<ChannelEntity> list2 = createCriteria.list();
			for (ChannelEntity channelEntity : list2) {
				channelName = channelEntity.getChannelName();
			}
			UsersDTO usersDTO = new UsersDTO();
			usersDTO.setChannelName(channelName);
			String[] brandIdArray = new String[brandIds.size()];
			for (int i = 0; i < brandIds.size(); i++) {
				brandIdArray[i] = brandIds.get(i);
			}
			usersDTO.setBrandID(brandIdArray);
			BeanUtils.copyProperties(usersEntity, usersDTO);

			userList.add(usersDTO);
		}

		return userList;
	}

	/**
	 * get brand names
	 */
	public List<BrandInfoDTO> getClientNames() {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.addOrder(Order.asc("brandName"));
		List<BrandInfoDTO> brandList = new ArrayList<BrandInfoDTO>();
		List<BrandEntity> list = criteria.list();
		List<Integer> brandIdList = new ArrayList<Integer>();
		for (BrandEntity brandEntity : list) {
			BrandInfoDTO brandInfoDTO = new BrandInfoDTO();
			BeanUtils.copyProperties(brandEntity, brandInfoDTO);
			if (brandIdList.contains(brandInfoDTO.getBrandID())) {
				continue;
			}
			brandIdList.add(brandInfoDTO.getBrandID());
			brandList.add(brandInfoDTO);
		}
		return brandList;
	}

	/**
	 * getAllBrandNames
	 */
	public List<String> getAllBrandNames() {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT DISTINCT brandName FROM BrandEntity ORDER BY brandName";
		Query createQuery = session.createQuery(sql);
		List<String> brandNames = createQuery.list();
		return brandNames;
	}

	/**
	 * getAllBrandNames
	 */
	public List<String> getAllBrandNames(boolean isSuperAdmin, String userName) {

		if (isSuperAdmin) {
			return getAllBrandNames();
		}

		Session session = sessionFactory.getCurrentSession();
		Criteria userCriteria = session.createCriteria(UsersEntity.class);
		userCriteria.add(Restrictions.eq("userName", userName));
		List<UsersEntity> usersEntities = userCriteria.list();
		UsersEntity usersEntity = new UsersEntity();
		if (usersEntities != null && !usersEntities.isEmpty()) {
			usersEntity = usersEntities.get(0);
		}
		// String brand = null;
		List<Integer> listbrandID = new ArrayList<Integer>();
		Integer userID = usersEntity.getUserID();
		Criteria criteria = session.createCriteria(User_brands.class);
		criteria.add(Restrictions.eq("userID", userID));
		List<User_brands> list2 = criteria.list();
		for (User_brands user_brands : list2) {
			Integer brandID = user_brands.getBrandID();
			listbrandID.add(brandID);
		}
		List<String> allBrandNames = new ArrayList<String>();

		if (listbrandID.size() > 0) {
			allBrandNames = getBrandById(listbrandID);
		}

		return allBrandNames;
	}

	/**
	 * getBrandById
	 */
	public List<String> getBrandById(List<Integer> brandId) {
		List<String> brandnames = new ArrayList<String>();
		if (!brandId.isEmpty()) {
			Session session = sessionFactory.getCurrentSession();

			Set<String> brands = new HashSet<String>();
			Criteria createCriteria = session.createCriteria(BrandEntity.class);
			createCriteria.add(Restrictions.in("brandID", brandId));
			List<BrandEntity> list = createCriteria.list();
			for (BrandEntity brandEntity : list) {
				String brandName = brandEntity.getBrandName();
				brands.add(brandName);

			}
			brandnames.addAll(brands);
		}
		return brandnames;

	}

	/**
	 * get list of states
	 */
	public List<LocalBusinessDTO> getStates() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		List<LocalBusinessDTO> stateList = new ArrayList<LocalBusinessDTO>();
		List<LocalBusinessEntity> list = criteria.list();
		for (LocalBusinessEntity businessBean : list) {
			LocalBusinessDTO businessDTO = new LocalBusinessDTO();
			BeanUtils.copyProperties(businessBean, businessDTO);
			stateList.add(businessDTO);
		}
		return stateList;
	}

	public List<StatesListEntity> getStateList() {
		List<StatesListEntity> statelist = new ArrayList<StatesListEntity>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(StatesListEntity.class);

		List<StatesListEntity> list = criteria.list();
		for (StatesListEntity sates : list) {
			statelist.add(sates);
		}

		return statelist;
	}

	public Set<LocalBusinessDTO> getUniqueStates() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		Set<LocalBusinessDTO> stateList = new LinkedHashSet<LocalBusinessDTO>();
		Set<LocalBusinessEntity> list = (Set<LocalBusinessEntity>) criteria
				.list();
		for (LocalBusinessEntity businessBean : list) {
			LocalBusinessDTO businessDTO = new LocalBusinessDTO();
			BeanUtils.copyProperties(businessBean, businessDTO);
			stateList.add(businessDTO);
		}
		return stateList;
	}

	/**
	 * get list of stores
	 */
	public List<LocalBusinessDTO> getStore() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		List<LocalBusinessDTO> storeList = new ArrayList<LocalBusinessDTO>();
		List<LocalBusinessEntity> list = criteria.list();
		for (LocalBusinessEntity businessBean : list) {
			LocalBusinessDTO businessDTO = new LocalBusinessDTO();
			BeanUtils.copyProperties(businessBean, businessDTO);
			storeList.add(businessDTO);
		}
		return storeList;
	}

	/**
	 * getChannelBasedClients:: get brand names based on channel name
	 */
	public List<BrandInfoDTO> getChannelBasedClients(String channelName) {
		List<BrandInfoDTO> brandList = new ArrayList<BrandInfoDTO>();
		Session session = sessionFactory.getCurrentSession();
		String userName = (String) httpSession.getAttribute("userName");
		Criteria userCriteria = session.createCriteria(UsersEntity.class);
		userCriteria.add(Restrictions.eq("userName", userName));
		List<UsersEntity> usersEntities = userCriteria.list();
		UsersEntity usersEntity = new UsersEntity();
		if (usersEntities != null && !usersEntities.isEmpty()) {
			usersEntity = usersEntities.get(0);
		}
		// String brand = null;
		List<Integer> brandIdList = new ArrayList<Integer>();
		List<Integer> listbrandID = new ArrayList<Integer>();
		Integer userID = usersEntity.getUserID();
		Criteria criteria = session.createCriteria(User_brands.class);
		criteria.add(Restrictions.eq("userID", userID));
		List<User_brands> list2 = criteria.list();
		for (User_brands user_brands : list2) {
			Integer brandID = user_brands.getBrandID();
			listbrandID.add(brandID);
		}
		List<String> allBrandNames = new ArrayList<String>();
		if (listbrandID.size() > 0) {

			if (!listbrandID.isEmpty()) {
				Session session1 = sessionFactory.getCurrentSession();

				// Set<BrandInfoDTO> brands = new HashSet<BrandInfoDTO>();
				Criteria createCriteria = session1
						.createCriteria(BrandEntity.class);
				createCriteria.add(Restrictions.in("brandID", listbrandID));
				List<BrandEntity> list = createCriteria.list();

				for (BrandEntity brandEntity : list) {
					String brandName = brandEntity.getBrandName();
					BrandInfoDTO brandInfoDTO = new BrandInfoDTO();
					brandInfoDTO.setBrandName(brandName);
					BeanUtils.copyProperties(brandEntity, brandInfoDTO);
					if (brandIdList.contains(brandInfoDTO.getBrandID())) {
						continue;
					}
					brandIdList.add(brandInfoDTO.getBrandID());
					brandList.add(brandInfoDTO);
				}

			}
		}
		logger.info("client brand names:::" + brandList.size());
		return brandList;
	}

	/**
	 * locationUploadedCount:: brands count on upload based on brand name
	 */
	public Integer locationUploadedCount(String brandName) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("client", brandName));
		// criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("brands")));

		List<LocalBusinessEntity> criteriaList = criteria.list();
		int count = criteriaList.size();
		logger.info("list size == " + count);
		return count;
	}

	/**
	 * get list of Channels
	 * 
	 */
	public List<String> getChannels(String channelName) {

		List<String> list = new ArrayList<String>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ChannelEntity.class);
		criteria.add(Restrictions.eq("channelName", channelName));
		List<ChannelEntity> list2 = criteria.list();
		for (ChannelEntity channelEntity : list2) {
			String channelName2 = channelEntity.getChannelName();
			list.add(channelName2);
		}
		return list;
	}

	/**
	 * get channels based on user id
	 */
	public List<String> getChannelsBasedOnUser(Integer userID) {
		List<String> list = new ArrayList<String>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User_brands.class);
		criteria.add(Restrictions.eq("userID", userID));
		List<User_brands> list2 = criteria.list();
		Integer channelName = null;
		for (User_brands usersEntity : list2) {
			channelName = usersEntity.getChannelID();
		}
		Criteria criteria1 = session.createCriteria(ChannelEntity.class);
		criteria1.add(Restrictions.eq("channelID", channelName));
		List<ChannelEntity> list3 = criteria1.list();
		for (ChannelEntity channelEntity : list3) {
			String channelName2 = channelEntity.getChannelName();
			list.add(channelName2);
		}
		return list;
	}

	/**
	 * getBrandAllSubmissions
	 */
	public List<String> getBrandAllSubmissions(String brandName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brandName));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("submisions"));
		criteria.setProjection(projectionList);
		List list = criteria.list();
		return list;
	}

	/**
	 * getAllActiveBrands
	 */
	public Map<String, List<BrandInfoDTO>> getAllActiveBrands() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("partnerActive", "Y"));
		List<BrandEntity> allActiveBrands = criteria.list();
		DAOUtil daoUtil = new DAOUtil();
		return daoUtil.getBrandsMap(allActiveBrands);
	}

	/**
	 * getListOfBussinessByBrandName
	 */
	public List<LocalBusinessDTO> getListOfBussinessByBrandName(String brandName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("client", brandName));
		criteria.addOrder(Order.asc("store"));
		List<LocalBusinessEntity> businessEntities = criteria.list();
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		for (LocalBusinessEntity localBusinessEntity : businessEntities) {
			LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();

			BeanUtils.copyProperties(localBusinessEntity, localBusinessDTO);
			businessDTOs.add(localBusinessDTO);
		}
		return businessDTOs;
	}

	public List<LocalBusinessDTO> getStoresByGMBAccount(String googleAccountId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("googleAccountId", googleAccountId));
		criteria.addOrder(Order.asc("store"));
		List<LocalBusinessEntity> businessEntities = criteria.list();
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		for (LocalBusinessEntity localBusinessEntity : businessEntities) {
			LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();

			BeanUtils.copyProperties(localBusinessEntity, localBusinessDTO);
			businessDTOs.add(localBusinessDTO);
		}
		return businessDTOs;
	}

	/**
	 * updateBrandWithActiveDate
	 */
	public void updateBrandWithActiveDate(BrandInfoDTO brandInfoDTO) {
		Session session = sessionFactory.getCurrentSession();
		BrandEntity brandEntity = new BrandEntity();
		BeanUtils.copyProperties(brandInfoDTO, brandEntity);
		session.update(brandEntity);
	}

	/**
	 * getChannelById
	 */
	public ChannelNameDTO getChannelById(Integer channelID) {
		Session session = sessionFactory.getCurrentSession();
		ChannelEntity channelEntity = (ChannelEntity) session.get(
				ChannelEntity.class, channelID);
		if (channelEntity != null) {
			ChannelNameDTO channelNameDTO = new ChannelNameDTO();
			BeanUtils.copyProperties(channelEntity, channelNameDTO);
			return channelNameDTO;
		}
		return null;
	}

	/**
	 * saveExpostInfo
	 */
	public void saveExpostInfo(ExportReportEntity exportReportEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.save(exportReportEntity);
	}

	public boolean exportReportDTO(List<ExportReportDTO> exportReports) {
		Session session = sessionFactory.getCurrentSession();
		for (ExportReportDTO exportReportDTO : exportReports) {
			Integer count = exportReportDTO.getCount();
			ExportReportEntity entity = new ExportReportEntity();
			BeanUtils.copyProperties(exportReportDTO, entity);
			entity.setNumberOfRecords(Long.valueOf(count));
			Serializable save = session.save(entity);
		}
		return true;
	}

	/**
	 * getListingActivityInfByBrand::get listing activity information based on
	 * brand and selecter date range
	 */
	public List<ExportReportDTO> getListingActivityInfByBrand(
			LocalBusinessDTO businessDTO) {

		Integer role = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UploadReportEntity.class);
		Criteria exportCriteria = session
				.createCriteria(ExportReportEntity.class);

		if (businessDTO.getBrands() != null
				&& businessDTO.getBrands().length() > 1) {
			String brandName = businessDTO.getBrands();
			brandName = brandName.contains(",") ? brandName.replace(",", "")
					: brandName;
			criteria.add(Restrictions.like("brand", "%" + brandName + "%"));
			exportCriteria.add(Restrictions.like("brandName", "%" + brandName
					+ "%"));
		}
		String dateRange = businessDTO.getDateRange();
		if (!dateRange.equalsIgnoreCase(LBLConstants.DATE_RANGE_ALL)) {
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			int currentYear = cal.get(Calendar.YEAR);
			int curentMonth = cal.get(Calendar.MONTH) + 1;
			int previousMonth = curentMonth;
			int previousYear = currentYear;
			if (dateRange.equalsIgnoreCase(LBLConstants.DATE_RANGE_LAST_MONTH)) {
				--previousMonth;
			} else if (dateRange
					.equalsIgnoreCase(LBLConstants.DATE_RANGE_LAST_3_MONTHS)) {
				previousMonth -= 3;
			}
			if (previousMonth <= 0) {
				previousMonth = 12 - previousMonth;
				--previousYear;
			}

			String startDateStr = previousYear + "/" + previousMonth + "/01";
			String currentDateStr = currentYear + "/" + curentMonth + "/01";
			Date startDate = DateUtil.getDate("MM/dd/yyyy HH:mm:ss", new Date(
					startDateStr));
			Date currentDate = DateUtil.getDate("MM/dd/yyyy HH:mm:ss",
					new Date(currentDateStr));
			if (curentMonth == previousMonth) {
				criteria.add(Restrictions.ge("date", currentDate));
				exportCriteria.add(Restrictions.ge("date", currentDate));
			} else {
				criteria.add(Restrictions.between("date", startDate,
						currentDate));
				exportCriteria.add(Restrictions.between("date", startDate,
						currentDate));
			}
		}
		// criteria.add(Restrictions.);
		List<ExportReportDTO> exportReportList = new ArrayList<ExportReportDTO>();

		if (role == LBLConstants.CLIENT_ADMIN && userName != null) {

			/*
			 * String sql =
			 * "SELECT b.brandName from BrandEntity b where b.brandID=(SELECT u.brandID from UsersEntity u where u.userName=?)"
			 * ; Query query = session.createQuery(sql); query.setString(0,
			 * userName); List<String> list = query.list(); String brandName =
			 * ""; if (list != null && !list.isEmpty()) { brandName =
			 * list.get(0); }
			 */
			List<String> allBrandNames = getAllBrandNames(false, userName);
			if (allBrandNames != null && allBrandNames.size() > 1)
				criteria.add(Restrictions.in("brand", allBrandNames));
		} else if ((role == LBLConstants.CHANNEL_ADMIN || role == LBLConstants.PURIST)
				&& userName != null) {

			List<String> allBrandNames = getAllBrandNames(false, userName);
			/*
			 * String sql =
			 * "SELECT b.brandName from BrandEntity b where b.channelID= (SELECT u.channelID from UsersEntity u where u.userName=?)"
			 * ; Query query = session.createQuery(sql); query.setString(0,
			 * userName); List<String> list = query.list();
			 */
			if (allBrandNames != null && allBrandNames.size() > 0)
				criteria.add(Restrictions.in("brand", allBrandNames));

		}

		/*
		 * if (!(role == LBLConstants.CONVERGENT_MOBILE_ADMIN) && userName !=
		 * null) { String sql =
		 * "SELECT b.brandName from BrandEntity b where b.brandID=(SELECT u.brandID from UsersEntity u where u.userName=?)"
		 * ; Query query = session.createQuery(sql); query.setString(0,
		 * userName); List<String> list = query.list(); String brandName = "";
		 * if (list != null && !list.isEmpty()) { brandName = list.get(0); } if
		 * (brandName != "") criteria.add(Restrictions.eq("brand", brandName));
		 * }
		 */

		List<UploadReportEntity> uploadReportList = criteria.list();
		for (UploadReportEntity uploadReportEntity : uploadReportList) {
			ExportReportDTO dto = new ExportReportDTO();
			dto.setExportID(uploadReportEntity.getId());
			dto.setCount(Integer.valueOf(uploadReportEntity
					.getNumberOfRecords()));
			Date myUploadedDate = uploadReportEntity.getDate();
			java.sql.Date sqlDate = new java.sql.Date(myUploadedDate.getTime());
			dto.setDate(sqlDate);
			dto.setBrandName(uploadReportEntity.getBrand());
			String activityDesc = "Uploaded to System";
			dto.setActivityDescription(activityDesc);
			exportReportList.add(dto);
		}

		/*
		 * if (!(role == LBLConstants.CONVERGENT_MOBILE_ADMIN) && userName !=
		 * null) { String sql =
		 * "SELECT b.brandName from BrandEntity b where b.brandID=(SELECT u.brandID from UsersEntity u where u.userName=?)"
		 * ; Query query = session.createQuery(sql); query.setString(0,
		 * userName); List<String> list = query.list(); String brandName = "";
		 * if (list != null && !list.isEmpty()) { brandName = list.get(0); } if
		 * (brandName != "") exportCriteria.add(Restrictions.eq("brandName",
		 * brandName)); }
		 */

		if (role == LBLConstants.CLIENT_ADMIN && userName != null) {
			/*
			 * String sql =
			 * "SELECT b.brandName from BrandEntity b where b.brandID=(SELECT u.brandID from UsersEntity u where u.userName=?)"
			 * ; Query query = session.createQuery(sql); query.setString(0,
			 * userName); List<String> list = query.list(); String brandName =
			 * ""; if (list != null && !list.isEmpty()) { brandName =
			 * list.get(0); } if (brandName != "")
			 */
			List<String> allBrandNames = getAllBrandNames(false, userName);
			if (allBrandNames != null && allBrandNames.size() > 0)

				exportCriteria.add(Restrictions.in("brandName", allBrandNames));

		} else if ((role == LBLConstants.CHANNEL_ADMIN || role == LBLConstants.PURIST)
				&& userName != null) {

			List<String> allBrandNames = getAllBrandNames(false, userName);
			/*
			 * String sql =
			 * "SELECT b.brandName from BrandEntity b where b.channelID= (SELECT u.channelID from UsersEntity u where u.userName=?)"
			 * ; Query query = session.createQuery(sql); query.setString(0,
			 * userName); List<String> list = query.list();
			 */
			if (allBrandNames != null && allBrandNames.size() > 0)
				exportCriteria.add(Restrictions.in("brandName", allBrandNames));

		}
		List<ExportReportEntity> exportReportEntities = exportCriteria.list();
		for (ExportReportEntity exportReportEntity : exportReportEntities) {
			ExportReportDTO exportReportDTO = new ExportReportDTO();
			exportReportDTO.setDate(new java.sql.Date(exportReportEntity
					.getDate().getTime()));
			exportReportDTO.setExportID(exportReportEntity.getExportID());
			String brand = exportReportEntity.getBrandName();
			exportReportDTO.setBrandName(brand);
			// String activityDesc = "Exported to " +
			// exportReportEntity.getPartner();

			String partner = exportReportEntity.getPartner();
			// String activityDesc = "Exported to " +
			// exportReportEntity.getPartner();
			String activityDesc = null;
			if (partner.contains("Template")) {
				String[] tempalteName = partner.split("Template");
				String temp = tempalteName[0];
				if (temp.contains("AcxiomCan")) {
					temp = "Acxiom Canada";
				} else if (temp.contains("AcxiomUS")) {
					temp = "Acxiom US";
				}
				partner = temp + " Template";
				activityDesc = "Export to " + partner;
			} else {
				activityDesc = "Distributed to " + partner;
			}

			exportReportDTO.setActivityDescription(activityDesc);
			exportReportDTO
					.setCount(exportReportEntity.getNumberOfRecords() != null ? Long
							.valueOf(exportReportEntity.getNumberOfRecords())
							.intValue() : 0);
			exportReportList.add(exportReportDTO);
		}
		return exportReportList;
	}

	/**
	 * addLocation
	 */
	public int addLocation(LocalBusinessDTO localBusinessDTO) {
		Session session = sessionFactory.getCurrentSession();
		int id = 0;
		Date date = new Date();
		if (localBusinessDTO != null) {
			LocalBusinessEntity localBusinessEntity = new LocalBusinessEntity();
			BeanUtils.copyProperties(localBusinessDTO, localBusinessEntity);
			localBusinessEntity.setUploadedTime(date);
			id = (Integer) session.save(localBusinessEntity);
		}

		return id;
	}

	/**
	 * getRoles
	 */
	public List<RoleEntity> getRoles() {
		List<RoleEntity> rolesList = new ArrayList<RoleEntity>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RoleEntity.class);
		List<RoleEntity> list = criteria.list();
		for (RoleEntity roleEntity : list) {
			if (roleEntity.getRoleId() == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
				continue;
			}
			rolesList.add(roleEntity);
		}

		return rolesList;
	}

	/**
	 * getForgotPassword based on userName
	 */
	public UsersDTO getForgotPassword(String userName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UsersEntity.class);
		criteria.add(Restrictions.eq("userName", userName));
		List<UsersEntity> list = criteria.list();
		UsersDTO dto = new UsersDTO();
		for (UsersEntity usersEntity : list) {
			BeanUtils.copyProperties(usersEntity, dto);
		}

		return dto;
	}

	/**
	 * addOrUpdateForgotPWD
	 */
	public void addOrUpdateForgotPWD(ForgotPasswordDto forgotPasswordDto) {
		Session session = sessionFactory.getCurrentSession();
		ForgotPasswordEntity forgotPassEntity = new ForgotPasswordEntity();
		BeanUtils.copyProperties(forgotPasswordDto, forgotPassEntity);
		if (forgotPassEntity.getId() != null) {
			session.merge(forgotPassEntity);
		} else {
			session.save(forgotPassEntity);
		}
	}

	/**
	 * getForgotPwdDtoByEmail
	 */
	public ForgotPasswordDto getForgotPwdDtoByEmail(String email) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ForgotPasswordEntity.class);
		criteria.add(Restrictions.eq("email", email));
		List<ForgotPasswordEntity> forgotPasswordEntities = criteria.list();
		if (forgotPasswordEntities != null && !forgotPasswordEntities.isEmpty()) {
			ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
			ForgotPasswordEntity forgotPasswordEntity = forgotPasswordEntities
					.get(0);
			BeanUtils.copyProperties(forgotPasswordEntity, forgotPasswordDto);
			return forgotPasswordDto;
		}
		return null;
	}

	/**
	 * updateUserPassword
	 */
	public boolean updateUserPassword(UsersBean usersBean) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UsersEntity.class);
		criteria.add(Restrictions.eq("email", usersBean.getEmail()));
		List<UsersEntity> userEntities = criteria.list();
		if (userEntities != null && !userEntities.isEmpty()) {
			UsersEntity usersEntity = userEntities.get(0);
			usersEntity.setConfirmPassWord(usersBean.getConfirmPassWord());
			usersEntity.setPassWord(usersBean.getPassWord());
			session.update(usersEntity);
			return true;
		}
		return false;
	}

	/**
	 * getForgotPassByToken
	 */
	public ForgotPasswordDto getForgotPassByToken(String tocken) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ForgotPasswordEntity.class);
		criteria.add(Restrictions.eq("emailCode", tocken));
		List<ForgotPasswordEntity> forgotPasswordEntities = criteria.list();
		ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
		if (forgotPasswordEntities != null && !forgotPasswordEntities.isEmpty()) {
			ForgotPasswordEntity forgotPasswordEntity = forgotPasswordEntities
					.get(0);
			BeanUtils.copyProperties(forgotPasswordEntity, forgotPasswordDto);
		}
		return forgotPasswordDto;
	}

	/**
	 * getListOfBusinessInfo by uploadJobId
	 */
	public List<LocalBusinessDTO> getListOfBusinessInfo(String uploadJobId) {
		List<LocalBusinessDTO> listofInfo = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("uploadJobId", uploadJobId));
		List<LocalBusinessEntity> list = criteria.list();
		for (LocalBusinessEntity localBusinessEntity : list) {
			LocalBusinessDTO businessDTO = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessEntity, businessDTO);
			listofInfo.add(businessDTO);
		}
		return listofInfo;
	}

	/**
	 * isStoreUnique
	 */
	public boolean isStoreUnique(String store, Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("clientId", clientId));
		List<LocalBusinessEntity> businessEntities = criteria.list();
		return (businessEntities != null && !(businessEntities.isEmpty()));
	}

	/**
	 * insertErrorBusiness information
	 */
	public boolean insertErrorBusiness(List<LblErrorDTO> inCorrectData,
			Date date, String uploadJobId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria errorCriteria = session.createCriteria(LblErrorEntity.class);

		for (LblErrorDTO lblErrorDTO : inCorrectData) {
			// logger.info(" lblErrorDTO length :: "
			// + lblErrorDTO.getErrorMessage().length());
			errorCriteria.add(Restrictions.eq("clientId",
					lblErrorDTO.getClientId()));
			errorCriteria.add(Restrictions.eq("store", lblErrorDTO.getStore()));
			List<LblErrorEntity> errorsList = errorCriteria.list();

			if ((errorsList != null && !errorsList.isEmpty())) {
				for (LblErrorEntity entity : errorsList) {
					session.delete(entity);
				}
			}

			LblErrorEntity lblErrorEntity = new LblErrorEntity();
			BeanUtils.copyProperties(lblErrorDTO, lblErrorEntity);
			lblErrorEntity.setUploadedTime(date);
			lblErrorEntity.setUploadJobId(uploadJobId);
			try {
				Serializable save = session.save(lblErrorEntity);
			} catch (Exception e) {
				logger.error("There was error while inserting error listing: ",
						e);
				e.printStackTrace();
			}

		}

		return true;

	}

	/**
	 * getClientNameById
	 */

	/*
	 * public BrandInfoDTO getClientNameById(Integer clientUserId) { Session
	 * session = sessionFactory.getCurrentSession(); Criteria criteria =
	 * session.createCriteria(User_brands.class);
	 * criteria.add(Restrictions.eq("userID", clientUserId)); BrandInfoDTO
	 * brandInfoDTO = new BrandInfoDTO(); String brandName = null;
	 * List<User_brands> list = criteria.list(); for (User_brands user_brands :
	 * list) { Integer brandID = user_brands.getBrandID(); Criteria
	 * createCriteria = session.createCriteria(BrandEntity.class);
	 * createCriteria.add(Restrictions.eq("brandID", brandID));
	 * List<BrandEntity> list2 = createCriteria.list(); for (BrandEntity
	 * brandEntity : list2) { brandName = brandEntity.getBrandName(); }
	 * brandInfoDTO.setBrandName(brandName);
	 * 
	 * } return brandInfoDTO; }
	 */

	/**
	 * getErrorBusinessInfo by id
	 */

	public LblErrorDTO getErrorBusinessInfo(Long lblStoreID) {

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(LblErrorEntity.class);
		criteria.add(Restrictions.eq("lblStoreId", lblStoreID));

		LblErrorEntity bean = (LblErrorEntity) criteria.uniqueResult();
		LblErrorDTO lblErrorDTO = new LblErrorDTO();
		BeanUtils.copyProperties(bean, lblErrorDTO);

		return lblErrorDTO;

	}

	public LblErrorEntity getErrorBusinessEntity(Long lblStoreID) {

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(LblErrorEntity.class);
		criteria.add(Restrictions.eq("lblStoreId", lblStoreID));

		LblErrorEntity bean = (LblErrorEntity) criteria.uniqueResult();

		return bean;

	}

	public void updateErrorBusinessInfo(LblErrorDTO businessInfoDto,
			Integer listingId) {
		Session session = sessionFactory.getCurrentSession();
		LocalBusinessEntity businessEntity = new LocalBusinessEntity();
		BeanUtils.copyProperties(businessInfoDto, businessEntity);

		/*
		 * LocalBusinessEntity entity = (LocalBusinessEntity) session
		 * .createCriteria(LocalBusinessEntity.class)
		 * .add(Restrictions.eq("store",
		 * businessInfoDto.getStore())).add(Restrictions.eq("clientId",
		 * businessInfoDto.getClientId())).uniqueResult();
		 */

		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("clientId", businessInfoDto.getClientId()));
		criteria.add(Restrictions.eq("lblStoreId",
				businessInfoDto.getLblStoreId()));
		List<LocalBusinessEntity> list = criteria.list();

		if ((list != null && !list.isEmpty())) {
			for (LocalBusinessEntity entity : list) {
				session.delete(entity);
			}
		}

		Criteria errorCriteria = session.createCriteria(LblErrorEntity.class);
		errorCriteria.add(Restrictions.eq("clientId",
				businessInfoDto.getClientId()));
		errorCriteria.add(Restrictions.eq("lblStoreId",
				businessInfoDto.getLblStoreId()));
		List<LblErrorEntity> errorsList = errorCriteria.list();

		if ((errorsList != null && !errorsList.isEmpty())) {
			for (LblErrorEntity entity : errorsList) {
				session.delete(entity);
			}
		}

		/*
		 * LblErrorEntity errorEntity = new LblErrorEntity();
		 * BeanUtils.copyProperties(businessInfoDto, errorEntity);
		 * session.delete(errorEntity);
		 */

	}

	/**
	 * saveErrorBusinessInfo
	 */
	public boolean saveErrorBusinessInfo(LblErrorDTO businessDTO) {
		Session session = sessionFactory.getCurrentSession();
		LocalBusinessEntity businessEntity = new LocalBusinessEntity();
		BeanUtils.copyProperties(businessDTO, businessEntity);
		session.save(businessEntity);
		/*
		 * LblErrorEntity errorEntity = new LblErrorEntity();
		 * BeanUtils.copyProperties(businessDTO, errorEntity);
		 * session.delete(errorEntity);
		 */
		return true;
	}

	/**
	 * getListingId
	 */
	public Integer getListingId(LblErrorDTO businessDTO) {
		Session session = sessionFactory.getCurrentSession();

		LblErrorEntity bean = (LblErrorEntity) session
				.createCriteria(LblErrorEntity.class)
				.add(Restrictions.eq("clientId", businessDTO.getClientId()))
				.add(Restrictions.eq("store", businessDTO.getStore()))
				.uniqueResult();
		;

		Integer id = 0;
		if (bean != null) {
			id = bean.getId();
		}
		return id;
	}

	/**
	 * updateListingError
	 */
	public boolean updateListingError(LblErrorDTO businessDTO) {
		Session session = sessionFactory.getCurrentSession();

		LblErrorEntity errorEntity = new LblErrorEntity();
		BeanUtils.copyProperties(businessDTO, errorEntity);
		session.saveOrUpdate(errorEntity);
		return true;
	}

	/**
	 * checking the state isValid or not
	 */
	public boolean isValidState(String state, String countryCode) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(StatesListEntity.class);
		criteria.add(Restrictions.eq("code", state));
		criteria.add(Restrictions.eq("country", countryCode));
		List<StatesListEntity> list = criteria.list();
		return (list != null && !list.isEmpty());
	}

	/**
	 * checking the category is Exist or not
	 */
	public boolean isCatagoryExist(String category) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CategoryEntity.class);
		criteria.add(Restrictions.eq("categoryCode", category));
		List<CategoryEntity> list = criteria.list();
		return (list != null && !list.isEmpty());
	}

	/**
	 * checking the brand is valid or not
	 */
	public boolean validBrand(String brand) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(AcceptedBrandsEntity.class);
		criteria.add(Restrictions.eq("brandName", brand));
		List<AcceptedBrandsEntity> list = criteria.list();
		return (list != null && !list.isEmpty());
	}

	/**
	 * getListOfErorBusinessInfo
	 */
	public List<LblErrorDTO> getListOfErorBusinessInfo() {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> lblErrorDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LblErrorEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			List<String> allBrandNames = new ArrayList<String>();
			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}
			sql += " where ";
			int index = 0;
			int totalBrands = allBrandNames.size();
			for (String brand : allBrandNames) {
				if (index != 0) {
					sql += " OR";
				}
				sql += " client=?";
				if (totalBrands == index + 1) {
					sql += " )";
				}
				brandsMap.put(index, brand);
				index++;
			}
		}
		sql += " order by uploadedTime desc";
		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LblErrorEntity> businessEntities = query.list();
		logger.info("list size == " + businessEntities.size());
		for (LblErrorEntity lblErrorEntity : businessEntities) {
			LblErrorDTO lblErrorDTO = new LblErrorDTO();
			BeanUtils.copyProperties(lblErrorEntity, lblErrorDTO);
			lblErrorDTOs.add(lblErrorDTO);
		}
		return lblErrorDTOs;
	}

	/**
	 * reomveCorrectErrorData from business error
	 */
	public void reomveCorrectErrorData(List<LblErrorDTO> correctDBErrorRecords) {
		Session session = sessionFactory.getCurrentSession();
		for (LblErrorDTO lblErrorDTO : correctDBErrorRecords) {
			LblErrorEntity lblErrorEntity = new LblErrorEntity();
			BeanUtils.copyProperties(lblErrorDTO, lblErrorEntity);
			session.delete(lblErrorEntity);
		}
	}

	/**
	 * getSpecificErrorBusinessInfo
	 * 
	 */
	public List<LblErrorDTO> getSpecificErrorBusinessInfo(List<Long> listIds) {
		Session session = sessionFactory.getCurrentSession();
		List<LblErrorDTO> list = new ArrayList<LblErrorDTO>();
		for (Long id : listIds) {
			/*
			 * LblErrorDTO dto = new LblErrorDTO(); LblErrorEntity entityBean =
			 * (LblErrorEntity) session.load( LblErrorEntity.class, id);
			 */
			LblErrorDTO dto = getErrorBusinessInfo(id);
			list.add(dto);
		}
		logger.info("Specific Error BusinessInfo List size == " + list.size());
		return list;

	}

	/**
	 * getBrandByClientId
	 */
	public String getBrandByClientId(Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		String brandName = null;
		List<BrandEntity> list = criteria.list();
		for (BrandEntity brandEntity : list) {
			brandName = brandEntity.getBrandName();
		}
		return brandName;
	}

	/**
	 * getMaxBrandId
	 */
	public int getMaxBrandId() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.setProjection(Projections.max("brandID"));
		List<Integer> brandIdList = criteria.list();
		Integer brandId = 0;
		if (brandIdList != null && !brandIdList.isEmpty()) {
			if (brandIdList.get(0) != null) {
				brandId = brandIdList.get(0);
			}
		}
		return brandId;
	}

	/**
	 * isClientExistis
	 */

	public boolean isClientExistis(String clientName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", clientName));
		List list = criteria.list();
		return list.size() > 0;
	}

	public boolean isClientIdExistis(String clientId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		List list = criteria.list();
		return list.size() > 0;
	}

	/**
	 * getListOfErrors
	 * 
	 */
	public List<LblErrorDTO> getListOfErrors(String uploadJobId) {
		List<LblErrorDTO> listofInfo = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LblErrorEntity.class);
		criteria.add(Restrictions.eq("uploadJobId", uploadJobId));
		List<LblErrorEntity> list = criteria.list();
		for (LblErrorEntity localBusinessEntity : list) {
			LblErrorDTO businessDTO = new LblErrorDTO();
			BeanUtils.copyProperties(localBusinessEntity, businessDTO);
			listofInfo.add(businessDTO);
		}
		return listofInfo;

	}

	/**
	 * get businesSearchInfo:: get business search information based on
	 * parameters
	 * 
	 */
	public Set<LblErrorDTO> errorBusinesSearchInfo(String brands,
			String companyName, String store, String locationPhone,
			String locationAddress, String locationCity, String locationState,
			String locationZipCode) {

		Set<LblErrorDTO> setOfBusinessRecords = new HashSet<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();

		if (brands.equals("") && companyName.equals("") && store.equals("")
				&& locationPhone.equals("") && locationAddress.equals("")
				&& locationCity.equals("") && locationState.equals("")
				&& locationZipCode.equals("")) {
			List<LblErrorDTO> listOfBusinessInfo = getListOfErrors();

			for (LblErrorDTO localBusinessDTO : listOfBusinessInfo) {

				setOfBusinessRecords.add(localBusinessDTO);
			}
		} else {

			StringBuffer whereCondtion = new StringBuffer(
					"select * from business_errors where");
			if (!brands.equals("")) {
				if (brands.contains("'")) {
					brands = brands.replaceAll("'", "''");
				}
				whereCondtion = whereCondtion.append(" client like '%" + brands
						+ "%'");
			}

			if (!companyName.equals("")) {
				if (companyName.contains("'")) {
					companyName = companyName.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business_errors where")) {
					whereCondtion = whereCondtion.append(" companyName like '%"
							+ companyName + "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and companyName like '%" + companyName
									+ "%'");
				}
			}
			if (!store.equals("")) {
				if (store.contains("'")) {
					store = store.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business_errors where")) {
					whereCondtion = whereCondtion.append(" store like '%"
							+ store + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and store like '%"
							+ store + "%'");
				}
			}
			if (!locationPhone.equals("")) {
				if (locationPhone.contains("'")) {
					locationPhone = locationPhone.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business_errors where")) {
					whereCondtion = whereCondtion
							.append(" locationPhone like '%" + locationPhone
									+ "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and locationPhone like '%"
									+ locationPhone + "%'");
				}
			}
			if (!locationAddress.equals("")) {
				if (locationAddress.contains("'")) {
					locationAddress = locationAddress.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business_errors where")) {
					whereCondtion = whereCondtion
							.append(" locationAddress like '%"
									+ locationAddress + "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and locationAddress like '%"
									+ locationAddress + "%'");
				}
			}
			if (!locationCity.equals("")) {
				if (locationCity.contains("'")) {
					locationCity = locationCity.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business_errors where")) {
					whereCondtion = whereCondtion
							.append(" locationCity like '%" + locationCity
									+ "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and locationCity like '%" + locationCity
									+ "%'");
				}
			}
			if (!locationState.equals("")) {
				if (locationState.contains("'")) {
					locationState = locationState.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business_errors where")) {
					whereCondtion = whereCondtion
							.append(" locationState like '%" + locationState
									+ "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and locationState like '%"
									+ locationState + "%'");
				}
			}
			if (!locationZipCode.equals("")) {
				if (locationZipCode.contains("'")) {
					locationZipCode = locationZipCode.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business_errors where")) {
					whereCondtion = whereCondtion
							.append(" locationZipCode like '%"
									+ locationZipCode + "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and locationZipCode like '%"
									+ locationZipCode + "%'");
				}
			}

			logger.info("### businesslisting search fields Query : "
					+ whereCondtion);
			List<LblErrorDTO> whereCoditionRecords = getErrorWhereCoditionRecords(
					whereCondtion.toString(), session);
			if (whereCoditionRecords.size() > 0) {
				for (LblErrorDTO localBusinessDTO : whereCoditionRecords) {
					setOfBusinessRecords.add(localBusinessDTO);
				}
			}

		}
		return setOfBusinessRecords;
	}

	/**
	 * getErrorWhereCoditionRecords
	 * 
	 * @param query
	 * @param session
	 * @return
	 */
	public List<LblErrorDTO> getErrorWhereCoditionRecords(String query,
			Session session) {
		List<LblErrorDTO> nameList = new ArrayList<LblErrorDTO>();
		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			List<String> allBrandNames = new ArrayList<String>();
			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}

			int index = 0;
			int totalBrands = allBrandNames.size();
			for (String brand : allBrandNames) {

				if (roleId == LBLConstants.CHANNEL_ADMIN
						|| roleId == LBLConstants.PURIST) {
					if (index == 0) {
						query += " AND (";
					}
				} else {
					if (index == 0) {
						query += " AND (";
					}
				}

				if (index != 0) {
					query += " OR";
				}
				query += " client=?";
				if (roleId == LBLConstants.CHANNEL_ADMIN
						|| roleId == LBLConstants.PURIST) {
					if (totalBrands == index + 1) {
						query += " )";
					}
				}
				if (roleId == LBLConstants.CLIENT_ADMIN) {
					if (totalBrands == index + 1) {
						query += " )";
					}
				}
				brandsMap.put(index, brand);
				index++;
			}
		}
		logger.debug("Query executed to fecth recirds are: " + query);

		SQLQuery createQuery = session.createSQLQuery(query);

		for (Integer position : brandsMap.keySet()) {
			createQuery.setString(position, brandsMap.get(position));
		}
		createQuery.addEntity(LblErrorEntity.class);
		List<LblErrorEntity> listOfName = (List<LblErrorEntity>) createQuery
				.list();
		for (LblErrorEntity localBusinessBean : listOfName) {
			LblErrorDTO dto = new LblErrorDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			nameList.add(dto);
		}
		return nameList;
	}

	/**
	 * deleteErrorBusinessInfo
	 */

	public void deleteErrorBusinessInfo(List<Long> listIds) {
		Session session = sessionFactory.getCurrentSession();
		for (Long id : listIds) {
			LblErrorEntity bean = (LblErrorEntity) session
					.createCriteria(LblErrorEntity.class)
					.add(Restrictions.eq("lblStoreId", id)).uniqueResult();
			;
			session.delete(bean);
		}

	}

	/**
	 * updateBusinessRecords based on actionCode
	 */

	public void updateBusinessRecords(
			List<LocalBusinessDTO> updateBusinessRecords, Date date,
			String uploadJobId, String userName) {

		Session session = sessionFactory.getCurrentSession();
		if (updateBusinessRecords != null) {
			for (LocalBusinessDTO localBusinessDTO : updateBusinessRecords) {

				LocalBusinessEntity bean = (LocalBusinessEntity) session.get(
						LocalBusinessEntity.class, localBusinessDTO.getId());
				BeanUtils.copyProperties(localBusinessDTO, bean);
				// bean.setUploadedTime(date);
				bean.setUploadJobId(uploadJobId);
				String locationAddress = localBusinessDTO.getLocationAddress();
				String store = localBusinessDTO.getStore();
				String companyName = localBusinessDTO.getCompanyName();
				String locationCity = localBusinessDTO.getLocationCity();
				String locationPhone = localBusinessDTO.getLocationPhone();
				String locationState = localBusinessDTO.getLocationState();
				String locationZipCode = localBusinessDTO.getLocationZipCode();
				LocalBusinessDTO businessDTO = getBusinesslistingBystore(store);
				String companyName2 = businessDTO.getCompanyName();
				String locationAddress2 = businessDTO.getLocationAddress();
				String locationCity2 = businessDTO.getLocationCity();
				String locationPhone2 = businessDTO.getLocationPhone();
				String locationState2 = businessDTO.getLocationState();
				String locationZipCode2 = businessDTO.getLocationZipCode();
				session.update(bean);
				ChangeTrackingEntity entity = new ChangeTrackingEntity();
				if (locationAddress.equalsIgnoreCase(locationAddress2)) {

				} else {
					entity.setLocationAddress(locationAddress);
					entity.setLocationAddressCDate(date);
				}
				if (locationCity.equalsIgnoreCase(locationCity2)) {

				} else {
					entity.setLocationCity(locationCity);
					entity.setLocationCityCDate(date);
				}
				if (locationPhone.equalsIgnoreCase(locationPhone2)) {

				} else {
					entity.setLocationPhone(locationPhone);
					entity.setLocationPhoneCDate(date);
				}
				if (locationZipCode.equalsIgnoreCase(locationZipCode2)) {

				} else {
					entity.setLocationZipCode(locationZipCode);
					entity.setLocationZipCodeCDate(date);
				}
				if (locationState.equalsIgnoreCase(locationState2)) {

				} else {
					entity.setLocationState(locationState);
					entity.setLocationStateCDate(date);
				}
				if (companyName.equalsIgnoreCase(companyName2)) {

				} else {
					entity.setBusinessName(companyName);
					entity.setBusinessNameCDate(date);
				}
				entity.setClientId(localBusinessDTO.getClientId());
				entity.setDate(date);
				entity.setStore(store);
				entity.setUser(userName);
				session.save(entity);

			}
		}
	}

	public LocalBusinessDTO getBusinesslistingBystore(String store) {
		Integer brandID = 0;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("store", store));

		List<LocalBusinessEntity> list = criteria.list();
		if (list != null && list.size() > 0) {
			LocalBusinessEntity brandEntity = list.get(0);
			LocalBusinessDTO brandInfoDTO = new LocalBusinessDTO();
			BeanUtils.copyProperties(brandEntity, brandInfoDTO);
			return brandInfoDTO;
		}
		return null;
	}

	/**
	 * deleteBusinessByActionCode based on actionCode
	 */

	public void deleteBusinessByActionCode(
			List<LocalBusinessDTO> listofDeletesbyActionCode) {

		Session session = sessionFactory.getCurrentSession();
		if (listofDeletesbyActionCode != null) {
			for (LocalBusinessDTO localBusinessDTO : listofDeletesbyActionCode) {

				Integer id = localBusinessDTO.getId();
				logger.debug("Id to delete from database is: " + id);
				LocalBusinessEntity bean = (LocalBusinessEntity) session
						.createCriteria(LocalBusinessEntity.class)
						.add(Restrictions.eq("id", id)).uniqueResult();
				;
				session.delete(bean);
			}
		}

	}

	/**
	 * deleteErrorBusinessByActioncode based on actionCode
	 */

	public void deleteErrorBusinessByActioncode(
			List<LblErrorDTO> listofErrorDeletesbyActionCode) {

		if (listofErrorDeletesbyActionCode != null
				&& listofErrorDeletesbyActionCode.size() > 0
				&& !listofErrorDeletesbyActionCode.isEmpty()) {
			for (LblErrorDTO lblErrorDTO : listofErrorDeletesbyActionCode) {
				Session session = sessionFactory.getCurrentSession();
				Integer id = lblErrorDTO.getId();
				logger.info("Id to delete from database is: " + id
						+ ", and store is: " + lblErrorDTO.getStore());

				Criteria criteria = session
						.createCriteria(LblErrorEntity.class);

				criteria.add(Restrictions.eq("clientId",
						lblErrorDTO.getClientId()));
				criteria.add(Restrictions.eq("store", lblErrorDTO.getStore()));
				// LblErrorEntity bean = (LblErrorEntity)
				// criteria.uniqueResult();
				List<LblErrorEntity> list = criteria.list();
				for (LblErrorEntity bean : list) {
					if (bean != null) {
						logger.info("Listing Identified to delete In errors table with Store: "
								+ lblErrorDTO.getStore());
						session.delete(bean);
					}
				}
			}
		}
	}

	/**
	 * updateErrorBusinessByActionCode based on actionCode
	 */

	public void updateErrorBusinessByActionCode(
			List<LblErrorDTO> listOfErrorUpdaatesByActionCode, Date date,
			String uploadJobId) {

		Session session = sessionFactory.getCurrentSession();
		if (listOfErrorUpdaatesByActionCode != null) {
			for (LblErrorDTO lblErrorDTO : listOfErrorUpdaatesByActionCode) {
				Integer id = lblErrorDTO.getId();
				LblErrorEntity errorEntity = (LblErrorEntity) session.get(
						LblErrorEntity.class, id);
				BeanUtils.copyProperties(lblErrorDTO, errorEntity);
				errorEntity.setUploadedTime(date);
				errorEntity.setUploadJobId(uploadJobId);

				session.update(errorEntity);
			}
		}

	}

	public String getCategoryNameById(Integer categoryId) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT localezeCategory FROM CategorySyphcode where sicCode=?";
		Query createQuery = session.createQuery(sql);
		createQuery.setInteger(0, categoryId);
		String categoryName = "";
		List<String> categoryList = createQuery.list();

		for (String catName : categoryList) {
			categoryName = catName;
			if (categoryName != "")
				break;
		}

		return categoryName;

	}

	public String getSyphCodeByClientAndCategoryID(String category,
			Integer clientId) {
		Session session = sessionFactory.getCurrentSession();

		String sql = "SELECT syphCode FROM CategorySyphcode where sicCode="
				+ category + " AND clientId=" + clientId + "";

		Query createQuery = session.createQuery(sql);
		List<String> Syphcode = createQuery.list();

		return (Syphcode != null && !Syphcode.isEmpty()) ? Syphcode.get(0) : "";
	}

	public String getSyphCodeByStore(String store) {
		Session session = sessionFactory.getCurrentSession();

		String sql = "SELECT syphCode FROM CategorySyphcode where store='"
				+ store + "'";

		Query createQuery = session.createQuery(sql);
		List<String> Syphcode = createQuery.list();

		return (Syphcode != null && !Syphcode.isEmpty()) ? Syphcode.get(0) : "";
	}

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

	public void deleteBrand(Integer brandID) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		Integer clientId = null;
		criteria.add(Restrictions.eq("brandID", brandID));
		List<BrandEntity> list = criteria.list();
		for (BrandEntity brandEntity : list) {
			clientId = brandEntity.getClientId();
		}

		Query createQuery = session
				.createQuery("delete from BrandEntity where brandID=" + brandID
						+ "");
		createQuery.executeUpdate();

		deleteBrnadInfoOnBusinessListings(clientId);
		deleteBrnadInfoOnErrorListings(clientId);
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

	public void deleteBrnadInfoOnBusinessListings(Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		session.createCriteria(LblErrorEntity.class);
		Query createQuery = session
				.createQuery("delete from LblErrorEntity where clientId="
						+ clientId + "");
		createQuery.executeUpdate();
	}

	public void deleteBrnadInfoOnErrorListings(Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		session.createCriteria(LocalBusinessEntity.class);
		Query createQuery = session
				.createQuery("delete from LocalBusinessEntity where clientId="
						+ clientId + "");
		createQuery.executeUpdate();
	}

	public void updateTheDeleteChannelInbrnds(Integer channelID) {

		Session session = sessionFactory.getCurrentSession();
		session.createCriteria(BrandEntity.class);
		Query createQuery = session
				.createQuery("UPDATE BrandEntity SET channelID=0 WHERE channelID="
						+ channelID + "");
		createQuery.executeUpdate();

	}

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
			Integer recoring = schedulerEntity.getRecurring();
			schedulerDTO.setBrandName(brandById);
			schedulerDTO.setPartnerName(partnerByID);
			schedulerDTO.setRecurring(recoring);
			schedulerDTO.setSchdulerId(schedulerEntity.getSchdulerId());
			schedulerDTO.setHours(schedulerEntity.getHours());
			schedulerDTO.setScheduleTime(scheduleTime);

			schedulerDTOs.add(schedulerDTO);

		}

		return schedulerDTOs;
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

	public void deleteBusinessInfotest(List<Integer> listIds) {
		Session session = sessionFactory.getCurrentSession();
		for (Integer id : listIds) {
			SchedulerEntity bean = (SchedulerEntity) session
					.createCriteria(SchedulerEntity.class)
					.add(Restrictions.eq("schdulerId", id)).uniqueResult();
			;
			session.delete(bean);
		}

	}

	public boolean saveScheduler(SchedulerDTO dto) {
		Session session = sessionFactory.getCurrentSession();

		SchedulerEntity schdulerEntity = new SchedulerEntity();

		BeanUtils.copyProperties(dto, schdulerEntity);

		session.save(schdulerEntity);

		return true;

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

	public List<String> getStoresbasedonState(String state) {
		Session session = sessionFactory.getCurrentSession();

		String sql = "SELECT store FROM LocalBusinessEntity where locationstate= ?";
		Query createQuery = session.createQuery(sql);
		createQuery.setString(0, state);
		List<String> stores = createQuery.list();
		return stores;
	}

	public void saveUserUserStore(UsersBean bean, List<String> listofStores) {

		Session session = sessionFactory.getCurrentSession();

		UsersEntity entity = new UsersEntity();
		BeanUtils.copyProperties(bean, entity);
		Serializable save = session.save(entity);

		if (save != null) {

			for (String string : listofStores) {

				UserStoresEntity userStoresEntity = new UserStoresEntity();
				userStoresEntity.setUserID(Integer.valueOf(save.toString()));
				userStoresEntity.setStore(string);
				session.save(userStoresEntity);

			}

		}
	}

	public void deleteBrands(List<Integer> listIds) {
		Session session = sessionFactory.getCurrentSession();
		for (Integer integer : listIds) {

			Criteria criteria = session.createCriteria(BrandEntity.class);
			criteria.add(Restrictions.eq("brandID", integer));
			Query createQuery = session
					.createQuery("delete from BrandEntity where brandID="
							+ integer + "");

			logger.info("deleting in brands" + integer);
			createQuery.executeUpdate();

		}

	}

	public List<LblErrorDTO> getListOfUserErrors() {
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> businessDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria userCriteria = session.createCriteria(UsersEntity.class);
		userCriteria.add(Restrictions.eq("userName", userName));
		List<UsersEntity> usersEntities = userCriteria.list();
		UsersEntity usersEntity = new UsersEntity();
		if (usersEntities != null && !usersEntities.isEmpty()) {
			usersEntity = usersEntities.get(0);
		}
		Integer userID = usersEntity.getUserID();
		Criteria createCriteria = session
				.createCriteria(UserStoresEntity.class);
		createCriteria.add(Restrictions.eq("userID", userID));
		List<UserStoresEntity> list = createCriteria.list();

		for (UserStoresEntity userStoresEntity : list) {
			String store = userStoresEntity.getStore();
			Criteria createCriteria2 = session
					.createCriteria(LblErrorEntity.class);
			createCriteria2.add(Restrictions.eq("store", store));
			List<LblErrorEntity> list2 = createCriteria2.list();
			for (LblErrorEntity errorEntity : list2) {
				LblErrorDTO dto = new LblErrorDTO();
				BeanUtils.copyProperties(errorEntity, dto);
				businessDTOs.add(dto);
			}
		}
		return businessDTOs;
	}

	public List<LocalBusinessDTO> getListOfUSerBusinessInfo() {
		String userName = (String) httpSession.getAttribute("userName");
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();

		Criteria userCriteria = session.createCriteria(UsersEntity.class);
		userCriteria.add(Restrictions.eq("userName", userName));
		List<UsersEntity> usersEntities = userCriteria.list();
		UsersEntity usersEntity = new UsersEntity();
		if (usersEntities != null && !usersEntities.isEmpty()) {
			usersEntity = usersEntities.get(0);
		}
		Integer userID = usersEntity.getUserID();
		Criteria createCriteria = session
				.createCriteria(UserStoresEntity.class);
		createCriteria.add(Restrictions.eq("userID", userID));
		List<UserStoresEntity> list = createCriteria.list();

		for (UserStoresEntity userStoresEntity : list) {
			String store = userStoresEntity.getStore();
			Criteria createCriteria2 = session
					.createCriteria(LocalBusinessEntity.class);
			createCriteria2.add(Restrictions.eq("store", store));
			List<LocalBusinessEntity> list2 = createCriteria2.list();
			for (LocalBusinessEntity localBusinessEntity : list2) {
				LocalBusinessDTO dto = new LocalBusinessDTO();
				BeanUtils.copyProperties(localBusinessEntity, dto);
				businessDTOs.add(dto);
			}
		}
		return businessDTOs;
	}

	public boolean saveChangeTrackingInfo(ChangeTrackingEntity entity) {
		Session session = sessionFactory.getCurrentSession();

		session.save(entity);

		return true;
	}

	public List<String> getUserStores() {
		String userName = (String) httpSession.getAttribute("userName");
		List<String> listofstores = new ArrayList<String>();
		Session session = sessionFactory.getCurrentSession();
		Criteria userCriteria = session.createCriteria(UsersEntity.class);
		userCriteria.add(Restrictions.eq("userName", userName));
		List<UsersEntity> usersEntities = userCriteria.list();
		UsersEntity usersEntity = new UsersEntity();
		if (usersEntities != null && !usersEntities.isEmpty()) {
			usersEntity = usersEntities.get(0);
		}
		Integer userID = usersEntity.getUserID();
		Criteria createCriteria = session
				.createCriteria(UserStoresEntity.class);
		createCriteria.add(Restrictions.eq("userID", userID));
		List<UserStoresEntity> list = createCriteria.list();

		for (UserStoresEntity userStoresEntity : list) {
			String store = userStoresEntity.getStore();
			listofstores.add(store);
		}
		return listofstores;
	}

	public List<BrandInfoDTO> getClientbrnds() {

		List<BrandInfoDTO> brandList = new ArrayList<BrandInfoDTO>();
		Session session = sessionFactory.getCurrentSession();
		String userName = (String) httpSession.getAttribute("userName");
		Criteria userCriteria = session.createCriteria(UsersEntity.class);
		userCriteria.add(Restrictions.eq("userName", userName));
		List<UsersEntity> usersEntities = userCriteria.list();
		UsersEntity usersEntity = new UsersEntity();
		if (usersEntities != null && !usersEntities.isEmpty()) {
			usersEntity = usersEntities.get(0);
		}
		// String brand = null;
		List<Integer> brandIdList = new ArrayList<Integer>();
		List<Integer> listbrandID = new ArrayList<Integer>();
		Integer userID = usersEntity.getUserID();
		Criteria criteria = session.createCriteria(User_brands.class);
		criteria.add(Restrictions.eq("userID", userID));
		List<User_brands> list2 = criteria.list();
		for (User_brands user_brands : list2) {
			Integer brandID = user_brands.getBrandID();
			listbrandID.add(brandID);
		}
		List<String> allBrandNames = new ArrayList<String>();
		if (listbrandID.size() > 0) {

			if (!listbrandID.isEmpty()) {
				Session session1 = sessionFactory.getCurrentSession();

				// Set<BrandInfoDTO> brands = new HashSet<BrandInfoDTO>();
				Criteria createCriteria = session1
						.createCriteria(BrandEntity.class);
				createCriteria.add(Restrictions.in("brandID", listbrandID));
				List<BrandEntity> list = createCriteria.list();

				for (BrandEntity brandEntity : list) {
					String brandName = brandEntity.getBrandName();
					BrandInfoDTO brandInfoDTO = new BrandInfoDTO();
					brandInfoDTO.setBrandName(brandName);
					BeanUtils.copyProperties(brandEntity, brandInfoDTO);
					if (brandIdList.contains(brandInfoDTO.getBrandID())) {
						continue;
					}
					brandIdList.add(brandInfoDTO.getBrandID());
					brandList.add(brandInfoDTO);
				}

			}
		}
		logger.info("client brand names:::" + brandList.size());
		return brandList;

	}

	public Map<String, List<BrandInfoDTO>> getActivePartners(String brandName) {
		Session session = sessionFactory.getCurrentSession();
		// String
		// sql=" from BrandEntity where brandName="+brandName+" and
		// partnerActive=Y";
		// Query createQuery = session.createQuery(sql);

		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("partnerActive", "Y"));
		criteria.add(Restrictions.eq("brandName", brandName));
		List<BrandEntity> allActiveBrands = criteria.list();

		DAOUtil daoUtil = new DAOUtil();
		return daoUtil.getBrandsMap(allActiveBrands);
	}

	public void deleteUSer(int parseInt) {
		UsersEntity entity = (UsersEntity) sessionFactory.getCurrentSession()
				.load(UsersEntity.class, parseInt);
		if (entity != null) {
			sessionFactory.getCurrentSession().delete(entity);

		}

	}

	public boolean saveBrand(String brandName, Date startDateValue,
			String locationsInvoiced, String submission, Integer channelID,
			String partnerActive, Integer clientId, int brandId, String email,
			String imagePath) {
		Session session = sessionFactory.getCurrentSession();
		BrandEntity brandEntity = getBrandEntity(brandName, startDateValue,
				locationsInvoiced, submission, channelID, partnerActive,
				clientId, brandId, email);
		brandEntity.setImagePath(imagePath);
		brandEntity.setInactive("N");
		Serializable save = session.save(brandEntity);
		boolean isexist = isBrandnameExist(brandName);
		int submissionId = isBrandExist(brandName);

		if (save.toString() != null && isexist == false) {
			String channelName = getChannelNameById(channelID);
			CustomSubmissions customSubmissions = new CustomSubmissions();

			customSubmissions.setBrandId(brandId);
			customSubmissions.setBrandName(brandName);
			customSubmissions.setChannelId(channelID);
			customSubmissions.setChannelName(channelName);
			session.save(customSubmissions);
		}
		if (save.toString() != null && isexist == true) {
			String channelName = getChannelNameById(channelID);
			CustomSubmissions customSubmissions = new CustomSubmissions();
			String sql = "UPDATE CustomSubmissions SET brandId=?,brandName=?,channelId=?,channelName=? WHERE brandName=?";

			Query createQuery = session.createQuery(sql);
			createQuery.setInteger(0, brandId);
			createQuery.setString(1, brandName);
			createQuery.setInteger(2, channelID);
			createQuery.setString(3, channelName);
			createQuery.setString(4, brandName);
			createQuery.executeUpdate();
		}

		return save != null;

	}

	private boolean isBrandnameExist(String brandName) {
		boolean isExist = false;
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session
				.createCriteria(CustomSubmissions.class);
		createCriteria.add(Restrictions.eq("brandName", brandName));
		List<CustomSubmissions> list = createCriteria.list();
		Integer submissionId = 0;
		if (list.size() > 0) {
			CustomSubmissions customSubmissions = list.get(0);
			CustomSubmissionsDTO dto = new CustomSubmissionsDTO();
			BeanUtils.copyProperties(customSubmissions, dto);
			submissionId = dto.getSubmissionId();
			isExist = true;

		}
		return isExist;
	}

	private int isBrandExist(String brandName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session
				.createCriteria(CustomSubmissions.class);
		createCriteria.add(Restrictions.eq("brandName", brandName));
		List<CustomSubmissions> list = createCriteria.list();
		Integer submissionId = 0;
		if (list.size() > 0) {
			CustomSubmissions customSubmissions = list.get(0);
			CustomSubmissionsDTO dto = new CustomSubmissionsDTO();
			BeanUtils.copyProperties(customSubmissions, dto);
			submissionId = dto.getSubmissionId();

		}
		return submissionId;
	}

	private BrandEntity getBrandEntity(String brandName, Date startDateValue,
			String locationsInvoiced, String submission, Integer channelID,
			String partnerActive, Integer clientId, int brandId, String email) {
		BrandEntity brandEntity = new BrandEntity();
		brandEntity.setBrandID(brandId);
		brandEntity.setBrandName(brandName);
		brandEntity.setChannelID(channelID);
		brandEntity.setStartDate(startDateValue);
		brandEntity.setLocationsInvoiced(locationsInvoiced);
		brandEntity.setSubmisions(submission);
		brandEntity.setPartnerActive(partnerActive);
		brandEntity.setClientId(clientId);
		brandEntity.setEmail(email);

		return brandEntity;
	}

	public boolean updateBrand(Integer brandID, String brandName,
			Date startDate, String locationsInvoiced, String submission,
			Integer channelID, String partnerActive, Integer clientId,
			Integer id, String email) {
		Session session = sessionFactory.getCurrentSession();

		BrandEntity brandEntity = getBrandEntity(brandName, startDate,
				locationsInvoiced, submission, channelID, partnerActive,
				clientId, brandID, email);
		brandEntity.setId(id);

		session.saveOrUpdate(brandEntity);
		if (brandEntity != null) {
			String channelName = getChannelNameById(channelID);
			String sql = "UPDATE CustomSubmissions SET brandId=" + brandID
					+ ",brandName='" + brandName + "',channelId=" + channelID
					+ ",channelName='" + channelName + "' WHERE brandid=?";
			Query createQuery = session.createQuery(sql);
			createQuery.setInteger(0, brandID);
			createQuery.executeUpdate();

		}

		return true;

	}

	public List<CustomSubmissionsDTO> getDaySchedules() throws Exception {
		Session session = sessionFactory.getCurrentSession();
		List<CustomSubmissionsDTO> schedulerDTOs = new ArrayList<CustomSubmissionsDTO>();
		Criteria criteria = session.createCriteria(CustomSubmissions.class);
		List<CustomSubmissions> list = criteria.list();
		for (CustomSubmissions customSubmissions : list) {
			CustomSubmissionsDTO customSubmissionsDTO = new CustomSubmissionsDTO();
			BeanUtils.copyProperties(customSubmissions, customSubmissionsDTO);
			schedulerDTOs.add(customSubmissionsDTO);
		}

		return schedulerDTOs;

	}

	/*
	 * public List<SchedulerDTO> getDaySchedules() throws Exception { Session
	 * session = sessionFactory.getCurrentSession(); List<SchedulerDTO>
	 * schedulerDTOs = new ArrayList<SchedulerDTO>();
	 * 
	 * Criteria criteria = session.createCriteria(SchedulerEntity.class);
	 * 
	 * // String formattedDate = sdf.format(new Date()); Calendar cal =
	 * Calendar.getInstance(); cal.set(Calendar.HOUR_OF_DAY, 0);
	 * cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0); Date today =
	 * cal.getTime();
	 * 
	 * Calendar endTime = Calendar.getInstance();
	 * endTime.set(Calendar.HOUR_OF_DAY, 23); endTime.set(Calendar.MINUTE, 59);
	 * endTime.set(Calendar.SECOND, 59); Date tonight = endTime.getTime();
	 * 
	 * criteria.add(Restrictions.between("scheduleTime", today, tonight));
	 * List<SchedulerEntity> list = criteria.list();
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); for
	 * (SchedulerEntity schedulerEntity : list) { SchedulerDTO schedulerDTO =
	 * new SchedulerDTO(); Integer brandID = schedulerEntity.getBrandID();
	 * String brandById = getBrandById(brandID); Integer partnerId =
	 * schedulerEntity.getPartnerId(); String partnerByID =
	 * getPartnerByID(partnerId); Date scheduleTime =
	 * schedulerEntity.getScheduleTime(); // Integer schdulerId =
	 * getSchdulerId(schedularId); String hours = schedulerEntity.getHours();
	 * Integer recoring = schedulerEntity.getRecurring();
	 * 
	 * schedulerDTO.setSchdulerId(schedulerEntity.getSchdulerId());
	 * schedulerDTO.setRecurring(recoring);
	 * schedulerDTO.setPartnerId(partnerId); schedulerDTO.setBrandID(brandID);
	 * schedulerDTO.setHours(hours);
	 * schedulerDTO.setNextScheduleTime(schedulerEntity .getNextScheduleTime());
	 * schedulerDTO.setScheduleTime(scheduleTime);
	 * schedulerDTO.setBrandName(brandById);
	 * schedulerDTO.setPartnerName(partnerByID);
	 * 
	 * schedulerDTOs.add(schedulerDTO);
	 * 
	 * }
	 * 
	 * return schedulerDTOs;
	 * 
	 * }
	 */

	public void updateSchedule(SchedulerDTO dto) throws Exception {
		Session session = sessionFactory.getCurrentSession();

		SchedulerEntity schdulerEntity = new SchedulerEntity();
		BeanUtils.copyProperties(dto, schdulerEntity);
		session.update(schdulerEntity);
	}

	public List<LocalBusinessDTO> getCLientListOfBusinessInfo(String client,
			String templateName) {
		Session session = sessionFactory.getCurrentSession();
		List<LocalBusinessDTO> listofinfo = new ArrayList<LocalBusinessDTO>();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("client", client));
		List<LocalBusinessEntity> list = criteria.list();
		Integer brandid = getClinetIdIdByName(client);
		Map<String, String> storeSubmissionMap = null;
		if (templateName.contains("InfogroupTemplate")) {
			storeSubmissionMap = getStoreSubmissionMap(list, brandid);
		}

		List<String> category = getCategoryList(client, templateName);
		Map<String, String> categoryMapByClient = null;
		Map<String, String> category1MapByStores = null;
		boolean isStoreBasedcategory = false;

		if (category.size() == 0) {

			category1MapByStores = getCategory1MapByStores(client,
					templateName, list);
			categoryMapByClient = getCategoryMapByClient(client, templateName);
			logger.info(client + " is having  store based categories");
			isStoreBasedcategory = true;
		}

		if (isStoreBasedcategory) {
			for (LocalBusinessEntity localBusinessEntity : list) {
				LocalBusinessDTO businessDTO = new LocalBusinessDTO();
				String store = localBusinessEntity.getStore();
				String categoryValue = categoryMapByClient.get(store);

				BeanUtils.copyProperties(localBusinessEntity, businessDTO);
				if (templateName.contains("LocalezeTemplate")) {
					String localezeCategory = category1MapByStores.get(store);
					businessDTO.setCategory1(localezeCategory);
					businessDTO.setCategory2(localezeCategory);
					businessDTO.setCategory3(localezeCategory);
				}
				if (templateName.contains("InfogroupTemplate")) {
					if (!storeSubmissionMap.isEmpty()) {
						businessDTO.setSubmissionType(storeSubmissionMap
								.get(localBusinessEntity.getStore()));
					} else {
						businessDTO.setSubmissionType("A");
					}
				}

				String syph1 = category1MapByStores.get(store);

				businessDTO.setPrimaryCategory(categoryValue);
				businessDTO.setYpInternetHeading(categoryValue);
				businessDTO.setSyph1(syph1);
				businessDTO.setSyph2(syph1);
				businessDTO.setSyph3(syph1);
				businessDTO.setSyph4(syph1);
				businessDTO.setSyph5(syph1);
				businessDTO.setSyph6(syph1);
				businessDTO.setAppleCategory(category1MapByStores
						.get(businessDTO.getClient()));
				businessDTO.setCategorySyph(categoryValue);
				listofinfo.add(businessDTO);
			}
			return listofinfo;
		}

		category = getCategory(client, templateName);

		StringBuffer sb = new StringBuffer();

		if (templateName.contains("GoogleTemplate")) {
			for (int i = 0; i < category.size(); i++) {
				String categorys = category.get(i);
				if (categorys != null) {
					sb.append(categorys);

					if (i < category.size() - 1) {

						sb.append(",");

					}
				}

			}
		}
		String additinalCategory = sb.toString();
		if (additinalCategory != null && additinalCategory.endsWith(",")) {
			additinalCategory = additinalCategory.substring(0,
					additinalCategory.length() - 1);
		}
		StringBuffer stringbuffer = new StringBuffer();
		List<String> categorylist = getBingCategory(client, templateName);
		if (templateName.contains("BingTemplate")) {
			for (int i = 0; i < categorylist.size(); i++) {
				String categorys = categorylist.get(i);

				if (categorys != null) {
					stringbuffer.append(categorys);
					if (i < categorylist.size() - 1) {

						stringbuffer.append("||");

					}
				}
			}
		}

		String localezeCategory = getLocalezeCategoryId(brandid);
		String bingCategory = stringbuffer.toString();
		if (bingCategory != null && bingCategory.endsWith("||")) {

			bingCategory = bingCategory.substring(0, bingCategory.length() - 1);
		}
		if (bingCategory != null && bingCategory.endsWith("|")) {

			bingCategory = bingCategory.substring(0, bingCategory.length() - 1);
		}

		String ypInternetHeading = getYpInternetHeading(client, templateName);
		String primeryCategory = getPrimeryCategory(client);
		List<String> aplecatgory = getApplecategory(client, templateName);
		StringBuffer applecatgorybuff = new StringBuffer();
		for (int i = 0; i < aplecatgory.size(); i++) {
			String categorys = aplecatgory.get(i);

			if (categorys != null) {
				applecatgorybuff.append(categorys);
				if (i < aplecatgory.size() - 1) {

					applecatgorybuff.append(",");

				}
			}
		}
		logger.info("primeryCategoryin dao ::" + primeryCategory);
		Map<String, String> categoryMapList = null;
		if (templateName.contains("Acxiom")) {
			categoryMapList = getCategoryMap(brandid, list);
		}
		if (templateName.contains("LocalezeTemplate")) {
			categoryMapList = getLocalezeCategoryMap(brandid, list);
		}
		for (LocalBusinessEntity localBusinessEntity : list) {
			LocalBusinessDTO businessDTO = new LocalBusinessDTO();
			if (templateName.contains("Acxiom")) {
				businessDTO.setSyph1(categoryMapList.get(localBusinessEntity
						.getCategory1()));
				businessDTO.setSyph2(categoryMapList.get(localBusinessEntity
						.getCategory2()));
				businessDTO.setSyph3(categoryMapList.get(localBusinessEntity
						.getCategory3()));
				businessDTO.setSyph4(categoryMapList.get(localBusinessEntity
						.getCategory4()));
				businessDTO.setSyph5(categoryMapList.get(localBusinessEntity
						.getCategory5()));
				businessDTO.setSyph6("");
			}
			if (templateName.contains("LocalezeTemplate")) {
				businessDTO.setCategory1(categoryMapList
						.get(localBusinessEntity.getCategory1()));
				businessDTO.setCategory2(categoryMapList
						.get(localBusinessEntity.getCategory2()));
				businessDTO.setCategory3(categoryMapList
						.get(localBusinessEntity.getCategory3()));

			}
			if (templateName.contains("InfogroupTemplate")) {
				if (!storeSubmissionMap.isEmpty()) {
					businessDTO.setSubmissionType(storeSubmissionMap
							.get(localBusinessEntity.getStore()));
				} else {
					businessDTO.setSubmissionType("A");
				}

			}

			BeanUtils.copyProperties(localBusinessEntity, businessDTO);
			businessDTO.setPrimaryCategory(primeryCategory);
			businessDTO.setYpInternetHeading(ypInternetHeading);
			businessDTO.setAppleCategory(applecatgorybuff.toString());
			businessDTO.setAdditionalCategories(additinalCategory);
			businessDTO.setCategorySyph(bingCategory);
			businessDTO.setLocalezeCategory(localezeCategory);
			listofinfo.add(businessDTO);
		}
		return listofinfo;
	}

	private List<String> getApplecategory(String client, String templateName) {

		Session session = sessionFactory.getCurrentSession();
		Integer clientId = getClinetIdIdByName(client);
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		criteria.add(Restrictions.isNull("store"));

		List<String> categorys = new ArrayList<String>();

		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {
			String category = null;
			if (templateName.contains("AppleTemplate")) {
				category = categorysyph.getAppleCategory();
			}
			if (category != null) {
				categorys.add(category);
			}

		}

		return categorys;
	}

	private Map<String, String> getStoreSubmissionMap(
			List<LocalBusinessEntity> businessList, Integer brandid) {
		Map<String, String> categorymap = new HashMap<String, String>();
		Map<String, String> map = new HashMap<String, String>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RenewalReportEntity.class);
		criteria.add(Restrictions.eq("clientId", brandid));
		List<String> storeList = new ArrayList<String>();
		for (LocalBusinessEntity localBusinessEntity : businessList) {
			storeList.add(localBusinessEntity.getStore());
		}
		criteria.add(Restrictions.in("store", storeList));
		List<RenewalReportEntity> list = criteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {

			String status = renewalReportEntity.getStatus();
			String submisssionType = "A";
			if (status.equalsIgnoreCase("Active")) {
				submisssionType = "U";
			} else if (status.equalsIgnoreCase("Renewed")) {
				submisssionType = "R";
			} else if (status.equalsIgnoreCase("cancel")) {
				submisssionType = "";
			}
			map.put(renewalReportEntity.getStore(), submisssionType);
		}
		return map;
	}

	private Map<String, String> getLocalezeCategoryMap(Integer brandid,
			List<LocalBusinessEntity> businessList) {

		Map<String, String> categorymap = new HashMap<String, String>();

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", brandid));

		List<String> categorys = new ArrayList<String>();
		String category = null;
		List<String> categoryList = new ArrayList<String>();
		for (LocalBusinessEntity localBusinessEntity : businessList) {
			categoryList.add(localBusinessEntity.getCategory1());
			categoryList.add(localBusinessEntity.getCategory2());
			categoryList.add(localBusinessEntity.getCategory3());

		}
		criteria.add(Restrictions.in("sicCode", categoryList));
		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {

			categorymap.put(categorysyph.getSicCode(),
					categorysyph.getSyphCode());
		}

		return categorymap;

	}

	private Map<String, String> getCategoryMap(Integer brandid,
			List<LocalBusinessEntity> businessList) {

		Map<String, String> categorymap = new HashMap<String, String>();

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", brandid));

		List<String> categorys = new ArrayList<String>();
		String category = null;
		List<String> categoryList = new ArrayList<String>();
		for (LocalBusinessEntity localBusinessEntity : businessList) {
			categoryList.add(localBusinessEntity.getCategory1());
			categoryList.add(localBusinessEntity.getCategory2());
			categoryList.add(localBusinessEntity.getCategory3());
			categoryList.add(localBusinessEntity.getCategory4());
			categoryList.add(localBusinessEntity.getCategory5());
		}
		criteria.add(Restrictions.in("sicCode", categoryList));
		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {

			categorymap.put(categorysyph.getSicCode(),
					categorysyph.getSyphCode());
		}

		return categorymap;

	}

	private String getAcxiomcategory(String store, String category1) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("sicCode", category1));
		List<String> pimeryCategorys = new ArrayList<String>();
		String localezeCategory = null;
		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {

			localezeCategory = categorysyph.getSyphCode();

		}

		return localezeCategory;
	}

	private String getInfogroupcategory(String store, String category1) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("store", store));
		List<String> pimeryCategorys = new ArrayList<String>();
		String localezeCategory = null;
		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {

			localezeCategory = categorysyph.getSicCode();

		}

		return localezeCategory;

	}

	private String getLocalezecategory(String store, String category1) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("sicCode", category1));
		List<String> pimeryCategorys = new ArrayList<String>();
		String localezeCategory = null;
		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {

			localezeCategory = categorysyph.getLocalezeCategory();

		}

		return localezeCategory;
	}

	private String getPrimeryCategory(String client) {
		Session session = sessionFactory.getCurrentSession();
		Integer clientId = getClinetIdIdByName(client);
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		criteria.add(Restrictions.eq("categoryLabel", "Category 1"));
		List<String> pimeryCategorys = new ArrayList<String>();
		String pimeryCategory = null;
		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {

			pimeryCategory = categorysyph.getGoogleCategory();
			pimeryCategorys.add(pimeryCategory);
		}

		return pimeryCategory;
	}

	private List<String> getCategory(String client, String templateName) {

		Session session = sessionFactory.getCurrentSession();
		Integer clientId = getClinetIdIdByName(client);
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		criteria.add(Restrictions.isNull("store"));
		criteria.add(Restrictions.ne("categoryLabel", "Category 1"));
		List<String> categorys = new ArrayList<String>();

		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {
			String category = null;
			if (templateName.contains("GoogleTemplate")) {
				category = categorysyph.getGoogleCategory();
			} else if (templateName.contains("BingTemplate")) {
				category = categorysyph.getBingCategory();
			}
			if (category != null) {
				categorys.add(category);
			}

		}

		return categorys;
	}

	private List<String> getCategoryList(String client, String templateName) {

		Session session = sessionFactory.getCurrentSession();
		Integer clientId = getClinetIdIdByName(client);
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		criteria.add(Restrictions.isNull("store"));

		List<String> categorys = new ArrayList<String>();

		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {
			String category = null;
			if (templateName.contains("GoogleTemplate")) {
				category = categorysyph.getGoogleCategory();
			} else if (templateName.contains("BingTemplate")) {
				category = categorysyph.getBingCategory();
			} else if (templateName.contains("LocalezeTemplate")) {
				category = categorysyph.getLocalezeCategory();
			} else if (templateName.contains("InfogroupTemplate")) {
				category = categorysyph.getLocalezeCategory();
			} else if (templateName.contains("Acxiom")) {
				category = categorysyph.getLocalezeCategory();
			} else if (templateName.contains("AppleTemplate")) {
				category = categorysyph.getAppleCategory();
			}
			if (category != null) {
				categorys.add(category);
			}

		}

		return categorys;
	}

	public Map<String, String> getCategoryMapByClient(String client,
			String templateName) {

		Map<String, String> categorymap = new HashMap<String, String>();

		Session session = sessionFactory.getCurrentSession();
		Integer clientId = getClinetIdIdByName(client);
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		criteria.add(Restrictions.isNotNull("store"));

		// List<String> categorys = new ArrayList<String>();
		String category = null;

		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {
			if (templateName.contains("GoogleTemplate")) {
				category = categorysyph.getGoogleCategory();
			} else if (templateName.contains("BingTemplate")) {
				category = categorysyph.getBingCategory();
			} else if (templateName.contains("YPTemplate")) {
				category = categorysyph.getYpInternetHeading();

			}
			categorymap.put(categorysyph.getStore(), category);
		}

		return categorymap;
	}

	public Map<String, String> getCategory1MapByStores(String client,
			String templateName, List<LocalBusinessEntity> businessList) {

		Map<String, String> category1map = new HashMap<String, String>();

		Session session = sessionFactory.getCurrentSession();
		Integer clientId = getClinetIdIdByName(client);
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		List<String> storesList = new ArrayList<String>();
		for (LocalBusinessEntity localBusinessEntity : businessList) {
			storesList.add(localBusinessEntity.getStore());
		}
		if (!storesList.isEmpty() && storesList.size() > 0) {
			criteria.add(Restrictions.in("store", storesList));
		}

		// List<String> categorys = new ArrayList<String>();
		String category = null;

		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {
			if (templateName.contains("GoogleTemplate")) {
				category = categorysyph.getGoogleCategory();
			} else if (templateName.contains("BingTemplate")) {
				category = categorysyph.getBingCategory();
			} else if (templateName.contains("YPTemplate")) {
				category = categorysyph.getYpInternetHeading();
			} else if (templateName.contains("Acxiom")) {
				category = categorysyph.getSyphCode();

			} else if (templateName.contains("LocalezeTemplate")) {
				category = categorysyph.getLocalezeCategory();

			} else if (templateName.contains("AppleTemplate")) {
				category = categorysyph.getAppleCategory();

			}
			category1map.put(categorysyph.getStore(), category);
		}

		return category1map;
	}

	private List<String> getBingCategory(String client, String templateName) {

		Session session = sessionFactory.getCurrentSession();
		Integer clientId = getClinetIdIdByName(client);
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		List<String> categorys = new ArrayList<String>();
		String category = null;

		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {
			if (templateName.contains("GoogleTemplate")) {
				category = categorysyph.getGoogleCategory();
			} else if (templateName.contains("BingTemplate")) {
				category = categorysyph.getBingCategory();
			}
			categorys.add(category);
		}

		return categorys;
	}

	private String getYpInternetHeading(String client, String templateName) {

		Session session = sessionFactory.getCurrentSession();
		Integer clientId = getClinetIdIdByName(client);
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		criteria.add(Restrictions.eq("categoryLabel", "Category 1"));
		List<String> ypInternetHeadings = new ArrayList<String>();
		String ypInternetHeading = null;
		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorysyph : list) {

			ypInternetHeading = categorysyph.getYpInternetHeading();
			ypInternetHeadings.add(ypInternetHeading);
		}

		return ypInternetHeading;
	}

	public List<CategoryDTO> getCategeoryListing() {
		Session session = sessionFactory.getCurrentSession();
		List<CategoryDTO> categorydtos = new ArrayList<CategoryDTO>();

		Criteria createCriteria = session
				.createCriteria(CategorySyphcode.class);
		List<CategorySyphcode> list = createCriteria.list();

		for (CategorySyphcode categeoryentity : list) {
			CategoryDTO dto = new CategoryDTO();
			BeanUtils.copyProperties(categeoryentity, dto);
			categorydtos.add(dto);

		}

		return categorydtos;
	}

	public List<BrandInfoDTO> getBrandListing() {
		Session session = sessionFactory.getCurrentSession();
		List<BrandInfoDTO> brandinfo = new ArrayList<BrandInfoDTO>();

		Criteria createCriteria = session.createCriteria(BrandEntity.class);
		List<BrandEntity> list = createCriteria.list();

		for (BrandEntity entity : list) {
			BrandInfoDTO dto = new BrandInfoDTO();
			Integer channelID = entity.getChannelID();
			String channelname = getChannelNameById(channelID);
			dto.setChannelName(channelname);
			BeanUtils.copyProperties(entity, dto);
			brandinfo.add(dto);

		}

		return brandinfo;
	}

	private String getChannelNameById(Integer channelID) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ChannelEntity.class);
		criteria.add(Restrictions.eq("channelID", channelID));
		String ChannelName = null;
		List<ChannelEntity> list = criteria.list();
		for (ChannelEntity chanelentity : list) {
			ChannelName = chanelentity.getChannelName();
		}
		return ChannelName;
	}

	public BrandInfoDTO getBrandInfo(Integer integer) {

		BrandInfoDTO brandInfoDTO = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandID", integer));
		// criteria.add(Restrictions.eq("partnerActive", "Y"));
		List<BrandEntity> list = criteria.list();
		if (list.size() > 0) {
			brandInfoDTO = new BrandInfoDTO();
			BrandEntity entity = (BrandEntity) list.get(0);

			BeanUtils.copyProperties(entity, brandInfoDTO);
			String submissionsStr = "";
			for (BrandEntity brandEntity : list) {
				if (brandEntity.getPartnerActive() == null
						|| brandEntity.getPartnerActive().equalsIgnoreCase("N")) {
					continue;
				}
				Date activeDate = brandInfoDTO.getActiveDate();
				Date date = null;
				if (activeDate != null) {
					date = DateUtil.getDate(activeDate);
				}
				submissionsStr += brandEntity.getSubmisions() + "_" + date
						+ ",";
			}
			brandInfoDTO.setSubmisions(submissionsStr == "" ? submissionsStr
					: submissionsStr.substring(0, submissionsStr.length() - 1));
		}
		return brandInfoDTO;
	}

	public BrandInfoDTO getBrandInfoByBrandName(String brandName,
			String channelName) {
		BrandInfoDTO brandInfoDTO = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brandName));
		Integer channelID = getChannelIdByName(channelName);
		criteria.add(Restrictions.eq("channelID", channelID));
		// criteria.add(Restrictions.eq("partnerActive", "Y"));
		List<BrandEntity> list = criteria.list();
		if (list.size() > 0) {
			brandInfoDTO = new BrandInfoDTO();
			BrandEntity entity = (BrandEntity) list.get(0);

			BeanUtils.copyProperties(entity, brandInfoDTO);
			String submissionsStr = "";
			for (BrandEntity brandEntity : list) {
				if (brandEntity.getPartnerActive() == null
						|| brandEntity.getPartnerActive().equalsIgnoreCase("N")) {
					continue;
				}
				Date activeDate = brandInfoDTO.getActiveDate();
				Date date = null;
				if (activeDate != null) {
					date = DateUtil.getDate(activeDate);
				}
				submissionsStr += brandEntity.getSubmisions() + "_" + date
						+ ",";
			}
			brandInfoDTO.setChannelName(channelName);
			Integer getlocationsByBrandName = getlocationsByBrandName(brandName);
			brandInfoDTO.setLocationCotracted(getlocationsByBrandName
					.toString());
			brandInfoDTO.setSubmisions(submissionsStr == "" ? submissionsStr
					: submissionsStr.substring(0, submissionsStr.length() - 1));
		}
		return brandInfoDTO;
	}

	public BrandInfoDTO getBrandInfoByBrand(String brandName) {
		BrandInfoDTO brandInfoDTO = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brandName));

		// criteria.add(Restrictions.eq("partnerActive", "Y"));
		List<BrandEntity> list = criteria.list();
		if (list.size() > 0) {
			brandInfoDTO = new BrandInfoDTO();
			BrandEntity entity = (BrandEntity) list.get(0);

			BeanUtils.copyProperties(entity, brandInfoDTO);
			String submissionsStr = "";
			for (BrandEntity brandEntity : list) {
				if (brandEntity.getPartnerActive() == null
						|| brandEntity.getPartnerActive().equalsIgnoreCase("N")) {
					continue;
				}
				Date activeDate = brandInfoDTO.getActiveDate();
				Date date = null;
				if (activeDate != null) {
					date = DateUtil.getDate(activeDate);
				}
				submissionsStr += brandEntity.getSubmisions() + "_" + date
						+ ",";
			}
			Integer channelID = entity.getChannelID();
			String channelName = getChannelNameById(channelID);
			Integer getlocationsByBrandName = getlocationsByBrandName(brandName);
			brandInfoDTO.setLocationCotracted(getlocationsByBrandName
					.toString());
			brandInfoDTO.setSubmisions(submissionsStr == "" ? submissionsStr
					: submissionsStr.substring(0, submissionsStr.length() - 1));
		}
		return brandInfoDTO;
	}

	public BrandInfoDTO getBrandInfoByChannel(String channelName) {
		BrandInfoDTO brandInfoDTO = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);

		Integer channelID = getChannelIdByName(channelName);
		criteria.add(Restrictions.eq("channelID", channelID));
		// criteria.add(Restrictions.eq("partnerActive", "Y"));
		List<BrandEntity> list = criteria.list();
		if (list.size() > 0) {
			brandInfoDTO = new BrandInfoDTO();
			BrandEntity entity = (BrandEntity) list.get(0);

			BeanUtils.copyProperties(entity, brandInfoDTO);
			String submissionsStr = "";
			for (BrandEntity brandEntity : list) {
				if (brandEntity.getPartnerActive() == null
						|| brandEntity.getPartnerActive().equalsIgnoreCase("N")) {
					continue;
				}
				Date activeDate = brandInfoDTO.getActiveDate();
				Date date = null;
				if (activeDate != null) {
					date = DateUtil.getDate(activeDate);
				}
				submissionsStr += brandEntity.getSubmisions() + "_" + date
						+ ",";
			}
			String brandName = entity.getBrandName();
			brandInfoDTO.setChannelName(channelName);
			Integer getlocationsByBrandName = getlocationsByBrandName(brandName);
			brandInfoDTO.setLocationCotracted(getlocationsByBrandName
					.toString());
			brandInfoDTO.setSubmisions(submissionsStr == "" ? submissionsStr
					: submissionsStr.substring(0, submissionsStr.length() - 1));
		}
		return brandInfoDTO;
	}

	public List<CategoryDTO> getCategeoryListingByBrand(String clinetname) {
		List<CategoryDTO> brandInfoDTO = new ArrayList<CategoryDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		Integer clientId = getClinetIdIdByName(clinetname);
		criteria.add(Restrictions.eq("clientId", clientId));
		List<CategorySyphcode> list = criteria.list();
		int size = list.size();
		logger.info("size:::::::::::::::::::::" + size);
		for (CategorySyphcode entity : list) {
			CategoryDTO dto = new CategoryDTO();

			BeanUtils.copyProperties(entity, dto);
			brandInfoDTO.add(dto);

		}
		return brandInfoDTO;
	}

	private Integer getClinetIdIdByName(String clinetname) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", clinetname));
		Integer ClientId = null;
		List<BrandEntity> list = criteria.list();
		for (BrandEntity chanelentity : list) {
			ClientId = chanelentity.getClientId();
		}
		return ClientId;
	}

	@Transactional
	public Set<LocalBusinessDTO> searchBusinessListinginfo(String store,
			String brands) {

		Set<LocalBusinessDTO> setOfLocalBusinessDto = new HashSet<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		logger.info("searchBusinessinfo Address == " + store);
		logger.info("searchBusinessinfo BrandName==" + brands);
		logger.info("searchBusinessinfo Before action set size == "
				+ setOfLocalBusinessDto.size());
		store = DAOUtil.removeCommaFromString(store, "");
		brands = DAOUtil.removeCommaFromString(brands, "");
		if (store.equals("") && brands.equals("")) {
			List<LocalBusinessDTO> listOfBusinessInfo = getListOfBusinessInfo();
			for (LocalBusinessDTO localBusinessDTO : listOfBusinessInfo) {
				setOfLocalBusinessDto.add(localBusinessDTO);
			}
		} else {
			StringBuffer whereCondtion = new StringBuffer(
					"select * from business where");

			if (!store.equals("")) {
				if (store.contains("'")) {
					store = store.replaceAll("'", "''");
				}
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business  where")) {
					whereCondtion = whereCondtion.append(" store like '%"
							+ store + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and store like '%"
							+ store + "%'");
				}
			}

			if (!brands.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase(
						"select * from business where")) {
					whereCondtion = whereCondtion.append(" client like '%"
							+ brands + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and client like '%"
							+ brands + "%'");
				}
			}
			logger.info("@@@@whereCondtion: " + whereCondtion);

			List<LocalBusinessDTO> whereCoditionRecords = getSQlWhereCoditionRecords(
					whereCondtion.toString(), session);

			if (whereCoditionRecords.size() > 0) {
				for (LocalBusinessDTO localBusinessDTO : whereCoditionRecords) {
					setOfLocalBusinessDto.add(localBusinessDTO);
				}
			}
		}
		logger.info("searchBusinessinfo Set  of Record size After == "
				+ setOfLocalBusinessDto.size());
		return setOfLocalBusinessDto;
	}

	public LocalBusinessDTO getBusinessListinginfoByBrandId(String store,
			String brandName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		Integer brandid = getClinetIdIdByName(brandName);
		criteria.add(Restrictions.eq("clientId", brandid));
		criteria.add(Restrictions.eq("store", store));
		List<LocalBusinessEntity> list = criteria.list();
		int size = list.size();
		logger.info("size:::::::::::::::::::::" + size);
		LocalBusinessDTO dto = new LocalBusinessDTO();

		BeanUtils.copyProperties(list.get(0), dto);
		String locationPhone = dto.getLocationPhone();
		if (locationPhone != null) {

			java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat(
					"({0}) {1}-{2}");

			String[] phoneNumArr = { locationPhone.substring(0, 3),
					locationPhone.substring(3, 6), locationPhone.substring(6) };
			dto.setLocationPhone(phoneMsgFmt.format(phoneNumArr));
		}
		// brandInfoDTO.add(dto);

		return dto;
	}

	public Integer getlocationInvoicedByBrandname(String brandName) {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brandName));
		Integer locationInvoiced = 0;
		List<BrandEntity> list = criteria.list();
		for (BrandEntity brandentity : list) {
			locationInvoiced = Integer.parseInt(brandentity
					.getLocationsInvoiced());
		}
		return locationInvoiced;
	}

	public Integer getlocationInvoicedByClient(Integer clientId) {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		Integer locationInvoiced = 0;
		List<BrandEntity> list = criteria.list();
		for (BrandEntity brandentity : list) {
			locationInvoiced = Integer.parseInt(brandentity
					.getLocationsInvoiced());
		}
		return locationInvoiced;
	}

	public Integer getlocationsByBrandName(String brandName) {
		Session session = sessionFactory.getCurrentSession();

		String sql = "SELECT  COUNT(client) FROM LocalBusinessEntity WHERE client = ?";
		Query createQuery = session.createQuery(sql);
		createQuery.setString(0, brandName);
		List<Long> list = createQuery.list();
		// logger.info("COUNT value::"+list.get(0));

		return list.get(0).intValue();
	}

	public void saveSearchDomainInfo(SearchDomainDTO domainDTO) {

		Session session = sessionFactory.getCurrentSession();

		SearchDomains domains = new SearchDomains();
		BeanUtils.copyProperties(domainDTO, domains);
		session.save(domains);

	}

	public void saveCheckReportInfo(CheckReportDTO checkReportDTO) {

		Session session = sessionFactory.getCurrentSession();

		CheckReportEntity checkReportEntity = new CheckReportEntity();

		BeanUtils.copyProperties(checkReportDTO, checkReportEntity);
		session.save(checkReportEntity);

	}

	public String getFactualCategoryId(Integer clientId) {

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(CategorySyphcode.class);

		criteria.add(Restrictions.eq("clientId", clientId));
		String factualId = "";
		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorySyphcode : list) {

			if (categorySyphcode.getFactualCategoryId() != null) {
				factualId = categorySyphcode.getFactualCategoryId();
				break;
			}
		}

		return factualId;
	}

	public String getLocalezeCategoryId(Integer clientId) {

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(CategorySyphcode.class);

		criteria.add(Restrictions.eq("clientId", clientId));
		String factualId = "";
		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorySyphcode : list) {

			if (categorySyphcode.getLocalezeCategory() != null) {
				factualId = categorySyphcode.getLocalezeCategory();
				break;
			}
		}

		return factualId;
	}

	public List<SearchDomainDTO> getDomainsByStore(String store) {

		List<SearchDomainDTO> domains = new ArrayList<SearchDomainDTO>();

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SearchDomains.class);
		criteria.add(Restrictions.eq("searchId", store));
		List<SearchDomains> list = criteria.list();
		for (SearchDomains domain : list) {
			SearchDomainDTO dto = new SearchDomainDTO();

			BeanUtils.copyProperties(domain, dto);
			domains.add(dto);
		}
		return domains;
	}

	public void insertAccuracyGraphInfo(AccuracyGraphDTO accuracyGraphDTO) {

		Session session = sessionFactory.getCurrentSession();

		AccuarcyGraphEntity entity = new AccuarcyGraphEntity();
		BeanUtils.copyProperties(accuracyGraphDTO, entity);
		session.save(entity);

	}

	public List<LocalBusinessDTO> getStoresByBrandName(String clinetname) {
		List<LocalBusinessDTO> domains = new ArrayList<LocalBusinessDTO>();

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("client", clinetname));
		List<LocalBusinessEntity> list = criteria.list();
		for (LocalBusinessEntity domain : list) {
			LocalBusinessDTO dto = new LocalBusinessDTO();

			BeanUtils.copyProperties(domain, dto);
			domains.add(dto);
		}
		return domains;

	}

	public void updateBrandInforBasedOnBrand(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "UPDATE BrandEntity SET inactive='Y' WHERE brandid=?";
		Query createQuery = session.createQuery(sql);
		createQuery.setInteger(0, id);
		createQuery.executeUpdate();

	}

	public List<BrandInfoDTO> getInActiveBrands(Integer brandid) {
		List<BrandInfoDTO> domains = new ArrayList<BrandInfoDTO>();

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandID", brandid));
		List<BrandEntity> list = criteria.list();
		for (BrandEntity domain : list) {
			BrandInfoDTO dto = new BrandInfoDTO();

			BeanUtils.copyProperties(domain, dto);
			domains.add(dto);
		}
		return domains;
	}

	public boolean updateBrand(Integer brandID, String brandName,
			Date startDate, String inactive, String locationsInvoiced,
			String submission, Integer channelID, String partnerActive,
			Integer clientId, Integer id, String email, String imagePath) {
		Session session = sessionFactory.getCurrentSession();

		BrandEntity brandEntity = getBrandEntity(brandName, startDate,
				inactive, locationsInvoiced, submission, channelID,
				partnerActive, clientId, brandID, email);
		brandEntity.setImagePath(imagePath);
		brandEntity.setId(id);
		session.saveOrUpdate(brandEntity);

		return true;

	}

	private BrandEntity getBrandEntity(String brandName, Date startDate,
			String inactive, String locationsInvoiced, String submission,
			Integer channelID, String partnerActive, Integer clientId,
			Integer brandID, String email) {
		BrandEntity brandEntity = new BrandEntity();
		brandEntity.setBrandID(brandID);
		brandEntity.setBrandName(brandName);
		brandEntity.setChannelID(channelID);
		brandEntity.setStartDate(startDate);
		brandEntity.setLocationsInvoiced(locationsInvoiced);
		brandEntity.setSubmisions(submission);
		brandEntity.setPartnerActive(partnerActive);
		brandEntity.setClientId(clientId);
		brandEntity.setEmail(email);
		brandEntity.setInactive(inactive);

		return brandEntity;
	}

	public String getGoogleCategory(String category1) {

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(CategorySyphcode.class);

		criteria.add(Restrictions.eq("categoryId", category1));
		String GoogleCategory = "";
		List<CategorySyphcode> list = criteria.list();
		for (CategorySyphcode categorySyphcode : list) {

			if (categorySyphcode.getFactualCategoryId() != null) {
				GoogleCategory = categorySyphcode.getGoogleCategory();
				break;
			}
		}

		return GoogleCategory;
	}

	public boolean isClientIdExistis(Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CategorySyphcode.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		List list = criteria.list();
		return list.size() > 0;
	}

	/*
	 * public String getGoogleCategoryId(Integer clientId) {
	 * 
	 * Session session = sessionFactory.getCurrentSession();
	 * 
	 * Criteria criteria = session.createCriteria(CategorySyphcode.class);
	 * 
	 * criteria.add(Restrictions.eq("clientId", clientId)); String factualId =
	 * ""; List<CategorySyphcode> list = criteria.list(); for (CategorySyphcode
	 * categorySyphcode : list) {
	 * 
	 * if (categorySyphcode.getFactualCategoryId() != null) { factualId =
	 * categorySyphcode.getFactualCatalso egoryId(); break; } }
	 * 
	 * return factualId; }
	 */
	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByBusinessName(
			String flag, Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				int totalBrands = allBrandNames.size();
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by companyName " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				int totalBrands = allBrandNames.size();
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by companyName " + flag);
				sql += sb.toString();
			}
		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();

			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by companyName " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by companyName " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LocalBusinessEntity> businessEntities = query.list();
		logger.info("total businesses are: " + businessEntities.size());
		for (LocalBusinessEntity localBusinessBean : businessEntities) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByAddress(
			String flag, Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by locationAddress " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;

				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationAddress " + flag);
				sql += sb.toString();
			}
		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by locationAddress " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationAddress " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LocalBusinessEntity> businessEntities = query.list();
		logger.info("total businesses are: " + businessEntities.size());
		for (LocalBusinessEntity localBusinessBean : businessEntities) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByCity(
			String flag, Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by locationCity  " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;

				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationCity " + flag);
				sql += sb.toString();
			}
		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();

			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by locationCity  " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationCity " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LocalBusinessEntity> businessEntities = query.list();
		logger.info("total businesses are: " + businessEntities.size());
		for (LocalBusinessEntity localBusinessBean : businessEntities) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByState(
			String flag, Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				int totalBrands = allBrandNames.size();
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by locationState " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				int totalBrands = allBrandNames.size();
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}

				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationState " + flag);
				sql += sb.toString();
			}
		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by locationState " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationState " + flag);
				sql += sb.toString();
			}

		}

		logger.debug("Fetching the list of businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LocalBusinessEntity> businessEntities = query.list();
		logger.info("total businesses are: " + businessEntities.size());
		for (LocalBusinessEntity localBusinessBean : businessEntities) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByZip(String flag,
			Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				int totalBrands = allBrandNames.size();
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by locationZipCode  " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				int totalBrands = allBrandNames.size();
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationZipCode " + flag);
				sql += sb.toString();
			}
		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();

			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by locationZipCode  " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationZipCode " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LocalBusinessEntity> businessEntities = query.list();
		logger.info("total businesses are: " + businessEntities.size());
		for (LocalBusinessEntity localBusinessBean : businessEntities) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	public List<LocalBusinessDTO> getListOfBusinessInfoOrederByPhone(
			String flag, Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				int totalBrands = allBrandNames.size();
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by locationPhone  " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				int totalBrands = allBrandNames.size();
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationPhone " + flag);
				sql += sb.toString();
			}
		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by locationPhone  " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationPhone " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LocalBusinessEntity> businessEntities = query.list();
		logger.info("total businesses are: " + businessEntities.size());
		for (LocalBusinessEntity localBusinessBean : businessEntities) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	public List<String> getStoreBasedOnBrandsandStore(String brandname,
			String store) {
		Session session = sessionFactory.getCurrentSession();
		logger.info("storesin dao:::::::::::::::" + store);
		List<String> storeslist = new ArrayList<String>();
		Criteria createCriteria = session
				.createCriteria(LocalBusinessEntity.class);
		createCriteria.add(Restrictions.eq("client", brandname));
		createCriteria.add(Restrictions
				.like("store", store, MatchMode.ANYWHERE));
		List<LocalBusinessEntity> list2 = createCriteria.list();
		String stores = "";
		for (LocalBusinessEntity localBusinessEntity : list2) {

			stores = localBusinessEntity.getStore();
			logger.info("storesin dao:::::::::::::::" + stores);
			storeslist.add(stores);

		}
		return storeslist;
	}

	public List<CustomSubmissionsBean> getCustomSubmissions() {

		Session session = sessionFactory.getCurrentSession();

		List<CustomSubmissionsBean> returnlist = new ArrayList<CustomSubmissionsBean>();

		Criteria createCriteria = session
				.createCriteria(CustomSubmissions.class);

		List<CustomSubmissions> list = createCriteria.list();
		for (CustomSubmissions customSubmissions : list) {
			CustomSubmissionsBean bean = new CustomSubmissionsBean();

			BeanUtils.copyProperties(customSubmissions, bean);

			returnlist.add(bean);
		}

		return returnlist;
	}

	public void saveCustomSubmissions(CustomSubmissionsDTO dto) {
		Session session = sessionFactory.getCurrentSession();
		CustomSubmissions entity = new CustomSubmissions();
		/*
		 * entity.setSubmissionId(dto.getSubmissionId());
		 * entity.setAcxiomFrequency(dto.getAcxiomFrequency());
		 * entity.setAcxiomTiming(dto.getAcxiomTiming());
		 * entity.setFactualFrequency(dto.getFactualFrequency());
		 * entity.setBrandId(dto.getBrandId());
		 * entity.setBrandName(dto.getBrandName());
		 * entity.setChannelId(dto.getChannelId());
		 * entity.setChannelName(dto.getChannelName());
		 * entity.setFactualTiming(dto.getFactualTiming());
		 * entity.setInfogroupFrequency(dto.getInfogroupFrequency());
		 * entity.setInfogroupTiming(dto.getInfogroupTiming());
		 * entity.setLocalezeFrequency(dto.getLocalezeFrequency());
		 * entity.setLocalezeTiming(dto.getLocalezeTiming());
		 * entity.setAcxiomScheduledDate(dto.getAcxiomScheduledDate());
		 * entity.setFactualScheduledDate(dto.getFactualScheduledDate());
		 * entity.setLocalezeScheduledDate(dto.getLocalezeScheduledDate());
		 * entity.setInfogroupScheduledDate(dto.getLocalezeScheduledDate());
		 */
		BeanUtils.copyProperties(dto, entity);
		session.update(entity);

	}

	public CustomSubmissionsDTO getCustomSubmissions(int parseInt) {
		CustomSubmissionsDTO submissionsDTO = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CustomSubmissions.class);
		criteria.add(Restrictions.eq("submissionId", parseInt));
		List<CustomSubmissions> list = criteria.list();
		for (CustomSubmissions entity : list) {
			submissionsDTO = new CustomSubmissionsDTO();
			BeanUtils.copyProperties(entity, submissionsDTO);

		}

		return submissionsDTO;

	}

	public List<LocalBusinessDTO> getAllBusinessListingsSortBytotallocations(
			String flag) {
		Session session = sessionFactory.getCurrentSession();
		List<LocalBusinessDTO> brands = new ArrayList<LocalBusinessDTO>();
		String nameQuery = "SELECT client, COUNT(client) from LocalBusinessEntity where client IS NOT NULL GROUP BY client ORDER BY COUNT(client) "
				+ flag;
		Integer role = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		boolean isSuperAdmin = true;
		if (!(role == LBLConstants.CONVERGENT_MOBILE_ADMIN) && userName != null) {
			isSuperAdmin = false;
			// nameQuery
			// +=" AND channelID = (SELECT u.channelID from UsersEntity u where
			// u.userName=?)";
		}
		// nameQuery += " GROUP BY brands";
		List<LocalBusinessDTO> zeroLocationBrandNames = getZeroLocationBrandNames(
				isSuperAdmin, userName);
		Query createQuery = session.createQuery(nameQuery);
		/*
		 * if(!role.equalsIgnoreCase(LBLConstants.CONVERGENT_MOBILE_ADMIN) &&
		 * userName!= null) createQuery.setString(0, userName);
		 */
		if (flag.equalsIgnoreCase("ASC") || flag.equalsIgnoreCase(""))
			brands.addAll(zeroLocationBrandNames);
		List<Object[]> nonZeroLocationBrandNames = createQuery.list();
		List<String> allBrandNames = getAllBrandNames(isSuperAdmin, userName);
		for (Object[] objects : nonZeroLocationBrandNames) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dto.setBrands((String) objects[0]);
			dto.setBrandsCount((Long) objects[1]);
			if (allBrandNames.contains((String) objects[0])) {
				brands.add(dto);
			}
			logger.info(Arrays.toString(objects));
		}

		if (flag.equalsIgnoreCase("DESC"))
			brands.addAll(zeroLocationBrandNames);

		logger.info("add list ::" + brands);
		/*
		 * Collections.sort(brands, new Comparator<LocalBusinessDTO>() {
		 * 
		 * public int compare(LocalBusinessDTO o1, LocalBusinessDTO o2) { return
		 * o1.getBrandsCount().compareTo(o2.getBrandsCount()); }
		 * 
		 * 
		 * 
		 * 
		 * });
		 */

		return brands;
	}

	public List<ExportReportDTO> getListingActivityInf(String flag) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UploadReportEntity.class);
		if (flag.equalsIgnoreCase("DESC")) {
			criteria.addOrder(Order.desc("numberOfRecords"));
		} else {
			criteria.addOrder(Order.asc("numberOfRecords"));
		}

		Integer role = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<ExportReportDTO> exportReportList = new ArrayList<ExportReportDTO>();

		if (role == LBLConstants.CLIENT_ADMIN && userName != null) {

			Criteria userCriteria = session.createCriteria(UsersEntity.class);
			userCriteria.add(Restrictions.eq("userName", userName));
			List<UsersEntity> usersEntities = userCriteria.list();
			UsersEntity usersEntity = new UsersEntity();
			if (usersEntities != null && !usersEntities.isEmpty()) {
				usersEntity = usersEntities.get(0);
			}
			List<String> brand = new ArrayList<String>();
			List<Integer> listbrandID = new ArrayList<Integer>();
			Integer userID = usersEntity.getUserID();
			Criteria brandcriteria = session.createCriteria(User_brands.class);
			brandcriteria.add(Restrictions.eq("userID", userID));
			List<User_brands> list2 = brandcriteria.list();
			for (User_brands user_brands : list2) {
				Integer brandID = user_brands.getBrandID();
				listbrandID.add(brandID);

			}
			brand = getBrandById(listbrandID);

			if (brand.size() > 0)
				criteria.add(Restrictions.in("brand", brand));
		} else if (((role == LBLConstants.CHANNEL_ADMIN || role == LBLConstants.PURIST))
				&& userName != null) {

			/*
			 * String sql =
			 * "SELECT b.brandName from BrandEntity b where b.channelID= (SELECT u.channelID from UsersEntity u where u.userName=?)"
			 * ; Query query = session.createQuery(sql); query.setString(0,
			 * userName); List<String> list = query.list();
			 */
			List<String> allBrandNames = getAllBrandNames(false, userName);
			if (allBrandNames != null && allBrandNames.size() > 0)
				criteria.add(Restrictions.in("brand", allBrandNames));

		}

		List<UploadReportEntity> uploadReportList = criteria.list();
		for (UploadReportEntity uploadReportEntity : uploadReportList) {
			ExportReportDTO dto = new ExportReportDTO();
			dto.setExportID(uploadReportEntity.getId());
			dto.setCount(Integer.valueOf(uploadReportEntity
					.getNumberOfRecords()));
			Date myUploadedDate = uploadReportEntity.getDate();
			java.sql.Date sqlDate = new java.sql.Date(myUploadedDate.getTime());
			dto.setDate(sqlDate);
			dto.setBrandName(uploadReportEntity.getBrand());
			String activityDesc = "Uploaded to System";
			dto.setActivityDescription(activityDesc);
			exportReportList.add(dto);

		}

		Criteria exportCriteria = session
				.createCriteria(ExportReportEntity.class);
		if (flag.equalsIgnoreCase("DESC")) {
			exportCriteria.addOrder(Order.desc("numberOfRecords"));
		} else {
			exportCriteria.addOrder(Order.asc("numberOfRecords"));
		}
		if (role == LBLConstants.CLIENT_ADMIN && userName != null) {

			Criteria userCriteria = session.createCriteria(UsersEntity.class);
			userCriteria.add(Restrictions.eq("userName", userName));
			List<UsersEntity> usersEntities = userCriteria.list();
			UsersEntity usersEntity = new UsersEntity();
			if (usersEntities != null && !usersEntities.isEmpty()) {
				usersEntity = usersEntities.get(0);
			}
			List<String> brand = new ArrayList<String>();
			List<Integer> listbrandID = new ArrayList<Integer>();
			Integer userID = usersEntity.getUserID();
			Criteria brandcriteria = session.createCriteria(User_brands.class);
			brandcriteria.add(Restrictions.eq("userID", userID));
			List<User_brands> list2 = brandcriteria.list();
			for (User_brands user_brands : list2) {
				Integer brandID = user_brands.getBrandID();
				listbrandID.add(brandID);
			}
			brand = getBrandById(listbrandID);

			if (brand.size() > 0)
				exportCriteria.add(Restrictions.in("brandName", brand));

		} else if (((role == LBLConstants.CHANNEL_ADMIN || role == LBLConstants.PURIST))
				&& userName != null) {
			/*
			 * String sql =
			 * "SELECT b.brandName from BrandEntity b where b.channelID= (SELECT u.channelID from UsersEntity u where u.userName=?)"
			 * ; Query query = session.createQuery(sql); query.setString(0,
			 * userName); List<String> list = query.list();
			 */
			List<String> allBrandNames = getAllBrandNames(false, userName);
			if (allBrandNames != null && allBrandNames.size() > 0)
				exportCriteria.add(Restrictions.in("brandName", allBrandNames));

		}

		List<ExportReportEntity> exportReportEntities = exportCriteria.list();
		for (ExportReportEntity exportReportEntity : exportReportEntities) {
			ExportReportDTO exportReportDTO = new ExportReportDTO();
			exportReportDTO.setDate(new java.sql.Date(exportReportEntity
					.getDate().getTime()));
			exportReportDTO.setExportID(exportReportEntity.getExportID());
			String brandName = exportReportEntity.getBrandName();
			exportReportDTO.setBrandName(brandName);
			String partner = exportReportEntity.getPartner();
			// String activityDesc = "Exported to " +
			// exportReportEntity.getPartner();
			String activityDesc = null;
			if (partner.contains("Template")) {
				String[] tempalteName = partner.split("Template");
				String temp = tempalteName[0];
				if (temp.contains("AcxiomCan")) {
					temp = "Acxiom Canada";
				} else if (temp.contains("AcxiomUS")) {
					temp = "Acxiom US";
				}
				partner = temp + " Template";
				activityDesc = "Export to " + partner;
			} else {
				activityDesc = "Distributed to " + partner;
			}

			exportReportDTO.setActivityDescription(activityDesc);
			exportReportDTO
					.setCount(exportReportEntity.getNumberOfRecords() != null ? Long
							.valueOf(exportReportEntity.getNumberOfRecords())
							.intValue() : 0);
			exportReportList.add(exportReportDTO);
		}
		if (flag.equalsIgnoreCase("ASC")) {
			Collections.sort(exportReportList,
					new Comparator<ExportReportDTO>() {
						public int compare(ExportReportDTO o1,
								ExportReportDTO o2) {
							return o1.getCount().compareTo(o2.getCount());
						}
					});
		} else {
			Collections.sort(exportReportList,
					new Comparator<ExportReportDTO>() {
						public int compare(ExportReportDTO o1,
								ExportReportDTO o2) {
							return o2.getCount().compareTo(o1.getCount());
						}
					});
		}

		return exportReportList;
	}

	public List<CustomSubmissionsBean> getSearchBrandandChannelDetails(
			String channelName, String brandName1) {

		List<CustomSubmissionsBean> submissionsBeans = new ArrayList<CustomSubmissionsBean>();
		Session session = sessionFactory.getCurrentSession();

		if (channelName.equals("") && brandName1.equals("")) {
			List<CustomSubmissionsBean> listOfSubmissionInfo = getCustomSubmissions();
			for (CustomSubmissionsBean bean : listOfSubmissionInfo) {
				submissionsBeans.add(bean);
			}
		} else {
			StringBuffer whereCondtion = new StringBuffer(
					"from CustomSubmissions where");

			if (!channelName.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase(
						"from CustomSubmissions where")) {
					whereCondtion = whereCondtion.append(" channelName like '%"
							+ channelName + "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and channelName like '%" + channelName
									+ "%'");
				}
			}
			if (!brandName1.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase(
						"from CustomSubmissions where")) {
					whereCondtion = whereCondtion.append(" brandName like '%"
							+ brandName1 + "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and brandName like '%" + brandName1
									+ "%'");
				}
			}

			logger.info("@@@@whereCondtion: " + whereCondtion);

			Query createQuery = session.createQuery(whereCondtion.toString());

			List<CustomSubmissions> list = createQuery.list();

			for (CustomSubmissions customSubmissions : list) {

				CustomSubmissionsBean bean = new CustomSubmissionsBean();
				BeanUtils.copyProperties(customSubmissions, bean);

				submissionsBeans.add(bean);
			}

		}
		logger.info("searchBusinessinfo Set  of Record size After == "
				+ submissionsBeans.size());
		return submissionsBeans;

	}
	
	public List<ExportReportDTO> getListingActivityInf(
			LocalBusinessDTO businessDTO) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UploadReportEntity.class);
		Integer role = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		
		Criteria exportCriteria = session
				.createCriteria(ExportReportEntity.class);
		
		String dateRange = businessDTO.getDateRange();
		if (dateRange!=null && !dateRange.equalsIgnoreCase(LBLConstants.DATE_RANGE_ALL)) {
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			int currentYear = cal.get(Calendar.YEAR);
			int curentMonth = cal.get(Calendar.MONTH) + 1;
			int previousMonth = curentMonth;
			int previousYear = currentYear;
			if (dateRange.equalsIgnoreCase(LBLConstants.DATE_RANGE_LAST_MONTH)) {
				--previousMonth;
			} else if (dateRange
					.equalsIgnoreCase(LBLConstants.DATE_RANGE_LAST_3_MONTHS)) {
				previousMonth -= 3;
			}
			if (previousMonth <= 0) {
				previousMonth = 12 - previousMonth;
				--previousYear;
			}

			String startDateStr = previousYear + "/" + previousMonth + "/01";
			String currentDateStr = currentYear + "/" + curentMonth + "/01";
			Date startDate = DateUtil.getDate("MM/dd/yyyy HH:mm:ss", new Date(
					startDateStr));
			Date currentDate = DateUtil.getDate("MM/dd/yyyy HH:mm:ss",
					new Date(currentDateStr));
			if (curentMonth == previousMonth) {
				criteria.add(Restrictions.ge("date", currentDate));
				exportCriteria.add(Restrictions.ge("date", currentDate));
			} else {
				criteria.add(Restrictions.between("date", startDate,
						currentDate));
				exportCriteria.add(Restrictions.between("date", startDate,
						currentDate));
			}
		}
		
		List<ExportReportDTO> exportReportList = new ArrayList<ExportReportDTO>();

		if (role == LBLConstants.CLIENT_ADMIN && userName != null) {

			Criteria userCriteria = session.createCriteria(UsersEntity.class);
			userCriteria.add(Restrictions.eq("userName", userName));
			List<UsersEntity> usersEntities = userCriteria.list();
			UsersEntity usersEntity = new UsersEntity();
			if (usersEntities != null && !usersEntities.isEmpty()) {
				usersEntity = usersEntities.get(0);
			}
			List<String> brand = new ArrayList<String>();
			List<Integer> listbrandID = new ArrayList<Integer>();
			Integer userID = usersEntity.getUserID();
			Criteria brandcriteria = session.createCriteria(User_brands.class);
			brandcriteria.add(Restrictions.eq("userID", userID));
			List<User_brands> list2 = brandcriteria.list();
			for (User_brands user_brands : list2) {
				Integer brandID = user_brands.getBrandID();
				listbrandID.add(brandID);

			}
			brand = getBrandById(listbrandID);

			if (brand.size() > 0)
				criteria.add(Restrictions.in("brand", brand));
		} else if ((role == LBLConstants.CHANNEL_ADMIN || role == LBLConstants.PURIST)
				&& userName != null) {

			/*
			 * String sql =
			 * "SELECT b.brandName from BrandEntity b where b.channelID= (SELECT u.channelID from UsersEntity u where u.userName=?)"
			 * ; Query query = session.createQuery(sql); query.setString(0,
			 * userName); List<String> list = query.list();
			 */
			List<String> allBrandNames = getAllBrandNames(false, userName);
			if (allBrandNames != null && allBrandNames.size() > 0)
				criteria.add(Restrictions.in("brand", allBrandNames));

		}

		List<UploadReportEntity> uploadReportList = criteria.list();
		for (UploadReportEntity uploadReportEntity : uploadReportList) {
			ExportReportDTO dto = new ExportReportDTO();
			dto.setExportID(uploadReportEntity.getId());
			dto.setCount(Integer.valueOf(uploadReportEntity
					.getNumberOfRecords()));
			Date myUploadedDate = uploadReportEntity.getDate();
			java.sql.Date sqlDate = new java.sql.Date(myUploadedDate.getTime());
			dto.setDate(sqlDate);
			dto.setBrandName(uploadReportEntity.getBrand());
			String activityDesc = "Uploaded to System";
			dto.setActivityDescription(activityDesc);
			exportReportList.add(dto);

		}


		if (role == LBLConstants.CLIENT_ADMIN && userName != null) {

			Criteria userCriteria = session.createCriteria(UsersEntity.class);
			userCriteria.add(Restrictions.eq("userName", userName));
			List<UsersEntity> usersEntities = userCriteria.list();
			UsersEntity usersEntity = new UsersEntity();
			if (usersEntities != null && !usersEntities.isEmpty()) {
				usersEntity = usersEntities.get(0);
			}
			List<String> brand = new ArrayList<String>();
			List<Integer> listbrandID = new ArrayList<Integer>();
			Integer userID = usersEntity.getUserID();
			Criteria brandcriteria = session.createCriteria(User_brands.class);
			brandcriteria.add(Restrictions.eq("userID", userID));
			List<User_brands> list2 = brandcriteria.list();
			for (User_brands user_brands : list2) {
				Integer brandID = user_brands.getBrandID();
				listbrandID.add(brandID);
			}
			brand = getBrandById(listbrandID);

			if (brand.size() > 0)
				exportCriteria.add(Restrictions.in("brandName", brand));

		} else if ((role == LBLConstants.CHANNEL_ADMIN || role == LBLConstants.PURIST)
				&& userName != null) {
			/*
			 * String sql =
			 * "SELECT b.brandName from BrandEntity b where b.channelID= (SELECT u.channelID from UsersEntity u where u.userName=?)"
			 * ; Query query = session.createQuery(sql); query.setString(0,
			 * userName); List<String> list = query.list();
			 */
			List<String> allBrandNames = getAllBrandNames(false, userName);
			if (allBrandNames != null && allBrandNames.size() > 0)
				exportCriteria.add(Restrictions.in("brandName", allBrandNames));

		}

		List<ExportReportEntity> exportReportEntities = exportCriteria.list();
		for (ExportReportEntity exportReportEntity : exportReportEntities) {
			ExportReportDTO exportReportDTO = new ExportReportDTO();
			exportReportDTO.setDate(new java.sql.Date(exportReportEntity
					.getDate().getTime()));
			exportReportDTO.setExportID(exportReportEntity.getExportID());
			String brandName = exportReportEntity.getBrandName();
			exportReportDTO.setBrandName(brandName);
			String partner = exportReportEntity.getPartner();
			String activityDesc = null;
			if (partner.contains("Template")) {
				String[] tempalteName = partner.split("Template");
				String temp = tempalteName[0];
				if (temp.contains("AcxiomCan")) {
					temp = "Acxiom Canada";
				} else if (temp.contains("AcxiomUS")) {
					temp = "Acxiom US";
				}
				partner = temp + " Template";

				activityDesc = "Export to " + partner;
			} else {
				activityDesc = "Distributed to " + partner;
			}

			exportReportDTO.setActivityDescription(activityDesc);
			exportReportDTO
					.setCount(exportReportEntity.getNumberOfRecords() != null ? Long
							.valueOf(exportReportEntity.getNumberOfRecords())
							.intValue() : 0);
			exportReportList.add(exportReportDTO);
		}
		return exportReportList;
	}


	public List<SearchDomainDTO> getDomainsByStore(String store,
			String directory) {

		List<SearchDomainDTO> domains = new ArrayList<SearchDomainDTO>();

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SearchDomains.class);
		criteria.add(Restrictions.eq("domainName", directory));
		criteria.add(Restrictions.like("searchId", store));
		List<SearchDomains> list = criteria.list();
		for (SearchDomains domain : list) {
			SearchDomainDTO dto = new SearchDomainDTO();

			BeanUtils.copyProperties(domain, dto);
			domains.add(dto);
		}
		return domains;
	}

	public List<LocalBusinessDTO> getListOfBusinessInfoByStore(String flag,
			Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = " FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by store " + flag;

			}

			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}

				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

				}

				sb.append(" order by store " + flag);
				sql += sb.toString();

			}
		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by store " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by store " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}
		List<LocalBusinessEntity> businessEntities = query.list();

		logger.info("total businesses are: " + businessEntities.size());
		for (LocalBusinessEntity localBusinessBean : businessEntities) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	public void deleteBusinessInfoByStoreAndClient(
			UploadBusinessBean uploadBusinessBean) {
		Session session = sessionFactory.getCurrentSession();
		LblErrorEntity errorEntity = new LblErrorEntity();
		BeanUtils.copyProperties(uploadBusinessBean, errorEntity);
		errorEntity
				.setErrorMessage("LocationAddress Verification is failed. Kindly verify the address info, ");

		String query = "delete from LocalBusinessEntity where store='"
				+ uploadBusinessBean.getStore() + "' and clientId='"
				+ uploadBusinessBean.getClientId() + "'";
		Query createQuery = session.createQuery(query);
		createQuery.executeUpdate();

		LblErrorEntity entity = getErrorBusinessInfo(
				uploadBusinessBean.getStore(), uploadBusinessBean.getClientId());

		if (entity != null && entity.getStore() == null) {

			session.save(errorEntity);
		} else {
			updateErrorInfoBySmartyStreets(uploadBusinessBean);
		}

	}

	public void updateErrorBusinessInfoByAddressVerfication(LblErrorDTO errorDTO) {
		Session session = sessionFactory.getCurrentSession();

		LblErrorEntity errorEntity = getErrorBusinessInfo(errorDTO.getStore(),
				errorDTO.getClientId());

		if (errorEntity != null && errorEntity.getStore() != null) {

			errorDTO.setId(errorEntity.getId());
			errorDTO.setErrorMessage(errorEntity.getErrorMessage());
			BeanUtils.copyProperties(errorDTO, errorEntity);
			StringBuffer stringBuffer = new StringBuffer();
			String errorMessage = errorEntity.getErrorMessage();
			if (errorMessage == null) {
				errorMessage = "";
			}
			stringBuffer.append(errorMessage);
			if (!errorMessage.contains("LocationAddress")) {

				stringBuffer
						.append(", LocationAddress Verification is failed.</br> Kindly verify the address info, ");
			} else {
				stringBuffer.append("");
			}
			String query = "update LblErrorEntity set errorMessage=?  where store='"
					+ errorDTO.getStore()
					+ "' and clientId='"
					+ errorDTO.getClientId() + "'";
			Query createQuery = session.createQuery(query);
			createQuery.setString(0, stringBuffer.toString());

			createQuery.executeUpdate();
		}

	}

	public void updateErrorInfoBySmartyStreets(UploadBusinessBean uploadBean) {
		// TODO Auto-generated method stub
		LblErrorEntity errorEntity = getErrorBusinessInfo(
				uploadBean.getStore(), uploadBean.getClientId());

		Session session = sessionFactory.getCurrentSession();

		if (errorEntity != null && errorEntity.getStore() != null) {
			StringBuffer stringBuffer = new StringBuffer();
			String errorMessage = errorEntity.getErrorMessage();
			if (errorMessage == null) {
				errorMessage = "";
			}
			stringBuffer.append(errorMessage);

			BeanUtils.copyProperties(uploadBean, errorEntity);
			errorEntity.setErrorMessage(errorMessage);
			if (!errorMessage.contains("LocationAddress")) {

				stringBuffer
						.append(", LocationAddress Verification is failed.</br> Kindly verify the address info, ");
			} else {
				stringBuffer.append("");
			}

			String query = "update LblErrorEntity set errorMessage=?  where store='"
					+ errorEntity.getStore()
					+ "' and clientId='"
					+ errorEntity.getClientId() + "'";

			Query createQuery = session.createQuery(query);
			createQuery.setString(0, stringBuffer.toString());

			createQuery.executeUpdate();
		} else {

			// delete from business

			deleteBusinessInfo(uploadBean);
			// add to errors

			LblErrorEntity entity = new LblErrorEntity();
			BeanUtils.copyProperties(uploadBean, entity);
			entity.setErrorMessage("LocationAddress Verification is failed.</br> Kindly verify the address info,");
			session.save(entity);
		}
	}

	public void deleteBusinessInfo(UploadBusinessBean uploadBusinessBean) {
		Session session = sessionFactory.getCurrentSession();
		/*
		 * LblErrorEntity errorEntity = new LblErrorEntity();
		 * BeanUtils.copyProperties(uploadBusinessBean, errorEntity);
		 * errorEntity .setErrorMessage(
		 * "LocationAddress Verification is failed. Kindly verify the address info, "
		 * );
		 */

		String query = "delete from LocalBusinessEntity where store='"
				+ uploadBusinessBean.getStore() + "' and clientId='"
				+ uploadBusinessBean.getClientId() + "'";
		Query createQuery = session.createQuery(query);
		createQuery.executeUpdate();

		// session.save(errorEntity);

	}

	private LblErrorEntity getErrorBusinessInfo(String store, Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LblErrorEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("clientId", clientId));
		LblErrorEntity entity = new LblErrorEntity();
		List<LblErrorEntity> list = criteria.list();
		for (LblErrorEntity lblErrorEntity : list) {
			return lblErrorEntity;
		}
		return entity;
	}

	public Map<Integer, List<ChangeTrackingDTO>> getBusinessListing(
			List<String> listofStores, String brand, Date fromDate, Date toDate) {
		Map<Integer, List<ChangeTrackingDTO>> map = new HashMap<Integer, List<ChangeTrackingDTO>>();
		List<ChangeTrackingDTO> changeTrackingDTOs = new ArrayList<ChangeTrackingDTO>();
		Session session = sessionFactory.getCurrentSession();

		for (String store : listofStores) {
			Criteria criteria = session
					.createCriteria(LocalBusinessEntity.class);

			criteria.add(Restrictions.eq("store", store));

			List<LocalBusinessEntity> list = criteria.list();
			for (LocalBusinessEntity localBusinessEntity : list) {
				LocalBusinessDTO businessDTO = new LocalBusinessDTO();
				ChangeTrackingDTO dto = new ChangeTrackingDTO();
				BeanUtils.copyProperties(localBusinessEntity, businessDTO);
				dto.setBusinessName(businessDTO.getCompanyName());

				dto.setStore(businessDTO.getStore());
				dto.setLocationAddress(businessDTO.getLocationAddress());
				dto.setLocationCity(businessDTO.getLocationCity());
				dto.setLocationState(businessDTO.getLocationState());
				dto.setLocationZipCode(businessDTO.getLocationZipCode());
				dto.setLocationPhone(businessDTO.getLocationPhone());
				dto.setWebSite(businessDTO.getWebAddress());
				dto.setHourseOfOperation("");

				changeTrackingDTOs.add(dto);

			}

		}
		map.put(1, changeTrackingDTOs);

		List<ChangeTrackingDTO> changeTrackingDTO = new ArrayList<ChangeTrackingDTO>();
		for (String store : listofStores) {
			Criteria createCriteria = session
					.createCriteria(ChangeTrackingEntity.class);
			createCriteria.add(Restrictions.eq("store", store));
			createCriteria.addOrder(Order.desc("date"));
			if (fromDate != null && toDate != null) {
				createCriteria.add(Restrictions.between("date", fromDate,
						toDate));
			}

			List<ChangeTrackingEntity> lists = createCriteria.list();
			for (ChangeTrackingEntity changeTrackingEntity : lists) {

				ChangeTrackingDTO dto = new ChangeTrackingDTO();
				BeanUtils.copyProperties(changeTrackingEntity, dto);

				changeTrackingDTO.add(dto);
				break;

			}
		}

		map.put(2, changeTrackingDTO);

		return map;
	}

	public Integer isStoreExist(String store, String directory) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CheckReportEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("directory", directory));
		List<CheckReportEntity> list = criteria.list();
		Integer listingId = 0;
		if (list.size() > 0) {
			CheckReportEntity checkReportStageEntity = list.get(0);
			listingId = checkReportStageEntity.getListingId();

		}
		return listingId;
	}

	public void updateCheckReportInfo(CheckReportDTO highestMatchingStore) {
		Session session = sessionFactory.getCurrentSession();

		CheckReportEntity checkReportEntity = new CheckReportEntity();

		BeanUtils.copyProperties(highestMatchingStore, checkReportEntity);
		session.update(checkReportEntity);

	}

	public ChangeTrackingEntity isClientIdAndStoreExists(Integer clientId,
			Long store) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ChangeTrackingEntity.class);
		criteria.add(Restrictions.eq("lblStoreId", store));
		criteria.add(Restrictions.eq("clientId", clientId));
		List<ChangeTrackingEntity> list = criteria.list();
		Integer listingId = 0;
		ChangeTrackingEntity chEntity = null;
		if (list.size() > 0) {
			chEntity = list.get(0);

		}
		return chEntity;
	}

	public boolean updateChangeTrackingInfo(ChangeTrackingEntity entity) {
		Session session = sessionFactory.getCurrentSession();

		session.update(entity);

		return true;
	}

	public void updateAccuracyInfo(AccuracyDTO dto) {
		Session session = sessionFactory.getCurrentSession();

		AccuracyReportEntity accuracyreportEntity = new AccuracyReportEntity();

		BeanUtils.copyProperties(dto, accuracyreportEntity);

		session.update(accuracyreportEntity);

	}

	public void saveAccuracyInfo(AccuracyDTO dto) {

		Session session = sessionFactory.getCurrentSession();

		AccuracyReportEntity accuracyreportEntity = new AccuracyReportEntity();

		BeanUtils.copyProperties(dto, accuracyreportEntity);

		session.save(accuracyreportEntity);
	}

	public Integer isStoreExistInAccuracy(String store, String brandName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(AccuracyReportEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("brandname", brandName));
		List<AccuracyReportEntity> list = criteria.list();
		Integer listingid = 0;
		if (list.size() > 0) {
			AccuracyReportEntity accuracyStageReportEntity = list.get(0);

			listingid = accuracyStageReportEntity.getId();
		}
		return listingid;
	}

	public void saveAccuracyGraphInfo(AccuracyGraphDTO accuracyGraphDTO) {

		Session session = sessionFactory.getCurrentSession();

		AccuarcyGraphEntity accuracyreportEntity = new AccuarcyGraphEntity();

		BeanUtils.copyProperties(accuracyGraphDTO, accuracyreportEntity);

		session.save(accuracyreportEntity);
	}

	public Map<String, Integer> getErrorsCount(String store) {
		Map<String, Integer> countDetails = new HashMap<String, Integer>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CheckReportEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.addOrder(Order.desc("checkedDate"));
		Integer errorcount = -1;
		List<CheckReportEntity> list = criteria.list();
		for (CheckReportEntity entity : list) {

			if (countDetails.size() >= 10)
				break;

			String directory = entity.getDirectory();
			errorcount = entity.getNoOfErrors();
			if (errorcount == null) {
				errorcount = -1;
			}
			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("google")) {
				countDetails.put(directory, errorcount);
			}
			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("bing")) {

				countDetails.put(directory, errorcount);
			}
			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("yahoo")) {

				countDetails.put(directory, errorcount);
			}
			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("yelp")) {

				countDetails.put(directory, errorcount);
			}
			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("yp")) {

				countDetails.put(directory, errorcount);
			}
			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("citysearch")) {

				countDetails.put(directory, errorcount);
			}
			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("mapquest")) {

				countDetails.put(directory, errorcount);
			}
			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("superpages")) {

				countDetails.put(directory, errorcount);
			}
			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("yellowbook")) {

				countDetails.put(directory, errorcount);
			}
			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("whitepages")) {

				countDetails.put(directory, errorcount);
			}
		}

		if (countDetails.get("google") == null) {
			countDetails.put("google", -1);
		}
		if (countDetails.get("bing") == null) {
			countDetails.put("bing", -1);
		}
		if (countDetails.get("yahoo") == null) {
			countDetails.put("yahoo", -1);
		}
		if (countDetails.get("google") == null) {
			countDetails.put("google", -1);
		}
		if (countDetails.get("yelp") == null) {
			countDetails.put("yelp", -1);
		}
		if (countDetails.get("yp") == null) {
			countDetails.put("yp", -1);
		}
		if (countDetails.get("citysearch") == null) {
			countDetails.put("citysearch", -1);
		}
		if (countDetails.get("mapquest") == null) {
			countDetails.put("mapquest", -1);
		}
		if (countDetails.get("superpages") == null) {
			countDetails.put("superpages", -1);
		}
		if (countDetails.get("yellowbook") == null) {
			countDetails.put("yellowbook", -1);
		}
		if (countDetails.get("whitepages") == null) {
			countDetails.put("whitepages", -1);
		}

		// logger.info("countDetails :::" + countDetails.size());

		return countDetails;

	}

	public List<String> getStoreForBrand(String brandName) {

		Session session = sessionFactory.getCurrentSession();
		List<String> storelist = new ArrayList<String>();

		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brandName));
		Integer clientId = null;
		List<BrandEntity> list = criteria.list();
		for (BrandEntity brandEntity : list) {
			clientId = brandEntity.getClientId();
			// logger.info("client is: "+clientId);
		}

		Criteria createCriteria = session
				.createCriteria(LocalBusinessEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clientId));
		createCriteria.addOrder(Order.asc("store"));
		List<LocalBusinessEntity> list2 = createCriteria.list();
		for (LocalBusinessEntity localBusinessEntity : list2) {

			String store = localBusinessEntity.getStore();

			storelist.add(store);
		}
		return storelist;
	}

	public void saveIntoFailedScapes(FailedScrapingsEntity scrapingsEntity) {

		Session session = sessionFactory.getCurrentSession();

		session.save(scrapingsEntity);
	}

	public List<FailedScrapingsEntity> getAllStoresFromFailedScraping() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FailedScrapingsEntity.class);
		List<FailedScrapingsEntity> list = criteria.list();
		List<FailedScrapingsEntity> allListings = new ArrayList<FailedScrapingsEntity>();
		for (FailedScrapingsEntity failedScrapingsEntity : list) {

			allListings.add(failedScrapingsEntity);
		}
		return allListings;
	}

	public boolean isStoreAndDirectoryExist(String store, String directory) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FailedScrapingsEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("directory", directory));
		List<FailedScrapingsEntity> businessEntities = criteria.list();
		return (businessEntities != null && !(businessEntities.isEmpty()));

	}

	public void deleteFailedScapingEntity(FailedScrapingsEntity scrapingsEntity) {
		Session session = sessionFactory.getCurrentSession();

		session.delete(scrapingsEntity);
	}

	public void deleteFailedScapingEntity(String store, String directory) {
		Session session = sessionFactory.getCurrentSession();
		String queryString = "Delete from FailedScrapingsEntity where store=? and directory=?";
		Query createQuery = session.createQuery(queryString);
		createQuery.setString(0, store);
		createQuery.setString(1, directory);
		createQuery.executeUpdate();

	}

	public String getStateFromStateList(String state) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(StatesListEntity.class);
		criteria.add(Restrictions.eq("state", state));
		String code = null;
		List<StatesListEntity> list = criteria.list();
		for (StatesListEntity statesListEntity : list) {
			code = statesListEntity.getCode();
		}
		return code;
	}

	public boolean isAbbreviationExist(String lastWordinDirectoryddress,
			String lastWordinLocationAddress) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session
				.createCriteria(StandardStreetAbbreviationsEntity.class);
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.eq("name", lastWordinDirectoryddress));
		disjunction.add(Restrictions.eq("abbreviation",
				lastWordinDirectoryddress));
		criteria.add(disjunction);

		List<StandardStreetAbbreviationsEntity> list = criteria.list();
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	public void updateCustomSubmissions(CustomSubmissionsDTO schedulerDTO,
			String brandName) {
		Session session = sessionFactory.getCurrentSession();
		CustomSubmissions submissions = new CustomSubmissions();
		BeanUtils.copyProperties(schedulerDTO, submissions);
		session.update(submissions);

	}

	public List<LocalBusinessDTO> getListOfBusinessInfoForExport(
			String templateName) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		logger.info("roleId::::::::::::::" + roleId);
		logger.info("userName::::::::::::::" + userName);
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
					logger.info(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			}
			if (allBrandNames != null && allBrandNames.size() > 0) {
				sql += " where ";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					logger.info(brand);
					sql += " client=?";
					brandsMap.put(index, brand);
					index++;
				}
			}
		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
		}

		if (allBrandNames != null && allBrandNames.size() > 0) {
			sql += " order by store DESC";
		}

		logger.debug("Fetching the list of businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LocalBusinessEntity> businessEntities = query.list();
		logger.info("total businesses are: " + businessEntities.size());
		for (LocalBusinessEntity localBusinessBean : businessEntities) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String client = dto.getClient();
			List<String> category = getCategory(client, templateName);
			StringBuffer sb = new StringBuffer();

			if (templateName.contains("GoogleTemplate")) {
				for (int i = 0; i < category.size(); i++) {
					String categorys = category.get(i);
					if (categorys != null) {
						sb.append(categorys);

						if (i < category.size() - 1) {

							sb.append(",");

						}
					}

				}
			}
			String additinalCategory = sb.toString();
			if (additinalCategory != null && additinalCategory.endsWith(",")) {
				additinalCategory = additinalCategory.substring(0,
						additinalCategory.length() - 1);
			}
			StringBuffer stringbuffer = new StringBuffer();
			List<String> categorylist = getBingCategory(client, templateName);
			if (templateName.contains("BingTemplate")) {
				for (int i = 0; i < categorylist.size(); i++) {
					String categorys = categorylist.get(i);

					if (categorys != null) {
						stringbuffer.append(categorys);
						if (i < categorylist.size() - 1) {

							stringbuffer.append("||");

						}
					}

				}
			}
			String bingCategory = stringbuffer.toString();
			if (bingCategory != null && bingCategory.endsWith("||")) {

				bingCategory = bingCategory.substring(0,
						bingCategory.length() - 1);
			}
			if (bingCategory != null && bingCategory.endsWith("|")) {

				bingCategory = bingCategory.substring(0,
						bingCategory.length() - 1);
			}

			String ypInternetHeading = getYpInternetHeading(client,
					templateName);
			String primeryCategory = getPrimeryCategory(client);

			dto.setPrimaryCategory(primeryCategory);
			dto.setYpInternetHeading(ypInternetHeading);
			dto.setAdditionalCategories(additinalCategory);
			dto.setCategorySyph(bingCategory);

			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	public List<BrandInfoDTO> getClientNamesByBrand(String brand) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.like("brandName", brand, MatchMode.ANYWHERE));
		criteria.addOrder(Order.asc("brandName"));
		List<BrandInfoDTO> brandList = new ArrayList<BrandInfoDTO>();
		List<BrandEntity> list = criteria.list();
		List<Integer> brandIdList = new ArrayList<Integer>();
		for (BrandEntity brandEntity : list) {
			BrandInfoDTO brandInfoDTO = new BrandInfoDTO();
			BeanUtils.copyProperties(brandEntity, brandInfoDTO);
			if (brandIdList.contains(brandInfoDTO.getBrandID())) {
				continue;
			}
			brandIdList.add(brandInfoDTO.getBrandID());
			brandList.add(brandInfoDTO);
		}
		return brandList;
	}

	public List<BrandInfoDTO> getChannelBasedClientsByBrand(String channelName,
			String brand) {
		Session session = sessionFactory.getCurrentSession();

		List<BrandInfoDTO> brandsList = new ArrayList<BrandInfoDTO>();
		List<Integer> brandIdList = new ArrayList<Integer>();
		Criteria criteria = session.createCriteria(ChannelEntity.class);
		criteria.add(Restrictions.eq("channelName", channelName));
		List<ChannelEntity> channelList = criteria.list();
		if (channelList.size() > 0) {
			ChannelEntity channelEntity = (ChannelEntity) channelList.get(0);
			Integer channelID = channelEntity.getChannelID();

			criteria = session.createCriteria(BrandEntity.class);
			criteria.add(Restrictions.eq("channelID", channelID));
			criteria.add(Restrictions.like("brandname", brand,
					MatchMode.ANYWHERE));
			List<BrandEntity> brandList = criteria.list();
			for (BrandEntity brandEntity : brandList) {
				BrandInfoDTO brandInfoDTO = new BrandInfoDTO();
				BeanUtils.copyProperties(brandEntity, brandInfoDTO);
				if (brandIdList.contains(brandInfoDTO.getBrandID())) {
					continue;
				}
				brandIdList.add(brandInfoDTO.getBrandID());
				brandsList.add(brandInfoDTO);
			}
		}
		return brandsList;
	}

	public List<StatesListEntity> getStateListByState(String state) {
		List<StatesListEntity> statelist = new ArrayList<StatesListEntity>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(StatesListEntity.class);
		criteria.add(Restrictions.like("code", state, MatchMode.ANYWHERE));
		List<StatesListEntity> list = criteria.list();
		for (StatesListEntity sates : list) {
			statelist.add(sates);
		}

		return statelist;
	}

	public void updateRenewalInfo(RenewalReportEntity entity) {
		Session session = sessionFactory.getCurrentSession();
		session.update(entity);
	}

	public void saveRenewalInfo(RenewalReportEntity entity) {
		Session session = sessionFactory.getCurrentSession();
		session.save(entity);
	}

	public RenewalReportEntity isStoreExistInRenewal(String store,
			Integer brandId) {
		RenewalReportEntity entity = new RenewalReportEntity();
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session
				.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("store", store));
		createCriteria.add(Restrictions.eq("clientId", brandId));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			return renewalReportEntity;
		}

		return entity;
	}

	public Date getActiveDateForClient(String brandName, String partner) {

		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session.createCriteria(BrandEntity.class);
		createCriteria.add(Restrictions.eq("brandName", brandName));
		createCriteria.add(Restrictions.eq("submisions", partner));
		List<BrandEntity> list = createCriteria.list();
		Date activeDate = null;
		for (BrandEntity brandEntity : list) {
			activeDate = brandEntity.getActiveDate();
			if (activeDate == null) {
				activeDate = brandEntity.getStartDate();
			}

		}

		return activeDate;
	}

	public List<AccuracyDTO> getAccuracyInfoFromAccuracyStage() {
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session
				.createCriteria(AccuracyStageReportEntity.class);
		List<AccuracyStageReportEntity> list = createCriteria.list();
		for (AccuracyStageReportEntity accuracyReportEntity : list) {
			AccuracyDTO accuracyDTO = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, accuracyDTO);
			accuracyDTOs.add(accuracyDTO);
		}

		return accuracyDTOs;
	}

	public List<CheckReportDTO> getcheckreportInfoFromCheckReportStage() {
		List<CheckReportDTO> checkReportDTOs = new ArrayList<CheckReportDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session
				.createCriteria(CheckReportStageEntity.class);
		List<CheckReportStageEntity> list = createCriteria.list();
		for (CheckReportStageEntity checkReportEntity : list) {
			CheckReportDTO checkReportDTO = new CheckReportDTO();
			BeanUtils.copyProperties(checkReportEntity, checkReportDTO);
			checkReportDTOs.add(checkReportDTO);
		}

		return checkReportDTOs;

	}

	public Integer getClientIdFromBusiness(String store, String zip) {

		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session
				.createCriteria(LocalBusinessEntity.class);
		createCriteria.add(Restrictions.eq("store", store));
		createCriteria.add(Restrictions.eq("locationZipCode", zip));
		List<LocalBusinessEntity> list = createCriteria.list();
		Integer clientId = 0;
		for (LocalBusinessEntity localBusinessEntity : list) {
			clientId = localBusinessEntity.getClientId();
		}
		return clientId;
	}

	public List<LocalBusinessDTO> getListOfBussinessByBrandId(Integer brandID) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("clientId", brandID));
		criteria.addOrder(Order.asc("store"));
		List<LocalBusinessEntity> businessEntities = criteria.list();
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		for (LocalBusinessEntity localBusinessEntity : businessEntities) {
			LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();

			BeanUtils.copyProperties(localBusinessEntity, localBusinessDTO);
			businessDTOs.add(localBusinessDTO);
		}
		return businessDTOs;
	}

	public Map<String, Integer> getPathCountMapForStores(String store,
			Integer brandID) {

		return null;
	}

	public List<CitationReportDTO> getListOfCitationInfo(String brandname) {
		Session session = sessionFactory.getCurrentSession();
		Integer brandID = getClinetIdIdByName(brandname);
		Criteria criteria = session.createCriteria(CitationReportEntity.class);
		criteria.add(Restrictions.eq("brandId", brandID));

		List<CitationReportEntity> businessEntities = criteria.list();
		List<CitationReportDTO> businessDTOs = new ArrayList<CitationReportDTO>();
		for (CitationReportEntity localBusinessEntity : businessEntities) {
			CitationReportDTO localBusinessDTO = new CitationReportDTO();

			BeanUtils.copyProperties(localBusinessEntity, localBusinessDTO);
			businessDTOs.add(localBusinessDTO);
		}
		return businessDTOs;
	}

	public Map<String, Integer> getPathCountMapForStores(
			List<LocalBusinessDTO> listOfBussinessinfo) {

		Map<String, Integer> map = new HashMap<String, Integer>();

		Session session = sessionFactory.getCurrentSession();

		for (LocalBusinessDTO localBusinessDTO : listOfBussinessinfo) {
			Criteria criteria = session.createCriteria(SearchDomains.class);

			int googleCount = 0;
			int bingCount = 0;
			int yelpCount = 0;
			int YahooCount = 0;
			int ypCount = 0;
			int citysearchCount = 0;
			int mapquestCount = 0;
			int superpagesCount = 0;
			int yellobookCount = 0;
			int whitepagesCount = 0;

			String store = localBusinessDTO.getStore();
			criteria.add(Restrictions.eq("searchId", store));

			List<SearchDomains> list = criteria.list();
			int count = 0;
			for (SearchDomains searchDomain : list) {

				String path = searchDomain.getDomainName();
				Integer pathsCount = searchDomain.getPathsCount();
				if (path.contains("google") && googleCount == 0) {

					count = count + pathsCount;
					googleCount++;
				}
				if (path.contains("yelp") && yelpCount == 0) {

					count = count + pathsCount;
					yelpCount++;
				}
				if (path.contains("superpages") && superpagesCount == 0) {

					count = count + pathsCount;
					superpagesCount++;
				}
				if (path.contains("yellowbook") && yellobookCount == 0) {

					count = count + pathsCount;
					yellobookCount++;
				}
				if (path.contains("bing") && bingCount == 0) {

					count = count + pathsCount;
					bingCount++;
				}
				if (path.contains("yp") && ypCount == 0) {
					count = count + pathsCount;
					ypCount++;
				}

				if (path.contains("yahoo") && YahooCount == 0) {

					count = count + pathsCount;
					YahooCount++;
				}
				if (path.contains("whitepages") && whitepagesCount == 0) {

					count = count + pathsCount;
					whitepagesCount++;
				}
				if (path.contains("citysearch") && citysearchCount == 0) {

					count = count + pathsCount;
					citysearchCount++;

				}
				if (path.contains("mapquest") && mapquestCount == 0) {

					count = count + pathsCount;
					mapquestCount++;
				}

			}
			map.put(store, count);
		}
		return map;
	}

	public Map<String, List<String>> getPathFromSearch(String store,
			Integer brandID) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SearchDomains.class);
		criteria.add(Restrictions.eq("searchId", store));
		// criteria.addOrder(Order.desc("dateSearched"));

		// createQuery.setString(0, store);

		int googleCount = 0;
		int bingCount = 0;
		int yelpCount = 0;
		int YahooCount = 0;
		int ypCount = 0;
		int citysearchCount = 0;
		int mapquestCount = 0;
		int superpagesCount = 0;
		int yellobookCount = 0;
		int whitepagesCount = 0;
		Map<String, List<String>> storePaths = new HashMap<String, List<String>>();
		List<SearchDomains> list = criteria.list();
		List<String> paths = new ArrayList<String>();
		if (list.size() != 0 && !list.isEmpty()) {
			for (SearchDomains domains : list) {

				String domain = domains.getDomainName();
				String pathval = domains.getPath();
				List<String> domainval = new ArrayList<String>();
				if (domains.getPathsCount() > 1) {

					String[] split = pathval.split(",");
					for (String pathvalues : split) {
						domainval.add(domains.getDomain() + pathvalues);
					}

				}
				String path = domains.getDomain() + pathval;
				if (domain.contains("google") && googleCount == 0) {

					if (domainval.size() > 0) {
						paths.addAll(domainval);
					} else {
						paths.add(path);
					}

					googleCount++;
				}
				if (domain.contains("yelp") && yelpCount == 0) {
					if (domainval.size() > 0) {
						paths.addAll(domainval);
					} else {
						paths.add(path);
					}
					yelpCount++;
				}
				if (domain.contains("superpages") && superpagesCount == 0) {

					if (domainval.size() > 0) {
						paths.addAll(domainval);
					} else {
						paths.add(path);
					}
					superpagesCount++;
				}
				if (domain.contains("yellowbook") && yellobookCount == 0) {

					if (domainval.size() > 0) {
						paths.addAll(domainval);
					} else {
						paths.add(path);
					}
					yellobookCount++;
				}
				if (domain.contains("bing") && bingCount == 0) {

					if (domainval.size() > 0) {
						paths.addAll(domainval);
					} else {
						paths.add(path);
					}
					bingCount++;
				}
				if (domain.contains("yp") && ypCount == 0) {

					if (domainval.size() > 0) {
						paths.addAll(domainval);
					} else {
						paths.add(path);
					}
					ypCount++;
				}
				if (domain.contains("yahoo") && YahooCount == 0) {

					if (domainval.size() > 0) {
						paths.addAll(domainval);
					} else {
						paths.add(path);
					}
					YahooCount++;
				}
				if (domain.contains("whitepages") && whitepagesCount == 0) {

					if (domainval.size() > 0) {
						paths.addAll(domainval);
					} else {
						paths.add(path);
					}
					whitepagesCount++;
				}
				if (domain.contains("citysearch") && citysearchCount == 0) {

					if (domainval.size() > 0) {
						paths.addAll(domainval);
					} else {
						paths.add(path);
					}
					citysearchCount++;

				}
				if (domain.contains("mapquest") && mapquestCount == 0) {

					if (domainval.size() > 0) {
						paths.addAll(domainval);
					} else {
						paths.add(path);
					}
					mapquestCount++;
				}
			}
		}
		storePaths.put(store, paths);

		return storePaths;

	}

	public Map<String, List<String>> getDomainAuthorities(String store,
			Integer brandID) {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(SearchDomains.class);
		criteria.add(Restrictions.eq("searchId", store));
		criteria.addOrder(Order.desc("dateSearched"));

		int googleCount = 0;
		int bingCount = 0;
		int yelpCount = 0;
		int YahooCount = 0;
		int ypCount = 0;
		int citysearchCount = 0;
		int mapquestCount = 0;
		int superpagesCount = 0;
		int yellobookCount = 0;
		int whitepagesCount = 0;
		Map<String, List<String>> domainAuthorities = new HashMap<String, List<String>>();
		List<SearchDomains> list = criteria.list();
		List<String> paths = new ArrayList<String>();
		if (list.size() != 0 && !list.isEmpty()) {
			for (SearchDomains domain : list) {
				String path = domain.getDomainName();
				String domainAuthority = domain.getDomainAuthority();
				if (path.contains("google") && googleCount == 0) {

					paths.add(domainAuthority);
					googleCount++;
				}
				if (path.contains("yelp") && yelpCount == 0) {

					paths.add(domainAuthority);
					yelpCount++;
				}
				if (path.contains("superpages") && superpagesCount == 0) {

					paths.add(domainAuthority);
					superpagesCount++;
				}
				if (path.contains("yellowbook") && yellobookCount == 0) {

					paths.add(domainAuthority);
					yellobookCount++;
				}
				if (path.contains("bing") && bingCount == 0) {

					paths.add(domainAuthority);
					bingCount++;
				}
				if (path.contains("yp") && ypCount == 0) {
					ypCount++;
					paths.add(domainAuthority);
				}
				if (path.contains("yahoo") && YahooCount == 0) {

					paths.add(domainAuthority);
					YahooCount++;
				}
				if (path.contains("whitepages") && whitepagesCount == 0) {

					paths.add(domainAuthority);
					whitepagesCount++;
				}
				if (path.contains("citysearch") && citysearchCount == 0) {

					paths.add(domainAuthority);
					citysearchCount++;

				}
				if (path.contains("mapquest") && mapquestCount == 0) {

					paths.add(domainAuthority);
					mapquestCount++;
				}
			}
		}
		domainAuthorities.put(store, paths);

		return domainAuthorities;
	}

	public void saveCitationreportInfo(CitationReportEntity citationReportEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.save(citationReportEntity);

	}

	public void updateCitationreportInfo(
			CitationReportEntity citationReportEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.update(citationReportEntity);

	}

	public Integer isStoreAndBrandExistInCitation(String store, Integer brandID) {
		Session session = sessionFactory.getCurrentSession();
		Integer citationId = 0;
		Criteria criteria = session.createCriteria(CitationReportEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("brandId", brandID));
		List<CitationReportEntity> list = criteria.list();
		for (CitationReportEntity citationReportEntity : list) {
			citationId = citationReportEntity.getCitationId();
		}

		return citationId;
	}

	public void saveCitationGraphInfo(CitationGraphEntity graphEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.save(graphEntity);

	}

	public Integer getTotalCitationCount(String brandname) {
		Integer clientId = getClinetIdIdByName(brandname);
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session
				.createCriteria(CitationGraphEntity.class);
		createCriteria.add(Restrictions.eq("brandId", clientId));
		createCriteria.addOrder(Order.asc("date"));
		List<CitationGraphEntity> list = createCriteria.list();
		Integer citationCount = 0;
		for (CitationGraphEntity citationGraphEntity : list) {
			citationCount = citationGraphEntity.getCitationCount();
		}
		return citationCount;
	}

	public CitationReportDTO getCitationInfoInfoByStore(String store,
			String brandname) {
		Integer brandID = getClinetIdIdByName(brandname);
		CitationReportDTO citationReportDTO = new CitationReportDTO();
		Session session = sessionFactory.getCurrentSession();
		Integer citationId = 0;
		Criteria criteria = session.createCriteria(CitationReportEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("brandId", brandID));
		List<CitationReportEntity> list = criteria.list();
		for (CitationReportEntity citationReportEntity : list) {
			CitationReportDTO citationReportDTOs = new CitationReportDTO();
			BeanUtils.copyProperties(citationReportEntity, citationReportDTOs);

			return citationReportDTOs;

		}

		return citationReportDTO;
	}

	public int getPercentagecategory1(String brandname) {
		Session session = sessionFactory.getCurrentSession();
		String nameQuery = "SELECT COUNT(accuracy) FROM AccuracyReportEntity WHERE (accuracy BETWEEN 76 AND 100) AND brandname=?";
		Query createQuery = session.createQuery(nameQuery);
		createQuery.setString(0, brandname);
		int Percentagecategory1 = 0;
		List<Long> list = createQuery.list();
		for (Long long1 : list) {
			Percentagecategory1 = long1.intValue();
		}

		return Percentagecategory1;

	}

	public int getPercentagecategory2(String brandname) {
		Session session = sessionFactory.getCurrentSession();
		String nameQuery = "SELECT COUNT(accuracy) FROM AccuracyReportEntity WHERE (accuracy BETWEEN 51 AND 75) AND brandname=?";
		Query createQuery = session.createQuery(nameQuery);
		createQuery.setString(0, brandname);
		int Percentagecategory2 = 0;
		List<Long> list = createQuery.list();
		for (Long long1 : list) {
			Percentagecategory2 = long1.intValue();
		}

		return Percentagecategory2;

	}

	public int getPercentagecategory3(String brandname) {
		Session session = sessionFactory.getCurrentSession();
		String nameQuery = "SELECT COUNT(accuracy) FROM AccuracyReportEntity WHERE (accuracy BETWEEN 25 AND 51) AND brandname=?";
		Query createQuery = session.createQuery(nameQuery);
		createQuery.setString(0, brandname);
		int Percentagecategory3 = 0;
		List<Long> list = createQuery.list();
		for (Long long1 : list) {
			Percentagecategory3 = long1.intValue();
		}

		return Percentagecategory3;
	}

	public int getPercentagecategory4(String brandname) {
		Session session = sessionFactory.getCurrentSession();
		String nameQuery = "SELECT COUNT(accuracy) FROM AccuracyReportEntity WHERE (accuracy BETWEEN 0 AND 25) AND brandname=?";
		Query createQuery = session.createQuery(nameQuery);
		createQuery.setString(0, brandname);
		int Percentagecategory4 = 0;
		List<Long> list = createQuery.list();
		for (Long long1 : list) {
			Percentagecategory4 = long1.intValue();
		}

		return Percentagecategory4;
	}

	public int gettotalStores(String brandname) {
		Session session = sessionFactory.getCurrentSession();
		String nameQuery = "SELECT COUNT(store) FROM LocalBusinessEntity WHERE  client=?";
		Query createQuery = session.createQuery(nameQuery);
		createQuery.setString(0, brandname);
		int totalStores = 0;
		List<Long> list = createQuery.list();
		for (Long long1 : list) {
			totalStores = long1.intValue();
		}

		return totalStores;
	}

	public double getTotalPercentage(String brandname) {
		Session session = sessionFactory.getCurrentSession();
		String nameQuery = "SELECT AVG(accuracy) FROM AccuracyReportEntity WHERE  brandname=?";
		Query createQuery = session.createQuery(nameQuery);
		createQuery.setString(0, brandname);
		double totalPercenatge = 0;
		List<Double> list = createQuery.list();
		for (Double long1 : list) {
			if (long1 != null)
				totalPercenatge = long1.doubleValue();

		}

		return totalPercenatge;
	}

	public void saveAccuracyPercentageInfo(
			AccuracyPercentageEntity percentageEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.save(percentageEntity);

	}

	public AccuracyPercentageEntity getAcccuracyPercentageInfo(String brandname) {
		Session session = sessionFactory.getCurrentSession();
		AccuracyPercentageEntity accuracyPercentageEntity = new AccuracyPercentageEntity();
		Criteria createCriteria = session
				.createCriteria(AccuracyPercentageEntity.class);
		Integer clinetId = getClinetIdIdByName(brandname);
		createCriteria.add(Restrictions.eq("brandId", clinetId));
		List<AccuracyPercentageEntity> list = createCriteria.list();
		for (AccuracyPercentageEntity accPercentageEntity : list) {
			return accPercentageEntity;
		}
		return accuracyPercentageEntity;
	}

	public List<LocalBusinessDTO> getListOfBusinessInfoByClient(String services) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		logger.info("roleId::::::::::::::" + roleId);
		logger.info("userName::::::::::::::" + userName);
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// List<String> brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
					logger.info(brandID);
				}
				// brand = getBrandById(listbrandID);
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					logger.info(brand);
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;
				}
			}
		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
		}

		if (allBrandNames != null && allBrandNames.size() > 0) {
			sql += " order by store DESC";
		}

		logger.debug("Fetching the list of businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LocalBusinessEntity> businessEntities = query.list();
		logger.info("total businesses are: " + businessEntities.size());
		for (LocalBusinessEntity localBusinessBean : businessEntities) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String client = dto.getClient();
			List<String> category = getCategory(client, services);
			StringBuffer sb = new StringBuffer();

			if (services.contains("GoogleTemplate")) {
				for (int i = 0; i < category.size(); i++) {
					String categorys = category.get(i);
					if (categorys != null) {
						sb.append(categorys);

						if (i < category.size() - 1) {

							sb.append(",");

						}
					}

				}
			}
			String additinalCategory = sb.toString();
			if (additinalCategory != null && additinalCategory.endsWith(",")) {
				additinalCategory = additinalCategory.substring(0,
						additinalCategory.length() - 1);
			}
			StringBuffer stringbuffer = new StringBuffer();
			List<String> categorylist = getBingCategory(client, services);
			if (services.contains("BingTemplate")) {
				for (int i = 0; i < categorylist.size(); i++) {
					String categorys = categorylist.get(i);

					if (categorys != null) {
						stringbuffer.append(categorys);
						if (i < categorylist.size() - 1) {

							stringbuffer.append("||");

						}
					}

				}
			}
			String bingCategory = stringbuffer.toString();
			if (bingCategory != null && bingCategory.endsWith("||")) {

				bingCategory = bingCategory.substring(0,
						bingCategory.length() - 1);
			}
			if (bingCategory != null && bingCategory.endsWith("|")) {

				bingCategory = bingCategory.substring(0,
						bingCategory.length() - 1);
			}

			String ypInternetHeading = getYpInternetHeading(client, services);
			String primeryCategory = getPrimeryCategory(client);
			logger.info("primeryCategoryin dao ::" + primeryCategory);

			dto.setPrimaryCategory(primeryCategory);
			dto.setYpInternetHeading(ypInternetHeading);
			dto.setAdditionalCategories(additinalCategory);
			dto.setCategorySyph(bingCategory);

			businessDTOs.add(dto);
		}
		return businessDTOs;

	}

	public Integer getRoleId(String userName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session.createCriteria(UsersEntity.class);
		createCriteria.add(Restrictions.eq("userName", userName));
		List<UsersEntity> list = createCriteria.list();
		Integer roleId = 0;
		for (UsersEntity usersEntity : list) {
			roleId = usersEntity.getRoleId();
		}
		return roleId;
	}

	/**
	 * getChannelBasedClients:: get brand names based on channel name
	 */
	public List<BrandInfoDTO> getChannelBasedClient(String channelName) {
		Session session = sessionFactory.getCurrentSession();

		List<BrandInfoDTO> brandsList = new ArrayList<BrandInfoDTO>();
		List<Integer> brandIdList = new ArrayList<Integer>();
		Criteria criteria = session.createCriteria(ChannelEntity.class);
		criteria.add(Restrictions.eq("channelName", channelName));
		List<ChannelEntity> channelList = criteria.list();
		if (channelList.size() > 0) {
			ChannelEntity channelEntity = (ChannelEntity) channelList.get(0);
			Integer channelID = channelEntity.getChannelID();

			criteria = session.createCriteria(BrandEntity.class);
			criteria.add(Restrictions.eq("channelID", channelID));
			List<BrandEntity> brandList = criteria.list();
			for (BrandEntity brandEntity : brandList) {
				BrandInfoDTO brandInfoDTO = new BrandInfoDTO();
				BeanUtils.copyProperties(brandEntity, brandInfoDTO);
				if (brandIdList.contains(brandInfoDTO.getBrandID())) {
					continue;
				}
				brandIdList.add(brandInfoDTO.getBrandID());
				brandsList.add(brandInfoDTO);
			}
		}
		return brandsList;
	}

	public void addErrorToBusinessList(LocalBusinessDTO localBusinessDTO,
			Date date, String uploadJobId, List<Long> listIds) {

		for (Long lblStoreID : listIds) {
			LblErrorDTO errorBusinessInfo = getErrorBusinessInfo(lblStoreID);
			LocalBusinessDTO localBusinessDTOs = new LocalBusinessDTO();
			localBusinessDTOs.setUploadedTime(date);
			localBusinessDTOs.setUploadJobId(uploadJobId);
			localBusinessDTOs.setLocationClosed("N");
			String[] ignoreProp = { "id" };
			BeanUtils.copyProperties(errorBusinessInfo, localBusinessDTOs,
					ignoreProp);
			Session session = sessionFactory.getCurrentSession();
			Criteria createCriteria = session
					.createCriteria(LocalBusinessEntity.class);
			createCriteria.add(Restrictions.eq("store",
					localBusinessDTOs.getStore()));
			createCriteria.add(Restrictions.eq("clientId",
					localBusinessDTOs.getClientId()));
			List<LocalBusinessEntity> list = createCriteria.list();
			LocalBusinessEntity localBusinessEntity = new LocalBusinessEntity();
			Integer listId = 0;
			if (list != null && !list.isEmpty()) {
				localBusinessEntity = list.get(0);
				listId = localBusinessEntity.getId();
			}

			if (localBusinessEntity.getId() != null
					&& localBusinessEntity.getId() != 0) {

				session.delete(localBusinessEntity);
				LocalBusinessEntity businessBean = new LocalBusinessEntity();
				BeanUtils.copyProperties(localBusinessDTOs, businessBean);

				Serializable save = session.save(businessBean);
			} else {

				LocalBusinessEntity businessBean = new LocalBusinessEntity();
				BeanUtils.copyProperties(localBusinessDTOs, businessBean);

				Serializable save = session.save(businessBean);
			}
			// Integer auditId =
			// isStoreExistInAudit(localBusinessDTOs.getStore(),
			// localBusinessDTOs.getClientId());
			AuditEntity auditEntity = new AuditEntity();
			String uploadUserName = (String) httpSession
					.getAttribute("userName");
			List<UsersDTO> userByUserName = getUserByUserName(uploadUserName);
			UsersDTO usersDTO = userByUserName.get(0);
			/*
			 * if (auditId != 0) {
			 * auditEntity.setAddress(localBusinessDTOs.getLocationAddress());
			 * auditEntity
			 * .setPhoneNumber(localBusinessDTOs.getLocationPhone());
			 * auditEntity.setBusinessName(localBusinessDTOs.getCompanyName());
			 * auditEntity.setId(auditId); auditEntity.setAction("move");
			 * auditEntity.setClientId(localBusinessDTOs.getClientId());
			 * auditEntity.setBrandName(localBusinessDTOs.getClient());
			 * auditEntity.setStore(localBusinessDTOs.getStore());
			 * auditEntity.setDate(new Date());
			 * auditEntity.setUserName(uploadUserName);
			 * auditEntity.setCity(localBusinessDTOs.getLocationCity());
			 * auditEntity.setState(localBusinessDTOs.getLocationState());
			 * auditEntity.setZip(localBusinessDTOs.getLocationZipCode());
			 * session.update(auditEntity); } else {
			 */
			auditEntity.setAddress(localBusinessDTOs.getLocationAddress());
			auditEntity.setPhoneNumber(localBusinessDTOs.getLocationPhone());
			auditEntity.setBusinessName(localBusinessDTOs.getCompanyName());

			auditEntity.setAction("move");
			auditEntity.setClientId(localBusinessDTOs.getClientId());
			auditEntity.setBrandName(localBusinessDTOs.getClient());
			auditEntity.setStore(localBusinessDTOs.getStore());
			auditEntity.setDate(new Date());
			auditEntity.setUserName(uploadUserName);
			auditEntity.setCity(localBusinessDTOs.getLocationCity());
			auditEntity.setState(localBusinessDTOs.getLocationState());
			auditEntity.setZip(localBusinessDTOs.getLocationZipCode());
			session.save(auditEntity);
			// }

			/*
			 * LblErrorEntity bean = (LblErrorEntity) session
			 * .createCriteria(LblErrorEntity.class) .add(Restrictions.eq("id",
			 * id)).uniqueResult();
			 */

			LblErrorEntity bean = getErrorBusinessEntity(lblStoreID);

			session.delete(bean);

		}

	}

	public Integer isStoreExistInAudit(String store, Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session.createCriteria(AuditEntity.class);
		createCriteria.add(Restrictions.eq("store", store));
		createCriteria.add(Restrictions.eq("clientId", clientId));
		List<AuditEntity> list = createCriteria.list();
		Integer id = 0;
		for (AuditEntity auditEntity : list) {
			id = auditEntity.getId();
		}
		return id;
	}

	public List<ValueObject> getData(String storeId, String brandName) {
		// System.out.println("storeId:::::::::::::::"+storeId);
		Integer brandId = getClinetIdIdByName(brandName);
		Session session = sessionFactory.getCurrentSession();
		String sql = "select b.companyName field1, b.locationAddress field2, b.locationCity field3, b.locationState field4,"
				+ " b.locationZipcode field5, b.locationPhone field6, b.webAddress field7,"
				+ " (select ar.averageAccuracy from accuracyreport ar where store = ? and brandname=?)  field8,"
				+ " cr.businessname field9, cr.address field10, cr.city field11, cr.state field12, cr.zip field13,  cr.phone field14,"
				+ " cr.website field15, cr.directory field16, 'black' field17, 'black' field21, 'black' field22, 'black' field23, 'black' field24, 'black' field25 ,'black' field26"
				+ " from checkreport cr, business b"
				+ " where b.store = ? and b.clientId=?"
				+ " and b.store = cr.store and b.clientId = cr.brandId";

		try {

			Query q = session
					.createSQLQuery(sql)
					.addScalar("field1", new StringType())
					.addScalar("field2", new StringType())
					.addScalar("field3", new StringType())
					.addScalar("field4", new StringType())
					.addScalar("field5", new StringType())
					.addScalar("field6", new StringType())
					.addScalar("field7", new StringType())
					.addScalar("field8", new StringType())
					.addScalar("field9", new StringType())
					.addScalar("field10", new StringType())
					.addScalar("field11", new StringType())
					.addScalar("field12", new StringType())
					.addScalar("field13", new StringType())
					.addScalar("field14", new StringType())
					.addScalar("field15", new StringType())
					.addScalar("field16", new StringType())
					.addScalar("field17", new StringType())
					.addScalar("field21", new StringType())
					.addScalar("field22", new StringType())
					.addScalar("field23", new StringType())
					.addScalar("field24", new StringType())
					.addScalar("field25", new StringType())
					.addScalar("field26", new StringType())
					.setResultTransformer(
							Transformers.aliasToBean(ValueObject.class));

			List<ValueObject> ret = q.setString(0, storeId)
					.setString(1, brandName).setString(2, storeId)
					.setInteger(3, brandId).list();

			if (ret.isEmpty())
				return null;

			// do the compares
			ret = this.compareIt(ret);

			return ret;
		} finally {
		}

	}

	private List<ValueObject> compareIt(List<ValueObject> volist) {

		String companyName = volist.get(0).getField1().toString();
		String locationAddress = volist.get(0).getField2().toString();
		String locationCity = volist.get(0).getField3().toString();
		String locationState = volist.get(0).getField4().toString();
		String locationZipCode = volist.get(0).getField5().toString();
		String locationPhone = volist.get(0).getField6().toString();
		String webAddress = volist.get(0).getField7().toString();
		locationPhone = getFormattedPhone(locationPhone);
		String businessColourCode = "B";
		String cityColourCode = "B";
		String stateColourCode = "B";
		String zipColourCode = "B";
		String phoneColourCode = "B";
		String websiteColourCode = "B";

		for (int i = 0; i < volist.size(); i++) {
			ValueObject vo = volist.get(i);

			String businessname = vo.getField9().toString();
			String address = vo.getField10().toString();
			String city = vo.getField11().toString();
			String state = vo.getField12().toString();
			String zip = vo.getField13().toString();
			String phone = vo.getField14().toString();
			phone = getFormattedPhone(phone);
			String storeUrl = null;
			if (vo.getField15() != null)
				storeUrl = vo.getField15().toString();

			if (storeUrl != null && storeUrl.length() > 1
					&& storeUrl.contains("www")) {
				storeUrl = storeUrl.substring(storeUrl.indexOf("www.") + 4);
			}
			if (storeUrl != null && storeUrl.length() > 0
					&& storeUrl.contains("?")) {
				storeUrl = storeUrl.substring(0, storeUrl.indexOf("?") + 1);
			}
			if (webAddress != null && webAddress.length() > 1
					&& webAddress.contains("www")) {
				webAddress = webAddress
						.substring(webAddress.indexOf("www.") + 4);
			}

			if (webAddress != null && webAddress.length() > 0
					&& webAddress.contains("?")) {
				webAddress = webAddress.substring(0,
						webAddress.indexOf("?") + 1);
			}

			int count = 0;
			if (businessname == null) {
				businessname = "";
			}
			if (address == null) {
				address = "";
			}
			if (city == null) {
				city = "";
			}

			if (zip == null) {
				zip = "";
			}

			if (state == null) {
				state = "";
			}
			if (phone == null) {
				phone = "";
			}
			if (storeUrl == null) {
				storeUrl = "";
			}

			if (!businessname.equalsIgnoreCase(companyName)) {
				count++;
				businessColourCode = "R";
				volist.get(i).setField22(businessColourCode);
			}
			if (!city.equalsIgnoreCase(locationCity)) {
				count++;
				cityColourCode = "R";
				volist.get(i).setField23(cityColourCode);
			}
			if (!zip.equalsIgnoreCase(locationZipCode)) {
				count++;
				zipColourCode = "R";
				volist.get(i).setField24(zipColourCode);
			}
			if (state != null && state.length() > 2) {
				state = getFormattedState(state);
				logger.info("sate::::::::::::::::::::::::" + state);
			}
			if (locationState != null && locationState.length() > 2) {
				locationState = getFormattedState(locationState);
				logger.info("location state" + locationState);
			}

			logger.info("sate::::::::::::::::::::::::" + state);
			logger.info("location state" + locationState);

			if (!state.equalsIgnoreCase(locationState)) {
				count++;
				stateColourCode = "R";
				volist.get(i).setField25(stateColourCode);
			}
			if (!storeUrl.equalsIgnoreCase(webAddress)) {
				count++;
				websiteColourCode = "R";
				volist.get(i).setField26(websiteColourCode);
			}

			if (!phone.equalsIgnoreCase(locationPhone)) {

				count++;
				phoneColourCode = "R";
				volist.get(i).setField21(phoneColourCode);
			}

			String adressColorCode = "B";

			if (locationAddress == null) {
				locationAddress = "";
			}
			String[] locationAddressArray = locationAddress.split(" ");
			String lastWordinLocationAddress = "";
			if (locationAddressArray.length > 0) {
				lastWordinLocationAddress = locationAddressArray[locationAddressArray.length - 1];
			}

			String[] locationDirectoryAddressArray = address.split(" ");
			String lastWordinDirectoryddress = "";
			if (locationAddressArray.length > 0) {
				lastWordinDirectoryddress = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 1];
				System.out.println("lastWordinDirectoryddress"
						+ lastWordinDirectoryddress);
			}

			if (!locationAddress.equalsIgnoreCase(address)) {

				if (!lastWordinDirectoryddress
						.equalsIgnoreCase(lastWordinLocationAddress)) {
					// get the the Abbrevation
					boolean isAbbreviationExist = isAbbreviationExist(
							lastWordinDirectoryddress,
							lastWordinLocationAddress);

					// boolean isAbbreviationExist = true; //this is just for
					// now - uncomment above lines

					String secondLastWordInDirectory = "";
					String secondLastWordInLBL = "";

					if (locationAddressArray.length > 1
							&& locationDirectoryAddressArray.length > 1) {
						secondLastWordInLBL = locationAddressArray[locationAddressArray.length - 2];
						secondLastWordInDirectory = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 2];
					}

					boolean isSecondWordsMatch = false;
					if (secondLastWordInLBL != ""
							&& secondLastWordInDirectory != "") {
						isSecondWordsMatch = secondLastWordInLBL
								.equalsIgnoreCase(secondLastWordInDirectory);
					}

					if (!isAbbreviationExist || !isSecondWordsMatch) {
						adressColorCode = "R";

						count++;
					}
				}
			}
			volist.get(i).setField17(adressColorCode);

		}
		return volist;
	}

	private String getFormattedState(String state) {

		String stateFromStateList = getStateFromStateList(state);

		return stateFromStateList;
	}

	public String getFormattedPhone(String phone) {

		String locationPhone = phone;

		if (locationPhone != null) {
			locationPhone = locationPhone.replaceAll("\\s+", "");
		}

		if (locationPhone != null && locationPhone.contains("Local:")) {
			locationPhone = locationPhone.replace("Local:", "");

			locationPhone = locationPhone.trim();
		}

		if (locationPhone != null && locationPhone.contains("Add")) {
			locationPhone = locationPhone.replace("Add", "");

			locationPhone = locationPhone.trim();
		}
		if (locationPhone != null && locationPhone.contains("+1")) {
			locationPhone = locationPhone.replaceAll("\\+1", "");
			locationPhone = locationPhone.trim();
		}
		if (locationPhone != null && locationPhone.contains("-")) {
			locationPhone = locationPhone.replaceAll("-", "");
			locationPhone = locationPhone.trim();
		}
		if (locationPhone != null && locationPhone.contains(")")) {
			locationPhone = locationPhone.replaceAll("[)]", "");
			locationPhone = locationPhone.trim();
		}
		if (locationPhone != null && locationPhone.contains("(")) {
			locationPhone = locationPhone.replaceAll("[(]", "");
			locationPhone = locationPhone.trim();
		}

		return locationPhone;
	}

	public List<ValueObject> getDS(String storeId, String directory,
			String brandname) {
		Session session = sessionFactory.getCurrentSession();
		String sqlSelect = "";

		if (directory.equals("yahoo"))
			sqlSelect = "ar.yahooAccuracy field8";
		if (directory.equals("google"))
			sqlSelect = "ar.googleAccuracy field8";
		if (directory.equals("foursquare"))
			sqlSelect = "ar.foursquareAccuracy field8";
		if (directory.equals("yelp"))
			sqlSelect = "ar.yelpAccuracy field8";
		if (directory.equals("usplaces"))
			sqlSelect = "ar.uslocalAccuracy field8";
		if (directory.equals("yp"))
			sqlSelect = "ar.ypAccuracy field8";
		if (directory.equals("mapquest"))
			sqlSelect = "ar.mapQuestAccuracy field8";
		if (directory.equals("yellowbot"))
			sqlSelect = "ar.yellobotAccuracy field8";
		if (directory.equals("ezlocal"))
			sqlSelect = "ar.ezlocalAccuracy field8";
		if (directory.equals("citysearch"))
			sqlSelect = "ar.citySearchAccuracy field8";
		// add the others

		String sql = "select 'Accurate' field17, " + sqlSelect
				+ " from accuracyreport ar"
				+ " where ar.store = ? and ar.brandname=?";

		try {

			Query q = session
					.createSQLQuery(sql)
					.addScalar("field8", new IntegerType())
					.addScalar("field17", new StringType())
					.setResultTransformer(
							Transformers.aliasToBean(ValueObject.class));

			List<ValueObject> ret = q.setString(0, storeId)
					.setString(1, brandname).list();

			ValueObject vo = new ValueObject();
			vo.setField17("Inaccurate");
			if (!ret.isEmpty() && ret != null) {
				vo.setField8(100 - (Integer) ret.get(0).getField8());

			}

			ret.add(vo);
			return ret;
		} finally {
		}
	}

	public List<ValueObject> getComapareListData(String storeId,
			String directory, String brandname) {
		Integer brandId = getClinetIdIdByName(brandname);
		Session session = sessionFactory.getCurrentSession();
		String sql = "select b.companyName field1, b.locationAddress field2, b.locationCity field3, b.locationState field4,"
				+ " b.locationZipcode field5, b.locationPhone field6, b.webAddress field7, '' field8,"
				+ " cr.businessname field9, cr.address field10, cr.city field11, cr.state field12, cr.zip field13,  cr.phone field14,"
				+ " cr.website field15, cr.directory field16, null field17,'black' field21, 'black' field22, 'black' field23, 'black' field24, 'black' field25 ,'black' field26,'black' field27"
				+ " from checkreport cr, business b"
				+ " where b.store = ? and b.clientId=?"
				+ " and b.store = cr.store and b.clientId = cr.brandId"
				+ " and cr.directory = ?";

		try {

			Query q = session
					.createSQLQuery(sql)
					.addScalar("field1", new StringType())
					.addScalar("field2", new StringType())
					.addScalar("field3", new StringType())
					.addScalar("field4", new StringType())
					.addScalar("field5", new StringType())
					.addScalar("field6", new StringType())
					.addScalar("field7", new StringType())
					.addScalar("field8", new StringType())
					.addScalar("field9", new StringType())
					.addScalar("field10", new StringType())
					.addScalar("field11", new StringType())
					.addScalar("field12", new StringType())
					.addScalar("field13", new StringType())
					.addScalar("field14", new StringType())
					.addScalar("field15", new StringType())
					.addScalar("field16", new StringType())
					.addScalar("field17", new StringType())
					.addScalar("field21", new StringType())
					.addScalar("field22", new StringType())
					.addScalar("field23", new StringType())
					.addScalar("field24", new StringType())
					.addScalar("field25", new StringType())
					.addScalar("field26", new StringType())
					.addScalar("field27", new StringType())
					.setResultTransformer(
							Transformers.aliasToBean(ValueObject.class));

			List<ValueObject> ret = q.setString(0, storeId)
					.setInteger(1, brandId).setString(2, directory).list();

			if (ret.isEmpty())
				return null;
			ret = this.compareCompareListIt(ret);
			return ret;
		} finally {
		}
	}

	private List<ValueObject> compareCompareListIt(List<ValueObject> volist) {

		String companyName = volist.get(0).getField1().toString();
		String locationAddress = volist.get(0).getField2().toString();
		String locationCity = volist.get(0).getField3().toString();

		String locationState = volist.get(0).getField4().toString();
		String locationPhone = volist.get(0).getField6().toString();
		String locationZipCode = volist.get(0).getField5().toString();
		String webAddress = volist.get(0).getField7().toString();
		String businessColourCode = "B";
		String cityColourCode = "B";
		String stateColourCode = "B";
		String zipColourCode = "B";
		String phoneColourCode = "B";
		String websiteColourCode = "B";

		for (int i = 0; i < volist.size(); i++) {
			ValueObject vo = volist.get(i);

			String businessname = vo.getField9().toString();
			String address = vo.getField10().toString();
			String city = vo.getField11().toString();
			String state = vo.getField12().toString();
			String zip = vo.getField13().toString();
			String phone = vo.getField14().toString();
			String storeUrl = null;
			if (vo.getField15() != null)
				storeUrl = vo.getField15().toString();

			if (storeUrl != null && storeUrl.length() > 1
					&& storeUrl.contains("www")) {
				storeUrl = storeUrl.substring(storeUrl.indexOf("www.") + 4);
			}
			if (storeUrl != null && storeUrl.length() > 0
					&& storeUrl.contains("?")) {
				storeUrl = storeUrl.substring(0, storeUrl.indexOf("?") + 1);
			}
			if (webAddress != null && webAddress.length() > 1
					&& webAddress.contains("www")) {
				webAddress = webAddress
						.substring(webAddress.indexOf("www.") + 4);
			}

			if (webAddress != null && webAddress.length() > 0
					&& webAddress.contains("?")) {
				webAddress = webAddress.substring(0,
						webAddress.indexOf("?") + 1);
			}

			int count = 0;
			if (businessname == null) {
				businessname = "";
			}
			if (address == null) {
				address = "";
			}
			if (city == null) {
				city = "";
			}

			if (zip == null) {
				zip = "";
			}

			if (state == null) {
				state = "";
			}
			if (phone == null) {
				phone = "";
			}
			if (storeUrl == null) {
				storeUrl = "";
			}

			if (!businessname.equalsIgnoreCase(companyName)) {
				count++;
				businessColourCode = "R";
				volist.get(i).setField22(businessColourCode);
			}
			if (!city.equalsIgnoreCase(locationCity)) {
				count++;
				cityColourCode = "R";
				volist.get(i).setField23(cityColourCode);
			}
			if (!zip.equalsIgnoreCase(locationZipCode)) {
				count++;
				zipColourCode = "R";
				volist.get(i).setField24(zipColourCode);
			}
			if (state != null && state.length() > 2) {
				state = getFormattedState(state);
				logger.info("sate::::::::::::::::::::::::" + state);
			}
			if (locationState != null && locationState.length() > 2) {
				locationState = getFormattedState(locationState);
				logger.info("location state" + locationState);
			}

			logger.info("sate::::::::::::::::::::::::" + state);
			logger.info("location state" + locationState);
			if (!state.equalsIgnoreCase(locationState)) {
				count++;
				stateColourCode = "R";
				volist.get(i).setField25(stateColourCode);
			}
			if (!storeUrl.equalsIgnoreCase(webAddress)) {
				count++;
				websiteColourCode = "R";
				volist.get(i).setField26(websiteColourCode);
			}
			if (phone != null && phone.contains("Add")) {
				phone = phone.substring(0, phone.indexOf("Add"));

				phone = phone.trim();
			}
			if (phone != null && phone.contains("+1")) {
				phone = phone.replaceAll("\\+1", "");
				phone = phone.trim();
			}
			if (phone != null && phone.contains("-")) {
				phone = phone.replaceAll("-", "");
				phone = phone.trim();
			}
			if (phone != null && phone.contains(")")) {
				phone = phone.replaceAll("[)]", "");
				phone = phone.trim();
			}
			if (phone != null && phone.contains("(")) {
				phone = phone.replaceAll("[(]", "");
				phone = phone.trim();
			}
			if (phone != null) {
				phone = phone.replaceAll("\\s+", "");
			}

			if (locationPhone != null) {
				locationPhone = locationPhone.replaceAll("\\s+", "");
			}

			if (locationPhone != null && locationPhone.contains("Add")) {
				locationPhone = locationPhone.substring(0,
						locationPhone.indexOf("Add"));

				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains("+1")) {
				locationPhone = locationPhone.replaceAll("\\+1", "");
				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains("-")) {
				locationPhone = locationPhone.replaceAll("-", "");
				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains(")")) {
				locationPhone = locationPhone.replaceAll("[)]", "");
				locationPhone = locationPhone.trim();
			}
			if (locationPhone != null && locationPhone.contains("(")) {
				locationPhone = locationPhone.replaceAll("[(]", "");
				locationPhone = locationPhone.trim();
			}

			if (!phone.equalsIgnoreCase(locationPhone)) {
				count++;
				phoneColourCode = "R";
				volist.get(i).setField21(phoneColourCode);
			}

			String adressColorCode = "B";

			if (locationAddress == null) {
				locationAddress = "";
			}
			String[] locationAddressArray = locationAddress.split(" ");
			String lastWordinLocationAddress = "";
			if (locationAddressArray.length > 0) {
				lastWordinLocationAddress = locationAddressArray[locationAddressArray.length - 1];
			}

			String[] locationDirectoryAddressArray = address.split(" ");
			String lastWordinDirectoryddress = "";
			if (locationAddressArray.length > 0) {
				lastWordinDirectoryddress = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 1];
				System.out.println("lastWordinDirectoryddress"
						+ lastWordinDirectoryddress);
			}

			if (!locationAddress.equalsIgnoreCase(address)) {

				if (!lastWordinDirectoryddress
						.equalsIgnoreCase(lastWordinLocationAddress)) {
					// get the the Abbrevation
					// boolean isAbbreviationExist = businessservice
					// .isAbbreviationExist(lastWordinDirectoryddress,
					// lastWordinLocationAddress);

					boolean isAbbreviationExist = true; // this is just for now
														// - uncomment above
														// lines

					String secondLastWordInDirectory = "";
					String secondLastWordInLBL = "";

					if (locationAddressArray.length > 1
							&& locationDirectoryAddressArray.length > 1) {
						secondLastWordInLBL = locationAddressArray[locationAddressArray.length - 2];
						secondLastWordInDirectory = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 2];
					}

					boolean isSecondWordsMatch = false;
					if (secondLastWordInLBL != ""
							&& secondLastWordInDirectory != "") {
						isSecondWordsMatch = secondLastWordInLBL
								.equalsIgnoreCase(secondLastWordInDirectory);
					}

					if (!isAbbreviationExist || !isSecondWordsMatch) {
						adressColorCode = "R";

						count++;
					}
				}
			}
			volist.get(i).setField27(adressColorCode);

		}
		return volist;

	}

	public List<AuditEntity> getListOfOverridenListingsByBrand(Date fromDate,
			Date toDate, String brand) {
		List<AuditEntity> auditEntities = new ArrayList<AuditEntity>();
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session.createCriteria(AuditEntity.class);
		createCriteria.add(Restrictions.eq("brandName", brand));
		if (fromDate != null && toDate != null) {
			createCriteria.add(Restrictions.between("date", fromDate, toDate));
		}
		List<AuditEntity> list = createCriteria.list();
		String userName = (String) httpSession.getAttribute("userName");
		List<UsersDTO> userByUserName = getUserByUserName(userName);
		UsersDTO usersDTO = userByUserName.get(0);
		for (AuditEntity auditEntity : list) {
			auditEntity.setUserName(usersDTO.getName() + " "
					+ usersDTO.getLastName());
			auditEntities.add(auditEntity);
		}
		return auditEntities;
	}

	public List<AuditEntity> getListOfOverridenListings(Date fromDate,
			Date toDate) {
		List<AuditEntity> auditEntities = new ArrayList<AuditEntity>();
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session.createCriteria(AuditEntity.class);

		if (fromDate != null && toDate != null) {
			createCriteria.add(Restrictions.between("date", fromDate, toDate));
		}
		List<AuditEntity> list = createCriteria.list();
		logger.info("auditlist" + list.size());
		for (AuditEntity auditEntity : list) {
			auditEntities.add(auditEntity);
		}
		return auditEntities;
	}

	public List<RenewalReportDTO> runRenewalReport(Date fromDate, Date toDate,
			String storeName, String brand) {
		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.MONTH, 1);
		int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		Date endate = calendar.getTime();
		endate.setDate(actualMaximum);
		Calendar cal = Calendar.getInstance();
		cal.setTime(endate);
		Date endDate = cal.getTime();

		Criteria createCriteria = currentSession
				.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.eq("store", storeName));
		createCriteria.add(Restrictions.ne("status", "cancel"));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", startDate,
				endDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}

		return renewalReportDTOs;
	}

	public List<RenewalReportDTO> runRenewalReportForBrand(Date fromDate,
			Date toDate, String brand) {
		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date();
		logger.info("startDate::::::::::::::::::::::::::::::::::" + startDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.MONTH, 1);
		int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		Date endate = calendar.getTime();
		endate.setDate(actualMaximum);
		Calendar cal = Calendar.getInstance();
		cal.setTime(endate);
		Date endDate = cal.getTime();
		logger.info("endDate::::::::::::::::::::::::::::::::::" + endDate);

		Criteria createCriteria = currentSession
				.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.ne("status", "cancel"));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", startDate,
				endDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}

		return renewalReportDTOs;
	}

	public RenewalReportDTO isRenewed(String store, Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session
				.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("store", store));
		createCriteria.add(Restrictions.eq("clientId", clientId));
		List<RenewalReportEntity> list = createCriteria.list();
		RenewalReportDTO renewalReportDTO = null;
		for (RenewalReportEntity renewalReportEntity : list) {
			renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
		}

		return renewalReportDTO;
	}

	public List<RenewalReportDTO> runSummaryReport(Date fromDate, Date toDate,
			String storeName, String brand) {
		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.MONTH, -1);

		Date startDate = calendar.getTime();

		logger.info("startDate::::::::::::::::::::::::::::::::::"
				+ simpleDateFormat.format(startDate));

		logger.info("endDate::::::::::::::::::::::::::::::::::"
				+ simpleDateFormat.format(currentDate));
		Criteria createCriteria = currentSession
				.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.eq("status", "Renewed"));
		createCriteria.add(Restrictions.eq("store", storeName));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", startDate,
				currentDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}

		List<RenewalReportDTO> renewalReportDTOs2 = runSummaryReportForBrandAndStore(
				brand, storeName);
		renewalReportDTOs.addAll(renewalReportDTOs2);
		return renewalReportDTOs;
	}

	public List<RenewalReportDTO> runSummaryReportForBrand(Date fromDate,
			Date toDate, String brand) {

		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.MONTH, -1);

		Date startDate = calendar.getTime();

		logger.info("startDate::::::::::::::::::::::::::::::::::"
				+ simpleDateFormat.format(startDate));

		logger.info("endDate::::::::::::::::::::::::::::::::::"
				+ simpleDateFormat.format(currentDate));
		Criteria createCriteria = currentSession
				.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.eq("status", "Renewed"));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", startDate,
				currentDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}
		List<RenewalReportDTO> runSummaryReportForBrands = runSummaryReportForBrands(brand);
		renewalReportDTOs.addAll(runSummaryReportForBrands);
		return renewalReportDTOs;
	}

	public List<RenewalReportDTO> runSummaryReportForBrand(String brand) {
		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.MONTH, -1);

		Date startDate = calendar.getTime();

		logger.info("startDate::::::::::::::::::::::::::::::::::"
				+ simpleDateFormat.format(startDate));

		logger.info("endDate::::::::::::::::::::::::::::::::::"
				+ simpleDateFormat.format(currentDate));
		Criteria createCriteria = currentSession
				.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.eq("status", "Renewed"));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", startDate,
				currentDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}

		return renewalReportDTOs;
	}

	public List<RenewalReportDTO> runSummaryReportForBrandAndStore(
			String brand, String storeName) {
		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.MONTH, -1);

		Date startDate = calendar.getTime();

		logger.info("startDate::::::::::::::::::::::::::::::::::" + startDate);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(currentDate);
		calendar2.add(Calendar.MONTH, 1);
		// int actualMaximum1 =
		// calendar2.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date endDate = calendar2.getTime();

		logger.info("startDate::::::::::::::::::::::::::::::::::"
				+ simpleDateFormat.format(endDate));

		logger.info("endDate::::::::::::::::::::::::::::::::::"
				+ simpleDateFormat.format(currentDate));
		Criteria createCriteria = currentSession
				.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.eq("status", "Active"));
		createCriteria.add(Restrictions.eq("store", storeName));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", currentDate,
				endDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}

		return renewalReportDTOs;
	}

	public List<RenewalReportDTO> runSummaryReportForBrands(String brand) {
		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.MONTH, -1);

		Date startDate = calendar.getTime();

		logger.info("startDate::::::::::::::::::::::::::::::::::" + startDate);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(currentDate);
		calendar2.add(Calendar.MONTH, 1);
		// int actualMaximum1 =
		// calendar2.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date endDate = calendar2.getTime();

		logger.info("startDate::::::::::::::::::::::::::::::::::"
				+ simpleDateFormat.format(endDate));

		logger.info("endDate::::::::::::::::::::::::::::::::::"
				+ simpleDateFormat.format(currentDate));
		Criteria createCriteria = currentSession
				.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.eq("status", "Active"));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", currentDate,
				endDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}

		return renewalReportDTOs;
	}

	public LocalBusinessDTO runSummaryReportForBrand(String store,
			String brandname) {
		Session session = sessionFactory.getCurrentSession();
		logger.info("storesin dao:::::::::::::::" + store);
		List<String> storeslist = new ArrayList<String>();
		Criteria createCriteria = session
				.createCriteria(LocalBusinessEntity.class);
		createCriteria.add(Restrictions.eq("client", brandname));
		createCriteria.add(Restrictions.eq("store", store));

		List<LocalBusinessEntity> list2 = createCriteria.list();
		LocalBusinessDTO localBusinessDTO = null;
		for (LocalBusinessEntity localBusinessEntity : list2) {

			localBusinessDTO = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessEntity, localBusinessDTO);
		}
		return localBusinessDTO;
	}

	public List<LblErrorDTO> getListOfBusinessErrorInfoByStore(String flag,
			Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> businessDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LblErrorEntity";
		List<String> allBrandNames = new ArrayList<String>();
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// String brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by store " + flag;

			}

			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}

				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

				}

				sb.append(" order by store " + flag);
				sql += sb.toString();
			}

		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by store " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by store " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of error businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LblErrorEntity> businessEntities = query.list();
		logger.info("total errored businesses are: " + businessEntities.size());
		for (LblErrorEntity localBusinessBean : businessEntities) {
			LblErrorDTO dto = new LblErrorDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;

	}

	public List<LblErrorDTO> getListOfBusinessErrorInfoByBusinessName(
			String flag, Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> businessDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LblErrorEntity";
		List<String> allBrandNames = new ArrayList<String>();
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// String brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by companyName " + flag;

			}

			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}

				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

				}

				sb.append(" order by companyName " + flag);
				sql += sb.toString();
			}

		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by companyName " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by companyName " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of error businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LblErrorEntity> businessEntities = query.list();
		logger.info("total errored businesses are: " + businessEntities.size());
		for (LblErrorEntity localBusinessBean : businessEntities) {
			LblErrorDTO dto = new LblErrorDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;

	}

	public List<LblErrorDTO> getListOfBusinessErrorInfoByAddress(String flag,
			Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> businessDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LblErrorEntity";
		List<String> allBrandNames = new ArrayList<String>();
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// String brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by locationAddress " + flag;

			}

			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}

				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

				}

				sb.append(" order by locationAddress " + flag);
				sql += sb.toString();
			}

		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by locationAddress " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationAddress " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of error businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LblErrorEntity> businessEntities = query.list();
		logger.info("total errored businesses are: " + businessEntities.size());
		for (LblErrorEntity localBusinessBean : businessEntities) {
			LblErrorDTO dto = new LblErrorDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;

	}

	public List<LblErrorDTO> getListOfBusinessErrorInfoByCity(String flag,
			Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> businessDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LblErrorEntity";
		List<String> allBrandNames = new ArrayList<String>();
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// String brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by locationCity " + flag;

			}

			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}

				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

				}

				sb.append(" order by locationCity " + flag);
				sql += sb.toString();
			}

		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by locationCity " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationCity " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of error businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LblErrorEntity> businessEntities = query.list();
		logger.info("total errored businesses are: " + businessEntities.size());
		for (LblErrorEntity localBusinessBean : businessEntities) {
			LblErrorDTO dto = new LblErrorDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;

	}

	public List<LblErrorDTO> getListOfBusinessErrorInfoByState(String flag,
			Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> businessDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LblErrorEntity";
		List<String> allBrandNames = new ArrayList<String>();
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// String brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by locationState " + flag;

			}

			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}

				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

				}

				sb.append(" order by locationState " + flag);
				sql += sb.toString();
			}

		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by locationState " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationState " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of error businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LblErrorEntity> businessEntities = query.list();
		logger.info("total errored businesses are: " + businessEntities.size());
		for (LblErrorEntity localBusinessBean : businessEntities) {
			LblErrorDTO dto = new LblErrorDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;

	}

	public List<LblErrorDTO> getListOfBusinessErrorInfoByZip(String flag,
			Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> businessDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LblErrorEntity";
		List<String> allBrandNames = new ArrayList<String>();
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// String brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by locationZipCode " + flag;

			}

			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}

				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

				}

				sb.append(" order by locationZipCode " + flag);
				sql += sb.toString();
			}

		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by locationZipCode " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationZipCode " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of error businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LblErrorEntity> businessEntities = query.list();
		logger.info("total errored businesses are: " + businessEntities.size());
		for (LblErrorEntity localBusinessBean : businessEntities) {
			LblErrorDTO dto = new LblErrorDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;

	}

	public List<LblErrorDTO> getListOfBusinessErrorInfoByPhone(String flag,
			Map<String, String> fmap) {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> businessDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LblErrorEntity";
		List<String> allBrandNames = new ArrayList<String>();
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN
					|| roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session
						.createCriteria(UsersEntity.class);
				userCriteria.add(Restrictions.eq("userName", userName));
				List<UsersEntity> usersEntities = userCriteria.list();
				UsersEntity usersEntity = new UsersEntity();
				if (usersEntities != null && !usersEntities.isEmpty()) {
					usersEntity = usersEntities.get(0);
				}
				// String brand = null;
				List<Integer> listbrandID = new ArrayList<Integer>();
				Integer userID = usersEntity.getUserID();
				Criteria criteria = session.createCriteria(User_brands.class);
				criteria.add(Restrictions.eq("userID", userID));
				List<User_brands> list2 = criteria.list();
				for (User_brands user_brands : list2) {
					Integer brandID = user_brands.getBrandID();
					listbrandID.add(brandID);
				}
				allBrandNames = getBrandById(listbrandID);
			}
			int totalBrands = allBrandNames.size();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}
				sql += " order by locationPhone " + flag;

			}

			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				sql += " where (";
				int index = 0;
				for (String brand : allBrandNames) {
					if (index != 0) {
						sql += " OR";
					}
					sql += " client=?";
					if (totalBrands == index + 1) {
						sql += " )";
					}
					brandsMap.put(index, brand);
					index++;

				}

				StringBuffer sb = new StringBuffer();
				sb.append(" AND ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

				}

				sb.append(" order by locationPhone " + flag);
				sql += sb.toString();
			}

		} else if (roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN) {

			allBrandNames = getAllBrandNames();
			if (allBrandNames != null && allBrandNames.size() > 0
					&& fmap.isEmpty()) {
				sql += " order by locationPhone " + flag;
			}
			if (allBrandNames != null && allBrandNames.size() > 0
					&& !fmap.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				sb.append(" WHERE ");
				int size = fmap.size();
				int count = 0;
				for (Map.Entry<String, String> entry : fmap.entrySet()) {
					count++;

					sb.append(entry.getKey() + " like '%" + entry.getValue()
							+ "%' ");

					if (size > 1 && count != size) {
						sb.append(" AND ");
					}

					// logger.info(entry.getKey() + "/" + entry.getValue());
				}

				sb.append(" order by locationPhone " + flag);
				sql += sb.toString();
			}
		}

		logger.debug("Fetching the list of error businesses with the query applied: "
				+ sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<LblErrorEntity> businessEntities = query.list();
		logger.info("total errored businesses are: " + businessEntities.size());
		for (LblErrorEntity localBusinessBean : businessEntities) {
			LblErrorDTO dto = new LblErrorDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			String companyName = dto.getCompanyName();

			if (companyName != null && companyName.contains("–")) {
				companyName = companyName.replaceAll("\\p{Pd}", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			if (companyName != null && companyName.contains("?")) {
				companyName = companyName.replaceAll("\\?", "-");
				// logger.info("replaceAll:::::::::"+companyName);

			}
			dto.setCompanyName(companyName);
			businessDTOs.add(dto);
		}
		return businessDTOs;

	}

	public Map<String, Integer> getisDirectoryExist(String store,
			String brandName) {
		Integer brandId = getClinetIdIdByName(brandName);
		Map<String, Integer> countDetails = new HashMap<String, Integer>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CheckReportEntity.class);
		criteria.add(Restrictions.eq("store", store));

		criteria.add(Restrictions.eq("brandId", brandId));
		criteria.addOrder(Order.desc("checkedDate"));
		Integer errorcount = 100;
		List<CheckReportEntity> list = criteria.list();
		for (CheckReportEntity entity : list) {

			if (countDetails.size() >= 10)
				break;

			String directory = entity.getDirectory();

			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("google")) {
				countDetails.put(directory, 100);
			}

			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("foursquare")) {

				countDetails.put(directory, 100);
			}

			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("yahoo")) {

				countDetails.put(directory, 100);
			}

			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("yelp")) {

				countDetails.put(directory, errorcount);
			}

			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("yp")) {

				countDetails.put(directory, errorcount);
			}

			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("citysearch")) {

				countDetails.put(directory, errorcount);
			}

			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("mapquest")) {

				countDetails.put(directory, errorcount);
			}

			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("usplaces")) {

				countDetails.put(directory, errorcount);
			}

			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("yellowbot")) {

				countDetails.put(directory, errorcount);
			}

			if (!countDetails.containsKey(directory)
					&& directory.equalsIgnoreCase("ezlocal")) {

				countDetails.put(directory, errorcount);
			}

		}

		if (countDetails.get("google") == null) {
			countDetails.put("google", 0);
		}
		if (countDetails.get("foursquare") == null) {
			countDetails.put("foursquare", 0);
		}
		if (countDetails.get("yahoo") == null) {
			countDetails.put("yahoo", 0);
		}
		if (countDetails.get("google") == null) {
			countDetails.put("google", 0);
		}
		if (countDetails.get("yelp") == null) {
			countDetails.put("yelp", 0);
		}
		if (countDetails.get("yp") == null) {
			countDetails.put("yp", 0);
		}
		if (countDetails.get("citysearch") == null) {
			countDetails.put("citysearch", 0);
		}
		if (countDetails.get("mapquest") == null) {
			countDetails.put("mapquest", 0);
		}
		if (countDetails.get("usplaces") == null) {
			countDetails.put("usplaces", 0);
		}
		if (countDetails.get("yellowbot") == null) {
			countDetails.put("yellowbot", 0);
		}
		if (countDetails.get("ezlocal") == null) {
			countDetails.put("ezlocal", 0);
		}

		// logger.info("countDetails :::" + countDetails.size());

		return countDetails;

	}

	public AccuracyReportEntity getpercentageForStoreCount(String store) {
		Map<String, Integer> countDetails = new HashMap<String, Integer>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(AccuracyReportEntity.class);
		criteria.add(Restrictions.eq("store", store));
		List<AccuracyReportEntity> list = criteria.list();
		AccuracyReportEntity accuracyReportEntity = null;
		if (list.size() > 0) {
			accuracyReportEntity = list.get(0);
		}

		return accuracyReportEntity;

	}

	public Integer getCountForBrand(Integer client,
			List<UploadBusinessBean> listDataFromXLS) {

		Integer count = 0;

		BrandInfoDTO brandInfo = getBrandInfoByBrandId(client);
		List<String> dupstores = new ArrayList<String>();
		List<String> stores = new ArrayList<String>();
		List<String> deletedStores = new ArrayList<String>();
		List<LocalBusinessDTO> storesByBrandName = getStoresByBrandName(brandInfo
				.getBrandName());

		for (UploadBusinessBean uploadBusinessBean : listDataFromXLS) {
			for (LocalBusinessDTO localBusinessDTO : storesByBrandName) {
				if (uploadBusinessBean.getStore().equalsIgnoreCase(
						localBusinessDTO.getStore())
						&& uploadBusinessBean.getClientId().equals(
								localBusinessDTO.getClientId())) {
					if (uploadBusinessBean.getActionCode()
							.equalsIgnoreCase("D")) {
						if (!deletedStores
								.contains(localBusinessDTO.getStore())) {
							deletedStores.add(localBusinessDTO.getStore());
						}

					} else {
						if (!dupstores.contains(localBusinessDTO.getStore())) {
							dupstores.add(localBusinessDTO.getStore());
						}

					}

				} else {
					if (!stores.contains(localBusinessDTO.getStore())) {
						stores.add(localBusinessDTO.getStore());
					}

				}
			}

		}
		count = stores.size() - dupstores.size() - deletedStores.size();

		return count;
	}

	private BrandInfoDTO getBrandInfoByBrandId(Integer client) {
		BrandInfoDTO brandInfoDTO = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("clientId", client));
		List<BrandEntity> list = criteria.list();

		for (BrandEntity brandEntity : list) {
			brandInfoDTO = new BrandInfoDTO();
			BeanUtils.copyProperties(brandEntity, brandInfoDTO);

		}

		return brandInfoDTO;
	}

	public boolean isBusinessExcelRecordUnique(LocalBusinessDTO excelRecord) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("store", excelRecord.getStore()));
		criteria.add(Restrictions.eq("clientId", excelRecord.getClientId()));
		criteria.add(Restrictions.eq("locationAddress",
				excelRecord.getLocationAddress()));
		criteria.add(Restrictions.eq("locationPhone",
				excelRecord.getLocationPhone()));
		criteria.add(Restrictions.eq("locationState",
				excelRecord.getLocationState()));
		criteria.add(Restrictions.eq("locationZipCode",
				excelRecord.getLocationZipCode()));
		criteria.add(Restrictions.eq("locationCity",
				excelRecord.getLocationCity()));
		criteria.add(Restrictions.eq("suite", excelRecord.getSuite()));
		criteria.add(Restrictions.eq("companyName",
				excelRecord.getCompanyName()));
		List<LocalBusinessEntity> businessEntities = criteria.list();
		return (businessEntities != null && !(businessEntities.isEmpty()));
	}

	public LocalBusinessDTO getListingByStore(String store, String clientId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("clientId", Integer.parseInt(clientId)));
		criteria.add(Restrictions.eq("store", store));
		List<LocalBusinessEntity> list = criteria.list();
		int size = list.size();
		// logger.info("size:::::::::::::::::::::" + size);
		LocalBusinessDTO dto = new LocalBusinessDTO();
		if (size > 0) {
			BeanUtils.copyProperties(list.get(0), dto);
			return dto;
		}

		return null;

	}

	public boolean updateBusinessWithGoogleMB(String clientId,
			List<Location> locations) {
		Session session = sessionFactory.getCurrentSession();
		
		int missingStores = 0;

		for (Location location : locations) {

			String storeCode = location.getStoreCode();
			String name = location.getName();
			String[] locationdetails = name.split("/");
			String googleAccountId = locationdetails[1];
			String googleLocationId = locationdetails[3];
			LocalBusinessDTO listingByStore = getListingByStore(storeCode,
					clientId);
			if (listingByStore == null) {
				missingStores++;
				logger.info("Listing with store: " + storeCode
						+ ", doesnot found in LBL");
				continue;
			} else if (listingByStore != null
					&& listingByStore.getGoogleLocationId() == null) {
				listingByStore.setGoogleAccountId(googleAccountId);

				listingByStore.setGoogleLocationId(googleLocationId);
				listingByStore.setStore(listingByStore.getStore());
				// update location id's in LBL with GMB location id's
				/*
				 * logger.info("Listing with store: " + storeCode +
				 * ", found in LBL");
				 */
				updateBusinessInfo(listingByStore);
			} else {
				/*
				 * logger.info(
				 * "Listing is already updated with GMB location Details:" +
				 * storeCode);
				 */
				continue;
			}
		}
		logger.info("The number of listings found in GMB and doe snot found in LBL are: "+ missingStores + " . for brand: "+ clientId);
		return true;
	}

	/**
	 * updateListingError
	 */
	public boolean updateBusinessInfoWithGoogleId(
			LocalBusinessDTO listingByStore) {
		Session session = sessionFactory.getCurrentSession();

		LocalBusinessEntity bean = (LocalBusinessEntity) session.get(
				LocalBusinessEntity.class, listingByStore.getId());
		BeanUtils.copyProperties(listingByStore, bean);

		logger.info("Updating store " + listingByStore.getGoogleLocationId()
				+ " with GoogleId: " + listingByStore.getGoogleLocationId());
		session.update(bean);

		return true;
	}

	public List<LocalBusinessDTO> getLocationsByGoogleAccount(
			String googleAccountId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("googleAccountId", googleAccountId));
		List<LocalBusinessEntity> businessEntities = criteria.list();
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		for (LocalBusinessEntity localBusinessEntity : businessEntities) {
			LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();

			BeanUtils.copyProperties(localBusinessEntity, localBusinessDTO);
			businessDTOs.add(localBusinessDTO);
		}
		return businessDTOs;
	}

	public LocalBusinessDTO getLocationByGoogleAccount(String googleAccountId,
			String locationId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("googleAccountId", googleAccountId));
		criteria.add(Restrictions.eq("googleLocationId", locationId));
		List<LocalBusinessEntity> businessEntities = criteria.list();

		int size = businessEntities.size();
		LocalBusinessDTO dto = new LocalBusinessDTO();
		if (size > 0) {
			BeanUtils.copyProperties(businessEntities.get(0), dto);
			return dto;
		}

		return null;
	}

	public void addInsightGraphDetails(LocalBusinessDTO dto,
			Date formattedEndDate, String googleAccountId,
			String googleLocationId, Long directCount, Long inDirectCount,
			Long searchCount, Long mapCount, Long callsCount,
			Long directionsCount, Long websiteCount) {
		Session session = sessionFactory.getCurrentSession();
		InsightsGraphEntity insightsEntity = new InsightsGraphEntity();

		if (dto != null) {

			Integer clientId = dto.getClientId();
			insightsEntity.setBrandId(clientId);
			insightsEntity.setBrandName(dto.getClient());
			String store = dto.getStore();
			// delete existing records
			// deleteExistingRecords(clientId, store, formattedEndDate);
			// add details now
			insightsEntity.setDate(formattedEndDate);
			insightsEntity.setState(dto.getLocationState());

			insightsEntity.setStore(store);
			insightsEntity.setLblStoreId(dto.getLblStoreId());
			insightsEntity.setCity(dto.getLocationCity());

			insightsEntity.setDirectCount(directCount);
			insightsEntity.setInDirectCount(inDirectCount);
			insightsEntity.setTotalSearchCount(directCount + inDirectCount);

			insightsEntity.setSearchCount(searchCount);
			insightsEntity.setMapCount(mapCount);
			insightsEntity.setTotalViewCount(searchCount + mapCount);

			insightsEntity.setWebsiteCount(websiteCount);
			insightsEntity.setCallsCount(callsCount);
			insightsEntity.setDirectionsCount(directionsCount);
			insightsEntity.setTotalActionCount(websiteCount + callsCount
					+ directionsCount);

			session.save(insightsEntity);

		}

	}

	public void deleteExistingRecords(Integer clientId, Date formattedEndDate) {
		Session session = sessionFactory.getCurrentSession();
		java.sql.Timestamp sqlDate = new java.sql.Timestamp(
				formattedEndDate.getTime());
		Query createQuery = session
				.createQuery("delete from InsightsGraphEntity where brandId =? and date=?");
		createQuery.setInteger(0, clientId);
		createQuery.setTimestamp(1, sqlDate);

		int result = createQuery.executeUpdate();
		logger.info("Total records deleted are: " + result + ", for Brand: "
				+ clientId + ", date is: " + sqlDate);
	}
	

	public void deleteExistingRecords(Integer clientId, Date formattedDate,
			Date formattedDate2) {
		Session session = sessionFactory.getCurrentSession();
		java.sql.Timestamp startDate = new java.sql.Timestamp(
				formattedDate.getTime());
		
		java.sql.Timestamp endDate = new java.sql.Timestamp(
				formattedDate2.getTime());
		
		Query createQuery = session
				.createQuery("delete from InsightsGraphEntity where brandId =? and date>=? and date<=?");
		createQuery.setInteger(0, clientId);
		createQuery.setTimestamp(1, startDate);
		createQuery.setTimestamp(2, endDate);

		int result = createQuery.executeUpdate();
		logger.info("Total records deleted are: " + result + ", for Brand: "
				+ clientId + ", from date : " + startDate + "to " + endDate  +", is " + result);
		
	}
	
	public static Date[] getStartandEndDatesOfCurrentMonth() {
		
		Date[] dates = new Date[2];
		
		Calendar calendar = Calendar.getInstance();         
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date startDate = calendar.getTime();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date endDate = calendar.getTime();
		
		dates[0]=startDate;
		dates[1]=endDate;
		
		System.out.println("startDate: "+ startDate + ", endDate: "+ endDate);
		return dates;
	}
	
	
	public void deleteCurrentMonthCountsForStore(String store, Integer brandId, Date startDate, Date endDate){
		Session session = sessionFactory.getCurrentSession();
		Query createQuery = session.createQuery("delete from InsightsMonthlyCountEntity where store=? and brandId=? and date>=? and date<=? ");
		createQuery.setString(0, store);
		createQuery.setInteger(1, brandId);
		createQuery.setDate(2, startDate);
		createQuery.setDate(3, endDate);
		int executeUpdate = createQuery.executeUpdate();
		logger.info("Total records deleted are: "+ executeUpdate );
	}
	

	public void updateInsightMonthlyCountsForStore(LocalBusinessDTO dto,
			String accountId, String googleLocationId) {
		// TODO Auto-generated method stub
		Date[] startandEndDatesOfCurrentMonth = getStartandEndDatesOfCurrentMonth();
		
		// delete current month accounts
		
		deleteCurrentMonthCountsForStore(dto.getStore(), dto.getClientId() ,startandEndDatesOfCurrentMonth[0], startandEndDatesOfCurrentMonth[1] );
		
		// add new row for current month
		List<InsightsMonthlyCountDTO> currentMonthCountForStore = getMonthlyCountsForStore(dto.getClientId(), dto.getStore(), startandEndDatesOfCurrentMonth[0], startandEndDatesOfCurrentMonth[1]);
		
		
		Session session = sessionFactory.getCurrentSession();
		InsightsMonthlyCountDTO totalDto = new InsightsMonthlyCountDTO();
		
		Long viewCount= 0L;
		Long searchCount= 0L;
		Long actionCount= 0L;

		InsightsMonthlyCountEntity monthlyEntity = new InsightsMonthlyCountEntity();
		for (InsightsMonthlyCountDTO insightsMonthlyCountDTO : currentMonthCountForStore) {
			BeanUtils.copyProperties(insightsMonthlyCountDTO, totalDto);
			viewCount = viewCount + insightsMonthlyCountDTO.getViewCount();
			searchCount =  searchCount + insightsMonthlyCountDTO.getSearchCount();
			actionCount =  actionCount + insightsMonthlyCountDTO.getActionCount();
		}
		
		totalDto.setDate(new Date());
		totalDto.setActionCount(actionCount);
		totalDto.setViewCount(viewCount);
		totalDto.setSearchCount(searchCount);

		BeanUtils.copyProperties(totalDto, monthlyEntity);
		session.save(monthlyEntity);
		
		
	}
	
	public List<InsightsMonthlyCountDTO> getMonthlyCountsForStore(
			Integer clientId, String store, Date startDate, Date endDate)  {

		Session session = sessionFactory.getCurrentSession();
		List<InsightsMonthlyCountDTO> trendData = new ArrayList<InsightsMonthlyCountDTO>();

		String query = "";
		Query createQuery = null;

		query = "SELECT date, sum(totalSearchCount) as searches, sum(totalViewCount) as views, sum(totalActionCount) as actions, store, state, lblStoreId, brandId "
				+ "from InsightsGraphEntity where brandId=? and store=? and  date >= ? and date <= ?";
		
				createQuery = session.createQuery(query);
		createQuery.setInteger(0, clientId);
		createQuery.setString(1, store);
		
		createQuery.setDate(2, startDate);
		createQuery.setDate(3, endDate);

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
	
	
	public InsightsMonthlyCountDTO getCurrentMonthCountForStore(
			Integer clientId, String store, Date startDate, Date endDate) {

		Session session = sessionFactory.getCurrentSession();
		InsightsMonthlyCountDTO history = new InsightsMonthlyCountDTO();

		String query = "";
		Query createQuery = null;

		query = "SELECT date, sum(totalSearchCount) as searches, sum(totalViewCount) as views, sum(totalActionCount) as actions, store, state, lblStoreId, brandId "
				+ "from InsightsGraphEntity where brandId=? and store=? and  date >= ? and date <= ?";
		
				createQuery = session.createQuery(query);
		createQuery.setInteger(0, clientId);
		createQuery.setString(1, store);
		
		createQuery.setDate(2, startDate);
		createQuery.setDate(3, endDate);

		List<Object[]> dataCount = createQuery.list();

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
	

	private List<InsightsGraphEntity> getInsightDetails(Integer clientId,
			String store, Date formattedEndDate) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(InsightsGraphEntity.class);
		criteria.add(Restrictions.eq("brandId", clientId));
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("date", formattedEndDate));

		List<InsightsGraphEntity> insights = criteria.list();
		return insights;

	}

	/**
	 * getAllStates
	 */
	public List<String> getAllStates() {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT DISTINCT code FROM StatesListEntity";
		Query createQuery = session.createQuery(sql);
		List<String> stateCodes = createQuery.list();
		return stateCodes;
	}

	public List<String> getAllStatesListByBrand(String brand) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT DISTINCT locationState FROM LocalBusinessEntity where client = ?";
		Query createQuery = session.createQuery(sql);
		createQuery.setString(0, brand);
		List<String> stateCodes = createQuery.list();
		return stateCodes;
	}

	public Map<String, Long> getInsightsData(String brand, String store,
			Date startDate, Date endDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		endDate = cal.getTime();

		Session session = sessionFactory.getCurrentSession();

		String query = "SELECT sum(directCount) as direct, sum(inDirectCount) as indirects, sum(searchCount) as searches,sum(mapCount) as maps, sum(websiteCount) as websites, sum(directionsCount) as directions, sum(callsCount) as calls "
				+ "FROM InsightsGraphEntity where date>=? and date<=? and brandName=? and store=?";

		Query createQuery = session.createQuery(query.toString());
		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setString(2, brand);
		createQuery.setString(3, store);

		List<Object[]> dataCount = createQuery.list();
		Map<String, Long> insightsDataMap = new HashMap<String, Long>();
		for (Object[] objects : dataCount) {
			insightsDataMap.put("QUERIES_DIRECT",
					Long.valueOf(objects[0].toString()));
			insightsDataMap.put("QUERIES_INDIRECT",
					Long.valueOf(objects[1].toString()));

			insightsDataMap.put("VIEWS_SEARCH",
					Long.valueOf(objects[2].toString()));
			insightsDataMap.put("VIEWS_MAPS",
					Long.valueOf(objects[3].toString()));

			insightsDataMap.put("ACTIONS_WEBSITE",
					Long.valueOf(objects[4].toString()));
			insightsDataMap.put("ACTIONS_DRIVING_DIRECTIONS",
					Long.valueOf(objects[5].toString()));
			insightsDataMap.put("ACTIONS_PHONE",
					Long.valueOf(objects[6].toString()));
		}

		return insightsDataMap;

	}

	public Map<String, Long> getInsightsDataForBrandAndStore(Integer brand,
			String store, Date startDate, Date endDate) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		endDate = cal.getTime();

		Session session = sessionFactory.getCurrentSession();

		String query = "SELECT sum(directCount) as direct, sum(inDirectCount) as indirects, sum(searchCount) as searches,sum(mapCount) as maps, sum(websiteCount) as websites, sum(directionsCount) as directions, sum(callsCount) as calls "
				+ "FROM InsightsGraphEntity where date>=? and date<=? and brandId=? and store=?";

		Query createQuery = session.createQuery(query.toString());
		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setInteger(2, brand);
		createQuery.setString(3, store);

		List<Object[]> dataCount = createQuery.list();
		Map<String, Long> insightsDataMap = new HashMap<String, Long>();
		for (Object[] objects : dataCount) {
			if (objects[0] != null) {
				insightsDataMap.put("QUERIES_DIRECT",
						Long.valueOf(objects[0].toString()));
				insightsDataMap.put("QUERIES_INDIRECT",
						Long.valueOf(objects[1].toString()));

				insightsDataMap.put("VIEWS_SEARCH",
						Long.valueOf(objects[2].toString()));
				insightsDataMap.put("VIEWS_MAPS",
						Long.valueOf(objects[3].toString()));

				insightsDataMap.put("ACTIONS_WEBSITE",
						Long.valueOf(objects[4].toString()));
				insightsDataMap.put("ACTIONS_DRIVING_DIRECTIONS",
						Long.valueOf(objects[5].toString()));
				insightsDataMap.put("ACTIONS_PHONE",
						Long.valueOf(objects[6].toString()));
			}
		}
		System.out.println("from qurty: "+ insightsDataMap.get("QUERIES_DIRECT"));
		return insightsDataMap;
	}

	/**
	 * to collect insight details for Brand
	 */
	public Map<String, Long> getInsightsBrandData(Integer brandId, String state,
			Date startDate, Date endDate) {
		


		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();

		String query = "SELECT sum(directCount) as direct, sum(inDirectCount) as indirects, sum(searchCount) as searches,sum(mapCount) as maps, sum(websiteCount) as websites, sum(directionsCount) as directions, sum(callsCount) as calls "
				+ "FROM InsightsGraphEntity where date>=? and  date<=? and brandId=?";

		String stateQuery = "";
		if (state != null && state.length() > 0) {
			stateQuery = " and state=?";
		}

		sb.append(query);
		if (state != null && state.length() > 0) {
			sb.append(stateQuery);
		}

		Query createQuery = session.createQuery(sb.toString());
		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setInteger(2, brandId);
		if (state != null && state.length() > 0) {
			createQuery.setString(3, state);
		}

		List<Object[]> dataCount = createQuery.list();
		Map<String, Long> insightsDataMap = new HashMap<String, Long>();
		Long directCount = 0L;
		for (Object[] objects : dataCount) {
			if (objects[0] != null) {
				insightsDataMap.put("QUERIES_DIRECT",
						Long.valueOf(objects[0].toString()));
				insightsDataMap.put("QUERIES_INDIRECT",
						Long.valueOf(objects[1].toString()));

				insightsDataMap.put("VIEWS_SEARCH",
						Long.valueOf(objects[2].toString()));
				insightsDataMap.put("VIEWS_MAPS",
						Long.valueOf(objects[3].toString()));
				insightsDataMap.put("ACTIONS_WEBSITE",
						Long.valueOf(objects[4].toString()));
				insightsDataMap.put("ACTIONS_DRIVING_DIRECTIONS",
						Long.valueOf(objects[5].toString()));
				insightsDataMap.put("ACTIONS_PHONE",
						Long.valueOf(objects[6].toString()));
			}
		}
		
		System.out.println("from hostory: "+insightsDataMap.get("ACTIONS_PHONE"));

		return insightsDataMap;
	}

	public List<InsightsGraphDTO> getInsightsBrandExcelData(String brand,
			String state, Date startDate, Date endDate) {

		List<InsightsGraphDTO> excelData = new ArrayList<InsightsGraphDTO>();


		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();

		String query = "SELECT brandId,store,city,state,sum(directCount) as direct, sum(inDirectCount) as indirects, sum(searchCount) as searches,sum(mapCount) as maps, sum(websiteCount) as websites, sum(directionsCount) as directions, sum(callsCount) as calls"
				+ " from InsightsGraphEntity where date>=? and date<=? and brandName=? ";

		String stateQuery = "";
		if (state != null && state.length() > 0) {
			stateQuery = " and state=?";
		}

		String groupQuery = " group by store";

		sb.append(query);
		if (state != null && state.length() > 0) {
			sb.append(stateQuery);
		}
		sb.append(groupQuery);

		Query createQuery = session.createQuery(sb.toString());
		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setString(2, brand);
		if (state != null && state.length() > 0) {
			createQuery.setString(3, state);
		}

		List<Object[]> dataCount = createQuery.list();
		Long directCount = 0L;
		Map<String, InsightsGraphDTO> dailyMap = new TreeMap<String, InsightsGraphDTO>();
		for (Object[] objects : dataCount) {
			InsightsGraphDTO history = new InsightsGraphDTO();
			if (objects[0] != null) {
				history.setBrandId(Integer.valueOf(objects[0].toString()));
				history.setStore(objects[1].toString());
				history.setCity(objects[2].toString());
				history.setState(objects[3].toString());
				history.setDirectCount(Long.valueOf(objects[4].toString()));
				history.setInDirectCount(Long.valueOf(objects[5].toString()));
				history.setSearchCount(Long.valueOf(objects[6].toString()));
				history.setMapCount(Long.valueOf(objects[7].toString()));
				history.setWebsiteCount(Long.valueOf(objects[8].toString()));
				history.setDirectionsCount(Long.valueOf(objects[9].toString()));
				history.setCallsCount(Long.valueOf(objects[10].toString()));
				excelData.add(history);
			}
		}
	

		return excelData;
	}

	public List<InsightsGraphDTO> getMonthlyReportData(String brand, String type) {
		// TODO Auto-generated method stub
		List<InsightsGraphDTO> excelData = new ArrayList<InsightsGraphDTO>();

		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();
		String query = "";
		boolean isStore = false;
		if (type.equalsIgnoreCase("location")) {

			query = "SELECT store,date, sum(totalSearchCount) as searches, sum(totalViewCount) as views, sum(totalActionCount) as actions"
					+ " from InsightsGraphEntity where brandName=? GROUP BY CONCAT(MONTH(date) ,'', store) order by store,date ";
			isStore = true;
		} else {
			query = "SELECT state,date, sum(totalSearchCount) as searches, sum(totalViewCount) as views, sum(totalActionCount) as actions"
					+ " from InsightsGraphEntity where brandName=? GROUP BY CONCAT(MONTH(date) ,'', state) order by state,date";
		}

		Query createQuery = session.createQuery(query);
		createQuery.setString(0, brand);

		List<Object[]> dataCount = createQuery.list();
		Map<String, InsightsGraphDTO> dailyMap = new TreeMap<String, InsightsGraphDTO>();
		for (Object[] objects : dataCount) {
			InsightsGraphDTO history = new InsightsGraphDTO();
			if (objects[0] != null) {

				if (isStore)
					history.setStore(objects[0].toString());
				else
					history.setState(objects[0].toString());

				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.S");
				Date date = null;
				try {
					date = format.parse(objects[1].toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				history.setDate(date);
				history.setTotalSearchCount(Long.valueOf(objects[2].toString()));
				history.setTotalViewCount(Long.valueOf(objects[3].toString()));
				history.setTotalActionCount(Long.valueOf(objects[4].toString()));
				excelData.add(history);
			}
		}

		return excelData;
	}

	public List<InsightsGraphDTO> getMonthlyTrends(Integer brandId, String state) {
		List<InsightsGraphDTO> trendData = new ArrayList<InsightsGraphDTO>();
		Session session = sessionFactory.getCurrentSession();
		
		String query = "";
		Query createQuery = null;
		if (state != null && state.length() > 0) {
			query = "SELECT date, sum(searchCount) as searches, sum(viewCount) as views, sum(actionCount) as actions "
					+ "from InsightsMonthlyCountEntity where brandId=? and state=? GROUP BY YEAR(date), MONTH(date)";
			createQuery = session.createQuery(query);
			createQuery.setInteger(0, brandId);
			createQuery.setString(1, state);
		} else {
			query = "SELECT date, sum(searchCount) as searches, sum(viewCount) as views, sum(actionCount) as actions "
					+ "from InsightsMonthlyCountEntity where brandId=? GROUP BY YEAR(date), MONTH(date)";
			createQuery = session.createQuery(query);
			createQuery.setInteger(0, brandId);
		}
		List<Object[]> dataCount = createQuery.list();
		Map<String, InsightsGraphDTO> dailyMap = new TreeMap<String, InsightsGraphDTO>();
		for (Object[] objects : dataCount) {
			InsightsGraphDTO history = new InsightsGraphDTO();
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
				history.setTotalSearchCount(Long.valueOf(objects[1].toString()));
				history.setTotalViewCount(Long.valueOf(objects[2].toString()));
				history.setTotalActionCount(Long.valueOf(objects[3].toString()));
				trendData.add(history);
			}
		}
		return trendData;
	}

	public List<InsightsGraphDTO> getViewsHistoryByWeek(String state,
			String brand, String store, Date startDate, Date endDate) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(InsightsGraphEntity.class);
		
		if (store != null && store.length() > 0) {
			criteria.add(Restrictions.eq("store", store));
		}
		criteria.add(Restrictions.eq("brandName", brand));
		criteria.add(Restrictions.ge("date", startDate));
		criteria.add(Restrictions.le("date", endDate));

		List<InsightsGraphDTO> insightsGraphsList = new ArrayList<InsightsGraphDTO>();
		List<InsightsGraphEntity> businessEntities = criteria.list();

		for (InsightsGraphEntity entity : businessEntities) {
			InsightsGraphDTO dto = new InsightsGraphDTO();
			BeanUtils.copyProperties(entity, dto);
			Date date = dto.getDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);

			dto.setDay(day);
			dto.setMonth(month);
			dto.setYear(year);
			insightsGraphsList.add(dto);

		}
		return insightsGraphsList;
	}

	public List<InsightsGraphDTO> getBrandViewsHistoryByWeek(String brand,
			String state, Date startDate, Date endDate) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(InsightsGraphEntity.class);

		if (state != null && state.length() > 0) {
			criteria.add(Restrictions.eq("state", state));
		}
		criteria.add(Restrictions.eq("brandName", brand));
		criteria.add(Restrictions.ge("date", startDate));
		criteria.add(Restrictions.le("date", endDate));

		List<InsightsGraphEntity> businessEntities = criteria.list();
		List<InsightsGraphDTO> insightsGraphsList = new ArrayList<InsightsGraphDTO>();
		for (InsightsGraphEntity entity : businessEntities) {

			InsightsGraphDTO dto = new InsightsGraphDTO();
			BeanUtils.copyProperties(entity, dto);
			Date date = dto.getDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);

			dto.setDay(day);
			dto.setMonth(month);
			dto.setYear(year);
			insightsGraphsList.add(dto);

		}

		return insightsGraphsList;
	}

	public Map<String, InsightsGraphDTO> getHistoryForStore(Integer brandId,
			String store, Date startDate, Date endDate) {


		Session session = sessionFactory.getCurrentSession();


		//Long lblStorByCleintIdandStore = getLBLStorByCleintIdandStore(brandId, store);

		String query = "SELECT date, sum(searchCount) as searches,sum(mapCount) as maps, sum(websiteCount) as websites, sum(directionsCount) as directions, sum(callsCount) as calls "
				+ "FROM InsightsGraphEntity where date>=? and date<=? and brandId=? and store=? group by date";

		Query createQuery = session.createQuery(query);
		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setInteger(2, brandId);
		createQuery.setString(3, store);

		List<Object[]> dataCount = createQuery.list();
		Map<String, InsightsGraphDTO> dailyMap = new TreeMap<String, InsightsGraphDTO>();
		for (Object[] objects : dataCount) {
			InsightsGraphDTO history = new InsightsGraphDTO();
			if (objects[0] != null) {
				String dateKey = objects[0].toString().substring(0, 10);
				history.setSearchCount(Long.valueOf(objects[1].toString()));
				history.setMapCount(Long.valueOf(objects[2].toString()));
				history.setWebsiteCount(Long.valueOf(objects[3].toString()));
				history.setDirectionsCount(Long.valueOf(objects[4].toString()));
				history.setCallsCount(Long.valueOf(objects[5].toString()));
				dailyMap.put(dateKey, history);
			}
		}

		return dailyMap;
	}

	public Long getInsightCountsForBrand(Integer brand, String store, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		

		StringBuffer sb = new StringBuffer();
		String brandQ= "select count(store) from InsightsGraphEntity where date>=? and date<=? and brandId=?";
		
		sb.append(brandQ);

		String stateQuery = "";
		if (store != null && store.length() > 0) {
			stateQuery = " and store=?";
			sb.append(stateQuery);
		}
		
		Query query = session.createQuery(sb.toString());
		query.setDate(0, startDate);
		query.setDate(1, endDate);
		query.setInteger(2, brand);
		if (store != null && store.length() > 0) {
			query.setString(3, store);
		}
		
		Long count = (Long)query.uniqueResult();

		return count;
	}

	public Map<String, InsightsGraphDTO> getHistory(Integer brandId, String state,
			Date startDate, Date endDate) {
		
		
		DateTime dt1 = new DateTime(startDate);
		DateTime dt2 = new DateTime(endDate);
		
		int days = Days.daysBetween(dt1, dt2).getDays();
		
		Session session = sessionFactory.getCurrentSession();
		

		StringBuffer sb = new StringBuffer();
		
		

		String query = "SELECT date, sum(searchCount) as searches,sum(mapCount) as maps, sum(websiteCount) as websites, sum(directionsCount) as directions, sum(callsCount) as calls ,sum(directCount) as direct, sum(inDirectCount) as indirects "
				+ "FROM InsightsGraphEntity where  date>=? and date<=? and brandId=? ";

		String stateQuery = "";
		if (state != null && state.length() > 0) {
			stateQuery = " and state=?";
		}
		String groupQuery = "";
		if(days<=14) {
			groupQuery = " group by date";
		} else if(days > 14 && days < 90) {
			groupQuery = "group by DATE_FORMAT( date, '%u' )";
		} else {
			groupQuery = "group by DATE_FORMAT( date, '%Y%m' )";
		}

		sb.append(query);
		if (state != null && state.length() > 0) {
			sb.append(stateQuery);
		}
		sb.append(groupQuery);

		Query createQuery = session.createQuery(sb.toString());
		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setInteger(2, brandId);
		if (state != null && state.length() > 0) {
			createQuery.setString(3, state);
		}

		List<Object[]> dataCount = createQuery.list();
		Map<String, InsightsGraphDTO> dailyMap = new TreeMap<String, InsightsGraphDTO>();
		Long directCount = 0L;
		Long discoveryCount = 0L;

		Long searchCount = 0L;
		Long mapsCount = 0L;
		Long viewsCount = 0L;

		Long websiteCount = 0L;
		Long phoneCount = 0L;
		Long directionsCount = 0L;
		Long actionsCount = 0L;

		for (Object[] objects : dataCount) {
			InsightsGraphDTO history = new InsightsGraphDTO();
			if (objects[0] != null) {
				String dateKey = objects[0].toString().substring(0, 10);
				history.setSearchCount(Long.valueOf(objects[1].toString()));
				history.setMapCount(Long.valueOf(objects[2].toString()));
				history.setWebsiteCount(Long.valueOf(objects[3].toString()));
				history.setDirectionsCount(Long.valueOf(objects[4].toString()));
				history.setCallsCount(Long.valueOf(objects[5].toString()));
				
				history.setDirectCount(Long.valueOf(objects[6].toString()));
				history.setInDirectCount(Long.valueOf(objects[7].toString()));
				dailyMap.put(dateKey, history);
				
				directCount = directCount + Long.valueOf(objects[6].toString());
				discoveryCount =  discoveryCount + Long.valueOf(objects[7].toString());
				searchCount = searchCount + Long.valueOf(objects[1].toString());
				mapsCount = mapsCount + Long.valueOf(objects[2].toString());
				websiteCount =  websiteCount + Long.valueOf(objects[3].toString());
				directionsCount = directionsCount + Long.valueOf(objects[4].toString());
				phoneCount =  phoneCount + Long.valueOf(objects[5].toString());
			}
		}
		InsightsGraphDTO totals = new InsightsGraphDTO();
		
		totals.setDirectCount(directCount);
		totals.setInDirectCount(discoveryCount);
		totals.setSearchCount(searchCount);
		totals.setMapCount(mapsCount);
		totals.setWebsiteCount(websiteCount);
		totals.setDirectionsCount(directionsCount);
		totals.setCallsCount(phoneCount);
		
		
		dailyMap.put("TotalCounts", totals);

		return dailyMap;
	}

	public Map<String, List<BingAnalyticsDTO>> getTopandBottomAnalytics(
			Integer brandId, String state, Date startDate, Date endDate) {

		Map<String, List<BingAnalyticsDTO>> topBottoms = new HashMap<String, List<BingAnalyticsDTO>>();

		List<BingAnalyticsDTO> analyticsData = new ArrayList<BingAnalyticsDTO>();
		List<BingAnalyticsDTO> analyticsDataBottoms = new ArrayList<BingAnalyticsDTO>();

		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		endDate = cal.getTime();

		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();


		Map<Long, String> lblStoreMapByCleintId = getLBLStoreMapByCleintId(brandId);

		String query = "SELECT lblStoreId, state, city, sum(impressionCount) as totalImpressions FROM BingAnalyticsEntity where date>=? and date<=? and brandId=? ";

		String stateQuery = "";
		if (state != null && state.length() > 0) {
			stateQuery = " and state=?";
		}
		String groupQuery = " group by store ORDER BY sum(impressionCount) desc";

		sb.append(query);
		if (state != null && state.length() > 0) {
			sb.append(stateQuery);
		}
		sb.append(groupQuery);

		Query createQuery = session.createQuery(sb.toString());

		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setInteger(2, brandId);
		if (state != null && state.length() > 0) {
			createQuery.setString(3, state);
		}

		List<Object[]> dataCount = createQuery.list();
		if (dataCount.size() > 10) {
			dataCount = dataCount.subList(0, 9);
		}
		for (Object[] objects : dataCount) {

			BingAnalyticsDTO history = new BingAnalyticsDTO();
			if (objects[0] != null) {
				Long lblStoreId = Long.valueOf(objects[0].toString());
				String stateVal = objects[1].toString();
				String city = objects[2].toString();
				history.setImpressionCount(Integer.valueOf(objects[3]
						.toString()));
				history.setStore(lblStoreMapByCleintId.get(lblStoreId));
				history.setState(stateVal);
				history.setCity(city);
				analyticsData.add(history);
			}
		}

		topBottoms.put("topAnalytics", analyticsData);

		StringBuffer sbBottoms = new StringBuffer();
		String queryBottoms = "SELECT store, state, city, sum(impressionCount) as totalImpressions FROM BingAnalyticsEntity where date>=? and date<=? and brandId=?";

		String sQuery = "";
		if (state != null && state.length() > 0) {
			sQuery = " and state=?";
		}
		String bgQuery = " group by store ORDER BY sum(impressionCount)";

		sbBottoms.append(queryBottoms);
		if (state != null && state.length() > 0) {
			sbBottoms.append(sQuery);
		}
		sbBottoms.append(bgQuery);

		Query createQueryBottoms = session.createQuery(sbBottoms.toString());

		createQueryBottoms.setDate(0, startDate);
		createQueryBottoms.setDate(1, endDate);
		createQueryBottoms.setInteger(2, brandId);
		if (state != null && state.length() > 0) {
			createQueryBottoms.setString(3, state);
		}

		List<Object[]> data = createQueryBottoms.list();
		if (data.size() > 10) {
			data = data.subList(0, 9);
		}
		for (Object[] objects : data) {

			BingAnalyticsDTO history = new BingAnalyticsDTO();
			if (objects[0] != null) {
				Long lblStoreId = Long.valueOf(objects[0].toString());
				String stateVal = objects[1].toString();
				String city = objects[2].toString();
				history.setImpressionCount(Integer.valueOf(objects[3]
						.toString()));
				history.setStore(lblStoreMapByCleintId.get(lblStoreId));
				history.setState(stateVal);
				history.setCity(city);
				analyticsDataBottoms.add(history);
			}
		}

		topBottoms.put("bottomAnalytics", analyticsDataBottoms);

		return topBottoms;
	}

	public Map<Integer, InsightsGraphDTO> getDailyTrends(String brand,
			String state, Date startDate, Date endDate) {

		Map<String, Map<String, Long>> dailyTrends = new HashMap<String, Map<String, Long>>();

		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		endDate = cal.getTime();

		Session session = sessionFactory.getCurrentSession();
		
		BrandInfoDTO brandInfo = getBrandInfoByBrandName(brand);
		Integer brandId=0;
		if(brandInfo!=null){
			brandId = brandInfo.getClientId();
		}

		StringBuffer sb = new StringBuffer();

		String query = "SELECT date, sum(totalViewCount) as views,sum(totalActionCount) as actions, sum(websiteCount) as websites, sum(directionsCount) as directions, sum(callsCount) as calls  "
				+ "FROM InsightsGraphEntity where brandId=? and date>=? and date<=?";

		String stateQuery = "";
		if (state != null && state.length() > 0) {
			stateQuery = " and state=?";
		}

		String groupQuery = " group by date";

		sb.append(query);
		if (state != null && state.length() > 0) {
			sb.append(stateQuery);
		}
		sb.append(groupQuery);

		Query createQuery = session.createQuery(sb.toString());
		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setInteger(2, brandId);
		if (state != null && state.length() > 0) {
			createQuery.setString(3, state);
		}

		List<Object[]> dataCount = createQuery.list();

		List<InsightsGraphDTO> dailyTrendData = new ArrayList<InsightsGraphDTO>();
		for (Object[] objects : dataCount) {
			InsightsGraphDTO history = new InsightsGraphDTO();
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
				history.setTotalViewCount(Long.valueOf(objects[1].toString()));
				history.setTotalActionCount(Long.valueOf(objects[2].toString()));
				history.setWebsiteCount(Long.valueOf(objects[3].toString()));
				history.setDirectionsCount(Long.valueOf(objects[4].toString()));
				history.setCallsCount(Long.valueOf(objects[5].toString()));
				dailyTrendData.add(history);
			}
		}

		Map<Integer, InsightsGraphDTO> dayTrends = new HashMap<Integer, InsightsGraphDTO>();

		for (InsightsGraphDTO insightsGraphDTO : dailyTrendData) {

			Date date = insightsGraphDTO.getDate();
			int dayOfWeek = getDayOfWeek(date);
			InsightsGraphDTO existingInsights  = dayTrends.get(dayOfWeek);
			if(existingInsights==null) {
				//System.out.println("adding first time for" + dayOfWeek);
				dayTrends.put(dayOfWeek, insightsGraphDTO);
			}else {
				existingInsights = dayTrends.get(dayOfWeek);
				Long viewCount = existingInsights.getTotalViewCount() +  insightsGraphDTO.getTotalViewCount();
				Long actionsCount = existingInsights.getTotalActionCount() +  insightsGraphDTO.getTotalActionCount();
				Long webSiteCount = existingInsights.getWebsiteCount() +  insightsGraphDTO.getWebsiteCount();
				Long directionsCount = existingInsights.getDirectionsCount() +  insightsGraphDTO.getDirectionsCount();
				Long callsCount = existingInsights.getCallsCount() +  insightsGraphDTO.getCallsCount();
				existingInsights.setTotalViewCount(viewCount);
				existingInsights.setTotalActionCount(actionsCount);
				existingInsights.setWebsiteCount(webSiteCount);
				existingInsights.setDirectionsCount(directionsCount);
				existingInsights.setCallsCount(callsCount);
				dayTrends.put(dayOfWeek, existingInsights);
			}

		}

		return dayTrends;
	}

	public int getDayOfWeek(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public Map<Long, String> getLBLStoreMapByCleintId(Integer clientId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LBLStoreEntity.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		List<LBLStoreDTO> stores = new ArrayList<LBLStoreDTO>();

		Map<Long, String> map = new HashMap<Long, String>();

		List<LBLStoreEntity> list = criteria.list();
		for (LBLStoreEntity lblStoreEntity : list) {

			map.put(lblStoreEntity.getLblStoreId(), lblStoreEntity.getStore());

		}
		return map;
	}

	public Long getLBLStorByCleintIdandStore(Integer clientId, String store) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LBLStoreEntity.class);
		criteria.add(Restrictions.eq("clientId", clientId));
		criteria.add(Restrictions.eq("store", store));
		List<LBLStoreDTO> stores = new ArrayList<LBLStoreDTO>();

		Long lblStore = 0L;

		Map<Long, String> map = new HashMap<Long, String>();

		List<LBLStoreEntity> list = criteria.list();
		for (LBLStoreEntity lblStoreEntity : list) {

			return lblStoreEntity.getLblStoreId();

		}
		return lblStore;
	}

	public Map<String, List<InsightsGraphDTO>> getTopandBottomSearches(
			Integer brand, String state, Date startDate, Date endDate) {


		Map<String, List<InsightsGraphDTO>> topBottomSearches = new HashMap<String, List<InsightsGraphDTO>>();

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(InsightsGraphEntity.class);

		if (state != null && state.length() > 0) {
			criteria.add(Restrictions.eq("state", state));
		}
		criteria.add(Restrictions.eq("brandId", brand));
		criteria.add(Restrictions.ge("date", startDate));
		criteria.add(Restrictions.le("date", endDate));

		List<InsightsGraphDTO> insightsGraphsList = new ArrayList<InsightsGraphDTO>();
		List<InsightsGraphEntity> businessEntities = criteria.list();

		List<InsightsGraphDTO> tSearchList = new ArrayList<InsightsGraphDTO>();
		List<InsightsGraphDTO> bSearchList = new ArrayList<InsightsGraphDTO>();
		List<InsightsGraphDTO> tViewList = new ArrayList<InsightsGraphDTO>();
		List<InsightsGraphDTO> bViewList = new ArrayList<InsightsGraphDTO>();
		List<InsightsGraphDTO> tActionList = new ArrayList<InsightsGraphDTO>();
		List<InsightsGraphDTO> bActionList = new ArrayList<InsightsGraphDTO>();

		Map<String, Long> searchStores = new HashMap<String, Long>();
		Map<String, Long> viewStores = new HashMap<String, Long>();
		Map<String, Long> actionStores = new HashMap<String, Long>();

		Map<String, InsightsGraphDTO> storesMap = new HashMap<String, InsightsGraphDTO>();


		//Map<Long, String> lBL_Client_store_map = getLBLStoreMapByCleintId(brand);

		// Collect all the stores
		for (InsightsGraphEntity entity : businessEntities) {

			InsightsGraphDTO dto = new InsightsGraphDTO();
			BeanUtils.copyProperties(entity, dto);

			//dto.setStore(lBL_Client_store_map.get(dto.getLblStoreId()));

			String store = dto.getStore();
			// searches
			Long dCount = dto.getDirectCount();
			Long inDcount = dto.getInDirectCount();
			Long tsCount = dto.getTotalSearchCount();

			Long taCount = dto.getTotalActionCount();

			// views
			Long vmCount = dto.getMapCount();
			Long vsCount = dto.getSearchCount();
			Long tvCount = dto.getTotalViewCount();

			// Actions
			Long websiteCount = dto.getWebsiteCount();
			Long directionsCount = dto.getDirectionsCount();
			Long callsCount = dto.getCallsCount();
			Long actionsCount = dto.getTotalActionCount();

			if (searchStores.containsKey(store)) {

				InsightsGraphDTO graphDTO = storesMap.get(store);

				Long oldDcount = graphDTO.getDirectCount();
				Long oldInDcount = graphDTO.getInDirectCount();
				Long oldTSDcount = graphDTO.getTotalSearchCount();

				oldDcount = oldDcount + dCount;
				oldInDcount = oldInDcount + inDcount;
				oldTSDcount = oldTSDcount + tsCount;

				dto.setDirectCount(oldDcount);
				dto.setInDirectCount(oldInDcount);
				dto.setTotalSearchCount(oldTSDcount);

				searchStores.put(store, oldTSDcount);

			} else {
				searchStores.put(store, tsCount);
			}
			if (viewStores.containsKey(store)) {

				InsightsGraphDTO graphDTO = storesMap.get(store);

				Long oldMcount = graphDTO.getMapCount();
				Long oldVSDcount = graphDTO.getSearchCount();
				Long oldTVDcount = graphDTO.getTotalViewCount();

				oldMcount = oldMcount + vmCount;
				oldVSDcount = oldVSDcount + vsCount;
				oldTVDcount = oldTVDcount + tvCount;

				dto.setMapCount(oldMcount);
				dto.setSearchCount(oldVSDcount);
				dto.setTotalViewCount(oldTVDcount);

				viewStores.put(store, oldTVDcount);
			} else {
				viewStores.put(store, tvCount);
			}

			if (actionStores.containsKey(store)) {

				InsightsGraphDTO graphDTO = storesMap.get(store);

				Long oldWebsiteCount = graphDTO.getWebsiteCount();
				Long oldDirectionsCount = graphDTO.getDirectionsCount();
				Long oldCallsCount = graphDTO.getCallsCount();
				Long oldActionsCount = graphDTO.getTotalActionCount();

				oldWebsiteCount = oldWebsiteCount + websiteCount;
				oldDirectionsCount = oldDirectionsCount + directionsCount;
				oldCallsCount = oldCallsCount + callsCount;
				oldActionsCount = oldActionsCount + actionsCount;

				dto.setWebsiteCount(oldWebsiteCount);
				dto.setDirectionsCount(oldDirectionsCount);
				dto.setCallsCount(oldCallsCount);
				dto.setTotalActionCount(oldActionsCount);

				actionStores.put(store, oldActionsCount);
			} else {
				actionStores.put(store, taCount);
			}

			storesMap.put(store, dto);
		}

		Map<Long, String> searchTopMap = new TreeMap<Long, String>()
				.descendingMap();
		for (Map.Entry entry : searchStores.entrySet()) {
			Long value = (Long) entry.getValue();
			String key = (String) entry.getKey();
			// System.out.println("Key : " + key + " Value : " + value);
			searchTopMap.put(value, key);
		}
		for (Map.Entry entry : searchTopMap.entrySet()) {
			String key = (String) entry.getValue();
			InsightsGraphDTO insightsGraphDTO = storesMap.get(key);
			if (tSearchList.size() > 9) {
				break;
			}
			tSearchList.add(insightsGraphDTO);
		}

		Map<Long, String> searchBottomMap = new TreeMap<Long, String>();
		for (Map.Entry entry : searchStores.entrySet()) {
			Long value = (Long) entry.getValue();
			String key = (String) entry.getKey();
			// System.out.println("Key : " + key + " Value : " + value);
			searchBottomMap.put(value, key);
		}
		for (Map.Entry entry : searchBottomMap.entrySet()) {
			String key = (String) entry.getValue();
			InsightsGraphDTO insightsGraphDTO = storesMap.get(key);
			if (bSearchList.size() > 9) {
				break;
			}
			bSearchList.add(insightsGraphDTO);
		}

		// get Top and Bottom Views details

		Map<Long, String> viewTopMap = new TreeMap<Long, String>()
				.descendingMap();
		for (Map.Entry entry : viewStores.entrySet()) {
			Long value = (Long) entry.getValue();
			String key = (String) entry.getKey();
			// System.out.println("Key : " + key + " Value : " + value);
			viewTopMap.put(value, key);
		}
		for (Map.Entry entry : viewTopMap.entrySet()) {
			String key = (String) entry.getValue();
			InsightsGraphDTO insightsGraphDTO = storesMap.get(key);
			if (tViewList.size() > 9) {
				break;
			}
			tViewList.add(insightsGraphDTO);
		}

		Map<Long, String> bottomViewMap = new TreeMap<Long, String>();
		for (Map.Entry entry : viewStores.entrySet()) {
			Long value = (Long) entry.getValue();
			String key = (String) entry.getKey();
			// System.out.println("Key : " + key + " Value : " + value);
			bottomViewMap.put(value, key);
		}
		for (Map.Entry entry : bottomViewMap.entrySet()) {
			String key = (String) entry.getValue();
			InsightsGraphDTO insightsGraphDTO = storesMap.get(key);
			if (bViewList.size() > 9) {
				break;
			}
			bViewList.add(insightsGraphDTO);
		}

		// get Top and Bottom Actions details

		Map<Long, String> topActionMap = new TreeMap<Long, String>()
				.descendingMap();
		for (Map.Entry entry : actionStores.entrySet()) {
			Long value = (Long) entry.getValue();
			String key = (String) entry.getKey();
			// System.out.println("Key : " + key + " Value : " + value);
			topActionMap.put(value, key);
		}
		for (Map.Entry entry : topActionMap.entrySet()) {
			String key = (String) entry.getValue();
			InsightsGraphDTO insightsGraphDTO = storesMap.get(key);
			if (tActionList.size() > 9) {
				break;
			}
			tActionList.add(insightsGraphDTO);
		}

		Map<Long, String> bottomActionsMap = new TreeMap<Long, String>();
		for (Map.Entry entry : actionStores.entrySet()) {
			Long value = (Long) entry.getValue();
			String key = (String) entry.getKey();
			// System.out.println("Key : " + key + " Value : " + value);
			bottomActionsMap.put(value, key);
		}
		for (Map.Entry entry : bottomActionsMap.entrySet()) {
			String key = (String) entry.getValue();
			InsightsGraphDTO insightsGraphDTO = storesMap.get(key);
			if (bActionList.size() > 9) {
				break;
			}
			bActionList.add(insightsGraphDTO);
		}

		topBottomSearches.put("tSearch", tSearchList);
		topBottomSearches.put("bSearch", bSearchList);
		topBottomSearches.put("tView", tViewList);
		topBottomSearches.put("bView", bViewList);
		topBottomSearches.put("tAction", tActionList);
		topBottomSearches.put("bAction", bActionList);
/*
		System.out.println("topBottomSearches are : "
				+ topBottomSearches.size());
		;
		System.out.println("end time:============> " + new Date());*/
		return topBottomSearches;
	}

	public List<BingReportDTO> getAnlyticsForStore(String brand, String store,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		List<BingReportDTO> analyticsData = new ArrayList<BingReportDTO>();

		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		endDate = cal.getTime();

		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();

		BrandInfoDTO brandInfo = getBrandInfoByBrandName(brand);

		Long lblStorByCleintIdandStore = getLBLStorByCleintIdandStore(
				brandInfo.getClientId(), store);

		String query = "SELECT date, sum(impressionCount) as totalImpressions FROM BingAnalyticsEntity where date>=? and date<=? and brandId=? and lblStoreId=? group by date";

		Query createQuery = session.createQuery(query);

		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setInteger(2, brandInfo.getClientId());
		createQuery.setLong(3, lblStorByCleintIdandStore);

		List<Object[]> dataCount = createQuery.list();
		for (Object[] objects : dataCount) {

			BingReportDTO history = new BingReportDTO();
			if (objects[0] != null) {
				String dateKey = objects[0].toString().substring(0, 10);
				history.setImpressionCount(Integer.valueOf(objects[1]
						.toString()));
				history.setDate(dateKey);
				analyticsData.add(history);
			}
		}
		return analyticsData;

	}

	public List<BingReportDTO> getAnlytics(Integer brandId, String state,
			Date startDate, Date endDate) {

		List<BingReportDTO> analyticsData = new ArrayList<BingReportDTO>();

		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		endDate = cal.getTime();

		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();

		String query = "SELECT date, sum(impressionCount) as totalImpressions FROM BingAnalyticsEntity where date>=? and date<=? and brandId=?";

		String stateQuery = "";
		if (state != null && state.length() > 0) {
			stateQuery = " and state=?";
		}

		String groupQuery = " group by date";

		sb.append(query);
		if (state != null && state.length() > 0) {
			sb.append(stateQuery);
		}
		sb.append(groupQuery);

		Query createQuery = session.createQuery(sb.toString());
		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setInteger(2, brandId);
		if (state != null && state.length() > 0) {
			createQuery.setString(3, state);
		}

		List<Object[]> dataCount = createQuery.list();
		for (Object[] objects : dataCount) {

			BingReportDTO history = new BingReportDTO();
			if (objects[0] != null) {
				String dateKey = objects[0].toString().substring(0, 10);
				history.setImpressionCount(Integer.valueOf(objects[1]
						.toString()));
				history.setDate(dateKey);
				analyticsData.add(history);
			}
		}
		return analyticsData;
	}

	public List<String> getStoresNames(String brand) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("client", brand));
		List<String> storeList = new ArrayList<String>();
		List<LocalBusinessEntity> list = criteria.list();
		for (LocalBusinessEntity businessBean : list) {
			LocalBusinessDTO businessDTO = new LocalBusinessDTO();
			BeanUtils.copyProperties(businessBean, businessDTO);
			storeList.add(businessDTO.getStore());
		}
		return storeList;
	}

	public void updateStoresWithGMBAccountId(String client,
			String googleAccountId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session
				.createQuery("update Stock set stockName = :stockName"
						+ " where stockCode = :stockCode");
		query.setParameter("stockName", "DIALOG1");
		query.setParameter("stockCode", "7277");
		int result = query.executeUpdate();

	}
}