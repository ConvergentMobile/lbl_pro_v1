package com.business.model.dataaccess.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.ForgotPasswordDto;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.UploadReportDTO;
import com.business.common.dto.UsersDTO;
import com.business.common.util.DAOUtil;
import com.business.common.util.DateUtil;
import com.business.common.util.LBLConstants;
import com.business.model.dataaccess.BusinessDao;
import com.business.model.pojo.AcceptedBrandsEntity;
import com.business.model.pojo.BrandEntity;
import com.business.model.pojo.CategoryEntity;
import com.business.model.pojo.CategorySyphcode;
import com.business.model.pojo.ChannelEntity;
import com.business.model.pojo.ExportReportEntity;
import com.business.model.pojo.ForgotPasswordEntity;
import com.business.model.pojo.LblErrorEntity;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.model.pojo.RoleEntity;
import com.business.model.pojo.StatesListEntity;
import com.business.model.pojo.UploadReportEntity;
import com.business.model.pojo.User_brands;
import com.business.model.pojo.UsersEntity;
import com.business.web.bean.UsersBean;

/**
 * 
 * @author Vasanth
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
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN) {
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

		if (allBrandNames != null && allBrandNames.size() > 0) {
			sql += " order by uploadedTime desc";
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
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	/**
	 * getListOfErrors
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

			if (roleId == LBLConstants.CHANNEL_ADMIN) {
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
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	/**
	 * deleteBusinessInfo
	 */
	public void deleteBusinessInfo(List<Integer> listIds) {

		Session session = sessionFactory.getCurrentSession();
		for (Integer id : listIds) {
			LocalBusinessEntity bean = (LocalBusinessEntity) session
					.createCriteria(LocalBusinessEntity.class)
					.add(Restrictions.eq("id", id)).uniqueResult();
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
		BeanUtils.copyProperties(businessDTO, bean);

		session.update(bean);

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
			logger.info("Business Information Saved or Not ? " + (save != null));
		}
		return true;
	}

	/**
	 * getSpecificBusinessInfo
	 */
	public List<LocalBusinessDTO> getSpecificBusinessInfo(List<Integer> listIds) {
		Session session = sessionFactory.getCurrentSession();
		List<LocalBusinessDTO> list = new ArrayList<LocalBusinessDTO>();
		for (Integer id : listIds) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			LocalBusinessEntity entityBean = (LocalBusinessEntity) session
					.load(LocalBusinessEntity.class, id);
			BeanUtils.copyProperties(entityBean, dto);
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
					"from LocalBusinessEntity where");
			if (!companyName.equals("")) {
				whereCondtion = whereCondtion.append(" companyName like '%"
						+ companyName + "%'");
			}
			if (!store.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LocalBusinessEntity where")) {
					whereCondtion = whereCondtion.append(" store like '%"
							+ store + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and store like '%"
							+ store + "%'");
				}
			}
			if (!locationPhone.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LocalBusinessEntity where")) {
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
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LocalBusinessEntity where")) {
					whereCondtion = whereCondtion.append(" client like '%"
							+ brands + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and client like '%"
							+ brands + "%'");
				}
			}
			logger.info("@@@@whereCondtion: " + whereCondtion);

			List<LocalBusinessDTO> whereCoditionRecords = getWhereCoditionRecords(
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
			if (roleId == LBLConstants.CHANNEL_ADMIN) {
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

				if (roleId == LBLConstants.CHANNEL_ADMIN) {
					if (index == 0) {
						query += " AND (";
					}
				} else {
					if (index == 0) {
						query += " AND ";
					}
				}

				if (index != 0) {
					query += " OR";
				}
				query += " client=?";
				if (roleId == LBLConstants.CHANNEL_ADMIN) {
					if (totalBrands == index + 1) {
						query += " )";
					}
				}
				brandsMap.put(index, brand);
				index++;
			}
		}
		logger.debug("Query executed to fecth recrds are: " + query);
		Query createQuery = session.createQuery(query);
		for (Integer position : brandsMap.keySet()) {
			createQuery.setString(position, brandsMap.get(position));
		}
		List<LocalBusinessEntity> listOfName = (List<LocalBusinessEntity>) createQuery
				.list();
		for (LocalBusinessEntity localBusinessBean : listOfName) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
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
		}
		return channelID;
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
		String nameQuery = "SELECT client, COUNT(client) from LocalBusinessEntity where client IS NOT NULL GROUP BY client";
		Integer role = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		boolean isSuperAdmin = true;
		if (!(role == LBLConstants.CONVERGENT_MOBILE_ADMIN) && userName != null) {
			isSuperAdmin = false;
			// nameQuery
			// +=" AND channelID = (SELECT u.channelID from UsersEntity u where u.userName=?)";
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
			dto.setBrandsCount((Long) objects[1]);
			if (allBrandNames.contains((String) objects[0])) {
				brands.add(dto);
			}
			logger.info(Arrays.toString(objects));
		}
		brands.addAll(zeroLocationBrandNames);
		// Collections.shuffle(brands);
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
					"from LocalBusinessEntity where");
			if (!brands.equals("")) {
				whereCondtion = whereCondtion.append(" client like '%" + brands
						+ "%'");
			}

			if (!companyName.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LocalBusinessEntity where")) {
					whereCondtion = whereCondtion.append(" companyName like '%"
							+ companyName + "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and companyName like '%" + companyName
									+ "%'");
				}
			}
			if (!store.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LocalBusinessEntity where")) {
					whereCondtion = whereCondtion.append(" store like '%"
							+ store + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and store like '%"
							+ store + "%'");
				}
			}
			if (!locationPhone.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LocalBusinessEntity where")) {
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
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LocalBusinessEntity where")) {
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
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LocalBusinessEntity where")) {
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
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LocalBusinessEntity where")) {
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
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LocalBusinessEntity where")) {
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
			List<LocalBusinessDTO> whereCoditionRecords = getWhereCoditionRecords(
					whereCondtion.toString(), session);
			if (whereCoditionRecords.size() > 0) {
				for (LocalBusinessDTO localBusinessDTO : whereCoditionRecords) {
					setOfBusinessRecords.add(localBusinessDTO);
				}
			}

		}
		return setOfBusinessRecords;
	}

	/**
	 * getBusinessInfo
	 * 
	 */
	public LocalBusinessDTO getBusinessInfo(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		LocalBusinessEntity bean = (LocalBusinessEntity) session.load(
				LocalBusinessEntity.class, id);
		LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();
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
		} else {
			ChannelEntity channelEntity = new ChannelEntity();
			channelEntity.setChannelName("Convergent Mobile");
			channelEntity.setStartDate(startDate);
			session.save(channelEntity);
			BrandEntity brandEntity = new BrandEntity();
			brandEntity.setBrandName(brandName);
			// brandEntity.setChannelID(Integer.parseInt(save.toString()));
			brandEntity.setLocationsInvoiced(locationsInvoiced);
			brandEntity.setStartDate(startDate);
			brandEntity.setSubmisions(submisions);
			Serializable save2 = session.save(brandEntity);
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
		} else if (role == LBLConstants.CHANNEL_ADMIN && userName != null) {

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

		} else if (role == LBLConstants.CHANNEL_ADMIN && userName != null) {
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
			String activityDesc = brandName + " is exported to "
					+ exportReportEntity.getPartner();
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
	 * 
	 * saveChannel
	 * 
	 */

	public Integer saveChannel(String channelName, Date startDate) {

		Session session = sessionFactory.getCurrentSession();

		ChannelEntity channelEntity = new ChannelEntity();
		channelEntity.setChannelName(channelName);
		channelEntity.setStartDate(startDate);
		Serializable save = session.save(channelEntity);

		return Integer.valueOf(save.toString());
	}

	/**
	 * updateChannel
	 * 
	 */

	public boolean updateChannel(String channelName, Date startDate,
			Integer channelID) {
		Session session = sessionFactory.getCurrentSession();
		ChannelEntity channelEntity = new ChannelEntity();
		channelEntity.setChannelID(channelID);
		channelEntity.setChannelName(channelName);
		channelEntity.setStartDate(startDate);
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
		if (bean.getRoleId() == LBLConstants.CLIENT_ADMIN) {
			// channelId = getChannelIdByBrandId(bean.getBrandID());
		} else {
			String channelName = bean.getChannelName();
			if (channelName.length() > 0) {
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
		if (bean.getUserID() == null) {
			Serializable save = session.save(entity);
			String[] brandID = bean.getBrandID();
			if (bean.getBrandID() != null) {
				for (String string : brandID) {

					User_brands brands = new User_brands();
					brands.setUserID(Integer.valueOf(save.toString()));
					brands.setBrandID(Integer.parseInt(string));
					brands.setChannelID(channelId);
					session.save(brands);
				}
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
							User_brands brands = new User_brands();
							brands.setUserID(bean.getUserID());
							brands.setBrandID(Integer.parseInt(string));
							brands.setChannelID(channelId);
							session.save(brands);
						}
					}
				} else {
					String[] brandID = bean.getBrandID();
					for (String string : brandID) {
						User_brands brands = new User_brands();
						brands.setUserID(bean.getUserID());
						brands.setBrandID(Integer.parseInt(string));
						brands.setChannelID(channelId);
						session.save(brands);
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
		if (roleId == LBLConstants.CHANNEL_ADMIN) {

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
		criteria.add(Restrictions
				.like("userName", userName, MatchMode.ANYWHERE));

		List<UsersDTO> userList = new ArrayList<UsersDTO>();
		List<UsersEntity> list = criteria.list();
		/*
		 * for (UsersEntity usersEntity : list) { UsersDTO usersDTO = new
		 * UsersDTO(); BeanUtils.copyProperties(usersEntity, usersDTO);
		 * userList.add(usersDTO);h }
		 */
		// for (UsersEntity usersEntity : list) {
		if (roleId == LBLConstants.CHANNEL_ADMIN) {

			String user = (String) httpSession.getAttribute("userName");

			Integer channelID = getChannelIdByUserName(user);
			/*
			 * String sql =
			 * "SELECT channelID FROM UsersEntity where channelID=?"; Query
			 * createQuery = session.createQuery(sql); createQuery.setInteger(0,
			 * channelID); List<Integer> brandid = createQuery.list(); for
			 * (Integer brand : brandid) {
			 */
			/*
			 * criteria.add(Restrictions.eq("channelID", channelID));
			 * List<UsersEntity> listNames = criteria.list(); for (UsersEntity
			 * usersEntity2 : listNames) {
			 * 
			 * UsersDTO usersDTO = new UsersDTO();
			 * BeanUtils.copyProperties(usersEntity2, usersDTO);
			 * userList.add(usersDTO); }
			 */

			Criteria channelUserCriteria = session
					.createCriteria(User_brands.class);
			channelUserCriteria.add(Restrictions.eq("channelID", channelID));
			List<User_brands> channelUsers = channelUserCriteria.list();
			channelUsers = removeDuplicateUsers(channelUsers);
			for (User_brands channelUser : channelUsers) {

				criteria.add(Restrictions.eq("userID", channelUser.getUserID()));

				List<UsersEntity> users = criteria.list();
				if (users.size() > 0) {
					UsersEntity userEntity = (UsersEntity) users.get(0);
					UsersDTO usersDTO = new UsersDTO();
					BeanUtils.copyProperties(userEntity, usersDTO);
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
		String sql = "SELECT DISTINCT brandName FROM BrandEntity";
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

	/**
	 * locationUploadedCount:: brands count on upload based on brand name
	 */
	public Integer locationUploadedCount(String brandName) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.like("brands", brandName));
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
		} else if (role == LBLConstants.CHANNEL_ADMIN && userName != null) {

			List<String> allBrandNames = getAllBrandNames(false, userName);
			/*
			 * String sql =
			 * "SELECT b.brandName from BrandEntity b where b.channelID= (SELECT u.channelID from UsersEntity u where u.userName=?)"
			 * ; Query query = session.createQuery(sql); query.setString(0,
			 * userName); List<String> list = query.list();
			 */
			if (allBrandNames != null && allBrandNames.size() > 1)
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

		} else if (role == LBLConstants.CHANNEL_ADMIN && userName != null) {

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
			String activityDesc = brand + " is exported to "
					+ exportReportEntity.getPartner();
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
		for (LblErrorDTO lblErrorDTO : inCorrectData) {
			logger.info(" lblErrorDTO length :: "
					+ lblErrorDTO.getErrorMessage().length());
			LblErrorEntity lblErrorEntity = new LblErrorEntity();
			BeanUtils.copyProperties(lblErrorDTO, lblErrorEntity);
			lblErrorEntity.setUploadedTime(date);
			lblErrorEntity.setUploadJobId(uploadJobId);
			Serializable save = session.save(lblErrorEntity);
			logger.info("Error Business Information Saved or Not ? "
					+ (save != null));
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

	public LblErrorDTO getErrorBusinessInfo(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		LblErrorEntity bean = (LblErrorEntity) session.load(
				LblErrorEntity.class, id);
		LblErrorDTO lblErrorDTO = new LblErrorDTO();
		BeanUtils.copyProperties(bean, lblErrorDTO);
		return lblErrorDTO;

	}

	public void updateErrorBusinessInfo(LblErrorDTO businessInfoDto,
			Integer listingId) {
		Session session = sessionFactory.getCurrentSession();
		LocalBusinessEntity businessEntity = new LocalBusinessEntity();
		BeanUtils.copyProperties(businessInfoDto, businessEntity);

		/*
		 * LocalBusinessEntity entity = (LocalBusinessEntity) session
		 * .createCriteria(LocalBusinessEntity.class) .add(Restrictions.eq("id",
		 * listingId)).uniqueResult(); ; session.delete(entity);
		 */

		LblErrorEntity errorEntity = new LblErrorEntity();
		BeanUtils.copyProperties(businessInfoDto, errorEntity);
		session.delete(errorEntity);

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
	public boolean isValidState(String state) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(StatesListEntity.class);
		criteria.add(Restrictions.eq("code", state));
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
			if (roleId == LBLConstants.CHANNEL_ADMIN) {
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
			for (String brand : allBrandNames) {
				if (index != 0) {
					sql += " OR";
				}
				sql += " client=?";
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
	public List<LblErrorDTO> getSpecificErrorBusinessInfo(List<Integer> listIds) {
		Session session = sessionFactory.getCurrentSession();
		List<LblErrorDTO> list = new ArrayList<LblErrorDTO>();
		for (Integer id : listIds) {
			LblErrorDTO dto = new LblErrorDTO();
			LblErrorEntity entityBean = (LblErrorEntity) session.load(
					LblErrorEntity.class, id);
			BeanUtils.copyProperties(entityBean, dto);
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
					"from LblErrorEntity where");
			if (!brands.equals("")) {
				whereCondtion = whereCondtion.append(" client like '%" + brands
						+ "%'");
			}

			if (!companyName.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LblErrorEntity where")) {
					whereCondtion = whereCondtion.append(" companyName like '%"
							+ companyName + "%'");
				} else {
					whereCondtion = whereCondtion
							.append(" and companyName like '%" + companyName
									+ "%'");
				}
			}
			if (!store.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LblErrorEntity where")) {
					whereCondtion = whereCondtion.append(" store like '%"
							+ store + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and store like '%"
							+ store + "%'");
				}
			}
			if (!locationPhone.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LblErrorEntity where")) {
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
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LblErrorEntity where")) {
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
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LblErrorEntity where")) {
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
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LblErrorEntity where")) {
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
				if (whereCondtion.toString().equalsIgnoreCase(
						"from LblErrorEntity where")) {
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
			if (roleId == LBLConstants.CHANNEL_ADMIN) {
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
			/*
			 * int index = 0; for (String brand : allBrandNames) { if (index ==
			 * 0) { query += " AND"; } if (index != 0) { query += " OR"; } query
			 * += " client=?"; brandsMap.put(index, brand); index++; }
			 */
			int index = 0;
			int totalBrands = allBrandNames.size();
			for (String brand : allBrandNames) {

				if (roleId == LBLConstants.CHANNEL_ADMIN) {
					if (index == 0) {
						query += " AND (";
					}
				} else {
					if (index == 0) {
						query += " AND ";
					}
				}

				if (index != 0) {
					query += " OR";
				}
				query += " client=?";
				if (roleId == LBLConstants.CHANNEL_ADMIN) {
					if (totalBrands == index + 1) {
						query += " )";
					}
				}
				brandsMap.put(index, brand);
				index++;
			}
		}
		logger.debug("Query executed to fecth recirds are: " + query);

		Query createQuery = session.createQuery(query);
		for (Integer position : brandsMap.keySet()) {
			createQuery.setString(position, brandsMap.get(position));
		}
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

	public void deleteErrorBusinessInfo(List<Integer> listIds) {
		Session session = sessionFactory.getCurrentSession();
		for (Integer id : listIds) {
			LblErrorEntity bean = (LblErrorEntity) session
					.createCriteria(LblErrorEntity.class)
					.add(Restrictions.eq("id", id)).uniqueResult();
			;
			session.delete(bean);
		}

	}

	/**
	 * updateBusinessRecords based on actionCode
	 */

	public void updateBusinessRecords(
			List<LocalBusinessDTO> updateBusinessRecords, Date date,
			String uploadJobId) {

		Session session = sessionFactory.getCurrentSession();
		if (updateBusinessRecords != null) {
			for (LocalBusinessDTO localBusinessDTO : updateBusinessRecords) {

				LocalBusinessEntity bean = (LocalBusinessEntity) session.get(
						LocalBusinessEntity.class, localBusinessDTO.getId());
				BeanUtils.copyProperties(localBusinessDTO, bean);
				bean.setUploadedTime(date);
				bean.setUploadJobId(uploadJobId);
				session.update(bean);

			}
		}
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
		Session session = sessionFactory.getCurrentSession();
		if (listofErrorDeletesbyActionCode != null) {
			for (LblErrorDTO lblErrorDTO : listofErrorDeletesbyActionCode) {

				Integer id = lblErrorDTO.getId();
				logger.debug("Id to delete from database is: " + id);
				LblErrorEntity bean = (LblErrorEntity) session
						.createCriteria(LblErrorEntity.class)
						.add(Restrictions.eq("id", id)).uniqueResult();
				;
				session.delete(bean);
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
		String sql = "SELECT categoryName FROM CategoryEntity where categoryCode=?";
		Query createQuery = session.createQuery(sql);
		createQuery.setInteger(0, categoryId);
		List<String> categoryName = createQuery.list();
		return categoryName != null && !categoryName.isEmpty() ? categoryName
				.get(0) : null;

	}

	public String getSyphCodeByClientAndCategoryID(String category,
			Integer clientId) {
		Session session = sessionFactory.getCurrentSession();

		String sql = "SELECT syphCode FROM CategorySyphcode where categoryId="
				+ category + " AND clientId=" + clientId + "";

		Query createQuery = session.createQuery(sql);
		List<String> Syphcode = createQuery.list();

		return (Syphcode != null && !Syphcode.isEmpty()) ? Syphcode.get(0) : "";
	}


}