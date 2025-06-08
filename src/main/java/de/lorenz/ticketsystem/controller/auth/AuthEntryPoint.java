package de.lorenz.ticketsystem.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        response.setStatus(501);
        response.setContentType("application/json");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Credentials missing or invalid.");

        ObjectMapper mapper = new ObjectMapper();
        response.getOutputStream().println(mapper.writeValueAsString(errorResponse));
    }
}
