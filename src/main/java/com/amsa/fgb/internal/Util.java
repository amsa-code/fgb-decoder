package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

final class Util {
    
    private Util() {
        //prevent instantiation
    }

    public static List<HexAttribute> coarsePositionAttributes(double latSeconds, double lonSeconds,
            int s, int f) {
        List<HexAttribute> list = new ArrayList<>();
        list.add(new HexAttribute(AttributeType.COARSE_POSITION_LATITUDE, s, f,
                latSeconds / 3600.0 + "", ""));
        list.add(new HexAttribute(AttributeType.COARSE_POSITION_LONGITUDE, s, f,
                lonSeconds / 3600.0 + "", ""));
        return list;
    }

    public static List<HexAttribute> offsetPositionAttributes(double latDiffSeconds, double lonDiffSeconds, int s,
            int f) {
        List<HexAttribute> list = new ArrayList<>();
        list.add(new HexAttribute(AttributeType.OFFSET_POSITION_LATITUDE_DIFF, s, f, latDiffSeconds/3600.0 + "",
                ""));
        list.add(new HexAttribute(AttributeType.OFFSET_POSITION_LONGITUDE_DIFF, s, f, lonDiffSeconds/3600.0 + "",
                ""));
        return list;
    }
}
