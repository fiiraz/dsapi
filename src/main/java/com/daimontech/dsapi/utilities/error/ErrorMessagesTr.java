package com.daimontech.dsapi.utilities.error;

import com.daimontech.dsapi.utilities.constant.ErrorConstant;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessagesTr {
   ErrorConstant errorConstant;

   //getter setter defines
    public String getExistUser() {
        return errorConstant.existUser;
    }

    public String getUnknownError() {
        return errorConstant.unknownError;
    }

    //set map by error messages_tr
    public Map<String, String> errorMap = new HashMap<String, String>();

    public void setErrorMap(Map<String, String> errorMap) {
        errorMap.put(getExistUser(),"kullanici mevcut");
        errorMap.put(getUnknownError(),"bilinmeyen hata");
        this.errorMap = errorMap;
    }

    /*
    bir controllerda hata mesaji donecek diyelim. bir if sartinin icinde mesela.
    currentLanguage = userin dil seciminis et eden kod.
    kullanici dili ne ise ilgili java class indan object instance alinacak(tr ise(switch case kullanilabilir)
       ErrorMessagesTr errormessages)

    return errorMap.get(getExistUser());
     */
}

