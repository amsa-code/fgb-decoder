package au.gov.amsa.fgb.internal;

final class UserSerialAircraftAddress extends UserSerialAircraftBase {

    UserSerialAircraftAddress() {
        super("Aircraft Address", "011", (u, binCode, result) -> {
            result.addAll(u.aircraft24BitAddress(binCode, 44, 67));
            result.add(((UserSerialAircraftAddress) u).eltNumber(binCode, 68, 73));
        });
    }

    private HexAttribute eltNumber(String binCode, int s, int f) {
        int v = Conversions.binaryToDecimal(binCode.substring(s, f + 1));
        String e = "";

        return new HexAttribute(AttributeType.ADDITIONAL_ELT_NUMBERS, s, f, v, e);
    }
}
