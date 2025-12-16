package utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    private static final String FROM_EMAIL = "marathon.system@gmail.com";
    private static final String PASSWORD = "APP_PASSWORD_HERE";

    public static void sendRegistrationMail(
            String toEmail,
            String runnerName,
            String eventName,
            String eventDate,
            String location
    ) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject("Marathon Registration Confirmation");

            String content =
                    "<h3>Hello " + runnerName + ",</h3>"
                  + "<p>You have successfully registered for:</p>"
                  + "<ul>"
                  + "<li><b>Event:</b> " + eventName + "</li>"
                  + "<li><b>Date:</b> " + eventDate + "</li>"
                  + "<li><b>Location:</b> " + location + "</li>"
                  + "</ul>"
                  + "<p>Thank you for joining our marathon!</p>"
                  + "<p><i>Marathon Management System</i></p>";

            message.setContent(content, "text/html; charset=UTF-8");

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
