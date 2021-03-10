package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

class UserSerial extends User {

    String serialCode;
    String serialBeaconType;;

    UserSerial() {
        protocolName = "Serial";
        userProtocolCode = "011";

    }

    @Override
    String getName() {
        return protocolName;
    }

    @Override
     boolean canDecode(String binCode) {
        if (super.canDecode(binCode)) {
            String serCode = binCode.substring(40, 43);

            // Note: SubClass Constructor sets the serialCode in
            return serCode.equals(this.serialCode);
        }
        return false;
    }

    @Override
     List<HexAttribute> decodePartial(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.hexId(binCode, 26, 85));
        result.add(this.serialNumber(binCode, 44, 63));

        return result;
    }

    HexAttribute beaconType(String binCode, int s, int f) {
        String v = serialBeaconType;
        String e = "";

        return new HexAttribute(AttributeType.BEACON_TYPE, s, f, v, e);
    }

    HexAttribute serialNumber(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute(AttributeType.SERIAL_NUMBER, s, f, v, e);
    }

    boolean cospasSarsatAppCertFlagPresent(String binCode) {
        return binCode.charAt(43) == '1';
    }

    HexAttribute cospasSarsatAppCertFlag(String binCode, int s) {
        String v = "";
        String e = "";
        if (binCode.charAt(s) == '1') {
            v = "YES";
        } else {
            v = "NO";
        }

        return new HexAttribute(AttributeType.CS_CERT_NO_PRESENT, s, v, e);
    }

    HexAttribute cospasSarsatAppCertNumber(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute(AttributeType.CS_TYPE_APPROVAL_NUMBER, s, f, v, e);
    }

    HexAttribute usManufacturerId(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute(AttributeType.US_MANUFACTURER_ID, s, f, v, e);
    }

    HexAttribute usSeqNo(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute(AttributeType.US_SEQUENCE_NO, s, f, v, e);
    }

    HexAttribute usModelId(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute(AttributeType.US_MODEL_ID, s, f, v, e);
    }

    HexAttribute usRunNo(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute(AttributeType.US_RUN_NO, s, f, v, e);
    }

    HexAttribute usNatUse(String binCode, int s, int f) {
        String v = binCode.substring(s, f + 1);
        String e = "";

        return new HexAttribute(AttributeType.US_NATIONAL_USE, s, f, v, e);
    }

}
