package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

class StandardLocationShipSecurityAlertSystem extends StandardLocation {

    StandardLocationShipSecurityAlertSystem() {
        stdProtocolCode = "1100";
        protocolName = "Ship Security MMSI";
    }

    @Override
     List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.messageType(binCode, 25, 26));
        result.add(this.hexData(hexStr, 25, (binCode.length() - 1)));
        // result.add(this.hexId(binCode, 26, 65));
        HexAttribute hexId = this.hexIdWithDefaultLocation(binCode, 26, 65);
        result.add(hexId);

        result.add(this.countryCode(binCode, 27, 36));
        result.add(this.protocolType(binCode, 37, 40));

        result.add(this.mmsi(binCode, 41, 60));
        result.add(this.securityFixedBits(binCode, 61, 64));

        result.addAll(this.coarsePositions(binCode, 65, 85));

        if (hexStr.length() > 15) {
            // result.add(this.bch1(binCode, 86, 106));
            result = this.bch1(result, binCode, hexId);

            result.add(this.fixedBits(binCode, 107, 110));

            if (this.isLongMessage(binCode)) {
                result.add(this.encodedPositionSource(binCode, 111));
                result.add(this.homing(binCode, 112));
                if (this.defaultFFFFFFFF(hexStr)) {
                    result.add(this.longMessage(binCode, 113, 144));
                } else {
                    if (this.default00000000(hexStr)) {
                        result.add(this.longMessage(binCode, 113, 144));
                    } else {
                        result.addAll(offsetPosition(binCode, 113, 132));
                        result.add(this.bch2(binCode, 133, 144));
                    }
                }
            } else {
                result.add(this.encodedPositionSource(binCode, 111));
                result.add(this.homing(binCode, 112));
            }
            if (this.actualLatLong) {
                result.add(actualLatitude());
                result.add(actualLongitude());
            }
        }

        return result;
    }

    private HexAttribute mmsi(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";
        String value = v + "";
        int len = value.length();
        for (int i = 0; i < (6 - len); i++) {
            value = "0" + value;
        }

        return new HexAttribute(AttributeType.SHIP_SECURITY_MMSI, s, f, value, e);
    }

    private HexAttribute securityFixedBits(String binCode, int s, int f) {
        String v = binCode.substring(s, f + 1);
        String e = "";

        // 08 Aug 2006
        if (!v.equals("0000"))
            v += " (Non-Spec)";

        return new HexAttribute(AttributeType.FIXED_BITS, s, f, v, e);
    }
}
