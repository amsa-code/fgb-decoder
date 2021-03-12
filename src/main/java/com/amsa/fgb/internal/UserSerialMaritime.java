package com.amsa.fgb.internal;

abstract class UserSerialMaritime extends UserSerialAviationOrPersonalOrMaritimeBase {

    UserSerialMaritime(String serialBeaconType, String serialCode) {
        super(serialBeaconType, serialCode);
    }

}
