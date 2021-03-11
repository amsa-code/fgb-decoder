package com.amsa.fgb.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Contains methods to decode hex strings
 * 
 * @author smw01
 * 
 */
public final class HexDecoder {

    private HexDecoder() {
        // prevent instantiation
    }

    private static List<HexAttribute> getHexAttributesDecodeFull(String hexStr) {
        return getHexAttributes(hexStr, (proto, hex) -> proto.decode(hex));
    }

    private static List<HexAttribute> getHexAttributes(String hexStr,
            BiFunction<com.amsa.fgb.internal.BeaconProtocol, String, List<HexAttribute>> attributesFunction) {
        hexStr = hexStr.trim();
        int hexLen = hexStr.length();
        if (!(hexLen == 15 || hexLen == 30)) {
            return Collections.singletonList(
                    new HexAttribute(AttributeType.ERROR, "", "HEX STRING MUST BE 15 OR 30 CHARACTERS IN LENGTH"));
        } else {
            // Convert the Hex String into Binary Code
            String binCode = com.amsa.fgb.internal.Conversions.hexToBinary(hexStr);
            String h = hexStr;
            // Note that the last protocol UnknownProtocol will always match so a return is
            // guaranteed
            return BeaconProtocol.createBeaconProtocols() //
                    .stream() //
                    .filter(proto -> proto.canDecode(binCode)) //
                    .map(proto -> attributesFunction.apply(proto, h)) //
                    .findFirst() //
                    .get();
        }
    }

    static Map<String, HexAttribute> decodeToMap(String hexStr) {
        Map<String, HexAttribute> map = new HashMap<String, HexAttribute>();
        for (HexAttribute h : getHexAttributesDecodeFull(hexStr)) {
            map.put(h.desc.toString(), h);
        }
        return map;
    }

    // This is the method that is used to decode the entire Hex Str.
    public static String decodeFullAsJson(String hexStr) {
        List<HexAttribute> r = getHexAttributesDecodeFull(hexStr);
        return DecodeAsJson.INSTANCE.getData(r);
    }

}
