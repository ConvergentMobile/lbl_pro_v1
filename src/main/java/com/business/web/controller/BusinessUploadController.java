package com.business.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import au.com.bytecode.opencsv.CSVReader;

import com.business.common.dto.ExportReportDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.UploadReportDTO;
import com.business.common.dto.UsersDTO;
import com.business.common.util.ControllerUtil;
import com.business.common.util.LBLConstants;
import com.business.common.util.SmartyStreetsThread;
import com.business.common.util.UploadBeanValidateUtil;
import com.business.common.util.UploadDataAddExecutorService;
import com.business.common.util.UploadDataErrorsAddExecutorService;
import com.business.common.util.UploadDataUpdateExecutorService;
import com.business.common.util.ValidationExecutorService;
import com.business.service.BusinessService;
import com.business.web.bean.BussinessData;
import com.business.web.bean.LblErrorBean;
import com.business.web.bean.LocalBusinessBean;
import com.business.web.bean.UploadBusinessBean;
import com.business.web.bean.UsersBean;

/**
 * 
 * @author Vasanth
 * 
 *         BusinessUploadController class which gets the data from the View
 *         Layer and sends to the Business Logic Layer and returns respective
 *         results to View Layer
 * 
 * 
 * 
 */

@Controller
public class BusinessUploadController {

	public int totalDataCount;
	private ControllerUtil controllerUtil = new ControllerUtil();

	Logger logger = Logger.getLogger(BusinessUploadController.class);

	Set<LocalBusinessDTO> duplicateRecord = new HashSet<LocalBusinessDTO>();
	List<LocalBusinessDTO> updateBusinessRecords = null;
	List<LocalBusinessDTO> listofDeletesbyActionCode = null;

	List<LblErrorDTO> listOfErrorUpdatesByActionCode = null;
	List<LblErrorDTO> listofErrorDeletesbyActionCode = null;

	Set<LblErrorDTO> incorrectDatas = new HashSet<LblErrorDTO>();
	ResourceBundle bundle = ResourceBundle.getBundle("messages");
	String fileName = bundle.getString("businessApp.lable.reportDirectory");

	@Autowired
	private BusinessService service;

