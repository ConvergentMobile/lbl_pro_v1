package com.lbl.scheduler;

import java.io.ObjectInputStream.GetField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.business.common.dto.SweetIQAccuracyDTO;
import com.business.common.dto.SweetIQCoverageDTO;
import com.business.common.dto.SweetIQDTO;
import com.business.common.dto.SweetIQRankingDTO;
import com.business.model.pojo.KeywordEntity;
import com.business.service.ListingService;
import com.business.service.SweetIQService;

@Component
public class SIQSchedulerTask {
	Logger logger = Logger.getLogger(SIQSchedulerTask.class);

	@Autowired
	private SweetIQService service;

	@Autowired
	private ListingService listingService;
	
	public void pullSweetIQReport() throws Exception {

		SweetIQUtil client = new SweetIQUtil(service, listingService);

		logger.info("Start: sweetIQ report Collection");
		Map<String, String> clientMap = new HashMap<String, String>();
		
		String apiKey = "580799607e7ec8ac6273b493";
		
		clientMap.put("58a7739a0b3fe9ce6faac396", "3225"); // Liberty
		clientMap.put("59ce67b9eb8380a3169b532e", "3991"); // Sec Finace
		
		
		client.updateSweetIQStoresInLBL("59ce67b9eb8380a3169b532e", 3991);
		client.updateSweetIQStoresInLBL("58a7739a0b3fe9ce6faac396", 3225);
		
		Date dateToday = getCurentMonthDate();
		
		
		// Accuracy data
		List<String> dates = new ArrayList<String>();
		String startDate = getFormattedDate(dateToday, -5);
		String endDate = getFormattedDate(dateToday, 5);
		String formattedStartEnd = startDate+"#"+endDate;
		System.out.println(formattedStartEnd);
		dates.add(formattedStartEnd);



		Set<String> keySet = clientMap.keySet();
		List<SweetIQDTO> storeList = null;
		
		for (String clientId : keySet) {
			int brandId = Integer.parseInt(clientMap.get(clientId));
			storeList = client.getSweetIQLBLMap(brandId);
			storeList = storeList.subList(0, 20);
			
			System.out.println("Store size: "+storeList.size() );
			logger.info("Pulling accuracy and coverage for : "+ brandId);
			for (String date : dates) {
				String[] stringDates = date.split("#");
		
				List<SweetIQAccuracyDTO> accuracyData = client.getAccuracyData(apiKey, clientId, brandId, stringDates[0], stringDates[1] );
				 service.addAccuracyDetails(accuracyData);

			

				for (SweetIQDTO sweetIQDTO : storeList) {

					List<SweetIQCoverageDTO> coverageReport = client.getCoverageReport(apiKey,clientId, brandId,sweetIQDTO.getsIQBranchId(), stringDates[0], stringDates[1] );
	
					service.saveCoverageReport(coverageReport);
				
				}
		}
		}
		/*List<String> locations = service.getBranchListFromAccuracy();
		System.out.println("distinct locas: " + locations.size());
		for (int i = 0; i < locations.size(); i++) {
			if (i == 100)
				break;
			List<SweetIQCoverageDTO> coverageReport = client.getCoverageReport(locations.get(i));
			System.out.println("distinct coverageReport: "
					+ coverageReport.size());
			service.saveCoverageReport(coverageReport);

		}*/
		

		List<String> datesSIq = new ArrayList<String>();
		

		String formattedDate = getFormattedDate(dateToday);

		dates.add(formattedDate);
	
		
		//Set<String> keySet = clientMap.keySet();
		
		for (String clientId : keySet) {
			int brandId = Integer.parseInt(clientMap.get(clientId));
			client.updateSweetIQStoresInLBL(clientId, brandId);

			
			for (SweetIQDTO sweetIQDTO : storeList) {

				for (String date : dates) {
					String searchType="local";
					String engine="google";
					client.pullRanks("580799607e7ec8ac6273b493", brandId, sweetIQDTO.getsIQclientId(), sweetIQDTO.getsIQLocationId(), searchType, engine, sweetIQDTO.getsIQBranchId(), sweetIQDTO.getLblStoreId(), date);
					
					searchType="organic";
					engine="google";
					client.pullRanks("580799607e7ec8ac6273b493", brandId, sweetIQDTO.getsIQclientId(), sweetIQDTO.getsIQLocationId(), searchType, engine, sweetIQDTO.getsIQBranchId(), sweetIQDTO.getLblStoreId(), date);
				
					
					searchType="local";
					engine="bing";
					client.pullRanks("580799607e7ec8ac6273b493", brandId, sweetIQDTO.getsIQclientId(), sweetIQDTO.getsIQLocationId(), searchType, engine, sweetIQDTO.getsIQBranchId(), sweetIQDTO.getLblStoreId(), date);
					
					searchType="organic";
					engine="bing";
					client.pullRanks("580799607e7ec8ac6273b493", brandId, sweetIQDTO.getsIQclientId(), sweetIQDTO.getsIQLocationId(), searchType, engine, sweetIQDTO.getsIQBranchId(), sweetIQDTO.getLblStoreId(), date);

				}
			}

		}

		logger.info("End: sweetIQ report Collection");

	}
	
	private Date getCurentMonthDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 15);
		Date date = cal.getTime();
		return date;
	}
	
	private String getFormattedDate(Date date) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(date);            

		return date1;
	}
	
	
	private String getFormattedDate(Date date, int daysToAddOrSubtract) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, daysToAddOrSubtract);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(cal.getTime());            

		return date1;
	}
	
	public static void main(String[] args) {
		SIQSchedulerTask siq=  new SIQSchedulerTask();
		Date date = siq.getCurentMonthDate();
		String curentMonthDate = siq.getFormattedDate(date);
		System.out.println(curentMonthDate);
		String formattedDate = siq.getFormattedDate(date, -5);
		String formattedDate2 = siq.getFormattedDate(date, 5);
		
		System.out.println(formattedDate + "#"+ formattedDate2);
		
	}

}
