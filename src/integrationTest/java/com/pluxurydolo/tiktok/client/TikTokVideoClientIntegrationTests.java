package com.pluxurydolo.tiktok.client;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.core.spi.AppenderAttachable;
import com.pluxurydolo.tiktok.base.AbstractIntegrationTests;
import com.pluxurydolo.tiktok.dto.request.PublishVideoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.slf4j.LoggerFactory.getLogger;

class TikTokVideoClientIntegrationTests extends AbstractIntegrationTests {
    private static final AppenderAttachable<ILoggingEvent> LOGGER =
        (Logger) getLogger(TikTokVideoClient.class);

    @Autowired
    private TikTokVideoClient tikTokVideoClient;

    @Test
    void testPublishVideo() {
        List<ILoggingEvent> logs = listAppender().list;

        tikTokVideoClient.publishVideo(publishVideoRequest())
            .subscribe();

        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                assertThat(logs)
                    .hasSize(1);

                assertThat(logs.getFirst().getFormattedMessage())
                    .isEqualTo("dizf [tiktok-starter] Видео успешно опубликовано");
            });
    }

    private static ListAppender<ILoggingEvent> listAppender() {
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        LOGGER.addAppender(listAppender);
        return listAppender;
    }

    private static PublishVideoRequest publishVideoRequest() {
        byte[] bytes = {};
        return new PublishVideoRequest(bytes, "title");
    }
}
