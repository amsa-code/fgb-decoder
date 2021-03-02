package com.amsa.fgb;

import java.util.ArrayList;
import java.util.Vector;

public class UserRadioCallsign extends User {

    public UserRadioCallsign () {
	protocolName = "Radio Call Sign";
	userProtocolCode = "110";
    }

    @Override
    public Vector<HexAttribute> decodeSearch (String hexStr) {
	String binCode = Conversions.hexToBinary(hexStr);
	Vector<HexAttribute> result = new Vector<HexAttribute>();

       	result.add(this.hexId(binCode, 26, 85));
	result.add(this.radioCallsign(binCode, 40, 75));
	
	return result;
    }

    @Override
    public Vector<HexAttribute> decode (String hexStr) {

	String binCode = Conversions.hexToBinary(hexStr);

	Vector<HexAttribute> result = new Vector<HexAttribute>();

	result.add(this.messageType(binCode, 25, 26));
       	result.add(this.hexData(hexStr, 25, (binCode.length()-1)));
       	result.add(this.hexId(binCode, 26, 85));
	result.add(this.countryCode(binCode, 27, 36));
       	result.add(this.protocolType(binCode, 37, 39));

	result.add(this.radioCallsign(binCode, 40, 75));
	result.add(this.specificBeaconNumber(binCode, 76, 81));

	result.add(this.spare(binCode, 82, 83));
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
	    else
	    {
		result = this.nonNationalUse(result, binCode);
	    }
	}

	return result;
    }

    public HexAttribute radioCallsign(String binCode, int s, int f) 
    {
	// 11 May 2005
	// Passed back the String[]
	String vE[] = Conversions.mBaudotBits2mBaudotStr(this.getName(), binCode.substring(s, s+24), 6);
	//String v = Conversions.mBaudotBits2mBaudotStr(binCode.substring(s, s+24), 6);
	//String h = Conversions.binaryToHex(binCode.substring(s+24, f+1));

	String v = vE[0];
	String e = vE[1];
	int count = Integer.parseInt(vE[2]);

	if (e != null && e.length() > 0)
	    e = "\nWARNING - SUSPECT NON-SPEC IN RADIO CALL SIGN\n" + e;

	//String h = Conversions.binaryToHex(binCode.substring(s+24, f+1));
	int digits[] = new int[3];
	digits[0] = Conversions.binaryToDecimal(binCode.substring(s+24, s+28));
	digits[1]  = Conversions.binaryToDecimal(binCode.substring(s+28, s+32));
        digits[2] = Conversions.binaryToDecimal(binCode.substring(s+32, f+1));

	String h = "";
	ArrayList<String> e2 = new ArrayList<String>();

	for (int i=0; i<3; i++)
	{
	    // only 0-9 are allowed, SPACE (1010, ie, 10) is allowed only when it is trailing
	    if(digits[i] > 9)
	    {
		if (digits[i] == 10)
		{
		    h += "*";

		    //e += "?" + ++count + " = " + Conversions.decimalToBinary(digits[i]) + " = Non-Spec\n";

		    e2.add(Conversions.decimalToBinary(digits[i]) + " = Non-Spec\n");
		}
		else
		{
		    h += "?";
		    //e += "?" + ++count + " = " + Conversions.decimalToBinary(digits[i]) + " = Non-Spec\n";

		    e2.add(Conversions.decimalToBinary(digits[i]) + " = Non-Spec\n");

		    //System.out.println("digits[" + i + "]=" + digits[i]);
		}
	    }	  
	    else
	    {
		h += digits[i] + "";

		// This is for syn only
		e2.add(Conversions.decimalToBinary(digits[i]) + " = Spec\n");
	    }
	}

	// Replace "*" with " " at the end, eg, "*9*" to "*9 "
	int index = h.length()-1;
	for (; index>=0; index--)
	{
	    if (h.charAt(index) == '*')
		continue;
	    else
		break;
	}

	//System.out.println("index=" + index);

	for (int i=index+1; i>=0 && i<h.length(); i++)
	{
	    if (h.charAt(i) == '*' )
		h = h.substring(0, i) + " " + h.substring(i+1);
	}

	// Replace all rest * to ?, eg "*9 " to "?9 "
	h = h.replace('*', '?');
	// Append the relevant elements in e2 to e
	for (int i=0; i<h.length(); i++)
	    if (h.charAt(i) == '?')
		e += "?" + ++count + " = " + (String)e2.get(i);
	   
	// 24 June 2005, double quote is not used any more
	//String vH = "\"" + v + h + "\"";
	String vH = v + h;

	return new HexAttribute("Radio Callsign", s, f, vH, e);
    }

    // This overidding method will be called by User.java
    @Override
    public Vector<HexAttribute> allEmergencyCodes (Vector<HexAttribute> result, String binCode)
    {
	result = Common.maritimeEmergencyCodes (result, binCode);

	return result;
    }

}

