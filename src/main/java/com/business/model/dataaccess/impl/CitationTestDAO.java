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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.w3c.dom.Document;

import com.business.model.pojo.ValueObject;
import com.business.web.controller.CitationsDS;
import com.business.web.controller.ListingAccuracyDS;



public class CitationTestDAO {
public static void main(String[] args) {
		  try {        
	            String reportName = "F:\\reports\\citationReportJava";
	            final String rptOut = "F:\\citationReportJava1222.pdf";

	            Map<String, Object> params = new HashMap<String, Object>();

	            String storeId = "000900128"; 
	            String brandName = "Goodyear";
	            
	         
	            params.put("brandName", brandName);
	            params.put("logoDir", "F:\\images.jpg");
	            params.put("storeId", storeId);
	            
	            List<ValueObject> res = CitationsDS.getData();
	            
	            List<ValueObject> subDS1 = CitationsDS.getDS(brandName);
	            params.put("subDS1", subDS1);

	            JasperCompileManager.compileReportToFile(reportName + ".jrxml", reportName + ".jasper");
	            //JasperPrint print = JasperFillManager.fillReport(reportName + ".jasper", params);    
	            JasperPrint print = JasperFillManager.fillReport(reportName + ".jasper", params, 
	                    new JRBeanCollectionDataSource(res));
	            JasperExportManager.exportReportToPdfFile(print, rptOut);

	        } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	        }
	}


}
