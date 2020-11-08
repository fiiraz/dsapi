package com.daimontech.dsapi.product.message.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscountAddRequest {

    private int discount;

    private Long packageId;
}
