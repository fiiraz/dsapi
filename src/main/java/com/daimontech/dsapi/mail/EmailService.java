package com.daimontech.dsapi.mail;

import com.daimontech.dsapi.mail.model.SignInCode;
import com.daimontech.dsapi.mail.service.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    MailServiceImpl mailServiceImp = new MailServiceImpl();
    SignInCode signInCode = new SignInCode();

    public void sendSimpleMessage(final Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent() + mail.getKey);
        message.setTo(mail.getTo());
        message.setFrom(mail.getFrom());

        emailSender.send(message);
    }

}
