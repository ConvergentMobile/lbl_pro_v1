package com.business.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.SearchDomainDTO;
import com.business.model.pojo.FailedScrapingsEntity;
import com.business.service.BusinessService;
import com.whitespark.ws.ScrapUtil;

public class FailedScrapinglUtil {
	ScrapUtil scrapUtil=new ScrapUtil();
	static Logger logger = Logger.getLogger(FailedScrapinglUtil.class);
	BusinessService service;

	public void StoresFailed(BusinessService service) throws Exception{
		this.service=service;
		List<FailedScrapingsEntity> stores=service.getAllStoresFromFailedScraping();
		for (FailedScrapingsEntity failedScrapingsEntity : stores) {
			LocalBusinessDTO localBusinessDTO=new LocalBusinessDTO();
			localBusinessDTO.setStore(failedScrapingsEntity.getStore());
			if(failedScrapingsEntity.getDirectory().equals("google")){
				List<SearchDomainDTO> domainsByStoreForGoogle = service.getDomainsByStore(failedScrapingsEntity.getStore(),
					       "plus.google.com");
			


				String businessName = "";
				String addr = "";
				String locality = "";
				String region = "";
				String zip = "";
				String phone = "";
				String targetURL = "";
				String directory="google";
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

						Document doc = Jsoup.connect(targetURL).timeout(10000).get();
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
						if(phone!=null&&phone.length()!=0)
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
						int errorsCount = getErrorsCount(localBusinessDTO,
								checkReportDTO);
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
							Integer listingId = service.isStoreExist(localBusinessDTO.getStore(),
									directory);

							if (listingId != 0) {
								highestMatchingStore.setListingId(listingId);
								service.updateCheckReportInfo(highestMatchingStore);
							} else {
								service.saveCheckReportInfo(highestMatchingStore);
							}
						}
					}

				} catch (Exception e) {
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					boolean isExist=service.isStoreAndDirectoryExist(localBusinessDTO.getStore(),directory);
					if(!isExist){
						service.saveIntoFailedScapes(scrapingsEntity);
					}
					logger.error("There was a issue while connecting to Google with: "
							+ targetURL);
					e.printStackTrace();
				}finally{
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					service.deleteFailedScapingEntity(localBusinessDTO.getStore(),directory);
				}

			
	
			
			}
			if(failedScrapingsEntity.getDirectory().equals("bing")){
				List<SearchDomainDTO> domainsByStoreForGoogle = service.getDomainsByStore(failedScrapingsEntity.getStore(),
					       "www.bing.com");


				String businessName = "";
				String addr = "";
				String locality = "";
				String region = "";
				String zip = "";
				String phone = "";
				String targetURL = "";
				String directory="bing";
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
						Document doc = Jsoup.connect(targetURL).timeout(10000).get();

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

						if (addresData!=null && addresData.length == 3) {

							addr = addresData[0];
							locality = addresData[1];
							String stateAndZip[] = addresData[2].trim().split(" ");
							region = stateAndZip[0];
							zip = stateAndZip[1];

						}
						if(phone!=null&&phone.length()!=0)
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
						checkReportDTO.setStore(localBusinessDTO.getStore());
						int errorsCount = getErrorsCount(localBusinessDTO, checkReportDTO);
						checkReportDTO.setNoOfErrors(errorsCount);
						checkReportDTO.setCheckedDate(new Date());

						allMatchings.add(checkReportDTO);

					}
					if (allMatchings.size() > 0) {
						CheckReportDTO highestMatchingStore = getHighestMatchingStore(
								allMatchings, localBusinessDTO);

						logger.info("Inserting record for: " + localBusinessDTO.getStore());
						if ((highestMatchingStore.getPhone() != null && highestMatchingStore
								.getPhone().length() != 0)
								&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
										.getBusinessname().length() != 0)
								&& (highestMatchingStore.getZip() != null || highestMatchingStore
										.getZip().length() != 0)) {
							Integer listingId = service.isStoreExist(localBusinessDTO.getStore(),
									directory);

							if (listingId != 0) {
								highestMatchingStore.setListingId(listingId);
								service.updateCheckReportInfo(highestMatchingStore);
							} else {
								service.saveCheckReportInfo(highestMatchingStore);
							}
						}
					}

				} catch (Exception e) {
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					boolean isExist=service.isStoreAndDirectoryExist(localBusinessDTO.getStore(),directory);
					if(!isExist){
						service.saveIntoFailedScapes(scrapingsEntity);
					}
					logger.error("There was a issue while connecting to MapQuest Domain with: "
							+ domainsByStoreForGoogle);
					e.printStackTrace();
				}finally{
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					service.deleteFailedScapingEntity(localBusinessDTO.getStore(),directory);
				}
			
	
			
			}
			if(failedScrapingsEntity.getDirectory().equals("yahoo")){
				List<SearchDomainDTO> domainsByStoreForGoogle = service.getDomainsByStore(failedScrapingsEntity.getStore(),
					       "local.yahoo.com");


				String businessName = "";
				String addr = "";
				String city = "";
				String state = "";
				String zip = "";
				String phone = "";
				String targetURL = "";
				String website = "";
				String directory="yahoo";
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

						// Document doc = Jsoup.connect(targetURL).timeout(10000).get();
						Document doc = Jsoup
								.connect(targetURL)
								.timeout(30000)
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

								addr += el.attr("content");
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
						Integer listingId = service.isStoreExist(localBusinessDTO.getStore(),
								directory);

						if (listingId != 0) {
							highestMatchingStore.setListingId(listingId);
							service.updateCheckReportInfo(highestMatchingStore);
						} else {
							service.saveCheckReportInfo(highestMatchingStore);
						}
					}

				} catch (Exception e) {
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					boolean isExist=service.isStoreAndDirectoryExist(localBusinessDTO.getStore(),directory);
					if(!isExist){
						service.saveIntoFailedScapes(scrapingsEntity);
					}
					logger.error("There was a issue while connecting to Yahoo Domain with: "
							+ targetURL);
					e.printStackTrace();
				}finally{
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					service.deleteFailedScapingEntity(localBusinessDTO.getStore(),directory);
				}

			
			
	
			
			}
			if(failedScrapingsEntity.getDirectory().equals("yelp")){
				List<SearchDomainDTO> domainsByStoreForGoogle = service.getDomainsByStore(failedScrapingsEntity.getStore(),
					       "www.yelp.com");


				String businessName = "";
				String addr = "";
				String locality = "";
				String region = "";
				String zip = "";
				String phone = "";
				String targetURL = "";
				String directory="yelp";
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

						// Document doc = Jsoup.connect(targetURL).timeout(10000).get();

						Document doc = Jsoup
								.connect(targetURL)
								.userAgent(
										"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
								.timeout(10000).get();

						String website;
						Element link = doc.select("div[class=biz-website]").first();
						if (link != null) {
							Element select = link.select("a").first();

							website = select.html();
						} else {
							website = "NOT APPLICABLE ";
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
						if(phone!=null&&phone.length()!=0)
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
						checkReportDTO.setStore(localBusinessDTO.getStore());
						int errorsCount = getErrorsCount(localBusinessDTO, checkReportDTO);
						checkReportDTO.setNoOfErrors(errorsCount);
						checkReportDTO.setCheckedDate(new Date());
						allMatchings.add(checkReportDTO);
					}
					if (allMatchings.size() > 0) {
						CheckReportDTO highestMatchingStore = getHighestMatchingStore(
								allMatchings, localBusinessDTO);

						logger.info("Inserting record for: " + localBusinessDTO.getStore());
						if ((highestMatchingStore.getPhone() != null && highestMatchingStore
								.getPhone().length() != 0)
								&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
										.getBusinessname().length() != 0)
								&& (highestMatchingStore.getZip() != null || highestMatchingStore
										.getZip().length() != 0)) {
							Integer listingId = service.isStoreExist(localBusinessDTO.getStore(),
									directory);

							if (listingId != 0) {
								highestMatchingStore.setListingId(listingId);
								service.updateCheckReportInfo(highestMatchingStore);
							} else {
								service.saveCheckReportInfo(highestMatchingStore);
							}
						}
					}

				} catch (Exception e) {
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					boolean isExist=service.isStoreAndDirectoryExist(localBusinessDTO.getStore(),directory);
					if(!isExist){
						service.saveIntoFailedScapes(scrapingsEntity);
					}
					logger.error("There was a issue while connecting to Yelp Domain with: "
							+ targetURL);

					e.printStackTrace();
				}finally{
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					service.deleteFailedScapingEntity(localBusinessDTO.getStore(),directory);
				}

			
			
	
			
			}
			if(failedScrapingsEntity.getDirectory().equals("yp")){
				List<SearchDomainDTO> domainsByStoreForGoogle = service.getDomainsByStore(failedScrapingsEntity.getStore(),
					       "www.myyp.com");


				String businessName = "";
				String addr = "";
				String locality = "";
				String region = "";
				String zip = "";
				String phone = "";
				String targetURL = "";
				String directory="yp";
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
						if(phone!=null&&phone.length()!=0)
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
						checkReportDTO.setStore(localBusinessDTO.getStore());
						int errorsCount = getErrorsCount(localBusinessDTO, checkReportDTO);
						checkReportDTO.setNoOfErrors(errorsCount);
						checkReportDTO.setCheckedDate(new Date());

						allMatchings.add(checkReportDTO);
					}
					if (allMatchings.size() > 0) {
						CheckReportDTO highestMatchingStore = getHighestMatchingStore(
								allMatchings, localBusinessDTO);

						logger.info("Inserting record for: " + localBusinessDTO.getStore());
						if ((highestMatchingStore.getPhone() != null && highestMatchingStore
								.getPhone().length() != 0)
								&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
										.getBusinessname().length() != 0)
								&& (highestMatchingStore.getZip() != null || highestMatchingStore
										.getZip().length() != 0)) {
							Integer listingId = service.isStoreExist(localBusinessDTO.getStore(),
									directory);

							if (listingId != 0) {
								highestMatchingStore.setListingId(listingId);
								service.updateCheckReportInfo(highestMatchingStore);
							} else {
								service.saveCheckReportInfo(highestMatchingStore);
							}
						}
					}

				} catch (Exception e) {
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					boolean isExist=service.isStoreAndDirectoryExist(localBusinessDTO.getStore(),directory);
					if(!isExist){
						service.saveIntoFailedScapes(scrapingsEntity);
					}
					logger.error("There was a issue while connecting to YP Domain with: "
							+ targetURL);
					e.printStackTrace();
				}finally{
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					service.deleteFailedScapingEntity(localBusinessDTO.getStore(),directory);
				}
			
				
			
	
			
			}
			if(failedScrapingsEntity.getDirectory().equals("mapquest")){
				List<SearchDomainDTO> domainsByStoreForGoogle = service.getDomainsByStore(failedScrapingsEntity.getStore(),
					       "www.mapquest.com");


				String businessName = "";
				String addr = "";
				String locality = "";
				String region = "";
				String zip = "";
				String phone = "";
				String targetURL = "";
				String website = "";
				String directory="mapquest";
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
						if(phone!=null&&phone.length()!=0)
							phone = getFormattedPhone(phone);
						checkReportDTO.setWebsite(website);
						checkReportDTO.setDirectory("mapquest");
						checkReportDTO.setBusinessname(businessName);
						checkReportDTO.setPhone(phone);
						checkReportDTO.setAddress(addr);
						checkReportDTO.setCity(locality);
						checkReportDTO.setState(region);
						checkReportDTO.setZip(zip);
						checkReportDTO.setStoreUrl(targetURL);
						checkReportDTO.setStore(localBusinessDTO.getStore());
						int errorsCount = getErrorsCount(localBusinessDTO, checkReportDTO);
						checkReportDTO.setNoOfErrors(errorsCount);
						checkReportDTO.setCheckedDate(new Date());

						allMatchings.add(checkReportDTO);
					}
					if (allMatchings.size() > 0) {
						CheckReportDTO highestMatchingStore = getHighestMatchingStore(
								allMatchings, localBusinessDTO);

						logger.info("Inserting record for: " + localBusinessDTO.getStore());
						if ((highestMatchingStore.getPhone() != null && highestMatchingStore
								.getPhone().length() != 0)
								&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
										.getBusinessname().length() != 0)
								&& (highestMatchingStore.getZip() != null || highestMatchingStore
										.getZip().length() != 0)) {
							Integer listingId = service.isStoreExist(localBusinessDTO.getStore(),
									directory);

							if (listingId != 0) {
								highestMatchingStore.setListingId(listingId);
								service.updateCheckReportInfo(highestMatchingStore);
							} else {
								service.saveCheckReportInfo(highestMatchingStore);
							}
						}
					}
				} catch (Exception e) {
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					boolean isExist=service.isStoreAndDirectoryExist(localBusinessDTO.getStore(),directory);
					if(!isExist){
						service.saveIntoFailedScapes(scrapingsEntity);
					}
					logger.error("There was a issue while connecting to MapQuest Domain with: "
							+ targetURL);
					e.printStackTrace();
				}finally{
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					service.deleteFailedScapingEntity(localBusinessDTO.getStore(),directory);
				}

			
			
	
			
			}
			if(failedScrapingsEntity.getDirectory().equals("superpages")){
				List<SearchDomainDTO> domainsByStoreForSuperPages = service.getDomainsByStore(failedScrapingsEntity.getStore(),
					       "www.superpages.com");


				String businessName = "";
				String addr = "";
				String locality = "";
				String region = "";
				String zip = "";
				String phone = "";
				String targetURL = "";
				String directory="superpages";
				try {
					List<CheckReportDTO> allMatchings = new ArrayList<CheckReportDTO>();
					for (SearchDomainDTO searchDomainDTO : domainsByStoreForSuperPages) {
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
						if(phone!=null&&phone.length()!=0)
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
						checkReportDTO.setStore(localBusinessDTO.getStore());
						int errorsCount = getErrorsCount(localBusinessDTO, checkReportDTO);
						checkReportDTO.setNoOfErrors(errorsCount);
						checkReportDTO.setCheckedDate(new Date());
						allMatchings.add(checkReportDTO);
					}
					if (allMatchings.size() > 0) {
						CheckReportDTO highestMatchingStore = getHighestMatchingStore(
								allMatchings, localBusinessDTO);

						logger.info("Inserting record for: " + localBusinessDTO.getStore());
						if ((highestMatchingStore.getPhone() != null && highestMatchingStore
								.getPhone().length() != 0)
								&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
										.getBusinessname().length() != 0)
								&& (highestMatchingStore.getZip() != null || highestMatchingStore
										.getZip().length() != 0)) {
							Integer listingId = service.isStoreExist(localBusinessDTO.getStore(),
									directory);

							if (listingId != 0) {
								highestMatchingStore.setListingId(listingId);
								service.updateCheckReportInfo(highestMatchingStore);
							} else {
								service.saveCheckReportInfo(highestMatchingStore);
							}
						}
					}

				} catch (Exception e) {
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					boolean isExist=service.isStoreAndDirectoryExist(localBusinessDTO.getStore(),directory);
					if(!isExist){
						service.saveIntoFailedScapes(scrapingsEntity);
					}
				
					logger.error("There was a issue while connecting to Suparepages Domain with: "
							+ targetURL);
					e.printStackTrace();
				}finally{
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					service.deleteFailedScapingEntity(localBusinessDTO.getStore(),directory);
				}
			
			
	
			
			}
			if(failedScrapingsEntity.getDirectory().equals("yellowbook")){
				List<SearchDomainDTO> domainsByStoreForYellowBook = service.getDomainsByStore(failedScrapingsEntity.getStore(),
					       "www.yellowbook.com");

				String businessName = "";
				String addr = "";
				String locality = "";
				String region = "";
				String zip = "";
				String phone = "";
				String targetURL = "";
				String directory="yellowbook";
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
								.timeout(10000).get();

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
						if(phone!=null&&phone.length()!=0)
							phone = getFormattedPhone(phone);

						checkReportDTO.setDirectory("yellowbook");
						checkReportDTO.setBusinessname(businessName);
						checkReportDTO.setPhone(phone);
						checkReportDTO.setAddress(addr);
						checkReportDTO.setCity(locality);
						checkReportDTO.setState(region);
						checkReportDTO.setZip(zip);
						checkReportDTO.setStoreUrl(targetURL);
						checkReportDTO.setStore(localBusinessDTO.getStore());
						int errorsCount = getErrorsCount(localBusinessDTO, checkReportDTO);
						checkReportDTO.setNoOfErrors(errorsCount);
						checkReportDTO.setCheckedDate(new Date());
						allMatchings.add(checkReportDTO);

					}
					if (allMatchings.size() > 0) {
						CheckReportDTO highestMatchingStore = getHighestMatchingStore(
								allMatchings, localBusinessDTO);

						logger.info("Inserting record for: " + localBusinessDTO.getStore());
						if ((highestMatchingStore.getPhone() != null && highestMatchingStore
								.getPhone().length() != 0)
								&& (highestMatchingStore.getBusinessname() != null || highestMatchingStore
										.getBusinessname().length() != 0)
								&& (highestMatchingStore.getZip() != null || highestMatchingStore
										.getZip().length() != 0)) {
						
							Integer listingId = service.isStoreExist(localBusinessDTO.getStore(),
									directory);

							if (listingId != 0) {
								highestMatchingStore.setListingId(listingId);
								service.updateCheckReportInfo(highestMatchingStore);
							} else {
								service.saveCheckReportInfo(highestMatchingStore);
							}

						}
					}
				} catch (Exception e) {
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					boolean isExist=service.isStoreAndDirectoryExist(localBusinessDTO.getStore(),directory);
					if(!isExist){
						service.saveIntoFailedScapes(scrapingsEntity);
					}
					logger.error("There was a issue while connecting to Yellowbook Domain with: "
							+ targetURL);

				}finally{
					FailedScrapingsEntity scrapingsEntity=new FailedScrapingsEntity();
					scrapingsEntity.setStore(localBusinessDTO.getStore());
					scrapingsEntity.setDirectory(directory);
					service.deleteFailedScapingEntity(localBusinessDTO.getStore(),directory);
				}
			
			
	
			
			}
			
		}
		
	
	}

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

		return dto;
	}

	private int getErrorsCount(LocalBusinessDTO localBusinessDto,
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
		if (!localBusinessDto.getLocationAddress().equalsIgnoreCase(
				checkReportDto.getAddress())) {
			count++;
		}
		if (!localBusinessDto.getLocationCity().equalsIgnoreCase(
				checkReportDto.getCity())) {
			count++;
		}
		if (!localBusinessDto.getLocationState().equalsIgnoreCase(
				checkReportDto.getState())) {
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
		if(address!=null && address.length() >0 && address.contains("www"))
		{
			address = address.substring(address.indexOf("www.")+4);
		}	
		if(webAddress!=null && webAddress.length() >0 && webAddress.contains("www"))
		{
			webAddress = webAddress.substring(webAddress.indexOf("www.")+4);
		}	
		if (!webAddress.equalsIgnoreCase(
				address )) {
			count++;
		}


		return count;
	
	}

	private String getFormattedPhone(String phone) {
		String locationPhone = phone;

		if (locationPhone != null) {
			locationPhone = locationPhone.replaceAll("\\s+", "");
		}

		if (locationPhone != null && locationPhone.contains("Local:")) {
			locationPhone = locationPhone.substring(locationPhone
					.indexOf("Local:"));

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

}
