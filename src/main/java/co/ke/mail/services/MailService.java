package co.ke.mail.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import co.ke.mail.beans.Mail;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail (final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("no-reply@capdo.org");
        mailMessage.setTo("info@capdo.org");
        mailMessage.setBcc("akidamjaffar@gmail.com");
        mailMessage.setSubject("You Have A New Capdo-form Mail from;- "+mail.getFrom());
        mailMessage.setText(mail.toString());

        mailSender.send(mailMessage);

    }
}
