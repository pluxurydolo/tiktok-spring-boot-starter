package com.pluxurydolo.tiktok.service;

import com.pluxurydolo.tiktok.flow.oauth.TikTokAccessTokenFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokAuthorizationCodeFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokRefreshTokenFlow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class TikTokOAuthServiceTests {

    @Mock
    private TikTokAuthorizationCodeFlow tikTokAuthorizationCodeFlow;

    @Mock
    private TikTokAccessTokenFlow tikTokAccessTokenFlow;

    @Mock
    private TikTokRefreshTokenFlow tikTokRefreshTokenFlow;

    @Mock
    private ServerWebExchange serverWebExchange;

    @Mock
    private ServerHttpResponse serverHttpResponse;

    @InjectMocks
    private TikTokOAuthService tikTokOAuthService;

    @Test
    void testLogin() {
        when(tikTokAuthorizationCodeFlow.getResponse(any()))
            .thenReturn(serverHttpResponse);
        when(serverHttpResponse.setComplete())
            .thenReturn(Mono.empty());

        Mono<Void> result = tikTokOAuthService.login(serverWebExchange);

        create(result)
            .verifyComplete();
    }

    @Test
    void testRedirect() {
        when(tikTokAccessTokenFlow.getAccessToken(anyString()))
            .thenReturn(Mono.just(""));

        Mono<String> result = tikTokOAuthService.redirect("code");

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    @Test
    void testRefreshToken() {
        when(tikTokRefreshTokenFlow.refreshToken())
            .thenReturn(Mono.just(""));

        Mono<String> result = tikTokOAuthService.refreshToken();

        create(result)
            .expectNext("")
            .verifyComplete();
    }
}
