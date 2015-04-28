package com.partners.infogroup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WhiteSpark {

	public static String md5(String input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(input.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}

	public static void main(String[] args) {

		try {
			String wsKey = "62121a3a70a15d4e9355e7c0106a0d5f";
			String wsSecret = "3bd7642a98a9797b8a0833f322372ff4";

			String apiMethod = "search";
			String requestParams = "city=Edmonton&country=Canada&custom=Custom+Value&region=Alberta&search_term=Edmonton+Flowers&type=keyphrase";

			String url = "http://lcf.whitespark.ca/api/v1/" + apiMethod
					+ "?key=" + wsKey + "&" + requestParams + "&sig="
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

			// print result
			System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
