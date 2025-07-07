package de.lorenz.ticketsystem.service;


import de.lorenz.ticketsystem.dto.request.CreateLoginRequest;
import de.lorenz.ticketsystem.dto.request.LoginRequest;
import de.lorenz.ticketsystem.entity.TicketLoginCreds;
import de.lorenz.ticketsystem.entity.TicketUser;
import de.lorenz.ticketsystem.repo.LoginRepository;
import de.lorenz.ticketsystem.repo.TicketUserRepository;
import de.lorenz.ticketsystem.security.BCryptPasswordManager;
import de.lorenz.ticketsystem.utils.APIUtils;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginService {

    LoginRepository loginRepository;
    TicketUserRepository ticketUserRepository;
    BCryptPasswordManager bCryptPasswordManager;
    APIUtils apiUtils;

    public ResponseWrapper<?> login(LoginRequest request) {
        String identifier = request.identifier();
        String password = request.password();
        TicketLoginCreds creds = null;

        if (identifier == null || identifier.isBlank()) {
            return ResponseWrapper.badRequest("Missing identifier");
        }

        if (password == null || password.isBlank()) {
            return ResponseWrapper.badRequest("Missing password");
        }

        if (apiUtils.validateEmail(identifier)) {
            // E-Mail Login
            creds = loginRepository.findByEmail(identifier).orElse(null);
        } else {
            // Username Login
            Optional<TicketUser> userOpt = ticketUserRepository.findByName(identifier);
            if (userOpt.isPresent()) {
                creds = userOpt.get().getLoginCreds();
            }
        }

        if (creds == null || !bCryptPasswordManager.passwordMatches(password, creds.getPasswordHash())) {
            return ResponseWrapper.unauthorized("Invalid credentials");
        }

        return ResponseWrapper.ok("Login successful", null);
    }


    public ResponseWrapper<?> createLogin(CreateLoginRequest request) {

        if (request.email() == null || request.email().isBlank()) {
            return ResponseWrapper.badRequest("Missing email");
        }

        if (!apiUtils.validateEmail(request.email())) {
            return ResponseWrapper.badRequest("Invalid email format");
        }

        if (loginRepository.findByEmail(request.email()).isPresent()) {
            return ResponseWrapper.badRequest("Email already registered");
        }

        if (request.password() == null || request.password().isBlank()) {
            return ResponseWrapper.badRequest("Missing password");
        }

        TicketLoginCreds account = new TicketLoginCreds();
        account.setEmail(request.email());
        account.setPasswordHash(bCryptPasswordManager.encodePassword(request.password()));

        loginRepository.save(account);

        return ResponseWrapper.ok("Created a new Login", "Account Created");
    }

}
