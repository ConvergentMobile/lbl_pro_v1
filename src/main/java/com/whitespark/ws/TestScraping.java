package com.whitespark.ws;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/*import org.junit.Test;*/

import com.business.service.BusinessService;
/*import org.junit.Test;*/
/*import org.junit.Test;*/
/*import org.junit.Test;*/
/*import org.junit.Test;
 */
import com.business.common.dto.CheckReportDTO;

public class TestScraping {
	// @Test
	public void testBing() throws Exception {
		String targetURL = "http://www.bing.com/local/Details.aspx?lid=YN117x2058809&mkt=en-ca";

		Document doc = Jsoup.connect(targetURL).get();
		// System.out.println(doc);

		Element bname = doc.select("div.business_name").first();
		Element baddr = doc.select("span.business_address").first();
		Element bphone = doc.select("span.business_phone_number").first();
		Element link = doc.select("a#ContactCard_WebsiteLink").first();
		String website = link.attr("href");
		System.out.println("bus name: " + bname.text());

		System.out.println("baddr.text().: " + baddr.text());

		String addresData[] = baddr.text().split(",");

		String address = "";
		String city = "";
		String state = "";
		String zip = "";

		if (addresData.length == 3) {

			address = addresData[0];
			city = addresData[1];
			String stateAndZip[] = addresData[2].trim().split(" ");
			state = stateAndZip[0];
			zip = stateAndZip[1];

		}

		System.out.println("bus addr: " + address);
		System.out.println("bus city: " + city);
		System.out.println("bus state: " + state);
		System.out.println("bus zip: " + zip);
		System.out.println("bus phone: " + bphone.text());
		System.out.println("bus website: " + website);
	}

	// @Test
	public void testYahoo(BusinessService service) throws Exception {
		String targetURL = "https://local.yahoo.com/info-21404096-kingfish-san-mateo";

		Document doc = Jsoup.connect(targetURL).get();

		String addr = "";
		String phone = "";
		String city = "";
		String state = "";
		String zip = "";
		String website = "";
		String businessName = "";
		Elements el1 = doc.select("meta");
		for (Element el : el1) {
			if (el.attr("property").equals("og:title")) {
				businessName = el.attr("content");

			}

			if (el.attr("property")
					.equals("business:contact_data:phone_number")) {
				phone = el.attr("content");

			}

			if (el.attr("property").equals(
					"business:contact_data:street_address")) {

				addr += el.attr("content");
			}

			if (el.attr("property").equals("business:contact_data:locality")) {
				// addr += "\n" + el.attr("content") ;
				city = el.attr("content");

			}

			if (el.attr("property").equals("business:contact_data:region")) {
				// addr += el.attr("content") ;
				state = el.attr("content");

			}

			if (el.attr("property").equals("business:contact_data:postal_code")) {
				// addr += el.attr("content");
				zip = el.attr("content");

			}
			if (el.attr("property").equals("business:contact_data:website")) {
				website = el.attr("content");

			}

		}
		System.out.println("addr: " + addr);

		CheckReportDTO checkReportDTO = new CheckReportDTO();

		checkReportDTO.setBusinessname(businessName);
		checkReportDTO.setPhone(phone);
		checkReportDTO.setAddress(addr);
		checkReportDTO.setCity(city);
		checkReportDTO.setState(state);
		checkReportDTO.setZip(zip);
		checkReportDTO.setWebsite(website);
		service.saveCheckReportInfo(checkReportDTO);

	}

	// @Test
	public void testGoogle() throws Exception {
		String targetURL = "https://plus.google.com/110866572769960200301/about";
		targetURL = "https://plus.google.com/104945121074434434002/about";

		Document doc = Jsoup.connect(targetURL).get();
		System.out.println(doc);
		Element bname = doc.select("div[class=fpd KXa]").first();
		System.out.println("bus name: " + bname.text());

		String addr = "";
		String locality = null;
		String region = null;
		String zip = null;

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
		addr += "\n" + locality + ", " + region + " " + zip;
		System.out.println("addr:" + addr);

		Element phone = doc.select("div[class=Ny nkb]").first();
		Element website = doc.select("div[class=Ny qkb]").first();
		System.out.println("phone: " + phone.text());
		System.out.println("website: " + website.text());
	}

