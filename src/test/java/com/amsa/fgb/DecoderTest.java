package com.amsa.fgb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.Test;

import com.amsa.fgb.internal.Util;

/**
 * Tests the decoding methods of {@link Decoder}
 * 
 * @author smw01
 * 
 */
public class DecoderTest {

    private static final String DECODE_30_CHRS_HTML_STRING = "<html>" + "<head>"
            + "<title>HEXDECODE of 406 Beacon</title>" + "</head>" + "<body>" + "<table>"
            + "<TR ALIGN=LEFT><TH>Item</TH><TH>Bits</TH><TH>Value</TH></TR>"
            + "<TR><TD>Message Type</TD><TD>b25-26</TD><TD>User Location (Long)</TD></TR>"
            + "<TR><TD>Hex Data</TD><TD>b25-144</TD><TD>D6E6202820000C29FF51041775302D</TD></TR>"
            + "<TR><TD>Hex Id</TD><TD>b26-85</TD><TD>ADCC40504000185</TD></TR>"
            + "<TR><TD>Country Code</TD><TD>b27-36</TD><TD>366</TD></TR>"
            + "<TR><TD>User Protocol Type</TD><TD>b37-39</TD><TD>Serial</TD></TR>"
            + "<TR><TD>Beacon Type</TD><TD>b40-42</TD><TD>Aviation</TD></TR>"
            + "<TR><TD>C/S cert. no. present</TD><TD>b43</TD><TD>YES</TD></TR>"
            + "<TR><TD>Serial Number</TD><TD>b44-63</TD><TD>5136</TD></TR>"
            + "<TR><TD>National Use</TD><TD>b64-73</TD><TD>0000000000</TD></TR>"
            + "<TR><TD>C/S Type approval number</TD><TD>b74-83</TD><TD>97</TD></TR>"
            + "<TR><TD>Aux. radio-locating</TD><TD>b84-85</TD><TD>121.5 MHz</TD></TR>"
            + "<TR><TD>US Manufacturer ID</TD><TD>b44-51</TD><TD>1</TD></TR>"
            + "<TR><TD>US Sequence No.</TD><TD>b52-63</TD><TD>1040</TD></TR>"
            + "<TR><TD>US Model ID.</TD><TD>b64-67</TD><TD>0</TD></TR>"
            + "<TR><TD>US Run No.</TD><TD>b68-75</TD><TD>0</TD></TR>"
            + "<TR><TD>US National Use</TD><TD>b76-83</TD><TD>01100001</TD></TR>"
            + "<TR><TD>Error Correcting Code</TD><TD>b86-106</TD><TD>001111111110101000100</TD></TR>"
            + "<TR><TD>Encoded Position Source</TD><TD>b107</TD><TD>EXTERNAL</TD></TR>"
            + "<TR><TD>Latitude</TD><TD>b108-119</TD><TD>32 44 00N</TD></TR>"
            + "<TR><TD>Longitude</TD><TD>b120-132</TD><TD>117 12 00W</TD></TR>"
            + "<TR><TD>Error Correcting Code</TD><TD>b133-144</TD><TD>000000101101</TD></TR>" + "</table>" + "<table>"
            + "</table>" + "</body>" + "</html>";
    private static final String HEXSTRING_LENGTH_MSG = "HEX STRING MUST BE 15 OR 30 CHARACTERS IN LENGTH";
    private static final String DECODE_30_CHRS_XML2_STRING = "<Line>"
            + "<Name>Message Type</Name><Position>b25-26</Position><Value>User Location (Long)</Value></Line>"
            + "<Line>"
            + "<Name>Hex Data</Name><Position>b25-144</Position><Value>D6E6202820000C29FF51041775302D</Value></Line>"
            + "<Line>" + "<Name>Hex Id</Name><Position>b26-85</Position><Value>ADCC40504000185</Value></Line>"
            + "<Line>" + "<Name>Country Code</Name><Position>b27-36</Position><Value>366</Value></Line>" + "<Line>"
            + "<Name>User Protocol Type</Name><Position>b37-39</Position><Value>Serial</Value></Line>" + "<Line>"
            + "<Name>Beacon Type</Name><Position>b40-42</Position><Value>Aviation</Value></Line>" + "<Line>"
            + "<Name>C/S cert. no. present</Name><Position>b43</Position><Value>YES</Value></Line>" + "<Line>"
            + "<Name>Serial Number</Name><Position>b44-63</Position><Value>5136</Value></Line>" + "<Line>"
            + "<Name>National Use</Name><Position>b64-73</Position><Value>0000000000</Value></Line>" + "<Line>"
            + "<Name>C/S Type approval number</Name><Position>b74-83</Position><Value>97</Value></Line>" + "<Line>"
            + "<Name>Aux. radio-locating</Name><Position>b84-85</Position><Value>121.5 MHz</Value></Line>" + "<Line>"
            + "<Name>US Manufacturer ID</Name><Position>b44-51</Position><Value>1</Value></Line>" + "<Line>"
            + "<Name>US Sequence No.</Name><Position>b52-63</Position><Value>1040</Value></Line>" + "<Line>"
            + "<Name>US Model ID.</Name><Position>b64-67</Position><Value>0</Value></Line>" + "<Line>"
            + "<Name>US Run No.</Name><Position>b68-75</Position><Value>0</Value></Line>" + "<Line>"
            + "<Name>US National Use</Name><Position>b76-83</Position><Value>01100001</Value></Line>" + "<Line>"
            + "<Name>Error Correcting Code</Name><Position>b86-106</Position><Value>001111111110101000100</Value></Line>"
            + "<Line>" + "<Name>Encoded Position Source</Name><Position>b107</Position><Value>EXTERNAL</Value></Line>"
            + "<Line>" + "<Name>Latitude</Name><Position>b108-119</Position><Value>32 44 00N</Value></Line>" + "<Line>"
            + "<Name>Longitude</Name><Position>b120-132</Position><Value>117 12 00W</Value></Line>" + "<Line>"
            + "<Name>Error Correcting Code</Name><Position>b133-144</Position><Value>000000101101</Value></Line>";
    private static final String DECODE_30_CHRS_XML_STRING = "<?xml version=\"1.0\"?>" + "<Hexdecode>"
            + "<MessageType><Position>b25-26</Position><Value>User Location (Long)</Value></MessageType>"
            + "<HexData><Position>b25-144</Position><Value>D6E6202820000C29FF51041775302D</Value></HexData>"
            + "<HexId><Position>b26-85</Position><Value>ADCC40504000185</Value></HexId>"
            + "<CountryCode><Position>b27-36</Position><Value>366</Value></CountryCode>"
            + "<UserProtocolType><Position>b37-39</Position><Value>Serial</Value></UserProtocolType>"
            + "<BeaconType><Position>b40-42</Position><Value>Aviation</Value></BeaconType>"
            + "<CScertnopresent><Position>b43</Position><Value>YES</Value></CScertnopresent>"
            + "<SerialNumber><Position>b44-63</Position><Value>5136</Value></SerialNumber>"
            + "<NationalUse><Position>b64-73</Position><Value>0000000000</Value></NationalUse>"
            + "<CSTypeapprovalnumber><Position>b74-83</Position><Value>97</Value></CSTypeapprovalnumber>"
            + "<Auxradio-locating><Position>b84-85</Position><Value>121.5 MHz</Value></Auxradio-locating>"
            + "<USManufacturerID><Position>b44-51</Position><Value>1</Value></USManufacturerID>"
            + "<USSequenceNo><Position>b52-63</Position><Value>1040</Value></USSequenceNo>"
            + "<USModelID><Position>b64-67</Position><Value>0</Value></USModelID>"
            + "<USRunNo><Position>b68-75</Position><Value>0</Value></USRunNo>"
            + "<USNationalUse><Position>b76-83</Position><Value>01100001</Value></USNationalUse>"
            + "<ErrorCorrectingCode><Position>b86-106</Position><Value>001111111110101000100</Value></ErrorCorrectingCode>"
            + "<EncodedPositionSource><Position>b107</Position><Value>EXTERNAL</Value></EncodedPositionSource>"
            + "<Latitude><Position>b108-119</Position><Value>32 44 00N</Value></Latitude>"
            + "<Longitude><Position>b120-132</Position><Value>117 12 00W</Value></Longitude>"
            + "<ErrorCorrectingCode><Position>b133-144</Position><Value>000000101101</Value></ErrorCorrectingCode>"
            + "</Hexdecode>";
    private static final String DECODE_30_CHRS_TEXT_STRING = //
            "Message Type                  b25-26       User Location (Long)" //
                    + "Hex Data                      b25-144      D6E6202820000C29FF51041775302D" //
                    + "Hex Id                        b26-85       ADCC40504000185" //
                    + "Country Code                  b27-36       366" //
                    + "User Protocol Type            b37-39       Serial" //
                    + "Beacon Type                   b40-42       Aviation"
                    + "C/S cert. no. present         b43          YES" //
                    + "Serial Number                 b44-63       5136" //
                    + "National Use                  b64-73       0000000000"
                    + "C/S Type approval number      b74-83       97" //
                    + "Aux. radio-locating           b84-85       121.5 MHz" //
                    + "US Manufacturer ID            b44-51       1" //
                    + "US Sequence No.               b52-63       1040" //
                    + "US Model ID.                  b64-67       0" //
                    + "US Run No.                    b68-75       0" //
                    + "US National Use               b76-83       01100001" //
                    + "Error Correcting Code         b86-106      001111111110101000100" //
                    + "Encoded Position Source       b107         EXTERNAL" //
                    + "Latitude                      b108-119     32 44 00N" //
                    + "Longitude                     b120-132     117 12 00W" //
                    + "Error Correcting Code         b133-144     000000101101";
    private static final String DECODE_15_CHRS_AVIATION_TEXT_STRING = //
            "Message Type                  b25-26       User (Format - Unknown)" //
                    + "Hex Data                      b25-144      Unknown" //
                    + "Hex Id                        b26-85       BEE64BE562F9BD9" //
                    + "Country Code                  b27-36       503" //
                    + "User Protocol Type            b37-39       Aviation" //
                    + "Aircraft Reg. Marking         b40-81        VH-VNQ" //
                    + "Specific ELT number           b82-83       2" //
                    + "Aux. radio-locating           b84-85       121.5 MHz";
    private static final String DECODE_15_CHRS_TEXT_STRING = //
            "Message Type                  b25-26       User (Format - Unknown)" //
                    + "Hex Data                      b25-144      Unknown" //
                    + "Hex Id                        b26-85       ADCC40504000185" //
                    + "Country Code                  b27-36       366" //
                    + "User Protocol Type            b37-39       Serial" //
                    + "Beacon Type                   b40-42       Aviation" //
                    + "C/S cert. no. present         b43          YES" //
                    + "Serial Number                 b44-63       5136" //
                    + "National Use                  b64-73       0000000000" //
                    + "C/S Type approval number      b74-83       97"
                    + "Aux. radio-locating           b84-85       121.5 MHz" //
                    + "US Manufacturer ID            b44-51       1" //
                    + "US Sequence No.               b52-63       1040" //
                    + "US Model ID.                  b64-67       0" //
                    + "US Run No.                    b68-75       0" //
                    + "US National Use               b76-83       01100001";
    private static final String DECODE_15_CHRS_XML2_STRING = "<Line>"
            + "<Name>Message Type</Name><Position>b25-26</Position><Value>User (Format - Unknown)</Value></Line>"
            + "<Line>" + "<Name>Hex Data</Name><Position>b25-144</Position><Value>Unknown</Value></Line>" + "<Line>"
            + "<Name>Hex Id</Name><Position>b26-85</Position><Value>ADCC40504000185</Value></Line>" + "<Line>"
            + "<Name>Country Code</Name><Position>b27-36</Position><Value>366</Value></Line>" + "<Line>"
            + "<Name>User Protocol Type</Name><Position>b37-39</Position><Value>Serial</Value></Line>" + "<Line>"
            + "<Name>Beacon Type</Name><Position>b40-42</Position><Value>Aviation</Value></Line>" + "<Line>"
            + "<Name>C/S cert. no. present</Name><Position>b43</Position><Value>YES</Value></Line>" + "<Line>"
            + "<Name>Serial Number</Name><Position>b44-63</Position><Value>5136</Value></Line>" + "<Line>"
            + "<Name>National Use</Name><Position>b64-73</Position><Value>0000000000</Value></Line>" + "<Line>"
            + "<Name>C/S Type approval number</Name><Position>b74-83</Position><Value>97</Value></Line>" + "<Line>"
            + "<Name>Aux. radio-locating</Name><Position>b84-85</Position><Value>121.5 MHz</Value></Line>" + "<Line>"
            + "<Name>US Manufacturer ID</Name><Position>b44-51</Position><Value>1</Value></Line>" + "<Line>"
            + "<Name>US Sequence No.</Name><Position>b52-63</Position><Value>1040</Value></Line>" + "<Line>"
            + "<Name>US Model ID.</Name><Position>b64-67</Position><Value>0</Value></Line>" + "<Line>"
            + "<Name>US Run No.</Name><Position>b68-75</Position><Value>0</Value></Line>" + "<Line>"
            + "<Name>US National Use</Name><Position>b76-83</Position><Value>01100001</Value></Line>";
    private static final String DECODE_15_CHRS_XML_STRING = "<?xml version=\"1.0\"?>" + "<Hexdecode>"
            + "<MessageType><Position>b25-26</Position><Value>User (Format - Unknown)</Value></MessageType>"
            + "<HexData><Position>b25-144</Position><Value>Unknown</Value></HexData>"
            + "<HexId><Position>b26-85</Position><Value>ADCC40504000185</Value></HexId>"
            + "<CountryCode><Position>b27-36</Position><Value>366</Value></CountryCode>"
            + "<UserProtocolType><Position>b37-39</Position><Value>Serial</Value></UserProtocolType>"
            + "<BeaconType><Position>b40-42</Position><Value>Aviation</Value></BeaconType>"
            + "<CScertnopresent><Position>b43</Position><Value>YES</Value></CScertnopresent>"
            + "<SerialNumber><Position>b44-63</Position><Value>5136</Value></SerialNumber>"
            + "<NationalUse><Position>b64-73</Position><Value>0000000000</Value></NationalUse>"
            + "<CSTypeapprovalnumber><Position>b74-83</Position><Value>97</Value></CSTypeapprovalnumber>"
            + "<Auxradio-locating><Position>b84-85</Position><Value>121.5 MHz</Value></Auxradio-locating>"
            + "<USManufacturerID><Position>b44-51</Position><Value>1</Value></USManufacturerID>"
            + "<USSequenceNo><Position>b52-63</Position><Value>1040</Value></USSequenceNo>"
            + "<USModelID><Position>b64-67</Position><Value>0</Value></USModelID>"
            + "<USRunNo><Position>b68-75</Position><Value>0</Value></USRunNo>"
            + "<USNationalUse><Position>b76-83</Position><Value>01100001</Value></USNationalUse>" + "</Hexdecode>";
    private static final String HEXSTRING_15_CHRS = "ADCC40504000185";
    private static final String HEXSTRING_15_CHRS_AVIATION = "BEE64BE562F9BD9";
    private static final String HEXSTRING_30_CHRS = "D6E6202820000C29FF51041775302D";
    private static final String DECODE_15_CHRS_HTML_STRING = "<html>" + "<head>"
            + "<title>HEXDECODE of 406 Beacon</title>" + "</head>" + "<body>" + "<table>"
            + "<TR ALIGN=LEFT><TH>Item</TH><TH>Bits</TH><TH>Value</TH></TR>"
            + "<TR><TD>Message Type</TD><TD>b25-26</TD><TD>User (Format - Unknown)</TD></TR>"
            + "<TR><TD>Hex Data</TD><TD>b25-144</TD><TD>Unknown</TD></TR>"
            + "<TR><TD>Hex Id</TD><TD>b26-85</TD><TD>ADCC40504000185</TD></TR>"
            + "<TR><TD>Country Code</TD><TD>b27-36</TD><TD>366</TD></TR>"
            + "<TR><TD>User Protocol Type</TD><TD>b37-39</TD><TD>Serial</TD></TR>"
            + "<TR><TD>Beacon Type</TD><TD>b40-42</TD><TD>Aviation</TD></TR>"
            + "<TR><TD>C/S cert. no. present</TD><TD>b43</TD><TD>YES</TD></TR>"
            + "<TR><TD>Serial Number</TD><TD>b44-63</TD><TD>5136</TD></TR>"
            + "<TR><TD>National Use</TD><TD>b64-73</TD><TD>0000000000</TD></TR>"
            + "<TR><TD>C/S Type approval number</TD><TD>b74-83</TD><TD>97</TD></TR>"
            + "<TR><TD>Aux. radio-locating</TD><TD>b84-85</TD><TD>121.5 MHz</TD></TR>"
            + "<TR><TD>US Manufacturer ID</TD><TD>b44-51</TD><TD>1</TD></TR>"
            + "<TR><TD>US Sequence No.</TD><TD>b52-63</TD><TD>1040</TD></TR>"
            + "<TR><TD>US Model ID.</TD><TD>b64-67</TD><TD>0</TD></TR>"
            + "<TR><TD>US Run No.</TD><TD>b68-75</TD><TD>0</TD></TR>"
            + "<TR><TD>US National Use</TD><TD>b76-83</TD><TD>01100001</TD></TR>" + "</table>" + "<table>" + "</table>"
            + "</body>" + "</html>";
    private static final String DECODESEARCH_HTML_STRING = "<html>" + "<head>"
            + "<title>HEXDECODE of 406 Beacon</title>" + "</head>" + "<body>" + "<table>"
            + "<TR ALIGN=LEFT><TH>Item</TH><TH>Bits</TH><TH>Value</TH></TR>"
            + "<TR><TD>Hex Id</TD><TD>b26-85</TD><TD>ADCC40504000185</TD></TR>"
            + "<TR><TD>Serial Number</TD><TD>b44-63</TD><TD>5136</TD></TR>" + "</table>" + "<table>" + "</table>"
            + "</body>" + "</html>";
    private static final String DECODESEARCH_XML2_STRING = "<Line>"
            + "<Name>Hex Id</Name><Position>b26-85</Position><Value>ADCC40504000185</Value></Line>" + "<Line>"
            + "<Name>Serial Number</Name><Position>b44-63</Position><Value>5136</Value></Line>";
    private static final String DECODESEARCH_TEXT_STRING = "Hex Id                        b26-85       ADCC40504000185"
            + "Serial Number                 b44-63       5136";
    private static final String DECODESEARCH_XML_STRING = "<?xml version=\"1.0\"?>" + "<Hexdecode>"
            + "<HexId><Position>b26-85</Position><Value>ADCC40504000185</Value></HexId>"
            + "<SerialNumber><Position>b44-63</Position><Value>5136</Value></SerialNumber>" + "</Hexdecode>";

