package com.daimontech.dsapi.orders.message.response;

import com.daimontech.dsapi.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersResponse {

    private Long orderID;

    private String assignedTo;

    private String orderNote;

    private String status;

    private String adminOrderNote;

    private double amount;

    private Long userID;
}
