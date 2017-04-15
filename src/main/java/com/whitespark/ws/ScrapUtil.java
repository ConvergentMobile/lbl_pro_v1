package com.whitespark.ws;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;

import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.common.dto.SearchDomainDTO;
import com.business.model.pojo.FailedScrapingsEntity;
import com.business.model.pojo.RenewalReportEntity;
import com.business.service.BusinessService;

/*import org.junit.Test;*/

public class ScrapUtil {
	protected static Logger logger = Logger.getLogger(ScrapUtil.class);

	public static void main(String[] args) {
		String formattedPhone = getFormattedPhone("Local:4129222220");
		System.out.println(formattedPhone);
	}

	BusinessService service;

	public void scrapDomains(BusinessService service) throws Exception {
		this.service = service;

		List<LocalBusinessDTO> listOfBusinessInfo = service.getAllListings();

		// List<LocalBusinessDTO> listOfBusinessInfo =
		// service.getListOfBussinessByBrandName("PNC Bank");

		logger.info("Total stores which needs to be scrapped are: "
				+ listOfBusinessInfo.size());
		for (LocalBusinessDTO localBusinessDTO : listOfBusinessInfo) {

			// Start: google
			List<SearchDomainDTO> domainsByStoreForGoogle = service
					.getDomainsByStore(localBusinessDTO.getStore(),
							"plus.google.com");
			scrapListingFromGoogle(domainsByStoreForGoogle, localBusinessDTO);
			// End: google

			// Start: Bing
			List<SearchDomainDTO> domainsByStoreForBing = service
					.getDomainsByStore(localBusinessDTO.getStore(), "bing.com");

			scrapListingFromBing(domainsByStoreForBing, localBusinessDTO);

			// End: Bing

			// Start: Yahoo
			List<SearchDomainDTO> domainsByStoreForYahoo = service
					.getDomainsByStore(localBusinessDTO.getStore(),
							"local.yahoo.com");
			scrapListingFromYahoo(domainsByStoreForYahoo, localBusinessDTO);
			// End: Yahoo

			// Start: Yelp
			List<SearchDomainDTO> domainsByStoreForYelp = service
					.getDomainsByStore(localBusinessDTO.getStore(), "yelp.com");
			scrapListingFromYelp(domainsByStoreForYelp, localBusinessDTO);
			// Start: Yelp

			// Start: Yp
			List<SearchDomainDTO> domainsByStoreForYp = service
					.getDomainsByStore(localBusinessDTO.getStore(), "myyp.com");
			scrapListingFromYP(domainsByStoreForYp, localBusinessDTO);
			// End: Yp

			// Start: CitySearch
			// List<SearchDomainDTO> domainsByStoreCitySearch = service
			// .getDomainsByStore(localBusinessDTO.getStore(),
			// "citysearch.com");
			// scrapListingFromCitySearch(domainsByStoreForYp,
			// localBusinessDTO);
			// End: CitySearch

			// Start: MapQuest
			List<SearchDomainDTO> domainsByStoreForMapQuest = service
					.getDomainsByStore(localBusinessDTO.getStore(),
							"mapquest.com");
			scrapListingFromMapQuest(domainsByStoreForMapQuest,
					localBusinessDTO);
			// End: MapQuest

			// Start: MapQuest
			List<SearchDomainDTO> domainsByStoreForSuperPages = service
					.getDomainsByStore(localBusinessDTO.getStore(),
							"superpages.com");
			scrapListingFromSuperPages(domainsByStoreForSuperPages,
					localBusinessDTO);
			// End: MapQuest

			// Start: yellowbook
			List<SearchDomainDTO> domainsByStoreForYellowBook = service
					.getDomainsByStore(localBusinessDTO.getStore(),
							"yellowbook.com");
			scrapListingFromYellowBook(domainsByStoreForYellowBook,
					localBusinessDTO);

			// End: yellowbook
			/*
			 * // Start: whitepages List<SearchDomainDTO>
			 * domainsByStoreForWhitePages = service
			 * .getDomainsByStore(localBusinessDTO.getStore(),
			 * "whitepages.com");
			 * scrapListingFromSuperPages(domainsByStoreForWhitePages,
			 * localBusinessDTO); // End: whitepages
			 */
		}

	}

