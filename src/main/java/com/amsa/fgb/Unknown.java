package com.amsa.fgb;

import java.util.ArrayList;
import java.util.List;

public class Unknown extends BeaconProtocol {

    public Unknown() {
    }

    // This is should always return true.
    // This will be the last beacon decode attempt.
    @Override
    public boolean canDecode(String binCode) {
        // System.out.println("Trying Unknown Protocol " );
        return true;
    }

    @Override
    public String getName() {
        return "Unknown Beacon Protocol";
    }

    @Override
    public List<HexAttribute> decode(String hexStr) {
        List<HexAttribute> result = new ArrayList<HexAttribute>();
        String errorMsg = "Unable to decode this hex string";
        result.add(new HexAttribute("", 0, "", errorMsg));

        return result;
    }

}
