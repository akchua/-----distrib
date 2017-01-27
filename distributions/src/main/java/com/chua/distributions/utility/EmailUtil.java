package com.chua.distributions.utility;

import java.io.IOException;
import java.util.Properties;

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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.chua.distributions.constants.MailConstants;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 2, 2016
 */
@Component
public class EmailUtil {
	
	@Value("${enable.send.email}")
	private Boolean enableSendEmail;
	
	private Session session = null;
	
	public EmailUtil() {
		connect();
	}
	
	public boolean send(String to, String subject, String content) {
		return send(to, subject, content, null);
	}
	
	public boolean send(String to, String subject, String content, String[] attachments) {
		return send(to, null, null, subject, content, attachments);
	}
	
	public boolean send(String to, String cc, String bcc, String subject, String content, String[] attachments) {
		boolean success = true;

		if(enableSendEmail) {
			try {
				Message message = new MimeMessage(session);
				Multipart multipart = new MimeMultipart();
				
				message.setFrom(new InternetAddress(MailConstants.SMTP_FROM_ADDRESS));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
				if(cc != null) message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
				if(bcc != null) message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
				message.setSubject(subject);
				
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(message, "text/html");
				messageBodyPart.setText(content);
				multipart.addBodyPart(messageBodyPart);
				
				if(attachments != null && attachments.length > 0) {
					for(String filePath : attachments) {
						MimeBodyPart attachPart = new MimeBodyPart();
						try {
							attachPart.attachFile(filePath);
						} catch (IOException e) {
							e.printStackTrace();
						}
						multipart.addBodyPart(attachPart);
					}
				}
	
				message.setContent(multipart);
				Transport.send(message);
			} catch (MessagingException e) {
				success = false;
				throw new RuntimeException(e);
			}
		}
		
		return success;
	}

	public void connect() {
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
	
	public boolean validateEmail(String emailAddress) {
		return emailAddress.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
										+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}
}
