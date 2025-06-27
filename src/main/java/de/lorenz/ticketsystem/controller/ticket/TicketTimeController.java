package de.lorenz.ticketsystem.controller.ticket;


import de.lorenz.ticketsystem.controller.ControllerDefaults;
import de.lorenz.ticketsystem.dto.request.TicketTimeSaveRequest;
import de.lorenz.ticketsystem.dto.request.TicketTimeSelectRequest;
import de.lorenz.ticketsystem.dto.request.TicketTimeUpdateRequest;
import de.lorenz.ticketsystem.service.TicketTimeService;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ticket/time")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketTimeController extends ControllerDefaults {

    final TicketTimeService ticketTimeService;

    @PostMapping("/save")
    public ResponseWrapper<?> saveTime(@RequestBody TicketTimeSaveRequest request) {
        return ticketTimeService.saveTime(request);
    }

    @GetMapping("/select")
    public ResponseWrapper<?> selectTime(@RequestBody TicketTimeSelectRequest request) {
        return ticketTimeService.selectTime(request);
    }

    @PatchMapping("/update/{ticketId}")
    public ResponseWrapper<?> updateTime(@PathVariable Long ticketId, @RequestBody TicketTimeUpdateRequest request) {
        return ticketTimeService.updateTime(ticketId, request);
    }

    @Override
    protected String getVersionString() {
        return "TicketTime-v1.0.0";
    }
}
