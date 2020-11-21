package com.daimontech.dsapi.product.message.response;

import com.daimontech.dsapi.product.message.request.PropertyAddRequest;
import com.daimontech.dsapi.product.model.Colors;
import com.daimontech.dsapi.product.model.Properties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PackagePaginationResponse {

    private Long id;

    private int productCode;

    private String description;

    private Long categoryId;

    private String categoryName;

    private int categoryParent;

    private Properties properties;

    private Set<Colors> colorsList;

    private Date createdDate;

    private double price;
}
