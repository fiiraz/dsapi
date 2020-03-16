package com.daimontech.dsapi.product.service;

import com.daimontech.dsapi.product.message.request.CategoryAddRequest;
import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriesServiceImpl implements CategoriesService{

    @Autowired
    CategoriesRepository categoriesRepository;

    public Boolean existsByCategoryName(String categoryName){
        return categoriesRepository.existsByCategoryName(categoryName);
    }

    public Boolean addNewCategory(Categories categories){
        try{
            categoriesRepository.save(categories);
            return true;
        } catch(Exception e){
            return false;
        }
    }

}
