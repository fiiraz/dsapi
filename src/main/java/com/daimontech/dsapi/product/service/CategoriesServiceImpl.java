package com.daimontech.dsapi.product.service;

import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.model.Colors;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    CategoriesRepository categoriesRepository;

    public Boolean existsByCategoryName(String categoryName) {
        return categoriesRepository.existsByCategoryName(categoryName);
    }

    public Boolean existsByCategoryId(Long categoryId) {
        return categoriesRepository.existsById(categoryId);
    }

    public Categories getCategoryById(Long categoryId) {
        return categoriesRepository.getById(categoryId);
    }

    public List<Categories> getAllCategories() {
        return categoriesRepository.getAllByCategoryNameIsNotNull();
    }

    public Boolean addNewCategory(Categories categories) {
        try {
            categoriesRepository.save(categories);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean delete(Categories categories) {
        try {
            categoriesRepository.delete(categories);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
