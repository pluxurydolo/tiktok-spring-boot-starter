package com.pluxurydolo.tiktok.scheduler;

import com.pluxurydolo.tiktok.base.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TikTokRefreshTokenSchedulerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private TikTokRefreshTokenScheduler scheduler;

    @Test
    void testSchedule() {
        assertDoesNotThrow(scheduler::schedule);
    }
}
