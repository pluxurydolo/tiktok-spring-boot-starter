package com.pluxurydolo.tiktok.flow.upload;

import com.pluxurydolo.tiktok.dto.request.PublishVideoRequest;
import com.pluxurydolo.tiktok.dto.response.VideoUploadResponse;
import com.pluxurydolo.tiktok.exception.TikTokInitUploadException;
import com.pluxurydolo.tiktok.web.TikTokUploadHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class TikTokVideoUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokVideoUploader.class);

    private final TikTokUploadHttpClient tikTokUploadHttpClient;

    public TikTokVideoUploader(TikTokUploadHttpClient tikTokUploadHttpClient) {
        this.tikTokUploadHttpClient = tikTokUploadHttpClient;
    }

    public Mono<VideoUploadResponse> upload(String uploadToken, PublishVideoRequest request) {
        String title = request.title();
        byte[] video = request.video();

        return tikTokUploadHttpClient.uploadVideo(uploadToken, video)
            .doOnSuccess(_ -> LOGGER.info("wepm [tiktok-starter] Видео {} успешно загружено", title))
            .onErrorResume(throwable -> {
                LOGGER.error("tnih [tiktok-starter] Произошла ошибка при загрузке видео {}", title);
                return Mono.error(new TikTokInitUploadException(throwable));
            });
    }
}
