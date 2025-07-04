package de.lorenz.ticketsystem.controller.ticket;


import de.lorenz.ticketsystem.controller.ControllerDefaults;
import de.lorenz.ticketsystem.dto.request.TicketCreateRequest;
import de.lorenz.ticketsystem.dto.request.TicketDeleteRequest;
import de.lorenz.ticketsystem.dto.request.TicketSelectRequest;
import de.lorenz.ticketsystem.dto.request.TicketUpdateRequest;
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
public class TicketController extends ControllerDefaults {

    final TicketService ticketService;

    @PostMapping("/create")
    public ResponseWrapper<?> createTicket(@RequestBody TicketCreateRequest request) {
        return ticketService.createTicket(request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseWrapper<?> deleteTicket(@PathVariable long id, @RequestBody TicketDeleteRequest request) {
        return ticketService.deleteTicket(id, request);
    }

    @PatchMapping("/update/{id}")
    public ResponseWrapper<?> updateTicket(@PathVariable Long id, @RequestBody TicketUpdateRequest request) {
        return ticketService.updateTicket(id, request);
    }

    @PostMapping("/select")
    public ResponseWrapper<?> selectTickets(@RequestBody TicketSelectRequest request) {
        return ticketService.selectTickets(request);
    }

    @Override
    protected String getVersionString() {
        return "Tickets-v1.0.0";
    }

}
