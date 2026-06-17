package com.pluxurydolo.tiktok.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

import java.time.Duration;

@ConfigurationProperties(prefix = "tiktok.polling")
public record TikTokPollingProperties(

    @Name("max-repeat")
    int maxRepeat,

    @Name("delay")
    Duration delay
) {
}