    /**
     * Tests <code>decodeSearch</code> for a given 15 character hex string.
     */
    @Test
    public void testDecodeSearchWith15Chr() {
        assertEquals(DECODESEARCH_HTML_STRING,
                Decoder.decodePartial(HEXSTRING_15_CHRS, Formatter.HTML).replace("\n", ""));

        assertEquals(DECODESEARCH_XML_STRING,
                Decoder.decodePartial(HEXSTRING_15_CHRS, Formatter.XML).replace("\n", ""));

        assertEquals(DECODESEARCH_XML2_STRING,
                Decoder.decodePartial(HEXSTRING_15_CHRS, Formatter.XML2).replace("\n", ""));

        assertEquals(DECODESEARCH_TEXT_STRING,
                Decoder.decodePartial(HEXSTRING_15_CHRS, Formatter.TEXT).replace("\n", ""));
    }

    /**
     * Tests decodeSearch for a given 30 character hex string.
     */
    @Test
    public void testDecodeSearchWith30Chr() {
        assertEquals(DECODESEARCH_HTML_STRING,
                Decoder.decodePartial(HEXSTRING_30_CHRS, Formatter.HTML).replace("\n", ""));

        assertEquals(DECODESEARCH_XML_STRING,
                Decoder.decodePartial(HEXSTRING_30_CHRS, Formatter.XML).replace("\n", ""));

        assertEquals(DECODESEARCH_XML2_STRING,
                Decoder.decodePartial(HEXSTRING_30_CHRS, Formatter.XML2).replace("\n", ""));

        assertEquals(DECODESEARCH_TEXT_STRING,
                Decoder.decodePartial(HEXSTRING_30_CHRS, Formatter.TEXT).replace("\n", ""));

    }

