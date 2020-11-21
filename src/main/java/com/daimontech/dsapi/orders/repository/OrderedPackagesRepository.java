package com.daimontech.dsapi.orders.repository;

import com.daimontech.dsapi.orders.model.Order;
import com.daimontech.dsapi.orders.model.OrderedPackages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedPackagesRepository extends JpaRepository<OrderedPackages, Long> {
    boolean existsById(Long orderedPackagesId);
    OrderedPackages findByOrder(Order order);
}
