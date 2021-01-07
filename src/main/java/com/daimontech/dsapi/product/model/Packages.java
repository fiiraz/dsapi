package com.daimontech.dsapi.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "packages")
public class Packages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int productCode;

    @NotBlank
    @Size(min=3, max = 255)
    private String description;

    private Date createdDate;

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

/*    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="property_id", referencedColumnName = "id")
    private Properties properties;*/

    private String patternCode;

    private String asortiCode;

    private int sizeMin;

    private int sizeMax;

    private double price;

    @Column(name="images")
    @ElementCollection(targetClass=String.class)
    private List<String> imagesPath;

    /*@OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "images_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<Images> images;*/

    /*@Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private List<MultipartFile> images;*/
}
