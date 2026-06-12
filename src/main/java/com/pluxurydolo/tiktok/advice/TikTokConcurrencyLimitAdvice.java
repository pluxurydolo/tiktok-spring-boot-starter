package com.pluxurydolo.tiktok.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.resilience.InvocationRejectedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import java.time.Clock;

import static java.time.Instant.now;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

@RestControllerAdvice
public class TikTokConcurrencyLimitAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokConcurrencyLimitAdvice.class);

    private final Clock clock;

    public TikTokConcurrencyLimitAdvice(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(InvocationRejectedException.class)
    public ProblemDetail handleConcurrencyLimit(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();

        String timestamp = now(clock)
            .toString();

        LOGGER.warn("xrii [tiktok-starter] Превышен лимит запросов по пути {} {}", path, timestamp);

        ProblemDetail problemDetail = forStatusAndDetail(TOO_MANY_REQUESTS, "Лимит запросов исчерпан");
        problemDetail.setTitle("Too Many Requests");
        problemDetail.setProperty("timestamp", timestamp);

        return problemDetail;
    }
}
