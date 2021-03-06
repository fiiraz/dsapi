package com.daimontech.dsapi.recommendedPackage.model;

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
@Table(name = "recommended_photos ")
//urun sahibinin musteriye onermeyi planladigi yeni urunlerin puanlamasi
public class RecommendedPhotos {  // bu tabloyu yorumu musteriden almak icin olusturduk
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String photos;
}
