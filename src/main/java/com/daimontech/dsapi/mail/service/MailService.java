package com.daimontech.dsapi.mail.service;

import com.daimontech.dsapi.mail.model.SignInCode;

public interface MailService {

    public Boolean AddCode(SignInCode signInCode);
    public Boolean AddNotification();

}
