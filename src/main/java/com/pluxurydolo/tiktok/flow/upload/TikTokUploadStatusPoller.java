package com.pluxurydolo.tiktok.flow.upload;

import com.pluxurydolo.tiktok.dto.request.VideoStatusRequest;
import com.pluxurydolo.tiktok.web.TikTokUploadHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class TikTokUploadStatusPoller {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokUploadStatusPoller.class);

    private final TikTokUploadHttpClient tikTokUploadHttpClient;

    public TikTokUploadStatusPoller(TikTokUploadHttpClient tikTokUploadHttpClient) {
        this.tikTokUploadHttpClient = tikTokUploadHttpClient;
    }

    // TODO Переписать
    public Mono<String> poll(String authorization, String publishId) {
        VideoStatusRequest request = new VideoStatusRequest(publishId);

        return tikTokUploadHttpClient.checkStatus(authorization, request)
            .flatMap(response -> {
                if (response.isSuccess()) {
                    LOGGER.info("aaaa [tiktok-starter] Видео обработано, video_id: {}", response.data().videoId());
                    return Mono.just(response.data().videoId());
                }

                if ("PROCESSING".equals(response.data().status())) {
                    LOGGER.info("bbbb [tiktok-starter] Видео обрабатывается, ждём 5 секунд...");
                    return Mono.delay(Duration.ofSeconds(5))
                        .then(poll(authorization, publishId));
                }

                return Mono.error(new RuntimeException("Ошибка обработки: " + response.data().failReason()));
            });
    }
}
