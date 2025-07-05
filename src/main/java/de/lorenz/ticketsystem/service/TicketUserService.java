package de.lorenz.ticketsystem.service;

import de.lorenz.ticketsystem.dto.request.TicketUserCreateRequest;
import de.lorenz.ticketsystem.dto.request.TicketUserDeleteRequest;
import de.lorenz.ticketsystem.dto.request.TicketUserUpdateRequest;
import de.lorenz.ticketsystem.dto.response.TicketUserDeleteResponse;
import de.lorenz.ticketsystem.dto.response.TicketUserUpdateResponse;
import de.lorenz.ticketsystem.entity.TicketLoginCreds;
import de.lorenz.ticketsystem.entity.TicketUser;
import de.lorenz.ticketsystem.repo.LoginRepository;
import de.lorenz.ticketsystem.repo.TicketUserRepository;
import de.lorenz.ticketsystem.service.lang.LanguageService;
import de.lorenz.ticketsystem.utils.APIUtils;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class TicketUserService {

    final LoginRepository loginRepository;
    final TicketUserRepository ticketUserRepository;
    final LanguageService languageService;
    TicketUser user;

    public ResponseWrapper<?> createTicketUser(TicketUserCreateRequest request) {
        if (request.email() == null || request.email().isEmpty()) {
            return ResponseWrapper.badRequest("Email cannot be empty", getPropMessage("api.response.400", request.lang()));
        }

        if (request.name() == null || request.name().isEmpty()) {
            return ResponseWrapper.badRequest("Name cannot be empty", getPropMessage("api.response.400", request.lang()));
        }

        Optional<TicketLoginCreds> optionalCreds = loginRepository.findByEmail(request.email());

        if (optionalCreds.isEmpty()) {
            return ResponseWrapper.badRequest("Login credentials not found for email " + request.email(), getPropMessage("api.response.400", request.lang()));
        }

        TicketLoginCreds creds = optionalCreds.get();

        if (ticketUserRepository.existsById(creds.getId())) {
            return ResponseWrapper.badRequest("User already exists for this email", getPropMessage("api.response.400", request.lang()));
        }

        TicketUser user = new TicketUser();
        user.setName(request.name());
        user.setShortName(APIUtils.shortenName(request.name()));
        user.setLoginCreds(creds);  // falls du die @OneToOne-Beziehung auch setzen willst

        ticketUserRepository.save(user);
        return ResponseWrapper.ok(request, getPropMessage("api.response.200", request.lang()));
    }



    public ResponseWrapper<?> deleteTicketUser(Long id, TicketUserDeleteRequest request) {
        if (!ticketUserRepository.existsById(id)) {
            return ResponseWrapper.badRequest("User does not exist", getPropMessage("api.response.400", request.lang()));
        }

        user = ticketUserRepository.findById(id).orElse(null);
        assert user != null;

        TicketLoginCreds creds = user.getLoginCreds();
        TicketUserDeleteResponse response = new TicketUserDeleteResponse(user.getName(), user.getLoginCreds().getEmail(), LocalDateTime.now());

        loginRepository.delete(creds);
        return ResponseWrapper.ok(response, getPropMessage("api.response.200", request.lang()));
    }

    public ResponseWrapper<?> updateTicketUser(Long id, TicketUserUpdateRequest request) {
        TicketUser user = ticketUserRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseWrapper.badRequest("User does not exist", getPropMessage("api.response.400", request.lang()));
        }

        Map<String, Object> changedFields = new HashMap<>();

        if (request.name() != null) {
            user.setName(request.name());
            changedFields.put("username", request.name());
        }

        if (request.email() != null) {
            TicketLoginCreds creds = user.getLoginCreds();
            if (!request.email().equals(creds.getEmail())) {
                if (loginRepository.findByEmail(request.email()).isPresent()) {
                    return ResponseWrapper.badRequest("E-Mail already in use", getPropMessage("api.response.400", request.lang()));
                }
                creds.setEmail(request.email());
                changedFields.put("email", request.email());
            }
        }

        if (request.shortName() != null) {
            user.setShortName(request.shortName());
            changedFields.put("shortName", request.shortName());
        }

        ticketUserRepository.save(user);

        return ResponseWrapper.ok(
                new TicketUserUpdateResponse(id, changedFields),
                getPropMessage("api.response.200", request.lang())
        );
    }

    private String getPropMessage(String key, String lang) {
        return languageService.getMessage(key, lang);
    }
}
