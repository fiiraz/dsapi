package com.daimontech.dsapi.company.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Table(name = "company")

public class CompanyAbout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 50)
    @NotBlank
    private String title;

    @Size(min = 3, max = 250)
    private String descrption;

    @NotBlank
    @Size(min = 11, max = 13)
    private Long phoneNumber;

    @Email
    @NotBlank
    private Long email;

    @Size(min = 11, max = 13)
    private Long fax;

    @NotBlank
    private String addressInformation;

    private String companyPhoto;
}
