package com.business.model.dataaccess.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.DateUtil;
import com.business.common.util.LBLConstants;
import com.business.model.dataaccess.ReportDao;
import com.business.model.pojo.BrandEntity;
import com.business.model.pojo.ExportReportEntity;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ReportParams;
import com.business.model.pojo.SearchDomains;
import com.business.model.pojo.User_brands;
import com.business.model.pojo.UsersEntity;
import com.business.model.pojo.ValueObject;

/**
 * 
 * @author lbl_dev
 * 
 * 
 * 
 */

@Repository
public class ReportDaoImpl implements ReportDao {
	Logger logger = Logger.getLogger(ReportDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * getReports
	 */

	@Transactional
	public Set<ReportEntity> getReports() {
		Session session = sessionFactory.getCurrentSession();
		Query q = session
				.createQuery("from ReportEntity r left join fetch r.params Order By r.name");
		List<ReportEntity> reports = q.list();

		if (reports.isEmpty())
			return null;
		Set<ReportEntity> reportSet = new LinkedHashSet<ReportEntity>();
		for (ReportEntity reportEntity : reports) {
			List<ReportParams> params = reportEntity.getParams();
			List<ReportParams> paramsList = new ArrayList<ReportParams>();
			for (ReportParams reportParams : params) {
				if (reportParams == null) {
					continue;
				}
				paramsList.add(reportParams);
			}
			reportEntity.setParams(paramsList);
			reportSet.add(reportEntity);
		}
		// Set reportSet = new HashSet<ReportEntity>(reports);
		return reportSet;
	}

	/**
	 * getReports::get reports based on role
	 * 
	 * @param role
	 * @return
	 */
	@Transactional
	public List<ReportEntity> getReports(String role) {
		String sql = "";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from ReportEntity");
		List<ReportEntity> reports = q.list();

		if (reports.isEmpty())
			return null;

		return reports;
	}

	/**
	 * renewalReport:: get renewalReport based on params
	 */
	@Transactional
	public List<ValueObject> renewalReport(List<ReportParams> params,
			int offset, int numRecords, String sortField, String sortOrder,
			String userName, int roleId, Date fromDate, Date toDate)
			throws Exception {

		Session session = sessionFactory.getCurrentSession();

		Integer userId = getUserFromUserId(userName);

		String sqlStr = "select b.brandname as field1, b.locationsinvoiced as field2, date_format(b.startdate, '%m/%d/%Y') as field3,"
				+ " b.submisions as field4 from brands b ";
		boolean isSuperAdmin = true;
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			isSuperAdmin = false;
			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
				sqlStr += " where b.channelId= (select u.channelId from user_brands u where u.userid=?)";
			} else {
				List<String> allBrandNames = getAllBrandNames(false, userName);
				String brands = "";
				if (allBrandNames != null && allBrandNames.size() > 0) {
					for (int i = 0; i < allBrandNames.size(); i++) {
						if (brands.length() != 0) {
							brands = brands + ",'" + allBrandNames.get(i) + "'";
						} else {
							brands = "'" + allBrandNames.get(i) + "'";
						}
					}
				}
				if (allBrandNames.size() == 1) {
					sqlStr += " where b.brandId = " + brands + "";
				} else {
					sqlStr += " where b.brandId in (" + brands + ")";
				}

			}
		}
		StringBuffer sql = new StringBuffer(sqlStr);

