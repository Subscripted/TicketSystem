package de.lorenz.ticketsystem.service;

import de.lorenz.ticketsystem.dto.request.TicketUserCreateRequest;
import de.lorenz.ticketsystem.dto.request.TicketUserDeleteRequest;
import de.lorenz.ticketsystem.dto.request.TicketUserUpdateRequest;
import de.lorenz.ticketsystem.dto.response.TicketUserDeleteResponse;
import de.lorenz.ticketsystem.dto.response.TicketUserUpdateResponse;
import de.lorenz.ticketsystem.entity.TicketUser;
import de.lorenz.ticketsystem.repo.TicketUserRepository;
import de.lorenz.ticketsystem.service.lang.LanguageService;
import de.lorenz.ticketsystem.utils.NameUtils;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class TicketUserService {

    final TicketUserRepository ticketUserRepository;
    final LanguageService languageService;
    TicketUser user;

    public ResponseWrapper<?> createTicketUser(TicketUserCreateRequest request) {

        if (request.email() == null || request.email().isEmpty()) {
            return ResponseWrapper.error("Email cannot be empty");
        }

        if (request.name() == null || request.name().isEmpty()) {
            return ResponseWrapper.error("Name cannot be empty");
        }
        if (ticketUserRepository.existsByEmail(request.email())) {
            return ResponseWrapper.badRequest("Email already exists", "The email address already exists as account" + request.email());
        }
        user = new TicketUser();
        user.setEmail(request.email());
        user.setName(request.name());
        user.setShortName(NameUtils.shortenName(request.name()));
        ticketUserRepository.save(user);
        return ResponseWrapper.ok(request, getPropMessage("api.response.200", request.lang()));
    }

    public ResponseWrapper<?> deleteTicketUser(Long id, TicketUserDeleteRequest request) {
        if (!ticketUserRepository.existsById(id)) {
            return ResponseWrapper.badRequest("User does not exist", "User with given ID does not exist");
        }

        user = ticketUserRepository.findById(id).orElse(null);
        assert user != null;
        TicketUserDeleteResponse response = new TicketUserDeleteResponse(user.getName(), user.getEmail(), LocalDateTime.now());
        ticketUserRepository.deleteById(id);
        return ResponseWrapper.ok(response, getPropMessage("api.reponse.200", request.lang()));
    }

    public ResponseWrapper<?> updateTicketUser(Long id, TicketUserUpdateRequest request) {
        user = ticketUserRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseWrapper.badRequest("User does not exist", "User with given ID does not exist");
        }

        Map<String, Object> changedFields = new HashMap<>();
        if (request.name() != null) {
            user.setName(request.name());
            changedFields.put("username", request.name());
        }

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (ticketUserRepository.existsByEmail(request.email())) {
                return ResponseWrapper.error("E-Mail already in use");
            }
            user.setEmail(request.email());
            changedFields.put("email", user.getEmail());
        }

        if (request.shortName() != null) {
            user.setShortName(request.shortName());
            changedFields.put("shortName", request.shortName());
        }
        ticketUserRepository.save(user);
        return ResponseWrapper.ok(new TicketUserUpdateResponse(id, changedFields), "Test");
    }

    private String getPropMessage(String key, String lang) {
        return languageService.getMessage(key, lang);
    }
}
