package de.lorenz.ticketsystem.security.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.lorenz.ticketsystem.globals.GlobalExceptionMsg;
import de.lorenz.ticketsystem.globals.GlobalHttpStatusCode;
import de.lorenz.ticketsystem.utils.ResponseWrapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(ip, this::newBucket);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(GlobalHttpStatusCode.TO_MANY_REQUESTS.getCode());
            response.setContentType("application/json");

            Map<String, Object> innerResponse = new HashMap<>();
            innerResponse.put("message", GlobalExceptionMsg.TOO_MANY_REQUESTS.getExceptionMsg());

            ResponseWrapper<Object> wrapper = new ResponseWrapper<>(
                    null, GlobalHttpStatusCode.FORBIDDEN.getCode(),
                    innerResponse
            );

            String json = objectMapper.writeValueAsString(wrapper);
            response.getWriter().write(json);
        }
    }

    private Bucket newBucket(String ip) {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                .build();
    }
}
