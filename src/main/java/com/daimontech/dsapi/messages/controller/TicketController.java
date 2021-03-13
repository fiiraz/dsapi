package com.daimontech.dsapi.messages.controller;

import com.daimontech.dsapi.messages.message.request.TicketMessageNewRequest;
import com.daimontech.dsapi.messages.message.request.TicketNewRequest;
import com.daimontech.dsapi.messages.model.Ticket;
import com.daimontech.dsapi.messages.model.TicketMessages;
import com.daimontech.dsapi.messages.service.TicketMessageServiceImpl;
import com.daimontech.dsapi.messages.service.TicketServiceImpl;
import com.daimontech.dsapi.model.DiscountUser;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.orders.message.request.OrderCount;
import com.daimontech.dsapi.orders.message.request.OrderNewRequest;
import com.daimontech.dsapi.orders.model.Order;
import com.daimontech.dsapi.orders.model.OrderedPackages;
import com.daimontech.dsapi.product.model.DiscountPackage;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.service.PackageServiceImpl;
import com.daimontech.dsapi.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api/ticket")
@Api(value = "Ticket islemleri")
public class TicketController {

    @Autowired
    TicketServiceImpl ticketService;

    @Autowired
    TicketMessageServiceImpl ticketMessageService;

    @Autowired
    PackageServiceImpl packageService;

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/newticket")
    @ApiOperation(value = "New Ticket")
    public ResponseEntity<String> newTicket(@Valid @RequestBody TicketNewRequest ticketNewRequest) {
        try {
            Optional<User> user = userRepository.findByUsername(ticketNewRequest.getUsername());
            Ticket ticket = new Ticket();
            Date date = new Date();
            ticket.setCreatedDate(new Timestamp(date.getTime()));
            ticket.setUpdatedDate(new Timestamp(date.getTime()));
            ticket.setTicketNote(ticketNewRequest.getTicketNote());
            ticket.setTicketTitle(ticketNewRequest.getTicketTitle());
            if (ticketNewRequest.getPackageID() != null) {
                Packages packages = packageService.getByPackageId(ticketNewRequest.getPackageID());
                ticket.setTicketPackage(packages);
            }
            ticket.setUserSentTicket(user.get());
            ticket.setTicketStatus("CREATED");
            boolean ticketSaveStatus = ticketService.createNewTicket(ticket);
            if (ticketSaveStatus) {
                return ResponseEntity.ok().body("Ticket saved successfully!");
            }
        } catch (Exception e) {
            return ResponseEntity.ok().body("Ticket couldn't be created!");
        }
        return ResponseEntity.ok().body("Ticket couldn't be created!");
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/newticketmessage")
    @ApiOperation(value = "New Ticket Message")
    public ResponseEntity<String> newTicketMessage(@Valid @RequestBody TicketMessageNewRequest ticketMessageNewRequest) {
        try {
            Optional<User> user = userRepository.findByUsername(ticketMessageNewRequest.getUsername());
            Ticket ticket = ticketService.findById(ticketMessageNewRequest.getTicketID());
            TicketMessages ticketMessages = new TicketMessages();
            Date date = new Date();
            ticketMessages.setCreatedDate(new Timestamp(date.getTime()));
            ticketMessages.setUpdatedDate(new Timestamp(date.getTime()));
            ticketMessages.setTicket(ticket);
            ticketMessages.setTicketMessage(ticketMessageNewRequest.getTicketMessage());
            ticketMessages.setUserSentMessage(user.get());
            boolean ticketMessageSaveStatus = ticketMessageService.createNewTicketMessage(ticketMessages);
            if (ticketMessageSaveStatus) {
                return ResponseEntity.ok().body("Ticket Message saved successfully!");
            }
        } catch (Exception e) {
            return ResponseEntity.ok().body("Ticket Message couldn't be created!");
        }
        return ResponseEntity.ok().body("Ticket Message couldn't be created!");
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @PutMapping("/assignticket/{ticketID}/{userName}")
    @ApiOperation(value = "Assign Ticket")
    public ResponseEntity<String> assignTicket(@Valid @PathVariable(value = "ticketID") long ticketID,
                                               @Valid @PathVariable(value = "userName") String userName) {
        try {
            Optional<User> user = userRepository.findByUsername(userName);
            Ticket ticket = ticketService.findById(ticketID);
            ticket.setAssignedUser(user.get());
            ticket.setTicketStatus("ASSIGNED");
            boolean ticketSaveStatus = ticketService.createNewTicket(ticket);
            if (ticketSaveStatus) {
                return ResponseEntity.ok().body("Ticket Assigned successfully!");
            }
        } catch (Exception e) {
            return ResponseEntity.ok().body("Ticket couldn't be Assigned!");
        }
        return ResponseEntity.ok().body("Ticket couldn't be Assigned!");
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/getalltickets/{userName}")
    @ApiOperation(value = "Get Tickets")
    public ResponseEntity<List<Ticket>> getAllTickets(@Valid @PathVariable(value = "userName") String userName) {
        List<Ticket> tickets = null;
        List<Ticket> tickets2 = null;
        try {
            Optional<User> user = userRepository.findByUsername(userName);
            tickets = ticketService.findAllByUserSentTicket(user.get());
            tickets2 = ticketService.findAllByAssignedUser(user.get());
            if (!tickets.isEmpty() || !tickets2.isEmpty())  {
                List<Ticket> newList = new ArrayList<>(tickets);
                newList.addAll(tickets2);
                Collections.reverse(newList);
                return ResponseEntity.ok().body(newList);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(tickets);
        }
        return ResponseEntity.badRequest().body(tickets);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/getallassignedtickets/{userName}")
    @ApiOperation(value = "Get Tickets")
    public ResponseEntity<List<Ticket>> getAllAssignedTickets(@Valid @PathVariable(value = "userName") String userName) {
        List<Ticket> tickets = null;
        try {
            Optional<User> user = userRepository.findByUsername(userName);
            tickets = ticketService.findAllByAssignedUser(user.get());
            if (!tickets.isEmpty())  {
                return ResponseEntity.ok().body(tickets);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(tickets);
        }
        return ResponseEntity.badRequest().body(tickets);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/getallusersenttickets/{userName}")
    @ApiOperation(value = "Get All User Sent Tickets")
    public ResponseEntity<List<Ticket>> getAllUserSentTickets(@Valid @PathVariable(value = "userName") String userName) {
        List<Ticket> tickets = null;
        try {
            Optional<User> user = userRepository.findByUsername(userName);
            tickets = ticketService.findAllByUserSentTicket(user.get());
            if (!tickets.isEmpty())  {
                return ResponseEntity.ok().body(tickets);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(tickets);
        }
        return ResponseEntity.badRequest().body(tickets);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/getallticketmessages/{ticketID}")
    @ApiOperation(value = "Get Tickets")
    public ResponseEntity<List<TicketMessages>> getAllTickets(@Valid @PathVariable(value = "ticketID") Long ticketID) {
        List<TicketMessages> ticketMessages = null;
        try {
            Ticket ticket = ticketService.findById(ticketID);
            ticketMessages = ticketMessageService.findAllByTicketOrderByCreatedDate(ticket);
            if (!ticketMessages.isEmpty())  {
                //Collections.reverse(ticketMessages);
                return ResponseEntity.ok().body(ticketMessages);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ticketMessages);
        }
        return ResponseEntity.badRequest().body(ticketMessages);
    }
}
