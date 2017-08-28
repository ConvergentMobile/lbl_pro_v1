package com.business.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.business.common.dto.BingApiResponseDTO;
import com.business.service.BingService;
import com.business.web.bean.UploadBusinessBean;

public class BingThread extends Thread {

	public Logger logger = Logger.getLogger(ScrappingThread.class);

	BingService service = null;

	BingClient cleint = null;
	List<UploadBusinessBean> stores;

	public BingThread(BingService service, List<UploadBusinessBean> listOfStores) {
		this.service = service;
		this.stores = listOfStores;

	}

	public void run() {
		logger.info("start ::  BingThread  ");
		cleint = new BingClient(service);
		try {

			List<BingApiResponseDTO> bingApiResposne = BingClient.addorUpdateStoresOnBing(stores);
			writeToExcelandSendMail(bingApiResposne);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end ::  BingThread  ");
	}

	public void writeToExcelandSendMail(List<BingApiResponseDTO> bingApiResposne) {

		String mail = LBLConstants.ALERT_MAIL_ID;
		String pathToReport = LBLConstants.REPORT_PATH;

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("Bing_Response");

		int rowCount = 0;
		createHeaderRow(sheet);
		for (BingApiResponseDTO apiRespose : bingApiResposne) {

			Row row = sheet.createRow(++rowCount);
			writeBook(apiRespose, row);
		}
		File file = new File(pathToReport);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try (FileOutputStream outputStream = new FileOutputStream(pathToReport)) {
			workbook.write(outputStream);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("Bing API Report is Sending to Mail: " + mail);
			MailUtil.sendEmail(mail, pathToReport, "Bing API Report", "Bing API Response file attached");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createHeaderRow(Sheet sheet) {

		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();

		Font font = sheet.getWorkbook().createFont();

		font.setFontName("Calibri");
		font.setFontHeight((short) 280);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);

		Row row = sheet.createRow(0);
		Cell cellTitle = row.createCell(0);

		cellTitle.setCellStyle(cellStyle);
		cellTitle.setCellValue("ClientId");

		Cell cellAuthor = row.createCell(1);
		cellAuthor.setCellStyle(cellStyle);
		cellAuthor.setCellValue("Store");

		Cell cellPrice = row.createCell(2);
		cellPrice.setCellStyle(cellStyle);
		cellPrice.setCellValue("Operation");

		Cell status = row.createCell(3);
		status.setCellStyle(cellStyle);
		status.setCellValue("Status");

		Cell error = row.createCell(4);
		error.setCellStyle(cellStyle);
		error.setCellValue("ErrorMessage");

	}

	private void writeBook(BingApiResponseDTO response, Row row) {

		
		Integer clientId = response.getClientId();
		if (clientId != null) {
			Cell cell = row.createCell(0);
			cell.setCellValue(clientId);

			cell = row.createCell(1);
			cell.setCellValue(response.getStore());

			cell = row.createCell(2);
			cell.setCellValue(response.getOperation());

			cell = row.createCell(3);
			cell.setCellValue(response.getStatus());

			cell = row.createCell(4);
			cell.setCellValue(response.getErrorMessage());
		}
	}
}
