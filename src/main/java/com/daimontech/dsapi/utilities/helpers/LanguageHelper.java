package com.daimontech.dsapi.utilities.helpers;

import com.daimontech.dsapi.utilities.error.ErrorMessagesEn;
import com.daimontech.dsapi.utilities.error.ErrorMessagesTr;


public class LanguageHelper {

    ErrorMessagesTr errorMessagesTr;
    ErrorMessagesEn errorMessagesEn;

    public Object language(String lang) {
        if (lang == "tr") {
                return errorMessagesTr;
        }

        if (lang == "en"){
            return errorMessagesEn;
        }
        else
            return errorMessagesEn;
    }
}

