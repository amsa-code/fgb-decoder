package com.amsa.fgb;

import com.amsa.fgb.internal.Decoder;
import com.github.davidmoten.guavamini.Preconditions;

public final class Detection {
    
    private Detection() {
        // prevent instantiation
    }
    
    public static String fromHex(String hex) {
        Preconditions.checkNotNull(hex);
        Preconditions.checkArgument(hex.length() == 30, "hex must be 30 characters");
        return Decoder.decodeFullAsJson(hex);
    }

}
