package com.amsa.fgb.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class DecodeAsJson implements DecodeFilter {

    public static final DecodeFilter INSTANCE = new DecodeAsJson();
    private static final String COLON = ":";

    private final Map<String, String> attributeTypes;

    private DecodeAsJson() {
        // prevent instantiation externally
        attributeTypes = loadAttributeTypes();
        System.out.println(attributeTypes);
    }

    private static Map<String, String> loadAttributeTypes() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                DecodeAsJson.class.getResourceAsStream("/attribute-types.txt"),
                StandardCharsets.UTF_8))) {
            return br //
                    .lines() //
                    .peek(System.out::println)
                    .map(x -> x.trim()) //
                    .filter(x -> !x.startsWith("#")) //
                    .filter(x -> !x.isEmpty()) //
                    .map(x -> x.split("\t")) //
                    .collect(Collectors.toMap(x -> x[0], x -> x[1]));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String quoted(String s) {
        return "\"" + escape(s) + "\"";
    }

    @Override
    public String getData(List<HexAttribute> v) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < v.size(); i++) {
            HexAttribute h = v.get(i);

            if (h.error != null && !h.error.trim().isEmpty()) {
                throw new RuntimeException("Error occurred at position " + h.getPos()
                        + " with desc='" + h.desc + "', value='" + h.value + "':" + h.error);
            }
            if (!h.getDesc().isEmpty()) {
                if (b.length() > 0) {
                    b.append(",\n");
                }
                b.append(quoted(h.getDesc()));
                b.append(COLON);
                b.append(getValue(h.getDesc(), h.getValue()));
            }
        }
        b.insert(0, "{");
        b.append("}");
        return b.toString();
    }

    private String getValue(String key, String value) {
        String type = attributeTypes.get(key);
        if (type == null) {
            throw new RuntimeException("unknown type: " + key);
        } else if (type.equals("integer")) {
            return value;
        } else if (type.equals("boolean")) {
            return value.equalsIgnoreCase("YES") + "";
        } else if (type.equals("number")) {
            return value;
        } else {
            return quoted(value);
        }
    }

    private static String escape(String raw) {
        // should in theory escape other non-printing characters using uXXXX notation
        // but those characters not expected
        return raw.replace("\\", "\\\\") //
                .replace("\"", "\\\"") //
                .replace("\b", "\\b") //
                .replace("\f", "\\f") //
                .replace("\n", "\\n") //
                .replace("\r", "\\r") //
                .replace("\t", "\\t");
    }

}