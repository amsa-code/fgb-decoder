package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

// NOTE: ALL other NationalLocation??? classes inheriting from this one
// only exist to identify the protocol.  The hex String is decoded from
// here.

abstract class NationalLocation extends BeaconProtocol {

    String natProtocolCode; // Set in constructors of sub-classes

    NationalLocation() {
        beaconTypeCode.add("00");
        beaconTypeCode.add("10");

        // 16 May 2005
        // This is for 15-char Hex string where bit25 is unknown since it starts with
        // bit26
        beaconTypeCode.add("?0");

        defaultFixedBits = "110";
    }

    @Override
    String getName() {

        return protocolName;
    }

    @Override
     boolean canDecode(String binCode) {
        String protocol = binCode.substring(25, 27);

        // System.out.println("Trying National Location " + name);

        if (beaconTypeCode.contains(protocol)) {
            String protocolCode = binCode.substring(37, 41);
            return protocolCode.equals(this.natProtocolCode);
        }

        return false;
    }

    @Override
     List<HexAttribute> decodePartial(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        // 16/03/2005, why 26-65??? decode() is 26-59
        // And do we need to decode more besides these 2 for decodeSearch() (like
        // decode() does)?

        // result.add(this.hexId(binCode, 26, 65));
        // result.add(this.beaconSerialNumber(binCode, 41, 58));

        // 02/Nov/2005
        // result.add(this.hexId(binCode,26,59));
        result.add(this.hexId(binCode, 26, 85));
        result.add(this.beaconSerialNumber(binCode, 41, 58));

        return result;
    }

