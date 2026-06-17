package com.pluxurydolo.tiktok.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
public abstract class AbstractControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    protected WebTestClient webTestClient;
}
