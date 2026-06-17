package com.pluxurydolo.tiktok.flow.upload;

import com.pluxurydolo.tiktok.dto.response.VideoStatusResponse;
import com.pluxurydolo.tiktok.dto.response.data.StatusData;
import com.pluxurydolo.tiktok.dto.response.error.Error;
import com.pluxurydolo.tiktok.exception.TikTokUploadStatusException;
import com.pluxurydolo.tiktok.properties.TikTokPollingProperties;
import com.pluxurydolo.tiktok.web.TikTokUploadHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class TikTokUploadStatusPollerTests {

    @Mock
    private TikTokUploadHttpClient tikTokUploadHttpClient;

    @Mock
    private TikTokPollingProperties tikTokPollingProperties;

    @InjectMocks
    private TikTokUploadStatusPoller tikTokUploadStatusPoller;

    @BeforeEach
    void setUp() {
        when(tikTokPollingProperties.delay())
            .thenReturn(Duration.ofMillis(100));
        when(tikTokPollingProperties.maxRepeat())
            .thenReturn(5);
    }

    @Test
    void testPoll() {
        when(tikTokUploadHttpClient.getUploadStatus(anyString(), any()))
            .thenReturn(Mono.just(videoStatusResponse("SUCCESS")));

        Mono<String> result = tikTokUploadStatusPoller.poll("authorization", "publishId");

        create(result)
            .expectNext("SUCCESS")
            .verifyComplete();
    }

    @Test
    void testPollWhenStatusIsProcessing() {
        when(tikTokUploadHttpClient.getUploadStatus(anyString(), any()))
            .thenReturn(Mono.just(videoStatusResponse("PROCESSING")));

        Mono<String> result = tikTokUploadStatusPoller.poll("authorization", "publishId");

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(IllegalStateException.class));
    }

    @Test
    void testPollWhenExceptionOccurred() {
        when(tikTokUploadHttpClient.getUploadStatus(anyString(), any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = tikTokUploadStatusPoller.poll("authorization", "publishId");

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(TikTokUploadStatusException.class));
    }

    private static VideoStatusResponse videoStatusResponse(String status) {
        StatusData statusData = new StatusData("publishId", status, "failReason", "videoId");
        Error error = new Error("code", "message");
        return new VideoStatusResponse(statusData, error);
    }
}
