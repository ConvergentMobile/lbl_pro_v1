package com.whitespark.ws;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/*import org.junit.Test;*/

public class TestAuth {
	protected Logger logger = Logger.getLogger(TestAuth.class);
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private static final String secretKey = "43hfjdk0cbnd34gk78ls";
	
	//@Test
	public void testWS() {
		try {			
			String wsKey = "62121a3a70a15d4e9355e7c0106a0d5f";
			String wsSecret = "3bd7642a98a9797b8a0833f322372ff4";

			String apiMethod = "search";
			//String requestParams = "city=Edmonton&country=Canada&custom=Custom+Value&region=Alberta&search_term=Edmonton+Flowers&type=keyphrase";
			String requestParams = "";
			//requestParams = "business_name=Liberty+Tax+Service&city=San+Francisco&country=US&custom=123123&region=California&search_term=(415)821-9002&type=phone";
			requestParams = "city=San+Francisco&country=US&custom=123923&region=California&search_term==Liberty+Tax+Service+94110&type=keyphrase";
			//requestParams = "city=San+Mateo&country=US&custom=123123&region=California&search_term=Kingfish+Restaurant&type=keyphrase";
			
			String url = "http://lcf.whitespark.ca/api/v1/" 
					+ apiMethod
					+ "?key=" + wsKey
					+ "&" + requestParams
					+ "&sig=" + md5( apiMethod + "&" + requestParams + "&" + wsSecret);

			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String md5(String input) {
		// TODO Auto-generated method stub

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

	//@Test
	public void testProcessJSON() throws Exception {
		String infile = "E:\\KrishnaPillai\\Prod\\line.txt";
		BufferedReader in = new BufferedReader(new FileReader(infile));
		String line = null;
		while ((line = in.readLine()) != null) {
			System.out.println("line: " + line);
			processJSON(line);
		}
	}
	
	public void processJSON(String s) throws Exception {
		JSONParser parser = new JSONParser();
				        
		JSONObject obj = (JSONObject) parser.parse(s);
        JSONArray sresults = (JSONArray) obj.get("search_results");
        Object object = obj.get("custom");
        
        System.out.println("Custom Value is: "+ object.toString());
        
        for (int i = 0; i < sresults.size(); i++) {
        	JSONObject sresult = (JSONObject) sresults.get(i);
        	System.out.println("rank, bus name: " + sresult.get("pack_rank") + " --- " +  sresult.get("name"));
        	
        	JSONObject domains = (JSONObject) sresult.get("domains");
        	for (Iterator iterator = domains.keySet().iterator(); iterator.hasNext();) {
        	    String key = (String) iterator.next();
            	System.out.println("domain: " + key);
            	
            	JSONObject domain = (JSONObject) domains.get(key);
            	System.out.println("submit_url: " + domain.get("submit_url"));
            	
            	JSONArray paths = (JSONArray) domain.get("paths");
                for (int j = 0; j < paths.size(); j++) {
                	System.out.println("path: " + paths.get(j));
                }
        	}
        }
	}
}

