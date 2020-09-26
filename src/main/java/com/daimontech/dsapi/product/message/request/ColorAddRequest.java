package com.daimontech.dsapi.product.message.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ColorAddRequest {

    @NotBlank
    private String colorName;
    @NotBlank
    private String colorCode;
}
