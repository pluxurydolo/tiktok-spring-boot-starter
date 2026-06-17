package com.pluxurydolo.tiktok.web;

import com.pluxurydolo.tiktok.dto.request.VideoInitRequest;
import com.pluxurydolo.tiktok.dto.request.VideoStatusRequest;
import com.pluxurydolo.tiktok.dto.response.VideoInitResponse;
import com.pluxurydolo.tiktok.dto.response.VideoStatusResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange("https://open.tiktokapis.com")
public interface TikTokUploadHttpClient {

    @PostExchange("/v2/post/publish/video/init/")
    Mono<VideoInitResponse> initUpload(
        @RequestHeader("Authorization") String authorization,
        @RequestBody VideoInitRequest request
    );

    @PostExchange("/v2/post/publish/status/")
    Mono<VideoStatusResponse> getUploadStatus(
        @RequestHeader("Authorization") String authorization,
        @RequestBody VideoStatusRequest request
    );
}
