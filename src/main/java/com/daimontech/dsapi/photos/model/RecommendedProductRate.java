package com.daimontech.dsapi.photos.model;


import com.daimontech.dsapi.model.User;
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
@Table(name = "recommended_product_rate ")

public class RecommendedProductRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 5)
    private int rate;

    @Size(min = 3, max = 50)
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}
