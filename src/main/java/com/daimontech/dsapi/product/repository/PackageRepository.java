package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.message.response.PackagePaginationResponse;
import com.daimontech.dsapi.product.model.Packages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Packages, Long> {

    boolean existsById(Long packagesId);

    /*@Query(value = "SELECT * FROM packages pk INNER JOIN properties pp ON pk.property_id = pp.id",
            countQuery = "SELECT count(*) FROM packages pk INNER JOIN properties pp ON pk.property_id = pp.id",
            nativeQuery = true)*/
//    Page<PackagePaginationResponse> findAllPackages(Pageable pageable);

    @Query(value = "SELECT * FROM packages WHERE LOWER(title) LIKE LOWER(concat('%', ?1,'%')) OR LOWER(asorti_code) LIKE LOWER(concat('%', ?1,'%')) OR LOWER(description) LIKE LOWER(concat('%', ?1,'%'))" +
            " OR LOWER(pattern_code) LIKE LOWER(concat('%', ?1,'%')) OR product_code = ?1 OR price = 1 OR size_min = ?1 OR size_max = ?1 AND for_rate_only = false", nativeQuery = true)
    Page<Packages> findAll(String title, Pageable pageable);

    @Query(value = "SELECT * FROM packages WHERE LOWER(title) LIKE LOWER(concat('%', ?1,'%')) OR LOWER(asorti_code) LIKE LOWER(concat('%', ?1,'%')) OR LOWER(description) LIKE LOWER(concat('%', ?1,'%'))  " +
            " OR LOWER(pattern_code) LIKE LOWER(concat('%', ?1,'%')) OR LOWER(product_code) LIKE LOWER(concat('%', ?1,'%')) OR price = ?1 OR size_min = ?1 OR size_max = ?1" +
            " AND for_rate_only = true AND rate_allowed = true", nativeQuery = true)
    Page<Packages> findAllForRateOnly(String title, Pageable pageable);


}
