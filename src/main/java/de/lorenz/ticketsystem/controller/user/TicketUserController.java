package de.lorenz.ticketsystem.controller.user;


import de.lorenz.ticketsystem.controller.ControllerDefaults;
import de.lorenz.ticketsystem.dto.request.TicketUserCreateRequest;
import de.lorenz.ticketsystem.dto.request.TicketUserDeleteRequest;
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
public class TicketUserController extends ControllerDefaults {

    final TicketUserService ticketUserService;

    @PostMapping("/create")
    public ResponseWrapper<?> createTicketUser(@RequestBody TicketUserCreateRequest request) {
        return ticketUserService.createTicketUser(request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseWrapper<?> deleteTicketUser(@PathVariable Long id, @RequestBody TicketUserDeleteRequest request) {
        return ticketUserService.deleteTicketUser(id, request);
    }

   @PatchMapping("/update/{id}")
   public ResponseWrapper<?> updateTicketUser(@PathVariable Long id, @RequestBody TicketUserUpdateRequest request) {
        return ticketUserService.updateTicketUser(id, request);
   }

    @Override
    protected String getVersionString() {
        return "TicketUser-v1.0.0";
    }
}

