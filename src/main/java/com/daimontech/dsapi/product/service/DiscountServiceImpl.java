package com.daimontech.dsapi.product.service;

import com.daimontech.dsapi.product.model.DiscountPackage;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    DiscountRepository discountRepository;

    public DiscountPackage findById(Long discountId){
        return discountRepository.findById(discountId).get();
    }

    public boolean existsById(Long discountId){
        return discountRepository.existsById(discountId);
    }

    public List<DiscountPackage> getAllByPackage(Packages packages){
        return discountRepository.getAllByPackages(packages);
    }

    public Boolean delete(DiscountPackage discountPackage) {
        try {
            discountRepository.delete(discountPackage);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Boolean addNewDiscount(DiscountPackage discountPackage){
        try{
            discountRepository.save(discountPackage);
            return true;
        } catch(Exception e){
            return false;
        }
    }
}
