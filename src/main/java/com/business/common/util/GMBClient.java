package com.business.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jfree.util.Log;

import com.business.common.dto.LocalBusinessDTO;
import com.business.service.BusinessService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.mybusiness.v4.MyBusiness;
import com.google.api.services.mybusiness.v4.MyBusiness.Accounts.Locations;
import com.google.api.services.mybusiness.v4.model.Account;
import com.google.api.services.mybusiness.v4.model.Admin;
import com.google.api.services.mybusiness.v4.model.BasicMetricsRequest;
import com.google.api.services.mybusiness.v4.model.BusinessHours;
import com.google.api.services.mybusiness.v4.model.Category;
import com.google.api.services.mybusiness.v4.model.Date;
import com.google.api.services.mybusiness.v4.model.DimensionalMetricValue;
import com.google.api.services.mybusiness.v4.model.GoogleUpdatedLocation;
import com.google.api.services.mybusiness.v4.model.ListAccountsResponse;
import com.google.api.services.mybusiness.v4.model.ListLocationAdminsResponse;
import com.google.api.services.mybusiness.v4.model.ListLocationsResponse;
import com.google.api.services.mybusiness.v4.model.ListReviewsResponse;
import com.google.api.services.mybusiness.v4.model.Location;
import com.google.api.services.mybusiness.v4.model.LocationMetrics;
import com.google.api.services.mybusiness.v4.model.MetricRequest;
import com.google.api.services.mybusiness.v4.model.MetricValue;
import com.google.api.services.mybusiness.v4.model.OpenInfo;
import com.google.api.services.mybusiness.v4.model.PostalAddress;
import com.google.api.services.mybusiness.v4.model.ReportLocationInsightsRequest;
import com.google.api.services.mybusiness.v4.model.ReportLocationInsightsResponse;
import com.google.api.services.mybusiness.v4.model.Review;
import com.google.api.services.mybusiness.v4.model.SpecialHourPeriod;
import com.google.api.services.mybusiness.v4.model.SpecialHours;
import com.google.api.services.mybusiness.v4.model.TimePeriod;
import com.google.api.services.mybusiness.v4.model.TimeRange;

/**
 * Simple client for the Google My Business API.
 */
public class GMBClient {

	static Logger logger = Logger.getLogger(GMBClient.class);
	static String startDate = LBLConstants.GMB_INSIGHTS_STARTDATE;
	static String endDate = LBLConstants.GMB_INSIGHTS_ENDDATE;
	static String dataCollectionType = LBLConstants.GMB_INSIGHTS_CoLLECTIONS_TYPE;
	/**
	 * Be sure to specify the name of your application. If the application name
	 * is {@code null} or blank, the application will log a warning. Suggested
	 * format is "MyCompany-ProductName/1.0".
	 */
	private static final String APPLICATION_NAME = "mybusinessapi-144208";

	private static final java.io.File DATA_STORE_DIR = new java.io.File("home",
			".store/mybusiness_sample");

	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();
	private static FileDataStoreFactory dataStoreFactory;
	private static HttpTransport httpTransport;
	private static MyBusiness mybusiness;
	private static BusinessService service;

