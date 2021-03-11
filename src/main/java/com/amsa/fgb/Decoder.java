package com.amsa.fgb;

import com.amsa.fgb.internal.Debug;
import com.amsa.fgb.internal.HexDecoder;

public final class Decoder {

    private Decoder() {
        // prevent instantiation
    }

    public static String decodeFullAsJson(String hexStr) {
        Debug.lastHexStr=hexStr;
        return HexDecoder.decodeFullAsJson(hexStr);
    }

    static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Only expecting one argument: <HexString>");
        }
        String hex = args[0];
        String output = decodeFullAsJson(hex);
        System.out.println(output);
    }
}
