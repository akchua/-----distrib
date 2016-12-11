package com.chua.distributions.utility;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.chua.distributions.constants.MailConstants;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 2, 2016
 */
public class EmailUtil {
	
	private static Session session = null;
	
	public static boolean send(String to, String subject, String content) {
		return send(to, null, null, subject, content);
	}
	
	public static boolean send(String to, String cc, String bcc, String subject, String content) {
		boolean success = true;
		
		try {
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(MailConstants.SMTP_FROM_ADDRESS));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			if(cc != null) message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
			if(bcc != null) message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
			message.setSubject(subject);
			message.setText(content);

			Transport.send(message);
		} catch (MessagingException e) {
			success = false;
			throw new RuntimeException(e);
		}
		
		return success;
	}

	public static void connect() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", MailConstants.SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.host", MailConstants.SMTP_HOST);
		props.put("mail.smtp.port", MailConstants.SMTP_PORT);
		
		session = Session.getInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(MailConstants.SMTP_USERNAME, MailConstants.SMTP_PASSWORD);
				}
			});
	}
	
	public static boolean validateEmail(String emailAddress) {
		return emailAddress.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
										+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}
}
