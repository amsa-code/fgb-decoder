package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

abstract class BeaconProtocol {

    protected boolean isUS = false;

    protected List<String> beaconTypeCode;
    protected String protocolName;
    // String messageTypeDesc;
    // String countryCode;

    protected boolean actualLatLong;
    protected double latSeconds;
    protected double lonSeconds;

    protected String defaultFixedBits;

    BeaconProtocol() {
        beaconTypeCode = new ArrayList<String>(2);
        this.actualLatLong = false;
        this.latSeconds = 0;
        this.lonSeconds = 0;
    }

    abstract boolean canDecode(String binCode);

    // This method is also overwritten by subclasses but
    // all sub-classes should call super.decode(binStr)
    // as this method will decode those bits that are
    // common to ALL protocols.
    abstract List<HexAttribute> decode(String hexStr);

    abstract String getName();

    boolean isLongMessage(String binCode) {
        String code = binCode.substring(25, 27);
        List<String> longCodes = new ArrayList<String>(2);
        longCodes.add("10");
        longCodes.add("11");

        return longCodes.contains(code);
    }

    // 16 May 2005
    boolean isShortMessage(String binCode) {
        String code = binCode.substring(25, 27);
        List<String> longCodes = new ArrayList<String>(2);
        longCodes.add("00");
        longCodes.add("01");

        return longCodes.contains(code);
    }

    // 16 May 2005
    String getMsgTypeDesc(String v, String binCode) {
        if (this.isLongMessage(binCode))
            v = v + " (Long)";
        else if (this.isShortMessage(binCode))
            v = v + " (Short)";
        else
            v = v + " (Format - Unknown)";

        return v;
    }

    boolean defaultFFFFFFFF(String hexStr) {
        int len = hexStr.length();
        String code = hexStr.substring(len - 8, len);

        return code.equals("FFFFFFFF");
    }

    HexAttribute longMessage(String binCode, int s, int f) {
        String v = "YES";
        String e = "";

        return new HexAttribute(AttributeType.IS_LONG_MESSAGE_DEFAULT, s, f, v, e);
    }

    boolean default00000000(String hexStr) {
        int len = hexStr.length();
        String code = hexStr.substring(len - 8, len);

        return code.equals("00000000");
    }

    HexAttribute protocolType(String binCode, int s, int f) {
        String name = this.getName();
        String e = "";

        return new HexAttribute(AttributeType.PROTOCOL_TYPE, s, f, name, e);
    }

    HexAttribute rlsTacNumber(String binCode, int s, int f) {
        char prefix = rlsTacNumberPrefix(binCode, s);
        String v = binCode.substring(s + 2, f + 1);
        String last3Digits = Conversions.zeroPadFromLeft(Integer.toString(Conversions.binaryToDecimal(v)), 3);
        String result = prefix + last3Digits;
        String e = "";
        return new HexAttribute(AttributeType.RLS_TAC_NUMBER, s, f, result, e);
    }

    private char rlsTacNumberPrefix(String binCode, int s) {
        final String v = binCode.substring(s, s + 2);
        if (v.equals("00")) {
            return '2';
        } else if (v.equals("01")) {
            return '1';
        } else if (v.equals("10")) {
            return '3';
        } else {
            return 'T';
        }
    }

    HexAttribute rlsId(String binCode, int s, int f) {
        String v = binCode.substring(s, f + 1);
        String e = "";
        int result = Conversions.binaryToDecimal(v);
        return new HexAttribute(AttributeType.RLS_ID, s, f, result, e);
    }

    HexAttribute hexData(String hexStr, int s, int f) {
        String e = "";

        // return new HexAttribute("Hex Data", s, f, hexStr, e);

        // 16 May 2005
        if ((hexStr.trim()).length() != 30)
            return new HexAttribute(AttributeType.HEX_DATA, 25, 144, "Unknown", e);
        else
            return new HexAttribute(AttributeType.HEX_DATA, s, f, hexStr, e);
    }

    HexAttribute hexId(String binCode, int s, int f) { // b26-85
        String binHexId = binCode.substring(s, f + 1);
        String v = Conversions.binaryToHex(binHexId);
        String e = "";

        return new HexAttribute(AttributeType.HEX_ID, s, f, v, e);
    }

    int getCountryCode(String binCode, int s, int f) {
        String binCC = binCode.substring(s, f + 1);

        return Conversions.binaryToDecimal(binCC);
    }

