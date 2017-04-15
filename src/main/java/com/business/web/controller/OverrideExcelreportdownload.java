package com.business.web.controller;

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

import com.business.common.dto.RenewalReportDTO;
import com.business.model.pojo.AuditEntity;
import com.business.model.pojo.ValueObject;
import com.business.web.bean.ReportForm;

public class OverrideExcelreportdownload extends AbstractExcelView {
	Logger logger = Logger.getLogger(OverrideExcelreportdownload.class);

	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String reportType = (String) model.get("overridereportType");
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ reportType + ".xls" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		List<AuditEntity> listOfBusiness = (List<AuditEntity>) model
				.get("listOfOverrideReportAPI");
		logger.info("listOfBusiness::::::::::::::::::::::::::" + listOfBusiness);

		SummaryListReport(workbook, listOfBusiness);

	}

	private void SummaryListReport(HSSFWorkbook workbook,
			List<AuditEntity> listOfBusiness) {

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
		header.createCell(4).setCellValue("Address");
		header.getCell(4).setCellStyle(style);
		header.createCell(5).setCellValue("City");
		header.getCell(5).setCellStyle(style);
		header.createCell(6).setCellValue("State");
		header.getCell(6).setCellStyle(style);
		header.createCell(7).setCellValue("Zip");
		header.getCell(7).setCellStyle(style);
		header.createCell(8).setCellValue("Override");
		header.getCell(8).setCellStyle(style);
		header.createCell(9).setCellValue("User");
		header.getCell(9).setCellStyle(style);

		// create data rows
		int rowCount = 1;
		for (AuditEntity obj : listOfBusiness) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			String store = obj.getStore();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

			aRow.createCell(1).setCellValue(store);
			aRow.createCell(2).setCellValue(obj.getBrandName());
			aRow.createCell(3).setCellValue(obj.getBusinessName());
			aRow.createCell(4).setCellValue(obj.getAddress());
			aRow.createCell(5).setCellValue(obj.getCity());
			aRow.createCell(6).setCellValue(obj.getState());
			aRow.createCell(7).setCellValue(obj.getZip());
			Date date = obj.getDate();
			aRow.createCell(8).setCellValue(format.format(date));
			aRow.createCell(9).setCellValue(obj.getUserName());

		}

	}

}
