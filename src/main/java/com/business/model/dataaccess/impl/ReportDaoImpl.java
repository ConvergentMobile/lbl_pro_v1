package com.business.model.dataaccess.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.DateUtil;
import com.business.common.util.LBLConstants;
import com.business.model.dataaccess.ReportDao;
import com.business.model.pojo.BrandEntity;
import com.business.model.pojo.LocalBusinessEntity;
import com.business.model.pojo.ReportEntity;
import com.business.model.pojo.ReportParams;
import com.business.model.pojo.User_brands;
import com.business.model.pojo.UsersEntity;
import com.business.model.pojo.ValueObject;

/**
 * 
 * @author Vasanth
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
		logger.info("Start: getReports");
		Session session = sessionFactory.getCurrentSession();
		Query q = session
				.createQuery("from ReportEntity r left join fetch r.params");
		List<ReportEntity> reports = q.list();

		if (reports.isEmpty())
			return null;
		Set<ReportEntity> reportSet = new HashSet<ReportEntity>();
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
		logger.info("End: getReports");
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
		logger.info("Start: renewalReport");
		String sqlStr = "select b.brandname as field1, b.locationsinvoiced as field2, date_format(b.startdate, '%m/%d/%Y') as field3,"
				+ " b.submisions as field4 from brands b";
		boolean isSuperAdmin = true;
		boolean isClient=false;
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			isSuperAdmin = false;
			//if (roleId == LBLConstants.CHANNEL_ADMIN) {
				//sqlStr += " where b.channelId= (select u.channelId from user_brands u where u.username=?)";
			//}else {

				//sqlStr += " where b.brandId= (select u.brandId from users u where u.username=?)";
				isClient=true;
				List<String> allBrandNames = getAllBrandNames(false, userName);
				String brands="";
				if(allBrandNames!=null&&allBrandNames.size()>0){
					for (int i = 0; i < allBrandNames.size(); i++) {
						if(brands.length()!=0){
							brands = brands +",'"+allBrandNames.get(i) +"'";
						}
						else {
							brands = "'"+allBrandNames.get(i) +"'";
						}
					}		
				}
				if(allBrandNames.size()==1){
					sqlStr += " where b.brandId = "+brands+"";
				}else {
					sqlStr += " where b.brandId in ("+brands+")";
				}
				
			}
		//}
		
		logger.info("Sql in renewalReport" + sqlStr);
		StringBuffer sql = new StringBuffer(sqlStr);
		
		Session session = sessionFactory.getCurrentSession();
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
		/*if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)&&!isClient) {
			q.setString(0, userName);
			++brandIndex;
			++dateIndex;
		}*/
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
		logger.info("End: renewalReport");
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
		
		logger.info("Start: uploadReport");
		String sqlStr = "select SQL_CALC_FOUND_ROWS u.brand as field1, u.numberofrecords as field2, u.date as field3"
				+ " from uploadinfo u";
		boolean isSuperAdmin = true;
		if (!(roleId == LBLConstants.CONVERGENT_MOBILE_ADMIN)) {
			sqlStr += " where u.username = ?";
			isSuperAdmin = false;
		}

		StringBuffer sql = new StringBuffer(sqlStr);

		Session session = sessionFactory.getCurrentSession();

		/*
		 * if (params != null) { for (ReportParams param : params) { if
		 * (param.getParamValue() != null &&
		 * param.getParamValue().toString().length() > 0)
		 * sql.append(" and ").append
		 * (param.getParamName()).append(param.getCondition
		 * ()).append(param.getParamValue()); } }
		 */
		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append(" where u.date BETWEEN ? AND ?");
			} else {
				sql.append(" AND u.date BETWEEN ? AND ?");
			}

		sql.append(" limit ").append(offset).append(", ").append(numRecords);

		Query q = session
				.createSQLQuery(sql.toString())
				.addScalar("field1", new StringType())
				.addScalar("field2", new IntegerType())
				.addScalar("field3", new StringType())
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
		logger.info("End: uploadReport");
		return reports;
	}

	/**
	 * get exportReport::export report info
	 */
	@Transactional
	public List<ValueObject> exportReport(String userName, List<ReportParams> params, int offset, 
								int numRecords, String sortField, String sortOrder,Date fromDate, Date toDate,int roleId)  {
	
		logger.info("Start: exportReport");
		String sqlStr = "select SQL_CALC_FOUND_ROWS e.brandname as field1, e.numberofrecords as field2, date_format(e.date, '%m/%d/%Y') as field3"
				+ " from exportinfo e";
		boolean isSuperAdmin = true;
		boolean isClient=false;
		if (!(roleId==LBLConstants.CONVERGENT_MOBILE_ADMIN)){
			isSuperAdmin = false;
			//if (roleId == LBLConstants.CHANNEL_ADMIN) {
				//sqlStr += " where e.channelname = (select c.channelname from channels c where " +
				//"c.channelid=(select u.channelid from user_brands u where u.username=?))";
			//}else {
				/*sqlStr += " where e.brandname=(select b.brandname from brands b where" +
						" b.brandId= (select u.brandid from users u where u.username=?))";*/
				isClient=true;
				List<String> allBrandNames = getAllBrandNames(false, userName);
				String brands="";
				if(allBrandNames!=null&&allBrandNames.size()>0){
					for (int i = 0; i < allBrandNames.size(); i++) {
						if(brands.length()!=0){
							brands = brands +",'"+allBrandNames.get(i) +"'";
						}
						else {
							brands = "'"+allBrandNames.get(i) +"'";
						}
					}		
				}
				if(allBrandNames.size()==1){
					sqlStr += " where e.brandname = "+brands+"";
				}else {
					sqlStr += " where e.brandname in ("+brands+")";
				}
				
			}
		//}
		
		logger.info("Sql in exportReport is: "+ sqlStr);
		
		StringBuffer sql = new StringBuffer(sqlStr);
		
		Session session = sessionFactory.getCurrentSession();
		
		/*if (params != null) {
	        for (ReportParams param : params) {
	        	if (param.getParamValue() != null && param.getParamValue().toString().length() > 0)
	        		sql.append(" and ").append(param.getParamName()).append(param.getCondition()).append(param.getParamValue());      	
	        }
		}*/
		if (fromDate != null && toDate != null)
			if (isSuperAdmin) {
				sql.append(" where e.date BETWEEN ? AND ?");
			}else {
				sql.append(" AND e.date BETWEEN ? AND ?");
			}
		sql.append(" limit ").append(offset).append(", ").append(numRecords);

        Query q = session.createSQLQuery(sql.toString())
        		.addScalar("field1", new StringType())
				.addScalar("field2", new IntegerType())
				.addScalar("field3", new StringType())
				.setResultTransformer(Transformers.aliasToBean(ValueObject.class));        		
        int dateIndex = 0;
      /* if (!isSuperAdmin&&!isClient) {
        	q.setString(0, userName);
        	dateIndex = 1;
		}*/
        if (fromDate != null && toDate != null){
        	q.setDate(dateIndex, fromDate);
        	q.setDate(dateIndex+1, toDate);
        }
		List<ValueObject> reports = q.list();
		
		String sql1 = "select found_rows() field1";
		q = session.createSQLQuery(sql1)
				.addScalar("field1", new IntegerType())														
				.setResultTransformer(Transformers.aliasToBean(ValueObject.class));			

		List<ValueObject> reportDataList1 = q.list();

		reports.addAll(reportDataList1);		
		
		if (reports.isEmpty())
			return null;
		logger.info("End: exportReport");
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

}
