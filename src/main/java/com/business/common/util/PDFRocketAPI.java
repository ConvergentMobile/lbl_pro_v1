/*package com.business.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class PDFRocketAPI {

	*//**
	 * Rocket Java API Example Run thru commandline Param 1: APIKey Param 2: URL
	 * or HTML add quote if you have spaces. use single quotes instead of double
	 * Param 3: Any extra params you want to add i.e &extra1=value&extra2=value
	 * 
	 *//*

	static String BaseURL = "http://api.html2pdfrocket.com/pdf";

	public static String createPDF(String pathtoPDF, String url, String fileName) {
		String API = "ff60aa04-d396-44bc-a262-e75b163071bc";
		// String url =
		// "http://23.23.203.174/lbl_pro/pdfGMBBrand.htm?data=Aspen Dental-08/08/2017-12/05/2017";
		// String FileName = "AspenDental-GMB-20170808-20171205.pdf";
		String ExtraParams = "JavascriptDelay=3000&MarginBottom=15&MarginLeft=0.25&MarginRight=0.25&MarginTop=0.25&PageWidth=215.9&PageHeight=279.4&FooterUrl=http://23.23.203.174/lbl_pro/pdfheader.htm";

		String fileName = getFile(API, url, pathtoPDF, fileName + ".pdf",
				ExtraParams);
		return fileName;
	}

	private static void getFile(String APIKey, String value, String pathtoPDF,
			String Filename, String ExtraParams) {
		URL url;
		String Params = "";
		try {
			if (ExtraParams != null && !"".equals(ExtraParams)) {
				Params = ExtraParams;
				if (!Params.substring(0, 1).equals("&")) {
					Params = "&" + Params;
				}
			}

			value = URLEncoder.encode(value,
					java.nio.charset.StandardCharsets.UTF_8.toString());
			value += Params;

			// Validate parameters
			if (APIKey == null || "".equals(APIKey))
				throw (new Exception("API key is empty"));
			if (Filename == null || "".equals(Filename))
				throw (new Exception("Filename is empty"));

			// Append parameters for API call
			url = new URL(BaseURL + "?apikey=" + APIKey + "&value=" + value);

			// Download PDF file
			URLConnection connection = url.openConnection();
			InputStream Instream = connection.getInputStream();

			// Write PDF file
			BufferedInputStream BISin = new BufferedInputStream(Instream);
			FileOutputStream FOSfile = new FileOutputStream(pathtoPDF
					+ File.separatorChar + Filename);
			BufferedOutputStream out = new BufferedOutputStream(FOSfile);

			int i;
			while ((i = BISin.read()) != -1) {
				out.write(i);
			}

			// Clean up
			out.flush();
			out.close();
			System.out.println("File " + Filename + " created");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}*/