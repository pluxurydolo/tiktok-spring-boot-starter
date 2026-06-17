package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.dto.response.TokenResponse;
import com.pluxurydolo.tiktok.flow.oauth.hook.AccessTokenFlowHook;
import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
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
class TikTokAccessTokenFlowTests {

    @Mock
    private TikTokAuthProperties tikTokAuthProperties;

    @Mock
    private TikTokApiHttpClient tikTokApiHttpClient;

    @Mock
    private AbstractTokenSaver abstractTokenSaver;

    @Mock
    private AccessTokenFlowHook accessTokenFlowHook;

    @InjectMocks
    private TikTokAccessTokenFlow tikTokAccessTokenFlow;

    @BeforeEach
    void setUp() {
        when(tikTokAuthProperties.clientKey())
            .thenReturn("clientKey");
        when(tikTokAuthProperties.clientSecret())
            .thenReturn("clientSecret");
        when(tikTokAuthProperties.redirectUri())
            .thenReturn("redirectUri");
    }

    @Test
    void testGetAccessToken() {
        when(tikTokApiHttpClient.getAccessToken(any()))
            .thenReturn(Mono.just(tokenResponse()));
        when(tikTokApiHttpClient.getAccessToken(any()))
            .thenReturn(Mono.just(tokenResponse()));
        when(abstractTokenSaver.save(any()))
            .thenReturn(Mono.just(""));
        when(accessTokenFlowHook.doAfter())
            .thenReturn(Mono.just(""));

        Mono<String> result = tikTokAccessTokenFlow.getAccessToken("exchangeToken");

        create(result)
            .expectNext("SUCCESS")
            .verifyComplete();
    }

    @Test
    void testGetAccessTokenWhenExceptionOccurred() {
        when(tikTokApiHttpClient.getAccessToken(any()))
            .thenReturn(Mono.error(new RuntimeException()));
        when(accessTokenFlowHook.handleException(any()))
            .thenReturn(Mono.just(""));

        Mono<String> result = tikTokAccessTokenFlow.getAccessToken("exchangeToken");

        create(result)
            .expectNext("")
            .verifyComplete();
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
            "errorDescription"
        );
    }
}
