package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import com.pluxurydolo.tiktok.util.TikTokPkceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HexFormat;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static org.springframework.http.HttpStatus.FOUND;

public class TikTokAuthorizationCodeFlow {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokAuthorizationCodeFlow.class);

    private final TikTokPkceUtil tikTokPkceUtil;
    private final TikTokAuthProperties tikTokAuthProperties;

    public TikTokAuthorizationCodeFlow(TikTokPkceUtil tikTokPkceUtil, TikTokAuthProperties tikTokAuthProperties) {
        this.tikTokPkceUtil = tikTokPkceUtil;
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

        String codeVerifier = tikTokPkceUtil.generateCodeVerifier();
        String codeChallenge = tikTokPkceUtil.generateCodeChallenge(codeVerifier);

        String statePayload = String.format("%s:%s", randomUUID(), codeVerifier);
        byte[] statePayloadBytes = statePayload.getBytes(UTF_8);
        String state = HexFormat.of().formatHex(statePayloadBytes);

        LOGGER.info("abcd [tiktok-starter] CODE_VERIFIER={}", codeVerifier);

        return UriComponentsBuilder.fromUriString("https://www.tiktok.com/v2/auth/authorize/")
            .queryParam("client_key", clientKey)
            .queryParam("response_type", "code")
            .queryParam("scope", "user.info.basic,video.upload,video.publish")
            .queryParam("redirect_uri", redirectUri)
            .queryParam("state", state)
            .queryParam("code_challenge", codeChallenge)
            .queryParam("code_challenge_method", "S256")
            .encode()
            .build()
            .toUri();
    }
}
