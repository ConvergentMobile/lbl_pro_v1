package com.business.common.util;

import java.io.File;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
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
import com.google.api.services.mybusiness.v3.MyBusiness;
import com.google.api.services.mybusiness.v3.model.Account;
import com.google.api.services.mybusiness.v3.model.Address;
import com.google.api.services.mybusiness.v3.model.Admin;
import com.google.api.services.mybusiness.v3.model.BasicMetricsRequest;
import com.google.api.services.mybusiness.v3.model.BusinessHours;
import com.google.api.services.mybusiness.v3.model.Category;
import com.google.api.services.mybusiness.v3.model.Date;
import com.google.api.services.mybusiness.v3.model.DimensionalMetricValue;
import com.google.api.services.mybusiness.v3.model.GoogleUpdatedLocation;
import com.google.api.services.mybusiness.v3.model.ListAccountsResponse;
import com.google.api.services.mybusiness.v3.model.ListLocationAdminsResponse;
import com.google.api.services.mybusiness.v3.model.ListLocationsResponse;
import com.google.api.services.mybusiness.v3.model.Location;
import com.google.api.services.mybusiness.v3.model.LocationMetrics;
import com.google.api.services.mybusiness.v3.model.MetricRequest;
import com.google.api.services.mybusiness.v3.model.MetricValue;
import com.google.api.services.mybusiness.v3.model.OpenInfo;
import com.google.api.services.mybusiness.v3.model.Photos;
import com.google.api.services.mybusiness.v3.model.ReportLocationInsightsRequest;
import com.google.api.services.mybusiness.v3.model.ReportLocationInsightsResponse;
import com.google.api.services.mybusiness.v3.model.SpecialHourPeriod;
import com.google.api.services.mybusiness.v3.model.SpecialHours;
import com.google.api.services.mybusiness.v3.model.TimePeriod;
import com.google.api.services.mybusiness.v3.model.TimeRange;

/**
 * Simple client for the Google My Business API.
 */
public class GMBClient {

	static Logger logger = Logger.getLogger(GMBClient.class);
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

		/*
		 * var locationsListRequest =
		 * service.Accounts.Locations.List(account.Name);
		 * locationsListRequest.PageSize = 100; locationsResult =
		 * locationsListRequest.Execute();
		 * 
		 * if (locationsResult != null) {
		 * PrintLocations(locationsResult.Locations); while
		 * (locationsResult.NextPageToken != null) {
		 * locationsListRequest.PageToken = locationsResult.NextPageToken;
		 * locationsResult = locationsListRequest.Execute();
		 * PrintLocations(locationsResult.Locations); } } else {
		 * Console.WriteLine("Account {0} has no locations.", account.Name); }
		 */

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

		/*
		 * List<Location> locations = response.getLocations();
		 * 
		 * if (locations != null) { for (Location location : locations) {
		 * //System.out.println(location.getLocationName() + "====" +
		 * location.getName() + "Location is:"+ location); } } else {
		 * System.out.printf("Account '%s' has no locations.",
		 * account.getName()); }
		 */
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
		updateLocation.setFieldMask("location_name");