	/***
	 * 
	 * This method returns to uplodbusiness page
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/uploadBusiness.htm", method = RequestMethod.GET)
	public String uploadBusinessInformationRequest(Model model) {
		logger.info("start :: uploadBusinessInformationRequest method");
		model.addAttribute("uploadBusiness", new LocalBusinessBean());
		logger.info("end :: uploadBusinessInformationRequest method");
		return "uploadBusinessInfo";
	}

	/***
	 * 
	 * This method validate the records in the excel sheet and with database and
	 * uploads the excel sheet data to database and returns to list page
	 * 
	 * @param bean
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/uploadBusiness.htm", method = RequestMethod.POST)
	public String uploadBusinessInformation(
			@ModelAttribute("uploadBusiness") LocalBusinessBean bean,
			Model model, HttpServletRequest request, HttpSession session,
			HSSFWorkbook workbook, HttpServletResponse response) {

		logger.info("start :: uploadBusinessInformation method");
		String pageName = request.getParameter("page");

		Integer role = (Integer) session.getAttribute("roleId");

		CommonsMultipartFile file = bean.getTargetFile();
		logger.info("File Name == " + file.getOriginalFilename());
		String fName = file.getOriginalFilename();
		logger.info("File Name : " + fName);

		Map<String, Long> brandsCountsMap = new HashMap<String, Long>();
		String headerpopup = "";
		List<UploadBusinessBean> listDataFromXLS = null;
		if (!fName.contains(".csv")) {
			listDataFromXLS = getListDataFromXLS(file, brandsCountsMap,
					headerpopup);
		} else {
			listDataFromXLS = getListFromCSV(file, brandsCountsMap, headerpopup);
		}

		boolean isClientIdExists = true;
		Set<Integer> clients = new HashSet<Integer>();
		for (UploadBusinessBean business : listDataFromXLS) {
			Integer clientId = business.getClientId();
			clients.add(clientId);
		}

		if (clients != null) {
			for (Integer clientId : clients) {
				String brandByClientId = service.getBrandByClientId(clientId);
				if (brandByClientId == null) {
					logger.error("Client does not exists in LBL: " + clientId);
					isClientIdExists = false;
					break;
				}
			}
		}

		String message = "";
		int correctDataCount = 0;
		int inCorrectDataCount = 0;

		headerpopup = "Invalid Bulk Upload Template";
		if (isClientIdExists == false) {
			headerpopup = "Client Does Not Exist. Contact Account Manager for Assistance";
			listDataFromXLS = null;
		}
		Integer locations = 0;
		Integer invoicedlocations = 0;
		for (Integer clientId : clients) {
			locations = locations
					+ service.getCountForBrand(clientId, listDataFromXLS);
			invoicedlocations = invoicedlocations
					+ service.getlocationInvoicedByClient(clientId);
		}

		if (locations.intValue() > invoicedlocations.intValue()) {
			headerpopup = "This upload will exceed the number of locations included in your current contract. Please contact your Sales Rep to increase the number of locations allowed for upload.";
			listDataFromXLS = null;

		}

		boolean isStoreDuplicate = false;
		if (listDataFromXLS != null) {
			isStoreDuplicate = isStoreDuplicateInExcel(listDataFromXLS);
		}
		if (isStoreDuplicate == true) {
			headerpopup = "Attempting to upload Duplicated Locations. Please check your file and try again.";
			listDataFromXLS = null;
		}

		if (listDataFromXLS != null && listDataFromXLS.size() > 0) {
			message = "Upload complete, results can be seen in Business Listings.";

			headerpopup = "";
			totalDataCount = listDataFromXLS.size();

			try {
				ValidationExecutorService.executeService(service,
						listDataFromXLS);
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}

			List<UploadBusinessBean> validBusinessList = ValidationExecutorService.validBusinessList;
			List<LblErrorBean> erroredBusinessList = ValidationExecutorService.erroredBusinessList;

			logger.info("Toatal valid records found are: "
					+ validBusinessList.size());
			logger.info("Toatal Errored records found are: "
					+ erroredBusinessList);

			List<LocalBusinessDTO> correctDatas = new ArrayList<LocalBusinessDTO>();
			List<LblErrorDTO> inCorrectData = new ArrayList<LblErrorDTO>();

			LocalBusinessDTO businessDTO = null;
			for (UploadBusinessBean uploadBusinessBean : validBusinessList) {
				businessDTO = new LocalBusinessDTO();
				BeanUtils.copyProperties(uploadBusinessBean, businessDTO);
				correctDatas.add(businessDTO);
			}
			LblErrorDTO errorDTO = null;
			for (LblErrorBean errorBean : erroredBusinessList) {
				errorDTO = new LblErrorDTO();
				BeanUtils.copyProperties(errorBean, errorDTO);
				inCorrectData.add(errorDTO);
			}

			List<LocalBusinessDTO> correctRecords = new ArrayList<LocalBusinessDTO>();

			correctRecords = checkRecordsInListings(correctDatas,
					brandsCountsMap);

			for (LocalBusinessDTO localBusinessDTO : correctRecords) {
				Long recordsCount = Long.valueOf(1);
				String brandName = localBusinessDTO.getClient();
				if (brandsCountsMap.containsKey(brandName)) {
					recordsCount = brandsCountsMap.get(brandName) + 1;
				}
				brandsCountsMap.put(brandName, recordsCount);
			}

			List<LblErrorDTO> inCorrectDataList = checkRecordsInListingErrors(
					inCorrectData, brandsCountsMap);

			for (LblErrorDTO lblErrorDTO : inCorrectDataList) {
				Long recordsCount = Long.valueOf(1);
				String brandName = lblErrorDTO.getClient();

				if (brandsCountsMap.containsKey(brandName)) {
					recordsCount = brandsCountsMap.get(brandName) + 1;
				}
				brandsCountsMap.put(brandName, recordsCount);
			}

			List<LblErrorDTO> correctDBErrorRecords = correctDBErrorRecords(correctDatas);

			Date date = new Date();
			String uploadUserName = (String) session.getAttribute("userName");
			String uploadJobId = uploadUserName + "_"
					+ System.currentTimeMillis();
			session.setAttribute("uploadJobId", uploadJobId);

			logger.info("Start: Add listings");
			UploadDataAddExecutorService uploadAddService = new UploadDataAddExecutorService();
			try {
				uploadAddService.addBusinessList(correctRecords, date,
						uploadJobId, service);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("End: Add listings");

			logger.info("Start: update listings");
			// String userName = (String) session.getAttribute("userName");
			UploadDataUpdateExecutorService uploadUpdateService = new UploadDataUpdateExecutorService();
			try {
				uploadUpdateService.updateBusinessList(updateBusinessRecords,
						date, uploadJobId, service, uploadUserName);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			logger.info("End: update listings");

			logger.info("reomve Correct ErrorData from business error ::"
					+ correctDBErrorRecords);
			// deleteBusinessByActionCode in business table
			service.deleteBusinessByActionCode(listofDeletesbyActionCode);

			// remove the CorrectErrorData from business table
			service.reomveCorrectErrorData(correctDBErrorRecords);

			logger.info("Start: Add  Error listings");
			UploadDataErrorsAddExecutorService errorService = new UploadDataErrorsAddExecutorService();

			try {
				errorService.insertErrorBusiness(inCorrectDataList, date,
						uploadJobId, service);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("End: Add  Error listings");

			// insert error records to error table
			// service.insertErrorBusiness(inCorrectDataList, date,
			// uploadJobId);

			// updateErrorBusinessByActionCode in business error table
			service.updateErrorBusinessByActionCode(
					listOfErrorUpdatesByActionCode, date, uploadJobId);

			// deleteErrorBusinessByActioncode in business error table
			service.deleteErrorBusinessByActioncode(listofErrorDeletesbyActionCode);

			SmartyStreetsThread st = new SmartyStreetsThread(service,
					correctRecords, updateBusinessRecords, inCorrectDataList,
					listOfErrorUpdatesByActionCode);
			st.start();

			logger.info("Total Records: " + totalDataCount);
			logger.info("Total Corect Records: " + correctRecords.size());
			logger.info("Total Updated Records: "
					+ updateBusinessRecords.size());
			logger.info("Total Deleted Records: "
					+ listofDeletesbyActionCode.size());

			logger.info("Total Error Records: " + inCorrectDataList.size());
			logger.info("Total error updated Records: "
					+ listOfErrorUpdatesByActionCode.size());
			logger.info("Total error deleted Records: "
					+ listofErrorDeletesbyActionCode.size());

			if (correctRecords != null) {
				correctDataCount = correctRecords.size();
				int updateSize = 0;
				if (updateBusinessRecords != null) {
					updateSize = updateBusinessRecords.size();
				}
				int deleteSize = 0;
				if (listofDeletesbyActionCode != null) {
					deleteSize = listofDeletesbyActionCode.size();
				}
				correctDataCount = correctDataCount + updateSize - deleteSize;
				if (correctDataCount < 0) {
					correctDataCount = 0;
				}
			}
			if (inCorrectDataList != null) {
				inCorrectDataCount = inCorrectDataList.size();
				int updateSize = 0;
				if (listOfErrorUpdatesByActionCode != null) {
					updateSize = listOfErrorUpdatesByActionCode.size();
				}
				int deleteSize = 0;
				if (listofErrorDeletesbyActionCode != null) {
					deleteSize = listofErrorDeletesbyActionCode.size();
				}
				inCorrectDataCount = inCorrectDataCount + updateSize
						- deleteSize;
				if (inCorrectDataCount < 0) {
					inCorrectDataCount = 0;
				}
			}
			if (correctRecords.size() > 0 || inCorrectDataList.size() > 0) {
				Set<String> brandNames = brandsCountsMap.keySet();
				for (String brand : brandNames) {
					UploadReportDTO uploadReportDTO = new UploadReportDTO();
					uploadReportDTO.setDate(date);
					uploadReportDTO.setNumberOfRecords(String
							.valueOf(brandsCountsMap.get(brand)));
					String userName = (String) request.getSession()
							.getAttribute("userName");
					uploadReportDTO.setUserName(userName);
					uploadReportDTO.setBrand(brand);
					boolean uploadReportInfo = service
							.uploadReportDTO(uploadReportDTO);
				}
			}
			model.addAttribute("showPopup", "showMessage");
		}
		model.addAttribute("headerPopup", headerpopup);

		try {
			response.getWriter().write(headerpopup);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		model.addAttribute("allBusinessInfo", listOfBusinessInfo);
		model.addAttribute("searchBusiness", new LocalBusinessDTO());
		logger.info("Total Data Count == " + totalDataCount);
		logger.info("Correct Data Count == " + correctDataCount);
		logger.info("Incorrect Data Count == " + inCorrectDataCount);
		model.addAttribute("listingsUploaded", totalDataCount);
		model.addAttribute("listingsCompleted", correctDataCount);
		model.addAttribute("listingsWithErrors", inCorrectDataCount);
		model.addAttribute("message", message);

		duplicateRecord.clear();
		// return "allBusinessInfo";
		model.addAttribute("uploadBusiness", new LocalBusinessBean());
		model.addAttribute("businessSearch", new LocalBusinessDTO());
		if (pageName.equals("businessListings")) {
			session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
			controllerUtil.listingsAddAttributes(1, model, request,
					listOfBusinessInfo);
			List<LblErrorDTO> listOfErrorBusinessInfo = service
					.getListOfErrors();
			int activeListSize = listOfBusinessInfo.size();
			int errorListSize = listOfErrorBusinessInfo.size();
			model.addAttribute("errorListSize", errorListSize);
			model.addAttribute("activeListSize", activeListSize);
			logger.info("end :: uploadBusinessInformation method");
			return "business-listings";
		} else if (pageName.equals("dashboard")) {
			List<LocalBusinessDTO> listOfBrandsInfo = service.getListOfBrands();

			List<ExportReportDTO> listingActivityInfo = service
					.getListingActivityInf();
			Collections.sort(listingActivityInfo);
			List<UsersDTO> usersList = service.getAllUsersList((Integer) role);
			model.addAttribute("usersListInfo", usersList);
			model.addAttribute("brandSize", listOfBusinessInfo.size());
			model.addAttribute("listingActivityInfo", listingActivityInfo);
			model.addAttribute("brandsInfo", listOfBrandsInfo);
			logger.info("end :: uploadBusinessInformation method");
			LocalBusinessDTO dto = new LocalBusinessDTO();
			dashBoardCommonInfo(model, session, dto);
			return "dashboard";
		} else if (pageName.equals("uploadExport")) {
			logger.info("end :: uploadBusinessInformation method");
			return "upload-export";
		} else if (pageName.equals("dashboardclient")) {
			List<LocalBusinessDTO> listOfBusinessInfo1 = service
					.getListOfBusinessInfo();
			Integer userId = (Integer) session.getAttribute("userID");
			List<UsersDTO> userInfo = service.userInfo(userId);
			UsersDTO usersDTO = userInfo.get(0);
			UsersBean bean1 = new UsersBean();
			BeanUtils.copyProperties(usersDTO, bean1);
			List<ExportReportDTO> listingActivityInfo = service
					.getListingActivityInf();
			Collections.sort(listingActivityInfo);
			model.addAttribute("adminUser", bean1);
			model.addAttribute("listingActivityInfo", listingActivityInfo);
			session.setAttribute("listOfBusinessInfo", listOfBusinessInfo1);
			controllerUtil.listingsAddAttributes(1, model, request,
					listOfBusinessInfo1);
			logger.info("end :: uploadBusinessInformation method");
			return "dashboard-client";
		}
		session.setAttribute("listOfBusinessInfo", listOfBusinessInfo);
		List<LblErrorDTO> listOfErrorBusinessInfo = service.getListOfErrors();
		int activeListSize = listOfBusinessInfo.size();
		int errorListSize = listOfErrorBusinessInfo.size();
		model.addAttribute("errorListSize", errorListSize);
		model.addAttribute("activeListSize", activeListSize);
		controllerUtil.listingsAddAttributes(1, model, request,
				listOfBusinessInfo);
		logger.info("end :: uploadBusinessInformation method");
		return "business-listings";
	}

	private void dashBoardCommonInfo(Model model, HttpSession session,
			LocalBusinessDTO dto) {
		Integer userId = (Integer) session.getAttribute("userID");
		List<UsersDTO> userInfo = service.userInfo(userId);
		UsersDTO usersDTO = userInfo.get(0);
		String name = usersDTO.getName();
		String lastName = usersDTO.getLastName();
		String fullName = name + " " + lastName;
		usersDTO.setFullName(fullName);
		UsersBean bean = new UsersBean();
		BeanUtils.copyProperties(usersDTO, bean);
		model.addAttribute("adminUser", bean);
		model.addAttribute("searchBusiness", dto);

	}

	/***
	 * this method check's the data in excel sheet and returns correct data to
	 * checkbusinessinfo method
	 * 
	 * 
	 * @param listDataFromXLS
	 * @return
	 * @return
	 */
	public BussinessData checkBusinessInfoFormat(
			List<UploadBusinessBean> listDataFromXLS) {

		UploadBeanValidateUtil validateUtil = new UploadBeanValidateUtil();
		return validateUtil.validateBusiness(service, listDataFromXLS);
	}

