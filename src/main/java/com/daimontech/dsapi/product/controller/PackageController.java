package com.daimontech.dsapi.product.controller;

import com.daimontech.dsapi.message.request.VerifyUserForm;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.product.message.request.CategoryAddRequest;
import com.daimontech.dsapi.product.message.request.PackageAddRequest;
import com.daimontech.dsapi.product.message.request.PackageDeleteRequest;
import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.model.Colors;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.model.Properties;
import com.daimontech.dsapi.product.service.*;
import com.daimontech.dsapi.utilities.error.BaseError;
import com.daimontech.dsapi.utilities.helpers.LanguageSwitch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/package")
@Api(value = "Package islemleri")
public class PackageController {

    @Autowired
    PropertyServiceImpl propertyService;

    @Autowired
    PackageServiceImpl packageService;

    @Autowired
    CategoriesServiceImpl categoriesService;

    @Autowired
    ColorServiceImpl colorService;

    @Autowired
    BaseError baseError;

    @Autowired
    LanguageSwitch languageSwitch;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/newpackage")
    @ApiOperation(value = "New Package")
    public ResponseEntity<String> newPackage(@Valid @RequestBody PackageAddRequest packageAddRequest) {
        languageSwitch.setLang();

        ModelMapper modelMapper = new ModelMapper();
        Properties properties = modelMapper.map(packageAddRequest.getPropertyAddRequest(), Properties.class);
/*        if(!propertyService.addNewProperty(properties)){
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getSexUnsaved()),
                    HttpStatus.BAD_REQUEST);
        }*/
        Properties newProperty = propertyService.addNewProperty(properties);
        if (newProperty != null) {
            Packages packages = new Packages();
            packages.setProperties(newProperty);
            packages.setDescription(packageAddRequest.getDescription());
            packages.setCategories(categoriesService.getCategoryById(packageAddRequest.getCategoryId()));
            packages.setProductCode(packageAddRequest.getProductCode());
            Set<Colors> colorList = new HashSet<>();
            for (Long color : packageAddRequest.getColorId()
            ) {
                colorList.add(colorService.getColorsById(color));
            }
            packages.setColors(colorList);
            if (!packageService.addNewPackage(packages)) {
                return new ResponseEntity<String>("Fail -> Package could not added!",
                        HttpStatus.BAD_REQUEST);
            } else {
                return ResponseEntity.ok().body("Package added successfully!");
            }
        }
        return new ResponseEntity<String>(baseError.errorMap.get(baseError.getUnknownError()),
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @DeleteMapping("/deletepackage")
    @ApiOperation(value = "Package User")
    public ResponseEntity<String> deletePackage(@Valid @RequestBody PackageDeleteRequest packageDeleteRequest) {
        if (packageService.existsByPackageId(packageDeleteRequest.getPackageId())) {
            Packages packages = packageService.getByPackageId(packageDeleteRequest.getPackageId());
            if (packageService.delete(packages))
                return ResponseEntity.ok().body("Package deleted successfully!");
        }
        return new ResponseEntity<String>("Fail -> Package could not be deleted!",
                HttpStatus.BAD_REQUEST);
    }
}
