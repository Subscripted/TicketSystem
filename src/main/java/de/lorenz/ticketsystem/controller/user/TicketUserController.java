package de.lorenz.ticketsystem.controller.user;


import de.lorenz.ticketsystem.dto.request.TicketUserCreateRequest;
import de.lorenz.ticketsystem.dto.request.TicketUserDeteleRequest;
import de.lorenz.ticketsystem.dto.request.TicketUserUpdateRequest;
import de.lorenz.ticketsystem.service.TicketUserService;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/ticket/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketUserController {

    final TicketUserService ticketUserService;

    @PostMapping("/create")
    public ResponseWrapper<?> createTicketUser(@RequestBody TicketUserCreateRequest request) {
        return ticketUserService.createTicketUser(request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseWrapper<?> deleteTicketUser(@PathVariable Long id) {
        return ticketUserService.deleteTicketUser(id);
    }

   @PatchMapping("/update/{id}")
   public ResponseWrapper<?> updateTicketUser(@PathVariable Long id, @RequestBody TicketUserUpdateRequest request) {
        return ticketUserService.updateTicketUser(id, request);
   }
}

