package com.daimontech.dsapi.product.model;


import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.product.model.Packages;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Size;


@Getter
@Setter
@Entity
@Table(name = "product_rate", uniqueConstraints = {@UniqueConstraint(columnNames = {"packges_id"})})
//urun sahibinin onerdigi mevcut urunler
public class ProductRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double rate;

    @Size(min = 3, max = 255)
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "packges_id", unique = false)
    @JsonIgnore
    private Packages packages;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = false)
    @JsonIgnore
    private User user;


}
