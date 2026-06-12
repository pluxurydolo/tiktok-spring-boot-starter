package com.pluxurydolo.tiktok.flow.oauth;

import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import com.pluxurydolo.tiktok.util.TikTokPkceUtil;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Base64;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.UUID.randomUUID;

public class TikTokAuthorizationCodeFlow {
    private final TikTokPkceUtil tikTokPkceUtil;
    private final TikTokAuthProperties tikTokAuthProperties;

    public TikTokAuthorizationCodeFlow(TikTokPkceUtil tikTokPkceUtil, TikTokAuthProperties tikTokAuthProperties) {
        this.tikTokPkceUtil = tikTokPkceUtil;
        this.tikTokAuthProperties = tikTokAuthProperties;
    }

    public URI getAuthorizationUri() {
        String clientKey = tikTokAuthProperties.clientKey();
        String redirectUri = tikTokAuthProperties.redirectUri();

        String codeVerifier = tikTokPkceUtil.generateCodeVerifier();
        String codeChallenge = tikTokPkceUtil.generateCodeChallenge(codeVerifier);

        String decodedState = String.format("%s:%s", randomUUID(), codeVerifier);
        byte[] decodedStateBytes = decodedState.getBytes(defaultCharset());

        String state = Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(decodedStateBytes);

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
