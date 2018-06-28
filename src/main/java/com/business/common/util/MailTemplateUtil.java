package com.business.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;







import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.axiom.om.impl.MIMEOutputUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.business.service.BusinessService;

@Component
public class MailTemplateUtil {
	@Autowired
	private static BusinessService businessService;
	

	static Logger logger = Logger.getLogger(MailTemplateUtil.class);

	public static void sendMailWithReceipt(String email,
			 String pdfPath, HSSFWorkbook workbook,String reportName,String toMail)
			throws FileNotFoundException, IOException {
		logger.info("toEmail::::::::::::::::::::"+toMail);
		try{
			
	        final String fromEmail = LBLConstants.MAIL_EMAIL;//requires valid gmail id
	        final String password = LBLConstants.MAIL_PASSWORD; // correct password for gmail id
	        //final String toEmail = email; // can be any email id 
	        List<String> string=new ArrayList<String>();

			if(toMail !=null && toMail.contains(",")){
				if(toMail!=null){
					String[] split = toMail.split(",");
					for (String mail : split) {
						string.add(mail);
					}
				}
				
			}else{
				string.add(toMail);
			}
			String[] mails = new String[string.size()];
	        System.out.println("TLSEmail Start");
	        Properties props = new Properties();
/*	        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
	        props.put("mail.smtp.port", "587"); //TLS Port
	        props.put("mail.smtp.auth", "true"); //enable authentication
	        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
	        props.put("mail.smtp.socketFactory.port", "465");
	        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	        
			Properties props = new Properties();*/
			props.put("mail.smtp.host", LBLConstants.MAIL_HOST);
			props.put("mail.smtp.socketFactory.port", LBLConstants.MAIL_PORT);
			props.put("mail.smtp.socketFactory.class", LBLConstants.MAIL_CLASS);
			props.put("mail.smtp.auth", LBLConstants.MAIL_AUTH);
			props.put("mail.smtp.port", LBLConstants.MAIL_PORT);

	            //create Authenticator object to pass in Session.getInstance argument
	        Authenticator auth = new Authenticator() {
	            //override the getPasswordAuthentication method
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(fromEmail, password);
	            }
	        };
	        Session session = Session.getInstance(props, auth);
	        MIMEOutputUtils mimeOutputUtils=new MIMEOutputUtils();
	       
	        MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(fromEmail));
	        mails = string.toArray(mails);
			 InternetAddress[] toAddress = new InternetAddress[mails.length];
			  for( int i = 0; i < mails.length; i++ ) {
	                toAddress[i] = new InternetAddress(mails[i]);
	            }

			message.setRecipients(RecipientType.TO, toAddress);
	       

	        message.setSubject(reportName);
	        message.setText("test");
	        BodyPart messageBodyPart1 = new MimeBodyPart();  
	        messageBodyPart1.setText(reportName);  
	          
	        //4) create new MimeBodyPart object and set DataHandler object to this object      
	        MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
	      
	        String filename =pdfPath;//change accordingly  
	        DataSource source = new FileDataSource(filename);  
	        messageBodyPart2.setDataHandler(new DataHandler(source));  
	        messageBodyPart2.setFileName(reportName);  
	         
	         
	        //5) create Multipart object and add MimeBodyPart objects to this object      
	        Multipart multipart = new MimeMultipart();  
	        multipart.addBodyPart(messageBodyPart1);  
	        multipart.addBodyPart(messageBodyPart2);  
	      
	        //6) set the multiplart object to the message object  
	        message.setContent(multipart );  

	        Transport.send(message);
	        logger.info("Mail Sent");
	    }catch(Exception ex){
	    	logger.info("Mail sending is failed");
	    	logger.info(ex);
	    } 
		logger.info("sendMailWithReceipt : end");
	}

	private static String readTemplate(String reservationTemplatePath) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(reservationTemplatePath)));
		StringBuilder buffer = new StringBuilder();
		while (input.ready()) {
			buffer.append(input.readLine());
		}
		input.close();
		return buffer.toString();
	}

	private static BodyPart attachFile(String pdfPath) throws MessagingException {
		logger.info(" pdf file path is :"+pdfPath);
		BodyPart bodyPart;
		bodyPart = new MimeBodyPart();
		FileDataSource fds = new FileDataSource(pdfPath);
		bodyPart.setFileName(fds.getName());
		bodyPart.setDataHandler(new DataHandler(fds));
		return bodyPart;
	}

	
}
