package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.dto.response.TokenResponse;
import com.pluxurydolo.tiktok.exception.TikTokExchangeTokenFlowException;
import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import com.pluxurydolo.tiktok.web.TikTokApiHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Base64;

import static java.nio.charset.Charset.defaultCharset;

public class TikTokExchangeTokenFlow {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokExchangeTokenFlow.class);

    private final TikTokApiHttpClient tikTokApiHttpClient;
    private final TikTokAuthProperties tikTokAuthProperties;

    public TikTokExchangeTokenFlow(TikTokApiHttpClient tikTokApiHttpClient, TikTokAuthProperties tikTokAuthProperties) {
        this.tikTokApiHttpClient = tikTokApiHttpClient;
        this.tikTokAuthProperties = tikTokAuthProperties;
    }

    public Mono<TokenResponse> getToken(String code, String state) {
        String clientKey = tikTokAuthProperties.clientKey();
        String clientSecret = tikTokAuthProperties.clientSecret();

        String grantType = "authorization_code";
        String redirectUri = tikTokAuthProperties.redirectUri();

        byte[] stateBytes = Base64.getUrlDecoder().decode(state);
        String decodedState = new String(stateBytes, defaultCharset());
        String codeVerifier = decodedState.split(":")[1];

        return tikTokApiHttpClient.getToken(clientKey, clientSecret, code, grantType, redirectUri, codeVerifier)
            .doOnSuccess(_ -> LOGGER.info("bpvh [tiktok-starter] Exchange token успешно получен"))
            .onErrorResume(throwable -> {
                LOGGER.error("gvxk [tiktok-starter] Произошла ошибка при получении exchange token");
                return Mono.error(new TikTokExchangeTokenFlowException(throwable));
            });
    }
}
