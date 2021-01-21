package com.daimontech.dsapi.orders.repository;

import com.daimontech.dsapi.orders.model.Order;
import com.daimontech.dsapi.orders.model.OrderedPackages;
import com.daimontech.dsapi.product.model.Packages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderedPackagesRepository extends JpaRepository<OrderedPackages, Long> {
    boolean existsById(Long orderedPackagesId);
    OrderedPackages findByOrder(Order order);
    OrderedPackages findByOrderedPackageId(Long orderedPackageIDId);
    Optional<OrderedPackages> findById(Long orderedPackagesId);
    List<OrderedPackages> findAllByOrderId(Long orderId);
    Optional<OrderedPackages> findByOrderIdAndOrderedPackage(Long orderId, Packages orderedPackage);
    @Query(value = "SELECT package_id FROM oredered_packages WHERE id = ?",
            nativeQuery = true)
    Long getPackageIdById(Long orderedPackagesID);
}
