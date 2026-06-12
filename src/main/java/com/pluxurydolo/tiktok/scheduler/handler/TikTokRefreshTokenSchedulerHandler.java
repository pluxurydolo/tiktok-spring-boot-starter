package com.pluxurydolo.tiktok.scheduler.handler;

import com.pluxurydolo.tiktok.dto.TikTokTokens;
import com.pluxurydolo.tiktok.flow.oauth.TikTokRefreshTokenFlow;
import com.pluxurydolo.tiktok.scheduler.hook.RefreshTokenSchedulerHandlerHook;
import com.pluxurydolo.tiktok.token.AbstractTokenRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class TikTokRefreshTokenSchedulerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokRefreshTokenSchedulerHandler.class);

    private final TikTokRefreshTokenFlow tikTokRefreshTokenFlow;
    private final AbstractTokenRetriever abstractTokenRetriever;
    private final RefreshTokenSchedulerHandlerHook refreshTokenSchedulerHandlerHook;

    public TikTokRefreshTokenSchedulerHandler(
        TikTokRefreshTokenFlow tikTokRefreshTokenFlow,
        AbstractTokenRetriever abstractTokenRetriever,
        RefreshTokenSchedulerHandlerHook refreshTokenSchedulerHandlerHook
    ) {
        this.tikTokRefreshTokenFlow = tikTokRefreshTokenFlow;
        this.abstractTokenRetriever = abstractTokenRetriever;
        this.refreshTokenSchedulerHandlerHook = refreshTokenSchedulerHandlerHook;
    }

    public Mono<String> handle(String jobName) {
        LOGGER.info("tgqm [tiktok-starter] Стартовала джоба {}", jobName);

        return abstractTokenRetriever.retrieve()
            .map(TikTokTokens::accessToken)
            .flatMap(tikTokRefreshTokenFlow::refreshToken)
            .flatMap(_ -> refreshTokenSchedulerHandlerHook.doAfter())
            .doOnSuccess(_ -> LOGGER.info("bxkm [tiktok-starter] Джоба {} успешно завершена", jobName))
            .onErrorResume(throwable -> {
                LOGGER.error("zwmf [tiktok-starter] Произошла ошибка при завершении джобы {}", jobName);
                return refreshTokenSchedulerHandlerHook.handleException(throwable, jobName);
            });
    }
}
