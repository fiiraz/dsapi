package com.daimontech.dsapi.orders.controller;

import com.daimontech.dsapi.model.DiscountUser;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.orders.message.request.*;
import com.daimontech.dsapi.orders.message.response.OrderedPackagesResponse;
import com.daimontech.dsapi.orders.message.response.OrdersPaginatedResponse;
import com.daimontech.dsapi.orders.message.response.OrdersResponse;
import com.daimontech.dsapi.orders.model.Order;
import com.daimontech.dsapi.orders.model.OrderedPackages;
import com.daimontech.dsapi.orders.service.OrderServiceImpl;
import com.daimontech.dsapi.product.model.DiscountPackage;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.service.DiscountServiceImpl;
import com.daimontech.dsapi.product.service.PackageServiceImpl;
import com.daimontech.dsapi.repository.UserRepository;
import com.daimontech.dsapi.security.services.DiscountUserServiceImpl;
import com.daimontech.dsapi.utilities.error.BaseError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    DiscountServiceImpl discountPackageService;

    @Autowired
    DiscountUserServiceImpl discountUserService;

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
        order.setOrderNote(orderNewRequest.getOrderNote());
        List<DiscountUser> discountUserList = discountUserService.getAllByUser(user.get());
        Boolean orderResult = orderService.createNewOrder(order);
        if (!orderResult) {
            return new ResponseEntity<String>("Fail -> Order could not be created!",
                    HttpStatus.BAD_REQUEST);
        }
        double totalAmount = 0;
        for (OrderCount orderCount :
                orderNewRequest.getOrders()) {
            if (packageService.existsByPackageId(orderCount.getPackageId())) {
                Packages packages = packageService.getByPackageId(orderCount.getPackageId());
                OrderedPackages orderedPackages = new OrderedPackages();
                List<DiscountPackage> discountPackageList = discountPackageService.getAllByPackage(packages);
                orderedPackages.setOrder(order);
                orderedPackages.setUserMadeOrder(user.get());
                orderedPackages.setOrderCount(orderCount.getCount());
                orderedPackages.setOrderedPackage(packages);
                double packagePrice = packages.getPrice();
                if (discountPackageList.size() != 0) {
                    for (DiscountPackage discountPackage :
                            discountPackageList
                    ) {
                        packagePrice -= packagePrice * (discountPackage.getDiscount() / 100);
                    }
                }
                if (discountUserList.size() != 0) {
                    for (DiscountUser discountUser :
                            discountUserList
                    ) {
                        packagePrice -= packagePrice * (discountUser.getDiscount() / 100);
                    }
                }
                totalAmount += packagePrice * orderCount.getCount();
                orderedPackages.setPrice(packagePrice * orderCount.getCount());
                Boolean orderedPackagesResult = orderService.createNewOrderPackage(orderedPackages);
                if (!orderedPackagesResult) {
                    return new ResponseEntity<String>("Fail -> Order could not be created!",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }
        order.setAmount(totalAmount);
        orderService.updateOrder(order);
        return ResponseEntity.ok().body("Order was created successfully!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteorder/{orderID}")
    @ApiOperation(value = "Delete Order")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable(value = "orderID") long orderID) {
        Order order = orderService.findById(orderID);
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
            if (!orderService.existstByOrderId(orderUpdateRequest.getOrderId())) {
                return new ResponseEntity<String>("Fail -> Order doesn't exist!",
                        HttpStatus.BAD_REQUEST);
            }
            Order order = orderService.findById(orderUpdateRequest.getOrderId());
            order.setStatus(orderUpdateRequest.getOrderType());
            order.setAssignedTo("ADMIN");
            order.setOrderNote(orderUpdateRequest.getOrderNote());
            order.setAdminOrderNote(orderUpdateRequest.getAdminOrderNote());
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
                    System.out.println(orderedPackages.toString());
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
        Order order = orderService.findById(orderDeleteRequest.getOrderId());
        if (order == null) {
            return new ResponseEntity<String>("Fail -> Order could not be found!",
                    HttpStatus.BAD_REQUEST);
        }
        Packages packages = packageService.getByPackageId(orderDeleteRequest.getOrderedPackageId());
        OrderedPackages orderedPackages = orderService.findByOrderIdAndOrderedPackage(orderDeleteRequest.getOrderId(), packages);
        if (orderedPackages != null) {
            if (orderService.deleteOrderedPackages(orderedPackages))
                return ResponseEntity.ok().body("Ordered Package deleted successfully!");
        }
        return new ResponseEntity<String>("Fail -> Ordered Package could not be deleted!",
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
    @GetMapping("/getorderbyOrderID")
    @ApiOperation(value = "Get Order By Order ID")
    public ResponseEntity<OrdersResponse> getOrderByOrderID(@Valid @RequestParam long orderId) {
        try {
            Order order = orderService.findById(orderId);
            if (order != null) {
                OrdersResponse ordersResponse = new OrdersResponse();
                ordersResponse.setUserID(order.getUserMadeOrder().getId());
                ordersResponse.setUserName(order.getUserMadeOrder().getName());
                ordersResponse.setStatus(order.getStatus());
                ordersResponse.setOrderNote(order.getOrderNote());
                ordersResponse.setAdminOrderNote(order.getAdminOrderNote());
                ordersResponse.setOrderID(order.getId());
                ordersResponse.setAmount(order.getAmount());
                return ResponseEntity.ok().body(ordersResponse);
            }
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND);
        } catch (Exception r) {
            return new ResponseEntity<>(
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/getallorders/{pageNo}/{sortingValue}/{sortType}/{searchingValue}/{pageSize}")
    @ApiOperation(value = "Get All Orders")
    public ResponseEntity<OrdersPaginatedResponse> getAllOrders(
            @Valid @PathVariable(value = "pageNo") int pageNo,
            @PathVariable(value = "sortingValue", required = false) String sortingValue,
            @PathVariable(value = "sortType", required = false) Optional<String> sortType,
            @PathVariable(value = "searchingValue", required = false) Optional<String> searchingValue,
            @PathVariable(value = "pageSize", required = false) Optional<Integer> pageSize) {
        try {
            Page<Order> page;
            Sort sort = "ASC".equals(sortType.get()) ? Sort.by(sortingValue).ascending() : Sort.by(sortingValue).descending();
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize.get(), sort);
            page = orderService.findPaginated(searchingValue.orElse("_"), pageable);
            List<Order> orders = page.getContent();
            List<OrdersResponse> ordersResponseList = new ArrayList<>();
            OrdersPaginatedResponse ordersPaginatedResponse = new OrdersPaginatedResponse();
            if (page.hasContent()) {
                for (Order order : orders) {
                    OrdersResponse ordersResponse = new OrdersResponse();
                    ordersResponse.setAdminOrderNote(order.getAdminOrderNote());
                    ordersResponse.setAmount(order.getAmount());
                    ordersResponse.setOrderNote(order.getOrderNote());
                    ordersResponse.setStatus(order.getStatus());
                    User user = userRepository.findById(order.getUserMadeOrder().getId()).get();
                    ordersResponse.setUserID(user.getId());
                    ordersResponse.setUserName(user.getName());
                    ordersResponse.setStatus(order.getStatus());
                    ordersResponse.setOrderID(order.getId());
                    ordersResponseList.add(ordersResponse);
                }
                ordersPaginatedResponse.setOrders(ordersResponseList);
                ordersPaginatedResponse.setTotalPage(page.getTotalPages());
                ordersPaginatedResponse.setPageSize(page.getSize());
                ordersPaginatedResponse.setCurrentPage(page.getNumber());
                ordersPaginatedResponse.setTotalElements(orderService.getTotalSize());
                return ResponseEntity.ok().body(ordersPaginatedResponse);
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
                                                                                   @RequestParam long orderId) {
        try {
            Order order = orderService.findById(orderId);
            List<OrderedPackages> orderedPackagesList = orderService.getAllOrderedPackagesByOrder(order.getId());
            List<OrderedPackagesResponse> orderedPackagesResponseList = new ArrayList<>();
            if (orderedPackagesList.size() > 0) {
                for (OrderedPackages orderedPackages :
                        orderedPackagesList) {
                    OrderedPackagesResponse orderedPackagesResponse = new OrderedPackagesResponse();
                    orderedPackagesResponse.setCount(orderedPackages.getOrderCount());
                    orderedPackagesResponse.setOrderedPackageId(orderedPackages.getId());
                    orderedPackagesResponse.setOrderedUserName(orderedPackages.getUserMadeOrder().getUsername());
                    orderedPackagesResponse.setOrderId(orderedPackages.getOrder().getId());
                    orderedPackagesResponse.setPrice(orderedPackages.getPrice());
                    Long packagesID = orderService.getPackagesIdByOrderedPackagesId(orderedPackages.getId());
                    Packages packages = packageService.getByPackageId(packagesID);
                    orderedPackagesResponse.setPackageId(packagesID);
                    orderedPackagesResponse.setPackageDescription(packages.getDescription());
                    orderedPackagesResponse.setPackageAsortiCode(packages.getAsortiCode());
                    orderedPackagesResponse.setPackageProductCode(packages.getProductCode());
                    orderedPackagesResponse.setPackagePrice(packages.getPrice());
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

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @PutMapping("/acceptorder")
    @ApiOperation(value = "Update Order Accept")
    public ResponseEntity<String> acceptOrder(@Valid @RequestParam long orderId) {
        try {
            if (!orderService.existstByOrderId(orderId)) {
                return new ResponseEntity<String>("Fail -> Order doesn't exist!",
                        HttpStatus.BAD_REQUEST);
            }
            Order order = orderService.findById(orderId);
            order.setStatus("ACCEPTED");
            order.setAssignedTo("ADMIN");
            boolean orderResult = orderService.updateOrder(order);
            if (!orderResult) {
                if (!orderService.existstByOrderId(orderId)) {
                    return new ResponseEntity<String>("Fail -> Order couldn't be Accepted!",
                            HttpStatus.BAD_REQUEST);
                }
            }
            return ResponseEntity.ok().body("Order was accepted successfully!");
        } catch (Exception e) {
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getUnknownError()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @PutMapping("/cancelorder")
    @ApiOperation(value = "Update Order Cancel")
    public ResponseEntity<String> cancelOrder(@Valid @RequestParam long orderId) {
        try {
            if (!orderService.existstByOrderId(orderId)) {
                return new ResponseEntity<String>("Fail -> Order doesn't exist!",
                        HttpStatus.BAD_REQUEST);
            }
            Order order = orderService.findById(orderId);
            order.setStatus("CANCELED");
            order.setAssignedTo("ADMIN");
            boolean orderResult = orderService.updateOrder(order);
            if (!orderResult) {
                if (!orderService.existstByOrderId(orderId)) {
                    return new ResponseEntity<String>("Fail -> Order couldn't be cancelled!",
                            HttpStatus.BAD_REQUEST);
                }
            }
            return ResponseEntity.ok().body("Order cancelled successfully!");
        } catch (Exception e) {
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getUnknownError()),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
