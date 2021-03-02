package com.amsa.Hexdecode;

import java.util.Vector;

public abstract class BeaconProtocol {

	public boolean isUS = false;

	public Vector<String> beaconTypeCode;
	public String protocolName;
	// public String messageTypeDesc;
	// public String countryCode;

	public boolean actualLatLong;
	public double latSeconds;
	public double lonSeconds;

	public String defaultFixedBits;

	public BeaconProtocol() {
		beaconTypeCode = new Vector<String>(2);
		this.actualLatLong = false;
		this.latSeconds = 0;
		this.lonSeconds = 0;
	}

	// Virtual Method.
	// This method should be overwritten by sub-classes.
	// Otherwise this method is respond and will NOT be
	// able to decode because it always returns false.
	public boolean canDecode(String binCode) {
		return false;
	}

	public Vector<HexAttribute> decodeSearch(String hexStr) {
		String binCode = Conversions.hexToBinary(hexStr);
		Vector<HexAttribute> result = new Vector<HexAttribute>();

		result.add(this.hexId(binCode, 26, 85));

		return result;
	}

	// This method is also overwritten by subclasses but
	// all sub-classes should call super.decode(binStr)
	// as this method will decode those bits that are
	// common to ALL protocols.
	public Vector<HexAttribute> decode(String hexStr) {
		return new Vector<HexAttribute>(0);
		// "ERROR - decode() called from BeaconProtocol";
	}

	public String getName() {
		return "";
	}

	public boolean isLongMessage(String binCode) {
		String code = binCode.substring(25, 27);
		Vector<String> longCodes = new Vector<String>(2);
		longCodes.add("10");
		longCodes.add("11");

		return longCodes.contains(code);
	}

	// 16 May 2005
	public boolean isShortMessage(String binCode) {
		String code = binCode.substring(25, 27);
		Vector<String> longCodes = new Vector<String>(2);
		longCodes.add("00");
		longCodes.add("01");

		return longCodes.contains(code);
	}

	// 16 May 2005
	public String getMsgTypeDesc(String v, String binCode) {
		if (this.isLongMessage(binCode))
			v = v + " (Long)";
		else if (this.isShortMessage(binCode))
			v = v + " (Short)";
		else
			v = v + " (Format - Unknown)";

		return v;
	}

	public boolean defaultFFFFFFFF(String hexStr) {
		int len = hexStr.length();
		String code = hexStr.substring(len - 8, len);

		return code.equals("FFFFFFFF");
	}

	public HexAttribute longMessage(String binCode, int s, int f) {
		String v = "DEFAULT";
		String e = "";

		return new HexAttribute("Long Message", s, f, v, e);
	}

	public boolean default00000000(String hexStr) {
		int len = hexStr.length();
		String code = hexStr.substring(len - 8, len);

		return code.equals("00000000");
	}

	public HexAttribute protocolType(String binCode, int s, int f) {
		String name = this.getName();
		String e = "";

		return new HexAttribute("Protocol Type", s, f, name, e);
	}
	
	public HexAttribute rlsTacNumber(String binCode, int s, int f) {
	        char prefix = rlsTacNumberPrefix(binCode, s);
	        String v = binCode.substring(s+2, f + 1);
	        String last3Digits = Conversions.zeroPadFromLeft(Integer.toString(Conversions.binaryToDecimal(v)), 3);
	        String result = prefix + last3Digits;
	        String e = "";
	        return new HexAttribute("RLS TAC Number", s, f, result, e);
    }

    private char rlsTacNumberPrefix(String binCode, int s) {
        final String v = binCode.substring(s, s + 2);

        if (v.length() == 2) {
            if (v.equals("00")) {
                return '2';
            } else if (v.equals("01")) {
                return '1';
            } else if (v.equals("10")) {
                return '3';
            } else if (v.equals("11")) {
                return 'T';
            } else {
                return '?';
            }
        } else {
            return '?';
        }
    }
	
	public HexAttribute rlsId(String binCode, int s, int f) {
        String v = binCode.substring(s, f + 1);
        String e = "";
        int result = Conversions.binaryToDecimal(v);
        return new HexAttribute("RLS ID", s, f, result, e);
    }

	public HexAttribute hexData(String hexStr, int s, int f) {
		String e = "";

		// return new HexAttribute("Hex Data", s, f, hexStr, e);

		// 16 May 2005
		if ((hexStr.trim()).length() != 30)
			return new HexAttribute("Hex Data", 25, 144, "Unknown", e);
		else
			return new HexAttribute("Hex Data", s, f, hexStr, e);
	}

	public HexAttribute hexId(String binCode, int s, int f) { // b26-85
		String binHexId = binCode.substring(s, f + 1);
		String v = Conversions.binaryToHex(binHexId);
		String e = "";

		return new HexAttribute("Hex Id", s, f, v, e);
	}

