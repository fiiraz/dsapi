package com.daimontech.dsapi.product.repository;

import com.daimontech.dsapi.product.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<Images,String> {
}
