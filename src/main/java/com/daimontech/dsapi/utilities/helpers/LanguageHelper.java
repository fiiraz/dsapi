package com.daimontech.dsapi.utilities.helpers;

import com.daimontech.dsapi.utilities.error.ErrorMessagesEn;
import com.daimontech.dsapi.utilities.error.ErrorMessagesTr;


public class LanguageHelper {

    ErrorMessagesTr errorMessagesTr;
    ErrorMessagesEn errorMessagesEn;

    public void language(String lang) {
        if (lang == "tr") {
            errorMessagesTr.setErrorMap();
        }

        if (lang == "en") {
            errorMessagesEn.setErrorMap();
        } else
            errorMessagesEn.setErrorMap();
    }
}