	public int getCountryCode(String binCode, int s, int f) {
		String binCC = binCode.substring(s, f + 1);

		return Conversions.binaryToDecimal(binCC);
	}

	public HexAttribute countryCode(String binCode, int s, int f) {
		int cc = this.getCountryCode(binCode, s, f);
		String e = "";
		String v = cc + "";

		// Is this a US beacon
		Vector<String> UScodes = new Vector<String>(0);
		UScodes.add("338");
		UScodes.add("366");
		UScodes.add("367");
		UScodes.add("368");
		UScodes.add("369");

		this.isUS = UScodes.contains(v);
		return new HexAttribute("Country Code", s, f, v, e);
	}

	// Beacon Serial Number</TD><TD>b51-64</TD><TD>999</TD></TR>
	public HexAttribute beaconSerialNumber(String binCode, int s, int f) {
		int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
		String e = "";

		return new HexAttribute("Beacon Serial Number", s, f, v, e);
	}

	public HexAttribute aircraft24BitAddress(String binCode, int s, int f) {
		String code = binCode.substring(s, f + 1);
		String e = "";
		String hex = Conversions.binaryToHex(code);
		String oct = removeLeadingZeros(Conversions.binaryToOctal(code));
		String v = "HEX: " + hex + " OCTAL: " + oct;

		HexAttribute h = this.aircraftCallSign(binCode, s, f);
		String result = h.getValue();
		if (result.length() > 0) {
			// 13 May 2005
			// Make the output align to the rest output
			v += "\n                                           Aircraft Reg. Marking: "
					+ result;
		}

		String countryReg = Conversions.getAircraftCountryOfReg(code);
		if (countryReg.length() > 0) {
			v += "\n                                           Country: "
					+ countryReg;
		}

		return new HexAttribute("Aircraft 24 bit address", s, f, v, e);
	}

	public HexAttribute aircraft24BitAddressBinary(String binCode, int s, int f) {
		String e = "";
		String v = binCode.substring(s, f + 1);

		return new HexAttribute("Aircraft 24 bit address Binary", s, f, v, e);
	}

	public HexAttribute aircraftCallSign(String binCode, int s, int f) {
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

		return new HexAttribute("Aircraft CallSign", s, f, v, e);
	}

	public HexAttribute specificBeaconNumber(String binCode, int s, int f) {
		// 11 May 2005
		String vE[] = Conversions.mBaudotBits2mBaudotStr(this.getName(),
				binCode.substring(s, f + 1), 6);

		String v = vE[0];
		// String e = "";

		String e = vE[1];

		if (e != null && e.length() > 0)
			e = "\nWARNING - SUSPECT NON-SPEC IN SPECIFIC BEACON NUMBER\n" + e;

		return new HexAttribute("Specific Beacon Number", s, f, v, e);
	}

	// sdc This needs to be converted using a 5 place BAUDOT conv.
	public HexAttribute aircraftOperator(String binCode, int s, int f) {
		// 11 May 2005
		String vE[] = Conversions.mBaudotBits2mBaudotStr(this.getName(),
				binCode.substring(s, f + 1), 5);
		String v = vE[0];

		String e = vE[1];

		if (e != null && e.length() > 0)
			e = "\nWARNING - SUSPECT NON-SPEC IN AIRCRAFT OPERATOR\n" + e;

		return new HexAttribute("Aircraft Operator", s, f, v, e);
	}

	public HexAttribute aircraftSerialNumber(String binCode, int s, int f) {
		int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
		String e = "";
		return new HexAttribute("Aircraft Serial Number", s, f, v, e);
	}

	public HexAttribute fixedBits(String binCode, int s, int f) {
		String v = "";
		String e = "";
		String code = binCode.substring(s, f + 1);
		if (code.equals(this.defaultFixedBits)) {
			v = "FIXED VALUE";
		} else {
			v = code + " (Non-Spec)";
			String position = "b" + s + "-" + f;
			// e = "INVALID Spare. " + position + ": " + code;
		}

		return new HexAttribute("Spare", s, f, v, e);
	}
	
	public boolean positionalDataPresent(String binCode) {
		return binCode.charAt(110) == '1';
	}

	public HexAttribute additionalDataFlag(String binCode, int s) { // b110
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

		return new HexAttribute("Additional Data Flag", s, v, e);
	}

	public HexAttribute encodedPositionSource(String binCode, int s) {
		String v = "";
		String e = "";

		if (binCode.charAt(s) == '1') {
			v = "INTERNAL";
		} else {
			v = "EXTERNAL";
		}

		return new HexAttribute("Encoded Position Source", s, v, e);
	}

	// 121.5 MHz Homing b112 NO
	public HexAttribute homing(String binCode, int s) { // b112
		String v = "";
		String e = "";

		if (binCode.charAt(s) == '1') {
			v = "YES";
		} else {
			v = "NO";
		}

		return new HexAttribute("121.5 MHz Homing", s, v, e);
	}