    // 2/Nov/2005
    // Change from "hexId()" to hexIdWithDefaultLocation()"
    // All beacon types conform to the following decode.
    @Override
     List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.messageType(binCode, 25, 26));
        result.add(this.hexData(hexStr, 25, (binCode.length() - 1)));
        // 2/Nov/2005
        // result.add(this.hexId(binCode, 26, 59));
        // HexAttribute hexId = this.hexId(binCode, 26, 85);
        // result.add(hexId);
        HexAttribute hexId = hexIdWithDefaultLocation(binCode, 26, 59);
        result.add(hexId);

        result.add(this.countryCode(binCode, 27, 36));
        result.add(this.protocolType(binCode, 37, 40));

        result.add(this.beaconSerialNumber(binCode, 41, 58));
        result.addAll(this.coarsePosition(binCode, 59, 85));

        if (hexStr.length() > 15) {
            // result.add(this.bch1(binCode, 86, 106));
            result = this.bch1(result, binCode, hexId);

            result.add(this.fixedBits(binCode, 107, 109));
            result.add(this.additionalDataFlag(binCode, 110));

            if (this.isLongMessage(binCode)) {
                result.add(this.encodedPositionSource(binCode, 111));
                result.add(this.homing(binCode, 112));
                if (this.defaultFFFFFFFF(hexStr)) {
                    result.add(this.longMessage(binCode, 113, 144));
                } else {
                    if (this.default00000000(hexStr)) {
                        result.add(this.longMessage(binCode, 113, 144));
                    } else {
                        if (this.positionalDataPresent(binCode)) {
                            result.add(offsetPosition(binCode, 113, 126));
                            result.add(nationalUse(binCode, 127, 132));
                        } else {
                            result.add(nationalUse(binCode, 113, 132));
                        }
                        result.add(this.bch2(binCode, 133, 144));
                    }
                }
            } else {
                result.add(this.encodedPositionSource(binCode, 111));
                result.add(this.homing(binCode, 112));
            }
            if (this.actualLatLong) {
                result.add(actualLatitude());
                result.add(actualLongitude());
            }
        }

        return result;
    }

    // Get the messageType. Is it LONG or SHORT?
    private HexAttribute messageType(String binCode, int s, int f) { // b25-26
        String v = "National Location";
        String e = "";

        // 16 May 2005
        v = this.getMsgTypeDesc(v, binCode);

        return new HexAttribute(AttributeType.MESSAGE_TYPE, s, f, v, e);
    }

    // 2/Nov/2005
    // Renamed from "hexId()"
    // Called by all subclasses
    private static HexAttribute hexIdWithDefaultLocation(String binCode, int s, int f) { // b26-59
        String binHexId = binCode.substring(s, f);

        // System.out.println(binHexId);
        String defaultValue1 = "0111111100000";
        String defaultValue2 = "01111111100000";
        binHexId += defaultValue1;
        binHexId += defaultValue2;

        String v = Conversions.binaryToHex(binHexId);
        String e = "";
        return new HexAttribute(AttributeType.HEX_ID, s, s + binHexId.length() - 1, v, e);
    }

    // Coarse Position</TD><TD>b59-85</TD><TD>35 44S 115 30E
    private List<HexAttribute> coarsePosition(String binCode, int s, int f) { // b59-85
        String code = binCode.substring(s, f + 1);
        String v = "";

        if (code.equals("011111110000001111111100000")) {
            v = "DEFAULT";
        } else {
            String lat = this.lat(binCode);
            String lon = this.lon(binCode);
            v = lat + " " + lon;
            this.actualLatLong = true;
        }
        return Util.coarsePositionAttributes(v, s, f);
    }

    // Check the * 2 (it used to be *4)
    private String lat(String binCode) {
        String result = "";
        int deg = Conversions.binaryToDecimal(binCode.substring(60, 67));
        String degStr = Conversions.zeroPadFromLeft(deg + "", 2);

        this.latSeconds = deg * 60 * 60;

        int min = Conversions.binaryToDecimal(binCode.substring(67, 72)) * 2;
        String minStr = Conversions.zeroPadFromLeft(min + "", 2);

        this.latSeconds += min * 60;

        char p = 'N';
        if (binCode.charAt(59) == '1') {
            p = 'S';
            this.latSeconds = this.latSeconds * -1;
        }
        result = degStr + " " + minStr + p;

        return result;
    }

    private String lon(String binCode) {
        String result = "";
        int deg = Conversions.binaryToDecimal(binCode.substring(73, 81));
        String degStr = Conversions.zeroPadFromLeft(deg + "", 3);

        this.lonSeconds = deg * 60 * 60;

        int min = Conversions.binaryToDecimal(binCode.substring(81, 86)) * 2;
        String minStr = Conversions.zeroPadFromLeft(min + "", 2);

        this.lonSeconds += min * 60;

        char p = 'E';
        if (binCode.charAt(72) == '1') {
            p = 'W';
            this.lonSeconds = this.lonSeconds * -1;
        }
        result = degStr + " " + minStr + p;

        return result;
    }

    private HexAttribute offsetPosition(String binCode, int s, int f) {
        String e = "";
        String def = "10011111001111";

        String bits = binCode.substring(s, f + 1);
        String pos = "";
        if (bits.equals(def)) {
            pos = "DEFAULT";
        } else {
            // Lat Offset
            int min1 = Conversions.binaryToDecimal(bits.substring(1, 3));
            int sec1 = Conversions.binaryToDecimal(bits.substring(3, 7)) * 4;
            int offset1 = (min1 * 60) + sec1;
            if (bits.charAt(0) == '1') {
                pos = "+";
            } else {
                pos = "-";
                offset1 = offset1 * -1;
            }

            // Apply offset to absolute value of coarse position
            double tempLat = Math.abs(this.latSeconds);
            tempLat += offset1;
            if (this.latSeconds < 0)
                tempLat *= -1;
            this.latSeconds = tempLat;

            pos = pos + min1;
            pos = pos + " " + sec1;

            // Lon Offset
            int min2 = Conversions.binaryToDecimal(bits.substring(8, 10));
            int sec2 = Conversions.binaryToDecimal(bits.substring(10, 14)) * 4;
            int offset2 = (min2 * 60) + sec2;
            if (bits.charAt(7) == '1') {
                pos = pos + " +";
            } else {
                pos = pos + " -";
                offset2 = offset2 * -1;
            }

            // Apply offset to absolute value of coarse position
            double tempLon = Math.abs(this.lonSeconds);
            tempLon += offset2;
            if (this.lonSeconds < 0)
                tempLon *= -1;
            this.lonSeconds = tempLon;

            pos = pos + min2;
            pos = pos + " " + sec2;
        }

        return new HexAttribute(AttributeType.OFFSET_POSITION, s, f, pos, e);
    }

}