		updateLocation.setLanguageCode("en-AU");
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
		updateLocation.setFieldMask("special_hours");
		updateLocation.setLanguageCode("en-AU");
		Location updatedLocation = updateLocation.execute();
		System.out.println("Holiday hours set:" + updatedLocation);
	}

	public static void setPhotos(String locationName) throws Exception {
		Location location = new Location()
				.setPhotos(new Photos()
						.setTeamPhotoUrls(
								Collections
										.singletonList("https://lh3.googleusercontent.com/-ccTboHHmaWc/Udtina8CDnI/AAAAAAAABFU/hwYAS9RuY7U/s696/R51C4274.jpg"))
						.setExteriorPhotoUrls(
								Collections
										.singletonList("https://lh3.googleusercontent.com/-VdwKC7Sf2r4/UdtiS0PikDI/AAAAAAAABFU/fA3ehF7XsB0/s696/R51C4675.jpg")));
		MyBusiness.Accounts.Locations.Patch updateLocation = mybusiness
				.accounts().locations().patch(locationName, location);
		updateLocation.setLanguageCode("en-AU");
		updateLocation
				.setFieldMask("photos.exterior_photo_urls,photos.team_photo_urls");
		Location updatedLocation = updateLocation.execute();
		System.out.println("Photos set:" + updatedLocation);
	}

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
		List<String> addressLines = new ArrayList();
		addressLines.add("Level 5, 48 Pirrama Road");
		Address address = new Address().setAddressLines(addressLines)
				.setLocality("Pyrmont").setAdministrativeArea("NSW")
				.setCountry("AU").setPostalCode("2009");

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
		Location location = new Location().setAddress(address)
				.setRegularHours(businessHours)
				.setLocationName("Google Sydney").setStoreCode("GOOG-SYD")
				.setPrimaryPhone("02 9374 4000")
				.setPrimaryCategory(new Category().setName("Software Company"))
				.setWebsiteUrl("https://www.google.com.au/");

		MyBusiness.Accounts.Locations.Create createLocation = mybusiness
				.accounts().locations().create(accountName, location);
		createLocation.setRequestId("1a84939c-ab7d-4581-8930-ee35af6fefac");
		createLocation.setLanguageCode("en-AU");
		// If you get a request validation error you can inject the following
		// header into any call as
		// shown below to get a more descriptive error and then it will throw
		// trying to digest that
		// richer message.
		// This is a bug in the client library generator that will be fixed in
		// the future.
		// createLocation.getRequestHeaders().set("X-GOOG-API-FORMAT-VERSION",
		// 2);
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
		Address address = new Address().setAddressLines(addressLines)
				.setLocality(localBusinessDTO.getLocationCity())
				.setAdministrativeArea(localBusinessDTO.getLocationState())
				.setCountry(localBusinessDTO.getCountryCode())
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
						new Category().setName("Credit").setCategoryId(
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
					.setLanguageCode("en")
					.setFieldMask(
							"storeCode,locationName,address,regularHours,openInfo")
					.setValidateOnly(false).getRequestHeaders()
					.set("X-GOOG-API-FORMAT-VERSION", 2);
		}
		// Spring and Liberrty tax
		else if (localBusinessDTO.getClientId() == 3225
				|| localBusinessDTO.getClientId() == 3456
				|| localBusinessDTO.getClientId() == 3628) {
			updateLocation
					.setLanguageCode("en")
					.setFieldMask(
							"storeCode,locationName,address,primaryPhone,regularHours,openInfo")
					.setValidateOnly(false).getRequestHeaders()
					.set("X-GOOG-API-FORMAT-VERSION", 2);
		} else {
			// all other brands
			updateLocation
					.setLanguageCode("en")
					.setFieldMask(
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
		Address address = new Address().setAddressLines(addressLines)
				.setLocality("Chapel Hill").setAdministrativeArea("NC")
				.setCountry("US").setPostalCode("27516");

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
						new Category().setName("Credit").setCategoryId(
								"gcid:credit_union")).setOpenInfo(openInfo);

		MyBusiness.Accounts.Locations.Patch updateLocation = mybusiness
				.accounts()
				.locations()
				.patch("accounts/114515985899098925999/locations/16805323811872814884",
						location);
		updateLocation
				.setLanguageCode("en")
				.setFieldMask(
						"storeCode,locationName,address,primaryPhone,websiteUrl,regularHours,openInfo")
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
		String[] dates = getStartandEndDates(2);

		java.util.Date formattedDate = getFormattedDate(dates[1]);
		System.out.println("clientId: " + clientId + ", formatted Date is:"
				+ formattedDate);

		List<java.util.Date> datesList = new ArrayList<java.util.Date>();
		datesList.add(formattedDate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(formattedDate);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		java.util.Date date2 = cal.getTime();
		datesList.add(date2);
	

		// delete records for exising dates
		for (int i = 0; i < datesList.size(); i++) {
			service.deleteExistingRecords(clientId, datesList.get(i));
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
				//System.out.println(startDate + "---" + endDate);
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
								/*System.out.println("=====>" + metricValue
										+ "  <<<<==== ==> " + metric);*/

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

	public static void getReportInsights(Account account) {

		List<Location> locations = new ArrayList<Location>();
		try {
			locations = listLocations(account);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String name = account.getName();
		String[] accountIds = name.split("/");
		String googleAccountId = accountIds[1];

		List<String> stores = new ArrayList<String>();
		// for (LocalBusinessDTO localBusinessDTO : listOfBussinessByBrandName)
		// {
		stores.add("SYR");
		// }

		List<String[]> listofDays = getListofWeeks();
		int a = 0;
		for (Location location : locations) {

			Map<String, Long> drivingDirMap = new HashMap<String, Long>();

			if (a > 1)
				break;
			if (stores.contains(location.getStoreCode())) {
				a++;

				Map<String, Long> directMap = new HashMap<String, Long>();
				Map<String, Long> inDirectMap = new HashMap<String, Long>();
				Map<String, Long> searchMap = new HashMap<String, Long>();
				Map<String, Long> mapsMap = new HashMap<String, Long>();
				Map<String, Long> websiteMap = new HashMap<String, Long>();
				Map<String, Long> callsMap = new HashMap<String, Long>();

				String[] dates = listofDays.get(0);

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

				time.setStartTime(startDate);
				time.setEndTime(endDate);

				System.out.println(startDate + "---" + endDate);

				request.getBasicRequest().setTimeRange(time);

				try {
					MyBusiness.Accounts.Locations.ReportInsights insights = mybusiness
							.accounts().locations()
							.reportInsights(account.getName(), request);
					ReportLocationInsightsResponse response = insights
							.execute();

					List<LocationMetrics> locationMetrics = response
							.getLocationMetrics();

					System.out.println("Location Metric size: "
							+ locationMetrics.size());

					if (locationMetrics != null) {

						for (int l = 0; l < locationMetrics.size(); l++) {
							LocationMetrics locationMetric = locationMetrics
									.get(l);
							List<MetricValue> metricValues = locationMetric
									.getMetricValues();

							System.out.println("metricValues  size: "
									+ metricValues.size());

							for (int k = 0; k < metricValues.size(); k++) {
								MetricValue metricValue = metricValues.get(k);

								if (metricValue.getMetric().equalsIgnoreCase(
										"QUERIES_DIRECT")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										System.out.println(startTime + "==== "
												+ value);
										directMap.put(
												startTime.substring(0, 10),
												value);
									}

								} else if (metricValue.getMetric()
										.equalsIgnoreCase("QUERIES_INDIRECT")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										System.out.println(startTime + "==== "
												+ value);
										inDirectMap.put(
												startTime.substring(0, 10),
												value);
									}
								} else if (metricValue.getMetric()
										.equalsIgnoreCase("VIEWS_SEARCH")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										System.out.println(startTime + "==== "
												+ value);
										searchMap.put(
												startTime.substring(0, 10),
												value);
									}
								} else if (metricValue.getMetric()
										.equalsIgnoreCase("VIEWS_MAPS")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										System.out.println(startTime + "==== "
												+ value);
										mapsMap.put(startTime.substring(0, 10),
												value);
									}
								} else if (metricValue.getMetric()
										.equalsIgnoreCase("ACTIONS_WEBSITE")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										System.out.println(startTime.substring(
												0, 10) + "==== " + value);
										websiteMap.put(
												startTime.substring(0, 10),
												value);
									}
								} else if (metricValue.getMetric()
										.equalsIgnoreCase("ACTIONS_PHONE")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										System.out.println(startTime.substring(
												0, 10) + "==== " + value);
										callsMap.put(
												startTime.substring(0, 10),
												value);
									}
								} else if (metricValue.getMetric()
										.equalsIgnoreCase(
												"ACTIONS_DRIVING_DIRECTIONS")) {
									List<DimensionalMetricValue> dimensionalValues = metricValue
											.getDimensionalValues();
									for (DimensionalMetricValue dimensionalMetricValue : dimensionalValues) {
										String startTime = dimensionalMetricValue
												.getTimeDimension()
												.getTimeRange().getStartTime();
										Long value = dimensionalMetricValue
												.getValue();
										System.out.println(startTime + "==== "
												+ value);
										drivingDirMap.put(
												startTime.substring(1, 10),
												value);
									}
								}
							}

							System.out.println(drivingDirMap.size());
							System.out.println(callsMap.size());

						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				Set<String> keySet = drivingDirMap.keySet();

				for (String date : keySet) {
					directCount = directMap.get(date);
					inDirectCount = inDirectMap.get(date);

					searchCount = searchMap.get(date);
					mapCount = mapsMap.get(date);

					websiteCount = websiteMap.get(date);
					callsCount = callsMap.get(date);
					directionsCount = drivingDirMap.get(date);

					java.util.Date formattedEndDate = getFormattedDate(date);
					/*
					 * service.addInsightGraphDetails(dto, formattedEndDate,
					 * accountId, googleLocationId, directCount, inDirectCount,
					 * searchCount, mapCount, callsCount, directionsCount,
					 * websiteCount);
					 */
				}

			}

		}

	}

	public static void main(String[] args) {
		GMBClient client = new GMBClient(null);

		/*
		 * String location =
		 * "accounts/114515985899098925999/locations/7702955619175411989";
		 * String[] locationDetails = location.split("/");
		 * System.out.println(locationDetails[1] + "- " + locationDetails[3]);
		 */

		try {
			// List<String[]> listofWeeks = getListofDates(90);
			/*
			 * for (String[] strings : listofWeeks) {
			 * System.out.println(strings[0] + "==" + strings[1]); }
			 */
			String[] startandEndDates = getStartandEndDates(538);

			System.out.println(startandEndDates[1] + "startandEndDates"
					+ startandEndDates[0]);

			/*
			 * java.util.Date formattedDate =
			 * getFormattedDate(startandEndDates[1]);
			 * System.out.println(formattedDate);
			 */

			/*
			 * java.util.Date formattedDate = getFormattedDate("2016-12-12");
			 * 
			 * List<String[]> listofDates = getListofWeeks(); for (int i = 0; i
			 * < listofDates.size(); i++) { String[] dates = listofDates.get(i);
			 * System.out.println(dates[1] + "==============" + dates[0]);
			 * getStartandEndDates }
			 * 
			 * List<Account> listAccounts = listAccounts(); Account
			 * findBusinessAccount = findBusinessAccount(listAccounts);
			 * System.out.println("===================insights" +
			 * findBusinessAccount.getAccountName());
			 * getReportInsights(findBusinessAccount);
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String[] getStartandEndDates(int daysToLess) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

		String[] dates = new String[2];

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(calendar2.getTime());
		calendar2.add(Calendar.DATE, -4);
		String endDate = formatter.format(calendar2.getTime());

		dates[1] = endDate;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(calendar2.getTime());
		calendar.add(Calendar.DATE, -daysToLess);

		String startDate = formatter.format(calendar.getTime());
		dates[0] = startDate;

		// System.out.println(dates[0] + "===="+ dates[1]);

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
			cal.add(Calendar.HOUR, 5);
			date = cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
