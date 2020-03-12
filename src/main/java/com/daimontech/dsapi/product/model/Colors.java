package com.daimontech.dsapi.product.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@Table(name = "colors", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "colorName"
        }),
        @UniqueConstraint(columnNames = {
                "colorCode"
        })
})
public class Colors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String colorName;

    @NotBlank
    private String colorCode;
}
