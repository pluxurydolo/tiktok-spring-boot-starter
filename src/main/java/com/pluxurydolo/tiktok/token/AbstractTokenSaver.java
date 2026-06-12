package com.pluxurydolo.tiktok.token;

import com.pluxurydolo.tiktok.dto.response.TokenResponse;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

public abstract class AbstractTokenSaver {
    private final Clock clock;

    protected AbstractTokenSaver(Clock clock) {
        this.clock = clock;
    }

    public Mono<String> save(TokenResponse tokenResponse) {
        String accessToken = tokenResponse.accessToken();
        int expiresIn = tokenResponse.expiresIn();
        String refreshToken = tokenResponse.refreshToken();
        int refreshExpiresIn = tokenResponse.refreshExpiresIn();
        String openId = tokenResponse.openId();
        String scope = tokenResponse.scope();
        String tokenType = tokenResponse.tokenType();
        String updatedAt = updatedAt();

        Map<String, String> tokens = Map.of(
            "access_token", accessToken,
            "expires_in", String.valueOf(expiresIn),
            "refresh_token", refreshToken,
            "refresh_expires_in", String.valueOf(refreshExpiresIn),
            "openid", openId,
            "scope", scope,
            "token_type", tokenType,
            "updated_at", updatedAt
        );

        return saveTokens(tokens);
    }

    private String updatedAt() {
        return now(clock)
            .format(ofPattern("yyyy-MM-dd HH:mm:ss 'МСК'"));
    }

    protected abstract Mono<String> saveTokens(Map<String, String> tokens);
}
