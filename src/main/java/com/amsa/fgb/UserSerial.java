package com.amsa.fgb;

import java.util.ArrayList;
import java.util.List;

public class UserSerial extends User {

    public String serialCode;
    public String serialBeaconType;;

    public UserSerial() {
        protocolName = "Serial";
        userProtocolCode = "011";

    }

    @Override
    public String getName() {
        return protocolName;
    }

    @Override
    public boolean canDecode(String binCode) {
        if (super.canDecode(binCode)) {
            String serCode = binCode.substring(40, 43);

            // Note: SubClass Constructor sets the serialCode in
            return serCode.equals(this.serialCode);
        }
        return false;
    }

    @Override
    public List<HexAttribute> decodeSearch(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.hexId(binCode, 26, 85));
        result.add(this.serialNumber(binCode, 44, 63));

        return result;
    }

    public HexAttribute beaconType(String binCode, int s, int f) {
        String v = serialBeaconType;
        String e = "";

        return new HexAttribute("Beacon Type", s, f, v, e);
    }

    public HexAttribute serialNumber(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute("Serial Number", s, f, v, e);
    }

    public boolean cospasSarsatAppCertFlagPresent(String binCode) {
        return binCode.charAt(43) == '1';
    }

    public HexAttribute cospasSarsatAppCertFlag(String binCode, int s) {
        String v = "";
        String e = "";
        if (binCode.charAt(s) == '1') {
            v = "YES";
        } else {
            v = "NO";
        }

        return new HexAttribute("C/S cert. no. present", s, v, e);
    }

    public HexAttribute cospasSarsatAppCertNumber(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute("C/S Type approval number", s, f, v, e);
    }

    public HexAttribute usManufacturerId(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute("US Manufacturer ID", s, f, v, e);
    }

    public HexAttribute usSeqNo(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute("US Sequence No.", s, f, v, e);
    }

    public HexAttribute usModelId(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute("US Model ID.", s, f, v, e);
    }

    public HexAttribute usRunNo(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute("US Run No.", s, f, v, e);
    }

    public HexAttribute usNatUse(String binCode, int s, int f) {
        String v = binCode.substring(s, f + 1);
        String e = "";

        return new HexAttribute("US National Use", s, f, v, e);
    }

}
