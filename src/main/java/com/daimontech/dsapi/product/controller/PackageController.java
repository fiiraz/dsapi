package com.daimontech.dsapi.product.controller;

import com.daimontech.dsapi.message.request.VerifyUserForm;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.product.message.request.CategoryAddRequest;
import com.daimontech.dsapi.product.message.request.PackageAddRequest;
import com.daimontech.dsapi.product.message.request.PackageDeleteRequest;
import com.daimontech.dsapi.product.message.response.PackagePaginationResponse;
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
import org.springframework.data.domain.Page;
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

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/page/{pageNo}/{sortingValue}")
    @ApiOperation(value = "Package User")
    public ResponseEntity<List<PackagePaginationResponse>> getPackagesPaginated(@Valid @PathVariable(value = "pageNo") int pageNo,
    @PathVariable(value = "sortingValue", required = false) String sortingValue) {
        int pageSize = 2;
        Page<Packages> page = packageService.findPaginated(pageNo, pageSize, sortingValue);
        List<Packages> listPackages = page.getContent();
        List<PackagePaginationResponse> pagedPackages = new ArrayList<>();
        for(Packages packages : listPackages){
            PackagePaginationResponse packagePaginationResponse = new PackagePaginationResponse();
            packagePaginationResponse.setProductCode(packages.getProductCode());
            packagePaginationResponse.setDescription(packages.getDescription());
            packagePaginationResponse.setProperties(packages.getProperties());
            packagePaginationResponse.setId(packages.getId());
            packagePaginationResponse.setCategoryId(packages.getCategories().getId());
            packagePaginationResponse.setCategoryName(packages.getCategories().getCategoryName());
            packagePaginationResponse.setCategoryParent(packages.getCategories().getParent());
            packagePaginationResponse.setColorsList(packages.getColors());
            pagedPackages.add(packagePaginationResponse);
        }
        if (!page.isEmpty()) {
            return ResponseEntity.ok().body(pagedPackages);
        }
        return new ResponseEntity<>(
                HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/getpackage/{id}")
    @ApiOperation(value = "Package User")
    public ResponseEntity<Optional<Packages>> getPackageById(@Valid @PathVariable(value = "id") Long packageId) {
        Optional<Packages> packages = packageService.findOneByPackageId(packageId);

            return ResponseEntity.ok().body(packages);

    }
}
