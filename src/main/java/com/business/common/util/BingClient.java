package com.business.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.CertificateException;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.business.common.dto.BingAnalyticsDTO;
import com.business.common.dto.BingApiResponseDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.service.BingService;
import com.business.web.bean.UploadBusinessBean;

public class BingClient {

	static String analytiscUrl = LBLConstants.BING_ANALYTICS_URL;
	static String createStoreURL = LBLConstants.BING_CREATE_URL;
	static String updateStoreURL = LBLConstants.BING_UPDATE_URL;

	static final String EMIAL = "convergentmobile@gmail.com";
	static final String AUTH_PROVIDER = "WINDOWS_LIVE";
	static final String Puid = "0003BFFDCC6B0FA4";

	static final UUID uid = UUID
			.fromString("7c48e923-b1e6-49b4-9f17-e0e2215c5b83");

	static final String OPERATION_ADD = "BUSINESS_ADD";
	static final String OPERATION_UPDATE = "BUSINESS_UPDATE";

	static List<BingApiResponseDTO> bingResponse = new ArrayList<BingApiResponseDTO>();

	static Logger logger = Logger.getLogger(BingClient.class);

	private static BingService service;

	public BingClient(BingService service) {
		this.service = service;
	}

	public static List<BingApiResponseDTO> addorUpdateStoresOnBing(
			List<UploadBusinessBean> listOfStores) {

		BingApiResponseDTO response = null;
		int callsCount=0;
		for (UploadBusinessBean uploadBusinessBean : listOfStores) {
			callsCount++;
			if(callsCount>10) {
				callsCount=0;
				try {
					Thread.sleep(2*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			if (uploadBusinessBean.getActionCode().equalsIgnoreCase("A")) {
				response = createBusinessOnBing(uploadBusinessBean);
			} else if (uploadBusinessBean.getActionCode().equalsIgnoreCase("U")) {
				response = updateBusinessOnBing(uploadBusinessBean);
			}
			bingResponse.add(response);
		}

		return bingResponse;

	}
	
	public static SSLSocketFactory buildSslContext(InputStream inputStream) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, java.security.cert.CertificateException {
        X509Certificate cert;
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);
 
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                cert = (X509Certificate)certificateFactory.generateCertificate(inputStream);
 
            String alias = cert.getSubjectX500Principal().getName();
            trustStore.setCertificateEntry(alias, cert);
   
 
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(trustStore);
        TrustManager[] trustManagers = tmf.getTrustManagers();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);
 
        return sslContext.getSocketFactory();
    }

	public static SSLSocketFactory getSocketFactoryFromPEM() throws Exception {
		KeyManagerFactory keyManagerFactory = KeyManagerFactory
				.getInstance("SunX509");
		KeyStore keyStore = KeyStore.getInstance("PKCS12");

		ClassLoader classLoader = BingClient.class.getClassLoader();
		File file = new File(classLoader.getResource("BingPlacesTPCert2018March.p12")
				.getFile());

		InputStream keyInput = new FileInputStream(file);
		keyStore.load(keyInput, "bpcert!123".toCharArray());
		keyInput.close();

		keyManagerFactory.init(keyStore, "bpcert!123".toCharArray());

		/*
		 * ClassLoader classLoader = BingClient.class.getClassLoader(); File
		 * file = new File(classLoader.getResource("BP-TPAPI-Prod.pfx")
		 * .getFile());
		 * 
		 * InputStream keyInput = new FileInputStream(file);
		 * keyStore.load(keyInput, "bpcert!123".toCharArray());
		 * keyInput.close();
		 * 
		 * keyManagerFactory.init(keyStore, "bpcert!123".toCharArray());
		 */

		SSLContext context = SSLContext.getInstance("TLS");
		context.init(keyManagerFactory.getKeyManagers(), null,
				new SecureRandom());
		

		
		/*SSLContext sslContext = buildSslContext(classLoader.getResourceAsStream("/BingPlacesTPCert2018March.pem"));
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
*/
		return context.getSocketFactory();
	}

	public static BingApiResponseDTO updateBusinessOnBing(UploadBusinessBean dto) {
		BingApiResponseDTO response = new BingApiResponseDTO();

		String store = null;
		String operation = null;
		String status = null;
		String errorMessage = null;

		Map<String, String> bingCategoryDetails = getBingCategoryDetails(
				dto.getClientId(), dto.getStore());
		Set<String> keySet = bingCategoryDetails.keySet();
		String bingCategory = "";
		String bingCategoryDesc = "";
		for (String key : keySet) {
			bingCategory = key;
			bingCategoryDesc = bingCategoryDetails.get(key);
		}

		try {

			response.setClientId(dto.getClientId());

			String data2 = "{" + "\"Businesses\": [" + "{"
					+ "\"StoreId\":\"" + dto.getStore() + "\","
					+ "\"BusinessName\":\"" + dto.getCompanyName() + "\","
					+ "\"AddressLine1\": \"" + dto.getLocationAddress() + "\","
					+ "\"AddressLine2\":\"" + dto.getSuite() + "\","
					+ "\"City\":\"" + dto.getLocationCity() + "\","
					+ "\"Country\": \"" + dto.getCountryCode() + "\","
					+ " \"ZipCode\": \"" + dto.getLocationZipCode() + "\","

					+ "\"StateOrProvince\": \"" + dto.getLocationState()
					+ "\"," + "\"PhoneNumber\":  \"" + dto.getLocationPhone()
					+ "\"," + "\"Categories\": {" + "\"BusinessCategories\": ["
					+ " {" + "\"CategoryName\":\"" + bingCategoryDesc + "\","
					+ "\"BPCategoryId\": " + bingCategory + " }" + " ],"
					+ "\"PrimaryCategory\": {" + "\"CategoryName\":\""
					+ bingCategoryDesc + "\"," + "\"BPCategoryId\":"
					+ bingCategory + "}" + "}" + "}" + "],"
					+ "\"TrackingId\":\"" + uid.randomUUID() + "\","
					+ "\"Identity\": {" + "\"Puid\": \"" + Puid + "\","
					+ "\"AuthProvider\": \"" + AUTH_PROVIDER + "\","
					+ "\"EmailId\": \"" + EMIAL + "\" }" + "}";

			//System.out.println("Request is: " + data2);

			URL obj = new URL(updateStoreURL);
			HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();

			conn.setSSLSocketFactory(getSocketFactoryFromPEM());

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/json;charset=UTF-8");

			OutputStream os = conn.getOutputStream();
			os.write(data2.getBytes());
			os.flush();

			String responseMessage = conn.getResponseMessage();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			StringBuilder content = new StringBuilder();
			String line;
			while (null != (line = br.readLine())) {
				content.append(line);
			}
			conn.disconnect();

			Object jsonStringObj = JSONValue.parse(content.toString());

			JSONObject jsonObject = (JSONObject) jsonStringObj;

			JSONObject sucessOrFail = ((JSONObject) jsonObject
					.get("UpdatedBusinesses"));
			JSONObject errors = ((JSONObject) jsonObject.get("Errors"));

		/*	System.out.println(errors);
			System.out.println(sucessOrFail);
		*/
			if (errors.isEmpty() && sucessOrFail == null) {
				logger.info("Store failed to update on bing as it does not exist, so adding it on bing");
				createBusinessOnBing(dto);
			} else {
				if (sucessOrFail != null && !sucessOrFail.isEmpty()) {

					JSONObject storeObj = (JSONObject) sucessOrFail.get("0");
					if (storeObj != null) {
						store = (String) storeObj.get("StoreId");
						operation = (String) storeObj.get("Operation");
						status = (String) storeObj.get("Status");
						errorMessage = (String) storeObj.get("ErrorMessage");

						response.setOperation(operation);
						response.setStatus(status);
						response.setErrorMessage(errorMessage);
					}
				} else if (!errors.isEmpty()) {
					JSONObject storeObj = (JSONObject) errors.get("0");
					if (storeObj != null) {
						store = (String) storeObj.get("StoreId");
						JSONArray errorsArray = (JSONArray) storeObj
								.get("BusinessErrors");

						errorMessage = ((JSONObject) errorsArray.get(0)).get(
								"ErrorMessage").toString();

						response.setStatus(status);
						response.setErrorMessage(errorMessage);

					}
				}
				response.setClientId(dto.getClientId());
				response.setStore(dto.getStore());
				if (response.getOperation() == null) {
					response.setOperation(OPERATION_UPDATE);
				}
				if (response.getStatus() == null) {
					response.setStatus("");
				}
			}
			logger.info("Updating of store on Bing is successful, Store is: "+ dto.getStore());
		} catch (Exception e) {
			logger.info("There was a error while updating store on Bing, Store is: "
					+ dto.getStore());
			e.printStackTrace();
		}
		return response;
	}

	public static BingApiResponseDTO createBusinessOnBing(UploadBusinessBean dto) {

		BingApiResponseDTO response = new BingApiResponseDTO();
		String store = null;
		String operation = null;
		String status = null;
		String errorMessage = null;

		Map<String, String> bingCategoryDetails = getBingCategoryDetails(
				dto.getClientId(), dto.getStore());
		Set<String> keySet = bingCategoryDetails.keySet();
		String bingCategory = "";
		String bingCategoryDesc = "";
		for (String key : keySet) {
			bingCategory = key;
			bingCategoryDesc = bingCategoryDetails.get(key);
		}

		try {

			String createRequest = "{" + "\"Businesses\": [" + "{"
			// + "\"StoreId\": \"Store_1_LBL_005\","
					+ "\"StoreId\":\"" + dto.getStore() + "\","
					+ "\"BusinessName\":\"" + dto.getCompanyName() + "\","
					+ "\"AddressLine1\": \"" + dto.getLocationAddress() + "\","
					+ "\"AddressLine2\":\"" + dto.getSuite() + "\","
					+ "\"City\":\"" + dto.getLocationCity() + "\","
					+ "\"Country\": \"" + dto.getCountryCode() + "\","
					+ " \"ZipCode\": \"" + dto.getLocationZipCode() + "\","

					+ "\"StateOrProvince\": \"" + dto.getLocationState()
					+ "\"," + "\"PhoneNumber\":  \"" + dto.getLocationPhone()
					+ "\"," + "\"Categories\": {" + "\"BusinessCategories\": ["
					+ " {" + "\"CategoryName\":\"" + bingCategoryDesc + "\","
					+ "\"BPCategoryId\": " + bingCategory + " }" + " ],"
					+ "\"PrimaryCategory\": {" + "\"CategoryName\":\""
					+ bingCategoryDesc + "\"," + "\"BPCategoryId\":"
					+ bingCategory + "}" + "}" + "}" + "],"
					+ "\"TrackingId\":\"" + uid.randomUUID() + "\","
					+ "\"Identity\": {" + "\"Puid\": \"" + Puid + "\","
					+ "\"AuthProvider\": \"" + AUTH_PROVIDER + "\","
					+ "\"EmailId\": \"" + EMIAL + "\" }" + "}";

			//System.out.println("createRequest is: " + createRequest);

			//System.out.println(createStoreURL);

			URL obj = new URL(createStoreURL);
			HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();

			conn.setSSLSocketFactory(getSocketFactoryFromPEM());

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/json;charset=UTF-8");

			OutputStream os = conn.getOutputStream();
			os.write(createRequest.getBytes());
			os.flush();

			String responseMessage = conn.getResponseMessage();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			StringBuilder content = new StringBuilder();
			String line;
			while (null != (line = br.readLine())) {
				content.append(line);
			}

			//System.out.println("Repsponse Output is: " + content);

			conn.disconnect();

			Object jsonStringObj = JSONValue.parse(content.toString());

			JSONObject jsonObject = (JSONObject) jsonStringObj;

			JSONObject sucessOrFail = ((JSONObject) jsonObject
					.get("CreatedBusinesses"));
			JSONObject errors = ((JSONObject) jsonObject.get("Errors"));

			if (!sucessOrFail.isEmpty()) {

				JSONObject storeObj = (JSONObject) sucessOrFail.get("0");
				if (storeObj != null) {
					store = (String) storeObj.get("StoreId");
					operation = (String) storeObj.get("Operation");
					status = (String) storeObj.get("Status");
					errorMessage = (String) storeObj.get("ErrorMessage");

					response.setOperation(operation);
					response.setStatus(status);
					response.setErrorMessage(errorMessage);
				}
			} else if (!errors.isEmpty()) {
				JSONObject storeObj = (JSONObject) errors.get("0");
				if (storeObj != null) {
					store = (String) storeObj.get("StoreId");
					JSONArray errorsArray = (JSONArray) storeObj
							.get("BusinessErrors");

					errorMessage = ((JSONObject) errorsArray.get(0)).get(
							"ErrorMessage").toString();

					response.setStatus(status);
					response.setErrorMessage(errorMessage);

				}
			}
			response.setClientId(dto.getClientId());
			response.setStore(dto.getStore());
			if (response.getOperation() == null) {
				response.setOperation(OPERATION_ADD);
			}
			if (response.getStatus() == null) {
				response.setStatus("");
			}
			logger.info("Creating of store on Bing is successful, Store is: "+ dto.getStore());
		} catch (Exception e) {
			logger.info("There was a error while creating store on Bing, Store is: "
					+ dto.getStore());
			e.printStackTrace();
		}

		return response;
	}

	public static List<BingAnalyticsDTO> getAnalytics(LocalBusinessDTO dto) {

		List<BingAnalyticsDTO> analytics = new ArrayList<BingAnalyticsDTO>();
		try {

			String getAnalytics = "{" + "\"PageNumber\": 1,"
					+ "\"PageSize\": 100,"
					+ "\"CriteriaType\": \"SearchByStoreIds\","
					+ "\"StoreIds\": \"" + dto.getStore()+ "\","

					+ "\"TrackingId\":\"" + uid.randomUUID() + "\","
					+ "\"Identity\": {" + "\"Puid\": \"" + Puid + "\","
					+ "\"AuthProvider\": \"" + AUTH_PROVIDER + "\","
					+ "\"EmailId\": \"" + EMIAL + "\" }" + "}";

			System.out.println("request is: " + getAnalytics);
			//System.out.println(analytiscUrl);

			URL obj = new URL(analytiscUrl);

			HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
			ClassLoader classLoader = BingClient.class.getClassLoader();

			conn.setSSLSocketFactory(getSocketFactoryFromPEM());

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/json;charset=UTF-8");

			OutputStream os = conn.getOutputStream();
			os.write(getAnalytics.getBytes());
			os.flush();

			String responseMessage = conn.getResponseMessage();
			//System.out.println(responseMessage);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			StringBuffer response = new StringBuffer();

			while ((output = br.readLine()) != null) {
				response.append(output);
				System.out.println(output);
			}

			conn.disconnect();
			//System.out.println("===================");

			JSONParser parser = new JSONParser();
			try {
				JSONObject jsonObject = (JSONObject) parser.parse(response.toString());

				JSONArray analyticsArray = (JSONArray) jsonObject
						.get("BusinessesAnalytics");

				for (int i = 0; i < analyticsArray.size(); i++) {
					JSONObject jsonObject1 = (JSONObject) analyticsArray.get(i);
					JSONArray statistics = (JSONArray) jsonObject1
							.get("BusinessStatisticsList");
					for (int j = 0; j < statistics.size(); j++) {
						BingAnalyticsDTO bingDto = new BingAnalyticsDTO();
						String impressionCount = ((JSONObject) statistics
								.get(j)).get("ImpressionCount").toString();
						String startTime = ((JSONObject) statistics.get(j))
								.get("BusinessStatStartTime").toString();
						Date formattedDate = getFormattedDate(startTime);

						bingDto.setBrandId(dto.getClientId());
						bingDto.setBrandName(dto.getClient());
						bingDto.setStore(dto.getStore());
						bingDto.setState(dto.getLocationState());
						bingDto.setImpressionCount(Integer
								.parseInt(impressionCount));
						bingDto.setDate(formattedDate);
						analytics.add(bingDto);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return analytics;
	}

	public static java.util.Date getFormattedDate(String dateValue) {
		java.util.Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
					.parse(dateValue);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			date = cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static void getBusinessByBatches() {
		try {
			String getData = "{"
					+ "\"PageNumber\": 1,"
					+ "\"PageSize\": 100,"
					+ "\"SearchCriteria\": {"
					+ "\"CriteriaType\": \"GetInBatches\","
					+ "},"
					+ "\"TrackingId\": \"7c48e923-b1e6-49b4-9f17-e0e2215c5b83\","
					+ "\"Identity\": {" + "\"Puid\": \"0003BFFDCC6B0FA4\","
					+ "\"AuthProvider\": \"WINDOWS_LIVE\","
					+ "\"EmailId\": \"convergentmobile@gmail.com\" }" + "}";

			//System.out.println(getData);

			String url = "https://bptestwebsite.cloudapp.net/trustedPartnerApi/v1/GetBusinesses";

			URL obj = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();

			conn.setSSLSocketFactory(getSocketFactoryFromPEM());

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/json;charset=UTF-8");

			OutputStream os = conn.getOutputStream();
			os.write(getData.getBytes());
			os.flush();

			String responseMessage = conn.getResponseMessage();
			//System.out.println(responseMessage);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			//System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getBusinessByStore() {
		try {
			String getData = "{"
					+ "\"PageNumber\": 1,"
					+ "\"PageSize\": 100,"
					+ "\"SearchCriteria\": {"
					+ "\"CriteriaType\": \"SearchByStoreIds\","
					+ "\"StoreIds\": [\"Store_1_LBL_004\"],"
					+ "},"
					+ "\"TrackingId\": \"7c48e923-b1e6-49b4-9f17-e0e2215c5b83\","
					+ "\"Identity\": {" + "\"Puid\": \"0003BFFDCC6B0FA4\","
					+ "\"AuthProvider\": \"WINDOWS_LIVE\","
					+ "\"EmailId\": \"convergentmobile@gmail.com\" }" + "}";

			//System.out.println(getData);

			String url = "https://www.bingplaces.com/trustedPartnerApi/v1/GetBusinesses";

			URL obj = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
			
			ClassLoader classLoader = BingClient.class.getClassLoader();

			//conn.setSSLSocketFactory(buildSslContext(classLoader.getResourceAsStream("BingPlacesTPCert2018March.pem")));

			conn.setSSLSocketFactory(getSocketFactoryFromPEM());

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/json;charset=UTF-8");

			OutputStream os = conn.getOutputStream();
			os.write(getData.getBytes());
			os.flush();

			String responseMessage = conn.getResponseMessage();
			//System.out.println(responseMessage);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			//System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> getBingCategoryDetails(Integer clientId,
			String store) {
		Map<String, String> map = new HashMap<String, String>();

		Map<String, String> bingMap = service.getBingCategoryDetails(clientId,
				store);
		Set<String> keySet = bingMap.keySet();
		for (String key : keySet) {
			String value = bingMap.get(key);
			// System.out.println(key + "==================="+ string );
			map.put(key, value);
		}

		return map;
	}

	public static void main(String[] args) {
		// createBusinessOnBing(new UploadBusinessBean());
		getBusinessByStore();

		/*
		 * String store = null; String Operation = null; String Status = null;
		 * String errorMessage = null;
		 * 
		 * JSONParser parser = new JSONParser(); try { Object obj = parser
		 * .parse(new FileReader("C:\\Code\\add_response.txt"));
		 * 
		 * JSONObject jsonObject = (JSONObject) obj;
		 * 
		 * 
		 * JSONObject sucessOrFail = ((JSONObject)
		 * jsonObject.get("CreatedBusinesses")); JSONObject errors =
		 * ((JSONObject) jsonObject.get("Errors"));
		 * 
		 * System.out.println(errors);
		 * 
		 * if(!sucessOrFail.isEmpty()) {
		 * 
		 * JSONObject storeObj = (JSONObject)sucessOrFail.get("0");
		 * if(storeObj!=null) { store = (String)storeObj.get("StoreId");
		 * Operation = (String)storeObj.get("Operation"); Status =
		 * (String)storeObj.get("Status"); errorMessage =
		 * (String)storeObj.get("ErrorMessage"); } } else if(!errors.isEmpty())
		 * { JSONObject storeObj = (JSONObject)errors.get("0");
		 * if(storeObj!=null) { store = (String)storeObj.get("StoreId");
		 * JSONArray errorsArray = (JSONArray) storeObj .get("BusinessErrors");
		 * 
		 * errorMessage = ((JSONObject)
		 * errorsArray.get(0)).get("ErrorMessage").toString();
		 * System.out.println(errorMessage); } }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}
}
