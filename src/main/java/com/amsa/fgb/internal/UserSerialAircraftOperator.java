package com.amsa.fgb.internal;

class UserSerialAircraftOperator extends UserSerialAircraftBase {

    UserSerialAircraftOperator() {
        super("Aircraft Operator", "001", (u, binCode, result) -> {
            result.add(((UserSerialAircraftOperator) u).aircraftOperator(binCode, 44, 61));
            result.add(((UserSerialAircraftOperator) u).aircraftSerialNumber(binCode, 62, 73));
        });
    }

    @Override
    public HexAttribute aircraftOperator(String binCode, int s, int f) {
        // 11 May 2005
        String[] vE = Conversions.mBaudotBits2mBaudotStr(this.getName() + " " + serialBeaconType(),
                binCode.substring(s, f + 1), 6);
        String v = vE[0];
        String e = vE[1];

        return new HexAttribute(AttributeType.AIRCRAFT_OPERATOR, s, f, v, e);
    }

}
