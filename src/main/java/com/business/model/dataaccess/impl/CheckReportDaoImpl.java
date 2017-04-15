package com.business.model.dataaccess.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.AccuracyGraphDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.CitationGraphDTO;
import com.business.common.dto.CitationReportDTO;
import com.business.common.dto.CitationStoreHistoryDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.common.util.LBLConstants;
import com.business.model.dataaccess.CheckReportDao;
import com.business.model.pojo.AccuarcyGraphEntity;
import com.business.model.pojo.AccuracyReportEntity;
import com.business.model.pojo.BrandEntity;
import com.business.model.pojo.CheckReportEntity;
import com.business.model.pojo.CitationGraphEntity;
import com.business.model.pojo.CitationReportEntity;
import com.business.model.pojo.CitationStoreHistoryEntity;
import com.business.model.pojo.LblErrorEntity;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.model.pojo.RenewalReportEntity;
import com.business.model.pojo.SearchDomains;
import com.business.model.pojo.StandardStreetAbbreviationsEntity;
import com.business.model.pojo.StatesListEntity;
import com.business.model.pojo.User_brands;
import com.business.model.pojo.UsersEntity;
import com.business.model.pojo.ValueObject;

@Repository
public class CheckReportDaoImpl implements CheckReportDao, Serializable {
	Logger logger = Logger.getLogger(CheckReportDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private HttpSession httpSession;

	public Set<LocalBusinessDTO> CheckSearchInfo(String brands, String companyName, String locationPhone,
			String locationAddress, String locationCity, String locationState, String locationZipCode) {

		Set<LocalBusinessDTO> setOfBusinessRecords = new HashSet<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();

		if (brands.equals("") && companyName.equals("") && locationPhone.equals("") && locationAddress.equals("")
				&& locationCity.equals("") && locationState.equals("") && locationZipCode.equals("")) {
			List<LocalBusinessDTO> listOfBusinessInfo = getListOfBusinessInfo();

			for (LocalBusinessDTO localBusinessDTO : listOfBusinessInfo) {

				setOfBusinessRecords.add(localBusinessDTO);
			}
		} else {
			StringBuffer whereCondtion = new StringBuffer("from LocalBusinessEntity where");
			if (!brands.equals("")) {
				whereCondtion = whereCondtion.append(" client like '%" + brands + "%'");
			}

			if (!companyName.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from LocalBusinessEntity where")) {
					whereCondtion = whereCondtion.append(" companyName like '%" + companyName + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and companyName like '%" + companyName + "%'");
				}
			}

			if (!locationPhone.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from LocalBusinessEntity where")) {
					whereCondtion = whereCondtion.append(" locationPhone like '%" + locationPhone + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and locationPhone like '%" + locationPhone + "%'");
				}
			}
			if (!locationAddress.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from LocalBusinessEntity where")) {
					whereCondtion = whereCondtion.append(" locationAddress like '%" + locationAddress + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and locationAddress like '%" + locationAddress + "%'");
				}
			}
			if (!locationCity.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from LocalBusinessEntity where")) {
					whereCondtion = whereCondtion.append(" locationCity like '%" + locationCity + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and locationCity like '%" + locationCity + "%'");
				}
			}
			if (!locationState.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from LocalBusinessEntity where")) {
					whereCondtion = whereCondtion.append(" locationState like '%" + locationState + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and locationState like '%" + locationState + "%'");
				}
			}
			if (!locationZipCode.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from LocalBusinessEntity where")) {
					whereCondtion = whereCondtion.append(" locationZipCode like '%" + locationZipCode + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and locationZipCode like '%" + locationZipCode + "%'");
				}
			}

			logger.info("### businesslisting search fields Query : " + whereCondtion);
			List<LocalBusinessDTO> whereCoditionRecords = getWhereCoditionRecords(whereCondtion.toString(), session);
			if (whereCoditionRecords.size() > 0) {
				for (LocalBusinessDTO localBusinessDTO : whereCoditionRecords) {
					setOfBusinessRecords.add(localBusinessDTO);
				}
			}

		}
		return setOfBusinessRecords;
	}

	public List<LocalBusinessDTO> getWhereCoditionRecords(String query, Session session) {
		List<LocalBusinessDTO> nameList = new ArrayList<LocalBusinessDTO>();
		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			List<String> allBrandNames = new ArrayList<String>();
			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session.createCriteria(UsersEntity.class);
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

				if (roleId == LBLConstants.CHANNEL_ADMIN || roleId == LBLConstants.PURIST) {
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
				if (roleId == LBLConstants.CHANNEL_ADMIN || roleId == LBLConstants.PURIST) {
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
		List<LocalBusinessEntity> listOfName = (List<LocalBusinessEntity>) createQuery.list();
		for (LocalBusinessEntity localBusinessBean : listOfName) {
			LocalBusinessDTO dto = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			nameList.add(dto);
		}
		return nameList;
	}

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

	public List<LocalBusinessDTO> getListOfBusinessInfo() {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false, userName);
				Criteria userCriteria = session.createCriteria(UsersEntity.class);
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
				Criteria userCriteria = session.createCriteria(UsersEntity.class);
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

		logger.debug("Fetching the list of businesses with the query applied: " + sql);

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

	public List<String> getAllBrandNames() {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT DISTINCT brandName FROM BrandEntity";
		Query createQuery = session.createQuery(sql);
		List<String> brandNames = createQuery.list();
		return brandNames;
	}

	public List<CheckReportDTO> getListOfCheckreportInfo() {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<CheckReportDTO> businessDTOs = new ArrayList<CheckReportDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId == LBLConstants.PURIST) {
				// allBrandNames = getAllBrandNames(false,userName);
				Criteria userCriteria = session.createCriteria(UsersEntity.class);
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
				Criteria userCriteria = session.createCriteria(UsersEntity.class);
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

		logger.debug("Fetching the list of businesses with the query applied: " + sql);

		Query query = session.createQuery(sql);
		for (Integer position : brandsMap.keySet()) {
			query.setString(position, brandsMap.get(position));
		}

		List<CheckReportEntity> businessEntities = query.list();
		logger.info("total businesses are: " + businessEntities.size());
		for (CheckReportEntity localBusinessBean : businessEntities) {
			CheckReportDTO dto = new CheckReportDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			businessDTOs.add(dto);
		}
		return businessDTOs;
	}

	public String getBrandById(Integer brandId) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT DISTINCT brandName FROM BrandEntity where brandID=?";
		Query createQuery = session.createQuery(sql);
		createQuery.setInteger(0, brandId);
		List<String> brandNames = createQuery.list();
		return brandNames != null && !brandNames.isEmpty() ? brandNames.get(0) : null;
	}

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

	public List<LblErrorDTO> getListOfErrors() {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> businessDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LblErrorEntity";
		List<String> allBrandNames = new ArrayList<String>();
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
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

		logger.debug("Fetching the list of error businesses with the query applied: " + sql);

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

	public Set<CheckReportDTO> CheckReportSearchInfo(String brands, String companyName, String locationPhone,
			String locationAddress, String locationCity, String locationState, String locationZipCode) {

		Set<CheckReportDTO> setOfBusinessRecords = new HashSet<CheckReportDTO>();
		Session session = sessionFactory.getCurrentSession();

		if (brands.equals("") && companyName.equals("") && locationPhone.equals("") && locationAddress.equals("")
				&& locationCity.equals("") && locationState.equals("") && locationZipCode.equals("")) {
			List<CheckReportDTO> listOfBusinessInfo = getListOfCheckreportInfo();

			for (CheckReportDTO localBusinessDTO : listOfBusinessInfo) {

				setOfBusinessRecords.add(localBusinessDTO);
			}
		} else {
			StringBuffer whereCondtion = new StringBuffer("from CheckReportEntity where");
			if (!brands.equals("")) {
				whereCondtion = whereCondtion.append(" directory like '%" + brands + "%'");
			}

			if (!companyName.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from CheckReportEntity where")) {
					whereCondtion = whereCondtion.append(" businessname like '%" + companyName + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and businessname like '%" + companyName + "%'");
				}
			}

			if (!locationPhone.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from CheckReportEntity where")) {
					whereCondtion = whereCondtion.append(" phone like '%" + locationPhone + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and phone like '%" + locationPhone + "%'");
				}
			}
			if (!locationAddress.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from CheckReportEntity where")) {
					whereCondtion = whereCondtion.append(" address like '%" + locationAddress + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and address like '%" + locationAddress + "%'");
				}
			}
			if (!locationCity.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from CheckReportEntity where")) {
					whereCondtion = whereCondtion.append(" city like '%" + locationCity + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and city like '%" + locationCity + "%'");
				}
			}
			if (!locationState.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from CheckReportEntity where")) {
					whereCondtion = whereCondtion.append(" state like '%" + locationState + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and state like '%" + locationState + "%'");
				}
			}
			if (!locationZipCode.equals("")) {
				if (whereCondtion.toString().equalsIgnoreCase("from CheckReportEntity where")) {
					whereCondtion = whereCondtion.append(" zip like '%" + locationZipCode + "%'");
				} else {
					whereCondtion = whereCondtion.append(" and zip like '%" + locationZipCode + "%'");
				}
			}

			logger.info("### businesslisting search fields Query : " + whereCondtion);
			List<CheckReportDTO> whereCoditionRecords = getWhereCoditionCheckRecords(whereCondtion.toString(), session);
			if (whereCoditionRecords.size() > 0) {
				for (CheckReportDTO localBusinessDTO : whereCoditionRecords) {
					setOfBusinessRecords.add(localBusinessDTO);
				}
			}

		}
		return setOfBusinessRecords;
	}

	public List<CheckReportDTO> getWhereCoditionCheckRecords(String query, Session session) {
		List<CheckReportDTO> nameList = new ArrayList<CheckReportDTO>();
		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			List<String> allBrandNames = new ArrayList<String>();
			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId == LBLConstants.PURIST) {
				allBrandNames = getAllBrandNames(false, userName);
			} else if (roleId == LBLConstants.CLIENT_ADMIN) {
				Criteria userCriteria = session.createCriteria(UsersEntity.class);
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

				if (roleId == LBLConstants.CHANNEL_ADMIN || roleId == LBLConstants.PURIST) {
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
				if (roleId == LBLConstants.CHANNEL_ADMIN || roleId == LBLConstants.PURIST) {
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
		List<CheckReportEntity> listOfName = (List<CheckReportEntity>) createQuery.list();
		for (CheckReportEntity localBusinessBean : listOfName) {
			CheckReportDTO dto = new CheckReportDTO();
			BeanUtils.copyProperties(localBusinessBean, dto);
			nameList.add(dto);
		}
		return nameList;
	}

	public List<CheckReportDTO> getcheckreport(Set<CheckReportDTO> businesSearchinfo) {

		return null;
	}

	public List<CheckReportDTO> checkReportSearch(String store) {
		List<CheckReportDTO> brandInfoDTO = new ArrayList<CheckReportDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CheckReportEntity.class);

		criteria.add(Restrictions.eq("store", store));
		List<CheckReportEntity> list = criteria.list();
		int size = list.size();
		logger.info("size:::::::::::::::::::::" + size);
		for (CheckReportEntity entity : list) {
			CheckReportDTO dto = new CheckReportDTO();

			BeanUtils.copyProperties(entity, dto);
			brandInfoDTO.add(dto);

		}
		return brandInfoDTO;
	}

	public Set<CheckReportDTO> CheckReportSearchInfo(String store, String locationAddress, String brandname) {
		Set<CheckReportDTO> brandInfoDTO = new LinkedHashSet<CheckReportDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CheckReportEntity.class);
		Integer brandId = getClinetIdIdByName(brandname);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("brandId", brandId));
		criteria.addOrder(Order.desc("checkedDate"));
		/*
		 * criteria.addOrder(Order.asc("directory"));
		 * criteria.addOrder(Order.desc("checkedDate"));
		 * criteria.add(Restrictions.eq("zip", locationZipCode));
		 */

		CheckReportDTO googleDto = new CheckReportDTO();
		CheckReportDTO bingDto = new CheckReportDTO();
		CheckReportDTO yelpDto = new CheckReportDTO();
		CheckReportDTO YahooDto = new CheckReportDTO();
		CheckReportDTO ypDto = new CheckReportDTO();
		CheckReportDTO citysearchDto = new CheckReportDTO();
		CheckReportDTO mapquestDto = new CheckReportDTO();
		CheckReportDTO superpagesDto = new CheckReportDTO();
		CheckReportDTO yellobookDto = new CheckReportDTO();
		CheckReportDTO whitepagesDto = new CheckReportDTO();

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

		List<CheckReportEntity> list = criteria.list();
		for (CheckReportEntity checkReportEntity : list) {

			if ("google".equalsIgnoreCase(checkReportEntity.getDirectory()) && googleCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, googleDto);
				String webAddress = googleDto.getWebsite();
				if (webAddress != null && webAddress.length() > 1 && webAddress.contains("www")) {
					webAddress = webAddress.substring(webAddress.indexOf("www.") + 4);
				}

				if (webAddress != null && webAddress.length() > 0 && webAddress.contains("?")) {
					webAddress = webAddress.substring(0, webAddress.indexOf("?") + 1);
				}
				googleDto.setActualWebAdress(webAddress);
				String googleAddress = googleDto.getAddress();
				if (googleAddress != null) {
					String addresscolorcode = getFormattedlocationAddress(googleAddress, locationAddress);
					googleDto.setColourcode(addresscolorcode);
				}

				String state2 = googleDto.getState();
				String phone = googleDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					googleDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					googleDto.setState(state);
				} else {
					googleDto.setState(state2);
				}
				brandInfoDTO.add(googleDto);
				googleCount++;
			}
			if ("foursquare".equalsIgnoreCase(checkReportEntity.getDirectory()) && bingCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, bingDto);
				String webAddress = bingDto.getWebsite();
				if (webAddress != null && webAddress.length() > 1 && webAddress.contains("www")) {
					webAddress = webAddress.substring(webAddress.indexOf("www.") + 4);
				}

				if (webAddress != null && webAddress.length() > 0 && webAddress.contains("?")) {
					webAddress = webAddress.substring(0, webAddress.indexOf("?") + 1);
				}
				bingDto.setActualWebAdress(webAddress);
				String bingAddress = bingDto.getAddress();
				if (bingAddress != null) {
					String addresscolorcode = getFormattedlocationAddress(bingAddress, locationAddress);
					bingDto.setColourcode(addresscolorcode);
				}

				String phone = bingDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					bingDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				String state2 = bingDto.getState();

				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					bingDto.setState(state);
				} else {
					bingDto.setState(state2);
				}

				brandInfoDTO.add(bingDto);

				bingCount++;
			}
			if ("yelp".equalsIgnoreCase(checkReportEntity.getDirectory()) && yelpCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, yelpDto);
				String webAddress = yelpDto.getWebsite();
				if (webAddress != null && webAddress.length() > 1 && webAddress.contains("www")) {
					webAddress = webAddress.substring(webAddress.indexOf("www.") + 4);
				}

