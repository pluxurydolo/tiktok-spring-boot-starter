package com.pluxurydolo.tiktok.configuration;

import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(TikTokAuthProperties.class)
@Import({
    TikTokOAuthConfiguration.class,
    TikTokWebConfiguration.class,
    TikTokClientConfiguration.class,
    TikTokUploadConfiguration.class,
    TikTokSchedulingConfiguration.class,
    TikTokResilienceConfiguration.class
})
public class TikTokAutoConfiguration {
}
