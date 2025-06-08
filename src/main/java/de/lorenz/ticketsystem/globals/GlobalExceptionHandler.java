package de.lorenz.ticketsystem.globals;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
        if (cause instanceof UnrecognizedPropertyException unrecognized) {
            String field = unrecognized.getPropertyName();
            response.put("path", request.getRequestURI());
            response.put("hint", "This Field is not Allowed or Incorrect: " + field);

            return ResponseWrapper.badRequest(response, "Something went Wrong");
        }
        response.put("error", "Wrong JSON-Body:");
        return ResponseWrapper.badRequest(response, "Something went Wrong");
    }
}

