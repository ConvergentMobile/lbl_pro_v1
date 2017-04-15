package com.business.web.controller;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.transaction.annotation.Transactional;

import com.business.model.pojo.ValueObject;
import com.whitespark.ws.HibernateUtil;


public class CitationsDS {
	//ApplicationContext context = new ClassPathXmlApplicationContext("classpath:ontyme-servlet-test-dev.xml");    

	/*private ApptService apptService = (ApptService) context.getBean("apptService");
	private SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
	*/
	public static List<ValueObject> getData() throws Exception {
		String storeId = "000900128";
		return new CitationsDS().getListing(storeId);	
	}
	
	public static List<ValueObject> getDS(String brandName) throws Exception {
		return new CitationsDS().getChartDS(brandName);
	}
	Session session = HibernateUtil.getSessionFactory()
			.openSession();

	Transaction beginTransaction = session
			.beginTransaction();
	
	@Transactional
	public List<ValueObject> getListing(String storeId) throws Exception {
		
		String sql = "select store field1, businesName field2, address field3, city field4, state field5, zip field6, phone field7,"
					+ " pathCount field8"
					+ " from citation_report"
					+ " where brandId = ?"; 
	
		try {
		
						
			Query q = session.createSQLQuery(sql)
					.addScalar("field1", new StringType())
					.addScalar("field2", new StringType())										
					.addScalar("field3", new StringType())
					.addScalar("field4", new StringType())
					.addScalar("field5", new StringType())
					.addScalar("field6", new StringType())
					.addScalar("field7", new StringType())
					.addScalar("field8", new IntegerType())
					.setResultTransformer(Transformers.aliasToBean(ValueObject.class));	
			
			List<ValueObject> ret = q.setInteger(0, 3351)
									  .list();
			
			if (ret.isEmpty())
				return null;
			
			return ret;	
		} finally {
		}
	}
	
	@Transactional
	public List<ValueObject> getChartDS(String brandName) throws Exception {		
		String sql = "select citationCount field10, date field11, '' field12"
				+ " from citation_graph "
				+ " where brandName = ?";
	
		try {
			
						
			Query q = session.createSQLQuery(sql)
					.addScalar("field10", new IntegerType())
					.addScalar("field11", new TimestampType())		
					.addScalar("field12", new StringType())	
					.setResultTransformer(Transformers.aliasToBean(ValueObject.class));	
			
			List<ValueObject> ret = q.setString(0, brandName)
									  .list();
			
			if (ret.isEmpty())
				return null;
			
			return ret;	
		} finally {
		}
	}
}
