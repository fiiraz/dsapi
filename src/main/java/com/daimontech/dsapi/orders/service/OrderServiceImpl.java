package com.daimontech.dsapi.orders.service;

import com.daimontech.dsapi.model.DiscountUser;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.orders.model.Order;
import com.daimontech.dsapi.orders.model.OrderedPackages;
import com.daimontech.dsapi.orders.repository.OrderRepository;
import com.daimontech.dsapi.orders.repository.OrderedPackagesRepository;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.recommendedPackage.model.RecommendedNewPackages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderedPackagesRepository orderedPackagesRepository;

    public Boolean createNewOrder(Order order) {
        try {
            orderRepository.save(order);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean createNewOrderPackage(OrderedPackages orderedPackages) {
        try {
            orderedPackagesRepository.save(orderedPackages);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean delete(Order order) {
        try {
            orderRepository.delete(order);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean deleteOrderedPackages(OrderedPackages order) {
        try {
            orderedPackagesRepository.delete(order);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateOrder(Order order) {
        try {
            orderRepository.save(order);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateOrderPackage(OrderedPackages orderedPackages) {
        try {
            orderedPackagesRepository.save(orderedPackages);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).get();
    }

//    public OrderedPackages findByOrder(Long orderedPackageID) {
//        return orderedPackagesRepository.findByOrderId(orderedPackageID);
//    }

    public OrderedPackages findByOrderedPackagesId(Long orderedPackagesId) {
        return orderedPackagesRepository.findById(orderedPackagesId).get();
    }

    public OrderedPackages findByOrderIdAndOrderedPackage(Long orderId, Packages orderedPackage){
        return orderedPackagesRepository.findByOrderIdAndOrderedPackage(orderId, orderedPackage).get();
    }

    public boolean existstById(Long orderedPackagesId) {
        return orderedPackagesRepository.existsById(orderedPackagesId);
    }

    public boolean existstByOrderId(Long orderId) {
        return orderRepository.existsById(orderId);
    }

    public List<Order> getAllByUser(User user) {
        return orderRepository.getAllByUserMadeOrder(user);
    }

    public List<OrderedPackages> getAllOrderedPackagesByOrder(Long orderId) {
        return orderedPackagesRepository.findAllByOrderId(orderId);
    }

    public Long getPackagesIdByOrderedPackagesId(Long orderedPackageID) {
        return orderedPackagesRepository.getPackageIdById(orderedPackageID);
    }

    public Page<Order> findPaginated(String value, Pageable pageable) {
        return this.orderRepository.findAll(value, pageable);
    }
    public int getTotalSize() {
        return this.orderRepository.countById();
    }
}