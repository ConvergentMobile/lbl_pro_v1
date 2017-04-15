package com.business.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.business.common.dto.InsightsGraphDTO;

public class GMBBrandExcelReport extends AbstractExcelView {
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String reportName  = (String) model.get("reportName");
		String startDate  = (String)model.get("startDate");
		String endDate = (String)model.get("endDate");
		List<InsightsGraphDTO> insightsExcelData = (List<InsightsGraphDTO>)model.get("insightsExcelData");
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""+reportName+".xls" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		
		GenerateGMBInsightsExcelReport(workbook, insightsExcelData,reportName, startDate,endDate);
		
	}
	
	private void GenerateGMBInsightsExcelReport(HSSFWorkbook workbook, List<InsightsGraphDTO> insightsExcelData , String reportName,String startDate,String endDate){

		HSSFSheet sheet = workbook.createSheet(reportName);
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeight((short)280);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);

		HSSFRow header = sheet.createRow(0);
		
		header.createCell(0).setCellValue("Client ID");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("Store #");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("City");
		header.getCell(2).setCellStyle(style);
		header.createCell(3).setCellValue("state");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("Start Date");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("End Date");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("Direct Searches");
		header.getCell(6).setCellStyle(style);
		header.createCell(7).setCellValue("Discovery Searches");
		header.getCell(7).setCellStyle(style);
		
		header.createCell(8).setCellValue("Search Views");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("Map Views");
		header.getCell(9).setCellStyle(style);
		
		header.createCell(10).setCellValue("Driving Directions");
		header.getCell(10).setCellStyle(style);
		header.createCell(11).setCellValue("Website Clicks");
		header.getCell(11).setCellStyle(style);
		
		header.createCell(12).setCellValue("Phone Calls");
		header.getCell(12).setCellStyle(style);

		CellStyle styleValues = workbook.createCellStyle();
		Font fontValues = workbook.createFont();
		fontValues.setFontName("Calibri");
		fontValues.setFontHeight((short)200);
		styleValues.setFont(fontValues);
		// create data rows
		int rowCount = 1;
		for (InsightsGraphDTO obj : insightsExcelData) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(obj.getBrandId());
			aRow.getCell(0).setCellStyle(styleValues);
			aRow.createCell(1).setCellValue(obj.getStore());
			aRow.getCell(1).setCellStyle(styleValues);
			aRow.createCell(2).setCellValue(obj.getCity());
			aRow.getCell(2).setCellStyle(styleValues);
			aRow.createCell(3).setCellValue(obj.getState());
			aRow.getCell(3).setCellStyle(styleValues);
			aRow.createCell(4).setCellValue(startDate);
			aRow.getCell(4).setCellStyle(styleValues);
			aRow.createCell(5).setCellValue(endDate);
			aRow.getCell(5).setCellStyle(styleValues);
			aRow.createCell(6).setCellValue(obj.getDirectCount());
			aRow.getCell(6).setCellStyle(styleValues);
			aRow.createCell(7).setCellValue(obj.getInDirectCount());
			aRow.getCell(7).setCellStyle(styleValues);
			aRow.createCell(8).setCellValue(obj.getSearchCount());
			aRow.getCell(8).setCellStyle(styleValues);
			aRow.createCell(9).setCellValue(obj.getMapCount());
			aRow.getCell(9).setCellStyle(styleValues);
			aRow.createCell(10).setCellValue(obj.getDirectionsCount());
			aRow.getCell(10).setCellStyle(styleValues);
			aRow.createCell(11).setCellValue(obj.getWebsiteCount());
			aRow.getCell(11).setCellStyle(styleValues);
			aRow.createCell(12).setCellValue(obj.getCallsCount());
			aRow.getCell(12).setCellStyle(styleValues);
		}
		
	}
	
	public GMBBrandExcelReport() {
		// TODO Auto-generated constructor stub
	}

}
