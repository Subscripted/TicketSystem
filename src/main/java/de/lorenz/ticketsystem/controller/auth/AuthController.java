package de.lorenz.ticketsystem.controller.auth;

import de.lorenz.ticketsystem.controller.ControllerDefaults;
import de.lorenz.ticketsystem.dto.request.TokenCreateRequest;
import de.lorenz.ticketsystem.service.TokenService;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ticket/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthController extends ControllerDefaults {

    private final TokenService tokenService;

    @PostMapping("/token")
    public ResponseWrapper<?> getToken(@RequestBody TokenCreateRequest request) {
        return tokenService.createToken(request);

    }

    @Override
    protected String getVersionString() {
        return "auth-v1.0.0";
    }
}
