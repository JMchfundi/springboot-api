package co.ke.capdo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import co.ke.capdo.beans.Mail;
import co.ke.capdo.services.MailService;

@RestController
@RequestMapping("/capdo/v1")
public class TechController {
    @Autowired
    private MailService service;

    /*.......................techno-reply post service made some little change on vsCode
     lets check if it works.............................*/
    @RequestMapping(value = "/send_mail", method = RequestMethod.POST)
    public ResponseEntity post_service(@RequestBody Mail mail
            , UriComponentsBuilder builder) {

        service.sendMail(mail);

        return new ResponseEntity(mail, HttpStatus.OK);
    }
}