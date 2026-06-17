package com.pluxurydolo.tiktok.flow.upload;

import com.pluxurydolo.tiktok.dto.TikTokTokens;
import com.pluxurydolo.tiktok.dto.request.PublishVideoRequest;
import com.pluxurydolo.tiktok.dto.response.VideoInitResponse;
import com.pluxurydolo.tiktok.dto.response.VideoUploadResponse;
import com.pluxurydolo.tiktok.dto.response.data.UploadData;
import com.pluxurydolo.tiktok.dto.response.data.VideoData;
import com.pluxurydolo.tiktok.dto.response.error.Error;
import com.pluxurydolo.tiktok.token.AbstractTokenRetriever;
import com.pluxurydolo.tiktok.web.TikTokVideoUploader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class TikTokVideoPublisherTests {

    @Mock
    private TikTokUploadInitializer tikTokUploadInitializer;

    @Mock
    private TikTokVideoUploader tikTokVideoUploader;

    @Mock
    private TikTokUploadStatusPoller tikTokUploadStatusPoller;

    @Mock
    private AbstractTokenRetriever abstractTokenRetriever;

    @InjectMocks
    private TikTokVideoPublisher tikTokVideoPublisher;

    @Test
    void testPublish() {
        when(abstractTokenRetriever.retrieve())
            .thenReturn(Mono.just(tikTokTokens()));
        when(tikTokUploadInitializer.initialize(anyString(), any()))
            .thenReturn(Mono.just(videoInitResponse()));
        when(tikTokVideoUploader.upload(anyString(), any()))
            .thenReturn(Mono.just(videoUploadResponse()));
        when(tikTokUploadStatusPoller.poll(anyString(), anyString()))
            .thenReturn(Mono.just(""));

        Mono<String> result = tikTokVideoPublisher.publish(publishVideoRequest());

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    @Test
    void testPublishWhenExceptionOccurred() {
        when(abstractTokenRetriever.retrieve())
            .thenReturn(Mono.just(tikTokTokens()));
        when(tikTokUploadInitializer.initialize(anyString(), any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = tikTokVideoPublisher.publish(publishVideoRequest());

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(RuntimeException.class));
    }

    private static PublishVideoRequest publishVideoRequest() {
        byte[] bytes = {};
        return new PublishVideoRequest(bytes, "title");
    }

    private static TikTokTokens tikTokTokens() {
        return new TikTokTokens("accessToken", "refreshToken");
    }

    private static VideoInitResponse videoInitResponse() {
        VideoData videoData = new VideoData("publishId", "uploadUrl");
        return new VideoInitResponse(videoData, error());
    }

    private static VideoUploadResponse videoUploadResponse() {
        UploadData uploadData = new UploadData("publishId", "status");
        return new VideoUploadResponse(uploadData, error());
    }

    private static Error error() {
        return new Error("code", "message");
    }
}
