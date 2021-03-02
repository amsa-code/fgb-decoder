package com.amsa.Hexdecode;

import java.util.Vector;

public class UserAviation extends User {

	public UserAviation() {
		protocolName = "Aviation";
		userProtocolCode = "001";
	}

	@Override
    public Vector<HexAttribute> decodeSearch(String hexStr) {
		String binCode = Conversions.hexToBinary(hexStr);
		Vector<HexAttribute> result = new Vector<HexAttribute>();

		result.add(this.hexId(binCode, 26, 85));
		result.add(this.aircraftRegistrationMarking(binCode, 40, 81));

		return result;
	}

	@Override
    public Vector<HexAttribute> decode(String hexStr) {

		String binCode = Conversions.hexToBinary(hexStr);

		Vector<HexAttribute> result = new Vector<HexAttribute>();

		result.add(this.messageType(binCode, 25, 26));
		result.add(this.hexData(hexStr, 25, (binCode.length() - 1)));
		result.add(this.hexId(binCode, 26, 85));
		result.add(this.countryCode(binCode, 27, 36));
		result.add(this.protocolType(binCode, 37, 39));

		result.add(this.aircraftRegistrationMarking(binCode, 40, 81));
		result.add(this.specificELTIdentifier(binCode, 82, 83));
		result.add(this.auxRadioLocating(binCode, 84, 85));

		if (hexStr.length() > 15) {
			result.add(this.bch1(binCode, 86, 106));
			if (this.isLongMessage(binCode)) {
				result.add(this.encodedPositionSource(binCode, 111));
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
					result.add(this.nationalUse(binCode, 113, 144));
				}
			}
			// 14/03/2005
			else {
				result = this.nonNationalUse(result, binCode);
			}
		}

		return result;
	}

	public HexAttribute aircraftRegistrationMarking(String binCode, int s, int f) {
		// 11 May 2005
		String vE[] = Conversions.mBaudotBits2mBaudotStr(this.getName(),
				binCode.substring(s, f + 1), 6);
		// 24 June 2005
		// Don't use double quote again on CDP's agreement
		// String v = "\"" + vE[0] + "\"";
		String v = vE[0];

		// String e = "";
		String e = vE[1];

		if (e != null && e.length() > 0)
			e = "\nWARNING - SUSPECT NON-SPEC IN AIRCRAFT REG. MARKING\n" + e;

		return new HexAttribute("Aircraft Reg. Marking", s, f, v, e);
	}

	// This overidding method will be called by User.java
	@Override
    public Vector<HexAttribute> allEmergencyCodes(Vector<HexAttribute> result, String binCode) {
		result = Common.nonMaritimeEmergencyCodes(result, binCode);

		return result;
	}
}
