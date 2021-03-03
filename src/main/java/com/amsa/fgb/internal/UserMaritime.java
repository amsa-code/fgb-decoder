package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

class UserMaritime extends User {

    UserMaritime() {
        protocolName = "Maritime";
        userProtocolCode = "010";
    }

    @Override
    public List<HexAttribute> decodeSearch(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.hexId(binCode, 26, 85));
        // result.add(this.getMMSI(binCode, 40, 81));

        result.add(this.getMMSI(binCode, 40, 75));

        return result;
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

        result.add(this.mmsi(binCode, 40, 75));
        result.add(this.specificBeaconNumber(binCode, 76, 81));

        result.add(this.spare(binCode, 82, 83));
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
                        result.add(this.latitude(binCode, 108, 119));
                        result.add(this.longitude(binCode, 120, 132));
                        result.add(this.bch2(binCode, 133, 144));
                    }
                    result.add(this.nationalUse(binCode, 113, 144));
                }
            }
            // 14/03/2005
            else {
                // result = Common.CommonInUserProtocols(result, binCode, 107, 112);
                result = this.nonNationalUse(result, binCode);
            }
        }

        return result;
    }

    private HexAttribute getMMSI(String binCode, int s, int f) {
        String d = "";
        String v = "";
        String e = "";

        // 11 May 2005
        String vE[] = Conversions.mBaudotBits2mBaudotStr(this.getName(),
                binCode.substring(s, f + 1), 6);

        String code = vE[0];
        e = vE[1];

        if (Conversions.isNumeric(code)) {
            d = "Ships MMSI";
            int countryCode = this.getCountryCode(binCode, 27, 36);
            v = countryCode + code;
        } else {
            d = "Radio Callsign";
            // 24 June 2005
            // Don't use double quote
            // v = "\"" + code + "\"" ;
            v = code;
        }

        return new HexAttribute(d, s, f, v, e);
    }

    private HexAttribute mmsi(String binCode, int s, int f) {
        String d = "";
        String v = "";
        String e = "";

        // 11 May 2005
        String vE[] = Conversions.mBaudotBits2mBaudotStr(this.getName(),
                binCode.substring(s, f + 1), 6);

        String code = vE[0];
        e = vE[1];

        if (Conversions.isNumeric(code)) {
            d = "Ships MMSI";
            v = code + " (LAST 6 DIGITS OF MMSI)";
            // v = this.countryCode + code;
        } else {
            d = "Radio Callsign";

            // 24 June 2005, Double quote is not used any more
            // v = "\"" + code + "\"" ;
            v = code;
        }

        if (e != null && e.length() > 0)
            e = "\nWARNING - SUSPECT NON-SPEC IN " + d.toUpperCase() + "\n" + e;

        return new HexAttribute(d, s, f, v, e);
    }

    // This overidding method will be called by User.java
    @Override
    List<HexAttribute> allEmergencyCodes(List<HexAttribute> result, String binCode) {
        result = Common.maritimeEmergencyCodes(result, binCode);

        return result;
    }
}
