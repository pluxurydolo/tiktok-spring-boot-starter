package com.pluxurydolo.tiktok.controller;

import com.pluxurydolo.tiktok.service.TikTokOAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class TikTokOAuthControllerTests {

    @Mock
    private TikTokOAuthService tikTokOAuthService;

    @InjectMocks
    private TikTokOAuthController tikTokOAuthController;

    @Mock
    private ServerWebExchange serverWebExchange;

    @Test
    void testLogin() {
        when(tikTokOAuthService.login(serverWebExchange))
            .thenReturn(Mono.empty());

        Mono<Void> result = tikTokOAuthController.login(serverWebExchange);

        create(result)
            .verifyComplete();
    }

    @Test
    void testRedirect() {
        when(tikTokOAuthService.redirect(anyString()))
            .thenReturn(Mono.just(""));

        Mono<String> result = tikTokOAuthController.redirect("code");

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    @Test
    void testRefreshToken() {
        when(tikTokOAuthService.refreshToken())
            .thenReturn(Mono.just(""));

        Mono<String> result = tikTokOAuthController.refreshToken();

        create(result)
            .expectNext("")
            .verifyComplete();
    }
}
