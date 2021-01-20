package com.daimontech.dsapi.orders.controller;

import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.orders.message.request.*;
import com.daimontech.dsapi.orders.message.response.OrderedPackagesResponse;
import com.daimontech.dsapi.orders.model.Order;
import com.daimontech.dsapi.orders.model.OrderedPackages;
import com.daimontech.dsapi.orders.service.OrderServiceImpl;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.service.PackageServiceImpl;
import com.daimontech.dsapi.repository.UserRepository;
import com.daimontech.dsapi.utilities.error.BaseError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
@Api(value = "Order islemleri")
@Transactional
public class OrderController {

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    PackageServiceImpl packageService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BaseError baseError;

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/neworder")
    @ApiOperation(value = "New Order")
    public ResponseEntity<String> newOrder(@Valid @RequestBody OrderNewRequest orderNewRequest) {
        Optional<User> user = userRepository.findByUsername(orderNewRequest.getUsername());
        Order order = new Order();
        order.setAssignedTo("ORDER");
        order.setStatus("IN PROGRESS");
        order.setUserMadeOrder(user.get());
        Boolean orderResult = orderService.createNewOrder(order);
        if (!orderResult) {
            return new ResponseEntity<String>("Fail -> Order could not be created!",
                    HttpStatus.BAD_REQUEST);
        }
        for (OrderCount orderCount :
                orderNewRequest.getOrders()) {
            if (packageService.existsByPackageId(orderCount.getPackageId())) {
                Packages packages = packageService.getByPackageId(orderCount.getPackageId());
                OrderedPackages orderedPackages = new OrderedPackages();
                orderedPackages.setOrder(order);
                orderedPackages.setUserMadeOrder(user.get());
                orderedPackages.setOrderCount(orderCount.getCount());
                orderedPackages.setOrderedPackage(packages);
                Boolean orderedPackagesResult = orderService.createNewOrderPackage(orderedPackages);
                if (!orderedPackagesResult) {
                    return new ResponseEntity<String>("Fail -> Order could not be created!",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }
        return ResponseEntity.ok().body("Order was created successfully!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteorder")
    @ApiOperation(value = "Delete Order")
    public ResponseEntity<String> deleteOrder(@Valid @RequestBody OrderDeleteRequest orderDeleteRequest) {
        Order order = orderService.findById(orderDeleteRequest.getOrderId());
        if (order != null) {
            if (orderService.delete(order))
                return ResponseEntity.ok().body("Order deleted successfully!");
        }
        return new ResponseEntity<String>("Fail -> Order could not be deleted!",
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/updateorder")
    @ApiOperation(value = "Update Order")
    public ResponseEntity<String> updateOrder(@Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        try {
            if(!orderService.existstByOrderId(orderUpdateRequest.getOrderId())){
                return new ResponseEntity<String>("Fail -> Order doesn't exist!",
                        HttpStatus.BAD_REQUEST);
            }
            Order order = orderService.findById(orderUpdateRequest.getOrderId());
            order.setAssignedTo(orderUpdateRequest.getOrderType());
            boolean orderResult = orderService.updateOrder(order);
            if (!orderResult) {
                return new ResponseEntity<String>("Fail -> Order could not be updated!",
                        HttpStatus.BAD_REQUEST);
            }
            for (OrderCountUpdate orderCount :
                    orderUpdateRequest.getOrders()) {
                if (orderService.existstById(orderCount.getOrderedPackageId())) {
                    OrderedPackages orderedPackages =
                            orderService.findByOrderedPackagesId(orderCount.getOrderedPackageId());
                    Packages packages = packageService.getByPackageId(orderCount.getPackageId());
                    System.out.println(orderCount.getOrderedPackageId());
                    orderedPackages.setOrderCount(orderCount.getCount());
                    orderedPackages.setOrderedPackage(packages);
                    orderedPackages.setOrder(order);
                    boolean orderedPackagesResult = orderService.updateOrderPackage(orderedPackages);
                    if (!orderedPackagesResult) {
                        return new ResponseEntity<String>("Fail -> Order could not be updated!",
                                HttpStatus.BAD_REQUEST);
                    }
                }
            }
            return ResponseEntity.ok().body("Order was updated successfully!");
        } catch (Exception e) {
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getUnknownError()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteorderedpackage")
    @ApiOperation(value = "Delete Ordered Packages")
    public ResponseEntity<String> deleteOrderedPackage(@Valid @RequestBody OrderedPackageDeleteRequest orderDeleteRequest) {
        OrderedPackages order = orderService.findByOrderedPackagesId(orderDeleteRequest.getOrderedPackageId());
        if (order != null) {
            if (orderService.deleteOrderedPackages(order))
                return ResponseEntity.ok().body("Ordered Package deleted successfully!");
        }
        return new ResponseEntity<String>("Fail -> Order could not be deleted!",
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getordersbyusername")
    @ApiOperation(value = "Get Orders By User Name")
    public ResponseEntity<List<Order>> getOrdersByUserId(@Valid @RequestBody OrdersGetByUserNameRequest ordersGetByUserIdRequest) {
        try {
            User user = userRepository.findByUsername(ordersGetByUserIdRequest.getUserName()).get();
            List<Order> orders = orderService.getAllByUser(user);
            if (orders.size() > 0) {
                return ResponseEntity.ok().body(orders);
            }
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND);
        } catch (Exception r) {
            return new ResponseEntity<>(
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getorderedpackages")
    @ApiOperation(value = "Get Ordered Packages By Order")
    public ResponseEntity<List<OrderedPackagesResponse>> getOrderedPackagesByOrder(@Valid
                                                                     @RequestBody OrderedPackagesGetByOrderIdRequest
                                                                             orderedPackagesGetByOrderIdRequest) {
        try {
            Order order = orderService.findById(orderedPackagesGetByOrderIdRequest.getOrderId());
            List<OrderedPackages> orderedPackagesList = orderService.getAllOrderedPackagesByOrder(order.getId());
            List<OrderedPackagesResponse> orderedPackagesResponseList = new ArrayList<>();
            if (orderedPackagesList.size() > 0) {
                for (OrderedPackages orderedPackages :
                        orderedPackagesList) {
                    OrderedPackagesResponse orderedPackagesResponse = new OrderedPackagesResponse();
                    orderedPackagesResponse.setCount(orderedPackages.getOrderCount());
                    orderedPackagesResponse.setOrderedPackageId(orderedPackages.getOrderedPackage().getId());
                    orderedPackagesResponse.setOrderedUserName(orderedPackages.getUserMadeOrder().getUsername());
                    orderedPackagesResponse.setOrderId(orderedPackages.getOrder().getId());
                    Long packagesID = orderService.getPackagesIdByOrderedPackagesId(orderedPackages.getId());
                    orderedPackagesResponse.setPackageId(packagesID);
                    orderedPackagesResponseList.add(orderedPackagesResponse);
                }
                return ResponseEntity.ok().body(orderedPackagesResponseList);
            }
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND);
        } catch (Exception r) {
            return new ResponseEntity<>(
                    HttpStatus.BAD_REQUEST);
        }
    }
}
