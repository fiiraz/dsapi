package com.daimontech.dsapi.orders.repository;

import com.daimontech.dsapi.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findById(Long orderId);
    boolean existsByID(Long orderId);
}
