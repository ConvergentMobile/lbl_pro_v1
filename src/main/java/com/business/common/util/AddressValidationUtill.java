package com.business.common.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.business.web.bean.UploadBusinessBean;

/**
 * 
 * @author Vasanth
 * 
 */

public class AddressValidationUtill {
	
	static Logger logger = Logger.getLogger(AddressValidationUtill.class);
	
	public static void main(String[] args) {
		
		UploadBusinessBean uploadBean = new UploadBusinessBean();
		uploadBean.setLocationAddress("Indiana State Route 933");
		uploadBean.setLocationCity("South Bend");
		uploadBean.setLocationState("NV");
		uploadBean.setLocationState("46637");
		validateAddressWithSS(uploadBean);
	}

	public static boolean validateAddressWithSS(UploadBusinessBean uploadBean) {
		
		if(uploadBean.getCountryCode().equalsIgnoreCase("CA")){
			return true;
		}

		String authId = LBLConstants.SMART_AUTHID;
		String authToken = LBLConstants.SMART_AUTHTOKEN;

		// The REST endpoint
		String url = "https://api.smartystreets.com/street-address/?auth-id="
				+ authId + "&auth-token=" + authToken;

		String response = "";

		JSONObject addr2 = new JSONObject();
		addr2.put("street", uploadBean.getLocationAddress());
		addr2.put("city", uploadBean.getLocationCity());
		addr2.put("state", uploadBean.getLocationState());
		addr2.put("zip", uploadBean.getLocationZipCode());

		// Build each address into the JSON array
		JSONArray list = new JSONArray();
		list.add(addr2);

		// The request to send to the server
		String req = list.toString();
		int len = req.length();

		try {
			URL u = new URL(url);

			try {

				URLConnection urlConn = u.openConnection();
				urlConn.setDoInput(true);
				urlConn.setDoOutput(true);
				urlConn.setUseCaches(false);
				urlConn.setRequestProperty("Content-Length",
						Integer.toString(len));

				DataOutputStream outgoing = new DataOutputStream(
						urlConn.getOutputStream());
				String content = req;
				outgoing.writeBytes(content);
				outgoing.flush();
				outgoing.close();

				// Save the response (a JSON string)
				DataInputStream incoming = new DataInputStream(
						urlConn.getInputStream());
				String str;
				while ((str = incoming.readLine()) != null)
					response += str;
				logger.debug("Response from Smartystreets is : " + response);
				if (response != null && response.equalsIgnoreCase("[]")) {
					logger.info("Smartystrets validated address as Invalid Address for store: "+uploadBean.getStore());
					return false;
				}
				
				incoming.close();
			} catch (Exception e) {
				logger.error(
						"There is a error while validating address with Smartystreets, error is:  ",
						e);
				return false;
			}
		} catch (MalformedURLException me) {
			logger.error(
					"There is a error while validating address with Smartystreets, error is:  ",
					me);
			return false;
		}
		return true;

	}
}