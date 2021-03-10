package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.amsa.fgb.internal.Common.Position;

// NOTE: This is a new location protocol (based on NationalLocation with minor modifications).

class ReturnLinkServiceLocation extends BeaconProtocol {

    private static final int COARSE_POSITION_START = 67;
    private static final int COARSE_POSITION_FINISH = 85;
    private static final String rlsProtocolCode = "1101";

    ReturnLinkServiceLocation() {
        beaconTypeCode.add("00");
        beaconTypeCode.add("10");

        // 16 May 2005
        // This is for 15-char Hex string where bit25 is unknown since it starts
        // with bit26
        beaconTypeCode.add("?0");

        defaultFixedBits = "110";

        protocolName = "Return Link Service";
    }

    @Override
    String getName() {

        return protocolName;
    }

    @Override
    boolean canDecode(String binCode) {
        String protocol = binCode.substring(25, 27);

        // System.out.println("Trying RLS Location " + name);

        if (beaconTypeCode.contains(protocol)) {
            String protocolCode = binCode.substring(37, 41);
            return protocolCode.equals(rlsProtocolCode);
        }

        return false;
    }

    @Override
    List<HexAttribute> decodePartial(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<HexAttribute>();

        // 16/03/2005, why 26-65??? decode() is 26-59
        // And do we need to decode more besides these 2 for decodeSearch()
        // (like decode() does)?

        // result.add(this.hexId(binCode, 26, 65));
        // result.add(this.beaconSerialNumber(binCode, 41, 58));

        // 02/Nov/2005
        // result.add(this.hexId(binCode,26,59));
        result.add(this.hexId(binCode, 26, 85));
        // slight change from NationalLocation 41 to 43 below
        result.add(this.beaconSerialNumber(binCode, 43, 58));

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

        // for some reason we always report the hex id with no location (called default)
        HexAttribute hexId = this.hexIdWithDefaultLocation(binCode, 26, 67);
        result.add(hexId);
        result.add(this.countryCode(binCode, 27, 36));
        result.add(this.protocolType(binCode, 37, 40));
        // new code for RLS (differs from NationalLocation)
        result.add(beaconType(binCode, 41, 42));
        result.add(this.rlsTacNumber(binCode, 41, 52));
        result.add(this.rlsId(binCode, 53, 66));
        result.addAll(this.coarsePositionAttributes(binCode));

        if (hexStr.length() > 15) {
            // result.add(this.bch1(binCode, 86, 106));
            result = this.bch1(result, binCode, hexId);

            result.add(this.encodedPositionSource(binCode, 107));
            result.add(this.homing(binCode, 108));
            result.add(rlmCapabilityType1(binCode, 109));
            result.add(rlmCapabilityType2(binCode, 110));
            if (binCode.charAt(109) == '0' && binCode.charAt(110) == '0') {
                result.add(new HexAttribute(AttributeType.RLM_CAPABILITY_TYPE, 109, 110, "00",
                        "Invalid"));
            }
            result.add(rlmReceivedType1(binCode, 111));
            result.add(rlmReceivedType2(binCode, 112));
            result.add(rlsProviderIdentification(binCode, 113, 114));
            result.addAll(finePositionAttributes(binCode));

            // this is a defensive check not mentioned in the spec
            if (this.defaultFFFFFFFF(hexStr)) {
                result.add(this.longMessage(binCode, 113, 144));
            } else {
                // this is a defensive check not mentioned in the spec
                if (this.default00000000(hexStr)) {
                    result.add(this.longMessage(binCode, 113, 144));
                } else {
                    result.add(this.bch2(binCode, 133, 144));
                }
            }

            if (this.actualLatLong) {
                result.add(actualLatitude());
                result.add(actualLongitude());
            }
        }

        return result;
    }

    private List<HexAttribute> finePositionAttributes(String binCode) {
        Position p = finePosition(binCode);
        if (p == null) {
            return Collections.emptyList();
        } else {
            actualLatLong = true;
            latSeconds = p.latSeconds();
            lonSeconds = p.lonSeconds();
            List<HexAttribute> list = new ArrayList<HexAttribute>();
            list.add(new HexAttribute(AttributeType.LATITUDE, 115, 132, p.latDecimal() + "", ""));
            list.add(new HexAttribute(AttributeType.LONGITUDE, 115, 132, p.lonDecimal() + "", ""));
            return list;
        }
    }

    private static HexAttribute rlsProviderIdentification(String binCode, int s, int f) {
        String x = binCode.substring(s, f + 1);
        final String v;
        if (x.equals("01")) {
            v = "Galileo";
        } else if (x.equals("10")) {
            v = "GLONASS";
        } else {
            v = "SPARE";
        }
        return new HexAttribute(AttributeType.RLS_PROVIDER_ID, s, f, v, "");
    }

