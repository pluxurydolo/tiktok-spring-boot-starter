package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.flow.oauth.hook.AccessTokenFlowHook;
import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import com.pluxurydolo.tiktok.token.AbstractTokenSaver;
import com.pluxurydolo.tiktok.web.TikTokApiHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class TikTokAccessTokenFlow {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokAccessTokenFlow.class);

    private final TikTokApiHttpClient tikTokApiHttpClient;
    private final TikTokAuthProperties tikTokAuthProperties;
    private final AbstractTokenSaver abstractTokenSaver;
    private final AccessTokenFlowHook accessTokenFlowHook;

    public TikTokAccessTokenFlow(
        TikTokApiHttpClient tikTokApiHttpClient,
        TikTokAuthProperties tikTokAuthProperties,
        AbstractTokenSaver abstractTokenSaver,
        AccessTokenFlowHook accessTokenFlowHook
    ) {
        this.tikTokApiHttpClient = tikTokApiHttpClient;
        this.tikTokAuthProperties = tikTokAuthProperties;
        this.abstractTokenSaver = abstractTokenSaver;
        this.accessTokenFlowHook = accessTokenFlowHook;
    }

    public Mono<String> getAccessToken(String code) {
        String clientKey = tikTokAuthProperties.clientKey();
        String clientSecret = tikTokAuthProperties.clientSecret();

        String grantType = "authorization_code";
        String redirectUri = tikTokAuthProperties.redirectUri();

        return tikTokApiHttpClient.getToken(clientKey, clientSecret, code, grantType, redirectUri)
            .doOnNext(response -> {
                LOGGER.info("ccaa [tiktok-starter] Ответ TikTok:");
                LOGGER.info("ccbb [tiktok-starter] access_token: {}", response.accessToken() != null ? "ЕСТЬ" : "null");
                LOGGER.info("ccca [tiktok-starter] error: {}", response.error());
                LOGGER.info("ccdd [tiktok-starter] error_description: {}", response.errorDescription());
            })
            .flatMap(abstractTokenSaver::save)
            .flatMap(_ -> accessTokenFlowHook.doAfter())
            .doOnSuccess(_ -> LOGGER.info("bpvh [tiktok-starter] Access token успешно получен"))
            .onErrorResume(throwable -> {
                LOGGER.error("gvxk [tiktok-starter] Тип ошибки: {}", throwable.getClass().getName());
                LOGGER.error("ssxr [tiktok-starter] Сообщение: {}", throwable.getMessage());

                if (throwable instanceof WebClientResponseException exception) {
                    LOGGER.error("xdcx [tiktok-starter] HTTP статус: {}", exception.getStatusCode());
                    LOGGER.error("iopu [tiktok-starter] Тело ответа: {}", exception.getResponseBodyAsString());
                } else {
                    LOGGER.error("urew [tiktok-starter] Полный стек:", throwable);
                }

                return accessTokenFlowHook.handleException(throwable);
            })
            .subscribeOn(Schedulers.boundedElastic());
    }
}
