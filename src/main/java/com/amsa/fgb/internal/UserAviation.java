package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

class UserAviation extends User {

    UserAviation() {
        protocolName = "Aviation";
        userProtocolCode = "001";
    }

    @Override
    List<HexAttribute> decodePartial(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.hexId(binCode, 26, 85));
        result.add(this.aircraftRegistrationMarking(binCode, 40, 81));

        return result;
    }

    @Override
    List<HexAttribute> decode(String hexStr) {

        String binCode = Conversions.hexToBinary(hexStr);

        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.messageType(binCode, 25, 26));
        result.add(this.hexData(hexStr, 25, (binCode.length() - 1)));
        result.add(this.hexId(binCode, 26, 85));
        result.add(this.countryCode(binCode, 27, 36));
        result.add(this.protocolType(binCode, 37, 39));

        result.add(this.aircraftRegistrationMarking(binCode, 40, 81));
        result.add(this.specificELTIdentifier(binCode, 82));
        result.add(this.auxRadioLocating(binCode, 84, 85));

        if (hexStr.length() > 15) {
            result.add(this.bch1(binCode, 86, 106));
            if (this.isLongMessage(binCode)) {
                result.add(this.encodedPositionSource(binCode, 111));
                if (this.defaultFFFFFFFF(hexStr)) {
                    result.add(this.longMessage(binCode, 113, 144));
                } else {
                    if (this.default00000000(hexStr)) {
                        result.add(this.longMessage(binCode, 113, 144));
                    } else {
                        List<HexAttribute> res = result;
                        this.latitude(binCode, 108, 119).ifPresent(x -> res.add(x));
                        this.longitude(binCode, 120, 132).ifPresent(x -> res.add(x));
                        result.add(this.bch2(binCode, 133, 144));
                    }
                    result.add(this.nationalUse(binCode, 113, 144));
                }
            }
            // 14/03/2005
            else {
                result = this.nonNationalUse(result, binCode);
            }
        }

        return result;
    }

    private HexAttribute aircraftRegistrationMarking(String binCode, int s, int f) {
        // 11 May 2005
        String vE[] = Conversions.mBaudotBits2mBaudotStr(this.getName(), binCode.substring(s, f + 1), 6);
        // 24 June 2005
        // Don't use double quote again on CDP's agreement
        // String v = "\"" + vE[0] + "\"";
        String v = vE[0];

        // String e = "";
        String e = vE[1];

        if (e != null && e.length() > 0)
            e = "\nWARNING - SUSPECT NON-SPEC IN AIRCRAFT REG. MARKING\n" + e;

        return new HexAttribute(AttributeType.AIRCRAFT_REG_MARKING, s, f, v, e);
    }

    // This overidding method will be called by User.java
    @Override
    List<HexAttribute> allEmergencyCodes(List<HexAttribute> result, String binCode) {
        result = Common.nonMaritimeEmergencyCodes(result, binCode);

        return result;
    }
}
