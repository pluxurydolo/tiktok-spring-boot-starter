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

import java.util.HexFormat;
import java.util.stream.Collectors;

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

        String cleanState = state.trim();
        byte[] stateBytes = HexFormat.of().parseHex(cleanState);
        String decodedState = new String(stateBytes, UTF_8);
        String codeVerifier = decodedState.split(":")[1];

        LOGGER.info("cccc [tiktok-starter] State bytes: {}",
            state.chars()
                .mapToObj(c -> String.format("%02x", c))
                .collect(Collectors.joining(" "))); // TODO удалить

        return tikTokApiHttpClient.getToken(clientKey, clientSecret, code, grantType, redirectUri, codeVerifier)
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
