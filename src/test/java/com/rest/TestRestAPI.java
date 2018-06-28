package com.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * 
 * @author lbl_dev
 *
 */
public class TestRestAPI {

	public static void main(String args[]) throws Exception {
		 uploadFile();

		//addUpdateDeleteTest();
	}
	/**
	 * validate upload file rest api 
	 */
	public static void uploadFile() {

		try {
			String targetURL = "http://localhost:8080/lbl_pro/uploadfile";
			CloseableHttpClient closableClient = HttpClientBuilder.create()
					.build();

			String file = "C:\\lbl\\LBL.xlsx";

			HttpPost httpPost = new HttpPost(targetURL);

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addBinaryBody("file", new File(file),
					ContentType.APPLICATION_OCTET_STREAM,
					"Sprint_LBL_Pro_MasterTemplate.xls");
			HttpEntity multipart = builder.build();

			httpPost.setEntity(multipart);

			CloseableHttpResponse response = closableClient.execute(httpPost);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * Validate add/update/delete API
	 */
	public static void addUpdateDeleteTest() {
		String store = "166";
		String storeUpdate = "66";
		String clientId = "3456";
		String companyName = "Sprint Store";
		String locationAddress = "2500 Moreland Rd 1093";
		String locationCity = "Willow Grove";
		String locationState = "PA";
		String countryCode = "US";
		String locationZipCode = "19090";
		String locationPhone = "2156570077";

		String fax = "";
		String shortWebAddress = "storelocator.sprint.com/locator/";
		String webAddress = "http://storelocator.sprint.com/locator/";

		String desc = "At Sprint stores, you get the phones you want with the plans you can't pass up. Come visit our helpful and knowledgeable staff of experts ready to answer your important questions. Get the Sprint Family Share Pack. Our \"Ready Now\" program gets you using your new phone by the time you walk out the front door. And with our Buyback program, we offer account credits for your old eligible device. Come visit or call to find what you're looking for at your local Sprint Store";
		String descShort = "Unlimited high-speed data, talk & text while on the Sprint network. Award-winning customer service. Innovative ways to get the hottest new devices. See details";
		String businessDesc = StringEscapeUtils.escapeJava(desc);
		String businessDescShort = StringEscapeUtils.escapeJava(descShort);

		String category1 = "481207";
		String category2 = "481205";
		String category3 = "";
		String category4 = "";
		String category5 = "";
		String logoLink = "http://www.smgdm.com/profiles/images/sprint_logo.png";


		String youTubeLink = "https://www.youtube.com/watch?v=LUGlp4MT3WI&feature=youtu.be";
		String products = "Unlimited data (available on select plans), 4G LTE, HD Voice, phone deals and promotions, accessories, home cable solutions with Sprint Phone Connect, internet service";

		String storeDelete = "062";

		try {

			String authId = "9173299";
			String authKey = "EoaATKtdKc";

			String request = "{" +
					"\"authKey\": \"" + authKey + "\","
					+ "\"authId\": \"" + authId + "\","
					+ "\"stores\": [" +
					"{"
						+ "\"clientId\":\"" + clientId + "\","
						+ "\"store\":\""+ store + "\","
						+ "\"actionCode\":\"A\","
						+ "\"companyName\":\"" + companyName + "\","
						+ "\"alternativeName\":\"\","
						+ "\"anchorOrHostBusiness\":\"\","
						+ "\"locationAddress\":\"" + locationAddress + "\","
						+ "\"suite\":\"\"," 
						+ "\"locationCity\":\"" + locationCity
						+ "\"," + "\"locationState\":\"" + locationState + "\","
						+ "\"locationZipCode\":\"" + locationZipCode + "\","
						+ "\"locationPhone\":\"" + locationPhone + "\","
						+ "\"businessDescription\":\"" + businessDesc + "\","
						+ "\"countryCode\":\"" + countryCode + "\","
						+ "\"businessDescriptionShort\":\"" + businessDescShort	+ "\"," 
						+ "\"fax\":\"" + fax + "\","
						+ "\"tollFree\":\"\","
						+ "\"tty\":\"\","
						+ "\"mobileNumber\":\"\"," + "\"additionalNumber\":\"\","
						+ "\"category1\":\"" + category1 + "\","
						+ "\"category2\":\"" + category2 + "\","
						+ "\"category3\":\"" + category3 + "\","
						+ "\"category4\":\"" + category4 + "\","
						+ "\"category5\":\"" + category5 + "\","
						+ "\"primaryContactFirstName\":\"\","
						+ "\"primaryContactLastName\":\"\","
						+ "\"contactTitle\":\"\"," + "\"contactEmail\":\"\","
						+ "\"locationEmployeeSize\":\"\","
						+ "\"title_ManagerOrOwner\":\"\","
						+ "\"professionalTitle\":\"\","
						+ "\"professionalAssociations\":\"\","
						+ "\"shortWebAddress\":\"\"," 
						+ "\"webAddress\":\"\","
						+ "\"aMEX\":\"\"," + "\"discover\":\"\","
						+ "\"visa\":\"\"," + "\"masterCard\":\"\","
						+ "\"dinersClub\":\"\","
						+ "\"debitCard\":\"\"," 
						+ "\"storeCard\":\"\","
						+ "\"otherCard\":\"\","
						+ "\"cash\":\"\","
						+ "\"check\":\"\","
						+ "\"travelersCheck\":\"\"," 
						+ "\"financing\":\"\","
						+ "\"googleCheckout\":\"\"," 
						+ "\"invoice\":\"\","
						+ "\"payPal\":\"\","
						+ "\"couponLink\":\"\"," 
						+ "\"twitterLink\":\"\","
						+ "\"linkedInLink\":\"\"," 
						+ "\"facebookLink\":\"\","
						+ "\"alternateSocialLink\":\"\","
						+ "\"youTubeOrVideoLink\":\"\","
						+ "\"googlePlusLink\":\"\","
						+ "\"myspaceLink\":\"\","
						+ "\"logoLink\":\"" + logoLink + "\","
						+ "\"pinteristLink\":\"\","
						+ "\"helpLink\":\"\"," 
						+ "\"products\":\"\","
						+ "\"services\":\"\","
						+ "\"productsOrServices_combined\":\"\","
						+ "\"brands\":\"\","
						+ "\"client\":\"\"," 
						+ "\"keywords\":\"\","
						+ "\"languages\":\"\"," 
						+ "\"yearEstablished\":\"\","
						+ "\"tagline\":\"\","
						+ "\"locationEmail\":\"\","
						+ "\"serviceArea\":\"\","
						+ "\"mondayOpen\":\"\","
						+ "\"mondayClose\":\"\","
						+ "\"tuesdayOpen\":\"\","
						+ "\"tuesdayClose\":\"\"," 
						+ "\"wednesdayOpen\":\"\","
						+ "\"wednesdayClose\":\"\","
						+ "\"thursdayOpen\":\"\","
						+ "\"thursdayClose\":\"\","
						+ "\"fridayOpen\":\"\"," 
						+ "\"fridayClose\":\"\","
						+ "\"saturdayOpen\":\"\","
						+ "\"sundayOpen\":\"\","
						+ "\"sundayClose\":\"\","
						+ "\"foursquareLink\":\"\"," + "\"instagramLink\":\"\","
						+ "\"menuLink\":\"\"," + "\"yelpLink\":\"\","
						+ "\"addressprivacyflag\":\"\"" 
						+ "}," 
						+ "{"
							+ "\"clientId\":\"" + clientId + "\"," 
							+ "\"store\":\""+ storeUpdate + "\","
							+ "\"actionCode\": \"U\","
							+ "\"businessDescription\":\"" + businessDesc + "\","
							+ "\"companyName\":\"" + companyName + "\","
							+ "\"locationAddress\": \"" + locationAddress + "\"},"
						+ "{" + "\"clientId\":\"" + clientId + "\","
							  + "\"store\": \"" + storeDelete + "\","
							  + "\"actionCode\": \"D\""
						 + "}"
						+ "]"
						+ "}";

			System.out.println(request);

			String targetURL = "http://localhost:8080/lbl_pro/storeapi";
			CloseableHttpClient closableClient = HttpClientBuilder.create()
					.build();

			HttpPost httpPost = new HttpPost(targetURL);

			StringEntity entity = new StringEntity(request);
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			CloseableHttpResponse response = closableClient.execute(httpPost);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
