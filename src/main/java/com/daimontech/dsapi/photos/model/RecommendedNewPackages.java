package com.daimontech.dsapi.photos.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@Entity
@Table(name = "recommended_new_packages ")

public class RecommendedNewPackages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 5)
    private int rate;

    @NotBlank
    @Size(min = 3, max = 40)
    private String description;

    @NotBlank
    private int sizeMin;

    @NotBlank
    private int sizeMax;

    @Column(name = "START_DATE" , columnDefinition = "DATE ​​DEFAULT CURRENT_DATE" )
    private java.sql.Date startDate;

}