	public HexAttribute nationalUse(String binCode, int s, int f) {
		String v = binCode.substring(s, f + 1);
		String e = "";

		return new HexAttribute("National Use", s, f, v, e);
	}

	// This method is called by ReturnLinkServiceLocation.java
	public HexAttribute rlsData(String binCode, int s, int f) {
		String v = binCode.substring(s, f + 1);
		String e = "";

		if (v.length() == 6) {
			if (v.equals("100000")) {
				v = "RLM-Request Type-1 only";
			} else if (v.equals("010000")) {
				v = "RLM-Request Type-2 only";
			} else if (v.equals("110000")) {
				v = "RLM-Request Type-1 + Type-2 (default)";
			} else {
				v += " (Non-Spec)";
			}
		} else {
			v += " (Non-Spec)";
		}

		return new HexAttribute("RLS Data", s, f, v, e);
	}

	// Latitude 36 00 20S
	public HexAttribute actualLatitude() {
		String e = "";
		String direction = "N";
		if (this.latSeconds < 0) {
			direction = "S";
			this.latSeconds = this.latSeconds * -1;
		}
		int deg = (int) (this.latSeconds / (60 * 60));
		int remain = (int) (this.latSeconds - (deg * 60 * 60));
		String degStr = Conversions.zeroPadFromLeft(deg + "", 2);
		int min = remain / 60;
		String minStr = Conversions.zeroPadFromLeft(min + "", 2);
		int sec = remain % 60;
		String secStr = Conversions.zeroPadFromLeft(sec + "", 2);
		String v = degStr + " " + minStr + " " + secStr + direction;

		return new HexAttribute("Latitude", v, e);
	}

	// Longitude 115 07 36E
	public HexAttribute actualLongitude() {
		String e = "";
		String direction = "E";
		if (this.lonSeconds < 0) {
			direction = "W";
			this.lonSeconds = this.lonSeconds * -1;
		}
		int deg = (int) (this.lonSeconds / (60 * 60));
		int remain = (int) (this.lonSeconds - (deg * 60 * 60));
		String degStr = Conversions.zeroPadFromLeft(deg + "", 3);
		int min = remain / 60;
		String minStr = Conversions.zeroPadFromLeft(min + "", 2);
		int sec = remain % 60;
		String secStr = Conversions.zeroPadFromLeft(sec + "", 2);

		String v = degStr + " " + minStr + " " + secStr + direction;

		return new HexAttribute("Longitude", v, e);
	}

	public HexAttribute bch1(String binCodeOrig, int s, int f) {
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

		return new HexAttribute("Error Correcting Code", s, f, binCode2, e);
	}

	// 1/Nov/2005
	// Overloaded method, called by StandardLocation's subclasses and
	// NationalLocation's subclasses
	public Vector<HexAttribute> bch1(Vector<HexAttribute> result, String binCode, HexAttribute hexId) {
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

				int noteInd = error
						.indexOf("\n\nNotes on Miscoded Beacons and BCH Errors:");
				String errPart1 = error.substring(0, noteInd);
				String errPart2 = error.substring(noteInd);

				error = errPart1 + "\n Hex Id with default location: "
						+ hexId.getValue() + "\n" + errPart2;
				bch1.setError(error);
			}
		}

		result.add(bch1);
		return result;
	}

	public HexAttribute bch2(String binCodeOrig, int s, int f) {
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

		return new HexAttribute("Error Correcting Code", s, f, binCode2, e);
	}

	public String calcBCHCODE(String bitCode, String generatorPolynomial) {

		int b = generatorPolynomial.length();
		String result = bitCode.substring(0, b);
		result = this.removeLeadingZeros(result);

		// Short protocols will have any leading 0 removed from the
		// above call to removeLeadingZeros. So we should even up
		// the ledger by removing the leading '0' from bitCode.
		if (bitCode.charAt(0) == '0') {
			bitCode = bitCode.substring(1, bitCode.length());
		}

		int ptr = b;

		for (int i = b - 1; i < bitCode.length(); i++) {
			char c = bitCode.charAt(i);

			if (result.length() < b) {
				ptr++;
				result += c;
			}
			if (result.length() == b) {
				result = this.xor(result, generatorPolynomial);
			} else {
				// System.out.println(result.length() + " " + ptr);
			}
		}

		while (result.length() < b - 1) {
			result = "0" + result;
		}

		return result;
	}

	public String removeLeadingZeros(String str) {
		while (str.length() > 0 && str.substring(0, 1).equals("0")) {
			str = str.substring(1, str.length());
		}

		return str;
	}

	// do a bitwise XOR of G and R to form new R
	public String xor(String bitStrA, String bitStrB) {
		String result = "";
		char bitA;
		char bitB;

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

}
