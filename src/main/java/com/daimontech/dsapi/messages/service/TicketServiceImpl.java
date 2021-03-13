package com.daimontech.dsapi.messages.service;

import com.daimontech.dsapi.messages.model.Ticket;
import com.daimontech.dsapi.messages.repository.TicketRepository;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.orders.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketRepository ticketRepository;

    public Boolean createNewTicket(Ticket ticket) {
        try {
            ticketRepository.save(ticket);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Ticket findById(Long ticketID) {
        return ticketRepository.findById(ticketID).get();
    }

    public List<Ticket> findAllByUserSentTicket(User user) {
        return ticketRepository.findAllByUserSentTicketOrderByCreatedDateDesc(user);
    }

    public List<Ticket> findAllByAssignedUser(User user) {
        return ticketRepository.findAllByAssignedUserOrderByCreatedDateDesc(user);
    }

    public List<Ticket> findAll(User user) {
        return ticketRepository.findAllByIdIsNotNull();
    }

}
