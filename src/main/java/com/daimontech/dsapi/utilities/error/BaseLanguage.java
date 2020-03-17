package com.daimontech.dsapi.utilities.error;

import com.daimontech.dsapi.utilities.constant.ErrorConstant;

import java.util.HashMap;
import java.util.Map;

public class BaseLanguage {
    ErrorMessagesTr errorMessagesTr;
    ErrorMessagesEn errorMessagesEn;

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
    public Map<String, String> errorMap = new HashMap<String, String>();
}
