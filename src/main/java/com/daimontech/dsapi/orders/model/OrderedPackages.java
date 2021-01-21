package com.daimontech.dsapi.orders.model;

import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.product.model.Packages;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "oredered_packages")
public class OrderedPackages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "package_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Packages orderedPackage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordered_user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userMadeOrder;

    private int orderCount;

    private double price;

    @Override
    public String toString() {
        return "OrderedPackages{" +
                "id=" + id +
                ", orderedPackage=" + orderedPackage +
                ", order=" + order +
                ", userMadeOrder=" + userMadeOrder +
                ", orderCount=" + orderCount +
                '}';
    }
}
