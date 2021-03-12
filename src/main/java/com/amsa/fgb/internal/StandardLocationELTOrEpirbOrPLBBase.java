package com.amsa.fgb.internal;

abstract class StandardLocationELTOrEpirbOrPLBBase extends StandardLocationBase {

    StandardLocationELTOrEpirbOrPLBBase(String stdProtocolCode, String protocolName) {
        super(stdProtocolCode, protocolName, (s, binCode, result) -> {
            result.add(s.cospatSarsatTypeApp(binCode, 41, 50));
            result.add(s.beaconSerialNumber(binCode, 51, 64));
        });
    }

}
