package com.daimontech.dsapi.messages.repository;

import com.daimontech.dsapi.messages.model.Ticket;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.orders.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findById(Long ticketID);
    List<Ticket> findAllByUserSentTicketOrderByCreatedDateDesc(User user);
    List<Ticket> findAllByAssignedUserOrderByCreatedDateDesc(User user);
    List<Ticket> findAllByIdIsNotNull();
    @Query(value = "SELECT * FROM tickets WHERE LOWER(ticket_status) LIKE LOWER(concat('%', ?1,'%')) OR ticket_note LIKE LOWER(concat('%', ?1,'%')) " +
            "OR LOWER(ticket_title) LIKE LOWER(concat('%', ?1,'%'))" +
            " OR package_id LIKE %?1% OR created_user_id LIKE %?1%", nativeQuery = true)
    Page<Ticket> findAll(String title, Pageable pageable);
    @Query(value = "SELECT COUNT(*) FROM tickets", nativeQuery = true)
    int countById();
}
