package com.pluxurydolo.tiktok.service;

import com.pluxurydolo.tiktok.flow.oauth.TikTokAccessTokenFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokAuthorizationCodeFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokRefreshTokenFlow;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class TikTokOAuthService {
    private final TikTokAuthorizationCodeFlow tikTokAuthorizationCodeFlow;
    private final TikTokAccessTokenFlow tikTokAccessTokenFlow;
    private final TikTokRefreshTokenFlow tikTokRefreshTokenFlow;

    public TikTokOAuthService(
        TikTokAuthorizationCodeFlow tikTokAuthorizationCodeFlow,
        TikTokAccessTokenFlow tikTokAccessTokenFlow,
        TikTokRefreshTokenFlow tikTokRefreshTokenFlow
    ) {
        this.tikTokAuthorizationCodeFlow = tikTokAuthorizationCodeFlow;
        this.tikTokAccessTokenFlow = tikTokAccessTokenFlow;
        this.tikTokRefreshTokenFlow = tikTokRefreshTokenFlow;
    }

    public Mono<Void> login(ServerWebExchange serverWebExchange) {
        ServerHttpResponse response = tikTokAuthorizationCodeFlow.getResponse(serverWebExchange);
        return response.setComplete();
    }

    public Mono<String> redirect(String code) {
        return tikTokAccessTokenFlow.getAccessToken(code);
    }

    public Mono<String> refreshToken() {
        return tikTokRefreshTokenFlow.refreshToken();
    }
}
