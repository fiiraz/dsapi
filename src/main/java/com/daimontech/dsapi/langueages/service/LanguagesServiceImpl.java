package com.daimontech.dsapi.langueages.service;

import com.daimontech.dsapi.langueages.Repository.LanguageRepository;
import com.daimontech.dsapi.langueages.model.LangueageTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LanguagesServiceImpl implements LanguagesService {

    @Autowired
    LanguageRepository languageRepository;

    public Boolean existsById(Long languageId) {
        return languageRepository.existsById(languageId);
    }

    public Optional<LangueageTable> findById(Long languageId) {
        return languageRepository.findById(languageId);
    }

    public Boolean delete(LangueageTable langueTable) {
        try {
            languageRepository.delete(langueTable);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
