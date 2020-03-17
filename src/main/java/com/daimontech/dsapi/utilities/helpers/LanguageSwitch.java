package com.daimontech.dsapi.utilities.helpers;

public class LanguageSwitch extends LanguageHelper {

    public void setLang() {
        String currentLanguage = "tr"; //suer dil bilgisi db den cekilecek.
        this.language(currentLanguage);
    }
}
