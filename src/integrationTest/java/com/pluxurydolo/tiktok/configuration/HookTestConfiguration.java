package com.pluxurydolo.tiktok.configuration;

import com.pluxurydolo.tiktok.flow.oauth.hook.AccessTokenFlowHook;
import com.pluxurydolo.tiktok.flow.oauth.hook.RefreshTokenFlowHook;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@TestConfiguration
public class HookTestConfiguration {

    @Bean
    public AccessTokenFlowHook accessTokenFlowHook() {
        return new AccessTokenFlowHook() {

            @Override
            public Mono<String> doAfter() {
                return Mono.just("");
            }

            @Override
            public Mono<String> handleException(Throwable throwable) {
                return Mono.just("");
            }
        };
    }

    @Bean
    public RefreshTokenFlowHook refreshTokenFlowHook() {
        return new RefreshTokenFlowHook() {

            @Override
            public Mono<String> doAfter() {
                return Mono.just("");
            }

            @Override
            public Mono<String> handleException(Throwable throwable) {
                return Mono.just("");
            }
        };
    }
}
