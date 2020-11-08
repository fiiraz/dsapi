package com.daimontech.dsapi.repository;

import com.daimontech.dsapi.model.DiscountUser;
import com.daimontech.dsapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountUserRepository extends JpaRepository<DiscountUser, Long> {

    Boolean existsByUser(User user);
}
