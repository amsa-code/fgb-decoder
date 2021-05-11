/*
  This class consists of static methods that are commonly used in some protocols in Hexdecode application.

 */

package au.gov.amsa.fgb.internal;

import java.util.List;

final class Common {

    private Common() {
        // prevent instantiation
    }

    // For bit 109-112 (Maritime Emergency Codes). See C/S T.001 Table A4
    static List<HexAttribute> maritimeEmergencyCodes(List<HexAttribute> result, String binCode) {
        // Convert bit 109-112 to decimal
        int bitToNum = Conversions.binaryToDecimal(binCode.substring(109, 113));

        String mec = "Maritime Emergency Codes";
        AttributeType name = AttributeType.NATURE_OF_DISTRESS;

        HexAttribute maritimeEmergencyCodes;

        switch (bitToNum) {
        case 0:
            maritimeEmergencyCodes = new HexAttribute(name, 109, 112,
                    mec + "(Unspecified distress) " + binCode.substring(109, 113), "");
            break;
        case 1:
            maritimeEmergencyCodes = new HexAttribute(name, 109, 112,
                    mec + "(Fire/explosion) " + binCode.substring(109, 113), "");
            break;
        case 2:
            maritimeEmergencyCodes = new HexAttribute(name, 109, 112,
                    mec + "(Flooding) " + binCode.substring(109, 113), "");
            break;
        case 3:
            maritimeEmergencyCodes = new HexAttribute(name, 109, 112,
                    mec + "(Collision) " + binCode.substring(109, 113), "");
            break;
        case 4:
            maritimeEmergencyCodes = new HexAttribute(name, 109, 112,
                    mec + "(Grounding) " + binCode.substring(109, 113), "");
            break;
        case 5:
            maritimeEmergencyCodes = new HexAttribute(name, 109, 112,
                    mec + "(Listing, in danger of capsizing) " + binCode.substring(109, 113), "");
            break;
        case 6:
            maritimeEmergencyCodes = new HexAttribute(name, 109, 112,
                    mec + "(Sinking) " + binCode.substring(109, 113), "");
            break;
        case 7:
            maritimeEmergencyCodes = new HexAttribute(name, 109, 112,
                    mec + "(Disabled and adrift) " + binCode.substring(109, 113), "");
            break;
        case 8:
            maritimeEmergencyCodes = new HexAttribute(name, 109, 112,
                    mec + "(Abandoning ship) " + binCode.substring(109, 113), "");
            break;
        default:
            maritimeEmergencyCodes = new HexAttribute(name, 109, 112,
                    mec + "(spare) " + binCode.substring(109, 113), "");
        }

        result.add(maritimeEmergencyCodes);

        return result;
    }

    private static HexAttribute fireFlag(String binCode, int s) {
        String v = "";
        String e = "";
        if (binCode.charAt(s) == '1') {
            v = "YES";
        } else {
            v = "NO";
        }

        // This is grouped under "Nature of Distress" with the identation of 30 white
        // spaces
        // return new HexAttribute(" ", s, v, e);
        return new HexAttribute(AttributeType.FIRE_FLAG, s, v, e);
    }

    private static HexAttribute medicalHelpFlag(String binCode, int s) {
        String v = "";
        String e = "";
        if (binCode.charAt(s) == '1') {
            v = "YES";
        } else {
            v = "NO";
        }

        // return new HexAttribute(" ", s, v, e);
        return new HexAttribute(AttributeType.MEDICAL_HELP_FLAG, s, v, e);
    }

    private static HexAttribute disabledFlag(String binCode, int s) {
        String v = "";
        String e = "";
        if (binCode.charAt(s) == '1') {
            v = "YES";
        } else {
            v = "NO";
        }

        return new HexAttribute(AttributeType.DISABLED, s, v, e);
    }

    private static HexAttribute emergencySpare(String binCode, int s) {
        String v = "";
        String e = "";
        if (binCode.charAt(s) == '0') {
            v = binCode.charAt(s) + " (N/A)";
        } else {
            v = binCode.charAt(s) + " (Non-Spec)";
        }

        return new HexAttribute(AttributeType.SPARE, s, v, e);
    }

    // For bit 109-112 (Non-Maritime Emergency Codes). See C/S T.001 Table A5
    static List<HexAttribute> nonMaritimeEmergencyCodes(List<HexAttribute> result, String binCode) {
        result.add(new HexAttribute(AttributeType.NATURE_OF_DISTRESS, 109, 112,
                binCode.substring(109, 113), ""));
        result.add(fireFlag(binCode, 109));
        result.add(medicalHelpFlag(binCode, 110));
        result.add(disabledFlag(binCode, 111));
        result.add(emergencySpare(binCode, 112));
        return result;
    }

