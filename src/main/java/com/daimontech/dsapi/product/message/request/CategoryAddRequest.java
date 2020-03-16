package com.daimontech.dsapi.product.message.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CategoryAddRequest {

    private Long parentId;

    @NotBlank
    @Size(min=3, max = 50)
    private String categoryName;

}
