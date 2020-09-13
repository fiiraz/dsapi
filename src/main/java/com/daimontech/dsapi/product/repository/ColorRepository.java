package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.model.Colors;
import com.daimontech.dsapi.product.model.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Colors, Long> {

    Boolean existsById(String propertyId);
    Colors getById(Long colorId);
}
