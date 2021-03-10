package com.amsa.fgb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Json {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Json() {
        // prevent instantiation
    }

    public static boolean equals(String json1, String json2) {
        ObjectMapper m = new ObjectMapper();
        try {
            return m.readTree(json1).equals(m.readTree(json2));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String prettyPrint(String json) {
        try {
            Object v = MAPPER.readValue(json, Object.class);
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(v);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
