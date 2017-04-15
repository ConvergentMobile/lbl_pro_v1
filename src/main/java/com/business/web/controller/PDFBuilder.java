package com.business.web.controller;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.business.model.pojo.ValueObject;
import com.business.web.bean.ReportForm;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This view class generates a PDF document 'on the fly' based on the data
 * contained in the model.
 * 
 */
public class PDFBuilder extends AbstractITextPdfView {

	protected void buildPdfDocument(Map<String, Object> model, Document doc,
			PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// get data model which is passed by the Spring container
		
		String reportType  = (String) model.get("reportType");
		
		response.setHeader("Content-Disposition", "attachment; filename=\""+reportType+".pdf" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		
		ReportForm listOfBusiness = (ReportForm) model.get("listOfAPI");
		
		
		if(reportType != null){
			if(reportType.equalsIgnoreCase("RenewalReport")){
				RenewalReport(doc, listOfBusiness);
			}else if(reportType.equalsIgnoreCase("UploadReport")){
				uploadReport(doc, listOfBusiness);
			}else if(reportType.equalsIgnoreCase("ExportReport")){
				ExportReport(doc, listOfBusiness);
				
			}else if(reportType.equalsIgnoreCase("DistributionReport")){
				DistributionReport(doc, listOfBusiness);
			}else if(reportType.equalsIgnoreCase("ChangeTrackingReport")){
				ChangeTrackingReport(doc, listOfBusiness);
				
			}
			}
	}
	
	public void ChangeTrackingReport(Document doc, ReportForm listOfBusiness)throws DocumentException {
		
		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] {1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f});
		table.setSpacingBefore(10);
		
		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		
		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setPadding(5);
		
		
		
		// write table header 
		cell.setPhrase(new Phrase("Brand", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Store#", font));
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
		
		cell.setPhrase(new Phrase("WebSite", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Hourse Of Operation", font));
		table.addCell(cell);
		
		// write table row data
		List<ValueObject> reportRows = listOfBusiness.getReportRows();
		for (ValueObject aBook : reportRows) {
			table.addCell((String.valueOf( aBook.getField1())));
			table.addCell((String.valueOf( aBook.getField2())));
			table.addCell((String) aBook.getField3());
			table.addCell((String) aBook.getField4());
			table.addCell((String) aBook.getField5());
			table.addCell((String) aBook.getField6());
			table.addCell((String) aBook.getField7());
			table.addCell((String) aBook.getField8());
			table.addCell((String) aBook.getField9());
			table.addCell((String) aBook.getField10());
			
		}
		
		doc.add(table);
	}

	public void DistributionReport(Document doc, ReportForm listOfBusiness)throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] {3.0f, 2.0f, 3.0f,2.0f});
		table.setSpacingBefore(10);
		
		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		
		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setPadding(5);
		
		// write table header 
		cell.setPhrase(new Phrase("Brand", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("#ClientId", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Provider", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Last Submission", font));
		table.addCell(cell);
		
		
		// write table row data
		List<ValueObject> reportRows = listOfBusiness.getReportRows();
		for (ValueObject aBook : reportRows) {
			table.addCell((String) aBook.getField1());
			table.addCell((String.valueOf( aBook.getField2())));
			table.addCell((String) aBook.getField3());
			table.addCell((String) aBook.getField4());
		}
		
		doc.add(table);
		
	}

	public void ExportReport(Document doc, ReportForm listOfBusiness)throws DocumentException {
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] {4.0f, 3.0f, 3.0f,});
		table.setSpacingBefore(10);
		
		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		
		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setPadding(5);
		
		// write table header 
		cell.setPhrase(new Phrase("Brand", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("#Records", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Date", font));
		table.addCell(cell);
		
		
		// write table row data
		List<ValueObject> reportRows = listOfBusiness.getReportRows();
		for (ValueObject aBook : reportRows) {
			table.addCell((String) aBook.getField1());
			table.addCell((String.valueOf( aBook.getField2())));
			table.addCell((String) aBook.getField3());
			
		}
		
		doc.add(table);
		
	}

	public void uploadReport(Document doc, ReportForm listOfBusiness)throws DocumentException {
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] {4.0f, 3.0f, 3.0f,});
		table.setSpacingBefore(10);
		
		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		
		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setPadding(5);
		
		// write table header 
		cell.setPhrase(new Phrase("Brand", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("#Records", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Date", font));
		table.addCell(cell);
		
		
		// write table row data
		List<ValueObject> reportRows = listOfBusiness.getReportRows();
		for (ValueObject aBook : reportRows) {
			table.addCell((String) aBook.getField1());
			table.addCell((String.valueOf( aBook.getField2())));
			table.addCell((String) aBook.getField3());
			
		}
		
		doc.add(table);
		
	}

	public void RenewalReport(Document doc, ReportForm listOfBusiness) throws DocumentException {
		
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] {3.0f,2.0f, 3.0f,2.0f});
		table.setSpacingBefore(10);
		
		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		
		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setPadding(5);
		
		// write table header 
		cell.setPhrase(new Phrase("Brand", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("#Locations", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Renewal Date", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Partner", font));
		table.addCell(cell);
		
		
		// write table row data
		List<ValueObject> reportRows = listOfBusiness.getReportRows();
		for (ValueObject aBook : reportRows) {
			
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String text = df.format(aBook.getField3());
			
			table.addCell((String) aBook.getField1());
			table.addCell((String.valueOf( aBook.getField2())));
			table.addCell(text);
			table.addCell((String) aBook.getField4());
			
		}
		
		doc.add(table);
		
	}
		
	}