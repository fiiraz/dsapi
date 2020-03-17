package com.daimontech.dsapi.utilities.error;

import com.daimontech.dsapi.utilities.constant.ErrorConstant;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessagesTr extends BaseError {

    public Map<String, String> _errorMap = new HashMap<String, String>();

    public void setErrorMap() {
        _errorMap.put(getexistSex(), "Fail -> Cinsiyet zaten kayitli!");
        _errorMap.put(getSexUnsaved(), "Fail -> Cinsiyet kaydedilemedi!");
        _errorMap.put(getSexSaved(), "Cinsiyet Ekllendi");
        _errorMap.put(getUnknownError(), "bilinmeyen hata");
        this.errorMap = _errorMap;
    }
}

