package com.daimontech.dsapi.photos.model;


import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.product.model.Packages;
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

    @Size(min = 1, max = 5)
    private int rate;

    @Size(min = 3, max = 255)
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "packges_id", unique = true)
    private Packages packages;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;


}
