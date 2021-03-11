package com.amsa.fgb.internal;

import java.util.List;

import com.github.davidmoten.guavamini.Preconditions;

public final class DecodeAsJson implements DecodeFilter {

    public static final DecodeFilter INSTANCE = new DecodeAsJson();
    private static final String COLON = ":";

    private DecodeAsJson() {
        // prevent instantiation externally
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
            } else if (!h.getDesc().isEmpty()
                    && !h.getDesc().equals(AttributeType.SPARE.toString())) {
                addKeyValue(b, h.getJsonType(), h.getDesc(), h.getValue());
            }
        }
        b.insert(0, "{");
        b.append("}");
        return b.toString();
    }

    private void addKeyValue(StringBuilder b, JsonType jsonType, String key, String value) {
        if (b.length() > 0) {
            b.append(",\n");
        }
        b.append(quoted(key));
        b.append(COLON);
        b.append(getValue(jsonType, key, value));
    }

    private String getValue(JsonType jsonType, String key, String value) {
        Preconditions.checkNotNull(jsonType);
        if (jsonType == JsonType.INTEGER) {
            return Integer.parseInt(value) + "";
        } else if (jsonType == JsonType.BOOLEAN) {
            return value.equalsIgnoreCase("YES") + "";
        } else if (jsonType == JsonType.NUMBER) {
            return Double.parseDouble(value) + "";
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