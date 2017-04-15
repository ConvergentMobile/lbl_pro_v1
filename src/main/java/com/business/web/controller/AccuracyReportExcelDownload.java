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

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.CheckReportDTO;

public class AccuracyReportExcelDownload extends AbstractExcelView {
	
	Logger logger = Logger.getLogger(AccuracyReportExcelDownload.class);

	
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String reportType  = (String) model.get("accuracyreportType");
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""+reportType+".xls" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");

		LinkedHashSet<AccuracyDTO> listOfBusiness = (LinkedHashSet<AccuracyDTO> ) model.get("listOfAPIs");
		
		
			if(reportType.equalsIgnoreCase("AccuracyListReport")){
				AccuracyListReport(workbook, listOfBusiness);
			}
		
	}
		
		private void AccuracyListReport(HSSFWorkbook workbook,
			LinkedHashSet<AccuracyDTO> listOfBusiness) {
	
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
		
		header.createCell(1).setCellValue("Store");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("Accuracy");
		header.getCell(2).setCellStyle(style);
		header.createCell(3).setCellValue("GoogleErrorsCount");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("BingErrorsCount");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("YahooErrorsCount");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("YelpErrorsCount");
		header.getCell(6).setCellStyle(style);
		header.createCell(7).setCellValue("YpErrorsCount");
		header.getCell(7).setCellStyle(style);
		header.createCell(8).setCellValue("CitySearchErrorsCount");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("MapquestErrorsCount");
		header.getCell(9).setCellStyle(style);
		header.createCell(10).setCellValue("SuperpagesErrorsCount");
		header.getCell(10).setCellStyle(style);
		header.createCell(11).setCellValue("YellobookErrorsCount");
		header.getCell(11).setCellStyle(style);
		header.createCell(12).setCellValue("WhitepagesErrorsCount");
		header.getCell(12).setCellStyle(style);
		
		// create data rows
		int rowCount = 1;
		for (AccuracyDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(1).setCellValue(obj.getStore());
			aRow.createCell(2).setCellValue(obj.getAccuracy());
			
			
			aRow.createCell(3).setCellValue(obj.getGoogleColourCode());
			aRow.createCell(4).setCellValue(obj.getBingColourCode());
			aRow.createCell(5).setCellValue(obj.getYahooColourCode());
			aRow.createCell(6).setCellValue(obj.getYelpColourCode());
			aRow.createCell(7).setCellValue(obj.getYpColourCode());
			aRow.createCell(8).setCellValue(obj.getCitySearchColourCode());
			aRow.createCell(9).setCellValue(obj.getMapquestColourCode());
			aRow.createCell(10).setCellValue(obj.getSuperpagesColourCode());
			aRow.createCell(11).setCellValue(obj.getYellowbookColourCode());
			aRow.createCell(12).setCellValue(obj.getWhitepagesColourCode());
			
		}
		
	}

}
