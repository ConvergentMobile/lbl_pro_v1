package com.business.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadCVS {
public static void main(String[] args) throws ParseException {
	
String start_dt="2016-04-24";
DateFormat parser = new SimpleDateFormat("yyyy-MM-dd"); 
Date date = (Date) parser.parse(start_dt);
System.out.println(date);



}
}
