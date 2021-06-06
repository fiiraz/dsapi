package com.daimontech.dsapi.messages.message.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketsReponse {

    private Long ID;

    private String ticketTitle;

    private String ticketNote;

    private String ticketStatus;

    private Long userIDSentTicket;

    private String userNameSentTicket;

    private Long userIDAssignedTicket;

    private String userNameAssignedTicket;

    private String userPhoneAssignedTicket;

    private Long packageID;

    private String packageTitle;

    @Override
    public String toString() {
        return "TicketsReponse{" +
                "ID=" + ID +
                ", ticketTitle='" + ticketTitle + '\'' +
                ", ticketNote='" + ticketNote + '\'' +
                ", ticketStatus='" + ticketStatus + '\'' +
                ", userIDSentTicket=" + userIDSentTicket +
                ", userNameSentTicket='" + userNameSentTicket + '\'' +
                ", userIDAssignedTicket=" + userIDAssignedTicket +
                ", userNameAssignedTicket='" + userNameAssignedTicket + '\'' +
                ", packageID=" + packageID +
                ", packageTitle='" + packageTitle + '\'' +
                '}';
    }
}
