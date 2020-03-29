package com.daimontech.dsapi.mail.service;

import com.daimontech.dsapi.mail.model.SignInCode;
import com.daimontech.dsapi.mail.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    MailRepository mailRepository;

    public Boolean AddCode(SignInCode signInCode) {

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
