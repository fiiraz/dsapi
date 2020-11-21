package com.daimontech.dsapi.product.controller;

import com.daimontech.dsapi.message.response.DiscountGetByUserResponse;
import com.daimontech.dsapi.model.DiscountUser;
import com.daimontech.dsapi.product.message.request.DiscountAddRequest;
import com.daimontech.dsapi.product.message.request.DiscountDeleteAllRequest;
import com.daimontech.dsapi.product.message.request.DiscountDeleteRequest;
import com.daimontech.dsapi.product.message.request.PackageAddRequest;
import com.daimontech.dsapi.product.message.response.DiscountGetByPackageResponse;
import com.daimontech.dsapi.product.model.Colors;
import com.daimontech.dsapi.product.model.DiscountPackage;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.service.DiscountService;
import com.daimontech.dsapi.product.service.DiscountServiceImpl;
import com.daimontech.dsapi.product.service.PackageServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/package/discount")
@Api(value = "Discount islemleri")
public class DiscountController {

    @Autowired
    DiscountServiceImpl discountService;

    @Autowired
    PackageServiceImpl packageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/newdiscount")
    @ApiOperation(value = "New Discount")
    public ResponseEntity<String> newDiscount(@Valid @RequestBody DiscountAddRequest discountAddRequest) {
        if (packageService.existsByPackageId(discountAddRequest.getPackageId())) {
            DiscountPackage discountPackage = new DiscountPackage();
            Packages packages = packageService.getByPackageId(discountAddRequest.getPackageId());
            discountPackage.setDiscount(discountAddRequest.getDiscount());
            discountPackage.setPackages(packages);
            if (discountService.addNewDiscount(discountPackage))
                return ResponseEntity.ok().body("Discount created successfully!");
        }
        return new ResponseEntity<String>("Fail -> Discount could not be created!",
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deletediscount")
    @ApiOperation(value = "Delete Discount")
    public ResponseEntity<String> deleteDiscount(@Valid @RequestBody DiscountDeleteRequest discountDeleteRequest) {
        if (discountService.existsById(discountDeleteRequest.getDiscountId())) {
            DiscountPackage discountPackage = discountService.findById(discountDeleteRequest.getDiscountId());
            if (discountService.delete(discountPackage))
                return ResponseEntity.ok().body("Discount deleted successfully!");
        }
        return new ResponseEntity<String>("Fail -> Discount could not be deleted!",
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deletealldiscount")
    @ApiOperation(value = "Delete All Discount")
    public ResponseEntity<String> deleteAllDiscount(@Valid @RequestBody DiscountDeleteAllRequest discountDeleteAllRequest) {
        try {
            Packages packages = packageService.getByPackageId(discountDeleteAllRequest.getPackageId());
            List<DiscountPackage> discountPackagesList = discountService.getAllByPackage(packages);
            for (DiscountPackage discountPackage :
                    discountPackagesList) {
                discountService.delete(discountPackage);
            }
            return ResponseEntity.ok().body("All Discounts deleted successfully!");
        } catch (Exception e) {
            return new ResponseEntity<String>("Fail -> All Discounts could not be deleted!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/getalldiscounts/{id}")
    @ApiOperation(value = "All Discounts")
    public ResponseEntity<List<DiscountGetByPackageResponse>> getAllDiscountsByPackageId(@Valid @PathVariable(value = "id") Long packageId) {
        Packages packages = packageService.getByPackageId(packageId);
        List<DiscountPackage> discountPackages = discountService.getAllByPackage(packages);
        DiscountGetByPackageResponse discountGetByPackageResponse = new DiscountGetByPackageResponse();
        List<DiscountGetByPackageResponse> discountGetByPackageResponseList = new ArrayList<>();
        for (DiscountPackage discountPackage:
             discountPackages) {
            discountGetByPackageResponse.setDiscount(discountPackage.getDiscount());
            discountGetByPackageResponse.setPackageId(discountPackage.getPackages().getId());
            discountGetByPackageResponseList.add(discountGetByPackageResponse);
        }
        return ResponseEntity.ok().body(discountGetByPackageResponseList);

    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/getalldiscounts")
    @ApiOperation(value = "All Colors")
    public ResponseEntity<List<DiscountGetByPackageResponse>> getAllDiscounts() {
        List<DiscountPackage> discountPackages = discountService.getAllDiscounts();
        List<DiscountGetByPackageResponse> discountGetByPackageResponseList = new ArrayList<>();
        for (DiscountPackage discountPackage:
                discountPackages) {
            DiscountGetByPackageResponse discountGetByPackageResponse = new DiscountGetByPackageResponse();
            discountGetByPackageResponse.setDiscount(discountPackage.getDiscount());
            discountGetByPackageResponse.setPackageId(discountPackage.getPackages().getId());
            discountGetByPackageResponseList.add(discountGetByPackageResponse);
        }
        return ResponseEntity.ok().body(discountGetByPackageResponseList);

    }
}