    HexAttribute countryCode(String binCode, int s, int f) {
        int cc = this.getCountryCode(binCode, s, f);
        String e = "";
        String v = cc + "";

        // Is this a US beacon
        List<String> UScodes = new ArrayList<String>(0);
        UScodes.add("338");
        UScodes.add("366");
        UScodes.add("367");
        UScodes.add("368");
        UScodes.add("369");

        this.isUS = UScodes.contains(v);
        return new HexAttribute(AttributeType.COUNTRY_CODE, s, f, v, e);
    }

    // Beacon Serial Number</TD><TD>b51-64</TD><TD>999</TD></TR>
    HexAttribute beaconSerialNumber(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute(AttributeType.BEACON_SERIAL_NUMBER, s, f, v, e);
    }

    List<HexAttribute> aircraft24BitAddress(String binCode, int s, int f) {
        String code = binCode.substring(s, f + 1);
        String e = "";
        List<HexAttribute> list = new ArrayList<>();
        String hex = Conversions.binaryToHex(code);
        String oct = removeLeadingZeros(Conversions.binaryToOctal(code));

        list.add(new HexAttribute(AttributeType.AIRCRAFT_24_BIT_ADDRESS_HEX, s, f, hex, e));
        list.add(new HexAttribute(AttributeType.AIRCRAFT_24_BIT_ADDRESS_OCTAL, s, f, oct, e));

        HexAttribute h = this.aircraftCallSign(binCode, s, f);
        String result = h.getValue();
        if (result.length() > 0) {
            // 13 May 2005
            // Make the output align to the rest output
            list.add(new HexAttribute(AttributeType.AIRCRAFT_CALL_SIGN, s, f, result, e));
        }

        String countryReg = Conversions.getAircraftCountryOfReg(code);
        if (countryReg.length() > 0) {
            list.add(new HexAttribute(AttributeType.COUNTRY_OF_REGISTRATION, s, f, countryReg, e));
        }
        return list;
    }

    final HexAttribute aircraft24BitAddressBinary(String binCode, int s, int f) {
        String e = "";
        String v = binCode.substring(s, f + 1);

        return new HexAttribute(AttributeType.AIRCRAFT_24_BIT_ADDRESS_BINARY, s, f, v, e);
    }

    HexAttribute aircraftCallSign(String binCode, int s, int f) {
        String e = "";
        String v = "";
        String country = binCode.substring(s, s + 6);

        // System.out.println("In BeaconProtocol.java, country=" + country);

        // if the first 6 bits are 011111 then it is an AUST reg beacon
        // we can use an alg. to work out the callsign
        if (country.equals("011111")) {
            // 10 May 2005, based on the CASA algorithm passed by Scott
            // Lillinton
            // Discard the first 6 bits, only use the rest 18 bits
            String code = binCode.substring(s + 6, f + 1);

            // String bin = binCode;
            // String code = bin.substring(6, bin.length());

            v = Conversions.binaryToAircraftCallsign(code);
        }

        // System.out.println("In BeaconProtocol.java, callSign: "+v);

        return new HexAttribute(AttributeType.AIRCRAFT_CALL_SIGN, s, f, v, e);
    }

    HexAttribute specificBeaconNumber(String binCode, int s, int f) {
        // 11 May 2005
        String vE[] = Conversions.mBaudotBits2mBaudotStr(this.getName(), binCode.substring(s, f + 1), 6);

        String v = vE[0];
        // String e = "";

        String e = vE[1];

        if (e != null && e.length() > 0)
            e = "\nWARNING - SUSPECT NON-SPEC IN SPECIFIC BEACON NUMBER\n" + e;

        return new HexAttribute(AttributeType.SPECIFIC_BEACON_NUMBER, s, f, v, e);
    }

    // sdc This needs to be converted using a 5 place BAUDOT conv.
    HexAttribute aircraftOperator(String binCode, int s, int f) {
        // 11 May 2005
        String vE[] = Conversions.mBaudotBits2mBaudotStr(this.getName(), binCode.substring(s, f + 1), 5);
        String v = vE[0];

        String e = vE[1];

        if (e != null && e.length() > 0)
            e = "\nWARNING - SUSPECT NON-SPEC IN AIRCRAFT OPERATOR\n" + e;

        return new HexAttribute(AttributeType.AIRCRAFT_OPERATOR, s, f, v, e);
    }

    HexAttribute aircraftSerialNumber(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";
        return new HexAttribute(AttributeType.AIRCRAFT_SERIAL_NUMBER, s, f, v, e);
    }

