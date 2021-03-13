package com.daimontech.dsapi.langueages.controller;

import com.daimontech.dsapi.langueages.model.LangueageTable;
import com.daimontech.dsapi.langueages.service.LanguagesServiceImpl;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/languages")
@Api(value="Language islemleri")
public class LanguagesController {

    @Autowired
    LanguagesServiceImpl languagesService;

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @DeleteMapping("/deletelanguage")
    @ApiOperation(value = "Delete Language")
    public ResponseEntity<String> deleteLanguage(@Valid @RequestParam long languageId) {
        if (languagesService.existsById(languageId)) {
            Optional<LangueageTable> langueTable = languagesService.findById(languageId);
            List<User> users = userRepository.findAllByLangueageTable(langueTable.get());
            for (User user : users
            ) {
                LangueageTable langueTableEdit = new LangueageTable();
                langueTableEdit.setActiveLangueage("EN");
                user.setLangueageTable(langueTableEdit);
                userRepository.save(user);
            }
            if (languagesService.delete(langueTable.get()))
                return ResponseEntity.ok().body("Language deleted successfully!");
        }
        return new ResponseEntity<String>("Fail -> Language could not be deleted!",
                HttpStatus.BAD_REQUEST);
    }
}
