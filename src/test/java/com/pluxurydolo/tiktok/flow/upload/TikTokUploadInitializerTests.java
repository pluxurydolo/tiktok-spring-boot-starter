package com.pluxurydolo.tiktok.flow.upload;

import com.pluxurydolo.tiktok.dto.request.PublishVideoRequest;
import com.pluxurydolo.tiktok.dto.response.VideoInitResponse;
import com.pluxurydolo.tiktok.dto.response.data.VideoData;
import com.pluxurydolo.tiktok.dto.response.error.Error;
import com.pluxurydolo.tiktok.exception.TikTokInitUploadException;
import com.pluxurydolo.tiktok.web.TikTokUploadHttpClient;
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
class TikTokUploadInitializerTests {

    @Mock
    private TikTokUploadHttpClient tikTokUploadHttpClient;

    @InjectMocks
    private TikTokUploadInitializer tikTokUploadInitializer;

    @Test
    void testInitialize() {
        when(tikTokUploadHttpClient.initUpload(anyString(), any()))
            .thenReturn(Mono.just(videoInitResponse()));

        Mono<VideoInitResponse> result = tikTokUploadInitializer.initialize("authorization", publishVideoRequest());

        create(result)
            .expectNext(videoInitResponse())
            .verifyComplete();
    }

    @Test
    void testInitializeWhenExceptionOccurred() {
        when(tikTokUploadHttpClient.initUpload(anyString(), any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<VideoInitResponse> result = tikTokUploadInitializer.initialize("authorization", publishVideoRequest());

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(TikTokInitUploadException.class));
    }

    private static PublishVideoRequest publishVideoRequest() {
        byte[] bytes = {};
        return new PublishVideoRequest(bytes, "title");
    }

    private static VideoInitResponse videoInitResponse() {
        VideoData videoData = new VideoData("publishId", "uploadUrl");
        Error error = new Error("code", "message");
        return new VideoInitResponse(videoData, error);
    }
}
