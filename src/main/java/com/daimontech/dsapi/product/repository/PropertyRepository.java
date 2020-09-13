package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.model.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Properties, Long> {

    Boolean existsById(String propertyId);

}
