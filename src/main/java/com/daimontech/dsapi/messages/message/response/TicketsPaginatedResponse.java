package com.daimontech.dsapi.messages.message.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketsPaginatedResponse {

    int totalPage;
    int currentPage;
    int pageSize;
    int totalElements;
    List<TicketsReponse> tickets;
}
