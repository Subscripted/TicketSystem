package de.lorenz.ticketsystem.globals;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import de.lorenz.ticketsystem.service.lang.LanguageService;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

   final LanguageService languageService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("error", "An error occurred");
        response.put("details", ex.getMessage());
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Permission denied");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(403).body(response);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseWrapper<?> handleJsonErrors(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Throwable cause = ex.getCause();
        Map<String, Object> response = new HashMap<>();
        response.put("path", request.getRequestURI());

        if (cause instanceof UnrecognizedPropertyException unrecognized) {
            String field = unrecognized.getPropertyName();
            response.put("error", "Unknown JSON property");
            response.put("hint", "This field is not allowed or misspelled: " + field);
            response.put("location", "Line: " + unrecognized.getLocation().getLineNr() + ", Column: " + unrecognized.getLocation().getColumnNr());
            return ResponseWrapper.badRequest(response, "Invalid JSON field");
        }

        if (ex.getMessage() != null && ex.getMessage().contains("Required request body is missing")) {
            response.put("error", "Missing request body");
            response.put("message", "Ein JSON-Body ist erforderlich, wurde aber nicht übergeben.");
            return ResponseWrapper.badRequest(response, getPropMessage("api.response.400", "en"));
        }

        if (cause != null && cause.getLocalizedMessage() != null) {
            response.put("error", cause.getClass().getSimpleName());
            response.put("message", cause.getLocalizedMessage());
        } else {
            response.put("error", "Unreadable JSON body");
            response.put("message", ex.getMessage());
        }

        return ResponseWrapper.badRequest(response, getPropMessage("api.response.400", "en"));
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleRateLimit(RateLimitException ex) {
        return ResponseEntity
                .status(429)
                .body(new ResponseWrapper<>(
                        null,
                        429,
                        Map.of(
                                "message", ex.getMessage(),
                                "retryAfterSeconds", ex.getRetryAfterSeconds()
                        )
                ));
    }

    private String getPropMessage(String key, String lang) {
        return languageService.getMessage(key, lang);
    }

}

