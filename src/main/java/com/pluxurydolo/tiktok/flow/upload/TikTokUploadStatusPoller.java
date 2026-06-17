package com.pluxurydolo.tiktok.flow.upload;

import com.pluxurydolo.tiktok.dto.request.VideoStatusRequest;
import com.pluxurydolo.tiktok.dto.response.VideoStatusResponse;
import com.pluxurydolo.tiktok.dto.response.data.StatusData;
import com.pluxurydolo.tiktok.exception.TikTokUploadStatusException;
import com.pluxurydolo.tiktok.properties.TikTokPollingProperties;
import com.pluxurydolo.tiktok.web.TikTokUploadHttpClient;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.function.Function;

public class TikTokUploadStatusPoller {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokUploadStatusPoller.class);

    private final TikTokUploadHttpClient tikTokUploadHttpClient;
    private final TikTokPollingProperties tikTokPollingProperties;

    public TikTokUploadStatusPoller(
        TikTokUploadHttpClient tikTokUploadHttpClient,
        TikTokPollingProperties tikTokPollingProperties
    ) {
        this.tikTokUploadHttpClient = tikTokUploadHttpClient;
        this.tikTokPollingProperties = tikTokPollingProperties;
    }

    public Mono<String> poll(String authorization, String publishId) {
        Duration delay = tikTokPollingProperties.delay();
        long delaySeconds = delay.getSeconds();
        int maxRepeat = tikTokPollingProperties.maxRepeat();

        Function<Flux<Long>, Publisher<?>> onRepeat = repeat -> repeat
            .doOnNext(repeatNum -> LOGGER.info(
                "utxa [tiktok-starter] Повторная попытка обработки загрузки произойдет через {} секунд ({}/{})",
                delaySeconds, repeatNum + 1, maxRepeat
            ))
            .delayElements(delay, Schedulers.boundedElastic());

        return Mono.defer(() -> validateContainerStatus(authorization, publishId))
            .repeatWhenEmpty(maxRepeat, onRepeat);
    }

    private Mono<String> validateContainerStatus(String authorization, String publishId) {
        VideoStatusRequest request = new VideoStatusRequest(publishId);

        return tikTokUploadHttpClient.getUploadStatus(authorization, request)
            .map(VideoStatusResponse::data)
            .map(StatusData::status)
            .doOnNext(status -> LOGGER.info("hhno [tiktok-starter] Статус загрузки: {}", status))
            .onErrorResume(throwable -> {
                LOGGER.error("domo [tiktok-starter] Произошла ошибка при проверке статуса загрузки {}", publishId);
                return Mono.error(new TikTokUploadStatusException(throwable));
            })
            .filter("SUCCESS"::equals);
    }
}
