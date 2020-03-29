package com.daimontech.dsapi.photos.model;


import com.daimontech.dsapi.product.model.Packages;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Entity
@Table(name = "product_photos")
//tum fotograflarin toplandigi fotolar
public class ProductPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String photos;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "package_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Packages packages;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "recommended_new_packages_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private RecommendedNewPackages recommendedNewPackages;

}
