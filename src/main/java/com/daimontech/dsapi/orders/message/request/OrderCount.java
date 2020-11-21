package com.daimontech.dsapi.orders.message.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCount {

    private Long packageId;
    private int count;
}
