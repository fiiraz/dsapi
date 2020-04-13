package com.daimontech.dsapi.utilities.helpers;

import com.daimontech.dsapi.utilities.error.ErrorMessagesEn;
import com.daimontech.dsapi.utilities.error.ErrorMessagesTr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageHelper {

    @Autowired
    ErrorMessagesTr errorMessagesTr;

    @Autowired
    ErrorMessagesEn errorMessagesEn;

    public void language(String lang) {
        if (lang.equals("tr")) {
            errorMessagesTr.setErrorMap();
        }

        if (lang.equals("en")) {
            errorMessagesEn.setErrorMap();
        } else
            errorMessagesEn.setErrorMap();
    }
}

