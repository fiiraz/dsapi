package com.daimontech.dsapi.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import javax.swing.*;

public class mailsender {
    @Autowired
    private EmailService emailService;

    Mail mail = new Mail();

    public void setFrom(Mail mail) {
        mail.setFrom("no-reply@melahat.com");
    }

    public void setTo(Mail mail) {
        mail.setTo("melahatmindivanli@gmail.com");
    }

    public void setSubject(Mail mail) {
        mail.setSubject("test");
    }

    public void setContent(Mail mail) {
        mail.setContent("mail test");
    }

    public void sendMail(Mail mail) {
        emailService.sendSimpleMessage(mail);
    }

}
