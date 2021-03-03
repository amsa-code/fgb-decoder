package com.amsa.fgb.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.amsa.fgb.Formatter;

/**
 * Contains methods to decode hex strings
 * 
 * @author smw01
 * 
 */
public final class HexDecoder {

    // This is the decode that is used by the Incident Navigator SAR Search
    public static String decodeSearch(String hexStr, Formatter formatter) {
        List<HexAttribute> r = getHexAttributesDecodeSearch(hexStr);
        return format(formatter, r);
    }

    private static List<HexAttribute> getHexAttributesDecodeSearch(String hexStr) {
        return getHexAttributes(hexStr, (proto, hex) -> proto.decodeSearch(hex));
    }

    private static List<HexAttribute> getHexAttributesDecode(String hexStr) {
        return getHexAttributes(hexStr, (proto, hex) -> proto.decode(hex));
    }

    private static List<HexAttribute> getHexAttributes(String hexStr,
            BiFunction<com.amsa.fgb.internal.BeaconProtocol, String, List<HexAttribute>> attributesFunction) {
        hexStr = hexStr.trim();
        int hexLen = hexStr.length();
        if (!(hexLen == 15 || hexLen == 30)) {
            return Collections.singletonList(
                    new HexAttribute("", "", "HEX STRING MUST BE 15 OR 30 CHARACTERS IN LENGTH"));
        } else {
            // Convert the Hex String into Binary Code
            String binCode = com.amsa.fgb.internal.Conversions.hexToBinary(hexStr);
            for (BeaconProtocol proto : BeaconProtocol.ALL) {
                if (proto.canDecode(binCode)) {
                    return attributesFunction.apply(proto, hexStr);
                }
            }
        }
        return Collections.emptyList();
    }

    public static Map<String, HexAttribute> decodeToMap(String hexStr) {
        Map<String, HexAttribute> map = new HashMap<String, HexAttribute>();
        for (HexAttribute h : getHexAttributesDecode(hexStr)) {
            map.put(h.desc, h);
        }
        return map;
    }

    // This is the method that is used to decode the entire Hex Str.
    public static String decode(String hexStr, Formatter formatter) {
        List<HexAttribute> r = getHexAttributesDecode(hexStr);
        return format(formatter, r);
    }

    private static String format(Formatter formatter, List<HexAttribute> r) {
        final DecodeFilter f;

        if (formatter == Formatter.HTML) {
            f = DecodeAsHTML.INSTANCE;
        } else if (formatter == Formatter.XML) {
            f = DecodeAsXML.INSTANCE;
        } else if (formatter == Formatter.XML2) {
            f = DecodeAsXML2.INSTANCE;
        } else {
            // plain text
            f = DecodeAsText.INSTANCE;
        }
        return f.getData(r);
    }

    public static void main(String[] args) {
        String s = "";
        String f = "";
        try {
            // s = args[0];

            // The last one should always be the formatter!
            for (int i = 0; i < args.length - 1; i++)
                s += args[i];
        } catch (Exception e) {
            System.out.println("Hexdecode: Insufficient arguments");
            System.out.println("Usage: java Hexdecode <HexStr> [<HTML|TEXT|XML>]");
            return;
        }
        try {
            // f = args[1];
            // The last one should always be the formatter!
            f = args[args.length - 1];
        } catch (Exception e) {
            f = "TEXT";
        }
        String output = HexDecoder.decode(s, Formatter.valueOf(f.toUpperCase()));

        System.out.println(output);
    }
}
