package com.business.common.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
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
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class MailUtil {
	static Logger logger = Logger.getLogger(MailUtil.class);

	public static boolean sendMail(String toMail, String text, String sub) {
		logger.debug("MailUtil Start: Sening email");
		boolean isMessageSent = false;

		Properties props = new Properties();
		props.put("mail.smtp.host", LBLConstants.MAIL_HOST);
		props.put("mail.smtp.socketFactory.port", LBLConstants.MAIL_PORT);
		props.put("mail.smtp.socketFactory.class", LBLConstants.MAIL_CLASS);
		props.put("mail.smtp.auth", LBLConstants.MAIL_AUTH);
		props.put("mail.smtp.port", LBLConstants.MAIL_PORT);

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								LBLConstants.MAIL_EMAIL,
								LBLConstants.MAIL_PASSWORD);
					}
				});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(LBLConstants.MAIL_EMAIL));
			List<String> string=new ArrayList<String>();

			if(toMail.contains(",")){
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
			
			mails = string.toArray(mails);
			 InternetAddress[] toAddress = new InternetAddress[mails.length];
			  for( int i = 0; i < mails.length; i++ ) {
	                toAddress[i] = new InternetAddress(mails[i]);
	            }

			message.setRecipients(RecipientType.TO, toAddress);
		

			message.setText(text);
			message.setSubject(LBLConstants.MAIL_SUBJECT);
			Transport.send(message);
			isMessageSent = true;

		} catch (MessagingException e) {
			isMessageSent = false;
			e.printStackTrace();
		}
		logger.debug("End: Email is Successfully sent");
		return isMessageSent;
	}

	/*
	 * public static void main(String[] args) { sendMail("ram789.it@gmail.com",
	 * "test", "LBL PRO TEST"); }
	 */

	public static boolean sendNotification(String toMail, String body,
			String subject) {
		logger.debug("MailUtil Start: Sening email");
		boolean isMessageSent = false;

		Properties props = new Properties();
		props.put("mail.smtp.host", LBLConstants.MAIL_HOST);
		props.put("mail.smtp.socketFactory.port", LBLConstants.MAIL_PORT);
		props.put("mail.smtp.socketFactory.class", LBLConstants.MAIL_CLASS);
		props.put("mail.smtp.auth", LBLConstants.MAIL_AUTH);
		props.put("mail.smtp.port", LBLConstants.MAIL_PORT);

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								LBLConstants.MAIL_EMAIL,
								LBLConstants.MAIL_PASSWORD);
					}
				});

		try {

			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			try {
				msg.setFrom(new InternetAddress(LBLConstants.MAIL_EMAIL,
						"NoReply-JD"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			msg.setReplyTo(InternetAddress
					.parse(LBLConstants.MAIL_EMAIL, false));

			msg.setSubject(subject, "UTF-8");

			msg.setText(subject + "\n \n" + body, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toMail, false));
			System.out.println("Message is ready");
			Transport.send(msg);
			isMessageSent = true;
			System.out.println("EMail Sent Successfully!!");

		} catch (MessagingException e) {
			isMessageSent = false;
			e.printStackTrace();
		}
		logger.debug("End: Email is Successfully sent");
		return isMessageSent;
	}

	public static void sendTemplate(String email, String absolutePath,
			HSSFWorkbook workbook) {
		absolutePath="C:\\Users\\Administrator.ALA-PC\\AppData\\Local\\Temp\\renewalReport3643911313825471184.xls";
		logger.info("sendMailWithReceipt : start"+absolutePath);
		boolean isSuccess = false;
		
		logger.info("user email :" + email);
		
		Properties props = new Properties();
		props.put("mail.smtp.host", LBLConstants.MAIL_HOST);
		props.put("mail.smtp.socketFactory.port", LBLConstants.MAIL_PORT);
		props.put("mail.smtp.socketFactory.class", LBLConstants.MAIL_CLASS);
		props.put("mail.smtp.auth", LBLConstants.MAIL_AUTH);
		props.put("mail.smtp.port", LBLConstants.MAIL_PORT);

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								LBLConstants.MAIL_EMAIL,
								LBLConstants.MAIL_PASSWORD);
					}
				});
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(LBLConstants.MAIL_EMAIL));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			// message.setText(activationLink);
			message.setSubject("Thank you, for your reservation with hotel name : ");
			BodyPart bodyPart = new MimeBodyPart();
			String rawHTML = "test";
			bodyPart.setContent(rawHTML, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(bodyPart);

			bodyPart = attachFile(absolutePath);
			multipart.addBodyPart(bodyPart);

			message.setContent(multipart);
			Transport.send(message);
			

		} catch (MessagingException e) {
			logger.info("came to catch block : send mail with receipt throws exception");
		}
		logger.info("sendMailWithReceipt : end");
	
		
	}

	private static BodyPart attachFile(String filename) throws MessagingException {
		logger.info("Pdf file path is :"+filename);
		BodyPart bodyPart;
		bodyPart = new MimeBodyPart();
		FileDataSource fds = new FileDataSource(filename);
		bodyPart.setFileName(fds.getName());
		bodyPart.setDataHandler(new DataHandler(fds));
		return bodyPart;
	}
}
