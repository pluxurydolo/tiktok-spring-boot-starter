package com.pluxurydolo.tiktok.configuration;

import com.pluxurydolo.tiktok.dto.response.TokenResponse;
import com.pluxurydolo.tiktok.dto.response.VideoInitResponse;
import com.pluxurydolo.tiktok.dto.response.VideoStatusResponse;
import com.pluxurydolo.tiktok.dto.response.VideoUploadResponse;
import com.pluxurydolo.tiktok.dto.response.data.StatusData;
import com.pluxurydolo.tiktok.dto.response.data.UploadData;
import com.pluxurydolo.tiktok.dto.response.data.VideoData;
import com.pluxurydolo.tiktok.dto.response.error.Error;
import com.pluxurydolo.tiktok.web.TikTokApiHttpClient;
import com.pluxurydolo.tiktok.web.TikTokUploadHttpClient;
import com.pluxurydolo.tiktok.web.TikTokVideoUploader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class WebTestConfiguration {

    @Bean
    public TikTokApiHttpClient tikTokApiHttpClient() {
        TikTokApiHttpClient mock = mock(TikTokApiHttpClient.class);

        when(mock.getAccessToken(any()))
            .thenReturn(Mono.just(tokenResponse()));
        when(mock.refreshToken(any()))
            .thenReturn(Mono.just(tokenResponse()));
        return mock;
    }

    @Bean
    public TikTokUploadHttpClient tikTokUploadHttpClient() {
        TikTokUploadHttpClient mock = mock(TikTokUploadHttpClient.class);

        when(mock.initUpload(anyString(), any()))
            .thenReturn(Mono.just(videoInitResponse()));
        when(mock.getUploadStatus(anyString(), any()))
            .thenReturn(Mono.just(videoStatusResponse()));

        return mock;
    }

    @Bean
    public TikTokVideoUploader tikTokVideoUploader() {
        TikTokVideoUploader mock = mock(TikTokVideoUploader.class);

        when(mock.upload(anyString(), any()))
            .thenReturn(Mono.just(videoUploadResponse()));

        return mock;
    }

    private static TokenResponse tokenResponse() {
        return new TokenResponse(
            "accessToken",
            1,
            "refreshToken",
            1,
            "openId",
            "scope",
            "tokenType",
            "error",
            "errorDescription"
        );
    }

    private static VideoInitResponse videoInitResponse() {
        VideoData videoData = new VideoData("publishId", "uploadUrl");
        return new VideoInitResponse(videoData, error());
    }

    private static VideoUploadResponse videoUploadResponse() {
        UploadData uploadData = new UploadData("publishId", "SUCCESS");
        return new VideoUploadResponse(uploadData, error());
    }

    private static VideoStatusResponse videoStatusResponse() {
        StatusData statusData = new StatusData("publishId", "SUCCESS", "failReason", "videoId");
        return new VideoStatusResponse(statusData, error());
    }

    private static Error error() {
        return new Error("code", "message");
    }
}
