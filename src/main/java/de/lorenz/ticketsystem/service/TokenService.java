package de.lorenz.ticketsystem.service;

import de.lorenz.ticketsystem.entity.ApiToken;
import de.lorenz.ticketsystem.repo.ApiTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenService {

    private final ApiTokenRepository apiTokenRepository;

    public TokenService(ApiTokenRepository apiTokenRepository) {
        this.apiTokenRepository = apiTokenRepository;
    }

    private static final String TOKEN_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

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
}
