package com.daimontech.dsapi.messages.service;

import com.daimontech.dsapi.messages.model.Ticket;
import com.daimontech.dsapi.messages.model.TicketMessages;
import com.daimontech.dsapi.messages.repository.TicketMessageRepository;
import com.daimontech.dsapi.messages.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketMessageServiceImpl implements TicketMessageService{

    @Autowired
    TicketMessageRepository ticketMessageRepository;

    public Boolean createNewTicketMessage(TicketMessages ticketMessages) {
        try {
            ticketMessageRepository.save(ticketMessages);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public TicketMessages findById(Long ticketMessagesID) {
        return ticketMessageRepository.findById(ticketMessagesID).get();
    }

    public List<TicketMessages> findAllByTicketOrderByCreatedDate(Ticket ticket) {
        return ticketMessageRepository.findAllByTicketOrderByCreatedDate(ticket);
    }
}
