package com.daimontech.dsapi.utilities.helpers;

import org.springframework.stereotype.Service;

@Service
public class LanguageSwitch extends LanguageHelper {

    public void setLang() {
        String currentLanguage = "tr"; //suer dil bilgisi db den cekilecek.
        this.language(currentLanguage);
    }
}
