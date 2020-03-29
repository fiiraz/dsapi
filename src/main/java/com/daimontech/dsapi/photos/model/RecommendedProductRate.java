package com.daimontech.dsapi.photos.model;


import com.daimontech.dsapi.model.User;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Size;


@Getter
@Setter
@Entity
@Table(name = "recommended_product_rate ")
//musterinin fotografini cekip onerdigi urunler
public class RecommendedProductRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 50)
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}
