package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

class Unknown extends BeaconProtocol {

    Unknown() {
    }

    // This is should always return true.
    // This will be the last beacon decode attempt.
    @Override
     boolean canDecode(String binCode) {
        // System.out.println("Trying Unknown Protocol " );
        return true;
    }

    @Override
    String getName() {
        return "Unknown Beacon Protocol";
    }

    @Override
     List<HexAttribute> decode(String hexStr) {
        List<HexAttribute> result = new ArrayList<HexAttribute>();
        String errorMsg = "Unable to decode this hex string";
        result.add(new HexAttribute(AttributeType.ERROR, 0, "", errorMsg));

        return result;
    }

}
