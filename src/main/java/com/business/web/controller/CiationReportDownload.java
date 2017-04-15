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
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.CitationDTO;
import com.business.common.dto.CitationReportDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

public class CiationReportDownload extends AbstractExcelView {
	
	Logger logger = Logger.getLogger(CiationReportDownload.class);

	
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String reportType  = (String) model.get("citationreportType");
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""+reportType+".xls" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");

		LinkedHashSet<CitationReportDTO> listOfBusiness = (LinkedHashSet<CitationReportDTO> ) model.get("listOfAPIs");
		
		
			if(reportType.equalsIgnoreCase("CitationListReport")){
				CiationListReport(workbook, listOfBusiness);
			}
		
	}
		
		private void CiationListReport(HSSFWorkbook workbook,
			LinkedHashSet<CitationReportDTO> listOfBusiness) {
	
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
		header.createCell(2).setCellValue("BusinessName");
		header.getCell(2).setCellStyle(style);
		header.createCell(3).setCellValue("Address");
		header.getCell(3).setCellStyle(style);
		header.createCell(4).setCellValue("State");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("ZipCode");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("PhoneNumber");
		header.getCell(6).setCellStyle(style);
		header.createCell(7).setCellValue("CitationByLocation");
		header.getCell(7).setCellStyle(style);


		// create data rows
		int rowCount = 1;
		for (CitationReportDTO obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(1).setCellValue(obj.getStore());
			aRow.createCell(2).setCellValue(obj.getBusinesName());
			aRow.createCell(3).setCellValue(obj.getAddress());
			aRow.createCell(4).setCellValue(obj.getState());
			aRow.createCell(5).setCellValue(obj.getZip());
			aRow.createCell(6).setCellValue(obj.getPhone());
			aRow.createCell(7).setCellValue(obj.getPathCount());

		}
		
	}

}