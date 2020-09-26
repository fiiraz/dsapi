package com.daimontech.dsapi.product.controller;

import com.daimontech.dsapi.product.message.request.CategoryAddRequest;
import com.daimontech.dsapi.product.message.request.ColorAddRequest;
import com.daimontech.dsapi.product.message.request.ColorDeleteRequest;
import com.daimontech.dsapi.product.message.request.PackageDeleteRequest;
import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.model.Colors;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.service.CategoriesServiceImpl;
import com.daimontech.dsapi.product.service.ColorServiceImpl;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/color")
@Api(value = "Color islemleri")
public class ColorsController {

    @Autowired
    ColorServiceImpl colorService;

    @Autowired
    BaseError baseError;

    @Autowired
    LanguageSwitch languageSwitch;

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @PostMapping("/newcolor")
    @ApiOperation(value = "New Color")
    public ResponseEntity<String> newColor(@Valid @RequestBody ColorAddRequest colorAddRequest) {
        languageSwitch.setLang();
        if (colorService.existsByColorCode(colorAddRequest.getColorCode())) {
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getexistSex()),
                    HttpStatus.BAD_REQUEST);
        }

        ModelMapper modelMapper = new ModelMapper();
        Colors colors = modelMapper.map(colorAddRequest, Colors.class);

        if (!colorService.addNewColor(colors)) {
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getSexUnsaved()),
                    HttpStatus.BAD_REQUEST);
        } else if (colorService.addNewColor(colors)) {
            return ResponseEntity.status(HttpStatus.OK).body(baseError.errorMap.get(baseError.getSexSaved()));
        }
        return new ResponseEntity<String>(baseError.errorMap.get(baseError.getUnknownError()),
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @DeleteMapping("/deletecolor")
    @ApiOperation(value = "Delete Color")
    public ResponseEntity<String> deletePackage(@Valid @RequestBody ColorDeleteRequest colorDeleteRequest) {
        if (colorService.existsByColorId(colorDeleteRequest.getColorId())) {
            Colors colors = colorService.getColorsById(colorDeleteRequest.getColorId());
            if (colorService.delete(colors))
                return ResponseEntity.ok().body("Color deleted successfully!");
        }
        return new ResponseEntity<String>("Fail -> Color could not be deleted!",
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/getallcolors")
    @ApiOperation(value = "All Colors")
    public ResponseEntity<List<Colors>> getPackageById() {
        List<Colors> colors = colorService.getAllColors();

        return ResponseEntity.ok().body(colors);

    }
}