	public GMBClient(BusinessService service) {
		try {
			this.service = service;
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("lbl_pro.p12")
					.getFile());

			GoogleCredential credential = new GoogleCredential.Builder()
					.setTransport(httpTransport)
					.setJsonFactory(JSON_FACTORY)
					.setServiceAccountId("lbl-pro@appspot.gserviceaccount.com")
					.setServiceAccountPrivateKeyFromP12File(file)
					// .setServiceAccountPrivateKeyFromP12File(new
					// File(url.toString()))
					.setServiceAccountScopes(
							Collections
									.singleton("https://www.googleapis.com/auth/plus.business.manage"))
					.setServiceAccountUser("convergentmobile@gmail.com")
					.build();

			mybusiness = new MyBusiness.Builder(httpTransport, JSON_FACTORY,
					credential).setApplicationName(APPLICATION_NAME).build();

		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Account> listAccounts() throws Exception {
		MyBusiness.Accounts.List listAccounts = mybusiness.accounts().list();
		ListAccountsResponse response = listAccounts.execute();

		List<Account> accounts = response.getAccounts();
		for (Account account : accounts) {
			System.out.println("============>>" + account.getAccountName()
					+ ", and name: " + account.getName());

		}
		return response.getAccounts();
	}

	public static List<Review> listReviews(String locationName)
			throws Exception {
		Locations.Reviews.List reviewsList = mybusiness.accounts().locations()
				.reviews().list(locationName);
		ListReviewsResponse response = reviewsList.execute();
		List<Review> reviews = response.getReviews();

		/*		for (Review review : reviews) {
			System.out.println(review.toPrettyString());

		}*/

		return reviews;
	}

	public static Account findPersonalAccount(List<Account> accounts) {
		for (Account account : accounts) {
			if (account.getType().equals("PERSONAL")) {
				return account;
			}
		}
		throw new RuntimeException("All users should have a PERSONAL account");
	}

	public static Account findBusinessAccount(List<Account> accounts) {
		for (Account account : accounts) {
			if (account.getType().equals("BUSINESS")
					&& account.getAccountName().contains("PODS")) {
				return account;
			}
		}
		throw new RuntimeException("All users should have a PERSONAL account");
	}

	public static Account findBusinessAccountByName(List<Account> accounts,
			String brandName) {
		for (Account account : accounts) {
			if (account.getType().equals("BUSINESS")
					&& account.getAccountName().contains(brandName)) {
				return account;
			}
		}
		throw new RuntimeException("All users should have a PERSONAL account");
	}

	private static Location findGoogleSydneyLocation(List<Location> locations) {
		if (locations != null) {
			for (Location location : locations) {
				System.out.println("Store code:" + location.getStoreCode());
				if ("GOOG-SYD".equals(location.getStoreCode())) {
					return location;
				}
			}
		}
		return null;
	}

	public static List<Location> listLocations(Account account)
			throws Exception {

		List<Location> locationsList = new ArrayList<Location>();

		MyBusiness.Accounts.Locations.List locationsListRequest = mybusiness
				.accounts().locations().list(account.getName());
		locationsListRequest.setPageSize(100);

		ListLocationsResponse locationsResult = locationsListRequest.execute();

		if (locationsResult != null) {
			List<Location> locations = locationsResult.getLocations();
			// System.out.println("===================>>>: " +
			// locations.size());
			if (locations != null)
				locationsList.addAll(locations);

			while (locationsResult.getNextPageToken() != null) {

				locationsListRequest.setPageToken(locationsResult
						.getNextPageToken());
				locationsResult = locationsListRequest.execute();
				locations = locationsResult.getLocations();
				locationsList.addAll(locations);
			}

		}

		System.out.println("Total locations Found for Account is: "
				+ locationsList.size());
		return locationsList;
	}

	public static List<Location> list100Locations(Account account)
			throws Exception {

		List<Location> locationsList = new ArrayList<Location>();

		MyBusiness.Accounts.Locations.List locationsListRequest = mybusiness
				.accounts().locations().list(account.getName());
		locationsListRequest.setPageSize(500);

		ListLocationsResponse locationsResult = locationsListRequest.execute();

		if (locationsResult != null) {
			List<Location> locations = locationsResult.getLocations();
			// System.out.println("===================>>>: " +
			// locations.size());
			if (locations != null)
				locationsList.addAll(locations);

		}

		System.out.println("Total locations Found for Account is: "
				+ locationsList.size());
		return locationsList;
	}

	public static List<Location> searchLocations(Account account)
			throws Exception {
		System.out.println("Search Locations");
		MyBusiness.Accounts.Locations.List listLocations = mybusiness
				.accounts().locations().list(account.getName());
		listLocations
				.setFilter("location.categories : \"gcid:software_company\"");
		// Alternate example filter
		// listLocations.setFilter("location.locationName = \"Google Sydney\"");
		ListLocationsResponse response = listLocations.execute();
		List<Location> locations = response.getLocations();

		if (locations != null) {
			for (Location location : locations) {
				System.out.println(location);
			}
		} else {
			System.out
					.printf("Account '%s' has no locations with gcid:software_company.",
							account.getName());
		}

		return locations;
	}

	public static void updateLocation(String locationName, String name)
			throws Exception {
		Location location = new Location().setName(locationName)
				.setLocationName(name);

		MyBusiness.Accounts.Locations.Patch updateLocation = mybusiness
				.accounts().locations().patch(locationName, location);
		updateLocation.setAttributeMask("location_name");
		//updateLocation.setLanguageCode("en-AU");
		Location updatedLocation = updateLocation.execute();

		System.out.printf("Updated Location:\n%s", updatedLocation);
	}

	public static void setHolidayHours(String locationName) throws Exception {
		Date newYearsEve = new Date().setYear(2015).setMonth(12).setDay(31);
		Date newYearsDay = new Date().setYear(2016).setMonth(1).setDay(1);
		List<SpecialHourPeriod> periods = new ArrayList<SpecialHourPeriod>();
		periods.add(new SpecialHourPeriod().setStartDate(newYearsEve)
				.setOpenTime("11:00").setCloseTime("23:30")
				.setEndDate(newYearsEve));
		periods.add(new SpecialHourPeriod().setStartDate(newYearsDay)
				.setIsClosed(true));
		SpecialHours holidayHours = new SpecialHours()
				.setSpecialHourPeriods(periods);
		Location location = new Location().setSpecialHours(holidayHours);

		MyBusiness.Accounts.Locations.Patch updateLocation = mybusiness
				.accounts().locations().patch(locationName, location);
		updateLocation.getRequestHeaders().set("X-GOOG-API-FORMAT-VERSION", 2);
		updateLocation.setAttributeMask("special_hours");
		//updateLocation.setLanguageCode("en-AU");
		Location updatedLocation = updateLocation.execute();
		System.out.println("Holiday hours set:" + updatedLocation);
	}

//	public static void setPhotos(String locationName) throws Exception {
//		
//	
//		Location location = new Location().setPhotos(new Photos()
//						.setTeamPhotoUrls(
//								Collections
//										.singletonList("https://lh3.googleusercontent.com/-ccTboHHmaWc/Udtina8CDnI/AAAAAAAABFU/hwYAS9RuY7U/s696/R51C4274.jpg"))
//						.setExteriorPhotoUrls(
//								Collections
//										.singletonList("https://lh3.googleusercontent.com/-VdwKC7Sf2r4/UdtiS0PikDI/AAAAAAAABFU/fA3ehF7XsB0/s696/R51C4675.jpg")));
//		MyBusiness.Accounts.Locations.Patch updateLocation = mybusiness
//				.accounts().locations().patch(locationName, location);
//		//updateLocation.setLanguageCode("en-AU");
//		updateLocation
//				.setAttributeMask("photos.exterior_photo_urls,photos.team_photo_urls");
//		Location updatedLocation = updateLocation.execute();
//		System.out.println("Photos set:" + updatedLocation);
//	}

	public static void getGoogleUpdates(String locationName) throws Exception {
		MyBusiness.Accounts.Locations.GetGoogleUpdated googleUpdated = mybusiness
				.accounts().locations().getGoogleUpdated(locationName);
		GoogleUpdatedLocation googleUpdates = googleUpdated.execute();

		System.out.println("Mask of changed fields:"
				+ googleUpdates.getDiffMask());
		System.out.printf("Google Updates:\n%s", googleUpdates.getLocation());
	}

	public static Location createLocation(String accountName) throws Exception {
		System.out.println("Creating Location");

		// Street address
		List addressLines = new ArrayList();
		addressLines.add("Level 5, 48 Pirrama Road");
		PostalAddress address = new PostalAddress().setAddressLines(addressLines)
				.setLocality("Pyrmont").setAdministrativeArea("NSW")
				.setRegionCode("AU").setPostalCode("2009");

		// Business hours
		List<TimePeriod> periods = new ArrayList<TimePeriod>();
		List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday");

		for (String day : days) {
			TimePeriod period = new TimePeriod().setOpenDay(day)
					.setOpenTime("9:00").setCloseTime("17:00").setCloseDay(day);
			periods.add(period);
		}
		BusinessHours businessHours = new BusinessHours().setPeriods(periods);
		Location location = new Location()
				.setAddress(address)
				.setRegularHours(businessHours)
				.setLocationName("Google Sydney")
				.setStoreCode("GOOG-SYD")
				.setPrimaryPhone("02 9374 4000")
				.setPrimaryCategory(
						new Category().setDisplayName("gcid:Software Company"))
				.setWebsiteUrl("https://www.google.com.au/");

		MyBusiness.Accounts.Locations.Create createLocation = mybusiness
				.accounts().locations().create(accountName, location);
		createLocation.setRequestId("1a84939c-ab7d-4581-8930-ee35af6fefac");
		
		//createLocation.setLanguageCode("en-AU");
		createLocation.getRequestHeaders().set("X-GOOG-API-FORMAT-VERSION", 2);
		Location createdLocation = createLocation.execute();

		System.out.printf("Created Location:\n%s", createdLocation);
		return createdLocation;
	}

	public static void listLocationAdmins(String locationName) throws Exception {
		MyBusiness.Accounts.Locations.Admins.List listAdmins = mybusiness
				.accounts().locations().admins().list(locationName);
		ListLocationAdminsResponse response = listAdmins.execute();

		List<Admin> admins = response.getAdmins();
		if (admins != null) {
			for (Admin admin : admins) {
				System.out.println(admin);
			}
		} else {
			System.out.printf("Location '%s' has no admins.", locationName);
		}
	}

	public static void inviteAdminToLocation(String locationName, String email)
			throws Exception {
		Admin invitee = new Admin().setName(locationName).setAdminName(email)
				.setRole("MANAGER");

		MyBusiness.Accounts.Locations.Admins.Create createAdmin = mybusiness
				.accounts().locations().admins().create(locationName, invitee);
		Admin response = createAdmin.execute();
		System.out.println("Admin invited:" + response);
	}

	/**
	 * Authorizes the installed application to access user's protected data.
	 */
	private static Credential authorize() throws Exception {
		// load client secrets
		InputStream secrets = GMBClient.class
				.getResourceAsStream("/client_secrets.json");
		if (secrets == null) {
			System.out
					.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=plus "
							+ "into google-my-business-api-sample/src/main/resources/client_secrets.json");
			System.exit(1);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY, new InputStreamReader(secrets));
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret()
						.startsWith("Enter ")) {
			System.out
					.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=plus "
							+ "into google-my-business-api-sample/src/main/resources/client_secrets.json");
			System.exit(1);
		}

		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport,
				JSON_FACTORY,
				clientSecrets,
				Collections
						.singleton("https://www.googleapis.com/auth/plus.business.manage"))
				.setDataStoreFactory(dataStoreFactory).build();

		// authorize
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize(clientSecrets.getDetails()
				.getClientId());
	}

	/**
	 * 
	 * @param localBusinessDTO
	 * @throws Exception
	 */
	public static void updateListingOnGoogle(LocalBusinessDTO localBusinessDTO)
			throws Exception {
		Location location = new Location();

		// Street address
		List<String> addressLines = new ArrayList<String>();
		addressLines.add(localBusinessDTO.getLocationAddress());
		String suite = localBusinessDTO.getSuite();
		if (suite != null && suite.length() > 0) {

			addressLines.add("Suite " + suite);
		}
		PostalAddress address = new PostalAddress().setAddressLines(addressLines)
				.setLocality(localBusinessDTO.getLocationCity())
				.setAdministrativeArea(localBusinessDTO.getLocationState())
				.setRegionCode(localBusinessDTO.getCountryCode())
				.setPostalCode(localBusinessDTO.getLocationZipCode());

		// Business hours
		List<TimePeriod> periods = new ArrayList<TimePeriod>();
		List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday");

		for (String day : days) {
			TimePeriod period = null;
			String mondayClose = localBusinessDTO.getMondayClose();
			String mondayOpen = localBusinessDTO.getMondayOpen();
			String tuesdayOpen = localBusinessDTO.getTuesdayOpen();
			String tuesdayClose = localBusinessDTO.getTuesdayClose();
			String wednesdayOpen = localBusinessDTO.getWednesdayOpen();
			String wednesdayClose = localBusinessDTO.getWednesdayClose();
			String thursdayOpen = localBusinessDTO.getThursdayOpen();
			String thursdayClose = localBusinessDTO.getThursdayClose();
			String fridayClose = localBusinessDTO.getFridayClose();
			String fridayOpen = localBusinessDTO.getFridayOpen();
			String saturdayOpen = localBusinessDTO.getSaturdayOpen();
			String saturdayClose = localBusinessDTO.getSaturdayClose();
			if (mondayOpen != null && day.equalsIgnoreCase("Monday")
					&& !mondayOpen.equalsIgnoreCase("CLOSE")
					&& mondayOpen.length() > 0) {

				period = new TimePeriod().setOpenDay(day)
						.setOpenTime(mondayOpen).setCloseTime(mondayClose)
						.setCloseDay(day);
				periods.add(period);
			}
			if (tuesdayOpen != null && day.equalsIgnoreCase("Tuesday")
					&& !tuesdayOpen.equalsIgnoreCase("CLOSE")
					&& tuesdayOpen.length() > 0) {

				period = new TimePeriod().setOpenDay(day)
						.setOpenTime(tuesdayOpen).setCloseTime(tuesdayClose)
						.setCloseDay(day);
				periods.add(period);
			}
			if (wednesdayOpen != null && day.equalsIgnoreCase("Wednesday")
					&& !wednesdayOpen.equalsIgnoreCase("CLOSE")
					&& wednesdayOpen.length() > 0) {

				period = new TimePeriod().setOpenDay(day)
						.setOpenTime(wednesdayOpen)
						.setCloseTime(wednesdayClose).setCloseDay(day);
				periods.add(period);
			}
			if (thursdayOpen != null && day.equalsIgnoreCase("Thursday")
					&& !thursdayOpen.equalsIgnoreCase("CLOSE")
					&& thursdayOpen.length() > 0) {

				period = new TimePeriod().setOpenDay(day)
						.setOpenTime(thursdayOpen).setCloseTime(thursdayClose)
						.setCloseDay(day);
				periods.add(period);
			}
			if (fridayOpen != null && day.equalsIgnoreCase("Friday")
					&& !fridayOpen.equalsIgnoreCase("CLOSE")
					&& fridayOpen.length() > 0) {

				period = new TimePeriod().setOpenDay(day)
						.setOpenTime(fridayOpen).setCloseTime(fridayClose)
						.setCloseDay(day);
				periods.add(period);
			}
			if (saturdayOpen != null && day.equalsIgnoreCase("Saturday")
					&& !saturdayOpen.equalsIgnoreCase("CLOSE")
					&& saturdayOpen.length() > 0) {

				period = new TimePeriod().setOpenDay(day)
						.setOpenTime(saturdayOpen).setCloseTime(saturdayClose)
						.setCloseDay(day);
				periods.add(period);
			}

		}
		BusinessHours businessHours = new BusinessHours().setPeriods(periods);

		// OpenInfo
		OpenInfo openInfo = new OpenInfo();
		openInfo.setStatus("OPEN");

		String locationResourceName = location.getName();
		location.setName(null)
				.setAddress(address)
				.setRegularHours(businessHours)
				.setLocationName(localBusinessDTO.getCompanyName())
				.setStoreCode(localBusinessDTO.getStore())
				.setPrimaryPhone(localBusinessDTO.getLocationPhone())
				.setPrimaryCategory(
						new Category().setDisplayName("Credit").setCategoryId(
								"gcid:credit_union")).setOpenInfo(openInfo);

		if (localBusinessDTO.getClientId() == 3351) {
			location.setLocationName(localBusinessDTO.getAlternativeName());
		}
		location.setWebsiteUrl(localBusinessDTO.getWebAddress());
		String name = "accounts/" + localBusinessDTO.getGoogleAccountId()
				+ "/locations/" + localBusinessDTO.getGoogleLocationId();

		System.out.println("name is: " + name);

		System.out.println("Location details before update: " + location);

		MyBusiness.Accounts.Locations.Patch updateLocation = mybusiness
				.accounts().locations().patch(name, location);
		// Aspen dental
		if (localBusinessDTO.getClientId() == 3471) {
			updateLocation
					.setAttributeMask(
							"storeCode,locationName,address,regularHours,openInfo")
					.setValidateOnly(false).getRequestHeaders()
					.set("X-GOOG-API-FORMAT-VERSION", 2);
		}
		// Spring and Liberrty tax
		else if (localBusinessDTO.getClientId() == 3225
				|| localBusinessDTO.getClientId() == 3456
				|| localBusinessDTO.getClientId() == 3628) {
			updateLocation
					.setAttributeMask(
							"storeCode,locationName,address,primaryPhone,regularHours,openInfo")
					.setValidateOnly(false).getRequestHeaders()
					.set("X-GOOG-API-FORMAT-VERSION", 2);
		} else {
			// all other brands
			updateLocation
					.setAttributeMask(
							"storeCode,locationName,address,primaryPhone,websiteUrl,regularHours,openInfo")
					.setValidateOnly(false).getRequestHeaders()
					.set("X-GOOG-API-FORMAT-VERSION", 2);
		}
		Location updatedLocation = updateLocation.execute();

		logger.info("Updated Location after Patch: " + updatedLocation);
	}

	public static void updateLocation() throws Exception {

		Location location = new Location();

		// Street address
		List<String> addressLines = new ArrayList<String>();
		addressLines.add("310 Pittsboro St");
		
		//PostalAddress address = new PostalAddress();
		
		PostalAddress address = new PostalAddress().setAddressLines(addressLines)
				.setLocality("Chapel Hill").setAdministrativeArea("NC").setRegionCode("US")
				.setPostalCode("27516");

		// Additional Phones
		List<String> additionalPhones = new ArrayList<String>();
		additionalPhones.add("(919) 962-6551");

		// Business hours
		List<TimePeriod> periods = new ArrayList<TimePeriod>();
		List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday");

		for (String day : days) {
			TimePeriod period = new TimePeriod().setOpenDay(day)
					.setOpenTime("CLOSE").setCloseTime("CLOSE")
					.setCloseDay(day);

			periods.add(period);
		}
		BusinessHours businessHours = new BusinessHours().setPeriods(periods);
		

		// OpenInfo
		OpenInfo openInfo = new OpenInfo();
		openInfo.setStatus("OPEN");

		String locationResourceName = location.getName();
		location.setName(null)
				.setAddress(address)
				.setRegularHours(businessHours)
				.setLocationName("State Employees' Credit Union")
				.setStoreCode("1")
				.setPrimaryPhone("(919) 962-6551")
				.setAdditionalPhones(additionalPhones)
				.setPrimaryCategory(
						new Category().setDisplayName("Credit").setCategoryId(
								"gcid:credit_union")).setOpenInfo(openInfo);

		MyBusiness.Accounts.Locations.Patch updateLocation = mybusiness
				.accounts()
				.locations()
				.patch("accounts/114515985899098925999/locations/16805323811872814884",
						location);
		
		updateLocation.setAttributeMask("storeCode,locationName,address,primaryPhone,websiteUrl,regularHours,openInfo")
				.setValidateOnly(false).getRequestHeaders()
				.set("X-GOOG-API-FORMAT-VERSION", 2);
		Location updatedLocation = updateLocation.execute();
		System.out.println("Locations after update " + updatedLocation);
	}

	public void getReportInsights(Account account, String googleAccountId) {

		List<Location> locations = new ArrayList<Location>();
		try {
			locations = listLocations(account);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		List<LocalBusinessDTO> listOfBussinessByBrandName = service
				.getStoresByGMBAccount(googleAccountId);
		Log.debug("Total locations found for: " + googleAccountId
				+ " in lbl is " + listOfBussinessByBrandName.size());

		Integer clientId = 0;

		if (listOfBussinessByBrandName.size() > 0) {
			clientId = listOfBussinessByBrandName.get(0).getClientId();
		}

		List<String> stores = new ArrayList<String>();
		List<String> storesForRemove = new ArrayList<String>();
		Map<String, Location> locationMap = new HashMap<String, Location>();
		for (LocalBusinessDTO localBusinessDTO : listOfBussinessByBrandName) {
			stores.add(localBusinessDTO.getStore());
			storesForRemove.add(localBusinessDTO.getStore());
		}
		for (Location location : locations) {
			locationMap.put(location.getStoreCode(), location);
		}
		String[] dates = null;
		// Code to modify if re re run
		/*
		 * if (dataCollectionType.equalsIgnoreCase("latest")) { dates =
		 * getStartandEndDates(2); } else if
		 * (dataCollectionType.equalsIgnoreCase("range")) { dates =
		 * getStartandEndDates(); }
		 */

		dates = getStartandEndDates(dataCollectionType);

		List<java.util.Date> datesList = null;

		if (dataCollectionType.equalsIgnoreCase("latest")) {

			java.util.Date formattedDate = getFormattedDate(dates[1]);
			System.out.println("clientId: " + clientId + ", formatted Date is:"
					+ formattedDate);

			datesList = new ArrayList<java.util.Date>();
			datesList.add(formattedDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(formattedDate);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			java.util.Date date2 = cal.getTime();
			datesList.add(date2);

			// delete records for exising dates
			for (int i = 0; i < datesList.size(); i++) {
				System.out.println("deleting for " + datesList.get(i));
				service.deleteExistingRecords(clientId, datesList.get(i));
			}
		} else if (dataCollectionType.equalsIgnoreCase("range")) {

			/*
			 * List<java.util.Date> listOfDates = getListOfDates(
			 * getFormattedDate(dates[0]), getFormattedDate(dates[1]));
			 * 
			 * // delete records for exising dates for (int i = 0; i <
			 * listOfDates.size(); i++) { System.out.println("deleting for " +
			 * listOfDates.get(i)); service.deleteExistingRecords(clientId,
			 * listOfDates.get(i)); }
			 */

			service.deleteExistingRecords(clientId, getFormattedDate(dates[0]));
			service.deleteExistingRecords(clientId, getFormattedDate(dates[1]));
			System.out.println("deleting old records for " + clientId
					+ " from " + getFormattedDate(dates[0]) + " to "
					+ getFormattedDate(dates[1]));
			service.deleteExistingRecords(clientId, getFormattedDate(dates[0]),
					getFormattedDate(dates[1]));
		}

		for (String store : stores) {

			Location location = locationMap.get(store);

			// System.out.println("GMB store is: "+ storeCode);
			if (location != null) {
				String storeCode = location.getStoreCode();
				// System.out.println("Collecting Insights for Store: "+
				// location.getStoreCode());
				Map<String, Long> directMap = new HashMap<String, Long>();
				Map<String, Long> inDirectMap = new HashMap<String, Long>();
				Map<String, Long> searchMap = new HashMap<String, Long>();
				Map<String, Long> mapsMap = new HashMap<String, Long>();
				Map<String, Long> websiteMap = new HashMap<String, Long>();
				Map<String, Long> callsMap = new HashMap<String, Long>();
				Map<String, Long> drivingDirMap = new HashMap<String, Long>();

				List<String> locationsNames = new ArrayList<String>();

				String gmbLocation = location.getName();
				String[] locationDetails = gmbLocation.split("/");
				locationsNames.add(location.getName());

				String accountId = locationDetails[1];
				String googleLocationId = locationDetails[3];

				Long directCount = 0L;
				Long inDirectCount = 0L;

				Long mapCount = 0L;
				Long searchCount = 0L;

				Long websiteCount = 0L;
				Long callsCount = 0L;
				Long directionsCount = 0L;

				ReportLocationInsightsRequest request = new ReportLocationInsightsRequest();
				request.setLocationNames(locationsNames);
				request.setBasicRequest(new BasicMetricsRequest());
				List<MetricRequest> metrics = new ArrayList<MetricRequest>();
				MetricRequest metric1 = new MetricRequest();
				MetricRequest metric2 = new MetricRequest();

				List<String> options = new ArrayList<String>();
				options.add("AGGREGATED_DAILY");
				metric1.setMetric("ALL");
				metric2.setMetric("ALL");
				metric2.setOptions(options);
				metric1.setOptions(options);

				metrics.add(metric1);
				metrics.add(metric2);

				request.getBasicRequest().setMetricRequests(metrics);
				TimeRange time = new TimeRange();
				String startDate = dates[0];
				String endDate = dates[1];

				logger.info("Location is: " + location.getStoreCode());

				time.setStartTime(startDate);
				time.setEndTime(endDate);
				// System.out.println(startDate + "---" + endDate);
				request.getBasicRequest().setTimeRange(time);

				try {
					MyBusiness.Accounts.Locations.ReportInsights insights = mybusiness
							.accounts().locations()
							.reportInsights(account.getName(), request);
					ReportLocationInsightsResponse response = insights
							.execute();

					List<LocationMetrics> locationMetrics = response
							.getLocationMetrics();

					if (locationMetrics != null) {

						for (int l = 0; l < locationMetrics.size(); l++) {
							LocationMetrics locationMetric = locationMetrics
									.get(l);
							List<MetricValue> metricValues = locationMetric
									.getMetricValues();

							for (int k = 0; k < metricValues.size(); k++) {
								MetricValue metricValue = metricValues.get(k);

								String metric = metricValue.getMetric();
								/*
								 * System.out.println("=====>" + metricValue +
								 * "  <<<<==== ==> " + metric);
								 */

								if (metric != null
										&& metric
												.equalsIgnoreCase("QUERIES_DIRECT")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										if (value == null) {
											value = 0L;
										}
										directMap.put(
												startTime.substring(0, 10),
												value);
									}

								} else if (metric != null
										&& metric
												.equalsIgnoreCase("QUERIES_INDIRECT")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										if (value == null) {
											value = 0L;
										}
										inDirectMap.put(
												startTime.substring(0, 10),
												value);
									}
								} else if (metric != null
										&& metric
												.equalsIgnoreCase("VIEWS_SEARCH")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										if (value == null) {
											value = 0L;
										}
										searchMap.put(
												startTime.substring(0, 10),
												value);
									}
								} else if (metric != null
										&& metric
												.equalsIgnoreCase("VIEWS_MAPS")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										if (value == null) {
											value = 0L;
										}
										mapsMap.put(startTime.substring(0, 10),
												value);
									}
								} else if (metric != null
										&& metric
												.equalsIgnoreCase("ACTIONS_WEBSITE")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										if (value == null) {
											value = 0L;
										}
										websiteMap.put(
												startTime.substring(0, 10),
												value);
									}
								} else if (metric != null
										&& metric
												.equalsIgnoreCase("ACTIONS_PHONE")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										if (value == null) {
											value = 0L;
										}
										callsMap.put(
												startTime.substring(0, 10),
												value);
									}
								} else if (metric != null
										&& metric
												.equalsIgnoreCase("ACTIONS_DRIVING_DIRECTIONS")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										if (value == null) {
											value = 0L;
										}
										drivingDirMap.put(
												startTime.substring(0, 10),
												value);
									}
								}
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				Set<String> keySet = directMap.keySet();
				LocalBusinessDTO dto = service.getLocationByGoogleAccount(
						googleAccountId, googleLocationId);

				for (String date : keySet) {
					directCount = directMap.get(date);
					inDirectCount = inDirectMap.get(date);

					searchCount = searchMap.get(date);
					mapCount = mapsMap.get(date);

					websiteCount = websiteMap.get(date);
					callsCount = callsMap.get(date);
					directionsCount = drivingDirMap.get(date);

					java.util.Date formattedEndDate = getFormattedDate(date);

					storesForRemove.remove(storeCode);
					Log.info("Adding Insight details for: " + storeCode
							+ ", and Date: " + formattedEndDate);
					service.addInsightGraphDetails(dto, formattedEndDate,
							accountId, googleLocationId, directCount,
							inDirectCount, searchCount, mapCount, callsCount,
							directionsCount, websiteCount);
					
			
				}
				service.updateInsightMonthlyCountsForStore(dto, accountId,googleLocationId);

			} else {
				Log.info("store in LBL does not found in GMB:" + store);
			}

		}
		Log.info("Total stores in after removing identified: "
				+ storesForRemove.size());
		for (int i = 0; i < storesForRemove.size(); i++) {
			Log.info("Store not Found is: " + stores.get(i));

		}

	}

	public static void main(String[] args) {
		GMBClient client = new GMBClient(null);

		List<com.business.common.util.Reviews> allReviews = new ArrayList<com.business.common.util.Reviews>();

		try {
			List<Account> listAccounts = listAccounts();
			for (Account account : listAccounts) {
				String accountName = account.getAccountName();
				System.out.println("Account is:" + accountName);

				String name = account.getName();
				String[] accountIds = name.split("/");
				String googleAccountId = accountIds[1];

				String clientId = "";

				if (googleAccountId.equalsIgnoreCase("101497916131701210690")) {

					System.out.println("fetch locations");
					// List<Location> listLocations = listLocations(account);

					// System.out.println(listLocations.size());
					String nameOfAcc = account.getName();
					// createLocation(nameOfAcc);
					List<Location> listLocationsUpdate = listLocations(account);
					System.out.println(listLocationsUpdate.size());
					int i = 0;
					List<String> stateList = new ArrayList<String>();

					
			/*		stateList.add("AB");
					stateList.add("AK");
					stateList.add("AL");
					stateList.add("AR");
					stateList.add("AZ");
					stateList.add("BC");
					stateList.add("CA");
					stateList.add("CO");
					stateList.add("CT");
					stateList.add("DC");
					stateList.add("DE");
					stateList.add("FL");
					
					stateList.add("GA");
					stateList.add("HI");
					stateList.add("IA");
					stateList.add("ID");
					stateList.add("IL");
					stateList.add("IN");
					stateList.add("KS");
					stateList.add("KY");
					stateList.add("LA");
					stateList.add("MA");
					stateList.add("MB");
					stateList.add("MD");
					
					
					stateList.add("ME");
					stateList.add("MI");
					stateList.add("MN");
					stateList.add("MO");
					stateList.add("MS");
					stateList.add("MT");*/
				/*	
					stateList.add("NB");
					stateList.add("NC");
					stateList.add("ND");
					stateList.add("NE");
					stateList.add("NH");
					stateList.add("NJ");
	
					stateList.add("NL");
					stateList.add("NM");
					stateList.add("NS");
					stateList.add("NV");
					stateList.add("NY");
					
					stateList.add("OH");
					stateList.add("OK");
					stateList.add("ON");
					stateList.add("OR");
					stateList.add("PA");
					stateList.add("PE");
	
					stateList.add("RI");
					stateList.add("SC");
					stateList.add("SD");
					stateList.add("SK");
					stateList.add("TN");
					
					stateList.add("TX");
					stateList.add("UT");
					stateList.add("UA");
					stateList.add("VT");
					stateList.add("WA");
					stateList.add("WI");*/
					
					stateList.add("WV");
					stateList.add("WY");


					for (Location location : listLocationsUpdate) {
						String administrativeArea = location.getAddress()
								.getAdministrativeArea();
						// System.out.println(administrativeArea);
						if (stateList.contains(administrativeArea)) {

							String gmbLocation = location.getName();
							System.out.println(gmbLocation);
							// createLocation(accountName);
							List listReviews = listReviews(location.getName());
							if(listReviews!=null) {

								com.business.common.util.Reviews reviews = new com.business.common.util.Reviews();
								reviews.setReviews(listReviews);
								reviews.setState(administrativeArea);
								reviews.setStoreCode(location.getStoreCode());
								allReviews.add(reviews);
								System.out.println(listReviews.size());
							}
						}

					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String excelFilePath = "C:\\lbl\\LibertyTax_Reviews_1.xls";

		try {
			System.out.println("writing to file");
			writeExcel(allReviews, excelFilePath);
			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void writeExcel(
			List<com.business.common.util.Reviews> allReviews,
			String excelFilePath) throws IOException {
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		int rowCount = 0;
		for (int i = 0; i < allReviews.size(); i++) {
			com.business.common.util.Reviews review = allReviews.get(i);
			List<Review> reviews2 = review.getReviews();
			if(reviews2!=null) {
			String state = review.getState();
			String storeCode = review.getStoreCode();
			for (Review reviewData : reviews2) {
				org.apache.poi.ss.usermodel.Row row = sheet
						.createRow(++rowCount);
				writeReview(reviewData, storeCode, state, row);
			}
			}
		}

		try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
			workbook.write(outputStream);
		}
	}

	private static void writeReview(Review review, String store, String state,
			org.apache.poi.ss.usermodel.Row row) {
		org.apache.poi.ss.usermodel.Cell cell = row.createCell(1);
		cell.setCellValue(store);

		cell = row.createCell(2);
		cell.setCellValue(state);

		cell = row.createCell(3);
		cell.setCellValue(review.getReviewId());

		cell = row.createCell(4);
		cell.setCellValue(review.getReviewer().getDisplayName());

		cell = row.createCell(5);
		cell.setCellValue(review.getStarRating());

		cell = row.createCell(6);
		cell.setCellValue(review.getComment());

		cell = row.createCell(7);
		cell.setCellValue(review.getCreateTime());

		cell = row.createCell(8);
		cell.setCellValue(review.getUpdateTime());

		cell = row.createCell(9);
		if (review.getReviewReply() != null) {
			cell.setCellValue(review.getReviewReply().getComment());
		} else {
			cell.setCellValue("");
		}

	}

	public static String[] getStartandEndDates(int daysToLess) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

		String[] dates = new String[2];

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(calendar2.getTime());
		calendar2.add(Calendar.DATE, -4);
		calendar2.set(Calendar.MILLISECOND, 0);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.MINUTE, 01);
		calendar2.set(Calendar.HOUR, 00);
		String endDate = formatter.format(calendar2.getTime());

		dates[1] = endDate;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(calendar2.getTime());
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.HOUR, 00);
		calendar.add(Calendar.DATE, -daysToLess);

		String startDate = formatter.format(calendar.getTime());
		dates[0] = startDate;

		System.out.println(dates[0] + "====" + dates[1]);

		return dates;
	}

	public static List<String[]> getListofDates(int daysToLess) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(calendar1.getTime());
		calendar1.add(Calendar.DATE, -3);
		java.util.Date end = calendar1.getTime();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(end);
		calendar2.add(Calendar.DATE, -daysToLess);
		java.util.Date start = calendar2.getTime();

		List<String[]> listofWeeks = new ArrayList<String[]>();

		long difference = (start.getTime() - end.getTime()) / 86400000;
		long days = Math.abs(difference);

		Calendar today = Calendar.getInstance();

		long diffinEndAndToday = (end.getTime() - today.getTimeInMillis()) / 86400000;
		long endDateTostart = Math.abs(diffinEndAndToday);

		int previousStart = -(int) endDateTostart;
		int previousEnd = -1;

		for (int i = 0; i <= days; i++) {

			String[] dates = new String[2];

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(calendar.getTime());
			calendar.add(Calendar.DATE, previousStart);

			String startDate = formatter.format(calendar.getTime());
			dates[0] = startDate;

			calendar.setTime(calendar.getTime());
			calendar.add(Calendar.DATE, previousEnd);
			String endDate = formatter.format(calendar.getTime());

			dates[1] = endDate;

			previousStart = previousStart - 1;
			listofWeeks.add(dates);
		}

		return listofWeeks;
	}

	public static List<String[]> getListofWeeks() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

		List<String[]> listofWeeks = new ArrayList<String[]>();

		String[] dates = new String[2];

		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.YEAR, 2016);
		calendar.set(calendar.MONTH, 11);
		calendar.set(calendar.DAY_OF_MONTH, 10);
		calendar.set(calendar.MINUTE, 00);

		String startDate = formatter.format(calendar.getTime());
		dates[0] = startDate;

		calendar.setTime(calendar.getTime());
		calendar.add(Calendar.DATE, 90);
		String endDate = formatter.format(calendar.getTime());

		dates[1] = endDate;

		listofWeeks.add(dates);

		return listofWeeks;
	}

	public static java.util.Date getFormattedDate(String dateValue) {
		java.util.Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateValue);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR, 0);
			date = cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String[] getStartandEndDates(String type) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

		String pattern = "MM/dd/yyyy";
		java.util.Date start = null;
		java.util.Date end = null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		if (type.equalsIgnoreCase("range")) {
			try {
				start = format.parse(startDate);
				if (endDate == null || endDate.length() == 0) {
					end = format.parse(startDate);
				} else {
					end = format.parse(endDate);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {

			try {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -4);
				SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
				String endDate = format1.format(cal.getTime());
				end = format.parse(endDate);

				Calendar calStart = Calendar.getInstance();
				calStart.setTime(end);
				calStart.add(Calendar.DATE, -2);
				String startDate = format1.format(cal.getTime());
				start = format.parse(startDate);

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		String[] dates = new String[2];

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 00);
		String startDate = formatter.format(calendar.getTime());

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(end);
		calendar2.set(Calendar.MILLISECOND, 0);
		calendar2.set(Calendar.MINUTE, 01);
		calendar2.set(Calendar.HOUR, 24);

		if (endDate == null || endDate.length() == 0) {
			calendar2.set(Calendar.MINUTE, 00);
			calendar2.set(Calendar.HOUR, 1);
			calendar2.add(Calendar.HOUR, 24);
		}

		String endDate = formatter.format(calendar2.getTime());

		dates[0] = startDate;

		dates[1] = endDate;

		System.out.println(dates[0] + "====" + dates[1]);

		return dates;
	}

	public static List<java.util.Date> getListOfDates(java.util.Date startDate,
			java.util.Date endDate) {
		List<java.util.Date> datesInRange = new ArrayList<java.util.Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);

		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		while (calendar.before(endCalendar)) {
			java.util.Date result = calendar.getTime();
			datesInRange.add(result);
			calendar.add(Calendar.DATE, 1);
		}

		datesInRange.add(endDate);

		return datesInRange;
	}

}
