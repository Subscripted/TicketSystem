package de.lorenz.ticketsystem.service;

import de.lorenz.ticketsystem.dto.request.TicketCreateRequest;
import de.lorenz.ticketsystem.dto.request.TicketDeleteRequest;
import de.lorenz.ticketsystem.dto.request.TicketSelectRequest;
import de.lorenz.ticketsystem.dto.request.TicketUpdateRequest;
import de.lorenz.ticketsystem.dto.response.TicketCreateResponse;
import de.lorenz.ticketsystem.dto.response.TicketDeleteResponse;
import de.lorenz.ticketsystem.dto.response.TicketSelectResponse;
import de.lorenz.ticketsystem.dto.response.TicketUpdateResponse;
import de.lorenz.ticketsystem.entity.Ticket;
import de.lorenz.ticketsystem.entity.TicketUser;
import de.lorenz.ticketsystem.repo.TicketRepository;
import de.lorenz.ticketsystem.repo.TicketUserRepository;
import de.lorenz.ticketsystem.service.lang.LanguageService;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class TicketService {

    TicketRepository ticketRepository;
    TicketUserRepository ticketUserRepository;
    LanguageService languageService;

    public ResponseWrapper<?> createTicket(TicketCreateRequest request) {

        if (request.assignedUserId() == null) {
            return ResponseWrapper.error("User not found", "You have to assign a user first.");
        }

        Optional<TicketUser> user = ticketUserRepository.findById(request.assignedUserId());
        if (user.isEmpty()) {
            return ResponseWrapper.badRequest("No user with ID " + request.assignedUserId() + " exists.",
                    getPropMessage("api.response.400", request.lang()));
        }

        if (request.title() == null || request.title().isBlank()) {
            return ResponseWrapper.badRequest("Title cannot be empty", getPropMessage("api.response.400", request.lang()));
        }

        if (request.type() == null) {
            return ResponseWrapper.badRequest("Type cannot be empty", getPropMessage("api.response.400", request.lang()));
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(request.title());
        ticket.setType(request.type());
        ticket.setNotiz(request.notiz());
        ticket.setStatus(request.status());
        ticket.setAssignedUser(user.get());

        if (request.assignedTesterId() != null) {
            Optional<TicketUser> tester = ticketUserRepository.findById(request.assignedTesterId());
            if (tester.isEmpty()) {
                return ResponseWrapper.badRequest("No tester with ID " + request.assignedTesterId() + " exists.",
                        getPropMessage("api.response.400", request.lang()));
            }
            ticket.setAssignedTester(tester.get());
        }

        ticketRepository.save(ticket);

        return ResponseWrapper.ok(
                new TicketCreateResponse(
                        ticket.getTitle(),
                        ticket.getNotiz(),
                        ticket.getAssignedUser().getUserId(),
                        ticket.getType(),
                        ticket.getStatus(),
                        ticket.getAssignedTester() != null ? ticket.getAssignedTester().getUserId() : null
                ),
                getPropMessage("api.response.200", request.lang())
        );
    }

    public ResponseWrapper<?> deleteTicket(long id, TicketDeleteRequest request) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isEmpty()) {
            return ResponseWrapper.badRequest("No ticket with ID " + id, getPropMessage("api.response.400", request.lang()));
        }

        Ticket ticket = ticketOpt.get();
        ticketRepository.delete(ticket);

        return ResponseWrapper.ok(
                new TicketDeleteResponse(
                        ticket.getId(),
                        ticket.getTitle(),
                        ticket.getAssignedUser().getUserId()
                ),
                getPropMessage("api.response.200", request.lang())
        );
    }

    public ResponseWrapper<?> updateTicket(long id, TicketUpdateRequest request) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isEmpty()) {
            return ResponseWrapper.badRequest(getPropMessage("api.response.400", request.lang()),"Ticket not found");
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
                return ResponseWrapper.badRequest("Tester user not found", getPropMessage("api.response.400", request.lang()));
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
                return ResponseWrapper.badRequest("Assigned user not found", getPropMessage("api.response.400", request.lang()));
            }

            changedFields.put("assignedUserId", Map.of(
                    "old", getIdOrNull(ticket.getAssignedUser()),
                    "new", request.assignedUserId()
            ));
            ticket.setAssignedUser(user.get());
        }

        if (changedFields.isEmpty()){
            return ResponseWrapper.ok("Es wurde kein Feld geupdated",  getPropMessage("api.response.200", request.lang()));
        }

        ticketRepository.save(ticket);
        return ResponseWrapper.ok(new TicketUpdateResponse(changedFields), getPropMessage("api.response.200", request.lang()));
    }

    public ResponseWrapper<?> selectTickets(TicketSelectRequest request) {
        return getTicketsWithRules(request);
    }


    private ResponseWrapper<?> getTicketsWithRules(TicketSelectRequest request) {
        List<Ticket> tickets = ticketRepository.findByFilter(
                request.id(),
                request.assignedUserId(),
                request.type(),
                request.status()
        );

        if (tickets.isEmpty()) {
            return ResponseWrapper.ok(List.of(), "No tickets found");
        }

        List<TicketSelectResponse> responseList = tickets.stream()
                .map(ticket -> new TicketSelectResponse(
                        ticket.getId(),
                        ticket.getStatus(),
                        ticket.getType(),
                        getIdOrNull(ticket.getAssignedUser()),
                        getIdOrNull(ticket.getAssignedTester()),
                        ticket.getTitle(),
                        ticket.getNotiz()
                ))
                .toList();

        return ResponseWrapper.ok(responseList, getPropMessage("api.response.200", request.lang()));
    }


    private Long getIdOrNull(TicketUser user) {
        return user != null ? user.getUserId() : null;
    }

    private String getPropMessage(String key, String lang) {
        return languageService.getMessage(key, lang);
    }
}