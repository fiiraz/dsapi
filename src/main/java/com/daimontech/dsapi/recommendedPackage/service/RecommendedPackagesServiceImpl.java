package com.daimontech.dsapi.recommendedPackage.service;

import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.recommendedPackage.model.RecommendedNewPackages;
import com.daimontech.dsapi.recommendedPackage.model.RecommendedNewPackagesRate;
import com.daimontech.dsapi.recommendedPackage.repository.RecommendedPackagesRateRepository;
import com.daimontech.dsapi.recommendedPackage.repository.RecommendedPackagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class RecommendedPackagesServiceImpl implements RecommendedPackagesService {

    @Autowired
    RecommendedPackagesRepository recommendedPackagesRepository;

    @Autowired
    RecommendedPackagesRateRepository recommendedPackagesRateRepository;

    public Optional<RecommendedNewPackages> findById(Long id) {
        return recommendedPackagesRepository.findById(id);
    }

    public Boolean addNewRecommendedPackage(RecommendedNewPackages recommendedNewPackages) {
        try {
            recommendedPackagesRepository.save(recommendedNewPackages);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean addNewRateForRecommendedPackace(RecommendedNewPackagesRate recommendedNewPackagesRate) {
        try {
            recommendedPackagesRateRepository.save(recommendedNewPackagesRate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isRatedBeforeByUser(User user, RecommendedNewPackages recommendedNewPackages) {
        return recommendedPackagesRateRepository.existsRecommendedNewPackagesRateByUserAndAndRecommendedNewPackages(user, recommendedNewPackages);
    }

    public void updateRate(String comment, int rate, Date ratedDate, Long userId, Long recommendedPackageId){
        recommendedPackagesRateRepository.updateRate(comment, rate, ratedDate, userId, recommendedPackageId);
    }

    public boolean updateRecommendedPackage(RecommendedNewPackages recommendedNewPackages){
        try {
            recommendedPackagesRepository.save(recommendedNewPackages);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<RecommendedNewPackages> findOneByPackageId(Long recommendedPackageId) {
        return recommendedPackagesRepository.findById(recommendedPackageId);
    }

    public Page<RecommendedNewPackages> findPaginated(int pageNo, int pageSize, String sortingValue) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortingValue).descending());
        return this.recommendedPackagesRepository.findAll(pageable);
    }

}
