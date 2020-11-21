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
import org.springframework.stereotype.Service;

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

    public OrderedPackages findByOrder(Order order) {
        return orderedPackagesRepository.findByOrder(order);
    }

    public boolean existstById(Long orderedPackagesId) {
        return orderedPackagesRepository.existsById(orderedPackagesId);
    }

    public boolean existstByOrderId(Long orderId) {
        return orderRepository.existsById(orderId);
    }
}