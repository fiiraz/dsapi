package com.daimontech.dsapi.product.controller;

import com.daimontech.dsapi.product.message.request.CategoryAddRequest;
import com.daimontech.dsapi.product.message.request.PackageAddRequest;
import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.service.PackageService;
import com.daimontech.dsapi.product.service.PackageServiceImpl;
import com.daimontech.dsapi.utilities.error.BaseError;
import com.daimontech.dsapi.utilities.helpers.LanguageSwitch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/package")
@Api(value="Package islemleri")
public class PackageController {

    @Autowired
    PackageServiceImpl packageService;

    @Autowired
    BaseError baseError;

    @Autowired
    LanguageSwitch languageSwitch;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/newpackage")
    @ApiOperation(value = "New Package")
    public ResponseEntity<String> newPackage(@Valid @RequestBody PackageAddRequest packageAddRequest){
        languageSwitch.setLang();

        ModelMapper modelMapper = new ModelMapper();
        Packages packages = modelMapper.map(packageAddRequest, Packages.class);

        if(!packageService.addNewPackage(packages)){
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getSexUnsaved()),
                    HttpStatus.BAD_REQUEST);
        }

        if(packageService.addNewPackage(packages)){
            return ResponseEntity.status(HttpStatus.OK).body(baseError.errorMap.get(baseError.getSexSaved()));
        }
        return new ResponseEntity<String>(baseError.errorMap.get(baseError.getUnknownError()),
                HttpStatus.BAD_REQUEST);
    }
}
