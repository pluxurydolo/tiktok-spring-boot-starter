package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.flow.oauth.hook.AccessTokenFlowHook;
import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import com.pluxurydolo.tiktok.token.AbstractTokenSaver;
import com.pluxurydolo.tiktok.web.TikTokApiHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HexFormat;

import static java.nio.charset.StandardCharsets.UTF_8;

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

    public Mono<String> getAccessToken(String code, String state) {
        String clientKey = tikTokAuthProperties.clientKey();
        String clientSecret = tikTokAuthProperties.clientSecret();

        String grantType = "authorization_code";
        String redirectUri = tikTokAuthProperties.redirectUri();

        byte[] stateBytes = HexFormat.of().parseHex(state);
        String decodedState = new String(stateBytes, UTF_8);
        String codeVerifier = decodedState.split(":")[1];

        return tikTokApiHttpClient.getToken(clientKey, clientSecret, code, grantType, redirectUri, codeVerifier)
            .flatMap(abstractTokenSaver::save)
            .flatMap(_ -> accessTokenFlowHook.doAfter())
            .doOnSuccess(_ -> LOGGER.info("bpvh [tiktok-starter] Access token успешно получен"))
            .onErrorResume(throwable -> {
                LOGGER.error("gvxk [tiktok-starter] Произошла ошибка при получении access token");
                return accessTokenFlowHook.handleException(throwable);
            })
            .subscribeOn(Schedulers.boundedElastic());
    }
}
