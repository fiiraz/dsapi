package com.daimontech.dsapi.product.service;

import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.model.Properties;
import com.daimontech.dsapi.product.repository.CategoriesRepository;
import com.daimontech.dsapi.product.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PackageServiceImpl implements PackageService {

    @Autowired
    PackageRepository packageRepository;

    public Boolean existsByPackageId(Long packageId) {
        return packageRepository.existsById(packageId);
    }

    public Packages getByPackageId(Long packageId) {
        return packageRepository.getOne(packageId);
    }

    public Optional<Packages> findOneByPackageId(Long packageId) {
        return packageRepository.findById(packageId);
    }

    public Boolean delete(Packages packages) {
         try {
             packageRepository.delete(packages);
             return true;
         } catch (Exception e){
             return false;
         }
    }

    public Boolean addNewPackage(Packages packages) {
        try {
            packageRepository.save(packages);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Page<Packages> findPaginated(int pageNo, int pageSize, String sortingValue) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortingValue).descending());
        return this.packageRepository.findAllPackages(pageable);
    }
}
