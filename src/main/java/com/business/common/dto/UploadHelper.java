package com.business.common.dto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVReader;

import com.business.common.util.LBLConstants;
import com.business.web.bean.UploadBusinessBean;

public class UploadHelper {
	Logger logger = Logger.getLogger(UploadHelper.class);

	public boolean headersValid(Row row, DataFormatter df) {
		Iterator<Cell> cellIterator = row.cellIterator();
		boolean isHeadersValid = true;
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if (cell == null
					|| (!df.formatCellValue(cell).equalsIgnoreCase(
							LBLConstants.UPLOAD_EXCEL_HEADERS[cell
									.getColumnIndex()]))) {
				isHeadersValid = false;
				break;
			}
		}
		return isHeadersValid;
	}

	private boolean isheaderValid(MultipartFile file) {
		boolean isHeadersValid = true;
		CSVParser parser;
		try {
			InputStream inputStream = file.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			parser = new CSVParser(bufferedReader,
					CSVFormat.DEFAULT.withHeader());
			Map<String, Integer> headerMap = parser.getHeaderMap();

			List<String> list = new ArrayList<String>();
			for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {

				list.add(entry.getKey());

			}
			for (int i = 0; i < list.size(); i++) {

				String anotherString = LBLConstants.UPLOAD_EXCEL_HEADERS[i];

				if (!list.get(i).equalsIgnoreCase(anotherString)) {
					isHeadersValid = false;
					break;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return isHeadersValid;
	}

	public boolean isRowEmpty(Row row) {
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
				return false;
		}
		return true;
	}

	public List<UploadBusinessBean> getListFromCSV(MultipartFile file) {
		List<UploadBusinessBean> tListData = new ArrayList<UploadBusinessBean>();
		boolean value = isheaderValid(file);

		if (!value) {
			return tListData;

		}
		InputStream inputStream = null;
		try {

			inputStream = file.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			CSVReader reader = new CSVReader(bufferedReader, ',', '"', 1);

			String[] nextLine;

			while ((nextLine = reader.readNext()) != null) {

				UploadBusinessBean uploadBusinessBean = new UploadBusinessBean();
				if (nextLine != null) {

					for (int i = 0; i < nextLine.length; i++) {

						try {

							org.apache.commons.beanutils.BeanUtils.setProperty(
									uploadBusinessBean,
									LBLConstants.UPLOAD_BEAN_PROPERTIES[i],
									nextLine[i]);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				}
				tListData.add(uploadBusinessBean);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		logger.info("Excel CSV  sheet Records size == " + tListData.size());
		logger.info(tListData);
		return tListData;
	}

	public List<UploadBusinessBean> getListDataFromXLS(
			MultipartFile inputFile) {

		List<UploadBusinessBean> tListData = new ArrayList<UploadBusinessBean>();
		try {
			Sheet sheet = (Sheet) WorkbookFactory.create(
					inputFile.getInputStream()).getSheetAt(0);
			DataFormatter df = new DataFormatter();
			Iterator<Row> rowIterator = sheet.rowIterator();
			boolean header = true;

			while (rowIterator.hasNext()) {
				Row row = (Row) rowIterator.next();
				boolean rowEmpty = isRowEmpty(row);
				if (rowEmpty == false) {
					if (header) {
						header = false;
						if (!headersValid(row, df)) {
							return tListData;
						}
						continue;
					}

					UploadBusinessBean uploadBusinessBean = new UploadBusinessBean();

					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {

						Cell cell = cellIterator.next();
						if (cell == null) // Process only the rows with some
											// data
							continue;

						int columnIndex = cell.getColumnIndex();
						// System.out.println("columnIndex" + columnIndex);
						String formatCellValue = df.formatCellValue(cell);

						// System.out.println("formatCellValue" +
						// formatCellValue);
						org.apache.commons.beanutils.BeanUtils
								.setProperty(
										uploadBusinessBean,
										LBLConstants.UPLOAD_BEAN_PROPERTIES[columnIndex],
										formatCellValue);

					}

					tListData.add(uploadBusinessBean);
				}

			}
		} catch (Exception e) {
			logger.error("Exception : " + e);
			e.printStackTrace();
		}

		logger.info("Excel sheet Records size == " + tListData.size());
		logger.info(tListData);
		return tListData;
	}

}
