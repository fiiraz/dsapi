package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.model.Packages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Packages, Long> {

    Boolean existsById(String packagesId);

    @Query(value = "SELECT * FROM packages",
            countQuery = "SELECT count(*) FROM packages",
            nativeQuery = true)
    Page<Packages> findAllPackages(Pageable pageable);
}
