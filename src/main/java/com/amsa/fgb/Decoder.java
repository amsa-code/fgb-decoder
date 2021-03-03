package com.amsa.fgb;

import com.amsa.fgb.internal.HexDecoder;

public final class Decoder {
    private Decoder() {
        // prevent instantiation
    }

    // This is the decode that is used by the Incident Navigator SAR Search
    public static String decodeSearch(String hexStr, Formatter formatter) {
        return HexDecoder.decodeSearch(hexStr, formatter);
    }

    // This is the method that is used to decode the entire Hex Str.
    public static String decode(String hexStr, Formatter formatter) {
        return HexDecoder.decode(hexStr, formatter);
    }
}
