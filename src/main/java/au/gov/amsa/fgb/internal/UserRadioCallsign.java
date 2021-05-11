package au.gov.amsa.fgb.internal;

import java.util.ArrayList;

final class UserRadioCallsign extends UserAviationOrMaritimeOrRadioCallsign {

    UserRadioCallsign() {
        super("Radio Call Sign", "110", (u, binCode, result) -> {
            result.add(((UserRadioCallsign) u).radioCallsign(binCode, 40, 75));
            result.add(u.specificBeaconNumber(binCode, 76, 81));
            result.add(u.spare(binCode, 82, 83));
        });
    }

    private HexAttribute radioCallsign(String binCode, int s, int f) {
        // 11 May 2005
        // Passed back the String[]
        String[] vE = Conversions.mBaudotBits2mBaudotStr(this.getName(),
                binCode.substring(s, s + 24), 6);
        // String v = Conversions.mBaudotBits2mBaudotStr(binCode.substring(s, s+24), 6);
        // String h = Conversions.binaryToHex(binCode.substring(s+24, f+1));

        String v = vE[0];
        String e = vE[1];
        int count = Integer.parseInt(vE[2]);

        if (e != null && e.length() > 0) {
            e = "\nWARNING - SUSPECT NON-SPEC IN RADIO CALL SIGN\n" + e;
        }

        // String h = Conversions.binaryToHex(binCode.substring(s+24, f+1));
        int[] digits = new int[3];
        digits[0] = Conversions.binaryToDecimal(binCode.substring(s + 24, s + 28));
        digits[1] = Conversions.binaryToDecimal(binCode.substring(s + 28, s + 32));
        digits[2] = Conversions.binaryToDecimal(binCode.substring(s + 32, f + 1));

        String h = "";
        ArrayList<String> e2 = new ArrayList<String>();

        for (int i = 0; i < 3; i++) {
            // only 0-9 are allowed, SPACE (1010, ie, 10) is allowed only when it is
            // trailing
            if (digits[i] > 9) {
                if (digits[i] == 10) {
                    h += "*";

                    // e += "?" + ++count + " = " + Conversions.decimalToBinary(digits[i]) + " =
                    // Non-Spec\n";

                    e2.add(Conversions.decimalToBinary(digits[i]) + " = Non-Spec\n");
                } else {
                    h += "?";
                    // e += "?" + ++count + " = " + Conversions.decimalToBinary(digits[i]) + " =
                    // Non-Spec\n";

                    e2.add(Conversions.decimalToBinary(digits[i]) + " = Non-Spec\n");

                    // System.out.println("digits[" + i + "]=" + digits[i]);
                }
            } else {
                h += digits[i] + "";

                // This is for syn only
                e2.add(Conversions.decimalToBinary(digits[i]) + " = Spec\n");
            }
        }

        // Replace "*" with " " at the end, eg, "*9*" to "*9 "
        int index = h.length() - 1;
        for (; index >= 0; index--) {
            if (h.charAt(index) == '*') {
                continue;
            } else {
                break;
            }
        }

        // System.out.println("index=" + index);

        for (int i = index + 1; i >= 0 && i < h.length(); i++) {
            if (h.charAt(i) == '*') {
                h = h.substring(0, i) + " " + h.substring(i + 1);
            }
        }

        // Replace all rest * to ?, eg "*9 " to "?9 "
        h = h.replace('*', '?');
        // Append the relevant elements in e2 to e
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < h.length(); i++) {
            if (h.charAt(i) == '?') {
                b.append("?" + ++count + " = " + e2.get(i));
            }
        }
        e += b.toString();
        // 24 June 2005, double quote is not used any more
        // String vH = "\"" + v + h + "\"";
        String vH = (v + h).trim();

        return new HexAttribute(AttributeType.RADIO_CALLSIGN, s, f, vH, e);
    }
}
