package com.daimontech.dsapi.mail;

import com.daimontech.dsapi.mail.model.SignInCode;
import com.daimontech.dsapi.mail.service.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import javax.swing.*;

public class Mailsender {
    @Autowired
    private EmailService emailService;

    Mail mail = new Mail();
    SignInCode signInCode = new SignInCode();

    public Mailsender(String from, String to, String subject, String content, String code, int type) {
        mail.setFrom(from);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setContent(content);
        mail.setType(type);
        signInCode.setCode(code);
        emailService.sendSimpleMessage(mail);

    }
    //Mailsender mailsender = new mailsender("emine@sa.com","aykut@saa.com","hi","geldimi"); mail bu sekilde
    //uygun yerde atilacak.
}
