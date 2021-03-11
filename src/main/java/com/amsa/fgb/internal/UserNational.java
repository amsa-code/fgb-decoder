package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

class UserNational extends User {
    UserNational() {
        protocolName = "National User";
        userProtocolCode = "100";
    }

    @Override
     List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);

        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.messageType(binCode, 25, 26));
        result.add(this.hexData(hexStr, 25, binCode.length() - 1));
        result.add(this.hexId(binCode, 26, 85));
        result.add(this.countryCode(binCode, 27, 36));
        result.add(this.protocolType(binCode, 37, 39));

        result.add(this.nationalUse(binCode, 40, 85));

        if (hexStr.length() > 15) {
            result.add(this.bch1(binCode, 86, 106));
            if (this.isLongMessage(binCode)) {
                // 25 May 2005
                // Based on CDP's latest guideline: PDF-2: bit107-132; BCH-2: bit133-144

                result.add(new HexAttribute(AttributeType.PDF_2, 107, 132, binCode.substring(113, 133), ""));
                result.add(new HexAttribute(AttributeType.BCH_2, 133, 144, binCode.substring(133, 145), ""));

                /*
                 * result.add(this.encodedPositionSource(binCode, 107));
                 * 
                 * if (this.defaultFFFFFFFF(hexStr)) { result.add(this.longMessage(binCode, 113,
                 * 144)); } else { if (this.default00000000(hexStr)) {
                 * result.add(this.longMessage(binCode, 113, 144)); } else {
                 * result.add(this.latitude(binCode, 108, 119));
                 * result.add(this.longitude(binCode, 120, 132)); result.add(this.bch2(binCode,
                 * 133, 144)); } result.add(this.nationalUse(binCode, 113, 144)); }
                 */
            }
            // 14/03/2005
            else {
                // 07/04/2005
                // result.add(this.nationalUse(binCode, 109, 112));
                result.add(this.nationalUse(binCode, 107, 112));
            }
        }
        return result;
    }

}
