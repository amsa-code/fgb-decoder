package au.gov.amsa.fgb.internal;

import com.github.davidmoten.junit.Asserts;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public final class HexDecoderTest {

    @Test
    public void isUtilityClass() {
        Asserts.assertIsUtilityClass(HexDecoder.class);
    }

    @Test
    public void testDecodeWithRLSHasDefaultPositionOnly() {
        String hex = "3EFA8C60A6BFDFF";
        Map<String, HexAttribute> map = decodeToMap(hex);
        assertEquals("3EFA8C60A6BFDFF", map.get("hexId").value());
        assertEquals("503", map.get("countryCode").value());
        assertEquals("Return Link Service", map.get("protocolType").value());
        assertEquals("EPIRB", map.get("beaconType").value());
        assertEquals("1099", map.get("rlsTacNumber").value());
        assertEquals("333", map.get("rlsId").value());
    }

    @Test
    public void testDecodeWithRLSHasPosition() {
        String hex = "3EFA8035B3D0540";
        Map<String, HexAttribute> map = decodeToMap(hex);
        assertEquals("3EFA8035B3BFDFF", map.get("hexId").value());
        assertEquals("503", map.get("countryCode").value());
        assertEquals("Return Link Service", map.get("protocolType").value());
        assertEquals("EPIRB", map.get("beaconType").value());
        assertEquals("1001", map.get("rlsTacNumber").value());
        assertEquals("11111", map.get("rlsId").value());
        assertEquals("-32.5", map.get("coarsePositionLatitude").value());
        assertEquals("160.0", map.get("coarsePositionLongitude").value());
    }

    @Test
    public void testDecodeFullHexString() {
        String hex = "9F7D4630535FEFF91E066861F0F731";
        Map<String, HexAttribute> map = decodeToMap(hex);
        assertEquals("3EFA8C60A6BFDFF", map.get("hexId").value());
        assertEquals("503", map.get("countryCode").value());
        assertEquals("Return Link Service", map.get("protocolType").value());
        assertEquals("333", map.get("rlsId").value());
        assertEquals("1099", map.get("rlsTacNumber").value());
        assertEquals("INTERNAL", map.get("encodedPositionSource").value());
        assertEquals("NO", map.get("homing121_5MHz").value());
        assertEquals("YES", map.get("rlmCapabilityType1Auto").value());
        assertEquals("NO", map.get("rlmCapabilityType2Manual").value());
        assertEquals("NO", map.get("rlmReceivedType1Auto").value());
        assertEquals("NO", map.get("rlmReceivedType2Manual").value());
        assertEquals("Galileo", map.get("rlsProviderId").value());
        assertEquals("011100110001", map.get("errorCorrectingCode2").value());
    }

    @Test
    public void testRlsPlb() {
        String hex = "3EFB0C80D3BFDFF";
        Map<String, HexAttribute> map = decodeToMap(hex);
        assertEquals("Return Link Service Location", map.get("messageType").value());
        assertEquals("3EFB0C80D3BFDFF", map.get("hexId").value());
        assertEquals("503", map.get("countryCode").value());
        assertEquals("Return Link Service", map.get("protocolType").value());
        assertEquals("PLB", map.get("beaconType").value());
        assertEquals("423", map.get("rlsId").value());
        assertEquals("3100", map.get("rlsTacNumber").value());
    }

    @Test
    public void decodeRlsEpirbFinePosition() {
        // TODO get confirmed values (McMurdo encoder and decoder broken for RLS as of
        // 17 April 2019. John Ophel to raise issue with them)
        String hex = "9F7D401037AAEACE32F4E863C1E8C0";
        Map<String, HexAttribute> map = decodeToMap(hex);
        assertEquals("3EFA80206F3FDFF", map.get("hexId").value());
        assertEquals("503", map.get("countryCode").value());
        assertEquals("Return Link Service", map.get("protocolType").value());
        assertEquals("222", map.get("rlsId").value());
        assertEquals("1001", map.get("rlsTacNumber").value());
        assertEquals("INTERNAL", map.get("encodedPositionSource").value());
        assertEquals("NO", map.get("homing121_5MHz").value());
        assertEquals("YES", map.get("rlmCapabilityType1Auto").value());
        assertEquals("NO", map.get("rlmCapabilityType2Manual").value());
        assertEquals("NO", map.get("rlmReceivedType1Auto").value());
        assertEquals("NO", map.get("rlmReceivedType2Manual").value());
        assertEquals("Galileo", map.get("rlsProviderId").value());
        assertEquals(-43.507778, Double.parseDouble(map.get("latitude").value()), 0.0001);
        assertEquals(172.492222, Double.parseDouble(map.get("longitude").value()), 0.0001);
    }

    @Test
    public void testDecodeWithRadioCallsignWithTrailingSpaces() {
        String hex = "C1AAD9FEF6490D1";
        Map<String, HexAttribute> m = decodeToMap(hex);
        assertEquals("PMKQ", m.get("radioCallsign").value());
    }

    @Test
    public void testDecodeEltDtLocationWithAircraft24BitAddress() {
        final Map<String, HexAttribute> m = decodeToMap("3EF23E3C543FDFF");
        assertEquals("ELT - ELT-DT Location Protocol", m.get("protocolType").value());
        assertEquals("503", m.get("countryCode").value());
        assertEquals("Aircraft 24 bit address", m.get("eltIdentityType").value());
        assertEquals("7C78A8", m.get("aircraft24BitAddressHex").value());
        assertEquals("37074250", m.get("aircraft24BitAddressOctal").value());
        assertEquals("VH-X4A", m.get("aircraftCallsign").value());
        assertNull(m.get("coarsePositionLatitude"));
        assertNull(m.get("coarsePositionLongitude"));
    }

    @Test
    public void testDecodeEltDtLocationWithOperatorDesignator() {
        final Map<String, HexAttribute> m = decodeToMap("3EF2BE3C543FDFF");
        assertEquals("ELT - ELT-DT Location Protocol", m.get("protocolType").value());
        assertEquals("503", m.get("countryCode").value());
        assertEquals("Aircraft operator designator and serial number", m.get("eltIdentityType").value());
        assertEquals("VZU", m.get("aircraftOperator").value());
        assertEquals("168", m.get("aircraftSerialNumber").value());
        assertNull(m.get("coarsePositionLatitude"));
        assertNull(m.get("coarsePositionLongitude"));
    }

    @Test
    public void testDecodeEltDtLocationWithTypeApprovalAndSerialNumber() {
        final Map<String, HexAttribute> m = decodeToMap("3EF30C84573FDFF");
        assertEquals("ELT - ELT-DT Location Protocol", m.get("protocolType").value());
        assertEquals("503", m.get("countryCode").value());
        assertEquals("Type approval certificate and serial number", m.get("eltIdentityType").value());
        assertEquals("100", m.get("cSTypeApprovalNumber").value());
        assertEquals("2222", m.get("beaconSerialNumber").value());
        assertNull(m.get("coarsePositionLatitude"));
        assertNull(m.get("coarsePositionLongitude"));
    }

    @Test
    public void testDecodeEltDtLocationWithForeignRegistration() {
        final Map<String, HexAttribute> m = decodeToMap("1D1220F03BBFDFF");
        assertEquals("ELT - ELT-DT Location Protocol", m.get("protocolType").value());
        assertEquals("232", m.get("countryCode").value());
        assertEquals("Aircraft 24 bit address", m.get("eltIdentityType").value());
        assertEquals("41E077", m.get("aircraft24BitAddressHex").value());
        assertEquals("20360167", m.get("aircraft24BitAddressOctal").value());
        assertNull(m.get("coarsePositionLatitude"));
        assertNull(m.get("coarsePositionLongitude"));
    }

    @Test
    public void testDecodeEltDtLocationNonDefaultLocation() {
        final Map<String, HexAttribute> m = decodeToMap("3EF23E3C5451D2A");
        assertEquals("ELT - ELT-DT Location Protocol", m.get("protocolType").value());
        assertEquals("503", m.get("countryCode").value());
        assertEquals("Aircraft 24 bit address", m.get("eltIdentityType").value());
        assertEquals("7C78A8", m.get("aircraft24BitAddressHex").value());
        assertEquals("37074250", m.get("aircraft24BitAddressOctal").value());
        assertEquals("VH-X4A", m.get("aircraftCallsign").value());
        assertEquals("-35.5", m.get("coarsePositionLatitude").value());
        assertEquals("149.0", m.get("coarsePositionLongitude").value());
    }

    @Test
    public void testDecode() {
        String hex = "D6E67C5F61F89568772240FFFFFFFF";
        Decoder.decodeFullAsJson(hex);
    }

    @Test(expected = RuntimeException.class)
    public void testDecodeWithBch1Error() {
        String hex = "A74334D34D1C9A92216E75FFFFFFFF";
        Decoder.decodeFullAsJson(hex);
    }

    @Test(expected = RuntimeException.class)
    public void testDecodeWithBch2Error() {
        String hex = "D52FE0000000000FB7EC00FFFFFFFF";
        Decoder.decodeFullAsJson(hex);
    }

    @Test(expected = RuntimeException.class)
    public void testDecodeOfUserSerialSpare2() {
        String hex = "64D7C638B761FA2B92137DFFFFFFFF";
        Decoder.decodeFullAsJson(hex);
    }

    @Test(expected = RuntimeException.class)
    public void testDecodeOfUserSerialSpare2CospasSarsatAppCertFlagPresent() {
        String hex = "4157E0002CDAC2C8A08844FFFFFFFF";
        Decoder.decodeFullAsJson(hex);
    }

    @Test(expected = RuntimeException.class)
    public void testDecodeOfUserSerialSpare2IsUS() {
        String hex = "56E7FF0FC620FF082B9650FFFFFFFF";
        Decoder.decodeFullAsJson(hex);
    }

    @Test(expected = RuntimeException.class)
    public void testDecodeOfUserSerialSpare2LongMessage() {
        String hex = "F207D401EB1F4E80362765FFFFFFFF";
        Decoder.decodeFullAsJson(hex);
    }

    private static Map<String, HexAttribute> decodeToMap(String hexStr) {
        Map<String, HexAttribute> map = new HashMap<String, HexAttribute>();
        for (HexAttribute h : HexDecoder.getHexAttributesDecodeFull(hexStr)) {
            map.put(h.desc().toString(), h);
        }
        return map;
    }

}
