package co.ke.mail.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import co.ke.mail.beans.Mail;
import jakarta.annotation.PostConstruct;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String mailFrom;


  public void sendMail (final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailFrom);
        mailMessage.setTo("info@capdo.org");
        mailMessage.setBcc("akidamjaffar@gmail.com");
        mailMessage.setSubject("You Have A New Capdo-form Mail from;- "+mail.getFrom());
        mailMessage.setText(mail.toString());

        mailSender.send(mailMessage);

    }

    @PostConstruct
public void testEmailSend() {
    try {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jakida@tucode.co.ke");
        message.setTo("JMchfundi@gmail.com");
        message.setSubject("SMTP Test from Spring Boot");
        message.setText("Hello! This is a test email from your Spring Boot app using port 587 and STARTTLS.");

        mailSender.send(message);

        System.out.println("✅ Test email sent successfully!");
    } catch (Exception e) {
        System.err.println("❌ Test email failed: " + e.getMessage());
        e.printStackTrace();
    }
}

       // ✅ New method for sending credentials to an officer
    public void sendCredentials(String recipientEmail, String officerName, String username, String password) {
        String body = """
                Dear %s,

                Your officer account has been created.

                Username: %s
                Password: %s

                Please login and change your password after your first login.

                Regards,
                TRES Team
                """.formatted(officerName, username, password);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(mailFrom);
                mailMessage.setTo(recipientEmail);
                mailMessage.setBcc("akidamjaffar@gmail.com");
                mailMessage.setSubject("Your Officer Account Has Been Created");
                mailMessage.setText(body);

        mailSender.send(mailMessage);
    }

}
