package com.daimontech.dsapi.recommendedPackage.message.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RecommendedPackagesAddRequest {

    @NotBlank
    private String patternCode;

    @NotBlank
    @Size(min = 3, max = 40)
    private String description;

    private int sizeMin;

    private int sizeMax;

    private String aimCountry;

}
