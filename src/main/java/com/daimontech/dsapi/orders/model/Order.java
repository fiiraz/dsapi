package com.daimontech.dsapi.orders.model;

import com.daimontech.dsapi.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assignedTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordered_user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userMadeOrder;

    private String status;

    private double amount;

    private String orderNote;

    private String adminOrderNote;
}
