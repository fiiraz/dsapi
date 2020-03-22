package com.daimontech.dsapi.mail.repository;

import com.daimontech.dsapi.mail.EmailService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<EmailService, Long> {

}
