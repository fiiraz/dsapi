package com.daimontech.dsapi.orders.repository;

import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.orders.model.Order;
import com.daimontech.dsapi.product.model.Packages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findById(Long orderId);
    List<Order> getAllByUserMadeOrder(User user);

    @Query(value = "SELECT * FROM orders WHERE admin_order_note LIKE %?1% OR amount LIKE %?1% OR order_note LIKE %?1%" +
            " OR status LIKE %?1%", nativeQuery = true)
    Page<Order> findAll(String title, Pageable pageable);
}