	/***
	 * This method validate the email format in excel sheet
	 * 
	 * @param id
	 * @return
	 */

	public boolean checkEmailFormat(String id) {
		Pattern pattern = Pattern
				.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(id);
		return matcher.matches();
	}

	/***
	 * this method validate the hours of operation
	 * 
	 * @param time
	 * @return
	 */

	public boolean isHours(String time) {
		Pattern pattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
		Matcher matcher = pattern.matcher(time);
		return matcher.matches();
	}

	public boolean isPhoneNumber(String phoneNumber) {
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(phoneNumber);
		return m.matches();

	}

	/***
	 * 
	 * this method skip the duplicate records in the excel sheet and returns the
	 * records
	 * 
	 * 
	 * @param records
	 * @return
	 */
	public boolean isStoreDuplicateInExcel(List<UploadBusinessBean> records) {

		boolean isStoreDuplicate = false;

		for (int x = 0; x < records.size(); x++) {
			UploadBusinessBean xRecord = records.get(x);

			for (int y = x + 1; y < records.size(); y++) {
				UploadBusinessBean yRecord = records.get(y);

				if (xRecord.getStore().equals(yRecord.getStore())
						&& xRecord.getClientId().equals(yRecord.getClientId())
						&& xRecord.getCompanyName().equalsIgnoreCase(
								yRecord.getCompanyName())
						&& xRecord.getSuite().equalsIgnoreCase(
								yRecord.getSuite())
						&& xRecord.getLocationAddress().equalsIgnoreCase(
								yRecord.getLocationAddress())
						&& xRecord.getLocationCity().equalsIgnoreCase(
								yRecord.getLocationCity())
						&& xRecord.getLocationState().equalsIgnoreCase(
								yRecord.getLocationState())
						&& xRecord.getLocationPhone().equals(
								yRecord.getLocationPhone())
						&& xRecord.getLocationZipCode().equalsIgnoreCase(
								yRecord.getLocationZipCode())) {
					isStoreDuplicate = true;
				}
			}
		}
		return isStoreDuplicate;
	}

