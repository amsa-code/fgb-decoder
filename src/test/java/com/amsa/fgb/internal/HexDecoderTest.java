package com.amsa.fgb.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Test;

import com.amsa.fgb.Decoder;
import com.amsa.fgb.Formatter;

public class HexDecoderTest {
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
        assertEquals("DEFAULT", map.get("Coarse Position").value);
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
        assertEquals("32 30S 160 00E", map.get("Coarse Position").value);
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
        assertEquals("DEFAULT", map.get("Fine Position").value);
        assertEquals("011100110001", map.get("Error Correcting Code").value);
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
        assertEquals("-43.508 172.492", map.get("Lat Lon").value);
        assertEquals("43 30 28S", map.get("Latitude").value);
        assertEquals("172 29 32E", map.get("Longitude").value);
    }

    @Test
    public void testComplianceKit() throws IOException {
//        createComplianceKitTests();
        File[] files = new File("src/test/resources/compliance-kit").listFiles();
        // ensure deterministic
        Arrays.sort(files, (a, b) -> a.getName().compareTo(b.getName()));
        for (File file : files) {
            if (file.getName().endsWith(".json")) {
                String hex = file.getName().substring(0, file.getName().lastIndexOf("."));
                String json = Decoder.decodeFull(hex, Formatter.JSON);
                // TODO use Jackson for JSON equals
                String expected = new String(Files.readAllBytes(file.toPath()),
                        StandardCharsets.UTF_8);
                assertEquals(expected, json);
            }
        }
    }

    @SuppressWarnings("unused")
    private static void createComplianceKitTests() throws IOException {
        Stream<String> a = Files.lines(new File("src/test/resources/hexes.txt").toPath());
        Stream<String> b = Files.lines(new File("src/test/resources/ids.txt").toPath());
        File kit = new File("src/test/resources/compliance-kit");
        if (kit.exists()) {
            delete(kit);
        }
        File tempKit = new File("target/compliance-kit");
        if (tempKit.exists()) {
            delete(tempKit);
        }
        Stream.concat(a, b) //
                // ensure deterministic
                .sorted() //
                .forEach(x -> {
                    try {
                        final String json = Decoder.decodeFull(x, Formatter.JSON);
                        File f = new File(tempKit, x + ".json");
                        f.getParentFile().mkdirs();
                        f.delete();
                        Files.write(f.toPath(), json.getBytes(StandardCharsets.UTF_8),
                                StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                    } catch (RuntimeException e) {
                        System.out.println("Errored: " + x);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
        tempKit.renameTo(kit);
    }

    private static void delete(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                delete(f);
            }
        }
        if (!file.delete()) {
            throw new RuntimeException("delete failed of " + file);
        }
    }
}
