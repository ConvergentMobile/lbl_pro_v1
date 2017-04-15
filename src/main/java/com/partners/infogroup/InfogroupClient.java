package com.partners.infogroup;

/**
 * @author Vasanth
 * 
 * InfoGroup API client
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ThisExpression;

import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.RenewalReportDTO;
import com.business.common.util.LBLConstants;
import com.business.common.util.SubmissionUtil;
import com.business.common.util.UnAccentUtil;
import com.business.service.BusinessService;
import com.business.web.bean.InfogroupAPIBean;
import com.business.web.bean.InfogroupAPI_AuthenticationBean;
import com.google.gson.Gson;

public class InfogroupClient {
	BusinessService service;
	static InfogroupClient infogroupClient;
	static Logger logger = Logger.getLogger(InfogroupClient.class);

	static String access_token;

	public static boolean getAccessToken() {
		boolean validate = true;
		try {

			String targetURL = LBLConstants.INFOGROUP_ACCESSTOKENURL;
			String userName = LBLConstants.INFOGROUP_USERNAME;
			String passWord = LBLConstants.INFOGROUP_PASSWORD;

			byte[] bytes = (userName + ":" + passWord).getBytes();
			String encoding = DatatypeConverter.printBase64Binary(bytes);
			CloseableHttpClient client = HttpClientBuilder.create().build();

			HttpGet request = new HttpGet(targetURL);
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Accept", "application/json");
			request.setHeader("Authorization", "Basic " + encoding);

			HttpResponse response = client.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			if ((line = rd.readLine()) != null) {

				Gson gson = new Gson();
				InfogroupAPI_AuthenticationBean beans = gson.fromJson(line,
						InfogroupAPI_AuthenticationBean.class);
				access_token = beans.getAccess_token();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return validate;
	}

	public static Map<String, List<String>> postToInfogroup(
			List<LocalBusinessDTO> localBusinessDTOs, BusinessService service)
			throws Exception {

		Map<String, List<String>> errorDetails = new HashMap<String, List<String>>();

		List<String> erroredStores = new ArrayList<String>();

		int totalCount = localBusinessDTOs.size();

		getAccessToken();

		for (int i = 0; i < totalCount; i++) {

			String postData = "{\"submissions\": [" + "temp" + "]}";
			String data = "";
			LocalBusinessDTO localBusinessDTO = localBusinessDTOs.get(i);
			SubmissionUtil submissionUtil = new SubmissionUtil();
			RenewalReportDTO renewalReportDTO = service
					.isRenewed(localBusinessDTO.getStore(),
							localBusinessDTO.getClientId());

			if (renewalReportDTO != null) {
				Date cancelledEffeciveDate = renewalReportDTO
						.getCancelledEffeciveDate();
				boolean isDate = false;
				if (cancelledEffeciveDate != null) {
					Date currentDateDate = new Date();
					isDate = cancelledEffeciveDate.compareTo(currentDateDate) < 0;
				}

				if ((renewalReportDTO.getStatus().equals("Renewed")
						|| renewalReportDTO.getStatus().equals("Active") || isDate)) {
					InfogroupAPIBean infogroupAPIBean = new InfogroupAPIBean();
					infogroupAPIBean.setCompanyName(localBusinessDTO
							.getCompanyName());
					infogroupAPIBean.setLocationAddress(localBusinessDTO
							.getLocationAddress());
					infogroupAPIBean.setLocationCity(localBusinessDTO
							.getLocationCity());
					infogroupAPIBean.setLocationPhone(localBusinessDTO
							.getLocationPhone());
					infogroupAPIBean.setLocationState(localBusinessDTO
							.getLocationState());
					infogroupAPIBean.setLocationZipCode(localBusinessDTO
							.getLocationZipCode());
					infogroupAPIBean
							.setSubmissionType(LBLConstants.LBL_INFOGROUP_SUBMISSION_TYPE);
					infogroupAPIBean.setAdditionalNumber(localBusinessDTO
							.getAdditionalNumber());
					infogroupAPIBean.setAlternateSocialLink(localBusinessDTO
							.getAlternateSocialLink());
					infogroupAPIBean.setAlternativeName(localBusinessDTO
							.getAlternativeName());
					infogroupAPIBean.setAMEX(localBusinessDTO.getaMEX());
					infogroupAPIBean.setAnchorHostBusiness(localBusinessDTO
							.getAnchorOrHostBusiness());
					infogroupAPIBean.setBrands(localBusinessDTO.getBrands());
					infogroupAPIBean.setBusinessDescription(localBusinessDTO
							.getBusinessDescription());
					infogroupAPIBean.setCash(localBusinessDTO.getCash());
					infogroupAPIBean.setCheck(localBusinessDTO.getCheck());
					infogroupAPIBean.setCouponLink(localBusinessDTO
							.getCouponLink());
					infogroupAPIBean.setDebitCard(localBusinessDTO
							.getDebitCard());
					infogroupAPIBean.setDinersClub(localBusinessDTO
							.getDinersClub());
					infogroupAPIBean
							.setDiscover(localBusinessDTO.getDiscover());
					infogroupAPIBean.setFacebookLink(localBusinessDTO
							.getFacebookLink());
					infogroupAPIBean.setFax(localBusinessDTO.getFax());
					infogroupAPIBean.setFinancing(localBusinessDTO
							.getFinancing());
					infogroupAPIBean.setGoogleCheckout(localBusinessDTO
							.getGoogleCheckout());
					infogroupAPIBean.setGooglePlusLink(localBusinessDTO
							.getGooglePlusLink());
					infogroupAPIBean.setInvoice(localBusinessDTO.getInvoice());
					infogroupAPIBean
							.setKeywords(localBusinessDTO.getKeywords());
					infogroupAPIBean.setLanguages(localBusinessDTO
							.getLanguages());
					infogroupAPIBean.setLinkedInLink(localBusinessDTO
							.getLinkedInLink());
					infogroupAPIBean.setLocationEmployeeSize(localBusinessDTO
							.getLocationEmployeeSize());
					infogroupAPIBean
							.setLogoLink(localBusinessDTO.getLogoLink());
					infogroupAPIBean.setMasterCard(localBusinessDTO
							.getMasterCard());
					infogroupAPIBean.setMobileNumber(localBusinessDTO
							.getMobileNumber());
					infogroupAPIBean.setMyspaceLink(localBusinessDTO
							.getMyspaceLink());
					infogroupAPIBean.setOtherCard(localBusinessDTO
							.getOtherCard());
					infogroupAPIBean.setPayPal(localBusinessDTO.getPayPal());
					infogroupAPIBean.setPinterestLink(localBusinessDTO
							.getPinteristLink());
					infogroupAPIBean
							.setPrimaryContactActualTitle(localBusinessDTO
									.getContactTitle());
					infogroupAPIBean.setPrimaryContactEmail(localBusinessDTO
							.getContactEmail());
					infogroupAPIBean
							.setPrimaryContactFirstName(localBusinessDTO
									.getPrimaryContactFirstName());
					infogroupAPIBean.setPrimaryContactLastName(localBusinessDTO
							.getPrimaryContactLastName());
					infogroupAPIBean
							.setProducts(localBusinessDTO.getProducts());
					infogroupAPIBean
							.setProfessionalAssociations(localBusinessDTO
									.getProfessionalAssociations());
					infogroupAPIBean
							.setServices(localBusinessDTO.getServices());
					infogroupAPIBean.setShorWebAddress(localBusinessDTO
							.getShortWebAddress());
					infogroupAPIBean.setStoreCard(localBusinessDTO
							.getStoreCard());
					infogroupAPIBean.setSuite(localBusinessDTO.getSuite());
					infogroupAPIBean
							.setTollFree(localBusinessDTO.getTollFree());
					infogroupAPIBean.setTravelersCheck(localBusinessDTO
							.getTravelersCheck());
					infogroupAPIBean.setTTY(localBusinessDTO.getTty());
					infogroupAPIBean.setTwitterLink(localBusinessDTO
							.getTwitterLink());
					infogroupAPIBean.setVisa(localBusinessDTO.getVisa());
					infogroupAPIBean.setWebAddress(localBusinessDTO
							.getWebAddress());
					infogroupAPIBean.setYouTubeorVideoLink(localBusinessDTO
							.getYouTubeOrVideoLink());

					data = data + infogroupPostDataFormat(infogroupAPIBean);
					data = data + ",";
					postData = postData.replace("temp",
							data.substring(0, data.length() - 1));

					postData = postData.replaceAll("null", "");
					postData =  postData.replaceAll("(\\r|\\n|\\r\\n)+", "");
					logger.info("Posting data to infogroup: " + postData);

					CloseableHttpClient client = HttpClientBuilder.create()
							.build();

					HttpResponse response = null;
					String submissionsURL = LBLConstants.INFOGROUP_SUBMISSIONSURL;
					try {
						HttpPost post = new HttpPost(submissionsURL);
						post.setHeader("X-AUTH-TOKEN",
								"b25f6d1a05c667ed5d42fc08");
						StringEntity entity = new StringEntity(postData);
						entity.setContentType("application/json");
						post.setEntity(entity);
						response = client.execute(post);
						logger.info("Status Code == "
								+ response.getStatusLine().getStatusCode());

						if (response.getStatusLine().getStatusCode() != 200) {
							erroredStores.add(localBusinessDTO.getStore());
							throw new RuntimeException(
									"Failed : HTTP error code : "
											+ response.getStatusLine()
													.getStatusCode());
						}
						String reasonPhrase = response.getStatusLine()
								.getReasonPhrase();
						logger.info("Reason Message: " + reasonPhrase);

						BufferedReader br = new BufferedReader(
								new InputStreamReader(
										(response.getEntity().getContent())));
						String output;
						logger.info("Response from Infogroup .... \n");
						while ((output = br.readLine()) != null) {
							logger.info(output);
						}
					} catch (Exception e) {
						logger.error("There was a problem while submitting the data to Infogroup "
								+ e);
						e.printStackTrace();
					}
				}
			}
		}
		errorDetails.put("errorDetails", erroredStores);
		return errorDetails;
	}

	public static String infogroupPostDataFormat(InfogroupAPIBean bean) {
		String type = bean.getSubmissionType();
		String companyName = bean.getCompanyName();
		/*
		 * if(companyName!=null && companyName.contains("&")){
		 * companyName=StringEscapeUtils.escapeJava(companyName); }
		 */
		String address = bean.getLocationAddress();
		String city = bean.getLocationCity();
		String state = bean.getLocationState();
		String zip = bean.getLocationZipCode();
		String phone = bean.getLocationPhone();
		String additionalNumber = bean.getAdditionalNumber();
		String alternateSocialLink = bean.getAlternateSocialLink();
		String alternativeName = bean.getAlternativeName();
		String amex = bean.getAMEX();
		String anchorHostBusiness = bean.getAnchorHostBusiness();
		String brands = bean.getBrands();
		String businessDescription = bean.getBusinessDescription();
		if (businessDescription != null) {
			businessDescription = StringEscapeUtils
					.escapeXml(businessDescription);
			// businessDescription=UnAccentUtil.formatString(businessDescription);
		}
		String cash = bean.getCash();
		String check = bean.getCheck();
		String couponLink = bean.getCouponLink();
		String debitCard = bean.getDebitCard();
		String dinersClub = bean.getDinersClub();
		String discover = bean.getDiscover();
		String facebookLink = bean.getFacebookLink();
		String fax = bean.getFax();
		String financing = bean.getFinancing();
		String googleCheckout = bean.getGoogleCheckout();
		String googlePlusLink = bean.getGooglePlusLink();
		String invoice = bean.getInvoice();
		String keywords = bean.getKeywords();
		String languages = bean.getLanguages();
		String linkedInLink = bean.getLinkedInLink();
		String locationEmployeeSize = bean.getLocationEmployeeSize();
		String logoLink = bean.getLogoLink();
		String masterCard = bean.getMasterCard();
		String mobileNumber = bean.getMobileNumber();
		String myspaceLink = bean.getMyspaceLink();
		String otherCard = bean.getOtherCard();
		String payPal = bean.getPayPal();
		String pinterestLink = bean.getPinterestLink();
		String primaryContactActualTitle = bean.getPrimaryContactActualTitle();
		String primaryContactEmail = bean.getPrimaryContactEmail();
		String primaryContactFirstName = bean.getPrimaryContactFirstName();
		String primaryContactLastName = bean.getPrimaryContactLastName();
		String professionalAssociations = bean.getProfessionalAssociations();
		String products = bean.getProducts();
		if (products != null) {
			products = UnAccentUtil.formatString(products);
		}
		String services = bean.getServices();
		String shorWebAddress = bean.getShorWebAddress();
		String storeCard = bean.getStoreCard();
		String suite = bean.getSuite();
		String tollFree = bean.getTollFree();
		String travelersCheck = bean.getTravelersCheck();
		String tty = bean.getTTY();
		String twitterLink = bean.getTwitterLink();
		String visa = bean.getVisa();
		String webAddress = bean.getWebAddress();
		String youTubeorVideoLink = bean.getYouTubeorVideoLink();

		String data = "{\"Submission Type\": \""
				+ type
				+ "\",\"Company Name\": \""
				+ companyName
				+ "\",\"Location Address\": \""
				+ address
				+ "\",\"Location City\": \""
				+ city
				+ "\",\"Location State\": \""
				+ state
				+ "\",\"Location Zip Code\": \""
				+ zip
				+ "\",\"Location Phone\": \""
				+ phone
				+ "\",\"Additional Number\": \""
				+ additionalNumber
				// + "\",\"Alternate SocialLink\": \"" + alternateSocialLink
				+ "\",\"Alternative Name\": \"" + alternativeName
				+ "\",\"AMEX\": \"" + amex + "\",\"Anchor/Host Business\": \""
				+ anchorHostBusiness + "\",\"Brands\": \"" + brands
				+ "\",\"Business Description\": \"" + businessDescription
				+ "\",\"Cash\": \"" + cash + "\",\"Check\": \"" + check
				+ "\",\"Coupon Link\": \"" + couponLink
				+ "\",\"Debit Card\": \"" + debitCard
				+ "\",\"Diners Club\": \"" + dinersClub + "\",\"Discover\": \""
				+ discover + "\",\"Facebook Link\": \"" + facebookLink
				+ "\",\"Fax\": \"" + fax + "\",\"Financing\": \"" + financing
				+ "\",\"Google Checkout\": \"" + googleCheckout
				+ "\",\"Google Plus Link\": \"" + googlePlusLink
				+ "\",\"Invoice\": \""
				+ invoice
				+ "\",\"Keywords\": \""
				+ keywords
				+ "\",\"Languages\": \""
				+ languages
				+ "\",\"LinkedIn Link\": \""
				+ linkedInLink
				+ "\",\"Location Employee Size\": \""
				+ locationEmployeeSize
				+ "\",\"Logo Link\": \""
				+ logoLink
				+ "\",\"Master Card\": \""
				+ masterCard
				+ "\",\"Mobile Number\": \""
				+ mobileNumber
				+ "\",\"Myspace Link\": \""
				+ myspaceLink
				+ "\",\"Other Card\": \""
				+ otherCard
				+ "\",\"PayPal\": \""
				+ payPal
				+ "\",\"Pinterest Link\": \""
				+ pinterestLink
				+ "\",\"Primary Contact Actual Title\": \""
				+ primaryContactActualTitle
				// + "\",\"Primary Contact Email\": \"" + primaryContactEmail
				+ "\",\"Primary Contact First Name\": \""
				+ primaryContactFirstName
				+ "\",\"Primary Contact Last Name\": \""
				+ primaryContactLastName
				+ "\",\"Professional Associations\": \""
				+ professionalAssociations + "\",\"Products\": \"" + products
				+ "\",\"Services\": \"" + services
				+ "\",\"Operating Hours\": \""
				+ "Mon 7:00am-6:00pm, Tue-Thu 9:00am-6:00pm"
				+ "\",\"Short Web Address\": \"" + shorWebAddress
				+ "\",\"Store Card\": \"" + storeCard + "\",\"Suite\": \""
				+ suite + "\",\"Toll Free\": \"" + tollFree
				+ "\",\"Traveler's Check\": \"" + travelersCheck
				+ "\",\"TTY\": \"" + tty + "\",\"Twitter Link\": \""
				+ twitterLink + "\",\"Visa\": \"" + visa
				+ "\",\"Web Address\": \"" + webAddress
				+ "\",\"YouTube/Video Link\": \"" + youTubeorVideoLink + "\"}";
		return data;
	}

	/*
	 * private static String getProducts(String products) { String temp =
	 * Normalizer.normalize(products, Normalizer.Form.NFD); return
	 * temp.replaceAll("[^\\p{ASCII}]", ""); }
	 */

	public static boolean getAllUserInfo() {
		boolean validate = true;
		try {
			String targetURL = LBLConstants.INFOGROUP_SUBMISSIONSURL;
			CloseableHttpClient client = HttpClientBuilder.create().build();

			HttpGet request = new HttpGet(targetURL);
			request.setHeader("Content-Type", "application/json");
			/*
			 * request.setHeader("X-AUTH-TOKEN", "43ca2b690b6baf99ea4a8768");
			 * request.setHeader("id", "400faf24-1e15-11e4-92d1-379cc8c4ee4b");
			 */

			HttpResponse response = client.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
			int length = result.length();
			if (length == 2) {
				validate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return validate;
	}

	public static void main(String[] args) {

		/*
		 * List<LocalBusinessDTO> dtos = new ArrayList<LocalBusinessDTO>();
		 * 
		 * LocalBusinessDTO localBusinessDTO = new LocalBusinessDTO();
		 * 
		 * localBusinessDTO.setCompanyName("Maid Brigade of Annapolis");
		 * localBusinessDTO.setLocationAddress("2516 Housley Rd");
		 * localBusinessDTO.setLocationCity("Annapolis");
		 * localBusinessDTO.setLocationPhone("4107740527");
		 * localBusinessDTO.setLocationState("VA");
		 * localBusinessDTO.setLocationZipCode("21401");
		 * 
		 * dtos.add(localBusinessDTO);
		 */
		/*
		 * try { getAllUserInfo(); } catch (Exception e) { e.printStackTrace();
		 * }
		 */

		try {
			String targetURL = LBLConstants.INFOGROUP_SUBMISSIONSURL;
			CloseableHttpClient client = HttpClientBuilder.create().build();

			HttpGet request = new HttpGet(
					"http://lcf.whitespark.ca/api/v1/search?key=62121a3a70a15d4e9355e7c0106a0d5f&sig=md5(search&city=Edmonton&country=Canada&custom=Custom+Value&region=Alberta&search_term=Edmonton+Flowers&type=keyphrase&3bd7642a98a9797b8a0833f322372ff4)");
			request.setHeader("Content-Type", "application/json");
			/*
			 * request.setHeader("X-AUTH-TOKEN", "43ca2b690b6baf99ea4a8768");
			 * request.setHeader("id", "400faf24-1e15-11e4-92d1-379cc8c4ee4b");
			 */

			HttpResponse response = client.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
			int length = result.length();
			if (length == 2) {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
