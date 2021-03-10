package com.amsa.fgb.internal;

import java.util.List;

class DecodeAsXML2 implements DecodeFilter {

    static final DecodeAsXML2 INSTANCE = new DecodeAsXML2();

    private DecodeAsXML2() {
        // force use of singleton
    }

    @Override
    public String getData(List<HexAttribute> v) {
        String data = "";

        for (int i = 0; i < v.size(); i++) {

            HexAttribute h = v.get(i);

            String line = "";
            String desc = h.getDesc();
            String pos = h.getPos();
            String val = h.getValue();
            int len = desc.length();
            if (len > 0) {
                line = "<Line>\n";
                line = line + "<Name>" + desc + "</Name>";
                line = line + "<Position>" + pos + "</Position>";
                line = line + "<Value>" + val + "</Value>";
                line = line + "</Line>\n";
            }
            data = data + line;
        }
        /* Now put the errors on */

        for (int i = 0; i < v.size(); i++) {

            HexAttribute h = v.get(i);
            String err = h.getError();

            String line = "";
            if (err.length() > 0) {
                line = "<Line>\n";
                // line = line + "<Name>" + err + "</Name>";

                // 25 May 2005
                // Cameron's Request to provide correct format:
                // <Name>WARNING</NAME>
                // <Position></Position>
                // <Value>the rest err text</Value>

                int warningIdx = err.indexOf("WARNING");
                if (warningIdx >= 0) {
                    line += "<Name>" + err.substring(0, warningIdx + 7) + "</Name>";
                    line += "<Position></Position>";
                    line += "<Value>" + err.substring(warningIdx + 7) + "</Value></Line>\n";
                } else {
                    line = line + "<Error>" + err + "</Error>\n";
                }
            }
            data = data + line;
        }

        return data;
    }

}
