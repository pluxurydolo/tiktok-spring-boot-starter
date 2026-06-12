package com.pluxurydolo.tiktok.configuration;

import com.pluxurydolo.tiktok.controller.TikTokOAuthController;
import com.pluxurydolo.tiktok.flow.oauth.TikTokAccessTokenFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokAuthorizationCodeFlow;
import com.pluxurydolo.tiktok.flow.oauth.TikTokRefreshTokenFlow;
import com.pluxurydolo.tiktok.service.TikTokOAuthService;
import com.pluxurydolo.tiktok.web.TikTokApiHttpClient;
import com.pluxurydolo.tiktok.web.TikTokUploadHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.function.Consumer;

@Configuration
public class TikTokWebConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TikTokOAuthController tikTokOAuthController(TikTokOAuthService tikTokOAuthService) {
        return new TikTokOAuthController(tikTokOAuthService);
    }

    @Bean
    @ConditionalOnMissingBean
    public TikTokOAuthService tikTokOAuthService(
        TikTokAuthorizationCodeFlow tikTokAuthorizationCodeFlow,
        TikTokAccessTokenFlow tikTokAccessTokenFlow,
        TikTokRefreshTokenFlow tikTokRefreshTokenFlow
    ) {
        return new TikTokOAuthService(tikTokAuthorizationCodeFlow, tikTokAccessTokenFlow, tikTokRefreshTokenFlow);
    }

    @Bean
    public TikTokApiHttpClient tikTokApiHttpClient() {
        WebClient webClient = WebClient.builder()
            .baseUrl("https://open-api.tiktok.com")
            .build();

        WebClientAdapter exchangeAdapter = WebClientAdapter.create(webClient);

        return HttpServiceProxyFactory.builderFor(exchangeAdapter)
            .build()
            .createClient(TikTokApiHttpClient.class);
    }

    @Bean
    public TikTokUploadHttpClient tikTokUploadHttpClient() {
        Consumer<ClientCodecConfigurer> codec = configurer -> configurer
            .defaultCodecs()
            .maxInMemorySize(16 * 1024 * 1024);

        WebClient webClient = WebClient.builder()
            .baseUrl("https://open-api.tiktok.com")
            .codecs(codec)
            .build();

        WebClientAdapter exchangeAdapter = WebClientAdapter.create(webClient);

        return HttpServiceProxyFactory.builderFor(exchangeAdapter)
            .build()
            .createClient(TikTokUploadHttpClient.class);
    }
}
