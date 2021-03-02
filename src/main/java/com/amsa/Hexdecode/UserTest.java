package com.amsa.Hexdecode;

import java.util.Vector;

public class UserTest extends User {

    public UserTest () {
	protocolName = "Test";
	userProtocolCode = "111";
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

	result.add(this.nationalUse(binCode, 40, 85));

	if (hexStr.length() > 15)
	{
	    result.add(this.bch1(binCode, 86, 106));
	    if (this.isLongMessage(binCode))
	    {
		result.add(this.nationalUse(binCode, 107, 132));
		result.add(this.bch2(binCode, 133, 144)); 	
	    }
	    else
	    {
		 result.add(this.nationalUse(binCode, 107, 112));
	    }
	}

	return result;
    }

}

