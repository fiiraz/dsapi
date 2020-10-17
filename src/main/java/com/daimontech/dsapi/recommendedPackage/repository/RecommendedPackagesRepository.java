package com.daimontech.dsapi.recommendedPackage.repository;

import com.daimontech.dsapi.recommendedPackage.model.RecommendedNewPackages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendedPackagesRepository extends JpaRepository<RecommendedNewPackages, Long> {

    Page<RecommendedNewPackages> findAll(Pageable pageable);
}
