package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.davidmoten.guavamini.Lists;

abstract class User extends BeaconProtocol {

    private final String userProtocolCode;

    User(String protocolName, String userProtocolCode) {
        // 16 May 2005
        // ?1 is for 15-char Hex string where bit25 is unknown since it starts
        // with bit26
        super(Lists.newArrayList("01", "11", "?1"), protocolName);
        this.userProtocolCode = userProtocolCode;
    }

    @Override
    String getName() {
        return protocolName();
    }

    @Override
    boolean canDecode(String binCode) {
        String beaconCode = binCode.substring(25, 27);
        // System.out.println("Trying User " + name);

        if (beaconTypeCodes().contains(beaconCode)) {
            // String protocolCode = binCode.substring(27, 40);
            String protocolCode = binCode.substring(37, 40);
            return protocolCode.equals(this.userProtocolCode);
        }

        return false;
    }

    // This method should be overwritten by sub-classes
    @Override
    List<HexAttribute> decode(String hexStr) {
        List<HexAttribute> result = new ArrayList<HexAttribute>();
        String errorMsg = "ERROR: decode() called from User";
        result.add(new HexAttribute(AttributeType.ERROR, 0, "", errorMsg));

        return result;
    }

    // protocolName is defined in BeaconProtocol and is set by individual
    // protocol programs.
    @Override
    HexAttribute protocolType(String binCode, int s, int f) {
        String name = this.getName();
        String e = "";

        return new HexAttribute(AttributeType.USER_PROTOCOL_TYPE, s, f, name, e);
    }

    // Get the messageType. Is it LONG or SHORT?
    HexAttribute messageType(String binCode, int s, int f) { // b25-26
        String v = "User";
        String e = "";

        if (this.isLongMessage(binCode)) {
            v = v + " Location (Long)";
        } else if (this.isShortMessage(binCode)) {
            v = v + " (Short)";
        } else {
            v = v + " (Format - Unknown)";
        }

        return new HexAttribute(AttributeType.MESSAGE_TYPE, s, f, v, e);
    }

    // This method is called by UserAviation.java
    HexAttribute specificELTIdentifier(String binCode, int s) {
        String v = binCode.substring(s, s + 2);
        String e = "";

        final int code;
        if (v.equals("00")) {
            code = 0;
        } else if (v.equals("01")) {
            code = 1;
        } else if (v.equals("10")) {
            code = 2;
        } else {
            code = 3;
        }
        return new HexAttribute(AttributeType.SPECIFIC_ELT_NUMBER, s, s + 1, code, e);
    }

    // This method is called by UserAviation.java, UserMaritime.java and
    // UserRarioCallSign.java
    HexAttribute spare(String binCode, int s, int f) {
        String v = binCode.substring(s, f + 1);
        String e = "";

        if (v.length() == 2 && !v.equals("00")) {
            v += " (Non-Spec)";
        }

        return new HexAttribute(AttributeType.SPARE, s, f, v, e);
    }

    HexAttribute auxRadioLocating(String binCode, int s, int f) {
        String code = binCode.substring(s, f + 1);
        String e = "";
        String v = "";

        if (code.equals("01")) {
            v = "121.5 MHz";
        } else if (code.equals("00")) {
            v = "NONE";
        } else if (code.equals("10")) {
            v = "Maritimelocating: 9GHz SART";
        } else {
            v = "Other device(s)";
        }
        return new HexAttribute(AttributeType.AUX_RADIO_LOCATING, s, f, v, e);
    }

    private HexAttribute emergencyCode(String binCode, int s) {
        String v = "";
        String e = "";

        String str = "(" + binCode.charAt(107) + ")";

        if (this.emergencyCodePresent(binCode)) {
            v = "Present " + str;
        } else {
            // 29/03/2005
            // v = "NOT PRESENT (National Use)";
            v = "Default " + str;
        }

        return new HexAttribute(AttributeType.EMERGENCY_CODE, s, v, e);
    }

    private boolean emergencyCodePresent(String binCode) {
        return binCode.charAt(107) == '1';
    }

    private HexAttribute activationType(String binCode, int s) {
        String v = "";
        String e = "";
        if (binCode.charAt(s) == '1') {
            v = "Automatic and Manual";
        } else {
            v = "Manual Activation Only";
        }

        return new HexAttribute(AttributeType.ACTIVATION_TYPE, s, v, e);
    }

    /*
     * ******************************************************* The following methods
     * are used for User Location (LONG)
     * ******************************************************
     */

    Optional<HexAttribute> latitude(String binCode, int s, int f) {
        // Latitude b108-119 1270 60S (NON-SPEC)
        String e = "";
        String def = "011111110000";
        String bits = binCode.substring(s, f + 1);
        if (bits.equals(def)) {
            return Optional.empty();
        } else {
            int deg = Conversions.binaryToDecimal(bits.substring(1, 8));
            double latSeconds = deg * 60 * 60;
            int min = Conversions.binaryToDecimal(bits.substring(8, 12)) * 4;
            latSeconds += min * 60;
            int sign = bits.charAt(0) == '1' ? -1 : 1;
            latSeconds = sign * latSeconds;
            super.setLatSeconds(latSeconds);
            return Optional.of(
                    new HexAttribute(AttributeType.LATITUDE, s, f, latSeconds / 3600.0 + "", e));
        }
    }

    Optional<HexAttribute> longitude(String binCode, int s, int f) { // b120-132
        // Longitude b120-132 255 32W (NON-SPEC)
        String e = "";
        String def = "0111111110000";
        String bits = binCode.substring(s, f + 1);
        if (bits.equals(def)) {
            return Optional.empty();
        } else {
            int deg = Conversions.binaryToDecimal(bits.substring(1, 9));
            double lonSeconds = deg * 60 * 60;
            int min = Conversions.binaryToDecimal(bits.substring(9, 13)) * 4;
            lonSeconds += min * 60;
            int sign = bits.charAt(0) == '1' ? -1 : 1;
            lonSeconds = sign * lonSeconds;
            setLonSeconds(lonSeconds);
            return Optional.of(
                    new HexAttribute(AttributeType.LONGITUDE, s, f, lonSeconds / 3600.0 + "", e));
        }
    }

    // For bit 107-112. See C/S T.001 Figure A4
    List<HexAttribute> nonNationalUse(List<HexAttribute> result, String binCode) {
        // Decode bit 107
        result.add(emergencyCode(binCode, 107));

        // Decode bit 108
        result.add(activationType(binCode, 108));

        if (binCode.charAt(107) == '0') {
            String v = "Default(" + binCode.substring(109, 113) + ")";
            result.add(new HexAttribute(AttributeType.NATURE_OF_DISTRESS, 109, 112, v, ""));
        } else {
            result = allEmergencyCodes(result, binCode);
        }

        return result;
    }

    // This method may be overiddden by the subclasses
    List<HexAttribute> allEmergencyCodes(List<HexAttribute> result, String binCode) {
        return result;
    }

    static void userFragment(User u, String hexStr, String binCode, List<HexAttribute> result) {
        result.add(u.messageType(binCode, 25, 26));
        result.add(u.hexData(hexStr, 25, binCode.length() - 1));
        result.add(u.hexId(binCode, 26, 85));
        result.add(u.countryCode(binCode, 27, 36));
        result.add(u.protocolType(binCode, 37, 39));
    }

}
