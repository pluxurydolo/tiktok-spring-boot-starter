package com.pluxurydolo.tiktok.flow.oauth.hook;

import reactor.core.publisher.Mono;

public interface RefreshTokenFlowHook {
    Mono<String> doAfter();

    Mono<String> handleException(Throwable throwable);
}
