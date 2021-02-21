package com.daimontech.dsapi.messages.model;

import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.product.model.Packages;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userSentTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Packages ticketPackage;

    private String ticketTitle;

    private String ticketNote;

    private String ticketStatus;

    private Timestamp createdDate;

    private Timestamp updatedDate;

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", userSentTicket=" + userSentTicket +
                ", ticketPackage=" + ticketPackage +
                ", ticketTitle='" + ticketTitle + '\'' +
                ", ticketNote='" + ticketNote + '\'' +
                ", ticketStatus='" + ticketStatus + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
