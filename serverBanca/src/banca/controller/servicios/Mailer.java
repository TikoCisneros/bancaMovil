package banca.controller.servicios;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class Mailer {

	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;

	static String guser = "bancawebmailer@gmail.com";
	static String gpass = "bancamovil7";

	public static void generateAndSendEmail(String to, String subject,
			String emailBody) throws AddressException, MessagingException {

		// setup Mail Server Properties..
		mailServerProperties = new Properties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		mailServerProperties.put("mail.smtp.host", "smtp.gmail.com");
		mailServerProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		// 2nd ===> get Mail Session.."
		getMailSession = Session.getDefaultInstance(mailServerProperties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(guser, gpass);
					}
				});
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO,
				new InternetAddress(to));
		generateMailMessage.setSubject(subject);
		generateMailMessage.setContent(emailBody, "text/html");
		Transport.send(generateMailMessage);
		System.out.println("Correo enviado a : " +to);
		// Get Session and Send mail"
		// Transport transport = getMailSession.getTransport("smtp");

		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		// transport.connect("smtp.gmail.com",guser, gpass);
		// transport.sendMessage(generateMailMessage,
		// generateMailMessage.getAllRecipients());
		// transport.close();
	}
	public static void generateAndSendEmailM(String to, String subject,
			String emailBody) {
	    Client client = Client.create();
	    client.addFilter(new HTTPBasicAuthFilter("api",
	                "key-29ebc947023a09b9b1202fca3d6f55f6"));
	    WebResource webResource =
	        client.resource("https://api.mailgun.net/v3/sandboxf40f3fcb3fab47848df8cee4306f4d10.mailgun.org/messages");
	    MultivaluedMapImpl formData = new MultivaluedMapImpl();
	    formData.add("from", "postmaster@sandboxf40f3fcb3fab47848df8cee4306f4d10.mailgun.org");
	    formData.add("to", to);
	    formData.add("subject", subject);
	    formData.add("text/html", emailBody);
	    ClientResponse c = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
	    System.out.println("Correo enviado a : " +to + " status " +c.getStatusInfo().getStatusCode());
	}
}
