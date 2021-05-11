package au.gov.amsa.fgb.internal;

import static au.gov.amsa.fgb.TestingUtil.assertJsonEquals;
import static org.junit.Assert.assertEquals;

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

import au.gov.amsa.fgb.TestingUtil;
import au.gov.amsa.fgb.internal.Decoder;

/**
 * Tests the decoding methods of {@link Decoder}.
 *
 * @author smw01
 *
 */
public class DecoderTest {

    // private static final String HEXSTRING_15_CHRS = "ADCC40504000185";
    // private static final String HEXSTRING_15_CHRS_AVIATION = "BEE64BE562F9BD9";
    private static final String HEXSTRING_30_CHRS = "D6E6202820000C29FF51041775302D";
    private static final boolean COMPARE_WITH_LEGACY = true;

    @Test
    public void testDecodeToJsonWith30Chr() {
        assertEquals(load("/detection.json"), Decoder.decodeFullAsJson(HEXSTRING_30_CHRS));
    }
    
    private static String load(String resource) {
        try (InputStream in = DecoderTest.class.getResourceAsStream(resource)) {
            return new String(read(in), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
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
        Decoder.main(new String[] {HEXSTRING_30_CHRS});
        System.setOut(previous);
        assertJsonEquals(load("/detection.json").trim(),
                new String(bytes.toByteArray(), StandardCharsets.UTF_8).trim());
    }

    @Test(expected = RuntimeException.class)
    public void testDecoderWrongLength() {
        Decoder.decodeFullAsJson("abc");
    }

    @Test
    public void testComplianceKit() throws IOException {
        // createComplianceKitTests();
        File[] files = new File("src/test/resources/compliance-kit").listFiles();
        // ensure deterministic
        if (files != null) {
            Arrays.sort(files, (a, b) -> a.getName().compareTo(b.getName()));
            for (File file : files) {
                if (file.getName().endsWith(".json")) {
                    String hex = file.getName().substring(0, file.getName().indexOf("."));
                    String expected = readString(file);
                    String json = Decoder.decodeFullAsJson(hex);
                    System.out.println(file);
                    assertJsonEquals(expected, json);
                    if (COMPARE_WITH_LEGACY) {
                        File f = new File("src/test/resources/legacy-output", file.getName());
                        if (f.exists()) {
                            String legacy = readString(f);
                            System.out.println("================================");
                            System.out.println(json);
                            System.out.println("--------------------------------");
                            System.out.println(legacy);
                            System.out.println();
                        }
                    }

                    // this commented out section is to produce comparison from legacy aussar-oracle-java library
//                    String legacy = Hexdecode.decode(hex, "XML2")//
//                            .replaceAll("<Position>.*</Position>", "") //
//                            .replace("<Name>", "\"") //
//                            .replace("</Name>", "\"") //
//                            .replace("<Value>", ":\"") //
//                            .replace("<Line>", "") //
//                            .replace("</Line>", "") //
//                            .replace("</Value>", "\",\n") //
//                            .replace("\n\n", "").trim();
//                    legacy = "{" + legacy.substring(0, legacy.length()-1) + "}";
//                    File f = new File("src/test/resources/legacy-output", file.getName());
//                    f.delete();
//                    Files.write(f.toPath(), legacy.getBytes(StandardCharsets.UTF_8),
//                            StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                }
            }
        }
    }

    private static String readString(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()),
                StandardCharsets.UTF_8);
    }

    @SuppressWarnings("unused")
    private static void createComplianceKitTests() throws IOException {
        Stream<String> hexes = Arrays
                .stream(new File("src/test/resources/compliance-kit").listFiles())
                .map(x -> x.getName().substring(0, x.getName().indexOf("."))).sorted().distinct();
        File kit = new File("src/test/resources/compliance-kit");
        if (kit.exists()) {
            TestingUtil.delete(kit);
        }
        File tempKit = new File("target/compliance-kit");
        if (tempKit.exists()) {
            TestingUtil.delete(tempKit);
        }
        hexes
                // ensure deterministic
                .sorted() //
                .forEach(x -> {
                    try {
                        {
                            final String json = Decoder.decodeFullAsJson(x);
                            File f = new File(tempKit, x + ".json");
                            f.getParentFile().mkdirs();
                            f.delete();
                            Files.write(f.toPath(), json.getBytes(StandardCharsets.UTF_8),
                                    StandardOpenOption.WRITE, StandardOpenOption.CREATE);
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
