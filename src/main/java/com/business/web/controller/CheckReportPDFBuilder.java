package com.business.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.business.common.dto.LocalBusinessDTO;
import com.business.model.dataaccess.impl.CompareTestDAO;
import com.business.model.dataaccess.impl.TestDAO;
import com.business.model.pojo.ValueObject;
import com.business.service.BusinessService;
import com.business.service.CheckReportService;
@Controller
public class CheckReportPDFBuilder{
	
	Logger logger = Logger.getLogger(CheckReportPDFBuilder.class);
	
	
	@Autowired
	private BusinessService service;
	
	
	@Autowired
	private CheckReportService checkReportService;
	@RequestMapping(value="clreportpdfreport.htm",method=RequestMethod.GET)
	public void exportCheckReportPDf(HttpServletResponse response,
			HttpServletRequest request, HttpSession session) throws IOException {
		String store = request.getParameter("store");
		logger.info("store:::::::::::::::" + store);
		String brandname = request.getParameter("brandname");
		logger.info("brandname:::::::::::::::" + brandname);
		Integer brandId = checkReportService.getClinetIdIdByName(brandname);
		ServletContext servletContext = request.getSession().getServletContext();
		String offerTempaltePath = servletContext.getRealPath("_rep_010_Java");
		String logo=servletContext.getRealPath("logo-lbl1.png");
		String logoListing=servletContext.getRealPath("ico_title_listing_report.png");
		File tempFile = File.createTempFile("listingAccracy", ".pdf");// pdf
		String absolutePath = tempFile.getAbsolutePath();
		try {		
			final String reportName = offerTempaltePath;
			//final String reportName = "D:\\reports\\listingActivityJava";
			JasperCompileManager.compileReportToFile(reportName + ".jrxml", reportName + ".jasper");
			
					
			final Map<String, Object> params = new HashMap<String, Object>();
			

			
			
		    List<ValueObject> res = service.getData(store,brandname);
		    
		   
		    String address="";
		    if(res!=null){
		    	 ValueObject valueObject = res.get(0);
		    	Object field2 = valueObject.getField2();
			    Object field3 = valueObject.getField3();
			    Object field4 = valueObject.getField4();
			    Object field5 = valueObject.getField5();
			     address = field2.toString()+","+field3.toString()+","+field4.toString()+" "+field5.toString();
				
		    }else{
		    	LocalBusinessDTO searchBusinessinfo = service.getBusinessListinginfo(store, brandname);
		    	if(searchBusinessinfo!=null){
		    		String locationAddress = searchBusinessinfo.getLocationAddress();
		    		String locationCity = searchBusinessinfo.getLocationCity();
		    		String locationZipCode = searchBusinessinfo.getLocationZipCode();
		    		String locationState = searchBusinessinfo.getLocationState();
		    		address=locationAddress+","+locationState+","+locationCity+","+locationZipCode;
		    	}
						
	
		    }
			
		    //System.out.println(res);
		   
		    String [] ll = TestDAO.getLatLongPositions(address);
		    //params.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
		    params.put("brandName", brandname);
		    params.put("brandId", brandId);
		    params.put("logoDir", logo);
		    params.put("logoListing", logoListing);
		    params.put("storeId", store);
		    params.put("lat", Float.valueOf(ll[0]));
		    params.put("lon", Float.valueOf(ll[1]));
		    
	        JRBeanCollectionDataSource jrBeanCollectionDataSource=new JRBeanCollectionDataSource(res);
	        JasperPrint print= JasperFillManager.fillReport(reportName + ".jasper", params, 
					jrBeanCollectionDataSource);
			  JasperExportManager.exportReportToPdfFile(print, absolutePath);
	
				ServletContext context = session.getServletContext();
				final int BUFFER_SIZE = 4096;
				try {
			
					File downloadFile = new File(absolutePath);
					FileInputStream inputStream = new FileInputStream(downloadFile);

					String mimeType = context.getMimeType(absolutePath);
					if (mimeType == null) {
						// set to binary type if MIME mapping not found
						mimeType = "application/pdf";
					}
					System.out.println("MIME type: " + mimeType);

					// set content attributes for the response
					response.setContentType(mimeType);
					response.setContentLength((int) downloadFile.length());

					// set headers for the response
					String headerKey = "Content-Disposition";
					String headerValue = String.format("attachment; filename=\"%s\"",
							downloadFile.getName());
					response.setHeader(headerKey, headerValue);

					// get output stream of the response
					OutputStream outStream = response.getOutputStream();
					byte[] buffer = new byte[BUFFER_SIZE];
					int bytesRead = -1;

					// write bytes read from the input stream into the output stream
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, bytesRead);
					}

					inputStream.close();
					outStream.close();

				} catch (Exception e) {
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	@RequestMapping(value="compareListingPdfExport.htm",method=RequestMethod.GET)
	public void exportCompareListingPdf(HttpServletResponse response,@RequestParam("directory") String directory,
			HttpServletRequest request, HttpSession session) throws IOException {
		String storeId = request.getParameter("store");
		logger.info("store:::::::::::::::" + storeId);
		String brandname = request.getParameter("brandname");
		logger.info("brandname:::::::::::::::" + brandname);
		Integer brandId = checkReportService.getClinetIdIdByName(brandname);
		logger.info("brandId::::::::::::::::::::"+brandId);
		File tempFile = File.createTempFile("compareListing", ".pdf");
		  String rptOut = tempFile.getAbsolutePath();
			ServletContext servletContext = request.getSession().getServletContext();
			String logo=servletContext.getRealPath("logo-lbl1.png");
			String logoCompare=servletContext.getRealPath("ico_title_listing_compare.png");
			String offerTempaltePath = servletContext.getRealPath("compareListingJava");
		try {        
            String reportName = offerTempaltePath;
           

            Map<String, Object> params = new HashMap<String, Object>();
            
            
    
            List<ValueObject> res = service.getComapareListData(storeId, directory,brandname);
            String address="";
            String otherAddress="";
            if(res!=null){
                Object field2 = res.get(0).getField2();
    		    Object field3 = res.get(0).getField3();
    		    Object field4 = res.get(0).getField4();
    		    Object field5 = res.get(0).getField5();
    		    //System.out.println(res);
    		     address = field2.toString()+","+field3.toString()+","+field4.toString()+" "+field5.toString();
    		    Object field10 = res.get(0).getField10();
   			 Object field11 = res.get(0).getField11();
   			 Object field12 = res.get(0).getField12();
   			 Object field13 = res.get(0).getField13();
   			     otherAddress = field10.toString()+","+field11.toString()+","+field12.toString()+" "+field13.toString();
            }else{
            	LocalBusinessDTO searchBusinessinfo = service.getBusinessListinginfo(storeId, brandname);
		    	if(searchBusinessinfo!=null){
		    		String locationAddress = searchBusinessinfo.getLocationAddress();
		    		String locationCity = searchBusinessinfo.getLocationCity();
		    		String locationZipCode = searchBusinessinfo.getLocationZipCode();
		    		String locationState = searchBusinessinfo.getLocationState();
		    		address=locationAddress+","+locationState+","+locationCity+","+locationZipCode;
		    		otherAddress=address;
		    	}
            }
            												
            
			String [] ll = TestDAO.getLatLongPositions(address);
			
				String [] l2 = TestDAO.getLatLongPositions(otherAddress);
				
            params.put("brandName", brandname);
            params.put("logoDir", logo);
            params.put("logoCompare", logoCompare);
            params.put("storeId", storeId);
            params.put("brandId", brandId);
            params.put("directory", directory);
            params.put("lat", Float.valueOf(l2[0]));
            params.put("lon", Float.valueOf(l2[1]));
            params.put("lat_d", Float.valueOf(ll[0]));
            params.put("lon_d", Float.valueOf(ll[1]));
            List<ValueObject> subDS1 = service.getDS(storeId, directory,brandname);
            
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
            ServletContext context = session.getServletContext();
			final int BUFFER_SIZE = 4096;
			try {
		
				File downloadFile = new File(rptOut);
				FileInputStream inputStream = new FileInputStream(downloadFile);

				String mimeType = context.getMimeType(rptOut);
				if (mimeType == null) {
					// set to binary type if MIME mapping not found
					mimeType = "application/pdf";
				}
				System.out.println("MIME type: " + mimeType);

				// set content attributes for the response
				response.setContentType(mimeType);
				response.setContentLength((int) downloadFile.length());

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"",
						downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				// get output stream of the response
				OutputStream outStream = response.getOutputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;

				// write bytes read from the input stream into the output stream
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}

				inputStream.close();
				outStream.close();

			} catch (Exception e) {
			}
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
        }
	}
}
