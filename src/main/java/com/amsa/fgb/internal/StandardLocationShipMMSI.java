package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

class StandardLocationShipMMSI extends StandardLocation {

    StandardLocationShipMMSI() {
        stdProtocolCode = "0010";
        protocolName = "Maritime MMSI";
    }

    @Override
     List<HexAttribute> decodePartial(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        // result.add(this.hexId(binCode, 26, 65));
        result.add(this.hexId(binCode, 26, 85));
        result.add(this.getMMSI(binCode, 41, 60));

        return result;
    }

    @Override
     List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<>();

        result.add(this.messageType(binCode, 25, 26));
        result.add(this.hexData(hexStr, 25, (binCode.length() - 1)));
        // result.add(this.hexId(binCode, 26, 65));
        HexAttribute hexId = this.hexIdWithDefaultLocation(binCode, 26, 65);
        result.add(hexId);

        result.add(this.countryCode(binCode, 27, 36));
        result.add(this.protocolType(binCode, 37, 40));

        result.add(this.mmsi(binCode, 41, 60));
        result.add(this.specificBeaconNumber(binCode, 61, 64));

        result.add(this.coarsePosition(binCode, 65, 85));

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
                        result.add(offsetPosition(binCode, 113, 132));
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

    private HexAttribute getMMSI(String binCode, int s, int f) {
        int countryCode = this.getCountryCode(binCode, 27, 36);
        HexAttribute h = this.mmsi(binCode, s, f);
        String mmsi = h.getValue();
        String v = countryCode + mmsi;
        String e = h.getError();

        return new HexAttribute(AttributeType.SHIP_MMSI, v, e);
    }

    private HexAttribute mmsi(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";
        String value = v + "";
        int len = value.length();
        for (int i = 0; i < (6 - len); i++) {
            value = "0" + value;
        }

        return new HexAttribute(AttributeType.SHIP_MMSI, s, f, value, e);
    }

    @Override
    HexAttribute specificBeaconNumber(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute(AttributeType.SPECIFIC_BEACON_NUMBER, s, f, v, e);
    }

}
