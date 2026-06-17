package com.pluxurydolo.tiktok.controller;

import com.pluxurydolo.tiktok.base.AbstractControllerIntegrationTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.LOCATION;

class TikTokOAuthControllerIntegrationTests extends AbstractControllerIntegrationTests {

    @Test
    void testLogin() {
        webTestClient.get()
            .uri("/app-name/v1/tiktok/login")
            .exchange()
            .expectStatus().isFound()
            .expectHeader().value(
                LOCATION,
                location -> assertThat(location).startsWith(locationHeader())
            )
            .expectBody().isEmpty();
    }

    @Test
    void testRedirect() {
        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/app-name/v1/tiktok/login/redirect")
                .queryParam("code", "code")
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .value(response -> assertThat(response).isEqualTo("SUCCESS"));
    }

    @Test
    void testRefresh() {
        webTestClient.get()
            .uri("/app-name/v1/tiktok/refresh-token")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .value(response -> assertThat(response).isEqualTo("SUCCESS"));
    }

    private static String locationHeader() {
        return "https://www.tiktok.com/v2/auth/authorize/?client_key=key&response_type=code&scope=user.info.basic,video.upload,video.publish&redirect_uri=http://localhost:8888$/app-name/v1/tiktok/login/redirect&state=";
    }
}
