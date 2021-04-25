package com.daimontech.dsapi.product.controller;

import com.daimontech.dsapi.product.message.request.CategoryAddRequest;
import com.daimontech.dsapi.product.message.response.CategoriesGetAllResponse;
import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.model.Colors;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.service.CategoriesServiceImpl;
import com.daimontech.dsapi.utilities.error.BaseError;
import com.daimontech.dsapi.utilities.helpers.LanguageHelper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
@Api(value="Category islemleri")
public class CategoriesController {

    @Autowired
    CategoriesServiceImpl categoriesService;

    @Autowired
    BaseError baseError;

    @Autowired
    LanguageHelper languageHelper;

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @PostMapping("/newcategory")
    @ApiOperation(value = "New Category")
    public ResponseEntity<String> newCategory(@Valid @RequestBody CategoryAddRequest categoryAddRequest){
        languageHelper.language("tr");
        if(categoriesService.existsByCategoryName(categoryAddRequest.getCategoryName())){
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getexistSex()),
                    HttpStatus.BAD_REQUEST);
        }

        ModelMapper modelMapper = new ModelMapper();
        Categories categories = modelMapper.map(categoryAddRequest, Categories.class);

        if(!categoriesService.addNewCategory(categories)){
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getCategoryCannotSaved()),
                    HttpStatus.BAD_REQUEST);
        }

        if(categoriesService.addNewCategory(categories)){
            return ResponseEntity.status(HttpStatus.OK).body(baseError.errorMap.get(baseError.getSexSaved()));
        }
        return new ResponseEntity<String>(baseError.errorMap.get(baseError.getUnknownError()),
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @DeleteMapping("/deletecategory")
    @ApiOperation(value = "Delete Category")
    public ResponseEntity<String> deleteCategory(@Valid @RequestParam long categoryId) {
        if (categoriesService.existsByCategoryId(categoryId)) {
            Categories categories = categoriesService.getCategoryById(categoryId);
            if (categoriesService.delete(categories))
                return ResponseEntity.ok().body("Category deleted successfully!");
        }
        return new ResponseEntity<String>("Fail -> Category could not be deleted!",
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getcategory/{id}")
    @ApiOperation(value = "Get Category")
    public ResponseEntity<Categories> getCategoryById(@Valid @PathVariable(value = "id") Long categoryId) {
        Categories categories = categoriesService.getCategoryById(categoryId);

        return ResponseEntity.ok().body(categories);

    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getallcategories")
    @ApiOperation(value = "Get All Categories")
    public ResponseEntity<List<Categories>> getAllCategories() {
        List<Categories> categories = categoriesService.getAllCategories();

        return ResponseEntity.ok().body(categories);

    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getonlyallsubcategories")
    @ApiOperation(value = "Get Only All Sub Categories")
    public ResponseEntity<List<Categories>> getOnlyAllSubCategories() {
        List<Categories> categories = categoriesService.getOnlyAllSubCategories();

        return ResponseEntity.ok().body(categories);

    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getchildrencategory/{parentId}")
    @ApiOperation(value = "Get Children Categories By Parent ID")
    public ResponseEntity<List<CategoriesGetAllResponse>> getChildrenCategoriesByParentId(@Valid @PathVariable(value = "parentId") Long parentId) {

        List<Categories> categories = categoriesService.getChildrenCategoriesByParentId(parentId.intValue());
        List<CategoriesGetAllResponse> allCategoriesResponse = new ArrayList<>();
        for (Categories category : categories) {
            CategoriesGetAllResponse categoryResponse = new CategoriesGetAllResponse();
            categoryResponse.setCategory(category);
            List<Categories> subCategories = categoriesService.getChildrenCategoriesByParentId(category.getId().intValue());
            if (!subCategories.isEmpty()) {
                categoryResponse.setSubCategories(subCategories);
            }
            allCategoriesResponse.add(categoryResponse);
        }

        return ResponseEntity.ok().body(allCategoriesResponse);

    }
}
