package com.daimontech.dsapi.orders.message.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderedPackagesGetByOrderIdRequest {

    private Long orderId;
}
