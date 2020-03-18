package com.daimontech.dsapi.product.model;

import com.daimontech.dsapi.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "packages", uniqueConstraints={@UniqueConstraint(columnNames={"property_id"})})
public class Packages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int productCode;

    @NotBlank
    @Size(min=3, max = 255)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "package_colors",
            joinColumns = @JoinColumn(name = "packages_id"),
            inverseJoinColumns = @JoinColumn(name = "colors_id"))
    private Set<Colors> colors = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Categories categories;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="property_id", unique = true)
    private Properties properties;

}