    HexAttribute fixedBits(String binCode, int s, int f) {
        String v = "";
        String e = "";
        String code = binCode.substring(s, f + 1);
        if (code.equals(this.defaultFixedBits)) {
            v = "FIXED VALUE";
        } else {
            v = code + " (Non-Spec)";
//			String position = "b" + s + "-" + f;
            // e = "INVALID Spare. " + position + ": " + code;
        }

        return new HexAttribute(AttributeType.SPARE, s, f, v, e);
    }

    boolean positionalDataPresent(String binCode) {
        return binCode.charAt(110) == '1';
    }

    HexAttribute additionalDataFlag(String binCode, int s) { // b110
        String v = "";
        String e = "";
        if (this.positionalDataPresent(binCode)) {
            if (this.isLongMessage(binCode)) {
                v = "POSITION DATA";
            } else {
                v = "FIXED VALUE";
            }
        } else {
            if (this.isLongMessage(binCode)) {
                v = "NATIONAL USE";
            } else {
                v = binCode.charAt(s) + " (Non-Spec)";
            }
        }

        return new HexAttribute(AttributeType.ADDITIONAL_DATA_FLAG, s, v, e);
    }

    HexAttribute encodedPositionSource(String binCode, int s) {
        String v = "";
        String e = "";

        if (binCode.charAt(s) == '1') {
            v = "INTERNAL";
        } else {
            v = "EXTERNAL";
        }

        return new HexAttribute(AttributeType.ENCODED_POSITION_SOURCE, s, v, e);
    }

    // 121.5 MHz Homing b112 NO
    HexAttribute homing(String binCode, int s) { // b112
        String v = "";
        String e = "";

        if (binCode.charAt(s) == '1') {
            v = "YES";
        } else {
            v = "NO";
        }

        return new HexAttribute(AttributeType._121_5_MHZ_HOMING, s, v, e);
    }

    HexAttribute nationalUse(String binCode, int s, int f) {
        String v = binCode.substring(s, f + 1);
        String e = "";

        return new HexAttribute(AttributeType.NATIONAL_USE, s, f, v, e);
    }

    // Latitude 36 00 20S
    HexAttribute actualLatitude() {
        String e = "";
        return new HexAttribute(AttributeType.LATITUDE, latSeconds / 3600.0 + "", e);
    }

    // Longitude 115 07 36E
    HexAttribute actualLongitude() {
        String e = "";
        return new HexAttribute(AttributeType.LONGITUDE, lonSeconds / 3600.0 + "", e);
    }

    HexAttribute bch1(String binCodeOrig, int s, int f) {
        // System.out.println("Orig " + binCodeOrig);
        String binCode = binCodeOrig.substring(25, s);
        String binCode2 = binCodeOrig.substring(s, f + 1);

        String e = "";

        int lenStr = binCode.length();
        int a = 61 - lenStr;
        for (int i = 0; i < a; i++) {
            binCode = binCode + "0";
        }

        int b = 21;
        for (int i = 0; i < b; i++) {
            binCode = binCode + "0";
        }

        String bchCode = calcBCHCODE(binCode, "1001101101100111100011");

        if (!bchCode.equals(binCode2)) {
            e = "WARNING - ERROR IN FIRST PROTECTED FIELD\n";
            e = e + " BCH SHOULD BE " + bchCode + "\n";
            e = e + " BCH IS ...... " + binCode2;

            // 04/04/2005
            // Add more words for BCH errors
            e = "\n" + e + "\n\nNotes on Miscoded Beacons and BCH Errors:\n";
            e += "1. Section 4.2.5.3 of C/S T.002 - LUT Specifications\n";
            e += "2. Section III/B.1.1.3 of C/S A.001 - Data Distribution Plan\n\n";
        }

        return new HexAttribute(AttributeType.ERROR_CORRECTING_CODE, s, f, binCode2, e);
    }

    // 1/Nov/2005
    // Overloaded method, called by StandardLocation's subclasses and
    // NationalLocation's subclasses
    List<HexAttribute> bch1(List<HexAttribute> result, String binCode, HexAttribute hexId) {
        HexAttribute bch1 = bch1(binCode, 86, 106);

        String error = bch1.getError();
        if (error != null && error.length() > 0) {
            // To remove the hexId with the default value in b66-85 if not the
            // same
            String hexIdDefaultLoc = hexId.getValue();
            HexAttribute hexIdOrig = hexId(binCode, 26, 85);
            String hexIdOrigLoc = hexIdOrig.getValue();

            if (!hexIdDefaultLoc.equalsIgnoreCase(hexIdOrigLoc)) {
                int hexIdInd = result.indexOf(hexId);
                result.remove(hexId);
                result.add(hexIdInd, hexIdOrig);

                int noteInd = error.indexOf("\n\nNotes on Miscoded Beacons and BCH Errors:");
                String errPart1 = error.substring(0, noteInd);
                String errPart2 = error.substring(noteInd);

                error = errPart1 + "\n Hex Id with default location: " + hexId.getValue() + "\n" + errPart2;
                bch1 = new HexAttribute(bch1.desc, bch1.start, bch1.finish + "", error);
            }
        }

        result.add(bch1);
        return result;
    }

