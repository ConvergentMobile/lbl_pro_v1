package com.business.web.controller;

import java.util.LinkedHashSet;
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

import com.business.common.dto.CheckReportDTO;

public class CheckReportListDownload extends AbstractExcelView {
	
	Logger logger = Logger.getLogger(ReportDownload.class);

	
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String reportType  = (String) model.get("checkreportType");
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""+reportType+".xls" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");

		LinkedHashSet<CheckReportDTO> listOfBusiness = (LinkedHashSet<CheckReportDTO> ) model.get("listOfAPIs");
		
		
			if(reportType.equalsIgnoreCase("CheckListReport")){
				CheckListReport(workbook, listOfBusiness);
			}
		
	}
		
		public void CheckListReport(HSSFWorkbook workbook,
				LinkedHashSet<CheckReportDTO>  listOfBusiness) {
			// create a new Excel sheet
			
			HSSFSheet sheet = workbook.createSheet("template");
			// create style for header cells
			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setFontName("Calibri");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			style.setFont(font);
			// create header row
			// BingPlacesSample Template
			HSSFRow header = sheet.createRow(0);
			
			header.createCell(2).setCellValue("Directory");
			header.getCell(2).setCellStyle(style);
			header.createCell(3).setCellValue("BusinessName");
			header.getCell(3).setCellStyle(style);
			header.createCell(4).setCellValue("Address");
			header.getCell(4).setCellStyle(style);
			header.createCell(5).setCellValue("City");
			header.getCell(5).setCellStyle(style);
			header.createCell(6).setCellValue("State");
			header.getCell(6).setCellStyle(style);
			header.createCell(7).setCellValue("Zip");
			header.getCell(7).setCellStyle(style);
			header.createCell(8).setCellValue("Phone");
			header.getCell(8).setCellStyle(style);
			header.createCell(9).setCellValue("Store");
			header.getCell(9).setCellStyle(style);
			// create data rows
			int rowCount = 1;
			for (CheckReportDTO obj : listOfBusiness) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				
				aRow.createCell(2).setCellValue(obj.getDirectory());
				aRow.createCell(3).setCellValue(obj.getBusinessname());
				aRow.createCell(4).setCellValue(obj.getAddress());
				aRow.createCell(5).setCellValue(obj.getCity());
				aRow.createCell(6).setCellValue(obj.getState());
				aRow.createCell(7).setCellValue(obj.getZip());
				aRow.createCell(8).setCellValue(obj.getPhone());
				aRow.createCell(9).setCellValue(obj.getStore());
			}
			
		}

}
