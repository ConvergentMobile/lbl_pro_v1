package com.whitespark.ws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.BeanUtils;

import com.business.common.dto.SearchDomainDTO;
import com.business.common.util.SubmissionUtil;
import com.business.model.pojo.SearchDomains;
import com.business.service.BusinessService;

public class wsProcessJSON {

	private static Logger logger = Logger.getLogger(wsProcessJSON.class);

	private static String[] domainsList ={ "https://plus.google.com/",
		"http://plus.google.com/", "https://local.yahoo.com/",
		"http://local.yahoo.com/", "http://www.bing.com/",
		"https://www.bing.com/", "http://www.yelp.com/",
		"https://www.yelp.com/", "http://www.myyp.com/",
		"https://www.myyp.com/", "http://www.citysearch.com/",
		"https://www.citysearch.com/", "http://www.mapquest.com/",
		"https://www.mapquest.com/", "http://www.superpages.com/",
		"https://www.superpages.com/", "http://www.whitepages.com/",
		"https://www.whitepages.com/", "http://www.yellowbook.com/",
		"https://www.yellowbook.com/" };

	public static void main(String[] args) throws Exception {

		InputStream in = new FileInputStream(new File(
				"E:\\Krishnapillai\\MB13.txt"));

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
		}
		System.out.println(out.toString()); // Prints the string content read
											// from input stream
		reader.close();
		String data = out.toString();

		processJSON(data);

		/*
		 * String val = "'FCS-3306'";
		 * 
		 * val = val.replace("'", "");
		 * 
		 * System.out.println(val);
		 */

	}

	public static void parseData(BusinessService service) throws Exception {

		InputStream in = new FileInputStream(new File(
				"E:\\Krishnapillai\\Prod\\line.txt"));

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
		}
		System.out.println(out.toString()); // Prints the string content read
											// from input stream
		reader.close();
		String data = out.toString();

		processJSON(data);
	}

	public static void processJSON(String data) throws Exception {
		//logger.info("Rseponse from Whitespark is: " + data);

		try {

			JSONParser parser = new JSONParser();

			if (data != null) {

				JSONObject obj = (JSONObject) parser.parse(data);
				JSONArray sresults = (JSONArray) obj.get("search_results");
				String customSerachId = (String) obj.get("custom");

				customSerachId = customSerachId.replace("'", "");

				logger.info("customSerachId: " + customSerachId);
				logger.info("Data received from Whitespark for the custom serach: "
						+ customSerachId + "is: " + data);

				SubmissionUtil util = new SubmissionUtil();
				List<String> asList = Arrays.asList(domainsList);
				for (int i = 0; i < sresults.size(); i++) {
					JSONObject sresult = (JSONObject) sresults.get(i);
					String listingName = (String) sresult.get("name");

					JSONObject domains = (JSONObject) sresult.get("domains");

					for (Iterator iterator = domains.keySet().iterator(); iterator
							.hasNext();) {

						String key = (String) iterator.next();

						JSONObject domain = (JSONObject) domains.get(key);

						if (asList.contains(key)) {
							logger.debug("**********Start Domains Search for the custom search: "
									+ customSerachId);
							String submitUrl = (String) domain
									.get("submit_url");

							StringBuffer path = new StringBuffer();
							JSONArray paths = (JSONArray) domain.get("paths");
							String domainAuthority = (String) domain.get("moz_da");
							
							System.out.println("domainAuthority:::"+domainAuthority);
							for (int j = 0; j < paths.size(); j++) {
								System.out.println("path: " + paths.get(j));
								String pathUrl = (String) paths.get(j);
								if (path.length() > 0) {

									path.append(",").append(pathUrl);
								} else {
									path.append(pathUrl);
								}
							}

							SearchDomainDTO domainDTO = new SearchDomainDTO();
							domainDTO.setSearchId(customSerachId);
							domainDTO.setListingName(listingName);
							domainDTO.setDomainAuthority(domainAuthority);
							domainDTO.setDomain(key);
							domainDTO.setSubmitUrl(submitUrl);
							domainDTO.setPath(path.toString());
							String domainName = key.replace("http://", "")
									.replace("https://", "")
									.replace("www.", "");
							domainName = domainName.replace("/", "");

							domainDTO.setDomainName(domainName);
							domainDTO.setDateSearched(new Date());
							domainDTO.setPathsCount(paths.size());

							Session session = HibernateUtil.getSessionFactory()
									.openSession();

							Transaction beginTransaction = session
									.beginTransaction();

							SearchDomains domainsvalue = new SearchDomains();
							BeanUtils.copyProperties(domainDTO, domainsvalue);
							session.save(domainsvalue);
							beginTransaction.commit();

							// service.saveSearchDomainInfo(domainDTO);
							logger.debug("********End:  Domains Search for the custom search: "
									+ customSerachId);
						}
					}
				}
			} else {
				logger.info("Whitespark responded with:  " + data);
			}
		} catch (Exception e) {
			logger.error("There was a error while Inserting Whitespark returned domains into database");
			e.printStackTrace();
		}
	}

}
