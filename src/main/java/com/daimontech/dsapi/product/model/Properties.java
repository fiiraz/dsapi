package com.daimontech.dsapi.product.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@Table(name = "properties")
public class Properties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patternCode;

    private String asortiCode;

    @NotBlank
    private int sizeMin;

    @NotBlank
    private int sizeMax;


}
