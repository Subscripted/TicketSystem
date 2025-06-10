package de.lorenz.ticketsystem.service;

import de.lorenz.ticketsystem.dto.request.TicketCreateRequest;
import de.lorenz.ticketsystem.dto.request.TicketUpdateRequest;
import de.lorenz.ticketsystem.dto.response.TicketCreateResponse;
import de.lorenz.ticketsystem.dto.response.TicketDeleteResponse;
import de.lorenz.ticketsystem.dto.response.TicketUpdateResponse;
import de.lorenz.ticketsystem.entity.Ticket;
import de.lorenz.ticketsystem.entity.TicketUser;
import de.lorenz.ticketsystem.repo.TicketRepository;
import de.lorenz.ticketsystem.repo.TicketUserRepository;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class TicketService {

    final TicketRepository ticketRepository;
    final TicketUserRepository ticketUserRepository;

    public ResponseWrapper<?> createTicket(TicketCreateRequest request) {

        if (request.assignedUserId() == null){
            return ResponseWrapper.error("User not found", "You have to assign a user first.");
        }

        Optional<TicketUser> user = ticketUserRepository.findById(request.assignedUserId());
        Optional<TicketUser> tester = ticketUserRepository.findById(request.assignedTesterId());

        if (user.isEmpty()) {
            return ResponseWrapper.badRequest("User not found", "No user with ID " + request.assignedUserId() + " exists.");
        }


        if (request.title() == null || request.title().isBlank()) {
            return ResponseWrapper.error("Title cannot be empty");
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(request.title());
        ticket.setType(request.type());
        ticket.setNotiz(request.notiz());
        ticket.setStatus(request.status());
        ticket.setAssignedUser(user.get());
        tester.ifPresent(ticket::setAssignedTester);

        ticketRepository.save(ticket);

        return ResponseWrapper.ok(
                new TicketCreateResponse(
                        ticket.getTitle(),
                        ticket.getNotiz(),
                        ticket.getAssignedUser().getUserId(),
                        ticket.getType(),
                        ticket.getStatus(),
                        tester.map(TicketUser::getUserId).orElse(null)
                )
        );
    }

    public ResponseWrapper<?> deleteTicket(long id) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isEmpty()) {
            return ResponseWrapper.badRequest("Ticket not found", "No ticket with ID " + id);
        }

        Ticket ticket = ticketOpt.get();
        ticketRepository.delete(ticket);

        return ResponseWrapper.ok(
                new TicketDeleteResponse(
                        ticket.getId(),
                        ticket.getTitle(),
                        ticket.getAssignedUser().getUserId()
                )
        );
    }

    public ResponseWrapper<?> updateTicket(long id, TicketUpdateRequest request) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isEmpty()) {
            return ResponseWrapper.badRequest("Ticket not found");
        }

        Ticket ticket = ticketOpt.get();
        Map<String, Map<String, Object>> changedFields = new HashMap<>();

        if (request.title() != null && !request.title().isBlank() && !request.title().equals(ticket.getTitle())) {
            changedFields.put("title", Map.of("old", ticket.getTitle(), "new", request.title()));
            ticket.setTitle(request.title());
        }

        if (request.type() != null && request.type() != ticket.getType()) {
            changedFields.put("type", Map.of("old", ticket.getType(), "new", request.type()));
            ticket.setType(request.type());
        }

        if (request.status() != null && request.status() != ticket.getStatus()) {
            changedFields.put("status", Map.of("old", ticket.getStatus(), "new", request.status()));
            ticket.setStatus(request.status());
        }

        if (request.testerId() != null && !Objects.equals(request.testerId(), getIdOrNull(ticket.getAssignedTester()))) {
            Optional<TicketUser> tester = ticketUserRepository.findById(request.testerId());
            if (tester.isEmpty()) {
                return ResponseWrapper.badRequest("Tester user not found");
            }

            changedFields.put("testerId", Map.of(
                    "old", getIdOrNull(ticket.getAssignedTester()),
                    "new", request.testerId()
            ));
            ticket.setAssignedTester(tester.get());
        }

        if (request.assignedUserId() != null && !Objects.equals(request.assignedUserId(), getIdOrNull(ticket.getAssignedUser()))) {
            Optional<TicketUser> user = ticketUserRepository.findById(request.assignedUserId());
            if (user.isEmpty()) {
                return ResponseWrapper.badRequest("Assigned user not found");
            }

            changedFields.put("assignedUserId", Map.of(
                    "old", getIdOrNull(ticket.getAssignedUser()),
                    "new", request.assignedUserId()
            ));
            ticket.setAssignedUser(user.get());
        }

        ticketRepository.save(ticket);
        return ResponseWrapper.ok(new TicketUpdateResponse(changedFields));
    }

    private Long getIdOrNull(TicketUser user) {
        return user != null ? user.getUserId() : null;
    }
}