package com.daimontech.dsapi.orders.message.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderedPackagesDeleteRequest {

    private Long orderId;
    private List<Long> orderedPackageIds;
}
