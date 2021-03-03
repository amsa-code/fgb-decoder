package com.amsa.fgb;

import com.amsa.fgb.internal.HexDecoder;

public final class Decoder {

    private Decoder() {
        // prevent instantiation
    }

    public static String decodePartial(String hexStr, Formatter formatter) {
        return HexDecoder.decodePartial(hexStr, formatter);
    }

    public static String decodeFull(String hexStr, Formatter formatter) {
        return HexDecoder.decodeFull(hexStr, formatter);
    }

    static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Decoder: Wrong number of arguments");
            System.out.println("Usage: <HexStr> [<HTML|TEXT|XML>]");
            return;
        }
        String hex = args[0];
        String format = args[1];
        String output = decodeFull(hex, Formatter.valueOf(format.toUpperCase()));
        System.out.println(output);
    }
}
