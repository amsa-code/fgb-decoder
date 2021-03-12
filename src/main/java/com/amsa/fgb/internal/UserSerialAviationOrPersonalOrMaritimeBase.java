package com.amsa.fgb.internal;

import java.util.List;

abstract class UserSerialAviationOrPersonalOrMaritimeBase extends UserSerial {

    UserSerialAviationOrPersonalOrMaritimeBase(String serialBeaconType, String serialCode) {
        this.serialBeaconType = serialBeaconType;
        this.serialCode = serialCode;
    }

    @Override
    List<HexAttribute> decode(String hexStr) {

        String binCode = Conversions.hexToBinary(hexStr);

        List<HexAttribute> result = UserSerial.userSerialFragment1(this, hexStr, binCode);

        if (hexStr.length() > 15) {
            result.add(this.bch1(binCode, 86, 106));
            if (this.isLongMessage(binCode)) {
                result.add(this.encodedPositionSource(binCode, 107));
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
                }
            }
            // 14/03/2005
            else {
                result = this.nonNationalUse(result, binCode);
            }
        }

        return result;
    }

    // This overidding method will be called by User.java
    @Override
    List<HexAttribute> allEmergencyCodes(List<HexAttribute> result, String binCode) {
        if (this instanceof UserSerialMaritime) {
            result = Common.maritimeEmergencyCodes(result, binCode);
        } else {
            result = Common.nonMaritimeEmergencyCodes(result, binCode);
        }
        return result;
    }

}
