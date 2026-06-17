package com.pluxurydolo.tiktok.configuration;

import com.pluxurydolo.tiktok.token.AbstractTokenRetriever;
import com.pluxurydolo.tiktok.token.AbstractTokenSaver;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.Map;

import static java.time.Clock.systemUTC;

@TestConfiguration
public class TokensTestConfiguration {

    @Bean
    public AbstractTokenRetriever abstractTokensRetriever() {
        return new AbstractTokenRetriever() {

            @Override
            public Mono<Map<String, String>> retrieveTokens() {
                return Mono.just(
                    Map.of(
                        "access_token", "accessToken",
                        "refresh_token", "refreshToken"
                    )
                );
            }
        };
    }

    @Bean
    public AbstractTokenSaver abstractTokensSaver() {
        return new AbstractTokenSaver(systemUTC()) {

            @Override
            public Mono<String> saveTokens(Map<String, String> tokens) {
                return Mono.just("saveTokens");
            }
        };
    }

    @Bean
    public Clock clock() {
        return systemUTC();
    }
}
