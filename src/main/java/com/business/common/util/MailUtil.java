package com.business.common.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class MailUtil {
	static Logger logger = Logger.getLogger(MailUtil.class);

	public static boolean sendMail(String toMail, String text,String sub){
		logger.debug("MailUtil Start: Sening email");
		boolean isMessageSent = false;
	
		Properties props = new Properties();
		props.put("mail.smtp.host", LBLConstants.MAIL_HOST);
		props.put("mail.smtp.socketFactory.port", LBLConstants.MAIL_PORT);
		props.put("mail.smtp.socketFactory.class",LBLConstants.MAIL_CLASS);
		props.put("mail.smtp.auth", LBLConstants.MAIL_AUTH);
		props.put("mail.smtp.port", LBLConstants.MAIL_PORT);
		
		
		Session session = Session.getInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(LBLConstants.MAIL_EMAIL,LBLConstants.MAIL_PASSWORD);
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(LBLConstants.MAIL_EMAIL));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toMail));			
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
	
	/*public static void main(String[] args) {
		sendMail("ram789.it@gmail.com", "test", "LBL PRO TEST");
	}*/
}

