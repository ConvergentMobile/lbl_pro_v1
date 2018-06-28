package com.business.common.util;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author lbl_dev
 * 
 */
public class UnAccentUtil {
	
	
		  public static String formatString(String s) {
		    String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		    return temp.replaceAll("[^\\p{ASCII}]", "");
		  }
		
}
