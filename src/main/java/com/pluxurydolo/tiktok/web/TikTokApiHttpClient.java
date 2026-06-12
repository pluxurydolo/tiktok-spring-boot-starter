package com.pluxurydolo.tiktok.web;

import com.pluxurydolo.tiktok.dto.response.TokenResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange("https://open-api.tiktok.com")
public interface TikTokApiHttpClient {

    @PostExchange("/v2/oauth/token/")
    Mono<TokenResponse> getToken(
        @RequestParam("client_key") String clientKey,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("code") String code,
        @RequestParam("grant_type") String grantType,
        @RequestParam("redirect_uri") String redirectUri,
        @RequestParam("code_verifier") String codeVerifier
    );

    @PostExchange("/v2/oauth/token/")
    Mono<TokenResponse> refreshToken(
        @RequestParam("client_key") String clientKey,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("grant_type") String grantType,
        @RequestParam("refresh_token") String refreshToken
    );
}
