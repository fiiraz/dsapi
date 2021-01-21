package com.daimontech.dsapi.product.message.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscountGetByPackageResponse {

    private double discount;

    private Long packageId;
}
