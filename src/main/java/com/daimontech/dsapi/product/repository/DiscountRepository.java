package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.model.DiscountPackage;
import com.daimontech.dsapi.product.model.Packages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountPackage, Long> {

    boolean existsById(Long discountId);
    Optional<DiscountPackage> findById(Long discountId);
    List<DiscountPackage> getAllByPackages(Packages packages);
    List<DiscountPackage> findAllByDiscountIsNotNull();
}
