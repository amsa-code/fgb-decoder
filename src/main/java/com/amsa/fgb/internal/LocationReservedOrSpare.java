package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

import com.github.davidmoten.guavamini.Lists;

abstract class LocationReservedOrSpare extends BeaconProtocol {

    private final String stdProtocolCode;

    LocationReservedOrSpare(String stdProtocolCode, String protocolName) {
        // 16 May 2005
        // ?0 is for 15-char Hex string where bit25 is unknown since it starts with
        // bit26
        super(Lists.newArrayList("00", "10", "?0"));
        this.stdProtocolCode = stdProtocolCode;
        this.protocolName = protocolName;
    }

    @Override
    String getName() {
        return protocolName;
    }

    @Override
    List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);

        List<HexAttribute> result = new ArrayList<HexAttribute>();

        result.add(this.messageType(binCode, 25, 26));
        result.add(this.hexData(hexStr, 25, binCode.length() - 1));
        result.add(this.hexId(binCode, 26, 85));
        result.add(this.countryCode(binCode, 27, 36));

        // b40 doesn't matter for this reserved
        result.add(this.protocolType(binCode, 37, 40));

        // 09 May 2005, based on CDP's guidance
        // Providing the binary codes from bit41 to bit 85 inclusively
        result.add(new HexAttribute(AttributeType.ID_POSN, 41, 85, binCode.substring(41, 86), ""));

        if (hexStr.length() > 15) {
            // Providing the BCH1 decoding
            result.add(this.bch1(binCode, 86, 106));
            // Providing the binary codes from bit107 to bit 112
            // result.add(new HexAttribute("Supplementary Data", 107, 112,
            // binCode.substring(107,113), ""));

            // 24 May 2005
            // CDP's latest Guidance
            if (this.isLongMessage(binCode)) {
                result.add(new HexAttribute(AttributeType.PDF_2, 107, 132,
                        binCode.substring(113, 133), ""));
                result.add(new HexAttribute(AttributeType.BCH_2, 133, 144,
                        binCode.substring(133, 145), ""));
            } else {
                result.add(new HexAttribute(AttributeType.SUPPLEMENTARY_DATA, 107, 112,
                        binCode.substring(107, 113), ""));
            }

        }

        return result;
    }

    // Override the method of "canCode" (b37-39).
    // b40 doesn't matter in this Reserved (orbitography) protocol
    @Override
    boolean canDecode(String binCode) {
        String protocol = binCode.substring(25, 27);

        // System.out.println("Trying " + name);
        int f = this instanceof LocationReserved ? 40 : 41;
        if (beaconTypeCodes().contains(protocol)) {
            String protocolCode = binCode.substring(37, f);
            return protocolCode.equals(this.stdProtocolCode);
        }

        // else
        return false;
    }

    // Overriden messageType
    private HexAttribute messageType(String binCode, int s, int f) {
        String v = "Location"; // this is where to oerride
        String e = "";

        // 16 May 2005
        v = this.getMsgTypeDesc(v, binCode);

        return new HexAttribute(AttributeType.MESSAGE_TYPE, s, f, v, e);
    }
}
