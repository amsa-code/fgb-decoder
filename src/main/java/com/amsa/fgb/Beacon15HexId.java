package com.amsa.fgb;

import com.amsa.fgb.internal.Decoder;
import com.github.davidmoten.guavamini.Preconditions;

public final class Beacon15HexId {

    private Beacon15HexId() {
        // prevent instantiation
    }
    
    public static String decodeHexToJson(String hex) {
        Preconditions.checkNotNull(hex);
        Preconditions.checkArgument(hex.length() == 15, "hex must be 15 characters");
        return Decoder.decodeFullAsJson(hex);
    }
}
