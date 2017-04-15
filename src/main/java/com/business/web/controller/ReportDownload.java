package com.business.web.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.business.model.pojo.ValueObject;
import com.business.web.bean.ReportForm;

/***
 * 
 * @author vasanth
 * 
 */

public class ReportDownload extends AbstractExcelView {

	Logger logger = Logger.getLogger(ReportDownload.class);

	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		
		String reportType  = (String) model.get("reportType");
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""+reportType+".xls" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");

		ReportForm listOfBusiness = (ReportForm) model
				.get("listOfAPI");
		if(reportType != null){
		if(reportType.equalsIgnoreCase("RenewalReport")){
			RenewalReport(workbook, listOfBusiness);
		}else if(reportType.equalsIgnoreCase("UploadReport")){
			uploadReport(workbook, listOfBusiness);
		}else if(reportType.equalsIgnoreCase("ExportReport")){
			ExportReport(workbook, listOfBusiness);
			
		}else if(reportType.equalsIgnoreCase("DistributionReport")){
			DistributionReport(workbook, listOfBusiness);
		}else if(reportType.equalsIgnoreCase("ChangeTrackingReport")){
			ChangeTrackingReport(workbook, listOfBusiness);
			
		}
		}

	}
	public void RenewalReport(HSSFWorkbook workbook,
			ReportForm listOfBusiness) {
		// create a new Excel sheet
		
		HSSFSheet sheet = workbook.createSheet("template");
		// create style for header cells		
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);		
		// create header row			
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Brand");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("#Locations");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("Renewal Date");
		header.getCell(2).setCellStyle(style);		
		header.createCell(3).setCellValue("Partner");
		header.getCell(3).setCellStyle(style);
		// create data rows
		int rowCount = 1;		
		List<ValueObject> reportRows = listOfBusiness.getReportRows();
			for (ValueObject valueObject : reportRows) {
				/*DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				String text = df.format( valueObject.getField3());*/
				HSSFRow aRow = sheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue((String) valueObject.getField1());
				aRow.createCell(1).setCellValue((String) valueObject.getField2());
				aRow.createCell(2).setCellValue((String)valueObject.getField3());
				aRow.createCell(3).setCellValue((String) valueObject.getField4());
				aRow.createCell(4).setCellValue((String) valueObject.getField5());
				
			}	
		
	}

	public void uploadReport(HSSFWorkbook workbook,
			ReportForm listOfBusiness) {
		// create a new Excel sheet
		
		HSSFSheet sheet = workbook.createSheet("template");
		// create style for header cells		
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);		
		// create header row			
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Brand");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("#Records");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("Date");
		header.getCell(2).setCellStyle(style);			
		// create data rows
		int rowCount = 1;		
		List<ValueObject> reportRows = listOfBusiness.getReportRows();
			for (ValueObject valueObject : reportRows) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue((String) valueObject.getField1());
				aRow.createCell(1).setCellValue((Integer) valueObject.getField2());
				aRow.createCell(2).setCellValue((String) valueObject.getField3());
				
			}	
		
	}

	public void ExportReport(HSSFWorkbook workbook,
			ReportForm listOfBusiness) {
		// create a new Excel sheet
		
		HSSFSheet sheet = workbook.createSheet("template");
		// create style for header cells		
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);		
		// create header row			
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Brand");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("#Records");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("Date");
		header.getCell(2).setCellStyle(style);			
		// create data rows
		int rowCount = 1;		
		List<ValueObject> reportRows = listOfBusiness.getReportRows();
			for (ValueObject valueObject : reportRows) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue((String) valueObject.getField1());
				aRow.createCell(1).setCellValue((Integer) valueObject.getField2());
				aRow.createCell(2).setCellValue((String) valueObject.getField3());
				
			}	
		
	}
	public void DistributionReport(HSSFWorkbook workbook,
			ReportForm listOfBusiness) {
		// create a new Excel sheet
		
		HSSFSheet sheet = workbook.createSheet("template");
		// create style for header cells		
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);		
		// create header row			
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Brand");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("#ClientId");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("Provider");
		header.getCell(2).setCellStyle(style);
		header.createCell(3).setCellValue("Last Submission");
		header.getCell(3).setCellStyle(style);	
		// create data rows
		int rowCount = 1;		
		List<ValueObject> reportRows = listOfBusiness.getReportRows();
			for (ValueObject valueObject : reportRows) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue((String) valueObject.getField1());
				aRow.createCell(1).setCellValue((Integer) valueObject.getField2());
				aRow.createCell(2).setCellValue((String) valueObject.getField3());
				aRow.createCell(3).setCellValue((String) valueObject.getField4());
			}		
	}	
	public void ChangeTrackingReport(HSSFWorkbook workbook,
			ReportForm listOfBusiness) {
		// create a new Excel sheet
		
		HSSFSheet sheet = workbook.createSheet("template");
		// create style for header cells		
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);		
		// create header row			
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Brand");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("Store#");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("BusinessName");
		header.getCell(2).setCellStyle(style);	
		header.createCell(3).setCellValue("Address");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("City");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("State");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("Zip");
		header.getCell(6).setCellStyle(style);
		header.createCell(7).setCellValue("Phone");
		header.getCell(7).setCellStyle(style);
		header.createCell(8).setCellValue("WebSite");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("Hourse Of Operation");
		header.getCell(9).setCellStyle(style);
		// create data rows
		int rowCount = 1;		
		List<ValueObject> reportRows = listOfBusiness.getReportRows();
			for (ValueObject valueObject : reportRows) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue((Integer) valueObject.getField1());
				aRow.createCell(1).setCellValue((String) valueObject.getField2());
				aRow.createCell(2).setCellValue((String) valueObject.getField3());
				aRow.createCell(3).setCellValue((String) valueObject.getField4());
				aRow.createCell(4).setCellValue((String) valueObject.getField5());
				aRow.createCell(5).setCellValue((String) valueObject.getField6());
				aRow.createCell(6).setCellValue((String) valueObject.getField7());
				aRow.createCell(7).setCellValue((String) valueObject.getField8());
				aRow.createCell(8).setCellValue((String) valueObject.getField9());				
				aRow.createCell(9).setCellValue((String) valueObject.getField10());
			}	
		
	}
	

}
