package com.pluxurydolo.tiktok.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

@ConfigurationProperties(prefix = "tiktok.auth")
public record TikTokAuthProperties(
    @Name("client.key") String clientKey,
    @Name("client.secret") String clientSecret,
    @Name("redirect-uri") String redirectUri
) {
}
