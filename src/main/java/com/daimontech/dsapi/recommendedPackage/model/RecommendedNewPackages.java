package com.daimontech.dsapi.recommendedPackage.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "recommended_new_packages ")
//urun sahibinin musteriye onermeyi planladigi yeni urunler
public class RecommendedNewPackages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @NotBlank
    private String patternCode;

    @NotBlank
    @Size(min = 3, max = 40)
    private String description;

    private int sizeMin;

    private int sizeMax;

    private String aimCountry;

    private Date releaseDate;

    private Boolean status;

    @Column(name="images")
    @ElementCollection(targetClass=String.class)
    private List<String> imagesPath;

}