		String brand = "";
		if (params != null) {
			for (ReportParams param : params) {
				if (param.getParamValue() != null
						&& param.getParamValue().toString().length() > 0) {
					sql.append(" AND b.brandname =?");
					brand = (String) param.getParamValue();
				}
			}
		}
		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append(" where b.startdate BETWEEN ? AND ?");
			} else {
				sql.append(" AND b.startdate BETWEEN ? AND ?");
			}
		sql.append(" order By b.startdate DESC");

		sql.append(" limit ").append(offset).append(", ").append(numRecords);
		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new StringType())
				.addScalar("field2", new IntegerType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));
		int brandIndex = 0;
		int dateIndex = 0;
		if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
			q.setInteger(0, userId);
			++brandIndex;
			++dateIndex;
		}
		if (brand != "") {
			q.setString(brandIndex, brand);
			++dateIndex;
		}
		if (fromDate != null && toDate != null) {
			q.setDate(dateIndex, fromDate);
			q.setDate(dateIndex + 1, toDate);
		}
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();
		reports.addAll(reportDataList1);
		if (reports.isEmpty())
			return null;
		List<ValueObject> rdList = new ArrayList<ValueObject>();
		for (ValueObject valueObject : reports) {
			String field3 = (String) valueObject.getField3();
			if (field3 == null) {
				continue;
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(field3));
			cal.add(Calendar.YEAR, 1);
			Date time = cal.getTime();
			Date date = DateUtil.getDate(time);
			valueObject.setField3(date);
			rdList.add(valueObject);
		}
		return reports;
	}

	/**
	 * getuploadReport::upload report info based on params
	 */
	@Transactional
	public List<ValueObject> uploadReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId) throws Exception {
		String sqlStr = "select SQL_CALC_FOUND_ROWS u.brand as field1, u.numberofrecords as field2, u.date as field3,u.username as field4"
				+ " from uploadinfo u";
		boolean isSuperAdmin = true;
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			sqlStr += " where u.username = ?";
			isSuperAdmin = false;
		}

		StringBuffer sql = new StringBuffer(sqlStr);

		Session session = sessionFactory.getCurrentSession();

		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append(" where u.date BETWEEN ? AND ?");
			} else {
				sql.append(" AND u.date BETWEEN ? AND ?");
			}
		sql.append(" order by u.date DESC");
		sql.append(" limit ").append(offset).append(", ").append(numRecords);

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new StringType())
				.addScalar("field2", new IntegerType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));
		int dateIndex = 0;
		if (!isSuperAdmin) {
			q.setString(0, userName);
			dateIndex = 1;
		}
		if (fromDate != null && toDate != null) {
			q.setDate(dateIndex, fromDate);
			q.setDate(dateIndex + 1, toDate);
		}
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();

		reports.addAll(reportDataList1);

		if (reports.isEmpty())
			return null;

		return reports;
	}

	/**
	 * get exportReport::export report info
	 */
	@Transactional
	public List<ValueObject> exportReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId) {
		Session session = sessionFactory.getCurrentSession();

		Integer userId = getUserFromUserId(userName);

		String sqlStr = "select SQL_CALC_FOUND_ROWS e.brandname as field1, e.numberofrecords as field2, e.partner as field3, date_format(e.date, '%m/%d/%Y') as field4,e.userName as field5 "
				+ " from exportinfo e where e.partner LIKE '%Template%' ";
		boolean isSuperAdmin = true;

		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			isSuperAdmin = false;
			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
				sqlStr += "AND e.channelname = (select c.channelname from channels c where "
						+ "c.channelid=(select u.channelid from user_brands u where u.userid=? LIMIT 1))";
			} else {
				List<String> allBrandNames = getAllBrandNames(false, userName);
				String brands = "";
				if (allBrandNames != null && allBrandNames.size() > 0) {
					for (int i = 0; i < allBrandNames.size(); i++) {
						if (brands.length() != 0) {
							brands = brands + ",'" + allBrandNames.get(i) + "'";
						} else {
							brands = "'" + allBrandNames.get(i) + "'";
						}
					}
				}
				if (allBrandNames.size() == 1) {
					sqlStr += "AND e.brandname = " + brands + "";
				} else {
					sqlStr += " AND e.brandname in (" + brands + ")";
				}

			}
		}
		StringBuffer sql = new StringBuffer(sqlStr);

		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append(" AND e.date BETWEEN ? AND ?");
			} else {
				sql.append(" AND e.date BETWEEN ? AND ?");
			}
		sql.append("order by e.date DESC");
		sql.append(" limit ").append(offset).append(", ").append(numRecords);

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new StringType())
				.addScalar("field2", new IntegerType())
				.addScalar("field3", new StringType())

				.addScalar("field4", new StringType())
				.addScalar("field5", new StringType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));
		int dateIndex = 0;
		if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
			q.setInteger(0, userId);
			dateIndex = 1;
		}
		if (fromDate != null && toDate != null) {
			q.setDate(dateIndex, fromDate);
			q.setDate(dateIndex + 1, toDate);
		}
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();

		reports.addAll(reportDataList1);

		if (reports.isEmpty())
			return null;

		return reports;
	}

	private Integer getUserFromUserId(String userName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UsersEntity.class);
		criteria.add(Restrictions.eq("userName", userName));
		List<UsersEntity> list = criteria.list();
		Integer userID = null;
		for (UsersEntity usersEntity : list) {
			userID = usersEntity.getUserID();
			logger.info("userID::::::::::::::::::::::::" + userID);
		}
		return userID;

	}

	/**
	 * get exportReport::export report info
	 */
	@Transactional
	public List<ValueObject> distributionReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId) {

		Session session = sessionFactory.getCurrentSession();
		Integer userId = getUserFromUserId(userName);

		String sqlStr = "select SQL_CALC_FOUND_ROWS e.brandname as field1, e.numberofrecords as field2, e.partner as field3 ,date_format(e.date, '%m/%d/%Y') as field4"
				+ " from exportinfo e WHERE e.partner NOT LIKE '%Template%'";
		boolean isSuperAdmin = true;
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			isSuperAdmin = false;
			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
				sqlStr += " AND e.channelname = (select c.channelname from channels c where "
						+ "c.channelid=(select u.channelid from user_brands u where u.userid=? LIMIT 1 ))";
			} else {
				List<String> allBrandNames = getAllBrandNames(false, userName);
				String brands = "";
				if (allBrandNames != null && allBrandNames.size() > 0) {
					for (int i = 0; i < allBrandNames.size(); i++) {
						if (brands.length() != 0) {
							brands = brands + ",'" + allBrandNames.get(i) + "'";
						} else {
							brands = "'" + allBrandNames.get(i) + "'";
						}
					}
				}
				if (allBrandNames.size() == 1) {
					sqlStr += " AND e.brandname = " + brands + "";
				} else if (!brands.isEmpty()) {
					sqlStr += "AND  e.brandname in (" + brands + ")";
				}
			}
		}

		StringBuffer sql = new StringBuffer(sqlStr);

		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append("AND e.date BETWEEN ? AND ?");
			} else {
				sql.append(" AND e.date BETWEEN ? AND ?");
			}
		sql.append("order by e.date DESC");

		sql.append(" limit ").append(offset).append(", ").append(numRecords);

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new StringType())
				.addScalar("field2", new IntegerType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));
		int dateIndex = 0;
		if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
			q.setInteger(0, userId);
			dateIndex = 1;
		}
		if (fromDate != null && toDate != null) {
			q.setDate(dateIndex, fromDate);
			q.setDate(dateIndex + 1, toDate);
		}
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();

		reports.addAll(reportDataList1);

		if (reports.isEmpty())
			return null;

		return reports;
	}

	/**
	 * getUploadReportDetails
	 */
	public List<LocalBusinessDTO> getUploadReportDetails(String brand, Date date) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LocalBusinessEntity.class);
		criteria.add(Restrictions.eq("client", brand));
		criteria.add(Restrictions.eq("uploadedTime", date));
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
	 * get changeTrackingReport :: changeTracking
	 */

	@Transactional
	public List<ValueObject> changeTrackingReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId) {
		Session session = sessionFactory.getCurrentSession();
		Integer userId = getUserFromUserId(userName);
		String sqlStr = "select SQL_CALC_FOUND_ROWS e.clientId as field1, e.store as field2, e.businessName as field3 ,e.locationAddress as field4,e.locationCity as field5,e.locationState as field6,e.locationZipCode as field7,e.locationPhone as field8,e.website as field9,e.hourseOfOperation as field10"
				+ " from changetracking e";
		boolean isSuperAdmin = true;
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			isSuperAdmin = false;
			if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
				sqlStr += " where e.channelname = (select c.channelname from channels c where "
						+ "c.channelid=(select u.channelid from user_brands u where u.userid=?))";
			} else {
				List<String> allBrandNames = getAllBrandNames(false, userName);
				String brands = "";
				if (allBrandNames != null && allBrandNames.size() > 0) {
					for (int i = 0; i < allBrandNames.size(); i++) {
						if (brands.length() != 0) {
							brands = brands + ",'" + allBrandNames.get(i) + "'";
						} else {
							brands = "'" + allBrandNames.get(i) + "'";
						}
					}
				}
				if (allBrandNames.size() == 1) {
					sqlStr += " where e.brandname = " + brands + "";
				} else {
					sqlStr += " where e.brandname in (" + brands + ")";
				}
			}
		}

		StringBuffer sql = new StringBuffer(sqlStr);

		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append(" where e.date BETWEEN ? AND ?");
			} else {
				sql.append(" AND e.date BETWEEN ? AND ?");
			}
		sql.append(" limit ").append(offset).append(", ").append(numRecords);

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new IntegerType())
				.addScalar("field2", new StringType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				.addScalar("field5", new StringType())
				.addScalar("field6", new StringType())
				.addScalar("field7", new StringType())
				.addScalar("field8", new StringType())
				.addScalar("field9", new StringType())
				.addScalar("field10", new StringType())

				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));
		int dateIndex = 0;
		if (roleId == LBLConstants.CHANNEL_ADMIN || roleId==LBLConstants.PURIST) {
			q.setInteger(0, userId);
			dateIndex = 1;
		}
		if (fromDate != null && toDate != null) {
			q.setDate(dateIndex, fromDate);
			q.setDate(dateIndex + 1, toDate);
		}
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();

		reports.addAll(reportDataList1);

		if (reports.isEmpty())
			return null;

		return reports;
	}

	@Transactional
	public List<ValueObject> renewalbrandReport(List<ReportParams> params,
			int offset, int numRecords, String sortField, String sortOrder,
			String userName, int roleId, Date fromDate, Date toDate,
			String brand) {

		String sqlStr = "select b.brandname as field1, b.locationsinvoiced as field2, date_format(b.startdate, '%m/%d/%Y') as field3,"
				+ " b.submisions as field4 from brands b ";
		boolean isSuperAdmin = true;
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			isSuperAdmin = false;

		}
		StringBuffer sql = new StringBuffer(sqlStr);

		Session session = sessionFactory.getCurrentSession();
		sql.append(" where b.brandname =?");

		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append(" AND b.startdate BETWEEN ? AND ?");
			} else {
				sql.append(" AND b.startdate BETWEEN ? AND ?");
			}
		sql.append("order By b.startdate DESC");
		sql.append(" limit ").append(offset).append(", ").append(numRecords);
		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new StringType())
				.addScalar("field2", new IntegerType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		if (brand != "") {
			q.setString(0, brand);
		}
		if (fromDate != null && toDate != null) {
			q.setDate(1, fromDate);
			q.setDate(2, toDate);
		}
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();
		reports.addAll(reportDataList1);
		if (reports.isEmpty())
			return null;
		List<ValueObject> rdList = new ArrayList<ValueObject>();
		for (ValueObject valueObject : reports) {
			String field3 = (String) valueObject.getField3();
			if (field3 == null) {
				continue;
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(field3));
			cal.add(Calendar.YEAR, 1);
			Date time = cal.getTime();
			Date date = DateUtil.getDate(time);
			valueObject.setField3(date);
			rdList.add(valueObject);
		}
		return reports;
	}

	@Transactional
	public List<ValueObject> distributionbrandReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId, String brand) {
		String sqlStr = "select SQL_CALC_FOUND_ROWS e.brandname as field1, e.numberofrecords as field2, e.partner as field3 ,date_format(e.date, '%m/%d/%Y') as field4"
				+ " from exportinfo e WHERE e.partner NOT LIKE '%Template%'";
		boolean isSuperAdmin = true;
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			isSuperAdmin = false;

		}
		StringBuffer sql = new StringBuffer(sqlStr);
		Session session = sessionFactory.getCurrentSession();
		sql.append(" AND  e.brandname =?");
		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append(" AND e.date BETWEEN ? AND ?");
			} else {
				sql.append(" AND e.date BETWEEN ? AND ?");
			}
		sql.append("order By e.date DESC");
		sql.append(" limit ").append(offset).append(", ").append(numRecords);

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new StringType())
				.addScalar("field2", new IntegerType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		if (brand != "") {
			q.setString(0, brand);
		}
		if (fromDate != null && toDate != null) {
			q.setDate(1, fromDate);
			q.setDate(2, toDate);
		}
		List<ValueObject> reports = q.list();
		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();

		reports.addAll(reportDataList1);

		if (reports.isEmpty())
			return null;

		return reports;
	}

	@Transactional
	public List<ValueObject> changeTrackingBrandReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId, String brand) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brand));
		Integer clientId = null;
		List<BrandEntity> list = criteria.list();
		for (BrandEntity brandEntity : list) {
			clientId = brandEntity.getClientId();
		}
		String sqlStr = "select SQL_CALC_FOUND_ROWS e.clientId as field1, e.store as field2, e.businessName as field3 ,e.locationAddress as field4,e.locationCity as field5,e.locationState as field6,e.locationZipCode as field7,e.locationPhone as field8,e.website as field9,e.hourseOfOperation as field10"
				+ " from changetracking e";
		boolean isSuperAdmin = true;
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			isSuperAdmin = false;

		}
		StringBuffer sql = new StringBuffer(sqlStr);

		sql.append(" where e.clientId =?");
		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append(" AND e.date BETWEEN ? AND ?");
			} else {
				sql.append(" AND e.date BETWEEN ? AND ?");
			}
		sql.append(" limit ").append(offset).append(", ").append(numRecords);

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new IntegerType())
				.addScalar("field2", new StringType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				.addScalar("field5", new StringType())
				.addScalar("field6", new StringType())
				.addScalar("field7", new StringType())
				.addScalar("field8", new StringType())
				.addScalar("field9", new StringType())
				.addScalar("field10", new StringType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		if (brand != "") {
			q.setInteger(0, clientId);

		}
		if (fromDate != null && toDate != null) {
			q.setDate(1, fromDate);
			q.setDate(2, toDate);
		}
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();

		reports.addAll(reportDataList1);

		if (reports.isEmpty())
			return null;

		return reports;
	}

	public List<ExportReportDTO> getDistrbutionReportDetails(String brand,
			String date) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ExportReportEntity.class);
		criteria.add(Restrictions.eq("brandName", brand));
		// criteria.add(Restrictions.eq("partner", date));
		List<ExportReportEntity> businessEntities = criteria.list();
		List<ExportReportDTO> businessDTOs = new ArrayList<ExportReportDTO>();
		Integer clientID = null;
		for (ExportReportEntity exportReportEntity : businessEntities) {
			ExportReportDTO exportReportDTO = new ExportReportDTO();
			String brandName = exportReportEntity.getBrandName();
			Criteria createCriteria = session.createCriteria(BrandEntity.class);
			createCriteria.add(Restrictions.eq("brandName", brandName));
			List<BrandEntity> list2 = createCriteria.list();
			for (BrandEntity brandEntity : list2) {
				clientID = brandEntity.getClientId();
			}
			exportReportDTO.setClientId(clientID);
			BeanUtils.copyProperties(exportReportEntity, exportReportDTO);
			businessDTOs.add(exportReportDTO);
		}
		return businessDTOs;
	}

	@Transactional
	public List<String> getStoreForBrand(String categoryId) {

		Session session = sessionFactory.getCurrentSession();
		List<String> storelist = new ArrayList<String>();

		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", categoryId));
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

	@Transactional
	public List<ValueObject> changeTrackingBrandListReport(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, Date fromDate, Date toDate,
			int roleId, String brand, List<String> listofStores) {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brand));
		Integer clientId = null;
		List<BrandEntity> list = criteria.list();
		for (BrandEntity brandEntity : list) {
			clientId = brandEntity.getClientId();
		}
		String sqlStr = "select SQL_CALC_FOUND_ROWS e.clientId as field1, e.store as field2, e.businessName as field3 ,e.locationAddress as field4,e.locationCity as field5,e.locationState as field6,e.locationZipCode as field7,e.locationPhone as field8,e.website as field9,e.hourseOfOperation as field10"
				+ " from changetracking e";
		boolean isSuperAdmin = true;
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			isSuperAdmin = false;

		}
		StringBuffer sql = new StringBuffer(sqlStr);
		StringBuffer stores = new StringBuffer();
		for (String store : listofStores) {
			// System.out.println("Stores: "+ store);

			if (stores.length() != 0) {
				stores = stores.append(",").append("'").append(store)
						.append("'");
			} else {
				stores = stores.append("'").append(store).append("'");
			}
		}

		// System.out.println("Stores: "+ stores.toString());

		if (listofStores.size() > 0) {
			sql.append(" where e.store in (" + stores.toString() + ")");
		}
		/*
		 * if(listofStores.size()== 1){ sql.append(" where e.store ="+
		 * stores.toString()+""); }
		 */

		sql.append(" AND  e.clientId =?");
		if (fromDate != null && toDate != null) {
			if (isSuperAdmin) {
				sql.append(" AND e.date BETWEEN ? AND ?");
			} else {
				sql.append(" AND e.date BETWEEN ? AND ?");
			}
		}

		// sql.append(" limit ").append(offset).append(", ").append(numRecords);

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new IntegerType())
				.addScalar("field2", new StringType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				.addScalar("field5", new StringType())
				.addScalar("field6", new StringType())
				.addScalar("field7", new StringType())
				.addScalar("field8", new StringType())
				.addScalar("field9", new StringType())
				.addScalar("field10", new StringType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		if (brand != "") {
			q.setInteger(0, clientId);

		}
		if (fromDate != null && toDate != null) {
			q.setDate(1, fromDate);
			q.setDate(2, toDate);
		}
		// System.out.println("total query ::"+q);
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		logger.info("sql::::::" + sql);
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();

		reports.addAll(reportDataList1);

		if (reports.isEmpty())
			return null;

		return reports;
	}

	public List<ValueObject> checkReportlisting(String userName,
			List<ReportParams> params, int offset, int numRecords,
			String sortField, String sortOrder, String brand, int roleId,
			String listofStores) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BrandEntity.class);
		criteria.add(Restrictions.eq("brandName", brand));
		Integer clientId = null;
		List<BrandEntity> list = criteria.list();
		for (BrandEntity brandEntity : list) {
			clientId = brandEntity.getClientId();
		}
		String sqlStr = "select SQL_CALC_FOUND_ROWS e.clientId as field1, e.store as field2, e.businessName as field3 ,e.locationAddress as field4,e.locationCity as field5,e.locationState as field6,e.locationZipCode as field7,e.locationPhone as field8,e.website as field9,e.hourseOfOperation as field10"
				+ " from changetracking e";
		boolean isSuperAdmin = true;
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			isSuperAdmin = false;

		}
		StringBuffer sql = new StringBuffer(sqlStr);

		// sql.append(" limit ").append(offset).append(", ").append(numRecords);

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new IntegerType())
				.addScalar("field2", new StringType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				.addScalar("field5", new StringType())
				.addScalar("field6", new StringType())
				.addScalar("field7", new StringType())
				.addScalar("field8", new StringType())
				.addScalar("field9", new StringType())
				.addScalar("field10", new StringType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		if (brand != "") {
			q.setInteger(0, clientId);

		}

		// System.out.println("total query ::"+q);
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();

		reports.addAll(reportDataList1);

		if (reports.isEmpty())
			return null;

		return reports;
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

	public List<ValueObject> runRenewalReport(Date fromDate, Date toDate,
			String storeName, String brand) {
		Integer clinetId = getClinetIdIdByName(brand);
		String sqlStr = "select r.store as field1,date_format(r.acxiomRenewalDate, '%m/%d/%Y') as field2,date_format(r.infogroupRenewalDate, '%m/%d/%Y') as field3,"
				+ "date_format(r.factualRenewalDate, '%m/%d/%Y') as field4,date_format(r.localezeRenewalDate, '%m/%d/%Y') as field5 from renewal_report r where store= ? and clientId=?";
		boolean isSuperAdmin = true;
		

		StringBuffer sql = new StringBuffer(sqlStr);
		Date startDate=new Date();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(startDate);
		
		calendar.add(Calendar.DATE, 61);
		Date endDate = calendar.getTime();
		
		sql.append(" AND r.renewalDate BETWEEN ? AND ?");
		Session session = sessionFactory.getCurrentSession();

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new StringType())
				.addScalar("field2", new StringType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				.addScalar("field5", new StringType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));
		q.setString(0, storeName);
		q.setInteger(1, clinetId);
		q.setDate(2, startDate);
		q.setDate(3, endDate);
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();
		reports.addAll(reportDataList1);
		if (reports.isEmpty())
			return null;
		/*
		 * List<ValueObject> rdList = new ArrayList<ValueObject>(); for
		 * (ValueObject valueObject : reports) { String field3 = (String)
		 * valueObject.getField3(); if (field3 == null) { continue; }
		 * 
		 * rdList.add(valueObject); }
		 */
		return reports;

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
				}
				else if (path.contains("yelp") && yelpCount == 0) {

					count = count + pathsCount;
				}
				else if (path.contains("superpages") && superpagesCount == 0) {

					count = count + pathsCount;
				}
				else if (path.contains("yellowbook") && yellobookCount == 0) {

					count = count + pathsCount;
				}
				else if (path.contains("bing") && bingCount == 0) {

					count = count + pathsCount;
				}
				else if (path.contains("yp") && ypCount == 0) {
					count = count + pathsCount;
				}
				else if (path.contains("yahoo") && YahooCount == 0) {

					count = count + pathsCount;
				}
				else if (path.contains("whitepages") && whitepagesCount == 0) {

					count = count + pathsCount;
				}
				else if (path.contains("citysearch") && citysearchCount == 0) {

					count = count + pathsCount;

				}
				else if (path.contains("mapquest") && mapquestCount == 0) {

					count = count + pathsCount;
				}

			}
			map.put(store, count);
		}
		return map;
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
	
	public List<ValueObject> runRenewalReportForBrand(Date fromDate,
			Date toDate, String brand) {
			Integer clinetId = getClinetIdIdByName(brand);
		String sqlStr = "select r.store as field1,date_format(r.acxiomRenewalDate, '%m/%d/%Y') as field2,date_format(r.infogroupRenewalDate, '%m/%d/%Y') as field3,"
				+ "date_format(r.factualRenewalDate, '%m/%d/%Y') as field4,date_format(r.localezeRenewalDate, '%m/%d/%Y') as field5 from renewal_report r where clientId= ?";
		boolean isSuperAdmin = true;
		StringBuffer sql = new StringBuffer(sqlStr);
		Date startDate=new Date();
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(startDate);
		logger.info("startDate::::::::::::"+startDate);
		calendar.add(Calendar.DATE, 61);
		Date endDate = calendar.getTime();
		logger.info("endDate::::::::::::"+endDate);
	
		sql.append(" AND r.renewalDate BETWEEN ? AND ?");
		Session session = sessionFactory.getCurrentSession();

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new StringType())
				.addScalar("field2", new StringType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				.addScalar("field5", new StringType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));
		q.setInteger(0, clinetId);
		
			q.setDate(1, startDate);
			q.setDate(2, endDate);
		
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();
		reports.addAll(reportDataList1);
		if (reports.isEmpty())
			return null;
	
		return reports;

	}

	//@Override
	public List<ValueObject> runOvveridenReportForBrand(Date fromDate,
			Date toDate, String brand) {
		Integer clinetId = getClinetIdIdByName(brand);
		String sqlStr = "select l.brandName as field1,l.store as field2,l.date as field3, userName as field4 from lbl_audit l where clientId= ?";
		boolean isSuperAdmin = true;
		
		StringBuffer sql = new StringBuffer(sqlStr);
		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append(" AND l.date BETWEEN ? AND ?");
			} else {
				sql.append(" AND l.date BETWEEN ? AND ?");
			}
		Session session = sessionFactory.getCurrentSession();

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new StringType())
				.addScalar("field2", new StringType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));
		q.setInteger(0, clinetId);
		
		if (fromDate != null && toDate != null) {
			q.setDate(1, fromDate);
			q.setDate(2, toDate);
		}
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();
		reports.addAll(reportDataList1);
		if (reports.isEmpty())
			return null;
	
		return reports;
	}


	public List<ValueObject> runOvveridenBrandsReport(Date fromDate, Date toDate) {
		
		String sqlStr = "select l.brandName as field1,l.store as field2,l.date as field3, userName as field4 from lbl_audit l";
		boolean isSuperAdmin = true;
		
		StringBuffer sql = new StringBuffer(sqlStr);
		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append(" WHERE l.date BETWEEN ? AND ?");
			} else {
				sql.append(" AND l.date BETWEEN ? AND ?");
			}
		Session session = sessionFactory.getCurrentSession();

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new StringType())
				.addScalar("field2", new StringType())
				.addScalar("field3", new StringType())
				.addScalar("field4", new StringType())
				
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));
		
		
		if (fromDate != null && toDate != null) {
			q.setDate(0, fromDate);
			q.setDate(1, toDate);
		}
		List<ValueObject> reports = q.list();

		String sql1 = "select found_rows() field1";
		q = session
				.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())
				.setResultTransformer(
						Transformers.aliasToBean(ValueObject.class));

		List<ValueObject> reportDataList1 = q.list();
		reports.addAll(reportDataList1);
		if (reports.isEmpty())
			return null;
	
		return reports;
	}
	
	

}
