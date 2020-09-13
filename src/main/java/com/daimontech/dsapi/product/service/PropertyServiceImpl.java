package com.daimontech.dsapi.product.service;

import com.daimontech.dsapi.product.model.Properties;
import com.daimontech.dsapi.product.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyServiceImpl implements PropertyService{

    @Autowired
    PropertyRepository propertyRepository;

    public Boolean existsByPropertyId(Long propertyId){
        return propertyRepository.existsById(propertyId);
    }

    public Properties addNewProperty(Properties properties){
        try{
            return propertyRepository.save(properties);
        } catch(Exception e){
            return null;
        }
    }

}
