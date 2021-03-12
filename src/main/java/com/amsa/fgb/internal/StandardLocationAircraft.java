package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

abstract class StandardLocationAircraft extends StandardLocation {

    private final Consumer consumer;
    
    protected StandardLocationAircraft(String stdProtocolCode, String protocolName, Consumer consumer) {
        this.stdProtocolCode = stdProtocolCode;
        this.protocolName = protocolName;
        this.consumer = consumer;
    }
    
    interface Consumer {
        void accept(StandardLocationAircraft s, String binCode, List<HexAttribute> list);
    }

    @Override
     List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.messageType(binCode, 25, 26));
        result.add(this.hexData(hexStr, 25, binCode.length() - 1));
        HexAttribute hexId = this.hexIdWithDefaultLocation(binCode, 26, 65);
        result.add(hexId);
        // result.add(this.hexId(binCode, 26, 65));
        result.add(this.countryCode(binCode, 27, 36));
        result.add(this.protocolType(binCode, 37, 40));

        consumer.accept(this, binCode, result);

        result.addAll(this.coarsePositions(binCode, 65, 85));

        if (hexStr.length() > 15) {
            result = this.bch1(result, binCode, hexId);
            // result.add(this.bch1(binCode, 86, 106));

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

}