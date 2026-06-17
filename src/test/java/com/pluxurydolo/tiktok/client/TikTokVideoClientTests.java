package com.pluxurydolo.tiktok.client;

import com.pluxurydolo.tiktok.dto.request.PublishVideoRequest;
import com.pluxurydolo.tiktok.exception.TikTokVideoPublicationException;
import com.pluxurydolo.tiktok.flow.upload.TikTokVideoPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class TikTokVideoClientTests {

    @Mock
    private TikTokVideoPublisher tikTokVideoPublisher;

    @InjectMocks
    private TikTokVideoClient tikTokVideoClient;

    @Test
    void testPublishVideo() {
        when(tikTokVideoPublisher.publish(any()))
            .thenReturn(Mono.just(""));

        Mono<String> result = tikTokVideoClient.publishVideo(publishVideoRequest());

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    @Test
    void testPublishVideoWhenExceptionOccurred() {
        when(tikTokVideoPublisher.publish(any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = tikTokVideoClient.publishVideo(publishVideoRequest());

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(TikTokVideoPublicationException.class));
    }

    private static PublishVideoRequest publishVideoRequest() {
        byte[] bytes = {};
        return new PublishVideoRequest(bytes, "title");
    }
}
