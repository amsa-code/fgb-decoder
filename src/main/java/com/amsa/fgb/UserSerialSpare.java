package com.amsa.fgb;

import java.util.ArrayList;
import java.util.List;

class UserSerialSpare extends UserSerial
{
    public UserSerialSpare()
    {
	   serialBeaconType = "Spare";
	   // b40-42, the other one for spare is "111" (See UserSpare2.java)
	  serialCode = "101";
    }

    @Override
    public List<HexAttribute> decode (String hexStr)
    {
	   String binCode = Conversions.hexToBinary(hexStr);
	   List<HexAttribute> result = new ArrayList<HexAttribute>();

	   result.add(this.messageType(binCode, 25, 26));
	   result.add(this.hexData(hexStr, 25, (binCode.length()-1)));
	   result.add(this.hexId(binCode, 26, 85));
	   result.add(this.countryCode(binCode, 27, 36));
	   result.add(this.protocolType(binCode, 37, 39));
	   result.add(this.beaconType(binCode, 40, 42));

	   // 13 May 2005
	   // Need to continue to decode, CDP's guidance
	   result.add(this.cospasSarsatAppCertFlag(binCode, 43));
	   result.add(this.serialNumber(binCode, 44, 63));
	   
	   if (this.cospasSarsatAppCertFlagPresent(binCode)) 
	   {
	       result.add(this.nationalUse(binCode, 64, 73));
	       result.add(this.cospasSarsatAppCertNumber(binCode, 74, 83));
	   } 
	   else 
	   {
	       result.add(this.nationalUse(binCode, 64, 83));
	   }

	   result.add(this.auxRadioLocating(binCode, 84, 85));

	   if (this.isUS) 
	   {
	       result.add(this.usManufacturerId(binCode, 44, 51));
	       result.add(this.usSeqNo(binCode, 52, 63));
	       result.add(this.usModelId(binCode, 64, 67));
	       result.add(this.usRunNo(binCode, 68, 75));
	       result.add(this.usNatUse(binCode, 76, 83));
	   }
	   
	   if (hexStr.length() > 15) 
	   {
	       result.add(this.bch1(binCode, 86, 106));
	       
	       if (this.isLongMessage(binCode)) 
	       {
		   result.add(this.nationalUse(binCode, 107, 144));
	       } 
	       else
	       {
		   result.add(this.nationalUse(binCode, 107, 112));
	       }
	   }

	   return result;
    }
    
	@Override
    public boolean canDecode (String binCode)
	{
	   if (super.canDecode(binCode) )
	   {
	      String serCode = binCode.substring(40, 43);

	      // Note: SubClass Constructor sets the serialCode in
	      // Only 111, 101 (which are the user serial spare codes) >= 0
          return (serCode.compareTo(this.serialCode) >= 0);
	   }
	   return false;
	}
}
