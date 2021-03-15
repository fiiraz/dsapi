package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.model.ProductRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRateRepository extends JpaRepository<ProductRate, Long> {

    Optional<ProductRate> findByUserIdAndPackages(Long userID, Packages packages);
}
