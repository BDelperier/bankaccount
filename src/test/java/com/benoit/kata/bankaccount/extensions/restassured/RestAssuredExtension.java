package com.benoit.kata.bankaccount.extensions.restassured;

import io.restassured.RestAssured;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

public class RestAssuredExtension implements BeforeEachCallback, AfterEachCallback {

    private static final Logger log = LoggerFactory.getLogger(RestAssuredExtension.class);

    private static final String LOCAL_SERVER_PORT_PROP_KEY = "local.server.port";

    @Override
    @SuppressWarnings("squid:S2696")
    public void beforeEach(ExtensionContext context) {
        Optional.ofNullable(SpringExtension.getApplicationContext(context))
                .map(ApplicationContext::getEnvironment)
                .map(env -> env.getProperty(LOCAL_SERVER_PORT_PROP_KEY))
                .map(Integer::valueOf)
                .ifPresent(localServerPort -> {
                    log.debug("Local server port is {}", localServerPort);
                    RestAssured.port = localServerPort;
                });
    }

    @Override
    public void afterEach(ExtensionContext context) {
        RestAssured.reset();
    }
}