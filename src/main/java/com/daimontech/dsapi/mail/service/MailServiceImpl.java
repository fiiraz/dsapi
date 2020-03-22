package com.daimontech.dsapi.mail.service;

import com.daimontech.dsapi.mail.model.SignInCode;
import com.daimontech.dsapi.mail.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class MailServiceImpl implements MailService {

    public Boolean AddCode(SignInCode signInCode) {

        @Autowired
        MailRepository mailRepository;

        try {
            mailRepository.save(signInCode);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public Boolean AddNotification() {
        return null;
    }
}
