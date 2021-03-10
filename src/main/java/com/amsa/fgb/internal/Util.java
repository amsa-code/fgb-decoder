package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

class Util {

    public static List<HexAttribute> coarsePositionAttributes(double latSeconds, double lonSeconds, int s, int f) {
        List<HexAttribute> list = new ArrayList<>();
        list.add(new HexAttribute(AttributeType.COARSE_POSITION_LATITUDE, s,
                f, latSeconds / 3600.0 + "", ""));
        list.add(new HexAttribute(AttributeType.COARSE_POSITION_LONGITUDE,
                s, f, lonSeconds / 3600.0 + "", ""));
        return list;
    }
}