    HexAttribute bch2(String binCodeOrig, int s, int f) {
        String binCode = binCodeOrig.substring(107, s);
        String binCode2 = binCodeOrig.substring(s, f + 1);
        String e = "";

        int lenStr = binCode.length();
        int a = 26 - lenStr;
        for (int i = 0; i < a; i++) {
            binCode = binCode + "0";
        }

        int b = 12;
        for (int i = 0; i < b; i++) {
            binCode = binCode + "0";
        }

        String bchCode = calcBCHCODE(binCode, "1010100111001");

        if (!bchCode.equals(binCode2)) {
            e = "WARNING - ERROR IN SECOND PROTECTED FIELD\n";
            e = e + " BCH SHOULD BE " + bchCode + "\n";
            e = e + " BCH IS ...... " + binCode2;
        }

        return new HexAttribute(AttributeType.ERROR_CORRECTING_CODE, s, f, binCode2, e);
    }

    private static String calcBCHCODE(String bitCode, String generatorPolynomial) {

        int b = generatorPolynomial.length();
        String result = bitCode.substring(0, b);
        result = removeLeadingZeros(result);

        // Short protocols will have any leading 0 removed from the
        // above call to removeLeadingZeros. So we should even up
        // the ledger by removing the leading '0' from bitCode.
        if (bitCode.charAt(0) == '0') {
            bitCode = bitCode.substring(1, bitCode.length());
        }

        for (int i = b - 1; i < bitCode.length(); i++) {
            char c = bitCode.charAt(i);

            if (result.length() < b) {
                result += c;
            }
            if (result.length() == b) {
                result = xor(result, generatorPolynomial);
            }
        }

        while (result.length() < b - 1) {
            result = "0" + result;
        }

        return result;
    }

    private static String removeLeadingZeros(String str) {
        while (str.length() > 0 && str.substring(0, 1).equals("0")) {
            str = str.substring(1, str.length());
        }
        return str;
    }

    // do a bitwise XOR of G and R to form new R
    private static String xor(String bitStrA, String bitStrB) {
        String result = "";
        for (int i = 0; i < bitStrA.length(); i++) {
            boolean bA = bitStrA.charAt(i) == '1';
            boolean bB = bitStrB.charAt(i) == '1';
            if (bA == bB) {
                result += "0";
            } else {
                result += "1";
            }
        }
        return removeLeadingZeros(result);
    }

    // don't make this a singleton because the protocol objects are not safe for
    // reuse!
    public static List<BeaconProtocol> createBeaconProtocols() {
        List<BeaconProtocol> list = new ArrayList<BeaconProtocol>(35);

        // All National Protocols
        list.add(new NationalLocationAviation());
        list.add(new NationalLocationMaritime());
        list.add(new NationalLocationPersonal());
        list.add(new NationalLocationSpare());
        list.add(new NationalLocationTest());

        // New Return Link Service Protocol
        list.add(new ReturnLinkServiceLocation());

        // All Standard Protocols
        list.add(new StandardLocationAircraftAddress());
        list.add(new StandardLocationAircraftOperator());
        list.add(new StandardLocationELT());
        list.add(new StandardLocationEpirb());
        list.add(new StandardLocationPLB());
        list.add(new StandardLocationShipMMSI());
        list.add(new StandardLocationShipSecurityAlertSystem());
        list.add(new LocationReserved());
        list.add(new LocationSpare());
        list.add(new StandardLocationTest());

        // All User Protocols
        list.add(new UserAviation());
        list.add(new UserMaritime());
        list.add(new UserNational());
        list.add(new UserOrbitography());
        list.add(new UserRadioCallsign());
        list.add(new UserSerialAircraftAddress());
        list.add(new UserSerialAircraftOperator());
        list.add(new UserSerialAviation());
        list.add(new UserSerialMaritimeFloatFree());
        list.add(new UserSerialMaritimeNonFloatFree());
        list.add(new UserSerialPersonal());
        list.add(new UserSerialSpare());
        list.add(new UserSerialSpare2());
        list.add(new UserSpare());
        list.add(new UserTest());

        // This is the default it always returns true
        list.add(new Unknown());
        return list;
    }

}
