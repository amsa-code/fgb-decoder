package com.amsa.fgb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.junit.ComparisonFailure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class TestingUtil {

    private TestingUtil() {
        // prevent instantiation
    }

    public static String readResource(String resourceName) {
        try (InputStream in = TestingUtil.class.getResourceAsStream(resourceName)) {
            return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void assertResourceEqualsJson(String resourceName, String json) {
        String expected = readResource(resourceName);
        assertJsonEquals(expected, json);
    }

    public static void assertJsonEquals(String expected, String json) throws ComparisonFailure {
        if (!equals(expected, json)) {
            System.out.println("not expected: " + json);
            throw new ComparisonFailure("unequal json", Json.prettyPrint(expected), Json.prettyPrint(json));
        }
    }
    
    private static boolean equals(String json1, String json2) {
        ObjectMapper m = new ObjectMapper();
        try {
            return m.readTree(json1).equals(m.readTree(json2));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
