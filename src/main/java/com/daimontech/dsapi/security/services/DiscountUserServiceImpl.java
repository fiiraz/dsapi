package com.daimontech.dsapi.security.services;

import com.daimontech.dsapi.model.DiscountUser;
import com.daimontech.dsapi.repository.DiscountUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountUserServiceImpl implements DiscountUserService {

    @Autowired
    DiscountUserRepository discountUserRepository;

    public Boolean addNewDiscount(DiscountUser discountUser){
        try{
            discountUserRepository.save(discountUser);
            return true;
        } catch(Exception e){
            return false;
        }
    }
}
