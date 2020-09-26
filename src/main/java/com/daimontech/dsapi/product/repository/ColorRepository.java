package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.model.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Colors, Long> {

    Boolean existsById(String propertyId);

    Boolean existsByColorCode(String colorCode);

    Colors getById(Long colorId);

    List<Colors> findAllByColorCodeNotNull();
}
