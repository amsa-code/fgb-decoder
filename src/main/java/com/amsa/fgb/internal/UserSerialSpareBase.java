package com.amsa.fgb.internal;

import java.util.List;

abstract class UserSerialSpareBase extends UserSerial {

    UserSerialSpareBase(String serialBeaconType, String serialCode) {
        super(serialBeaconType, serialCode);
    }

    @Override
    List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = UserSerial.userSerialFragment1(this, hexStr, binCode);

        if (hexStr.length() > 15) {
            result.add(this.bch1(binCode, 86, 106));

            if (this.isLongMessage(binCode)) {
                result.add(this.nationalUse(binCode, 107, 144));
            } else {
                result.add(this.nationalUse(binCode, 107, 112));
            }
        }

        return result;
    }

    @Override
    boolean canDecode(String binCode) {
        if (super.canDecode(binCode)) {
            String serCode = binCode.substring(40, 43);

            // Note: SubClass Constructor sets the serialCode in
            // Only 111, 101 (which are the user serial spare codes) >= 0
            return serCode.compareTo(this.serialCode()) >= 0;
        }
        return false;
    }
}