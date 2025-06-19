package de.lorenz.ticketsystem.service;

import de.lorenz.ticketsystem.dto.request.TicketTimeSaveRequest;
import de.lorenz.ticketsystem.dto.request.TicketTimeSelectRequest;
import de.lorenz.ticketsystem.dto.response.TicketTimeSelectResponse;
import de.lorenz.ticketsystem.entity.Ticket;
import de.lorenz.ticketsystem.entity.TicketTime;
import de.lorenz.ticketsystem.entity.TicketUser;
import de.lorenz.ticketsystem.repo.TicketRepository;
import de.lorenz.ticketsystem.repo.TicketTimeRepository;
import de.lorenz.ticketsystem.repo.TicketUserRepository;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;


//todo: Anstatt .get bei Optional<> zu nutzen, bitte .orElseThrow

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class TicketTimeService {

    final TicketTimeRepository ticketTimeRepository;
    final TicketUserRepository ticketUserRepository;
    final TicketRepository ticketRepository;

    public ResponseWrapper<?> saveTime(TicketTimeSaveRequest request) {
        if (request.userId() == null) {
            return ResponseWrapper.error("Invalid UserID", "You need to assign a UserID");
        }

        if (request.ticketId() == null) {
            return ResponseWrapper.error("Invalid TicketID", "You need to assign a TicketID");
        }

        if (request.zeitInSekunden() == null) {
            return ResponseWrapper.error("Time error", "You have to assign a valid Time Value in Seconds");
        }

        Optional<TicketUser> userOpt = ticketUserRepository.findById(request.userId());
        if (userOpt.isEmpty()) {
            return ResponseWrapper.error("Invalid UserID", "User not found.");
        }

        Optional<Ticket> ticketOpt = ticketRepository.findById(request.ticketId());
        if (ticketOpt.isEmpty()) {
            return ResponseWrapper.error("Invalid TicketID", "Ticket not found.");
        }

        TicketTime ticketTime = new TicketTime();
        ticketTime.setUser(userOpt.get());
        ticketTime.setTicket(ticketOpt.get());
        ticketTime.setTime(request.zeitInSekunden());

        ticketTimeRepository.save(ticketTime);

        return ResponseWrapper.ok("Time entry saved successfully.");
    }


    public ResponseWrapper<?> selectTime(TicketTimeSelectRequest request) {
        if (request.ticketId() == null) {
            return ResponseWrapper.error("Invalid TicketID", "You need to assign a TicketID");
        }

        Optional<TicketTime> ticketTimeOpt = ticketTimeRepository.findByTicketId(request.ticketId());
        if (ticketTimeOpt.isEmpty()) {
            return ResponseWrapper.error("Invalid TicketID", "Ticket not found.");
        }

        TicketTime ticketTime = ticketTimeOpt.get();
        Integer time = ticketTime.getTime();
        return ResponseWrapper.ok(new TicketTimeSelectResponse(time), "Time selected successfully.");
    }

}
