package com.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

class Util {

 // TODO unit test
    private static double getLatitudeFromCoarsePosition(String value) {
        // 35 44S 115 30E
        int d = Integer.parseInt(value.substring(0, 2));
        int m = Integer.parseInt(value.substring(3, 5));
        int sign = value.charAt(6) == 'N' ? 1 : -1;
        return sign * (d + m / 60.0);
    }

    // TODO unit test
    private static double getLongitudeFromCoarsePosition(String value) {
        // 35 44S 115 30E
        int d = Integer.parseInt(value.substring(7, 10));
        int m = Integer.parseInt(value.substring(11, 13));
        int sign = value.charAt(13) == 'E' ? 1 : -1;
        return sign * (d + m / 60.0);
    }
    
    static List<HexAttribute> coarsePositionAttributes(String v, int start, int finish) {
        List<HexAttribute> list = new ArrayList<>();
        list.add(new HexAttribute(AttributeType.COARSE_POSITION_LATITUDE, start,
                finish, Util.getLatitudeFromCoarsePosition(v) + "", ""));
        list.add(new HexAttribute(AttributeType.COARSE_POSITION_LONGITUDE, start,
                finish, Util.getLongitudeFromCoarsePosition(v) + "", ""));
        return list;
    }
}
