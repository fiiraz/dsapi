package com.daimontech.dsapi.mail.model;

import com.daimontech.dsapi.model.User;
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
public class SignInVeryfy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String content;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
