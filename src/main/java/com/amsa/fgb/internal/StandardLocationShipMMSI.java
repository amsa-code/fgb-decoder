package com.amsa.fgb.internal;

class StandardLocationShipMMSI extends StandardLocationBase {

    StandardLocationShipMMSI() {
        super("0010", "Maritime MMSI", (s, binCode, result) -> {
            result.add(s.mmsi(AttributeType.SHIP_MMSI, binCode, 41, 60));
            result.add(s.specificBeaconNumber(binCode, 61, 64));
        });
    }

}
