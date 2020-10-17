package com.daimontech.dsapi.recommendedPackage.message.response;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class RecommendedPackagePaginationResponse {

    private Long id;

    private String patternCode;

    private String description;

    private int sizeMin;

    private int sizeMax;

    private String aimCountry;

    private Date releaseDate;

    private Boolean status;
}
