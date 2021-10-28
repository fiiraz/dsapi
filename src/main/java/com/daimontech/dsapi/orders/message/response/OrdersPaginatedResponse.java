package com.daimontech.dsapi.orders.message.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrdersPaginatedResponse {

    int totalPage;
    int currentPage;
    int pageSize;
    int totalElements;
    List<OrdersResponse> orders;
}