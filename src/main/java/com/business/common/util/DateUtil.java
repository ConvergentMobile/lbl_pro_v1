package com.business.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Vasanth
 *
 */
public class DateUtil {

	/**
	 * getCurrentDate
	 * @param dateFomat
	 * @return
	 */
	public static Date getCurrentDate(String dateFomat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFomat);
		String currentDate = sdf.format(new Date());
		return getDate(sdf, currentDate);
	}

	/**
	 * getDate
	 * @param sdf
	 * @param date
	 * @return
	 */
	public static Date getDate(SimpleDateFormat sdf, String date) {
		Date startDateValue = null;
		try {
			startDateValue = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startDateValue;
	}
	
	/**
	 * getDate
	 * @param sdf
	 * @param date
	 * @return
	 */
	public static Date getDate(SimpleDateFormat sdf, Date date) {
		Date startDateValue = null;
		try {
			startDateValue = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startDateValue;
	}
	
	/**
	 * getDate
	 * @param sdf
	 * @param date
	 * @return
	 */
	public static Date getDate(String dateFomat,Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFomat);
		Date startDateValue = null;
		try {
			startDateValue = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startDateValue;
	}
	/**
	 * getDate 
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDate(Date date){
		return new java.sql.Date(date.getTime());
	}
	
	/**
	 * getDate
	 * @param dateFomat
	 * @param date
	 * @return
	 */
	public static Date getDate(String dateFomat,String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFomat);
		Date startDateValue = null;
		try {
			startDateValue = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startDateValue;
	}

	public static long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public static boolean validateTime(long timeStamp, long currentTimeStamp) {
		long timeDiff = currentTimeStamp-timeStamp;
		return (timeDiff<LBLConstants.PASS_RESET_LINK_DDEFAULT_VALID_TIME);
	}
}
