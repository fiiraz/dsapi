package com.daimontech.dsapi.product.message.response;

import com.daimontech.dsapi.product.model.Categories;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoriesGetAllResponse {

    private Categories category;

    private List<Categories> subCategories;
}
