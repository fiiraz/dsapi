package com.daimontech.dsapi.mail.model;

import com.daimontech.dsapi.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@Table(name = "sign_in_code ", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "code"
        }),

})
public class SignInCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String code;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
