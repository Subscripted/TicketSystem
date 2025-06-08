package de.lorenz.ticketsystem.controller.ticket;


import de.lorenz.ticketsystem.dto.request.TicketCreateRequest;
import de.lorenz.ticketsystem.entity.Ticket;
import de.lorenz.ticketsystem.service.TicketService;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ticket/tickets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketController {


    final TicketService ticketService;


    @PostMapping("/create")
    public ResponseWrapper<?> createTicket(@RequestBody TicketCreateRequest request) {
       return ticketService.createTicket(request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseWrapper<?> deleteTicket(@PathVariable long id) {
        return ticketService.deleteTicket(id);
    }


}
