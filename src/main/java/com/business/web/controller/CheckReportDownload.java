package com.business.web.controller;

import java.util.LinkedHashSet;
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

import com.business.common.dto.SprintDTO;

public class CheckReportDownload extends AbstractExcelView {
	
	Logger logger = Logger.getLogger(CheckReportDownload.class);

	
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String reportType  = (String) model.get("clreportType");		
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""+reportType+".xls" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		LinkedHashSet<SprintDTO> listOfBusiness = (LinkedHashSet<SprintDTO> ) model.get("listOfAPI");		
		
			 if(reportType.equalsIgnoreCase("SprintReport")){
				SprintListReport(workbook, listOfBusiness);
			}
		
	}
		
		private void SprintListReport(HSSFWorkbook workbook,
			LinkedHashSet<SprintDTO> listOfBusiness) {

			HSSFSheet sheet = workbook.createSheet("template");
			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setFontName("Calibri");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			style.setFont(font);
			// create header row
			HSSFRow header = sheet.createRow(0);
			header.createCell(1).setCellValue("Store");
			header.getCell(1).setCellStyle(style);
			header.createCell(2).setCellValue("Directory");
			header.getCell(2).setCellStyle(style);
			header.createCell(3).setCellValue("LBl Business Name");
			header.getCell(3).setCellStyle(style);
			header.createCell(4).setCellValue("LBL Address");
			header.getCell(4).setCellStyle(style);
			header.createCell(5).setCellValue("LBL City");
			header.getCell(5).setCellStyle(style);
			header.createCell(6).setCellValue("LBl State");
			header.getCell(6).setCellStyle(style);
			header.createCell(7).setCellValue("LBl Zip");
			header.getCell(7).setCellStyle(style);
			header.createCell(8).setCellValue("LBl Phone");
			header.getCell(8).setCellStyle(style);			
			header.createCell(10).setCellValue("Directory");
			header.getCell(10).setCellStyle(style);
			header.createCell(11).setCellValue("Business Name");
			header.getCell(11).setCellStyle(style);
			header.createCell(12).setCellValue("Address");
			header.getCell(12).setCellStyle(style);
			header.createCell(13).setCellValue("City");
			header.getCell(13).setCellStyle(style);
			header.createCell(14).setCellValue("State");
			header.getCell(14).setCellStyle(style);
			header.createCell(15).setCellValue("Zip");
			header.getCell(15).setCellStyle(style);
			header.createCell(16).setCellValue("Phone");
			header.getCell(16).setCellStyle(style);
			
			// create data rows
			int rowCount = 1;
			for (SprintDTO obj : listOfBusiness) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				
					aRow.createCell(1).setCellValue(obj.getStore());
					aRow.createCell(2).setCellValue(obj.getDirectory());
					aRow.createCell(3).setCellValue(obj.getLbl_businessName());
					aRow.createCell(4).setCellValue(obj.getLbl_address());
					aRow.createCell(5).setCellValue(obj.getLbl_city());
					aRow.createCell(6).setCellValue(obj.getLbl_state());
					aRow.createCell(7).setCellValue(obj.getLbl_zip());
					aRow.createCell(8).setCellValue(obj.getLbl_phone());				
					aRow.createCell(10).setCellValue(obj.getSprintDirectory());
					aRow.createCell(11).setCellValue(obj.getSprint_businessName());
					aRow.createCell(12).setCellValue(obj.getSprint_address());
					aRow.createCell(13).setCellValue(obj.getSprint_city());
					aRow.createCell(14).setCellValue(obj.getSprint_state());
					aRow.createCell(15).setCellValue(obj.getSprint_zip());
					aRow.createCell(16).setCellValue(obj.getSprint_phone());
				}
			
			
				
			}
			
		
	}

	