	/***
	 * 
	 * this method check's the data in the excel sheet with database and returns
	 * the correct data to businessinfo method
	 * 
	 * @param records
	 * @return
	 */

	public List<LocalBusinessDTO> checkRecordsInListings(
			List<LocalBusinessDTO> records, Map<String, Long> brandsCountsMap) {
		List<LocalBusinessDTO> insertRecord = new ArrayList<LocalBusinessDTO>();
		listofDeletesbyActionCode = new ArrayList<LocalBusinessDTO>();
		updateBusinessRecords = new ArrayList<LocalBusinessDTO>();
		List<LocalBusinessDTO> listOfBusinessInfo = service
				.getListOfBusinessInfo();
		List<LblErrorDTO> listOfErorBusinessInfo = service
				.getListOfErorBusinessInfo();
		List<LblErrorDTO> listOfErorBusinessInfos = new ArrayList<LblErrorDTO>();

		for (int i = 0; i < records.size(); i++) {
			LocalBusinessDTO excelRecord = records.get(i);
			for (LblErrorDTO lblErrorDTO : listOfErorBusinessInfo) {

				if (lblErrorDTO.getStore().equals(excelRecord.getStore())
						&& lblErrorDTO.getClientId().equals(
								excelRecord.getClientId())) {
					listOfErorBusinessInfos.add(lblErrorDTO);

				}
			}
			if (!listOfErorBusinessInfos.isEmpty()
					&& listOfErorBusinessInfos.size() > 0) {
				service.deleteErrorBusinessByActioncode(listOfErorBusinessInfos);
			}

			boolean isDuplicate = false;
			for (int j = 0; j < listOfBusinessInfo.size(); j++) {
				/* Business Name, Address, Suite, City, State, Zip and Phone */
				LocalBusinessDTO dbRecord = listOfBusinessInfo.get(j);

				if (dbRecord.getStore().equals(excelRecord.getStore())
						&& dbRecord.getClientId().equals(
								excelRecord.getClientId())) {
					if (excelRecord.getActionCode().equalsIgnoreCase("D")) {
						excelRecord.setId(dbRecord.getId());
						listofDeletesbyActionCode.add(excelRecord);
					} else {
						excelRecord.setId(dbRecord.getId());

						updateBusinessRecords.add(excelRecord);
					}
					isDuplicate = true;
					break;
				}

			}

			if (!isDuplicate) {
				if (!service.isBusinessExcelRecordUnique(excelRecord)) {
					insertRecord.add(excelRecord);
				}
			} /*
			 * else { decreaseDuplicateRecordsLength(brandsCountsMap,
			 * excelRecord); }
			 */
		}

		return insertRecord;
	}

