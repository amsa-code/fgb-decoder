package com.amsa.fgb.internal;

class StandardLocationShipSecurityAlertSystem extends StandardLocationBase {

    StandardLocationShipSecurityAlertSystem() {
        super("1100", "Ship Security MMSI", (s, binCode, result) -> {
            result.add(Util.mmsiFromBinary(AttributeType.SHIP_SECURITY_MMSI, binCode, 41, 60));
            result.add(((StandardLocationShipSecurityAlertSystem) s).securityFixedBits(binCode, 61,
                    64));
        });
    }

    private HexAttribute securityFixedBits(String binCode, int s, int f) {
        String v = binCode.substring(s, f + 1);
        String e = "";

        // 08 Aug 2006
        if (!v.equals("0000")) {
            v += " (Non-Spec)";
        }

        return new HexAttribute(AttributeType.FIXED_BITS, s, f, v, e);
    }
}