    // This method is called by ReturnLinkServiceLocation.java
    private static HexAttribute beaconType(String binCode, int s, int f) {
        String v = binCode.substring(s, f + 1);
        String e = "";

        if (v.length() == 2) {
            if (v.equals("00")) {
                v = "ELT";
            } else if (v.equals("01")) {
                v = "EPIRB";
            } else if (v.equals("10")) {
                v = "PLB";
            } else if (v.equals("11")) {
                v = "SPARE";
            } else {
                v += " (Non-Spec)";
            }
        } else {
            v += " (Non-Spec)";
        }

        return new HexAttribute(AttributeType.BEACON_TYPE, s, f, v, e);
    }

    private static HexAttribute rlmCapabilityType1(String binCode, int i) {
        String v = binCode.charAt(i) == '1' ? "YES" : "NO";
        return new HexAttribute(AttributeType.RLM_CAPABILITY_TYPE_1_AUTO, i, i, v, "");
    }

    private static HexAttribute rlmCapabilityType2(String binCode, int i) {
        String v = binCode.charAt(i) == '1' ? "YES" : "NO";
        return new HexAttribute(AttributeType.RLM_CAPABILITY_TYPE_2_MANUAL, i, i, v, "");
    }

    private static HexAttribute rlmReceivedType1(String binCode, int i) {
        String v = binCode.charAt(i) == '1' ? "YES" : "NO";
        return new HexAttribute(AttributeType.RLM_RECEIVED_TYPE_1_AUTO, i, i, v, "");
    }

    private static HexAttribute rlmReceivedType2(String binCode, int i) {
        String v = binCode.charAt(i) == '1' ? "YES" : "NO";
        return new HexAttribute(AttributeType.RLM_RECEIVED_TYPE_2_MANUAL, i, i, v, "");
    }

    // Get the messageType. Is it LONG or SHORT?
    private HexAttribute messageType(String binCode, int s, int f) { // b25-26
        String v = "Return Link Service Location";
        String e = "";

        // // 16 May 2005
        // v = this.getMsgTypeDesc(v, binCode);

        return new HexAttribute(AttributeType.MESSAGE_TYPE, s, f, v, e);
    }

    // 2/Nov/2005
    // Renamed from "hexId()"
    // Called by all subclasses
    private HexAttribute hexIdWithDefaultLocation(String binCode, int s, int f) { // b26-59
        String binHexId = binCode.substring(s, f);

        // System.out.println(binHexId);
        String defaultValue1 = "011111111";
        String defaultValue2 = "0111111111";
        binHexId += defaultValue1;
        binHexId += defaultValue2;

        String v = Conversions.binaryToHex(binHexId);
        String e = "";
        return new HexAttribute(AttributeType.HEX_ID, s, s + binHexId.length() - 1, v, e);
    }

    private static Position coarsePosition(String binCode) {
        int start = COARSE_POSITION_START;
        int finish = COARSE_POSITION_FINISH;
        String code = binCode.substring(start, finish + 1);

        if (code.equals("0111111110111111111")) {
            return null;
        } else {
            int secondsPerUnit = 30 * 60; // 30 minutes
            return Common.position(binCode, start, finish - start + 1, secondsPerUnit);
        }
    }

    private List<HexAttribute> coarsePositionAttributes(String binCode) {
        Position p = coarsePosition(binCode);

        if (p == null) {
            return Collections.emptyList();
        } else {
            this.latSeconds = p.latSeconds();
            this.lonSeconds = p.lonSeconds();
            this.actualLatLong = true;
            return Util.coarsePositionAttributes(latSeconds, lonSeconds, COARSE_POSITION_START,
                    COARSE_POSITION_FINISH);
        }
    }

    private static Position finePosition(String binCode) {
        String latBits = binCode.substring(115, 123);
        if (latBits.equals("100001111")) {
            return null;
        }
        final int latOffsetSeconds;
        {
            int sign = binCode.charAt(115) == '1' ? 1 : -1;
            int minutes = Conversions.binaryToDecimal(binCode.substring(116, 119));
            int seconds = Conversions.binaryToDecimal(binCode.substring(120, 123)) * 4;
            latOffsetSeconds = sign * (minutes * 60 + seconds);
        }
        final int lonOffsetSeconds;
        {
            int sign = binCode.charAt(124) == '1' ? 1 : -1;
            int minutes = Conversions.binaryToDecimal(binCode.substring(125, 128));
            int seconds = Conversions.binaryToDecimal(binCode.substring(129, 132)) * 4;
            lonOffsetSeconds = sign * (minutes * 60 + seconds);
        }
        Position p = coarsePosition(binCode);
        if (p == null) {
            return null;
        }
        // From spec C/S T.0001 June 2018 Issue 4 Rev 3, offset is applied to magnitude
        // of value and then sign reapplied
        int latSeconds = (Math.abs(p.latSeconds()) + latOffsetSeconds) * sign(p.latSeconds());
        int lonSeconds = (Math.abs(p.lonSeconds()) + lonOffsetSeconds) * sign(p.lonSeconds());
        return new Position(latSeconds, lonSeconds);
    }

    private static int sign(int n) {
        if (n == 0) {
            return 0;
        } else if (n > 0) {
            return 1;
        } else {
            return -1;
        }
    }

}
