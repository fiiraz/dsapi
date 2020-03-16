package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Boolean existsByCategoryName(String categoryName);

}
