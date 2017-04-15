package com.business.web.controller;

import java.util.LinkedHashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.business.common.dto.CheckReportDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class CheckListPDfBuilder extends AbstractITextPdfView{


	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String reportType  = (String) model.get("checkreportType");
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""+reportType+".pdf" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");

		LinkedHashSet<CheckReportDTO> listOfBusiness = (LinkedHashSet<CheckReportDTO> ) model.get("listOfAPIs");
		
		
			if(reportType.equalsIgnoreCase("CheckListReport")){
				CheckListReport(document, listOfBusiness);
			}
		
	}

	public void CheckListReport(Document document,
			LinkedHashSet<CheckReportDTO> listOfBusiness) throws DocumentException {
		
		PdfPTable table = new PdfPTable(8);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] {1.0f, 2.0f,2.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f});
		table.setSpacingBefore(10);
		
		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		
		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setPadding(5);
		
		// write table header 
		cell.setPhrase(new Phrase("Directory", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("BusinessName", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Address", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("City", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("State", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Zip", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Phone", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Store", font));
		table.addCell(cell);
		
		
		for (CheckReportDTO checkReportDTO : listOfBusiness) {
			
			table.addCell(checkReportDTO.getDirectory());
			table.addCell(checkReportDTO.getBusinessname());
			table.addCell(checkReportDTO.getAddress());
			table.addCell(checkReportDTO.getCity());
			table.addCell(checkReportDTO.getState());
			table.addCell(checkReportDTO.getZip());
			table.addCell(checkReportDTO.getPhone());
			table.addCell(checkReportDTO.getStore());
		
			
		}
		
		document.add(table);
		
	}
		
	
	
	

}
