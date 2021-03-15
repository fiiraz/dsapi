package com.daimontech.dsapi.product.message.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateUserRatePackageRequest {

    Long productRateID;

    private double rate;
}
