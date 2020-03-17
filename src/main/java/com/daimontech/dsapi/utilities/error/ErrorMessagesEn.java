package com.daimontech.dsapi.utilities.error;

import com.daimontech.dsapi.utilities.constant.ErrorConstant;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessagesEn {

    //getter setter defines
    public String getExistUser() {
        return ErrorConstant.existUser;
    }

    public String getexistSex() {
        return ErrorConstant.existSex;
    }

    public String getSexUnsaved() {
        return ErrorConstant.SexUnsaved;
    }

    public String getSexSaved() {
        return ErrorConstant.SexSaved;
    }

    public String getUnknownError() {
        return ErrorConstant.unknownError;
    }
    //set map by error messages_en
    Map<String, String> errorMap = new HashMap<String, String>();

    public void setErrorMap(Map<String, String> errorMap) {
        errorMap.put(getExistUser(),"user is exist");
        errorMap.put(getUnknownError(),"unknown error");
        this.errorMap = errorMap;
    }
}
