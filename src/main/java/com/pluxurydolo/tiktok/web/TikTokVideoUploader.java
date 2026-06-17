package com.pluxurydolo.tiktok.web;

import com.pluxurydolo.tiktok.dto.response.VideoUploadResponse;
import com.pluxurydolo.tiktok.exception.TikTokVideoUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_RANGE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public class TikTokVideoUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(TikTokVideoUploader.class);

    private final WebClient webClient;

    public TikTokVideoUploader() {
        Consumer<ClientCodecConfigurer> codecConfigurerConsumer = config -> config
            .defaultCodecs()
            .maxInMemorySize(16 * 1024 * 1024);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(codecConfigurerConsumer)
            .build();

        this.webClient = WebClient.builder()
            .exchangeStrategies(exchangeStrategies)
            .build();
    }

    public Mono<VideoUploadResponse> upload(String uploadUrl, byte[] video) {
        String contentLength = String.valueOf(video.length);
        String contentRange = "bytes 0-" + (video.length - 1) + "/" + video.length;

        return webClient.put()
            .uri(uploadUrl)
            .header(CONTENT_TYPE, "video/mp4")
            .header(CONTENT_LENGTH, contentLength)
            .header(CONTENT_RANGE, contentRange)
            .bodyValue(video)
            .retrieve()
            .bodyToMono(VideoUploadResponse.class)
            .doOnSuccess(_ -> LOGGER.info("wepm [tiktok-starter] Видео успешно загружено"))
            .onErrorResume(throwable -> {
                LOGGER.error("tnih [tiktok-starter] Произошла ошибка при загрузке видео");
                return Mono.error(new TikTokVideoUploadException(throwable));
            });
    }
}
