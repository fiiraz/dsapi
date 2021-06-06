package com.daimontech.dsapi.messages.message.response;

import com.daimontech.dsapi.messages.model.Ticket;
import com.daimontech.dsapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TicketMessagesResponse {

    private Long ID;

    private Ticket ticket;

    private User userSentMessage;

    private String ticketMessage;

}
