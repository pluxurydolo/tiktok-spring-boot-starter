package com.pluxurydolo.tiktok.web;

import com.pluxurydolo.tiktok.dto.response.TokenResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@HttpExchange("https://open.tiktokapis.com")
public interface TikTokApiHttpClient {

    @PostExchange(
        value = "/v2/oauth/token/",
        contentType = APPLICATION_FORM_URLENCODED_VALUE
    )
    Mono<TokenResponse> getToken(
        @RequestBody MultiValueMap<String, String> body
    );


    @PostExchange(
        value = "/v2/oauth/token/",
        contentType = APPLICATION_FORM_URLENCODED_VALUE
    )
    Mono<TokenResponse> refreshToken(
        @RequestBody MultiValueMap<String, String> body
    );
}
