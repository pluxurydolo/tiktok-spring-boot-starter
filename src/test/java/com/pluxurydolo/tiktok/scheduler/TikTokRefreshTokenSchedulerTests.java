package com.pluxurydolo.tiktok.scheduler;

import com.pluxurydolo.tiktok.scheduler.handler.TikTokRefreshTokenSchedulerHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TikTokRefreshTokenSchedulerTests {

    @Mock
    private TikTokRefreshTokenSchedulerHandler tikTokRefreshTokenSchedulerHandler;

    @InjectMocks
    private TikTokRefreshTokenScheduler tikTokRefreshTokenScheduler;

    @Test
    void testSchedule() {
        when(tikTokRefreshTokenSchedulerHandler.handle(anyString()))
            .thenReturn(Mono.just(""));

        assertDoesNotThrow(tikTokRefreshTokenScheduler::schedule);
    }
}
