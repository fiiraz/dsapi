package com.daimontech.dsapi.message.request;

import com.daimontech.dsapi.model.enums.SaleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EditUserForm {
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Pattern(regexp ="^\\+(?:[0-9] ?){6,14}[0-9]$")
    private String username;

    @Size(max = 60)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @Size(min = 3, max = 50)
    private String country;

    @Size(min = 3, max = 50)
    private String city;

    @Size(min = 3, max = 50)
    private String companyName;

    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    @Size(min = 3, max = 50)
    private String photoOne;

    @Size(min = 3, max = 50)
    private String photoTwo;
}
