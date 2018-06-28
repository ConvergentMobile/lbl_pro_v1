package com.business.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.business.common.dto.MonthlyReportDTO;

public class GMBBrandMonthlyReport extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String reportName = (String) model.get("reportName");
		List<MonthlyReportDTO> insightsExcelData = (List<MonthlyReportDTO>) model
				.get("excelData");
		List<String> months = (List<String>) model.get("months");

		String reportType = (String) model.get("reportType");

		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ reportName + ".xls" + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");

		GenerateGMBInsightsExcelReport(workbook, insightsExcelData, months,
				reportName, reportType);

	}

	private void GenerateGMBInsightsExcelReport(HSSFWorkbook workbook,
			List<MonthlyReportDTO> insightsExcelData, List<String> months,
			String reportName, String reportType) {

		HSSFSheet sheet = workbook.createSheet("Total Searches");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeight((short) 280);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);

		HSSFRow header = sheet.createRow(0);

		if (reportType.equalsIgnoreCase("Location")) {
			header.createCell(0).setCellValue("Store");
		} else {
			header.createCell(0).setCellValue("State");
		}
		header.getCell(0).setCellStyle(style);

		int i = 1;

		for (int j = months.size() - 1; j >= 0; j--) {
			String month = months.get(j);
			header.createCell(i).setCellValue(month);
			header.getCell(i).setCellStyle(style);
			i++;
		}

		CellStyle styleValues = workbook.createCellStyle();
		Font fontValues = workbook.createFont();
		fontValues.setFontName("Calibri");
		fontValues.setFontHeight((short) 200);
		styleValues.setFont(fontValues);
		// create data rows
		int rowCount = 1;
		for (MonthlyReportDTO obj : insightsExcelData) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			if (reportType.equalsIgnoreCase("Location")) {
				aRow.createCell(0).setCellValue(obj.getStore());
			} else {
				aRow.createCell(0).setCellValue(obj.getState());
			}
			aRow.getCell(0).setCellStyle(styleValues);

			if (obj.getMonth1() != null) {
				String[] monthdetails = obj.getMonth1().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(1).setCellValue(searchCount);
				aRow.getCell(1).setCellStyle(styleValues);
			} else {
				aRow.createCell(1).setCellValue(new Long(0L));
				aRow.getCell(1).setCellStyle(styleValues);
			}
			if (obj.getMonth2() != null) {
				String[] monthdetails = obj.getMonth2().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(2).setCellValue(searchCount);
				aRow.getCell(2).setCellStyle(styleValues);
			} else {
				aRow.createCell(2).setCellValue(new Long(0L));
				aRow.getCell(2).setCellStyle(styleValues);
			}
			if (obj.getMonth3() != null) {
				String[] monthdetails = obj.getMonth3().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(3).setCellValue(searchCount);
				aRow.getCell(3).setCellStyle(styleValues);
			} else {
				aRow.createCell(3).setCellValue(new Long(0L));
				aRow.getCell(3).setCellStyle(styleValues);
			}
			if (obj.getMonth4() != null) {
				String[] monthdetails = obj.getMonth4().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(4).setCellValue(searchCount);
				aRow.getCell(4).setCellStyle(styleValues);
			} else {
				aRow.createCell(4).setCellValue(new Long(0L));
				aRow.getCell(4).setCellStyle(styleValues);
			}
			if (obj.getMonth5() != null) {
				String[] monthdetails = obj.getMonth5().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(5).setCellValue(searchCount);
				aRow.getCell(5).setCellStyle(styleValues);
			} else {
				aRow.createCell(5).setCellValue(new Long(0L));
				aRow.getCell(5).setCellStyle(styleValues);
			}

			if (obj.getMonth6() != null) {
				String[] monthdetails = obj.getMonth6().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(6).setCellValue(searchCount);
				aRow.getCell(6).setCellStyle(styleValues);
			} else {
				aRow.createCell(6).setCellValue(new Long(0L));
				aRow.getCell(6).setCellStyle(styleValues);
			}

			if (obj.getMonth7() != null) {
				String[] monthdetails = obj.getMonth7().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(7).setCellValue(searchCount);
				aRow.getCell(7).setCellStyle(styleValues);
			} else {
				aRow.createCell(7).setCellValue(new Long(0L));
				aRow.getCell(7).setCellStyle(styleValues);
			}
			if (obj.getMonth8() != null) {
				String[] monthdetails = obj.getMonth8().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(8).setCellValue(searchCount);
				aRow.getCell(8).setCellStyle(styleValues);
			} else {
				aRow.createCell(8).setCellValue(new Long(0L));
				aRow.getCell(8).setCellStyle(styleValues);
			}
			if (obj.getMonth9() != null) {
				String[] monthdetails = obj.getMonth9().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(9).setCellValue(searchCount);
				aRow.getCell(9).setCellStyle(styleValues);
			} else {
				aRow.createCell(9).setCellValue(new Long(0L));
				aRow.getCell(9).setCellStyle(styleValues);
			}

			if (obj.getMonth10() != null) {
				String[] monthdetails = obj.getMonth10().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(10).setCellValue(searchCount);
				aRow.getCell(10).setCellStyle(styleValues);
			} else {
				aRow.createCell(10).setCellValue(new Long(0L));
				aRow.getCell(10).setCellStyle(styleValues);
			}

			if (obj.getMonth11() != null) {
				String[] monthdetails = obj.getMonth11().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(11).setCellValue(searchCount);
				aRow.getCell(11).setCellStyle(styleValues);
			} else {
				aRow.createCell(11).setCellValue(new Long(0L));
				aRow.getCell(11).setCellStyle(styleValues);
			}

			if (obj.getMonth12() != null) {
				String[] monthdetails = obj.getMonth12().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[1]);
				aRow.createCell(12).setCellValue(searchCount);
				aRow.getCell(12).setCellStyle(styleValues);
			} else {
				aRow.createCell(12).setCellValue(new Long(0L));
				aRow.getCell(12).setCellStyle(styleValues);
			}

		}

		HSSFSheet sheetViews = workbook.createSheet("Total views");

		HSSFRow headerViews = sheetViews.createRow(0);

		if (reportType.equalsIgnoreCase("Location")) {
			headerViews.createCell(0).setCellValue("Store");
		} else {
			headerViews.createCell(0).setCellValue("State");
		}
		headerViews.getCell(0).setCellStyle(style);

		int a = 1;

		for (int j = months.size() - 1; j >= 0; j--) {
			String month = months.get(j);
			headerViews.createCell(a).setCellValue(month);
			headerViews.getCell(a).setCellStyle(style);
			a++;
		}

		// create data rows
		int rowCount1 = 1;
		for (MonthlyReportDTO obj : insightsExcelData) {
			HSSFRow aRow = sheetViews.createRow(rowCount1++);
			if (reportType.equalsIgnoreCase("Location")) {
				aRow.createCell(0).setCellValue(obj.getStore());
			} else {
				aRow.createCell(0).setCellValue(obj.getState());
			}
			aRow.getCell(0).setCellStyle(styleValues);

			if (obj.getMonth1() != null) {
				String[] monthdetails = obj.getMonth1().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(1).setCellValue(searchCount);
				aRow.getCell(1).setCellStyle(styleValues);
			} else {
				aRow.createCell(1).setCellValue(new Long(0L));
				aRow.getCell(1).setCellStyle(styleValues);
			}
			if (obj.getMonth2() != null) {
				String[] monthdetails = obj.getMonth2().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(2).setCellValue(searchCount);
				aRow.getCell(2).setCellStyle(styleValues);
			} else {
				aRow.createCell(2).setCellValue(new Long(0L));
				aRow.getCell(2).setCellStyle(styleValues);
			}
			if (obj.getMonth3() != null) {
				String[] monthdetails = obj.getMonth3().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(3).setCellValue(searchCount);
				aRow.getCell(3).setCellStyle(styleValues);
			} else {
				aRow.createCell(3).setCellValue(new Long(0L));
				aRow.getCell(3).setCellStyle(styleValues);
			}
			if (obj.getMonth4() != null) {
				String[] monthdetails = obj.getMonth4().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(4).setCellValue(searchCount);
				aRow.getCell(4).setCellStyle(styleValues);
			} else {
				aRow.createCell(4).setCellValue(new Long(0L));
				aRow.getCell(4).setCellStyle(styleValues);
			}
			if (obj.getMonth5() != null) {
				String[] monthdetails = obj.getMonth5().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(5).setCellValue(searchCount);
				aRow.getCell(5).setCellStyle(styleValues);
			} else {
				aRow.createCell(5).setCellValue(new Long(0L));
				aRow.getCell(5).setCellStyle(styleValues);
			}

			if (obj.getMonth6() != null) {
				String[] monthdetails = obj.getMonth6().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(6).setCellValue(searchCount);
				aRow.getCell(6).setCellStyle(styleValues);
			} else {
				aRow.createCell(6).setCellValue(new Long(0L));
				aRow.getCell(6).setCellStyle(styleValues);
			}

			if (obj.getMonth7() != null) {
				String[] monthdetails = obj.getMonth7().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(7).setCellValue(searchCount);
				aRow.getCell(7).setCellStyle(styleValues);
			} else {
				aRow.createCell(7).setCellValue(new Long(0L));
				aRow.getCell(7).setCellStyle(styleValues);
			}
			if (obj.getMonth8() != null) {
				String[] monthdetails = obj.getMonth8().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(8).setCellValue(searchCount);
				aRow.getCell(8).setCellStyle(styleValues);
			} else {
				aRow.createCell(8).setCellValue(new Long(0L));
				aRow.getCell(8).setCellStyle(styleValues);
			}
			if (obj.getMonth9() != null) {
				String[] monthdetails = obj.getMonth9().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(9).setCellValue(searchCount);
				aRow.getCell(9).setCellStyle(styleValues);
			} else {
				aRow.createCell(9).setCellValue(new Long(0L));
				aRow.getCell(9).setCellStyle(styleValues);
			}

			if (obj.getMonth10() != null) {
				String[] monthdetails = obj.getMonth10().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(10).setCellValue(searchCount);
				aRow.getCell(10).setCellStyle(styleValues);
			} else {
				aRow.createCell(10).setCellValue(new Long(0L));
				aRow.getCell(10).setCellStyle(styleValues);
			}

			if (obj.getMonth11() != null) {
				String[] monthdetails = obj.getMonth11().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(11).setCellValue(searchCount);
				aRow.getCell(11).setCellStyle(styleValues);
			} else {
				aRow.createCell(11).setCellValue(new Long(0L));
				aRow.getCell(11).setCellStyle(styleValues);
			}

			if (obj.getMonth12() != null) {
				String[] monthdetails = obj.getMonth12().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[2]);
				aRow.createCell(12).setCellValue(searchCount);
				aRow.getCell(12).setCellStyle(styleValues);
			} else {
				aRow.createCell(12).setCellValue(new Long(0L));
				aRow.getCell(12).setCellStyle(styleValues);
			}

		}

		HSSFSheet sheetViews2 = workbook.createSheet("Total Actions");
		HSSFRow headerViews2 = sheetViews2.createRow(0);

		if (reportType.equalsIgnoreCase("Location")) {
			headerViews2.createCell(0).setCellValue("Store");
		} else {
			headerViews2.createCell(0).setCellValue("State");
		}
		headerViews2.getCell(0).setCellStyle(style);

		int b = 1;

		for (int j = months.size() - 1; j >= 0; j--) {
			String month = months.get(j);
			headerViews2.createCell(b).setCellValue(month);
			headerViews2.getCell(b).setCellStyle(style);
			b++;
		}

		// create data rows
		int rowCount2 = 1;
		for (MonthlyReportDTO obj : insightsExcelData) {
			HSSFRow aRow = sheetViews2.createRow(rowCount2++);
			if (reportType.equalsIgnoreCase("Location")) {
				aRow.createCell(0).setCellValue(obj.getStore());
			} else {
				aRow.createCell(0).setCellValue(obj.getState());
			}
			aRow.getCell(0).setCellStyle(styleValues);

			if (obj.getMonth1() != null) {
				String[] monthdetails = obj.getMonth1().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(1).setCellValue(searchCount);
				aRow.getCell(1).setCellStyle(styleValues);
			} else {
				aRow.createCell(1).setCellValue(new Long(0L));
				aRow.getCell(1).setCellStyle(styleValues);
			}
			if (obj.getMonth2() != null) {
				String[] monthdetails = obj.getMonth2().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(2).setCellValue(searchCount);
				aRow.getCell(2).setCellStyle(styleValues);
			} else {
				aRow.createCell(2).setCellValue(new Long(0L));
				aRow.getCell(2).setCellStyle(styleValues);
			}
			if (obj.getMonth3() != null) {
				String[] monthdetails = obj.getMonth3().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(3).setCellValue(searchCount);
				aRow.getCell(3).setCellStyle(styleValues);
			} else {
				aRow.createCell(3).setCellValue(new Long(0L));
				aRow.getCell(3).setCellStyle(styleValues);
			}
			if (obj.getMonth4() != null) {
				String[] monthdetails = obj.getMonth4().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(4).setCellValue(searchCount);
				aRow.getCell(4).setCellStyle(styleValues);
			} else {
				aRow.createCell(4).setCellValue(new Long(0L));
				aRow.getCell(4).setCellStyle(styleValues);
			}
			if (obj.getMonth5() != null) {
				String[] monthdetails = obj.getMonth5().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(5).setCellValue(searchCount);
				aRow.getCell(5).setCellStyle(styleValues);
			} else {
				aRow.createCell(5).setCellValue(new Long(0L));
				aRow.getCell(5).setCellStyle(styleValues);
			}

			if (obj.getMonth6() != null) {
				String[] monthdetails = obj.getMonth6().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(6).setCellValue(searchCount);
				aRow.getCell(6).setCellStyle(styleValues);
			} else {
				aRow.createCell(6).setCellValue(new Long(0L));
				aRow.getCell(6).setCellStyle(styleValues);
			}

			if (obj.getMonth7() != null) {
				String[] monthdetails = obj.getMonth7().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(7).setCellValue(searchCount);
				aRow.getCell(7).setCellStyle(styleValues);
			} else {
				aRow.createCell(7).setCellValue(new Long(0L));
				aRow.getCell(7).setCellStyle(styleValues);
			}
			if (obj.getMonth8() != null) {
				String[] monthdetails = obj.getMonth8().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(8).setCellValue(searchCount);
				aRow.getCell(8).setCellStyle(styleValues);
			} else {
				aRow.createCell(8).setCellValue(new Long(0L));
				aRow.getCell(8).setCellStyle(styleValues);
			}
			if (obj.getMonth9() != null) {
				String[] monthdetails = obj.getMonth9().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(9).setCellValue(searchCount);
				aRow.getCell(9).setCellStyle(styleValues);
			} else {
				aRow.createCell(9).setCellValue(new Long(0L));
				aRow.getCell(9).setCellStyle(styleValues);
			}

			if (obj.getMonth10() != null) {
				String[] monthdetails = obj.getMonth10().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(10).setCellValue(searchCount);
				aRow.getCell(10).setCellStyle(styleValues);
			} else {
				aRow.createCell(10).setCellValue(new Long(0L));
				aRow.getCell(10).setCellStyle(styleValues);
			}

			if (obj.getMonth11() != null) {
				String[] monthdetails = obj.getMonth11().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(11).setCellValue(searchCount);
				aRow.getCell(11).setCellStyle(styleValues);
			} else {
				aRow.createCell(11).setCellValue(new Long(0L));
				aRow.getCell(11).setCellStyle(styleValues);
			}

			if (obj.getMonth12() != null) {
				String[] monthdetails = obj.getMonth12().split("\\|");
				Long searchCount = Long.valueOf(monthdetails[3]);
				aRow.createCell(12).setCellValue(searchCount);
				aRow.getCell(12).setCellStyle(styleValues);
			} else {
				aRow.createCell(12).setCellValue(new Long(0L));
				aRow.getCell(12).setCellStyle(styleValues);
			}

		}

	}

	public GMBBrandMonthlyReport() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String s = "Mar-2017|10|0";
		String[] split = s.split("\\|");
		System.out.println(split[1]);
	}

}
