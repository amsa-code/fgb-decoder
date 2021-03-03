package com.amsa.fgb.internal;

import java.util.List;

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
            }
            if (!h.getDesc().isEmpty()) {
                if (!b.isEmpty()) {
                    b.append(",\n");
                }
                b.append(quoted(h.getDesc()));
                b.append(COLON);
                b.append(quoted(h.getValue()));
            }
        }
        b.insert(0, "{");
        b.append("}");
        return b.toString();
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