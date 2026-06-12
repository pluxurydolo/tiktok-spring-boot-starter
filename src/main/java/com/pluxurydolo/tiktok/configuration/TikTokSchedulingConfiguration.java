package com.pluxurydolo.tiktok.configuration;

import com.pluxurydolo.tiktok.scheduler.TikTokRefreshTokenScheduler;
import com.pluxurydolo.tiktok.scheduler.handler.TikTokRefreshTokenSchedulerHandler;
import com.pluxurydolo.tiktok.scheduler.hook.RefreshTokenSchedulerHandlerHook;
import com.pluxurydolo.tiktok.flow.oauth.TikTokRefreshTokenFlow;
import com.pluxurydolo.tiktok.token.AbstractTokenRetriever;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class TikTokSchedulingConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TikTokRefreshTokenScheduler tikTokRefreshTokenScheduler(
        TikTokRefreshTokenSchedulerHandler tikTokRefreshTokenSchedulerHandler
    ) {
        return new TikTokRefreshTokenScheduler(tikTokRefreshTokenSchedulerHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public TikTokRefreshTokenSchedulerHandler tikTokRefreshTokenSchedulerHandler(
        TikTokRefreshTokenFlow tikTokRefreshTokenFlow,
        AbstractTokenRetriever abstractTokenRetriever,
        RefreshTokenSchedulerHandlerHook refreshTokenSchedulerHandlerHook
    ) {
        return new TikTokRefreshTokenSchedulerHandler(
            tikTokRefreshTokenFlow,
            abstractTokenRetriever,
            refreshTokenSchedulerHandlerHook
        );
    }
}
