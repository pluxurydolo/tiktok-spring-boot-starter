package com.pluxurydolo.tiktok.configuration;

import com.pluxurydolo.tiktok.client.TikTokVideoClient;
import com.pluxurydolo.tiktok.flow.upload.TikTokVideoPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TikTokClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TikTokVideoClient tikTokVideoClient(TikTokVideoPublisher tikTokVideoPublisher) {
        return new TikTokVideoClient(tikTokVideoPublisher);
    }
}
