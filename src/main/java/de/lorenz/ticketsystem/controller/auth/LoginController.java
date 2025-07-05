package de.lorenz.ticketsystem.controller.auth;

import de.lorenz.ticketsystem.controller.ControllerDefaults;
import de.lorenz.ticketsystem.dto.request.CreateLoginRequest;
import de.lorenz.ticketsystem.dto.request.LoginRequest;
import de.lorenz.ticketsystem.service.LoginService;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ticket/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginController extends ControllerDefaults {

    LoginService loginService;

    @PostMapping("/login")
    public ResponseWrapper<?> login(@RequestBody LoginRequest request) {
        return loginService.login(request);
    }


    @PostMapping("/create/login")
    public ResponseWrapper<?> createLogin(@RequestBody CreateLoginRequest request) {
        return loginService.createLogin(request);
    }


    @Override
    protected String getVersionString() {
        return "logi-v1.0.0";
    }
}
