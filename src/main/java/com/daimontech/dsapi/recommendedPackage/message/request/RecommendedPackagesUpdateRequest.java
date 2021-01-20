package com.daimontech.dsapi.recommendedPackage.message.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class RecommendedPackagesUpdateRequest {

    private Integer id;

    private String title;

    @NotBlank
    private String patternCode;

    @NotBlank
    @Size(min = 3, max = 40)
    private String description;

    private int sizeMin;

    private int sizeMax;

    private String aimCountry;

    private Boolean status;

    private List<String> imagesPath;

}
