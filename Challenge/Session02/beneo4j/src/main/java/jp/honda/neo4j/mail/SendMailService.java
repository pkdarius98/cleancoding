package jp.honda.neo4j.mail;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
@Service
public class SendMailService {

    /**
     Outgoing Mail (SMTP) Server
     requires TLS or SSL: smtp.gmail.com (use authentication)
     Use Authentication: Yes
     Port for TLS/STARTTLS: 587
     */
    public void handleSendMail(String toEmail, List<String> partNames) {
        final String fromEmail = "example@gmail.com"; //requires valid gmail id
        final String password = ""; // correct password for gmail id

        System.out.println("Send Mail Start");
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

        String body = "Part Name: " + String.join(", ", partNames);

        EmailUtil.sendEmail(session, toEmail,"Notify impact", body);

    }


}
