package com.business.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

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
			List<String> string = new ArrayList<String>();

			if (toMail.contains(",")) {
				if (toMail != null) {
					String[] split = toMail.split(",");
					for (String mail : split) {
						string.add(mail);
					}
				}

			} else {
				string.add(toMail);
			}
			String[] mails = new String[string.size()];

			mails = string.toArray(mails);
			InternetAddress[] toAddress = new InternetAddress[mails.length];
			for (int i = 0; i < mails.length; i++) {
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

	public static void sendTemplate(String email, String absolutePath) {
		// absolutePath="C:\\Users\\Administrator.ALA-PC\\AppData\\Local\\Temp\\renewalReport3643911313825471184.xls";
		logger.info("sendMailWithReceipt : start" + absolutePath);
		boolean isSuccess = false;

		logger.info("user email :" + email);

		Properties props = new Properties();
		props.put("mail.smtp.host", LBLConstants.MAIL_HOST);
		props.put("mail.smtp.socketFactory.port", LBLConstants.MAIL_PORT);
		props.put("mail.smtp.socketFactory.class", LBLConstants.MAIL_CLASS);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", LBLConstants.MAIL_AUTH);
		props.put("mail.smtp.port", LBLConstants.MAIL_PORT);

		/*
		 * Session session = Session.getInstance(props, new
		 * javax.mail.Authenticator() { protected PasswordAuthentication
		 * getPasswordAuthentication() { return new PasswordAuthentication(
		 * LBLConstants.MAIL_EMAIL, LBLConstants.MAIL_PASSWORD); } }); try {
		 * 
		 * Message message = new MimeMessage(session); message.setFrom(new
		 * InternetAddress(LBLConstants.MAIL_EMAIL));
		 * message.setRecipients(Message.RecipientType.TO,
		 * InternetAddress.parse(email)); // message.setText(activationLink);
		 * message
		 * .setSubject("Thank you, for your reservation with hotel name : ");
		 * 
		 * BodyPart messageBodyPart1 = new MimeBodyPart();
		 * messageBodyPart1.setText("This is message body");
		 * 
		 * //4) create new MimeBodyPart object and set DataHandler object to
		 * this object MimeBodyPart messageBodyPart2 = new MimeBodyPart();
		 * 
		 * DataSource source = new FileDataSource(absolutePath);
		 * messageBodyPart2.setDataHandler(new DataHandler(source));
		 * messageBodyPart2.setFileName(absolutePath);
		 * 
		 * BodyPart bodyPart = new MimeBodyPart();
		 * 
		 * String rawHTML = "test"; bodyPart.setContent(rawHTML, "text/html");
		 * Multipart multipart = new MimeMultipart();
		 * multipart.addBodyPart(bodyPart);
		 * 
		 * //bodyPart = attachFile(absolutePath);
		 * multipart.addBodyPart(messageBodyPart2);
		 * 
		 * message.setContent(multipart);
		 * 
		 * 
		 * Transport.send(message);
		 */
		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(LBLConstants.MAIL_EMAIL,
						LBLConstants.MAIL_PASSWORD);
			}
		};
		Session session = Session.getInstance(props, auth);
		try {
			// creates a new e-mail message
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress(LBLConstants.MAIL_EMAIL));
			InternetAddress[] toAddresses = { new InternetAddress(email) };
			msg.setRecipients(Message.RecipientType.TO, toAddresses);
			msg.setSubject("Bing Report");
			msg.setSentDate(new Date());

			// creates message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(msg, "text/html");

			// creates multi-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			String[] attachFiles = new String[1];
			attachFiles[0] = absolutePath;

			// adds attachments
			if (attachFiles != null && attachFiles.length > 0) {
				for (String filePath : attachFiles) {
					MimeBodyPart attachPart = new MimeBodyPart();

					try {
						attachPart.attachFile(filePath);
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					multipart.addBodyPart(attachPart);
				}
			}

			// sets the multi-part as e-mail's content
			msg.setContent(multipart);

			// sends the e-mail
			Transport.send(msg);

		} catch (MessagingException e) {
			e.printStackTrace();
			logger.info("came to catch block : send mail with receipt throws exception");
		}
		logger.info("sendMailWithReceipt : end");

	}

	public static void sendEmailWithAttachments(String host, String port,
			final String userName, final String password, String toAddress,
			String subject, String message, String[] attachFiles)
			throws AddressException, MessagingException {
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(userName));
		InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html; charset=utf-8");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// adds attachments
		if (attachFiles != null && attachFiles.length > 0) {
			for (String filePath : attachFiles) {
				MimeBodyPart attachPart = new MimeBodyPart();

				try {
					attachPart.attachFile(filePath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			}
		}

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);

		// sends the e-mail
		Transport.send(msg);

	}

	/**
	 * Test sending e-mail with attachments
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
	

		sendEmail("vasanth.odesk@gmail.com", "C:\\lbl\\BingReport.xls",
				"Bing API Response", "Bing API Response file attached");
	}

	public static void sendEmail(String toMail, String fileName, String subject, String bodyText)
			throws Exception {
		final String user = "lblpro.dev@gmail.com";
		final String password = "lblpro@123";
		String host = "smtp.gmail.com";

		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", user);
		properties.put("mail.password", password);
		properties.put("mail.smtp.ssl.trust", host);

		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		};
		Session session = Session.getInstance(properties, auth);

		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(user));
		InternetAddress[] toAddresses = { new InternetAddress(toMail) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(subject, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		MimeBodyPart attachPart = new MimeBodyPart();
		attachPart.attachFile(fileName);
		multipart.addBodyPart(attachPart);
		msg.setContent(multipart);

		Transport.send(msg);
	}
}
