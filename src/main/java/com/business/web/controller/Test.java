package com.business.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

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
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.business.common.util.LBLConstants;

public class Test {
public static void main(String[] args) {
	try{
        final String fromEmail = "pinky.priya451@gmail.com"; //requires valid gmail id
        final String password = "pinkychinni@92"; // correct password for gmail id
        final String toEmail = "vasanth.odesk@gmail.com"; // can be any email id 

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

        System.out.println("Mail Check 2");

        message.setSubject("Oil Error Report");
        message.setText("test");
        BodyPart messageBodyPart1 = new MimeBodyPart();  
        messageBodyPart1.setText("This is message body");  
          
        //4) create new MimeBodyPart object and set DataHandler object to this object      
        MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
      
        String filename = "C:\\Users\\ADMINI~1.ALA\\AppData\\Local\\Temp\\renewalReport8108423848966705981.xls";//change accordingly  
        DataSource source = new FileDataSource(filename);  
        messageBodyPart2.setDataHandler(new DataHandler(source)); 
       
        messageBodyPart2.setFileName(filename);  
        messageBodyPart2.attachFile(filename);
         
        //5) create Multipart object and add MimeBodyPart objects to this object      
        Multipart multipart = new MimeMultipart();  
        multipart.addBodyPart(messageBodyPart1);  
        multipart.addBodyPart(messageBodyPart2);  
      
        //6) set the multiplart object to the message object  
        message.setContent(multipart );  

        System.out.println("Mail Check 3");

        Transport.send(message);
        System.out.println("Mail Sent");
    }catch(Exception ex){
        System.out.println("Mail fail");
        System.out.println(ex);
    } 
}
public void emailsend(String pdfPath){
	try{
        final String fromEmail = LBLConstants.MAIL_EMAIL; //requires valid gmail id
        final String password = LBLConstants.MAIL_PASSWORD; // correct password for gmail id
        final String toEmail = "vasanth.odesk@gmail.com"; // can be any email id 

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

        System.out.println("Mail Check 2");

        message.setSubject("Oil Error Report","UTF8");
        message.setText("test");
        BodyPart messageBodyPart1 = new MimeBodyPart();  
        messageBodyPart1.setText("This is message body");  
          
        //4) create new MimeBodyPart object and set DataHandler object to this object      
        MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
      
        String filename = pdfPath;//change accordingly  
       File file=new File(filename);
        
        DataSource source = new FileDataSource(file);  
        messageBodyPart2.setDataHandler(new DataHandler(source));  
        //messageBodyPart2.setHeader("Content-Disposition", "attachment;filename="+filename+".xls");
        messageBodyPart2.setFileName(filename);  
        //messageBodyPart2.attachFile(file);
        //5) create Multipart object and add MimeBodyPart objects to this object      
        Multipart multipart = new MimeMultipart();  
        multipart.addBodyPart(messageBodyPart1); 
        
        multipart.addBodyPart(messageBodyPart2);  
      
        //6) set the multiplart object to the message object  
        message.setContent(multipart );  
       

        System.out.println("Mail Check 3");

        Transport.send(message);
        System.out.println("Mail Sent");
    }catch(Exception ex){
        System.out.println("Mail fail");
        System.out.println(ex);
    } 
}
public void sendEmailWithAttachment(String recipients, String subject, String body,
		String attachmentFileName, HSSFWorkbook hssWorkBook) throws UnsupportedEncodingException {
	Properties props = new Properties();
	props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
    props.put("mail.smtp.port", "587"); //TLS Port
    props.put("mail.smtp.auth", "true"); //enable authentication
    props.put("mail.smtp.starttls.enable", "true");
	 
	 /*if (recipients.equals("") || subject.equals("") || body.equals("") || attachmentFileName.equals("") || hssWorkBook.getBytes().length < 0 ) {
	 System.out.println("Usage: sendEmailAttachemt() method parameter might be missing, you may check for the /test/local/surveyToExcel-test.properties for any recent changes");
	 System.exit(1);
	 }*/
    Authenticator auth = new Authenticator() {
        //override the getPasswordAuthentication method
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("pinky.priya451@gmail.com", "pinkychinni@92");
        }
    };
	 Session session = Session.getInstance(props, auth);
	 try {
	 // create a message
	 MimeMessage msg = new MimeMessage(session);
	 DataSource ds = null;
	 msg.setFrom(new InternetAddress("pinky.priya451@gmail.com"));
	 
	/* ArrayList recipientsArray = new ArrayList();
	 StringTokenizer stringTokenizer = new StringTokenizer(recipients, ",");
	 
	 while (stringTokenizer.hasMoreTokens()) {
	 recipientsArray.add(stringTokenizer.nextToken());
	 }*/
	 /*int sizeTo = recipientsArray.size();
	 InternetAddress[] addressTo = new InternetAddress[sizeTo];
	 for (int i = 0; i < sizeTo; i++) {
	 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
	 }*/
	 msg.setRecipients(Message.RecipientType.TO, recipients);
	 
	 msg.setSubject(MimeUtility.encodeText(subject, "UTF-8", "Q"));
	 
	 // create and fill the first message part
	 MimeBodyPart mimeBodyPart1 = new MimeBodyPart();
	 mimeBodyPart1.setText(body);
	 
	 // create the second message part
	 MimeBodyPart mimeBodyPart2 = new MimeBodyPart();
	 
	 ByteArrayOutputStream baos = new ByteArrayOutputStream();
	 byte[] bytes =null;
	 try{
	 hssWorkBook.write(baos);
	 bytes = baos.toByteArray();
	 ds = new ByteArrayDataSource(bytes, "application/vnd.ms-excel");
	 }catch (IOException ioe ){
	 ioe.printStackTrace();
	 }
	System.out.println("bytes::::::::::::"+bytes.toString());
	 DataHandler dh = new DataHandler(ds);
	 mimeBodyPart2.setHeader("Content-Disposition", "attachment;filename="+attachmentFileName+".xls");
	 mimeBodyPart2.setContent(bytes, "application/vnd.ms-excel");
	 mimeBodyPart2.setDataHandler(dh);
	 mimeBodyPart2.setDisposition(MimeBodyPart.ATTACHMENT);
	 mimeBodyPart2.setFileName(attachmentFileName);
	 // create the Multipart and add its parts to it
	 Multipart multiPart = new MimeMultipart();
	 multiPart.addBodyPart(mimeBodyPart1);
	 multiPart.addBodyPart(mimeBodyPart2);
	
	 // add the Multipart to the message
	 msg.setContent(multiPart);
	 
	 // set the Date: header
	 msg.setSentDate(new Date());
	 msg.saveChanges(); 
	 // send the message
	 javax.mail.Transport.send(msg);
	 
	 }catch (MessagingException mex) {
	 mex.printStackTrace();
	 Exception ex = null;
	 if ((ex = mex.getNextException()) != null) {
	 ex.printStackTrace();
	 }
	 }
	
}

}
