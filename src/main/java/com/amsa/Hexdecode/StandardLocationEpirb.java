package com.amsa.Hexdecode;

import java.util.Vector;

class StandardLocationEpirb extends StandardLocation {

    public StandardLocationEpirb()
    {
	stdProtocolCode = "0110";
	protocolName = "EPIRB - Serial";
    }

    @Override
    public Vector<HexAttribute> decode (String hexStr) {
	String binCode = Conversions.hexToBinary(hexStr);
	Vector<HexAttribute> result = new Vector<HexAttribute>();

	result.add(this.messageType(binCode, 25, 26));
       	result.add(this.hexData(hexStr, 25, (binCode.length()-1)));
       	//result.add(this.hexId(binCode, 26, 65));
	HexAttribute hexId = this.hexIdWithDefaultLocation(binCode, 26, 65);
	result.add(hexId);
	result.add(this.countryCode(binCode, 27, 36));
       	result.add(this.protocolType(binCode, 37, 40));

	result.add(this.cospatSarsatTypeApp(binCode, 41, 50));
	result.add(this.beaconSerialNumber(binCode, 51, 64));

	result.add(this.coarsePosition(binCode, 65, 85));

	if (hexStr.length() > 15) 
	{
	    //result.add(this.bch1(binCode, 86, 106));
	    result = this.bch1(result, binCode, hexId);

	    result.add(this.fixedBits(binCode, 107, 110));

	    if (this.isLongMessage(binCode)) {
		result.add(this.encodedPositionSource(binCode, 111));
		result.add(this.homing(binCode, 112));
		if (this.defaultFFFFFFFF(hexStr)) {
		    result.add(this.longMessage(binCode, 113, 144));
		} else {
		    if (this.default00000000(hexStr)) {
			result.add(this.longMessage(binCode, 113, 144));
		    } else {
			result.add(offsetPosition(binCode, 113, 132));
			result.add(this.bch2(binCode, 133, 144));
		    }
		}
	    } else {
		result.add(this.encodedPositionSource(binCode, 111));
		result.add(this.homing(binCode, 112));
	    }
	    if (this.actualLatLong) {
		result.add(actualLatitude());
		result.add(actualLongitude());
	    }
	}

	return result;
    }

}
