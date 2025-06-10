package de.lorenz.ticketsystem.service;

import de.lorenz.ticketsystem.dto.request.TokenCreateRequest;
import de.lorenz.ticketsystem.entity.ApiToken;
import de.lorenz.ticketsystem.entity.LoginCreds;
import de.lorenz.ticketsystem.globals.GlobalExceptionMsg;
import de.lorenz.ticketsystem.repo.ApiTokenRepository;
import de.lorenz.ticketsystem.repo.LoginCredsRepository;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TokenService {

    final ApiTokenRepository apiTokenRepository;
    final LoginCredsRepository loginCredsRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanExpiredTokens() {
        apiTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    public Optional<String> getValidToken(String email) {
        LocalDateTime now = LocalDateTime.now();
        return apiTokenRepository.findByEmailAndExpiresAtAfter(email, now)
                .map(ApiToken::getToken);
    }

    public ResponseWrapper<?> createToken(TokenCreateRequest request) {
        Optional<LoginCreds> creds = loginCredsRepository.findByEmailAndClientIdAndClientSecret(

                request.email(),
                request.clientId(),
                request.clientSecret()
        );

        Map<String, String> response = new HashMap<>();
        if (creds.isEmpty()) {
            response.put("message", GlobalExceptionMsg.UNAUTHORIZED.getExceptionMsg());
            return ResponseWrapper.unauthorized(response, GlobalExceptionMsg.WRONG_LOGIN_CREDS.getExceptionMsg());
        }

        Optional<String> existingToken = getValidToken(request.email());

        response = new HashMap<>();
        if (existingToken.isPresent()) {
            response.put("token", existingToken.get());
            return ResponseWrapper.ok(response);
        }

        String newToken = generateToken(request.email());
        response.put("message", GlobalExceptionMsg.TOKEN_RESPONSE.getExceptionMsg());
        response.put("token", newToken);
        return ResponseWrapper.ok(response);

    }


    public String generateToken(String email) {
        String tokenValue = randomToken(32);
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
        ApiToken token = new ApiToken();
        token.setEmail(email);
        token.setExpiresAt(expiresAt);
        token.setToken(tokenValue);

        apiTokenRepository.save(token);
        return tokenValue;
    }

    private String randomToken(int length) {
        String TOKEN_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * TOKEN_CHARS.length());
            token.append(TOKEN_CHARS.charAt(index));
        }
        return token.toString();
    }


    public boolean isTokenValid(String tokenValue) {
        return apiTokenRepository.findByToken(tokenValue)
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}
