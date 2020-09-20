package com.daimontech.dsapi.product.controller;

import com.daimontech.dsapi.product.message.request.CategoryAddRequest;
import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.service.CategoriesServiceImpl;
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
@RequestMapping("/api/category")
@Api(value="Category islemleri")
public class CategoriesController {

    @Autowired
    CategoriesServiceImpl categoriesService;

    @Autowired
    BaseError baseError;

    @Autowired
    LanguageSwitch languageSwitch;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/newcategory")
    @ApiOperation(value = "New Category")
    public ResponseEntity<String> newCategory(@Valid @RequestBody CategoryAddRequest categoryAddRequest){
        languageSwitch.setLang();
        if(categoriesService.existsByCategoryName(categoryAddRequest.getCategoryName())){
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getexistSex()),
                    HttpStatus.BAD_REQUEST);
        }

        ModelMapper modelMapper = new ModelMapper();
        Categories categories = modelMapper.map(categoryAddRequest, Categories.class);

        if(!categoriesService.addNewCategory(categories)){
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getSexUnsaved()),
                    HttpStatus.BAD_REQUEST);
        }

        if(categoriesService.addNewCategory(categories)){
            return ResponseEntity.status(HttpStatus.OK).body(baseError.errorMap.get(baseError.getSexSaved()));
        }
        return new ResponseEntity<String>(baseError.errorMap.get(baseError.getUnknownError()),
                HttpStatus.BAD_REQUEST);
    }
}
