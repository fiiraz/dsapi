package com.daimontech.dsapi.recommendedPackage.message.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class RecommendedPackageRateRequest {

    private int rate;

    @Size(min = 3, max = 50)
    private String comment;

    private Integer packageId;

    private Integer userId;

}
