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

    static List<HexAttribute> userSerialFragment1(UserSerial u,String hexStr, String binCode) {
        List<HexAttribute> result = new ArrayList<HexAttribute>();
        result.add(u.messageType(binCode, 25, 26));
        result.add(u.hexData(hexStr, 25, binCode.length() - 1));
        result.add(u.hexId(binCode, 26, 85));
        result.add(u.countryCode(binCode, 27, 36));
        result.add(u.protocolType(binCode, 37, 39));
        result.add(u.beaconType(binCode, 40, 42));
        result.add(u.cospasSarsatAppCertFlag(binCode, 43));
        result.add(u.serialNumber(binCode, 44, 63));

        if (u.cospasSarsatAppCertFlagPresent(binCode)) {
            result.add(u.nationalUse(binCode, 64, 73));
            result.add(u.cospasSarsatAppCertNumber(binCode, 74, 83));
        } else {
            result.add(u.nationalUse(binCode, 64, 83));
        }

        result.add(u.auxRadioLocating(binCode, 84, 85));

        if (u.isUS) {
            result.add(u.usManufacturerId(binCode, 44, 51));
            result.add(u.usSeqNo(binCode, 52, 63));
            result.add(u.usModelId(binCode, 64, 67));
            result.add(u.usRunNo(binCode, 68, 75));
            result.add(u.usNatUse(binCode, 76, 83));
        }
        return result;
    }
    
    static List<HexAttribute> userSerialFragment2(UserSerial u, String hexStr, String binCode, List<HexAttribute> result) {
        if (hexStr.length() > 15) {
            result.add(u.bch1(binCode, 86, 106));
            if (u.isLongMessage(binCode)) {
                result.add(u.encodedPositionSource(binCode, 107));
                if (u.defaultFFFFFFFF(hexStr)) {
                    result.add(u.longMessage(binCode, 113, 144));
                } else {
                    if (u.default00000000(hexStr)) {
                        result.add(u.longMessage(binCode, 113, 144));
                    } else {
                        List<HexAttribute> res = result;
                        u.latitude(binCode, 108, 119).ifPresent(x -> res.add(x));
                        u.longitude(binCode, 120, 132).ifPresent(x -> res.add(x));
                        result.add(u.bch2(binCode, 133, 144));
                    }
                }
            }
            // 14/03/2005
            else {
                result = u.nonNationalUse(result, binCode);
            }
        }

        return result;
    }
}
