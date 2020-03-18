package com.daimontech.dsapi.photos.model;


import com.daimontech.dsapi.product.model.Packages;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@Entity
@Table(name = "my_considerations", uniqueConstraints = {@UniqueConstraint(columnNames = {"packges_id"})})

public class MyConsiderations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 5)
    private int rate;

    @Size(min = 3, max = 50)
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "packges_id", unique = true)
    private Packages packages;

}
