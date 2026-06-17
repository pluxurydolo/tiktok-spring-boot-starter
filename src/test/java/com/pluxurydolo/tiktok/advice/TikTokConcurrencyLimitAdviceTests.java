package com.pluxurydolo.tiktok.advice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.time.Clock;
import java.time.Instant;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@ExtendWith(MockitoExtension.class)
class TikTokConcurrencyLimitAdviceTests {
    private static final TikTokConcurrencyLimitAdvice ADVICE = new TikTokConcurrencyLimitAdvice(clock());

    @Mock
    private ServerWebExchange serverWebExchange;

    @Mock
    private ServerHttpRequest serverHttpRequest;

    @Mock
    private URI uri;

    @Test
    void testHandleConcurrencyLimit() {
        when(serverWebExchange.getRequest())
            .thenReturn(serverHttpRequest);
        when(serverHttpRequest.getURI())
            .thenReturn(uri);
        when(uri.getPath())
            .thenReturn("path");

        ProblemDetail result = ADVICE.handleConcurrencyLimit(serverWebExchange);

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(problemDetail());
    }

    private static ProblemDetail problemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(TOO_MANY_REQUESTS);
        problemDetail.setDetail("Лимит запросов исчерпан");
        problemDetail.setTitle("Too Many Requests");
        problemDetail.setProperty("timestamp", instant().toString());
        return problemDetail;
    }

    private static Clock clock() {
        return Clock.fixed(instant(), UTC);
    }

    private static Instant instant() {
        return Instant.parse("1970-01-01T12:00:00Z");
    }
}
