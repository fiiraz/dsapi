package com.daimontech.dsapi.repository;

import com.daimontech.dsapi.langueages.model.LangueageTable;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.model.enums.Status;
import com.daimontech.dsapi.product.model.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    @Query(value = "SELECT status FROM users u WHERE u.username = ?", nativeQuery = true)
    Status findStatusByUsername(String username);
    List<User> findAllByUsernameIsNotNull();
    List<User> findAllByLangueageTable(LangueageTable langueageTable);
    Optional<User> findById(Long userID);
}