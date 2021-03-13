package com.daimontech.dsapi.product.message.response;

import com.daimontech.dsapi.product.model.Colors;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PackagePaginationResponse {

    private Long id;

    private String title;

    private int productCode;

    private String description;

    private Long categoryId;

    private String categoryName;

    private int categoryParent;

    private String patternCode;

    private String asortiCode;

    private int sizeMin;

    private int sizeMax;

    private Set<Colors> colorsList;

    private Date createdDate;

    private double price;

    private double discountPrice;

    private List<String> imagesPath;

    private Boolean forRateOnly;

    private int rate;

    private Boolean RateAllowed;

    private List<String> aimCountries;
}
