package com.daimontech.dsapi.photos.model;


import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.product.model.Categories;
import com.daimontech.dsapi.product.model.Packages;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@Entity
@Table(name = "recommended_photos", uniqueConstraints = {@UniqueConstraint(columnNames = {"recommended_product_rate_id"})})
//tum fotograflarin toplandigi fotolar
public class ProductPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recommended_product_rate_id", unique = true, nullable = true)
    private RecommendedProductRate recommendedProductRate;

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
