package com.daimontech.dsapi.photos.model;

import com.daimontech.dsapi.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.constraints.Size;


@Getter
@Setter
@Entity
@Table(name = "recommended_new_packages_rate ")
//urun sahibinin musteriye onermeyi planladigi yeni urunlerin puanlamasi
public class RecommendedNewPackagesRate {  // bu tabloyu yorumu musteriden almak icin olusturduk
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 5)
    private int rate;

    @Size(min = 3, max = 50)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recommended_new_packages_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private RecommendedNewPackages recommendedNewPackages;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;



}
