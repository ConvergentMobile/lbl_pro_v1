package com.business.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.business.common.util.LBLConstants;
import com.business.service.CategoryService;
import com.business.web.bean.CategoryBean;

@Controller
public class CategoryConversionController {
	
	Logger logger = Logger.getLogger(CategoryConversionController.class);
	
	@Autowired
	private CategoryService categoryService;
	
	
	
	@RequestMapping(value="/catgoryBusiness.htm",method=RequestMethod.POST)
	public String uploadBusinessInformation(
			@ModelAttribute("CatgoryBusiness") CategoryBean  bean,
			Model model, HttpServletRequest request, HttpSession session,
			HSSFWorkbook workbook, HttpServletResponse response) {
		
		//String pageName = request.getParameter("page");

		//Integer role = (Integer) session.getAttribute("roleId");

		CommonsMultipartFile file = bean.getTargetFile();
		logger.info("File Name == " + file.getOriginalFilename());
		String fName = file.getOriginalFilename();
		logger.info("File Name : " + fName);
		
		Map<String, Long> brandsCountsMap = new HashMap<String, Long>();
		String headerpopup = "";
		List<CategoryBean> listDataFromXLS = getListDataFromXLS(file,
				brandsCountsMap, headerpopup);
		if(listDataFromXLS!=null && listDataFromXLS.size()>0){
			categoryService.saveCategoryInfo(listDataFromXLS);
		}
				
	
		return "upload-export";
		
	}

	private List<CategoryBean> getListDataFromXLS(
			CommonsMultipartFile file, Map<String, Long> brandsCountsMap,
			String headerpopup) {
		List<CategoryBean> tListData = new ArrayList<CategoryBean>();
		try {
			Sheet sheet = (Sheet) WorkbookFactory.create(
					file.getInputStream()).getSheetAt(0);
			DataFormatter df = new DataFormatter();
			Iterator<Row> rowIterator = sheet.rowIterator();
			boolean header = true;
			while (rowIterator.hasNext()) {
				Row row = (Row) rowIterator.next();
				if (header) {
					header = false;
					headerpopup = "";
					if (!headersValid(row, df)) {
						headerpopup = "Invalid Bulk Upload Template";
						return tListData;
					}
					continue;
				}

				CategoryBean uploadBusinessBean = new CategoryBean();

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();
					if (cell == null) // Process only the rows with some data
						continue;
					
					logger.info("row index: "+ row.getRowNum());
					
					logger.info("cell index " +cell
									.getColumnIndex());
					
					logger.info("cell style"+cell.getCellType());				

					org.apache.commons.beanutils.BeanUtils.setProperty(
							uploadBusinessBean,
							LBLConstants.UPLOAD_CATEGORY_PROPERTIES[cell
									.getColumnIndex()], df
									.formatCellValue(cell));

				}
				
				tListData.add(uploadBusinessBean);
			}
		} catch (Exception e) {
			logger.error("Exception : " + e);
			e.printStackTrace();
		}

		logger.info("Excel sheet Records size == " + tListData.size());
		logger.info(tListData);
		return tListData;
	}
	
	/**
	 * headersValidation
	 * 
	 * @param row
	 * @param df
	 * @return
	 */
	public boolean headersValid(Row row, DataFormatter df) {
		Iterator<Cell> cellIterator = row.cellIterator();
		boolean isHeadersValid = true;
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if (cell == null
					|| (!df.formatCellValue(cell).equalsIgnoreCase(
							LBLConstants.UPLOAD_CATGORY_HEADERS[cell
									.getColumnIndex()]))) {
				isHeadersValid = false;
				break;
			}
		}
		return isHeadersValid;
	}

}
