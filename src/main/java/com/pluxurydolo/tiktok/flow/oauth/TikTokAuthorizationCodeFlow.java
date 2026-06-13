package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static java.util.UUID.randomUUID;
import static org.springframework.http.HttpStatus.FOUND;

public class TikTokAuthorizationCodeFlow {
    private final TikTokAuthProperties tikTokAuthProperties;

    public TikTokAuthorizationCodeFlow(TikTokAuthProperties tikTokAuthProperties) {
        this.tikTokAuthProperties = tikTokAuthProperties;
    }

    public ServerHttpResponse getResponse(ServerWebExchange serverWebExchange) {
        URI authorizationUri = getAuthorizationUri();

        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(FOUND);
        response.getHeaders().setLocation(authorizationUri);

        return response;
    }

    private URI getAuthorizationUri() {
        String clientKey = tikTokAuthProperties.clientKey();
        String redirectUri = tikTokAuthProperties.redirectUri();
        String state = randomUUID().toString();

        return UriComponentsBuilder.fromUriString("https://www.tiktokapis.com/v2/auth/authorize/")
            .queryParam("client_key", clientKey)
            .queryParam("response_type", "code")
            .queryParam("scope", "user.info.basic,video.upload,video.publish")
            .queryParam("redirect_uri", redirectUri)
            .queryParam("state", state)
            .build()
            .toUri();
    }
}
