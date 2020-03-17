package com.daimontech.dsapi.utilities.error;

import com.daimontech.dsapi.utilities.constant.ErrorConstant;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessagesTr {

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

    //set map by error messages_tr
    public Map<String, String> errorMap = new HashMap<String, String>();

    public void setErrorMap(Map<String, String> errorMap) {
        errorMap.put(getexistSex(),"Fail -> Cinsiyet zaten kayitli!");
        errorMap.put(getSexUnsaved(),"Fail -> Cinsiyet kaydedilemedi!");
        errorMap.put(getSexSaved(),"Cinsiyet Ekllendi");
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

