package com.business.web.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class ReadExcelFile {
public static void main(String[] args) {
	List<String> array=new ArrayList<String>();
	String s="name's";
	if(s.contains("'")){
		s = s.replace("'", "\\'");
	}



	System.out.println(s);

	 
	
	// System.out.println(array);
	
}

}