package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.dto.TikTokTokens;
import com.pluxurydolo.tiktok.flow.oauth.hook.RefreshTokenFlowHook;
import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import com.pluxurydolo.tiktok.token.AbstractTokenRetriever;
import com.pluxurydolo.tiktok.token.AbstractTokenSaver;
import com.pluxurydolo.tiktok.web.TikTokApiHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class TikTokRefreshTokenFlow {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokRefreshTokenFlow.class);

    private final TikTokAuthProperties tikTokAuthProperties;
    private final TikTokApiHttpClient tikTokApiHttpClient;
    private final AbstractTokenRetriever abstractTokenRetriever;
    private final AbstractTokenSaver abstractTokenSaver;
    private final RefreshTokenFlowHook refreshTokenFlowHook;

    public TikTokRefreshTokenFlow(
        TikTokAuthProperties tikTokAuthProperties,
        TikTokApiHttpClient tikTokApiHttpClient,
        AbstractTokenRetriever abstractTokenRetriever,
        AbstractTokenSaver abstractTokenSaver,
        RefreshTokenFlowHook refreshTokenFlowHook
    ) {
        this.tikTokAuthProperties = tikTokAuthProperties;
        this.tikTokApiHttpClient = tikTokApiHttpClient;
        this.abstractTokenRetriever = abstractTokenRetriever;
        this.abstractTokenSaver = abstractTokenSaver;
        this.refreshTokenFlowHook = refreshTokenFlowHook;
    }

    public Mono<String> refreshToken() {
        return abstractTokenRetriever.retrieve()
            .flatMap(this::updateTokens)
            .flatMap(_ -> refreshTokenFlowHook.doAfter())
            .thenReturn("SUCCESS")
            .doOnSuccess(_ -> LOGGER.info("itra [tiktok-starter] Access token успешно обновлен"))
            .onErrorResume(throwable -> {
                LOGGER.error("iabp [tiktok-starter] Произошла ошибка при обновлении access token");
                return refreshTokenFlowHook.handleException(throwable);
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<String> updateTokens(TikTokTokens tikTokTokens) {
        String clientKey = tikTokAuthProperties.clientKey();
        String clientSecret = tikTokAuthProperties.clientSecret();
        String grantType = "refresh_token";
        String refreshToken = tikTokTokens.refreshToken();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_key", clientKey);
        body.add("client_secret", clientSecret);
        body.add("grant_type", grantType);
        body.add("refresh_token", refreshToken);

        return tikTokApiHttpClient.refreshToken(body)
            .doOnNext(response -> {
                LOGGER.info("ccaa [tiktok-starter] Ответ TikTok:");
                LOGGER.info("ccbb [tiktok-starter] access_token: {}", response.accessToken() != null ? "ЕСТЬ" : "null");
                LOGGER.info("ccca [tiktok-starter] error: {}", response.error());
                LOGGER.info("ccdd [tiktok-starter] error_description: {}", response.errorDescription());
            })
            .flatMap(abstractTokenSaver::save);
    }
}
