package com.business.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

/***
 * 
 * @author vasanth
 *
 */

@Controller
public class TemplateDownloadController {
	
	Logger logger = Logger.getLogger(TemplateDownloadController.class);
	ResourceBundle bundle = ResourceBundle.getBundle("messages");
	
	/**
	 * Size of a byte buffer to read/write file
	 */
	private static final int BUFFER_SIZE = 4096;
			
	/**
	 * Path of the file to be downloaded, relative to application's directory
	 */
	private String filePath = bundle.getString("mastertemplate.path");
	
	/**
	 * Path of the file to be downloaded, relative to application's directory
	 */
	private String bingFilePath = bundle.getString("mastertemplate.bingPath");
	/**
	 * Path of the file to be downloaded, relative to application's directory
	 */
	private String googleFilePath = bundle.getString("mastertemplate.googlePath");
	private String YpFilePath = bundle.getString("mastertemplate.ypPath");
	//2533_598167450219661_2142348102_n.jpg
	
	/**
	 * Method for handling file download request from client
	 */
	//@RequestMapping(value="/bulkExports.htm",method = RequestMethod.GET)
	public void doDownload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// get absolute path of the application
		ServletContext context = request.getSession().getServletContext();
		String appPath = context.getRealPath("");
		logger.info("appPath = " + appPath);		
		String templateName = request.getParameter("service");
		// construct the complete absolute path of the file
				String fullPath = appPath ;	
		if(templateName.equals("MasterTemplate")){
			 fullPath = appPath + filePath;		
		}else if(templateName.equals("Bing")){
			 fullPath = appPath + bingFilePath;		
		}else if(templateName.equals("Google")){
			 fullPath = appPath + googleFilePath;		
		}else if(templateName.equals("YP")){
			 fullPath = appPath + YpFilePath;		
		}
		
		logger.info("Full path = "+fullPath);
		File downloadFile = new File(fullPath);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		
		// get MIME type of the file
		String mimeType = context.getMimeType(fullPath);
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}
		logger.info("MIME type: " + mimeType);

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

	}
	
}