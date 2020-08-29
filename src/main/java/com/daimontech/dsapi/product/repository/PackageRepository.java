package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.model.Packages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Packages, Long> {

    Boolean existsById(String packagesId);

}