	public void scrapListingFromYellowBook(
			List<SearchDomainDTO> domainsByStoreForYellowBook,
			LocalBusinessDTO listingDTO) throws Exception {
		String businessName = "";
		String addr = "";
		String locality = "";
		String region = "";
		String zip = "";
		String phone = "";
		String targetURL = "";
		String directory = "yellowbook";
		try {

			List<CheckReportDTO> allMatchings = new ArrayList<CheckReportDTO>();
			for (SearchDomainDTO searchDomainDTO : domainsByStoreForYellowBook) {
				CheckReportDTO checkReportDTO = new CheckReportDTO();
				String paths[] = searchDomainDTO.getPath().split(",");

				String path = "";
				for (int i = 0; i < paths.length; i++) {
					path = paths[i].substring(1);
				}

				targetURL = searchDomainDTO.getDomain() + path;

				logger.info("scraping url for the store: "
						+ searchDomainDTO.getSearchId() + ", is: " + targetURL);
				String URL = "http://www.yellowbook.com/profile/pnc-bank_1860053414.html";
				Document document = Jsoup
						.connect(URL)
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
						.get();

				Elements bname = document.select("h2");
				Elements website = document.select("a.s_website");
				for (Element element : bname) {
					if (element.attr("id").equals("profileName")) {
						businessName = element.text();
					}
				}

				Elements el = document.select("div");

				for (Element element : el) {

					if (element.attr("class").equals("address"))
						addr = element.text();
					if (element.attr("class").equals("state"))
						locality = element.text();

					if (element.attr("class").equals("phoneNumber c")) {
						phone = element.text();
					}
				}
				if (website != null) {

					checkReportDTO.setWebsite(website.toString());
				} else {
					checkReportDTO.setWebsite("NOT AVAILABLE");
				}

				String[] stateAndCityAndZip = null;
				if (locality != null) {
					stateAndCityAndZip = locality.split(",");
				}
				if (stateAndCityAndZip != null) {

					locality = stateAndCityAndZip[0];
					String[] stateAndZip = stateAndCityAndZip[1].split(" ");
					region = stateAndZip[0];
					zip = stateAndZip[1];
				}
				if (phone != null && phone.length() != 0)
					phone = getFormattedPhone(phone);

				checkReportDTO.setDirectory("yellowbook");
				checkReportDTO.setBusinessname(businessName);
				checkReportDTO.setPhone(phone);
				checkReportDTO.setAddress(addr);
				checkReportDTO.setCity(locality);
				checkReportDTO.setState(region);
				checkReportDTO.setZip(zip);
				checkReportDTO.setBrandId(listingDTO.getClientId());
				checkReportDTO.setStoreUrl(targetURL);
				checkReportDTO.setStore(listingDTO.getStore());
				int errorsCount = getErrorsCount(listingDTO, checkReportDTO);
				checkReportDTO.setNoOfErrors(errorsCount);
				checkReportDTO.setCheckedDate(new Date());
				allMatchings.add(checkReportDTO);

			}
			if (allMatchings.size() > 0) {
				CheckReportDTO highestMatchingStore = getHighestMatchingStore(
						allMatchings, listingDTO);

				logger.info("Inserting record for: " + listingDTO.getStore());
				if ((highestMatchingStore.getPhone() != null && highestMatchingStore
						.getPhone().length() != 0)
						&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
								.getBusinessname().length() != 0)
						&& (highestMatchingStore.getZip() != null || highestMatchingStore
								.getZip().length() != 0)) {

					Integer listingId = service.isStoreExist(
							listingDTO.getStore(), directory);

					if (listingId != 0) {
						highestMatchingStore.setListingId(listingId);
						service.updateCheckReportInfo(highestMatchingStore);
					} else {
						service.saveCheckReportInfo(highestMatchingStore);
					}

				}
			}
		} catch (Exception e) {
			FailedScrapingsEntity scrapingsEntity = new FailedScrapingsEntity();
			scrapingsEntity.setStore(listingDTO.getStore());
			scrapingsEntity.setDirectory(directory);
			boolean isExist = service.isStoreAndDirectoryExist(
					listingDTO.getStore(), directory);
			if (!isExist) {
				service.saveIntoFailedScapes(scrapingsEntity);
			}
			logger.error("There was a issue while connecting to Yellowbook Domain with: "
					+ targetURL);

		}
	}

	/**
	 * 
	 * @param domainsByStoreForGoogle
	 * @throws Exception
	 */
	public void scrapListingFromMapQuest(
			List<SearchDomainDTO> domainsByStoreForGoogle,
			LocalBusinessDTO listingDTO) {

		String businessName = "";
		String addr = "";
		String locality = "";
		String region = "";
		String zip = "";
		String phone = "";
		String targetURL = "";
		String website = "";
		String directory = "mapquest";
		try {
			List<CheckReportDTO> allMatchings = new ArrayList<CheckReportDTO>();
			for (SearchDomainDTO searchDomainDTO : domainsByStoreForGoogle) {
				CheckReportDTO checkReportDTO = new CheckReportDTO();
				String paths[] = searchDomainDTO.getPath().split(",");

				String path = "";
				for (int i = 0; i < paths.length; i++) {
					path = paths[i].substring(1);
				}

				targetURL = searchDomainDTO.getDomain() + path;

				logger.info("scraping url for the store: "
						+ searchDomainDTO.getSearchId() + ", is: " + targetURL);
				Document document = Jsoup.connect(targetURL).timeout(10000)
						.get();
				Elements websites = document.select("ul.action-bar");
				Elements select = websites.select("a");
				for (Element element : select) {
					if (element.attr("itemprop").equals("url sameAs")) {
						website = element.attr("href");
					}
				}
				Elements bname = document.select("h1");
				for (Element element : bname) {
					if (element.attr("itemprop").equals("name")) {
						businessName = element.text();
					}
				}
				// System.out.println("businessName ::" + businessName);

				Elements el = document.select("span");

				for (Element element : el) {

					if (element.attr("itemprop").equals("streetAddress"))
						addr = element.text();
					if (element.attr("itemprop").equals("addressLocality"))
						locality = element.text();
					if (element.attr("itemprop").equals("addressRegion"))
						region = element.text();
					if (element.attr("itemprop").equals("postalCode"))
						zip = element.text();
					if (element.attr("itemprop").equals("telephone")) {
						phone = element.text();
					}
				}
				if (phone != null && phone.length() != 0)
					phone = getFormattedPhone(phone);
				checkReportDTO.setWebsite(website);
				checkReportDTO.setDirectory("mapquest");
				checkReportDTO.setBusinessname(businessName);
				checkReportDTO.setPhone(phone);
				checkReportDTO.setAddress(addr);
				checkReportDTO.setCity(locality);
				checkReportDTO.setState(region);
				checkReportDTO.setZip(zip);
				checkReportDTO.setBrandId(listingDTO.getClientId());
				checkReportDTO.setStoreUrl(targetURL);
				checkReportDTO.setStore(listingDTO.getStore());
				int errorsCount = getErrorsCount(listingDTO, checkReportDTO);
				checkReportDTO.setNoOfErrors(errorsCount);
				checkReportDTO.setCheckedDate(new Date());

				allMatchings.add(checkReportDTO);
			}
			if (allMatchings.size() > 0) {
				CheckReportDTO highestMatchingStore = getHighestMatchingStore(
						allMatchings, listingDTO);

				logger.info("Inserting record for: " + listingDTO.getStore());
				if ((highestMatchingStore.getPhone() != null && highestMatchingStore
						.getPhone().length() != 0)
						&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
								.getBusinessname().length() != 0)
						&& (highestMatchingStore.getZip() != null || highestMatchingStore
								.getZip().length() != 0)) {
					Integer listingId = service.isStoreExist(
							listingDTO.getStore(), directory);

					if (listingId != 0) {
						highestMatchingStore.setListingId(listingId);
						service.updateCheckReportInfo(highestMatchingStore);
					} else {
						service.saveCheckReportInfo(highestMatchingStore);
					}
				}
			}
		} catch (Exception e) {
			FailedScrapingsEntity scrapingsEntity = new FailedScrapingsEntity();
			scrapingsEntity.setStore(listingDTO.getStore());
			scrapingsEntity.setDirectory(directory);
			boolean isExist = service.isStoreAndDirectoryExist(
					listingDTO.getStore(), directory);
			if (!isExist) {
				service.saveIntoFailedScapes(scrapingsEntity);
			}
			logger.error("There was a issue while connecting to MapQuest Domain with: "
					+ targetURL);
			e.printStackTrace();
		}

	}

	public void scrapListingFromBing(
			List<SearchDomainDTO> domainsByStoreForGoogle,
			LocalBusinessDTO listingDTO) throws Exception {

		String businessName = "";
		String addr = "";
		String locality = "";
		String region = "";
		String zip = "";
		String phone = "";
		String targetURL = "";
		String directory = "bing";
		try {
			List<CheckReportDTO> allMatchings = new ArrayList<CheckReportDTO>();
			for (SearchDomainDTO searchDomainDTO : domainsByStoreForGoogle) {
				CheckReportDTO checkReportDTO = new CheckReportDTO();
				String paths[] = searchDomainDTO.getPath().split(",");

				String path = "";
				for (int i = 0; i < paths.length; i++) {
					path = paths[i].substring(1);
				}

				targetURL = searchDomainDTO.getDomain() + path;

				logger.info("scraping url for the store: "
						+ searchDomainDTO.getSearchId() + ", is: " + targetURL);
				Document doc = Jsoup.connect(targetURL).get();

				Element bname = doc.select("div.business_name").first();
				Element baddr = doc.select("span.business_address").first();
				Element bphone = doc.select("span.business_phone_number")
						.first();
				Element link = doc.select("a#ContactCard_WebsiteLink").first();
				String website = link.attr("href");
				businessName = bname.text();
				phone = bphone.text();
				// System.out.println("bus name: " + businessName);

				// System.out.println("baddr.text().: " + baddr.text());

				String addresData[] = baddr.text().split(",");

				if (addresData != null && addresData.length == 3) {

					addr = addresData[0];
					locality = addresData[1];
					String stateAndZip[] = addresData[2].trim().split(" ");
					region = stateAndZip[0];
					zip = stateAndZip[1];

				}
				if (phone != null && phone.length() != 0)
					phone = getFormattedPhone(phone);
				checkReportDTO.setWebsite(website);
				checkReportDTO.setBusinessname(businessName);
				checkReportDTO.setPhone(phone);
				checkReportDTO.setAddress(addr);
				checkReportDTO.setCity(locality);
				checkReportDTO.setState(region);
				checkReportDTO.setZip(zip);
				checkReportDTO.setDirectory("bing");
				checkReportDTO.setStoreUrl(targetURL);
				checkReportDTO.setBrandId(listingDTO.getClientId());
				checkReportDTO.setStore(listingDTO.getStore());
				int errorsCount = getErrorsCount(listingDTO, checkReportDTO);
				checkReportDTO.setNoOfErrors(errorsCount);
				checkReportDTO.setCheckedDate(new Date());

				allMatchings.add(checkReportDTO);

			}
			if (allMatchings.size() > 0) {
				CheckReportDTO highestMatchingStore = getHighestMatchingStore(
						allMatchings, listingDTO);

				logger.info("Inserting record for: " + listingDTO.getStore());
				if ((highestMatchingStore.getPhone() != null && highestMatchingStore
						.getPhone().length() != 0)
						&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
								.getBusinessname().length() != 0)
						&& (highestMatchingStore.getZip() != null || highestMatchingStore
								.getZip().length() != 0)) {
					Integer listingId = service.isStoreExist(
							listingDTO.getStore(), directory);

					if (listingId != 0) {
						highestMatchingStore.setListingId(listingId);
						service.updateCheckReportInfo(highestMatchingStore);
					} else {
						service.saveCheckReportInfo(highestMatchingStore);
					}
				}
			}

		} catch (Exception e) {
			FailedScrapingsEntity scrapingsEntity = new FailedScrapingsEntity();
			scrapingsEntity.setStore(listingDTO.getStore());
			scrapingsEntity.setDirectory(directory);
			boolean isExist = service.isStoreAndDirectoryExist(
					listingDTO.getStore(), directory);
			if (!isExist) {
				service.saveIntoFailedScapes(scrapingsEntity);
			}
			logger.error("There was a issue while connecting to MapQuest Domain with: "
					+ domainsByStoreForGoogle);
			e.printStackTrace();
		}
	}

	public void scrapListingFromYP(
			List<SearchDomainDTO> domainsByStoreForGoogle,
			LocalBusinessDTO listingDTO) throws Exception {

		String businessName = "";
		String addr = "";
		String locality = "";
		String region = "";
		String zip = "";
		String phone = "";
		String targetURL = "";
		String directory = "yp";
		try {
			List<CheckReportDTO> allMatchings = new ArrayList<CheckReportDTO>();
			for (SearchDomainDTO searchDomainDTO : domainsByStoreForGoogle) {
				CheckReportDTO checkReportDTO = new CheckReportDTO();
				String paths[] = searchDomainDTO.getPath().split(",");

				String path = "";
				for (int i = 0; i < paths.length; i++) {
					path = paths[i].substring(1);
				}

				targetURL = searchDomainDTO.getDomain() + path;

				logger.info("scraping url for the store: "
						+ searchDomainDTO.getSearchId() + ", is: " + targetURL);

				Document document = Jsoup.connect(targetURL).timeout(10000)
						.get();

				Elements bname = document.select("h1");
				for (Element element : bname) {
					if (element.attr("itemprop").equals("name")) {
						businessName = element.text();
					}
				}

				Elements el = document.select("span");

				for (Element element : el) {
					if (element.attr("itemprop").equals("streetAddress"))
						addr = element.text();
					if (element.attr("itemprop").equals("addressLocality"))
						locality = element.text();
					if (element.attr("itemprop").equals("addressRegion"))
						region = element.text();
					if (element.attr("itemprop").equals("postalCode"))
						zip = element.text();

				}
				// addr += "\n" + locality + ", " + region + " " + zip;

				Elements phonenumber = document.select("div");
				for (Element element : phonenumber) {
					if (element.attr("itemprop").equals("telephone")) {
						phone = element.text();
					}
				}
				if (phone != null && phone.length() != 0)
					phone = getFormattedPhone(phone);
				checkReportDTO.setWebsite("");
				checkReportDTO.setBusinessname(businessName);
				checkReportDTO.setPhone(phone);
				checkReportDTO.setAddress(addr);
				checkReportDTO.setCity(locality);
				checkReportDTO.setState(region);
				checkReportDTO.setZip(zip);
				checkReportDTO.setStoreUrl(targetURL);
				checkReportDTO.setDirectory("yp");
				checkReportDTO.setBrandId(listingDTO.getClientId());
				checkReportDTO.setStore(listingDTO.getStore());
				int errorsCount = getErrorsCount(listingDTO, checkReportDTO);
				checkReportDTO.setNoOfErrors(errorsCount);
				checkReportDTO.setCheckedDate(new Date());

				allMatchings.add(checkReportDTO);
			}
			if (allMatchings.size() > 0) {
				CheckReportDTO highestMatchingStore = getHighestMatchingStore(
						allMatchings, listingDTO);

				logger.info("Inserting record for: " + listingDTO.getStore());
				if ((highestMatchingStore.getPhone() != null && highestMatchingStore
						.getPhone().length() != 0)
						&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
								.getBusinessname().length() != 0)
						&& (highestMatchingStore.getZip() != null || highestMatchingStore
								.getZip().length() != 0)) {
					Integer listingId = service.isStoreExist(
							listingDTO.getStore(), directory);

					if (listingId != 0) {
						highestMatchingStore.setListingId(listingId);
						service.updateCheckReportInfo(highestMatchingStore);
					} else {
						service.saveCheckReportInfo(highestMatchingStore);
					}
				}
			}

		} catch (Exception e) {
			FailedScrapingsEntity scrapingsEntity = new FailedScrapingsEntity();
			scrapingsEntity.setStore(listingDTO.getStore());
			scrapingsEntity.setDirectory(directory);
			boolean isExist = service.isStoreAndDirectoryExist(
					listingDTO.getStore(), directory);
			if (!isExist) {
				service.saveIntoFailedScapes(scrapingsEntity);
			}
			logger.error("There was a issue while connecting to YP Domain with: "
					+ targetURL);
			e.printStackTrace();
		}
	}

	public void scrapListingFromSuperPages(
			List<SearchDomainDTO> domainsByStoreForGoogle,
			LocalBusinessDTO listingDTO) throws Exception {

		String businessName = "";
		String addr = "";
		String locality = "";
		String region = "";
		String zip = "";
		String phone = "";
		String targetURL = "";
		String directory = "superpages";
		try {
			List<CheckReportDTO> allMatchings = new ArrayList<CheckReportDTO>();
			for (SearchDomainDTO searchDomainDTO : domainsByStoreForGoogle) {
				CheckReportDTO checkReportDTO = new CheckReportDTO();
				String paths[] = searchDomainDTO.getPath().split(",");

				String path = "";
				for (int i = 0; i < paths.length; i++) {
					path = paths[i].substring(1);
				}

				targetURL = searchDomainDTO.getDomain() + path;

				logger.info("scraping url for the store: "
						+ searchDomainDTO.getSearchId() + ", is: " + targetURL);

				Document document = Jsoup.connect(targetURL).timeout(10000)
						.get();

				Elements link = document.select("a#website");

				String website = link.attr("href");
				Elements bname = document.select("h1");
				for (Element element : bname) {
					if (element.attr("id").equals("profileName")) {
						businessName = element.text();
					}
				}
				// System.out.println("businessName ::" + busName);

				Elements el = document.select("span");

				for (Element element : el) {

					if (element.attr("id").equals("streetAddress"))
						addr = element.text();
					if (element.attr("id").equals("cityAddress"))
						locality = element.text();
					if (element.attr("id").equals("stateAddress"))
						region = element.text();
					if (element.attr("id").equals("zipAddress"))
						zip = element.text();
					if (element.attr("class").equals("contact-info")) {
						phone = element.text();
					}
				}
				if (phone != null && phone.length() != 0)
					phone = getFormattedPhone(phone);
				checkReportDTO.setWebsite(website);
				checkReportDTO.setBusinessname(businessName);
				checkReportDTO.setPhone(phone);
				checkReportDTO.setAddress(addr);
				checkReportDTO.setCity(locality);
				checkReportDTO.setState(region);
				checkReportDTO.setZip(zip);
				checkReportDTO.setStoreUrl(targetURL);
				checkReportDTO.setDirectory("superpages");
				checkReportDTO.setStore(listingDTO.getStore());
				checkReportDTO.setBrandId(listingDTO.getClientId());
				int errorsCount = getErrorsCount(listingDTO, checkReportDTO);
				checkReportDTO.setNoOfErrors(errorsCount);
				checkReportDTO.setCheckedDate(new Date());
				allMatchings.add(checkReportDTO);
			}
			if (allMatchings.size() > 0) {
				CheckReportDTO highestMatchingStore = getHighestMatchingStore(
						allMatchings, listingDTO);

				logger.info("Inserting record for: " + listingDTO.getStore());
				if ((highestMatchingStore.getPhone() != null && highestMatchingStore
						.getPhone().length() != 0)
						&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
								.getBusinessname().length() != 0)
						&& (highestMatchingStore.getZip() != null || highestMatchingStore
								.getZip().length() != 0)) {
					Integer listingId = service.isStoreExist(
							listingDTO.getStore(), directory);

					if (listingId != 0) {
						highestMatchingStore.setListingId(listingId);
						service.updateCheckReportInfo(highestMatchingStore);
					} else {
						service.saveCheckReportInfo(highestMatchingStore);
					}
				}
			}

		} catch (Exception e) {
			FailedScrapingsEntity scrapingsEntity = new FailedScrapingsEntity();
			scrapingsEntity.setStore(listingDTO.getStore());
			scrapingsEntity.setDirectory(directory);
			boolean isExist = service.isStoreAndDirectoryExist(
					listingDTO.getStore(), directory);
			if (!isExist) {
				service.saveIntoFailedScapes(scrapingsEntity);
			}

			logger.error("There was a issue while connecting to Suparepages Domain with: "
					+ targetURL);
			e.printStackTrace();
		}
	}

	public void scrapListingFromYelp(
			List<SearchDomainDTO> domainsByStoreForGoogle,
			LocalBusinessDTO listingDTO) {

		String businessName = "";
		String addr = "";
		String locality = "";
		String region = "";
		String zip = "";
		String phone = "";
		String targetURL = "";
		String directory = "yelp";
		try {
			List<CheckReportDTO> allMatchings = new ArrayList<CheckReportDTO>();
			for (SearchDomainDTO searchDomainDTO : domainsByStoreForGoogle) {
				CheckReportDTO checkReportDTO = new CheckReportDTO();
				String paths[] = searchDomainDTO.getPath().split(",");

				String path = "";
				for (int i = 0; i < paths.length; i++) {
					path = paths[i].substring(1);
				}

				targetURL = searchDomainDTO.getDomain() + path;

				logger.info("scraping url for the store: "
						+ searchDomainDTO.getSearchId() + ", is: " + targetURL);

				// Document doc = Jsoup.connect(targetURL).get();

				Document doc = Jsoup
						.connect(targetURL)
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
						.get();

				String website;
				Element link = doc.select("div[class=biz-website]").first();
				if (link != null) {
					Element select = link.select("a").first();

					website = select.html();
				} else {
					website = "NOT AVAILABLE" ;
				}
				Elements bname = doc.select("h1");
				for (Element element : bname) {
					if (element.attr("itemprop").equals("name")) {
						businessName = element.text();
					}
				}

				Elements el = doc.select("span");

				for (Element element : el) {

					if (element.attr("itemprop").equals("streetAddress"))
						addr = element.text();
					if (element.attr("itemprop").equals("addressLocality"))
						locality = element.text();
					if (element.attr("itemprop").equals("addressRegion"))
						region = element.text();
					if (element.attr("itemprop").equals("postalCode"))
						zip = element.text();
					if (element.attr("itemprop").equals("telephone")) {
						phone = element.text();
					}

				}
				// addr += "\n" + locality + ", " + region + " " + zip;
				// System.out.println("addr:" + addr);
				// System.out.println("phone: " + phone);
				if (phone != null && phone.length() != 0)
					phone = getFormattedPhone(phone);
				checkReportDTO.setWebsite(website);
				checkReportDTO.setBusinessname(businessName);
				checkReportDTO.setPhone(phone);
				checkReportDTO.setAddress(addr);
				checkReportDTO.setCity(locality);
				checkReportDTO.setState(region);
				checkReportDTO.setZip(zip);
				checkReportDTO.setStoreUrl(targetURL);
				checkReportDTO.setDirectory("yelp");
				checkReportDTO.setStore(listingDTO.getStore());
				checkReportDTO.setBrandId(listingDTO.getClientId());
				int errorsCount = getErrorsCount(listingDTO, checkReportDTO);
				checkReportDTO.setNoOfErrors(errorsCount);
				checkReportDTO.setCheckedDate(new Date());
				allMatchings.add(checkReportDTO);
			}
			if (allMatchings.size() > 0) {
				CheckReportDTO highestMatchingStore = getHighestMatchingStore(
						allMatchings, listingDTO);

				logger.info("Inserting record for: " + listingDTO.getStore());
				if ((highestMatchingStore.getPhone() != null && highestMatchingStore
						.getPhone().length() != 0)
						&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
								.getBusinessname().length() != 0)
						&& (highestMatchingStore.getZip() != null || highestMatchingStore
								.getZip().length() != 0)) {
					Integer listingId = service.isStoreExist(
							listingDTO.getStore(), directory);

					if (listingId != 0) {
						highestMatchingStore.setListingId(listingId);
						service.updateCheckReportInfo(highestMatchingStore);
					} else {
						service.saveCheckReportInfo(highestMatchingStore);
					}
				}
			}

		} catch (Exception e) {
			FailedScrapingsEntity scrapingsEntity = new FailedScrapingsEntity();
			scrapingsEntity.setStore(listingDTO.getStore());
			scrapingsEntity.setDirectory(directory);
			boolean isExist = service.isStoreAndDirectoryExist(
					listingDTO.getStore(), directory);
			if (!isExist) {
				service.saveIntoFailedScapes(scrapingsEntity);
			}
			logger.error("There was a issue while connecting to Yelp Domain with: "
					+ targetURL);

			e.printStackTrace();
		}

	}

	public void scrapListingFromGoogle(
			List<SearchDomainDTO> domainsByStoreForGoogle,
			LocalBusinessDTO localBusinessDTO) {

		String businessName = "";
		String addr = "";
		String locality = "";
		String region = "";
		String zip = "";
		String phone = "";
		String targetURL = "";
		String directory = "google";
		try {

			List<CheckReportDTO> allMatchings = new ArrayList<CheckReportDTO>();

			for (SearchDomainDTO searchDomainDTO : domainsByStoreForGoogle) {
				CheckReportDTO checkReportDTO = new CheckReportDTO();
				String paths[] = searchDomainDTO.getPath().split(",");

				String path = "";
				for (int i = 0; i < paths.length; i++) {
					path = paths[i].substring(1);
				}

				targetURL = searchDomainDTO.getDomain() + path;

				logger.info("scraping url for the store: "
						+ searchDomainDTO.getSearchId() + ", is: " + targetURL);

				Document doc = Jsoup.connect(targetURL).get();
				Element bname = doc.select("div[class=fpd KXa]").first();
				businessName = bname.text();
				Elements el = doc.select("span");
				for (Element el1 : el) {
					if (el1.attr("itemprop").equals("streetAddress"))
						addr = el1.text();
					if (el1.attr("itemprop").equals("addressLocality"))
						locality = el1.text();
					if (el1.attr("itemprop").equals("addressRegion"))
						region = el1.text();
					if (el1.attr("itemprop").equals("postalCode"))
						zip = el1.text();
				}

				Element phoneNumber = doc.select("div[class=Ny nkb]").first();
				Element website = doc.select("div[class=Ny qkb]").first();
				phone = phoneNumber.text();
				if (phone != null && phone.length() != 0)
					phone = getFormattedPhone(phone);

				checkReportDTO.setBusinessname(businessName);
				checkReportDTO.setPhone(phone);
				if (website != null) {
					checkReportDTO.setWebsite(website.text());
				} else {
					checkReportDTO.setWebsite("NOT AVAILABLE");
				}
				phone = getFormattedPhone(phone);
				checkReportDTO.setAddress(addr);
				checkReportDTO.setCity(locality);
				checkReportDTO.setState(region);
				checkReportDTO.setZip(zip);
				checkReportDTO.setDirectory("google");
				checkReportDTO.setStoreUrl(targetURL);
				checkReportDTO.setStore(localBusinessDTO.getStore());
				checkReportDTO.setBrandId(localBusinessDTO.getClientId());
				int errorsCount = getErrorsCount(localBusinessDTO,
						checkReportDTO);
				System.out.println("errorscount:::::::::::::" + errorsCount);
				checkReportDTO.setNoOfErrors(errorsCount);
				checkReportDTO.setCheckedDate(new Date());

				allMatchings.add(checkReportDTO);

			}
			if (allMatchings.size() > 0) {
				CheckReportDTO highestMatchingStore = getHighestMatchingStore(
						allMatchings, localBusinessDTO);

				logger.info("Inserting record for: "
						+ localBusinessDTO.getStore());
				if ((highestMatchingStore.getPhone() != null && highestMatchingStore
						.getPhone().length() != 0)
						&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
								.getBusinessname().length() != 0)
						&& (highestMatchingStore.getZip() != null || highestMatchingStore
								.getZip().length() != 0)) {
					Integer listingId = service.isStoreExist(
							localBusinessDTO.getStore(), directory);

					if (listingId != 0) {
						highestMatchingStore.setListingId(listingId);
						service.updateCheckReportInfo(highestMatchingStore);
					} else {
						service.saveCheckReportInfo(highestMatchingStore);
					}
				}
			}

		} catch (Exception e) {
			FailedScrapingsEntity scrapingsEntity = new FailedScrapingsEntity();
			scrapingsEntity.setStore(localBusinessDTO.getStore());
			scrapingsEntity.setDirectory(directory);
			boolean isExist = service.isStoreAndDirectoryExist(
					localBusinessDTO.getStore(), directory);
			if (!isExist) {
				service.saveIntoFailedScapes(scrapingsEntity);
			}
			logger.error("There was a issue while connecting to Google with: "
					+ targetURL);
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 * @param allStores
	 * @param localBusinessDTO
	 * @return
	 */
	private CheckReportDTO getHighestMatchingStore(
			List<CheckReportDTO> allStores, LocalBusinessDTO localBusinessDTO) {

		CheckReportDTO dto = new CheckReportDTO();

		String locationAddress = localBusinessDTO.getLocationAddress();
		String locationZip = localBusinessDTO.getLocationZipCode();
		String locationPhone = localBusinessDTO.getLocationPhone();
		Map<Integer, String> pathScores = new HashMap<Integer, String>();
		Map<String, CheckReportDTO> pathsWithDetails = new HashMap<String, CheckReportDTO>();
		for (int i = 0; i < allStores.size(); i++) {

			CheckReportDTO checkReportDTO = allStores.get(i);
			String address = checkReportDTO.getAddress();
			String phone = checkReportDTO.getPhone();
			String zip = checkReportDTO.getZip();
			String storeUrl = checkReportDTO.getStoreUrl();

			pathsWithDetails.put(storeUrl, checkReportDTO);

			String[] split2 = address.split(" ");

			phone = getFormattedPhone(phone);

			if (locationAddress.contains(split2[0])
					&& zip.equalsIgnoreCase(locationZip)
					&& phone.equals(locationPhone)) {
				pathScores.put(100, storeUrl);
			}

			else if ((locationAddress.contains(split2[0]) || zip
					.equalsIgnoreCase(locationZip))
					&& phone.equals(locationPhone)) {
				pathScores.put(75, storeUrl);
			}

			else if ((locationAddress.contains(split2[0]) || phone
					.equals(locationPhone))
					&& zip.equalsIgnoreCase(locationZip)) {
				pathScores.put(50, storeUrl);
			} else if (locationAddress.contains(split2[0])
					|| zip.equalsIgnoreCase(locationZip)
					|| phone.equals(locationPhone)) {
				pathScores.put(25, storeUrl);
			} else {
				pathScores.put(0, storeUrl);
			}
		}

		Set<Integer> keySet = pathScores.keySet();

		Integer maxValue = Collections.max(keySet);

		String targetUrl = pathScores.get(maxValue);

		dto = pathsWithDetails.get(targetUrl);
		String website = dto.getWebsite();
		if (dto != null && website != null) {
			if (website.length() > 255)
				website = website.substring(0, 255);
			dto.setWebsite(website);
		}

		return dto;

	}

	public void scrapListingFromYahoo(
			List<SearchDomainDTO> domainsByStoreForGoogle,
			LocalBusinessDTO localBusinessDTO) {

		String businessName = "";
		String addr = "";
		String city = "";
		String state = "";
		String zip = "";
		String phone = "";
		String targetURL = "";
		String website = "";
		String directory = "yahoo";
		try {

			List<CheckReportDTO> allMatchings = new ArrayList<CheckReportDTO>();
			for (SearchDomainDTO searchDomainDTO : domainsByStoreForGoogle) {
				CheckReportDTO checkReportDTO = new CheckReportDTO();
				String paths[] = searchDomainDTO.getPath().split(",");

				String path = "";
				for (int i = 0; i < paths.length; i++) {
					path = paths[i].substring(1);
				}

				targetURL = searchDomainDTO.getDomain() + path;

				logger.info("scraping url for the store: "
						+ searchDomainDTO.getSearchId() + ", is: " + targetURL);

				// Document doc = Jsoup.connect(targetURL).get();
				Document doc = Jsoup
						.connect(targetURL)
						.timeout(10000)
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
						.get();

				Elements el1 = doc.select("meta");
				for (Element el : el1) {
					if (el.attr("property").equals("og:title")) {
						businessName = el.attr("content");
					}

					if (el.attr("property").equals(
							"business:contact_data:phone_number")) {
						phone = el.attr("content");

					}

					if (el.attr("property").equals(
							"business:contact_data:street_address")) {

						addr = el.attr("content");
					}

					if (el.attr("property").equals(
							"business:contact_data:locality")) {
						// addr += "\n" + el.attr("content") ;
						city = el.attr("content");

					}

					if (el.attr("property").equals(
							"business:contact_data:region")) {
						// addr += el.attr("content") ;
						state = el.attr("content");
					}

					if (el.attr("property").equals(
							"business:contact_data:postal_code")) {
						// addr += el.attr("content");
						zip = el.attr("content");

					}
					if (el.attr("property").equals(
							"business:contact_data:website")) {
						website = el.attr("content");

					}
					checkReportDTO.setBusinessname(businessName);
					checkReportDTO.setPhone(phone);
					checkReportDTO.setAddress(addr);
					checkReportDTO.setCity(city);
					checkReportDTO.setState(state);
					checkReportDTO.setZip(zip);
					checkReportDTO.setWebsite(website);
					checkReportDTO.setDirectory("yahoo");
					checkReportDTO.setStoreUrl(targetURL);
					checkReportDTO.setStore(localBusinessDTO.getStore());
					checkReportDTO.setBrandId(localBusinessDTO.getClientId());
					int errorsCount = getErrorsCount(localBusinessDTO,
							checkReportDTO);
					checkReportDTO.setNoOfErrors(errorsCount);
					checkReportDTO.setCheckedDate(new Date());

					allMatchings.add(checkReportDTO);
				}
			}
			CheckReportDTO highestMatchingStore = getHighestMatchingStore(
					allMatchings, localBusinessDTO);

			logger.info("Inserting record for: " + localBusinessDTO.getStore());
			if ((highestMatchingStore.getPhone() != null && highestMatchingStore
					.getPhone().length() != 0)
					&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
							.getBusinessname().length() != 0)
					&& (highestMatchingStore.getZip() != null || highestMatchingStore
							.getZip().length() != 0)) {
				Integer listingId = service.isStoreExist(
						localBusinessDTO.getStore(), directory);

				if (listingId != 0) {
					highestMatchingStore.setListingId(listingId);
					service.updateCheckReportInfo(highestMatchingStore);
				} else {
					service.saveCheckReportInfo(highestMatchingStore);
				}
			}

		} catch (Exception e) {
			FailedScrapingsEntity scrapingsEntity = new FailedScrapingsEntity();
			scrapingsEntity.setStore(localBusinessDTO.getStore());
			scrapingsEntity.setDirectory(directory);
			boolean isExist = service.isStoreAndDirectoryExist(
					localBusinessDTO.getStore(), directory);
			if (!isExist) {
				service.saveIntoFailedScapes(scrapingsEntity);
			}
			logger.error("There was a issue while connecting to Yahoo Domain with: "
					+ targetURL);
			e.printStackTrace();
		}

	}

	// @Test
	public void testSuperPages() throws Exception {

		String targetUrl = "http://www.superpages.com/bp//Liberty-Tax-Service-L2354902032.htm";

		Document document = Jsoup.connect(targetUrl).get();
		String busName = null;
		Elements bname = document.select("h1");
		for (Element element : bname) {
			if (element.attr("id").equals("profileName")) {
				busName = element.text();
			}
		}

		String addr = "";
		String locality = null;
		String region = null;
		String zip = null;
		String phone = null;
		Elements el = document.select("span");

		for (Element element : el) {

			if (element.attr("id").equals("streetAddress"))
				addr = element.text();
			if (element.attr("id").equals("cityAddress"))
				locality = element.text();
			if (element.attr("id").equals("stateAddress"))
				region = element.text();
			if (element.attr("id").equals("zipAddress"))
				zip = element.text();
			if (element.attr("class").equals("contact-info")) {
				phone = element.text();
			}
		}
		addr += "\n" + locality + ", " + region + " " + zip;
		System.out.println("addr:" + addr);

		System.out.println("phone: " + phone);
	}

	public int getErrorsCount(LocalBusinessDTO localBusinessDto,
			CheckReportDTO checkReportDto) {

		int count = 0;

		if (!localBusinessDto.getCompanyName().equalsIgnoreCase(
				checkReportDto.getBusinessname())) {
			count++;

		}
		String locationPhone = localBusinessDto.getLocationPhone();
		String phone = checkReportDto.getPhone();

		if (locationPhone != null) {
			locationPhone = getFormattedPhone(locationPhone);

		}
		if (phone != null) {
			phone = getFormattedPhone(phone);

		}

		if (!locationPhone.equalsIgnoreCase(phone)) {
			count++;

		}
		String locationDirectoryAddress = checkReportDto.getAddress();
		String locationAddress = localBusinessDto.getLocationAddress();
		if (locationDirectoryAddress == null) {
			locationDirectoryAddress = "";
		}
		if (locationAddress == null) {
			locationAddress = "";
		}
		String[] locationAddressArray = locationAddress.split(" ");
		String lastWordinLocationAddress = "";
		if (locationAddressArray.length > 0) {
			lastWordinLocationAddress = locationAddressArray[locationAddressArray.length - 1];
		}

		String[] locationDirectoryAddressArray = locationDirectoryAddress
				.split(" ");
		String lastWordinDirectoryddress = "";
		if (locationAddressArray.length > 0) {
			lastWordinDirectoryddress = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 1];
		}

		if (!locationAddress.equalsIgnoreCase(locationDirectoryAddress)) {

			if (!lastWordinDirectoryddress
					.equalsIgnoreCase(lastWordinLocationAddress)) {
				// get the the Abbrevation
				boolean isAbbreviationExist = service.isAbbreviationExist(
						lastWordinDirectoryddress, lastWordinLocationAddress);

				String secondLastWordInDirectory = "";
				String secondLastWordInLBL = "";

				if (locationAddressArray.length > 1
						&& locationDirectoryAddressArray.length > 1) {
					secondLastWordInLBL = locationAddressArray[locationAddressArray.length - 2];
					secondLastWordInDirectory = locationDirectoryAddressArray[locationDirectoryAddressArray.length - 2];
				}

				boolean isSecondWordsMatch = false;
				if (secondLastWordInLBL != ""
						&& secondLastWordInDirectory != "") {
					isSecondWordsMatch = secondLastWordInLBL
							.equalsIgnoreCase(secondLastWordInDirectory);
				}

				if (!isAbbreviationExist || !isSecondWordsMatch) {
					count++;
				}
			}
		}
		if (!localBusinessDto.getLocationCity().equalsIgnoreCase(
				checkReportDto.getCity())) {
			count++;

		}

		String state = checkReportDto.getState();
		if (state != null && state.length() > 2) {
			state = service.getStateFromStateList(state);

		}

		if (!localBusinessDto.getLocationState().equalsIgnoreCase(state)) {
			count++;
		}
		if (!localBusinessDto.getLocationZipCode().equalsIgnoreCase(
				checkReportDto.getZip())) {
			count++;
		}
		String webAddress = localBusinessDto.getWebAddress();
		String address = checkReportDto.getWebsite();
		if (address == null) {
			address = "";
		}
		if (webAddress == null) {
			webAddress = "";
		}
		if (address != null && address.length() > 0 && address.contains("www")) {
			address = address.substring(address.indexOf("www.") + 4);
		}
		if (address != null && address.length() > 0 && address.contains("?")) {
			address = address.substring(0, address.indexOf("?") + 1);
		}
		if (webAddress != null && webAddress.length() > 0
				&& webAddress.contains("www")) {
			webAddress = webAddress.substring(webAddress.indexOf("www.") + 4);
		}
		if (webAddress != null && webAddress.length() > 0
				&& webAddress.contains("?")) {
			webAddress = webAddress.substring(0, webAddress.indexOf("?") + 1);
		}
		if (!webAddress.equalsIgnoreCase(address)) {
			count++;

		}

		return count;
	}

	public static String getFormattedPhone(String phone) {

		String locationPhone = phone;

		if (locationPhone != null) {
			locationPhone = locationPhone.replaceAll("\\s+", "");
		}

		if (locationPhone != null && locationPhone.contains("Local:")) {
			locationPhone = locationPhone.substring(locationPhone
					.indexOf("Local:") + 6);

			locationPhone = locationPhone.trim();
		}

		if (locationPhone != null && locationPhone.contains("Add")) {
			locationPhone = locationPhone.substring(0,
					locationPhone.indexOf("Add"));

			locationPhone = locationPhone.trim();
		}
		if (locationPhone != null && locationPhone.contains("+1")) {
			locationPhone = locationPhone.replaceAll("\\+1", "");
			locationPhone = locationPhone.trim();
		}
		if (locationPhone != null && locationPhone.contains("-")) {
			locationPhone = locationPhone.replaceAll("-", "");
			locationPhone = locationPhone.trim();
		}
		if (locationPhone != null && locationPhone.contains(")")) {
			locationPhone = locationPhone.replaceAll("[)]", "");
			locationPhone = locationPhone.trim();
		}
		if (locationPhone != null && locationPhone.contains("(")) {
			locationPhone = locationPhone.replaceAll("[(]", "");
			locationPhone = locationPhone.trim();
		}

		return locationPhone;
	}
	public void scrapListingFromWhitepages(
			List<SearchDomainDTO> domainsByStoreForGoogle,
			LocalBusinessDTO localBusinessDTO) {

		String businessName = "";
		String addr = "";
		String locality = "";
		String region = "";
		String zip = "";
		String phone = "";
		String targetURL = "";
		String directory = "google";
		try {

			List<CheckReportDTO> allMatchings = new ArrayList<CheckReportDTO>();

			for (SearchDomainDTO searchDomainDTO : domainsByStoreForGoogle) {
				CheckReportDTO checkReportDTO = new CheckReportDTO();
				String paths[] = searchDomainDTO.getPath().split(",");

				String path = "";
				for (int i = 0; i < paths.length; i++) {
					path = paths[i].substring(1);
				}

				targetURL = searchDomainDTO.getDomain() + path;

				logger.info("scraping url for the store: "
						+ searchDomainDTO.getSearchId() + ", is: " + targetURL);

				Document document = Jsoup.connect(targetURL).get();

				Element busname = document.select("span.name").first();
				if(busname==null){
					businessName=" ";			
				}else{
					businessName=busname.text();
				}
				
				Elements tellphone = document.select("a.clickstream-link");
				for (Element element : tellphone) {
					if(element.attr("data-gaaction").equals("business")){
						phone=element.text();
					}
				}
				Element addressval = document.select("span.address-primary").first();
				if(addressval==null){
					addr="";
				}else{
					addr=addressval.text();	
				}

				Element location = document.select("span.locality ").first();
				if(location==null){
					locality="";
				}else{
					locality=location.text();
				}
				
			
				Element regions = document.select("span.region").first();
				if(regions==null){
					region="";
				}else{
					region=regions.text();
				}
				                  
				
				Element postalcode = document.select("span.postal-code").first();
				if(postalcode==null){
					zip="";
				}else{
					zip=postalcode.text();
				}
				

				if (phone != null && phone.length() != 0)
					phone = getFormattedPhone(phone);

				checkReportDTO.setBusinessname(businessName);
				checkReportDTO.setPhone(phone);
				
				phone = getFormattedPhone(phone);
				checkReportDTO.setAddress(addr);
				checkReportDTO.setCity(locality);
				checkReportDTO.setState(region);
				checkReportDTO.setZip(zip);
				checkReportDTO.setWebsite("NOT AVAILABLE");
				checkReportDTO.setDirectory("witepages");
				checkReportDTO.setStoreUrl(targetURL);
				checkReportDTO.setStore(localBusinessDTO.getStore());
				checkReportDTO.setBrandId(localBusinessDTO.getClientId());
				int errorsCount = getErrorsCount(localBusinessDTO,
						checkReportDTO);
				System.out.println("errorscount:::::::::::::" + errorsCount);
				checkReportDTO.setNoOfErrors(errorsCount);
				checkReportDTO.setCheckedDate(new Date());

				allMatchings.add(checkReportDTO);

			}
			if (allMatchings.size() > 0) {
				CheckReportDTO highestMatchingStore = getHighestMatchingStore(
						allMatchings, localBusinessDTO);

				logger.info("Inserting record for: "
						+ localBusinessDTO.getStore());
				if ((highestMatchingStore.getPhone() != null && highestMatchingStore
						.getPhone().length() != 0)
						&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
								.getBusinessname().length() != 0)
						&& (highestMatchingStore.getZip() != null || highestMatchingStore
								.getZip().length() != 0)) {
					Integer listingId = service.isStoreExist(
							localBusinessDTO.getStore(), directory);

					if (listingId != 0) {
						highestMatchingStore.setListingId(listingId);
						service.updateCheckReportInfo(highestMatchingStore);
					} else {
						service.saveCheckReportInfo(highestMatchingStore);
					}
				}
			}

		} catch (Exception e) {
			FailedScrapingsEntity scrapingsEntity = new FailedScrapingsEntity();
			scrapingsEntity.setStore(localBusinessDTO.getStore());
			scrapingsEntity.setDirectory(directory);
			boolean isExist = service.isStoreAndDirectoryExist(
					localBusinessDTO.getStore(), directory);
			if (!isExist) {
				service.saveIntoFailedScapes(scrapingsEntity);
			}
			logger.error("There was a issue while connecting to Google with: "
					+ targetURL);
			e.printStackTrace();
		}

	}

	public void renewBrands(BusinessService service, String clinet) {
		this.service = service;
		Date date=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = dateFormat.format(date);
		List<LocalBusinessDTO> localBusinessDTOs = service
				.getListOfBussinessByBrandName(clinet);
		for (LocalBusinessDTO localBusinessDTO : localBusinessDTOs) {
			RenewalReportDTO renewalReportDTO = service.isRenewed(localBusinessDTO.getStore(), localBusinessDTO.getClientId());
			if(renewalReportDTO!=null){
				Date renewalDate = renewalReportDTO.getRenewalDate();
				String renewDate="";
				if(renewalDate!=null){
					 renewDate = dateFormat.format(renewalDate);
				}
				if(renewDate.equals(currentDate)){
					renewalReportDTO.setStatus("Renewed");
					RenewalReportEntity renewalReportEntity=new RenewalReportEntity();
					BeanUtils.copyProperties(renewalReportDTO, renewalReportEntity);
					service.updateRenewalInfo(renewalReportEntity);
				}
			}
			
			
			
			
		}
		
		
		
		
	}

}
