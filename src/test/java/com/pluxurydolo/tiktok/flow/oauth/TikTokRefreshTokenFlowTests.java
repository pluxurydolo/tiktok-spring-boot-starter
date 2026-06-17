package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.dto.TikTokTokens;
import com.pluxurydolo.tiktok.dto.response.TokenResponse;
import com.pluxurydolo.tiktok.flow.oauth.hook.RefreshTokenFlowHook;
import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import com.pluxurydolo.tiktok.token.AbstractTokenRetriever;
import com.pluxurydolo.tiktok.token.AbstractTokenSaver;
import com.pluxurydolo.tiktok.web.TikTokApiHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class TikTokRefreshTokenFlowTests {

    @Mock
    private TikTokAuthProperties tikTokAuthProperties;

    @Mock
    private TikTokApiHttpClient tikTokApiHttpClient;

    @Mock
    private AbstractTokenRetriever abstractTokenRetriever;

    @Mock
    private AbstractTokenSaver abstractTokenSaver;

    @Mock
    private RefreshTokenFlowHook refreshTokenFlowHook;

    @InjectMocks
    private TikTokRefreshTokenFlow tikTokRefreshTokenFlow;

    @BeforeEach
    void setUp() {
        when(tikTokAuthProperties.clientKey())
            .thenReturn("clientKey");
        when(tikTokAuthProperties.clientSecret())
            .thenReturn("clientSecret");
    }

    @Test
    void testRefreshToken() {
        when(abstractTokenRetriever.retrieve())
            .thenReturn(Mono.just(tikTokTokens()));
        when(tikTokApiHttpClient.refreshToken(any()))
            .thenReturn(Mono.just(tokenResponse()));
        when(abstractTokenSaver.save(any()))
            .thenReturn(Mono.just(""));
        when(refreshTokenFlowHook.doAfter())
            .thenReturn(Mono.just(""));

        Mono<String> result = tikTokRefreshTokenFlow.refreshToken();

        create(result)
            .expectNext("SUCCESS")
            .verifyComplete();
    }

    @Test
    void testRefreshTokenWhenExceptionOccurred() {
        when(abstractTokenRetriever.retrieve())
            .thenReturn(Mono.just(tikTokTokens()));
        when(tikTokApiHttpClient.refreshToken(any()))
            .thenReturn(Mono.error(new RuntimeException()));
        when(refreshTokenFlowHook.handleException(any()))
            .thenReturn(Mono.just(""));

        Mono<String> result = tikTokRefreshTokenFlow.refreshToken();

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    private static TikTokTokens tikTokTokens() {
        return new TikTokTokens("accessToken", "refreshToken");
    }

    private static TokenResponse tokenResponse() {
        return new TokenResponse(
            "accessToken",
            1,
            "refreshToken",
            1,
            "openId",
            "scope",
            "tokenType",
            "error",
            "errorDesription"
        );
    }
}
