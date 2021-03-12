package com.amsa.fgb.internal;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.amsa.fgb.Decoder;
import com.github.davidmoten.junit.Asserts;

public final class HexDecoderTest {

    @Test
    public void isUtilityClass() {
        Asserts.assertIsUtilityClass(HexDecoder.class);
    }

    @Test
    public void testDecodeWithRLSHasDefaultPositionOnly() {
        String hex = "3EFA8C60A6BFDFF";
        Map<String, HexAttribute> map = HexDecoder.decodeToMap(hex);
        assertEquals("3EFA8C60A6BFDFF", map.get("Hex Id").value);
        assertEquals("503", map.get("Country Code").value);
        assertEquals("Return Link Service", map.get("Protocol Type").value);
        assertEquals("EPIRB", map.get("Beacon Type").value);
        assertEquals("1099", map.get("RLS TAC Number").value);
        assertEquals("333", map.get("RLS ID").value);
    }

    @Test
    public void testDecodeWithRLSHasPosition() {
        String hex = "3EFA8035B3D0540";
        Map<String, HexAttribute> map = HexDecoder.decodeToMap(hex);
        assertEquals("3EFA8035B3BFDFF", map.get("Hex Id").value);
        assertEquals("503", map.get("Country Code").value);
        assertEquals("Return Link Service", map.get("Protocol Type").value);
        assertEquals("EPIRB", map.get("Beacon Type").value);
        assertEquals("1001", map.get("RLS TAC Number").value);
        assertEquals("11111", map.get("RLS ID").value);
        assertEquals("-32.5", map.get("Coarse Position Latitude").value);
        assertEquals("160.0", map.get("Coarse Position Longitude").value);
    }

    @Test
    public void testDecodeFullHexString() {
        String hex = "9F7D4630535FEFF91E066861F0F731";
        Map<String, HexAttribute> map = HexDecoder.decodeToMap(hex);
        assertEquals("3EFA8C60A6BFDFF", map.get("Hex Id").value);
        assertEquals("503", map.get("Country Code").value);
        assertEquals("Return Link Service", map.get("Protocol Type").value);
        assertEquals("333", map.get("RLS ID").value);
        assertEquals("1099", map.get("RLS TAC Number").value);
        assertEquals("INTERNAL", map.get("Encoded Position Source").value);
        assertEquals("NO", map.get("121.5 MHz Homing").value);
        assertEquals("YES", map.get("RLM Capability Type-1 (Auto)").value);
        assertEquals("NO", map.get("RLM Capability Type-2 (Manual)").value);
        assertEquals("NO", map.get("RLM Received Type-1 (Auto)").value);
        assertEquals("NO", map.get("RLM Received Type-2 (Manual)").value);
        assertEquals("Galileo", map.get("RLS Provider ID").value);
        assertEquals("011100110001", map.get("Error Correcting Code 2").value);
    }

    @Test
    public void testRlsPlb() {
        String hex = "3EFB0C80D3BFDFF";
        Map<String, HexAttribute> map = HexDecoder.decodeToMap(hex);
        assertEquals("Return Link Service Location", map.get("Message Type").value);
        assertEquals("3EFB0C80D3BFDFF", map.get("Hex Id").value);
        assertEquals("503", map.get("Country Code").value);
        assertEquals("Return Link Service", map.get("Protocol Type").value);
        assertEquals("PLB", map.get("Beacon Type").value);
        assertEquals("423", map.get("RLS ID").value);
        assertEquals("3100", map.get("RLS TAC Number").value);
    }

    @Test
    public void decodeRlsEpirbFinePosition() {
        // TODO get confirmed values (McMurdo encoder and decoder broken for RLS as of
        // 17 April 2019. John Ophel to raise issue with them)
        String hex = "9F7D401037AAEACE32F4E863C1E8C0";
        Map<String, HexAttribute> map = HexDecoder.decodeToMap(hex);
        assertEquals("3EFA80206F3FDFF", map.get("Hex Id").value);
        assertEquals("503", map.get("Country Code").value);
        assertEquals("Return Link Service", map.get("Protocol Type").value);
        assertEquals("222", map.get("RLS ID").value);
        assertEquals("1001", map.get("RLS TAC Number").value);
        assertEquals("INTERNAL", map.get("Encoded Position Source").value);
        assertEquals("NO", map.get("121.5 MHz Homing").value);
        assertEquals("YES", map.get("RLM Capability Type-1 (Auto)").value);
        assertEquals("NO", map.get("RLM Capability Type-2 (Manual)").value);
        assertEquals("NO", map.get("RLM Received Type-1 (Auto)").value);
        assertEquals("NO", map.get("RLM Received Type-2 (Manual)").value);
        assertEquals("Galileo", map.get("RLS Provider ID").value);
        assertEquals(-43.507778, Double.parseDouble(map.get("Latitude").value), 0.0001);
        assertEquals(172.492222, Double.parseDouble(map.get("Longitude").value), 0.0001);
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

    //    @Test
    public void testDecodeAll() throws FileNotFoundException, IOException {
        try {
            Debug.startSearching();
            DecodeHistoricalFilesMain.decode();
            Debug.writeFoundToComplianceKit();
        } finally {
            Debug.stopSearching();
        }
    }

}
