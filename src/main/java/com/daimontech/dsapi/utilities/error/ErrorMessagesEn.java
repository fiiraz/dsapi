package com.daimontech.dsapi.utilities.error;

import com.daimontech.dsapi.utilities.constant.ErrorConstant;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessagesEn extends BaseError {

    public Map<String, String> _errorMap = new HashMap<String, String>();

    public void setErrorMap() {
        _errorMap.put(getexistSex(), "Fail -> Sex Is Exist");
        _errorMap.put(getSexUnsaved(), "Fail -> Sex Is Not Saved!");
        _errorMap.put(getSexSaved(), "Sex Added");
        _errorMap.put(getUnknownError(), "Unknown Error");
        this.errorMap = _errorMap;
    }
}
