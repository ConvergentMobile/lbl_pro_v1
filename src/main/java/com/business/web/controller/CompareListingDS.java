package com.business.web.controller;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.transaction.annotation.Transactional;

import com.business.model.pojo.ValueObject;
import com.whitespark.ws.HibernateUtil;



public class CompareListingDS {
	//ApplicationContext context = new ClassPathXmlApplicationContext("file:./test/classes/ontyme-servlet-test-dev.xml");    
	//ApplicationContext context = new ClassPathXmlApplicationContext("classpath:ontyme-servlet-test-dev.xml");    

	//private ApptService apptService = (ApptService) context.getBean("apptService");
	//private SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
	
	public static List<ValueObject> getData(String storeId, String directory) throws Exception {
		return new CompareListingDS().getListing(storeId, directory);	
	}
	
	public static List<ValueObject> getDS(String storeId, String directory) throws Exception {
		return new CompareListingDS().getChartDS(storeId, directory);
	}
	

	
	@Transactional
	public List<ValueObject> getListing(String storeId, String directory) throws Exception {
		Session session = HibernateUtil.getSessionFactory()
				.openSession();

		Transaction beginTransaction = session
				.beginTransaction();
		String sql = "select b.companyName field1, b.locationAddress field2, b.locationCity field3, b.locationState field4,"
				+ " b.locationZipcode field5, b.locationPhone field6, b.webAddress field7, '' field8,"
				+ " cr.businessname field9, cr.address field10, cr.city field11, cr.state field12, cr.zip field13,  cr.phone field14,"
				+ " cr.website field15, cr.directory field16, null field17, 'black' field21, 'black' field22, 'black' field23, 'black' field24, 'black' field25 ,'black' field26,'black' field27"
				+ " from checkreport cr, business b"
				+ " where b.store = ?"
				+ " and b.store = cr.store"
				+ " and cr.directory = ?";
			
		try {
			
						
			Query q = session.createSQLQuery(sql)
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
					.setResultTransformer(Transformers.aliasToBean(ValueObject.class));	
			
			List<ValueObject> ret = q.setString(0, storeId)
									  .setString(1, storeId)
									  .setString(1, directory)
									  .list();
			System.out.println(ret);
			
			beginTransaction.commit();
			
			if (ret.isEmpty())
				return null;
			ret = this.compareIt(ret);
			return ret;	
		} finally {
		}
	}
public List<ValueObject> compareIt(List<ValueObject> volist) throws Exception {

		
		String companyName = volist.get(0).getField1().toString();
		String locationAddress = volist.get(0).getField2().toString();
		String locationCity = volist.get(0).getField3().toString();
		
		String locationState = volist.get(0).getField4().toString();
		String locationPhone = volist.get(0).getField6().toString();
		String locationZipCode = volist.get(0).getField5().toString();
		String webAddress = volist.get(0).getField7().toString();
		String businessColourCode="B";
		String cityColourCode="B";
		String stateColourCode="B";
		String zipColourCode="B";
		String phoneColourCode="B";
		String websiteColourCode="B";

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
				businessColourCode="R";
				volist.get(i).setField22(businessColourCode);
				System.out.println("getting");
			}
			if (!city.equalsIgnoreCase(locationCity)) {
				count++;
				cityColourCode="R";
				volist.get(i).setField23(cityColourCode);
			}
			if (!zip.equalsIgnoreCase(locationZipCode)) {
				count++;
				zipColourCode="R";
				volist.get(i).setField24(zipColourCode);
			}
			if (!state.equalsIgnoreCase(locationState)) {
				count++;
				stateColourCode="R";
				volist.get(i).setField25(stateColourCode);
			}
			if (!storeUrl.equalsIgnoreCase(webAddress)) {
				count++;
				websiteColourCode="R";
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
				phoneColourCode="R";
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
					//boolean isAbbreviationExist = businessservice
					//		.isAbbreviationExist(lastWordinDirectoryddress,
					//				lastWordinLocationAddress);

					boolean isAbbreviationExist = true; //this is just for now - uncomment above lines
					
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
	@Transactional
	public List<ValueObject> getChartDS(String storeId, String directory) throws Exception {
		Session session = HibernateUtil.getSessionFactory()
				.openSession();

		Transaction beginTransaction = session
				.beginTransaction();
		String sqlSelect = "";

		if (directory.equals("yahoo"))
			sqlSelect = "ar.yahooAccuracy field8";
		if (directory.equals("google"))
			sqlSelect = "ar.googleAccuracy field8";		
		//add the others
		
		String sql = "select 'Accurate' field17, " + sqlSelect
				+ " from accuracyreport ar"
				+ " where ar.store = ?";
	
		try {
			
						
			Query q = session.createSQLQuery(sql)
					.addScalar("field8", new IntegerType())
					.addScalar("field17", new StringType())										
					.setResultTransformer(Transformers.aliasToBean(ValueObject.class));	
			
			List<ValueObject> ret = q.setString(0, storeId)
									  .list();
			System.out.println(ret.get(0));
			
			
			ValueObject vo = new ValueObject();
			vo.setField17("Inaccurate");
			if (!ret.isEmpty() && ret!=null){
				vo.setField8(100 - (Integer)ret.get(0).getField8());
				
			}
			
			ret.add(vo);
			beginTransaction.commit();
			return ret;	
		} finally {
		}
	}
}
