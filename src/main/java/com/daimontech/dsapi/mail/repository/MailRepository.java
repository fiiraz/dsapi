package com.daimontech.dsapi.mail.repository;

import com.daimontech.dsapi.mail.EmailService;
import com.daimontech.dsapi.mail.model.SignInCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<SignInCode, Long> {

}
