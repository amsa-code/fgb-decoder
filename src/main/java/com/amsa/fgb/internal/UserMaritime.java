package com.amsa.fgb.internal;

import java.util.Locale;

final class UserMaritime extends UserAviationOrMaritimeOrRadioCallsign {

    UserMaritime() {
        super("Maritime", "010", (u, binCode, result) -> {
            result.add(((UserMaritime) u).mmsi(binCode, 40, 75));
            result.add(u.specificBeaconNumber(binCode, 76, 81));
            result.add(u.spare(binCode, 82, 83));
        });
    }

    private HexAttribute mmsi(String binCode, int s, int f) {
        final AttributeType d;
        String v = "";
        String e = "";

        // 11 May 2005
        String[] vE = Conversions.mBaudotBits2mBaudotStr(this.getName(),
                binCode.substring(s, f + 1), 6);

        String code = vE[0];
        e = vE[1];

        if (Conversions.isNumeric(code)) {
            d = AttributeType.SHIP_MMSI_LAST_6_DIGITS;
            v = code;
            // v = this.countryCode + code;
        } else {
            d = AttributeType.RADIO_CALLSIGN;

            // 24 June 2005, Double quote is not used any more
            // v = "\"" + code + "\"" ;
            v = code.trim();
        }

        if (e != null && e.length() > 0) {
            e = "\nWARNING - SUSPECT NON-SPEC IN " + d.toString().toUpperCase(Locale.ENGLISH) + "\n"
                    + e;
        }

        return new HexAttribute(d, s, f, v, e);
    }

}
