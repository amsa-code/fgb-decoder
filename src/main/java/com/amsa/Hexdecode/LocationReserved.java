package com.amsa.Hexdecode;

import java.util.Vector;

class LocationReserved extends BeaconProtocol
{
    public String stdProtocolCode; 

    public LocationReserved()
    {
	beaconTypeCode.add("00");
        beaconTypeCode.add("10");

	// 16 May 2005
	// This is for 15-char Hex string where bit25 is unknown since it starts with bit26
	beaconTypeCode.add("?0");

	stdProtocolCode = "000";
	protocolName = "Reserved (orbitography)";
    }

    @Override
    public String getName() {
	return protocolName;
    }

    @Override
    public Vector<HexAttribute> decode (String hexStr)
    {
	   String binCode = Conversions.hexToBinary(hexStr);

	   Vector<HexAttribute> result = new Vector<HexAttribute>();

	   result.add(this.messageType(binCode, 25, 26));
	   result.add(this.hexData(hexStr, 25, (binCode.length()-1)));
	   result.add(this.hexId(binCode, 26, 85));
	   result.add(this.countryCode(binCode, 27, 36));
	   
	   // b40 doesn't matter for this reserved
	   result.add(this.protocolType(binCode, 37, 40));
	   
	   // 09 May 2005, based on CDP's guidance
	   // Providing the binary codes from bit41 to bit 85 inclusively
	   result.add(new HexAttribute("ID+POSN", 41, 85, binCode.substring(41,86), ""));

	   if (hexStr.length() > 15) 
	   {
	       // Providing the BCH1 decoding
	       result.add(this.bch1(binCode, 86, 106));
	       // Providing the binary codes from bit107 to bit 112
	       //result.add(new HexAttribute("Supplementary Data", 107, 112, binCode.substring(107,113), ""));

	       // 24 May 2005
	       // CDP's latest Guidance
	       if (this.isLongMessage(binCode)) 
	       {
		   result.add(new HexAttribute("PDF-2", 107, 132, binCode.substring(113,133), ""));
		   result.add(new HexAttribute("BCH-2", 133, 144, binCode.substring(133,145), ""));
	       }
	       else
		   result.add(new HexAttribute("Supplementary Data", 107, 112, binCode.substring(107,113), ""));
		   
	   }

	   return result;
    }
    
    // Override the method of "canCode" (b37-39). 
    // b40 doesn't matter in this Reserved (orbitography) protocol
    @Override
    public boolean canDecode (String binCode)
    {
	String protocol = binCode.substring(25, 27);
	String name = this.getName();
	
	//System.out.println("Trying " + name);

	if (beaconTypeCode.contains(protocol)) 
	{
	    String protocolCode = binCode.substring(37, 40);
	    return protocolCode.equals(this.stdProtocolCode);
        }

	// else
	return false;
    }

    // Overriden messageType
    public HexAttribute messageType(String binCode, int s, int f)
    {
	String v = "Location"; // this is where to oerride
	String e = "";

	// 16 May 2005
	v = this.getMsgTypeDesc(v, binCode);

	return new HexAttribute("Message Type", s, f, v, e);
    }
}
