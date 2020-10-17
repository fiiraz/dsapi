package com.daimontech.dsapi.recommendedPackage.repository;

import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.recommendedPackage.model.RecommendedNewPackages;
import com.daimontech.dsapi.recommendedPackage.model.RecommendedNewPackagesRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Repository
public interface RecommendedPackagesRateRepository extends JpaRepository<RecommendedNewPackagesRate, Long> {
    Boolean existsRecommendedNewPackagesRateByUserAndAndRecommendedNewPackages(User user, RecommendedNewPackages recommendedNewPackages);

    @Transactional
    @Modifying
    @Query(value = "UPDATE recommended_new_packages_rate SET comment = ?1, rate = ?2, rated_date = ?3 WHERE user_id = ?4 AND recommended_new_packages_id = ?5",
            nativeQuery = true)
    void updateRate(String comment, int rate, Date ratedDate, Long userId, Long recommendedPackageId);
}
