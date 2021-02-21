package com.daimontech.dsapi.messages.model;

import com.daimontech.dsapi.model.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "ticket_messages")
public class TicketMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userSentMessage;

    private String ticketMessage;

    private Timestamp createdDate;

    private Timestamp updatedDate;

    @Override
    public String toString() {
        return "TicketMessages{" +
                "id=" + id +
                ", ticket=" + ticket +
                ", userSentMessage=" + userSentMessage +
                ", ticketMessage='" + ticketMessage + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
