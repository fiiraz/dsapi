package com.daimontech.dsapi.langueages.Repository;

import com.daimontech.dsapi.langueages.model.LangueageTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<LangueageTable, Long> {
    LangueageTable findByActiveLangueage(String language);
}
