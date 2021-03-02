package com.amsa.fgb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Contains methods to decode hex strings
 * 
 * @author smw01
 * 
 */
public final class Hexdecode {

	// This is the decode that is used by the Incident Navigator SAR Search
	public static String decodeSearch(String hexStr, String formatter) {

		Vector<HexAttribute> r = new Vector<HexAttribute>(0);

		hexStr = hexStr.trim();

		int hexLen = hexStr.length();
		if (!(hexLen == 15 || hexLen == 30)) {
			r.add(new HexAttribute("", "",
					"HEX STRING MUST BE 15 OR 30 CHARACTERS IN LENGTH"));
		} else {

			List<BeaconProtocol> beaconFactory = createBeaconProtocols();

			// Convert the Hex String into Binary Code
			String binCode = Conversions.hexToBinary(hexStr);

			int noOfBeacons = beaconFactory.size();

			for (int i = 0; i < noOfBeacons; i++) {
				BeaconProtocol proto = beaconFactory
						.get(i);
				if (proto.canDecode(binCode)) {
					r = proto.decodeSearch(hexStr);
					break;
				}
			}
		}
		DecodeFilter f;

		formatter = formatter.toUpperCase();

		if (formatter.equals("HTML")) {

			f = DecodeAsHTML.INSTANCE;

		} else {

			if (formatter.equals("XML")) {

				f = DecodeAsXML.INSTANCE;

			} else {

				if (formatter.equals("XML2")) {

					f = DecodeAsXML2.INSTANCE;

				} else {

					f = DecodeAsText.INSTANCE;

				}

			}

		}

		String output = f.getData(r);

		return output;
	}
	
	public static Map<String, HexAttribute> decodeToMap(String hexStr){
	    Map<String,HexAttribute> map = new HashMap<String,HexAttribute>();
	    for (HexAttribute h: getHexAttributes(hexStr)) {
	        map.put(h.desc,h);
	    }
	    return map;
	}

	// This is the method that is used to decode the entire Hex Str.
	public static String decode(String hexStr, String formatter) {
		List<HexAttribute> r = getHexAttributes(hexStr);
		
		DecodeFilter f;

		formatter = formatter.toUpperCase();

		if (formatter.equals("HTML")) {

			f = DecodeAsHTML.INSTANCE;

		} else {

			if (formatter.equals("XML")) {

				f = DecodeAsXML.INSTANCE;

			} else {

				if (formatter.equals("XML2")) {

					f = DecodeAsXML2.INSTANCE;

				} else {

					f = DecodeAsText.INSTANCE;

				}

			}

		}

		String output = f.getData(r);

		return output;
	}

    private static List<HexAttribute> getHexAttributes(String hexStr) {
        List<HexAttribute> r = new ArrayList<HexAttribute>(0);

		hexStr = hexStr.trim();

		int hexLen = hexStr.length();
		if (!(hexLen == 15 || hexLen == 30)) {
			r.add(new HexAttribute("", "",
					"HEX STRING MUST BE 15 OR 30 CHARACTERS IN LENGTH"));
		} else {

		    List<BeaconProtocol> beaconFactory = createBeaconProtocols();

			// Convert the Hex String into Binary Code
			String binCode = Conversions.hexToBinary(hexStr);

			int noOfBeacons = beaconFactory.size();

			for (int i = 0; i < noOfBeacons; i++) {
				BeaconProtocol proto = beaconFactory.get(i);
				if (proto.canDecode(binCode)) {
					r = proto.decode(hexStr);
					break;
				}
			}
		}
        return r;
    }

    private static List<BeaconProtocol> createBeaconProtocols() {
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

	public static void main(String[] args) {
		String s = "";
		String f = "";
		try {
			// s = args[0];

			// The last one should always be the formatter!
			for (int i = 0; i < args.length - 1; i++)
				s += args[i];
		} catch (Exception e) {
			System.out.println("Hexdecode: Insufficient arguments");
			System.out
					.println("Usage: java Hexdecode <HexStr> [<HTML|TEXT|XML>]");
			return;
		}
		try {
			// f = args[1];
			// The last one should always be the formatter!
			f = args[args.length - 1];
		} catch (Exception e) {
			f = "TEXT";
		}
		String output = Hexdecode.decode(s, f);

		System.out.println(output);
	}
}
