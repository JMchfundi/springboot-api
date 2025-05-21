package co.ke.mail.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import co.ke.finsis.entity.OfficerRegistration;
import co.ke.finsis.repository.OfficerRegistrationRepository;
import co.ke.mail.beans.Mail;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private OfficerRegistrationRepository repository;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendMail(final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailFrom);
        mailMessage.setTo("info@capdo.org");
        mailMessage.setBcc("akidamjaffar@gmail.com");
        mailMessage.setSubject("You Have A New Capdo-form Mail from;- " + mail.getFrom());
        mailMessage.setText(mail.toString());

        mailSender.send(mailMessage);

    }

    // ✅ New method for sending credentials to an officer
    @Async
    public void sendCredentials(Long officerId) {
        OfficerRegistration officer = repository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found"));

        String body = """
                Dear %s,

                Your officer account has been created.

                Username: %s
                Password: %s

                Please login and change your password after your first login.

                Regards,
                TRES Team
                """.formatted(officer.getFullName(), officer.getSystemUser().getUsername(), "Password@2906");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(officer.getSystemUser().getEmail());
        mailMessage.setBcc("akidamjaffar@gmail.com");
        mailMessage.setSubject("Your Officer Account Has Been Created");
        mailMessage.setText(body);

        mailSender.send(mailMessage);
    }

}
