package com.daimontech.dsapi.messages.repository;


import com.daimontech.dsapi.messages.model.Ticket;
import com.daimontech.dsapi.messages.model.TicketMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketMessageRepository extends JpaRepository<TicketMessages, Long> {
    Optional<TicketMessages> findById(Long ticketMessagesID);
    List<TicketMessages> findAllByTicketOrderByCreatedDate(Ticket ticket);
}


