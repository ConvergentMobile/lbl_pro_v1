package com.business.model.dataaccess.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Document;

import com.business.model.pojo.ValueObject;
import com.business.web.controller.CompareListingDS;
import com.whitespark.ws.HibernateUtil;



public class CompareTestDAO {


	
	
	public static String[] getLatLongPositions(String address) throws Exception {
	    int responseCode = 0;
	    String api = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=true";
	    URL url = new URL(api);
	    HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
	    httpConnection.connect();
	    responseCode = httpConnection.getResponseCode();
	    if(responseCode == 200) {
	      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();;
	      Document document = builder.parse(httpConnection.getInputStream());
	      XPathFactory xPathfactory = XPathFactory.newInstance();
	      XPath xpath = xPathfactory.newXPath();
	      XPathExpression expr = xpath.compile("/GeocodeResponse/status");
	      String status = (String)expr.evaluate(document, XPathConstants.STRING);
	      if(status.equals("OK")) {
	         expr = xpath.compile("//geometry/location/lat");
	         String latitude = (String)expr.evaluate(document, XPathConstants.STRING);
	         expr = xpath.compile("//geometry/location/lng");
	         String longitude = (String)expr.evaluate(document, XPathConstants.STRING);
	         return new String[] {latitude, longitude};
	      }
	      else {
	         throw new Exception("Error from the API - response status: "+status);
	      }
	    }
	    return null;
	  }
	
	public String[] testLaton(String address) throws Exception {
		String[] ll = this.getLatLongPositions(address);
		System.out.println("lat, lon: " + ll[0] + ", " + ll[1]);
		return ll;
	}
	

	

	
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory()
				.openSession();

		Transaction beginTransaction = session
				.beginTransaction();
		 
	        try {        
	        	
	            String reportName = "F:\\reports\\compareListingJava";
	            final String rptOut = "F:\\reports\\compareListingJava.pdf";

	            Map<String, Object> params = new HashMap<String, Object>();
	            
	            String address = "1 Foster Ave, Pittsburg, PA 15205";
	            String [] ll = getLatLongPositions(address);
	            System.out.println("11"+ll[0]);
	            System.out.println(ll[1]);
	            String directory = "yahoo";
	            String storeId = "000902141";
	            

	           // params.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
	            params.put("brandName", "Goodyear");
	            params.put("logoDir", "F:\\images.jpg");
	            params.put("storeId", storeId);
	            params.put("directory", directory);
	            params.put("lat", Float.valueOf(ll[0]));
	            params.put("lon", Float.valueOf(ll[1]));
	            params.put("lat_d", Float.valueOf(ll[0]));
	            params.put("lon_d", Float.valueOf(ll[1]));
	            beginTransaction.commit();
	            List<ValueObject> res = CompareListingDS.getData(storeId, directory);
	            List<ValueObject> subDS1 = CompareListingDS.getDS(storeId, directory);
	            	System.out.println("subs1"+subDS1.get(0).getField8());
	            params.put("subDS1", subDS1);
	            Object field8 = subDS1.get(0).getField8();
	           
	            	
	            	params.put("accuracy", field8.toString());
	            
				  //to be used in the chart title

	            JasperCompileManager.compileReportToFile(reportName + ".jrxml", reportName + ".jasper");

	            JasperPrint print = JasperFillManager.fillReport(reportName + ".jasper", params, 
	                    new JRBeanCollectionDataSource(res));
	         
	            JasperFillManager.fillReport(reportName+".jasper", params, new  JRBeanCollectionDataSource(res));
	            System.out.println(print);
	            System.out.println(params);
	            JasperExportManager.exportReportToPdfFile(print, rptOut);

	        } catch (Exception e) {
				e.printStackTrace();
			} finally {
	        }
	}


}
