package com.pluxurydolo.tiktok.service;

import com.pluxurydolo.tiktok.dto.TikTokTokens;
import com.pluxurydolo.tiktok.flow.oauth.TikTokAuthorizationCodeFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokExchangeTokenFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokRefreshTokenFlow;
import com.pluxurydolo.tiktok.token.AbstractTokenRetriever;
import com.pluxurydolo.tiktok.token.AbstractTokenSaver;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;

import static org.springframework.http.HttpStatus.FOUND;

public class TikTokOAuthService {
    private final TikTokAuthorizationCodeFlow tikTokAuthorizationCodeFlow;
    private final TikTokExchangeTokenFlow tikTokExchangeTokenFlow;
    private final TikTokRefreshTokenFlow tikTokRefreshTokenFlow;
    private final AbstractTokenSaver abstractTokenSaver;
    private final AbstractTokenRetriever abstractTokenRetriever;

    public TikTokOAuthService(
        TikTokAuthorizationCodeFlow tikTokAuthorizationCodeFlow,
        TikTokExchangeTokenFlow tikTokExchangeTokenFlow,
        TikTokRefreshTokenFlow tikTokRefreshTokenFlow,
        AbstractTokenSaver abstractTokenSaver,
        AbstractTokenRetriever abstractTokenRetriever
    ) {
        this.tikTokAuthorizationCodeFlow = tikTokAuthorizationCodeFlow;
        this.tikTokExchangeTokenFlow = tikTokExchangeTokenFlow;
        this.tikTokRefreshTokenFlow = tikTokRefreshTokenFlow;
        this.abstractTokenSaver = abstractTokenSaver;
        this.abstractTokenRetriever = abstractTokenRetriever;
    }

    public Mono<Void> login(ServerWebExchange serverWebExchange) {
        URI authorizationUri = tikTokAuthorizationCodeFlow.getAuthorizationUri();

        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(FOUND);
        response.getHeaders().setLocation(authorizationUri);

        return response.setComplete();
    }

    public Mono<String> redirect(String code, String state) {
        return tikTokExchangeTokenFlow.getToken(code, state)
            .flatMap(abstractTokenSaver::save)
            .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> refreshToken() {
        return abstractTokenRetriever.retrieve()
            .map(TikTokTokens::accessToken)
            .flatMap(tikTokRefreshTokenFlow::refreshToken)
            .subscribeOn(Schedulers.boundedElastic());
    }
}
