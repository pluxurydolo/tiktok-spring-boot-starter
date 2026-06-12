package com.pluxurydolo.tiktok.controller;

import com.pluxurydolo.tiktok.service.TikTokOAuthService;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.resilience.annotation.ConcurrencyLimit.ThrottlePolicy.REJECT;

@RestController
public class TikTokOAuthController {
    private final TikTokOAuthService tikTokOAuthService;

    public TikTokOAuthController(TikTokOAuthService tikTokOAuthService) {
        this.tikTokOAuthService = tikTokOAuthService;
    }

    @GetMapping("${tiktok.endpoint.login}")
    @ConcurrencyLimit(limit = 1, policy = REJECT)
    public Mono<Void> login(ServerWebExchange serverWebExchange) {
        return tikTokOAuthService.login(serverWebExchange);
    }

    @GetMapping("${tiktok.endpoint.redirect}")
    @ConcurrencyLimit(limit = 1, policy = REJECT)
    public Mono<String> redirect(
        @RequestParam("code") String code,
        @RequestParam("state") String state
    ) {
        return tikTokOAuthService.redirect(code, state);
    }

    @GetMapping("${tiktok.endpoint.refresh-token}")
    @ConcurrencyLimit(limit = 1, policy = REJECT)
    public Mono<String> refreshToken() {
        return tikTokOAuthService.refreshToken();
    }
}
