package com.amsa.fgb.internal;

final class StandardLocationAircraftAddress extends StandardLocationBase {

    StandardLocationAircraftAddress() {
        super("0011", "Aircraft Address", (u, binCode, result) -> {
            result.addAll(u.aircraft24BitAddress(binCode, 41, 64));
        });
    }
}
