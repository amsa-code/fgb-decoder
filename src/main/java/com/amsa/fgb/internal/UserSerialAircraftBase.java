package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

abstract class UserSerialAircraftBase extends UserSerial {

    private final Consumer consumer;

    UserSerialAircraftBase(String serialBeaconType, String serialCode, Consumer consumer) {
        this.serialBeaconType = serialBeaconType;
        this.serialCode = serialCode;
        this.consumer = consumer;
    }

    @Override
     List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);

        List<HexAttribute> result = new ArrayList<HexAttribute>();

        userFragment(this, hexStr, binCode, result);

        result.add(this.beaconType(binCode, 40, 42));
        result.add(this.cospasSarsatAppCertFlag(binCode, 43));
        
        consumer.accept(this, binCode, result);

        if (this.cospasSarsatAppCertFlagPresent(binCode)) {
            result.add(this.cospasSarsatAppCertNumber(binCode, 74, 83));
        } else {
            result.add(this.nationalUse(binCode, 74, 83));
        }

        result.add(this.auxRadioLocating(binCode, 84, 85));

        if (this.isUS) {
            result.add(this.usManufacturerId(binCode, 44, 51));
            result.add(this.usSeqNo(binCode, 52, 63));
            result.add(this.usModelId(binCode, 64, 67));
            result.add(this.usRunNo(binCode, 68, 75));
            result.add(this.usNatUse(binCode, 76, 83));
        }

        return userSerialFragment2(this, hexStr, binCode, result);
    }

    // This overidding method will be called by User.java
    @Override
    List<HexAttribute> allEmergencyCodes(List<HexAttribute> result, String binCode) {
        result = Common.nonMaritimeEmergencyCodes(result, binCode);
        return result;
    }
    
    interface Consumer {
        void accept(UserSerialAircraftBase u, String binCode, List<HexAttribute> result);
    }

}
