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
//urun sahibinin musteriye onermeyi planladigi yeni urunler
public class RecommendedNewPackages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photos;

    @NotBlank
    @Size(min = 3, max = 40)
    private String description;

    @NotBlank
    private int sizeMin;

    @NotBlank
    private int sizeMax;

}