    /**
     * Tests <code>decode</code> for a given 15 character hex string.
     */
    @Test
    public void testDecodeWith15Chr() {
        assertEquals(DECODE_15_CHRS_HTML_STRING,
                Decoder.decodeFull(HEXSTRING_15_CHRS, Formatter.HTML).replace("\n", ""));

        assertEquals(DECODE_15_CHRS_XML_STRING, Decoder.decodeFull(HEXSTRING_15_CHRS, Formatter.XML).replace("\n", ""));

        assertEquals(DECODE_15_CHRS_XML2_STRING,
                Decoder.decodeFull(HEXSTRING_15_CHRS, Formatter.XML2).replace("\n", ""));

        assertEquals(DECODE_15_CHRS_TEXT_STRING,
                Decoder.decodeFull(HEXSTRING_15_CHRS, Formatter.TEXT).replace("\n", ""));

    }

    @Test
    public void testDecodeWith15ChrAviation() {
        assertEquals(DECODE_15_CHRS_AVIATION_TEXT_STRING,
                Decoder.decodeFull(HEXSTRING_15_CHRS_AVIATION, Formatter.TEXT).replace("\n", ""));
    }

    /**
     * Tests <code>decode</code> for a given 30 character hex string.
     */
    @Test
    public void testDecodeWith30Chr() {

        assertEquals(DECODE_30_CHRS_XML_STRING, Decoder.decodeFull(HEXSTRING_30_CHRS, Formatter.XML).replace("\n", ""));

        assertEquals(DECODE_30_CHRS_XML2_STRING,
                Decoder.decodeFull(HEXSTRING_30_CHRS, Formatter.XML2).replace("\n", ""));

        assertEquals(DECODE_30_CHRS_TEXT_STRING,
                Decoder.decodeFull(HEXSTRING_30_CHRS, Formatter.TEXT).replace("\n", ""));

        assertEquals(DECODE_30_CHRS_HTML_STRING,
                Decoder.decodeFull(HEXSTRING_30_CHRS, Formatter.HTML).replace("\n", ""));
    }

