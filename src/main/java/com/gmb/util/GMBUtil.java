package com.gmb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GMBUtil {

	public static void main(String[] args) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String dateInString = "2014-10-05T15:23:01Z";

		try {

			Date date = new Date();
			//System.out.println(date);

			//System.out.println("time zone : " + TimeZone.getDefault().getID());
			System.out.println(formatter.format(date));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
