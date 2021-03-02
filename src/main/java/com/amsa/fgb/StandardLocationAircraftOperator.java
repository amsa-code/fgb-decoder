package com.amsa.fgb;

import java.util.Vector;

class StandardLocationAircraftOperator extends StandardLocation {

    public StandardLocationAircraftOperator()
    {
	stdProtocolCode = "0101";
	protocolName = "Aircraft Operator";
    }

    @Override
    public Vector<HexAttribute> decode (String hexStr) 
    {
	String binCode = Conversions.hexToBinary(hexStr);
	Vector<HexAttribute> result = new Vector<HexAttribute>();

	result.add(this.messageType(binCode, 25, 26));
       	result.add(this.hexData(hexStr, 25, (binCode.length()-1)));
	HexAttribute hexId = this.hexIdWithDefaultLocation(binCode, 26, 65);
       	result.add(hexId);
	result.add(this.countryCode(binCode, 27, 36));
       	result.add(this.protocolType(binCode, 37, 40));

	result.add(this.aircraftOperator(binCode, 41, 55));
	result.add(this.aircraftSerialNumber(binCode, 56, 64));

	result.add(this.coarsePosition(binCode, 65, 85));

	if (hexStr.length() > 15) 
	{
	    result = this.bch1(result, binCode, hexId);
	    result.add(this.fixedBits(binCode, 107, 110));

	    if (this.isLongMessage(binCode)) 
	    {
		result.add(this.encodedPositionSource(binCode, 111));
		result.add(this.homing(binCode, 112));
		if (this.defaultFFFFFFFF(hexStr)) 
		{
		    result.add(this.longMessage(binCode, 113, 144));
		} 
		else 
		{
		    if (this.default00000000(hexStr)) 
		    {
			result.add(this.longMessage(binCode, 113, 144));
		    }
		    else 
		    {
			result.add(offsetPosition(binCode, 113, 132));
			result.add(this.bch2(binCode, 133, 144));
		    }
		}
	    } 
	    else 
	    {
		result.add(this.encodedPositionSource(binCode, 111));
		result.add(this.homing(binCode, 112));
	    }
	    if (this.actualLatLong) 
	    {
		result.add(actualLatitude());
		result.add(actualLongitude());
	    }
	}

	return result;
    }
}
