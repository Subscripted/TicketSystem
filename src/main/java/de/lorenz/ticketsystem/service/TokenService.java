package de.lorenz.ticketsystem.service;

import de.lorenz.ticketsystem.dto.request.TokenCreateRequest;
import de.lorenz.ticketsystem.dto.response.TokenResponse;
import de.lorenz.ticketsystem.entity.ApiToken;
import de.lorenz.ticketsystem.entity.ApiLoginCreds;
import de.lorenz.ticketsystem.globals.GlobalExceptionMsg;
import de.lorenz.ticketsystem.repo.ApiTokenRepository;
import de.lorenz.ticketsystem.repo.LoginCredsRepository;
import de.lorenz.ticketsystem.service.lang.LanguageService;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TokenService {

    ApiTokenRepository apiTokenRepository;
    LoginCredsRepository loginCredsRepository;
    final LanguageService languageService;

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

        if (request.email() == null || request.email().isEmpty()) {
            return ResponseWrapper.badRequest("Email ist leer oder wurde nicht übergeben","");
        }

        if (request.clientId() == null || request.clientId().isEmpty()) {
            return ResponseWrapper.badRequest("ClientID ist leer oder wurde nicht übergeben","");
        }

        if (request.clientSecret() == null || request.clientSecret().isEmpty()) {
            return ResponseWrapper.badRequest("ClientSecret ist leer oder wurde nicht übergeben","");
        }

        Optional<ApiLoginCreds> creds = loginCredsRepository.findByEmailAndClientIdAndClientSecret(

                request.email(),
                request.clientId(),
                request.clientSecret()
        );

        TokenResponse response;

        if (creds.isEmpty()) {
            response = new TokenResponse(null, null);
            return ResponseWrapper.unauthorized(response, getPropMessage("api.response.401", request.lang()));
        }

        Optional<String> existingToken = getValidToken(request.email());
        if (existingToken.isPresent()) {
            response = new TokenResponse(existingToken.get(), getExpirationTimeInSeconds(existingToken.get()));

            return ResponseWrapper.ok(response, getPropMessage("api.response.200", request.lang()));
        }

        String newToken = generateToken(request.email());
        response = new TokenResponse(newToken, getExpirationTimeInSeconds(newToken));
        return ResponseWrapper.ok(response, getPropMessage("api.response.200", request.lang()));

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

    private String getPropMessage(String key, String lang) {
        return languageService.getMessage(key, lang);
    }

    private Integer getExpirationTimeInSeconds(String tokenValue) {
        Optional<ApiToken> optionalToken = apiTokenRepository.findByToken(tokenValue);

        if (optionalToken.isEmpty()) {
            return 0;
        }

        ApiToken token = optionalToken.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = token.getExpiresAt();

        if (expiresAt.isBefore(now)) {
            return 0;
        }

        return (int) Duration.between(now, expiresAt).getSeconds();
    }
}
