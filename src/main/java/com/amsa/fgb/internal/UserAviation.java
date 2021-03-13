package com.amsa.fgb.internal;

import java.util.List;

class UserAviation extends UserAviationOrMaritimeOrRadioCallsign {

    UserAviation() {
        super("Aviation", "001", (u, binCode, result) -> {
            result.add(((UserAviation) u).aircraftRegistrationMarking(binCode, 40, 81));
            result.add(u.specificELTIdentifier(binCode, 82));
        });
    }

    private HexAttribute aircraftRegistrationMarking(String binCode, int s, int f) {
        // 11 May 2005
        String[] vE = Conversions.mBaudotBits2mBaudotStr(this.getName(),
                binCode.substring(s, f + 1), 6);
        // 24 June 2005
        // Don't use double quote again on CDP's agreement
        // String v = "\"" + vE[0] + "\"";
        String v = vE[0];

        // String e = "";
        String e = vE[1];

        if (e != null && e.length() > 0) {
            e = "\nWARNING - SUSPECT NON-SPEC IN AIRCRAFT REG. MARKING\n" + e;
        }

        return new HexAttribute(AttributeType.AIRCRAFT_REG_MARKING, s, f, v, e);
    }

    // This overidding method will be called by User.java
    @Override
    List<HexAttribute> allEmergencyCodes(List<HexAttribute> result, String binCode) {
        result = Common.nonMaritimeEmergencyCodes(result, binCode);

        return result;
    }
}
