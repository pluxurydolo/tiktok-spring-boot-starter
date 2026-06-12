package com.pluxurydolo.tiktok.scheduler;

import com.pluxurydolo.tiktok.scheduler.handler.TikTokRefreshTokenSchedulerHandler;
import org.springframework.scheduling.annotation.Scheduled;

public class TikTokRefreshTokenScheduler {
    private final TikTokRefreshTokenSchedulerHandler tikTokRefreshTokenSchedulerHandler;

    public TikTokRefreshTokenScheduler(TikTokRefreshTokenSchedulerHandler tikTokRefreshTokenSchedulerHandler) {
        this.tikTokRefreshTokenSchedulerHandler = tikTokRefreshTokenSchedulerHandler;
    }

    @Scheduled(
        cron = "${tiktok.scheduler.refresh-token.cron}",
        zone = "${tiktok.scheduler.refresh-token.zone}"
    )
    public void schedule() {
        String jobName = getClass().getName();

        tikTokRefreshTokenSchedulerHandler.handle(jobName)
            .subscribe();
    }
}