	public List<LblErrorDTO> checkRecordsInListingErrors(
			List<LblErrorDTO> records, Map<String, Long> brandsCountsMap) {
		List<LblErrorDTO> insertErrorRecord = new ArrayList<LblErrorDTO>();
		listofErrorDeletesbyActionCode = new ArrayList<LblErrorDTO>();
		listOfErrorUpdatesByActionCode = new ArrayList<LblErrorDTO>();
		List<LblErrorDTO> listOfErrorRecords = service
				.getListOfErorBusinessInfo();

		for (int i = 0; i < records.size(); i++) {
			LblErrorDTO excelErrorRecord = records.get(i);

			boolean isDuplicate = false;
			for (int j = 0; j < listOfErrorRecords.size(); j++) {
				LblErrorDTO dbRecord = listOfErrorRecords.get(j);
				if (dbRecord.getStore().equals(excelErrorRecord.getStore())
						&& dbRecord.getClientId().equals(
								excelErrorRecord.getClientId())) {
					if (excelErrorRecord.getActionCode().equalsIgnoreCase("D")) {
						excelErrorRecord.setId(dbRecord.getId());
						listofErrorDeletesbyActionCode.add(excelErrorRecord);
					} else {
						excelErrorRecord.setId(dbRecord.getId());
						listOfErrorUpdatesByActionCode.add(excelErrorRecord);
					}
					isDuplicate = true;
					break;
				}
			}
			if (!isDuplicate) {
				insertErrorRecord.add(excelErrorRecord);
			}/*
			 * else { decreaseErrorDuplicateRecordsLength(brandsCountsMap,
			 * excelErrorRecord); }
			 */

		}
		return insertErrorRecord;
	}

