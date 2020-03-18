package com.daimontech.dsapi.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import javax.swing.*;

public class Mailsender {
    @Autowired
    private EmailService emailService;

    Mail mail = new Mail();

    public Mailsender(String from, String to, String subject, String content) {
        mail.setFrom(from);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setContent(content);
        emailService.sendSimpleMessage(mail);
    }
    //Mailsender mailsender = new mailsender("emine@sa.com","aykut@saa.com","hi","geldimi"); mail bu sekilde
    //uygun yerde atilacak.
}
