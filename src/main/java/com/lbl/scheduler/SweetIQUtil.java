package com.lbl.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import au.com.bytecode.opencsv.CSVReader;

import com.business.common.dto.KeywordDTO;
import com.business.common.dto.SweetIQAccuracyDTO;
import com.business.common.dto.SweetIQCoverageDTO;
import com.business.common.dto.SweetIQDTO;
import com.business.common.dto.SweetIQRankingDTO;
import com.business.service.ListingService;
import com.business.service.SweetIQService;

public class SweetIQUtil {

	private static SweetIQService service;
	private static ListingService listingService;

	public SweetIQUtil(SweetIQService service, ListingService listingService) {
		this.service = service;
		this.listingService = listingService;
	}

	public static void updateSweetIQStoresInLBL(String clientId, Integer brandId) {

		String listings = "https://api.sweetiq.com/locations?apikey=580799607e7ec8ac6273b493&client_id="
				+ clientId + "&format=csv";

		CloseableHttpClient closableClient = HttpClientBuilder.create().build();

		HttpGet get = new HttpGet(listings);

		get.setHeader("Content-Type", "text/csv");
		String filename = "locations-" + clientId + ".csv";

		CloseableHttpResponse response;
		try {
			response = closableClient.execute(get);
			response.setHeader("Content-Disposition", "attachment; filename="
					+ filename + ".csv");

			InputStream in = response.getEntity().getContent();

			File file = new File(filename);
			FileOutputStream fos = new FileOutputStream(file);

			byte[] buffer = new byte[4096];
			int length;
			while ((length = in.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			fos.close();
			System.out.println("done");

			CSVReader reader = new CSVReader(new FileReader(filename), ',');

			String[] record = null;
			String[] header = reader.readNext();

			List<SweetIQDTO> listOfStores = new ArrayList<SweetIQDTO>();

			SweetIQDTO dto = null;
			while ((record = reader.readNext()) != null) {

				dto = new SweetIQDTO();
				String locId = record[0];
				String branchId = record[2];

				Long lblStoreID = listingService.getLBLStoreID(brandId,
						branchId);

				dto.setBrandId(brandId);
				dto.setsIQBranchId(branchId);
				dto.setStore(branchId);
				dto.setsIQLocationId(locId);
				dto.setsIQclientId(clientId);
				dto.setLblStoreId(lblStoreID);

				listOfStores.add(dto);
			}

			service.addOrUpdateStores(listOfStores);

			if (file.exists()) {
				file.delete();
				System.out.println("File deleted successfully");
			} else {
				System.out.println("Failed to delete the file");
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getAccuracyCSV(String apiKey, String clientId,
			String startDate, String endDate) {

		String targetURL = "https://api.sweetiq.com/locations/listings?"
				+ "format=csv&type=accuracy&search=&getMissingListings=0&duplicateStatus=primary&domains=bing.com"
				+ "&domains=facebook&domains=foursquare&domains=maps.google.com&domains=yellowpages.com"
				+ "&withErrors=name&withErrors=address&withErrors=phone&withErrors=website"
				+ "startDate=" + startDate + "&endDate=" + endDate + "&apikey="
				+ apiKey + "&client_id=" + clientId;

		CloseableHttpClient closableClient = HttpClientBuilder.create().build();

		HttpGet get = new HttpGet(targetURL);

		get.setHeader("Content-Type", "text/csv");

		String filename = "accuracy-" + clientId + "-" + startDate + ".csv";

		CloseableHttpResponse response;
		try {
			response = closableClient.execute(get);
			response.setHeader("Content-Disposition", "attachment; filename="
					+ filename + ".csv");

			InputStream in = response.getEntity().getContent();

			FileOutputStream fos = new FileOutputStream(new File(filename));

			byte[] buffer = new byte[4096];
			int length;
			while ((length = in.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			fos.close();

			System.out.println("done");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filename;
	}

	public static List<SweetIQCoverageDTO> getCoverageReport(String apiKey,
			String clientId, Integer brandId, String locId, String startDate,
			String endDate) {

		List<SweetIQCoverageDTO> coverageData = new ArrayList<SweetIQCoverageDTO>();

		String targetURL = "http://api.sweetiq.com/locations/v2/listings/coverage-by-date?apikey="
				+ apiKey
				+ "&client_id="
				+ clientId
				+ "&startDate="
				+ startDate
				+ "&endDate=" + endDate + "&locations=" + locId;

		try {

			CloseableHttpClient closableClient = HttpClientBuilder.create()
					.build();
			HttpGet getRequest = new HttpGet(targetURL);
			getRequest.addHeader("accept", "application/json");

			HttpResponse response = closableClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output;
			StringBuffer data = new StringBuffer();
			while ((output = br.readLine()) != null) {
				data.append(output);
			}
		
			if (!data.toString().equals("[]")) {
				
				JSONArray jsonarray = new JSONArray(data.toString());
				for (int i = 0; i < 1; i++) {
					SweetIQCoverageDTO dto = new SweetIQCoverageDTO();
					JSONObject jsonobject = jsonarray.getJSONObject(i);
					String date = (String) jsonobject.get("date");
					Integer locationCount = (Integer) jsonobject
							.get("locationCount");
					Integer totalListingCount = (Integer) jsonobject
							.get("totalListingCount");
					Integer possibleListings = (Integer) jsonobject
							.get("possibleListings");
					Integer coverage = (Integer) jsonobject.get("coverage");

					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss");


					dto.setDate(getDate(startDate));
					if (locationCount != null)
						dto.setLocationCount(locationCount);
	
					if (locationCount != null)
						dto.setLocationCount(locationCount);

					if (totalListingCount != null)
						dto.setTotalListingCount(totalListingCount);
					if (possibleListings != null)
						dto.setPossibleListings(possibleListings);
					if (coverage != null)
						dto.setCoverage(coverage);

					JSONObject byDomain = (JSONObject) jsonobject
							.get("byDomain");

					if (byDomain != null) {
						JSONObject bing = (JSONObject) byDomain.get("bing.com");
						if (bing != null) {
							Integer bingCoverage = (Integer) bing
									.get("coverage");
							dto.setBingCoverage(bingCoverage);
						}
						
						JSONObject facebook = (JSONObject) byDomain
								.get("facebook");
						if (facebook != null) {
							Integer facebookCoverage = (Integer) facebook
									.get("coverage");
							dto.setFacebookCoverage(facebookCoverage);
						}
						/*
						 * JSONObject yelp = (JSONObject)
						 * byDomain.get("yelp.com"); if (yelp != null) { Integer
						 * yelpCoverage = (Integer) bing.get("coverage");
						 * dto.setYelpCoverage(yelpCoverage); }
						 */
						JSONObject yellowpages = (JSONObject) byDomain
								.get("yellowpages.com");
						if (yellowpages != null) {
							Integer yellowCoverage = (Integer) yellowpages
									.get("coverage");
							dto.setYellowPagesCoverage(yellowCoverage);
						}
						JSONObject yahoo = (JSONObject) byDomain
								.get("local.yahoo.com");
						if (yahoo != null) {
							Integer yahooCoverage = (Integer) yahoo
									.get("coverage");
							dto.setFourSquareCoverage(yahooCoverage);
						}
						JSONObject google = (JSONObject) byDomain
								.get("maps.google.com");
						if (google != null) {
							Integer googleCoverage = (Integer) google
									.get("coverage");
							dto.setGoogleCoverage(googleCoverage);
						}
					}
					dto.setLocId(locId);
					dto.setBrandId(brandId);
					coverageData.add(dto);

				}
			}
			closableClient.getConnectionManager().shutdown();

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return coverageData;
	}

	public static void pullRanks(String apiKey, Integer brandId,
			String clientId, String locId, String searchType, String engine,
			String branhId, Long lblStoreId, String date) {

		List<SweetIQCoverageDTO> coverageData = new ArrayList<SweetIQCoverageDTO>();

		String targetURL = "https://api.sweetiq.com/locations/ranks/stats-by-keywords?apikey=580799607e7ec8ac6273b493"
				+ "&client_id="
				+ clientId
				+ "&locations="
				+ locId
				+ "&searchType="
				+ searchType
				+ "&engine="
				+ engine
				+ "&date="
				+ date;

		try {

			CloseableHttpClient closableClient = HttpClientBuilder.create()
					.build();
			HttpGet getRequest = new HttpGet(targetURL);
			getRequest.addHeader("accept", "application/json");

			HttpResponse response = closableClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output;
			StringBuilder data = new StringBuilder();
			while ((output = br.readLine()) != null) {
				data.append(output);
			}

			String rankData = data.toString();
			// System.out.println(rankData);

			JSONObject obj = new JSONObject(rankData);
			long totalRecords = obj.getInt("totalRecords");
			// System.out.println("totalRecords: " + totalRecords);

			if (totalRecords > 0) {
				JSONArray jsonarray = obj.getJSONArray("records");
				StringBuffer keywords = new StringBuffer();

				long rankedKeywords = 0;
				SweetIQRankingDTO dto = new SweetIQRankingDTO();
				int sumOfRanks = 0;

				List<KeywordDTO> keywordList = new ArrayList<KeywordDTO>();

				for (int i = 0; i < jsonarray.length(); i++) {

					KeywordDTO keywordDTO = new KeywordDTO();
					JSONObject jsonobject = jsonarray.getJSONObject(i);

					int rank = jsonobject.getInt("avgRank");
					String keyword = jsonobject.getString("keyword");
					int coverage = jsonobject.getInt("coverage");

					keywordDTO.setAvgRank(rank);
					keywordDTO.setKeyword(keyword);
					keywordDTO.setCoverage(coverage * 100);

					keywordList.add(keywordDTO);

					if (keywords.length() > 0)
						keywords.append(keyword);
					else {
						keywords.append("," + keyword);
					}

					sumOfRanks = sumOfRanks + rank;

					if (rank > 0)
						rankedKeywords++;

					// System.out.println(rank + "=" + keyword + "=" +
					// coverage);
				}

				Double coverage = Math
						.ceil(rankedKeywords * 100 / totalRecords);

				dto.setTotalKeyWords(totalRecords);
				dto.setRankedKeyWords(rankedKeywords);
				Double averageRank = 0.0;

				if (rankedKeywords > 0)
					averageRank = Math.ceil((sumOfRanks / rankedKeywords));

				dto.setAverageRank(averageRank);

				dto.setCoverage(coverage);

				dto.setBrandId(brandId);

				dto.setDate(getDate(date));

				dto.setLblStoreId(lblStoreId);

				dto.setEngine(engine);
				dto.setSearchType(searchType);

				dto.setsIQBranchId(branhId);

				dto.setsIQclientId(clientId);

				dto.setsIQLocationId(locId);

				service.addRankAndKeywords(dto, keywordList);

			}

			closableClient.getConnectionManager().shutdown();

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static Date getDate(String date) {

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dateValue = null;
		try {

			dateValue = formatter.parse(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dateValue;

	}

	public static void main(String[] args) {
		String date = "2017-06-15";
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dateValue = null;
		try {

			dateValue = formatter.parse(date);
			System.out.println(dateValue);
			// System.out.println(formatter.format(dateValue));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		// getAccuracyCSV();

		/*
		 * List<SweetIQCoverageDTO> coverageReport =
		 * getCoverageReport("58a7739a0b3fe9ce6faac396"
		 * ,"58b08dd4289d94c80469cf25");
		 * System.out.println(coverageReport.size()); for (SweetIQCoverageDTO
		 * sweetIQCoverageDTO : coverageReport) {
		 * System.out.println(sweetIQCoverageDTO.getDate() + "=== "+
		 * sweetIQCoverageDTO.getCoverage()); }
		 */

		// List<SweetIQCoverageDTO> coverageReport =
		// getStats("58a7739a0b3fe9ce6faac396","58b08dd4289d94c80469cf25");

		String y = "https://api.sweetiq.com/locations/ranks/stats-by-keywords?apikey=580799607e7ec8ac6273b493&client_id=58a7739a0b3fe9ce6faac396"
				+ "&locations=58b08dd8289d94c80469cf5f&searchType=local&engine=google&date=2017-10-08";

		// String w =
		// "https://api.sweetiq.com/locations/ranks/stats?apikey=580799607e7ec8ac6273b493&client_id=59ce67b9eb8380a3169b532e&&locations=59d63dee50ce533878f79533&startDate=2017-11-25&endDate=2017-12-25";

		// String z =
		// "https://api.sweetiq.com/locations/ranks/aggregate-by-date?apikey=580799607e7ec8ac6273b493&client_id=58a7739a0b3fe9ce6faac396&locations=58b08dd5289d94c80469cf2d&type=organic&startDate=2017-11-25&endDate=2017-12-24";

		String listings = "https://api.sweetiq.com/locations?apikey=580799607e7ec8ac6273b493&client_id=58a7739a0b3fe9ce6faac396&format=csv&page=100&perPage=100";

		// 580799607e7ec8ac6273b493 ->iberty

		// W---? monthly data google, bing, yahhoo, local and oraganic

		// Y fpr

		/*
		 * String x =
		 * "https://api.sweetiq.com/locations/ranks/stats-by-keywords?apikey=580799607e7ec8ac6273b493&client_id=58a7739a0b3fe9ce6faac396&locations=58b08dd6289d94c80469cf44&searchType=local&engine=google"
		 * ; // rank for brand
		 * 
		 * 
		 * String a =
		 * "https://api.sweetiq.com/locations/ranks?apikey=580799607e7ec8ac6273b493&client_id=58a7739a0b3fe9ce6faac396"
		 * ;
		 */

		try {

			CloseableHttpClient closableClient = HttpClientBuilder.create()
					.build();
			HttpGet getRequest = new HttpGet(y);
			getRequest.addHeader("accept", "application/json");

			HttpResponse response = closableClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output;
			StringBuilder data = new StringBuilder();
			while ((output = br.readLine()) != null) {
				data.append(output);
			}

			String rankData = data.toString();
			System.out.println(rankData);

			JSONObject obj = new JSONObject(rankData);
			long totalRecords = obj.getInt("totalRecords");
			// System.out.println("totalRecords: " + totalRecords);
			JSONArray jsonarray = obj.getJSONArray("records");
			StringBuffer keywords = new StringBuffer();

			long rankedKeywords = 0;
			SweetIQRankingDTO dto = new SweetIQRankingDTO();
			int sumOfRanks = 0;
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobject = jsonarray.getJSONObject(i);

				int rank = jsonobject.getInt("avgRank");
				String keyword = jsonobject.getString("keyword");
				int coverage = jsonobject.getInt("coverage");

				if (keywords.length() > 0)
					keywords.append(keyword);
				else {
					keywords.append("," + keyword);
				}

				sumOfRanks = sumOfRanks + rank;

				if (rank > 0)
					rankedKeywords++;

				// System.out.println(rank + "=" + keyword + "=" + coverage);
			}

			Double coverage = Math.ceil(rankedKeywords / totalRecords) * 100;

			dto.setTotalKeyWords(totalRecords);
			dto.setRankedKeyWords(rankedKeywords);

			Double averageRank = Math.ceil(sumOfRanks / totalRecords);

			dto.setAverageRank(averageRank);

			dto.setCoverage(coverage);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	static void processData() {
		try {

			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";

			br = new BufferedReader(new FileReader("C:\\lbl\\accuracy.csv"));
			SweetIQAccuracyDTO dto = null;
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] record = line.split(cvsSplitBy);

				dto = new SweetIQAccuracyDTO();
				String locId = record[0];
				String branchId = record[1];
				String directoryName = record[2];
				String directory = record[3];
				String listingURL = record[4];

				String listingName = record[5];
				String providedName = record[6];
				String listingNameMatch = record[7];

				String listingCivic = record[8];
				String providedCivic = record[9];
				String civicMatch = record[10];

				String streetAddress = record[11];
				String providedStreetAddress = record[12];
				String streetAdressMatch = record[13];

				String listingAddress = record[14];
				String locationAddress = record[15];
				String addressMatch = record[16];

				String city = record[17];
				String providedCity = record[18];
				String cityMatch = record[19];

				String listingState = record[20];
				String providedstate = record[21];
				String stateMatch = record[22];

				String zipcode = record[23];
				String providedZipcode = record[24];
				String zipcodeMatch = record[25];

				String country = record[26];
				String providedcountry = record[27];
				String countryMatch = record[28];

				String phone = record[29];
				String providedPhone = record[30];
				String phoneMatch = record[31];

				String website = record[32];
				String providedWebsite = record[33];
				String websiteMatch = record[34];

				String averageMatch = record[35];
				String duplicateStatus = record[36];

				dto.setLocId(locId);
				dto.setBranchId(branchId);
				dto.setDirectory(directory);
				dto.setDirectoryName(directoryName);
				dto.setURL(listingURL);
				dto.setName(listingName);
				dto.setProvided_Name(providedName);
				dto.setName_Match(listingNameMatch);
				dto.setCivic(listingCivic);
				dto.setProvided_Civic(providedCivic);
				dto.setCivic_Match(civicMatch);

				dto.setStreet_Address(streetAddress);
				dto.setProvided_Street_Address(providedStreetAddress);
				dto.setStreet_Address_Match(streetAdressMatch);

				dto.setAddress(listingAddress);
				dto.setLocation_Address(locationAddress);
				dto.setAddress_Match(addressMatch);

				dto.setCity(city);
				dto.setProvided_City(providedCity);
				dto.setCity_Match(cityMatch);

				dto.setProvince_State(listingState);
				dto.setProvided_Province_State(providedstate);
				dto.setProvince_State_Match(stateMatch);

				dto.setPostal_ZipCode(zipcode);
				dto.setProvided_PostalZipCode(providedZipcode);
				dto.setPostalZiCode_Match(zipcodeMatch);

				dto.setCountry(country);
				dto.setProvided_Country(providedcountry);
				dto.setCountry_Match(countryMatch);

				dto.setListing_Phone(phone);
				dto.setProvided_Phone(providedPhone);
				dto.setPhone_Match(phoneMatch);

				dto.setWebsite(website);
				dto.setProvided_Website(providedWebsite);
				dto.setWebsite_Match(websiteMatch);

				dto.setAverage_Match(averageMatch);
				dto.setDuplicate_Status(duplicateStatus);

				System.out.println(dto);

			}

			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static List<SweetIQAccuracyDTO> getAccuracyData(String apiKey,
			String clientId, Integer brandId, String startDate, String endDate) {

		List<SweetIQAccuracyDTO> accuracyReport = new ArrayList<SweetIQAccuracyDTO>();
		// TODO Auto-generated method stub
		try {

			String file = getAccuracyCSV(apiKey, clientId, startDate, endDate);

			CSVReader reader = new CSVReader(new FileReader(file), ',');

			String[] record = null;
			String[] header = reader.readNext();

			SweetIQAccuracyDTO dto = null;
			while ((record = reader.readNext()) != null) {

				dto = new SweetIQAccuracyDTO();
				String locId = record[0];
				String branchId = record[1];
				String directoryName = record[2];
				String directory = record[3];
				String listingURL = record[4];

				String listingName = record[5];
				String providedName = record[6];
				String listingNameMatch = record[7];

				String listingCivic = record[8];
				String providedCivic = record[9];
				String civicMatch = record[10];

				String streetAddress = record[11];
				String providedStreetAddress = record[12];
				String streetAdressMatch = record[13];

				String listingAddress = record[14];
				String locationAddress = record[15];
				String addressMatch = record[16];

				String city = record[17];
				String providedCity = record[18];
				String cityMatch = record[19];

				String listingState = record[20];
				String providedstate = record[21];
				String stateMatch = record[22];

				String zipcode = record[23];
				String providedZipcode = record[24];
				String zipcodeMatch = record[25];

				String country = record[26];
				String providedcountry = record[27];
				String countryMatch = record[28];

				String phone = record[29];
				String providedPhone = record[30];
				String phoneMatch = record[31];

				String website = record[32];
				String providedWebsite = record[33];
				String websiteMatch = record[34];

				String averageMatch = record[35];
				String duplicateStatus = record[36];

				dto.setLocId(locId);
				dto.setBranchId(branchId);
				dto.setDirectory(directory);
				dto.setDirectoryName(directoryName);
				dto.setURL(listingURL);
				dto.setName(listingName);
				dto.setProvided_Name(providedName);
				dto.setName_Match(listingNameMatch);
				dto.setCivic(listingCivic);
				dto.setProvided_Civic(providedCivic);
				dto.setCivic_Match(civicMatch);

				dto.setStreet_Address(streetAddress);
				dto.setProvided_Street_Address(providedStreetAddress);
				dto.setStreet_Address_Match(streetAdressMatch);

				dto.setAddress(listingAddress);
				dto.setLocation_Address(locationAddress);
				dto.setAddress_Match(addressMatch);

				dto.setCity(city);
				dto.setProvided_City(providedCity);
				dto.setCity_Match(cityMatch);

				dto.setProvince_State(listingState);
				dto.setProvided_Province_State(providedstate);
				dto.setProvince_State_Match(stateMatch);

				dto.setPostal_ZipCode(zipcode);
				dto.setProvided_PostalZipCode(providedZipcode);
				dto.setPostalZiCode_Match(zipcodeMatch);

				dto.setCountry(country);
				dto.setProvided_Country(providedcountry);
				dto.setCountry_Match(countryMatch);

				dto.setListing_Phone(phone);
				dto.setProvided_Phone(providedPhone);
				dto.setPhone_Match(phoneMatch);

				dto.setWebsite(website);
				dto.setProvided_Website(providedWebsite);
				dto.setWebsite_Match(websiteMatch);

				dto.setAverage_Match(averageMatch);
				dto.setDuplicate_Status(duplicateStatus);

				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date dateValue = null;
				try {

					dateValue = formatter.parse(startDate);
					System.out.println(dateValue);
					// System.out.println(formatter.format(dateValue));

				} catch (ParseException e) {
					e.printStackTrace();
				}

				dto.setDate(dateValue);
				dto.setBrandId(brandId);
				accuracyReport.add(dto);

			}

			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accuracyReport;
	}

	public void updateSweetIQStoresInLBL() {
		// TODO Auto-generated method stub

	}

	public List<SweetIQRankingDTO> getRanks() {
		// TODO Auto-generated method stub
		List<SweetIQRankingDTO> ranks = service.getRanks();
		return ranks;
	}

	public List<SweetIQDTO> getSweetIQLBLMap(Integer brandId) {
		// TODO Auto-generated method stub
		return service.getSweetIQLBLMap(brandId);
	}

}
