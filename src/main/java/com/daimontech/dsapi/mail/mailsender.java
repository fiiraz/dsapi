package com.daimontech.dsapi.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import javax.swing.*;

public class mailsender {
    @Autowired
    private EmailService emailService;

    Mail mail = new Mail();

    public mailsender(String from, String to, String subject, String content) {
        mail.setFrom(from);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setContent(content);
        emailService.sendSimpleMessage(mail);
    }
    //mailsender mailsender = new mailsender("emine@sa.com","aykut@saa.com","hi","geldimi"); mail bu sekilde
    //uygun yerde atilacak.
}
