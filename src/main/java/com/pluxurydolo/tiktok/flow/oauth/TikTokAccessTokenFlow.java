package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.flow.oauth.hook.AccessTokenFlowHook;
import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import com.pluxurydolo.tiktok.token.AbstractTokenSaver;
import com.pluxurydolo.tiktok.web.TikTokApiHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_key", clientKey);
        body.add("client_secret", clientSecret);
        body.add("code", code);
        body.add("grant_type", grantType);
        body.add("redirect_uri", redirectUri);

        return tikTokApiHttpClient.getAccessToken(body)
            .flatMap(abstractTokenSaver::save)
            .flatMap(_ -> accessTokenFlowHook.doAfter())
            .thenReturn("SUCCESS")
            .doOnSuccess(_ -> LOGGER.info("bpvh [tiktok-starter] Access token успешно получен"))
            .onErrorResume(throwable -> {
                LOGGER.error("gvxk [tiktok-starter] Произошла ошибка при получении access token");
                return accessTokenFlowHook.handleException(throwable);
            })
            .subscribeOn(Schedulers.boundedElastic());
    }
}
