package com.benoit.kata.bankaccount.extensions.restassured;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.skyscreamer.jsonassert.comparator.JSONComparator;

/**
 * A Hamcrest matcher to compare json contents.
 */
public final class JsonMatcher extends TypeSafeMatcher<String> {

    private final JSONComparator jsonComparator;
    private final String expectedJson;
    private JSONCompareResult result;

    private JsonMatcher(JSONComparator jsonComparator, String expectedJson) {
        this.jsonComparator = jsonComparator;
        this.expectedJson = expectedJson;
    }

    @Override
    public void describeTo(Description description) {

        if (result != null) {
            description.appendText(result.getMessage());
        } else {
            description.appendText("A valid JSON");
        }
    }

    @Override
    protected boolean matchesSafely(String item) {
        try {
            result = JSONCompare.compareJSON(expectedJson, item, jsonComparator);
            return result.passed();
        } catch (JSONException e) {
            throw new IllegalArgumentException("Unable to parse expected JSON", e);
        }
    }

    /**
     * The default JSON matcher.
     *
     * @param mode Compare mode ( strict, lenient, ... )
     * @param expectedJson JSON expected string
     * @return JSON Hamcrest matcher instance
     */
    public static Matcher<String> jsonMatcher(JSONCompareMode mode, String expectedJson) {
        return new JsonMatcher(new DefaultComparator(mode), expectedJson);
    }
}
