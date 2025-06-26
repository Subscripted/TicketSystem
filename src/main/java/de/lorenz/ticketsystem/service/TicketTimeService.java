package de.lorenz.ticketsystem.service;

import de.lorenz.ticketsystem.dto.request.TicketTimeSaveRequest;
import de.lorenz.ticketsystem.dto.request.TicketTimeSelectRequest;
import de.lorenz.ticketsystem.dto.request.TicketTimeUpdateRequest;
import de.lorenz.ticketsystem.dto.response.TicketTimeSelectResponse;
import de.lorenz.ticketsystem.entity.Ticket;
import de.lorenz.ticketsystem.entity.TicketTime;
import de.lorenz.ticketsystem.entity.TicketUser;
import de.lorenz.ticketsystem.repo.TicketRepository;
import de.lorenz.ticketsystem.repo.TicketTimeRepository;
import de.lorenz.ticketsystem.repo.TicketUserRepository;
import de.lorenz.ticketsystem.service.lang.LanguageService;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class TicketTimeService {

    TicketTimeRepository ticketTimeRepository;
    TicketUserRepository ticketUserRepository;
    TicketRepository ticketRepository;
    LanguageService languageService;

    public ResponseWrapper<?> saveTime(TicketTimeSaveRequest request) {
        if (request.userId() == null) {
            return ResponseWrapper.badRequest("You need to assign a UserID", getPropMessage("api.response.400", request.lang()));
        }

        if (request.ticketId() == null) {
            return ResponseWrapper.badRequest("You need to assign a TicketID", getPropMessage("api.response.400", request.lang()));
        }

        if (request.zeitInSekunden() == null) {
            return ResponseWrapper.badRequest("You have to assign a valid Time Value in Seconds", getPropMessage("api.response.400", request.lang()));
        }

        Optional<TicketUser> userOpt = ticketUserRepository.findById(request.userId());
        if (userOpt.isEmpty()) {
            return ResponseWrapper.badRequest("User not found.", getPropMessage("api.response.400", request.lang()));
        }

        Optional<Ticket> ticketOpt = ticketRepository.findById(request.ticketId());
        if (ticketOpt.isEmpty()) {
            return ResponseWrapper.badRequest("Ticket not found.", getPropMessage("api.response.400", request.lang()));
        }

        TicketTime ticketTime = new TicketTime();
        ticketTime.setUser(userOpt.get());
        ticketTime.setTicket(ticketOpt.get());
        ticketTime.setTime(request.zeitInSekunden());

        ticketTimeRepository.save(ticketTime);

        return ResponseWrapper.ok("Time entry saved successfully.", getPropMessage("api.response.200", request.lang()));
    }


    public ResponseWrapper<?> selectTime(TicketTimeSelectRequest request) {
        if (request.ticketId() == null) {
            return ResponseWrapper.badRequest("You need to assign a TicketID", getPropMessage("api.response.400", request.lang()));
        }

        Optional<TicketTime> ticketTimeOpt = ticketTimeRepository.findByTicketId(request.ticketId());
        if (ticketTimeOpt.isEmpty()) {
            return ResponseWrapper.badRequest("Ticket not found.", getPropMessage("api.response.400", request.lang()));
        }

        TicketTime ticketTime = ticketTimeOpt.get();
        Integer time = ticketTime.getTime();
        return ResponseWrapper.ok(new TicketTimeSelectResponse(time), getPropMessage("api.response.200", request.lang()));
    }

    public ResponseWrapper<?> updateTime(Long ticketId, TicketTimeUpdateRequest request) {
        if (request.insertId() == null) {
            return ResponseWrapper.badRequest("You need to assign a InsertId fro locating the correct Time", getPropMessage("api.response.400", request.lang()));
        }

        if (request.zeit() == null) {
            return ResponseWrapper.badRequest("You need to assign a valid Time Value in Seconds.", getPropMessage("api.response.400", request.lang()));
        }

        Optional<TicketTime> ticketTimeOpt = ticketTimeRepository.findByTicketId(ticketId);

        if (ticketTimeOpt.isEmpty()) {
            return ResponseWrapper.badRequest("Ticket not found.", getPropMessage("api.response.400", request.lang()));
        }

        TicketTime ticketTime = ticketTimeOpt.get();

        if (request.zeit() < 1) {
            ticketTimeRepository.delete(ticketTime);
            return ResponseWrapper.ok("Ticket Time was deleted", getPropMessage("api.response.200", request.lang()));
        }
        ticketTime.setTime(request.zeit());

        ticketTimeRepository.save(ticketTime);
        return ResponseWrapper.ok("Time updated successfully.", getPropMessage("api.response.200", request.lang()));
    }

    private String getPropMessage(String key, String lang) {
        return languageService.getMessage(key, lang);
    }

}
