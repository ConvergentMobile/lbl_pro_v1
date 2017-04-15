package com.business.common.util;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Vasanth
 * 
 */
public class LBLConstants {

	public static final String LBL_ACXIOM = "Acxiom";
	public static final String LBL_INFOGROUP = "InfoGroup";
	public static final String LBL_GOOGLE = "Google";
	public static final String LBL_FACTUAL = "Factual";
	public static final String LBL_BING = "Bing";
	public static final String LBL_NEUSTAR = "Neustar";

	public static final String[] LBL_PARTNERS_ARR = { LBL_ACXIOM, LBL_BING,
			LBL_FACTUAL, LBL_GOOGLE, LBL_INFOGROUP, LBL_NEUSTAR };

	public static final String LBL_INFOGROUP_SUBMISSION_TYPE = "A";

	public static final String LBL_ACXIOM_EXPORT_FILE_PATH = "";

	public static final String ACXIOM_FILE_PATH = PropUtil.PROVIDERS_BUNDLE
			.getString("acxiom.export.file.path");

	public static final String ACXIOM_USERNAME = PropUtil.PROVIDERS_BUNDLE
			.getString("acxiom.authorization.userName");

	public static final String ACXIOM_PASSWORD = PropUtil.PROVIDERS_BUNDLE
			.getString("acxiom.authorization.password");

	public static final String ACXIOM_APIKEY = PropUtil.PROVIDERS_BUNDLE
			.getString("acxiom.apiKey");

	public static final Integer ACXIOM_VENDORID = Integer
			.valueOf(PropUtil.PROVIDERS_BUNDLE.getString("acxiom.vendorId"));

	public static final String NEUSTAR_USERNAME = PropUtil.PROVIDERS_BUNDLE
			.getString("neustarLocaleze.authorization.userName");

	public static final String NEUSTAR_PASSWORD = PropUtil.PROVIDERS_BUNDLE
			.getString("neustarLocaleze.authorization.password");

	public static final String NEUSTAR_SERVICEID = PropUtil.PROVIDERS_BUNDLE
			.getString("neustarLocaleze.serviceId");

	public static final String NEUSTAR_ENDPOINT = PropUtil.PROVIDERS_BUNDLE
			.getString("neustarLocaleze.endPoint");

	public static final String INFOGROUP_USERNAME = PropUtil.PROVIDERS_BUNDLE
			.getString("infogroup.authorization.userName");

	public static final String INFOGROUP_PASSWORD = PropUtil.PROVIDERS_BUNDLE
			.getString("infogroup.authorization.password");

	public static final String INFOGROUP_ACCESSTOKENURL = PropUtil.PROVIDERS_BUNDLE
			.getString("infogroup.accessTokenURL");

	public static final String INFOGROUP_SUBMISSIONSURL = PropUtil.PROVIDERS_BUNDLE
			.getString("infogroup.submissionsURL");

	public static final String DATE_RANGE_ALL = "all";

	public static final String DATE_RANGE_THIS_MONTH = "this month";

	public static final String DATE_RANGE_LAST_MONTH = "last month";

	public static final String DATE_RANGE_LAST_3_MONTHS = "last 3 months";

	public static final int CONVERGENT_MOBILE_ADMIN = 1;

	public static final int CHANNEL_ADMIN = 2;

	public static final int CLIENT_ADMIN = 3;

	public static final int USER = 4;
	public static final int PURIST = 5;

	public static final int YEAR_ESTABLISHED = 1492;

	public static final long PASS_RESET_LINK_DDEFAULT_VALID_TIME = 24 * 60 * 60 * 1000;

	public static final String MAIL_HOST = PropUtil.MAIL_BUNDLE
			.getString("mail.host");

	public static final String MAIL_PORT = PropUtil.MAIL_BUNDLE
			.getString("mail.port");

	public static final String MAIL_CLASS = PropUtil.MAIL_BUNDLE
			.getString("mail.class");

	public static final String MAIL_AUTH = PropUtil.MAIL_BUNDLE
			.getString("mail.auth");

	public static final String MAIL_EMAIL = PropUtil.MAIL_BUNDLE
			.getString("mail.email");

	public static final String MAIL_PASSWORD = PropUtil.MAIL_BUNDLE
			.getString("mail.password");

	public static final String MAIL_URL = PropUtil.MAIL_BUNDLE
			.getString("mail.url");

	public static final String MAIL_MESSAGE = PropUtil.MAIL_BUNDLE
			.getString("mail.message");

	public static final String MAIL_SUBJECT = PropUtil.MAIL_BUNDLE
			.getString("mail.subject");

	public static final String NOTIFY_MAIL = PropUtil.MAIL_BUNDLE
			.getString("mail.notification");
	public static final String SUPPORT_MAIL = PropUtil.MAIL_BUNDLE
			.getString("support.mail");

	public static final int MAX_RECORDS_PER_PAGE = Integer
			.valueOf(PropUtil.LBL_BUNDLE.getString("pagination.maxrows.page"));

	public static final String[] UPLOAD_BEAN_PROPERTIES = PropUtil.UPLOAD_BUNDLE
			.getString("upload.file.bean.properties").split(",");

	public static final String[] UPLOAD_CATEGORY_PROPERTIES = PropUtil.UPLOAD_BUNDLE
			.getString("upload.file.catgory.properties").split(",");

	public static final String[] UPLOAD_EXCEL_HEADERS = PropUtil.UPLOAD_BUNDLE
			.getString("upload.file.excel.headers").split(",");

	public static final String[] UPLOAD_CATGORY_HEADERS = PropUtil.UPLOAD_BUNDLE
			.getString("upload.file.catgory.headers").split(",");

	public static final List<String> ACTION_CODES = Arrays
			.asList(PropUtil.UPLOAD_BUNDLE.getString("upload.file.actioncodes")
					.split(","));

	public static final List<String> COUNTRY_CODES = Arrays
			.asList(PropUtil.UPLOAD_BUNDLE
					.getString("upload.file.countrycodes").split(","));

	public static final List<String> NON_LOCATION_ADDRESS_DATA = Arrays
			.asList(PropUtil.UPLOAD_BUNDLE.getString(
					"upload.file.non.location.addressdata").split(","));

	public static final List<String> NON_SUITE_DATA = Arrays
			.asList(PropUtil.UPLOAD_BUNDLE.getString(
					"upload.file.non.suitedata").split(","));

	public static final String FIELD_TYPE_ALPHA_NUMERIC = "AN";

	public static final String FIELD_TYPE_ALPHA = "A";

	public static final String FIELD_TYPE_NUMERIC = "N";

	public static final String[] WEB_ADDRESS_ENDS_WITH = { ".com", ".org",
			".net" };

	public static final List<String> TITLE_MANAGER_OR_OWNER = Arrays
			.asList(PropUtil.UPLOAD_BUNDLE.getString(
					"upload.file.title.manager.or.owner").split(","));

	public static final List<String> PROFESSIONAL_TITLES = Arrays
			.asList(PropUtil.UPLOAD_BUNDLE.getString(
					"upload.file.professional.title").split(","));

	public static final String SMARTYSTREETS_CHECK = PropUtil.SMART_BUNDLE
			.getString("smartstreetcheck");

	public static final String SMART_AUTHID = PropUtil.SMART_BUNDLE
			.getString("smart.authId");

	public static final String SMART_AUTHTOKEN = PropUtil.SMART_BUNDLE
			.getString("smart.authToken");

	public static final String SMART_URL = PropUtil.SMART_BUNDLE
			.getString("smart.url");
	public static final String google_Key = PropUtil.Google_Key
			.getString("google.Key");
	
}