	// /@Test
	public void testYelp() throws Exception {

		String targetUrl = "http://www.yelp.com//biz/pnc-bank-pittsburgh-16";

		Document document = Jsoup.connect(targetUrl).get();
		System.out.println(document);
		String busName = null;
		/*
		 * Element link = document.select("div[class=biz-website]").first();
		 * 
		 * Element select = link.select("a").first();
		 * 
		 * String linkInnerH = select.html(); System.out.println(linkInnerH);
		 */

		Elements bname = document.select("h1");
		for (Element element : bname) {
			if (element.attr("itemprop").equals("name")) {
				busName = element.text();
			}
		}
		System.out.println("businessName ::" + busName);

		// System.out.println(document);
		String addr = "";
		String locality = null;
		String region = null;
		String zip = null;
		String phone = null;

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
		addr += "\n" + locality + ", " + region + " " + zip;
		System.out.println("addr:" + addr);

		System.out.println("phone: " + phone);
		/* System.out.println("website: " + linkInnerH); */
	}

	// @Test
	public void testYp() throws Exception {

		String targetUrl = "http://yp.ocregister.com/profile?listingId=125678356p";

		Document document = Jsoup.connect(targetUrl).get();
		System.out.println("document" + document);

		Elements web = document.select("div#partner-header-html");
		Elements attr = web.select("div#topper");

		/* String attr = web.attr("href"); */
		System.out.println(attr);
		String busName = null;
		Elements bname = document.select("h1");
		for (Element element : bname) {
			if (element.attr("itemprop").equals("name")) {
				busName = element.text();
			}
		}
		System.out.println("businessName ::" + busName);

		System.out.println(web);
		String addr = "";
		String locality = null;
		String region = null;
		String zip = null;
		String phone = null;
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
		addr += "\n" + locality + ", " + region + " " + zip;
		System.out.println("addr:" + addr);

		Elements phonenumber = document.select("div");
		for (Element element : phonenumber) {
			if (element.attr("itemprop").equals("telephone")) {
				phone = element.text();
			}
		}
		System.out.println("phone: " + phone);
	}

 //@Test
	public void testMapQuest() throws Exception {

		String targetUrl = "http://www.mapquest.com/us/new-york/auto-repair-latham/goodyear-auto-service-center-289439164";

		Document document = Jsoup.connect(targetUrl).get();
		System.out.println(document);
		String busName = null;
		String website = null;
		Elements bname = document.select("h1");
		for (Element element : bname) {
			if (element.attr("itemprop").equals("name")) {
				busName = element.text();
			}
		}
		Elements phn = document.select(".phone");
		String ph=null;
		for (Element element : phn) {
			ph = element.attr("href");
		}
		System.out.println("ph:::::"+ph);
		Elements websites = document.select("ul.action-bar");
		Elements select = websites.select("a");
		for (Element element : select) {
			if (element.attr("itemprop").equals("url sameAs")) {
				website = element.attr("href");
			}
		}
		
		Elements websitesval = document.select(".website");
		System.out.println(websitesval);
		for (Element element : websitesval) {
			website = element.attr("href");
		}
		System.out.println("businessName ::" + busName);

		System.out.println("website" + select);
		String addr = "";
		String locality = null;
		String region = null;
		String zip = null;
		String phone = null;

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
		addr += "\n" + locality + ", " + region + " " + zip;
		System.out.println("addr:" + addr);

		System.out.println("website::" + website);

		System.out.println("phone: " + phone);
	}

	 //@Test
	public void testSuperPages() throws Exception {

		String targetUrl = "http://www.superpages.com/bp/Rockford-IL/US-Cellular-Authorized-Agent-Dr-Detail-Inc-L2439792607.htm";

		Document document = Jsoup.connect(targetUrl).get();
		String busName = null;
		Elements bname = document.select("h1");

		Elements link = document.select("a#website");

		String website = link.attr("href");
		for (Element element : bname) {
			if (element.attr("id").equals("profileName")) {
				busName = element.text();
			}
		}
		System.out.println("businessName ::" + busName);

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

		System.out.println("website:" + website);
		System.out.println("phone: " + phone);
	}

