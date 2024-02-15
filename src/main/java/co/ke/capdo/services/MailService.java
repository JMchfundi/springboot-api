package co.ke.capdo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import co.ke.capdo.beans.Mail;

@Service
public class MailService {

    @Autowired
    private MailSender mailSender;

    public void sendMail (final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mail.getFrom());
        mailMessage.setTo(mail.getTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getContent());

        mailSender.send(mailMessage);
    }
}
