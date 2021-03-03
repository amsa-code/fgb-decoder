package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

class StandardLocationAircraftAddress extends StandardLocation {

    StandardLocationAircraftAddress() {
        stdProtocolCode = "0011";
        protocolName = "Aircraft Address";
    }

    @Override
     List<HexAttribute> decodeSearch(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        // result.add(this.hexId(binCode, 26, 65));
        result.add(this.hexId(binCode, 26, 85));

        HexAttribute h = this.aircraftCallSign(binCode, 41, 64);
        String value = h.getValue();
        if (value.length() > 0) {
            result.add(h);
        }

        result.add(this.aircraft24BitAddressBinary(binCode, 41, 64));

        return result;
    }

    @Override
     List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.messageType(binCode, 25, 26));
        result.add(this.hexData(hexStr, 25, (binCode.length() - 1)));
        HexAttribute hexId = this.hexIdWithDefaultLocation(binCode, 26, 65);
        result.add(hexId);
        // result.add(this.hexId(binCode, 26, 65));
        result.add(this.countryCode(binCode, 27, 36));
        result.add(this.protocolType(binCode, 37, 40));

        result.add(this.aircraft24BitAddress(binCode, 41, 64));

        result.add(this.coarsePosition(binCode, 65, 85));

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

}
