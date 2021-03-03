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

    static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Decoder: Wrong number of arguments");
            System.out.println("Usage: <HexStr> [<HTML|TEXT|XML>]");
            return;
        }
        String hex = args[0];
        String format = args[1];
        String output = decode(hex, Formatter.valueOf(format.toUpperCase()));
        System.out.println(output);
    }
}
