package com.business.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.business.common.dto.RenewalReportDTO;
import com.business.common.util.ControllerUtil;
import com.business.model.pojo.ValueObject;
import com.business.web.bean.ReportForm;

public class SummaryExcelreportdownload extends AbstractExcelView {
	Logger logger = Logger.getLogger(SummaryExcelreportdownload.class);

	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String reportType = (String) model.get("summaryreportType");
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ reportType + ".xls" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		List<RenewalReportDTO> listOfBusiness = (List<RenewalReportDTO>) model
				.get("listOfSummaryReportAPI");
		logger.info("listOfBusiness::::::::::::::::::::::::::" + listOfBusiness);
			HttpSession session = request.getSession();
			String brandName="";
			String email="";
			if(listOfBusiness !=null &&listOfBusiness.size()>0){
				RenewalReportDTO renewalReportDTO = listOfBusiness.get(0);
				brandName=renewalReportDTO.getBrandName();
				email=renewalReportDTO.getEmail();
			}
		SummaryListReport(workbook, listOfBusiness,session,request,brandName,email);

	}

	private void SummaryListReport(HSSFWorkbook workbook,
			List<RenewalReportDTO> listOfBusiness,HttpSession session,HttpServletRequest request,String brandName,String email) throws IOException {

		HSSFSheet sheet = workbook.createSheet("template");
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// create header row
		HSSFRow header = sheet.createRow(0);
		header.createCell(1).setCellValue("Store # (Listing ID)");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("Brand");
		header.getCell(2).setCellStyle(style);
		header.createCell(3).setCellValue("Business Name");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("acxiom");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("factual");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("Infogroup");
		header.getCell(6).setCellStyle(style);
		header.createCell(7).setCellValue("nustar");
		header.getCell(7).setCellStyle(style);

		// create data rows
		int rowCount = 1;
		for (RenewalReportDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			String store = obj.getStore();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

			aRow.createCell(1).setCellValue(store);
			aRow.createCell(2).setCellValue(obj.getBrandName());
			aRow.createCell(3).setCellValue(obj.getBusinessName());
			Date renewalDate = obj.getRenewalDate();
			Date acxiomRenewalDate = obj.getAcxiomRenewalDate();
			String renewaldate = "";
			String acxiomdate = "";
			String neustardate = "";
			String infogroupdate = "";
			String factualdate = "";
			if (renewalDate != null) {
				renewaldate = format.format(renewalDate);
			}
			if (acxiomRenewalDate != null) {
				acxiomdate = format.format(acxiomRenewalDate);
			}

			aRow.createCell(4).setCellValue(renewaldate);
			Date factualRenewalDate = obj.getFactualRenewalDate();
			if (acxiomRenewalDate != null) {
				factualdate = format.format(factualRenewalDate);
			}

			aRow.createCell(5).setCellValue(renewaldate);
			Date infogroupRenewalDate = obj.getInfogroupRenewalDate();
			if (acxiomRenewalDate != null) {
				infogroupdate = format.format(infogroupRenewalDate);
			}

			aRow.createCell(6).setCellValue(renewaldate);
			Date localezeRenewalDate = obj.getLocalezeRenewalDate();
			if (acxiomRenewalDate != null) {
				neustardate = format.format(localezeRenewalDate);
			}

			aRow.createCell(7).setCellValue(renewaldate);

		}
		File tempFile = File.createTempFile("renewalReport", ".xls");
		 FileOutputStream out = 
		            new FileOutputStream(tempFile);
		 String absolutePath = tempFile.getAbsolutePath();
		 System.out.println(absolutePath);
		 String userName = (String) session.getAttribute("userName");
		 System.out.println(userName);
		
		 workbook.write(out);
		 String reportName="Summary Report";
		 //DataSource fds = new FileDataSource("renewalReport.xls");
		 ControllerUtil mailUtil=new ControllerUtil();
		 mailUtil.sendTemplate(userName,absolutePath,workbook,request,reportName,email);

	}

}
