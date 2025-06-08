package de.lorenz.ticketsystem.service;


import de.lorenz.ticketsystem.dto.request.TicketCreateRequest;
import de.lorenz.ticketsystem.dto.response.TicketCreateResponse;
import de.lorenz.ticketsystem.dto.response.TicketDeleteResponse;
import de.lorenz.ticketsystem.entity.Ticket;
import de.lorenz.ticketsystem.entity.TicketUser;
import de.lorenz.ticketsystem.repo.TicketRepository;
import de.lorenz.ticketsystem.repo.TicketUserRepository;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class TicketService {

    final TicketRepository ticketRepository;
    final TicketUserRepository ticketUserRepository;
    Ticket ticket;
    Optional<TicketUser> user;


    public ResponseWrapper<?> createTicket(TicketCreateRequest request) {
        user = ticketUserRepository.findById(request.assignedUserId());
        if (user.isEmpty()) {
            return ResponseWrapper.badRequest("User not found", "No user with ID " + request.assignedUserId() + " exists.");
        }

        if (request.title() == null || request.title().isEmpty()) {
            return ResponseWrapper.error("Title cannot be empty");
        }

        if (request.assignedUserId() <= 0) {
            return ResponseWrapper.error("Wrong UserID");
        }

        if (request.type() <= 0) {
            return ResponseWrapper.error("Type cannot be empty");
        }

        if (request.status() <= 0) {
            return ResponseWrapper.error("Status cannot be empty");
        }

        ticket = new Ticket();
        ticket.setTitle(request.title());
        ticket.setType(request.type());
        ticket.setStatus(request.status());
        ticket.setAssignedUser(user.get());
        ticketRepository.save(ticket);

        return ResponseWrapper.ok(new TicketCreateResponse(request.title(), request.assignedUserId(), request.type(), request.status()));
    }

    public ResponseWrapper<?> deleteTicket(long id) {

        ticket = ticketRepository.findById(id).orElse(null);
        if (ticket == null) {
            return ResponseWrapper.badRequest("Ticket not found");
        }
        new TicketDeleteResponse(id, ticket.getTitle(), ticket.getAssignedUser().getUserId());
        ticketRepository.deleteById(id);
        return ResponseWrapper.ok("Deleted ticket with ID " + id);
    }
}
