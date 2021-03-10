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

    public static double getLatitudeFromFinePosition(String value) {
        return toLatitude(value.substring(0, 9));
    }

    public static double getLongitudeFromFinePosition(String value) {
        return toLongitude(value.substring(10));
    }

    // TODO unit test
    public static double toLatitude(String value) {
        int d = Integer.parseInt(value.substring(0, 2));
        int m = Integer.parseInt(value.substring(3, 5));
        int s = Integer.parseInt(value.substring(6, 8));
        boolean positive = value.charAt(8) == 'N';
        int sign = positive ? 1 : -1;
        return sign * (d + m / 60.0 + s / 3600.0);
    }

    // TODO unit test
    public static double toLongitude(String value) {
        int d = Integer.parseInt(value.substring(0, 3));
        int m = Integer.parseInt(value.substring(4, 6));
        int s = Integer.parseInt(value.substring(7, 9));
        boolean positive = value.charAt(9) == 'N';
        int sign = positive ? 1 : -1;
        return sign * (d + m / 60.0 + s / 3600.0);
    }

    static List<HexAttribute> coarsePositionAttributes(String v, int start, int finish) {
        List<HexAttribute> list = new ArrayList<>();
        if (!v.equals("DEFAULT")) {
            list.add(new HexAttribute(AttributeType.COARSE_POSITION_LATITUDE, start, finish,
                    Util.getLatitudeFromCoarsePosition(v) + "", ""));
            list.add(new HexAttribute(AttributeType.COARSE_POSITION_LONGITUDE, start, finish,
                    Util.getLongitudeFromCoarsePosition(v) + "", ""));
        }
        return list;
    }
}
