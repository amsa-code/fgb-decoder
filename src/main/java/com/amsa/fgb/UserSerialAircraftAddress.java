package com.amsa.fgb;

import java.util.ArrayList;
import java.util.List;

class UserSerialAircraftAddress extends UserSerial {

    UserSerialAircraftAddress() {
        serialBeaconType = "Aircraft Address";
        serialCode = "011";
    }

    @Override
    List<HexAttribute> decodeSearch(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.hexId(binCode, 26, 85));

        HexAttribute h = this.aircraftCallSign(binCode, 44, 67);
        String value = h.getValue();
        if (value.length() > 0) {
            result.add(h);
        }

        result.add(this.aircraft24BitAddressBinary(binCode, 44, 67));

        return result;
    }

    @Override
    List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);

        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.messageType(binCode, 25, 26));
        result.add(this.hexData(hexStr, 25, (binCode.length() - 1)));
        result.add(this.hexId(binCode, 26, 85));
        result.add(this.countryCode(binCode, 27, 36));
        result.add(this.protocolType(binCode, 37, 39));

        result.add(this.beaconType(binCode, 40, 42));
        result.add(this.cospasSarsatAppCertFlag(binCode, 43));
        result.add(this.aircraft24BitAddress(binCode, 44, 67));
        result.add(this.eltNumber(binCode, 68, 73));

        if (this.cospasSarsatAppCertFlagPresent(binCode)) {
            result.add(this.cospasSarsatAppCertNumber(binCode, 74, 83));
        } else {
            result.add(this.nationalUse(binCode, 74, 83));
        }

        result.add(this.auxRadioLocating(binCode, 84, 85));

        if (this.isUS) {
            result.add(this.usManufacturerId(binCode, 44, 51));
            result.add(this.usSeqNo(binCode, 52, 63));
            result.add(this.usModelId(binCode, 64, 67));
            result.add(this.usRunNo(binCode, 68, 75));
            result.add(this.usNatUse(binCode, 76, 83));
        }

        if (hexStr.length() > 15) {
            result.add(this.bch1(binCode, 86, 106));

            if (this.isLongMessage(binCode)) {
                result.add(this.encodedPositionSource(binCode, 107));

                if (this.defaultFFFFFFFF(hexStr)) {
                    result.add(this.longMessage(binCode, 113, 144));
                } else {
                    if (this.default00000000(hexStr)) {
                        result.add(this.longMessage(binCode, 113, 144));
                    } else {
                        result.add(this.latitude(binCode, 108, 119));
                        result.add(this.longitude(binCode, 120, 132));
                        result.add(this.bch2(binCode, 133, 144));
                    }
                }
            }
            // 14/03/2005
            else {
                result = this.nonNationalUse(result, binCode);
            }
        }

        return result;
    }

    private HexAttribute eltNumber(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute("Additional ELT numbers", s, f, v, e);
    }

    // This overidding method will be called by User.java
    @Override
    List<HexAttribute> allEmergencyCodes(List<HexAttribute> result, String binCode) {
        result = Common.nonMaritimeEmergencyCodes(result, binCode);

        return result;
    }

}
