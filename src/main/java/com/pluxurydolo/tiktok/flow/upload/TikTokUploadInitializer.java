package com.pluxurydolo.tiktok.flow.upload;

import com.pluxurydolo.tiktok.dto.request.PostInfo;
import com.pluxurydolo.tiktok.dto.request.PublishVideoRequest;
import com.pluxurydolo.tiktok.dto.request.SourceInfo;
import com.pluxurydolo.tiktok.dto.request.VideoInitRequest;
import com.pluxurydolo.tiktok.dto.response.VideoInitResponse;
import com.pluxurydolo.tiktok.exception.TikTokInitUploadException;
import com.pluxurydolo.tiktok.web.TikTokUploadHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class TikTokUploadInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokUploadInitializer.class);

    private final TikTokUploadHttpClient tikTokUploadHttpClient;

    public TikTokUploadInitializer(TikTokUploadHttpClient tikTokUploadHttpClient) {
        this.tikTokUploadHttpClient = tikTokUploadHttpClient;
    }

    public Mono<VideoInitResponse> initialize(String authorization, PublishVideoRequest request) {
        String title = request.title();
        byte[] video = request.video();

//        String privacyLevel = "PUBLIC_TO_EVERYONE"; TODO раскомментировать после модерации
        String privacyLevel = "SELF_ONLY";
        boolean disableComment = false;
        boolean disableDuet = false;
        boolean disableStitch = false;

        PostInfo postInfo = new PostInfo(title, privacyLevel, disableComment, disableDuet, disableStitch);

        String source = "FILE_UPLOAD";
        long videoSize = video.length;
        SourceInfo sourceInfo = new SourceInfo(source, videoSize);

        VideoInitRequest videoInitRequest = new VideoInitRequest(postInfo, sourceInfo);

        return tikTokUploadHttpClient.initUpload(authorization, videoInitRequest)
            .doOnSuccess(_ -> LOGGER.info("qglk [tiktok-starter] Загрузка видео {} успешно инициализирована", title))
            .onErrorResume(throwable -> {
                LOGGER.error("qtzx [tiktok-starter] Произошла ошибка при инициализации загрузки видео {}", title);
                return Mono.error(new TikTokInitUploadException(throwable));
            });
    }
}
