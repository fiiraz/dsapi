package com.daimontech.dsapi.product.controller;

import com.daimontech.dsapi.message.response.JwtResponse;
import com.daimontech.dsapi.product.message.request.CategoryAddRequest;
import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.service.CategoriesServiceImpl;
import com.daimontech.dsapi.utilities.error.ErrorMessagesTr;
import com.daimontech.dsapi.utilities.helpers.LanguageHelper;
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

    ErrorMessagesTr errorMessagesTr;

    LanguageHelper languageHelper;
    //app acilisinda bir defa user language cekilip baska class dan cagirilacak.
    String lang = "tr";

    public void setLang() {
       errorMessagesTr = (ErrorMessagesTr) languageHelper.language(lang);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/newcategory")
    @ApiOperation(value = "New Category")
    public ResponseEntity<String> newCategory(@Valid @RequestBody CategoryAddRequest categoryAddRequest){
        setLang(); //normalde burda cagrilmayacak api baslangicinda bir kez setlenecek.

        if(categoriesService.existsByCategoryName(categoryAddRequest.getCategoryName())){
            return new ResponseEntity<String>(errorMessagesTr.errorMap.get(errorMessagesTr.getexistSex()),
                    HttpStatus.BAD_REQUEST);
        }

        ModelMapper modelMapper = new ModelMapper();
        Categories categories = modelMapper.map(categoryAddRequest, Categories.class);

        if(!categoriesService.addNewCategory(categories)){
            return new ResponseEntity<String>(errorMessagesTr.errorMap.get(errorMessagesTr.getSexUnsaved()),
                    HttpStatus.BAD_REQUEST);
        }

        if(categoriesService.addNewCategory(categories)){
            return ResponseEntity.status(HttpStatus.OK).body(errorMessagesTr.errorMap.get(errorMessagesTr.getSexSaved()));
        }
        return new ResponseEntity<String>(errorMessagesTr.errorMap.get(errorMessagesTr.getUnknownError()),
                HttpStatus.BAD_REQUEST);
    }
}
