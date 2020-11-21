package com.daimontech.dsapi.orders.message.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCountUpdate {

    private Long orderedPackageId;
    private int count;
}
