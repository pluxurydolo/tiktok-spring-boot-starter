package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TikTokAuthorizationCodeFlowTests {

    @Mock
    private TikTokAuthProperties tikTokAuthProperties;

    @Mock
    private ServerWebExchange serverWebExchange;

    @Mock
    private ServerHttpResponse serverHttpResponse;

    @Mock
    private HttpHeaders httpHeaders;

    @InjectMocks
    private TikTokAuthorizationCodeFlow tikTokAuthorizationCodeFlow;

    @BeforeEach
    void setUp() {
        when(tikTokAuthProperties.clientKey())
            .thenReturn("clientKey");
        when(tikTokAuthProperties.redirectUri())
            .thenReturn("redirectUri");
    }

    @Test
    void testGetResponse() {
        doNothing()
            .when(httpHeaders).setLocation(any());
        when(serverWebExchange.getResponse())
            .thenReturn(serverHttpResponse);
        when(serverHttpResponse.setStatusCode(any()))
            .thenReturn(true);
        when(serverHttpResponse.getHeaders())
            .thenReturn(httpHeaders);

        ServerHttpResponse result = tikTokAuthorizationCodeFlow.getResponse(serverWebExchange);

        assertThat(result)
            .isEqualTo(serverHttpResponse);
    }

    @Test
    void testGetResponseWhenExceptionOccurred() {
        doThrow(RuntimeException.class)
            .when(serverWebExchange).getResponse();

        assertThrows(
            RuntimeException.class,
            () -> tikTokAuthorizationCodeFlow.getResponse(serverWebExchange)
        );
    }
}
