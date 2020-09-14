package com.daimontech.dsapi.product.message.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PropertyAddRequest {

    private String patternCode;

    private String asortiCode;

    @NotBlank
    private int sizeMin;

    @NotBlank
    private int sizeMax;

}
