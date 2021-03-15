package com.daimontech.dsapi.product.service;

import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.model.ProductRate;
import com.daimontech.dsapi.product.repository.ProductRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductRateServiceImpl implements ProductRateService{

    @Autowired
    ProductRateRepository productRateRepository;

    public Boolean delete(ProductRate productRate) {
        try {
            productRateRepository.delete(productRate);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Boolean addNewProductRate(ProductRate productRate) {
        try {
            productRateRepository.save(productRate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<ProductRate> findOneByPackageId(Long productRateID) {
        return productRateRepository.findById(productRateID);
    }

    public Optional<ProductRate> findByUserIdAndPackages(Long userID, Packages packages) {
        return productRateRepository.findByUserIdAndPackages(userID, packages);
    }
}
