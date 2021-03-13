package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

abstract class StandardLocationBase extends StandardLocation {

    private final Consumer consumer;

    StandardLocationBase(String stdProtocolCode, String protocolName, Consumer consumer) {
        super(stdProtocolCode, protocolName);
        this.consumer = consumer;
    }

    @Override
    List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<>();
        result.add(this.messageType(binCode, 25, 26));
        result.add(this.hexData(hexStr, 25, binCode.length() - 1));
        HexAttribute hexId = this.hexIdWithDefaultLocation(binCode, 26, 65);
        result.add(hexId);
        result.add(this.countryCode(binCode, 27, 36));
        result.add(this.protocolType(binCode, 37, 40));

        consumer.accept(this, binCode, result);

        result.addAll(this.coarsePositions(binCode, 65, 85));

        if (hexStr.length() > 15) {
            result = this.bch1(result, binCode, hexId);

            result.add(this.fixedBits(binCode, 107, 110));

            if (this.isLongMessage(binCode)) {
                result.add(this.encodedPositionSource(binCode, 111));
                result.add(this.homing(binCode, 112));
                if (this.defaultFFFFFFFF(hexStr)) {
                    result.add(this.longMessage(binCode, 113, 144));
                } else if (this.default00000000(hexStr)) {
                    result.add(this.longMessage(binCode, 113, 144));
                } else {
                    result.addAll(offsetPosition(binCode, 113, 132));
                    result.add(this.bch2(binCode, 133, 144));
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

    protected HexAttribute mmsi(AttributeType type, String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";
        String value = v + "";
        int len = value.length();
        for (int i = 0; i < (6 - len); i++) {
            value = "0" + value;
        }
        return new HexAttribute(type, s, f, value, e);
    }

    @Override
    HexAttribute specificBeaconNumber(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute(AttributeType.SPECIFIC_BEACON_NUMBER, s, f, v, e);
    }

    interface Consumer {
        void accept(StandardLocationBase s, String binCode, List<HexAttribute> result);
    }

}
