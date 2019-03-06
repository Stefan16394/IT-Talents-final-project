package com.vmzone.demo.utils;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.http.HttpStatus;

import com.vmzone.demo.exceptions.InvalidEmailException;

public class EmailSender {
	
	
	public static final String EMAIL = "vmzona.noreply@gmail.com";
	private static final String PASS = "vmzona123456";
	
	public static String forgottenPassword(String email) throws AddressException, InvalidEmailException, MessagingException, IOException {
		String newPass = EmailConstantsHelper.forgottenPassword();
		sendEmail(email, EmailConstantsHelper.SUBJECT_FORGOTTEN_PASSWORD, newPass);
		
		return newPass;
	}
	
	public static void registration(String email) throws AddressException, InvalidEmailException, MessagingException, IOException {
		
		sendEmail(email, EmailConstantsHelper.SUBJECT_WELCOME, EmailConstantsHelper.registration());
		
	}
	
	public static void sendSubscripedPromotions(List<String> emails) throws AddressException, InvalidEmailException, MessagingException, IOException {
		
		for(String email : emails) {
			sendEmail(email, EmailConstantsHelper.SUBJECT_PROMOTIONS, EmailConstantsHelper.subscribedPromotions());
		}
		
	}
	
	
	public static void contactUs(String text) throws AddressException, InvalidEmailException, MessagingException, IOException {
		sendEmail(EMAIL, EmailConstantsHelper.SUBJECT_CONTACT_US, text);
	}
	
	
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
