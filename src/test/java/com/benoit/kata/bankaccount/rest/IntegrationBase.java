package com.benoit.kata.bankaccount.rest;

import com.benoit.kata.bankaccount.extensions.restassured.JsonMatcher;
import com.google.common.io.Resources;
import org.hamcrest.Matcher;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.charset.Charset;

@ActiveProfiles("test")
public abstract class IntegrationBase {

    private String resourceJson(String path) throws IOException {
        return Resources.toString(Resources.getResource(path), Charset.defaultCharset());
    }

    protected Matcher<String> jsonMatcher(String resourcePath) {
        try {
            return JsonMatcher.jsonMatcher(JSONCompareMode.STRICT_ORDER, resourceJson(resourcePath));
        } catch (IOException ioe) {
            throw new AssertionError("Cannot manage json resource '"+resourcePath+"'.", ioe);
        }
    }
}
