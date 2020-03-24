package com.daimontech.dsapi.repository;

import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    @Query(value = "SELECT status FROM users u WHERE u.username = ?", nativeQuery = true)
    Status findStatusByUsername(String username);
}