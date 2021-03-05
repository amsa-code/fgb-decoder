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
    }

    private static Map<String, String> loadAttributeTypes() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                DecodeAsJson.class.getResourceAsStream("/attribute-types.txt"),
                StandardCharsets.UTF_8))) {
            return br //
                    .lines() //
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
            } else if (!h.getDesc().isEmpty() && !h.getDesc().equals("Spare")) {
                if (h.getDesc().equals("Coarse Position")) {
                    if (!h.getValue().equals("DEFAULT")) {
                        addKeyValue(b, "Coarse Position Latitude",
                                "" + getLatitudeFromCoarsePosition(h.getValue()));
                        addKeyValue(b, "Coarse Position Longitude",
                                "" + getLongitudeFromCoarsePosition(h.getValue()));
                    }
                } else if (h.getDesc().equals("Fine Position")) {
                    //TODO unit test
                    if (!h.getValue().equals("DEFAULT")) {
                        addKeyValue(b, "Fine Position Latitude",
                                "" + toLatitude(h.getValue().substring(0, 9)));
                        addKeyValue(b, "Fine Position Longitude",
                                "" + getLongitudeFromCoarsePosition(h.getValue().substring(10)));
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

    private void addKeyValue(StringBuilder b, String key, String value) {
        if (b.length() > 0) {
            b.append(",\n");
        }
        b.append(quoted(key));
        b.append(COLON);
        b.append(getValue(key, value));
    }

    // TODO unit test
    private static double getLatitudeFromCoarsePosition(String value) {
        // 35 44S 115 30E
        int d = Integer.parseInt(value.substring(0, 2));
        int m = Integer.parseInt(value.substring(3, 5));
        int sign = value.charAt(6) == 'N' ? 1 : -1;
        return sign * (d + m / 60.0);
    }

    // TODO unit test
    private static double getLongitudeFromCoarsePosition(String value) {
        // 35 44S 115 30E
        int d = Integer.parseInt(value.substring(7, 10));
        int m = Integer.parseInt(value.substring(11, 13));
        int sign = value.charAt(13) == 'E' ? 1 : -1;
        return sign * (d + m / 60.0);
    }

    private String getValue(String key, String value) {
        if (key.equals("Latitude")) {
            return "" + toLatitude(value);
        } else if (key.equals("Longitude")) {
            return "" + toLongitude(value);
        } else {
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
    }

    // TODO unit test
    private static double toLatitude(String value) {
        int d = Integer.parseInt(value.substring(0, 2));
        int m = Integer.parseInt(value.substring(3, 5));
        int s = Integer.parseInt(value.substring(6, 8));
        boolean positive = value.charAt(8) == 'N';
        int sign = positive ? 1 : -1;
        return sign * (d + m / 60.0 + s / 3600.0);
    }

    // TODO unit test
    private static double toLongitude(String value) {
        int d = Integer.parseInt(value.substring(0, 3));
        int m = Integer.parseInt(value.substring(4, 6));
        int s = Integer.parseInt(value.substring(7, 9));
        boolean positive = value.charAt(9) == 'N';
        int sign = positive ? 1 : -1;
        return sign * (d + m / 60.0 + s / 3600.0);
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