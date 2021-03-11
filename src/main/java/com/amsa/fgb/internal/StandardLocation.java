package com.amsa.fgb.internal;

import java.util.Collections;
import java.util.List;

abstract class StandardLocation extends BeaconProtocol {

    String stdProtocolCode; // Set in constructors of sub-classes

    StandardLocation() {
        beaconTypeCode.add("00");
        beaconTypeCode.add("10");

        // 16 May 2005
        // This is for 15-char Hex string where bit25 is unknown since it starts with
        // bit26
        beaconTypeCode.add("?0");

        defaultFixedBits = "1101";
    }

    @Override
    String getName() {
        return protocolName;
    }

    @Override
    boolean canDecode(String binCode) {
        String protocol = binCode.substring(25, 27);
        // System.out.println("Trying Standard Location " + name);
        if (beaconTypeCode.contains(protocol)) {
            String protocolCode = binCode.substring(37, 41);
            return protocolCode.equals(this.stdProtocolCode);
        }

        return false;
    }

    // Get the messageType. Is it LONG or SHORT?
    HexAttribute messageType(String binCode, int s, int f) { // b25-26
        String v = "Standard Location";
        String e = "";

        // 16 May 2005
        v = this.getMsgTypeDesc(v, binCode);

        return new HexAttribute(AttributeType.MESSAGE_TYPE, s, f, v, e);
    }

    // 1/Nov/2005
    // Renamed from "hexId()"
    // Called by all subclasses
    HexAttribute hexIdWithDefaultLocation(String binCode, int s, int f) { // b26-65
        String binHexId = binCode.substring(s, f);

        String defaultValue1 = "0111111111";
        String defaultValue2 = "01111111111";
        binHexId += defaultValue1;
        binHexId += defaultValue2;

        String v = Conversions.binaryToHex(binHexId);
        String e = "";

        return new HexAttribute(AttributeType.HEX_ID, s, s + binHexId.length() - 1, v, e);
    }

    List<HexAttribute> coarsePositions(String binCode, int s, int f) {
        String code = binCode.substring(s, f + 1);
        if (code.equals("011111111101111111111")) {
            return Collections.emptyList();
        } else {
            this.latSeconds = latSeconds(binCode);
            this.lonSeconds = lonSeconds(binCode);
            this.actualLatLong = true;
            return Util.coarsePositionAttributes(latSeconds, lonSeconds, s, f);
        }
    }

    private static double latSeconds(String binCode) {
        int code = Conversions.binaryToDecimal(binCode.substring(66, 75));
        int deg = code / 4;
        double latSeconds = deg * 60 * 60;

        int min = (code % 4) * 15;
        latSeconds += min * 60;

        if (binCode.charAt(65) == '1') {
            latSeconds = latSeconds * -1;
        }
        return latSeconds;
    }

    private static double lonSeconds(String binCode) {
        int code = Conversions.binaryToDecimal(binCode.substring(76, 86));
        int deg = code / 4;
        double lonSeconds = deg * 60 * 60;
        int min = (code % 4) * 15;
        lonSeconds += min * 60;
        if (binCode.charAt(75) == '1') {
            lonSeconds = lonSeconds * -1;
        }
        return lonSeconds;
    }

    List<HexAttribute> offsetPosition(String binCode, int s, int f) {
        String def = "10000011111000001111";
        String bits = binCode.substring(s, f + 1);
        if (bits.equals(def)) {
            return Collections.emptyList();
        } else {
            // Lat Offset
            int min1 = Conversions.binaryToDecimal(bits.substring(1, 6));
            int sec1 = Conversions.binaryToDecimal(bits.substring(6, 10)) * 4;
            int offset1 = (min1 * 60) + sec1;

            if (bits.charAt(0) != '1') {
                offset1 = offset1 * -1;
            }

            // Apply offset to absolute value of coarse position
            double tempLat = Math.abs(this.latSeconds);
            tempLat += offset1;
            if (this.latSeconds < 0)
                tempLat *= -1;
            this.latSeconds = tempLat;

            // Lon Offset
            int min2 = Conversions.binaryToDecimal(bits.substring(11, 16));
            int sec2 = Conversions.binaryToDecimal(bits.substring(16, 20)) * 4;
            int offset2 = (min2 * 60) + sec2;

            if (bits.charAt(10) != '1') {
                offset2 = offset2 * -1;
            }

            // Apply offset to absolute value of coarse position
            double tempLon = Math.abs(this.lonSeconds);
            tempLon += offset2;
            if (this.lonSeconds < 0)
                tempLon *= -1;
            this.lonSeconds = tempLon;
            return Util.offsetPositionAttributes(offset1, offset2, s, f);
        }
    }

    // C/S Type Approval</TD><TD>b41-50</TD><TD>115</TD></TR>
    HexAttribute cospatSarsatTypeApp(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute(AttributeType.CS_TYPE_APPROVAL, s, f, v, e);
    }

}
