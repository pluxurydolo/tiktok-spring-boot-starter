package com.pluxurydolo.tiktok.base;

import com.pluxurydolo.tiktok.TestApplication;
import com.pluxurydolo.tiktok.configuration.HookTestConfiguration;
import com.pluxurydolo.tiktok.configuration.TokensTestConfiguration;
import com.pluxurydolo.tiktok.configuration.WebTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(classes = {
    TestApplication.class,
    TokensTestConfiguration.class,
    HookTestConfiguration.class,
    WebTestConfiguration.class
})
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTests {
}
