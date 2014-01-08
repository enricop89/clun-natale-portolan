package beans.utils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Session Bean implementation class SendEmail
 */
@Stateless
@LocalBean
public class SendEmail {    
    public static boolean send(String receiver, String subject, String text)
    {    
    	final String username = "traveldreamcnp@gmail.com";
		final String password = "traveldream.";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(receiver));
			message.setSubject(subject);
			message.setText(text);
 
			Transport.send(message);
			return true;
 
		} catch (MessagingException e) {
			return false;
		}
	}

}
