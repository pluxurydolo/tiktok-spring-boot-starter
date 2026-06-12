package com.pluxurydolo.tiktok.flow.upload;

import com.pluxurydolo.tiktok.dto.TikTokTokens;
import com.pluxurydolo.tiktok.dto.request.PublishVideoRequest;
import com.pluxurydolo.tiktok.dto.response.VideoInitResponse;
import com.pluxurydolo.tiktok.dto.response.VideoUploadResponse;
import com.pluxurydolo.tiktok.token.AbstractTokenRetriever;
import reactor.core.publisher.Mono;

public class TikTokVideoPublisher {
    private final TikTokUploadInitializer tikTokUploadInitializer;
    private final TikTokVideoUploader tikTokVideoUploader;
    private final TikTokUploadStatusPoller tikTokUploadStatusPoller;
    private final AbstractTokenRetriever abstractTokenRetriever;

    public TikTokVideoPublisher(
        TikTokUploadInitializer tikTokUploadInitializer,
        TikTokVideoUploader tikTokVideoUploader,
        TikTokUploadStatusPoller tikTokUploadStatusPoller,
        AbstractTokenRetriever abstractTokenRetriever
    ) {
        this.tikTokUploadInitializer = tikTokUploadInitializer;
        this.tikTokVideoUploader = tikTokVideoUploader;
        this.tikTokUploadStatusPoller = tikTokUploadStatusPoller;
        this.abstractTokenRetriever = abstractTokenRetriever;
    }

    public Mono<String> publish(PublishVideoRequest request) {
        return abstractTokenRetriever.retrieve()
            .flatMap(tikTokTokens -> publishVideo(tikTokTokens, request));
    }

    private Mono<String> publishVideo(TikTokTokens tikTokTokens, PublishVideoRequest request) {
        String accessToken = tikTokTokens.accessToken();
        String authorization = String.format("Bearer %s", accessToken);

        return tikTokUploadInitializer.initialize(authorization, request)
            .flatMap(response -> uploadVideo(response, request))
            .flatMap(response -> poll(authorization, response));
    }

    private Mono<VideoUploadResponse> uploadVideo(VideoInitResponse response, PublishVideoRequest request) {
        String uploadUrl = response.data().uploadUrl();
        String uploadToken = uploadUrl.substring(uploadUrl.indexOf("upload_token=") + "upload_token=".length());
        return tikTokVideoUploader.upload(uploadToken, request);
    }

    private Mono<String> poll(String authorization, VideoUploadResponse response) {
        String publishId = response.data().publishId();
        return tikTokUploadStatusPoller.poll(authorization, publishId);
    }
}
