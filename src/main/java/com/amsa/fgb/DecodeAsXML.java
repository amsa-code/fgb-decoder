package com.amsa.fgb;

import java.util.List;

public class DecodeAsXML implements DecodeFilter {

    public static final DecodeAsXML INSTANCE = new DecodeAsXML();

    private DecodeAsXML() {
        // force use of singletons
    }

    /*
     * public String getData(List v) { String data = "<?xml version=\"1.0\"?>\n";
     * 
     * List reservedChars = new ArrayList(0); data = data + "<Hexdecode>\n"; for
     * (int i=0; i<v.size(); i++) { HexAttribute h = (HexAttribute) v.elementAt(i);
     * String line = ""; String desc = h.getDesc(); String pos = h.getPos(); String
     * val = h.getValue(); String err = h.getError(); int len = desc.length(); if
     * (len > 0) { String tag = ""; for (int j=0; j<len; j++) { char c =
     * desc.charAt(j); if (c != ' ' && c!= '.') { tag = tag + c; } } line = "<" +
     * tag + ">"; if (pos.equals("b0")) { pos = ""; } line = line + "<Position>" +
     * pos + "</Position>"; line = line + "<Value>" + val + "</Value>"; if
     * (err.length() > 0) { line = line + "<Error>" + err + "</Error>"; } line =
     * line + "</" + tag + ">\n"; } data = data + line; }
     * 
     * data = data + "</Hexdecode>\n";
     * 
     * return data; }
     */

    @Override
    public String getData(List<HexAttribute> v) {
        String data = "<?xml version=\"1.0\"?>\n";

        data = data + "<Hexdecode>\n";

        for (int i = 0; i < v.size(); i++) {
            HexAttribute h = v.get(i);

            String line = "";
            String desc = h.getDesc();
            String pos = h.getPos();
            String val = h.getValue();
            String err = h.getError();
            int len = desc.length();

            if (len > 0) {
                String tag = "";
                for (int j = 0; j < len; j++) {
                    char c = desc.charAt(j);
                    // Get rid of "/" in the string
                    if (c != ' ' && c != '.' && c != '/') {
                        tag = tag + c;
                    }
                }

                line = "<" + tag + ">";
                if (pos.equals("b0")) {
                    pos = "";
                }

                line = line + "<Position>" + pos + "</Position>";
                line = line + "<Value>" + val + "</Value>";
                if (err.length() > 0) {
                    line = line + "<Error>" + err + "</Error>";
                }
                line = line + "</" + tag + ">\n";
            } else if (err.length() > 0) {
                line = line + "<Error>" + err + "</Error>\n";
            }
            data = data + line;
        }

        data = data + "</Hexdecode>\n";

        return data;
    }

}
