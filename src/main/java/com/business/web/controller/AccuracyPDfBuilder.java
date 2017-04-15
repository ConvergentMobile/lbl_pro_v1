package com.business.web.controller;

import java.util.LinkedHashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.business.common.dto.AccuracyDTO;
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

public class AccuracyPDfBuilder extends AbstractITextPdfView{


	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		/*String reportType  = (String) model.get("accuracyreportType");
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""+reportType+".pdf" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");

		LinkedHashSet<AccuracyDTO> listOfBusiness = (LinkedHashSet<AccuracyDTO> ) model.get("listOfAPIs");
		
		
			if(reportType.equalsIgnoreCase("AccuracyListReport")){
				AccuracyListReport(document, listOfBusiness);
			}
		*/
	}

	private void AccuracyListReport(Document document,
			LinkedHashSet<AccuracyDTO> listOfBusiness) throws DocumentException {
	/*	PdfPTable table = new PdfPTable(12);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] {1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f,1.0f,1.0f});
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
		
		cell.setPhrase(new Phrase("Accuracy", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("GoogleErrorsCount", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("BingErrorsCount", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("YahooErrorsCount", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("YelpErrorsCount", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("YpErrorsCount", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("CitySearchErrorsCount", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("MapquestErrorsCount", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("SuperpagesErrorsCount", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("YellobookErrorsCount", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("WhitepagesErrorsCount", font));
		table.addCell(cell);
		
		
		
		for (AccuracyDTO accuracydto : listOfBusiness) {
			
	/*		table.addCell(accuracydto.getStore());
			table.addCell(String.valueOf(accuracydto.getAccuracy().toString()));
			table.addCell(String.valueOf(accuracydto.getGoogleErrorsCount()));
			table.addCell(String.valueOf(accuracydto.getBingErrorsCount()));
			table.addCell(String.valueOf(accuracydto.getYahooErrorsCount()));
			table.addCell(String.valueOf(accuracydto.getYelpErrorsCount()));
			table.addCell(String.valueOf(accuracydto.getYpErrorsCount()));
			table.addCell(String.valueOf(accuracydto.getCitySearchErrorsCount()));
			table.addCell(String.valueOf(accuracydto.getMapquestErrorsCount()));
			table.addCell(String.valueOf(accuracydto.getSuperpagesErrorsCount()));
			table.addCell(String.valueOf(accuracydto.getYellobookErrorsCount()));
			table.addCell(String.valueOf(accuracydto.getWhitepagesErrorsCount()));
		
			
		}
		
		document.add(table);*/
		
	}
		

	

}
