package com.amsa.fgb.internal;

final class StandardLocationAircraftOperator extends StandardLocationBase {

    StandardLocationAircraftOperator() {
        super("0101", "Aircraft Operator", (s, binCode, result) -> {
            result.add(s.aircraftOperator(binCode, 41, 55));
            result.add(s.aircraftSerialNumber(binCode, 56, 64));
        });
    }
}
