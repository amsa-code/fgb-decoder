package com.amsa.fgb.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import com.github.davidmoten.guavamini.annotations.VisibleForTesting;

public final class DecodeAsJson implements DecodeFilter {

    public static final DecodeFilter INSTANCE = new DecodeAsJson();
    private static final String COLON = ":";

    private final Map<String, String> attributeTypes;

    private DecodeAsJson() {
        // prevent instantiation externally
        attributeTypes = loadAttributeTypes();
    }

    private static Map<String, String> loadAttributeTypes() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                DecodeAsJson.class.getResourceAsStream("/attribute-types.txt"), StandardCharsets.UTF_8))) {
            return br //
                    .lines() //
                    .map(x -> x.trim()) //
                    .filter(x -> !x.startsWith("#")) //
                    .filter(x -> !x.isEmpty()) //
                    .map(x -> x.split("\t")) //
                    .peek(x -> {
                        if (x.length != 2) {
                            throw new RuntimeException("wrong item count: " + Arrays.toString(x));
                        }
                    }) //
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
                throw new RuntimeException("Error occurred at position " + h.getPos() + " with desc='" + h.desc
                        + "', value='" + h.value + "':" + h.error);
            } else if (!h.getDesc().isEmpty() && !h.getDesc().equals(AttributeType.SPARE.toString())) {
                if (h.getDesc().equals(AttributeType.FINE_POSITION.toString())) {
                    // TODO unit test
                    if (!h.getValue().equals("DEFAULT")) {
                        addKeyValue(b, "Fine Position Latitude", "" + Util.toLatitude(h.getValue().substring(0, 9)));
                        addKeyValue(b, "Fine Position Longitude", "" + Util.toLongitude(h.getValue().substring(10)));
                    }
                } else if (h.getDesc().equals(AttributeType.OFFSET_POSITION.toString())) {
                    // TODO unit test
                    if (!h.getValue().equals("DEFAULT")) {
                        Offset offset = new Offset(h.getValue());
                        addKeyValue(b, "Offset Position Latitude Diff", "" + offset.latDiff);
                        addKeyValue(b, "Offset Position Longitude Diff", "" + offset.lonDiff);
                    }
                } else {
                    addKeyValue(b, h.getDesc(), h.getValue());
                }
            }
        }
        b.insert(0, "{");
        b.append("}");
        return b.toString();
    }

    @VisibleForTesting
    static final class Offset {
        final double latDiff;
        final double lonDiff;

        // TODO unit test
        Offset(String value) {
            // +15 20 -22 24
            // +0 20 -0 24
            StringTokenizer t = new StringTokenizer(value);
            {
                String a = t.nextToken();
                int signLat = a.charAt(0) == '+' ? 1 : -1;
                int latDegs = Integer.parseInt(a.substring(1));
                int latMins = Integer.parseInt(t.nextToken());
                this.latDiff = signLat * (latDegs + latMins / 60.0);
            }
            {
                String a = t.nextToken();
                int signLon = a.charAt(0) == '+' ? 1 : -1;
                int lonDegs = Integer.parseInt(a.substring(1));
                int lonMins = Integer.parseInt(t.nextToken());
                this.lonDiff = signLon * (lonDegs + lonMins / 60.0);
            }
        }
    }

    private void addKeyValue(StringBuilder b, String key, String value) {
        if (b.length() > 0) {
            b.append(",\n");
        }
        b.append(quoted(key));
        b.append(COLON);
        b.append(getValue(key, value));
    }

    private String getValue(String key, String value) {
        if (key.equals("Latitude")) {
            return "" + Util.toLatitude(value);
        } else if (key.equals("Longitude")) {
            return "" + Util.toLongitude(value);
        } else {
            String type = attributeTypes.get(key);
            if (type == null) {
                throw new RuntimeException("unknown type: " + key);
            } else if (type.equals("integer")) {
                return Integer.parseInt(value) + "";
            } else if (type.equals("boolean")) {
                return value.equalsIgnoreCase("YES") + "";
            } else if (type.equals("number")) {
                return Double.parseDouble(value) + "";
            } else {
                return quoted(value);
            }
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