package de.lorenz.ticketsystem.controller.auth;

import de.lorenz.ticketsystem.dto.request.TokenCreateRequest;
import de.lorenz.ticketsystem.entity.LoginCreds;
import de.lorenz.ticketsystem.globals.GlobalExceptionMsg;
import de.lorenz.ticketsystem.repo.LoginCredsRepository;
import de.lorenz.ticketsystem.service.TokenService;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthController {

    private final TokenService tokenService;
    private final LoginCredsRepository loginCredsRepository;

    @PostMapping("/token")
    public ResponseWrapper<Map<String, String>> getToken(@RequestBody TokenCreateRequest tokenRequest) {

        Optional<LoginCreds> creds = loginCredsRepository.findByEmailAndClientIdAndClientSecret(

                tokenRequest.email(),
                tokenRequest.clientId(),
                tokenRequest.clientSecret()
        );

        Map<String, String> response = new HashMap<>();
        if (creds.isEmpty()) {
            response.put("message", GlobalExceptionMsg.UNAUTHORIZED.getExceptionMsg());
            return ResponseWrapper.unauthorized(response, GlobalExceptionMsg.WRONG_LOGIN_CREDS.getExceptionMsg());
        }

        Optional<String> existingToken = tokenService.getValidToken(tokenRequest.email());

        response = new HashMap<>();
        if (existingToken.isPresent()) {
            response.put("token", existingToken.get());
            return ResponseWrapper.ok(response);
        }

        String newToken = tokenService.generateToken(tokenRequest.email());
        response.put("message", GlobalExceptionMsg.TOKEN_RESPONSE.getExceptionMsg());
        response.put("token", newToken);
        return ResponseWrapper.ok(response);

    }
}