	public List<LblErrorDTO> correctDBErrorRecords(
			List<LocalBusinessDTO> records) {
		List<LblErrorDTO> insertErrorRecord = new ArrayList<LblErrorDTO>();

		List<LblErrorDTO> listOfErrorRecords = service
				.getListOfErorBusinessInfo();

		for (int i = 0; i < records.size(); i++) {

			LocalBusinessDTO excelRecord = records.get(i);
			for (int j = 0; j < listOfErrorRecords.size(); j++) {

				LblErrorDTO dbRecord = listOfErrorRecords.get(j);
				/*
				 * logger.info(dbRecord.getStore() + ".equals(" +
				 * excelRecord.getStore() + " , " + dbRecord.getClientId() +
				 * ".equals(" + excelRecord.getClientId());
				 */
				if (dbRecord.getStore().equals(excelRecord.getStore())
						&& dbRecord.getClientId().equals(
								excelRecord.getClientId())) {
					records.remove(i);
					i--;
					insertErrorRecord.add(dbRecord);
					break;
				}

			}
		}
		logger.info("Correct Suitable Records == " + insertErrorRecord.size());
		logger.info("Duplicate Records == " + duplicateRecord.size());

		return insertErrorRecord;
	}

	/***
	 * this method takes the list of information from excel
	 * 
	 * @param infile
	 * @return
	 */
	public boolean isRowEmpty(Row row) {
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
				return false;
		}
		return true;
	}

	private List<UploadBusinessBean> getListDataFromXLS(
			CommonsMultipartFile infile, Map<String, Long> brandsCountsMap,
			String headerpopup) {

		List<UploadBusinessBean> tListData = new ArrayList<UploadBusinessBean>();
		try {
			Sheet sheet = (Sheet) WorkbookFactory.create(
					infile.getInputStream()).getSheetAt(0);
			DataFormatter df = new DataFormatter();
			Iterator<Row> rowIterator = sheet.rowIterator();
			boolean header = true;

			while (rowIterator.hasNext()) {
				Row row = (Row) rowIterator.next();
				boolean rowEmpty = isRowEmpty(row);
				if (rowEmpty == false) {
					if (header) {
						header = false;
						headerpopup = "";
						if (!headersValid(row, df)) {
							headerpopup = "Invalid Bulk Upload Template";
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

	private boolean allValuesInRowAreEmpty(String[] row) {
		boolean returnValue = true;
		for (String s : row) {
			if (s.length() != 0) {
				returnValue = false;
			}
		}
		return returnValue;
	}

	private List<UploadBusinessBean> getListFromCSV(CommonsMultipartFile file,
			Map<String, Long> brandsCountsMap, String headerpopup) {
		List<UploadBusinessBean> tListData = new ArrayList<UploadBusinessBean>();
		boolean value = isheaderValid(file);

		if (!value) {
			headerpopup = "Invalid Bulk Upload Template";
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

	private boolean isheaderValid(CommonsMultipartFile file) {
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

	private boolean isheaderValid(Map<String, Integer> headerMap) {
		boolean isHeadersValid = true;
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
		return isHeadersValid;
		// TODO Auto-generated method stub

	}

	private boolean headersValid(List<String> list) {

		boolean isHeadersValid = true;
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i));
			String anotherString = LBLConstants.UPLOAD_EXCEL_HEADERS[i];
			// System.out.println(anotherString);
			if (!list.get(i).equalsIgnoreCase(anotherString)) {
				isHeadersValid = false;
				break;
			}

		}

		return isHeadersValid;
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
							LBLConstants.UPLOAD_EXCEL_HEADERS[cell
									.getColumnIndex()]))) {
				isHeadersValid = false;
				break;
			}
		}
		return isHeadersValid;
	}

}
