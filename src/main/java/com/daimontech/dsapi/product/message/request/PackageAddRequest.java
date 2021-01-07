package com.daimontech.dsapi.product.message.request;

import com.daimontech.dsapi.product.model.Images;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class PackageAddRequest {

    private int productCode;

    private String title;

    @NotBlank
    @Size(min=3, max = 255)
    private String description;

    @NotNull
    private Long categoryId;

    private String patternCode;

    private String asortiCode;

    private int sizeMin;

    private int sizeMax;

    /*@NotNull
    private PropertyAddRequest propertyAddRequest;*/

    @NotNull
    private List<Long> colorId;

    private double price;

    private List<String> imagesPath;

}
