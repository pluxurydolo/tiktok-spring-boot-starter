package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.exception.TikTokRefreshTokenFlowException;
import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import com.pluxurydolo.tiktok.token.AbstractTokenSaver;
import com.pluxurydolo.tiktok.web.TikTokApiHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class TikTokRefreshTokenFlow {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokRefreshTokenFlow.class);

    private final TikTokAuthProperties tikTokAuthProperties;
    private final TikTokApiHttpClient tikTokApiHttpClient;
    private final AbstractTokenSaver abstractTokenSaver;

    public TikTokRefreshTokenFlow(
        TikTokAuthProperties tikTokAuthProperties,
        TikTokApiHttpClient tikTokApiHttpClient,
        AbstractTokenSaver abstractTokenSaver
    ) {
        this.tikTokAuthProperties = tikTokAuthProperties;
        this.tikTokApiHttpClient = tikTokApiHttpClient;
        this.abstractTokenSaver = abstractTokenSaver;
    }

    public Mono<String> refreshToken(String refreshToken) {
        String clientKey = tikTokAuthProperties.clientKey();
        String clientSecret = tikTokAuthProperties.clientSecret();
        String grantType = "refresh_token";

        return tikTokApiHttpClient.refreshToken(clientKey, clientSecret, grantType, refreshToken)
            .flatMap(abstractTokenSaver::save)
            .doOnSuccess(_ -> LOGGER.info("itra [tiktok-starter] Access token успешно обновлен"))
            .onErrorResume(throwable -> {
                LOGGER.error("iabp [tiktok-starter] Произошла ошибка при обновлении access token");
                return Mono.error(new TikTokRefreshTokenFlowException(throwable));
            });
    }
}
