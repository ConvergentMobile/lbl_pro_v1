package com.whitespark.ws;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.business.common.dto.AccuracyGraphDTO;
import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.util.PropUtil;
import com.business.service.BusinessService;

public class WhitesparkUtil {

	BusinessService service;

	protected static Logger logger = Logger.getLogger(WhitesparkUtil.class);
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private static final String secretKey = "43hfjdk0cbnd34gk78ls";

	public static final String wsKey = PropUtil.WHITE_SPARK
			.getString("whitespark.Key");
	public static final String wsSecret = PropUtil.WHITE_SPARK
			.getString("whitespark.Secret");
	private static String apiMethod = "search";

	public static void main(String[] args) {
		String url = "";
		try {
			logger.info("Posting Search request to WhiteSpark");
			String requestParams = "";
			//requestParams = "business_name=Sprint+Store&city=Bohemia&country=US&custom=3456-FCS-1001&region=NY&search_term=6315671702&type=phone";
			requestParams = "business_name=Liberty+Tax+Service&city=Leavittsburg&country=US&custom=3225-10027&region=OH&search_term=2348064529&type=phone";

			// requestParams =
			// "city=Rockford&country=US&custom=IL852&region=IL&search_term=U.S.+Cellular+Authorized+Agent+–+Dr.+Detail,+Inc.+61103&type=keyphrase";

			 requestParams =
			"city=West+Hartford&country=US&custom=000900338&region=CT&search_term=GOODYEAR+AUTO+SERVICE+CENTER+06110+4158219002&type=keyphrase";
			// requestParams =
			// "city=San+Diego&country=US&custom=LAJOLLA&region=CA&search_term=Vi+at+La+Jolla+Village+92122&type=keyphrase";
			/*
			 * requestParams =
			 * "city=Dallas&country=US&custom=7103&region=TX&search_term=Texas+Land+&+Cattle+75204&type=keyphrase"
			 * ;
			 */

			requestParams = requestParams.replaceAll("\\+&+", "+");

			url = "http://lcf.whitespark.ca/api/v1/" + apiMethod + "?key="
					+ wsKey + "&" + requestParams + "&sig="
					+ md5(apiMethod + "&" + requestParams + "&" + wsSecret);

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			logger.info("search response:  " + response.toString());

		} catch (Exception e) {
			logger.error("There was a issue while posting search request for whitespark with url:"
					+ url + ", the error is: " + e.getLocalizedMessage());
			e.printStackTrace();
		}

	}

	public void triggerWhiteSparkSerach(BusinessService service)
			throws InterruptedException {
		this.service = service;
	/*	Map<String, List<BrandInfoDTO>> allActiveBrandsMap = service
				.getAllActiveBrands();

		Set<String> activeBrandNamesList = allActiveBrandsMap.keySet();
		*/
		String brandName ="PNC Bank";
		//for (String brandName : activeBrandNamesList) {

			

			List<LocalBusinessDTO> localBusinessDTOs = service
					.getListOfBussinessByBrandName(brandName);
			logger.info("Triggering white spark search for : " + brandName + ", total stores are: "+ localBusinessDTOs.size());
			for (LocalBusinessDTO localBusinessDTO : localBusinessDTOs) {
				searchWhiteSpark(localBusinessDTO);
			}
			AccuracyGraphDTO accuracyGraphDTO = new AccuracyGraphDTO();
			accuracyGraphDTO
					.setListingCount(new Long(localBusinessDTOs.size()));
			accuracyGraphDTO.setDate(new Date());
			service.insertAccuracyGraphInfo(accuracyGraphDTO);
		//}

	}

	public void searchWhiteSpark(LocalBusinessDTO localBusinessDTO) {
		String url = "";
		try {

			logger.info("Posting Search request to WhiteSpark for the store: "
					+ localBusinessDTO.getStore());
			String requestParams = "";

		/*	requestParams = "business_name="
					+ localBusinessDTO.getCompanyName().replaceAll(" ", "+")
					+ "&city="
					+ localBusinessDTO.getLocationCity().replaceAll(" ", "+")
					+ "&country=" + localBusinessDTO.getCountryCode()
					+ "&custom=" + localBusinessDTO.getClientId() +"-"+ localBusinessDTO.getStore() + "&region="
					+ localBusinessDTO.getLocationState() + "&search_term="
					+ localBusinessDTO.getLocationPhone() + "&type=phone";*/
			
			requestParams = "city="
					+ localBusinessDTO.getLocationCity().replaceAll(" ", "+")
					+ "&country=" + localBusinessDTO.getCountryCode()
					+ "&custom=" + localBusinessDTO.getClientId() +"-"+ localBusinessDTO.getStore() + "&region="
					+ localBusinessDTO.getLocationState() + "&search_term="
					+  localBusinessDTO.getCompanyName().replaceAll(" ", "+") + "+" +localBusinessDTO.getLocationPhone()+  "+" + localBusinessDTO.getLocationPhone() + "&type=keyphrase";

			/*
			 * String requestParams = ""; requestParams =
			 * "business_name=Liberty+Tax+Service&city=San+Francisco&country=US&custom=9999&region=California&search_term=4158219002&type=phone"
			 * ;
			 */
			requestParams = requestParams.replaceAll("\\+&+", "+");

			url = "http://lcf.whitespark.ca/api/v1/" + apiMethod + "?key="
					+ wsKey + "&" + requestParams + "&sig="
					+ md5(apiMethod + "&" + requestParams + "&" + wsSecret);

			logger.info("\nSending 'GET' request to URL : " + url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			int responseCode = con.getResponseCode();

			logger.info("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			logger.info("Search response from Whitespark for :  " + localBusinessDTO.getStore()+ ", is"  + response.toString() + ", ");

		} catch (Exception e) {
			logger.error("There was a issue while posting search request for whitespark with url:"
					+ url + ", the error is: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private static String md5(String input) {

		String md5 = null;

		if (null == input)
			return null;

		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(input.getBytes(), 0, input.length());
			md5 = new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}

}
