package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Boolean existsByCategoryName(String categoryName);
    boolean existsById(Long categoryId);
    Categories getById(Long categoryId);
    List<Categories> getAllByCategoryNameIsNotNull();

}
