package com.pluxurydolo.tiktok.scheduler.handler;

import com.pluxurydolo.tiktok.flow.oauth.TikTokRefreshTokenFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class TikTokRefreshTokenSchedulerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokRefreshTokenSchedulerHandler.class);

    private final TikTokRefreshTokenFlow tikTokRefreshTokenFlow;

    public TikTokRefreshTokenSchedulerHandler(TikTokRefreshTokenFlow tikTokRefreshTokenFlow) {
        this.tikTokRefreshTokenFlow = tikTokRefreshTokenFlow;
    }

    public Mono<String> handle(String jobName) {
        LOGGER.info("tgqm [tiktok-starter] Стартовала джоба {}", jobName);

        return tikTokRefreshTokenFlow.refreshToken()
            .doOnSuccess(_ -> LOGGER.info("bxkm [tiktok-starter] Джоба {} успешно завершена", jobName))
            .doOnError(_ -> LOGGER.error("zwmf [tiktok-starter] Произошла ошибка при завершении джобы {}", jobName));
    }
}
