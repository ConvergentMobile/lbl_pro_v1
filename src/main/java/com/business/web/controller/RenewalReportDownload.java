package com.business.web.controller;

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

import com.business.common.dto.RenewalReportDTO;

public class RenewalReportDownload extends AbstractExcelView {
	
	Logger logger = Logger.getLogger(RenewalReportDownload.class);

	
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String reportType  = (String) model.get("renewalreportType");		
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""+reportType+".xls" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		List<RenewalReportDTO> listOfBusiness = (List<RenewalReportDTO> ) model.get("listOfAPI");		
		
			 if(reportType.equalsIgnoreCase("SprintReport")){
				RenewalListReport(workbook, listOfBusiness);
			}
		
	}
		
		private void RenewalListReport(HSSFWorkbook workbook,
			List<RenewalReportDTO> listOfBusiness) {

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
				
					aRow.createCell(1).setCellValue(obj.getStore());
					aRow.createCell(2).setCellValue(obj.getBrandName());
					aRow.createCell(3).setCellValue(obj.getBusinessName());
					aRow.createCell(4).setCellValue(obj.getAcxiomRenewalDate());
					aRow.createCell(5).setCellValue(obj.getFactualRenewalDate());
					aRow.createCell(6).setCellValue(obj.getInfogroupRenewalDate());
					aRow.createCell(7).setCellValue(obj.getLocalezeRenewalDate());
				
				}
			
			
				
			}
			
		
	}

	


