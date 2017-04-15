package com.business.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Vasanth
 * 
 */
public class DateUtil {
	/**
	 * getDate
	 * 
	 * @param sdf
	 * @param date
	 * @return
	 */
	public static Date getDateByDayOfWeek(String dateFomat, String freequency,
			String dayOfWeek) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFomat);
		Date date = null;
		Calendar c = Calendar.getInstance();
		Calendar currentDate = Calendar.getInstance();
		Date today = currentDate.getTime();
		try {

			if (freequency.equalsIgnoreCase("Weekly")) {
				
				if(dayOfWeek==null) {
					dayOfWeek ="monday";
				}

				if (dayOfWeek.equalsIgnoreCase("monday")) {
					c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				}
				if (dayOfWeek.equalsIgnoreCase("tuesday")) {
					c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
				}
				if (dayOfWeek.equalsIgnoreCase("wednesday")) {
					c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
				}
				if (dayOfWeek.equalsIgnoreCase("thursday")) {
					c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
				}
				if (dayOfWeek.equalsIgnoreCase("friday")) {
					c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
				}
				if (dayOfWeek.equalsIgnoreCase("saturday")) {
					c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				}
				if (dayOfWeek.equalsIgnoreCase("sunday")) {
					c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				}

				if (c.getTime().before(today)) {
					c.add(Calendar.DATE,7);
				}
			}
			if (freequency.equalsIgnoreCase("Daily")) {
				c.add(Calendar.DATE, 0);
			}
			if (freequency.equalsIgnoreCase("Monthly")) {
				
				if(dayOfWeek==null) {
					dayOfWeek ="1";
				}

				c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayOfWeek));

				

				if (c.getTime().before(today)) {
					c.add(Calendar.MONTH,1);
				}
			}

			date = sdf.parse(sdf.format(c.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * getCurrentDate
	 * 
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
	 * 
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
	 * 
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
	 * 
	 * @param sdf
	 * @param date
	 * @return
	 */
	public static Date getDate(String dateFomat, Date date) {
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
	public static Date getDate(Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * getDate
	 * 
	 * @param dateFomat
	 * @param date
	 * @return
	 */
	public static Date getDate(String dateFomat, String date) {
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
		long timeDiff = currentTimeStamp - timeStamp;
		return (timeDiff < LBLConstants.PASS_RESET_LINK_DDEFAULT_VALID_TIME);
	}
}
