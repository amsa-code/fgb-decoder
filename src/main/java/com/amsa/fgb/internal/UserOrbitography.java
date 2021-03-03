package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

// 13 May 2005
// Changed based on C/ST.006 and CDP
class UserOrbitography extends User {

    UserOrbitography() {
        protocolName = "Orbitography";
        userProtocolCode = "000";
    }

    @Override
    public List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);

        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.messageType(binCode, 25, 26));
        result.add(this.hexData(hexStr, 25, (binCode.length() - 1)));
        result.add(this.hexId(binCode, 26, 85));
        result.add(this.countryCode(binCode, 27, 36));
        result.add(this.protocolType(binCode, 37, 39));

        result = this.orbitographyData(result, binCode, 40, 85);

        // The if condition is always true because it is prefixed to be 30 chars already
        if (hexStr.length() > 15) {
            result.add(this.bch1(binCode, 86, 106));

            if (this.isLongMessage(binCode)) {
                result.add(this.nationalUse(binCode, 107, 144));
            }
            // 07/04/2005
            else {
                if (binCode.charAt(107) == '0')
                    result.add(this.nationalUse(binCode, 107, 112));
                else {
                    result.add(new HexAttribute("Emergency Code Flag", 107,
                            "Non-Spec (" + binCode.charAt(107) + ")", ""));
                    result.add(this.nationalUse(binCode, 108, 112));
                }
            }

        }

        return result;
    }

    // This is where changes go
    private List<HexAttribute> orbitographyData(List<HexAttribute> result, String binCode, int s, int f) {
        String v1 = binCode.substring(s, f + 1);
        String e1 = "";

        result.add(new HexAttribute("Orbitography Data", s, f, v1, e1));

        // bit 40-81
        String vE[] = Conversions.mBaudotBits2mBaudotStr(this.getName(),
                binCode.substring(s, f - 4 + 1), 6);
        // 24 June 2005, double quote is not used any more
        // String v2 = "\"" + vE[0] + "\"";
        String v2 = vE[0];
        String e2 = vE[1];

        if (e2 != null && e2.length() > 0)
            e2 = "\nWARNING - SUSPECT NON-SPEC IN ORBITOGRAPHY BEACON CLEAR TEXT IDENTIFIER\n" + e2;

        result.add(new HexAttribute("Orbitography ID", s, f - 4, v2, e2));

        // Bit 82-85
        String v3 = binCode.substring(f - 4 + 1, f + 1);

        if (v3.equals("0000"))
            result.add(new HexAttribute("Orbitography 4 bits", f - 4 + 1, f, v3, ""));
        else
            result.add(
                    new HexAttribute("Orbitography 4 bits", f - 4 + 1, f, v3 + " (Non-Spec)", ""));

        return result;
    }

}