    @Test
    public void testDecodeToJsonWith30Chr() {
        assertEquals(load("/detection.json"), Decoder.decodeFull(HEXSTRING_30_CHRS, Formatter.JSON));
    }

    private static final String load(String resource) {
        try (InputStream in = DecoderTest.class.getResourceAsStream(resource)) {
            return new String(read(in), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static final byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * Tests for length of hex string which must be 15 or 30 characters.
     */
    public void testHexStringLength() {
        assertTrue(Decoder.decodePartial("abc", Formatter.HTML).contains(HEXSTRING_LENGTH_MSG));
        assertTrue(Decoder.decodePartial("abc", Formatter.XML).contains(HEXSTRING_LENGTH_MSG));
        assertTrue(Decoder.decodePartial("abc", Formatter.XML2).contains(HEXSTRING_LENGTH_MSG));
        assertTrue(Decoder.decodePartial("abc", Formatter.TEXT).contains(HEXSTRING_LENGTH_MSG));
        assertTrue(Decoder.decodeFull("abc", Formatter.HTML).contains(HEXSTRING_LENGTH_MSG));
        assertTrue(Decoder.decodeFull("abc", Formatter.XML).contains(HEXSTRING_LENGTH_MSG));
        assertTrue(Decoder.decodeFull("abc", Formatter.XML2).contains(HEXSTRING_LENGTH_MSG));
        assertTrue(Decoder.decodeFull("abc", Formatter.TEXT).contains(HEXSTRING_LENGTH_MSG));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecoderMainWrongNumberOfArguments() {
        Decoder.main(new String[] {});
    }

    @Test
    public void testDecoderMain() {
        // TODO capture output and test result
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes);
        PrintStream previous = System.out;
        System.setOut(out);
        Decoder.main(new String[] { HEXSTRING_30_CHRS, "JSON" });
        System.setOut(previous);
        assertEquals(load("/detection.json").trim(), new String(bytes.toByteArray(), StandardCharsets.UTF_8).trim());
    }

    @Test(expected = RuntimeException.class)
    public void testDecoderWrongLength() {
        Decoder.decodeFull("abc", Formatter.JSON);
    }

    @Test
    public void testComplianceKit() throws IOException {
        createComplianceKitTests();
        File[] files = new File("src/test/resources/compliance-kit").listFiles();
        // ensure deterministic
        Arrays.sort(files, (a, b) -> a.getName().compareTo(b.getName()));
        for (File file : files) {
            if (file.getName().endsWith(".json")) {
                String hex = file.getName().substring(0, file.getName().indexOf("."));
                String expected = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                String json;
                if (file.getName().contains("partial")) {
                    json = Decoder.decodePartial(hex, Formatter.JSON);
                } else {
                    json = Decoder.decodeFull(hex, Formatter.JSON);
                }
                // TODO use Jackson for JSON equals
                assertEquals(expected, json);
            }
        }
    }

    @SuppressWarnings("unused")
    private static void createComplianceKitTests() throws IOException {
        Stream<String> hexes = Arrays.stream(new File("src/test/resources/compliance-kit").listFiles())
                .map(x -> x.getName().substring(0, x.getName().indexOf("."))).sorted().distinct();
        File kit = new File("src/test/resources/compliance-kit");
        if (kit.exists()) {
            Util.delete(kit);
        }
        File tempKit = new File("target/compliance-kit");
        if (tempKit.exists()) {
            Util.delete(tempKit);
        }
        hexes
                // ensure deterministic
                .sorted() //
                .forEach(x -> {
                    try {
                        {
                            final String json = Decoder.decodeFull(x, Formatter.JSON);
                            File f = new File(tempKit, x + ".json");
                            f.getParentFile().mkdirs();
                            f.delete();
                            Files.write(f.toPath(), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE,
                                    StandardOpenOption.CREATE);
                        }
                        {
                            final String json = Decoder.decodePartial(x, Formatter.JSON);
                            File f = new File(tempKit, x + ".partial.json");
                            f.getParentFile().mkdirs();
                            f.delete();
                            Files.write(f.toPath(), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE,
                                    StandardOpenOption.CREATE);
                        }
                    } catch (RuntimeException e) {
                        System.out.println("Errored: " + x + "\n" + e.getMessage());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
        tempKit.renameTo(kit);
    }

}
