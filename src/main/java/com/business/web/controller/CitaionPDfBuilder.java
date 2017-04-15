package com.business.web.controller;

import java.util.LinkedHashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.CitationDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class CitaionPDfBuilder extends AbstractITextPdfView{


	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String reportType  = (String) model.get("citationreportType");
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""+reportType+".pdf" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");

		LinkedHashSet<CitationDTO> listOfBusiness = (LinkedHashSet<CitationDTO> ) model.get("listOfAPIs");
		
		
			if(reportType.equalsIgnoreCase("CitationListReport")){
				CiationListReport(document, listOfBusiness);
			}
		
	}

	private void CiationListReport(Document document,
			LinkedHashSet<CitationDTO> listOfBusiness) throws DocumentException {
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] {1.0f, 1.0f,1.0f,1.0f, 1.0f});
		table.setSpacingBefore(10);
		
		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		
		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setPadding(5);
		
		// write table header 
		cell.setPhrase(new Phrase("Store", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("BusinessName", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Address", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("PhoneNumber", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("CitationByLocation", font));
		table.addCell(cell);
		


		
		
		
		for (CitationDTO accuracydto : listOfBusiness) {
			
			table.addCell(accuracydto.getStore());
			table.addCell(String.valueOf(accuracydto.getBusinessName()));
			table.addCell(String.valueOf(accuracydto.getAddress()));
			table.addCell(String.valueOf(accuracydto.getPhoneNumber()));
			table.addCell(String.valueOf(accuracydto.getCitationCount()));




		
			
		}
		
		document.add(table);
		
	}
		

	

}
