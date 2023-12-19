package au.gov.amsa.fgb.internal;

import com.github.davidmoten.guavamini.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ELTDTLocation
    extends BeaconProtocol {

    public static final String ELTDT_PROTOCOL_CODE = "1001";
    public static final String DEFAULT_COARSE_LATITUDE = "011111111";
    public static final String DEFAULT_COARSE_LONGITUDE = "0111111111";

    public ELTDTLocation() {
        super(Lists.newArrayList("00", "10", "?0"), "ELT - ELT-DT Location Protocol", "110");
    }

    @Override
    String getName() {
        return protocolName();
    }

    @Override
    boolean canDecode(final String binCode) {
        final String protocol = binCode.substring(25, 27);

        if (beaconTypeCodes().contains(protocol)) {
            final String protocolCode = binCode.substring(37, 41);
            return protocolCode.equals(ELTDT_PROTOCOL_CODE);
        }

        return false;
    }

    @Override
    List<HexAttribute> decode(final String hexStr) {
        final String binCode = Conversions.hexToBinary(hexStr);
        final List<HexAttribute> result = new ArrayList<>();

        result.add(messageType(binCode));
        result.add(hexData(hexStr, 25, binCode.length() - 1));
        result.add(hexIdWithDefaultLocation(binCode));

        result.add(countryCode(binCode, 27, 36));
        result.add(protocolType(binCode, 37, 40));

        result.addAll(identificationData(binCode));

        result.addAll(coarsePosition(binCode));

        return result;
    }

    private HexAttribute messageType(final String binCode) {
        return new HexAttribute(AttributeType.MESSAGE_TYPE,
            25,
            26,
            this.getMsgTypeDesc("ELT(DT) Location Protocol", binCode),
            "");
    }

    private static HexAttribute hexIdWithDefaultLocation(final String binCode) {
        final String binHexId = binCode.substring(26, 67) + DEFAULT_COARSE_LATITUDE + DEFAULT_COARSE_LONGITUDE;
        return new HexAttribute(AttributeType.HEX_ID,
            26,
            26 + binHexId.length() - 1,
            Conversions.binaryToHex(binHexId),
            "");
    }

    private List<HexAttribute> identificationData(final String binCode) {
        final List<HexAttribute> result = new ArrayList<>();
        final String idType = binCode.substring(41, 43);
        switch (Conversions.binaryToDecimal(idType)) {
            case 0:
                result.addAll(aircraft24BitAddressIdentificationType(binCode));
                break;
            case 1:
                result.addAll(aircraftOperatorAndSerialNumberIdentificationType(binCode));
                break;
            case 2:
                result.addAll(cospasSarsatTypeApprovalCertAndBeaconSerialIdentificationType(binCode));
                break;
            case 3:
                // Test, now what?
                break;
            default:
                break;
        }

        return result;
    }

    private Collection<HexAttribute> aircraft24BitAddressIdentificationType(final String binCode) {
        final List<HexAttribute> result = new ArrayList<>();

        result.add(new HexAttribute(AttributeType.ELT_IDENTITY_TYPE, "Aircraft 24 bit address", ""));
        result.addAll(aircraft24BitAddress(binCode, 43, 66));

        return result;
    }

    private Collection<HexAttribute> aircraftOperatorAndSerialNumberIdentificationType(final String binCode) {
        return Arrays.asList(
            new HexAttribute(AttributeType.ELT_IDENTITY_TYPE, "Aircraft operator designator and serial number", ""),
            aircraftOperator(binCode, 43, 57),
            aircraftSerialNumber(binCode, 58, 66));
    }

    private Collection<HexAttribute> cospasSarsatTypeApprovalCertAndBeaconSerialIdentificationType(
        final String binCode) {
        return Arrays.asList(new HexAttribute(
                AttributeType.ELT_IDENTITY_TYPE,
                "Type approval certificate and serial number",
                ""),
            new HexAttribute(AttributeType.CS_TYPE_APPROVAL_NUMBER,
                43,
                52,
                Conversions.binaryToDecimal(binCode.substring(43, 53)),
                ""),
            new HexAttribute(AttributeType.BEACON_SERIAL_NUMBER,
                53,
                66,
                Conversions.binaryToDecimal(binCode.substring(53, 67)),
                ""));
    }

    private List<HexAttribute> coarsePosition(final String binCode) {
        final String code = binCode.substring(67, 86);
        if (code.equals(DEFAULT_COARSE_LATITUDE + DEFAULT_COARSE_LONGITUDE)) {
            return Collections.emptyList();
        } else {
            final long latSeconds = latitudeSeconds(binCode);
            final long lonSeconds = longitudeSeconds(binCode);
            return Util.coarsePositionAttributes(latSeconds, lonSeconds, 67, 85);
        }
    }

    private long latitudeSeconds(final String binCode) {
        final long halfDegrees = Conversions.binaryToDecimal(binCode.substring(68, 76));
        final long seconds = halfDegrees * 30 * 60;
        return binCode.charAt(67) == '1' ? seconds * -1 : seconds;
    }

    private long longitudeSeconds(final String binCode) {
        final long halfDegrees = Conversions.binaryToDecimal(binCode.substring(77, 86));
        final long seconds = halfDegrees * 30 * 60;
        return binCode.charAt(76) == '1' ? seconds * -1 : seconds;
    }
}
