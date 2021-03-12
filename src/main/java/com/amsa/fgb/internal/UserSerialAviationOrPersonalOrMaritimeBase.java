package com.amsa.fgb.internal;

import java.util.List;

abstract class UserSerialAviationOrPersonalOrMaritimeBase extends UserSerial {

    UserSerialAviationOrPersonalOrMaritimeBase(String serialBeaconType, String serialCode) {
        super(serialBeaconType, serialCode);
    }

    @Override
    List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);

        List<HexAttribute> result = userSerialFragment1(this, hexStr, binCode);

        return userSerialFragment2(this, hexStr, binCode, result);
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
