package com.partners.acxiom;

import java.io.File;
import java.io.FileInputStream;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.llom.OMNamespaceImpl;
import org.apache.axis2.AxisFault;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.business.common.util.LBLConstants;
import com.partners.acxiom.www.bulkapi.schema.BulkAPIServiceStub;
import com.partners.acxiom.www.bulkapi.schema.BulkAPIServiceStub.BulkHeader;
import com.partners.acxiom.www.bulkapi.schema.BulkAPIServiceStub.ClientIdListIdLatestXRefRequest;
import com.partners.acxiom.www.bulkapi.schema.BulkAPIServiceStub.ClientIdListIdLatestXRefResponse;
import com.partners.acxiom.www.bulkapi.schema.BulkAPIServiceStub.UploadFileRequest;
import com.partners.acxiom.www.bulkapi.schema.BulkAPIServiceStub.UploadFileResponse;

/**
 * 
 * @author lbl_dev
 * 
 */
public class AcxiomClient {
	static Logger logger = Logger.getLogger(AcxiomClient.class);

	static BulkAPIServiceStub stub;

	/**
	 * 
	 * @throws AxisFault
	 */
	public static void initializeAcxiomService() throws Exception {
		String userName = LBLConstants.ACXIOM_USERNAME;
		String password = LBLConstants.ACXIOM_PASSWORD;

		try {
			OMFactory omFactory = OMAbstractFactory.getOMFactory();
			OMNamespace SecurityElementNamespace = new OMNamespaceImpl(
					"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
					"wsse");

			OMElement omSecurityElement = omFactory
					.createOMElement(
							new QName(
									"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
									"Security", "wsse"), null);
			omSecurityElement
					.declareNamespace(new OMNamespaceImpl(
							"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd",
							"wsu"));

			OMElement omusertoken = omFactory.createOMElement("UsernameToken",
					SecurityElementNamespace);

			OMElement omuserName = omFactory.createOMElement("Username",
					SecurityElementNamespace);
			omuserName.setText(userName);

			OMElement omPassword = omFactory.createOMElement("Password",
					SecurityElementNamespace);

			omPassword
					.addAttribute(
							"Type",
							"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText",
							null);
			omPassword.setText(password);

			omusertoken.addChild(omuserName);
			omusertoken.addChild(omPassword);
			omSecurityElement.addChild(omusertoken);
			stub = new BulkAPIServiceStub();
			stub._getServiceClient().addHeader(omSecurityElement);

		} catch (Exception e) {
			logger.error("There was a problem to connect with acxiom service");
			e.printStackTrace();
		}

	}

	/**
	 * Get the ListingId
	 * 
	 * @param clinetId
	 * @return
	 * @throws Exception
	 */
	public long getListingId(String clinetId) throws Exception {
		if (stub == null) {
			initializeAcxiomService();
		}
		ClientIdListIdLatestXRefRequest clientIdListIdLatestXRefRequest = new ClientIdListIdLatestXRefRequest();
		String apiKey = LBLConstants.ACXIOM_APIKEY;
		clientIdListIdLatestXRefRequest.setClientRecordId(clinetId);
		clientIdListIdLatestXRefRequest.setHeader(new BulkHeader());
		clientIdListIdLatestXRefRequest.getHeader().setAPIKey(apiKey);

		ClientIdListIdLatestXRefResponse response = stub
				.ClientIdListIdLatestXRef(clientIdListIdLatestXRefRequest);

		long listingId = response.getListingId();

		return listingId;
	}

	public static void uploadAcxiomFile(String acxiomDataFile, Integer vendorId)
			throws Exception {
		String apiKey = LBLConstants.ACXIOM_APIKEY;
		try {
			File file = new File(acxiomDataFile);
			FileInputStream fin = new FileInputStream(file);
			byte fileContent[] = new byte[(int) file.length()];
			fin.read(fileContent);
			byte fc[] = Base64.encodeBase64(fileContent);

			DataHandler dh = new DataHandler(new ByteArrayDataSource(
					fileContent));
			UploadFileRequest req = new UploadFileRequest();
			req.setHeader(new BulkHeader());
			req.getHeader().setAPIKey(apiKey);
			req.setVendorId(vendorId);
			req.setIsTestFile(false);
			req.setFileName("lbl_pro");
			req.setFileData(dh);

			UploadFileResponse resp = stub.UploadFile(req);
			logger.debug("Return Message from Acxiom: " + resp.getFileId()
					+ " -- " + resp.getReturnMessage());
			logger.debug("Return Value and Message Code form Acxiom are: "
					+ resp.getReturnValue() + " --- "
					+ resp.getReturnMessageCode());

		} catch (Exception e) {
			logger.error("There was a problem to while uploading the data file to acxiom service for the Vendor: "
					+ vendorId + " Issue is: " + e);
			e.printStackTrace();
			throw new Exception();
		}

	}

	public static void main(String[] args) throws Exception {
		try {
			initializeAcxiomService();
			// uploadAcxiomFile("D:/KrishnaPillai/myAcxiomData.csv");

			AcxiomClient clinet = new AcxiomClient();
			Long listingId = clinet.getListingId("908238");
			System.out.println("listingID: " + listingId);

		} catch (Exception e) {
			logger.error("There was a problem to while uploading the data file to acxiom service"
					+ e);
			e.printStackTrace();
		}
	}

	/*
	 * private static Policy loadPolicy(String filePath) throws
	 * XMLStreamException, FileNotFoundException { StAXOMBuilder builder = new
	 * StAXOMBuilder(filePath); return
	 * PolicyEngine.getPolicy(builder.getDocumentElement()); }
	 */
}
