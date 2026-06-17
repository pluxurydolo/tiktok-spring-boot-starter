package com.pluxurydolo.tiktok.scheduler.handler;

import com.pluxurydolo.tiktok.flow.oauth.TikTokRefreshTokenFlow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class TikTokRefreshTokenSchedulerHandlerTests {

    @Mock
    private TikTokRefreshTokenFlow tikTokRefreshTokenFlow;

    @InjectMocks
    private TikTokRefreshTokenSchedulerHandler tikTokRefreshTokenSchedulerHandler;

    @Test
    void testHandle() {
        when(tikTokRefreshTokenFlow.refreshToken())
            .thenReturn(Mono.just(""));

        Mono<String> result = tikTokRefreshTokenSchedulerHandler.handle("jobName");

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    @Test
    void testHandleWhenExceptionOccurred() {
        when(tikTokRefreshTokenFlow.refreshToken())
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = tikTokRefreshTokenSchedulerHandler.handle("jobName");

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(RuntimeException.class));
    }
}
