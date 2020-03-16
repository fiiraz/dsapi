package com.daimontech.dsapi.utilities.error;

import com.daimontech.dsapi.utilities.constant.ErrorConstant;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessagesEn {
    ErrorConstant errorConstant;

    //getter setter defines
    public String getExistUser() {
        return errorConstant.existUser;
    }

    public String getUnknownError() {
        return errorConstant.unknownError;
    }

    //set map by error messages_en
    Map<String, String> errorMap = new HashMap<String, String>();

    public void setErrorMap(Map<String, String> errorMap) {
        errorMap.put(getExistUser(),"user is exist");
        errorMap.put(getUnknownError(),"unknown error");
        this.errorMap = errorMap;
    }
}
