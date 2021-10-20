package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUploadRepository extends JpaRepository<Images,String> {
    List<Images> getAllByPackageId(Long id);
}
