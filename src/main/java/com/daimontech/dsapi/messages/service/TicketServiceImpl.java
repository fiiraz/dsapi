package com.daimontech.dsapi.messages.service;

import com.daimontech.dsapi.messages.model.Ticket;
import com.daimontech.dsapi.messages.repository.TicketRepository;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.orders.model.Order;
import com.daimontech.dsapi.product.model.ProductRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Ticket> findPaginated(String value, Pageable pageable) {
        return this.ticketRepository.findAll(value, pageable);
    }

    public int getTotalSize() {
        return this.ticketRepository.countById();
    }

    public Boolean delete(Ticket ticket) {
        try {
            ticketRepository.delete(ticket);
            return true;
        } catch (Exception e){
            return false;
        }
    }

}
