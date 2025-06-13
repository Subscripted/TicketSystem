package de.lorenz.ticketsystem.controller.ticket;


import de.lorenz.ticketsystem.dto.request.TicketTimeSaveRequest;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ticket/time")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketTimeController {

    @PostMapping("/save/{id}")
    public ResponseWrapper<?> saveTime(@ResponseBody TicketTimeSaveRequest) {

        return
    }
}
