package com.pluxurydolo.tiktok.web;

import com.pluxurydolo.tiktok.dto.request.VideoInitRequest;
import com.pluxurydolo.tiktok.dto.request.VideoStatusRequest;
import com.pluxurydolo.tiktok.dto.response.VideoInitResponse;
import com.pluxurydolo.tiktok.dto.response.VideoStatusResponse;
import com.pluxurydolo.tiktok.dto.response.VideoUploadResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

@HttpExchange("https://open.tiktokapis.com")
public interface TikTokUploadHttpClient {

    @PostExchange("/v2/video/upload/init/")
    Mono<VideoInitResponse> initUpload(
        @RequestHeader("Authorization") String authorization,
        @RequestBody VideoInitRequest request
    );

    @PostExchange(
        value = "/v2/video/upload/",
        contentType = APPLICATION_OCTET_STREAM_VALUE
    )
    Mono<VideoUploadResponse> uploadVideo(
        @RequestParam("upload_token") String uploadToken,
        @RequestBody byte[] video
    );

    @PostExchange("/v2/video/upload/status/")
    Mono<VideoStatusResponse> checkStatus(
        @RequestHeader("Authorization") String authorization,
        @RequestBody VideoStatusRequest request
    );
}
