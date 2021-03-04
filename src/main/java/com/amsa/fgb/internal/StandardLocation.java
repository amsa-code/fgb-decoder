package com.amsa.fgb.internal;

import java.util.ArrayList;
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

    // This method should be overwritten by sub-classes
    @Override
     List<HexAttribute> decode(String hexStr) {
        List<HexAttribute> result = new ArrayList<HexAttribute>();
        String errorMsg = "ERROR: decode() called from StandardLocation";
        result.add(new HexAttribute("", 0, "", errorMsg));

        return result;
    }

    // Get the messageType. Is it LONG or SHORT?
    HexAttribute messageType(String binCode, int s, int f) { // b25-26
        String v = "Standard Location";
        String e = "";

        // 16 May 2005
        v = this.getMsgTypeDesc(v, binCode);

        return new HexAttribute("Message Type", s, f, v, e);
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

        return new HexAttribute("Hex Id", s, s + binHexId.length() - 1, v, e);
    }

    HexAttribute coarsePosition(String binCode, int s, int f) {
        String code = binCode.substring(s, f + 1);
        String v = "";
        String e = "";

        if (code.equals("011111111101111111111")) {
            v = "DEFAULT";
        } else {
            String lat = this.lat(binCode);
            String lon = this.lon(binCode);
            v = lat + " " + lon;
            this.actualLatLong = true;
        }

        return new HexAttribute("Coarse Position", s, f, v, e);
    }

    private String lat(String binCode) {
        String result = "";

        int code = Conversions.binaryToDecimal(binCode.substring(66, 75));
        int deg = code / 4;
        this.latSeconds = deg * 60 * 60;

        int min = (code % 4) * 15;
        this.latSeconds += min * 60;

        char p = 'N';
        if (binCode.charAt(65) == '1') {
            p = 'S';
            this.latSeconds = this.latSeconds * -1;
        }

        // Format data for display with zero padding.
        String degStr = Conversions.zeroPadFromLeft(deg + "", 2);

        String minStr = Conversions.zeroPadFromLeft(min + "", 2);

        result = degStr + " " + minStr + p;

        return result;
    }

    private String lon(String binCode) {
        String result = "";

        int code = Conversions.binaryToDecimal(binCode.substring(76, 86));
        int deg = code / 4;
        this.lonSeconds = deg * 60 * 60;

        int min = (code % 4) * 15;
        this.lonSeconds += min * 60;

        char p = 'E';
        if (binCode.charAt(75) == '1') {
            p = 'W';
            this.lonSeconds = this.lonSeconds * -1;
        }

        // Format data for display with zero padding.
        String degStr = Conversions.zeroPadFromLeft(deg + "", 3);

        String minStr = Conversions.zeroPadFromLeft(min + "", 2);

        result = degStr + " " + minStr + p;

        return result;
    }

    HexAttribute offsetPosition(String binCode, int s, int f) {
        String e = "";
        String def = "10000011111000001111";

        String bits = binCode.substring(s, f + 1);
        String pos = "";
        if (bits.equals(def)) {
            pos = "DEFAULT";
        } else {
            // Lat Offset
            int min1 = Conversions.binaryToDecimal(bits.substring(1, 6));
            int sec1 = Conversions.binaryToDecimal(bits.substring(6, 10)) * 4;
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
            int min2 = Conversions.binaryToDecimal(bits.substring(11, 16));
            int sec2 = Conversions.binaryToDecimal(bits.substring(16, 20)) * 4;
            int offset2 = (min2 * 60) + sec2;

            if (bits.charAt(10) == '1') {
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

        return new HexAttribute("Offset Position", s, f, pos, e);
    }

    // C/S Type Approval</TD><TD>b41-50</TD><TD>115</TD></TR>
    HexAttribute cospatSarsatTypeApp(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute("C/S Type Approval", s, f, v, e);
    }

}