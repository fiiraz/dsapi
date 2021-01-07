package com.daimontech.dsapi.product.service;

import com.daimontech.dsapi.product.model.Images;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    public Images uploadToDb(Images images);
    public Images downloadFile(String id);
}
