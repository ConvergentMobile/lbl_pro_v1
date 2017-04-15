package com.whitespark.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.business.common.dto.LocalBusinessDTO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SerpAPI {

	protected static Logger logger = Logger.getLogger(SerpAPI.class);

	public static void postSearchRequestToSerp(LocalBusinessDTO localBusinessDTO) {
		String serpUrl = "http://serp.synergetica.net/api/report_request_package";
		try {
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpPost httppost = new HttpPost(
					"http://serp.synergetica.net/api/report_request_package");

			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
					"serpuser1@mail.com" + ":" + "12345serp678qwerty");
			httppost.addHeader(BasicScheme.authenticate(creds, "US-ASCII",
					false));

			String phone = localBusinessDTO.getLocationPhone();

			String contactNumber = String.format("%s-%s-%s",
					phone.substring(0, 3), phone.substring(3, 6),
					phone.substring(6, 10));

			String postEnity = "reportRequests[0][searchText]=" + contactNumber
					+ "&reportRequests[0][keywords]="
					+ localBusinessDTO.getCompanyName()
					+ "&reportRequests[0][zipCode]="
					+ localBusinessDTO.getLocationZipCode() + "&clientId="
					+ localBusinessDTO.getClientId() + "-"
					+ localBusinessDTO.getStore() + "++";

			logger.info("Request Entiry: " + postEnity);

			StringEntity entity = new StringEntity(postEnity);
			entity.setContentType("application/x-www-form-urlencoded");
			httppost.setEntity(entity);
			org.apache.http.HttpResponse response = client.execute(httppost);

			BufferedReader rd;
			rd = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {

				JsonParser j = new JsonParser();

				logger.info("Serp Post response: " + line);

				System.out.println("line: " + line);
				JsonObject obj = (JsonObject) j.parse(line);
				String customSerachId = (String) obj.get("id").toString();

				logger.info("Serp Post Request Id: " + customSerachId);
			}

			client.close();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("There was a issue while posting search request for whitespark with url:"
					+ serpUrl + ", the error is: " + e.getLocalizedMessage());
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws ClientProtocolException,
			IOException {

		String phone = "1234567890";

		String contactNumber = String.format("%s-%s-%s", phone.substring(0, 3),
				phone.substring(3, 6), phone.substring(6, 10));

		System.out.println(contactNumber);

		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(
				"http://serp.synergetica.net/api/report_request_package");

		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
				"serpuser1@mail.com" + ":" + "12345serp678qwerty");
		httppost.addHeader(BasicScheme.authenticate(creds, "US-ASCII", false));
		StringEntity entity = new StringEntity(
				"reportRequests[0][searchText]=111-111-1121&reportRequests[0][keywords]=Company1&reportRequests[0][zipCode]=90220&clientId=899936");
		entity.setContentType("application/x-www-form-urlencoded");
		httppost.setEntity(entity);
		org.apache.http.HttpResponse response = client.execute(httppost);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Parse our JSON response
			JsonParser j = new JsonParser();

			System.out.println("line: " + line);
			JsonObject obj = (JsonObject) j.parse(line);
			String customSerachId = (String) obj.get("id").toString();

			System.out.println(customSerachId);
		}

	}

}
