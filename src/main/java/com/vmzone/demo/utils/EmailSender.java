package com.vmzone.demo.utils;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
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

import org.springframework.http.HttpStatus;

import com.vmzone.demo.exceptions.InvalidEmailException;

public class EmailSender {
	
	public static final String EMAIL = "vmzona.noreply@gmail.com";
	private static final String PASS = "vmzona123456";
	
	public static void sendEmail(String receiver, String subject, String bodyText) throws AddressException, MessagingException, IOException, InvalidEmailException {
		   Properties props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", "smtp.gmail.com");
		   props.put("mail.smtp.port", "587");
		   props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication(EMAIL, PASS);
		      }
		   });
		   Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress("vmzona.noreply@gmail.com", false));
		   
		   if(RegexValidator.validateEmail(receiver)) {
			  msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver)); 
		   } else {
			   throw new InvalidEmailException(HttpStatus.NOT_ACCEPTABLE, "Incorrect email");
		   }
		   
		   msg.setSubject(subject);
		   msg.setText(bodyText);
		   //msg.setSentDate(new Date());

		   
		   Transport.send(msg);   
		   System.out.println("Mail sent!");
		}

}
