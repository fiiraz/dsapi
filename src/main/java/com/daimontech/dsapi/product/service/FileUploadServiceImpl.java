package com.daimontech.dsapi.product.service;

import com.daimontech.dsapi.product.model.Images;
import com.daimontech.dsapi.product.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    FileUploadRepository fileUploadRepository;

    @Override
    public Images uploadToDb(Images images) {
        try {
            return fileUploadRepository.save(images);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Images downloadFile(String id) {
        return fileUploadRepository.findById(id).get();
    }

    public Images getFile(String id) {
        return fileUploadRepository.findById(id).get();
    }

    public Stream<Images> getAllFiles() {
        return fileUploadRepository.findAll().stream();
    }
}