				if (webAddress != null && webAddress.length() > 0 && webAddress.contains("?")) {
					webAddress = webAddress.substring(0, webAddress.indexOf("?") + 1);
				}
				yelpDto.setActualWebAdress(webAddress);
				String yelpAddress = yelpDto.getAddress();
				if (yelpAddress != null) {
					String addresscolorcode = getFormattedlocationAddress(yelpAddress, locationAddress);
					yelpDto.setColourcode(addresscolorcode);
				}

				String phone = yelpDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					yelpDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				String state2 = yelpDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					yelpDto.setState(state);
				} else {
					yelpDto.setState(state2);
				}

				brandInfoDTO.add(yelpDto);
				yelpCount++;
			}
			if ("yahoo".equalsIgnoreCase(checkReportEntity.getDirectory()) && YahooCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, YahooDto);
				String webAddress = YahooDto.getWebsite();
				if (webAddress != null && webAddress.length() > 1 && webAddress.contains("www")) {
					webAddress = webAddress.substring(webAddress.indexOf("www.") + 4);
				}

				if (webAddress != null && webAddress.length() > 0 && webAddress.contains("?")) {
					webAddress = webAddress.substring(0, webAddress.indexOf("?") + 1);
				}
				YahooDto.setActualWebAdress(webAddress);
				String yahooaddress = YahooDto.getAddress();
				if (yahooaddress != null) {
					String addresscolorcode = getFormattedlocationAddress(yahooaddress, locationAddress);
					YahooDto.setColourcode(addresscolorcode);
				}

				String state2 = YahooDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					YahooDto.setState(state);
				} else {
					YahooDto.setState(state2);
				}
				String phone = YahooDto.getPhone();
				logger.info("phone::::::::::::" + phone);
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

				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");

				if (phone != null && !phone.isEmpty()) {

					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					YahooDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(YahooDto);
				YahooCount++;
			}
			if ("yp".equalsIgnoreCase(checkReportEntity.getDirectory()) && ypCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, ypDto);
				String webAddress = ypDto.getWebsite();
				if (webAddress != null && webAddress.length() > 1 && webAddress.contains("www")) {
					webAddress = webAddress.substring(webAddress.indexOf("www.") + 4);
				}

				if (webAddress != null && webAddress.length() > 0 && webAddress.contains("?")) {
					webAddress = webAddress.substring(0, webAddress.indexOf("?") + 1);
				}
				ypDto.setActualWebAdress(webAddress);
				String ypAddress = ypDto.getAddress();
				if (ypAddress != null) {
					String addresscolorcode = getFormattedlocationAddress(ypAddress, locationAddress);
					ypDto.setColourcode(addresscolorcode);
				}

				String state2 = ypDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					ypDto.setState(state);
				} else {
					ypDto.setState(state2);
				}
				String phone = ypDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					ypDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(ypDto);
				ypCount++;
			}
			if ("citysearch".equalsIgnoreCase(checkReportEntity.getDirectory()) && citysearchCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, citysearchDto);
				String webAddress = citysearchDto.getWebsite();
				if (webAddress != null && webAddress.length() > 1 && webAddress.contains("www")) {
					webAddress = webAddress.substring(webAddress.indexOf("www.") + 4);
				}

				if (webAddress != null && webAddress.length() > 0 && webAddress.contains("?")) {
					webAddress = webAddress.substring(0, webAddress.indexOf("?") + 1);
				}
				citysearchDto.setActualWebAdress(webAddress);
				String citySearchAddress = citysearchDto.getAddress();
				if (citySearchAddress != null) {
					String addresscolorcode = getFormattedlocationAddress(citySearchAddress, locationAddress);
					citysearchDto.setColourcode(addresscolorcode);
				}

				String state2 = citysearchDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					citysearchDto.setState(state);
				} else {
					citysearchDto.setState(state2);
				}
				String phone = citysearchDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					citysearchDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(citysearchDto);
				citysearchCount++;
			}
			if ("mapquest".equalsIgnoreCase(checkReportEntity.getDirectory()) && mapquestCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, mapquestDto);
				String webAddress = mapquestDto.getWebsite();
				if (webAddress != null && webAddress.length() > 1 && webAddress.contains("www")) {
					webAddress = webAddress.substring(webAddress.indexOf("www.") + 4);
				}

				if (webAddress != null && webAddress.length() > 0 && webAddress.contains("?")) {
					webAddress = webAddress.substring(0, webAddress.indexOf("?") + 1);
				}
				mapquestDto.setActualWebAdress(webAddress);
				String mapquestaddress = mapquestDto.getAddress();
				if (mapquestaddress != null) {
					String addresscolorcode = getFormattedlocationAddress(mapquestaddress, locationAddress);
					mapquestDto.setColourcode(addresscolorcode);
				}

				String state2 = mapquestDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					mapquestDto.setState(state);
				} else {
					mapquestDto.setState(state2);
				}
				String phone = mapquestDto.getPhone();
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

				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					mapquestDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(mapquestDto);
				mapquestCount++;

			}
			if ("uslocal".equalsIgnoreCase(checkReportEntity.getDirectory()) && superpagesCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, superpagesDto);
				String webAddress = superpagesDto.getWebsite();
				if (webAddress != null && webAddress.length() > 1 && webAddress.contains("www")) {
					webAddress = webAddress.substring(webAddress.indexOf("www.") + 4);
				}

				if (webAddress != null && webAddress.length() > 0 && webAddress.contains("?")) {
					webAddress = webAddress.substring(0, webAddress.indexOf("?") + 1);
				}
				superpagesDto.setActualWebAdress(webAddress);
				String superPagesAddress = superpagesDto.getAddress();
				if (superPagesAddress != null) {
					String addresscolorcode = getFormattedlocationAddress(superPagesAddress, locationAddress);
					superpagesDto.setColourcode(addresscolorcode);
				}

				String state2 = superpagesDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					superpagesDto.setState(state);
				} else {
					superpagesDto.setState(state2);
				}
				String phone = superpagesDto.getPhone();
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

				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					superpagesDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(superpagesDto);
				superpagesCount++;
			}
			if ("yellowbot".equalsIgnoreCase(checkReportEntity.getDirectory()) && yellobookCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, yellobookDto);
				String webAddress = yellobookDto.getWebsite();
				if (webAddress != null && webAddress.length() > 1 && webAddress.contains("www")) {
					webAddress = webAddress.substring(webAddress.indexOf("www.") + 4);
				}

				if (webAddress != null && webAddress.length() > 0 && webAddress.contains("?")) {
					webAddress = webAddress.substring(0, webAddress.indexOf("?") + 1);
				}
				yellobookDto.setActualWebAdress(webAddress);
				String yellowBookAddress = yellobookDto.getAddress();
				if (yellowBookAddress != null) {
					String addresscolorcode = getFormattedlocationAddress(yellowBookAddress, locationAddress);
					yellobookDto.setColourcode(addresscolorcode);
				}

				String state2 = yellobookDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					yellobookDto.setState(state);
				} else {
					yellobookDto.setState(state2);
				}
				String phone = yellobookDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");

				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					yellobookDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(yellobookDto);
				yellobookCount++;
			}
			if ("usplaces".equalsIgnoreCase(checkReportEntity.getDirectory()) && whitepagesCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, whitepagesDto);
				String webAddress = whitepagesDto.getWebsite();
				if (webAddress != null && webAddress.length() > 1 && webAddress.contains("www")) {
					webAddress = webAddress.substring(webAddress.indexOf("www.") + 4);
				}

				if (webAddress != null && webAddress.length() > 0 && webAddress.contains("?")) {
					webAddress = webAddress.substring(0, webAddress.indexOf("?") + 1);
				}
				whitepagesDto.setActualWebAdress(webAddress);
				String whitePagesaddress = whitepagesDto.getAddress();
				if (whitePagesaddress != null) {
					String addresscolorcode = getFormattedlocationAddress(whitePagesaddress, locationAddress);
					whitepagesDto.setColourcode(addresscolorcode);
				}

				String state2 = whitepagesDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					whitepagesDto.setState(state);
				} else {
					whitepagesDto.setState(state2);
				}
				String phone = whitepagesDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");

				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					whitepagesDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(whitepagesDto);

				whitepagesCount++;
			}

		}

		List<CheckReportDTO> brandInfoDTOs = new ArrayList<CheckReportDTO>(brandInfoDTO);

		String directories[] = { "google", "foursquare", "yahoo", "yelp", "yp", "citysearch", "mapquest", "ezlocal",
				"yellowbot", "usplaces" };

		List<String> directoryList = Arrays.asList(directories);
		List<String> foundDirectoryList = new ArrayList<String>();

		for (CheckReportDTO checkReportDTO : brandInfoDTOs) {
			foundDirectoryList.add(checkReportDTO.getDirectory());
		}

		for (String directory : directoryList) {
			if (!foundDirectoryList.contains(directory)) {
				CheckReportDTO reportDTO = new CheckReportDTO();
				reportDTO.setDirectory(directory);
				reportDTO.setNoOfErrors(-1);
				brandInfoDTO.add(reportDTO);
			}
		}

		Set<CheckReportDTO> checkReports = new LinkedHashSet<CheckReportDTO>();

		for (int i = 0; i < directories.length; i++) {
			for (CheckReportDTO checkReport : brandInfoDTO) {
				if (directories[i].equals(checkReport.getDirectory())) {
					checkReports.add(checkReport);
				}
			}
		}

		return checkReports;
	}

	private String getFormattedlocationAddress(String address, String locationAddress) {
		String adressColorCode = "B";
		String[] locationAddressArray = locationAddress.split(" ");
		String lastWordinLocationAddress = "";
		if (locationAddressArray.length > 0) {
			lastWordinLocationAddress = locationAddressArray[locationAddressArray.length - 1];
		}

		String[] locationDirectoryAddressArray = address.split(" ");
		String lastWordinDirectoryddress = "";
		if (locationAddressArray.length > 0) {
			lastWordinDirectoryddress = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 1];

		}

		if (!locationAddress.equalsIgnoreCase(address)) {

			if (!lastWordinDirectoryddress.equalsIgnoreCase(lastWordinLocationAddress)) {
				// get the the Abbrevation
				boolean isAbbreviationExist = isAbbreviationExist(lastWordinDirectoryddress, lastWordinLocationAddress);

				String secondLastWordInDirectory = "";
				String secondLastWordInLBL = "";

				if (locationAddressArray.length > 1 && locationDirectoryAddressArray.length > 1) {
					secondLastWordInLBL = locationAddressArray[locationAddressArray.length - 2];
					secondLastWordInDirectory = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 2];
				}

				boolean isSecondWordsMatch = false;
				if (secondLastWordInLBL != "" && secondLastWordInDirectory != "") {
					isSecondWordsMatch = secondLastWordInLBL.equalsIgnoreCase(secondLastWordInDirectory);
				}

				if (!isAbbreviationExist || !isSecondWordsMatch) {
					adressColorCode = "R";
				}
			}
		}
		return adressColorCode;

	}

	private boolean isAbbreviationExist(String lastWordinDirectoryddress, String lastWordinLocationAddress) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(StandardStreetAbbreviationsEntity.class);
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.eq("name", lastWordinDirectoryddress));
		disjunction.add(Restrictions.eq("abbreviation", lastWordinDirectoryddress));
		criteria.add(disjunction);

		List<StandardStreetAbbreviationsEntity> list = criteria.list();
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	public List<CheckReportDTO> checkListing(String listingId) {

		List<CheckReportDTO> brandInfoDTO = new ArrayList<CheckReportDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CheckReportEntity.class);

		criteria.add(Restrictions.eq("listingId", Integer.parseInt(listingId)));

		List<CheckReportEntity> list = criteria.list();
		int size = list.size();
		logger.info("size in checklisting:::::::::::::::::::::" + size);
		for (CheckReportEntity entity : list) {
			CheckReportDTO dto = new CheckReportDTO();

			BeanUtils.copyProperties(entity, dto);
			String phone = dto.getPhone();
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
			java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
			if (phone != null && !phone.isEmpty()) {
				String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
				dto.setPhone(phoneMsgFmt.format(phoneNumArr));
			}

			// dto.setPhone(phone);
			String state2 = dto.getState();
			if (state2 != null && state2.length() > 2) {
				String state = getStateFromStateList(state2);
				dto.setState(state);
			} else {
				dto.setState(state2);
			}

			brandInfoDTO.add(dto);

		}
		return brandInfoDTO;
	}

	private String getStateFromStateList(String state) {
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

	public List<LocalBusinessDTO> getBusinesslisting(String store, String brandname) {
		List<LocalBusinessDTO> brandInfoDTO = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		Integer brandid = getClinetIdIdByName(brandname);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("clientId", brandid));
		List<LocalBusinessEntity> list = criteria.list();
		int size = list.size();
		logger.info("Listings size is: " + size);
		for (LocalBusinessEntity entity : list) {
			LocalBusinessDTO dto = new LocalBusinessDTO();

			BeanUtils.copyProperties(entity, dto);
			String phone = dto.getLocationPhone();

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

			java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
			if (phone != null && !phone.isEmpty()) {
				String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
				dto.setLocationPhone(phoneMsgFmt.format(phoneNumArr));
			}

			String companyName = dto.getCompanyName();

			String companyName2 = companyName.replaceAll("&", "");

			dto.setCompanyName(companyName2);

			brandInfoDTO.add(dto);

		}
		return brandInfoDTO;
	}

	public Integer getClinetIdIdByName(String clinetname) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", clinetname));
		Integer ClientId = 0;
		List<BrandEntity> list = criteria.list();
		for (BrandEntity brandEntity : list) {
			ClientId = brandEntity.getClientId();
		}
		return ClientId;
	}

	public Integer getErrorCount(String directory, String store) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CheckReportEntity.class);
		criteria.add(Restrictions.eq("directory", directory));
		criteria.add(Restrictions.eq("store", store));
		criteria.addOrder(Order.desc("checkedDate"));
		Integer errorcount = -1;
		List<CheckReportEntity> list = criteria.list();
		for (CheckReportEntity entity : list) {
			errorcount = entity.getNoOfErrors();
			logger.info("errorcount:::" + errorcount);
			break;
		}
		if (errorcount == null) {
			errorcount = -1;
		}
		logger.info("errorcount:::" + errorcount);

		return errorcount;

	}

	public Map<String, Integer> getErrorsCount(String store, String brandname) {
		Integer clientId = getClinetIdIdByName(brandname);
		Map<String, Integer> countDetails = new HashMap<String, Integer>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CheckReportEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("brandId", clientId));
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
			} else if (!countDetails.containsKey(directory) && directory.equalsIgnoreCase("google")) {
				countDetails.put(directory, errorcount);
			} else if (!countDetails.containsKey(directory) && directory.equalsIgnoreCase("bing")) {

				countDetails.put(directory, errorcount);
			} else if (!countDetails.containsKey(directory) && directory.equalsIgnoreCase("yahoo")) {

				countDetails.put(directory, errorcount);
			} else if (!countDetails.containsKey(directory) && directory.equalsIgnoreCase("yelp")) {

				countDetails.put(directory, errorcount);
			} else if (!countDetails.containsKey(directory) && directory.equalsIgnoreCase("yp")) {

				countDetails.put(directory, errorcount);
			} else if (!countDetails.containsKey(directory) && directory.equalsIgnoreCase("citysearch")) {

				countDetails.put(directory, errorcount);
			} else if (!countDetails.containsKey(directory) && directory.equalsIgnoreCase("mapquest")) {

				countDetails.put(directory, errorcount);
			} else if (!countDetails.containsKey(directory) && directory.equalsIgnoreCase("superpages")) {

				countDetails.put(directory, errorcount);
			} else if (!countDetails.containsKey(directory) && directory.equalsIgnoreCase("yellowbook")) {

				countDetails.put(directory, errorcount);
			} else if (!countDetails.containsKey(directory) && directory.equalsIgnoreCase("whitepages")) {

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

	public Integer getPathCountFromSearchDomain(String store) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SearchDomains.class);
		criteria.add(Restrictions.eq("searchId", store));

		Integer countOfCitations = 0;

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
		List<SearchDomains> list = criteria.list();
		for (SearchDomains searchDomains : list) {

			if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "google") && googleCount == 0) {
				int count = 0;
				if (searchDomains.getPathsCount() != null) {
					count = searchDomains.getPathsCount().intValue();
				}
				countOfCitations = countOfCitations + count;

				googleCount++;
			} else if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "bing") && bingCount == 0) {
				int count = 0;

				if (searchDomains.getPathsCount() != null) {
					count = searchDomains.getPathsCount().intValue();
				}
				countOfCitations = countOfCitations + count;
				bingCount++;
			} else if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "yelp") && yelpCount == 0) {
				int count = 0;
				if (searchDomains.getPathsCount() != null) {
					count = searchDomains.getPathsCount().intValue();
				}
				countOfCitations = countOfCitations + count;
				yelpCount++;
			} else if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "yahoo") && YahooCount == 0) {
				int count = 0;
				if (searchDomains.getPathsCount() != null) {
					count = searchDomains.getPathsCount().intValue();
				}
				countOfCitations = countOfCitations + count;
				YahooCount++;
			} else if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "yp") && ypCount == 0) {
				int count = 0;
				if (searchDomains.getPathsCount() != null) {
					count = searchDomains.getPathsCount().intValue();
				}
				countOfCitations = countOfCitations + count;
				ypCount++;
			} else if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "citysearch")
					&& citysearchCount == 0) {
				int count = 0;
				if (searchDomains.getPathsCount() != null) {
					count = searchDomains.getPathsCount().intValue();
				}
				countOfCitations = countOfCitations + count;
				citysearchCount++;
			} else if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "mapquest")
					&& mapquestCount == 0) {
				int count = 0;
				if (searchDomains.getPathsCount() != null) {
					count = searchDomains.getPathsCount().intValue();
				}
				countOfCitations = countOfCitations + count;
				mapquestCount++;
			} else if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "superpages")
					&& superpagesCount == 0) {
				int count = 0;
				if (searchDomains.getPathsCount() != null) {
					count = searchDomains.getPathsCount().intValue();
				}
				countOfCitations = countOfCitations + count;
				superpagesCount++;
			} else if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "yellowbook")
					&& yellobookCount == 0) {
				int count = 0;
				if (searchDomains.getPathsCount() != null) {
					count = searchDomains.getPathsCount().intValue();
				}
				countOfCitations = countOfCitations + count;
				yellobookCount++;
			} else if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "whitepages")
					&& whitepagesCount == 0) {
				int count = 0;
				if (searchDomains.getPathsCount() != null) {
					count = searchDomains.getPathsCount().intValue();
				}
				countOfCitations = countOfCitations + count;
				whitepagesCount++;
			}

		}
		return countOfCitations;
	}

	public List<AccuracyGraphDTO> getGraphInfo(String brandname) {
		Session session = sessionFactory.getCurrentSession();

		String sql = "FROM AccuarcyGraphEntity  WHERE brandName =?  GROUP BY WEEK(DATE) ORDER BY DATE ASC";
		Query createQuery = session.createQuery(sql);
		createQuery.setString(0, brandname);

		List<AccuracyGraphDTO> listDto = new ArrayList<AccuracyGraphDTO>();
		List<AccuarcyGraphEntity> list = createQuery.list();
		for (AccuarcyGraphEntity accuracyEntity : list) {
			AccuracyGraphDTO accuracyDTO = new AccuracyGraphDTO();

			BeanUtils.copyProperties(accuracyEntity, accuracyDTO);

			Date date = accuracyDTO.getDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			String formatted = String.format("%02d", month);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			logger.info(date);
			accuracyDTO.setDay(day);
			accuracyDTO.setMonth(Integer.valueOf(formatted));
			accuracyDTO.setYear(year);
			listDto.add(accuracyDTO);
		}

		return listDto;
	}

	public List<LocalBusinessDTO> getListOfBusinessInfo(String client) {
		Session session = sessionFactory.getCurrentSession();

		String nameQuery = " From LocalBusinessEntity where client='Sprint'";

		Query createQuery = session.createQuery(nameQuery);

		List<LocalBusinessEntity> businessEntities = createQuery.list();
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		for (LocalBusinessEntity localBusinessEntity : businessEntities) {
			LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessEntity, localBusinessDTO);
			businessDTOs.add(localBusinessDTO);
		}
		return businessDTOs;

	}

	public List<CheckReportDTO> getcheckreportInfo(String directory, String store, Integer brandId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CheckReportEntity.class);
		criteria.add(Restrictions.eq("directory", directory));
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("brandId", brandId));
		logger.info("store::::::::" + store);
		List<CheckReportEntity> businessEntities = criteria.list();
		List<CheckReportDTO> businessDTOs = new ArrayList<CheckReportDTO>();
		for (CheckReportEntity localBusinessEntity : businessEntities) {
			CheckReportDTO localBusinessDTO = new CheckReportDTO();
			BeanUtils.copyProperties(localBusinessEntity, localBusinessDTO);
			businessDTOs.add(localBusinessDTO);
		}
		return businessDTOs;
	}

	public Set<CheckReportDTO> CheckReportListingSearchInfo(String store, String directory, String brandname) {
		Set<CheckReportDTO> brandInfoDTO = new LinkedHashSet<CheckReportDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CheckReportEntity.class);
		logger.info("directory:::::::::::::" + directory);
		logger.info("store:::::::::::::" + store);
		Integer brandId = getClinetIdIdByName(brandname);
		criteria.add(Restrictions.eq("directory", directory));
		criteria.add(Restrictions.eq("brandId", brandId));
		criteria.add(Restrictions.eq("store", store));
		criteria.addOrder(Order.desc("checkedDate"));
		/*
		 * criteria.addOrder(Order.asc("directory"));
		 * criteria.addOrder(Order.desc("checkedDate"));
		 * criteria.add(Restrictions.eq("zip", locationZipCode));
		 */

		CheckReportDTO googleDto = new CheckReportDTO();
		CheckReportDTO bingDto = new CheckReportDTO();
		CheckReportDTO yelpDto = new CheckReportDTO();
		CheckReportDTO YahooDto = new CheckReportDTO();
		CheckReportDTO ypDto = new CheckReportDTO();
		CheckReportDTO citysearchDto = new CheckReportDTO();
		CheckReportDTO mapquestDto = new CheckReportDTO();
		CheckReportDTO superpagesDto = new CheckReportDTO();
		CheckReportDTO yellobookDto = new CheckReportDTO();
		CheckReportDTO whitepagesDto = new CheckReportDTO();

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

		List<CheckReportEntity> list = criteria.list();
		for (CheckReportEntity checkReportEntity : list) {

			if ("google".equalsIgnoreCase(checkReportEntity.getDirectory()) && googleCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, googleDto);
				String state2 = googleDto.getState();
				String phone = googleDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					googleDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					googleDto.setState(state);
				} else {
					googleDto.setState(state2);
				}
				brandInfoDTO.add(googleDto);
				googleCount++;
			}
			if ("foursquare".equalsIgnoreCase(checkReportEntity.getDirectory()) && bingCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, bingDto);
				String phone = bingDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					bingDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				String state2 = bingDto.getState();

				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					bingDto.setState(state);
				} else {
					bingDto.setState(state2);
				}

				brandInfoDTO.add(bingDto);

				bingCount++;
			}
			if ("yelp".equalsIgnoreCase(checkReportEntity.getDirectory()) && yelpCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, yelpDto);
				String phone = yelpDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					yelpDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				String state2 = yelpDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					yelpDto.setState(state);
				} else {
					yelpDto.setState(state2);
				}

				brandInfoDTO.add(yelpDto);
				yelpCount++;
			}
			if ("yahoo".equalsIgnoreCase(checkReportEntity.getDirectory()) && YahooCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, YahooDto);
				String state2 = YahooDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					YahooDto.setState(state);
				} else {
					YahooDto.setState(state2);
				}
				String phone = YahooDto.getPhone();
				// logger.info("phone::::::::::::" + phone);
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

				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");

				if (phone != null && !phone.isEmpty()) {

					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					YahooDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(YahooDto);
				YahooCount++;
			}
			if ("yp".equalsIgnoreCase(checkReportEntity.getDirectory()) && ypCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, ypDto);
				String state2 = ypDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					ypDto.setState(state);
				} else {
					ypDto.setState(state2);
				}
				String phone = ypDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					ypDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(ypDto);
				ypCount++;
			}
			if ("citysearch".equalsIgnoreCase(checkReportEntity.getDirectory()) && citysearchCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, citysearchDto);
				String state2 = citysearchDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					citysearchDto.setState(state);
				} else {
					citysearchDto.setState(state2);
				}
				String phone = citysearchDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					citysearchDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(citysearchDto);
				citysearchCount++;
			}
			if ("mapquest".equalsIgnoreCase(checkReportEntity.getDirectory()) && mapquestCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, mapquestDto);
				String state2 = mapquestDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					mapquestDto.setState(state);
				} else {
					mapquestDto.setState(state2);
				}
				String phone = mapquestDto.getPhone();
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

				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					mapquestDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(mapquestDto);
				mapquestCount++;

			}
			if ("usplaces".equalsIgnoreCase(checkReportEntity.getDirectory()) && superpagesCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, superpagesDto);
				String state2 = superpagesDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					superpagesDto.setState(state);
				} else {
					superpagesDto.setState(state2);
				}
				String phone = superpagesDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");
				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					superpagesDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(superpagesDto);
				superpagesCount++;
			}
			if ("yellowbot".equalsIgnoreCase(checkReportEntity.getDirectory()) && yellobookCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, yellobookDto);
				String state2 = yellobookDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					yellobookDto.setState(state);
				} else {
					yellobookDto.setState(state2);
				}
				String phone = yellobookDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");

				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					yellobookDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(yellobookDto);
				yellobookCount++;
			}
			if ("ezlocal".equalsIgnoreCase(checkReportEntity.getDirectory()) && whitepagesCount == 0) {
				BeanUtils.copyProperties(checkReportEntity, whitepagesDto);
				String state2 = whitepagesDto.getState();
				if (state2 != null && state2.length() > 2) {
					String state = getStateFromStateList(state2);
					whitepagesDto.setState(state);
				} else {
					whitepagesDto.setState(state2);
				}
				String phone = whitepagesDto.getPhone();
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
				java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}-{2}");

				if (phone != null && !phone.isEmpty()) {
					String[] phoneNumArr = { phone.substring(0, 3), phone.substring(3, 6), phone.substring(6) };
					whitepagesDto.setPhone(phoneMsgFmt.format(phoneNumArr));
				}

				brandInfoDTO.add(whitepagesDto);

				whitepagesCount++;
			}

		}

		return brandInfoDTO;
	}

	public List<CitationGraphDTO> getCitationGraphInfo(String brandname) {
		Session session = sessionFactory.getCurrentSession();
		List<CitationGraphDTO> businessDTOs = new ArrayList<CitationGraphDTO>();
		String sql = "FROM CitationGraphEntity  WHERE brandName =?  GROUP BY WEEK(DATE) ORDER BY DATE ASC";
		Query createQuery = session.createQuery(sql);
		createQuery.setString(0, brandname);
		List<CitationGraphEntity> businessEntities = createQuery.list();
		for (CitationGraphEntity citationGraphEntity : businessEntities) {
			CitationGraphDTO citationGraphDTO = new CitationGraphDTO();
			BeanUtils.copyProperties(citationGraphEntity, citationGraphDTO);
			Date date = citationGraphDTO.getDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			logger.info(date);
			citationGraphDTO.setDay(day);
			citationGraphDTO.setMonth(month);
			citationGraphDTO.setYear(year);
			businessDTOs.add(citationGraphDTO);

		}

		return businessDTOs;

	}

	public LocalBusinessDTO getBusinessInfoByStore(String store) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("store", store));
		LocalBusinessDTO businessDTO = null;
		List<LocalBusinessEntity> list = criteria.list();
		for (LocalBusinessEntity localBusinessEntity : list) {
			businessDTO = new LocalBusinessDTO();
			BeanUtils.copyProperties(localBusinessEntity, businessDTO);
		}

		return businessDTO;
	}

	public Map<String, List<String>> getPathFromSearch(String store, Integer brandId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SearchDomains.class);
		criteria.add(Restrictions.eq("searchId", store));
		criteria.addOrder(Order.desc("dateSearched"));

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
				if (domains.getPathsCount().intValue() > 1) {

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

					paths.add(path);
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

	public Map<String, List<String>> getDomainAuthorities(String store, Integer brandId) {
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

	public Map<String, Integer> getDomainCitationCount(String store) {
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
		int countOfCitations = 0;
		Map<String, Integer> domainAuthorities = new HashMap<String, Integer>();
		List<SearchDomains> list = criteria.list();

		if (list.size() != 0 && !list.isEmpty()) {
			for (SearchDomains searchDomains : list) {

				if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "google") && googleCount == 0) {
					int count = 0;
					if (searchDomains.getPathsCount() != null) {
						count = searchDomains.getPathsCount().intValue();
					}
					countOfCitations = countOfCitations + count;

					googleCount++;
				}
				if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "bing") && bingCount == 0) {
					int count = 0;

					if (searchDomains.getPathsCount() != null) {
						count = searchDomains.getPathsCount().intValue();
					}
					countOfCitations = countOfCitations + count;
					bingCount++;
				}
				if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "yelp") && yelpCount == 0) {
					int count = 0;
					if (searchDomains.getPathsCount() != null) {
						count = searchDomains.getPathsCount().intValue();
					}
					countOfCitations = countOfCitations + count;
					yelpCount++;
				}
				if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "yahoo") && YahooCount == 0) {
					int count = 0;
					if (searchDomains.getPathsCount() != null) {
						count = searchDomains.getPathsCount().intValue();
					}
					countOfCitations = countOfCitations + count;
					YahooCount++;
				}
				if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "yp") && ypCount == 0) {
					int count = 0;
					if (searchDomains.getPathsCount() != null) {
						count = searchDomains.getPathsCount().intValue();
					}
					countOfCitations = countOfCitations + count;
					ypCount++;
				}
				if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "citysearch")
						&& citysearchCount == 0) {
					int count = 0;
					if (searchDomains.getPathsCount() != null) {
						count = searchDomains.getPathsCount().intValue();
					}
					countOfCitations = countOfCitations + count;
					citysearchCount++;
				}
				if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "mapquest") && mapquestCount == 0) {
					int count = 0;
					if (searchDomains.getPathsCount() != null) {
						count = searchDomains.getPathsCount().intValue();
					}
					countOfCitations = countOfCitations + count;
					mapquestCount++;
				}
				if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "superpages")
						&& superpagesCount == 0) {
					int count = 0;
					if (searchDomains.getPathsCount() != null) {
						count = searchDomains.getPathsCount().intValue();
					}
					countOfCitations = countOfCitations + count;
					superpagesCount++;
				}
				if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "yellowbook")
						&& yellobookCount == 0) {
					int count = 0;
					if (searchDomains.getPathsCount() != null) {
						count = searchDomains.getPathsCount().intValue();
					}
					countOfCitations = countOfCitations + count;
					yellobookCount++;
				}
				if (StringUtils.containsIgnoreCase(searchDomains.getDomainName(), "whitepages")
						&& whitepagesCount == 0) {
					int count = 0;
					if (searchDomains.getPathsCount() != null) {
						count = searchDomains.getPathsCount().intValue();
					}
					countOfCitations = countOfCitations + count;
					whitepagesCount++;
				}

			}
		}
		domainAuthorities.put(store, countOfCitations);

		return domainAuthorities;
	}

	public String getdomainAuthorityFromSearchDomains(String store, Integer brandId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SearchDomains.class);
		criteria.add(Restrictions.eq("searchId", store));
		criteria.addOrder(Order.desc("dateSearched"));
		String domainAuthority = "";
		List<SearchDomains> list = criteria.list();
		for (SearchDomains searchDomains : list) {

			domainAuthority = searchDomains.getDomainAuthority();

		}
		return domainAuthority;
	}

	public AccuracyDTO getAccuracyListInfo(String store, String brandName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(AccuracyReportEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("brandname", brandName));
		List<AccuracyReportEntity> list = criteria.list();
		AccuracyDTO accuracyDTO = null;
		if (list.size() > 0) {
			accuracyDTO = new AccuracyDTO();
			AccuracyReportEntity accuracyReportEntity = list.get(0);
			BeanUtils.copyProperties(accuracyReportEntity, accuracyDTO);
		}
		return accuracyDTO;
	}

	public void saveAccuracyInfo(AccuracyDTO dto) {
		Session session = sessionFactory.getCurrentSession();

		AccuracyReportEntity accuracyreportEntity = new AccuracyReportEntity();

		BeanUtils.copyProperties(dto, accuracyreportEntity);

		session.save(accuracyreportEntity);
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

	public Integer isStoreExistInAccuracy(String store, String brandName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(AccuracyReportEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("store", store));
		List<AccuracyReportEntity> list = criteria.list();
		Integer listingid = 0;
		if (list.size() > 0) {
			AccuracyReportEntity accuracyStageReportEntity = list.get(0);

			listingid = accuracyStageReportEntity.getId();
		}
		return listingid;
	}

	public void updateAccuracyInfo(AccuracyDTO dto) {
		Session session = sessionFactory.getCurrentSession();

		AccuracyReportEntity accuracyStageReportEntity = new AccuracyReportEntity();

		BeanUtils.copyProperties(dto, accuracyStageReportEntity);
		session.update(accuracyStageReportEntity);

	}

	public List<AccuracyDTO> getSortByGoogleAccuracyListInfo(String brandname, String flag) {

		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY FIELD(googleColourCode, 'G', 'O', 'R', 'D') "
				+ flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public List<AccuracyDTO> getSortByBingAccuracyListInfo(String brandname, String flag) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY FIELD(foursquareColourCode, 'G', 'O', 'R', 'D') "
				+ flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public List<AccuracyDTO> getSortByYahooAccuracyListInfo(String brandname, String flag) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY FIELD(yahooColourCode, 'G', 'O', 'R', 'D') "
				+ flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public List<AccuracyDTO> getSortByYelpAccuracyListInfo(String brandname, String flag) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY FIELD(yelpColourCode, 'G', 'O', 'R', 'D') "
				+ flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public List<AccuracyDTO> getSortByYpAccuracyListInfo(String brandname, String flag) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY FIELD(ypColourCode, 'G', 'O', 'R', 'D') "
				+ flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public List<AccuracyDTO> getSortByCitySearchAccuracyListInfo(String brandname, String flag) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY FIELD(citySearchColourCode, 'G', 'O', 'R', 'D') "
				+ flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public List<AccuracyDTO> getSortByMapquestAccuracyListInfo(String brandname, String flag) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY FIELD(mapquestColourCode, 'G', 'O', 'R', 'D') "
				+ flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public List<AccuracyDTO> getSortBySuperPagesAccuracyListInfo(String brandname, String flag) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY FIELD(uslocalColourCode, 'G', 'O', 'R', 'D') "
				+ flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public List<AccuracyDTO> getSortByYellowBookAccuracyListInfo(String brandname, String flag) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY FIELD(yellobotColourCode, 'G', 'O', 'R', 'D') "
				+ flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public List<AccuracyDTO> getSortByWhitePagesAccuracyListInfo(String brandname, String flag) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY FIELD(ezlocalColourCode, 'G', 'O', 'R', 'D') "
				+ flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public List<AccuracyDTO> getSortByStoreAccuracyListInfo(String brandname, String flag) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY store " + flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public List<AccuracyDTO> getSortByAccuracyListInfo(String brandname, String flag) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "from AccuracyReportEntity  where brandname =? ORDER BY accuracy " + flag + " ";
		List<AccuracyDTO> accuracyDTOs = new ArrayList<AccuracyDTO>();
		Query createQuery = session.createQuery(sql);
		createQuery.setParameter(0, brandname);

		List<AccuracyReportEntity> list = createQuery.list();

		for (AccuracyReportEntity accuracyReportEntity : list) {
			AccuracyDTO dto = new AccuracyDTO();
			BeanUtils.copyProperties(accuracyReportEntity, dto);
			accuracyDTOs.add(dto);
		}
		return accuracyDTOs;
	}

	public void saveAccuracyGraphInfo(AccuracyGraphDTO accuracyGraphDTO) {

		Session session = sessionFactory.getCurrentSession();

		AccuarcyGraphEntity accuracyreportEntity = new AccuarcyGraphEntity();

		BeanUtils.copyProperties(accuracyGraphDTO, accuracyreportEntity);

		session.save(accuracyreportEntity);
	}

	public AccuracyDTO getLocationAccuracy(String store, String brandname) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(AccuracyReportEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("brandname", brandname));
		List<AccuracyReportEntity> list = criteria.list();
		AccuracyDTO accuracyDTO = null;
		if (list.size() > 0) {
			accuracyDTO = new AccuracyDTO();
			AccuracyReportEntity accuracyReportEntity = list.get(0);
			BeanUtils.copyProperties(accuracyReportEntity, accuracyDTO);
		}
		return accuracyDTO;
	}

	public int getGoogleAccuracy(String store, String brandname, String directory) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(AccuracyReportEntity.class);
		criteria.add(Restrictions.eq("store", store));
		criteria.add(Restrictions.eq("brandname", brandname));

		List<AccuracyReportEntity> list = criteria.list();
		int locationAccuracy = 0;
		for (AccuracyReportEntity accuracyReportEntity : list) {
			if (directory.contains("google")) {
				locationAccuracy = accuracyReportEntity.getGoogleAccuracy();
			}
			if (directory.contains("bing")) {
				locationAccuracy = accuracyReportEntity.getBingAccuracy();
			}
			if (directory.contains("mapquest")) {
				locationAccuracy = accuracyReportEntity.getMapQuestAccuracy();
			}
			if (directory.contains("yahoo")) {
				locationAccuracy = accuracyReportEntity.getYahooAccuracy();
			}
			if (directory.contains("yelp")) {
				locationAccuracy = accuracyReportEntity.getYelpAccuracy();
			}
			if (directory.contains("yp")) {
				locationAccuracy = accuracyReportEntity.getYpAccuracy();
			}
			if (directory.contains("citysearch")) {
				locationAccuracy = accuracyReportEntity.getCitySearchAccuracy();
			}
			if (directory.contains("whitepages")) {
				locationAccuracy = accuracyReportEntity.getWhitepagesAccuracy();
			}
			if (directory.contains("yellowbook")) {
				locationAccuracy = accuracyReportEntity.getYellowbookAccuracy();
			}
			if (directory.contains("superpages")) {
				locationAccuracy = accuracyReportEntity.getSuperPagesAccuracy();
			}

		}
		return locationAccuracy;
	}

	public List<CheckReportDTO> getCheckreportInfo() {
		List<CheckReportDTO> checkReportDTOs = new ArrayList<CheckReportDTO>();
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session.createCriteria(CheckReportEntity.class);
		List<CheckReportEntity> list = createCriteria.list();
		for (CheckReportEntity checkReportEntity : list) {
			CheckReportDTO checkReportDTO = new CheckReportDTO();
			BeanUtils.copyProperties(checkReportEntity, checkReportDTO);
			checkReportDTOs.add(checkReportDTO);

		}
		return checkReportDTOs;
	}

	public void updateCheckreportInfo(Integer clinetId, String zip, String store) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "UPDATE CheckReportEntity set brandId=? where zip=? And store=? ";
		Query createQuery = session.createQuery(sql);
		createQuery.setInteger(0, clinetId);
		createQuery.setString(1, zip);
		createQuery.setString(2, store);
		createQuery.executeUpdate();

	}

	public void saveCitationreportInfo(CitationReportEntity citationReportEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.save(citationReportEntity);

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

	public void updateCitationreportInfo(CitationReportEntity citationReportEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.update(citationReportEntity);
	}

	public List<ValueObject> getCiationData(String brandName, String store) {
		Integer brandId = getClinetIdIdByName(brandName);
		Session session = sessionFactory.getCurrentSession();
		String sql = "select store field1, businesName field2, address field3, city field4, state field5, zip field6, phone field7,"
				+ " pathCount field8" + " from citation_report" + " where brandId = ? and store=?";

		try {

			Query q = session.createSQLQuery(sql).addScalar("field1", new StringType())
					.addScalar("field2", new StringType()).addScalar("field3", new StringType())
					.addScalar("field4", new StringType()).addScalar("field5", new StringType())
					.addScalar("field6", new StringType()).addScalar("field7", new StringType())
					.addScalar("field8", new IntegerType())
					.setResultTransformer(Transformers.aliasToBean(ValueObject.class));

			List<ValueObject> ret = q.setInteger(0, brandId).setString(1, store).list();

			if (ret.isEmpty())
				return null;

			return ret;
		} finally {
		}

	}

	public List<ValueObject> getChartDataDS(String brandName) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "select citationCount field10, date field11, '' field12" + " from citation_graph "
				+ " where brandName = ?";

		try {

			Query q = session.createSQLQuery(sql).addScalar("field10", new IntegerType())
					.addScalar("field11", new TimestampType()).addScalar("field12", new StringType())
					.setResultTransformer(Transformers.aliasToBean(ValueObject.class));

			List<ValueObject> ret = q.setString(0, brandName).list();

			if (ret.isEmpty())
				return null;

			return ret;
		} finally {
		}
	}

	public List<RenewalReportDTO> runRenewalReport(String storeName, String brand) {

		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		int actualMaximum = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		Date minDate = calendar.getTime();
		minDate.setDate(actualMaximum);
		Calendar cal = Calendar.getInstance();
		cal.setTime(minDate);
		Date startDate = cal.getTime();
		logger.info("startDate::::::::::::::::::::::::::" + startDate);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(startDate);
		calendar1.add(Calendar.DATE, 61);
		Date endDate = calendar1.getTime();

		logger.info("endDate::::::::::::::::::::::::::" + endDate);
		Criteria createCriteria = currentSession.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.eq("store", storeName));
		createCriteria.add(Restrictions.ne("status", "cancel"));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", startDate, endDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			String email = getEmailFromBrand(brand);
			renewalReportDTO.setEmail(email);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}

		return renewalReportDTOs;

	}

	private String getEmailFromBrand(String brand) {
		Session session = sessionFactory.getCurrentSession();
		Criteria createCriteria = session.createCriteria(BrandEntity.class);
		createCriteria.add(Restrictions.eq("brandName", brand));
		List<BrandEntity> list = createCriteria.list();
		String email = "";
		for (BrandEntity brandEntity : list) {
			email = brandEntity.getEmail();
		}
		return email;
	}

	public List<RenewalReportDTO> runRenewalReportForBrand(String brand) {

		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		int actualMaximum = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		Date minDate = calendar.getTime();
		minDate.setDate(actualMaximum);
		Calendar cal = Calendar.getInstance();
		cal.setTime(minDate);
		Date startDate = cal.getTime();
		logger.info("startDate::::::::::::::::::::::::::" + startDate);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(startDate);
		calendar1.add(Calendar.DATE, 61);
		Date endDate = calendar1.getTime();

		logger.info("endDate::::::::::::::::::::::::::" + endDate);

		Criteria createCriteria = currentSession.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.ne("status", "cancel"));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", startDate, endDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			String email = getEmailFromBrand(brand);
			renewalReportDTO.setEmail(email);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}

		return renewalReportDTOs;

	}

	public List<RenewalReportDTO> runSummaryReport(Date fromDate, Date toDate, String storeName, String brand) {

		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		int actualMaximum = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		Date minDate = calendar.getTime();
		minDate.setDate(actualMaximum);
		Calendar cal = Calendar.getInstance();
		cal.setTime(minDate);
		Date endDate = cal.getTime();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(endDate);
		calendar2.add(Calendar.MONTH, -1);
		Date startDate = calendar2.getTime();

		logger.info("endDate::::::::::::::::::::::::::::::::::" + simpleDateFormat.format(endDate));

		logger.info("startDate::::::::::::::::::::::::::::::::::" + simpleDateFormat.format(startDate));
		Criteria createCriteria = currentSession.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.eq("status", "Renewed"));
		createCriteria.add(Restrictions.eq("store", storeName));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", startDate, endDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			String email = getEmailFromBrand(brand);
			renewalReportDTO.setEmail(email);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}
		renewalReportDTOs.addAll(runSummaryReportForBrandAndStore(brand, storeName));
		return renewalReportDTOs;

	}

	public List<RenewalReportDTO> runSummaryReportForBrand(Date fromDate, Date toDate, String brand) {

		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		int actualMaximum = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		Date minDate = calendar.getTime();
		minDate.setDate(actualMaximum);
		Calendar cal = Calendar.getInstance();
		cal.setTime(minDate);
		Date endDate = cal.getTime();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(endDate);
		calendar2.add(Calendar.MONTH, -1);
		Date startDate = calendar2.getTime();

		logger.info("startDate::::::::::::::::::::::::::::::::::" + simpleDateFormat.format(startDate));

		logger.info("endDate::::::::::::::::::::::::::::::::::" + simpleDateFormat.format(endDate));
		Criteria createCriteria = currentSession.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.eq("status", "Renewed"));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", startDate, endDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			String email = getEmailFromBrand(brand);
			renewalReportDTO.setEmail(email);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}
		renewalReportDTOs.addAll(runRenewalReportForBrand(brand));
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
		int actualMaximum = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		Date minDate = calendar.getTime();
		minDate.setDate(actualMaximum);
		Calendar cal = Calendar.getInstance();
		cal.setTime(minDate);
		Date startDate = cal.getTime();

		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(startDate);
		calendar1.add(Calendar.MONTH, 1);
		Date endDate = calendar1.getTime();

		logger.info("startDate::::::::::::::::::::::::::::::::::" + simpleDateFormat.format(startDate));

		logger.info("endDate::::::::::::::::::::::::::::::::::" + simpleDateFormat.format(endDate));
		Criteria createCriteria = currentSession.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.eq("status", "Active"));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", startDate, endDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}

		return renewalReportDTOs;
	}

	public List<RenewalReportDTO> runSummaryReportForBrandAndStore(String brand, String storeName) {
		List<RenewalReportDTO> renewalReportDTOs = new ArrayList<RenewalReportDTO>();
		Integer clinetId = getClinetIdIdByName(brand);
		Session currentSession = sessionFactory.getCurrentSession();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		int actualMaximum = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		Date minDate = calendar.getTime();
		minDate.setDate(actualMaximum);
		Calendar cal = Calendar.getInstance();
		cal.setTime(minDate);
		Date startDate = cal.getTime();

		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(startDate);
		calendar1.add(Calendar.MONTH, 1);
		Date endDate = calendar1.getTime();

		Criteria createCriteria = currentSession.createCriteria(RenewalReportEntity.class);
		createCriteria.add(Restrictions.eq("clientId", clinetId));
		createCriteria.add(Restrictions.eq("status", "Active"));
		createCriteria.add(Restrictions.eq("store", storeName));
		createCriteria.addOrder(Order.asc("renewalDate"));
		createCriteria.add(Restrictions.between("renewalDate", startDate, endDate));
		List<RenewalReportEntity> list = createCriteria.list();
		for (RenewalReportEntity renewalReportEntity : list) {
			RenewalReportDTO renewalReportDTO = new RenewalReportDTO();
			BeanUtils.copyProperties(renewalReportEntity, renewalReportDTO);
			renewalReportDTO.setBrandName(brand);
			renewalReportDTOs.add(renewalReportDTO);
		}

		return renewalReportDTOs;
	}

	public boolean getGoogleGraphInfo(String store, String string, String brandName) {
		Session session = sessionFactory.getCurrentSession();
		Integer brandId = getClinetIdIdByName(brandName);
		Criteria createCriteria = session.createCriteria(CheckReportEntity.class);
		createCriteria.add(Restrictions.eq("directory", string));
		createCriteria.add(Restrictions.eq("store", store));
		createCriteria.add(Restrictions.eq("brandId", brandId));
		List<CheckReportEntity> list = createCriteria.list();

		return list.size() > 0;
	}

	public List<AccuracyGraphDTO> getTotalListingGraphInfo(String brandname) {
		Session session = sessionFactory.getCurrentSession();

		String sql = "FROM AccuarcyGraphEntity  WHERE brandName =?  GROUP BY MONTH(DATE) ORDER BY DATE ASC";
		Query createQuery = session.createQuery(sql);
		createQuery.setString(0, brandname);

		List<AccuracyGraphDTO> listDto = new ArrayList<AccuracyGraphDTO>();
		List<AccuarcyGraphEntity> list = createQuery.list();
		for (AccuarcyGraphEntity accuracyEntity : list) {
			AccuracyGraphDTO accuracyDTO = new AccuracyGraphDTO();

			BeanUtils.copyProperties(accuracyEntity, accuracyDTO);

			Date date = accuracyDTO.getDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			String format = new SimpleDateFormat("MMM").format(date);

			String formatted = String.format("%02d", month);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			logger.info(date);
			accuracyDTO.setMonthName(format);
			accuracyDTO.setDay(day);
			accuracyDTO.setMonth(Integer.valueOf(formatted));
			accuracyDTO.setYear(year);
			listDto.add(accuracyDTO);
		}

		return listDto;
	}

	public List<CitationReportDTO> getCitationStoregraphInfo(String store, String brandname) {
		Session session = sessionFactory.getCurrentSession();

		String sql = "FROM CitationReportEntity  WHERE brandName =? and store=? GROUP BY MONTH(dateSearched) ORDER BY dateSearched ASC";
		Query createQuery = session.createQuery(sql);
		createQuery.setString(0, brandname);
		createQuery.setString(1, store);
		List<CitationReportDTO> listDto = new ArrayList<CitationReportDTO>();
		List<CitationReportEntity> list = createQuery.list();
		for (CitationReportEntity accuracyEntity : list) {
			CitationReportDTO accuracyDTO = new CitationReportDTO();

			BeanUtils.copyProperties(accuracyEntity, accuracyDTO);

			Date date = accuracyDTO.getDateSearched();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			String format = new SimpleDateFormat("MMM").format(date);

			String formatted = String.format("%02d", month);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			logger.info(date);
			accuracyDTO.setMonthName(format);
			accuracyDTO.setDay(day);
			accuracyDTO.setMonth(Integer.valueOf(formatted));
			accuracyDTO.setYear(year);
			listDto.add(accuracyDTO);
		}

		return listDto;
	}

	public List<CitationStoreHistoryDTO> getCitationStoreHistoryInfo(String store, String brandname) {
		Session session = sessionFactory.getCurrentSession();

		String sql = "FROM CitationStoreHistoryEntity  WHERE brandName =? and store=? GROUP BY MONTH(date) ORDER BY date ASC";
		Query createQuery = session.createQuery(sql);
		createQuery.setString(0, brandname);
		createQuery.setString(1, store);
		List<CitationStoreHistoryDTO> listDto = new ArrayList<CitationStoreHistoryDTO>();
		List<CitationStoreHistoryEntity> list = createQuery.list();
		for (CitationStoreHistoryEntity accuracyEntity : list) {
			CitationStoreHistoryDTO accuracyDTO = new CitationStoreHistoryDTO();

			BeanUtils.copyProperties(accuracyEntity, accuracyDTO);

			Date date = accuracyDTO.getDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			String format = new SimpleDateFormat("MMM").format(date);

			String formatted = String.format("%02d", month);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			logger.info(date);
			accuracyDTO.setMonthName(format);
			accuracyDTO.setDay(day);
			accuracyDTO.setMonth(Integer.valueOf(formatted));
			accuracyDTO.setYear(year);
			listDto.add(accuracyDTO);
		}

		return listDto;
	}

	public List<CitationGraphDTO> getCitationBrandGraphInfo(String brandname) {
		Session session = sessionFactory.getCurrentSession();
		List<CitationGraphDTO> businessDTOs = new ArrayList<CitationGraphDTO>();
		String sql = "FROM CitationGraphEntity  WHERE brandName =?  GROUP BY MONTH(DATE) ORDER BY DATE ASC";
		Query createQuery = session.createQuery(sql);
		createQuery.setString(0, brandname);
		List<CitationGraphEntity> businessEntities = createQuery.list();
		for (CitationGraphEntity citationGraphEntity : businessEntities) {
			CitationGraphDTO citationGraphDTO = new CitationGraphDTO();
			BeanUtils.copyProperties(citationGraphEntity, citationGraphDTO);
			Date date = citationGraphDTO.getDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			String format = new SimpleDateFormat("MMM").format(date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			logger.info(date);
			citationGraphDTO.setDay(day);
			citationGraphDTO.setMonthName(format);
			citationGraphDTO.setMonth(month);
			citationGraphDTO.setYear(year);
			businessDTOs.add(citationGraphDTO);

		}

		return businessDTOs;

	}



}
