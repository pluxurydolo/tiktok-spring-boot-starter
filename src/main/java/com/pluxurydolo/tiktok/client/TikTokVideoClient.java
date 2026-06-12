package com.pluxurydolo.tiktok.client;

import com.pluxurydolo.tiktok.dto.request.PublishVideoRequest;
import com.pluxurydolo.tiktok.exception.TikTokPublicationException;
import com.pluxurydolo.tiktok.flow.upload.TikTokVideoPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class TikTokVideoClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokVideoClient.class);

    private final TikTokVideoPublisher tikTokVideoPublisher;

    public TikTokVideoClient(TikTokVideoPublisher tikTokVideoPublisher) {
        this.tikTokVideoPublisher = tikTokVideoPublisher;
    }

    public Mono<String> publishVideo(PublishVideoRequest request) {
        return tikTokVideoPublisher.publish(request)
            .doOnSuccess(_ -> LOGGER.info("dizf [tiktok-starter] Видео успешно опубликовано"))
            .onErrorResume(throwable -> {
                LOGGER.info("oiqk [tiktok-starter] Произошла ошибка при публикации видео");
                return Mono.error(new TikTokPublicationException(throwable));
            })
            .subscribeOn(Schedulers.boundedElastic());
    }
}
