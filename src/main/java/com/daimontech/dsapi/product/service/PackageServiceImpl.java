package com.daimontech.dsapi.product.service;

import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.model.Properties;
import com.daimontech.dsapi.product.repository.CategoriesRepository;
import com.daimontech.dsapi.product.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
