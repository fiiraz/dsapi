package com.daimontech.dsapi.messages.repository;

import com.daimontech.dsapi.messages.model.Ticket;
import com.daimontech.dsapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findById(Long ticketID);
    List<Ticket> findAllByUserSentTicketOrderByCreatedDateDesc(User user);
    List<Ticket> findAllByAssignedUserOrderByCreatedDateDesc(User user);
    List<Ticket> findAllByIdIsNotNull();
}