	// @Test
	public void testYellowBook() throws Exception {

		String URL = "http://www.yellowbook.com/profile/pnc-bank_1860053414.html";
		Document document = Jsoup
				.connect(URL)
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
				.timeout(10000).get();

		/* Document document = Jsoup.connect(targetUrl).get(); */
		System.out.println(document);
		String busName = null;
		Elements bname = document.select("h2");
		Elements website = document.select("a.s_website");
		for (Element element : bname) {
			if (element.attr("id").equals("profileName")) {
				busName = element.text();
			}
		}
		System.out.println("businessName ::" + busName);

		String addr = "";
		String locality = null;
		String region = null;
		String zip = null;
		String phone = null;
		Elements el = document.select("div");
		System.out.println("el::" + el);

		for (Element element : el) {

			if (element.attr("class").equals("address"))
				addr = element.text();
			if (element.attr("class").equals("state"))
				locality = element.text();

			if (element.attr("class").equals("phoneNumber c")) {
				phone = element.text();
			}
		}
		addr += "\n" + locality + ", " + region + " " + zip;
		System.out.println("addr:" + addr);

		System.out.println("website:" + website);

		System.out.println("website:" + website);

		System.out.println("phone: " + phone);
	}

	//@Test
	public void testCitySearch() throws Exception {

		String URL = "http://www.citysearch.com/profile/42919771/west_hartford_ct/botticello_auto_repair.html";
		Document document = Jsoup
				.connect(URL)
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
				.timeout(30000).get();

		/* Document document = Jsoup.connect(targetUrl).get(); */
		System.out.println(document);
		String busName = null;

		String addr = "";
		String locality = null;
		String region = null;
		String zip = null;
		String phone = null;
		Elements tellphone = document.select("span.tel");

		for (Element element : tellphone) {

			if (element.attr("itemprop").equals("telephone"))
				phone = element.text();

		}
		Elements businessname = document.select("span.placeBusinessName");
		System.out.println("businessname::"+businessname);
		Elements val = document.select("p.placeAddress");
		for (Element element : val) {
			Elements select = element.select("span");
			for (Element element2 : select) {

				if (element2.attr("itemprop").equals("streetAddress"))
					addr = element2.text();
				if (element2.attr("itemprop").equals("postalCode"))
					zip = element2.text();
				if (element2.attr("itemprop").equals("addressLocality"))
					locality = element2.text();
				if (element2.attr("itemprop").equals("addressRegion"))
					region = element2.text();

			}

		}
		

		addr += "\n" + locality + ", " + region + " " + zip;
		System.out.println("addr:" + addr);
		// System.out.println("website:"+website);

		System.out.println("phone: " + phone);
	}
	//@Test
	public void testWhitePages() throws Exception {

		String URL = "http://www.whitepages.com/business/goodyear-auto-service-centers-west-hartford-ct";
		Document document = Jsoup
				.connect(URL)
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
				.timeout(50000).get();

		/* Document document = Jsoup.connect(targetUrl).get(); */
		System.out.println(document);
		String busName = null;

		String addr = "";
		String locality = null;
		String region = null;
		String zip = null;
		String phone = null;
		String state=null;
		
		//Element busiessName = document.select("input.business-typeahead").first();
		Element busiessName = document.select("span.name").first();
		//busName=busiessName.text();
		//phone=tellphone.text();
		System.out.println(busName);
		Elements tellphone = document.select("a.clickstream-link");
		for (Element element : tellphone) {
			if(element.attr("data-gaaction").equals("business")){
				phone=element.text();
			}
		}
		//phone=tellphone.attr("href");
		System.out.println(phone);
		
		Element addressval = document.select("span.address-primary").first();
		//addr=addressval.text();
		System.out.println(addr);
		Element location = document.select("span.locality ").first();
		
		locality=location.text();
		Element regions = document.select("span.region").first();
		region=regions.text();
		
		Element postalcode = document.select("span.postal-code").first();
		zip=postalcode.text();
		/*zip=postalcode.text();
		region=regions.text();
		locality=location.text();
		System.out.println(locality);
		String[] split = locality.split(",");
		locality= split[0];
		String stageZip = split[1];
		String[] split2 = stageZip.split("  ");
		state=split2[0];*/
		System.out.println("state: " + state);
		System.out.println("locality : " + locality);
		System.out.println("region: " + region);
		System.out.println("phone: " + phone);
		System.out.println("zip: " + zip);
	}

}
