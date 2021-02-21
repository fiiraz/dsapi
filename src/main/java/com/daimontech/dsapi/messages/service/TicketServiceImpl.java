package com.daimontech.dsapi.messages.service;

import com.daimontech.dsapi.messages.model.Ticket;
import com.daimontech.dsapi.messages.repository.TicketRepository;
import com.daimontech.dsapi.orders.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
