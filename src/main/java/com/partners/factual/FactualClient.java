package com.partners.factual;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.common.util.SubmissionThread;
import com.business.common.util.SubmissionUtil;
import com.business.service.BusinessService;
import com.factual.driver.Factual;
import com.factual.driver.FactualApiException;
import com.factual.driver.Metadata;
import com.factual.driver.Submit;
import com.factual.driver.SubmitResponse;
import com.google.api.client.util.Maps;

/**
 * 
 * @author Vasanth
 * 
 */

public class FactualClient {
	static Logger logger = Logger.getLogger(FactualClient.class);

	static ResourceBundle bundle = ResourceBundle.getBundle("providers");

	public Map<String, List<String>> exportToFactual(
			List<LocalBusinessDTO> listOfBusinessInfo, BusinessService service) {

		Map<String, List<String>> errorDetails = new HashMap<String, List<String>>();

		List<String> erroredStores = new ArrayList<String>();

		int totalCount = listOfBusinessInfo.size();
		logger.info("Factual exporting Business list size is : "
				+ listOfBusinessInfo.size());
		String oauthKey = bundle.getString("factual.oauthKey");
		String oauthSecret = bundle.getString("factual.oauthSecret");
		for (LocalBusinessDTO localBusinessDTO : listOfBusinessInfo) {
			String store = localBusinessDTO.getStore();
			Integer clientId = localBusinessDTO.getClientId();
			RenewalReportDTO renewalReportDTO = service.isRenewed(localBusinessDTO.getStore(), localBusinessDTO.getClientId());
			if(renewalReportDTO!=null  ){
				Date cancelledEffeciveDate = renewalReportDTO.getCancelledEffeciveDate();
				boolean isDate=false;
				if(cancelledEffeciveDate!=null){
					Date currentDateDate=new Date();
					 isDate=cancelledEffeciveDate.compareTo(currentDateDate)< 0;
				}
				
				if((renewalReportDTO.getStatus().equals("Renewed")|| renewalReportDTO.getStatus().equals("Active") || isDate)){
				try {
					String hours = SubmissionUtil.getHours(localBusinessDTO);

					Factual factual = new Factual(oauthKey, oauthSecret);
					Map<String, Object> values = Maps.newHashMap();
					values.put(bundle.getString("factual.schema.name"),
							localBusinessDTO.getCompanyName());
					values.put(bundle.getString("factual.schema.address"),
							localBusinessDTO.getLocationAddress());
					values.put(bundle.getString("factual.schema.neighborhood"), "");
					values.put(bundle.getString("factual.schema.locality"),
							localBusinessDTO.getLocationCity());
					values.put(bundle.getString("factual.schema.region"),
							localBusinessDTO.getLocationState());
					values.put(bundle.getString("factual.schema.postcode"),
							localBusinessDTO.getLocationZipCode());
					values.put(bundle.getString("factual.schema.country"),
							localBusinessDTO.getCountryCode());

					String categoryId = service
							.getFactualCategoryId(clientId);

					values.put(bundle.getString("factual.schema.category_ids"),
							categoryId);
					values.put(bundle.getString("factual.schema.category_labels"),
							"");
					values.put(bundle.getString("factual.schema.latitude"), "");
					values.put(bundle.getString("factual.schema.longitude"), "");
					values.put(bundle.getString("factual.schema.tel"),
							localBusinessDTO.getLocationPhone());
					values.put(bundle.getString("factual.schema.fax"),
							localBusinessDTO.getFax());
					values.put(bundle.getString("factual.schema.website"),
							localBusinessDTO.getWebAddress());
					values.put(bundle.getString("factual.schema.chain_name"),
							localBusinessDTO.getCompanyName());
					values.put(bundle.getString("factual.schema.chain_id"), "");
					values.put(bundle.getString("factual.schema.hours_display"), "");
					values.put(bundle.getString("factual.schema.hours"), hours);
					values.put(bundle.getString("factual.schema.admin_region"), "");
					values.put(bundle.getString("factual.schema.post_town"),
							localBusinessDTO.getLocationCity());

					// An end user id is required
					Metadata metadata = new Metadata().user(bundle
							.getString("factual.userName"));

					// Run the Submit
					Submit submit = new Submit(values);
					SubmitResponse resps = factual
							.submit(bundle.getString("factual.tableName"), submit,
									metadata);
					logger.debug("response from Factual is : " + resps);
					logger.debug("returned Factual id is:  " + resps.getFactualId());
				} catch (FactualApiException e) {
					erroredStores.add(store);
					logger.error("There was a error in posting the data to Fatual: "
							+ e.getRequestUrl());
					e.printStackTrace();
				} catch (Exception e) {
					logger.error("There was a error in posting the data to Fatual"
							+ e);
					e.printStackTrace();
				}	
			}
			}
			
		}

		errorDetails.put("errorDetails", erroredStores);
		return errorDetails;

	}

	public static void main(String[] args) {
		try {

			String oauthKey = bundle.getString("factual.oauthKey");
			String oauthSecret = bundle.getString("factual.oauthSecret");
			Factual factual = new Factual(oauthKey, oauthSecret);
			Map<String, Object> values = Maps.newHashMap();
			values.put(bundle.getString("factual.schema.name"),
					"GOODYEAR AUTO SERVICE CENTER");
			values.put(bundle.getString("factual.schema.address"),
					"1953 Rosebud Road");
			values.put(bundle.getString("factual.schema.neighborhood"), "");
			values.put(bundle.getString("factual.schema.locality"), "Grayson");
			values.put(bundle.getString("factual.schema.region"), "GA");
			values.put(bundle.getString("factual.schema.postcode"), "30017");
			values.put(bundle.getString("factual.schema.country"), "US");
			values.put(bundle.getString("factual.schema.category_ids"), "12");
			values.put(bundle.getString("factual.schema.category_labels"), "");
			values.put(bundle.getString("factual.schema.latitude"), "");
			values.put(bundle.getString("factual.schema.longitude"), "");
			values.put(bundle.getString("factual.schema.tel"), "");
			values.put(bundle.getString("factual.schema.fax"), "");
			values.put(bundle.getString("factual.schema.website"),
					"www.goodyearautoservice.com/custserv/store_details.jsp?storeId=1299");
			values.put(bundle.getString("factual.schema.chain_name"),
					"GOODYEAR AUTO SERVICE CENTER");
			values.put(bundle.getString("factual.schema.chain_id"), "902141");
			values.put(bundle.getString("factual.schema.hours_display"), "");
			values.put(
					bundle.getString("factual.schema.hours"),
					"MON 07:00-19:00, TUE 07:00-19:00, WED 07:00-19:00, TUE 07:00-19:00, FRI 07:00-19:00, SAT 07:00-19:00, SUN09:00-18:00");
			values.put(bundle.getString("factual.schema.admin_region"), "");
			values.put(bundle.getString("factual.schema.post_town"), "US");

			// An end user id is required
			Metadata metadata = new Metadata().user(bundle
					.getString("factual.userName"));

			// Run the Submit
			Submit submit = new Submit(values);
			SubmitResponse resps = factual.submit(
					bundle.getString("factual.tableName"), submit, metadata);
			logger.info("response from Factual is : " + resps);
			logger.info("returned Factual id is:  " + resps.getFactualId());
		} catch (FactualApiException e) {
			logger.error("There was a error in posting the data to Fatual: "
					+ e.getRequestUrl());
			e.printStackTrace();
		}
	}

}
