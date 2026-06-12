package com.pluxurydolo.tiktok.configuration;

import com.pluxurydolo.tiktok.flow.upload.TikTokUploadInitializer;
import com.pluxurydolo.tiktok.flow.upload.TikTokUploadStatusPoller;
import com.pluxurydolo.tiktok.flow.upload.TikTokVideoPublisher;
import com.pluxurydolo.tiktok.flow.upload.TikTokVideoUploader;
import com.pluxurydolo.tiktok.token.AbstractTokenRetriever;
import com.pluxurydolo.tiktok.web.TikTokUploadHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TikTokUploadConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TikTokVideoPublisher tikTokVideoPublisher(
        TikTokUploadInitializer tikTokUploadInitializer,
        TikTokVideoUploader tikTokVideoUploader,
        TikTokUploadStatusPoller tikTokUploadStatusPoller,
        AbstractTokenRetriever abstractTokenRetriever
    ) {
        return new TikTokVideoPublisher(
            tikTokUploadInitializer,
            tikTokVideoUploader,
            tikTokUploadStatusPoller,
            abstractTokenRetriever
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public TikTokUploadInitializer tikTokUploadInitializer(TikTokUploadHttpClient tikTokUploadHttpClient) {
        return new TikTokUploadInitializer(tikTokUploadHttpClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public TikTokVideoUploader tikTokVideoUploader(TikTokUploadHttpClient tikTokUploadHttpClient) {
        return new TikTokVideoUploader(tikTokUploadHttpClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public TikTokUploadStatusPoller tikTokUploadStatusPoller(TikTokUploadHttpClient tikTokUploadHttpClient) {
        return new TikTokUploadStatusPoller(tikTokUploadHttpClient);
    }
}
