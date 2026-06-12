package com.pluxurydolo.tiktok.configuration;

import com.pluxurydolo.tiktok.flow.oauth.TikTokAccessTokenFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokAuthorizationCodeFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokRefreshTokenFlow;
import com.pluxurydolo.tiktok.flow.oauth.hook.AccessTokenFlowHook;
import com.pluxurydolo.tiktok.flow.oauth.hook.RefreshTokenFlowHook;
import com.pluxurydolo.tiktok.properties.TikTokAuthProperties;
import com.pluxurydolo.tiktok.token.AbstractTokenRetriever;
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
    public TikTokAccessTokenFlow tikTokAccessTokenFlow(
        TikTokApiHttpClient tikTokApiHttpClient,
        TikTokAuthProperties tikTokAuthProperties,
        AbstractTokenSaver abstractTokenSaver,
        AccessTokenFlowHook accessTokenFlowHook
    ) {
        return new TikTokAccessTokenFlow(
            tikTokApiHttpClient,
            tikTokAuthProperties,
            abstractTokenSaver,
            accessTokenFlowHook
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public TikTokRefreshTokenFlow tikTokRefreshTokenFlow(
        TikTokAuthProperties tikTokAuthProperties,
        TikTokApiHttpClient tikTokApiHttpClient,
        AbstractTokenRetriever abstractTokenRetriever,
        AbstractTokenSaver abstractTokenSaver,
        RefreshTokenFlowHook refreshTokenFlowHook
    ) {
        return new TikTokRefreshTokenFlow(
            tikTokAuthProperties,
            tikTokApiHttpClient,
            abstractTokenRetriever,
            abstractTokenSaver,
            refreshTokenFlowHook
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public TikTokPkceUtil tikTokPkceUtil() {
        return new TikTokPkceUtil();
    }
}
