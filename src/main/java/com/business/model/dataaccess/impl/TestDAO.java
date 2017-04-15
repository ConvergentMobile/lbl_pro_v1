package com.business.model.dataaccess.impl;

import java.io.File;
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

import org.w3c.dom.Document;

import com.business.model.pojo.ValueObject;
import com.business.web.controller.ListingAccuracyDS;

public class TestDAO {

	// https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyBurxLsw2-u-PNBptKQ_IqP2TAEXt7wBII&location=45.4586408,-98.4447197&radius=50000
	// -- Google Places API
	public static String[] getLatLongPositions(String address) throws Exception {
		int responseCode = 0;
		String api = "https://maps.googleapis.com/maps/api/place/radarsearch/json?location="
				+ URLEncoder.encode("45.458,-98.444&radius=500&type=dentist", "UTF-8")
				+ "&key=AIzaSyBurxLsw2-u-PNBptKQ_IqP2TAEXt7wBII";
		// String api =
		// "http://maps.googleapis.com/maps/api/geocode/xml?address=" +
		// URLEncoder.encode(address, "UTF-8") + "&sensor=true";
		URL url = new URL(api);
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.connect();
		responseCode = httpConnection.getResponseCode();
		if (responseCode == 200) {
			String responseMessage = (String) httpConnection.getContent();
			System.out.println(responseMessage);
			/*
			 * DocumentBuilder builder =
			 * DocumentBuilderFactory.newInstance().newDocumentBuilder();;
			 * Document document =
			 * builder.parse(httpConnection.getInputStream()); XPathFactory
			 * xPathfactory = XPathFactory.newInstance(); XPath xpath =
			 * xPathfactory.newXPath(); XPathExpression expr =
			 * xpath.compile("/PlaceSearchResponse/status");
			 * 
			 * System.out.println(expr);
			 * 
			 * String status = (String)expr.evaluate(document,
			 * XPathConstants.STRING); if(status.equals("OK")) { expr =
			 * xpath.compile("//geometry/location/lat"); String latitude =
			 * (String)expr.evaluate(document, XPathConstants.STRING); expr =
			 * xpath.compile("//geometry/location/lng"); String longitude =
			 * (String)expr.evaluate(document, XPathConstants.STRING); expr =
			 * xpath.compile("//place_id"); String placeId =
			 * (String)expr.evaluate(document, XPathConstants.STRING);
			 * System.out.println("placeId====> "+ placeId);
			 * 
			 * return new String[] {latitude, longitude};
			 */
		}
		/*
		 * else { throw new Exception("Error from the API - response status: "
		 * +status); }
		 */

		return null;
	}

	public String[] testLaton(String address) throws Exception {
		String[] ll = this.getLatLongPositions(address);
		System.out.println("lat, lon: " + ll[0] + ", " + ll[1]);
		return ll;
	}

	public static void main(String[] args) {
		try {

			String[] ll = getLatLongPositions("zcxzcxz");
			// System.out.println("lat, lon: " + ll[0] + ", " + ll[1]);
			/*
			 * final String reportName = "_rep_010_Java"; //final String
			 * reportName = "D:\\reports\\listingActivityJava";
			 * JasperCompileManager.compileReportToFile(reportName + ".jrxml",
			 * reportName + ".jasper"); File tempFile =
			 * File.createTempFile("listingAccracy", ".pdf");// pdf String
			 * rptOut = tempFile.getAbsolutePath(); System.out.println(rptOut);
			 * 
			 * final Map<String, Object> params = new HashMap<String, Object>();
			 * 
			 * 
			 * 
			 * String address = "1 Foster Ave, Pittsburg, PA 15205"; String []
			 * ll = getLatLongPositions(address);
			 * 
			 * //params.put(JRHibernateQueryExecuterFactory.
			 * PARAMETER_HIBERNATE_SESSION, session); params.put("brandName",
			 * "PNC Bank"); params.put("logoDir", "F:\\images.jpg");
			 * params.put("storeId", "00005001"); params.put("lat",
			 * Float.valueOf(ll[0])); params.put("lon", Float.valueOf(ll[1]));
			 * 
			 * List<ValueObject> res = ListingAccuracyDS.getData();
			 * System.out.println(res);
			 * 
			 * JRBeanCollectionDataSource jrBeanCollectionDataSource=new
			 * JRBeanCollectionDataSource(res); JasperPrint print=
			 * JasperFillManager.fillReport(reportName + ".jasper", params,
			 * jrBeanCollectionDataSource);
			 * JasperExportManager.exportReportToPdfFile(print, rptOut);
			 */

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

}
