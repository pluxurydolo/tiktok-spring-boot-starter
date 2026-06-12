package com.pluxurydolo.tiktok.configuration;

import com.pluxurydolo.tiktok.flow.oauth.TikTokAuthorizationCodeFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokExchangeTokenFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokRefreshTokenFlow;
import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import com.pluxurydolo.tiktok.token.AbstractTokenSaver;
import com.pluxurydolo.tiktok.util.TikTokPkceUtil;
import com.pluxurydolo.tiktok.web.TikTokApiHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TikTokOAuthConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TikTokAuthorizationCodeFlow tikTokAuthorizationCodeFlow(
        TikTokPkceUtil tikTokPkceUtil,
        TikTokAuthProperties tikTokAuthProperties
    ) {
        return new TikTokAuthorizationCodeFlow(tikTokPkceUtil, tikTokAuthProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public TikTokExchangeTokenFlow tikTokExchangeTokenFlow(
        TikTokApiHttpClient tikTokApiHttpClient,
        TikTokAuthProperties tikTokAuthProperties
    ) {
        return new TikTokExchangeTokenFlow(tikTokApiHttpClient, tikTokAuthProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public TikTokRefreshTokenFlow tikTokRefreshTokenFlow(
        TikTokAuthProperties tikTokAuthProperties,
        TikTokApiHttpClient tikTokApiHttpClient,
        AbstractTokenSaver abstractTokenSaver
    ) {
        return new TikTokRefreshTokenFlow(tikTokAuthProperties, tikTokApiHttpClient, abstractTokenSaver);
    }

    @Bean
    @ConditionalOnMissingBean
    public TikTokPkceUtil tikTokPkceUtil() {
        return new TikTokPkceUtil();
    }
}
