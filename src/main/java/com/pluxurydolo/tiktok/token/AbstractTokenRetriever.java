package com.pluxurydolo.tiktok.token;

import com.pluxurydolo.tiktok.dto.TikTokTokens;
import reactor.core.publisher.Mono;

import java.util.Map;

public abstract class AbstractTokenRetriever {
    public Mono<TikTokTokens> retrieve() {
        return retrieveTokens()
            .map(AbstractTokenRetriever::mapToTokens);
    }

    private static TikTokTokens mapToTokens(Map<String, String> tokens) {
        String accessToken = tokens.get("access_token");
        String refreshToken = tokens.get("refresh_token");
        return new TikTokTokens(accessToken, refreshToken);
    }

    protected abstract Mono<Map<String, String>> retrieveTokens();
}
