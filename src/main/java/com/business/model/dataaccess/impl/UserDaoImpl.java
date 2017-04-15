package com.business.model.dataaccess.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.UsersDTO;
import com.business.common.util.DateUtil;
import com.business.common.util.LBLConstants;
import com.business.model.dataaccess.UserDao;
import com.business.model.pojo.BrandEntity;
import com.business.model.pojo.ChangeTrackingEntity;
import com.business.model.pojo.ChannelEntity;
import com.business.model.pojo.ExportReportEntity;
import com.business.model.pojo.LblErrorEntity;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.model.pojo.UploadReportEntity;
import com.business.model.pojo.UserStoresEntity;
import com.business.model.pojo.User_brands;
import com.business.model.pojo.UsersEntity;
import com.business.web.bean.UsersBean;

@Repository
public class UserDaoImpl implements UserDao {
	Logger logger = Logger.getLogger(BusinessDaoImpl.class);
	@Autowired
	private HttpSession httpSession;

	@Autowired
	private SessionFactory sessionFactory;

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

	public boolean isUserExistis(String userName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UsersEntity.class);
		criteria.add(Restrictions.eq("userName", userName));
		List list = criteria.list();

		return list.size() > 0;
	}

	@SuppressWarnings("unused")
	public boolean saveUser(UsersBean bean) {
		Session session = sessionFactory.getCurrentSession();
		Integer channelId = 0;
		if (bean.getRoleId() != LBLConstants.USER) {
			if (bean.getRoleId() == LBLConstants.CLIENT_ADMIN) {
				// channelId = getChannelIdByBrandId(bean.getBrandID());
			} else {
				String channelName = bean.getChannelName();
				System.out.println("channelName:::::::::::::" + channelName);
				if (channelName.length() > 0) {
					Criteria criteria = session
							.createCriteria(ChannelEntity.class);
					criteria.add(Restrictions.eq("channelName", channelName));
					List<ChannelEntity> list = criteria.list();
					for (ChannelEntity channelEntity : list) {
						channelId = channelEntity.getChannelID();
					}
				}
			}
		}
		UsersEntity entity = new UsersEntity();

		BeanUtils.copyProperties(bean, entity);
		if (bean.getUserID() == null) {
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

	public LocalBusinessDTO getBusinessInfo(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		LocalBusinessEntity bean = (LocalBusinessEntity) session.load(
				LocalBusinessEntity.class, id);
		LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();
		BeanUtils.copyProperties(bean, localBusinessDTO);
		return localBusinessDTO;
	}

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

	public boolean updateBusinessInfo(LocalBusinessDTO businessDTO) {
		Session session = sessionFactory.getCurrentSession();

		LocalBusinessEntity bean = (LocalBusinessEntity) session.get(
				LocalBusinessEntity.class, businessDTO.getId());
		BeanUtils.copyProperties(businessDTO, bean);

		session.update(bean);

		return true;
	}

	public boolean saveChangeTrackingInfo(ChangeTrackingEntity entity) {
		Session session = sessionFactory.getCurrentSession();

		session.save(entity);

		return true;
	}

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

	public LblErrorDTO getErrorBusinessInfo(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		LblErrorEntity bean = (LblErrorEntity) session.load(
				LblErrorEntity.class, id);
		LblErrorDTO lblErrorDTO = new LblErrorDTO();
		BeanUtils.copyProperties(bean, lblErrorDTO);
		return lblErrorDTO;
	}

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

	public void updateErrorBusinessInfo(LblErrorDTO businessInfoDto,
			Integer listingId) {
		Session session = sessionFactory.getCurrentSession();
		LocalBusinessEntity businessEntity = new LocalBusinessEntity();
		BeanUtils.copyProperties(businessInfoDto, businessEntity);

		LblErrorEntity errorEntity = new LblErrorEntity();
		BeanUtils.copyProperties(businessInfoDto, errorEntity);
		session.delete(errorEntity);

	}

	public boolean saveErrorBusinessInfo(LblErrorDTO businessInfoDto) {
		Session session = sessionFactory.getCurrentSession();
		LocalBusinessEntity businessEntity = new LocalBusinessEntity();
		BeanUtils.copyProperties(businessInfoDto, businessEntity);
		session.save(businessEntity);

		return true;
	}

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

	public List<LocalBusinessDTO> getWhereCoditionRecords(String query,
			Session session) {
		List<LocalBusinessDTO> nameList = new ArrayList<LocalBusinessDTO>();

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			List<String> allBrandNames = new ArrayList<String>();
			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
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

				if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
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
				if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
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

	public List<LocalBusinessDTO> getListOfBusinessInfo() {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LocalBusinessDTO> businessDTOs = new ArrayList<LocalBusinessDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LocalBusinessEntity";
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		List<String> allBrandNames = new ArrayList<String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
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

	public List<String> getAllBrandNames() {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT DISTINCT brandName FROM BrandEntity";
		Query createQuery = session.createQuery(sql);
		List<String> brandNames = createQuery.list();
		return brandNames;
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

	public List<LblErrorDTO> getErrorWhereCoditionRecords(String query,
			Session session) {
		List<LblErrorDTO> nameList = new ArrayList<LblErrorDTO>();
		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			List<String> allBrandNames = new ArrayList<String>();
			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
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

				if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
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
				if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
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

	public List<LblErrorDTO> getListOfErrors() {

		int roleId = (Integer) httpSession.getAttribute("roleId");
		String userName = (String) httpSession.getAttribute("userName");
		List<LblErrorDTO> businessDTOs = new ArrayList<LblErrorDTO>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM LblErrorEntity";
		List<String> allBrandNames = new ArrayList<String>();
		Map<Integer, String> brandsMap = new HashMap<Integer, String>();
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {

			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
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
		createCriteria.list();

		for (UserStoresEntity userStoresEntity : list) {
			String store = userStoresEntity.getStore();
			listofstores.add(store);
		}
		return listofstores;
	}

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
		} else if (((role == LBLConstants.CHANNEL_ADMIN || role==LBLConstants.PURIST)) && userName != null) {

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

		} else if ((role == LBLConstants.CHANNEL_ADMIN || role==LBLConstants.PURIST) && userName != null) {
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
			/*String activityDesc = brandName + " is exported to "
					+ exportReportEntity.getPartner();*/
			String partner = exportReportEntity.getPartner();
			// String activityDesc = "Exported to " +
			// exportReportEntity.getPartner();
			String activityDesc = null;
			if (partner.contains("Template")) {
				String[] tempalteName = partner.split("Template");
				//partner = tempalteName[0] + " Template";
				String temp = tempalteName[0];
				if(temp.contains("AcxiomCan")) {
					temp = "Acxiom Canada";
				}
				else if(temp.contains("AcxiomUS")) {
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
		} else if ((role == LBLConstants.CHANNEL_ADMIN || role==LBLConstants.PURIST) && userName != null) {

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

		} else if ((role == LBLConstants.CHANNEL_ADMIN || role==LBLConstants.PURIST) && userName != null) {

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
			/*String activityDesc = brand + " is exported to "
					+ exportReportEntity.getPartner();*/
			String partner = exportReportEntity.getPartner();
			// String activityDesc = "Exported to " +
			// exportReportEntity.getPartner();
			String activityDesc = null;
			if (partner.contains("Template")) {
				String[] tempalteName = partner.split("Template");
				//partner = tempalteName[0] + " Template";
				String temp = tempalteName[0];
				if(temp.contains("AcxiomCan")) {
					temp = "Acxiom Canada";
				}
				else if(temp.contains("AcxiomUS")) {
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

	public void deleteUSer(int parseInt) {
		Integer role = (Integer) httpSession.getAttribute("roleId");
		UsersEntity entity = (UsersEntity) sessionFactory.getCurrentSession()
				.load(UsersEntity.class, parseInt);
		if (entity != null) {
			sessionFactory.getCurrentSession().delete(entity);

		}
		if(role!=LBLConstants.USER){
			deleteUserFromUserBrands(parseInt);
		}else{
			deletuserFromUserStores(parseInt);
		}
		
		
	}

	private void deletuserFromUserStores(int parseInt) {
		Session session = sessionFactory.getCurrentSession();
		String sql="Delete From UserStoresEntity where userID ="+parseInt;
		Query createQuery = session.createQuery(sql);
		createQuery.executeUpdate();
	}

	private void deleteUserFromUserBrands(int parseInt) {
		Session session = sessionFactory.getCurrentSession();
	String sql="Delete From User_brands where userID ="+parseInt;
	Query createQuery = session.createQuery(sql);
	createQuery.executeUpdate();
	
		
	}


	public List<String> getChannelsBasedOnUser(int userID) {
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

}