    static final class Position {
        private final int latSeconds;
        private final int lonSeconds;

        Position(int latSeconds, int lonSeconds) {
            this.latSeconds = latSeconds;
            this.lonSeconds = lonSeconds;
        }

        int latSeconds() {
            return latSeconds;
        }

        int lonSeconds() {
            return lonSeconds;
        }

        double latDecimal() {
            return latSeconds / 3600.0;
        }

        double lonDecimal() {
            return lonSeconds / 3600.0;
        }

    }

    // TODO use this wherever lat/long decoding happens because there is a lot of
    // copy and paste around! Dave Moten April 11 2019
    /**
     * @param binCode        input binary string
     * @param start          start index
     * @param length         length of position encoding
     * @param secondsPerUnit factor to use when converting a decimal number to
     *                       seconds
     * @return position
     */
    static Position position(String binCode, int start, int length, int secondsPerUnit) {
        final int lonLength;
        // if odd number of bits then lon gets one more than lat
        if (length % 2 != 0) {
            lonLength = floorDiv(length, 2) + 1;
        } else {
            lonLength = length / 2;
        }
        final int latLength = length - lonLength;

        final int latSeconds = extractLatSeconds(binCode, start, secondsPerUnit, latLength);
        final int lonSeconds = extractLonSeconds(binCode, start, secondsPerUnit, lonLength, latLength);
        return new Position(latSeconds, lonSeconds);
    }

    private static int extractLonSeconds(String binCode, int start, int secondsPerUnit,
            final int lonLength, final int latLength) {
        String lonBits = binCode.substring(start + latLength + 1, start + latLength + lonLength);
        int code = Conversions.binaryToDecimal(lonBits);
        final int codeSeconds = code * secondsPerUnit;
        final int deg = codeSeconds / 3600;
        final int min = codeSeconds % 3600 / 60;
        int seconds = deg * 3600 + min * 60;

        if (binCode.charAt(start + latLength) == '1') {
            seconds = seconds * -1;
        }
        return seconds;
    }

    private static int extractLatSeconds(String binCode, int start, int secondsPerUnit,
            final int latLength) {
        String latBits = binCode.substring(start + 1, start + latLength);
        int code = Conversions.binaryToDecimal(latBits);
        final int codeSeconds = code * secondsPerUnit;
        final int deg = codeSeconds / 3600;
        final int min = codeSeconds % 3600 / 60;
        int seconds = deg * 3600 + min * 60;
        if (binCode.charAt(start) == '1') {
            seconds = seconds * -1;
        }
        return seconds;
    }

    /**
     * Returns the largest (closest to positive infinity) {@code int} value that is
     * less than or equal to the algebraic quotient. There is one special case, if
     * the dividend is the {@linkplain Integer#MIN_VALUE Integer.MIN_VALUE} and the
     * divisor is {@code -1}, then integer overflow occurs and the result is equal
     * to the {@code Integer.MIN_VALUE}.
     * <p>
     * Normal integer division operates under the round to zero rounding mode
     * (truncation). This operation instead acts under the round toward negative
     * infinity (floor) rounding mode. The floor rounding mode gives different
     * results than truncation when the exact result is negative.
     * <ul>
     * <li>If the signs of the arguments are the same, the results of
     * {@code floorDiv} and the {@code /} operator are the same. <br>
     * For example, {@code floorDiv(4, 3) == 1} and {@code (4 / 3) == 1}.</li>
     * <li>If the signs of the arguments are different, the quotient is negative and
     * {@code floorDiv} returns the integer less than or equal to the quotient and
     * the {@code /} operator returns the integer closest to zero.<br>
     * For example, {@code floorDiv(-4, 3) == -2}, whereas {@code (-4 / 3) == -1}.
     * </li>
     * </ul>
     * <p>
     *
     * @param x the dividend
     * @param y the divisor
     * @return the largest (closest to positive infinity) {@code int} value that is
     *         less than or equal to the algebraic quotient.
     * @throws ArithmeticException if the divisor {@code y} is zero
     * @see #floorMod(int, int)
     * @see #floor(double)
     * @since 1.8
     */
    // copied from java 1.8
    private static int floorDiv(int x, int y) {
        int r = x / y;
        // if the signs are different and modulo not zero, round down
        if ((x ^ y) < 0 && (r * y != x)) {
            r--;
        }
        return r;
    }

}
