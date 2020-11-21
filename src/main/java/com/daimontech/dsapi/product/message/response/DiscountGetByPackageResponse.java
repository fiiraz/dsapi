package com.daimontech.dsapi.product.message.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscountGetByPackageResponse {

    private int discount;

    private Long packageId;
}
