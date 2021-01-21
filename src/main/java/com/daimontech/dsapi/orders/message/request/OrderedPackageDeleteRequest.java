package com.daimontech.dsapi.orders.message.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderedPackageDeleteRequest {

    private Long orderId;
    private Long orderedPackageId;
}
